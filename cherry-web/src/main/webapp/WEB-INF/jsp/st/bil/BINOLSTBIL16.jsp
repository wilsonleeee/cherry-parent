<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<%--盘点申请单明细 --%>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL16.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.st.BINOLSTBIL16">
<div class="main container clearfix">
<s:url id="doaction_url" value="/st/BINOLSTBIL16_doaction"/>
<s:url id="save_url" value="/st/BINOLSTBIL16_save"/>
<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="saveUrl" href="${save_url}"></a>
    <s:url id="url_getdepotAjax" value="/st/BINOLSTSFH03_getDepot" />
    <span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
    <span id ="defaultHandleType" style="display:none"><s:property value='#application.CodeTable.getVal("1020", "2")'/></span>
    <input type="hidden" id="allowNegativeFlag" name="allowNegativeFlag" value='<s:property value="allowNegativeFlag" />'/>
</div>
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL16_title"/>&nbsp;(<s:text name="BIL16_num"/>:<s:property value="proStocktakeRequestMainData.StockTakingNo"/>)</span>
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
	                <input type="hidden" name="billType" value='<s:property value="proStocktakeRequestMainData.TradeType"/>'/>
	                <input type="hidden" name="pageId" value="BINOLSTBIL16"/>
	                <input type="hidden" name="billId" value="${proStocktakeRequestID}"/>
	                </div>
	                <cherry:show domId="BINOLSTBIL16PNT">
	                <button onclick="openPrintApp('Print');return false;" class="confirm right">
	                    <span class="ui-icon icon-file-print"></span>
	                    <span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
	                </button>
	                </cherry:show>
	                <cherry:show domId="BINOLSTBIL16VEW">
	                <button onclick="openPrintApp('View');return false;" class="confirm right">
	                    <span class="ui-icon icon-file-view"></span>
	                    <span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
	                </button>
	                </cherry:show>
	                <cherry:show domId="BINOLSTBIL16EXP">
	                <div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${proStocktakeRequestID}"/></div>
	                <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTBIL16',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
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
	                            <%-- 盘点申请单号 --%>
	                            <th><s:text name="BIL16_stockTakingNo"/></th>
	                            <td><s:property value="proStocktakeRequestMainData.StockTakingNoIF"/></td>
	                            <%-- 操作日期 --%>
	                            <th><s:text name="BIL16_tradeDateTime"/></th>
	                            <td><s:property value="proStocktakeRequestMainData.TradeDateTime"/></td>
	                        </tr>
	                        <tr>
	                            <%-- 关联单号 --%>
	                            <th><s:text name="BIL16_relevanceNo"/></th>
	                            <td><s:property value="proStocktakeRequestMainData.RelevanceNo"/></td>
	                            <%-- 申请人 --%>
	                            <th><s:text name="BIL16_employeeName"/></th>
	                            <td><s:property value="proStocktakeRequestMainData.EmployeeName"/></td>
	                        </tr>
	                        <tr>
	                            <%-- 业务类型 --%>
	                            <th><s:text name="BIL16_tradeType"/></th>
	                            <td><s:property value='#application.CodeTable.getVal("1144", proStocktakeRequestMainData.TradeType)'/></td>
	                            <th><s:text name="BIL16_StocktakeType"/></th>
	                            <td><s:property value='#application.CodeTable.getVal("1054", proStocktakeRequestMainData.StocktakeType)'/></td>
	                        </tr>
	                        <tr>
	                            <%-- 盘点理由 --%>
	                            <th><s:text name="BIL16_reason"/></th>
	                            <td colspan=3>
	                                <s:property value='proStocktakeRequestMainData.Comments'/>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	                <table class="detail">
	                    <tbody>
	                        <tr>
	                            <%-- 盘点部门 --%>
	                            <th><s:text name="BIL16_DepartCodeName"/></th>
	                            <td><s:property value="proStocktakeRequestMainData.DepartCodeName"/></td>
	                            <th></th>
	                            <td></td>
	                        </tr>
	                        <tr>
	                            <s:text name="BIL16_selectAll" id="BIL16_selectAll"/>
	                            <%-- 盘点实体仓库 --%>
	                            <th><s:text name="BIL16_DepotCodeName"/></th>
	                            <td>
	                                <s:property value="proStocktakeRequestMainData.DepotCodeName"/>
	                            </td>
	                            <%-- 盘点逻辑仓库 --%>
	                            <th><s:text name="BIL16_LogicInventoryName"/></th>
	                            <td>
	                                <s:property value="proStocktakeRequestMainData.LogicInventoryCodeName"/>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	                <table class="detail" style="margin-bottom: 5px;">
	                    <tbody>
	                        <tr>
	                            <%-- 审核状态 --%>
	                            <th><s:text name="BIL16_verifiedFlag"/></th>
	                            <td><s:property value='#application.CodeTable.getVal("1238", proStocktakeRequestMainData.VerifiedFlag)'/></td>
	                            <%-- 审核者 --%>
	                            <th><s:text name="BIL16_employeeNameAudit"/></th>
	                            <td><s:property value="proStocktakeRequestMainData.EmployeeNameAudit"/></td>
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
	                    <%-- 盘点申请单明细一览 --%>
	                    <s:text name="BIL16_results_list"/>
	                </strong>
	            </div>
	            <div class="section-content">
	            <%--
	            <s:if test='"2".equals(operateType) || "141".equals(operateType)'>
	                <div id="showToolbar" class="hide">
	                <div class="toolbar clearfix">
	                    <span class="left">
	                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
	                        <a class="add" onclick="BINOLSTBIL16.openProPopup(this);">
	                            <span class="ui-icon icon-add"></span>
	                            <span class="button-text"><s:text name="BIL16_add"/></span>
	                        </a>
	                        <a class="delete" onclick="BINOLSTBIL16.deleteRow();">
	                            <span class="ui-icon icon-delete"></span>
	                            <span class="button-text"><s:text name="BIL16_delete"/></span>
	                        </a>
	                    </span>
	                </div>
	                </div>
	            </s:if>
	            --%>
	            <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
	            <input type="hidden" id="entryID" name="entryID" value='<s:property value="proStocktakeRequestMainData.WorkFlowID"/>'/>
	            <input type="hidden" id="actionID" name="actionID"/>
	            <input type="hidden" id="entryid" name="entryid"/>
	            <input type="hidden" id="actionid" name="actionid"/>
	            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
	            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	                <thead>
	                    <tr>
	                        <%-- 
	                        <s:if test='"2".equals(operateType) || "141".equals(operateType)'>
	                            <th id="showCheckbox" class="center hide" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTBIL16.selectAll();"/><s:text name="BIL16_select"/></th>
	                        </s:if>
	                        --%>
	                        <th class="center"><s:text name="BIL16_no"/></th><%-- 编号 --%>
	                        <th class="center"><s:text name="BIL16_UnitCode"/></th><%-- 厂商编码 --%>
	                        <th class="center"><s:text name="BIL16_BarCode"/></th><%-- 产品条码 --%> 
	                        <th class="center"><s:text name="BIL16_ProductName"/></th><%-- 产品名称 --%>
	                        <th class="center"><s:text name="BIL16_Price"/></th><%-- 单价 --%>
	                        <th class="center"><s:text name="BIL16_BookQuantity"/></th><%-- 账面数量 --%>
	                        <th class="center"><s:text name="BIL16_CheckQuantity"/></th><%-- 实盘数量 --%>
	                        <th class="center"><s:text name="BIL16_GainQuantity"/></th><%-- 盘差 --%>
	                        <th class="center"><s:text name="BIL16_Amount"/></th><%-- 金额 --%>
	                        <th class="center"><s:text name="BIL16_handleType"/></th><%-- 处理方式 --%>
	                        <th class="center"><s:text name="BIL16_remark"/></th><%-- 备注 --%>
	                    </tr>
	                </thead>
	                <tbody id="databody">
	                    <s:iterator value="proStocktakeRequestDetailData" status="status">
	                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
	                        <%--
	                        <s:if test='"2".equals(operateType) || "141".equals(operateType)'>
	                            <td id="dataTd0" class="hide"><input id="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTBIL16.changechkbox(this);"/></td>
	                        </s:if>
	                        --%>
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
	                        <td id="dataTd6" class="alignRight">
                                <s:if test='null!=BookQuantity'>
                                    <s:text name="format.number"><s:param value="BookQuantity"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
	                        </td>
	                        <td id="newCount" class="alignRight" style="width:10%;">
	                            <s:if test='"2".equals(operateType) || "141".equals(operateType) || "145".equals(operateType)'>
	                                <s:textfield name="checkQuantityArr" cssClass="text-number hide" size="10" maxlength="9" value="%{CheckQuantity}" onchange="BINOLSTBIL16.changeCount(this);"></s:textfield>
	                                <div id="hideCheckQuantiyArr">
	                                    <s:text name="format.number"><s:param value="CheckQuantity"></s:param></s:text>
	                                </div>
	                            </s:if>
	                            <s:else>
	                                <s:if test='null!=CheckQuantity'>
	                                    <s:text name="format.number"><s:param value="CheckQuantity"></s:param></s:text>
	                                    <s:hidden name="checkQuantityArr" value="%{CheckQuantity}"/>
	                                </s:if>
	                                <s:else>&nbsp;</s:else>
	                            </s:else>
	                        </td>
                            <td id="dataTd7" class="alignRight">
                                <s:if test='null!=GainQuantity'>
                                    <s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text>
                                    <s:hidden name="quantityArr" value="%{GainQuantity}"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
	                        <td id="money" class="alignRight">
	                            <s:if test='null!=Price && null!=GainQuantity'>
	                                <s:text name="format.price"><s:param value="Price*GainQuantity"></s:param></s:text>
	                            </s:if>
	                            <s:else>&nbsp;</s:else>
	                        </td>
                            <td id="dataTd8"><span><s:property value='#application.CodeTable.getVal("1020", HandleType)'/></span></td>
	                        <td id="dataTd9">
	                            <s:if test='"2".equals(operateType) || "141".equals(operateType) || "145".equals(operateType)'>
	                                <s:textfield name="commentsArr" size="25" maxlength="200" value="%{Comments}" cssClass="hide" CssStyle="width:98%"></s:textfield>
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
	                            <input type="hidden" id="bookQuantityArr<s:property value='#status.index+1'/>" name="bookQuantityArr" value="<s:property value='BookQuantity'/>"/>
	                            <input type="hidden" id="handleTypeArr<s:property value='#status.index+1'/>" name="handleTypeArr" value="<s:property value='HandleType'/>"/>
	                            <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
	                        </td>
	                    </tr>
	                    </s:iterator>
	                </tbody>
	            </table>
	            </div>
	            <div style="display:none">
	                <input type="hidden" id="rowNumber" value="<s:property value='proStocktakeRequestDetailData.size()'/>"/>
	                <input type="hidden" id="inTestType" value="<s:property value='proStocktakeRequestMainData.TestType'/>">
	                <input type="hidden" id="updateTime" name="updateTime" value="<s:property value="proStocktakeRequestMainData.UpdateTime"/>">
	                <input type="hidden" id="modifyCount" name="modifyCount" value="<s:property value="proStocktakeRequestMainData.ModifyCount"/>">
	                <input type="hidden" id="verifiedFlag" name="verifiedFlag" value="<s:property value="proStocktakeRequestMainData.VerifiedFlag"/>">
	                <input type="hidden" id="organizationID" name="organizationID" value="<s:property value="proStocktakeRequestMainData.BIN_OrganizationID"/>" >
	                <input type="hidden" id="inventoryInfoID" name="inventoryInfoID" value="<s:property value="proStocktakeRequestMainData.BIN_InventoryInfoID"/>">
	                <input type="hidden" id="logicInventoryInfoID" name="logicInventoryInfoID" value="<s:property value="proStocktakeRequestMainData.BIN_LogicInventoryInfoID"/>">
	                <s:hidden id="proStocktakeRequestID" name="proStocktakeRequestID" value="%{#request.proStocktakeRequestID}"></s:hidden>
	            </div>
	            <hr class="space" />
	            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
	                <tr>
	                    <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
	                    <td class="center"><s:text name="BIL16_BookQuantity"/></td><%-- 账面数量 --%>
	                    <td class="center"><s:text name="BIL16_CheckQuantity"/></td><%-- 实盘数量--%>
                        <td class="center"><s:text name="BIL16_overQuantity"/></td><%-- 盘盈数 --%>
                        <td class="center"><s:text name="BIL16_overAmount"/></td><%-- 盘盈金额--%>
                        <td class="center"><s:text name="BIL16_shortQuantity"/></td><%-- 盘亏数 --%>
                        <td class="center"><s:text name="BIL16_shortAmount"/></td><%-- 盘亏金额--%>
	                </tr>
	                <tr>
                        <td id="sumQuantity" class="center">
                            <s:text name="format.number"><s:param value="sumInfo.SumQuantity"></s:param></s:text>
                        </td>
                        <td id="sumRealQuantity" class="center">
                            <s:text name="format.number"><s:param value="sumInfo.SumRealQuantity"></s:param></s:text>
                        </td>
                        <td id="overQuantity" class="center">
                            <s:text name="format.number"><s:param value="sumInfo.OverQuantity"></s:param></s:text>
                        </td>
                        <td id="overAmount" class="center">
                            <s:text name="format.price"><s:param value="sumInfo.OverAmount"></s:param></s:text>
                        </td>
                        <td id="shortQuantity" class="center">
                            <s:text name="format.number"><s:param value="sumInfo.ShortQuantity"></s:param></s:text>
                        </td>
                        <td id="shortAmount" class="highlight center">
                            <s:text name="format.price"><s:param value="sumInfo.ShortAmount"></s:param></s:text>
                        </td>
	                </tr>
	            </table>
	            <hr class="space" />
	            <div class="center">
	            <s:if test='"2".equals(operateType)'>
	                <button class="save" onclick="BINOLSTBIL16.saveForm();return false;"><span class="ui-icon icon-save"></span>
	                    <%-- 保存 --%>
	                    <span class="button-text"><s:text name="global.page.save"/></span>
	                </button>
	            </s:if>
	            <s:elseif test='@com.cherry.cm.core.CherryConstants@OPERATE_CR_AUDIT.equals(operateType) 
	               || @com.cherry.cm.core.CherryConstants@OPERATE_CR_AUDIT2.equals(operateType)'>
	                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
	                <%-- 保存 --%>
	                <%--
	                <button id="btnSave" class="save" style="display:none;" onclick="BINOLSTBIL16.saveForm();return false;"><span class="ui-icon icon-save"></span>
	                    <span class="button-text"><s:text name="global.page.save"/></span>
	                </button>
	                --%>
	            </s:elseif>
	            <button id="btnReturn" class="close" onclick="BINOLSTBIL16.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
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
<form action="BINOLSTBIL16_init" id="proStocktakeRequestIDDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="proStocktakeRequestID" value="%{#request.proStocktakeRequestID}"></s:hidden>
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