<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Credit Request List</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/creditRequestList.css">
</head>
<body>
<div class="container">
  <h1>Credit Request List</h1>
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

<script src="${pageContext.request.contextPath}/js/creditRequestList.js"></script>
</body>
</html>
