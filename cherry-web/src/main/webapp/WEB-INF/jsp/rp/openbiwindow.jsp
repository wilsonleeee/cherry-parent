<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%> 
<form id="MAINFORM" action="<s:property value='biUrl'/>" method="post">
<input type="hidden" value="" name="redirect">
<input type="hidden" value="LOGON" name="MODE"></input>
<input type="hidden" value="<s:property value='encryptFlag'/>" id="encryptFlag" name="encryptFlag"></input>
<input type="hidden" value="<s:property value='biUrl'/>" id="hidBiUrl"/>
<input type="hidden" value="<s:property value='biUserName'/>" id="login_id" name="userid"/>
<input type="hidden" value="<s:property value='biPassWord'/>" id="login_pass" name="userpwd" />
<script type=text/javascript>
function Encrypt(theText){
	output = new String;
	Temp = new Array();
	Temp2 = new Array();
	TextSize = theText.length;
	for (i = 0; i < TextSize; i++) {
	rnd = Math.round(Math.random() * 122) + 68;
	Temp[i] = theText.charCodeAt(i) + rnd;
	Temp2[i] = rnd;
	}
	for (i = 0; i < TextSize; i++) {
	output += String.fromCharCode(Temp[i], Temp2[i]);
	}
	return output;
} 

if(document.getElementById("encryptFlag").value==='Y'){
document.getElementById("login_pass").value=Encrypt(document.getElementById("login_pass").value);
}
document.getElementById("MAINFORM").submit();
</script>
</form>