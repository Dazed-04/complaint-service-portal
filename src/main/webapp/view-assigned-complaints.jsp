<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Assigned Complaints</title>
  </head>

  <body>
    <h1>Complaints assigned to agent:</h1>
    <c:if test="${empty complaints}">
      <p style="color: red;">No complaints assigned to agent.</p>
    </c:if>
    <table border="1" cellpadding="5">
      <thead>
        <tr>
          <th>S.no</th>
          <th>Complaint Id</th>
          <th>Complaint Title</th>
          <th>Complaint Status</th>
          <th>Complaint Description</th>
          <th>Update Status</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="c" items="${complaints}" varStatus="loop">
          <tr>
            <td>
              <c:out value="${loop.index + 1}" />
            </td>
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
            <td>
              <a href="${pageContext.request.contextPath}/agent/update?id=${c.id}">update</a>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </body>

  </html>
