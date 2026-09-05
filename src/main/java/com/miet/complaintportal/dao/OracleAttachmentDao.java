package com.miet.complaintportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.miet.complaintportal.model.Attachment;

@Repository
public class OracleAttachmentDao implements AttachmentDao {

  @Autowired
  private DbConnectionProvider connectionProvider;

  public OracleAttachmentDao() {
    this.connectionProvider = new DbConnectionProvider();
  }

  @Override
  public Attachment save(Attachment attachment) throws SQLException {
    String query = "INSERT into attachments (complaint_id, filename, content_type, file_data) VALUES (?, ?, ?, ?)";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query, new String[] { "id" })) {
      preparedStatement.setLong(1, attachment.getComplaintId());
      preparedStatement.setString(2, attachment.getFilename());
      preparedStatement.setString(3, attachment.getContentType());
      preparedStatement.setBytes(4, attachment.getFileData());

      int rowsInserted = preparedStatement.executeUpdate();
      if (rowsInserted > 0) {
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            long generatedId = generatedKeys.getLong(1);
            if (!generatedKeys.wasNull()) {
              return new Attachment(generatedId, attachment.getComplaintId(), attachment.getFilename(),
                  attachment.getContentType(), attachment.getFileData(), attachment.getUploadedAt());
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public List<Attachment> findByComplaintId(long complaintId) throws SQLException {
    List<Attachment> results = new ArrayList<>();
    String query = "SELECT * FROM Attachments WHERE complaint_id = ? ORDER BY uploaded_at DESC";
    try (Connection conn = connectionProvider.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query)) {
      preparedStatement.setLong(1, complaintId);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          long id = rs.getLong("id");
          String filename = rs.getString("filename");
          String contentType = rs.getString("content_type");
          byte[] fileData = rs.getBytes("file_data");
          LocalDateTime uploadedAt = rs.getObject("uploaded_at", LocalDateTime.class);
          Attachment attachment = new Attachment(id, complaintId, filename, contentType, fileData, uploadedAt);
          results.add(attachment);
        }
      }
    }
    return results;
  }

}
