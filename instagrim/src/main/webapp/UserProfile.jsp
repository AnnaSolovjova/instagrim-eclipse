<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile</title>
</head>
<body>
<p>Hello user<p>
  <nav>
            <ul>
                <li class="nav"><a href="/Instagrim/uploadPic.jsp">Upload</a></li>
                <li class="nav"><a href="/Instagrim/Images/majed">Sample Images</a></li>
            </ul>
        </nav>
   <%
            String name = (String) request.getAttribute("firstname");
            if (name == null) {
        %>
        <p>No name specified</p>
        <%
        } else{

        %>
        <p>Name <%=name%>" <p>  <%

            }
           
        %>
</body>
</html>