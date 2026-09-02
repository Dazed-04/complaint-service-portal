<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Update Complaint Status</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <p><a class="action-link" href="${pageContext.request.contextPath}/welcome.jsp">&larr; Home</a></p>
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
          <option value="" disabled selected>-- Select new status --</option>
          <c:forEach var="s" items="${statuses}">
            <option value="${s}">${s}</option>
          </c:forEach>
        </select>
      </label><br>
      <label>Remark: <textarea name="remark" required></textarea></label><br>
      <button type="submit" class="btn">Update</button>
    </form>

    <h2>Status History</h2>
    <c:if test="${empty detail.history}">
      <p>No status changes recorded yet.</p>
    </c:if>
    <c:if test="${not empty detail.history}">
      <table>
        <colgroup>
          <col style="width: 16%">
          <col style="width: 16%">
          <col style="width: 16%">
          <col style="width: 22%">
          <col style="width: 30%">
        </colgroup>
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
    <h2>Attached Files</h2>
    <c:if test="${empty detail.attachments}">
      <p>No attachments for this complaint</p>
    </c:if>
    <c:if test="${not empty detail.attachments}">
      <table>
        <colgroup>
          <col style="width: 75%">
          <col style="width: 25%">
        </colgroup>
        <thead>
          <tr>
            <th>Filename</th>
            <th>Download</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="a" items="${detail.attachments}">
            <tr>
              <td>
                <c:out value="${a.filename}" />
              </td>
              <td>
                <p><a class="btn"
                    href="${pageContext.request.contextPath}/complaints/download?complaintId=${detail.complaint.id}&attachmentId=${a.id}">download</a>
                </p>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:if>
    <p><a class="action-link" href="${pageContext.request.contextPath}/agent/assigned">Back to assigned complaints</a>
    </p>
  </body>

  </html>
