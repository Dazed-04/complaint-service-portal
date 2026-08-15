<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>File Complaint</title>
  </head>

  <body>
    <h1>File a Complaint</h1>
    <c:if test="${param.success == 'true'}">
      <p style="color: green;">Complaint Filed successfully.</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/complaints/file" method="post">
      <label>Category:
        <select name="categoryId" required>
          <c:forEach var="cat" items="${categories}">
            <option value="${cat.id}">${cat.name}</option>
          </c:forEach>
        </select>
      </label><br>
      <label>Title: <input type="text" name="title" required></label><br>
      <label>Description: <textarea name="description" required></textarea></label><br>
      <button type="submit">File</button>
    </form>
  </body>

  </html>
