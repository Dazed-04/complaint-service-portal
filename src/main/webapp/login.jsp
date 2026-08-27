<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <h1>Customer Login</h1>
    <c:if test="${not empty errorMessage}">
      <p style="color: red;">${errorMessage}</p>
    </c:if>
    <form action="login" method="post">
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>
      </div>
      <button type="submit" class="btn">Login</button>
    </form>
  </body>

  </html>
