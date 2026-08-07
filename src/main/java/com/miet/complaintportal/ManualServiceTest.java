package com.miet.complaintportal;

import com.miet.complaintportal.model.User;
import com.miet.complaintportal.service.EmailAlreadyExistsException;
import com.miet.complaintportal.service.UserService;
import com.miet.complaintportal.service.UserServiceImpl;

public class ManualServiceTest {
  public static void main(String[] args) throws Exception {
    UserService userService = new UserServiceImpl();
    String name = "Test Service User";
    String email = "test_service@example.com";
    String rawPassword = "should_be_hash_now";
    String role = "CUSTOMER";

    try {
      User saved = userService.registerUser(name, email, rawPassword, role);
      System.out.println("Saved user with generated id: " + saved.getId());
    } catch (EmailAlreadyExistsException e) {
      System.out.println("Unexpected: " + e.getMessage());
    }

    try {
      userService.registerUser(name, email, rawPassword, role);
      System.out.println("BUG: duplicate registration was allowed!");
    } catch (EmailAlreadyExistsException e) {
      System.out.println("Correctly rejected duplicate: " + e.getMessage());
    }
    System.exit(0);
  }
}
