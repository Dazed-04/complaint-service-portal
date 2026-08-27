<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <!DOCTYPE html>
  <html>

  <head>
    <title>Welcome</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>

  <body>
    <h3>Welcome,
      <c:out value="${sessionScope.userName}" />.
    </h3>
  </body>
  <jsp:include page="/nav.jsp" />

  </html>
