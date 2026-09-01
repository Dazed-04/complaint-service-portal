package com.miet.complaintportal.servlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miet.complaintportal.model.Attachment;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.service.AttachmentService;
import com.miet.complaintportal.service.ComplaintDetail;
import com.miet.complaintportal.service.ComplaintService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@ExtendWith(MockitoExtension.class)
class AttachComplaintServletTest {

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private HttpSession session;
  @Mock
  private Part filePart;
  @Mock
  private RequestDispatcher dispatcher;
  @Mock
  private AttachmentService attachmentService;
  @Mock
  private ComplaintService complaintService;

  private AttachComplaintServlet servlet;

  @BeforeEach
  void setup() {
    servlet = new AttachComplaintServlet(attachmentService, complaintService);
  }

  @Test
  void doPost_attachesFile_whenComplaintExists() throws Exception {
    Complaint complaint = new Complaint();
    complaint.setId(5L);
    complaint.setCustomerId(5L);
    ComplaintDetail complaintDetail = new ComplaintDetail(complaint, "test category", new ArrayList<>(),
        new ArrayList<>());
    when(complaintService.viewComplaintDetail(5L)).thenReturn(Optional.of(complaintDetail));

    when(filePart.getSubmittedFileName()).thenReturn("test file");
    when(filePart.getContentType()).thenReturn("plain/text");
    when(filePart.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));
    when(filePart.getSize()).thenReturn(9L);
    when(request.getPart("file")).thenReturn(filePart);

    Attachment attachment = new Attachment();
    attachment.setId(1L);
    attachment.setComplaintId(5L);
    attachment.setFilename("test file");
    attachment.setContentType("plain/text");
    attachment.setFileData("test data".getBytes());
    when(attachmentService.addAttachment(5L, "test file", "plain/text", "test data".getBytes())).thenReturn(attachment);

    when(request.getParameter("complaintId")).thenReturn("5");
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(5L);
    when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
    when(request.getContextPath()).thenReturn("/complaint-portal");

    servlet.doPost(request, response);
    verify(attachmentService).addAttachment(5L, "test file", "plain/text", "test data".getBytes());
  }

  @Test
  void doPost_redirectsToError_whenEmptyAttachmentSubmits() throws Exception {
    Complaint complaint = new Complaint();
    complaint.setId(5L);
    complaint.setCustomerId(1L);
    ComplaintDetail complaintDetail = new ComplaintDetail(complaint, "test category", new ArrayList<>(),
        new ArrayList<>());
    when(complaintService.viewComplaintDetail(5L)).thenReturn(Optional.of(complaintDetail));

    when(request.getParameter("complaintId")).thenReturn("5");
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(1L);
    when(session.getAttribute("userRole")).thenReturn("CUSTOMER");

    when(request.getPart("file")).thenReturn(filePart);
    when(filePart.getSize()).thenReturn(0L);

    when(request.getRequestDispatcher("/error.jsp")).thenReturn(dispatcher);

    servlet.doPost(request, response);

    verify(attachmentService, never()).addAttachment(anyLong(), anyString(), anyString(), any());
    verify(dispatcher).forward(request, response);
  }

}
