<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="ct" uri="/cherry-tags"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>WITPOS店务通</title>
</head>
<body>
<%-- 
<s:if test="hasActionErrors()">
<s:actionerror/>
</s:if>
<s:else>
您好，<s:property value="#session.userinfo.loginName"/>，欢迎使用WITPOS店务通CRM系统，请等待管理员的审核！
<a href="https://oauth.taobao.com/logoff?client_id=23034260&view=web">退出</a>
</s:else>
--%>
<ct:form id="mainForm"></ct:form>
<s:a action="initialTop" namespace="/" id="initialTopUrl" cssStyle="display: none;"></s:a>
</body>
<script type="text/javascript">
document.getElementById("mainForm").action=document.getElementById("initialTopUrl").href;
document.getElementById("mainForm").submit();
</script>
</html>