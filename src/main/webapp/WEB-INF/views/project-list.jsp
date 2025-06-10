<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Projects</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        h1 { color: #333; margin: 0; }
        .user-info { text-align: right; }
        .user-info span { font-weight: bold; }
        .user-info a { margin-left: 15px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        a { color: #007bff; text-decoration: none; }
        a:hover { text-decoration: underline; }
        .add-link { margin-bottom: 20px; display: inline-block; }
    </style>
</head>
<body>
    <div class="header">
        <h1>Project Management</h1>
        <div class="user-info">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <span>Welcome, ${sessionScope.user.username}! (${sessionScope.user.role})</span>
                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/users">User Management</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <c:if test="${sessionScope.user.role == 'MANAGER'}">
        <a href="${pageContext.request.contextPath}/add-project.jsp" class="add-link">Add New Project</a>
    </c:if>

    <table>
        <thead>
            <tr>
                <th><a href="?sort=id&order=${sort eq 'id' ? nextOrder : 'asc'}">ID</a></th>
                <th><a href="?sort=name&order=${sort eq 'name' ? nextOrder : 'asc'}">Name</a></th>
                <th><a href="?sort=customer&order=${sort eq 'customer' ? nextOrder : 'asc'}">Customer</a></th>
                <th><a href="?sort=start_date&order=${sort eq 'start_date' ? nextOrder : 'asc'}">Start Date</a></th>
                <th><a href="?sort=end_date&order=${sort eq 'end_date' ? nextOrder : 'asc'}">End Date</a></th>
                <th>Cost</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${projects}" var="project">
                <tr>
                    <td>${project.id}</td>
                    <td>${project.name}</td>
                    <td>${project.customer}</td>
                    <td>${project.startDate}</td>
                    <td>${project.endDate}</td>
                    <td><fmt:formatNumber value="${project.calculatedCost}" type="currency" currencySymbol="$" /></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/programmers?projectId=${project.id}">View Programmers</a>
                        <c:if test="${sessionScope.user.role == 'MANAGER'}">
                            <a href="${pageContext.request.contextPath}/editProject?projectId=${project.id}" style="margin-left: 10px;">Edit</a>
                            <form action="${pageContext.request.contextPath}/deleteProject" method="post" style="display:inline-block; margin-left: 10px;">
                                <input type="hidden" name="projectId" value="${project.id}">
                                <input type="submit" value="Delete" onclick="return confirm('Are you sure you want to delete this project? This will remove it from all assigned programmers.');">
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html> 