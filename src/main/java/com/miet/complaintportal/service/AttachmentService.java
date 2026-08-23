package com.miet.complaintportal.service;

import java.sql.SQLException;
import java.util.List;

import com.miet.complaintportal.exceptions.ComplaintNotFoundException;
import com.miet.complaintportal.exceptions.FileTooLargeException;
import com.miet.complaintportal.model.Attachment;

public interface AttachmentService {
  Attachment addAttachment(long complaintId, String filename, String contentType, byte[] fileData)
      throws SQLException, FileTooLargeException, ComplaintNotFoundException;

  List<Attachment> listAttachments(long complaintId) throws SQLException;
}
