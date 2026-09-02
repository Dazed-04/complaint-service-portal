package com.miet.complaintportal.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private HttpSession session;
  @Mock
  private FilterChain chain;

  private AuthenticationFilter filter;

  @BeforeEach
  void setup() {
    filter = new AuthenticationFilter();
  }

  @Test
  void doFilter_redirectsToLogin_whenNotLoggedIn() throws Exception {
    when(request.getSession(false)).thenReturn(null);
    when(request.getSession(true)).thenReturn(session);
    when(request.getRequestURI()).thenReturn("/complaint-portal/complaints/file");
    when(request.getContextPath()).thenReturn("/complaint-portal");

    filter.doFilter(request, response, chain);

    verify(session).setAttribute(eq("redirectAfterLogin"), anyString());
    verify(response).sendRedirect("/complaint-portal/login");
    verify(chain, never()).doFilter(any(), any());
  }

  @Test
  void doFilter_allowsRequest_whenLoggedIn() throws Exception {
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(1L);
    when(request.getRequestURI()).thenReturn("/complaint-portal/complaints/file");
    when(request.getContextPath()).thenReturn("/complaint-portal");
    when(session.getAttribute("userRole")).thenReturn("CUSTOMER");

    filter.doFilter(request, response, chain);

    verify(chain).doFilter(request, response);
  }

  @Test
  void doFilter_redirectsToLogin_whenSessionExistsButNotUserId() throws Exception {
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(null);
    when(request.getSession(true)).thenReturn(session);
    when(request.getRequestURI()).thenReturn("/complaint-portal/complaints/view");
    when(request.getContextPath()).thenReturn("/complaint-portal");

    filter.doFilter(request, response, chain);

    verify(session).setAttribute(eq("redirectAfterLogin"), anyString());
    verify(response).sendRedirect("/complaint-portal/login");
    verify(chain, never()).doFilter(any(), any());
  }

  @Test
  void doFilter_returns403_whenAgentAccessesAdminPath() throws Exception {
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(1L);
    when(session.getAttribute("userRole")).thenReturn("AGENT");

    when(request.getRequestURI()).thenReturn("/complaint-portal/admin/welcome.jsp");
    when(request.getContextPath()).thenReturn("/complaint-portal");

    filter.doFilter(request, response, chain);

    verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    verify(chain, never()).doFilter(any(), any());
  }

  @Test
  void doFilter_allowsAdmin_onAdminPath() throws Exception {
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(1L);
    when(session.getAttribute("userRole")).thenReturn("ADMIN");

    when(request.getRequestURI()).thenReturn("/complaint-portal/admin/welcome.jsp");
    when(request.getContextPath()).thenReturn("/complaint-portal");

    filter.doFilter(request, response, chain);

    verify(chain).doFilter(request, response);
  }

  @Test
  void doFilter_returns403_whenCustomerAccessesAgentPath() throws Exception {
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(1L);
    when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
    when(request.getRequestURI()).thenReturn("/complaint-portal/agent/assigned");
    when(request.getContextPath()).thenReturn("/complaint-portal");

    filter.doFilter(request, response, chain);

    verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    verify(chain, never()).doFilter(any(), any());
  }

  @Test
  void doFilter_allowsAdmin_onAgentPath() throws Exception {
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("userId")).thenReturn(1L);
    when(session.getAttribute("userRole")).thenReturn("ADMIN");
    when(request.getRequestURI()).thenReturn("/complaint-portal/agent/assigned");
    when(request.getContextPath()).thenReturn("/complaint-portal");

    filter.doFilter(request, response, chain);

    verify(chain).doFilter(request, response);
  }
}
