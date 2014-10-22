/**
 * 
 */

function check() {
alert("Checking your input");
String password = document.getElementById('2');
alert(password);
//String login = document.getElementById('1');
if(password.length>15||password.length<4){
	alert("your password should be in between 4 and 15 char");
}
//if(login.length>15||login.length<4){
	//alert("your login should be  in between 4 and 15 char");
//}

}
document.getElementById('buttonB').onclick = check;
