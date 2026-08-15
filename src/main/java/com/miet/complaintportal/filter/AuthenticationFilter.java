package com.miet.complaintportal.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/complaints/*")
public class AuthenticationFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    HttpSession session = httpRequest.getSession(false);
    boolean loggedIn = (session != null && session.getAttribute("userId") != null);

    if (loggedIn) {
      chain.doFilter(request, response);
    } else {
      HttpSession newSession = httpRequest.getSession(true);
      newSession.setAttribute("redirectAfterLogin", httpRequest.getRequestURI());
      httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
    }
  }
}
