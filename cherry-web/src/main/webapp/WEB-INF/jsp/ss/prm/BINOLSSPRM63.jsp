<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- 入库 --%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="java.util.*" %>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM63.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	$('#inDepotDate').cherryDate({});
</script>
<s:i18n name="i18n.ss.BINOLSSPRM63">
<%UserInfo userinfo = (UserInfo)request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);%>
<s:url id="url_getdepotAjax" value="/ss/BINOLSSPRM63_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_save" value="/ss/BINOLSSPRM63_save" />
<span id ="urlSave" style="display:none">${url_save}</span>
<s:url id="url_submit" value="/ss/BINOLSSPRM63_submit" />
<span id ="urlSubmit" style="display:none">${url_submit}</span>
<s:url id="getLogicInfo_url" value="/ss/BINOLSSPRM63_getLogicInfo" />
<span id ="getLogicInfo" style="display:none">${getLogicInfo_url}</span>
<div class="hide">
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
    <input type="hidden"  id="brandInfoId" value='<%=userinfo.getBIN_BrandInfoID() %>'>
    <s:url id="url_getInDepotDetail" value="/ss/BINOLSSCM10_getInDepotDetail" />
    <span id ="s_getInDepotDetail" >${url_getInDepotDetail}</span>
</div>
<div class="panel-header">
    <div class="clearfix">
        <span class="breadcrumb left">
            <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
        </span>
    </div>
</div>
    <div id="actionResultDisplay"></div>
    <cherry:form id="mainForm" class="inline" onsubmit="return false;">
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <s:if test="hasActionErrors()">
        <div class="actionError" id="actionResultDiv">
            <s:actionerror/>
        </div>
        <div style="height:20px"></div>
    </s:if>
    <s:else>
    <div class="panel-content">
        <div class="section">
            <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="PRM63_general"/></strong></div>
            <div class="section-content">
                <table class="detail">
                    <tr>
                        <th><s:text name="PRM63_depart"/></th>
                        <td>
                            <input type="hidden" name="inOrganizationId" id="inOrganizationId" value='${initInfoMap.defaultInDepartID}'>
                            <span id="inOrgName" class="left"><s:property value='initInfoMap.defaultInDepartCodeName'/></span>
                            <input type="hidden" id="counterInDepotFlag" value='<s:property value="counterInDepotFlag"/>'>
                            <a class="add right" onclick="BINOLSSPRM63.openDepartBox(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="PRM63_select"/></span>
                            </a>
                        </td>
                        <th>
                            <s:text name="PRM63_inDepotDate"></s:text>
                        </th>
                        <td>
                        <p class="date">
                            <span><s:textfield name="inDepotDate" cssClass="date" id="inDepotDate"/></span>
                        </p>
                        </td>
                    </tr>
                    <tr>
                        <th><s:text name="PRM63_depot"/></th>
                        <td>
                            <select disabled="true" name="depotInfoId" id="depotInfoId" onchange="BINOLSSPRM63.clearDetailData();" style="width:200px;">
                                <option value=""><s:text name="PRM63_selectAll"/></option>
                            </select>
                        </td>
                         <th> 
                            <s:text name="PRM63_partner"/>
                        </th>
                        <td>
                            <input type="hidden" name="bussinessPartnerId" id="bussinessPartnerId"></input>
                            <input type="text" class="text" id="bussinessPartner"></input>
                        </td>
                    </tr>
                    <s:if test='null!=logicDepotsInfoList && logicDepotsInfoList.size()>0'>
                    <tr>
                         <th><s:text name="PRM63_logicDepot"/></th>
                        <td><s:select disabled="true" id="logicDepotsInfoId" name="logicDepotsInfoId" list="logicDepotsInfoList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" Cssstyle="width:200px;" onchange="BINOLSSPRM63.clearDetailData();"></s:select></td>
                        <th></th>
                        <td></td>
                    </tr>
                    </s:if>
                    <tr>
                        <th><s:text name="PRM63_reason"/></th>
                        <td colspan=3><input type="text" name="reason" class="text" style="width:95%;" maxlength="200"/></td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="section">
            <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="PRM63_detail"/></strong></div>
            <div class="section-content">
                <div class="toolbar clearfix">
                    <span class="left">
                        <a class="add" onclick="BINOLSSPRM63.openProPopup(this);">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="PRM63_add"/></span>
                        </a>
                        <a class="delete" onclick="BINOLSSPRM63.deleteRow();">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="PRM63_delete"/></span>
                        </a>
                        <a class="add" id="billCopy" onclick="BINOLSSPRM63.copyInDepot();">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="PRM63_billCopy"/></span>
                        </a>
                    </span>
                </div>
                <div style="width:100%;overflow-x:scroll;">
                    <input type="hidden" id="rowNumber" value="1"/>
                    <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
                        <thead>
                            <tr>
                                <th class="tableheader" width="3%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/><s:text name="PRM63_select"/></th>
                                <th class="tableheader" width="10%"><s:text name="PRM63_code"/></th>
                                <th class="tableheader" width="10%"><s:text name="PRM63_barcode"/></th>
                                <th class="tableheader" width="18%"><s:text name="PRM63_name"/></th>
                                <th class="tableheader hide" width="15%"><s:text name="PRM63_batchNo"/></th>
                                <th class="tableheader" width="5%"><s:text name="PRM63_standardCost"/></th>
                                <th class="tableheader" width="10%"><s:text name="PRM63_quantity"/></th>
                                <th class="tableheader" width="10%"><s:text name="PRM63_money"/></th>
                                <th class="tableheader" width="20%"><s:text name="PRM63_remark"/></th>
                                <th style="display:none">
                            </tr>
                        </thead>
                        <tbody id="databody">
                        </tbody>
                    </table>
                </div>
                <hr class="space" />
             <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="PRM63_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="PRM63_totalAmount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td class="center">
                    <%-- 总数量 --%>
                   <span id="totalQuantity">0</span>
                </td>
                <td class="center">
                    <%-- 总金额--%>
                    <span id="totalAmount">0.00</span>
                </td>
              </tr>
            </table>
                <hr class="space" />
                <div class="center clearfix">
                    <cherry:show domId="BINOLSSPRM6301">
                    <button class="confirm" type="button" onclick="BINOLSSPRM63.confirm();"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="PRM63_confirm"/></span></button>
                    </cherry:show>
                </div>
            </div>
        </div>
    </div>
    </s:else>
    </cherry:form>
    <input type="hidden" id="partnerError" value='<s:text name="PRM63_partnerError"/>'/>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出datatable -- 促销品入库单共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/ss/common/BINOLSSCM10.jsp" flush="true" />
<%-- ================== 弹出datatable -- 促销品入库单共通START ======================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00013" value='<s:text name="EST00013"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
</div>