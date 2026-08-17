package com.miet.complaintportal.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatusHistory {

  private long id, complaintId, changedBy;
  private ComplaintStatus oldStatus, newStatus;
  private String remark;
  private LocalDateTime changedAt;
  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

  public StatusHistory(long id, long complaintId, long changedBy, ComplaintStatus oldStatus, ComplaintStatus newStatus,
      String remark, LocalDateTime changedAt) {
    this.id = id;
    this.complaintId = complaintId;
    this.changedBy = changedBy;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.remark = remark;
    this.changedAt = changedAt;
  }

  public StatusHistory() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getComplaintId() {
    return complaintId;
  }

  public void setComplaintId(long complaintId) {
    this.complaintId = complaintId;
  }

  public long getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(long changedBy) {
    this.changedBy = changedBy;
  }

  public ComplaintStatus getOldStatus() {
    return oldStatus;
  }

  public void setOldStatus(ComplaintStatus oldStatus) {
    this.oldStatus = oldStatus;
  }

  public ComplaintStatus getNewStatus() {
    return newStatus;
  }

  public void setNewStatus(ComplaintStatus newStatus) {
    this.newStatus = newStatus;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public LocalDateTime getChangedAt() {
    return changedAt;
  }

  public void setChangedAt(LocalDateTime changedAt) {
    this.changedAt = changedAt;
  }

  public String getFormattedChangedAt() {
    return changedAt != null ? changedAt.format(dateTimeFormatter) : null;
  }

  public String getFormattedChangedAtDate() {
    return changedAt != null ? changedAt.format(dateFormatter) : null;
  }
}
