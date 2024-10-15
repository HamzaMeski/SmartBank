<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Credit Request List</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/creditRequestList.css">
</head>
<body>
<form method="GET" action="${pageContext.request.contextPath}/creditRequest" class="nav-button"><button type="submit">New Credit Simulation</button></form>
<div class="container">

  <form method="GET" action="${pageContext.request.contextPath}/creditRequestList" class="nav-button"><button type="submit">All Credit Requests</button></form>

  <form method="GET" action="${pageContext.request.contextPath}/creditRequestList">
    <input type="date" name="date" required>
    <select name="status" required>
      <option value="PENDING">PENDING</option>
      <option value="APPROVED">APPROVED</option>
      <option value="REJECTED">REJECTED</option>
    </select>
    <button type="submit">Filter</button>
  </form>

  <table class="credit-request-table">
    <thead>
    <tr>
      <th>Request Number</th>
      <th>Name</th>
      <th>Amount</th>
      <th>Status</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="request" items="${creditRequests}">
      <tr>
        <td>${request.requestNumber}</td>
        <td>${request.firstName} ${request.lastName}</td>
        <td>${request.amount}</td>
        <td>${request.currentStatus}</td>
        <td>
          <button onclick="updateStatus('${request.id}')">Update Status</button>
          <button onclick="showDetails('${request.id}')">Details</button>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

<!-- Modal for showing details -->
<div id="detailsModal" class="modal">
  <div class="modal-content">
    <span class="close">&times;</span>
    <h2>Credit Request Details</h2>
    <div id="detailsContent"></div>
  </div>
</div>

<script>
  // Add this before including the JavaScript file
  var contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/creditRequestList.js"></script>
</body>
</html>
