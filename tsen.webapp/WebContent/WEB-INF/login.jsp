<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Login page - TSEN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/public/css/style.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/public/css/login.css" type="text/css">
</head>
<body>


    <div class="body"></div>
    <div class="grad"></div>
    <div class="header">
        <div>TSEN <span>ESIR</span></div>
    </div>
    <br>
    <div class="login">
        <input type="text" placeholder="Username" name="user"><br>
        <input type="password" placeholder="Password" name="password"><br>
        <input type="button" value="Login">
        <c:if test="${!empty sessionScope.user}">
                <div class="message info">Vous êtes connecté(e) avec le login : ${sessionScope.user.username}</div>
        </c:if>
<!--        <div class="message error">wrong password</div>
-->
		<c:if test="${!empty sessionScope.user}">
                <div class="message info">Vous êtes connecté(e) avec le login : ${sessionScope.user.username}</div>
        </c:if>
    </div>
    
    
</body>
</html>