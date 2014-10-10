<%-- 
    Document   : login.jsp
    Created on : Sep 28, 2014, 12:04:14 PM
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
            <h3>Login</h3>
            <form method="POST"  action="Login">
              <div id="Rform">
                <div id="tags">
                <ul>
                    <li>User Name </li>
                    <li>Password </li>
                </ul>
                </div>
                  <div id="inpurW"><input type="text" name="username" class="menu">  <br/>
                <input type="password" name="password" class="menu">  <br/>
                </div>
              </div>
               <div id="button">  <input type="submit" value="Login" id="button"> </div>>
              
            </form>

        </article>
     
    </body>
</html>
