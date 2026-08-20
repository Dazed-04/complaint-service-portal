package com.miet.complaintportal.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.miet.complaintportal.exceptions.CategoryAlreadyExistsException;
import com.miet.complaintportal.exceptions.CategoryNameRequiredException;
import com.miet.complaintportal.model.Category;
import com.miet.complaintportal.service.CategoryService;
import com.miet.complaintportal.service.CategoryServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {
  private final CategoryService categoryService = new CategoryServiceImpl();

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      List<Category> categories = categoryService.listCategories();
      request.setAttribute("categories", categories);
      request.getRequestDispatcher("/admin-categories.jsp").forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Failed to load categories", e);
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String name = request.getParameter("name");
    String description = request.getParameter("description");
    try {
      categoryService.createCategory(name, description);
      response.sendRedirect(request.getContextPath() + "/admin/categories");
    } catch (CategoryNameRequiredException | CategoryAlreadyExistsException e) {
      request.setAttribute("errorMessage", e.getMessage());
      try {
        request.setAttribute("categories", categoryService.listCategories());
      } catch (SQLException ex) {
        throw new ServletException("Failed to load categories", ex);
      }
      request.getRequestDispatcher("/admin-categories.jsp").forward(request, response);
    } catch (SQLException e) {
      throw new ServletException("Database error during category creation", e);
    }

  }
}
