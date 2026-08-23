package com.miet.complaintportal.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

  long createUser() throws SQLException {
    String email = "test_" + UUID.randomUUID() + "@example.com";
    User user = new User();
    user.setName("test");
    user.setEmail(email);
    user.setRole(Role.CUSTOMER);
    user.setPasswordHash("testPasswd");
    User saved = userDao.save(user);
    return saved.getId();
  }

  long createCategory() throws SQLException {
    String name = "test_" + UUID.randomUUID();
    Category category = new Category();
    category.setName(name);
    category.setDescription(null);
    Category saved = categoryDao.save(category);
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

  @Test
  void save_thenFindByComplaintId_returnsMatchingAttachment() throws Exception {
    byte[] fileData = "test content".getBytes();
    Complaint saved = complaintDao.save(complaint);
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
