

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page import="uk.ac.dundee.computing.aec.instagrim.servlets.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ProfileChange</title>
</head>
<body>
<%
String UserName="guest";
boolean change=false;
LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
if (lg != null) {
 UserName = lg.getUsername();}
String name = (String) request.getAttribute("firstname"); System.out.println(name+"wrong");
//byte[] avatar =(byte[])request.getAttribute("avatarPic");
 String surname = (String) request.getAttribute("lastname"); 
 String email = (String) request.getAttribute("email"); 
 String pageUserName =(String) request.getAttribute("pageUser");%>

<p>Changing profile for<%=UserName%><p>
  <nav>
            <ul>
                <li class="nav"><a href="/Instagrim/uploadPic.jsp">Upload</a></li>
                <li class="nav"><a href="/Instagrim/Images/majed">Sample Images</a></li>
                <li class="nav"><a href="/Instagrim/LogOut">Logout</a></li>
            </ul>
        </nav>
        
   <% 
         
         
        
        %>
      
	
         	<form enctype="multipart/form-data" action="/Instagrim/Profile/<%=pageUserName%>" method="Post">
         	<input type="hidden" id="thisField1" name="login" value=<%=pageUserName%>>
       		<p >Name: <input type="text" name="name" class="menu" value=<%=name%>> <p>
        	<p>Surname: <input type="text" name="surname" class="menu" value=<%=surname%>><p>
        	<p>Email: <input type="text" name="email" class="menu" value=<%=email%>><p>
        	<input type="hidden" id="thisField2" name="file2" value="1">
        	<p>Avatar</p> <input type="file" id="file" name="avatar" class="menu"  >
        	
        	<input id="change2" type="submit" value="Save"  onclick="myFunction()"><br/>
        	<script>
        	function myFunction() {
        	    alert("alert2");
        	if(document.getElementById('file').value=='') {document.getElementById('thisField2').value="0" }
        	else{ document.getElementById('thisField2').value="1"; alert("alert2");}
        	}
        	</script>
         	</form>

           
         
       
</body>
</html>
</body>
</html>