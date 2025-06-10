<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
    <style>
        body { font-family: Arial, sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f5f5f5; }
        .login-container { background-color: white; padding: 40px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
        h1 { text-align: center; color: #333; }
        form { display: flex; flex-direction: column; }
        label { font-weight: bold; margin-bottom: 5px; }
        input[type="text"], input[type="password"] { padding: 10px; margin-bottom: 20px; border: 1px solid #ddd; border-radius: 4px; }
        input[type="submit"] { padding: 10px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        input[type="submit"]:hover { background-color: #0056b3; }
        .error { color: #d9534f; text-align: center; margin-bottom: 15px; }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>Welcome</h1>
        <c:if test="${param.error != null}">
            <p class="error">Invalid username or password.</p>
        </c:if>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            <input type="submit" value="Login">
        </form>
    </div>
</body>
</html> 