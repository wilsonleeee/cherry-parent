<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH01.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/ws/mng/BINOLWSMNG01.js"></script>

<s:i18n name="i18n.st.BINOLSTSFH01">
<s:if test='operateType.equals("2")'>
  <input type="hidden" id="rowNumber" value="<s:property value='productOrderDetailData.size()'/>"/>
</s:if>
<s:else>
  <input type="hidden" id="rowNumber" value="0"/>
</s:else>
<cherry:form id="mainForm" class="inline" onsubmit="return false;" csrftoken="false">
<s:if test='operateType.equals("newBill")'>
<input type="hidden" name="inOrganizationId" id="inOrganizationId" value='${initInfoMap.defaultDepartID}'>
<input type="hidden" name="outOrganizationId" id="outOrganizationId" value='${initInfoMap.defaultOutDepartID}'>
<input type="hidden" name="inDepotId" id="inDepotId" value='${initInfoMap.inDepotID}'>
<input type="hidden" name="inLogicDepotId" id="inLogicDepotId" value='${initInfoMap.inLgDepotID}'>
<input type="hidden" name="fromPage" value="1">
<s:hidden name="operateType"></s:hidden>
</s:if>
<s:else>
<input type="hidden" id="rowNumber" value="<s:property value='productOrderDetailData.size()'/>"/>
<input type="hidden" id="inDepotId" value='<s:property value="productOrderMainData.BIN_InventoryInfoID"/>'/>
<input type="hidden" id="inLogicDepotId" value='<s:property value="productOrderMainData.BIN_LogicInventoryInfoID"/>'/>
<input type="hidden" value="<s:property value="productOrderMainData.UpdateTime"/>" name="updateTime" id="updateTime">
<input type="hidden" value="<s:property value="productOrderMainData.ModifyCount"/>" name="modifyCount" id="modifyCount">
<input type="hidden" value="<s:property value="productOrderMainData.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
<input type="hidden" value="<s:property value="productOrderMainData.BIN_OrganizationID"/>" id="inOrganizationId" name="inOrganizationID">
<input type="hidden" value="<s:property value="productOrderMainData.BIN_OrganizationIDAccept"/>" id="outOrganizationId" name="outOrganizationID">
<input type="hidden" value="<s:property value="productOrderMainData.BIN_InventoryInfoIDAccept"/>" id="depotInfoIdAccept" name="depotInfoIdAccept">
<input type="hidden" value="<s:property value="productOrderMainData.BIN_LogicInventoryInfoIDAccept"/>" id="logicDepotsInfoIdAccept" name="logicDepotsInfoIdAccept">
<s:hidden name="productOrderID"></s:hidden>
<input type="hidden" value="" name="editFlag" id="editFlag">
<input type="hidden" id="operateTypeAudit"  value="<s:property value="productOrderMainData.OperateTypeAudit"/>"/>
<s:hidden name="operateType"></s:hidden>
</s:else>
<div class="main container clearfix">
<div class="panel ui-corner-all">
    <div class="hide">
        <input type="hidden" id="counterCode" value='<s:property value="counterCode"/>'/>
        <s:text name="global.page.select" id="globalSelect"/>
        <s:text name="SFH01_Select" id="select"/>
        <s:url id="url_getdepotAjax" value="/st/BINOLSTSFH01_getDepot" />
        <span id ="urlgetdepotAjax" >${url_getdepotAjax}</span>
        <s:url id="getLogicInfo_url" value="/st/BINOLSTSFH01_getLogicInfo" />
        <span id ="getLogicInfo" >${getLogicInfo_url}</span>
        <s:url id="s_submitURL" value="/st/BINOLSTSFH01_submit" />
        <span id ="submitURL" >${s_submitURL}</span>
        <s:url id="s_saveURL" value="/st/BINOLSTSFH01_save" />
        <span id ="saveURL" >${s_saveURL}</span>
        <s:url id="url_getStockCount" value="/st/BINOLSTSFH01_getPrtVenIdAndStock" />
        <span id ="s_getStockCount" >${url_getStockCount}</span>
        <s:url id="url_getOutDepart" value="/st/BINOLSTSFH01_getOutDepart" />
        <span id ="s_getOutDepart" >${url_getOutDepart}</span>
        <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
        <input type="hidden" id="brandInfoId" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'>
        
        <s:url id="save_url" value="/st/BINOLSTSFH03_save"/>
		<s:url id="submit_url" value="/st/BINOLSTSFH03_submit"/>
		<a id="save_url" href="${save_url}"></a>
    	<a id="submit_url" href="${submit_url}"></a>
    </div>
    <div class="panel-header">
        <div class="clearfix"> 
            <span class="breadcrumb left">      
                <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="SFH01_title"/>
            </span>
        </div>
    </div>
    <div id="actionResultDisplay"></div>
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
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-info"></span><s:text name="SFH01_general"/>
                    </strong>
                </div>
            <div class="section-content">
                <table class="detail">
                    <tr>
                        <s:if test='operateType.equals("newBill")'>
                        <th><s:text name="SFH01_date"/></th>
                        <td><s:property value="initInfoMap.bussinessDate"/><input type="hidden" name="date" value='<s:property value="initInfoMap.bussinessDate"/>'></td>
                        <th><s:text name="SFH01_BA"/></th>
                        <td>
                            <%-- <s:if test='null != counterBAList && counterBAList.size>0'>
                                <s:select name="tradeEmployeeID" list='counterBAList' listKey="baInfoId" listValue="baName" headerKey="" headerValue="%{globalSelect}" />
                            </s:if>
                            <s:else>
                                <s:property value="#session.userinfo.employeeName"/>
                                <s:hidden name="tradeEmployeeID" value="%{#session.userinfo.BIN_EmployeeID}"></s:hidden>
                            </s:else> --%>
                            <s:select name="tradeEmployeeID" list='counterBAList' listKey="baInfoId" listValue="baName" headerKey="" headerValue="%{globalSelect}" />
                        </td>
                        </s:if>
                        <s:else>
                        <th><s:text name="SFH01_date"/></th>
                        <td><s:property value="productOrderMainData.Date"/><s:hidden id="orderTime" value="%{productOrderMainData.OrderDateTime}"/></td>
                        <th><s:text name="SFH01_BA"/></th>
                        <td><s:property value="productOrderMainData.EmployeeName"/></td>
                        </s:else>
                    </tr>
                    <tr>
                        <s:if test='operateType.equals("newBill")'>
                        <th><s:text name="SFH01_ExpectDeliverDate"/></th>
                        <td><s:textfield name="expectDeliverDate" cssClass="date" readOnly="readOnly"/></td>
                        <th><s:text name="SFH01_orderType"/></th>
                        <td>
                            <s:select name="orderType" list='#application.CodeTable.getCodes("1168")' listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
                        </td>
                        </s:if>
                        <s:else>
                        <s:if test='operateType.equals("2")'>
                        <th><s:text name="SFH01_ExpectDeliverDate"/></th>
                        <td><s:textfield name="expectDeliverDate" cssClass="date" value="%{productOrderMainData.ExpectDeliverDate}" readOnly="readOnly"/></td>
                        <th><s:text name="SFH01_orderType"/></th>
                        <td>
                            <s:select name="orderType" list='#application.CodeTable.getCodes("1168")' listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;" value="%{productOrderMainData.OrderType}"/>
                        </td>
                        </s:if>
                        <s:else>
                        <th><s:text name="SFH01_ExpectDeliverDate"/></th>
                        <td><s:property value="productOrderMainData.ExpectDeliverDate"/></td>
                        <th><s:text name="SFH01_orderType"/></th>
                        <td>
                            <s:property value='#application.CodeTable.getVal("1168", productOrderMainData.OrderType)'/>
                        </td>
                        </s:else>
                        </s:else>
                    </tr>                
                </table>
                </div>
            </div>
        <div class="section">
            <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="SFH01_detail"/></strong></div>
            <div class="section-content">
            <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
            <div class="toolbar clearfix">
                <span class="left"> 
                    <a id="spanBtnadd" class="add" onclick="BINOLWSMNG01.openProPopup();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="SFH01_add"/></span></a> 
                    <a id="spanBtdelete" class="delete" onclick="BINOLWSMNG01.deleterowWithOne();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="SFH01_delete"/></span></a>
                </span>
                <span class="bg_yew" ><span style="line-height:25px;" class="highlight">*<s:text name="SFH01_drag"/> </span></span>
            </div>
            </s:if>
           
                <div style="width:100%;overflow-x:scroll;">
                <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%" class="background_1">
                    <thead>
                       <tr>
                            <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                            <th class="tableheader" width="3%">
                                <input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/>
                                <s:text name="SFH01_allselect"/>
                            </th>
                            </s:if>
                            <s:else>
                               <th class="tableheader" width="3%"><s:text name="SFH01_num"/></th>
                            </s:else>
                            <th class="tableheader" width="15%" onclick="BINOLSTSFH01.sortTable('_unitCodeArr');">
                            <div class="DataTables_sort_wrapper"><s:text name="SFH01_code"/><span id="_unitCodeArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                            <th class="tableheader" width="15%" onclick="BINOLSTSFH01.sortTable('_barCodeArr');">
                            <div class="DataTables_sort_wrapper"><s:text name="SFH01_barcode"/><span id="_barCodeArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                            <th class="tableheader" width="18%"><s:text name="SFH01_proname"/></th>
                            <th class="tableheader" width="8%"><s:text name="SFH01_price"/></th>
                            <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                            <th class="tableheader" width="5%"><s:text name="SFH01_NowCount"/></th>
                            </s:if>
                            <th class="tableheader" width="10%" onclick="BINOLSTSFH01.sortTable('_quantArr');">
                            <div class="DataTables_sort_wrapper"><s:text name="SFH01_quantity"/><span id="_quantArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                            <th class="tableheader" width="10%"><div style="line-height:13px;"><s:text name="SFH01_money"/><s:text name="SFH01_moneyu"/></div></th>
                            <th class="tableheader" width="20%"><s:text name="SFH01_reason"/></th>
                            <th style="display:none"></th>
                        </tr>
                    </thead>
                    <tbody id="databody">
<%--                    </tbody>
                </table>
                 <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
                     <thead>
                       <tr>
                            <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                            <th class="tableheader" width="3%">
                                <input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/>
                                <s:text name="SFH01_allselect"/>
                            </th>
                            </s:if>
                            <s:else>
                               <th class="tableheader" width="3%"><s:text name="SFH01_num"/></th>
                            </s:else>
                            <th class="tableheader" width="15%"><s:text name="SFH01_code"/></th>
                            <th class="tableheader" width="15%"><s:text name="SFH01_barcode"/></th>
                            <th class="tableheader" width="18%"><s:text name="SFH01_proname"/></th>
                            <th class="tableheader" width="8%"><s:text name="SFH01_price"/></th>
                            <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                            <th class="tableheader" width="5%"><s:text name="SFH01_NowCount"/></th>
                            </s:if>
                            <th class="tableheader" width="10%"><s:text name="SFH01_quantity"/></th>
                            <th class="tableheader" width="10%"><div style="line-height:13px;"><s:text name="SFH01_money"/><s:text name="SFH01_moneyu"/></div></th>
                            <th class="tableheader" width="20%"><s:text name="SFH01_reason"/></th>
                            <th style="display:none"></th>
                        </tr>
                    </thead>
                    <tbody id="databody"> --%>
                    
                    <s:if test='!operateType.equals("newBill")'>
                    <s:iterator value="productOrderDetailData" status="status">
                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else> <s:if test="abnormalQuantityFlag.equals('true')">abnormal</s:if>">
                        <s:if test='operateType.equals("2")'>
                            <td id="dataTd1" class="center"><input id="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTSFH01.changechkbox(this);"/></td>
                        </s:if>
                        <s:else>
                            <td id="dataTd1"><span><s:property value="#status.index+1"/></span></td>
                        </s:else>
                        
                        <td id="dataTd2"><span><s:property value="UnitCode"/></span></td>
                        <td id="dataTd3"><span><s:property value="BarCode"/></span></td>
                        <td id="dataTd4"><span><s:property value="ProductName"/></span></td>
                        
                        <td id="dataTd5" class="alignRight" style="width:10%;">
                            <s:if test='null!=Price'>
                                <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        
                        <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                           <td id="dataTd6" class="alignRight">
                                <s:if test='null!=OrderStock'>
                                    <s:text name="format.number"><s:param value="OrderStock"></s:param></s:text>
                                </s:if>
                                <s:else>0</s:else>
                            </td>
                        </s:if>
                        <td id="dataTd7" class="alignRight" >
                            <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                                <s:textfield name="quantityArr" cssClass="text-number" size="10" maxlength="4" value="%{Quantity}" onchange="BINOLWSMNG01.changeCount(this);" oninput="BINOLWSMNG01.changeCount(this);" cssStyle="width:120px;text-align:right;"></s:textfield>
                            </s:if>
                            <s:else>
                            <s:if test='null!=Quantity'>
                                <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                            </s:if>
                            </s:else>
                        </td>
                        <td id="dataTd8" class="alignRight">
                            <s:if test='null!=Price && null!=Quantity'>
                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
                            </s:if>
                            <s:else>0.00</s:else>
                        </td>
                        <td id="dataTd9">
                            <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                                <s:textfield name="commentsArr" size="25" maxlength="200" value="%{Comments}" cssClass=""></s:textfield>
                            </s:if>
                            <s:else>
                                <p style="margin-bottom:0;"><s:property value="Comments"/></p>
                            </s:else>
                        </td>
                        <td style="display:none" id="dataTd10">
                            <input type="hidden" name="referencePriceArr" value="<s:property value="Price"/>" />
                            <input type="hidden" id="priceUnitArr" name="priceUnitArr" value="<s:property value='Price'/>"/>                   
                            <input type="hidden" id="suggestedQuantityArr" name="suggestedQuantityArr" value="<s:property value='SuggestedQuantity'/>"/>
                            <input type="hidden" id="applyQuantityArr" name="applyQuantityArr" value="<s:property value='ApplyQuantity'/>"/>
                            <input type="hidden" id="productVendorPackageIDArr" name="productVendorPackageIDArr" value="<s:property value='BIN_ProductVendorPackageID'/>"/>
                            <input type="hidden" id="inventoryInfoIDArr" name="inventoryInfoIDArr" value="<s:property value='BIN_InventoryInfoID'/>"/>
                            <input type="hidden" id="logicInventoryInfoIDArr" name="logicInventoryInfoIDArr" value="<s:property value='BIN_LogicInventoryInfoID'/>"/>
                            <input type="hidden" id="storageLocationInfoIDArr" name="storageLocationInfoIDArr" value="<s:property value='BIN_StorageLocationInfoID'/>"/>
                            <input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
                            <input type="hidden" id="inventoryInfoIDAcceptArr" name="inventoryInfoIDAcceptArr" value="<s:property value='BIN_InventoryInfoIDAccept'/>"/>
                            <input type="hidden" id="logicInventoryInfoIDAcceptArr" name="logicInventoryInfoIDAcceptArr" value="<s:property value='BIN_LogicInventoryInfoIDAccept'/>"/>
                            <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
                        </td>
                    </tr>
                </s:iterator>
                    </s:if>
                    
                    </tbody>
                </table> 
                </div>
            
            
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
                <tr>
                    <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                    <td class="center"><s:text name="SFH01_totalQuantity"/></td><%-- 总数量 --%>
                    <td class="center"><s:text name="SFH01_totalAmount"/></td><%-- 总金额--%>
                </tr>
                <tr>
                    <td class="center">
	                    <%-- 总数量 --%>
	                    <s:if test='null!=productOrderMainData.TotalQuantity'>
	                        <span id="totalQuantity"><s:text name="format.number"><s:param value="productOrderMainData.TotalQuantity"></s:param></s:text></span>
	                    </s:if>
	                    <s:else><span id="totalQuantity">0</span></s:else>
                    </td>
                    <td class="center">
                        <%-- 总金额--%>
	                    <s:if test='null!=productOrderMainData.TotalAmount'>
	                        <span id="totalAmount"><s:text name="format.price"><s:param value="productOrderMainData.TotalAmount"></s:param></s:text></span>
	                    </s:if>
	                    <s:else><span id="totalAmount">0.00</span></s:else>
                    </td>
                </tr>
            </table>
            <hr class="space" />
            <div class="center clearfix">
                <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                	<button class="save" type="button" onclick="BINOLWSMNG01.saveExt();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="SFH01_Save"/></span></button>
                	<button class="confirm" type="button" onclick="BINOLWSMNG01.submitExt()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="SFH01_OK"/></span></button>
                </s:if>
                <s:else>
                	<button class="close" type="button" onclick="window.close();">
                        <span class="ui-icon icon-close"></span>
                        <span class="button-text"><s:text name="global.page.close"/></span>
                    </button>
                </s:else>
            </div>
          </div>
        </div>
      </div>
      </s:else>
      <div class="hide" id="dialogInit"></div>
<%-- ================== 弹出发货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出收货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/popDepartTableBusinessType.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/BINOLSTCM12.jsp" flush="true" />
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
</div>
</div>
</cherry:form>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="ESS00040"/>'/>
    <input type="hidden" id="errmsg4" value='<s:text name="ESS00042"/>'/>
    <input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
    <input type="hidden" id="notOnlyOneWarning" value='<s:text name="SFH01_notOnlyOne"/>'/>
    <input type="hidden" id="noOutDepart" value='<s:text name="SFH01_noOutDepart"/>'/>
    <input type="hidden" id="noOutDepot" value='<s:text name="SFH01_noOutDepot"/>'/>
    <input type="hidden" id="noInDepart" value='<s:text name="SFH01_noInDepart"/>'/>
    <input type="hidden" id="noInDepot" value='<s:text name="SFH01_noInDepot"/>'/>
    <input type="hidden" id="noInDepart" value='<s:text name="SFH01_noInDepart"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00039" value='<s:text name="EST00039"/>'/>
    <input type="hidden" id="errmsg_EST00040" value='<s:text name="EST00040"/>'/>
</div>
<s:if test='!operateType.equals("newBill")'>
<div id="firstDetailData" style="display:none">
<s:iterator value="productOrderDetailData" status="status" begin="0" end="0">	
  <input type="hidden" name="referencePriceArr" value="" />
  <input type="hidden" name="priceUnitArr" value=""/>                   
  <input type="hidden" name="suggestedQuantityArr" value=""/>
  <input type="hidden" name="applyQuantityArr" value=""/>
  <input type="hidden" name="productVendorPackageIDArr" value="<s:property value='BIN_ProductVendorPackageID'/>"/>
  <input type="hidden" name="inventoryInfoIDArr" value="<s:property value='BIN_InventoryInfoID'/>"/>
  <input type="hidden" name="logicInventoryInfoIDArr" value="<s:property value='BIN_LogicInventoryInfoID'/>"/>
  <input type="hidden" name="storageLocationInfoIDArr" value="<s:property value='BIN_StorageLocationInfoID'/>"/>
  <input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>
  <input type="hidden" name="inventoryInfoIDAcceptArr" value="<s:property value='BIN_InventoryInfoIDAccept'/>"/>
  <input type="hidden" name="logicInventoryInfoIDAcceptArr" value="<s:property value='BIN_LogicInventoryInfoIDAccept'/>"/>
  <input type="hidden" name="prtVendorId" value=""/>
</s:iterator>
</div>
<div id="firstDetailData2" style="display:none">
<s:iterator value="productOrderDetailData" status="status" begin="0" end="0">	
  <%-- <input type="hidden" name="referencePriceArr" value="" /> --%>
  <input type="hidden" name="suggestedQuantityArr" value=""/>
  <input type="hidden" name="applyQuantityArr" value=""/>
  <input type="hidden" name="productVendorPackageIDArr" value="<s:property value='BIN_ProductVendorPackageID'/>"/>
  <input type="hidden" name="inventoryInfoIDArr" value="<s:property value='BIN_InventoryInfoID'/>"/>
  <input type="hidden" name="logicInventoryInfoIDArr" value="<s:property value='BIN_LogicInventoryInfoID'/>"/>
  <input type="hidden" name="storageLocationInfoIDArr" value="<s:property value='BIN_StorageLocationInfoID'/>"/>
  <input type="hidden" name="inventoryInfoIDAcceptArr" value="<s:property value='BIN_InventoryInfoIDAccept'/>"/>
  <input type="hidden" name="logicInventoryInfoIDAcceptArr" value="<s:property value='BIN_LogicInventoryInfoIDAccept'/>"/>

</s:iterator>
</div>
</s:if>
</s:i18n>
<%-- ================== dataTable共通 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导 END  ======================= --%>
<script type="text/javascript">
$(function(){
    if (window.opener) {
        window.opener.lockParentWindow();
    }
	var operateType = '${operateType}';
	var bussinessDate = '${initInfoMap.bussinessDate}';
	$('#expectDeliverDate').cherryDate({
		minDate: bussinessDate
	});
	if(operateType == 'newBill') {
		//代码暂时未使用
		//BINOLWSMNG01.addNewLine();
	}
	if(operateType == '2') {
		BINOLWSMNG01.bindInput();
	}
});
window.onbeforeunload = function(){
    if (window.opener) {
        window.opener.unlockParentWindow();
    }
};
</script>