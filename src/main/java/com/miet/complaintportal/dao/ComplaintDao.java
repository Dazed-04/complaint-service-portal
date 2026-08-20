package com.miet.complaintportal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;

public interface ComplaintDao {

  Complaint save(Complaint complaint) throws SQLException;

  Optional<Complaint> findById(long id) throws SQLException;

  List<Complaint> findByCustomerId(long customerId) throws SQLException;

  List<Complaint> findByAgentId(long agentId) throws SQLException;

  List<Complaint> findAll() throws SQLException;

  void assignAgent(long complaintId, long agentId) throws SQLException;

  void updateStatus(long complaintId, ComplaintStatus newStatus, long changedByUserId, String remarks)
      throws SQLException;
}
