<%--退库单明细 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL12.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.st.BINOLSTBIL12">
<div class="main container clearfix">
<s:url id="doaction_url" value="/st/BINOLSTBIL12_doaction"/>
<s:url id="save_url" value="/st/BINOLSTBIL12_save"/>
<s:url id="submit_url" value="/st/BINOLSTBIL12_submit"/>
<s:url id="delete_url" value="/st/BINOLSTBIL12_delete"/>
<s:url id="url_getdepotAjax" value="/st/BINOLSTBIL12_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_getStockCount" value="/st/BINOLSTBIL12_getStockCount" />
<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>
<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="saveUrl" href="${save_url}"></a>
    <a id="submitUrl" href="${submit_url}"></a>
    <a id="deleteUrl" href="${delete_url}"></a>
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
</div>     
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL12_title"/>&nbsp;(<s:text name="BIL12_num"/>:<s:property value="productReturnMainData.ReturnNo"/>)</span>
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
          		<input type="hidden" name="billType" value='<s:property value="productReturnMainData.TradeType"/>'/>
          		<input type="hidden" name="pageId" value="BINOLSTBIL12"/>
          		<input type="hidden" name="billId" value="${productReturnID}"/>
	          	</div>
	          	<cherry:show domId="BINOLSTBIL12PNT">
	          	<button onclick="openPrintApp('Print');return false;" class="confirm right">
					<span class="ui-icon icon-file-print"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
				</button>
				</cherry:show>
				<cherry:show domId="BINOLSTBIL12VEW">
				<button onclick="openPrintApp('View');return false;" class="confirm right">
					<span class="ui-icon icon-file-view"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
				</button>
				</cherry:show>
                <cherry:show domId="BINOLSTBIL12EXP">
                <div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${productReturnID}"/></div>
                 <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTBIL12',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
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
                            <%-- 退库单号 --%>
                            <th><s:text name="BIL12_billNo"/></th>
                            <td><s:property value="productReturnMainData.ReturnNoIF"/></td>
                            <%-- 操作日期 --%>
                            <th><s:text name="BIL12_date"/></th>
                            <td><s:property value="productReturnMainData.ReturnDate"/></td>
                        </tr>
                        <tr>
                            <%-- 关联单号 --%>
                            <th><s:text name="BIL12_relevanceNo"/></th>
                            <td><s:property value="productReturnMainData.RelevanceNo"/></td>
                            <%-- 申请人 --%>
                            <th><s:text name="BIL12_employeeName"/></th>
                            <td><s:property value="productReturnMainData.EmployeeName"/></td>
                        </tr>
                        <tr>
                            <%-- 业务类型 --%>
                            <th><s:text name="BIL12_tradeType"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1144", productReturnMainData.TradeType)'/></td>
                            <th></th>
                            <td></td>
                        </tr>
                        <tr>
                            <%-- 退库理由 --%>
                            <th><s:text name="BIL12_reason"/></th>
                            <td colspan=3>
                                <s:property value='productReturnMainData.Reason'/>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <table class="detail">
                    <tbody>
                        <tr>
                            <%-- 退库部门 --%>
                            <th><s:text name="BIL12_DepartCodeName"/></th>
                            <td><s:property value="productReturnMainData.DepartCodeName"/></td>
                            <%-- 退库接收部门 --%>
                            <th><s:text name="BIL12_DepartCodeNameReceive"/></th>
                            <td><s:property value="productReturnMainData.DepartCodeNameReceive"/></td>
                        </tr>
                        <tr>
                            <s:text name="BIL12_selectAll" id="BIL12_selectAll"/>
                            <%-- 退库实体仓库 --%>
                            <th><s:text name="BIL12_DepotCodeName"/></th>
                            <td><s:property value="productReturnMainData.DepotCodeName"/></td>
                            <%-- 接收退库实体仓库 --%>
                            <th><s:text name="BIL12_DepotCodeNameReceive"/></th>
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_RR_AUDIT.equals(operateType)'>
                                <td>
                                    <s:if test='null!=depotsInfoList'>
                                        <div id="showAcceptDepotCodeName" class="hide">
                                            <s:select id="inDepotInfoId" name="inDepotInfoId" list="depotsInfoList" value="%{productReturnMainData.BIN_InventoryInfoIDReceive}" onchange="BINOLSTBIL12.setHideInventoryValue();" listKey="BIN_DepotInfoID" listValue="DepotCodeName" headerKey="" cssStyle="width:200px;">
                                            </s:select>
                                        </div>
                                        <div id="hideAcceptDepotCodeName">
                                            <s:property value="productReturnMainData.DepotCodeNameReceive"/>
                                        </div>
                                    </s:if>
                                </td>
                            </s:if>
                            <s:else>
                                <td><s:property value="productReturnMainData.DepotCodeNameReceive"/></td>
                            </s:else>
                        </tr>
                        <tr>
                            <%-- 退库逻辑仓库 --%>
                            <th><s:text name="BIL12_LogicInventoryName"/></th>
                            <td><s:property value="productReturnMainData.LogicInventoryCodeName"/></td>
                            <%-- 接收退库逻辑仓库 --%>
                            <th><s:text name="BIL12_LogicInventoryNameReceive"/></th>
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_RR_AUDIT.equals(operateType)'>
                                <td>
                                    <s:if test='null!=logicDepotsInfoList'>
                                        <div id="showAcceptLogicInventoryName" class="hide">
                                            <s:select id="inLogicDepotsInfoId" name="inLogicDepotsInfoId" list="logicDepotsInfoList" value="%{productReturnMainData.BIN_LogicInventoryInfoIDReceive}" onchange="BINOLSTBIL12.setHideInventoryValue();" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" cssStyle="width:200px;">
                                            </s:select>
                                        </div>
                                        <div id="hideAcceptLogicInventoryName">
                                            <s:property value="productReturnMainData.LogicInventoryCodeNameReceive"/>
                                        </div>
                                    </s:if>
                                </td>
                            </s:if>
                            <s:else>
                                <td><s:property value="productReturnMainData.LogicInventoryCodeNameReceive"/></td>
                            </s:else>         
                        </tr>

                    </tbody>
                </table>
                <table class="detail" style="margin-bottom: 5px;">
                    <tbody>
                        <tr>
                            <%-- 审核状态 --%>
                            <th><s:text name="BIL12_verifiedFlag"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1007", productReturnMainData.VerifiedFlag)'/></td>
                            <%-- 审核者 --%>
                            <th><s:text name="BIL12_employeeNameAudit"/></th>
                            <td><s:property value="productReturnMainData.AuditName"/></td>
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
                    <%-- 退库单明细一览 --%>
                    <s:text name="BIL12_results_list"/>
                </strong>
            </div>
            <div class="section-content">
            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_RR_AUDIT.equals(operateType)'>
                <div id="showToolbar" class="hide">
                    <div class="toolbar clearfix">
                        <span class="left">
                            <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                            <a class="add" onclick="BINOLSTBIL12.openProPopup(this);">
                                <span class="ui-icon icon-add"></span>
                                <span class="button-text"><s:text name="BIL12_add"/></span>
                            </a>
                            <a class="delete" onclick="BINOLSTBIL12.deleteRow();">
                                <span class="ui-icon icon-delete"></span>
                                <span class="button-text"><s:text name="BIL12_delete"/></span>
                            </a>
                        </span>
                    </div>
                </div>
            </s:if>
            <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
            <input type="hidden" id="entryID" name="entryID" value='<s:property value="productReturnMainData.WorkFlowID"/>'/>
            <input type="hidden" id="actionID" name="actionID"/>
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                <thead>
                    <tr>
                        <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_RR_AUDIT.equals(operateType)'>
                            <th id="showCheckbox" class="center hide" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTBIL12.selectAll();"/><s:text name="BIL12_select"/></th><%-- 选择 --%>
                        </s:if>
                        <th class="center"><s:text name="BIL12_no"/></th><%-- 编号 --%>
                        <th class="center"><s:text name="BIL12_UnitCode"/></th><%-- 厂商编码 --%>
                        <th class="center"><s:text name="BIL12_BarCode"/></th><%-- 产品条码 --%> 
                        <th class="center"><s:text name="BIL12_ProductName"/></th><%-- 产品名称 --%>
                        <th class="center"><s:text name="BIL12_Price"/></th><%-- 单价 --%>
                        <th class="center"><s:text name="BIL12_costPrice"/></th><%-- 成本价 --%>
                        <th class="center"><s:text name="BIL12_totalCostPrice"/></th><%-- 总成本价 --%>
                        <th class="center"><s:text name="BIL12_Quantity"/></th><%-- 数量 --%>
                        <th class="center"><s:text name="BIL12_Amount"/></th><%-- 金额 --%>
                        <th class="center"><s:text name="BIL12_remark"/></th><%-- 备注 --%>
                    </tr>
                </thead>
                <tbody id="databody">
                    <s:iterator value="productReturnDetailData" status="status">
                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                        <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_RR_AUDIT.equals(operateType)'>
                            <td id="dataTd0" class="hide"><input id="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTBIL12.changechkbox(this);"/></td>
                        </s:if>
                        <td id="dataTd1"><s:property value="#status.index+1"/></td>
                        <td id="dataTd2"><span><s:property value="UnitCode"/></span></td>
                        <td id="dataTd4"><span><s:property value="BarCode"/></span></td>
                        <td id="dataTd3"><span><s:property value="ProductName"/></span></td>                        
                        <td id="dataTd5" class="alignRight">
                            <s:if test='null!=Price'>
                                <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        
                        <s:if test='null!=totalCostPrice1 && ""!=totalCostPrice1'>
                         	<td>${costPrice1}</td>
                         	<td>${totalCostPrice1}</td>
                        </s:if>
                        <s:else>
                        	<td></td>
                        	<td></td>
                        </s:else>
                        
                        <td id="newCount" class="alignRight" style="width:10%;">
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_RR_AUDIT.equals(operateType)'>
                                <s:textfield name="quantityArr" cssClass="text-number hide" size="10" maxlength="9" value="%{Quantity}" onchange="BINOLSTBIL12.changeCount(this);"></s:textfield>
                                <div id="hideQuantiyArr">
                                    <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                                </div>
                            </s:if>
                            <s:else>
                                <s:if test='null!=Quantity'>
                                    <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                                    <s:hidden name="quantityArr" value="%{Quantity}"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </s:else>
                        </td>
                        <td id="money" class="alignRight">
                            <s:if test='null!=Price && null!=Quantity'>
                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="dataTd9">
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_RR_AUDIT.equals(operateType)'>
                                <s:textfield name="reasonArr" size="25" maxlength="200" value="%{Reason}" cssClass="hide" cssStyle="width:98%"></s:textfield>
                                <div id="hideReason">
                                    <p><s:property value="Reason"/></p>
                                </div>
                            </s:if>
                            <s:else>
                                <p><s:property value="Reason"/></p>
                            </s:else>
                        </td>
                        <td style="display:none" id="dataTd10">
                            <input type="hidden" id="priceUnitArr<s:property value='#status.index+1'/>" name="priceUnitArr" value="<s:property value='Price'/>"/>                   
                            <input type="hidden" id="productVendorPackageIDArr<s:property value='#status.index+1'/>" name="productVendorPackageIDArr" value="<s:property value='BIN_ProductVendorPackageID'/>"/>
                            <input type="hidden" id="inventoryInfoIDArr<s:property value='#status.index+1'/>" name="inventoryInfoIDArr" value="<s:property value='BIN_InventoryInfoID'/>"/>
                            <input type="hidden" id="logicInventoryInfoIDArr<s:property value='#status.index+1'/>" name="logicInventoryInfoIDArr" value="<s:property value='BIN_LogicInventoryInfoID'/>"/>
                            <input type="hidden" id="storageLocationInfoIDArr<s:property value='#status.index+1'/>" name="storageLocationInfoIDArr" value="<s:property value='BIN_StorageLocationInfoID'/>"/>
                            <input type="hidden" id="productVendorIDArr<s:property value='#status.index+1'/>" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
                            <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
                        </td>
                    </tr>
                    </s:iterator>
                </tbody>
            </table>
            </div>
            <div style="display:none">
                <input type="hidden" id="rowNumber" value="<s:property value='productReturnDetailData.size()'/>"/>
                <input type="hidden" value="<s:property value="productReturnMainData.UpdateTime"/>" name="updateTime" id="updateTime">
                <input type="hidden" value="<s:property value="productReturnMainData.ModifyCount"/>" name="modifyCount" id="modifyCount">
                <input type="hidden" value="<s:property value="productReturnMainData.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
                <input type="hidden" value="<s:property value="productReturnMainData.BIN_OrganizationID"/>" id="organizationID" name="organizationID">
                <input type="hidden" value="<s:property value="productReturnMainData.BIN_OrganizationIDReceive"/>" id="organizationIDReceive" name="organizationIDReceive">
                <input type="hidden" value="<s:property value="productReturnMainData.BIN_DepotInfoIDReceive"/>" id="inventoryInfoIDReceive" name="inventoryInfoIDReceive">
                <input type="hidden" value="<s:property value="productReturnMainData.BIN_LogicInventoryInfoIDReceive"/>" id="logicInventoryInfoIDReceive" name="logicInventoryInfoIDReceive">
                <s:hidden id="productReturnID" name="productReturnID" value="%{#request.productReturnID}"></s:hidden>
            </div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
                <tr>
                    <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                    <td class="center"><s:text name="BIL12_totalQuantity"/></td><%-- 总数量 --%>
                    <td class="center"><s:text name="BIL12_totalAmount"/></td><%-- 总金额--%>
                </tr>
                <tr>
                    <td class="center">
                        <%-- 总数量 --%>
                        <s:if test='null!=productReturnMainData.TotalQuantity'>
                            <span id="totalQuantity"><s:text name="format.number"><s:param value="productReturnMainData.TotalQuantity"></s:param></s:text></span>
                        </s:if>
                        <s:else>&nbsp;</s:else>
                    </td>
                    <td class="center">
                        <%-- 总金额--%>
                        <s:if test='null!=productReturnMainData.TotalAmount'>
                            <span id="totalAmount"><s:text name="format.price"><s:param value="productReturnMainData.TotalAmount"></s:param></s:text></span>
                        </s:if>
                        <s:else>&nbsp;</s:else>
                    </td>
                </tr>
            </table>
            <hr class="space" />
            <div class="center">
            <s:if test='@com.cherry.cm.core.CherryConstants@OPERATE_RR_AUDIT.equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_RR_EDIT.equals(operateType)'>
                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
            </s:if>
            <button id="btnReturn" class="close" onclick="BINOLSTBIL12.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
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
<form action="BINOLSTBIL12_init" id="productReturnDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="productReturnID" value="%{#request.productReturnID}"></s:hidden>
</form>
<%-- ================== 报表打印共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
<%-- ==================  报表打印共通导入 End ======================== --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>