package com.miet.complaintportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.miet.complaintportal.model.Category;

public class OracleCategoryDao implements CategoryDao {

  private final DbConnectionProvider connectionProvider;

  public OracleCategoryDao() {
    this.connectionProvider = new DbConnectionProvider();
  }

  @Override
  public Category save(Category category) throws SQLException {
    String query = "INSERT into categories (name, description) VALUES (?, ?)";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query, new String[] { "id" })) {
      preparedStatement.setString(1, category.getName());
      preparedStatement.setString(2, category.getDescription());

      int rowsInserted = preparedStatement.executeUpdate();
      if (rowsInserted > 0) {
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            long generatedId = generatedKeys.getLong(1);
            if (!generatedKeys.wasNull()) {
              return new Category(generatedId, category.getName(), category.getDescription());
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public Optional<Category> findById(long id) throws SQLException {
    String query = "SELECT * from categories WHERE id = ?";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query)) {
      preparedStatement.setLong(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          String name = rs.getString("name");
          String description = rs.getString("description");
          Category category = new Category(id, name, description);
          return Optional.of(category);
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public Optional<Category> findByName(String name) throws SQLException {
    String query = "SELECT * from categories WHERE name = ?";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query)) {
      preparedStatement.setString(1, name);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          long id = rs.getLong("id");
          String description = rs.getString("description");
          Category category = new Category(id, name, description);
          return Optional.of(category);
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public List<Category> findAll() throws SQLException {
    String query = "SELECT * from categories";
    List<Category> result = new ArrayList<>();
    try (Connection conn = connectionProvider.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {
      while (rs.next()) {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Category category = new Category(id, name, description);
        result.add(category);
      }
      return result;
    }
  }
}
