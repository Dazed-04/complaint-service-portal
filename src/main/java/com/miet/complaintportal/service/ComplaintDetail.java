package com.miet.complaintportal.service;

import java.util.List;

import com.miet.complaintportal.model.Complaint;
import com.miet.complaintportal.model.StatusHistory;

public class ComplaintDetail {
  private final Complaint complaint;
  private final String categoryName;
  private final List<StatusHistory> history;

  public ComplaintDetail(Complaint complaint, String categoryName, List<StatusHistory> history) {
    this.complaint = complaint;
    this.categoryName = categoryName;
    this.history = history;
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
}
