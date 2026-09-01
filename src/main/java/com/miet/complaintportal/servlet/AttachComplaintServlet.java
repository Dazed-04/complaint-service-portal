package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.miet.complaintportal.exceptions.ComplaintNotFoundException;
import com.miet.complaintportal.exceptions.FileTooLargeException;
import com.miet.complaintportal.service.AttachmentService;
import com.miet.complaintportal.service.AttachmentServiceImpl;
import com.miet.complaintportal.service.ComplaintDetail;
import com.miet.complaintportal.service.ComplaintService;
import com.miet.complaintportal.service.ComplaintServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/complaints/attach")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class AttachComplaintServlet extends HttpServlet {

  private final AttachmentService attachmentService;
  private final ComplaintService complaintService;

  public AttachComplaintServlet() {
    this(new AttachmentServiceImpl(), new ComplaintServiceImpl());
  }

  public AttachComplaintServlet(AttachmentService attachmentService, ComplaintService complaintService) {
    this.attachmentService = attachmentService;
    this.complaintService = complaintService;
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      long complaintId = Long.parseLong(request.getParameter("complaintId"));
      Optional<ComplaintDetail> detail = complaintService.viewComplaintDetail(complaintId);
      HttpSession session = request.getSession(false);
      long sessionUserId = (long) session.getAttribute("userId");
      String userRole = (String) session.getAttribute("userRole");
      boolean isStaff = "AGENT".equals(userRole) || "ADMIN".equals(userRole);

      if (detail.isEmpty()) {
        if (isStaff) {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          request.setAttribute("errorMessage", "No complaint found with that id");
        } else {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          request.setAttribute("errorMessage", "You do not have permission to view this complaint.");
        }
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
      }

      boolean isOwner = detail.get().getComplaint().getCustomerId() == sessionUserId;
      if (!isOwner && !isStaff) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        request.setAttribute("errorMessage", "You do not have permission to view this complaint.");
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
      }

      Part filePart = request.getPart("file");
      if (filePart == null || filePart.getSize() == 0) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        request.setAttribute("errorMessage", "Please select a file to upload.");
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
      }
      String filename = filePart.getSubmittedFileName();
      String contentType = filePart.getContentType();
      byte[] fileData = filePart.getInputStream().readAllBytes();

      attachmentService.addAttachment(complaintId, filename, contentType, fileData);
      response.sendRedirect(request.getContextPath() + "/complaints/detail?id=" + complaintId);

    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      request.setAttribute("errorMessage", "Invalid complaint id.");
      request.getRequestDispatcher("/error.jsp").forward(request, response);
    } catch (FileTooLargeException e) {
      request.setAttribute("errorMessage", e.getMessage());
      request.getRequestDispatcher("/error.jsp").forward(request, response);
    } catch (ComplaintNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      request.setAttribute("errorMessage", e.getMessage());
      request.getRequestDispatcher("/error.jsp").forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Failed to save attachment", e);
    }
  }
}
