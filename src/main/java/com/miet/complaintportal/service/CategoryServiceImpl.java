package com.miet.complaintportal.service;

import java.sql.SQLException;
import java.util.List;

import com.miet.complaintportal.dao.CategoryDao;
import com.miet.complaintportal.dao.OracleCategoryDao;
import com.miet.complaintportal.exceptions.CategoryAlreadyExistsException;
import com.miet.complaintportal.model.Category;

public class CategoryServiceImpl implements CategoryService {

  private final CategoryDao categoryDao;

  public CategoryServiceImpl() {
    this(new OracleCategoryDao());
  }

  public CategoryServiceImpl(CategoryDao categoryDao) {
    this.categoryDao = categoryDao;
  }

  @Override
  public Category createCategory(String name, String description) throws SQLException, CategoryAlreadyExistsException {

    if (categoryDao.findByName(name).isPresent()) {
      throw new CategoryAlreadyExistsException("Category already exists: " + name);
    }
    Category category = new Category();
    category.setName(name);
    if (description == null || description.isBlank()) {
      category.setDescription(null);
    } else {
      category.setDescription(description);
    }
    Category newCategory = categoryDao.save(category);
    return newCategory;
  }

  @Override
  public List<Category> listCategories() throws SQLException {
    return categoryDao.findAll();
  }

}
