<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="com.cherry.cm.core.CherryException" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.PrintStream" %>
<%@ page import="java.lang.reflect.*" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/print.css" type="text/css" media="print">
<title>出错啦</title>
<script type="text/javascript">
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};
</script>
<style>
.errorMessage {	
	color: #FF3300;	
}
.div1{
border-style:Double;
border-width:0pt; 
border-color:#FF6D06;
width: 70%;
margin:0 auto;
}
.div2{
align:center;
margin-top:5px;
color:red;
}
</style>
</head>
<body style="color:#000000">
<form action="" method="post">
<div id="AJAXSYSERROR"></div>
<div style="height:150px"></div>
<div class="div1">
        <div style="height:50px"></div>
		<div class="header container clearfix" style="width: 50%">
			<h1 class="logo left"><a href="#">WITPOS 店务通后台管理系统</a></h1>			
		</div>
		<div style="height:50px"></div>
		<div align="left">
		</div>
		<div align="left" class="div2">
		<s:text name="global.errorpage.msg1"></s:text>
		<br/>
		<br/>
			<% Object o = session.getAttribute("seriousException"); 
					if(o!=null){						
						
						Exception ex = (Exception)o;
						if(ex instanceof CherryException){
							CherryException temp = (CherryException)ex;			
							out.print(temp.getErrMessage());		
						}else if(ex.getCause() instanceof CherryException){
							CherryException temp = (CherryException)(ex.getCause());			
							out.print(temp.getErrMessage());
						}else if(ex instanceof InvocationTargetException){
							Throwable targetEx = ((InvocationTargetException) ex).getTargetException();
				            if (targetEx != null)
				            {
				            	out.print(targetEx.getMessage());;
				            }
						}else if(ex.getCause() instanceof java.sql.SQLException || ex instanceof java.sql.SQLException){
							//java.sql.SQLException tmp = (java.sql.SQLException)(ex.getCause());
							//if(tmp.getErrorCode()==1205){
								%>
								<s:text name="global.errorpage.msg2"></s:text>
								<%
							//}
						}else{
							out.print(ex.getMessage());
						}
					}
					session.removeAttribute("seriousException");
					
					String statcode = String.valueOf(request.getAttribute("javax.servlet.error.status_code"));
					if("404".equals(statcode)){
						out.print("404:无法匹配到您所请求的页面！");
						out.clear();
						response.sendRedirect("/Cherry/index.html");
					}
					//request.getAttribute("javax.servlet.error.status_code")
				    //request.getAttribute("javax.servlet.error.message")
				    //request.getAttribute("javax.servlet.error.exception_type")
				%>
		</div>
		<div align="center" class="div2">
			
		</div>		
		<div align="center" class="div2">
	
			   
		</div>
		
		<div align="center" style="margin-top:50px">
			
		</div>
		<div align="center" style="margin-top:20px">
			
		</div>

</div>

</form>
</body>
</html>