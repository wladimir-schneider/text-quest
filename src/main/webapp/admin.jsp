<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    java.util.List users = (java.util.List) request.getAttribute("users");
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
    <title><fmt:message key="admin.title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-5">
    <h2><fmt:message key="button.role.admin"/> - <fmt:message key="admin.users"/></h2>
    <a class="btn btn-link mb-3" href="<%= request.getContextPath() %>/"><fmt:message key="link.home"/></a>
    <table class="table">
        <thead><tr><th><fmt:message key="admin.uname"/></th><th><fmt:message key="admin.role"/></th><th><fmt:message key="admin.action"/></th></tr></thead>
        <tbody>
        <% if (users != null) {
               for (Object _o : users) {
                   pantera.textquest.model.User u = (pantera.textquest.model.User)_o;
        %>
            <tr>
                <td><%= u.getUsername() %></td>
                <td><%= (u.getRole() != null ? u.getRole().name() : "") %></td>
                <td>
                    <% if (!"admin".equals(u.getUsername())) { %>
                        <form method="post" style="display:inline" action="<%= request.getContextPath() %>/admin">
                            <input type="hidden" name="action" value="delete" />
                            <input type="hidden" name="username" value="<%= u.getUsername() %>" />
                            <button class="btn btn-sm btn-danger"><fmt:message key="admin.del"/></button>
                        </form>
                    <% } %>
                </td>
            </tr>
        <%   }
           }
        %>
        </tbody>
    </table>
</body>
</html>
