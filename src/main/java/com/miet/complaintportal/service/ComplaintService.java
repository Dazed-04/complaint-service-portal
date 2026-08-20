package com.miet.complaintportal.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.miet.complaintportal.exceptions.InvalidAgentException;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;

public interface ComplaintService {
  Complaint fileComplaint(long complaintId, long categoryId, String title, String description)
      throws SQLException;

  List<Complaint> viewComplaints(long customerId) throws SQLException;

  Complaint updateComplaintStatus(long complaintId, ComplaintStatus newStatus, long changedByUserId, String remarks)
      throws SQLException;

  Optional<ComplaintDetail> viewComplaintDetail(long complaintId) throws SQLException;

  List<Complaint> viewAssignedComplaints(long agentId) throws SQLException;

  void assignAgent(long complaintId, long agentId) throws SQLException, InvalidAgentException;

}
