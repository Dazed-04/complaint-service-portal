<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Manage Users</title>
  </head>

  <body>
    <h1>Manage Users</h1>
    <c:if test="${not empty errorMessage}">
      <p style="color: red;">
        <c:out value="${errorMessage}" />
      </p>
    </c:if>
    <c:if test="${not empty users}">
      <table border="1" cellpadding="5">
        <thead>
          <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Current Role</th>
            <th>Roles</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="u" items="${users}">
            <tr>
              <td>
                <c:out value="${u.id}" />
              </td>
              <td>
                <c:out value="${u.name}" />
              </td>
              <td>
                <c:out value="${u.role}" />
              </td>
              <td>
                <form action="${pageContext.request.contextPath}/admin/manage" method="post">
                  <input type="hidden" name="userId" value="${u.id}">
                  <label>New Role:
                    <c:if test="${empty roles}">
                      <p>No roles available</p>
                    </c:if>
                    <c:if test="${not empty roles}">
                      <select name="newRole" required>
                        <c:forEach var="r" items="${roles}">
                          <option value="${r}">${r}</option>
                        </c:forEach>
                      </select>
                    </c:if>
                  </label><br>
                  <button type="submit">Change Role</button>
                </form>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:if>
    <c:if test="${empty users}">
      <p>No users available</p>
    </c:if>
  </body>

  </html>
