package com.miet.complaintportal.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;

import com.miet.complaintportal.model.User;

public class OracleUserDao implements UserDao {

  static {
    try {
      Class.forName("oracle.jdbc.OracleDriver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Oracle JDBC driver not found on classpath", e);
    }
  }

  private final String url;
  private final String user;
  private final String password;

  public OracleUserDao() {
    Properties props = new Properties();
    InputStream inputStream = OracleUserDao.class.getResourceAsStream("/db.properties");
    try {
      props.load(inputStream);
    } catch (IOException io) {
      throw new RuntimeException("Failed to load db.properties", io);
    }
    this.url = props.getProperty("db.url");
    this.user = props.getProperty("db.user");
    this.password = props.getProperty("db.password");
  }

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }

  @Override
  public User save(User user) throws SQLException {
    String query = "INSERT into users (name, email, password_hash, role) VALUES (?, ?, ?, ?)";
    try (Connection conn = getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query, new String[] { "id" })) {
      preparedStatement.setString(1, user.getName());
      preparedStatement.setString(2, user.getEmail());
      preparedStatement.setString(3, user.getPasswordHash());
      preparedStatement.setString(4, user.getRole());

      int rowsInserted = preparedStatement.executeUpdate();
      if (rowsInserted > 0) {
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            long generatedId = generatedKeys.getLong(1);
            if (!generatedKeys.wasNull()) {
              return new User(generatedId, user.getName(), user.getEmail(), user.getPasswordHash(),
                  user.getRole(), user.getCreatedAt());
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public Optional<User> findById(long id) throws SQLException {
    String query = "SELECT * from users WHERE id = ?";
    try (Connection conn = getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query)) {
      preparedStatement.setLong(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          String name = rs.getString("name");
          String email = rs.getString("email");
          String pass_hash = rs.getString("password_hash");
          String role = rs.getString("role");
          LocalDateTime created_at = rs.getObject("created_at", LocalDateTime.class);
          User user = new User(id, name, email, pass_hash, role, created_at);
          return Optional.of(user);
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public Optional<User> findByEmail(String email) throws SQLException {
    String query = "SELECT * from users WHERE email = ?";
    try (Connection conn = getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query)) {
      preparedStatement.setString(1, email);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          long id = rs.getLong("id");
          String name = rs.getString("name");
          String pass_hash = rs.getString("password_hash");
          String role = rs.getString("role");
          LocalDateTime created_at = rs.getObject("created_at", LocalDateTime.class);
          User user = new User(id, name, email, pass_hash, role, created_at);
          return Optional.of(user);
        }
      }
    }
    return Optional.empty();
  }
}
