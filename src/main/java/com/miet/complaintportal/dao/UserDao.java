package com.miet.complaintportal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.miet.complaintportal.model.Role;
import com.miet.complaintportal.model.User;

public interface UserDao {
  User save(User user) throws SQLException;

  Optional<User> findById(long id) throws SQLException;

  Optional<User> findByEmail(String email) throws SQLException;

  List<User> findAllAgents() throws SQLException;

  List<User> findAllCustomers() throws SQLException;

  void updateRole(long userId, Role newRole) throws SQLException;
}
