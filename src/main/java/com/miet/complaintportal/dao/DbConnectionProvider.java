package com.miet.complaintportal.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnectionProvider {

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

  public DbConnectionProvider() {
    Properties props = new Properties();
    try (InputStream inputStream = DbConnectionProvider.class.getResourceAsStream("/db.properties")) {
      props.load(inputStream);
    } catch (IOException io) {
      throw new RuntimeException("Failed to load db.properties", io);
    }
    this.url = props.getProperty("db.url");
    this.user = props.getProperty("db.user");
    this.password = props.getProperty("db.password");
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }
}
