<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head><title>Login</title></head>
  <body>
    <h1>Customer Login</h1>
    <c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
    </c:if>
    <form action="login" method="post">
      <label>Email: <input type="email" name="email" required></label><br>
      <label>Password: <input type="password" name="password" required></label><br>
      <button type="submit">Login</button>
    </form>
  </body>
</html>


