<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Assign Agents</title>
  </head>

  <body>
    <h1>Assign Agents</h1>
    <c:if test="${not empty errorMessage}">
      <p style="color: red;">
        <c:out value="${errorMessage}" />
      </p>
    </c:if>
    <c:if test="${not empty complaints}">
      <table border="1" cellpadding="5">
        <thead>
          <tr>
            <th>Id</th>
            <th>Title</th>
            <th>Status</th>
            <th>Current Agent</th>
            <th>Agents</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="c" items="${complaints}">
            <tr>
              <td>
                <c:out value="${c.id}" />
              </td>
              <td>
                <c:out value="${c.title}" />
              </td>
              <td>
                <c:out value="${c.status}" />
              </td>
              <td>
                <c:if test="${c.agentId != null}">
                  <c:out value="${c.agentId}" />
                </c:if>
                <c:if test="${c.agentId == null}">
                  <p>Unassigned</p>
                </c:if>
              </td>
              <td>
                <form action="${pageContext.request.contextPath}/admin/assign" method="post">
                  <input type="hidden" name="complaintId" value="${c.id}">
                  <label>New Agent:
                    <select name="agentId" required>
                      <c:forEach var="a" items="${agents}">
                        <c:if test="{empty agents}">
                          <p>No agents</p>
                        </c:if>
                        <option value="${a.id}">${a.name}</option>
                      </c:forEach>
                    </select>
                  </label><br>
                  <button type="submit">Assign</button>
                </form>
              </td>
              <td>
                <c:if test="${c.agentId != null}">
                  <a href="${pageContext.request.contextPath}/agent/update?id=${c.id}">Update Status</a>
                </c:if>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:if>
    <c:if test="${empty complaints}">
      <p>No complaints available</p>
    </c:if>
  </body>

  </html>
