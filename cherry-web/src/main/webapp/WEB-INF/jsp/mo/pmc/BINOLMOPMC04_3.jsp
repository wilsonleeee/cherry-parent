<!-- 新增POS菜单 -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/mo/pmc/BINOLMOPMC04_3.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>

<script type="text/javascript">

$().ready(function() {
	
	cherryValidate({
		formId: 'addMenuForm',	
		rules: {
			menuCode: {required: true,maxlength: 50},//菜单code
			brandMenuNameCN:{required: true,maxlength: 50},// nvarchar(50)
			brandMenuNameEN:{maxlength: 50},
			menuType: {required: true,byteLengthValid: [50]},
			container: {byteLengthValid: [200]}, // 字节数最大为200（一个中文两个字节）
			menuValue: {byteLengthValid: [50]}, // varchar(50)
			menuLink: {maxlength: 255},
			comment: {maxlength: 50},
			machineType: {required:true}
		}	
	});
});

</script>

<s:i18n name="i18n.mo.BINOLMOPMC04">
<!-- 保存对菜单的新增-->
<s:url id="saveAddMenu_url" action="BINOLMOPMC04_saveAddMenu"></s:url>
<s:url id="popParentMenuUrl" action="BINOLMOPMC04_popParentMenu"></s:url>
<div class="hide">
	<a id="saveAddMenuUrl" href="${saveAddMenu_url}"></a>
	<span id="PopParentMenuTitle"><s:text name="PMC04_posMenuInfo"/></span><%--POS菜单信息 --%>
</div>
<div class="main container clearfix">
<div class="panel ui-corner-all">
	  <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="PMC04_title"/> &gt; <s:text name="PMC04_addPosMenu"/>
	       </span>
	    </div>
	  </div>
	  <div id="actionResultDisplay"></div>
	  <div class="panel-content clearfix">
        <form id="addMenuForm" class="inline">
          <%-- ====================== 基本信息DIV开始 ===================== --%>
			<div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="global.page.title"/><%-- 基本信息 --%>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
	            <tr>
			    	<th><s:text name="PMC04_menuCode"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			    	<td><span><s:textfield name="menuCode" id="menuCode" cssClass="text" maxlength="50"/></span></td>
					<th><s:text name="PMC04_menuNameCN"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			    	<td><span><s:textfield name="brandMenuNameCN" id="brandMenuNameCN" cssClass="text" maxlength="50" /></span></td>
			    </tr>
			    <tr>
			    	<th><s:text name="PMC04_menuType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
					<td><span><s:textfield name="menuType" cssClass="text" maxlength="50" /></span></td>
					<th><s:text name="PMC04_menuNameEN"/></th>
			    	<td><span><s:textfield name="brandMenuNameEN" id="brandMenuNameEN" cssClass="text" maxlength="50" /></span></td>
			    </tr>
				<tr>
					<th><s:text name="PMC04_menuContainer"/></th>
					<td><span><s:textfield name="container" cssClass="text" maxlength="200" /></span></td>
					<th><s:text name="PMC04_menuScreen"/></th>
					<td><span><s:textfield name="menuValue" cssClass="text" maxlength="50" /></span></td>
				</tr>
				<tr>
					<th><s:text name="PMC04_menuLink"/></th>
					<td><span><s:textfield name="menuLink" cssClass="text" maxlength="255" /></span></td>
					<th><s:text name="PMC04_comment"/></th>
					<td><span><s:textfield name="comment" cssClass="text" maxlength="50" /></span></td>
				</tr>
				<tr>
					<th><s:text name="PMC04_machineType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
					<td>
			        	<span><s:select name="machineType" list='#application.CodeTable.getCodes("1284")' 
			           		listKey="CodeKey" listValue="Value" onchange="BINOLMOPMC04_3.cleanParentMenu();return false;">
			           </s:select></span>
		            </td>
					<th><s:text name="PMC04_isLeaf"/></th>
				    <td>
					    <input type="radio" id="isLeafTrue" name="isLeaf" value="1"/><label for="isLeafTrue" style="width:10px"><s:text name="PMC04_yes"/></label>
						<input type="radio" id="isLeafFalse" name="isLeaf" value="0" checked /><label for="isLeafFalse" style="width:10px"><s:text name="PMC04_no"/></label>
					</td>
				</tr>
                </table>
			  </div>
			</div>
			
	<%--========================= 上级菜单====================================--%>
	<div class="section" id="parentMenuDiv">
		<div class="section-header clearfix">
	    	<a class="add left" href="${popParentMenuUrl}" onclick="BINOLMOPMC04_3.popParentMenuDialog(this);return false;">
				<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="PMC04_parentMenu" /></span>
			</a>
	    </div>
	    <div class="section-content">
			<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
			  <thead>
			    <tr>
			      <th width="30%"><s:text name="PMC04_menuCode" /></th>
			      <th width="30%"><s:text name="PMC04_menuName" /></th>
			      <th width="30%"><s:text name="PMC04_menuType" /></th>
			      <th width="10%"><s:text name="PMC04_operation" /></th>
			    </tr>
			  </thead>
			  <tbody>
			  </tbody>
			</table>
	  	</div>
	</div>
	<%--========================= 操作按钮 ====================================--%>
    <hr class="space" />
	<div class="center">
	  <button class="save" onclick="BINOLMOPMC04_3.saveAddMenu();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="PMC04_save"></s:text></span></button>
	  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
	</div>
    </form>
</div>
</div>
</div>
<!--==========================上级菜单选择框START =================================== -->
<div id="parentMenuDialog" class="hide">
    <input id="parentMenuText" type="text" class="text" onKeyup ="datatableFilter(this,1);"/>
    <a class="search" onclick="datatableFilter('#parentMenuText',1);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="parentMenu_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="PMC04_menuCode"/></th><%--代码 --%>
               <th><s:text name="PMC04_menuName"/></th><%--名称 --%>
               <th><s:text name="PMC04_menuType"/></th><%--类型 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="parentMenuConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
</div>
<!--==========================上级菜单选择框END =================================== -->
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>