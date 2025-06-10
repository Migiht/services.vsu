<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Add User</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        form { display: flex; flex-direction: column; max-width: 400px; }
        label { margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="password"], select { padding: 8px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }
        input[type="submit"] { padding: 10px 15px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; }
        input[type="submit"]:hover { background-color: #218838; }
        .back-link { margin-top: 20px; }
        .error { color: #d9534f; margin-bottom: 15px; }
    </style>
</head>
<body>
    <h1>Add New User</h1>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/addUser" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required>
        
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <label for="role">Role:</label>
        <select id="role" name="role">
            <option value="USER">User</option>
            <option value="MANAGER">Manager</option>
            <option value="ADMIN">Admin</option>
        </select>

        <input type="submit" value="Add User">
    </form>
    <a href="${pageContext.request.contextPath}/users" class="back-link">Back to User List</a>
</body>
</html> 