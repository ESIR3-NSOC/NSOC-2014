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
    <form method="post" class="login">
        <input type="text" placeholder="Username" name="username"><br>
        <input type="password" placeholder="Password" name="password"><br>
        <input type="submit" value="Login" name="login">
        <c:if test="${!empty sessionScope.user}">
                <div class="message info">Vous êtes connecté(e) avec le login : ${sessionScope.user.name}</div>
        </c:if>
        <c:if test="${!empty form.errors}">
                <div class="message error">${form.errors['username']}${form.errors['password']}</div>
        </c:if>
		
        
    </form>
    
    
</body>
</html>