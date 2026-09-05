package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.miet.complaintportal.dao.UserDao;
import com.miet.complaintportal.exceptions.LastAdminException;
import com.miet.complaintportal.model.Role;
import com.miet.complaintportal.model.User;
import com.miet.complaintportal.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/manage")
public class AdminUserManagementServlet extends HttpServlet {

  @Autowired
  private UserDao userDao;
  @Autowired
  private UserService userService;

  public AdminUserManagementServlet() {
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
      List<User> users = userDao.findAll();
      request.setAttribute("users", users);
      request.setAttribute("roles", Role.values());
      request.getRequestDispatcher("/admin-manage.jsp").forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Failed to load users", e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      long userId = Long.parseLong(request.getParameter("userId"));
      Role newRole = Role.valueOf(request.getParameter("newRole"));
      userService.updateRole(userId, newRole);
      response.sendRedirect(request.getContextPath() + "/admin/manage");
    } catch (LastAdminException e) {
      try {
        request.setAttribute("errorMessage", e.getMessage());
        request.setAttribute("users", userDao.findAll());
        request.setAttribute("roles", Role.values());
        request.getRequestDispatcher("/admin-manage.jsp").forward(request, response);

      } catch (SQLException ex) {
        throw new ServletException("Failed to reload users", ex);
      }
    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      request.setAttribute("errorMessage", "Invalid user id or role");
      request.getRequestDispatcher("/error.jsp").forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Failed to update user role", e);
    }
  }
}
