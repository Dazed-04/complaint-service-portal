package com.miet.complaintportal.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.miet.complaintportal.model.Role;
import com.miet.complaintportal.model.User;

class OracleUserDaoIT {

  private UserDao userDao;
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

  @Test
  void save_thenFindById_returnsMatchingUser() throws Exception {
    User user = createUser();
    User saved = userDao.save(user);
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
  void findAllAgents_returnsOnlyAgents() throws Exception {
    User user = createUser();
    User agent = createUser();
    agent.setRole(Role.AGENT);
    User savedUser = userDao.save(user);
    User savedAgent = userDao.save(agent);
    List<User> agents = userDao.findAllAgents();
    assertTrue(agents.stream().anyMatch(a -> a.getId() == savedAgent.getId()));
    assertTrue(agents.stream().noneMatch(a -> a.getId() == savedUser.getId()));
  }

  @Test
  void findAll_returnsOnlyCustomers() throws Exception {
    User customer = createUser();
    User agent = createUser();
    agent.setRole(Role.AGENT);
    User savedCustomer = userDao.save(customer);
    User savedAgent = userDao.save(agent);
    List<User> customers = userDao.findAllCustomers();
    assertTrue(customers.stream().anyMatch(c -> c.getId() == savedCustomer.getId()));
    assertTrue(customers.stream().noneMatch(c -> c.getId() == savedAgent.getId()));
  }

  @Test
  void updateRole_supportsArbitraryRoleTransitions() throws Exception {
    User customer = createUser();
    User saved = userDao.save(customer);
    userDao.updateRole(saved.getId(), Role.AGENT);
    Optional<User> agent = userDao.findById(saved.getId());
    assertEquals(Role.AGENT, agent.get().getRole());
    userDao.updateRole(saved.getId(), Role.ADMIN);
    Optional<User> admin = userDao.findById(saved.getId());
    assertEquals(Role.ADMIN, admin.get().getRole());
  }
}
