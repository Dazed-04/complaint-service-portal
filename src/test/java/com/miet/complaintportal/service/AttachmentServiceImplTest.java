package com.miet.complaintportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miet.complaintportal.dao.AttachmentDao;
import com.miet.complaintportal.dao.ComplaintDao;
import com.miet.complaintportal.exceptions.ComplaintNotFoundException;
import com.miet.complaintportal.exceptions.FileTooLargeException;
import com.miet.complaintportal.model.Attachment;
import com.miet.complaintportal.model.Complaint;

@ExtendWith(MockitoExtension.class)
public class AttachmentServiceImplTest {

  @Mock
  private ComplaintDao complaintDao;
  @Mock
  private AttachmentDao attachmentDao;
  private AttachmentServiceImpl attachmentService;

  @BeforeEach
  void setup() {
    attachmentService = new AttachmentServiceImpl(attachmentDao, complaintDao);
  }

  @Test
  void addAttachment_savesAttachment_whenComplaintIsValid() throws Exception {
    Complaint complaint = new Complaint();
    complaint.setId(5L);
    when(complaintDao.findById(5L)).thenReturn(Optional.of(complaint));

    Attachment attachment = new Attachment();
    attachment.setComplaintId(5L);
    attachment.setFilename("test file");
    attachment.setContentType("text/plain");
    attachment.setFileData("test byte array".getBytes());
    when(attachmentDao.save(any(Attachment.class))).thenReturn(attachment);

    long complaintId = 5L;
    String filename = "test file";
    String contentType = "text/plain";
    byte[] fileData = "test byte array".getBytes();

    Attachment saved = attachmentService.addAttachment(complaintId, filename, contentType, fileData);
    assertEquals(complaintId, saved.getComplaintId());
    assertEquals(filename, saved.getFilename());
    assertEquals(contentType, saved.getContentType());
    assertTrue(Arrays.equals(fileData, saved.getFileData()));
  }

  @Test
  void addAttachment_throwsComplaintNotFoundException_whenInvalidComplaintId() throws Exception {
    when(complaintDao.findById(anyLong())).thenReturn(Optional.empty());
    long complaintId = 5L;
    String filename = "test file";
    String contentType = "text/plain";
    byte[] fileData = "test byte array".getBytes();

    assertThrows(ComplaintNotFoundException.class, () -> {
      attachmentService.addAttachment(complaintId, filename, contentType, fileData);
    });

  }

  @Test
  void addAttachment_throwsFileTooLargeException_whenFileExceedsLimit() throws Exception {
    Complaint complaint = new Complaint();
    complaint.setId(5L);
    when(complaintDao.findById(5L)).thenReturn(Optional.of(complaint));

    long complaintId = 5L;
    String filename = "test file";
    String contentType = "text/plain";
    byte[] fileData = new byte[6 * 1024 * 1024];

    assertThrows(FileTooLargeException.class, () -> {
      attachmentService.addAttachment(complaintId, filename, contentType, fileData);
    });

  }

}
