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
<script src="mouseclick.js"></script>
<%
String UserName="guest";
boolean change=false;
LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
if (lg != null) {
 UserName = lg.getUsername();}
String name = (String) request.getAttribute("firstname"); %>   
<% String surname = (String) request.getAttribute("lastname"); %>
<% %>

<p>Hello <%=UserName%><p>
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
        	<p>No name specified</p>
        <%
        	} else{
        %>
         	<form action="/Instagrim/ChangeProfile" method="Get">
         	<input type="hidden" id="thisField1" name="login" value=<%=UserName%>>
       		<p>Name: <%=name%> <p><input type="hidden" id="thisField1" name="name" value=<%=name%>>
        	<p">Surname: <%=surname%><p><input type="hidden" id="thisField2" name="surname" value=<%=surname%>><br/>
        	<p>Avarat</p><%java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
            if (lsPics == null) {
       		 %>
       		 <p>No Pictures found!</p>
      		  <%
        } else {
            Iterator<Pic> iterator;
            iterator = lsPics.iterator();
            while (iterator.hasNext()) {
                Pic p = (Pic) iterator.next();

        %>
        <a href="/Instagrim/Image/<%=p.getSUUID()%>" ><img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a><br/><%

            }
        	
          } }%>
            <input type="submit" value="ChangeInfo"> 
          <%}else {System.out.println("rabotaet come on");}%>
           
       </form>
</body>
</html>