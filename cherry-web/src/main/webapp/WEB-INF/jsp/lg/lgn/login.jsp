<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
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
<!-- 设置缓存时间为60*60*24*7=7天 Pragma是http1.0协议下的参数，Cache-Control属于http1.1 -->
<meta http-equiv="Pragma" content="max-age=604800">
<meta http-equiv="Cache-Control" content="max-age=604800">

<link rel="stylesheet" href="/Cherry/css/common/sunny/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/print.css" type="text/css" media="print">
<link rel="stylesheet" href="/Cherry/css/cherry/cherry.css" type="text/css">
<!--[if lt IE 8]><link rel="stylesheet" href="/Cherry/css/common/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
<!--[if lte IE 8]><link rel="stylesheet" href="/Cherry/css/common/blueprint/ie-corner-fix.css" type="text/css" media="screen, projection"><![endif]-->
<!--[if IE 7]><link rel="stylesheet" href="/Cherry/css/common/blueprint/ie7-fix.css" type="text/css" media="screen, projection"><![endif]-->
<script type="text/javascript" src="/Cherry/js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-1.8.22.custom.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.cluetip.js"></script>
<script type="text/javascript" src="/Cherry/js/common/IEcssrepair.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/sjcl.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherry.js"></script>
<script type="text/javascript" src="/Cherry/js/common/commonAjax.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.cluetip.js"></script>
<script type="text/javascript" src="/Cherry/plugins/jquery-validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/Cherry/plugins/jquery-validate/additional-methods.js"></script>
<script type="text/javascript">
var commitFlag = false;
function login(){
	$('#actionResultDiv').hide();
    if($.trim($("#txtname").val())=="" || $.trim($("#txtpsdShow").val())==""){
        $("#errorDiv2 #errorSpan2").html($("#errmsg_ECM00039").val());
        $("#errorDiv2").attr("style","");
        return false;
    }
    if($("#codeText").length>0 && $.trim($("#codeText").val())==""){
        $("#errorDiv2 #errorSpan2").html($("#errmsg_ECM00040").val());
        $("#errorDiv2").attr("style","");
        return false;
    }
	if(commitFlag){
		return false;
	}
	commitFlag = true;	
	$('#mainForm').attr('action',$("#urllogin").html()+"?MENU_ID=LG");


	var p = {adata:'CHERRYAUTH',
	        iter:1000,
	        mode:'ccm',
	        ts:64,
	        ks:128};
    var rp = {};
    var password = $("#txtpsdShow").val();
    var plaintext = $("#csrftoken").val();
	var ct = sjcl.encrypt(password, plaintext, p, rp).replace(/,/g,",\n");
	$("#txtpsd").val(ct);
	$("#txtpsdShow").attr("disabled",true);
	
	$('#mainForm').submit();	
	$("#txtpsdShow").attr("disabled",false);
}

function getLonginPassword(obj){
	var dialogSetting = {
		dialogInit: "#getPwdDialogInit",
		width: 500,
		height: 220,
		title: $("#getPwdDialogTitle").text()
	};
	openDialog(dialogSetting);
	var url = $(obj).attr("href");
	var callback = function(msg) {
		$("#getPwdDialogInit").html(msg);
	};
	
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: callback
	});
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
 //alert(document.documentMode);
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
<cherry:form id="mainForm" action="" method="post">	
<s:i18n name="i18n.lg.login">
<div class="hide">
<s:url id="url_login" value="/login" />
<span id="urllogin" >${url_login}</span>
<s:url id="getLonginPasswordURL" action="BINOLCTCOM09_init" namespace="/ct" />
</div>
<div class="main container clearfix" id="div_main">
	     <s:url id="chinese" value="init.action">
			<s:param name ="request_locale" value="@java.util.Locale@CHINA" />
		</s:url>
		<s:url id="english" value="init.action">
			<s:param name ="request_locale" value="@java.util.Locale@US" />
		</s:url>
		<s:url id="taiwan" value="init.action">
			<s:param name ="request_locale" value="@java.util.Locale@TAIWAN" />
		</s:url>
		
<div style="height:30px">
<span style="float:right;margin:5px 10px 0 0;color:#7f8690;"><s:a href="%{#chinese}"><s:text name="chinese"/></s:a>│<s:a href="%{#taiwan}"><s:text name="taiwan"/></s:a></span>
</div>
  <div class="sidebar right">
    <div class="panel ui-corner-all">
      <div class="panel-header">
      <div style="height:8px"></div>
      <strong><s:text name="lblTitle"/></strong></div>
      <div class="panel-content">
	      <div id="actionResultDisplay"></div>
	      <div id="errorDiv2" class="actionError" style="display:none">
			      <ul>
			         <li><span id="errorSpan2"></span></li>
			      </ul>			
	      </div>
			<s:if test="hasActionErrors()">
				<div class="actionError" id="actionResultDiv">
				  	<s:actionerror/>
				</div>
				<div style="height:20px"></div>
			</s:if>
		 <p style="margin:0 0 0.8em">
          </p>
            <%
            String rememberflag=request.getAttribute("rememberflag")==null?"":request.getAttribute("rememberflag").toString();
    		String rememberusername=request.getAttribute("rememberusername")==null?"":request.getAttribute("rememberusername").toString();
    		String rememberpassword=request.getAttribute("rememberpassword")==null?"":request.getAttribute("rememberpassword").toString();
          %>
          <p style="margin:0 0 0.8em">
            <label><s:text name="username" /></label>
            <input name="txtname" type="text" maxlength="30" id="txtname" class="input" value="<%= rememberusername %>"/>
            <s:hidden name="browser"></s:hidden>
          </p>
          <p style="margin:0 0 0.8em">
            <label><s:text name="password"/></label>
            <input name="txtpsdShow" type="password" id="txtpsdShow" maxlength="30" class="input" value="<%= rememberpassword %>"/>
            <s:hidden name="txtpsd"></s:hidden>
          <%
          String retrievePwdFlag=request.getAttribute("retrievePwdFlag")==null?"":request.getAttribute("retrievePwdFlag").toString();
          if(retrievePwdFlag.equals("true")){
          %>
            <a id="getPasswordButton" href="${getLonginPasswordURL}" onclick="getLonginPassword(this);return false;">
				<s:text name="forgetPassword"/>
			</a>
		  <%}%>
          </p>
          <p style="margin:0 0 0.8em">
          <label>&nbsp;&nbsp;</label>
         <%
         if(rememberflag.equals("1")){
         %>
         <input id="chkRemember" class="checkbox" type="checkbox" name="chkRemember" value="1" checked/>
         <%}else{ %>
         <input id="chkRemember" class="checkbox" type="checkbox" name="chkRemember" value="1" />
          <%} %>           
            <s:text name="txtchkRemember"/>
          </p>
          <%
          Object obj = request.getAttribute("validateimage");
          if(obj!=null&&"true".equals(obj.toString()))
          {
          %>
          <div>          
            <div><label><s:text name="validatecode"/></label>
            <s:textfield name="codeText" maxlength="10"/>&nbsp;
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
          <p>
            <button type="button" id="btnlogin" class="logon" onclick="login();"><span class="ui-icon icon-login"></span><span class="button-text">登录</span></button>
          </p>
          <br />              
      </div>
    </div>
  </div>
  <div class="maincontent">
    <div>
      <h1><a><s:text name="lblText1"/></a></h1>
      <p> <strong><s:text name="lblText2"/></strong> </p>
      <p><s:text name="lblText3"/></p>
      <p class="clearfix"><span class="ui_icon_bg icon-statistics"></span><span><a><strong><s:text name="lblText4"/></strong></a><br />
      <s:text name="lblText5"/></span></p>
      <p class="clearfix"><span class="ui_icon_bg icon-customers"></span><span><a><strong><s:text name="lblText6"/></strong></a><br />
       <s:text name="lblText7"/></span> </p>
      <p class="clearfix"><span class="ui_icon_bg icon-contact"></span><span><a><strong><s:text name="lblText8"/></strong></a><br />
        <s:text name="lblText9"/></span></p>
    </div>
  </div>
<div class="hide" id="getPwdDialogInit"></div>
<div class="hide" id="getPwdDialogTitle"><s:text name="getPassword"/></div>
</div>
<div class="footer">
 <jsp:include page="/WEB-INF/jsp/common/foot.inc.jsp" flush="true" />
 </div>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_ECM00039" value='<s:text name="ECM00039"/>'/>
    <input type="hidden" id="errmsg_ECM00040" value='<s:text name="ECM00040"/>'/>
</div>
 	</s:i18n> 
 	<%
 			String logoutFlag=request.getParameter("logoutFlag");
 		
 	%>
        <input type="hidden" id="logoutFlag" value='<%= logoutFlag %>' />
     <% %>
          
</cherry:form>
</body>
<script type="text/javascript">
if($('#txtname').val()!="" && $('#logoutFlag').val()!="logoutFlag"){
	login();
}
</script>
</html>
