<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.wp.BINOLWYWYL01">
<div id="aaData">
	<s:iterator value="billList" id="searchBills">
		<ul>
			<li><s:property value="rowNumber" /></li>
			<li><s:property value="memberName" /></li>
			<li><s:property value="mobilePhone" /></li>
			<li><s:property value="birthday" /></li>
			<li><s:property value="gender" /></li>
			<li><s:property value="activityType" /></li>
			<li><s:property value="applyGetDate" /></li>
			<li><s:property value="city" /></li>
			<li><s:property value="state" /></li>
			<li>
				<button id="btnReservation" class="wp_search_s" type="button" style="float:left; margin:0 5px 0 0" onclick="BINOLWYWYL01.reservation(this);return false;">
					<span class="icon_search"></span>
					<span class="wp_search_text"><s:text name="wywyl01.reservation" /></span>
				</button>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
