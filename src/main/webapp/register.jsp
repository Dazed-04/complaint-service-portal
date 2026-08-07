<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head><title>Register</title></head>
  <body>
    <h1>Customer registration</h1>
    <c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
    </c:if>
    <form action="register" method="post">
      <label>Name: <input type="text" name="name" required></label><br>
      <label>Email: <input type="email" name="email" required></label><br>
      <label>Password: <input type="password" name="password" required></label><br>
      <button type="submit">Register</button>
    </form>
  </body>
</html>
