package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.miet.complaintportal.exceptions.InvalidCredentialsException;
import com.miet.complaintportal.model.User;
import com.miet.complaintportal.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  @Autowired
  private UserService userService;

  public LoginServlet(UserService userService) {
    this.userService = userService;
  }

  public LoginServlet() {
  }

  @Override
  public void init() throws ServletException {
    super.init();
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
  }

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
      String redirectPath = null;
      if (existingSession != null) {
        redirectPath = (String) existingSession.getAttribute("redirectAfterLogin");
        existingSession.invalidate();
      }
      HttpSession session = request.getSession(true);
      session.setAttribute("userId", user.getId());
      session.setAttribute("userName", user.getName());
      session.setAttribute("userRole", user.getRole().name());
      if (redirectPath != null) {
        response.sendRedirect(redirectPath);
      } else {
        response.sendRedirect(request.getContextPath() + "/welcome.jsp");
      }
    } catch (InvalidCredentialsException e) {
      request.setAttribute("errorMessage", e.getMessage());
      RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
      dispatcher.forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Database error during login", e);
    }
  }
}
