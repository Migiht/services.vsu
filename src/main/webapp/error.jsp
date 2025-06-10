<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; text-align: center; }
        h1 { color: #d9534f; }
        p { font-size: 1.2em; }
        a { color: #007bff; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <h1>An Unexpected Error Occurred</h1>
    <p>We're sorry, but something went wrong. Please try again later.</p>
    
    <!-- This provides detailed error message for developers during development -->
    <% if (exception != null) { %>
        <hr/>
        <h2>Developer Details:</h2>
        <p><strong>Exception:</strong> <%= exception.getClass().getName() %></p>
        <p><strong>Message:</strong> <%= exception.getMessage() %></p>
        <pre><% exception.printStackTrace(new java.io.PrintWriter(out)); %></pre>
    <% } %>

    <br/><br/>
    <a href="${pageContext.request.contextPath}/projects">Back to Home</a>
</body>
</html> 