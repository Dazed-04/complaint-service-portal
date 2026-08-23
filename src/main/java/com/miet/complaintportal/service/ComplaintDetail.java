package com.miet.complaintportal.service;

import java.util.List;

import com.miet.complaintportal.model.Attachment;
import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.StatusHistory;

public class ComplaintDetail {
  private final Complaint complaint;
  private final String categoryName;
  private final List<StatusHistory> history;
  private final List<Attachment> attachments;

  public ComplaintDetail(Complaint complaint, String categoryName, List<StatusHistory> history,
      List<Attachment> attachments) {
    this.complaint = complaint;
    this.categoryName = categoryName;
    this.history = history;
    this.attachments = attachments;
  }

  public Complaint getComplaint() {
    return complaint;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public List<StatusHistory> getHistory() {
    return history;
  }

  public List<Attachment> getAttachments() {
    return attachments;
  }
}
