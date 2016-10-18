<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.wp.BINOLWPSAL06">
<div id="aaData">
	<s:iterator value="billList" id="getBills">
		<ul>
			<li><s:property value="RowNumber" /></li>
			<li><s:property value="billCode" /></li>
			<li><s:property value="businessDate" /></li>
			<li><s:property value="hangTime" /></li>
			<li><s:property value="memberCode" /></li>
			<li><s:property value="baName" /></li>
			<li><%-- 数量 --%>
				<s:if test="quantity != null">
					<s:if test="quantity >= 0"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li><%-- 金额 --%>
				<s:if test="amount != null">
					<s:if test="amount >= 0"><s:text name="format.price"><s:param value="amount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="amount"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li>
				<s:if test='collectStatus == NULL || "0" == collectStatus'>
					<button id="dgBtnGetBill" class="wp_search_s" type="button" onclick="BINOLWPSAL06.getBill('${hangBillId}');return false;">
						<span class="icon_search"></span>
						<span class="wp_search_text"><s:text name="wpsal06.getBill" /></span>
					</button>
				</s:if>
				<s:else>
					<button id="dgBtnGetBill" class="wp_search_s" type="button" onclick="BINOLWPSAL06.sendMQ('${hangBillId}');return false;">
						<span class="icon_search"></span>
						<span class="wp_search_text">重试</span>
					</button>
				</s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
