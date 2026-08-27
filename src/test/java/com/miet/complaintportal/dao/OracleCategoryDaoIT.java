package com.miet.complaintportal.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.miet.complaintportal.model.Category;

class OracleCategoryDaoIT {
  private CategoryDao categoryDao;
  private final List<Long> createdCategoryIds = new ArrayList<>();

  Category createCategory(String name) {
    Category category = new Category();
    category.setName(name);
    return category;
  }

  @BeforeEach
  void setup() throws Exception {
    categoryDao = new OracleCategoryDao();
  }

  @AfterEach
  void cleanup() throws SQLException {
    if (createdCategoryIds.isEmpty()) {
      return;
    }
    try (Connection conn = new DbConnectionProvider().getConnection();
        Statement stmt = conn.createStatement()) {
      String ids = createdCategoryIds.stream().map(String::valueOf).collect(Collectors.joining(","));
      stmt.executeUpdate("DELETE FROM categories WHERE id IN (" + ids + ")");
    }
    createdCategoryIds.clear();
  }

  @Test
  void save_thenFindById_returnsMatchingCategory() throws Exception {
    String name = "test_" + UUID.randomUUID();
    Category category = createCategory(name);
    Category saved = categoryDao.save(category);
    createdCategoryIds.add(saved.getId());
    assertNotEquals(0, saved.getId());
    Optional<Category> found = categoryDao.findById(saved.getId());
    assertTrue(found.isPresent());
    assertEquals(name, found.get().getName());
  }

  @Test
  void findByName_returnsCategory_whenExists() throws Exception {
    String name = "test_" + UUID.randomUUID();
    Category category = createCategory(name);
    Category saved = categoryDao.save(category);
    createdCategoryIds.add(saved.getId());
    Optional<Category> found = categoryDao.findByName(name);
    assertNotEquals(Optional.empty(), found);
    assertEquals(name, found.get().getName());
  }

  @Test
  void findByName_isCaseInsensitive() throws Exception {
    String name = "test_" + UUID.randomUUID();
    Category category = createCategory(name);
    Category saved = categoryDao.save(category);
    createdCategoryIds.add(saved.getId());
    String differentCaseName = name.toUpperCase();
    Optional<Category> found = categoryDao.findByName(differentCaseName);
    assertTrue(found.isPresent());
    assertEquals(name, found.get().getName());
  }

  @Test
  void findByName_returnsEmpty_whenNoSuchCategory() throws Exception {
    String name = "test_" + UUID.randomUUID();
    assertEquals(Optional.empty(), categoryDao.findByName(name));
  }

  @Test
  void findAll_returnAllCategories() throws Exception {
    String name = "test_" + UUID.randomUUID();
    Category category = createCategory(name);
    Category saved1 = categoryDao.save(category);
    createdCategoryIds.add(saved1.getId());
    String otherName = "test_" + UUID.randomUUID();
    Category otherCategory = createCategory(otherName);
    Category saved2 = categoryDao.save(otherCategory);
    createdCategoryIds.add(saved2.getId());
    List<Category> categories = categoryDao.findAll();

    assertTrue(categories.stream().anyMatch(c -> c.getId() == saved1.getId()));
    assertTrue(categories.stream().anyMatch(c -> c.getId() == saved2.getId()));
  }

}
