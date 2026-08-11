package com.miet.complaintportal.model;

import java.time.LocalDateTime;

public class Complaint {
  private long id, customerId, categoryId;
  private Long agentId;
  private String title, description;
  private ComplaintStatus status;
  private LocalDateTime createdAt, updatedAt;

  public Complaint() {
  }

  public Complaint(long id, long customerId, Long agentId, long categoryId, String title, String description,
      ComplaintStatus status,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.customerId = customerId;
    this.agentId = agentId;
    this.categoryId = categoryId;
    this.title = title;
    this.description = description;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(long customerId) {
    this.customerId = customerId;
  }

  public long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(long categoryId) {
    this.categoryId = categoryId;
  }

  public Long getAgentId() {
    return agentId;
  }

  public void setAgentId(Long agentId) {
    this.agentId = agentId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ComplaintStatus getStatus() {
    return status;
  }

  public void setStatus(ComplaintStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

}
