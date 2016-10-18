<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH16.js"></script>
<s:i18n name="i18n.st.BINOLSTSFH16">
<div class="main container clearfix">
	<div class="hide">
		<s:url id="doaction_url" value="/st/BINOLSTSFH16_doaction"/>
		<a id="osdoactionUrl" href="${doaction_url}"></a>
		<s:url id="s_submitURL" value="/st/BINOLSTSFH16_submit"/>
		<a id="submitUrl" href="${s_submitURL}"></a>
		<s:url id="s_saveURL" value="/st/BINOLSTSFH16_save"/>
		<a id="saveUrl" href="${s_saveURL}"></a>
		<s:url id="delete_url" value="/st/BINOLSTSFH16_delete"/>
		<a id="deleteUrl" href="${delete_url}"></a>
		<input type="hidden" id="BINOLSTSFH16" value="BINOLSTSFH16"/>
	</div>
	<div id="div_main" class="panel ui-corner-all">
		<div class="panel-header">
			<span class="hide">
				<input id="currentPageID" value="BINOLSTSFH16">
			</span>
			<div class="clearfix">
				<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:if test='historyFlag.equals("true")'><s:text name="stsfh16.historytitle"/></s:if><s:else><s:text name="stsfh16.title"/></s:else> &nbsp;(<s:text name="stsfh16.numText"/><s:property value="saleOrdersMainData.BillCode"/>)</span>
			</div>
		</div>
		<div class="ui-tabs" id="ui-tabs">
			<ul>
				<li>
					<a href="#tabs-1"><s:text name="global.page.title"/></a>
				</li>
				<li>
					<a href="#tabs-2" onclick="clickTab_2WorkFlow();return false;"><s:text name="global.page.work"/></a>
				</li>
			</ul>
			<div id="tabs-1" class="panel-content">
				<form id="mainForm" method="post" class="inline">
					<div id="actionResultDisplay"></div>
					<%-- ================== 错误信息提示 START ======================= --%>
					<div id="errorDiv2" class="actionError" style="display:none">
						<ul>
							<li><span id="errorSpan2"></span></li>
						</ul>
					</div>
					<%-- ================== 错误信息提示   END  ======================= --%>
					<input type="hidden" id="entryID" name="entryID" value='<s:property value="saleOrdersMainData.WorkFlowID"/>'/>
					<input type="hidden" id="actionID" name="actionID"/>
					<s:hidden id="saleId" name="saleId" value="%{#request.saleId}"></s:hidden>
					<div class="section-header">
						<strong>
							<span class="ui-icon icon-ttl-section-info"></span>
							<s:text name="global.page.title"/>
						</strong>
						<div id="print_param_hide" class="hide">
							<input type="hidden" name="billType" value="NS"/>
							<input type="hidden" name="pageId" value="BINOLSTSFH16"/>
							<input type="hidden" name="billId" value="${saleId}"/>
						</div>
						<s:if test="!historyFlag.equals('true')">
						<!-- 打印签收单 -->
						<%-- <cherry:show domId="STSFH16PNTRET">
							<button onclick="openPrintApp('PrintRet');return false;" class="confirm right">
								<span class="ui-icon icon-file-print"></span>
								<span class="button-text" style="font-size:12px;"><s:text name="stsfh16.printRet"/></span>
							</button>
						</cherry:show> --%>
						<cherry:show domId="STSFH16VIEWRET">
							<button onclick="openPrintApp('ViewRet');return false;" class="confirm right">
								<span class="ui-icon icon-file-view"></span>
								<span class="button-text" style="font-size:12px;"><s:text name="stsfh16.viewRet"/></span>
							</button>
						</cherry:show>
						<!-- 打印销售单 -->
						<%-- <cherry:show domId="BINOLSTSFH16PNT">
							<button onclick="openPrintApp('Print');return false;" class="confirm right">
								<span class="ui-icon icon-file-print"></span>
								<span class="button-text" style="font-size:12px;"><s:text name="stsfh16.print"/></span>
							</button>
						</cherry:show> --%>
						<!-- 预览销售单 -->
						<cherry:show domId="BINOLSTSFH16VEW">
							<button onclick="openPrintApp('View');return false;" class="confirm right">
								<span class="ui-icon icon-file-view"></span>
								<span class="button-text" style="font-size:12px;"><s:text name="stsfh16.view"/></span>
							</button>
						</cherry:show>
						<cherry:show domId="BINOLSTSFH16EXP">
							<div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${saleId}"/></div>
							<button id="exportButton" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTSFH16',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
								<span class="ui-icon icon-file-export"></span>
								<span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
							</button>
						</cherry:show>
						</s:if>
					</div>
					<div class="section-content">
						<input type="hidden" name="curPerson" id="curPerson" value="<s:property value="saleOrdersMainData.ContactPerson"/>"/>
						<input type="hidden" name="curAddress" id="curAddress" value="<s:property value="saleOrdersMainData.DeliverAddress"/>"/>
						<input type="hidden" name="customerType" id="customerType" value="<s:property value="saleOrdersMainData.CustomerType"/>"/>
						<table class="detail">
						<tr>
							<th><s:text name="stsfh16.saleOrderNo"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.BillCode && saleOrdersMainData.BillCode!=""'>
		            				<s:property value="saleOrdersMainData.BillCode"/>
		            			</s:if>
			            		<s:else>&nbsp;</s:else>
		            		</td>
		            		<th><s:text name="stsfh16.billType"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.BillType && saleOrdersMainData.BillType!=""'>
		            				<s:if test='"2".equals(operateType)'>
				            			<s:select name="saleBillType" list='#application.CodeTable.getCodes("1293")' value="%{saleOrdersMainData.BillType}" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
			            			</s:if>
			            			<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanBillType" class="left"><s:property value='#application.CodeTable.getVal("1293", saleOrdersMainData.BillType)'/></span>
			            				<div id="divBillType" class="hide">
			            					<s:select name="saleBillType" list='#application.CodeTable.getCodes("1293")' value="%{saleOrdersMainData.BillType}" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
			            				</div>
			            			</s:elseif>
			            			<s:else>
			            				<s:property value='#application.CodeTable.getVal("1293", saleOrdersMainData.BillType)'/>
			            			</s:else>
			            		</s:if>
			            		<s:else>
									<s:if test='"2".equals(operateType)'>
										<s:select name="saleBillType" list='#application.CodeTable.getCodes("1293")' value="" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
		            				</s:if>
		            				<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanBillType" class="left">&nbsp;</span>
			            				<div id="divBillType" class="hide">
			            					<s:select name="saleBillType" list='#application.CodeTable.getCodes("1293")' value="" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
			            				</div>
			            			</s:elseif>
			            			<s:else>
		            					&nbsp;
									</s:else>
								</s:else>
		            		</td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="stsfh16.customerType"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.CustomerType && saleOrdersMainData.CustomerType!=""'>
			            			<s:property value='#application.CodeTable.getVal("1297", saleOrdersMainData.CustomerType)'/>
			            		</s:if>
		            			<s:else>&nbsp;</s:else>
		            		</td>
		            		<th><s:text name="stsfh16.expectFinishDate"/></th>
		            		<td>
			            		<s:if test='null!=saleOrdersMainData.ExpectFinishDate && saleOrdersMainData.ExpectFinishDate!=""'>
			            			<s:property value="saleOrdersMainData.ExpectFinishDate"/>
			            		</s:if>
		            			<s:else>&nbsp;</s:else>
		            		</td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="stsfh16.customerName"/></th>
		            		<td>
		            			<input type="hidden" name="customerOrganizationId" id="customerOrganizationId" class="text" maxlength="30" value="<s:property value="saleOrdersMainData.CustomerID"/>"/>
		            			<s:if test='null!=saleOrdersMainData.CustomerCodeName && saleOrdersMainData.CustomerCodeName!=""'>
			            			<s:property value="saleOrdersMainData.CustomerCodeName"/>
			            		</s:if>
			            		<s:else>&nbsp;</s:else>
		            		</td>
		            		<th><s:text name="stsfh16.saleDate"/></th>
		            		<td>
			            		<s:property value="saleOrdersMainData.SaleDate"/>
		            		</td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="stsfh16.contactPerson"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.ContactPerson && saleOrdersMainData.ContactPerson!=""'>
		            				<s:if test='"2".equals(operateType)'>
			            				<input type="text" name="contactPerson" id="contactPerson" class="text" maxlength="30" value="<s:property value='saleOrdersMainData.ContactPerson'/>"/>
			            			</s:if>
			            			<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanContactPerson" class="left"><s:property value="saleOrdersMainData.ContactPerson"/></span>
			            				<div id="divContactPerson" class="hide">
			            					<input type="text" name="contactPerson" id="contactPerson" class="text" maxlength="30" value="<s:property value='saleOrdersMainData.ContactPerson'/>"/>
			            				</div>
			            			</s:elseif>
			            			<s:else>
			            				<s:property value="saleOrdersMainData.ContactPerson"/>
		            				</s:else>
		            			</s:if>
		            			<s:else>
		            				<s:if test='"2".equals(operateType)'>
										<input type="text" name="contactPerson" id="contactPerson" class="text" maxlength="30" value=""/>
			            			</s:if>
			            			<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanContactPerson" class="left">&nbsp;</span>
			            				<div id="divContactPerson" class="hide">
			            					<input type="text" name="contactPerson" id="contactPerson" class="text" maxlength="30" value=""/>
			            				</div>
			            			</s:elseif>
			            			<s:else>
			            				&nbsp;
									</s:else>
								</s:else>
		            		</td>
		            		<th><s:text name="stsfh16.deliverAddress"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.DeliverAddress && saleOrdersMainData.DeliverAddress!=""'>
		            				<s:if test='"2".equals(operateType)'>
			            				<input type="text" name=deliverAddress id="deliverAddress" class="text" style="width:95%;" maxlength="200" value="<s:property value='saleOrdersMainData.DeliverAddress'/>"/>
			            			</s:if>
			            			<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanDeliverAddress" class="left"><s:property value="saleOrdersMainData.DeliverAddress"/></span>
			            				<div id="divDeliverAddress" class="hide">
			            					<input type="text" name=deliverAddress id="deliverAddress" class="text" style="width:95%;" maxlength="200" value="<s:property value='saleOrdersMainData.DeliverAddress'/>"/>
			            				</div>
			            			</s:elseif>
			            			<s:else>
			            				<s:property value="saleOrdersMainData.DeliverAddress"/>
		            				</s:else>
		            			</s:if>
		            			<s:else>
		            				<s:if test='"2".equals(operateType)'>
										<input type="text" name=deliverAddress id="deliverAddress" class="text" style="width:95%;" maxlength="200" value=""/>
			            			</s:if>
			            			<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanDeliverAddress" class="left">&nbsp;</span>
			            				<div id="divDeliverAddress" class="hide">
			            					<input type="text" name=deliverAddress id="deliverAddress" class="text" style="width:95%;" maxlength="200" value=""/>
			            				</div>
			            			</s:elseif>
			            			<s:else>
			            				&nbsp;
									</s:else>
								</s:else>
		            		</td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="stsfh16.customerDepot"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.BIN_InventoryInfoIDAccept && saleOrdersMainData.BIN_InventoryInfoIDAccept!=""'>
			            			<s:if test='null!=saleOrdersMainData.AcceptDepotCodeName && saleOrdersMainData.AcceptDepotCodeName!=""'>
		            					<s:property value="saleOrdersMainData.AcceptDepotCodeName"/>
		            				</s:if>
			            			<input type="hidden" id="customerDepot" name="customerDepot" value="<s:property value='saleOrdersMainData.BIN_InventoryInfoIDAccept'/>"/>
		            			</s:if>
		            			<s:else>
									<input type="hidden" id="customerDepot" name="customerDepot" value=""/>
								</s:else>
		            		</td>
		            		<th><s:text name="stsfh16.customerLogicDepot"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.BIN_LogicInventoryInfoIDAccept && saleOrdersMainData.BIN_LogicInventoryInfoIDAccept!=""'>
			            			<s:if test='null!=saleOrdersMainData.AcceptLogicInventoryName && saleOrdersMainData.AcceptLogicInventoryName!=""'>
		            					<s:property value="saleOrdersMainData.AcceptLogicInventoryName"/>
		            				</s:if>
			            			<input type="hidden" id="customerLogicDepot" name="customerLogicDepot" value="<s:property value='saleOrdersMainData.BIN_LogicInventoryInfoIDAccept'/>"/>
		            			</s:if>
		            			<s:else>
		            				<s:if test='null!=saleOrdersMainData.AcceptLogicInventoryName && saleOrdersMainData.AcceptLogicInventoryName!=""'>
		            					<s:property value="saleOrdersMainData.AcceptLogicInventoryName"/>
		            				</s:if>
									<input type="hidden" id="customerLogicDepot" name="customerLogicDepot" value=""/>
								</s:else>
		            		</td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="stsfh16.settlement"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.Settlement && saleOrdersMainData.Settlement!=""'>
			            			<s:if test='"2".equals(operateType)'>
			            				<s:select name="settlement" list='#application.CodeTable.getCodes("1175")' value="%{saleOrdersMainData.Settlement}" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
				            		</s:if>
				            		<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanSettlement" class="left"><s:property value='#application.CodeTable.getVal("1175", saleOrdersMainData.Settlement)'/></span>
			            				<div id="divSettlement" class="hide">
			            					<s:select name="settlement" list='#application.CodeTable.getCodes("1175")' value="%{saleOrdersMainData.Settlement}" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
			            				</div>
			            			</s:elseif>
			            			<s:else>
			            				<s:property value='#application.CodeTable.getVal("1175", saleOrdersMainData.Settlement)'/>
			            			</s:else>
		            			</s:if>
		            			<s:else>
		            				<s:if test='"2".equals(operateType)'>
		            					<s:select name="settlement" list='#application.CodeTable.getCodes("1175")' value="" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
		            				</s:if>
		            				<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanSettlement" class="left">&nbsp;</span>
			            				<div id="divSettlement" class="hide">
			            					<s:select name="settlement" list='#application.CodeTable.getCodes("1175")' value="" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
			            				</div>
			            			</s:elseif>
			            			<s:else>
		            					&nbsp;
		            				</s:else>
		            			</s:else>
		            		</td>
		            		<th><s:text name="stsfh16.currency"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.Currency && saleOrdersMainData.Currency!=""'>
		            				<s:if test='"2".equals(operateType)'>
			            				<s:select name="currency" list='#application.CodeTable.getCodes("1296")' value="%{saleOrdersMainData.Currency}" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
			            			</s:if>
			            			<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanCurrency" class="left"><s:property value='#application.CodeTable.getVal("1296", saleOrdersMainData.Currency)'/></span>
			            				<div id="divCurrency" class="hide">
			            					<s:select name="currency" list='#application.CodeTable.getCodes("1296")' value="%{saleOrdersMainData.Currency}" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
			            				</div>
			            			</s:elseif>
			            			<s:else>
			            				<s:property value='#application.CodeTable.getVal("1296", saleOrdersMainData.Currency)'/>
			            			</s:else>
		            			</s:if>
		            			<s:else>
		            				<s:if test='"2".equals(operateType)'>
		            					<s:select name="currency" list='#application.CodeTable.getCodes("1296")' value="" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
		            				</s:if>
		            				<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanCurrency" class="left">&nbsp;</span>
			            				<div id="divCurrency" class="hide">
			            					<s:select name="currency" list='#application.CodeTable.getCodes("1296")' value="" listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
			            				</div>
			            			</s:elseif>
			            			<s:else>
		            					&nbsp;
		            				</s:else>
		            			</s:else>
		            		</td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="stsfh16.saleOrganization"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.DepartCodeName && saleOrdersMainData.DepartCodeName!=""'>
			            			<s:property value="saleOrdersMainData.DepartCodeName"/>
			            		</s:if>
		            			<s:else>&nbsp;</s:else>
		            		</td>
		            		<th><s:text name="stsfh16.saleEmployee"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.SaleEmployeeName && saleOrdersMainData.SaleEmployeeName!=""'>
			            			<s:property value="saleOrdersMainData.SaleEmployeeName"/>
			            		</s:if>
		            			<s:else>&nbsp;</s:else>
		            		</td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="stsfh16.saleDepot"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.BIN_InventoryInfoID && saleOrdersMainData.BIN_InventoryInfoID!=""'>
			            			<s:if test='null!=saleOrdersMainData.DepotCodeName && saleOrdersMainData.DepotCodeName!=""'>
		            					<s:property value="saleOrdersMainData.DepotCodeName"/>
		            				</s:if>
			            			<input type="hidden" id="saleDepot" name="saleDepot" value="<s:property value='saleOrdersMainData.BIN_InventoryInfoID'/>"/>
		            			</s:if>
		            			<s:else>
									<input type="hidden" id="saleDepot" name="saleDepot" value=""/>
								</s:else>
		            		</td>
		            		<th><s:text name="stsfh16.saleLogicDepot"/></th>
		            		<td>
		            			<s:if test='null!=saleOrdersMainData.BIN_LogicInventoryInfoID && saleOrdersMainData.BIN_LogicInventoryInfoID!=""'>
			            			<s:if test='null!=saleOrdersMainData.LogicInventoryName && saleOrdersMainData.LogicInventoryName!=""'>
		            					<s:property value="saleOrdersMainData.LogicInventoryName"/>
		            				</s:if>
			            			<input type="hidden" id="saleLogicDepot" name="saleLogicDepot" value="<s:property value='saleOrdersMainData.BIN_LogicInventoryInfoID'/>"/>
		            			</s:if>
		            			<s:else>
		            				<s:if test='null!=saleOrdersMainData.LogicInventoryName && saleOrdersMainData.LogicInventoryName!=""'>
		            					<s:property value="saleOrdersMainData.LogicInventoryName"/>
		            				</s:if>
									<input type="hidden" id="saleLogicDepot" name="saleLogicDepot" value=""/>
								</s:else>
		            		</td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="stsfh16.comments"/></th>
		            		<td colspan="5">
		            			<s:if test='null!=saleOrdersMainData.Comments && saleOrdersMainData.Comments!=""'>
		            				<s:if test='"2".equals(operateType)'>
			            				<input type="text" name=comments id="comments" class="text" style="width:95%;" maxlength="50" value="<s:property value='saleOrdersMainData.Comments'/>"/>
			            			</s:if>
			            			<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanComments" class="left"><s:property value="saleOrdersMainData.Comments"/></span>
			            				<div id="divComments" class="hide">
			            					<input type="text" name=comments id="comments" class="text" style="width:95%;" maxlength="50" value="<s:property value='saleOrdersMainData.Comments'/>"/>
			            				</div>
			            			</s:elseif>
		            				<s:else>
			            				<s:property value="saleOrdersMainData.Comments"/>
									</s:else>
		            			</s:if>
		            			<s:else>
		            				<s:if test='"2".equals(operateType)'>
										<input type="text" name=comments id="comments" class="text" style="width:95%;" maxlength="50" value=""/>
									</s:if>
									<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
			            				<span id="spanComments" class="left">&nbsp;</span>
			            				<div id="divComments" class="hide">
			            					<input type="text" name=comments id="comments" class="text" style="width:95%;" maxlength="50" value=""/>
			            				</div>
			            			</s:elseif>
		            				<s:else>
										&nbsp;
									</s:else>
								</s:else>
		            		</td>
		            	</tr>
		            	</table>
		            </div>
					<div class="section">
						<div class="section-header">
							<strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="stsfh16.detail"/></strong>
						</div>
						<div class="section-content">
							<div class="toolbar clearfix">
							<span class="left">
								<s:if test='"2".equals(operateType)'>
									<a id="spanBtnAddNewLine" class="add" onclick="BINOLSTSFH16.addNewLine();">
										<span class="ui-icon icon-add"></span>
										<span class="button-text"><s:text name="stsfh16.btnAddRow"/></span>
									</a>
			                        <a id="spanBtnDeleteRow" class="delete" onclick="BINOLSTSFH16.deleteRow();">
			                            <span class="ui-icon icon-delete"></span>
			                            <span class="button-text"><s:text name="stsfh16.btnDeleteRow"/></span>
			                        </a>
		                        </s:if>
		                        <s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
		                        	<span id="showAddBtn" class="hide">
			                        	<a id="spanBtnAddNewLine" class="add" onclick="BINOLSTSFH16.addNewLine();">
											<span class="ui-icon icon-add"></span>
											<span class="button-text"><s:text name="stsfh16.btnAddRow"/></span>
										</a>
									</span>
									<span id="showDelBtn" class="hide">
				                        <a id="spanBtnDeleteRow" class="delete" onclick="BINOLSTSFH16.deleteRow();">
				                            <span class="ui-icon icon-delete"></span>
				                            <span class="button-text"><s:text name="stsfh16.btnDeleteRow"/></span>
				                        </a>
			                        </span>
		                        </s:elseif>
							</span>
							</div>
							<div style="width:100%;overflow-x:scroll;">
								<input type="hidden" id="rowNumber" value="<s:property value='saleOrderDetailList.size()'/>"/>
								<input type="hidden" id="saleOrderNo" name="saleOrderNo" value="<s:property value='saleOrderNo'/>"/>
								<input type="hidden" id="saleDetailList" name="saleDetailList" value=""/>
								<table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
									<thead>
										<tr>
											<th class="tableheader" width="3%">
												<input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/>
												<s:text name="stsfh16.select"/>
											</th>
											<th class="tableheader" width="10%"><s:text name="stsfh16.unitCode"/></th>
											<th class="tableheader" width="10%"><s:text name="stsfh16.barCode"/></th>
											<th class="tableheader" width="10%"><s:text name="stsfh16.productName"/></th>
											<th class="tableheader" width="5%"><s:text name="stsfh16.unit"/></th>
											<th class="tableheader" width="5%"><s:text name="stsfh16.price"/></th>
											<th class="tableheader" width="10%"><s:text name="stsfh16.quantity"/></th>
											<th class="tableheader" width="10%"><s:text name="stsfh16.discountRate"/></th>
											<th class="tableheader" width="10%"><s:text name="stsfh16.discountPrice"/></th>
											<th class="tableheader" width="10%"><s:text name="stsfh16.amount"/></th>
											<th class="tableheader" width="20%"><s:text name="stsfh16.comments"/></th>
											<th style="display:none">
										</tr>
									</thead>
									<tbody id="databody">
										<s:iterator value="saleOrderDetailList" id="saleOrderDetail" status="status">
										<tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
											<td class="center">
												<input type="checkbox" id="chkbox" value='<s:property value="BIN_BackstageSaleID" />' onclick="BINOLSTSFH16.changechkbox(this);"/>
											</td>
											<td>
												<span id="spanUnitCode"><s:property value="UnitCode"/></span>
												<input type="hidden" id="unitCode" name="unitCode" value="<s:property value='UnitCode'/>"/>
											</td>
											<td>
												<span id="spanBarCode"><s:property value="BarCode"/></span>
												<input type="hidden" id="barCode" name="barCode" value="<s:property value='BarCode'/>"/>
											</td>
											<td>
												<span id="spanProductName"><s:property value="ProductName"/></span>
											</td>
											<td>
												<span id="spanModuleCode"><s:property value="unit"/></span>
											</td>
											<td>
												<span id="spanPrice"><s:property value="PricePay"/></span>
												<input type="hidden" id="pricePay" name="pricePay" value="<s:property value='PricePay'/>">
											</td>
											<td class="center">
												<s:if test='"2".equals(operateType)'>
													<input value="<s:property value='Quantity'/>" name="quantityuArr" id="quantityuArr" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="7" onchange="BINOLSTSFH16.changeQuantity(this)" onkeyup="BINOLSTSFH16.changeQuantity(this)"  cssStyle="width:98%"/>
												</s:if>
												<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
						            				<span id="spanQuantity" class="left"><s:property value="Quantity"/></span>
						            				<input class="hide" value="<s:property value='Quantity'/>" name="quantityuArr" id="quantityuArr" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="7" onchange="BINOLSTSFH16.changeQuantity(this)" onkeyup="BINOLSTSFH16.changeQuantity(this)"  cssStyle="width:98%"/>
						            			</s:elseif>
						            			<s:else>
													<s:property value="Quantity"/>
												</s:else>
											</td>
											<td class="center">
												<s:if test='"2".equals(operateType)'>
													<input value="<s:property value='DiscountRate'/>" name="discountRateArr" id="discountRateArr" cssClass="text-number" style="width:60px;text-align:right;" size="10" maxlength="7" onchange="BINOLSTSFH16.changeDiscountRate(this)" onkeyup="BINOLSTSFH16.keyUpChangeDiscountRate(this)"  cssStyle="width:98%"/>
												</s:if>
												<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
						            				<span id="spanDiscountRate" class="left"><s:property value="DiscountRate"/></span>
						            				<input class="hide" value="<s:property value='DiscountRate'/>" name="discountRateArr" id="discountRateArr" cssClass="text-number" style="width:60px;text-align:right;" size="10" maxlength="7" onchange="BINOLSTSFH16.changeDiscountRate(this)" onkeyup="BINOLSTSFH16.keyUpChangeDiscountRate(this)"  cssStyle="width:98%"/>
						            			</s:elseif>
						            			<s:else>
													<s:property value="DiscountRate"/>
												</s:else>
											</td>
											<td class="center">
												<s:if test='"2".equals(operateType)'>
													<input value="<s:property value='DiscountAmount'/>" name="discountPriceArr" id="discountPriceArr" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="12" onchange="BINOLSTSFH16.changeDiscountPrice(this)" onkeyup="BINOLSTSFH16.keyUpChangeDiscountPrice(this)"  cssStyle="width:98%"/>
												</s:if>
												<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
						            				<span id="spanDiscountAmount" class="left"><s:property value="DiscountAmount"/></span>
						            				<input class="hide" value="<s:property value='DiscountAmount'/>" name="discountPriceArr" id="discountPriceArr" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="12" onchange="BINOLSTSFH16.changeDiscountPrice(this)" onkeyup="BINOLSTSFH16.keyUpChangeDiscountPrice(this)"  cssStyle="width:98%"/>
						            			</s:elseif>
						            			<s:else>
													<s:property value="DiscountAmount"/>
												</s:else>
											</td>
											<td class="center tdAmount" style="text-align:right;">
												<s:property value="PayAmount"/>
											</td>
											<td class="center">
												<s:if test='"2".equals(operateType)'>
													<input value="<s:property value='Comment'/>" name="reasonArr" type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/>
												</s:if>
												<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
						            				<span id="spanComment" class="left"><s:property value="Comment"/></span>
						            				<input class="hide" value="<s:property value='Comment'/>" name="reasonArr" type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/>
						            			</s:elseif>
						            			<s:else>
													<s:property value="Comment"/>
												</s:else>
											</td>
											<td style="display:none">
												<input type="hidden" id="prtVendorId" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
												<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
												<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="<s:property value='Price'/>"/>
												<input type="hidden" id="payAmount" name="payAmount" value="<s:property value='PayAmount'/>"/>
												<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value="<s:property value='Quantity*Price'/>"/>
											</td>
										</tr>
										</s:iterator>
									</tbody>
								</table>
							</div>
							<hr class="space" />
							<table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
							<tr>
								<th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
								<td class="center"><s:text name="stsfh16.totalDiscountRate"/></td><%-- 整单折扣率 --%>
								<td class="center"><s:text name="stsfh16.totalDiscountPrice"/></td><%-- 整单折扣金额 --%>
								<td class="center"><s:text name="stsfh16.totalQuantity"/></td><%-- 总数量 --%>
								<td class="center"><s:text name="stsfh16.totalAmount"/></td><%-- 总金额--%>
							</tr>
							<tr>
								<td class="center">
									<%-- 整单折扣率 --%>
									<s:if test='null!=saleOrdersMainData.DiscountRate && saleOrdersMainData.DiscountRate!=""'>
										<s:if test='"2".equals(operateType)'>
											<input type="text" value="<s:property value='saleOrdersMainData.DiscountRate'/>" name="totalDiscountRate" id="totalDiscountRate" class="text" onchange="BINOLSTSFH16.changeBillDiscountRate(this)" onkeyup="BINOLSTSFH16.keyUpChangeBillDiscountRate(this)" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="7" cssStyle="width:98%"/>
										</s:if>
										<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
				            				<span id="spanTotalDiscountRate" class="left"><s:property value="saleOrdersMainData.DiscountRate"/></span>
				            				<div id="divTotalDiscountRate" class="hide">
				            					<input type="text" value="<s:property value='saleOrdersMainData.DiscountRate'/>" name="totalDiscountRate" id="totalDiscountRate" class="text" onchange="BINOLSTSFH16.changeBillDiscountRate(this)" onkeyup="BINOLSTSFH16.keyUpChangeBillDiscountRate(this)" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="7" cssStyle="width:98%"/>
				            				</div>
				            			</s:elseif>
										<s:else>
											<s:property value='saleOrdersMainData.DiscountRate'/>
										</s:else>
									</s:if>
									<s:else>
										<s:if test='"2".equals(operateType)'>
											<input type="text" value="" name="totalDiscountRate" id="totalDiscountRate" class="text" onchange="BINOLSTSFH16.changeBillDiscountRate(this)" onkeyup="BINOLSTSFH16.keyUpChangeBillDiscountRate(this)" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="7" cssStyle="width:98%"/>
										</s:if>
										<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
				            				<span id="spanTotalDiscountRate" class="left">&nbsp;</span>
				            				<div id="divTotalDiscountRate" class="hide">
				            					<input type="text" value="" name="totalDiscountRate" id="totalDiscountRate" class="text" onchange="BINOLSTSFH16.changeBillDiscountRate(this)" onkeyup="BINOLSTSFH16.keyUpChangeBillDiscountRate(this)" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="7" cssStyle="width:98%"/>
				            				</div>
				            			</s:elseif>
										<s:else>
											&nbsp;
										</s:else>
									</s:else>
								</td>
								<td class="center">
									<%-- 整单折扣金额--%>
									<s:if test='null!=saleOrdersMainData.DiscountAmount && saleOrdersMainData.DiscountAmount!=""'>
										<s:if test='"2".equals(operateType)'>
											<input type="text" value="<s:property value='saleOrdersMainData.DiscountAmount'/>" name="totalDiscountPrice" id="totalDiscountPrice" class="text" onchange="BINOLSTSFH16.changeBillDiscountPrice(this)" onkeyup="BINOLSTSFH16.keyUpChangeBillDiscountPrice(this)" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="12" cssStyle="width:98%"/>
										</s:if>
										<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
				            				<span id="spanTotalDiscountPrice" class="left"><s:property value='saleOrdersMainData.DiscountAmount'/></span>
				            				<div id="divTotalDiscountPrice" class="hide">
				            					<input type="text" value="<s:property value='saleOrdersMainData.DiscountAmount'/>" name="totalDiscountPrice" id="totalDiscountPrice" class="text" onchange="BINOLSTSFH16.changeBillDiscountPrice(this)" onkeyup="BINOLSTSFH16.keyUpChangeBillDiscountPrice(this)" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="12" cssStyle="width:98%"/>
				            				</div>
				            			</s:elseif>
										<s:else>
											<s:property value='saleOrdersMainData.DiscountAmount'/>
										</s:else>
									</s:if>
									<s:else>
										<s:if test='"2".equals(operateType)'>
											<input type="text" value="" name="totalDiscountPrice" id="totalDiscountPrice" class="text" onchange="BINOLSTSFH16.changeBillDiscountPrice(this)" onkeyup="BINOLSTSFH16.keyUpChangeBillDiscountPrice(this)" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="12" cssStyle="width:98%"/>
										</s:if>
										<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
				            				<span id="spanTotalDiscountPrice" class="left">&nbsp;</span>
				            				<div id="divTotalDiscountPrice" class="hide">
				            					<input type="text" value="" name="totalDiscountPrice" id="totalDiscountPrice" class="text" onchange="BINOLSTSFH16.changeBillDiscountPrice(this)" onkeyup="BINOLSTSFH16.keyUpChangeBillDiscountPrice(this)" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="12" cssStyle="width:98%"/>
				            				</div>
				            			</s:elseif>
										<s:else>
											&nbsp;
										</s:else>
									</s:else>
								</td>
								<td class="center">
									<%-- 总数量 --%>
									<s:if test='null!=saleOrdersMainData.TotalQuantity && saleOrdersMainData.TotalQuantity!=""'>
										<span id="spanTotalQuantity"><s:property value='saleOrdersMainData.TotalQuantity'/></span>
										<input type="hidden" id="totalQuantity" name="totalQuantity" value="<s:property value='saleOrdersMainData.TotalQuantity'/>"/>
									</s:if>
									<s:else>
										<span id="spanTotalQuantity">0</span>
										<input type="hidden" id="totalQuantity" name="totalQuantity" value=""/>
									</s:else>
								</td>
								<td class="center">
									<%-- 总金额--%>
									<s:if test='null!=saleOrdersMainData.PayAmount && saleOrdersMainData.PayAmount!=""'>
										<span id="spanTotalAmount"><s:property value='saleOrdersMainData.PayAmount'/></span>
										<input type="hidden" id="totalAmount" name="totalAmount" value="<s:property value='saleOrdersMainData.PayAmount'/>"/>
									</s:if>
									<s:else>
										<span id="spanTotalAmount">0.00</span>
										<input type="hidden" id="totalAmount" name="totalAmount" value=""/>
									</s:else>
								</td>
							</tr>
							</table>
							<input type="hidden" id="billTotalAmount" name="billTotalAmount" value="<s:property value='saleOrdersMainData.OriginalAmount'/>"/>
							<hr class="space" />
							<div class="center clearfix">
								<s:if test='"2".equals(operateType)'>
									<button id="btnSave" class="save" type="button" onclick="BINOLSTSFH16.save();return false;">
										<span class="ui-icon icon-save"></span>
										<span class="button-text"><s:text name="stsfh16.save"/></span>
									</button>
									<cherry:show domId="BINOLSTSFH1601">
										<button id="btnSubmit" class="confirm" type="button" onclick="BINOLSTSFH16.confirm();return false;">
											<span class="ui-icon icon-confirm"></span>
											<span class="button-text"><s:text name="stsfh16.confirm"/></span>
										</button>
									</cherry:show>
				                    <button id="btnDelete" class="delete" onclick="BINOLSTSFH16.delete();return false;">
				                    	<span class="ui-icon icon-delete-big"></span>
				                        <span class="button-text"><s:text name="global.page.delete"/></span>
				                    </button>
				                    <button id="btnEdit" class="confirm" onclick="BINOLSTSFH16.modifyOrder();return false;">
				                        <span class="ui-icon icon-edit-big"></span>
				                        <%-- 修改 --%>
				                        <span class="button-text"><s:text name="os.edit"/></span>
				                    </button>
								</s:if>
								<s:elseif test='"151".equals(operateType) || "152".equals(operateType) || "40".equals(operateType)'>
									<jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
									<s:if test='"152".equals(operateType)'>
										<button id="btnSave" class="save" type="button" onclick="BINOLSTSFH16.save();return false;">
											<span class="ui-icon icon-save"></span>
											<span class="button-text"><s:text name="stsfh16.save"/></span>
										</button>
										<!--
										<cherry:show domId="BINOLSTSFH1601">
											<button id="btnSubmit" class="confirm" type="button" onclick="BINOLSTSFH16.confirm();return false;">
												<span class="ui-icon icon-confirm"></span>
												<span class="button-text"><s:text name="stsfh16.confirm"/></span>
											</button>
										</cherry:show>
										-->
                                        <button id="btnEdit" class="confirm" onclick="BINOLSTSFH16.modifyOrder();return false;">
                                            <span class="ui-icon icon-edit-big"></span>
                                            <%-- 修改 --%>
                                            <span class="button-text"><s:text name="os.edit"/></span>
                                        </button>
                                        <!--
										<button id="btnDelete" class="delete" onclick="BINOLSTSFH16.delete();return false;">
											<span class="ui-icon icon-delete-big"></span>
					                        <span class="button-text"><s:text name="global.page.delete"/></span>
					                    </button>
					                    -->
									</s:if>
								</s:elseif>
								<button id="btnBack" class="close" onclick="BINOLSTSFH16.back();return false;" style="display:none;">
									<span class="ui-icon icon-back"></span>
									<%--返回 --%>
									<span class="button-text"><s:text name="global.page.back"/></span>
								</button>
								<button id="btnClose" class="close" onclick="window.close();return false;"><span class="ui-icon icon-close"></span>
									<%-- 关闭 --%>
									<span class="button-text"><s:text name="global.page.close"/></span>
								</button>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div id="tabs-2">
				<strong><s:text name="global.page.worksProcessing"/></strong>
			</div>
		</div>
	</div>
</div>
</s:i18n>
<form action="BINOLSTSFH16_init" id="saleOrdersDetailUrl" method="post">
	<input name="csrftoken" type="hidden"/>
	<s:hidden id="saleId" name="saleId" value="%{#request.saleId}"></s:hidden>
</form>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 报表打印共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
<%-- ==================  报表打印共通导入 End ======================== --%>
<%-- ================== 工作流提示弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOSDialog.jsp" flush="true" />
<%-- ==================  工作流提示弹出窗口End ======================== --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<div id="errmessage" style="display:none">
  <input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
<input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
<input type="hidden" id="errmsg3" value='<s:text name="EST00035"/>'/>
<input type="hidden" id="errorDiscountRate" value='<s:text name="stsfh16.errorDiscountRate"/>'/>
<input type="hidden" id="errorDiscountPrice" value='<s:text name="stsfh16.errorDiscountPrice"/>'/>
</div>


