package com.miet.complaintportal.service;

import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.miet.complaintportal.dao.OracleUserDao;
import com.miet.complaintportal.dao.UserDao;
import com.miet.complaintportal.exceptions.EmailAlreadyExistsException;
import com.miet.complaintportal.exceptions.InvalidCredentialsException;
import com.miet.complaintportal.model.Role;
import com.miet.complaintportal.model.User;

public class UserServiceImpl implements UserService {
  private final UserDao userDao;

  public UserServiceImpl() {
    this(new OracleUserDao());
  }

  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public User registerUser(String name, String email, String rawPassword, Role role)
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

  @Override
  public User login(String email, String rawPassword) throws InvalidCredentialsException, SQLException {
    User storedUser = userDao.findByEmail(email)
        .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    if (!BCrypt.checkpw(rawPassword, storedUser.getPasswordHash())) {
      throw new InvalidCredentialsException("Invalid email or password");
    }
    return storedUser;
  }
}
