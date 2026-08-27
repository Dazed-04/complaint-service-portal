package com.miet.complaintportal.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.miet.complaintportal.model.Attachment;
import com.miet.complaintportal.model.Category;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;
import com.miet.complaintportal.model.Role;
import com.miet.complaintportal.model.User;

class OracleAttachmentDaoIT {
  private UserDao userDao;
  private ComplaintDao complaintDao;
  private AttachmentDao attachmentDao;
  private CategoryDao categoryDao;
  private long userId;
  private long categoryId;
  private Complaint complaint;
  private String title;
  private String description;
  private final List<Long> createdUserIds = new ArrayList<>();
  private final List<Long> createdCategoryIds = new ArrayList<>();
  private final List<Long> createdComplaintIds = new ArrayList<>();

  long createUser() throws SQLException {
    String email = "test_" + UUID.randomUUID() + "@example.com";
    User user = new User();
    user.setName("test");
    user.setEmail(email);
    user.setRole(Role.CUSTOMER);
    user.setPasswordHash("testPasswd");
    User saved = userDao.save(user);
    createdUserIds.add(saved.getId());
    return saved.getId();
  }

  long createCategory() throws SQLException {
    String name = "test_" + UUID.randomUUID();
    Category category = new Category();
    category.setName(name);
    category.setDescription(null);
    Category saved = categoryDao.save(category);
    createdCategoryIds.add(saved.getId());
    return saved.getId();
  }

  Complaint createComplaint() throws SQLException {
    title = "test_" + UUID.randomUUID();
    description = "Test complaint.";
    ComplaintStatus status = ComplaintStatus.OPEN;
    Complaint complaint = new Complaint();
    complaint.setCustomerId(userId);
    complaint.setCategoryId(categoryId);
    complaint.setTitle(title);
    complaint.setDescription(description);
    complaint.setStatus(status);
    return complaint;
  }

  @BeforeEach
  void setup() throws Exception {
    userDao = new OracleUserDao();
    categoryDao = new OracleCategoryDao();
    complaintDao = new OracleComplaintDao();
    attachmentDao = new OracleAttachmentDao();
    userId = createUser();
    categoryId = createCategory();
    complaint = createComplaint();
  }

  @AfterEach
  void cleanup() throws SQLException {
    try (Connection conn = new DbConnectionProvider().getConnection();
        Statement stmt = conn.createStatement()) {

      if (!createdComplaintIds.isEmpty()) {
        String complaintIds = createdComplaintIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        stmt.executeUpdate("DELETE FROM attachments WHERE complaint_id IN (" + complaintIds + ")");
        stmt.executeUpdate("DELETE FROM status_history WHERE complaint_id IN (" + complaintIds + ")");
        stmt.executeUpdate("DELETE FROM complaints WHERE id IN (" + complaintIds + ")");
      }
      if (!createdCategoryIds.isEmpty()) {
        String categoryIds = createdCategoryIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        stmt.executeUpdate("DELETE FROM categories WHERE id IN (" + categoryIds + ")");
      }
      if (!createdUserIds.isEmpty()) {
        String userIds = createdUserIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        stmt.executeUpdate("DELETE FROM users WHERE id IN (" + userIds + ")");
      }
    }
    createdComplaintIds.clear();
    createdCategoryIds.clear();
    createdUserIds.clear();
  }

  @Test
  void save_thenFindByComplaintId_returnsMatchingAttachment() throws Exception {
    byte[] fileData = "test content".getBytes();
    Complaint saved = complaintDao.save(complaint);
    createdComplaintIds.add(saved.getId());
    Attachment attachment = new Attachment();
    attachment.setComplaintId(saved.getId());
    attachment.setContentType("test/plain");
    attachment.setFilename("test attachment");
    attachment.setFileData(fileData);
    attachmentDao.save(attachment);

    List<Attachment> result = attachmentDao.findByComplaintId(saved.getId());
    Attachment found = result.get(0);
    assertEquals("test attachment", found.getFilename());
    assertEquals("test/plain", found.getContentType());
    assertTrue(Arrays.equals(fileData, found.getFileData()));
  }
}
