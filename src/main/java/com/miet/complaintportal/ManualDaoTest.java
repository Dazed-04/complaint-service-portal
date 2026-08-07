package com.miet.complaintportal;

import com.miet.complaintportal.dao.OracleUserDao;
import com.miet.complaintportal.dao.UserDao;
import com.miet.complaintportal.model.User;

public class ManualDaoTest {
  public static void main(String[] args) throws Exception {
    UserDao userDao = new OracleUserDao();
    User newUser = new User();
    newUser.setName("Test User");
    newUser.setEmail("test@example.com");
    newUser.setPasswordHash("not_a_real_hash_yet");
    newUser.setRole("CUSTOMER");

    User saved = userDao.save(newUser);
    System.out.println("Saved user with generated id: " + saved.getId());

    userDao.findById(saved.getId())
        .ifPresentOrElse(
            u -> System.out.println("Found by id: " + u.getEmail()),
            () -> System.out.println("findById Failed - not found"));

    userDao.findByEmail("test@example.com")
        .ifPresentOrElse(u -> System.out.println("Found by email: " + u.getName()),
            () -> System.out.println("findbyEmail Failed - not found"));

    System.exit(0);
  }
}
