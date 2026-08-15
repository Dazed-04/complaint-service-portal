package com.miet.complaintportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miet.complaintportal.dao.ComplaintDao;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.ComplaintStatus;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceImplTest {

  @Mock
  private ComplaintDao complaintDao;
  private ComplaintService complaintService;

  @BeforeEach
  void setup() {
    complaintService = new ComplaintServiceImpl(complaintDao);
  }

  @Test
  void fileComplaint_savesComplaint() throws Exception {
    when(complaintDao.save(any(Complaint.class))).thenAnswer(invocation -> {
      Complaint complaint = invocation.getArgument(0);
      complaint.setId(1L);
      return complaint;
    });
    Complaint complaint = complaintService.fileComplaint(1L, 1L, "Test", "Complaint for testing purposes.");
    assertNotNull(complaint);
    assertEquals(1L, complaint.getId());
    assertEquals(1L, complaint.getCustomerId());
    assertEquals(1L, complaint.getCategoryId());
    assertEquals("Test", complaint.getTitle());
    assertEquals("Complaint for testing purposes.", complaint.getDescription());

    ArgumentCaptor<Complaint> complaintCaptor = ArgumentCaptor.forClass(Complaint.class);
    verify(complaintDao).save(complaintCaptor.capture());
    Complaint capturedComplaint = complaintCaptor.getValue();
    assertEquals(ComplaintStatus.OPEN, capturedComplaint.getStatus());
  }

  @Test
  void viewComplaints_returnsEmptyList_whenCustomerHasNoComplaints() throws Exception {
    when(complaintDao.findByCustomerId(anyLong())).thenReturn(List.of());
    List<Complaint> result = complaintService.viewComplaints(999L);
    assertTrue(result.isEmpty());
    verify(complaintDao).findByCustomerId(999L);
  }

  @Test
  void viewComplaints_returnsComplaints_whenCustomerHasComplaints() throws Exception {
    Complaint c1 = new Complaint();
    c1.setId(1L);
    c1.setCustomerId(42L);
    c1.setTitle("First complaint");
    Complaint c2 = new Complaint();
    c2.setId(2L);
    c2.setCustomerId(42L);
    c2.setTitle("Second complaint");

    when(complaintDao.findByCustomerId(42L)).thenReturn(List.of(c1, c2));
    List<Complaint> result = complaintService.viewComplaints(42L);
    assertEquals(2, result.size());
    assertTrue(result.contains(c1));
    assertTrue(result.contains(c2));
  }

  @Test
  void updateComplaintStatus_callsUpdateStatus_thenReturnsUpdatedComplaint() throws Exception {
    Complaint updatedComplaint = new Complaint();
    updatedComplaint.setId(1L);
    updatedComplaint.setStatus(ComplaintStatus.IN_PROGRESS);
    when(complaintDao.findById(1L)).thenReturn(Optional.of(updatedComplaint));
    Complaint result = complaintService.updateComplaintStatus(1L, ComplaintStatus.IN_PROGRESS, 1L, "test remark");

    assertEquals(ComplaintStatus.IN_PROGRESS, result.getStatus());
    InOrder inOrder = inOrder(complaintDao);
    inOrder.verify(complaintDao).updateStatus(1L, ComplaintStatus.IN_PROGRESS, 1L, "test remark");
    inOrder.verify(complaintDao).findById(1L);
  }

}
