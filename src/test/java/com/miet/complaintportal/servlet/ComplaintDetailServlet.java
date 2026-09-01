package com.miet.complaintportal.servlet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miet.complaintportal.model.Attachment;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;
import com.miet.complaintportal.model.StatusHistory;
import com.miet.complaintportal.service.ComplaintDetail;
import com.miet.complaintportal.service.ComplaintService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class ComplaintDetailServletTest {

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private HttpSession session;
  @Mock
  private ComplaintService complaintService;
  @Mock
  private RequestDispatcher dispatcher;

  private ComplaintDetailServlet servlet;

  @BeforeEach
  void setup() {
    servlet = new ComplaintDetailServlet(complaintService);
  }

  @Test
  void doGet_forwardsToDetailPage_whenOwnerViewsOwnComplaint() throws Exception {
    Complaint complaint = new Complaint();
    complaint.setId(1L);
    complaint.setCustomerId(1L);
    complaint.setStatus(ComplaintStatus.OPEN);
    List<StatusHistory> history = new ArrayList<>();
    List<Attachment> attachments = new ArrayList<>();
    ComplaintDetail complaintDetail = new ComplaintDetail(complaint, "test category", history, attachments);

    when(complaintService.viewComplaintDetail(1L)).thenReturn(Optional.of(complaintDetail));
    when(request.getParameter("id")).thenReturn("1");
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(1L);
    when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
    when(request.getRequestDispatcher("/complaint-detail.jsp")).thenReturn(dispatcher);

    servlet.doGet(request, response);
    verify(request).setAttribute("detail", complaintDetail);
    verify(dispatcher).forward(request, response);

  }

  @Test
  void doGet_forwardsTo403_whenUserViewsSomeoneElseComplaint() throws Exception {
    Complaint complaint = new Complaint();
    complaint.setId(1L);
    complaint.setCustomerId(1L);
    complaint.setStatus(ComplaintStatus.OPEN);
    List<StatusHistory> history = new ArrayList<>();
    List<Attachment> attachments = new ArrayList<>();
    ComplaintDetail complaintDetail = new ComplaintDetail(complaint, "test category", history, attachments);

    when(complaintService.viewComplaintDetail(1L)).thenReturn(Optional.of(complaintDetail));
    when(request.getParameter("id")).thenReturn("1");
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(999L);
    when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
    when(request.getRequestDispatcher("/error.jsp")).thenReturn(dispatcher);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    verify(dispatcher).forward(request, response);
  }

  @Test
  void doGet_forwardsToDetailPage_whenStaffViewsAnyComplaint() throws Exception {
    Complaint complaint = new Complaint();
    complaint.setId(1L);
    complaint.setCustomerId(1L);
    complaint.setStatus(ComplaintStatus.OPEN);
    List<StatusHistory> history = new ArrayList<>();
    List<Attachment> attachments = new ArrayList<>();
    ComplaintDetail complaintDetail = new ComplaintDetail(complaint, "test category", history, attachments);

    when(complaintService.viewComplaintDetail(1L)).thenReturn(Optional.of(complaintDetail));
    when(request.getParameter("id")).thenReturn("1");
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(999L);
    when(session.getAttribute("userRole")).thenReturn("AGENT");
    when(request.getRequestDispatcher("/complaint-detail.jsp")).thenReturn(dispatcher);

    servlet.doGet(request, response);
    verify(request).setAttribute("detail", complaintDetail);
    verify(dispatcher).forward(request, response);
  }

  @Test
  void doGet_forwardsTo403_whenUserViewsNonExistentComplaint() throws Exception {
    when(complaintService.viewComplaintDetail(anyLong())).thenReturn(Optional.empty());
    when(request.getParameter("id")).thenReturn("1");
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(1L);
    when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
    when(request.getRequestDispatcher("/error.jsp")).thenReturn(dispatcher);

    servlet.doGet(request, response);
    verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    verify(dispatcher).forward(request, response);
  }

}
