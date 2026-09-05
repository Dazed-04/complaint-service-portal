package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.miet.complaintportal.dao.UserDao;
import com.miet.complaintportal.exceptions.InvalidAgentException;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.User;
import com.miet.complaintportal.service.ComplaintService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/assign")
public class AdminAssignAgentServlet extends HttpServlet {

  @Autowired
  private ComplaintService complaintService;
  @Autowired
  private UserDao userDao;

  public AdminAssignAgentServlet() {
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
      List<Complaint> complaints = complaintService.listAllComplaints();
      List<User> agents = userDao.findAllAgents();
      request.setAttribute("complaints", complaints);
      request.setAttribute("agents", agents);
      request.getRequestDispatcher("/admin-assign.jsp").forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Failed to load complaints or agents", e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      long agentId = Long.parseLong(request.getParameter("agentId"));
      long complaintId = Long.parseLong(request.getParameter("complaintId"));
      complaintService.assignAgent(complaintId, agentId);
      response.sendRedirect(request.getContextPath() + "/admin/assign");
    } catch (InvalidAgentException e) {
      try {
        request.setAttribute("errorMessage", e.getMessage());
        request.setAttribute("complaints", complaintService.listAllComplaints());
        request.setAttribute("agents", userDao.findAllAgents());
        request.getRequestDispatcher("/admin-assign.jsp").forward(request, response);

      } catch (SQLException ex) {
        throw new ServletException("Failed to reload assignment page", ex);
      }
    } catch (NumberFormatException e) {
      throw new ServletException("Invalid agent Id");
    } catch (SQLException e) {
      throw new ServletException("Agent could not be assigned", e);
    }
  }
}
