package com.miet.complaintportal.service;

import java.sql.SQLException;
import java.util.List;

import com.miet.complaintportal.dao.ComplaintDao;
import com.miet.complaintportal.dao.OracleComplaintDao;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;

public class ComplaintServiceImpl implements ComplaintService {

  private final ComplaintDao complaintDao;

  public ComplaintServiceImpl() {
    this(new OracleComplaintDao());
  }

  public ComplaintServiceImpl(ComplaintDao complaintDao) {
    this.complaintDao = complaintDao;
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

}
