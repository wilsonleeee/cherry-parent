<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<%--退库申请单明细 --%>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL20.js?v=20160829"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.st.BINOLSTBIL20">
<div class="main container clearfix">
<s:url id="doaction_url" value="/st/BINOLSTBIL20_doaction"/>
<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
</div>
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL20_title"/>&nbsp;(<s:text name="BIL20_num"/>:<s:property value="saleRerurnRequestInfo.billCode"/>)</span>
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
	                <input type="hidden" name="billType" value='<s:property value="saleRerurnRequestInfo.TradeType"/>'/>
	                <input type="hidden" name="pageId" value="BINOLSTBIL20"/>
	                <input type="hidden" name="billId" value="${saleReturnRequestID}"/>
	                </div>
	                <cherry:show domId="BINOLSTBIL20PNT">
	                <button onclick="openPrintApp('Print');return false;" class="confirm right">
	                    <span class="ui-icon icon-file-print"></span>
	                    <span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
	                </button>
	                </cherry:show>
	                <cherry:show domId="BINOLSTBIL20VEW">
	                <button onclick="openPrintApp('View');return false;" class="confirm right">
	                    <span class="ui-icon icon-file-view"></span>
	                    <span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
	                </button>
	                </cherry:show>
	                <cherry:show domId="BINOLSTBIL20EXP">
	                <div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${saleReturnRequestID}"/></div>
	                <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTBIL20',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
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
	                            <%-- 销售退货申请单号 --%>
	                            <th><s:text name="BIL20_billNo"/></th>
	                            <td><s:property value="saleRerurnRequestInfo.billCode"/></td>
	                            <%-- 申请日期 --%>
	                            <th><s:text name="BIL20_date"/></th>
	                            <td><s:property value="saleRerurnRequestInfo.tradeDate"/></td>
	                        </tr>
	                        <tr>
	                            <%-- 关联单号 --%>
	                            <th><s:text name="BIL20_relevanceNo"/></th>
	                            <td><s:property value="saleRerurnRequestInfo.relevanceNo"/></td>
	                            <%-- 申请人 --%>
	                            <th><s:text name="BIL20_employeeName"/></th>
	                            <td><s:property value="saleRerurnRequestInfo.employeeName"/></td>
	                        </tr>
	                        <tr>
	                            <%-- 业务类型 --%>
	                            <th><s:text name="BIL20_tradeType"/></th>
	                            <td><s:text name="BIL20_SA"/></td>
	                            <th></th>
	                            <td></td>
	                        </tr>
	                        <tr>
	                            <%-- 退库申请理由 --%>
	                            <th><s:text name="BIL20_refuseReason"/></th>
	                            <td colspan=3>
                                    <s:set id="reasonCodeValue" name="reasonCodeValue" value="#application.CodeTable.getVal('1283', saleRerurnRequestInfo.Reason) "></s:set>
                                    <s:if test="null == #reasonCodeValue || #reasonCodeValue.equals('')">
                                        <s:property value='saleRerurnRequestInfo.reason'/>
                                    </s:if>
                                    <s:else>
                                        <s:property value='#reasonCodeValue'/>
                                    </s:else>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	                <table class="detail">
	                    <tbody>
	                        <tr>
	                            <%-- 会员卡号--%>
	                            <th><s:text name="BIL20_memberCode"/></th>
	                            <td><s:property value="saleRerurnRequestInfo.memberCode"/></td>
	                         	<th><s:text name="BIL20_verifiedFlag"/></th>
	                            <td><s:property value='#application.CodeTable.getVal("1252", saleRerurnRequestInfo.verifiedFlag)'/></td>
	                        </tr>
	                        <tr><%-- 柜台名称 --%>                            
	                            <th><s:text name="BIL20_counterCode"/></th>
	                            <td>
	                             <s:if test='null != saleRerurnRequestInfo.counterCode && !saleRerurnRequestInfo.counterCode.equals("")'>
	                                (<s:property value="saleRerurnRequestInfo.counterCode"/>)<s:property value="saleRerurnRequestInfo.counterName"/>
	                            </s:if>
	                            </td>
	                            <%-- 审核者 --%>
	                            <th><s:text name="BIL20_employeeNameAudit"/></th>
	                            <td>
	                             <s:if test='null != saleRerurnRequestInfo.employeeIDAuditCode && !saleRerurnRequestInfo.employeeIDAuditCode.equals("")'>
	                                (<s:property value="saleRerurnRequestInfo.employeeIDAuditCode"/>)<s:property value="saleRerurnRequestInfo.employeeIDAudit"/>
	                           	</s:if>
	                            </td>
	                        </tr>
	                        <tr>
	                          	<th><s:text name="BIL20_reason"/></th>
	                            <td colspan=3><s:property value="saleRerurnRequestInfo.comments"/></td>
	                        </tr>
	                    </tbody>
	                </table>	               	                
	                <div class="clearfix"></div>
	            </div>
	          </div>
	        </div>
	        <%-- 支付方式详细为空不显示，迭代显示存在的支付方式及对应的金额 --%>
			<s:if test="getPayTypeDetail != null && getPayTypeDetail.size() > 0">
				<div class="section">
					<div class="section-header">
						<strong>
							<span class="ui-icon icon-money"></span>
							<%-- 支付明细 --%>
							<s:text name="BIL20_payment_details"/>
						</strong>
					</div>
					<div class="section-content">
						<div class="box2-content clearfix">
							<s:iterator value="getPayTypeDetail" status="status">
                                <s:set var="payTypeWidthPercent" value="25" />
                                <s:set var="showSerialNumber" value="false"/>
                                <s:if test='null != serialNumber && !serialNumber.equals("")'>
								    <s:set var="payTypeWidthPercent" value="50" />
								    <s:set var="showSerialNumber" value="true"/>
                                </s:if>
								<span class="left" style="width:<s:property value="#payTypeWidthPercent"/>%;margin:3px 0;">
									<span class="ui-icon icon-arrow-crm"></span>
									<span class="bg_title"><s:property value='#application.CodeTable.getVal("1175", payTypeCode)'/></span>
									<span class="red"><s:text name="format.price"><s:param value="payAmount"/></s:text></span>
									<s:if test='#showSerialNumber == true'>
									   <span style="display:inline-block;">(<s:text name="BIL20_SerialNumber"/>:<s:property value='serialNumber'/>)</span>
									</s:if>
								</span>
							</s:iterator>
						</div>
					</div>
				</div>
			</s:if>
	        <div class="section">
	            <div class="section-header">
	                <strong>
	                    <span class="ui-icon icon-ttl-section-search-result"></span>
	                    <%-- 退库申请单明细一览 --%>
	                    <s:text name="BIL20_results_list"/>
	                </strong>
	            </div>
	            <div class="section-content">
	            <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
	            <input type="hidden" id="entryID" name="entryID" value='<s:property value="saleRerurnRequestInfo.WorkFlowID"/>'/>
	            <input type="hidden" id="actionID" name="actionID"/>
	            <input type="hidden" id="entryid" name="entryid"/>
	            <input type="hidden" id="actionid" name="actionid"/>
	            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
	            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	                <thead>
	                    <tr>
	                        <th class="center"><s:text name="BIL20_no"/></th><%-- 编号 --%>
	                        <th class="center"><s:text name="BIL20_UnitCode"/></th><%-- 厂商编码 --%>
	                        <th class="center"><s:text name="BIL20_BarCode"/></th><%-- 产品条码 --%> 
	                        <th class="center"><s:text name="BIL20_ProductName"/></th><%-- 产品名称 --%>
	                        <th class="center"><s:text name="BIL20_saleType"/></th> 
	                        <th class="center"><s:text name="BIL20_Price"/></th><%-- 单价 --%>
	                        <th class="center"><s:text name="BIL20_Quantity"/></th><%-- 数量 --%>
	                        <th class="center"><s:text name="BIL20_Amount"/></th><%-- 金额 --%>
	                        <th class="center"><s:text name="BIL20_remark"/></th><%-- 备注 --%>
	                    </tr>
	                </thead>
	                <tbody id="databody">
	                    <s:iterator value="saleRerurnRequestDetailList" status="status">
	                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">	                        
	                        <td><s:property value="#status.index+1"/></td>
	                        <td><span><s:property value="unitCode"/></span></td>
	                        <td><span><s:property value="barCode"/></span></td>
	                        <td ><span><s:property value="productName"/></span></td>
	                        <td><span><s:property value='#application.CodeTable.getVal("1106", saleType)'/></span></td>
	                        <td>
	                            <s:if test='null!=price'>
	                                <s:text name="format.price"><s:param value="price"></s:param></s:text>
	                            </s:if>
	                            <s:else>&nbsp;</s:else>
	                        </td>
	                        <td class="alignRight">
                                <s:if test='null!=quantity'>
                                    <s:text name="format.number"><s:param value="quantity"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>	                        
	                       <td>
	                            <s:if test='null!=price'>
	                                <s:text name="format.price"><s:param value="quantity*price"></s:param></s:text>
	                            </s:if>
	                            <s:else>&nbsp;</s:else>
	                        </td>
	                      
	                       
	                        <td><s:property value="comments"/></td>
	                    </tr>
	                    </s:iterator>
	                </tbody>
	            </table>
	            </div>
	            <div style="display:none">
	                <input type="hidden" id="rowNumber" value="<s:property value='saleRerurnRequestDetailList'/>"/>	                
	                <input type="hidden" id="verifiedFlag" name="verifiedFlag" value="<s:property value="saleRerurnRequestInfo.VerifiedFlag"/>">
	                <input type="hidden" id="organizationID" name="organizationID" value="<s:property value="saleRerurnRequestInfo.BIN_OrganizationID"/>" >	              
	                <s:hidden id="saleReturnRequestID" name="saleReturnRequestID" value="%{#request.saleReturnRequestID}"></s:hidden>
	            </div>
	            <hr class="space" />
	            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
	                <tr>
	                    <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
	                    <td class="center"><s:text name="BIL20_totalQuantity"/></td><%-- 总数量 --%>
	                    <td class="center"><s:text name="BIL20_totalAmount"/></td><%-- 总金额--%>
	                </tr>
	                <tr>
	                    <td class="center">
	                        <%-- 总数量 --%>
	                        <s:if test='null!=saleRerurnRequestInfo.totalQuantity'>
	                            <span id="totalQuantity"><s:text name="format.number"><s:param value="saleRerurnRequestInfo.totalQuantity"></s:param></s:text></span>
	                        </s:if>
	                        <s:else>&nbsp;</s:else>
	                    </td>
	                    <td class="center">
	                        <%-- 总金额--%>
	                        <s:if test='null!=saleRerurnRequestInfo.totalAmount'>
	                            <span id="totalAmount"><s:text name="format.price"><s:param value="saleRerurnRequestInfo.totalAmount"></s:param></s:text></span>
	                        </s:if>
	                        <s:else>&nbsp;</s:else>
	                    </td>
	                </tr>
	            </table>
	            <hr class="space" />
	            <div class="center">
	            <s:if test='"2".equals(operateType)'>
	            </s:if>
	            <s:elseif test='@com.cherry.cm.core.CherryConstants@OPERATE_SA_AUDIT.equals(operateType)'>
	                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
	                <%-- 保存 --%>
	                <%--
	                <button id="btnSave" class="save" style="display:none;" onclick="BINOLSTBIL20.saveForm();return false;"><span class="ui-icon icon-save"></span>
	                    <span class="button-text"><s:text name="global.page.save"/></span>
	                </button>
	                --%>
	            </s:elseif>	            
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
<form action="BINOLSTBIL20_init" id="saleReturnReqDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="saleReturnRequestID" value="%{#request.saleReturnRequestID}"></s:hidden>
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