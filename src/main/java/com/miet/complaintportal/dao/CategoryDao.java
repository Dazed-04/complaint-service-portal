package com.miet.complaintportal.dao;

import java.sql.SQLException;
import java.util.Optional;

import com.miet.complaintportal.model.Category;

public interface CategoryDao {
  Category save(Category category) throws SQLException;

  Optional<Category> findById(long id) throws SQLException;

  Optional<Category> findByName(String name) throws SQLException;

}
