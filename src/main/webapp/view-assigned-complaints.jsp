<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Assigned Complaints</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <p><a class="action-link" href="${pageContext.request.contextPath}/welcome.jsp">&larr; Home</a></p>
    <h1>Complaints assigned to agent:</h1>
    <c:if test="${empty complaints}">
      <p style="color: red;">No complaints assigned to agent.</p>
    </c:if>
    <table>
      <colgroup>
        <col style="width: 15%">
        <col style="width: 20%">
        <col style="width: 15%">
        <col style="width: 35%">
        <col style="width: 15%">
      </colgroup>
      <thead>
        <tr>
          <th>Complaint Id</th>
          <th>Complaint Title</th>
          <th>Complaint Status</th>
          <th>Complaint Description</th>
          <th>Update Status</th>
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
              <c:out value="${c.description}" />
            </td>
            <td class="actions">
              <a class="btn" href="${pageContext.request.contextPath}/agent/update?id=${c.id}">update</a>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </body>

  </html>
