<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Error Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <h1>
      <c:out value="${errorMessage}" />
    </h1>
    <c:choose>
      <c:when test="${sessionScope.userRole == 'AGENT' || sessionScope.userRole == 'ADMIN'}">
        <p><a class="action-link" href="${pageContext.request.contextPath}/agent/assigned">Back to assigned
            complaints</a></p>
      </c:when>
      <c:otherwise>
        <p><a class="action-link" href="${pageContext.request.contextPath}/complaints/view">Back to my complaints</a>
        </p>
      </c:otherwise>
    </c:choose>
  </body>

  </html>
