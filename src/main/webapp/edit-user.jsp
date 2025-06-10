<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Edit User</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        form { display: flex; flex-direction: column; max-width: 400px; }
        label { margin-bottom: 5px; font-weight: bold; }
        input[type="text"], select { padding: 8px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }
        input[type="submit"] { padding: 10px 15px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        input[type="submit"]:hover { background-color: #0056b3; }
        .back-link { margin-top: 20px; }
    </style>
</head>
<body>
    <h1>Edit User: ${userToEdit.username}</h1>
    <form action="${pageContext.request.contextPath}/editUser" method="post">
        <input type="hidden" name="userId" value="${userToEdit.id}">

        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="${userToEdit.username}" required>

        <label for="role">Role:</label>
        <select id="role" name="role">
            <option value="USER" ${userToEdit.role == 'USER' ? 'selected' : ''}>User</option>
            <option value="MANAGER" ${userToEdit.role == 'MANAGER' ? 'selected' : ''}>Manager</option>
            <option value="ADMIN" ${userToEdit.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
        </select>
        
        <input type="submit" value="Save Changes">
    </form>
    <a href="${pageContext.request.contextPath}/users" class="back-link">Back to User List</a>
</body>
</html> 