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
boolean change=false;
LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
if (lg != null) {
 UserName = lg.getUsername();}
String name = (String) request.getAttribute("firstname"); %>   
<% String surname = (String) request.getAttribute("lastname"); %>

<p>Hello <%=UserName%><p>
  <nav>
            <ul>
                <li class="nav"><a href="/Instagrim/uploadPic.jsp">Upload</a></li>
                <li class="nav"><a href="/Instagrim/Images/majed">Sample Images</a></li>
                <li class="nav"><a href="/Instagrim/Logout">Logout</a></li>
            </ul>
        </nav>
        <form method="POST"  action="/Instagrim/Profile/Change/<%=lg.getUsername()%>">
   <% 
         if(!change){
         
            if (name == null) {
        %>
        	<p>No name specified</p>
        <%
        	} else{
        %>
         	
       		<p>Name: <%=name%> <p>
        	<p>Surname: <%=surname%><p><br/>
        	<p>Avarat</p>
        	<input id="change" type="submit" value="Change">
         	</form>

          <% } %>
            
          <%}else {System.out.println("rabotaet come on");}%>
           
       
</body>
</html>