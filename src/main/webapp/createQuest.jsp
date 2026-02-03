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
    <title><fmt:message key="quest.create.new"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        .quest-form { background: white; border-radius: 10px; padding: 30px; max-width: 700px; margin: 30px auto; box-shadow: 0 8px 16px rgba(0,0,0,0.2); }
        .form-label { font-weight: bold; color: #333; }
        .btn-primary { background: #667eea; border: none; }
        .btn-primary:hover { background: #764ba2; }
        .help-text { font-size: 12px; color: #666; margin-top: 5px; }
        .code-example { background: #f5f5f5; border-left: 3px solid #667eea; padding: 10px; margin: 10px 0; font-family: monospace; font-size: 12px; }
    </style>
</head>
<body class="container py-5">
<div class="quest-form">
    <h2 style="color: #667eea; margin-bottom: 30px;"><fmt:message key="quest.create.title"/></h2>
    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <form method="post" action="<%= request.getContextPath() %>/quests/create">
        <div class="mb-3">
            <label class="form-label"><fmt:message key="quest.create.ttitle"/></label>
            <input class="form-control" name="title" required />
        </div>
        <div class="mb-3">
            <label class="form-label"><fmt:message key="quest.create.structure"/></label>
            <textarea class="form-control" name="questStructure" rows="10" placeholder="step1|Welcome to the quest|Continue->step2&#10;step2|You see two doors|Left Door->step3|Right Door->step4&#10;step3|You found gold!|END&#10;step4|A trap!|END"></textarea>
            <div class="help-text">
                <strong><fmt:message key="quest.create.format"/></strong> <fmt:message key="quest.create.format.desc"/><br>
                <strong><fmt:message key="quest.create.important"/>:</strong> <fmt:message key="quest.create.important.desc"/><br>
                <strong><fmt:message key="quest.create.ending"/></strong> <fmt:message key="quest.create.ending.desc"/>
            </div>
            <div class="code-example">
                <strong><fmt:message key="quest.create.example"/></strong><br>
                start|You are in a dark cave|Go north->north|Go south->south<br>
                north|You see a dragon!|Fight->fight|Run away->start<br>
                fight|You win!|END<br>
                south|You find a treasure!|END
            </div>
        </div>
        <button class="btn btn-primary"><fmt:message key="button.create"/></button>
        <a class="btn btn-link" href="<%= request.getContextPath() %>/quests"><fmt:message key="button.cancel"/></a>
    </form>
</div>
</body>
</html>
