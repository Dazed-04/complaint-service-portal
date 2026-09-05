package com.miet.complaintportal.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miet.complaintportal.dao.AttachmentDao;
import com.miet.complaintportal.dao.CategoryDao;
import com.miet.complaintportal.dao.ComplaintDao;
import com.miet.complaintportal.dao.OracleAttachmentDao;
import com.miet.complaintportal.dao.OracleCategoryDao;
import com.miet.complaintportal.dao.OracleComplaintDao;
import com.miet.complaintportal.dao.OracleStatusHistoryDao;
import com.miet.complaintportal.dao.OracleUserDao;
import com.miet.complaintportal.dao.StatusHistoryDao;
import com.miet.complaintportal.dao.UserDao;
import com.miet.complaintportal.exceptions.InvalidAgentException;
import com.miet.complaintportal.model.Attachment;
import com.miet.complaintportal.model.Category;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;
import com.miet.complaintportal.model.Role;
import com.miet.complaintportal.model.StatusHistory;
import com.miet.complaintportal.model.User;

@Service
public class ComplaintServiceImpl implements ComplaintService {

  @Autowired
  private UserDao userDao;
  @Autowired
  private ComplaintDao complaintDao;
  @Autowired
  private CategoryDao categoryDao;
  @Autowired
  private StatusHistoryDao statusHistoryDao;
  @Autowired
  private AttachmentDao attachmentDao;

  public ComplaintServiceImpl() {
    this(new OracleUserDao(), new OracleComplaintDao(), new OracleCategoryDao(), new OracleStatusHistoryDao(),
        new OracleAttachmentDao());
  }

  public ComplaintServiceImpl(UserDao userDao, ComplaintDao complaintDao, CategoryDao categoryDao,
      StatusHistoryDao statusHistoryDao, AttachmentDao attachmentDao) {
    this.userDao = userDao;
    this.complaintDao = complaintDao;
    this.categoryDao = categoryDao;
    this.statusHistoryDao = statusHistoryDao;
    this.attachmentDao = attachmentDao;
  }

  @Override
  public Complaint fileComplaint(long customerId, long categoryId, String title,
      String description)
      throws SQLException {
    Complaint complaint = new Complaint();
    complaint.setCustomerId(customerId);
    complaint.setCategoryId(categoryId);
    complaint.setTitle(title);
    complaint.setDescription(description);
    complaint.setStatus(ComplaintStatus.OPEN);
    Complaint saved = complaintDao.save(complaint);
    return saved;

  }

  @Override
  public List<Complaint> viewComplaints(long customerId) throws SQLException {
    return complaintDao.findByCustomerId(customerId);
  }

  @Override
  public Complaint updateComplaintStatus(long complaintId, ComplaintStatus newStatus, long changedByUserId,
      String remarks) throws SQLException {
    complaintDao.updateStatus(complaintId, newStatus, changedByUserId, remarks);
    return complaintDao.findById(complaintId).orElseThrow(() -> new IllegalStateException(
        "Complaint " + complaintId + " vanished immediately after a successful status update"));
  }

  @Override
  public Optional<ComplaintDetail> viewComplaintDetail(long complaintId) throws SQLException {
    Optional<Complaint> complaint = complaintDao.findById(complaintId);
    if (complaint.isEmpty()) {
      return Optional.empty();
    }

    Optional<Category> categoryOpt = categoryDao.findById(complaint.get().getCategoryId());
    if (categoryOpt.isEmpty()) {
      throw new IllegalStateException(
          "Complaint " + complaintId + " references nonexistent category " + complaint.get().getCategoryId());
    }
    String categoryName = categoryOpt.get().getName();
    List<StatusHistory> history = statusHistoryDao.findByComplaintId(complaintId);
    List<Attachment> attachments = attachmentDao.findByComplaintId(complaintId);
    ComplaintDetail complaintDetail = new ComplaintDetail(complaint.get(), categoryName, history, attachments);
    return Optional.of(complaintDetail);
  }

  @Override
  public List<Complaint> viewAssignedComplaints(long agentId) throws SQLException {
    return complaintDao.findByAgentId(agentId);
  }

  @Override
  public void assignAgent(long complaintId, long agentId) throws SQLException, InvalidAgentException {
    Optional<User> agent = userDao.findById(agentId);
    if (agent.isEmpty()) {
      throw new InvalidAgentException(
          "Agent " + agentId + " references nonexistent agent ");
    }
    if (agent.get().getRole() != Role.AGENT) {
      throw new InvalidAgentException("Provided agentId " + agentId + " is not an agent");
    }
    complaintDao.assignAgent(complaintId, agentId);
  }

  @Override
  public List<Complaint> listAllComplaints() throws SQLException {
    return complaintDao.findAll();
  }
}
