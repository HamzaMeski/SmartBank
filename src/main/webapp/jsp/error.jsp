<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Error - Credit Request</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<div class="container">
    <h1>Error in Credit Request</h1>
    <c:if test="${not empty error}">
        <div class="error-message">
            <p>${error}</p>
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error-list">
            <h2>Please correct the following errors:</h2>
            <ul>
                <c:forEach var="error" items="${errors}">
                    <li><strong>${error.key}:</strong> ${error.value}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
    <a href="${pageContext.request.contextPath}/jsp/creditSimulation.jsp" class="btn">Back to Credit Request Form</a>
</div>
</body>
</html>
