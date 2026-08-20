package com.miet.complaintportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miet.complaintportal.dao.CategoryDao;
import com.miet.complaintportal.exceptions.CategoryAlreadyExistsException;
import com.miet.complaintportal.exceptions.CategoryNameRequiredException;
import com.miet.complaintportal.model.Category;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

  @Mock
  private CategoryDao categoryDao;
  private CategoryServiceImpl categoryService;

  @BeforeEach
  void setup() {
    categoryService = new CategoryServiceImpl(categoryDao);
  }

  @Test
  void createCategory_savesCategory_whenNoDuplicateCategoryExists() throws Exception {
    when(categoryDao.findByName(anyString())).thenReturn(Optional.empty());
    when(categoryDao.save(any(Category.class))).thenAnswer(invocation -> {
      Category category = invocation.getArgument(0);
      category.setId(1L);
      return category;
    });
    String name = "test category";
    String description = "test description";
    Category saved = categoryService.createCategory(name, description);
    assertEquals(name, saved.getName());
    assertEquals(description, saved.getDescription());
  }

  @Test
  void createCategory_throwsException_whenNameBlank() throws Exception {
    String name = "";
    String description = "testing blank category name";
    assertThrows(CategoryNameRequiredException.class, () -> {
      categoryService.createCategory(name, description);
    });
  }

  @Test
  void createCategory_setsDescriptionNull_whenDescriptionBlank() throws Exception {
    when(categoryDao.findByName(anyString())).thenReturn(Optional.empty());
    when(categoryDao.save(any(Category.class))).thenAnswer(invocation -> {
      Category category = invocation.getArgument(0);
      category.setId(1L);
      return category;
    });
    String name = "test category";
    String description = "";
    Category newCategory = categoryService.createCategory(name, description);
    assertNull(newCategory.getDescription());
  }

  @Test
  void createCategory_throwsException_whenDuplicateCategoryExists() throws Exception {
    when(categoryDao.findByName(anyString())).thenAnswer(invocation -> {
      Category category = new Category();
      category.setName(invocation.getArgument(0));
      return Optional.of(category);
    });
    String name = "testCategory";
    assertThrows(CategoryAlreadyExistsException.class, () -> {
      categoryService.createCategory(name, "test description");
    });
  }

}
