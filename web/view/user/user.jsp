<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>demo 测试</title>
</head>
<body>
<h1>demo 测试</h1><h1>user ggggg---------------测试</h1>
<c:forEach items="${users}" var="user">
	id=${user.id }-------ip=${user.ip }--------state=${user.state }
</c:forEach>
</body>
</html>