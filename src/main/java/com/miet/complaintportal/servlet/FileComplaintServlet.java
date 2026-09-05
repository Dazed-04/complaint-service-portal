package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.miet.complaintportal.dao.CategoryDao;
import com.miet.complaintportal.model.Category;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.service.ComplaintService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/complaints/file")
public class FileComplaintServlet extends HttpServlet {

  @Autowired
  private ComplaintService complaintService;
  @Autowired
  private CategoryDao categoryDao;

  public FileComplaintServlet() {
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
      List<Category> categories = categoryDao.findAll();
      request.setAttribute("categories", categories);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/file-complaint.jsp");
      dispatcher.forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Failed to load categories", e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    long customerId = (long) session.getAttribute("userId");

    long categoryId = Long.parseLong(request.getParameter("categoryId"));
    String title = request.getParameter("title");
    String description = request.getParameter("description");

    try {
      Complaint filed = complaintService.fileComplaint(customerId, categoryId, title, description);
      session.setAttribute("filedComplaintId", filed.getId());
      response.sendRedirect(request.getContextPath() + "/complaints/file?success=true");
    } catch (SQLException e) {
      throw new ServletException("Failed to file complaint", e);
    }
  }
}
