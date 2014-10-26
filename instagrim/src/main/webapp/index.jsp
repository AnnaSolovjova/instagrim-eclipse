<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <header>
            <h1>InstaGrim ! </h1>
            <h2>Your world in Black and White</h2>
        </header>
        <nav>
            <ul>

                    <%
                        
                        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                        if (lg != null) {
                            String UserName = lg.getUsername();
                            if (lg.getlogedin()) {
                    %>
				
                <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
                <li><a href="/Instagrim/Profile/<%=lg.getUsername()%>">Your Profile</a></li>
                <li><a href="/Instagrim/Profile/<%=lg.getUsername()%>" id="delete" onclick="deleteUser()">Delete user</a></li>
                <li class="nav"><a href="/Instagrim/LogOut">Logout</a></li>
                <input type=hidden id="user" value=<%=lg.getUsername() %>>
					<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
                <script>

                function deleteUser (user) {
				var user_del=document.getElementById('user').value;alert(user_del);
				  var user = this.name;
			            if (confirm('Are you sure you want to Delete User '+user_del+'?')) {
                    jQuery.ajax({
                        type: "DELETE",
                       // method:"DELETE",
                        url: "http://localhost:8080/Instagrim/Profile/"+user_del,
                        //dataType: "json",
                        //contentType: "application/json; charset=utf-8",
                        //dataType: {"user":user_del},
                        success: function (data, status, jqXHR) {
                             
                             $(location).attr('href',"/Instagrim/login.jsp");
                        },
                    
                        error: function (jqXHR, status,errorThrown) {            
                           
                            alert(errorThrown);
                        
                        }

                    });
			      }
               }
									
								    	
                    
				</script>
                    <%}
                            }else{
                                %>
                 <li><a href="register.jsp">Register</a></li>
                <li><a href="login.jsp">Login</a></li>
                 <li><a href="uploadPic.jsp">Upload</a></li>
                <%
                                        
                            
                    }%>
            </ul>
        </nav>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
                <li>&COPY; Andy C</li>
            </ul>
        </footer>
    </body>
</html>
