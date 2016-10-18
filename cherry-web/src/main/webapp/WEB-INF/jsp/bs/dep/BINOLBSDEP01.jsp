<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/dep/BINOLBSDEP01.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript">
$(document).ready(function() {

	$.ajaxSetup({ cache: false });
	
} );
</script>

<s:i18n name="i18n.bs.BINOLBSDEP01">
  <div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
    <span class="right">
    	<cherry:show domId="BINOLBSDEP0106">
   		<%-- 实时刷新数据权限URL --%>
		<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init" />
    	<a class="add" href="" onclick="javascript:privilegeRefreshConfirm('${refreshPrivilegeUrl }');return false;"><span class="ui-icon icon-refresh-s"></span><span class="button-text"><s:text name="global.page.privilegeTitle"></s:text></span></a>
   		</cherry:show>
    	<cherry:show domId="BINOLBSDEP0101">
    	<s:url action="BINOLBSDEP04_init" id="addOrganizationInitUrl"></s:url>
    	<a class="add" id="addDepartButton" onclick="bsdep01_addOrganization('${addOrganizationInitUrl}');return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="add_organization"></s:text></span></a>
    	</cherry:show>
    	<s:url action="BINOLBSDEP01_treeInit" id="treeModeInitUrl"></s:url>
    	<s:url action="BINOLBSDEP01_listInit" id="listModeInitUrl"></s:url>
    	<small><s:text name="display_mode"></s:text>:</small>
    	<a class="display display-tree display-tree-on" title='<s:text name="tree_mode"></s:text>' onclick="bsdep01_changeTreeOrTable(this,'${treeModeInitUrl}');return false;"></a><a class="display display-table" title='<s:text name="list_mode"></s:text>' onclick="bsdep01_changeTreeOrTable(this,'${listModeInitUrl}');return false;"></a>
    </span>
    </div>
  </div>
  <div id="treeOrtableId">
  	<s:action name="BINOLBSDEP01_treeInit" executeResult="true"></s:action>
  </div>
  <div style="display: none;">
	<div id="disableTitle"><s:text name="depart.disableTitle" /></div>
	<div id="enableTitle"><s:text name="depart.enableTitle" /></div>
	<div id="disableMessage"><p class="message"><span><s:text name="depart.disableMessage" /></span></p></div>
	<div id="enableMessage"><p class="message"><span><s:text name="depart.enableMessage" /></span></p></div>
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
    <div id="privilegeTitle"><s:text name="global.page.privilegeTitle" /></div>
	<div id="privileMessage"><p class="message"><span><s:text name="global.page.privileMessage" /></span></p></div>
  </div>  
  <div class="hide" id="dialogInit"></div>
</s:i18n>  























 

    

 