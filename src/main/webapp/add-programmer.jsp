<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Add Programmer</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        form { display: flex; flex-direction: column; max-width: 400px; }
        label { margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="date"], input[type="number"], select { padding: 8px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }
        input[type="submit"] { padding: 10px 15px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        input[type="submit"]:hover { background-color: #0056b3; }
        .back-link { margin-top: 20px; }
        .error { color: #d9534f; background-color: #f2dede; border: 1px solid #ebccd1; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
    </style>
</head>
<body>

    <h1>Add a New Programmer</h1>

    <c:if test="${param.error == 'date'}">
        <p class="error">
            <strong>Date Error:</strong> The programmer's work dates are invalid. Please ensure:
            <ul>
                <li>The start date is not before the project's start date.</li>
                <li>The end date is not after the project's end date.</li>
                <li>The end date is after the start date.</li>
            </ul>
        </p>
    </c:if>

    <form action="${pageContext.request.contextPath}/addProgrammer" method="post">
        <input type="hidden" name="projectId" value="${param.projectId}">

        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" required>

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" required>

        <label for="patronymic">Patronymic:</label>
        <input type="text" id="patronymic" name="patronymic">

        <label for="position">Position:</label>
        <input type="text" id="position" name="position" required>

        <label for="startDate">Start Date on Project:</label>
        <input type="date" id="startDate" name="startDate" required>

        <label for="endDate">End Date on Project:</label>
        <input type="date" id="endDate" name="endDate">

        <label for="hourlyRate">Hourly Rate:</label>
        <input type="number" id="hourlyRate" name="hourlyRate" step="0.01" required>
        
        <label for="fullTime">Employment:</label>
        <select id="fullTime" name="fullTime">
            <option value="true">Full-time</option>
            <option value="false">Part-time</option>
        </select>

        <input type="submit" value="Add Programmer">
    </form>

    <a href="${pageContext.request.contextPath}/programmers?projectId=${param.projectId}" class="back-link">Back to Programmer List</a>

</body>
</html> 