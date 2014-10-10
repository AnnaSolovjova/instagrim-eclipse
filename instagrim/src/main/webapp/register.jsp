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
  border: 1px solid black;
}
#tags
{

    float: left;
	width:150px;
	 border: 1px solid black;
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
                <div id="tags"><ul>
                    <li>User Name </li>
                    <li>Password </li>
                    <li>Name </li>
                    <li>Surname</li>
                    <li>email </li>
                    <li>addresses </li>
                
                 </ul></div>
                   <div id="inputW">
                    <input type="text" name="username" class="menu"><br/>
                    <input type="password" name="password" class="menu"> <br/>
                    <input type="text" name="name" class="menu"> <br/>
                    <input type="text" name="surname" class="menu"> <br/>
                    <input type="text" name="email" class="menu"> <br/>
                    <input type="text" name="addresses" class="menu"> <br/>
               	   </div>
          
             <br/>  </div>
             <div id="button"> <input type="submit" value="Register"> </div> 
              
            </form>

        </article>
        
    </body>
</html>
