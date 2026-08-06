package com.miet.complaintportal.dao;

import java.sql.SQLException;
import java.util.Optional;

import com.miet.complaintportal.model.User;

public interface UserDao {
  User save(User user) throws SQLException;

  Optional<User> findById(long id) throws SQLException;

  Optional<User> findByEmail(String email) throws SQLException;
}
