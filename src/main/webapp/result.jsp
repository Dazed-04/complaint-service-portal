<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Registered</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <h1>Registration received</h1>
    <p>Name:
      <c:out value="${sessionScope.registeredName}" />
    </p>
    <p>Email:
      <c:out value="${sessionScope.registeredEmail}" />
    </p>

    <p><a class="action-link" href="${pageContext.request.contextPath}/login">← Go to login</a></p>
  </body>

  </html>
