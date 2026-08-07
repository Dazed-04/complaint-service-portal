package com.miet.complaintportal.service;

public class EmailAlreadyExistsException extends Exception {
  public EmailAlreadyExistsException(String message) {
    super(message);
  }
}
