<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Update Complaint Status</title>
  </head>

  <body>
    <h1>
      <c:out value="${detail.complaint.title}" />
    </h1>

    <p><strong>Status:</strong>
      <c:out value="${detail.complaint.status}" />
    </p>
    <p><strong>Category:</strong>
      <c:out value="${detail.categoryName}" />
    </p>
    <p><strong>Description:</strong>
      <c:out value="${detail.complaint.description}" />
    </p>
    <form action="${pageContext.request.contextPath}/agent/update?id=${detail.complaint.id}" method="post">
      <label>New Status:
        <select name="newStatus" required>
          <c:forEach var="s" items="${statuses}">
            <option value="${s}">${s}</option>
          </c:forEach>
        </select>
      </label><br>
      <label>Remark: <textarea name="remark" required></textarea></label><br>
      <button type="submit">Update</button>
    </form>

    <h2>Status History</h2>
    <c:if test="${empty detail.history}">
      <p>No status changes recorded yet.</p>
    </c:if>
    <c:if test="${not empty detail.history}">
      <table border="1" cellpadding="5">
        <thead>
          <tr>
            <th>Changed At</th>
            <th>Old Status</th>
            <th>New Status</th>
            <th>Changed By (user id)</th>
            <th>Remark</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="h" items="${detail.history}">
            <tr>
              <td>
                <c:out value="${h.formattedChangedAt}" />
              </td>
              <td>
                <c:out value="${h.oldStatus}" />
              </td>
              <td>
                <c:out value="${h.newStatus}" />
              </td>
              <td>
                <c:out value="${h.changedBy}" />
              </td>
              <td>
                <c:out value="${h.remark}" />
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:if>
    <p><a href="${pageContext.request.contextPath}/agent/assigned">Back to assigned complaints</a></p>
  </body>

  </html>
