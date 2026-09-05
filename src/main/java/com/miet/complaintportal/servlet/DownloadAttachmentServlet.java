package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.miet.complaintportal.model.Attachment;
import com.miet.complaintportal.service.ComplaintDetail;
import com.miet.complaintportal.service.ComplaintService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/complaints/download")
public class DownloadAttachmentServlet extends HttpServlet {

  @Autowired
  private ComplaintService complaintService;

  public DownloadAttachmentServlet() {
  }

  @Override
  public void init() throws ServletException {
    super.init();
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      long complaintId = Long.parseLong(request.getParameter("complaintId"));
      long attachmentId = Long.parseLong(request.getParameter("attachmentId"));

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
          request.setAttribute("errorMessage", "You do not have permission to view this complaint");
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

      List<Attachment> attachments = detail.get().getAttachments();
      Optional<Attachment> matched = attachments.stream().filter(c -> c.getId() == attachmentId).findFirst();
      if (matched.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        request.setAttribute("errorMessage", "You do not have permission to view this complaint.");
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
      }

      Attachment attachment = matched.get();

      response.setContentType(attachment.getContentType());
      response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getFilename() + "\"");
      response.getOutputStream().write(attachment.getFileData());

    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      request.setAttribute("errorMessage", "Invalid complaint or attachment id.");
      request.getRequestDispatcher("/error.jsp").forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Failed to load attachment", e);
    }
  }
}
