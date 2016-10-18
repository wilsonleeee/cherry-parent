<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<%--退库申请单明细 --%>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL14.js?v=20160615"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.st.BINOLSTBIL14">
<div class="main container clearfix">
<s:url id="doaction_url" value="/st/BINOLSTBIL14_doaction"/>
<s:url id="save_url" value="/st/BINOLSTBIL14_save"/>
<s:url id="submit_url" value="/st/BINOLSTBIL14_submit"/>
<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="saveUrl" href="${save_url}"></a>
    <a id="submitUrl" href="${submit_url}"></a>
    <s:url id="url_getdepotAjax" value="/st/BINOLSTSFH03_getDepot" />
    <span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
    <!-- 获取产品库存 -->
    <s:url id="url_getStockCount" value="/st/BINOLSTBIL14_getStockCount" />
	<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>
    
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
	<input type="hidden" id="sysConfigUsePrice" value='<s:property value='sysConfigUsePrice'/>'>
</div>
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL14_title"/>&nbsp;(<s:text name="BIL14_num"/>:<s:property value="proReturnReqMainData.BillNo"/>)</span>
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
	                <input type="hidden" name="billType" value='<s:property value="proReturnReqMainData.TradeType"/>'/>
	                <input type="hidden" name="pageId" value="BINOLSTBIL14"/>
	                <input type="hidden" name="billId" value="${proReturnRequestID}"/>
	                </div>
	                <cherry:show domId="BINOLSTBIL14PNT">
	                <button onclick="openPrintApp('Print');return false;" class="confirm right">
	                    <span class="ui-icon icon-file-print"></span>
	                    <span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
	                </button>
	                </cherry:show>
	                <cherry:show domId="BINOLSTBIL14VEW">
	                <button onclick="openPrintApp('View');return false;" class="confirm right">
	                    <span class="ui-icon icon-file-view"></span>
	                    <span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
	                </button>
	                </cherry:show>
	                <cherry:show domId="BINOLSTBIL14EXP">
	                <div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${proReturnRequestID}"/></div>
	                <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTBIL14',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
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
	                            <%-- 退库申请单号 --%>
	                            <th><s:text name="BIL14_billNo"/></th>
	                            <td><s:property value="proReturnReqMainData.BillNoIF"/></td>
	                            <%-- 操作日期 --%>
	                            <th><s:text name="BIL14_date"/></th>
	                            <td><s:property value="proReturnReqMainData.TradeDate"/></td>
	                        </tr>
	                        <tr>
	                            <%-- 关联单号 --%>
	                            <th><s:text name="BIL14_relevanceNo"/></th>
	                            <td><s:property value="proReturnReqMainData.RelevanceNo"/></td>
	                            <%-- 申请人 --%>
	                            <th><s:text name="BIL14_employeeName"/></th>
	                            <td><s:property value="proReturnReqMainData.EmployeeName"/></td>
	                        </tr>
	                        <tr>
	                            <%-- 业务类型 --%>
	                            <th><s:text name="BIL14_tradeType"/></th>
	                            <td><s:property value='#application.CodeTable.getVal("1144", proReturnReqMainData.TradeType)'/></td>
	                            <th></th>
	                            <td></td>
	                        </tr>
	                        <tr>
	                            <%-- 退库申请理由 --%>
	                            <th><s:text name="BIL14_reason"/></th>
	                            <td colspan=3>
                                    <s:set id="reasonCodeValue" name="reasonCodeValue" value="#application.CodeTable.getVal('1283', proReturnReqMainData.Reason) "></s:set>
                                    <s:if test="null == #reasonCodeValue || #reasonCodeValue.equals('')">
                                        <s:property value='proReturnReqMainData.Reason'/>
                                    </s:if>
                                    <s:else>
                                        <s:property value='#reasonCodeValue'/>
                                    </s:else>
                                    <%--
                                    <s:if test="null != proReturnReqMainData.Comment">
                                        <s:property value="proReturnReqMainData.Comment"/>
                                    </s:if>
                                    --%>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	                <table class="detail">
	                    <tbody>
	                        <tr>
	                            <%-- 退库部门 --%>
	                            <th><s:text name="BIL14_DepartCodeName"/></th>
	                            <td><s:property value="proReturnReqMainData.DepartCodeName"/></td>
	                            <th></th>
	                            <td></td>
	                        </tr>
	                        <tr>
	                            <s:text name="BIL14_selectAll" id="BIL14_selectAll"/>
	                            <%-- 退库实体仓库 --%>
	                            <th><s:text name="BIL14_DepotCodeName"/></th>
	                            <td>
	                                <s:property value="proReturnReqMainData.DepotCodeName"/>
	                            </td>
	                            <%-- 退库逻辑仓库 --%>
	                            <th><s:text name="BIL14_LogicInventoryName"/></th>
	                            <td>
	                                <s:property value="proReturnReqMainData.LogicInventoryCodeName"/>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	                <table class="detail">
	                    <tbody>
	                        <tr>
	                            <%-- 退库接收部门 --%>
	                            <th><s:text name="BIL14_DepartCodeNameReceive"/></th>
	                            <s:if test='"131".equals(operateType) 
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT2.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT3.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT4.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_LOGISTICSCONFIRM.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_CONFIRM.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RD.equals(operateType)'>
	                                <td>
	                                <span id="inOrgName" class="left"><s:property value="proReturnReqMainData.DepartCodeNameReceive"/></span>
	                                <div id="showBtnopenDepartBox" class="hide">
	                                    <a class="add right hide" onclick="BINOLSTBIL14.openDepartBox(this);">
	                                        <span class="ui-icon icon-search"></span>
	                                        <span class="button-text"><s:text name="global.page.Popselect"/></span>
	                                    </a>
	                                </div>
	                                </td>
	                            </s:if>
	                            <s:else>
	                                <td><s:property value="proReturnReqMainData.DepartCodeNameReceive"/></td>
	                            </s:else>
	                            <th></th>
	                            <td></td>
	                        </tr>
	                        <tr>
	                            <s:text name="BIL14_selectAll" id="BIL14_selectAll"/>
	                            <%-- 接收退库实体仓库 --%>
	                            <th><s:text name="BIL14_DepotCodeNameReceive"/></th>
	                            <s:if test='"131".equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT2.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT3.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT4.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_LOGISTICSCONFIRM.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_CONFIRM.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RD.equals(operateType)'>
	                                <td>
	                                    <s:if test='null!=depotsInfoList'>
	                                        <div id="showAcceptDepotCodeName" class="hide">
	                                            <s:select id="inDepotInfoId" name="inDepotInfoId" list="depotsInfoList" value="%{proReturnReqMainData.BIN_InventoryInfoIDReceive}" onchange="BINOLSTBIL14.setHideInventoryValue();" listKey="BIN_DepotInfoID" listValue="DepotCodeName" headerKey="" cssStyle="width:200px;">
	                                            </s:select>
	                                        </div>
	                                        <div id="hideAcceptDepotCodeName">
	                                            <s:property value="proReturnReqMainData.DepotCodeNameReceive"/>
	                                        </div>
	                                    </s:if>
	                                </td>
	                            </s:if>
	                            <s:else>
	                                <td><s:property value="proReturnReqMainData.DepotCodeNameReceive"/></td>
	                            </s:else>
	                            <%-- 接收退库逻辑仓库 --%>
	                            <th><s:text name="BIL14_LogicInventoryNameReceive"/></th>
	                            <s:if test='"131".equals(operateType)
	                               || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT2.equals(operateType)
	                               || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT3.equals(operateType)
	                               || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT4.equals(operateType)
	                               || @com.cherry.cm.core.CherryConstants@OPERATE_RA_LOGISTICSCONFIRM.equals(operateType)
	                               || @com.cherry.cm.core.CherryConstants@OPERATE_RA_CONFIRM.equals(operateType)
	                               || @com.cherry.cm.core.CherryConstants@OPERATE_RD.equals(operateType)'>
	                                <td>
	                                    <s:if test='null!=logicDepotsInfoList'>
	                                        <div id="showAcceptLogicInventoryName" class="hide">
	                                            <s:select id="inLogicDepotsInfoId" name="inLogicDepotsInfoId" list="logicDepotsInfoList" value="%{proReturnReqMainData.BIN_LogicInventoryInfoIDReceive}" onchange="BINOLSTBIL14.setHideInventoryValue();" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" cssStyle="width:200px;">
	                                            </s:select>
	                                        </div>
	                                        <div id="hideAcceptLogicInventoryName">
	                                            <s:property value="proReturnReqMainData.LogicInventoryCodeNameReceive"/>
	                                        </div>
	                                    </s:if>
	                                </td>
	                            </s:if>
	                            <s:else>
	                                <td><s:property value="proReturnReqMainData.LogicInventoryCodeNameReceive"/></td>
	                            </s:else>
	                        </tr>
	                    </tbody>
	                </table>
	                <table class="detail" style="margin-bottom: 5px;">
	                    <tbody>
	                        <tr>
	                            <%-- 审核状态 --%>
	                            <th><s:text name="BIL14_verifiedFlag"/></th>
	                            <td><s:property value='#application.CodeTable.getVal("1252", proReturnReqMainData.VerifiedFlag)'/></td>
	                            <%-- 审核者 --%>
	                            <th><s:text name="BIL14_employeeNameAudit"/></th>
	                            <td><s:property value="proReturnReqMainData.EmployeeNameAudit"/></td>
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
	                    <%-- 退库申请单明细一览 --%>
	                    <s:text name="BIL14_results_list"/>
	                </strong>
	            </div>
	            <div class="section-content">
	            <s:if test='"2".equals(operateType) || "131".equals(operateType) 
                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT2.equals(operateType)
                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT3.equals(operateType)
                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT4.equals(operateType)
                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_LOGISTICSCONFIRM.equals(operateType)
                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_CONFIRM.equals(operateType)
                    || @com.cherry.cm.core.CherryConstants@OPERATE_RD.equals(operateType)'>
	                <div id="showToolbar" class="hide">
	                <div class="toolbar clearfix">
	                    <span class="left">
	                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
	                        <a class="add" onclick="BINOLSTBIL14.openProPopup(this);">
	                            <span class="ui-icon icon-add"></span>
	                            <span class="button-text"><s:text name="BIL14_add"/></span>
	                        </a>
	                        <a class="delete" onclick="BINOLSTBIL14.deleteRow();">
	                            <span class="ui-icon icon-delete"></span>
	                            <span class="button-text"><s:text name="BIL14_delete"/></span>
	                        </a>
	                    </span>
	                </div>
	                </div>
	            </s:if>
	            <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
	            <input type="hidden" id="entryID" name="entryID" value='<s:property value="proReturnReqMainData.WorkFlowID"/>'/>
	            <input type="hidden" id="actionID" name="actionID"/>
	            <input type="hidden" id="entryid" name="entryid"/>
	            <input type="hidden" id="actionid" name="actionid"/>
	            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
	            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	                <thead>
	                    <tr>
	                        <s:if test='"2".equals(operateType) || "131".equals(operateType)
	                            || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT2.equals(operateType)
	                            || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT3.equals(operateType)
	                            || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT4.equals(operateType)
                                || @com.cherry.cm.core.CherryConstants@OPERATE_RA_LOGISTICSCONFIRM.equals(operateType)
                                || @com.cherry.cm.core.CherryConstants@OPERATE_RA_CONFIRM.equals(operateType)
                                || @com.cherry.cm.core.CherryConstants@OPERATE_RD.equals(operateType)'>
	                            <th id="showCheckbox" class="center hide" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTBIL14.selectAll();"/><s:text name="BIL14_select"/></th><%-- 编号 --%>
	                        </s:if>
	                        <th class="center"><s:text name="BIL14_no"/></th><%-- 编号 --%>
	                        <th class="center"><s:text name="BIL14_UnitCode"/></th><%-- 厂商编码 --%>
	                        <th class="center"><s:text name="BIL14_BarCode"/></th><%-- 产品条码 --%> 
	                        <th class="center"><s:text name="BIL14_ProductName"/></th><%-- 产品名称 --%>
	                        <th class="center"><s:text name="BIL14_Price"/></th><%-- 单价 --%>
	                        <s:if test='"2".equals(operateType) || "131".equals(operateType) 
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT2.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT3.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT4.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_LOGISTICSCONFIRM.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_CONFIRM.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RD.equals(operateType)'>
	                        	<th id="th_stockQuantity" class="center"><s:text name="BIL14_stockQuantity"/></th><%-- 库存 --%>
	                        </s:if>
	                        <th class="center"><s:text name="BIL14_Quantity"/></th><%-- 数量 --%>
	                        <th class="center"><s:text name="BIL14_Amount"/></th><%-- 金额 --%>
	                        <th class="center"><s:text name="BIL14_remark"/></th><%-- 备注 --%>
	                    </tr>
	                </thead>
	                <tbody id="databody">
	                    <s:iterator value="proReturnReqDetailData" status="status">
	                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
	                        <s:if test='"2".equals(operateType) || "131".equals(operateType) 
	                           || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT2.equals(operateType)
	                           || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT3.equals(operateType)
	                           || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT4.equals(operateType)
	                           || @com.cherry.cm.core.CherryConstants@OPERATE_RA_LOGISTICSCONFIRM.equals(operateType)
	                           || @com.cherry.cm.core.CherryConstants@OPERATE_RA_CONFIRM.equals(operateType)
	                           || @com.cherry.cm.core.CherryConstants@OPERATE_RD.equals(operateType)'>
	                            <td id="dataTd0" class="hide"><input id="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTBIL14.changechkbox(this);"/></td>
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
	                        <s:if test='"2".equals(operateType) || "131".equals(operateType) 
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT2.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT3.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT4.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_LOGISTICSCONFIRM.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_CONFIRM.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RD.equals(operateType)'>
		                        <td id="dataTd6" class="alignRight">
	                                <s:if test='null!=StockQuantity'>
	                                    <s:text name="format.number"><s:param value="StockQuantity"></s:param></s:text>
	                                </s:if>
	                                <s:else>&nbsp;</s:else>
	                            </td>
		                        <td id="newCount" class="alignRight" style="width:10%;">
	                                <s:textfield name="quantityArr" cssClass="text-number hide" size="10" maxlength="9" value="%{Quantity}" onchange="BINOLSTBIL14.changeCount(this);"></s:textfield>
	                                <div id="hideQuantiyArr">
	                                    <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
	                                </div>
		                        </td>
	                        </s:if>
	                        <s:else>
		                        <td id="newCount" class="alignRight" style="width:10%;">
	                                <s:if test='null!=Quantity'>
	                                    <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
	                                    <s:hidden name="quantityArr" value="%{Quantity}"/>
	                                </s:if>
	                                <s:else>&nbsp;</s:else>
	                            </td>
                            </s:else>
	                        <td id="money" class="alignRight">
	                            <s:if test='null!=Price && null!=Quantity'>
	                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
	                            </s:if>
	                            <s:else>&nbsp;</s:else>
	                        </td>
	                        <td id="dataTd9">
	                            <s:if test='"2".equals(operateType) || "131".equals(operateType) 
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT2.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT3.equals(operateType)
                                    || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT4.equals(operateType)
	                                || @com.cherry.cm.core.CherryConstants@OPERATE_RA_LOGISTICSCONFIRM.equals(operateType)
	                                || @com.cherry.cm.core.CherryConstants@OPERATE_RA_CONFIRM.equals(operateType)
	                                || @com.cherry.cm.core.CherryConstants@OPERATE_RD.equals(operateType)'>
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
	                            <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
	                        </td>
	                    </tr>
	                    </s:iterator>
	                </tbody>
	            </table>
	            </div>
	            <div style="display:none">
	                <input type="hidden" id="rowNumber" value="<s:property value='proReturnReqDetailData.size()'/>"/>
	                <input type="hidden" id="inTestType" value="<s:property value='proReturnReqMainData.TestType'/>">
	                <input type="hidden" id="updateTime" name="updateTime" value="<s:property value="proReturnReqMainData.UpdateTime"/>">
	                <input type="hidden" id="modifyCount" name="modifyCount" value="<s:property value="proReturnReqMainData.ModifyCount"/>">
	                <input type="hidden" id="verifiedFlag" name="verifiedFlag" value="<s:property value="proReturnReqMainData.VerifiedFlag"/>">
	                <input type="hidden" id="organizationID" name="organizationID" value="<s:property value="proReturnReqMainData.BIN_OrganizationID"/>" >
	                <input type="hidden" id="inventoryInfoID" name="inventoryInfoID" value="<s:property value="proReturnReqMainData.BIN_InventoryInfoID"/>">
	                <input type="hidden" id="logicInventoryInfoID" name="logicInventoryInfoID" value="<s:property value="proReturnReqMainData.BIN_LogicInventoryInfoID"/>">
	                <input type="hidden" id="organizationIDReceive" name="organizationIDReceive" value="<s:property value="proReturnReqMainData.BIN_OrganizationIDReceive"/>">
	                <input type="hidden" id="inventoryInfoIDReceive" name="inventoryInfoIDReceive" value="<s:property value="proReturnReqMainData.BIN_InventoryInfoIDReceive"/>">
	                <input type="hidden" id="logicInventoryInfoIDReceive" name="logicInventoryInfoIDReceive" value="<s:property value="proReturnReqMainData.BIN_LogicInventoryInfoIDReceive"/>">
	                <s:hidden id="proReturnRequestID" name="proReturnRequestID" value="%{#request.proReturnRequestID}"></s:hidden>
	            	<input type="hidden" id="checkStockFlag" name="checkStockFlag" value="<s:property value='checkStockFlag'/>"/>
	            </div>
	            <hr class="space" />
	            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
	                <tr>
	                    <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
	                    <td class="center"><s:text name="BIL14_totalQuantity"/></td><%-- 总数量 --%>
	                    <td class="center"><s:text name="BIL14_totalAmount"/></td><%-- 总金额--%>
	                </tr>
	                <tr>
	                    <td class="center">
	                        <%-- 总数量 --%>
	                        <s:if test='null!=proReturnReqMainData.TotalQuantity'>
	                            <span id="totalQuantity"><s:text name="format.number"><s:param value="proReturnReqMainData.TotalQuantity"></s:param></s:text></span>
	                        </s:if>
	                        <s:else>&nbsp;</s:else>
	                    </td>
	                    <td class="center">
	                        <%-- 总金额--%>
	                        <s:if test='null!=proReturnReqMainData.TotalAmount'>
	                            <span id="totalAmount"><s:text name="format.price"><s:param value="proReturnReqMainData.TotalAmount"></s:param></s:text></span>
	                        </s:if>
	                        <s:else>&nbsp;</s:else>
	                    </td>
	                </tr>
	            </table>
	            <hr class="space" />
	            <div class="center">
	            <s:if test='"2".equals(operateType)'>
                    <button id="btnSave" class="save" style="display:none;" onclick="BINOLSTBIL14.saveForm();return false;">
                        <span class="ui-icon icon-save"></span>
                        <%-- 保存 --%>
                        <span class="button-text"><s:text name="global.page.save"/></span>
                    </button>
                    <button id="btn-icon-edit-big" class="confirm" onclick="BINOLSTBIL14.modifyForm();return false;">
                        <span class="ui-icon icon-edit-big"></span>
                        <%-- 修改 --%>
                        <span class="button-text"><s:text name="os.edit"/></span>
                    </button>
                    <button class="confirm" onclick="BINOLSTBIL14.submitForm();return false;">
                        <span class="ui-icon icon-confirm"></span>
                        <%-- 提交 --%>
                        <span class="button-text"><s:text name="global.page.submit"/></span>
                    </button>
	            </s:if>
	            <s:elseif test='@com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT.equals(operateType) 
	               || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT2.equals(operateType)
	               || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT3.equals(operateType)
	               || @com.cherry.cm.core.CherryConstants@OPERATE_RA_AUDIT4.equals(operateType)
	               || @com.cherry.cm.core.CherryConstants@OPERATE_RA_LOGISTICSCONFIRM.equals(operateType)
	               || @com.cherry.cm.core.CherryConstants@OPERATE_RA_CONFIRM.equals(operateType)
	               || @com.cherry.cm.core.CherryConstants@OPERATE_RD.equals(operateType)'>
	                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
	                <%-- 保存 --%>
	                <%--
	                <button id="btnSave" class="save" style="display:none;" onclick="BINOLSTBIL14.saveForm();return false;"><span class="ui-icon icon-save"></span>
	                    <span class="button-text"><s:text name="global.page.save"/></span>
	                </button>
	                --%>
	            </s:elseif>
	            <button id="btnReturn" class="close" onclick="BINOLSTBIL14.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
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
<form action="BINOLSTBIL14_init" id="proReturnReqDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="proReturnRequestID" value="%{#request.proReturnRequestID}"></s:hidden>
</form>
<%-- ================== 报表导出共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
<%-- ==================  报表导出共通导入 End ======================== --%>
<%-- ================== 打印预览、打印共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览、打印共通导入 END ========================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
    <input type="hidden" id="errmsg_EST00044" value='<s:text name="EST00044"/>'/>
</div>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 工作流提示弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOSDialog.jsp" flush="true" />
<%-- ==================  工作流提示弹出窗口End ======================== --%>