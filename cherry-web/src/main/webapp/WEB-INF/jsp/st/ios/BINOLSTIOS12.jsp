<%-- 大仓入库（带有计算加权平均价） --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="java.util.*" %>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/st/ios/BINOLSTIOS12.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	$('#inDepotDate').cherryDate({});
</script>
<s:i18n name="i18n.st.BINOLSTIOS12">
<%UserInfo userinfo = (UserInfo)request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);%>
<s:url id="url_getdepotAjax" value="/st/BINOLSTIOS12_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_save" value="/st/BINOLSTIOS12_save" />
<span id ="urlSave" style="display:none">${url_save}</span>
<s:url id="url_submit" value="/st/BINOLSTIOS12_submit" />
<span id ="urlSubmit" style="display:none">${url_submit}</span>
<s:url id="getLogicInfo_url" value="/st/BINOLSTIOS12_getLogicInfo" />
<span id ="getLogicInfo" style="display:none">${getLogicInfo_url}</span>
<div class="hide">
	<input type="hidden"  id="brandInfoId" value='<%=userinfo.getBIN_BrandInfoID() %>'>
	<s:url id="url_getInDepotDetail" value="/st/BINOLSTCM18_getInDepotDetail" />
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
            <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="IOS12_general"/></strong></div>
            <div class="section-content">
                <table class="detail">
                    <tr>
                        <th><s:text name="IOS12_depart"/></th>
                        <td>
                            <input type="hidden" name="inOrganizationId" id="inOrganizationId" value='${initInfoMap.defaultInDepartID}'>
                            <span id="inOrgName" class="left"><s:property value='initInfoMap.defaultInDepartCodeName'/></span>
                            <!-- 只能给大仓做入库 -->
                            <%-- <a class="add right" onclick="BINOLSTIOS12.openDepartBox(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="IOS12_select"/></span>
                            </a> --%>
                        </td>
                        <th>
                        	<s:text name="IOS12_inDepotDate"></s:text>
                        </th>
                        <td>
                        <p class="date">
                        	<span><s:textfield name="inDepotDate" cssClass="date" id="inDepotDate"/></span>
                        </p>
                        </td>
                    </tr>
                    <tr>
                        <th><s:text name="IOS12_depot"/></th>
                        <td>
                            <select disabled="disabled" name="depotInfoId" id="depotInfoId" onchange="BINOLSTIOS12.clearDetailData();" style="width:200px;">
                                <option value=""><s:text name="IOS12_selectAll"/></option>
                            </select>
                        </td>
                        <th> 
                        	<s:text name="IOS12_partner"/>
                        </th>
                        <td>
                        	<input type="hidden" name="bussinessPartnerId" id="bussinessPartnerId"></input>
                        	<input type="text" class="text" id="bussinessPartner"></input>
                        </td>
                    </tr>
                    <s:if test='null!=logicDepotsInfoList && logicDepotsInfoList.size()>0'>
                    <tr>
                         <th><s:text name="IOS12_logicDepot"/></th>
                        <td><s:select disabled="true" id="logicDepotsInfoId" name="logicDepotsInfoId" list="logicDepotsInfoList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" Cssstyle="width:200px;" onchange="BINOLSTIOS12.clearDetailData();"></s:select></td>
                        <th></th>
                        <td></td>
                    </tr>
                    </s:if>
                    <tr>
                        <th><s:text name="IOS12_reason"/></th>
                        <td colspan=3><input type="text" name="reason" class="text" style="width:95%;" maxlength="200"/></td>
                    </tr>
                </table>
            </div>
        </div>
        
        <div class="section">
            <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="IOS12_detail"/></strong></div>
            <div class="section-content">
                <div class="toolbar clearfix">
                    <span class="left">
                        <a class="add" onclick="BINOLSTIOS12.openProPopup(this);">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="IOS12_add"/></span>
                        </a>
                        <a class="delete" onclick="BINOLSTIOS12.deleteRow();">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="IOS12_delete"/></span>
                        </a>
                        <a class="add" id="billCopy" onclick="BINOLSTIOS12.copyInDepot();">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="IOS12_billCopy"/></span>
                        </a>
                        <a id="spanBtnAddNewLine" class="add" onclick="BINOLSTIOS12.addNewLine();">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="IOS12_addNewLine"/></span>
                         </a>
                    </span>
                </div>
                <div style="width:100%;overflow-x:scroll;">
                    <input type="hidden" id="rowNumber" value="0"/>
                    <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
                        <thead>
                            <tr>
                                <th class="tableheader" width="3%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/><s:text name="IOS12_select"/></th>
                                <th class="tableheader" width="10%"><s:text name="IOS12_code"/></th>
                                <th class="tableheader" width="10%"><s:text name="IOS12_barcode"/></th>
                                <th class="tableheader" width="18%"><s:text name="IOS12_name"/></th>
                                <%-- <th class="tableheader" width="15%"><s:text name="IOS12_batchNo"/></th> --%>
                                <th class="tableheader" width="5%"><s:text name="IOS12_referencePrice"/></th>
                                <th class="tableheader" width="5%">
                                    <s:text name="IOS12_price"/>
                                    <span class="calculator" onclick="BINOLSTIOS12.initRateDiv(this,'batch');" title="<s:text name="IOS12_BatchCalTitle"/>"></span>
                                    <input type="hidden" id="batchPriceRate" value="100.00">
                                </th>
                                <th class="tableheader" width="10%"><s:text name="IOS12_quantity"/></th>
                                <th class="tableheader" width="10%"><s:text name="IOS12_money"/></th>
                                <th class="tableheader" width="20%"><s:text name="IOS12_remark"/></th>
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
                <td class="center"><s:text name="IOS12_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="IOS12_totalAmount"/></td><%-- 总金额--%>
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
                	<%-- <button class="save" type="button" onclick="BINOLSTIOS12.save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="IOS12_save"/></span></button> --%>
                    <cherry:show domId="BINOLSTIOS1201">
                    <button class="confirm" type="button" onclick="BINOLSTIOS12.confirm();"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="IOS12_confirm"/></span></button>
                    </cherry:show>
                </div>
            </div>
        </div>
    
    </div>
    </s:else>
    </cherry:form>
    <div class="rateDialog hide">
        <span id="spanCalTitle" style="display:none;"><s:text name="IOS12_CalTitle"/></span>
        <s:text name="IOS12_discountRate"/><%-- 折扣率 --%>
        <input class="number" id="priceRate" value="100" onblur="BINOLSTIOS12.closeDialog(this);return false;"  
            onkeyup="BINOLSTIOS12.setDiscountPrice(this);return false;"/><s:text name="global.page.percent"/>
        <input type ="hidden" id="curRateIndex" value=""/>
    </div>
    <input type="hidden" id="partnerError" value='<s:text name="IOS12_partnerError"/>'/>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通END ======================= --%>
<%-- ================== 弹出datatable -- 产品入库单共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/BINOLSTCM18.jsp" flush="true" />
<%-- ================== 弹出datatable -- 产品入库单共通END ======================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00013" value='<s:text name="EST00013"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
    <input type="hidden" id="errmsg_EST00035" value='<s:text name="EST00035"/>'/>
</div>