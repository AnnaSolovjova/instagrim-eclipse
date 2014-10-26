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
       <font color="red">  <p id="notification"></p></font>
            <div id="Rform">

                <div id="tags"><ul>
                    <li>User Name </li>
                    <li>Password </li>
                    <li>PasswordRepeat </li>
                    <li>Name </li>
                    <li>Surname</li>
                    <li>email </li></br>
                    
                    <li>Street</li>
                    <li>City</li>
                    <li>PostCode</li>
                
                 </ul></div>
                   <div id="inputW">
                    <input type="text" name="username" class="menu" id="login" onchange="myFunction2()"><br/>
                    <input type="password" name="password" class="menu" id="pass" onchange="myFunction()"> <br/>
                    <input type="password" name="password2" class="menu" id="pass2" onchange=myFunction3() > <br/>
                    <input type="text" name="name" class="menu" id="3"> <br/>
                    <input type="text" name="surname" class="menu" id="4"> <br/>
                    <input type="text" name="email2" class="menu" id="5"> <br/>
                    <br/>
                    <input type="text" name="street" class="menu" id="6"> <br/>
                    <input type="text" name="city" class="menu" id="7"> <br/>
                    <input type="text" name="post" class="menu" id="9"> <br/>
                    
               	   </div>
          
             </div>
             
             <script >
             function myFunction() {
				 var password=document.getElementById('pass');
				 if(password.value.length<5||password.value.length>10){
				 document.getElementById('notification').innerHTML = 'Password should be 5 to 10 chatacters';
				 
				     }
             }
             function myFunction2() {	
			 	var login=document.getElementById('login');
			 	if(login.value.length<3||login.value.length>10){
			 	document.getElementById('notification').innerHTML = 'Login should be 3 to 10 chatacters';}
			 
		     }
             function myFunction3() {
		 	
		 		var pass=document.getElementById('pass');
		 		var pass2=document.getElementById('pass2');
		 		if(!pass.equals(pass2)){
		 		document.getElementById('notification').innerHTML = 'Passwords are different';}
		 
	     	}
             
             
	     
			</script>
            <noscript>Sorry, your browser does not support JavaScript!</noscript>
             </br>
          
			
              </div>    <div id="button"> <input type="submit" value="Register" id="registerB" ">
             
            </form>

        </article>
        
    </body>
</html>
