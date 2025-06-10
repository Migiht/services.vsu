<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Edit Programmer</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        form { display: flex; flex-direction: column; max-width: 400px; }
        label { margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="date"], input[type="number"], select { padding: 8px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }
        input[type="submit"] { padding: 10px 15px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        input[type="submit"]:hover { background-color: #0056b3; }
        .back-link { margin-top: 20px; }
    </style>
</head>
<body>

    <h1>Edit Programmer</h1>

    <form action="${pageContext.request.contextPath}/editProgrammer" method="post">
        <input type="hidden" name="programmerId" value="${programmer.id}">
        <input type="hidden" name="projectId" value="${programmer.projectId}">

        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" value="${programmer.firstName}" required>

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" value="${programmer.lastName}" required>

        <label for="patronymic">Patronymic:</label>
        <input type="text" id="patronymic" name="patronymic" value="${programmer.patronymic}">

        <label for="position">Position:</label>
        <input type="text" id="position" name="position" value="${programmer.position}" required>

        <label for="startDate">Start Date on Project:</label>
        <input type="date" id="startDate" name="startDate" value="${programmer.startDate}" required>

        <label for="endDate">End Date on Project:</label>
        <input type="date" id="endDate" name="endDate" value="${programmer.endDate}">

        <label for="hourlyRate">Hourly Rate:</label>
        <input type="number" id="hourlyRate" name="hourlyRate" step="0.01" value="${programmer.hourlyRate}" required>
        
        <label for="fullTime">Employment:</label>
        <select id="fullTime" name="fullTime">
            <option value="true" ${programmer.fullTime ? 'selected' : ''}>Full-time</option>
            <option value="false" ${!programmer.fullTime ? 'selected' : ''}>Part-time</option>
        </select>

        <input type="submit" value="Save Changes">
    </form>

    <a href="${pageContext.request.contextPath}/programmers?projectId=${programmer.projectId}" class="back-link">Back to Programmer List</a>

</body>
</html> 