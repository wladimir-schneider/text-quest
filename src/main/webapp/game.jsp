<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    pantera.textquest.model.User u = (pantera.textquest.model.User) session.getAttribute("currentUser");
    String progress = (String) request.getAttribute("progress");
    String message = "";
    if (progress == null) progress = (u != null ? u.getProgress() : "start");
    if ("start".equals(progress)) {
        message = "Welcome, Stalker. You awaken in the Zone...";
    } else if ("zone".equals(progress)) {
        message = "You stand in the heart of the Zone. Ruins stretch before you. To the east, a shimmering anomaly pulses with strange energy. What do you do?";
    } else if ("ruins".equals(progress)) {
        message = "You explore the decaying ruins. Strange artifacts litter the ground. You hear growls in the distance. The air feels heavy.";
    } else if ("anomaly".equals(progress)) {
        message = "You approach the anomaly. The air crackles. Gravity seems bent here. Artifacts float nearby. One wrong move could be your last.";
    } else if ("artifact_found".equals(progress)) {
        message = "You carefully retrieve a glowing artifact! It hums with energy. This will fetch a good price. Quest Complete!";
    } else if ("mutant_encounter".equals(progress)) {
        message = "A growl freezes your blood. A mutant emerges from the shadows! Fight or flee?";
    } else if ("teleported".equals(progress)) {
        message = "You jump into the anomaly! Reality distorts. You are teleported far away. Disoriented but alive. Quest Complete!";
    } else if ("escaped".equals(progress)) {
        message = "You run from the anomaly zone. Your heart pounds. You made it out. Safe, but empty-handed. Quest Complete.";
    } else if ("fought_mutant".equals(progress)) {
        message = "You fought the mutant! You survived, barely. The Zone respects strength. Quest Complete!";
    } else if ("fled".equals(progress)) {
        message = "You fled the mutant and escaped the ruins. Alive is what matters. Quest Complete.";
    } else {
        message = "You wander the Zone, unsure of your path.";
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Game - Text Quest</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-5">
    <h2>Text Quest</h2>
    <p class="lead"><%= message %></p>
    <div class="mb-3">
        <form method="post" action="<%= request.getContextPath() %>/game">
            <% if ("start".equals(progress)) { %>
                <button class="btn btn-primary" name="action" value="start">Enter the Zone</button>
            <% } else if ("zone".equals(progress)) { %>
                <button class="btn btn-primary me-2" name="action" value="explore_ruins">Explore Ruins</button>
                <button class="btn btn-danger" name="action" value="enter_anomaly">Enter Anomaly</button>
            <% } else if ("ruins".equals(progress)) { %>
                <button class="btn btn-primary me-2" name="action" value="search_artifacts">Search for Artifacts</button>
                <button class="btn btn-warning" name="action" value="danger_zone">Venture Deeper</button>
            <% } else if ("anomaly".equals(progress)) { %>
                <button class="btn btn-danger me-2" name="action" value="jump_anomaly">Jump into Anomaly</button>
                <button class="btn btn-secondary" name="action" value="escape_anomaly">Escape</button>
            <% } else if ("mutant_encounter".equals(progress)) { %>
                <button class="btn btn-danger me-2" name="action" value="fight">Fight</button>
                <button class="btn btn-secondary" name="action" value="flee">Flee</button>
            <% } else if ("artifact_found".equals(progress) || "teleported".equals(progress) || "escaped".equals(progress) || "fought_mutant".equals(progress) || "fled".equals(progress)) { %>
                <span class="badge bg-success">Quest Completed</span>
            <% } %>
            <button class="btn btn-link ms-2" name="action" value="reset">Reset Quest</button>
        </form>
    </div>
    <a class="btn btn-link" href="<%= request.getContextPath() %>/">Home</a>
</body>
</html>
