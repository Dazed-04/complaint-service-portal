package com.miet.complaintportal.service;

import java.sql.SQLException;

import com.miet.complaintportal.exceptions.EmailAlreadyExistsException;
import com.miet.complaintportal.exceptions.InvalidCredentialsException;
import com.miet.complaintportal.model.User;

public interface UserService {
  User registerUser(String name, String email, String rawPassword, String role)
      throws EmailAlreadyExistsException, SQLException;

  User login(String email, String rawPassword) throws InvalidCredentialsException, SQLException;
}
