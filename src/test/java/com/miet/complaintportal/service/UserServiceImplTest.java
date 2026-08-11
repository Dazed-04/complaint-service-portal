package com.miet.complaintportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miet.complaintportal.dao.UserDao;
import com.miet.complaintportal.exceptions.EmailAlreadyExistsException;
import com.miet.complaintportal.exceptions.InvalidCredentialsException;
import com.miet.complaintportal.model.User;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserDao userDao; // a fake UserDao — Mockito generates this automatically

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserServiceImpl(userDao);
  }

  @Test
  void registerUser_savesNewUser_whenEmailNotTaken() throws Exception {
    String rawPassword = "myPlainPassword123";
    when(userDao.findByEmail("new@email.com")).thenReturn(Optional.empty());
    when(userDao.save(any(User.class))).thenAnswer(invocation -> {
      User passedUser = invocation.getArgument(0);
      passedUser.setId(1L);
      return passedUser;
    });
    User result = userService.registerUser("Test User", "new@email.com", rawPassword, "CUSTOMER");

    assertNotNull(result);
    assertEquals("Test User", result.getName());
    assertEquals("new@email.com", result.getEmail());
    assertEquals(1L, result.getId());

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userDao).save(userCaptor.capture());

    User capturedUser = userCaptor.getValue();
    assertNotEquals(rawPassword, capturedUser.getPasswordHash());

    assertTrue(BCrypt.checkpw(rawPassword, capturedUser.getPasswordHash()));
  }

  @Test
  void registerUser_throwsException_whenEmailAlreadyExists() throws Exception {
    when(userDao.findByEmail(anyString())).thenAnswer(invocation -> {
      User exists = new User(1L, "test", invocation.getArgument(0), "unhashed", "CUSTOMER", LocalDateTime.now());
      return Optional.of(exists);
    });

    assertThrows(EmailAlreadyExistsException.class, () -> {
      userService.registerUser("Existing User", "taken@example.com", "somePassword", "CUSTOMER");
    });
    verify(userDao, never()).save(any());
  }

  @Test
  void login_succeeds_withCorrectCredentials() throws Exception {
    String passHash = BCrypt.hashpw("1234", BCrypt.gensalt());
    when(userDao.findByEmail("test@email.com")).thenAnswer(invocation -> {
      User exists = new User(1L, "test", invocation.getArgument(0), passHash, "CUSTOMER",
          LocalDateTime.now());
      return Optional.of(exists);
    });
    User exists = userService.login("test@email.com", "1234");
    assertEquals("test", exists.getName());
    assertEquals(passHash, exists.getPasswordHash());
  }

  @Test
  void login_throwsException_withWrongPassword() throws Exception {
    String passHash = BCrypt.hashpw("1234", BCrypt.gensalt());
    when(userDao.findByEmail("test@email.com")).thenAnswer(invocation -> {
      User exists = new User(1L, "test", invocation.getArgument(0), passHash, "CUSTOMER",
          LocalDateTime.now());
      return Optional.of(exists);
    });
    assertThrows(InvalidCredentialsException.class, () -> {
      userService.login("test@email.com", "wrongPass");
    });
  }

  @Test
  void login_throwsException_withNoneexistentEmail() throws Exception {
    when(userDao.findByEmail("test@email.com")).thenReturn(Optional.empty());
    assertThrows(InvalidCredentialsException.class, () -> {
      userService.login("test@email.com", "1234");
    });
  }

}
