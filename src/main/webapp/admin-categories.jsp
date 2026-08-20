<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Categories</title>
  </head>

  <body>
    <h1>Create new category</h1>
    <c:if test="${not empty errorMessage}">
      <p style="color: red;">
        <c:out value="${errorMessage}" />
      </p>
    </c:if>
    <form action="${pageContext.request.contextPath}/admin/categories" method="post">
      <label>Name: <input type="text" name="name" required></label><br>
      <label>Description: <textarea name="description"></textarea></label><br>
      <button type="submit">Create</button>
    </form>
    <c:if test="${empty categories}">
      <p>No categories created yet.</p>
    </c:if>
    <c:if test="${not empty categories}">
      <table border="1" cellpadding="5">
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
