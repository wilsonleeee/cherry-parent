<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH14.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/cherry_timepicker.css" type="text/css">
<s:i18n name="i18n.st.BINOLSTSFH14">
<div class="hide">
	<s:url id="s_submitURL" value="/st/BINOLSTSFH14_submit" />
	<a id="submitUrl" href="${s_submitURL}"></a>
	<s:url id="s_saveURL" value="/st/BINOLSTSFH14_save" />
	<a id="saveUrl" href="${s_saveURL}"></a>
	<s:url id="s_getBillCodeURL" value="/st/BINOLSTSFH14_getBillCode" />
	<a id="getBillCodeUrl" href="${s_getBillCodeURL}"></a>
	<s:url id="s_getdepotAjaxURL" value="/st/BINOLSTSFH14_getDepot" />
	<a id="getdepotAjaxUrl" href="${s_getdepotAjaxURL}"></a>
	<s:url id="s_getDepartAjaxURL" value="/st/BINOLSTSFH14_getDepartInfo" />
	<a id="getDepartAjaxUrl" href="${s_getDepartAjaxURL}"></a>
	<s:url id="s_getBussPartnerAjaxURL" value="/st/BINOLSTSFH14_getBussinessPartner" />
	<a id="getBussPartnerAjaxUrl" href="${s_getBussPartnerAjaxURL}"></a>
	<s:url id="s_initImportURL" value="/st/BINOLSTSFH14_initImportProduct" />
	<a id="initImportUrl" href="${s_initImportURL}"></a>
	<s:url id="s_saleProductImportURL" value="/st/BINOLSTSFH14_importSaleProduct" />
	<a id="saleProductImportUrl" href="${s_saleProductImportURL}"></a>
</div>
<cherry:form id="mainForm" class="inline">
	<div class="panel-header">
		<div class="clearfix"> 
			<span class="breadcrumb left">      
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
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
					<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="stsfh14.general"/></strong>
				</div>
				<div class="section-content">
					<input type="hidden" type="text" name="curPerson" id="curPerson" value=""/>
					<input type="hidden" type="text" name="curAddress" id="curAddress" value=""/>
					<table class="detail">
					<tr>
						<th><s:text name="stsfh14.saleOrderNo"/></th>
	            		<td id="tdSaleBill"><s:property value="saleOrderNo"/></td>
	            		<th><s:text name="stsfh14.billType"/></th>
	            		<td><s:select name="saleBillType" list='#application.CodeTable.getCodes("1293")' listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/></td>
	            	</tr>
	            	<tr>
	            		<th><s:text name="stsfh14.customerType"/></th>
	            		<td><s:select name="customerType" list='#application.CodeTable.getCodes("1297")' listKey="CodeKey" listValue="Value" headerKey="" onchange="BINOLSTSFH14.changeCustomerType(this);return false;" cssStyle="width:150px;"/></td>
	            		<th><s:text name="stsfh14.expectFinishDate"/><span class="highlight">*</span></th>
	            		<td><s:textfield name="expectFinishDate" id="expectFinishDate" cssClass="date"/></td>
	            	</tr>
	            	<tr>
	            		<th><s:text name="stsfh14.customerName"/><span class="highlight">*</span></th>
	            		<td>
	            			<input type="hidden" name="customerOrganizationId" id="customerOrganizationId"></input>
                        	<input type="text" class="text" id="customerOrganization"></input>
                        </td>
                        <th><s:text name="stsfh14.saleDate"/></th>
	            		<td>
	            			<s:textfield name="saleDate" id="saleDate" cssClass="date" value="%{saleDate}"/>
	            			<s:textfield name="saleTime" id="saleTime" cssClass="date" value="%{saleTime}"/>
	            		</td>
	            	</tr>
	            	<tr>
	            		<th><s:text name="stsfh14.contactPerson"/></th>
	            		<td><input type="text" name="contactPerson" id="contactPerson" class="text" maxlength="30"/></td>
	            		<th><s:text name="stsfh14.deliverAddress"/></th>
	            		<td><input type="text" name="deliverAddress" id="deliverAddress" class="text" style="width:75%;" maxlength="200"/></td>
	            	</tr>
	            	<tr id="trCustomerDepot">
	            		<th><s:text name="stsfh14.customerDepot"/><span class="highlight">*</span></th>
	            		<td>
	            			<select style="width:200px;" name="customerDepot" id="customerDepot" disabled="disabled" >
								<option value=""><s:text name="stsfh14.ddlDefaultText"/></option>
		                  		<s:iterator>
					         		<option value="<s:property value="BIN_InventoryInfoID" />">
					         			<s:property value="InventoryName"/>
					         		</option>
					      		</s:iterator>
		                  	</select>
	            		</td>
	            		<th><s:text name="stsfh14.customerLogicDepot"/><span class="highlight">*</span></th>
	            		<td><s:select id="customerLogicDepot" name="customerLogicDepot" list="logicInventoryList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" disabled="disabled" cssStyle="width:200px;" /></td>
	            	</tr>
	            	<tr>
	            		<th><s:text name="stsfh14.settlement"/></th>
	            		<td><s:select name="settlement" list='#application.CodeTable.getCodes("1175")' listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/></td>
	            		<th><s:text name="stsfh14.currency"/></th>
	            		<td><s:select name="currency" list='#application.CodeTable.getCodes("1296")' listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/></td>
	            	</tr>
	            </table>
	            <table class="detail">
	            	<tr>
	            		<th><s:text name="stsfh14.saleOrganization"/><span class="highlight">*</span></th>
	            		<td>
	            			<input type="hidden" name="organizationId" id="organizationId"></input>
                        	<input type="text" class="text" id="organization"></input>
	            		</td>
	            		<th><s:text name="stsfh14.saleEmployee"/><span class="highlight">*</span></th>
	            		<td>
	            			<input type="hidden" name="salesStaffId" id="salesStaffId"></input>
                        	<input type="text" class="text" id="salesStaff"></input>
	            		</td>
	            	</tr>
	            	<tr>
	            		<th><s:text name="stsfh14.saleDepot"/><span class="highlight">*</span></th>
	            		<td>
	            			<select style="width:200px;" name="saleDepot" id="saleDepot" disabled="disabled" >
								<option value=""><s:text name="stsfh14.ddlDefaultText"/></option>
		                  		<s:iterator>
					         		<option value="<s:property value="BIN_InventoryInfoID" />">
					         			<s:property value="InventoryName"/>
					         		</option>
					      		</s:iterator>
		                  	</select>
	            		</td>
	            		<th><s:text name="stsfh14.saleLogicDepot"/><span class="highlight">*</span></th>
	            		<td><s:select id="saleLogicDepot" name="saleLogicDepot" list="logicInventoryList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" disabled="disabled" cssStyle="width:200px;" /></td>
	            	</tr>
	            	<tr>
	            		<th><s:text name="stsfh14.comments"/></th>
	            		<td colspan="3"><input type="text" name=comments id="comments" class="text" style="width:75%" maxlength="50"/></td>
	            	</tr>
	            	</table>
	            </div>
			</div>
			<div class="section">
				<div class="section-header">
					<strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="stsfh14.detail"/></strong>
				</div>
				<div class="section-content">
					<div class="toolbar clearfix">
					<span class="left">
						<a id="spanBtnAddNewLine" class="add" onclick="BINOLSTSFH14.addNewLine();">
							<span class="ui-icon icon-add"></span>
							<span class="button-text"><s:text name="stsfh14.btnAddRow"/></span>
						</a>
                        <a id="spanBtnDeleteRow" class="delete" onclick="BINOLSTSFH14.deleteRow();">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="stsfh14.btnDeleteRow"/></span>
                        </a>
                        <a id="spanBtnImportProduct" class="add" onclick="BINOLSTSFH14.importProduct();">
							<span class="ui-icon icon-add"></span>
							<span class="button-text"><s:text name="stsfh14.btnImportProduct"/></span>
						</a>
                        <%--
						<a id="spanBtnSelectProduct" class="add" onclick="BINOLSTSFH14.openProPopup();">
							<span class="ui-icon icon-add"></span>
							<span class="button-text"><s:text name="stsfh14.btnSelectProduct"/></span>
						</a>
						--%>
					</span>
					</div>
					<div style="width:100%;overflow-x:scroll;">
						<input type="hidden" id="rowNumber" value="1"/>
						<input type="hidden" id="saleOrderNo" name="saleOrderNo" value="<s:property value='saleOrderNo'/>"/>
						<input type="hidden" id="saleDetailList" name="saleDetailList" value=""/>
						<table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
							<thead>
								<tr>
									<th class="tableheader" width="3%">
										<input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/>
										<s:text name="stsfh14.select"/>
									</th>
									<th class="tableheader" width="10%"><s:text name="stsfh14.unitCode"/></th>
									<th class="tableheader" width="10%"><s:text name="stsfh14.barCode"/></th>
									<th class="tableheader" width="10%"><s:text name="stsfh14.productName"/></th>
									<th class="tableheader" width="5%"><s:text name="stsfh14.unit"/></th>
									<th class="tableheader" width="5%"><s:text name="stsfh14.price"/></th>
									<th class="tableheader" width="10%"><s:text name="stsfh14.quantity"/></th>
									<th class="tableheader" width="10%"><s:text name="stsfh14.discountRate"/></th>
									<th class="tableheader" width="10%"><s:text name="stsfh14.discountPrice"/></th>
									<th class="tableheader" width="10%"><s:text name="stsfh14.amount"/></th>
									<th class="tableheader" width="20%"><s:text name="stsfh14.comments"/></th>
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
						<td class="center"><s:text name="stsfh14.totalDiscountRate"/></td><%-- 整单折扣率 --%>
						<td class="center"><s:text name="stsfh14.totalDiscountPrice"/></td><%-- 整单折扣金额 --%>
						<td class="center"><s:text name="stsfh14.totalQuantity"/></td><%-- 总数量 --%>
						<td class="center"><s:text name="stsfh14.totalAmount"/></td><%-- 总金额--%>
					</tr>
					<tr>
						<td class="center">
							<%-- 整单折扣率 --%>
							<input type="text" name="totalDiscountRate" id="totalDiscountRate" class="text" onchange="BINOLSTSFH14.changeBillDiscountRate(this)" onkeyup="BINOLSTSFH14.keyUpChangeBillDiscountRate(this)" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="7" cssStyle="width:98%"/>
						</td>
						<td class="center">
							<%-- 整单折扣金额--%>
							<input type="text" name="totalDiscountPrice" id="totalDiscountPrice" class="text" onchange="BINOLSTSFH14.changeBillDiscountPrice(this)" onkeyup="BINOLSTSFH14.keyUpChangeBillDiscountPrice(this)" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="12" cssStyle="width:98%"/>
						</td>
						<td class="center">
							<%-- 总数量 --%>
							<span id="spanTotalQuantity">0</span>
							<input type="hidden" id="totalQuantity" name="totalQuantity" value=""/>
						</td>
						<td class="center">
							<%-- 总金额--%>
							<span id="spanTotalAmount">0.00</span>
							<input type="hidden" id="totalAmount" name="totalAmount" value=""/>
						</td>
					</tr>
					</table>
					<input type="hidden" id="billTotalAmount" name="billTotalAmount" value=""/>
					<hr class="space" />
					<div class="center clearfix">
						<button class="save" type="button" onclick="BINOLSTSFH14.save();return false;">
							<span class="ui-icon icon-save"></span>
							<span class="button-text"><s:text name="stsfh14.save"/></span>
						</button>
						<cherry:show domId="BINOLSTSFH1401">
							<button class="confirm" type="button" onclick="BINOLSTSFH14.confirm();return false;">
								<span class="ui-icon icon-confirm"></span>
								<span class="button-text"><s:text name="stsfh14.confirm"/></span>
							</button>
						</cherry:show>
					</div>
				</div>
			</div>
		</div>
	</s:else>   
</cherry:form>
<div class="hide" id="dialogInit"></div>
<div class="hide" id="dialogTitle"><s:text name="stsfh14.importProduct"/></div>
<div id="errmessage" style="display:none">
	<input type="hidden" id="timeOnlyTitle" name="timeOnlyTitle" value='<s:text name="stsfh14.timeOnlyTitle"/>'/>
	<input type="hidden" id="currentText" name="currentText" value='<s:text name="stsfh14.currentText"/>'/>
	<input type="hidden" id="closeText" name="closeText" value='<s:text name="stsfh14.closeText"/>'/>
	<input type="hidden" id="timeText" name="timeText" value=""/>
	<input type="hidden" id="hourText" name="hourText" value=""/>
	<input type="hidden" id="minuteText" name="minuteText" value=""/>

	<input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
	<input type="hidden" id="errmsg3" value='<s:text name="EST00035"/>'/>
	<input type="hidden" id="errorDiscountRate" value='<s:text name="stsfh14.errorDiscountRate"/>'/>
	<input type="hidden" id="errorDiscountPrice" value='<s:text name="stsfh14.errorDiscountPrice"/>'/>
</div>
</s:i18n>
<%-- ================== dataTable共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通END  ======================= --%>