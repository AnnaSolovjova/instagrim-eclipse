

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page import="uk.ac.dundee.computing.aec.instagrim.servlets.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ProfileChange</title>
</head>
<body>
<%
String UserName="guest";
boolean change=false;
LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
if (lg != null) {
 UserName = lg.getUsername();}
String name = (String) request.getAttribute("firstname"); System.out.println(name+"wrong");%>   
<% String surname = (String) request.getAttribute("lastname"); %>

<p>Changing profile for<%=UserName%><p>
  <nav>
            <ul>
                <li class="nav"><a href="/Instagrim/uploadPic.jsp">Upload</a></li>
                <li class="nav"><a href="/Instagrim/Images/majed">Sample Images</a></li>
                <li class="nav"><a href="/Instagrim/LogOut">Logout</a></li>
            </ul>
        </nav>
        
   <% 
         if(!change){
         
            if (name == null) {
        %>
        	<p>Error</p>
        <%
        	} else{
        %>	
         	<form action="/Instagrim/ChangeProfile" method="Post">
         	<input type="hidden" id="thisField1" name="login" value=<%=UserName%>>
       		<p >Name: <input type="text" name="name" class="menu" value=<%=name%>> <p>
        	<p>Surname: <input type="text" name="surname" class="menu" value=<%=surname%>><p>
        	<p>Avarat</p> <input type="file" name="avatar" class="menu" >
        	
        	<input id="change2" type="submit" value="S"><br/>
         	</form>

          <% } %>
            
          <%}else {System.out.println("rabotaet come on");}%>
           
       
</body>
</html>
</body>
</html>