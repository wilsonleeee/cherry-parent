<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/wy/wyl/BINOLWYWYL02.js"></script>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/blueprint/webpos.css" type="text/css">
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/cherry_timepicker.css" type="text/css">
<s:i18n name="i18n.wp.BINOLWYWYL02">
<div class="hide">
	<s:url id="s_searchURL" value="/wy/BINOLWYWYL02_search" />
	<a id="searchUrl" href="${s_searchURL}"></a>
	<s:url id="s_getGiftURL" value="/wy/BINOLWYWYL02_getGift" />
	<a id="getGiftUrl" href="${s_getGiftURL}"></a>
</div>
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
<div class="panel-content">
	<div style="height:30px;">
		<span>
			<s:text name="wywyl02.orderCode" />
			<input id="applyCouponEncry" name="applyCouponEncry" type="text" maxlength="30" class="text" style="width:220px;" value="" onchange="BINOLWYWYL02.changeCouponCode(this)" onkeyup="BINOLWYWYL02.changeCouponCode(this)" />
	    	<input id="applyCoupon" name="applyCoupon" type="hidden" value=""/>
	    	<button id="btnSearch" class="close" type="button" onclick="BINOLWYWYL02.search();return false;">
	    		<span class="ui-icon icon-search"></span>
				<span class="button-text"><s:text name="wywyl02.search"/></span>
			</button>
	    </span>
	</div>
	<div id="detailDiv" class="hide">
		<div class="section">
			<s:text name="wywyl02.selectAll" id="selectAll"/>
			<div class="section-content">
				<cherry:form id="mainForm" class="inline">
					<input type="hidden" id="billCode" name="billCode" value=""/>
					<input type="hidden" id="subType" name="subType" value=""/>
					<input type="hidden" id="memberInfoId" name="memberInfoId" value=""/>
					<input type="hidden" id="messageId" name="messageId" value=""/>
					<input type="hidden" id="orgMemberName" name="orgMemberName" value=""/>
					<input type="hidden" id="orgBirthDay" name="orgBirthDay" value=""/>
					<input type="hidden" id="orgGender" name="orgGender" value=""/>
					<input type="hidden" id="rowNumber" name="rowNumber" value="0"/>
					<table class="detail">
						<tr>
							<th><s:text name="wywyl02.memberName" /></th>
		            		<td><span><input id="memberName" name="memberName" maxlength="10" class="text" value="" /></span></td>
		            		<th><s:text name="wywyl02.state" /></th>
		            		<td><s:select id="state" name="state" list='#application.CodeTable.getCodes("1325")' listKey="CodeKey" listValue="Value" /></td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="wywyl02.birthDay" /></th>
		            		<td>
			            		<span>
									<select id="birthDayMonthQ" name="birthDayMonthQ" style="width:50px;height:20px;">
										<option value=""></option>
									</select>
									<s:text name="wywyl02.month" />
									<select id="birthDayDateQ" name="birthDayDateQ" style="width:50px;height:20px;">
										<option value=""></option>
									</select>
									<s:text name="wywyl02.day" />
								</span>
		            		</td>
		            		<th><s:text name="wywyl02.gender" /></th>
		            		<td>
		            			<s:select id="gender" name="gender" list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" />
		            		</td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="wywyl02.mobilePhone" /></th>
		            		<td>
		            			<span id="mobilePhone"></span>
		            			<input type="hidden" id="mobile" name="mobile" value=""/>
		            		</td>
		            		<th><s:text name="wywyl02.counterName" /></th>
		            		<td><span id="counterName"></span></td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="wywyl02.activityType" /></th>
		            		<td>
		            			<input id="campaignId" name="campaignId" type="hidden" />
	           	  				<span><input id="subCampaignName" name="subCampaignName" maxlength="30" class="text" disabled="disabled" value=""/></span>
		            		</td>
		            		<th><s:text name="wywyl02.actionTime" /></th>
		            		<td><span id="actionTime"></span></td>
		            	</tr>
		            	<tr>
		            		<th><s:text name="wywyl02.baName" /></th>
		            		<td><span id="baName"><s:property value="userName"/></span></td>
		            		<th><s:text name="wywyl02.memo" /></th>
		            		<td><span><input id="memo" name="memo" maxlength="100" class="text" style="width:300px;" value="" /></span></td>
		            	</tr>
		            </table>
				</cherry:form>
			</div>
		</div>
		<div class="section">
			<div class="section-header">
				<strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="wywyl02.detail"/></strong>
			</div>
			<div class="section-content">
				<div style="width:100%;overflow-x:scroll;">
					<table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
						<thead>
							<tr>
								<th class="tableheader" width="3%"><s:text name="wywyl02.rowNumber"/></th>
								<th class="tableheader" width="10%"><s:text name="wywyl02.unitCode"/></th>
								<th class="tableheader" width="10%"><s:text name="wywyl02.barCode"/></th>
								<th class="tableheader" width="20%"><s:text name="wywyl02.productName"/></th>
								<th class="tableheader" width="10%"><s:text name="wywyl02.price"/></th>
								<th class="tableheader" width="10%"><s:text name="wywyl02.quantity"/></th>
								<th class="tableheader" width="20%"><s:text name="wywyl02.activityName"/></th>
								<th class="tableheader" width="15%"><s:text name="wywyl02.memo"/></th>
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
					<td class="center"><s:text name="wywyl02.totalQuantity"/></td><%-- 总数量 --%>
					<td class="center"><s:text name="wywyl02.totalAmount"/></td><%-- 总金额--%>
				</tr>
				<tr>
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
				<hr class="space" />
				<div class="center clearfix">
					<button id="btnGetGift" class="save" type="button" onclick="BINOLWYWYL02.getGift();return false;">
						<span class="ui-icon icon-save"></span>
						<span class="button-text"><s:text name="wywyl02.getGift"/></span>
					</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="hide" id="messageDialogTitle"><s:text name="wywyl02.messageDialogTitle"/></div>
<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
	<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
	<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
	<p class="center">
		<button id="btnMessageConfirm" class="close" type="button">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="wywyl02.confirm"/></span>
		</button>
	</p>
</div>
</s:i18n>
<%-- ================== dataTable共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通END  ======================= --%>