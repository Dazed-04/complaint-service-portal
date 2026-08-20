package com.miet.complaintportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;

public class OracleComplaintDao implements ComplaintDao {

  private final DbConnectionProvider connectionProvider;

  public OracleComplaintDao() {
    this.connectionProvider = new DbConnectionProvider();
  }

  @Override
  public Complaint save(Complaint complaint) throws SQLException {
    String query = "INSERT into complaints (customer_id, category_id, title, description, status) VALUES ( ?, ?, ?, ?, ? )";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement prepareStatement = conn.prepareStatement(query, new String[] { "id" })) {
      prepareStatement.setLong(1, complaint.getCustomerId());
      prepareStatement.setLong(2, complaint.getCategoryId());
      prepareStatement.setString(3, complaint.getTitle());
      prepareStatement.setString(4, complaint.getDescription());
      prepareStatement.setString(5, complaint.getStatus().name());

      int rowsInserted = prepareStatement.executeUpdate();
      if (rowsInserted > 0) {
        try (ResultSet generatedKeys = prepareStatement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            long generatedId = generatedKeys.getLong(1);
            if (!generatedKeys.wasNull()) {
              Complaint newComplaint = new Complaint();
              newComplaint.setId(generatedId);
              newComplaint.setCustomerId(complaint.getCustomerId());
              newComplaint.setCategoryId(complaint.getCategoryId());
              newComplaint.setTitle(complaint.getTitle());
              newComplaint.setDescription(complaint.getDescription());
              newComplaint.setStatus(complaint.getStatus());
              return newComplaint;
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public Optional<Complaint> findById(long id) throws SQLException {
    String query = "SELECT * from complaints where id = ?";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query)) {
      preparedStatement.setLong(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          long customerId = rs.getLong("customer_id");
          Long agentID = rs.getLong("agent_id");
          if (rs.wasNull()) {
            agentID = null;
          }
          long categoryId = rs.getLong("category_id");
          String title = rs.getString("title");
          String description = rs.getString("description");
          ComplaintStatus status = ComplaintStatus.valueOf(rs.getString("status"));
          LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
          LocalDateTime updatedAt = rs.getObject("updated_at", LocalDateTime.class);
          Complaint complaint = new Complaint(id, customerId, agentID, categoryId, title, description, status,
              createdAt, updatedAt);
          return Optional.of(complaint);
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public List<Complaint> findByCustomerId(long customerId) throws SQLException {
    List<Complaint> results = new ArrayList<>();
    String query = "SELECT * from complaints where customer_id = ?";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query)) {
      preparedStatement.setLong(1, customerId);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          long id = rs.getLong("id");
          Long agentId = rs.getLong("agent_id");
          if (rs.wasNull()) {
            agentId = null;
          }
          long categoryId = rs.getLong("category_id");
          String title = rs.getString("title");
          String description = rs.getString("description");
          ComplaintStatus status = ComplaintStatus.valueOf(rs.getString("status"));
          LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
          LocalDateTime updatedAt = rs.getObject("updated_at", LocalDateTime.class);
          Complaint complaint = new Complaint(id, customerId, agentId, categoryId, title, description, status,
              createdAt, updatedAt);
          results.add(complaint);
        }
        return results;
      }
    }
  }

  @Override
  public List<Complaint> findByAgentId(long agentId) throws SQLException {
    List<Complaint> results = new ArrayList<>();
    String query = "SELECT * from complaints where agent_id = ?";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query)) {
      preparedStatement.setLong(1, agentId);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          long id = rs.getLong("id");
          long customerId = rs.getLong("customer_id");
          long categoryId = rs.getLong("category_id");
          String title = rs.getString("title");
          String description = rs.getString("description");
          ComplaintStatus status = ComplaintStatus.valueOf(rs.getString("status"));
          LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
          LocalDateTime updatedAt = rs.getObject("updated_at", LocalDateTime.class);
          Complaint complaint = new Complaint(id, customerId, agentId, categoryId, title, description, status,
              createdAt, updatedAt);
          results.add(complaint);
        }
        return results;
      }
    }
  }

  @Override
  public void updateStatus(long complaintId, ComplaintStatus newStatus, long changedByUserId, String remarks)
      throws SQLException {
    Connection conn = null;
    try {
      conn = connectionProvider.getConnection();
      conn.setAutoCommit(false);

      String oldStatus = null;
      String selectQuery = "SELECT status FROM complaints WHERE id = ? FOR UPDATE";
      try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
        selectStmt.setLong(1, complaintId);
        try (ResultSet rs = selectStmt.executeQuery()) {
          if (rs.next()) {
            oldStatus = rs.getString("status");
          } else {
            throw new SQLException("No complaint found with id: " + complaintId);
          }
        }
      }

      String updateQuery = "UPDATE complaints SET status = ? WHERE id = ?";
      try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
        updateStmt.setString(1, newStatus.name());
        updateStmt.setLong(2, complaintId);
        updateStmt.executeUpdate();
      }

      String insertQuery = "INSERT into status_history (complaint_id, old_status, new_status, changed_by, remark) VALUES (?, ?, ?, ?, ?)";
      try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
        insertStmt.setLong(1, complaintId);
        insertStmt.setString(2, oldStatus);
        insertStmt.setString(3, newStatus.name());
        insertStmt.setLong(4, changedByUserId);
        insertStmt.setString(5, remarks);
        insertStmt.executeUpdate();
      }

      conn.commit();
    } catch (SQLException e) {
      if (conn != null) {
        conn.rollback();
      }
      throw e;
    } finally {
      if (conn != null) {
        conn.setAutoCommit(true);
        conn.close();
      }
    }
  }

  @Override
  public List<Complaint> findAll() throws SQLException {
    String query = "SELECT * from complaints";
    List<Complaint> complaints = new ArrayList<>();
    try (Connection conn = connectionProvider.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {
      while (rs.next()) {
        long id = rs.getLong("id");
        long customerId = rs.getLong("customer_id");
        Long agentId = rs.getLong("agent_id");
        if (rs.wasNull()) {
          agentId = null;
        }
        long categoryId = rs.getLong("category_id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        ComplaintStatus status = ComplaintStatus.valueOf(rs.getString("status"));
        LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
        LocalDateTime updatedAt = rs.getObject("updated_at", LocalDateTime.class);
        Complaint complaint = new Complaint(id, customerId, agentId, categoryId, title, description, status,
            createdAt, updatedAt);
        complaints.add(complaint);
      }
      return complaints;
    }
  }

  @Override
  public void assignAgent(long complaintId, long agentId) throws SQLException {
    String updateQuery = "UPDATE complaints set agent_id = ? where id = ?";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
      updateStmt.setLong(1, agentId);
      updateStmt.setLong(2, complaintId);
      updateStmt.executeUpdate();
    }
  }
}
