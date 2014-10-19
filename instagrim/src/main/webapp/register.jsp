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
        
        <style>
#Rform
{	
 position: relative;
 top: 20px;	
 overflow: hidden;
}
#tags
{
    float: left;
	width:150px;
}
#inputW
{
    float: left;
	 border: 1px solid black;
}
.menu
{
margin-top:5px;
}
ul
{
margin-top:3px;
line-height: 163%;
}
#button
{
margin-top:50px;
position:relative;
}
</style>


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
                    <li>Name </li>
                    <li>Surname</li>
                    <li>email </li>
                    <li>addresses </li>
                
                 </ul></div>
                   <div id="inputW">
                    <input type="text" name="username" class="menu" id="1" onclick="myFunction(this.value)"><br/>
                      
                    <input type="password" name="password" class="menu" id="2" onchange="myFunction2(this.value)"> <br/>
                    <input type="text" name="name" class="menu" id="3"> <br/>
                    <input type="text" name="surname" class="menu" id="4"> <br/>
                    <input type="text" name="email" class="menu" id="5"> <br/>
                    <input type="text" name="addresses" class="menu" id="6"> <br/>
               	   </div>
          
             <br/>  </div>
             
             <div id="button"> <input type="submit" value="Register" id="registerB">
			 <script src="validPassword.js" type="text/javascript"></script>
            
              </div> 
             
            </form>

        </article>
        
    </body>
</html>
