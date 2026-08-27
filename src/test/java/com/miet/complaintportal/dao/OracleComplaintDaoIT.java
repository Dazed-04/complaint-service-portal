package com.miet.complaintportal.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.miet.complaintportal.model.Category;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;
import com.miet.complaintportal.model.Role;
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
  private final List<Long> createdComplaintIds = new ArrayList<>();
  private final List<Long> createdCategoryIds = new ArrayList<>();
  private final List<Long> createdUserIds = new ArrayList<>();

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

  long createAgent() throws SQLException {
    String email = "test_" + UUID.randomUUID() + "@example.com";
    User agent = new User();
    agent.setName("test");
    agent.setEmail(email);
    agent.setRole(Role.CUSTOMER);
    agent.setPasswordHash("testPasswd");
    User saved = userDao.save(agent);
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
    statusHistoryDao = new OracleStatusHistoryDao();
    userId = createUser();
    categoryId = createCategory();
  }

  @AfterEach
  void cleanup() throws SQLException {
    try (Connection conn = new DbConnectionProvider().getConnection();
        Statement stmt = conn.createStatement()) {

      if (!createdComplaintIds.isEmpty()) {
        String complaintIds = createdComplaintIds.stream().map(String::valueOf).collect(Collectors.joining(","));
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
  void save_thenFindById_returnsMatchingComplaint() throws Exception {
    Complaint complaint = createComplaint();
    Complaint saved = complaintDao.save(complaint);
    createdComplaintIds.add(saved.getId());
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
    createdComplaintIds.add(saved.getId());
    assertNotEquals(0, saved.getId());
    List<Complaint> found = complaintDao.findByCustomerId(userId);
    assertTrue(found.stream().anyMatch(c -> c.getId() == saved.getId()));
  }

  @Test
  void findByComplaintId_updateComplaintStatus_saveToStatusHistory() throws Exception {
    Complaint saved = complaintDao.save(createComplaint());
    createdComplaintIds.add(saved.getId());
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
    createdComplaintIds.add(id);
    ComplaintStatus oldStatus = saved.getStatus();
    long wrongUserId = 999999L;
    assertThrows(SQLException.class, () -> {
      complaintDao.updateStatus(id, ComplaintStatus.IN_PROGRESS, wrongUserId, "Should fail");
    });
    Optional<Complaint> afterFail = complaintDao.findById(id);
    assertEquals(oldStatus, afterFail.get().getStatus());
  }

  @Test
  void findByComplaintId_returnsHistoryNewestFirst() throws Exception {
    Complaint saved = complaintDao.save(createComplaint());
    createdComplaintIds.add(saved.getId());
    complaintDao.updateStatus(saved.getId(), ComplaintStatus.IN_PROGRESS, userId,
        "Status should be in descending order");
    Thread.sleep(1000);
    complaintDao.updateStatus(saved.getId(), ComplaintStatus.RESOLVED, userId,
        "Status should be in descending order");
    List<StatusHistory> history = statusHistoryDao.findByComplaintId(saved.getId());
    List<LocalDateTime> timestamps = history.stream().map(StatusHistory::getChangedAt).toList();
    List<LocalDateTime> sortedDesc = timestamps.stream().sorted(Comparator.reverseOrder()).toList();
    assertEquals(sortedDesc, timestamps);
  }

  @Test
  void findAll_returnsAllComplaints() throws Exception {
    Complaint saved = complaintDao.save(createComplaint());
    createdComplaintIds.add(saved.getId());
    Complaint saved2 = complaintDao.save(createComplaint());
    createdComplaintIds.add(saved2.getId());
    List<Complaint> complaints = complaintDao.findAll();
    assertTrue(complaints.stream().anyMatch(c -> c.getId() == saved.getId()));
    assertTrue(complaints.stream().anyMatch(c -> c.getId() == saved2.getId()));
  }

  @Test
  void assignAgent_setsAgentId() throws Exception {
    Complaint saved = complaintDao.save(createComplaint());
    createdComplaintIds.add(saved.getId());
    long agentId = createAgent();
    complaintDao.assignAgent(saved.getId(), agentId);
    Optional<Complaint> found = complaintDao.findById(saved.getId());
    assertEquals(agentId, found.get().getAgentId());
  }

  @Test
  void findByComplaintId_returnsEmptyList_whenNoStatusChangesRecorded() throws Exception {
    Complaint saved = complaintDao.save(createComplaint());
    createdComplaintIds.add(saved.getId());
    List<StatusHistory> history = statusHistoryDao.findByComplaintId(saved.getId());
    assertTrue(history.isEmpty());
  }

}
