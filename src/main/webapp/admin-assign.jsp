<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Assign Agents</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <p><a class="action-link" href="${pageContext.request.contextPath}/welcome.jsp">&larr; Home</a></p>
    <h1>Assign Agents</h1>
    <c:if test="${not empty errorMessage}">
      <p style="color: red;">
        <c:out value="${errorMessage}" />
      </p>
    </c:if>
    <c:if test="${not empty complaints}">
      <table>
        <colgroup>
          <col style="width: 8%">
          <col style="width: 30%">
          <col style="width: 16%">
          <col style="width: 16%">
          <col style="width: 30%">
        </colgroup>
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
                  <label for="agentId-${c.id}">New Agent:</label>
                  <div class="inline-form">
                    <c:if test="${empty agents}">
                      <p>No agents available</p>
                    </c:if>
                    <select name="agentId" id="agentId-${c.id}" required>
                      <option value="" disabled selected>- Select agent -</option>
                      <c:forEach var="a" items="${agents}">
                        <option value="${a.id}">${a.name}</option>
                      </c:forEach>
                    </select>
                    <button type="submit" class="btn">Assign</button>
                  </div>
                </form>
              </td>
              <td class="actions">
                <a class="btn" href="${pageContext.request.contextPath}/agent/update?id=${c.id}">Update</a>
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
