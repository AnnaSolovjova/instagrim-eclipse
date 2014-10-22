<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%String uuid=(String)request.getAttribute("uuid"); %>
<%String login=(String)request.getAttribute("login"); %>

<img src="/Instagrim/Thumb/<%=uuid%>">


</br>
<form action="/Instagrim/ImageUp/<%=uuid%>" method="Get">
<input type="hidden" id="thisField1" name="login" value=<%=login%>>
 <script >
             function myFunction() {
        	 	alert("alert");
				 //var wind=document.getElementById('window');
				 //wind=wind-1;
				 //document.getElementById('wind').value=wind ;
             }
             function myFunction() {
        	 alert("alert2");
		 		//var wind=document.getElementById('window');
		 		//wind=wind-1;
				//document.getElementById('wind').value=wind ;
		 
}
</script><noscript>Sorry, your browser does not support JavaScript!</noscript>
<input type="button" id="minus" name="minus" value="-" onclick="myFunction()">
<input type="text" id="window" name="wind" value="5" disabled>
<input type="button" id="plus" name="plus"  value="+" onchangek="myFunction1()">


<input type="submit" value="Update"> </form>
</br></br></br>
<form action="/Instagrim/ImageDel/<%=uuid%>" method="Get">
<input type="hidden" id="thisField1" name="login" value=<%=login%>> <input type="submit" value="Delete"> </form>

</body>
</html>