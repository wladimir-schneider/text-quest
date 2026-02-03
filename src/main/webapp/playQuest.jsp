<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="pantera.textquest.model.Quest" %>
<%@ page import="pantera.textquest.model.QuestStep" %>
<%@ page import="pantera.textquest.model.User" %>


<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty param.lang}">
    <fmt:setLocale value="${param.lang}" scope="session"/>
</c:if>

<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="play.quest.playq"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }
        .quest-container { background: white; border-radius: 10px; box-shadow: 0 8px 16px rgba(0,0,0,0.2); padding: 30px; margin: 30px auto; max-width: 600px; }
        .quest-title { color: #667eea; font-weight: bold; margin-bottom: 20px; }
        .quest-description { font-size: 16px; line-height: 1.6; margin-bottom: 30px; color: #333; }
        .quest-choice { margin-bottom: 10px; }
        .choice-btn { width: 100%; text-align: left; padding: 12px; border: 2px solid #667eea; background: white; color: #667eea; border-radius: 5px; transition: all 0.3s; }
        .choice-btn:hover { background: #667eea; color: white; }
        .ending-box { background: #f0f0f0; border-left: 4px solid #764ba2; padding: 15px; border-radius: 5px; }
        .nav-btn { margin-top: 20px; }
    </style>
</head>
<body>
<%
    Quest quest = (Quest) request.getAttribute("quest");
    QuestStep currentStep = (QuestStep) request.getAttribute("currentStep");
    String stepId = (String) request.getAttribute("stepId");
    Boolean isEnding = (Boolean) request.getAttribute("isEnding");
    User user = (User) session.getAttribute("currentUser");
%>

<div class="quest-container">
    <h1 class="quest-title"><%= quest.getTitle() %></h1>

    <div class="quest-description">
        <h4><%= currentStep.getDescription() %></h4>
    </div>

    <% if (isEnding != null && isEnding) { %>
    <div class="ending-box">
        <h5><fmt:message key="play.quest.comp"/></h5>
        <p><%= currentStep.getEndingText() %></p>
    </div>
    <div class="nav-btn">
        <a href="<%= request.getContextPath() %>/quests" class="btn btn-primary"><fmt:message key="play.quest.backq"/></a>
    </div>
    <% } else if (currentStep.getChoices() != null && !currentStep.getChoices().isEmpty()) { %>
    <form method="post" action="<%= request.getContextPath() %>/play-quest">
        <input type="hidden" name="questId" value="<%= quest.getId() %>">
        <input type="hidden" name="currentStepId" value="<%= stepId %>">

        <% for (int i = 0; i < currentStep.getChoices().size(); i++) {
            QuestStep.QuestChoice choice = currentStep.getChoices().get(i);
        %>
        <div class="quest-choice">
            <button type="submit" name="choiceIndex" value="<%= i %>" class="choice-btn">
                → <%= choice.getText() %>
            </button>
        </div>
        <% } %>
    </form>
    <% } else { %>
    <div class="alert alert-warning">
        <fmt:message key="play.quest.message"/>
    </div>
    <div class="nav-btn">
        <a href="<%= request.getContextPath() %>/quests" class="btn btn-secondary"><fmt:message key="play.quest.back"/></a>
    </div>
    <% } %>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
