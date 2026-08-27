<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>File Complaint</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <p><a class="action-link" href="${pageContext.request.contextPath}/welcome.jsp">&larr; Home</a></p>
    <h1>File a Complaint</h1>
    <c:if test="${param.success == 'true'}">
      <p style="color: green;">Complaint Filed successfully.</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/complaints/file" method="post">
      <div class="form-group">
        <label for="categoryId">Category:</label>
        <select name="categoryId" id="categoryId" required>
          <option value="" disabled selected>-- Select a category --</option>
          <c:forEach var="cat" items="${categories}">
            <option value="${cat.id}">${cat.name}</option>
          </c:forEach>
        </select>
      </div>

      <div class="form-group">
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" required>
      </div>

      <div class="form-group">
        <label for="description">Description:</label>
        <textarea id="description" name="description" required></textarea>
      </div>

      <button type="submit" class="btn">File</button>
    </form>

  </body>

  </html>
