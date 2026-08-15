<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Welcome</title>
  </head>

  <body>
    <h3>Welcome,
      <c:out value="${sessionScope.userName}" />.
    </h3>
  </body>

  </html>
