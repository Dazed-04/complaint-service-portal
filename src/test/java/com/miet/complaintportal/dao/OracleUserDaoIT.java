package com.miet.complaintportal.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.miet.complaintportal.model.User;

class OracleUserDaoIT {

  private UserDao userDao;
  String email;

  User createUser() throws SQLException {
    email = "test_" + UUID.randomUUID() + "@example.com";
    User user = new User();
    user.setName("test");
    user.setEmail(email);
    user.setRole("CUSTOMER");
    user.setPasswordHash("testPasswd");
    return user;
  }

  @BeforeEach
  void setUp() {
    userDao = new OracleUserDao();
  }

  @Test
  void save_thenFindById_returnsMatchingUser() throws Exception {
    User user = createUser();
    User saved = userDao.save(user);
    assertNotEquals(0, saved.getId());

    Optional<User> found = userDao.findById(saved.getId());
    assertTrue(found.isPresent());
    assertEquals(email, found.get().getEmail());
    assertEquals("test", found.get().getName());
    assertEquals("CUSTOMER", found.get().getRole());
  }

  @Test
  void findByEmail_returnsEmpty_whenNoSuchUser() throws Exception {
    String email = UUID.randomUUID() + "random";
    assertEquals(Optional.empty(), userDao.findByEmail(email));
  }
}
