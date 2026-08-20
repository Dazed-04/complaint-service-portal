package com.miet.complaintportal.service;

import java.sql.SQLException;
import java.util.List;

import com.miet.complaintportal.exceptions.CategoryAlreadyExistsException;
import com.miet.complaintportal.exceptions.CategoryNameRequiredException;
import com.miet.complaintportal.model.Category;

public interface CategoryService {
  Category createCategory(String name, String description)
      throws SQLException, CategoryAlreadyExistsException, CategoryNameRequiredException;

  List<Category> listCategories() throws SQLException;
}
