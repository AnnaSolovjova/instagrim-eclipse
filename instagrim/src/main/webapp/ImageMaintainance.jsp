<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.*"%>
    <%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <link rel="stylesheet" type="text/css" href="Styles.css" />
<title>Update picture</title>
</head>
<body>
<%String uuid=(String)request.getAttribute("uuid"); %>
<%String login=(String)request.getAttribute("login"); %>
<%String likes=(String)request.getAttribute("like"); %>


<div id="photoMode">
<div id="image"><img src="/Instagrim/Thumb/<%=uuid%>"></div>
<%//Comment section on the page %>
<div id="comment"><form action="/Instagrim/InputComment/<%=uuid%>" method="Get"> 
<input type="text" id="comm" name="comment" value="input yout text" width=200px height=100px>
<input type="hidden" id="log" name="log" value=<%=login%>> <input type="submit" value="Comment"> </form></div>
 
 
<%//Like section on the page %>
<div id="comment"><form action="/Instagrim/Like/<%=uuid%>" method="Get"> 
<input type="text" id="like" name="like" value=<%=likes%> disabled>
<input type="hidden" id="log" name="log" value=<%=login%>> <input type="submit" value="Like"> </form></div>
 
 <%
         java.util.LinkedList<Comment> lsComment = (java.util.LinkedList<Comment>) request.getAttribute("Comment");
          if (lsComment == null) {
        %>
        <p>No Comments found!</p>
        <%
        } else {
            Iterator<Comment> iterator;
            iterator = lsComment.iterator();
            while (iterator.hasNext()) {
                 Comment c=(Comment)iterator.next();
                 String com=c.getComment();%>
				<p ><%=com%></p><%}}%>


</div>
</br>
<form action="/Instagrim/ImageUp/<%=uuid%>" method="Get">
<input type="hidden" id="thisField1" name="login" value=<%=login%>>
 <script >
             function myFunction() {
        	 	alert("alert");
				 var wind=document.getElementById('window').value;
				 alert(wind);wind=wind-1;alert(wind);
				 document.getElementById('window').value=wind ;
             }
             function myFunction1() {
        	 alert("alert2");
		 		var wind=document.getElementById('window').value;
		 		alert(wind);wind=(wind*1+1);alert(wind);
				document.getElementById('window').value=wind ;
		 
}
             
</script><noscript>Sorry, your browser does not support JavaScript!</noscript>
<input type="button" id="minus" name="minus" value="-" onclick="myFunction()">
<input type="text" id="window" name="wind" value="5" disabled>
<input type="button" id="plus" name="plus"  value="+" onclick="myFunction1()"><br>


<input type="radio" name="sex" value="b'n'w">Black'n'White<br>
<input type="radio" name="sex" value="serpija">Serpia<br>
<input type="radio" name="sex" value="serpija" checked>None<br>
<input type="submit" value="Update"> </form>
</br>

<form action="/Instagrim/ImageDel/<%=uuid%>" method="Get">
<input type="hidden" id="thisField1" name="login" value=<%=login%>> <input type="submit" value="Delete"> </form>

</body>
</html>