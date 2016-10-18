<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<%@page import="com.cherry.cm.cmbeans.UserInfo"%><link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/st/ios/BINOLSTIOS02.js"></script>
<style>
input.text1{
	background-color: #FFFFFF;
    border: 1px solid #CCCCCC;
}
</style>
<s:i18n name="i18n.st.BINOLSTIOS02">
<s:url id="getDapotInfo_url" value="/st/BINOLSTIOS02_getDapotInfo"></s:url>
<s:url id="getLogicInfo_url" value="/st/BINOLSTIOS02_getLogicInfo"></s:url>
<s:url id="getPrtVenIdAndStock_url" value="/st/BINOLSTIOS02_getPrtVenIdAndStock"></s:url>
<s:url id="save_url" value="/st/BINOLSTIOS02_save"></s:url>
<div class="hide">
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
</div>
<div id="popProduct_main">
</div>
<!--<div id="popCounter_main">-->
<!--<%-- ================== 弹出datatable -- 柜台共通导入 START ======================= --%>-->
<!--<jsp:include page="/WEB-INF/jsp/pt/common/BINOLPTCOM01.jsp" flush="true" />-->
<!--<%-- ================== 弹出datatable -- 柜台共通导入 START ======================= --%>-->
<!--</div>-->
<cherry:form id="mainForm" class="inline">
<div class="panel-header">
     	<%-- ###问卷查询 --%>
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
     <s:if test="hasActionErrors()">
        <div class="actionError" id="actionResultDiv">
            <s:actionerror/>                
        </div>
        <div style="height:20px"></div>
    </s:if>
     <s:else>
    <%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content">
	<div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="IOS02_main"></s:text> </strong></div>
	<div class="section-content">
		<table class="detail">
			<tr>
                        <th><s:text name="IOS02_depart"></s:text></th>
                        <td>
                        	<span id="departName" class="left"><s:property value="departName"></s:property></span>
                            <a class="add right" onclick="binOLSTIOS02.readyPopDepart(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="IOS02_select"/></span>
                            </a>
                        	<input id="departInfoId" type="hidden" name="departId" value="${organizationId}"></input>
							<s:hidden name="brandInfoId"></s:hidden>
							<input id="CounterShiftFlag" name="CounterShiftFlag" type="hidden" value='<s:property value="counterShiftFlag"/>'></input>
						</td>
                        <th><s:text name="IOS02_depot"></s:text></th>
                        <td>
                        	<s:select id="depotInfoId" name="depotInfoId" list="depotList" listKey="BIN_DepotInfoID" listValue="DepotCodeName" cssStyle="width:200px" onchange="javascript:$('#shfitDate').html('');"></s:select>
                        
                        </td>
                    </tr>
                     <s:if test='null!=logicInventoryList && logicInventoryList.size()>0'>
                    <tr>
						<th><s:text name="IOS02_fromLogicInventoryName"></s:text></th>
						<td>
							<s:select name="fromLogicInventoryInfoId" list="logicInventoryList" listKey="BIN_LogicInventoryInfoID" listValue="'('+LogicInventoryCode+')'+InventoryNameCN"  id="fromLogicInventoryInfoId" cssStyle="width:200px" onchange="javascript:$('#shfitDate').html('');"></s:select>
						</td>
						<th><s:text name="IOS02_toLogicInventoryName"></s:text></th>
						<td>
							<s:select name="toLogicInventoryInfoId" list="logicInventoryList" listKey="BIN_LogicInventoryInfoID" listValue="'('+LogicInventoryCode+')'+InventoryNameCN"  id="toLogicInventoryInfoId" cssStyle="width:200px" onchange="javascript:$('#shfitDate').html('');"></s:select>
						</td>
						</tr>
					</s:if>
                    <tr>
                        <th><s:text name="IOS02_comments"></s:text></th>
                        <td colspan=3><input class="text" type="text" name="comments" id="comments" maxlength="200" style="width:95%;"/></td>
                    </tr>
		</table>
	</div>
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section">
		<div class="section-header">
			<strong> 
			<span class="ui-icon icon-ttl-section-list"></span> 
			<s:text name="IOS02_detail" />
		 	</strong>
		</div>
		<div class="section-content">
		<div class="toolbar clearfix">
			<span class="left" id="udiskOption">
				<cherry:show domId="BINOLSTIOS0201">
					 <a id="addBtn" class="delete" onclick="binOLSTIOS02.openProPopup(this);return false;">
                		<span class="ui-icon icon-add"></span>
                		<span class="button-text"><s:text name="IOS02_selectPrt"/></span>
               	 	</a>
				</cherry:show>
				<cherry:show domId="BINOLSTIOS0202">
					 <a id="deleteBtn" class="delete" onclick="binOLSTIOS02.deleteLine();return false;">
                		<span class="ui-icon icon-delete"></span>
                		<span class="button-text"><s:text name="IOS02_delete"/></span>
               	 	</a>
				</cherry:show>
				<cherry:show domId="BINOLSTIOS0201">
					 <a id="addLineBtn" class="delete" onclick="binOLSTIOS02.addNewLine();return false;">
                		<span class="ui-icon icon-add"></span>
                		<span class="button-text"><s:text name="IOS02_add"/></span>
               	 	</a>
				</cherry:show>
			</span>
		</div>
		<input id="rowNumber" type="hidden" value="0">
		<div style="width:100%;overflow-x:scroll;">
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0"
		class="jquery_table" width="100%">
		<thead>
			<tr>
				<%--选择 --%>
				<%-- <th><s:checkbox name="allSelect" id="checkAll" onclick="checkSelectAll(this)"/></th>--%>
				<th class="tableheader" style="width: 1%;"><input type="checkbox" name="allSelect" id="checkAll" onclick="binOLSTIOS02.checkSelectAll(this)"/><s:text name="IOS02_select"></s:text></th>
				<%-- 厂商编码   --%>
				<th class="tableheader" style="width: 5%;"><s:text name="IOS02_unitCode" /></th>
				<%-- 条码   --%>
				<th class="tableheader" style="width: 5%;"><s:text name="IOS02_barCode" /></th>
				<%-- 产品名称--%>
				<th class="tableheader" style="width: 10%;"><s:text name="IOS02_productName" /></th>
				<%-- 当前库存  --%>
				<th class="tableheader" style="width: 5%;"><s:text name="IOS02_curStock" /></th>
				<%-- 数量  --%>
				<th class="tableheader" style="width: 5%;"><s:text name="IOS02_quantity" /></th>
				<%-- 理由 --%>
				<th class="tableheader" style="width: 20%;"><s:text name="IOS02_comments" /></th>
			</tr>
		</thead>
		<tbody id="shfitDate"></tbody>
		
	</table>
	
	</div>
	<hr class="space" />
            <div class="center clearfix">
            	<cherry:show domId="BINOLSTIOS0204">
                <button class="save" type="button" onclick="binOLSTIOS02.saveTemp()"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.saveTemp"/></span></button>
                </cherry:show>
                <cherry:show domId="BINOLSTIOS0203">
            	<button class="save" type="button" onclick="binOLSTIOS02.btnSaveClick()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="IOS02_yes"/></span></button>
                </cherry:show>
            </div>
	</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
 </div>
 </s:else>
</cherry:form>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>


<span id="getDapotInfo" style="display:none">${getDapotInfo_url}</span>
<span id="getLogicInfo" style="display:none">${getLogicInfo_url}</span>
<span id="getPrtVenIdAndStock" style="display:none">${getPrtVenIdAndStock_url}</span>
<span id="save" style="display:none">${save_url}</span>
<s:url id="saveTemp_url" value="/st/BINOLSTIOS02_saveTemp"></s:url>
<span id="saveTemp" style="display:none">${saveTemp_url}</span>

<input type="hidden" value="" id="getDepotInfo_param"></input>

<span id="noDepotWarning" style="display:none"><s:text name="IOS02_noDepotWarning"></s:text></span>
<span id="noDepartWarnig" style="display:none"><s:text name="IOS02_noDepartWarnig"></s:text></span>
<span id="noProductWarning" style="display:none"><s:text name="EST00010"></s:text></span>
<span id="noQuestityWarning" style="display:none"><s:text name="IOS02_noQuestity"></s:text></span>
<span id="notOnlyOneWarning" style="display:none"><s:text name="IOS02_notOnlyOne"></s:text></span>
<span id="noLogiDepotWarning" style="display:none"><s:text name="IOS02_noLogiDepot"></s:text></span>
<span id="errmsg_EST00008" style="display:none"><s:text name="EST00008"></s:text></span>
<input type="hidden" id="errmsg_EST00035" value='<s:text name="EST00035"/>'/>
</s:i18n>