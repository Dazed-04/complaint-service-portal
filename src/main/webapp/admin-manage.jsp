<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Manage Users</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <p><a class="action-link" href="${pageContext.request.contextPath}/welcome.jsp">&larr; Home</a></p>
    <h1>Manage Users</h1>
    <c:if test="${not empty errorMessage}">
      <p style="color: red;">
        <c:out value="${errorMessage}" />
      </p>
    </c:if>
    <c:if test="${not empty users}">
      <table>
        <colgroup>
          <col style="width: 8%">
          <col style="width: 30%">
          <col style="width: 18%">
          <col style="width: 44%">
        </colgroup>
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
                  <label for="newRole-${u.id}">New Role:</label>
                  <div class="inline-form">
                    <c:if test="${empty roles}">
                      <p>No roles available</p>
                    </c:if>
                    <c:if test="${not empty roles}">
                      <select name="newRole" id="newRole-${u.id}" required>
                        <option value="" disabled selected>-- Select new role --</option>
                        <c:forEach var="r" items="${roles}">
                          <option value="${r}">${r}</option>
                        </c:forEach>
                      </select>
                    </c:if>
                    <button type="submit" class="btn">Change Role</button>
                  </div>
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
