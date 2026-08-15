package com.miet.complaintportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.miet.complaintportal.model.ComplaintStatus;
import com.miet.complaintportal.model.StatusHistory;

public class OracleStatusHistoryDao implements StatusHistoryDao {

  private final DbConnectionProvider connectionProvider;

  public OracleStatusHistoryDao() {
    this.connectionProvider = new DbConnectionProvider();
  }

  @Override
  public List<StatusHistory> findByComplaintId(long complaintId) throws SQLException {
    List<StatusHistory> results = new ArrayList<>();
    String query = "SELECT * FROM status_history WHERE complaint_id = ?";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query)) {
      preparedStatement.setLong(1, complaintId);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          long id = rs.getLong("id");
          long changedBy = rs.getLong("changed_by");
          ComplaintStatus oldStatus = ComplaintStatus.valueOf(rs.getString("old_status"));
          ComplaintStatus newStatus = ComplaintStatus.valueOf(rs.getString("new_status"));
          String remark = rs.getString("remark");
          LocalDateTime changedAt = rs.getObject("changed_at", LocalDateTime.class);
          StatusHistory statusHistory = new StatusHistory(id, complaintId, changedBy, oldStatus, newStatus, remark,
              changedAt);
          results.add(statusHistory);
        }
      }
    }
    return results;
  }

}
