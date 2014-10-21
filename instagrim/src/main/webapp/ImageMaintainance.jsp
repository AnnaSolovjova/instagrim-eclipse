<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%String uuid=(String)request.getAttribute("uuid"); %>
<%String login=(String)request.getAttribute("login"); %>

<img src="/Instagrim/Thumb/<%=uuid%>">

<form action="/Instagrim/ImageDel/<%=uuid%>" method="Get">
<input type="hidden" id="thisField1" name="login" value=<%=login%>> </br>
<input type="submit" value="Delete"> </form>

<form action="/Instagrim/ImageDel/<%=uuid%>" method="Get">
<input type="hidden" id="thisField1" name="login" value=<%=login%>> </br>
<input type="submit" value="Update"> </form>
</body>
</html>