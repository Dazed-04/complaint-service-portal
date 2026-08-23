package com.miet.complaintportal.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.miet.complaintportal.dao.AttachmentDao;
import com.miet.complaintportal.dao.ComplaintDao;
import com.miet.complaintportal.dao.OracleAttachmentDao;
import com.miet.complaintportal.dao.OracleComplaintDao;
import com.miet.complaintportal.exceptions.ComplaintNotFoundException;
import com.miet.complaintportal.exceptions.FileTooLargeException;
import com.miet.complaintportal.model.Attachment;
import com.miet.complaintportal.model.Complaint;

public class AttachmentServiceImpl implements AttachmentService {

  private final AttachmentDao attachmentDao;
  private final ComplaintDao complaintDao;

  public AttachmentServiceImpl() {
    this(new OracleAttachmentDao(), new OracleComplaintDao());
  }

  public AttachmentServiceImpl(AttachmentDao attachmentDao, ComplaintDao complaintDao) {
    this.attachmentDao = attachmentDao;
    this.complaintDao = complaintDao;
  }

  @Override
  public Attachment addAttachment(long complaintId, String filename, String contentType, byte[] fileData)
      throws SQLException, FileTooLargeException, ComplaintNotFoundException {

    Optional<Complaint> attachingTo = complaintDao.findById(complaintId);
    if (attachingTo.isEmpty()) {
      throw new ComplaintNotFoundException("No such complaint exist");
    }

    if (fileData.length > 5 * 1024 * 1024) {
      throw new FileTooLargeException("Files must be less than 5MB");
    }

    Attachment attachment = new Attachment();
    attachment.setComplaintId(complaintId);
    attachment.setFilename(filename);
    attachment.setContentType(contentType);
    attachment.setFileData(fileData);
    Attachment saved = attachmentDao.save(attachment);
    return saved;
  }

  @Override
  public List<Attachment> listAttachments(long complaintId) throws SQLException {
    return attachmentDao.findByComplaintId(complaintId);
  }

}
