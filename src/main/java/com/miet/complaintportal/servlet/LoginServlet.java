package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;

import com.miet.complaintportal.exceptions.InvalidCredentialsException;
import com.miet.complaintportal.model.User;
import com.miet.complaintportal.service.UserService;
import com.miet.complaintportal.service.UserServiceImpl;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  private final UserService userService = new UserServiceImpl();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    try {
      User user = userService.login(email, password);
      HttpSession existingSession = request.getSession(false);
      if (existingSession != null) {
        existingSession.invalidate();
      }
      HttpSession session = request.getSession(true);
      session.setAttribute("userId", user.getId());
      session.setAttribute("userName", user.getName());
      session.setAttribute("userRole", user.getRole());
      response.sendRedirect(request.getContextPath() + "/welcome.jsp");

    } catch (InvalidCredentialsException e) {
      request.setAttribute("errorMessage", e.getMessage());
      RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
      dispatcher.forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Database error during login", e);
    }
  }
}
