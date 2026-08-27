<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Categories</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <p><a class="action-link" href="${pageContext.request.contextPath}/welcome.jsp">&larr; Home</a></p>
    <h1>Create new category</h1>
    <c:if test="${not empty errorMessage}">
      <p style="color: red;">
        <c:out value="${errorMessage}" />
      </p>
    </c:if>
    <form action="${pageContext.request.contextPath}/admin/categories" method="post">
      <div class="form-group">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" required>
      </div>
      <div class="form-group">
        <label for="description">Description:</label>
        <textarea id="description" name="description"></textarea>
      </div>
      <button type="submit" class="btn">Create</button>
    </form>
    <c:if test="${empty categories}">
      <p>No categories created yet.</p>
    </c:if>
    <c:if test="${not empty categories}">
      <table>
        <colgroup>
          <col style="width: 10%">
          <col style="width: 30%">
          <col style="width: 60%">
        </colgroup>
        <thead>
          <tr>
            <th>S.no</th>
            <th>Category Name</th>
            <th>Description</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="c" items="${categories}" varStatus="loop">
            <tr>
              <td>
                <c:out value="${loop.index + 1}" />
              </td>
              <td>
                <c:out value="${c.name}" />
              </td>
              <td>
                <c:out value="${c.description}" />
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:if>
  </body>

  </html>
