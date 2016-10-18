<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>


<script type="text/javascript">
$(function(){
	if (window.opener) {
       window.opener.lockParentWindow();
    }
});
/*
 * 全局变量定义
 */
var binolbsdep02_global = {};
//是否需要解锁
binolbsdep02_global.needUnlock = true;
window.onbeforeunload = function(){
	if (binolbsdep02_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
function updateOrganization() {
	binolbsdep02_global.needUnlock=false;
	var tokenVal = $('#csrftoken',window.opener.document).val();
	$('#updateOrganization').find("input[name='csrftoken']").val(tokenVal);
	$('#updateOrganization').submit();
}
</script>




<s:i18n name="i18n.bs.BINOLBSDEP01">
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="base_mng"></s:text>&nbsp;&gt;&nbsp;<s:text name="detail_title"></s:text> </span>
  </div>
</div>
<div class="panel-content clearfix">
<s:include value="/WEB-INF/jsp/bs/dep/BINOLBSDEP02_01.jsp"></s:include>
</div>

</div>
</div>
</div>  
</s:i18n>


















      
 