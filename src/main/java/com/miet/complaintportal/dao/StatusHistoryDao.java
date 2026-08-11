package com.miet.complaintportal.dao;

import java.sql.SQLException;
import java.util.List;

import com.miet.complaintportal.model.StatusHistory;

public interface StatusHistoryDao {
  List<StatusHistory> findByComplaintId(long complaintId) throws SQLException;
}
