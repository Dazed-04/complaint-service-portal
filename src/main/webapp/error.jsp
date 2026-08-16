<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Error Page</title>
  </head>

  <body>
    <h1>
      <c:out value="${errorMessage}" />
    </h1>
    <p><a href="${pageContext.request.contextPath}/complaints/view">Back to my complaints</a></p>
  </body>

  </html>
