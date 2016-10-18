<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/ios/BINOLSTIOS03.js"></script>
<s:i18n name="i18n.st.BINOLSTIOS03">
<s:url id="url_getdepotAjax" value="/st/BINOLSTIOS03_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="save_url" action="BINOLSTIOS03_save"/>
<div class="hide">
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
</div>
<s:url id="getPrtVenIdAndStock_url" action="BINOLSTIOS03_getPrtVenIdAndStock"></s:url>
<div class="hide">
	<a id="save" href="${save_url}"></a>
	<a id="getPrtVenIdAndStock" href="${getPrtVenIdAndStock_url}"></a>
</div>
<div class="panel-header">
       <div class="clearfix"> 
        <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
       </div>
</div>
<div id="actionResultDisplay"></div>
<div id="errorMessage"></div>
 <%-- ================== 错误信息提示 START ======================= --%>
     <div id="errorDiv" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content">
<cherry:form id="mainForm" class="inline">
	<div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="IOS03_main"></s:text> </strong></div>
	<div class="section-content">
        <table class="detail">
           <tr>
           <th><s:text name="IOS03_department"/></th>
              		<td>
                       <input type="hidden" name="inOrganizationId" id="inOrganizationId" value='${organizationId}'>
                        <span id="inOrgName" class="left"><s:property value='departInit'/></span>
                        <a class="add right" onclick="BINOLSTIOS03.openDepartBox(this);">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="IOS03_select"/></span>
                        </a>
                        <s:hidden name="brandInfoId"></s:hidden>
              		</td>
              		<th><s:text name="IOS03_depotsName"></s:text></th>
                <td>
                	<select name="depotInfoId" disabled="disabled" id="depotInfoId" style="width:200px;" onchange="BINOLSTIOS03.clearDetailData();"></select>
                </td>
           </tr>
           <s:if test='null!=logicDepotsList && logicDepotsList.size()>0'>
            <tr>
                <th><s:text name="IOS03_logicInventory"></s:text></th>
                <td><s:select disabled="true" onchange="BINOLSTIOS03.clearDetailData();" name="logicInventoryInfoId" list="logicDepotsList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" id="logicInventoryInfoId" cssStyle="width:200px;"></s:select></td>
                <th></th>
                <td></td>
            </tr>
            </s:if>
            <tr>
                <th><s:text name="IOS03_comments"/></th>
                <td colspan=3><input class="text" type="text" name="comments" style="width:95%;" maxlength="200"/></td>
            </tr>
        </table>
	</div>
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section">
		<div class="section-header">
			<strong> 
			<span class="ui-icon icon-ttl-section-search-result"></span> 
			<s:text name="IOS03_detail" />
		 	</strong>
		</div>
		<div class="section-content">
		<div class="toolbar clearfix">
			<span class="left" id="udiskOption">
				<cherry:show domId="BINOLSTIOS03ADD">
					 <a id="add" class="add" onclick="BINOLSTIOS03.openProPopup(this);return false;">
                		<span class="ui-icon icon-add"></span>
                		<span class="button-text"><s:text name="IOS03_add"/></span>
               	 	</a>
				</cherry:show>
				<cherry:show domId="BINOLSTIOS03DEL">
					 <a id="del" class="delete" onclick="BINOLSTIOS03.deleteLine();return false;">
                		<span class="ui-icon icon-delete"></span>
                		<span class="button-text"><s:text name="IOS03_delete"/></span>
               	 	</a>
				</cherry:show>
			</span>
		</div>
		<input id="number" class="hide" value="1"/>
		<div style="width:100%;overflow-x:scroll;">
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<%--选择 --%>
				<th class="tableheader" style="width: 1%;"><input type="checkbox" name="allSelect" id="checkAll" onclick="BINOLSTIOS03.checkSelectAll(this)"/><s:text name="IOS03_select" /></th>
				<%-- 厂商编码   --%>
				<th class="tableheader" style="width: 5%;"><s:text name="IOS03_unitCode" /></th>
				<%-- 条码   --%>
				<th class="tableheader" style="width: 5%;"><s:text name="IOS03_barCode" /></th>
				<%-- 产品名称--%>
				<th class="tableheader" style="width: 10%;"><s:text name="IOS03_productName" /></th>
				<%-- 当前库存  --%>
				<th class="tableheader" style="width: 5%;"><s:text name="IOS03_curStock" /></th>
				<%-- 数量  --%>
				<th class="tableheader" style="width: 5%;"><s:text name="IOS03_quantity" /></th>
				<%-- 理由 --%>
				<th class="tableheader" style="width: 20%;"><s:text name="IOS03_comments" /></th>
			</tr>
		</thead>
		<tbody id="databody"></tbody>
	</table>
	</div>
	<hr class="space" />
		<div class="center clearfix">
		    <cherry:show domId="BINOLSTIOS0301">
			<button class="save" type="button" onclick="BINOLSTIOS03.save()">
			<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><s:text name="IOS03_save"/></span>
			</button>
			</cherry:show>
		</div>
	</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
	</cherry:form>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="quantityWarnig" value='<s:text name="EST00011"/>'/>
    <input type="hidden" id="noProductWarning" value='<s:text name="EST00009"/>'/>
    <span id="noDepotWarning" style="display:none"><s:text name="IOS03_noDepotWarning"></s:text></span>
	<span id="noDepartWarnig" style="display:none"><s:text name="IOS03_noDepartWarnig"></s:text></span>
 <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
</div>
</s:i18n>