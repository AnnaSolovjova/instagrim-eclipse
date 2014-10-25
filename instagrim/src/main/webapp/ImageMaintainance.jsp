
    <%@page import="java.util.*"%>
    <%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
       

    </head>
<body>
<li class="nav"><a href="/Instagrim/LogOut">Logout</a></li>
<%String uuid=(String)request.getAttribute("uuid"); %>
<%String pageUserName=(String)request.getAttribute("login"); %>
<%String likes=(String)request.getAttribute("like"); %>
<%  String UserName="guest";
		LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
		if (lg != null) {
 		UserName = lg.getUsername();} %>
<div id="photoMode">
<div id="image"><img src="/Instagrim/Thumb/<%=uuid%>"></div>


<%//Comment section on the page %>
<div id="comment"><form action="/Instagrim/InputComment/<%=uuid%>" method="Get"> 
<input type="text" id="comm" name="comment" value="input yout text" width=200px height=100px>
<input type="hidden" id="log" name="log" value=<%=pageUserName%>> <input type="submit" value="Comment"> </form></div>
 
 
<%//Like section on the page %>
<div id="comment"><form action="/Instagrim/Like/<%=uuid%>" method="Get"> 
<input type="text" id="like" name="like" value=<%=likes%> disabled>
<input type="hidden" id="log" name="log" value=<%=pageUserName%>> <input type="submit" value="Like"> </form></div>
 
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
<%if(UserName.equals(pageUserName)) {%>



<form action="/Instagrim/ImageUp/<%=uuid%>" method="Get">
<input type="hidden" id="thisField1" name="login" value=<%=pageUserName%>>
 
 <p>Adjust brightness(5 is current)</p></br>
<input type="button" id="minus" name="minus" value="-" onclick="myFunction()">
<input type="button" id="plus" name="plus"  value="+" onclick="myFunction1()">
<input type="text" id="window" name="darkvalue" value="5" ></br>
<input type="hidden" id="test" name="test" value="5">
 <script >
             function myFunction() {
				 var wind=document.getElementById('window').value;
				 if(wind>0)wind=wind-1;alert(wind);
				 document.getElementById('window').value=wind
				 document.getElementById('test').value=wind;
            	 }
            	function myFunction1() {
		 		var wind=document.getElementById('window').value;
		 		if(wind<10)wind=(wind*1+1);alert(wind);
				document.getElementById('window').value=wind ;
				document.getElementById('test').value=wind
}
             
</script><noscript>Sorry, your browser does not support JavaScript!</noscript>


<input type="radio" name="radio1" value="bnw">Black'n'White<br>
<input type="radio" name="radio1" value="serpia">Serpia<br>
<input type="radio" name="radio1" value="none" checked>None<br>
<input type="submit" value="Update">

 </form>
</br>

<

<form action="/Instagrim/ImageDel/<%=uuid%>" method="Get">
<input type="hidden" id="thisField1" name="login" value=<%=pageUserName%>> <input type="submit" value="Delete"> </form>
<%} %>
</body>
</html>