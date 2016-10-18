<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/pl/common/BINOLPLCOM01.js"></script>

<script type="text/javascript">

function changeRoleAssign(url) {

	// 变量初始化
	oTableArr = new Array(null,null);
	fixedColArr = new Array(null,null);
	// 防止ie下使用layout插件后点左边菜单报js错问题
	$(window).unbind('resize');

	var callback = function(msg){
		$("#roleAssign").html(msg);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: callback
	});
}
</script>

<s:i18n name="i18n.pl.BINOLPLRLA01">
	<div class="panel-header">
	  <div class="clearfix"> 
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span> 
	  <span class="right"> 
	    <s:url action="BINOLPLRLA01_init" id="orgRoleAssign"></s:url>
	    <a class="authority" href="#" onclick="javascript:changeRoleAssign('${orgRoleAssign }');return false;">
          <span class="ui-icon icon-authority"></span><span class="button-text"><s:text name="organization_role_title" /></span>
        </a>
        <s:url action="BINOLPLRLA02_init" id="posRoleAssign"></s:url>
        <a class="authority" href="#" onclick="javascript:changeRoleAssign('${posRoleAssign }');return false;">
          <span class="ui-icon icon-authority"></span><span class="button-text"><s:text name="positionCategory_role_title" /></span>
        </a>
        <s:url action="BINOLPLRLA04_init" id="userRoleAssign"></s:url>
        <a class="authority" href="#" onclick="javascript:changeRoleAssign('${userRoleAssign }');return false;">
          <span class="ui-icon icon-authority"></span><span class="button-text"><s:text name="user_role_title" /></span>
        </a> 
      </span> 
	  </div>
	</div>
	<div id="roleAssign">
		<s:action name="BINOLPLRLA01_init" executeResult="true"></s:action>
	</div>
	<div class="hide" id="dialogInit"></div>
</s:i18n>
