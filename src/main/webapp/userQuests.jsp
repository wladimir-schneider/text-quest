<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    java.util.List quests = (java.util.List) request.getAttribute("quests");
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
    <title><fmt:message key="quests.title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }
        .quests-container { background: white; border-radius: 10px; padding: 30px; max-width: 900px; margin: 30px auto; box-shadow: 0 8px 16px rgba(0,0,0,0.2); }
        .quest-card { border: 1px solid #ddd; border-radius: 8px; padding: 15px; margin-bottom: 15px; background: #f9f9f9; transition: all 0.3s; }
        .quest-card:hover { box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3); }
        .quest-title { color: #667eea; font-weight: bold; font-size: 18px; margin-bottom: 10px; }
        .quest-info { color: #666; font-size: 14px; margin-bottom: 10px; }
        .quest-steps { color: #999; font-size: 13px; font-style: italic; }
        .play-btn { background: #667eea; border: none; color: white; padding: 8px 16px; border-radius: 5px; text-decoration: none; display: inline-block; margin-right: 10px; }
        .play-btn:hover { background: #764ba2; color: white; text-decoration: none; }
        .create-btn { background: #28a745; border: none; margin-bottom: 20px; }
        .create-btn:hover { background: #218838; }
    </style>
</head>
<body class="container py-5">
<div class="quests-container">
    <h2 style="color: #667eea; margin-bottom: 30px;"><fmt:message key="quests.title"/></h2>
    <a class="btn create-btn mb-3" href="<%= request.getContextPath() %>/quests/create">+ <fmt:message key="quests.createNew"/></a>

    <% if (quests != null && !quests.isEmpty()) { %>
    <% for (Object _o : quests) {
        pantera.textquest.model.Quest q = (pantera.textquest.model.Quest)_o;
    %>
    <div class="quest-card">
        <div class="quest-title"><%= q.getTitle() %></div>
        <div class="quest-info">
            <strong><fmt:message key="quests.createdBy"/>:</strong> <%= q.getCreator() %><br>
            <strong><fmt:message key="quests.steps"/>:</strong> <%= q.getSteps() != null ? q.getSteps().size() : 0 %>
        </div>
        <div class="quest-steps">
            <fmt:message key="quests.description"/>
        </div>
        <div style="margin-top: 10px;">
            <a class="play-btn" href="<%= request.getContextPath() %>/play-quest?questId=<%= q.getId() %>">▶ <fmt:message key="button.play"/></a>
        </div>
    </div>
    <%  } %>
    <% } else { %>
    <div class="alert alert-info">
        <fmt:message key="quests.none"/>
        <a href="<%= request.getContextPath() %>/quests/create"><fmt:message key="quests.first"/></a>
    </div>
    <% } %>

    <div style="margin-top: 30px;">
        <a class="btn btn-secondary" href="<%= request.getContextPath() %>/">← <fmt:message key="button.backHome"/></a>
    </div>
</div>
</body>
</html>
