package com.miet.complaintportal.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.miet.complaintportal.model.Category;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;
import com.miet.complaintportal.model.StatusHistory;
import com.miet.complaintportal.model.User;

class OracleComplaintDaoIT {
  private UserDao userDao;
  private CategoryDao categoryDao;
  private ComplaintDao complaintDao;
  private StatusHistoryDao statusHistoryDao;
  private long userId;
  private long categoryId;
  private String title;
  private String description;

  long createUser() throws SQLException {
    String email = "test_" + UUID.randomUUID() + "@example.com";
    User user = new User();
    user.setName("test");
    user.setEmail(email);
    user.setRole("CUSTOMER");
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
    statusHistoryDao = new OracleStatusHistoryDao();
    userId = createUser();
    categoryId = createCategory();
  }

  @Test
  void save_thenFindById_returnsMatchingComplaint() throws Exception {
    Complaint complaint = createComplaint();
    Complaint saved = complaintDao.save(complaint);
    assertNotEquals(0, saved.getId());
    Optional<Complaint> found = complaintDao.findById(saved.getId());
    assertTrue(found.isPresent());
    assertEquals(title, found.get().getTitle());
    assertEquals(description, found.get().getDescription());
    assertEquals(ComplaintStatus.OPEN, found.get().getStatus());
  }

  @Test
  void findByCustomerId_returnsComplaint_afterSave() throws Exception {
    Complaint complaint = createComplaint();
    Complaint saved = complaintDao.save(complaint);
    assertNotEquals(0, saved.getId());
    List<Complaint> found = complaintDao.findByCustomerId(userId);
    assertTrue(found.stream().anyMatch(c -> c.getId() == saved.getId()));
  }

  @Test
  void findByComplaintId_updateComplaintStatus_saveToStatusHistory() throws Exception {
    Complaint saved = complaintDao.save(createComplaint());
    ComplaintStatus oldStatus = saved.getStatus();
    long id = saved.getId();
    complaintDao.updateStatus(id, ComplaintStatus.IN_PROGRESS, userId, "Test remark.");
    Optional<Complaint> updated = complaintDao.findById(id);
    assertNotEquals(updated, Optional.empty());
    assertEquals(ComplaintStatus.IN_PROGRESS, updated.get().getStatus());
    List<StatusHistory> found = statusHistoryDao.findByComplaintId(id);
    assertTrue(found.stream().anyMatch(c -> c.getChangedBy() == userId));
    assertTrue(found.stream().anyMatch(c -> c.getOldStatus() == oldStatus));
  }

  @Test
  void updatedStatus_rollsBackStatusChange_whenHistoryInsertFails() throws Exception {
    Complaint saved = complaintDao.save(createComplaint());
    long id = saved.getId();
    ComplaintStatus oldStatus = saved.getStatus();
    long wrongUserId = 999999L;
    assertThrows(SQLException.class, () -> {
      complaintDao.updateStatus(id, ComplaintStatus.IN_PROGRESS, wrongUserId, "Should fail");
    });
    Optional<Complaint> afterFail = complaintDao.findById(id);
    assertEquals(oldStatus, afterFail.get().getStatus());
  }

}
