package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.miet.complaintportal.model.ComplaintStatus;
import com.miet.complaintportal.service.ComplaintDetail;
import com.miet.complaintportal.service.ComplaintService;
import com.miet.complaintportal.service.ComplaintServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/update")
public class AgentUpdateComplaintServlet extends HttpServlet {

  private final ComplaintService complaintService = new ComplaintServiceImpl();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      long complaintId = Long.parseLong(request.getParameter("id"));
      Optional<ComplaintDetail> detail = complaintService.viewComplaintDetail(complaintId);

      if (detail.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("errorMessage", "No complaint found with that id");
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
      }

      HttpSession session = request.getSession(false);
      long sessionUserId = (long) session.getAttribute("userId");
      Long agentId = detail.get().getComplaint().getAgentId();
      String userRole = (String) session.getAttribute("userRole");
      boolean isAdmin = "ADMIN".equals(userRole);

      if (!isAdmin) {
        if (agentId == null) {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          request.setAttribute("errorMessage", "No agent assigned to complaint.");
          request.getRequestDispatcher("/error.jsp").forward(request, response);
          return;
        }

        if (sessionUserId != agentId) {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          request.setAttribute("errorMessage", "You do not have permission to view this complaint.");
          request.getRequestDispatcher("/error.jsp").forward(request, response);
          return;
        }

      }

      request.setAttribute("detail", detail.get());
      request.setAttribute("statuses", ComplaintStatus.values());
      request.getRequestDispatcher("/agent-update-complaint.jsp").forward(request, response);

    } catch (IllegalStateException | NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      request.setAttribute("errorMessage", "Invalid complaint id.");
      request.getRequestDispatcher("/error.jsp").forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Failed to load complaint", e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      long complaintId = Long.parseLong(request.getParameter("id"));
      Optional<ComplaintDetail> detail = complaintService.viewComplaintDetail(complaintId);
      HttpSession session = request.getSession(false);
      long sessionUserId = (long) session.getAttribute("userId");
      String userRole = (String) session.getAttribute("userRole");
      boolean isAdmin = "ADMIN".equals(userRole);

      if (detail.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("errorMessage", "No complaint found with that id");
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
      }

      Long agentId = detail.get().getComplaint().getAgentId();
      ComplaintStatus newStatus = ComplaintStatus.valueOf(request.getParameter("newStatus"));
      String remark = request.getParameter("remark");

      if (!isAdmin) {

        if (agentId == null) {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          request.setAttribute("errorMessage", "No agent assigned to complaint.");
          request.getRequestDispatcher("/error.jsp").forward(request, response);
          return;
        }

        if (sessionUserId != agentId) {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          request.setAttribute("errorMessage", "You do not have permission to view this complaint.");
          request.getRequestDispatcher("/error.jsp").forward(request, response);
          return;
        }
      }

      complaintService.updateComplaintStatus(complaintId, newStatus, sessionUserId, remark);
      if (isAdmin) {
        response.sendRedirect(request.getContextPath() + "/admin/assign");
      } else {
        response.sendRedirect(request.getContextPath() + "/agent/assigned");
      }
    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      request.setAttribute("errorMessage", "Invalid status update request.");
      request.getRequestDispatcher("/error.jsp").forward(request, response);
    } catch (IllegalStateException e) {
      throw new ServletException("Invalid category found", e);
    } catch (SQLException e) {
      throw new ServletException("Failed to update status", e);
    }
  }
}
