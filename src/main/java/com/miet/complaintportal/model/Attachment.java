package com.miet.complaintportal.model;

import java.time.LocalDateTime;

public class Attachment {
  private long id, complaintId;
  private String filename, contentType;
  private byte[] fileData;
  private LocalDateTime uploadedAt;

  public Attachment(long id, long complaintId, String filename, String contentType, byte[] fileData,
      LocalDateTime uploadedAt) {
    this.id = id;
    this.complaintId = complaintId;
    this.filename = filename;
    this.contentType = contentType;
    this.fileData = fileData;
    this.uploadedAt = uploadedAt;
  }

  public Attachment() {
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

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public byte[] getFileData() {
    return fileData;
  }

  public void setFileData(byte[] fileData) {
    this.fileData = fileData;
  }

  public LocalDateTime getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(LocalDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }
}
