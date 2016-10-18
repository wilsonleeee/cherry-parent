<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/wy/wyl/BINOLWYWYL03.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/cherry_timepicker.css" type="text/css">
<s:i18n name="i18n.wp.BINOLWYWYL03">
<div class="hide">
	<s:url id="s_searchURL" value="/wy/BINOLWYWYL03_search" />
	<a id="searchUrl" href="${s_searchURL}"></a>
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
	<div class="section">
		<s:text name="wywyl03.selectAll" id="selectAll"/>
		<div class="section-header">
			<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="wywyl03.general"/></strong>
		</div>
		<div class="section-content">
			<cherry:form id="mainForm" class="inline">
				<table class="detail">
					<tr>
						<th><s:text name="wywyl03.memberName" /></th>
	            		<td><span><input id="memberName" name="memberName" maxlength="20" value="" /></span></td>
	            		<th><s:text name="wywyl03.mobilePhone" /></th>
	            		<td><span id="mobilePhone"></span></td>
	            	</tr>
	            	<tr>
	            		<th><s:text name="wywyl03.birthDay" /></th>
	            		<td>
		            		<span>
								<select id="birthDayMonthQ" name="birthDayMonthQ" style="width:50px;height:20px;">
									<option value=""></option>
								</select>
								<select id="birthDayDateQ" name="birthDayDateQ" style="width:50px;height:20px;">
									<option value=""></option>
								</select>
							</span>
	            		</td>
	            		<th><s:text name="wywyl03.gender" /></th>
	            		<td>
	            			<s:select id="gender" name="gender" list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" />
	            		</td>
	            	</tr>
	            	<tr>
						<th><s:text name="wywyl03.reservationDate" /></th>
	            		<td><span><s:textfield id="reservationDate" name="reservationDate" cssClass="date" cssStyle="width:90px"></s:textfield></span></td>
	            		<th><s:text name="wywyl03.reservationTime" /></th>
	            		<td>
	            			<select id="reservationTime" name="reservationTime" style="width:50px;height:20px;">
								<option value="">9:00</option>
								<option value="">10:00</option>
								<option value="">11:00</option>
								<option value="">12:00</option>
								<option value="">13:00</option>
								<option value="">14:00</option>
								<option value="">15:00</option>
								<option value="">16:00</option>
								<option value="">17:00</option>
								<option value="">18:00</option>
								<option value="">19:00</option>
								<option value="">20:00</option>
							</select>
						</td>
	            	</tr>
	            	<tr>
	            		<th><s:text name="wywyl03.activityType" /></th>
	            		<td><s:select id="activityType" name="activityType" list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" /></td>
	            		<th><s:text name="wywyl03.counterName" /></th>
	            		<td><span id="counterName"></span></td>
	            	</tr>
	            	<tr>
	            		<th><s:text name="wywyl03.baName" /></th>
	            		<td><span id="baName"></span></td>
	            		<th><s:text name="wywyl03.memo" /></th>
	            		<td><span><input id="memo" name="memo" maxlength="50" value="" /></span></td>
	            	</tr>
	            </table>
			</cherry:form>
		</div>
	</div>
	<div class="section">
		<div class="section-header">
			<strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="wywyl03.detail"/></strong>
		</div>
		<div class="section-content">
			<div style="width:100%;overflow-x:scroll;">
				<table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
					<thead>
						<tr>
							<th class="tableheader" width="10%"><s:text name="wywyl03.rowNumber"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl03.unitCode"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl03.barCode"/></th>
							<th class="tableheader" width="5%"><s:text name="wywyl03.productName"/></th>
							<th class="tableheader" width="5%"><s:text name="wywyl03.price"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl03.quantity"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl03.activityName"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl03.memo"/></th>
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
				<td class="center"><s:text name="wywyl03.totalQuantity"/></td><%-- 总数量 --%>
				<td class="center"><s:text name="wywyl03.totalAmount"/></td><%-- 总金额--%>
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
				<button class="save" type="button" onclick="BINOLWYWYL03.reservation();return false;">
					<span class="ui-icon icon-save"></span>
					<span class="button-text"><s:text name="wywyl03.reservation"/></span>
				</button>
			</div>
		</div>
	</div>
</div>
</s:i18n>
<%-- ================== dataTable共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通END  ======================= --%>