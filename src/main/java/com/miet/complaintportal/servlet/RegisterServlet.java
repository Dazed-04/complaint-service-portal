package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.miet.complaintportal.exceptions.EmailAlreadyExistsException;
import com.miet.complaintportal.model.Role;
import com.miet.complaintportal.model.User;
import com.miet.complaintportal.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

  @Autowired
  private UserService userService;

  public RegisterServlet() {
  }

  @Override
  public void init() throws ServletException {
    super.init();
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
    dispatcher.forward(request, response);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    try {
      User saved = userService.registerUser(name, email, password, Role.CUSTOMER);
      // success-> PRG: redirect to GET endpoint
      HttpSession session = request.getSession();
      session.setAttribute("registeredName", saved.getName());
      session.setAttribute("registeredEmail", saved.getEmail());
      response.sendRedirect(request.getContextPath() + "/result.jsp");
    } catch (EmailAlreadyExistsException e) {
      request.setAttribute("errorMessage", e.getMessage());
      RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
      dispatcher.forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Database error during registration", e);
    }
    // Removed as context needs to survive between requests
    // request.setAttribute("name", name);
    // request.setAttribute("email", email);
    // RequestDispatcher dispatcher = request.getRequestDispatcher("/result.jsp");
    // dispatcher.forward(request, response);
  }
}
