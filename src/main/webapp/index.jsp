<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Object _user = session.getAttribute("currentUser");
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
    <title>Text Quest</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-5">
    <h1><fmt:message key="index.welcome"/></h1>
    
    <div>
        <div style="text-align: right; margin-bottom: 10px;">
            <a href="?lang=en">🇬🇧 English</a> |
            <a href="?lang=de">🇩🇪 Deutsch</a> |
            <a href="?lang=ru">🇷🇺 Русский</a>
        </div>
        <% if (_user != null) {
               pantera.textquest.model.User u = (pantera.textquest.model.User)_user;
        %>
            <p><fmt:message key="index.hello"/>, <strong><%= u.getUsername() %></strong>!</p>
            <a class="btn btn-primary me-2" href="<%= request.getContextPath() %>/game"><fmt:message key="button.play"/></a>
            <a class="btn btn-secondary me-2" href="<%= request.getContextPath() %>/logout"><fmt:message key="button.logout"/></a>
            <a class="btn btn-outline-primary me-2" href="<%= request.getContextPath() %>/quests"><fmt:message key="quests.title"/></a>
            <% if (u.getRole() != null && "ADMIN".equals(u.getRole().name())) { %>
                <a class="btn btn-danger" href="<%= request.getContextPath() %>/admin"><fmt:message key="button.role.admin"/></a>
            <% } %>
        <% } else { %>
            <a class="btn btn-primary me-2" href="<%= request.getContextPath() %>/register"><fmt:message key="button.register"/></a>
            <a class="btn btn-secondary" href="<%= request.getContextPath() %>/login"><fmt:message key="button.login"/></a>
        <% } %>
    </div>
</body>
</html>