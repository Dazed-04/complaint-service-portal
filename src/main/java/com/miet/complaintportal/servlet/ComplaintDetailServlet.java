package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.miet.complaintportal.service.ComplaintDetail;
import com.miet.complaintportal.service.ComplaintService;
import com.miet.complaintportal.service.ComplaintServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/complaints/detail")
public class ComplaintDetailServlet extends HttpServlet {
  private ComplaintService complaintService = new ComplaintServiceImpl();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      long complaintId = Long.parseLong(request.getParameter("id"));
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

      request.setAttribute("detail", detail.get());
      request.getRequestDispatcher("/complaint-detail.jsp").forward(request, response);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      request.setAttribute("errorMessage", "Invalid complaint id.");
      request.getRequestDispatcher("/error.jsp").forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Failed to fetch details", e);
    }
  }

}
