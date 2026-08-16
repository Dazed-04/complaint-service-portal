package com.miet.complaintportal.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.miet.complaintportal.dao.CategoryDao;
import com.miet.complaintportal.dao.ComplaintDao;
import com.miet.complaintportal.dao.OracleCategoryDao;
import com.miet.complaintportal.dao.OracleComplaintDao;
import com.miet.complaintportal.dao.OracleStatusHistoryDao;
import com.miet.complaintportal.dao.StatusHistoryDao;
import com.miet.complaintportal.model.Category;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;
import com.miet.complaintportal.model.StatusHistory;

public class ComplaintServiceImpl implements ComplaintService {

  private final ComplaintDao complaintDao;
  private final CategoryDao categoryDao;
  private final StatusHistoryDao statusHistoryDao;

  public ComplaintServiceImpl() {
    this(new OracleComplaintDao(), new OracleCategoryDao(), new OracleStatusHistoryDao());
  }

  public ComplaintServiceImpl(ComplaintDao complaintDao, CategoryDao categoryDao, StatusHistoryDao statusHistoryDao) {
    this.complaintDao = complaintDao;
    this.categoryDao = categoryDao;
    this.statusHistoryDao = statusHistoryDao;
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
          "Complaint" + complaintId + " references nonexistent category " + complaint.get().getCategoryId());
    }
    String categoryName = categoryOpt.get().getName();
    List<StatusHistory> history = statusHistoryDao.findByComplaintId(complaintId);
    ComplaintDetail complaintDetail = new ComplaintDetail(complaint.get(), categoryName, history);
    return Optional.of(complaintDetail);
  }
}
