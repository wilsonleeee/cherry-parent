<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%@ page import="java.awt.*"%>
<%@ page import="java.awt.image.*" %>
<%@ page import="java.util.*"%>
<%@ page import="javax.imageio.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- pageid=login:特殊的字符串，用来判断此页是登录画面，用于session过期时的跳转，此注释行不可去掉-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">

<script type="text/javascript" src="js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="js/lib/sjcl.js"></script>

<script type="text/javascript">
var submitflag = false;
function login(){
	if(submitflag){
		return false;
	}
	submitflag = true;	

	var p = {adata:'CHERRYAUTH',
	        iter:1000,
	        mode:'ccm',
	        ts:64,
	        ks:128};
    var rp = {};
    var password = $("#txtpsdShow").val();
    var plaintext = '${csrftoken}';
	ct = sjcl.encrypt(password, plaintext, p, rp).replace(/,/g,",\n");
	$("#txtpsd").val(ct);
	$("#txtpsdShow").attr("disabled", true);
	
	$('#mainForm').attr('action',$("#urllogin").html());
	$('#mainForm').submit();	
	$("#txtpsdShow").attr("disabled", false);
}

document.onkeydown = function(e){
	if ($.browser.msie){
	  if(event.keyCode==13){
		  login();
	   }
	}else{
	   if(e.which==13){
		   login();
	   }
	}
};
</script>
 <script type="text/javascript">   
    function changeValidateCode() {   
           //获取当前的时间作为参数，无具体意义   
        var timenow = new Date().getTime();   
           //每次请求需要一个不同的参数，否则可能会返回同样的验证码   
        //这和浏览器的缓存机制有关系，也可以把页面设置为不缓存，这样就不用这个参数了。   
        obj=document.getElementById("checkiamge");        
        obj.src="getCheckImage.action?d="+timenow;   
    }   
</script> 
</head>
<body class="login">
<form id="mainForm" action="" method="post">
<div style="display:none">
	<s:url id="url_login" value="/login" />
	<span id ="urllogin" >${url_login}</span>
</div>
<div class="main container clearfix">
<div style="height:30px"></div>
  <div class="sidebar right">
    <div class="panel ui-corner-all">      
      <div class="panel-content">

			<s:if test="hasActionErrors()">
				<div class="actionError" id="actionResultDiv">
				  	<s:actionerror/>
				</div>
				<div style="height:20px"></div>
			</s:if>
          <p style="margin:0 0 0.8em">
            <label>账号：</label>
            <s:textfield name="txtname"/>
          </p>
          <p style="margin:0 0 0.8em">
            <label>密码：</label>
            <s:password name="txtpsdShow"/>
            <s:hidden name="txtpsd"></s:hidden>
          </p>
          <%
          Object obj = request.getAttribute("validateimage");
          if(obj!=null&&"true".equals(obj.toString()))
          {
          %>
          <div>          
            <div><label><s:text name="validatecode"/></label>
            <s:textfield name="codeText"/>&nbsp;
            <a title="<s:text name='refresh'/>"><img border=1 id="refresh" src="images/refresh.png" onclick="changeValidateCode();" alt=""></a>
            </div>
          <div>
          <label>&nbsp;</label>
            <img border=1 id="checkiamge" src="getCheckImage.action">
               </div>        
          
          </div>
          <%
          }
          %>
          <br />
          <p style="padding-left:80px;">
            <button type="button" id="btnlogin" class="logon" onclick="login();"><span class="button-text">登录</span></button>
          </p>
          <br />              
      </div>
    </div>
  </div>  
</div>
<div class="footer">
 </div> 
</form>
</body>
</html>
