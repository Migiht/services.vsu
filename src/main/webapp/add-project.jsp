<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Project</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        form { display: flex; flex-direction: column; max-width: 400px; }
        label { margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="date"] { padding: 8px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }
        input[type="submit"] { padding: 10px 15px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        input[type="submit"]:hover { background-color: #0056b3; }
        .back-link { margin-top: 20px; }
    </style>
</head>
<body>

    <h1>Add a New Project</h1>

    <form action="${pageContext.request.contextPath}/addProject" method="post">
        <label for="name">Project Name:</label>
        <input type="text" id="name" name="name" required>

        <label for="customer">Customer Name:</label>
        <input type="text" id="customer" name="customer" required>

        <label for="startDate">Start Date:</label>
        <input type="date" id="startDate" name="startDate" required>

        <label for="endDate">End Date:</label>
        <input type="date" id="endDate" name="endDate">

        <input type="submit" value="Add Project">
    </form>

    <a href="${pageContext.request.contextPath}/projects" class="back-link">Back to Project List</a>

</body>
</html> 