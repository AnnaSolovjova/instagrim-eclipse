<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page import="uk.ac.dundee.computing.aec.instagrim.servlets.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile</title>
</head>
<body>

<%
String UserName="guest";
LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
if (lg != null) {
 UserName = lg.getUsername();}
String name = (String) request.getAttribute("firstname"); 
//String email = (String) request.getAttribute("email");%>   
<% String surname = (String) request.getAttribute("lastname"); %>
<% Pic lsPics = (Pic) request.getAttribute("Pics");%>
<% String pageUserName =(String) request.getAttribute("pageUser");%>
<p>Hello <%=UserName%></p>
<p>This is <%=pageUserName %> Profile</p>
  <nav>
            <ul>
                <li class="nav"><a href="/Instagrim/uploadPic.jsp">Upload</a></li>
                <li><a href="/Instagrim/Images/<%=pageUserName%>">Images</a></li>
                <li class="nav"><a href="/Instagrim/LogOut">Logout</a></li>
            </ul>
        </nav>
     
 
         	<form action="/Instagrim/Profile/Settings/<%=pageUserName%>" method="Get">
         	<input type="hidden" id="thisField1" name="login" value=<%=pageUserName%>>
         	<p>Avatar</p><p><image src="/Instagrim/Avatar/<%=pageUserName%>" height=120px width=120px></p>
       		<p>Name: <%=name%> </p> <input type="hidden" id="thisField1" name="name" value=<%=name%>>
        	<p>Surname: <%=surname%></p> <input type="hidden" id="thisField2" name="surname" value=<%=surname%>>
        	<p>Email: <%=name%> </p> <input type="hidden" id="thisField3" name="email" value=<%=name%>><br/>
        	<%if(pageUserName.equals(UserName)){%>
            <input type="submit" value="Change Information"> 
       		<%}%>
          
           
       </form>
</body>
</html>