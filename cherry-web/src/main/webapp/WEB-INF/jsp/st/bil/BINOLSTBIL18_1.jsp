<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%--调入/出单明细 --%>
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
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="saveUrl" href="${save_url}"></a>
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
    <s:text name="BIL18_selectAll" id="BIL18_selectAll"/>
</div>
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <s:if test='@com.cherry.cm.core.CherryConstants@OS_BILLTYPE_BG.equals(tradeType)'>
                <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL18_titleIn"/>&nbsp;(<s:text name="BIL18_num"/>:<s:property value="productAllocationMainData.AllocationInNo"/>)</span>
            </s:if>
            <s:else>
                <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL18_titleOut"/>&nbsp;(<s:text name="BIL18_num"/>:<s:property value="productAllocationMainData.AllocationOutNo"/>)</span>
            </s:else>
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
                </div>
                <div class="section-content">
                <div>
                    <div class="box-header"></div>
                    <table class="detail">
                        <tbody>
                            <tr>
                                <%-- 调入/出单号 --%>
                                <s:if test='@com.cherry.cm.core.CherryConstants@OS_BILLTYPE_BG.equals(tradeType)'>
                                    <th><s:text name="BIL18_allocationInNo"/></th>
                                    <td><s:property value="productAllocationMainData.AllocationInNoIF"/></td>
                                </s:if>
                                <s:else>
                                    <th><s:text name="BIL18_allocationOutNo"/></th>
                                    <td><s:property value="productAllocationMainData.AllocationOutNoIF"/></td>
                                </s:else>
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
                                <%-- 备注 --%>
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
                            <s:if test='@com.cherry.cm.core.CherryConstants@OS_BILLTYPE_BG.equals(tradeType)'>
	                            <tr>
	                                <%-- 调入实体仓库 --%>
	                                <th><s:text name="BIL18_DepotCodeName"/></th>
	                                <td>
	                                    <s:property value="productAllocationMainData.DepotCodeNameIn"/>
	                                </td>
	                                <%-- 调入逻辑仓库 --%>
	                                <th><s:text name="BIL18_LogicInventoryName"/></th>
	                                <td>
	                                    <s:property value="productAllocationMainData.LogicInventoryCodeNameIn"/>
	                                </td>
	                            </tr>
                            </s:if>
                            <s:else>
	                            <tr>
	                                <%-- 调出实体仓库 --%>
	                                <th><s:text name="BIL18_DepotCodeNameOut"/></th>
	                                <td>
	                                    <s:property value="productAllocationMainData.DepotCodeNameOut"/>
	                                </td>
	                                <%-- 调出逻辑仓库 --%>
	                                <th><s:text name="BIL18_LogicInventoryNameOut"/></th>
	                                <td>
	                                    <s:property value="productAllocationMainData.LogicInventoryCodeNameOut"/>
	                                </td>
	                            </tr>
                            </s:else>
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
                        <%-- 调入/出单明细一览 --%>
                        <s:if test='@com.cherry.cm.core.CherryConstants@OS_BILLTYPE_BG.equals(tradeType)'>
                            <s:text name="BIL18_results_listIn"/>
                        </s:if>
                        <s:else>
                            <s:text name="BIL18_results_listOut"/>
                        </s:else>
                    </strong>
                </div>
                <div class="section-content">
                <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
                <input type="hidden" id="entryID" name="entryID" value='<s:property value="productAllocationMainData.WorkFlowID"/>'/>
                <input type="hidden" id="actionID" name="actionID"/>
                <input type="hidden" id="entryid" name="entryid"/>
                <input type="hidden" id="actionid" name="actionid"/>
                <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
                <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                        <tr>
                            <th class="center"><s:text name="BIL18_no"/></th><%-- 编号 --%>
                            <th class="center"><s:text name="BIL18_UnitCode"/></th><%-- 厂商编码 --%>
                            <th class="center"><s:text name="BIL18_BarCode"/></th><%-- 产品条码 --%> 
                            <th class="center"><s:text name="BIL18_ProductName"/></th><%-- 产品名称 --%>
                            <th class="center"><s:text name="BIL18_Price"/></th><%-- 单价 --%>
                            <s:if test='@com.cherry.cm.core.CherryConstants@OS_BILLTYPE_BG.equals(tradeType)'>
                                <th class="center"><s:text name="BIL18_ReceiveQuantity"/></th><%-- 实收数量 --%>
                            </s:if>
                            <s:else>
                                <th class="center"><s:text name="BIL18_Quantity"/></th><%-- 调拨数量 --%>
                            </s:else>                      
                            <th class="center"><s:text name="BIL18_Amount"/></th><%-- 金额 --%>
                            <s:if test='@com.cherry.cm.core.CherryConstants@OS_BILLTYPE_BG.equals(tradeType)'><%-- 显示调出单明细时显示总成本和平均成本 --%>
                                <th class="center"><s:text name="BIL18_AvgCostPrice"/></th><%-- 平均成本 --%>
                                <th class="center"><s:text name="BIL18_TotalCostPrice"/></th><%-- 总成本 --%>
                            </s:if>
                            <s:if test='@com.cherry.cm.core.CherryConstants@OS_BILLTYPE_LG.equals(tradeType)'><%-- 显示调入单明细时显示总成本和平均成本 --%>
                                <th class="center"><s:text name="BIL18_AvgCostPrice"/></th><%-- 平均成本 --%>
                                <th class="center"><s:text name="BIL18_TotalCostPrice"/></th><%-- 总成本 --%>
                            </s:if>
                            <th class="center"><s:text name="BIL18_remark"/></th><%-- 备注 --%>
                        </tr>
                    </thead>
                    <tbody id="databody">
                        <s:iterator value="productAllocationDetailData" status="status">
                        <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
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
                                <s:if test='null != Quantity'>
                                    <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
	                        <td id="money" class="alignRight">
	                            <s:if test='null!=Price && null != Quantity'>
	                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
	                            </s:if>
	                            <s:else>&nbsp;</s:else>
	                        </td>
	                        <s:if test='@com.cherry.cm.core.CherryConstants@OS_BILLTYPE_BG.equals(tradeType)'>
	                        <td id="dataTd11" class="alignRight">
                                <s:if test='null!=AvgCostPrice'>
                                   <span><s:property value="AvgCostPrice"/></span> 
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td id="dataTd12" class="alignRight">
                                <s:if test='null!=TotalCostPrice'>
                                    <span><s:property value="TotalCostPrice"/></span>  
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            </s:if>
                            <s:if test='@com.cherry.cm.core.CherryConstants@OS_BILLTYPE_LG.equals(tradeType)'>
	                        <td id="dataTd13" class="alignRight">
                                <s:if test='null!=AvgCostPrice'>
                                   <span><s:property value="AvgCostPrice"/></span> 
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td id="dataTd14" class="alignRight">
                                <s:if test='null!=TotalCostPrice'>
                                    <span><s:property value="TotalCostPrice"/></span>  
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            </s:if>
	                        <td id="dataTd9">
                                <p><s:property value="Comments"/></p>
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