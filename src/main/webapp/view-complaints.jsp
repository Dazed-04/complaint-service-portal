<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>View Complaints</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <p><a class="action-link" href="${pageContext.request.contextPath}/welcome.jsp">&larr; Home</a></p>
    <h1>Complaints registered by user:</h1>
    <c:if test="${empty complaints}">
      <p style="color: red;">No complaints registered by user.</p>
    </c:if>
    <table>
      <colgroup>
        <col style="width: 8%">
        <col style="width: 17%">
        <col style="width: 17%">
        <col style="width: 18%">
        <col style="width: 30%">
        <col style="width: 10%">
      </colgroup>
      <thead>
        <tr>
          <th>S.no</th>
          <th>Complaint Id</th>
          <th>Complaint Title</th>
          <th>Complaint Status</th>
          <th>Complaint Description</th>
          <th>Details</th>
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
            <td class="actions">
              <a class="btn" href="${pageContext.request.contextPath}/complaints/detail?id=${c.id}">View</a>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </body>

  </html>
