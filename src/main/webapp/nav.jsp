<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <nav>
    <c:choose>
      <c:when test="${empty sessionScope.userId}">
        <a class="action-link" href="${pageContext.request.contextPath}/login">Login</a>
        <a class="action-link" href="${pageContext.request.contextPath}/register">Register</a>
      </c:when>
      <c:otherwise>
        <c:choose>
          <c:when test="${sessionScope.userRole == 'CUSTOMER'}">
            <a class="action-link" href="${pageContext.request.contextPath}/complaints/file">File a Complaint</a>
            <a class="action-link" href="${pageContext.request.contextPath}/complaints/view">My Complaints</a>
          </c:when>
          <c:when test="${sessionScope.userRole == 'AGENT'}">
            <a class="action-link" href="${pageContext.request.contextPath}/agent/assigned">Assigned Complaints</a>
          </c:when>
          <c:when test="${sessionScope.userRole == 'ADMIN'}">
            <a class="action-link" href="${pageContext.request.contextPath}/admin/categories">Manage Categories</a>
            <a class="action-link" href="${pageContext.request.contextPath}/admin/assign">Assign Agents</a>
            <a class="action-link" href="${pageContext.request.contextPath}/admin/manage">Manage Users</a>
          </c:when>
        </c:choose>
        <a class="action-link" href="${pageContext.request.contextPath}/logout">Logout (
          <c:out value="${sessionScope.userName}" />)
        </a>
      </c:otherwise>
    </c:choose>
  </nav>
