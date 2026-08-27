package com.miet.complaintportal.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.miet.complaintportal.model.Role;
import com.miet.complaintportal.model.User;

class OracleUserDaoIT {

  private UserDao userDao;
  private final List<Long> createdUserIds = new ArrayList<>();
  String email;

  User createUser() throws SQLException {
    email = "test_" + UUID.randomUUID() + "@example.com";
    User user = new User();
    user.setName("test");
    user.setEmail(email);
    user.setRole(Role.CUSTOMER);
    user.setPasswordHash("testPasswd");
    return user;
  }

  @BeforeEach
  void setUp() {
    userDao = new OracleUserDao();
  }

  @AfterEach
  void cleanup() throws SQLException {
    if (createdUserIds.isEmpty()) {
      return;
    }
    try (Connection conn = new DbConnectionProvider().getConnection();
        Statement stmt = conn.createStatement()) {
      String ids = createdUserIds.stream().map(String::valueOf).collect(Collectors.joining(","));
      stmt.executeUpdate("DELETE FROM users WHERE id IN (" + ids + ")");
    }
    createdUserIds.clear();
  }

  @Test
  void save_thenFindById_returnsMatchingUser() throws Exception {
    User user = createUser();
    User saved = userDao.save(user);
    createdUserIds.add(saved.getId());
    assertNotEquals(0, saved.getId());

    Optional<User> found = userDao.findById(saved.getId());
    assertTrue(found.isPresent());
    assertEquals(email, found.get().getEmail());
    assertEquals("test", found.get().getName());
    assertEquals(Role.CUSTOMER, found.get().getRole());
  }

  @Test
  void findByEmail_returnsEmpty_whenNoSuchUser() throws Exception {
    String email = UUID.randomUUID() + "random";
    assertEquals(Optional.empty(), userDao.findByEmail(email));
  }

  @Test
  void findAllAdmins_returnsOnlyAdmins() throws Exception {
    User user = createUser();
    User admin = createUser();
    admin.setRole(Role.ADMIN);
    User savedUser = userDao.save(user);
    createdUserIds.add(savedUser.getId());
    User savedAgent = userDao.save(admin);
    createdUserIds.add(savedAgent.getId());
    List<User> admins = userDao.findAllAdmins();
    assertTrue(admins.stream().anyMatch(a -> a.getId() == savedAgent.getId()));
    assertTrue(admins.stream().noneMatch(a -> a.getId() == savedUser.getId()));
  }

  @Test
  void findAllAgents_returnsOnlyAgents() throws Exception {
    User user = createUser();
    User agent = createUser();
    agent.setRole(Role.AGENT);
    User savedUser = userDao.save(user);
    createdUserIds.add(savedUser.getId());
    User savedAgent = userDao.save(agent);
    createdUserIds.add(savedAgent.getId());
    List<User> agents = userDao.findAllAgents();
    assertTrue(agents.stream().anyMatch(a -> a.getId() == savedAgent.getId()));
    assertTrue(agents.stream().noneMatch(a -> a.getId() == savedUser.getId()));
  }

  @Test
  void findAllCustomers_returnsOnlyCustomers() throws Exception {
    User customer = createUser();
    User agent = createUser();
    agent.setRole(Role.AGENT);
    User savedCustomer = userDao.save(customer);
    createdUserIds.add(savedCustomer.getId());
    User savedAgent = userDao.save(agent);
    createdUserIds.add(savedAgent.getId());
    List<User> customers = userDao.findAllCustomers();
    assertTrue(customers.stream().anyMatch(c -> c.getId() == savedCustomer.getId()));
    assertTrue(customers.stream().noneMatch(c -> c.getId() == savedAgent.getId()));
  }

  @Test
  void updateRole_supportsArbitraryRoleTransitions() throws Exception {
    User customer = createUser();
    User saved = userDao.save(customer);
    createdUserIds.add(saved.getId());
    userDao.updateRole(saved.getId(), Role.AGENT);
    Optional<User> agent = userDao.findById(saved.getId());
    assertEquals(Role.AGENT, agent.get().getRole());
    userDao.updateRole(saved.getId(), Role.ADMIN);
    Optional<User> admin = userDao.findById(saved.getId());
    assertEquals(Role.ADMIN, admin.get().getRole());
  }
}
