<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Programmers for Project: ${project.name}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px;}
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        a { color: #007bff; text-decoration: none; }
        a:hover { text-decoration: underline; }
        .nav-links { margin-bottom: 20px; }
        .project-details { background-color: #e7f3fe; padding: 15px; border-radius: 5px; margin-bottom: 20px; }
    </style>
</head>
<body>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/projects">Back to Project List</a>
    </div>

    <h1>Project: ${project.name}</h1>
    <div class="project-details">
        <strong>Customer:</strong> ${project.customer}<br>
        <strong>Start Date:</strong> ${project.startDate}<br>
        <strong>End Date:</strong> ${project.endDate}<br>
        <strong>Calculated Cost:</strong> <fmt:formatNumber value="${project.calculatedCost}" type="currency" currencySymbol="$" />
    </div>

    <h2>Programmers</h2>

    <a href="${pageContext.request.contextPath}/add-programmer.jsp?projectId=${project.id}" class="add-link">Add New Programmer</a>

    <table>
        <thead>
            <tr>
                <th><a href="?projectId=${project.id}&sort=last_name&order=${sort eq 'last_name' ? nextOrder : 'asc'}">Name</a></th>
                <th><a href="?projectId=${project.id}&sort=position&order=${sort eq 'position' ? nextOrder : 'asc'}">Position</a></th>
                <th><a href="?projectId=${project.id}&sort=start_date&order=${sort eq 'start_date' ? nextOrder : 'asc'}">Start Date</a></th>
                <th><a href="?projectId=${project.id}&sort=end_date&order=${sort eq 'end_date' ? nextOrder : 'asc'}">End Date</a></th>
                <th><a href="?projectId=${project.id}&sort=hourly_rate&order=${sort eq 'hourly_rate' ? nextOrder : 'asc'}">Hourly Rate</a></th>
                <th>Full Time</th>
                <th>Calculated Salary</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${programmers}" var="p">
                <tr>
                    <td>${p.lastName} ${p.firstName} ${p.patronymic}</td>
                    <td>${p.position}</td>
                    <td>${p.startDate}</td>
                    <td>${p.endDate}</td>
                    <td><fmt:formatNumber value="${p.hourlyRate}" type="currency" currencySymbol="$" /></td>
                    <td>${p.fullTime ? 'Yes' : 'No'}</td>
                    <td><fmt:formatNumber value="${p.calculatedSalary}" type="currency" currencySymbol="$" /></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/deleteProgrammer" method="post" style="display:inline-block;">
                            <input type="hidden" name="programmerId" value="${p.id}">
                            <input type="hidden" name="projectId" value="${project.id}">
                            <input type="submit" value="Delete" onclick="return confirm('Are you sure you want to delete this programmer?');">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html> 