<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Registered</title>
  </head>

  <body>
    <h1>Registration received</h1>
    <p>Name:
      <c:out value="${sessionScope.registeredName}" />
    </p>
    <p>Email:
      <c:out value="${sessionScope.registeredEmail}" />
    </p>
  </body>

  </html>
