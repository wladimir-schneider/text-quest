<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) request.getAttribute("error");
%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty param.lang}">
    <fmt:setLocale value="${param.lang}" scope="session"/>
</c:if>

<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="login.title"/> - <fmt:message key="app.title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-5">
    <h2><fmt:message key="login.title"/></h2>
    <% if (error != null) { %>
        <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <form method="post" action="<%= request.getContextPath() %>/login">
        <div class="mb-3">
            <label class="form-label"><fmt:message key="login.username"/></label>
            <input class="form-control" name="username" required />
        </div>
        <div class="mb-3">
            <label class="form-label"><fmt:message key="login.password"/></label>
            <input class="form-control" type="password" name="password" required />
        </div>
        <button class="btn btn-primary"><fmt:message key="login.submit"/></button>
        <a class="btn btn-link" href="<%= request.getContextPath() %>/"><fmt:message key="button.cancel"/></a>
    </form>
</body>
</html>
