<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        
      


    </head>
    <body>
            
            <%String status = (String) request.getAttribute("status");;
            System.out.println(status);%>
        <header>
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>

        </header>
        <nav>
            <ul>
                
                <li><a href="/Instagrim/Images/majed">Sample Images</a></li>
                <li ><a href="/Instagrim">Home</a></li>
            </ul>
        </nav>
       
        <article>
            <h3>Register as user</h3>
         <form method="POST"  action="Register">
            <div id="Rform">
           <%if (status=="1"){ %><p>Couldn't register the user.</p> <%} %>
                <div id="tags"><ul>
                    <li>User Name </li>
                    <li>Password </li>
                    <li>PasswordRepeat </li>
                    <li>Name </li>
                    <li>Surname</li>
                    <li>email </li>
                    <li>addresses </li>
                
                 </ul></div>
                   <div id="inputW">
                    <input type="text" name="username" class="menu" id="login" onchange="myFunction2()"><br/>
                    <input type="password" name="password" class="menu" id="pass" onchange="myFunction()"> <br/>
                    <input type="password" name="password" class="menu" id="pass2" onchange=myFunction() > <br/>
                    <input type="text" name="name" class="menu" id="3"> <br/>
                    <input type="text" name="surname" class="menu" id="4"> <br/>
                    <input type="text" name="email" class="menu" id="5"> <br/>
                    <input type="text" name="addresses" class="menu" id="6"> <br/>
               	   </div>
          
             </div>
             <script >
             function myFunction() {
				 
				 var password=document.getElementById('pass');
				 if(password.length<5||password.length>10){alert("Password should be 5 to 10 chatacters");
				     }
             }
             function myFunction2() {
			 	
			 	var login=document.getElementById('login');
			 	if(login.length<3||login.length>10){alert("Login should be 3 to 10 chatacters");}
			 
		     }
             function myFunction3() {
        	 	var password=document.getElementById('pass');
        	 	var password2=document.getElementById('pass2');
		 		var login=document.getElementById('login');
		 		if(login.length<3||login.length>10){window.location.href = "register.jsp";}
		 		if(password.length<5||password.length>10){window.location.href = "register.jsp";}
		 		if(!password.eq(password2)){window.location.href = "register.jsp";}
             }
	     
			</script>
            <noscript>Sorry, your browser does not support JavaScript!</noscript>
             </br>
          
			
              </div>    <div id="button"> <input type="submit" value="Register" id="registerB" onclick="myFunction3()" disabled>
             
            </form>

        </article>
        
    </body>
</html>
