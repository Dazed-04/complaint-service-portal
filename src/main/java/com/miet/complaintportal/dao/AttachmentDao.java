package com.miet.complaintportal.dao;

import java.sql.SQLException;
import java.util.List;

import com.miet.complaintportal.model.Attachment;

public interface AttachmentDao {

  Attachment save(Attachment attachment) throws SQLException;

  List<Attachment> findByComplaintId(long complaintId) throws SQLException;

}
