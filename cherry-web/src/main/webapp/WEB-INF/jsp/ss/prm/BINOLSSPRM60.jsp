<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- 促销品移库 --%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@page import="com.cherry.cm.cmbeans.UserInfo"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM60.js"></script>
<style>
input.text1{
    background-color: #FFFFFF;
    border: 1px solid #CCCCCC;
}
</style>
<s:i18n name="i18n.ss.BINOLSSPRM60">
<s:url id="getDapotInfo_url" value="/ss/BINOLSSPRM60_getDapotInfo"></s:url>
<s:url id="getLogicInfo_url" value="/ss/BINOLSSPRM60_getLogicInfo"></s:url>
<s:url id="save_url" value="/ss/BINOLSSPRM60_save"></s:url>
<s:url id="url_getStockCount" value="/ss/BINOLSSCM01_GETSTOCKCOUNT" />
<div class="hide">
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
</div>
<div id="popProduct_main">
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
    <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="PRM60_main"></s:text> </strong></div>
    <div class="section-content">
        <table class="detail">
            <tr>
                <th><s:text name="PRM60_depart"></s:text></th>
                <td>
                    <span id="departName" class="left"><s:property value="departInfoMap.DepartCodeName"></s:property></span>
                    <a class="add right" onclick="binOLSSPRM60.readyPopDepart(this);">
                        <span class="ui-icon icon-search"></span>
                        <span class="button-text"><s:text name="PRM60_select"/></span>
                    </a>
                    <input id="departId" type="hidden" name="departId" value="${departInfoMap.BIN_OrganizationID}"></input>
                    <input id="brandInfoId" name="brandInfoId" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                    <input id="CounterShiftFlag" name="CounterShiftFlag" type="hidden" value='<s:property value="counterShiftFlag"/>'></input>
                </td>
                <th><s:text name="PRM60_depot"></s:text></th>
                <td>
                    <s:select id="depotInfoId" name="depotInfoId" list="depotList" listKey="BIN_DepotInfoID" listValue="DepotCodeName" cssStyle="width:200px" onchange="javascript:$('#shiftData').html('');"></s:select>
                </td>
            </tr>
            <tr>
                <th><s:text name="PRM60_fromLogicInventoryName"></s:text></th>
                <td>
                    <s:select name="fromLogicInventoryInfoId" list="logicInventoryList" listKey="BIN_LogicInventoryInfoID" listValue="'('+LogicInventoryCode+')'+InventoryNameCN" cssStyle="width:100px;" id="fromLogicInventoryInfoId" cssStyle="width:200px" onchange="binOLSSPRM60.changeLogic(this);"></s:select>
                </td>
                <th><s:text name="PRM60_toLogicInventoryName"></s:text></th>
                <td>
                    <s:select name="toLogicInventoryInfoId" list="logicInventoryList" listKey="BIN_LogicInventoryInfoID" listValue="'('+LogicInventoryCode+')'+InventoryNameCN" cssStyle="width:100px;" id="toLogicInventoryInfoId" cssStyle="width:200px" onchange="binOLSSPRM60.changeLogic(this);"></s:select>
                </td>
            </tr>
            <tr>
                <th><s:text name="PRM60_comments"></s:text></th>
                <td colspan=3><input class="text" type="text" name="comments" id="comments" maxlength="200" style="width:95%;"/></td>
            </tr>
        </table>
    </div>
    <%-- ====================== 结果一览开始 ====================== --%>
    <div id="section" class="section">
        <div class="section-header">
            <strong> 
	            <span class="ui-icon icon-ttl-section-list"></span> 
	            <s:text name="PRM60_detail" />
            </strong>
        </div>
        <div class="section-content">
        <div class="toolbar clearfix">
            <span class="left" id="udiskOption">
                <a id="addBtn" class="delete" onclick="binOLSSPRM60.openProPopup(this);return false;">
                    <span class="ui-icon icon-add"></span>
                    <span class="button-text"><s:text name="PRM60_add"/></span>
                </a>
                <a id="deleteBtn" class="delete" onclick="binOLSSPRM60.deleteLine();return false;">
                    <span class="ui-icon icon-delete"></span>
                    <span class="button-text"><s:text name="PRM60_delete"/></span>
                </a>
            </span>
        </div>
        <input id="number" class="hide" value="1"/>
        <div style="width:100%;overflow-x:scroll;">
        <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
        <thead>
            <tr>
                <%--选择 --%>
                <%-- <th><s:checkbox name="allSelect" id="checkAll" onclick="checkSelectAll(this)"/></th>--%>
                <th class="tableheader" style="width: 1%;"><input type="checkbox" name="allSelect" id="checkAll" onclick="binOLSSPRM60.checkSelectAll(this)"/><s:text name="PRM60_select"></s:text></th>
                <%-- 厂商编码   --%>
                <th class="tableheader" style="width: 5%;"><s:text name="PRM60_unitCode" /></th>
                <%-- 条码   --%>
                <th class="tableheader" style="width: 5%;"><s:text name="PRM60_barCode" /></th>
                <%-- 产品名称--%>
                <th class="tableheader" style="width: 10%;"><s:text name="PRM60_productName" /></th>
                <%-- 当前库存  --%>
                <th class="tableheader" style="width: 5%;"><s:text name="PRM60_curStock" /></th>
                <%-- 数量  --%>
                <th class="tableheader" style="width: 5%;"><s:text name="PRM60_quantity" /></th>
                <%-- 理由 --%>
                <th class="tableheader" style="width: 20%;"><s:text name="PRM60_comments" /></th>
            </tr>
        </thead>
        <tbody id="shiftData"></tbody>
        </table>
    </div>
        <hr class="space" />
        <div class="center clearfix">
            <cherry:show domId="BINOLSSPRM6001">
                <button class="save" type="button" onclick="binOLSSPRM60.btnSaveClick()">
                    <span class="ui-icon icon-confirm"></span>
                    <span class="button-text"><s:text name="PRM60_yes"/></span>
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

<span id="getDapotInfo" style="display:none">${getDapotInfo_url}</span>
<span id="getLogicInfo" style="display:none">${getLogicInfo_url}</span>
<span id="s_getStockCount" style="display:none">${url_getStockCount}</span>
<span id="save" style="display:none">${save_url}</span>

<span id="noDepotWarning" style="display:none"><s:text name="PRM60_noDepotWarning"></s:text></span>
<span id="noDepartWarnig" style="display:none"><s:text name="PRM60_noDepartWarnig"></s:text></span>
<span id="noProductWarning" style="display:none"><s:text name="EST00010"></s:text></span>
<span id="noQuestityWarning" style="display:none"><s:text name="PRM60_noQuestity"></s:text></span>
<span id="notOnlyOneWarning" style="display:none"><s:text name="PRM60_notOnlyOne"></s:text></span>
<span id="noLogiDepotWarning" style="display:none"><s:text name="PRM60_noLogiDepot"></s:text></span>
<span id="errmsg_EST00008" style="display:none"><s:text name="EST00008"></s:text></span>
</s:i18n>