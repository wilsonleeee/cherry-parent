<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%--调拨申请单明细 --%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL18.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.st.BINOLSTBIL18">
<div class="main container clearfix">
<div class="hide">
    <s:url id="doaction_url" value="/st/BINOLSTBIL18_doaction"/>
    <s:url id="save_url" value="/st/BINOLSTBIL18_save"/>
    <s:url id="submit_url" value="/st/BINOLSTBIL18_submit"/>
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="saveUrl" href="${save_url}"></a>
    <a id="submitUrl" href="${submit_url}"></a>
    <s:url id="url_getStockCount" value="/st/BINOLSTBIL18_getStockCount" />
	<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
    <s:text name="BIL18_selectAll" id="BIL18_selectAll"/>
</div>
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL18_title"/>&nbsp;(<s:text name="BIL18_num"/>:<s:property value="productAllocationMainData.AllocationrNo"/>)</span>
        </div>
    </div>
    <div class="tabs">
        <ul>
            <li><a href="#tabs-1"><s:text name="global.page.title"/></a></li><%-- 基本信息 --%>
            <li><a href="#tabs-2" onclick="clickTab_2WorkFlow();return false;"><s:text name="global.page.work"/></a>
            </li><%-- 单据流程 --%>
        </ul>
        <div id="tabs-1" class="panel-content">
            <form id="mainForm" method="post" class="inline">
                <%--防止有button的form在text框输入后按Enter键后自动submit --%>
                <button type="submit" onclick="return false;" class="hide"></button>
                <div class="section">
                <div id="actionResultDisplay"></div>
                <%-- ================== 错误信息提示 START ======================= --%>
                <div id="errorDiv2" class="actionError" style="display:none">
                    <ul>
                        <li><span id="errorSpan2"></span></li>
                    </ul>
                </div>
                <%-- ================== 错误信息提示   END  ======================= --%>
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-info"></span>
                        <%-- 基本信息 --%>
                        <s:text name="global.page.title"/>
                    </strong>
                    <div id="print_param_hide" class="hide">
                    <input type="hidden" name="billType" value='BG'/>
                    <input type="hidden" name="pageId" value="BINOLSTBIL18"/>
                    <input type="hidden" name="billId" value="${productAllocationID}"/>
                    </div>
                    <cherry:show domId="BINOLSTBIL18PNT">
                    <button onclick="openPrintApp('Print');return false;" class="confirm right">
                        <span class="ui-icon icon-file-print"></span>
                        <span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
                    </button>
                    </cherry:show>
                    <cherry:show domId="BINOLSTBIL18VEW">
                    <button onclick="openPrintApp('View');return false;" class="confirm right">
                        <span class="ui-icon icon-file-view"></span>
                        <span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
                    </button>
                    </cherry:show>
                    <cherry:show domId="BINOLSTBIL18EXP">
                    <div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${productAllocationID}"/></div>
                    <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTBIL18',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
                        <span class="ui-icon icon-file-export"></span>
                        <span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
                    </button>
                    </cherry:show>
                </div>
                <div class="section-content">
                <div>
                    <div class="box-header"></div>
                    <table class="detail">
                        <tbody>
                            <tr>
                                <%-- 调拨申请单号 --%>
                                <th><s:text name="BIL18_allocationrNo"/></th>
                                <td><s:property value="productAllocationMainData.AllocationNoIF"/></td>
                                <%-- 操作日期 --%>
                                <th><s:text name="BIL18_date"/></th>
                                <td><s:property value="productAllocationMainData.Date"/></td>
                            </tr>
                            <tr>
                                <%-- 关联单号 --%>
                                <th><s:text name="BIL18_relevanceNo"/></th>
                                <td><s:property value="productAllocationMainData.RelevanceNo"/></td>
                                <%-- 申请人 --%>
                                <th><s:text name="BIL18_employeeName"/></th>
                                <td><s:property value="productAllocationMainData.EmployeeName"/></td>
                            </tr>
                            <tr>
                                <%-- 调拨备注 --%>
                                <th><s:text name="BIL18_comments"/></th>
                                <td colspan=3>
                                    <s:property value='productAllocationMainData.Comments'/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <table class="detail">
                        <tbody>
                            <tr>
                                <%-- 调入部门 --%>
                                <th><s:text name="BIL18_DepartCodeName"/></th>
                                <td><s:property value="productAllocationMainData.DepartCodeNameIn"/></td>
                                <%-- 调出部门 --%>
                                <th><s:text name="BIL18_DepartCodeNameOut"/></th>
                                <td>
                                   <s:property value="productAllocationMainData.DepartCodeNameOut"/>
                                </td>
                            </tr>
                            <s:if test='!"80".equals(operateType)'>
                            <tr>
                                <%-- 调入实体仓库 --%>
                                <th><s:text name="BIL18_DepotCodeName"/></th>
                                <td>
                                    <s:if test='"2".equals(operateType) || "76".equals(operateType)'>
                                        <s:property value="productAllocationMainData.DepotCodeNameIn"/>
                                        <input id="inventoryInfoIDIn" name="inventoryInfoIDIn" type="hidden" value="<s:property value="productAllocationMainData.BIN_InventoryInfoIDIn"/>">
                                    </s:if>
                                    <s:else>
                                        <div id="showDepotCodeNameIn" class="hide">
                                            <s:if test='null != depotsInfoListIn && depotsInfoListIn.size()>0'>
                                                <s:select id="inventoryInfoIDIn" name="inventoryInfoIDIn" list="depotsInfoListIn"
                                                    value="%{productAllocationMainData.BIN_InventoryInfoIDIn}"
                                                    listKey="BIN_DepotInfoID" listValue="DepotCodeName" headerKey="" cssStyle="width:200px;">
                                                </s:select>
                                            </s:if>
                                        </div>
                                        <div id="hideDepotCodeNameIn">
                                            <s:property value="productAllocationMainData.DepotCodeNameIn"/>
                                        </div>
                                    </s:else>
                                </td>
                                <%-- 调入逻辑仓库 --%>
                                <th><s:text name="BIL18_LogicInventoryName"/></th>
                                <td>
                                    <s:if test='"2".equals(operateType) || "76".equals(operateType)'>
                                        <s:property value="productAllocationMainData.LogicInventoryCodeNameIn"/>
                                        <input id="logicInventoryInfoIDIn" name="logicInventoryInfoIDIn" type="hidden" value="<s:property value="productAllocationMainData.BIN_LogicInventoryInfoIDIn"/>">
                                    </s:if>
                                    <s:else>
                                        <div id="showLogicInventoryCodeNameIn" class="hide">
                                            <s:if test='null != logicDepotsInfoListIn && logicDepotsInfoListIn.size()>0'>
                                                <s:select id="logicInventoryInfoIDIn" name="logicInventoryInfoIDIn" list="logicDepotsInfoListIn"
                                                    value="%{productAllocationMainData.BIN_LogicInventoryInfoIDIn}"
                                                    listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" cssStyle="width:200px;">
                                                </s:select>
                                            </s:if>
                                        </div>
                                        <div id="hideLogicInventoryCodeNameIn">
                                            <s:property value="productAllocationMainData.LogicInventoryCodeNameIn"/>
                                        </div>
                                    </s:else>
                                </td>
                            </tr>
                            </s:if>
                            <s:if test='"80".equals(operateType)'>
                            <tr>
                                <%-- 调出实体仓库 --%>
                                <th><s:text name="BIL18_DepotCodeNameOut"/></th>
                                <td>
                                    <s:select id="inventoryInfoIDOut" name="inventoryInfoIDOut" list="depotsInfoListOut" onchange="BINOLSTBIL18.refreshStockCount();return false;" 
                                        listKey="BIN_DepotInfoID" listValue="DepotCodeName" headerKey="" cssStyle="width:200px;">
                                    </s:select>
                                </td>
                                <%-- 调出逻辑仓库 --%>
                                <th><s:text name="BIL18_LogicInventoryNameOut"/></th>
                                <td>
                                    <s:select id="logicInventoryInfoIDOut" name="logicInventoryInfoIDOut" list="logicDepotsInfoListOut" onchange="BINOLSTBIL18.refreshStockCount();return false;" 
                                        listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" cssStyle="width:200px;">
                                    </s:select>
                                </td>
                            </tr>
                            </s:if>
                            <s:if test='"76".equals(operateType) && auditLGFlag.equals("YES")'>
                                <tr>
	                                <%-- 调出实体仓库 --%>
	                                <th><s:text name="BIL18_DepotCodeNameOut"/></th>
	                                <td>
	                                    <s:property value="productAllocationMainData.DepotCodeNameOut"/>
	                                    <input type="hidden" id="inventoryInfoIDOut" name="inventoryInfoIDOut" value='<s:property value="productAllocationMainData.BIN_InventoryInfoIDOut"/>'/>
	                                </td>
	                                <%-- 调出逻辑仓库 --%>
	                                <th><s:text name="BIL18_LogicInventoryNameOut"/></th>
	                                <td>
	                                    <s:property value="productAllocationMainData.LogicInventoryCodeNameOut"/>
	                                    <input type="hidden" id="logicInventoryInfoIDOut" name="logicInventoryInfoIDOut" value='<s:property value="productAllocationMainData.BIN_LogicInventoryInfoIDOut"/>'/>
	                                </td>
                                </tr>
                            </s:if>
                        </tbody>
                    </table>
                    <table class="detail" style="margin-bottom: 5px;">
                        <tbody>
                            <tr>
                                <%-- 审核状态 --%>
                                <th><s:text name="BIL18_verifiedFlag"/></th>
                                <td><s:property value='#application.CodeTable.getVal("1007", productAllocationMainData.VerifiedFlag)'/></td>
                                <%-- 审核者 --%>
                                <th><s:text name="BIL18_employeeNameAudit"/></th>
                                <td><s:property value="productAllocationMainData.EmployeeNameAudit"/></td>
                            </tr>
                            <tr>
                                <%-- 处理状态 --%>
                                <th><s:text name="BIL18_tradeStatus"/></th>
                                <td><s:property value='#application.CodeTable.getVal("1200", productAllocationMainData.TradeStatus)'/></td>
                                <th></th>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="clearfix"></div>
                </div>
              </div>
            </div>
            <div class="section">
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-search-result"></span>
                        <%-- 调拨申请单明细一览 --%>
                        <s:text name="BIL18_results_list"/>
                    </strong>
                </div>
                <div class="section-content">
                <s:if test='"2".equals(operateType) || "76".equals(operateType) || "70".equals(operateType) || "80".equals(operateType)'>
                    <div id="showToolbar" class="hide">
                    <div class="toolbar clearfix">
                        <span class="left">
                            <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                            <a class="add" onclick="BINOLSTBIL18.openProPopup(this);">
                                <span class="ui-icon icon-add"></span>
                                <span class="button-text"><s:text name="BIL18_add"/></span>
                            </a>
                            <a class="delete" onclick="BINOLSTBIL18.deleteRow();">
                                <span class="ui-icon icon-delete"></span>
                                <span class="button-text"><s:text name="BIL18_delete"/></span>
                            </a>
                        </span>
                    </div>
                    </div>
                </s:if>
                <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
                <input type="hidden" id="entryID" name="entryID" value='<s:property value="productAllocationMainData.WorkFlowID"/>'/>
                <input type="hidden" id="actionID" name="actionID"/>
                <input type="hidden" id="entryid" name="entryid"/>
                <input type="hidden" id="actionid" name="actionid"/>
                <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
                <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                        <tr>
                            <s:if test='"2".equals(operateType) || "76".equals(operateType) || "70".equals(operateType) || "80".equals(operateType)'>
                                <th id="showCheckbox" class="center hide" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTBIL18.selectAll();"/><s:text name="BIL18_select"/></th>
                            </s:if>
                            <th class="center"><s:text name="BIL18_no"/></th><%-- 编号 --%>
                            <th class="center"><s:text name="BIL18_UnitCode"/></th><%-- 厂商编码 --%>
                            <th class="center"><s:text name="BIL18_BarCode"/></th><%-- 产品条码 --%> 
                            <th class="center"><s:text name="BIL18_ProductName"/></th><%-- 产品名称 --%>
                            <th class="center"><s:text name="BIL18_Price"/></th><%-- 单价 --%>
                            <s:if test='"80".equals(operateType) || ("76".equals(operateType) && auditLGFlag.equals("YES"))'>
                            	<!-- 调出或者审核时显示调出方库存数量 -->
                            	<th id="th_ProductQuantity" class="center"><s:text name="BIL18_ProductQuantity"/></th><%-- 调出数量 --%>
                            </s:if>
                            <s:if test='"76".equals(operateType)'>
                                <%--审核 --%>
                                <th class="center"><s:text name="BIL18_ApplyQuantity"/></th><%-- 申请数量 --%>
                                <s:if test='auditLGFlag.equals("YES")'>
                                    <th class="center"><s:text name="BIL18_LGQuantity"/></th><%-- 调出数量 --%>
                                </s:if>
                                <th class="center"><s:text name="BIL18_CheckQuantity"/></th><%-- 核定数量 --%>
                            </s:if>
                            <s:elseif test='"70".equals(operateType)'>
                                <%--调入确认--%>
                                <th class="center"><s:text name="BIL18_Quantity"/></th><%-- 调拨数量 --%>
                                <th class="center"><s:text name="BIL18_ReceiveQuantity"/></th><%-- 实收数量 --%>
                            </s:elseif>
                            <s:elseif test='"80".equals(operateType)'>
                                <%--调出确认--%>
                                <th class="center"><s:text name="BIL18_ApplyQuantity"/></th><%-- 申请数量 --%>
                                <th class="center"><s:text name="BIL18_LGQuantity"/></th><%-- 调出数量 --%>
                            </s:elseif>
                            <s:else>
                                <th class="center"><s:text name="BIL18_ApplyQuantity"/></th><%-- 申请数量 --%>
                            </s:else>                      
                            <th class="center"><s:text name="BIL18_Amount"/></th><%-- 金额 --%>
                            <th class="center"><s:text name="BIL18_remark"/></th><%-- 备注 --%>
                        </tr>
                    </thead>
                    <tbody id="databody">
                        <s:iterator value="productAllocationDetailData" status="status">
                        <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                            <s:if test='"2".equals(operateType) || "76".equals(operateType) || "70".equals(operateType) || "80".equals(operateType)'>
                                <td id="dataTd0" class="hide"><input id="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTBIL18.changechkbox(this);"/></td>
                            </s:if>
                            <td id="dataTd1"><s:property value="#status.index+1"/></td>
                            <td id="dataTd2"><span><s:property value="UnitCode"/></span></td>
                            <td id="dataTd3"><span><s:property value="BarCode"/></span></td>
                            <td id="dataTd4"><span><s:property value="ProductName"/></span></td>
                            <td id="dataTd5" class="alignRight">
                                <s:if test='null!=Price'>
                                    <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <s:if test='"80".equals(operateType) || ("76".equals(operateType) && auditLGFlag.equals("YES"))'>
                            	<!-- 调出或者调出审核时需要显示调出方的库存数量 -->
                            	<td id="dataTdStock" class="alignRight">
                            	<s:if test='null!=ProductQuantity'>
                            		<s:text name="format.number"><s:param value="ProductQuantity"></s:param></s:text>
                            	</s:if>
                            	</td>
                            </s:if>
                            <s:if test='"76".equals(operateType) || "70".equals(operateType) || "80".equals(operateType)'>
	                            <s:if test='auditLGFlag.equals("YES")'>
	                            	<!-- 审核步骤,审核调出单-->
	                                <td id="dataTdApplyQuantity" class="alignRight">
	                                    <s:if test='null != Quantity'>
	                                        <s:text name="format.number"><s:param value="ApplyQuantity"></s:param></s:text>
	                                    </s:if>
	                                    <s:else>&nbsp;</s:else>
	                                </td>
	                                <td id="dataTd6" class="alignRight">
	                                    <s:if test='null != Quantity'>
	                                        <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
	                                    </s:if>
	                                    <s:else>&nbsp;</s:else>
	                                </td>
                                </s:if>
                                <s:else>
                                    <td id="dataTd6" class="alignRight">
                                        <s:if test='null != Quantity'>
                                            <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                                        </s:if>
                                        <s:else>&nbsp;</s:else>
                                    </td>
                                </s:else>
	                            <td id="newCount" class="alignRight" style="width:10%;">
	                                <s:if test='"76".equals(operateType) || "70".equals(operateType) || "80".equals(operateType)'>
	                                    <s:textfield name="quantityArr" cssClass="text-number hide" size="10" maxlength="9" value="%{Quantity}" onchange="BINOLSTBIL18.changeCount(this);"></s:textfield>
	                                    <div id="hideQuantiyArr">
	                                        <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
	                                    </div>
	                                </s:if>
	                            </td>
                            </s:if>
                            <s:elseif test='"2".equals(operateType)'>
                                <td id="newCount" class="alignRight" style="width:10%;">
                                    <s:textfield name="quantityArr" cssClass="text-number hide" size="10" maxlength="9" value="%{Quantity}" onchange="BINOLSTBIL18.changeCount(this);"></s:textfield>
                                    <div id="hideQuantiyArr">
                                        <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                                    </div>
                                </td>
                            </s:elseif>
                            <s:else>
                                <td id="dataTd6" class="alignRight">
                                    <s:if test='null != Quantity'>
                                        <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                                    </s:if>
                                    <s:else>&nbsp;</s:else>
                                </td>
                            </s:else>
	                        <td id="money" class="alignRight">
	                            <s:if test='null!=Price && null != Quantity'>
	                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
	                            </s:if>
	                            <s:else>&nbsp;</s:else>
	                        </td>
	                        <td id="dataTd9">
	                            <s:if test='"2".equals(operateType) || "76".equals(operateType) || "70".equals(operateType) || "80".equals(operateType)'>
	                                <s:textfield name="commentsArr" size="25" maxlength="200" value="%{Comments}" cssClass="hide" cssStyle="width:98%"></s:textfield>
	                                <div id="hideReason">
	                                    <p><s:property value="Comments"/></p>
	                                </div>
	                            </s:if>
	                            <s:else>
	                                <p><s:property value="Comments"/></p>
	                            </s:else>
	                        </td>
	                        <td style="display:none" id="dataTd10">
	                            <input type="hidden" id="priceUnitArr<s:property value='#status.index+1'/>" name="priceUnitArr" value="<s:property value='Price'/>"/>
	                            <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
	                        </td>
	                    </tr>
	                    </s:iterator>
	                </tbody>
	            </table>
	            </div>
	            <div style="display:none">
	                <input type="hidden" id="rowNumber" value="<s:property value='productAllocationDetailData.size()'/>"/>
	                <input type="hidden" id="inTestType" value="<s:property value='productAllocationDetailData.TestType'/>">
	                <input type="hidden" id="updateTime" name="updateTime" value="<s:property value="productAllocationMainData.UpdateTime"/>">
	                <input type="hidden" id="modifyCount" name="modifyCount" value="<s:property value="productAllocationMainData.ModifyCount"/>">
	                <input type="hidden" id="verifiedFlag" name="verifiedFlag" value="<s:property value="productAllocationMainData.VerifiedFlag"/>">
	                <input type="hidden" id="organizationID" name="organizationID" value="<s:property value="productAllocationMainData.BIN_OrganizationID"/>" >
	                <input type="hidden" id="inventoryInfoID" name="inventoryInfoID" value="<s:property value="productAllocationMainData.BIN_InventoryInfoID"/>">
	                <input type="hidden" id="logicInventoryInfoID" name="logicInventoryInfoID" value="<s:property value="productAllocationMainData.BIN_LogicInventoryInfoID"/>">
	                <s:hidden id="productAllocationID" name="productAllocationID" value="%{#request.productAllocationID}"></s:hidden>
	                <input type="hidden" id="auditLGFlag" name="auditLGFlag" value='<s:property value="auditLGFlag"/>'/>
	                <input type="hidden" id="productAllocationOutID" name="productAllocationOutID" value='<s:property value="productAllocationOutID"/>'/>
	                <s:if test='"80".equals(operateType)'>
                        <input type="hidden" id="inventoryInfoIDIn" name="inventoryInfoIDIn" value='<s:property value="productAllocationMainData.BIN_InventoryInfoIDIn"/>'/>
                        <input type="hidden" id="logicInventoryInfoIDIn" name="logicInventoryInfoIDIn" value='<s:property value="productAllocationMainData.BIN_LogicInventoryInfoIDIn"/>'/>
	                </s:if>
	            </div>
	            <hr class="space" />
	            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
	                <tr>
	                    <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
	                    <td class="center"><s:text name="BIL18_totalQuantity"/></td><%-- 总数量 --%>
                        <td class="center"><s:text name="BIL18_totalAmount"/></td><%-- 总金额--%>
	                </tr>
	                <tr>
                        <td class="center">
                            <%-- 总数量 --%>
                            <s:if test='null!=productAllocationMainData.TotalQuantity'>
                                <span id="totalQuantity"><s:text name="format.number"><s:param value="productAllocationMainData.TotalQuantity"></s:param></s:text></span>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td class="center">
                            <%-- 总金额--%>
                            <s:if test='null!=productAllocationMainData.TotalAmount'>
                                <span id="totalAmount"><s:text name="format.price"><s:param value="productAllocationMainData.TotalAmount"></s:param></s:text></span>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
	                </tr>
	            </table>
	            <hr class="space" />
	            <div class="center">
	            <s:if test='"2".equals(operateType)'>
	                <button id="btnSave" class="save" onclick="BINOLSTBIL18.saveForm();return false;" style="display:none;"><span class="ui-icon icon-save"></span>
	                    <%-- 保存 --%>
	                    <span class="button-text"><s:text name="global.page.save"/></span>
	                </button>
                    <button class="confirm" onclick="BINOLSTBIL18.submitForm();return false;"><span class="ui-icon icon-confirm"></span>
                        <%-- 提交 --%>
                        <span class="button-text"><s:text name="global.page.submit"/></span>
                    </button>
                    <button id="btn-icon-edit-big" class="confirm" onclick="BINOLSTBIL18.modifyForm();return false;">
                        <span class="ui-icon icon-edit-big"></span>
                        <%-- 修改 --%>
                        <span class="button-text"><s:text name="os.edit"/></span>
                    </button>
	            </s:if>
	            <s:elseif test='@com.cherry.cm.core.CherryConstants@OPERATE_AC_AUDIT.equals(operateType) 
	                           || @com.cherry.cm.core.CherryConstants@OPERATE_BG.equals(operateType) 
	                           || @com.cherry.cm.core.CherryConstants@OPERATE_LG.equals(operateType) '>
	                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
	            </s:elseif>
	            <button id="btnReturn" class="close" onclick="BINOLSTBIL18.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
	                <%--返回 --%>
	                <span class="button-text"><s:text name="global.page.back"/></span>
	            </button>
	            <button class="close" onclick="window.close();return false;">
	                <span class="ui-icon icon-close"></span>
	                <%-- 关闭 --%>
	                <span class="button-text"><s:text name="global.page.close"/></span>
	            </button>
	            </div>
	          </div>
	        </div>
	        </form>
	    </div>
        <div id="tabs-2">
            <b><s:text name="global.page.worksProcessing"/></b>
        </div>
    </div>
</div>
</div>
</s:i18n>
<form action="BINOLSTBIL18_init" id="productAllocationDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="productAllocationID" value="%{#request.productAllocationID}"></s:hidden>
</form>
<%-- ================== 报表打印共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
<%-- ==================  报表打印共通导入 End ======================== --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
</div>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 工作流提示弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOSDialog.jsp" flush="true" />
<%-- ==================  工作流提示弹出窗口End ======================== --%>