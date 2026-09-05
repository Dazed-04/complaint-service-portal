package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.service.ComplaintService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/assigned")
public class AgentViewComplaintsServlet extends HttpServlet {

  @Autowired
  private ComplaintService complaintService;

  public AgentViewComplaintsServlet() {
  }

  @Override
  public void init() throws ServletException {
    super.init();
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    long agentId = (long) session.getAttribute("userId");

    try {
      List<Complaint> complaints = complaintService.viewAssignedComplaints(agentId);
      request.setAttribute("complaints", complaints);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/view-assigned-complaints.jsp");
      dispatcher.forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Failed to load complaints", e);
    }
  }
}
