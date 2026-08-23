<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Complaint Detail</title>
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
    <p><strong>Upload Attachments:</strong></p>
    <form action="${pageContext.request.contextPath}/complaints/attach" method="post" enctype="multipart/form-data">
      <input type="hidden" name="complaintId" value="${detail.complaint.id}">
      <label>Attach files
        <input type="file" name="file">
      </label>
      <button type="submit">Upload</button>
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
          </tr>
        </thead>
        <tbody>
          <c:forEach var="h" items="${detail.history}">
            <tr>
              <td>
                <c:out value="${h.formattedChangedAtDate}" />
              </td>
              <td>
                <c:out value="${h.oldStatus}" />
              </td>
              <td>
                <c:out value="${h.newStatus}" />
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:if>
    <c:if test="${empty detail.attachments}">
      <p>No attachments for this complaint</p>
    </c:if>
    <c:if test="${not empty detail.attachments}">
      <table border="1" cellpadding="5">
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
                <p><a
                    href="${pageContext.request.contextPath}/complaints/download?complaintId=${detail.complaint.id}&attachmentId=${a.id}">download</a>
                </p>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:if>
    <p><a href="${pageContext.request.contextPath}/complaints/view">Back to my complaints</a></p>
  </body>

  </html>
