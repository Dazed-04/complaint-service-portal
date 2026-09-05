package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

  public HelloServlet() {
  }

  @Override
  public void init() throws ServletException {
    super.init();
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<h1>Complaint Portal is alive</h1>");

  }
}
