<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management</title>
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
        <h1>User Management</h1>
        <div class="user-info">
            <span>Welcome, ${sessionScope.user.username}! (${sessionScope.user.role})</span>
            <a href="${pageContext.request.contextPath}/projects">Back to Projects</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </div>

    <a href="${pageContext.request.contextPath}/add-user.jsp" class="add-link">Add New User</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Role</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${users}" var="u">
                <tr>
                    <td>${u.id}</td>
                    <td>${u.username}</td>
                    <td>${u.role}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/editUser?userId=${u.id}">Edit</a>
                        <%-- Do not allow deleting the currently logged-in user --%>
                        <c:if test="${sessionScope.user.id != u.id}">
                             <form action="${pageContext.request.contextPath}/deleteUser" method="post" style="display:inline-block; margin-left: 10px;">
                                <input type="hidden" name="userId" value="${u.id}">
                                <input type="submit" value="Delete" onclick="return confirm('Are you sure you want to delete this user?');">
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html> 