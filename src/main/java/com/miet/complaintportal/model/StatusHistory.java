package com.miet.complaintportal.model;

import java.time.LocalDateTime;

public class StatusHistory {

  private long id, complaintId, changedBy;
  private ComplaintStatus oldStatus, newStatus;
  private String remark;
  private LocalDateTime changedAt;

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

}
