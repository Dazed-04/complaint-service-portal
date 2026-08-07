package com.miet.complaintportal.service;

import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.miet.complaintportal.dao.OracleUserDao;
import com.miet.complaintportal.dao.UserDao;
import com.miet.complaintportal.model.User;

public class UserServiceImpl implements UserService {
  private final UserDao userDao;

  public UserServiceImpl() {
    this.userDao = new OracleUserDao();
  }

  @Override
  public User registerUser(String name, String email, String rawPassword, String role)
      throws EmailAlreadyExistsException, SQLException {

    if (userDao.findByEmail(email).isPresent()) {
      throw new EmailAlreadyExistsException("Email already registered: " + email);
    }
    String passwordHash = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setPasswordHash(passwordHash);
    user.setRole(role);
    User new_user = userDao.save(user);
    return new_user;

  }
}
