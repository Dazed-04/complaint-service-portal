package com.miet.complaintportal.servlet;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miet.complaintportal.exceptions.InvalidCredentialsException;
import com.miet.complaintportal.model.Role;
import com.miet.complaintportal.model.User;
import com.miet.complaintportal.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private HttpSession session;
  @Mock
  private UserService userService;
  @Mock
  private RequestDispatcher dispatcher;

  private LoginServlet servlet;

  @BeforeEach
  void setup() {
    servlet = new LoginServlet(userService);
  }

  @Test
  void doGet_forwardsToLogin_WhenGoingToLogin() throws Exception {
    when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

    servlet.doGet(request, response);

    verify(dispatcher).forward(request, response);
  }

  @Test
  void doPost_redirectsToWelcome_onSuccessfulLogin() throws Exception {
    when(request.getParameter("email")).thenReturn("test@example.com");
    when(request.getParameter("password")).thenReturn("correctPassword");

    User user = new User();
    user.setId(1L);
    user.setName("Test User");
    user.setRole(Role.CUSTOMER);
    when(userService.login("test@example.com", "correctPassword")).thenReturn(user);

    when(request.getSession(false)).thenReturn(null);
    when(request.getSession(true)).thenReturn(session);
    when(request.getContextPath()).thenReturn("/complaint-portal");

    servlet.doPost(request, response);

    verify(session).setAttribute("userId", 1L);
    verify(session).setAttribute("userRole", "CUSTOMER");
    verify(response).sendRedirect("/complaint-portal/welcome.jsp");
  }

  @Test
  void doPost_forwardsToLogin_withErrorMessage_withInvalidCredentials() throws Exception {
    when(userService.login(anyString(), anyString()))
        .thenThrow(new InvalidCredentialsException("Invalid email or password"));
    when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
    when(request.getParameter("email")).thenReturn("test@example.com");
    when(request.getParameter("password")).thenReturn("correctPassword");

    servlet.doPost(request, response);

    verify(request).setAttribute("errorMessage", "Invalid email or password");

  }

  @Test
  void doPost_throwsServletException_whenSQLExceptionOccurs() throws Exception {
    when(userService.login(anyString(), anyString())).thenThrow(new SQLException("DB down"));
    when(request.getParameter("email")).thenReturn("test@example.com");
    when(request.getParameter("password")).thenReturn("correctPassword");

    assertThrows(ServletException.class, () -> servlet.doPost(request, response));
  }
}
