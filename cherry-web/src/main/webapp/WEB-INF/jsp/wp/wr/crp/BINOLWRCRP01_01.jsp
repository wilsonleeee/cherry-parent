<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.wp.BINOLWRCRP01">
	<div id="aaData">
		<s:iterator value="campaignOrderList" id="campaignOrderMap">
			<ul>
				<%-- 编号 --%>
				<li>${RowNumber}</li>
				<%-- 二维码 --%>
				<li><s:property value="couponCode" /></li>
				<%-- 活动类型 --%>
				<li><s:property value='#application.CodeTable.getVal("1178", subType)'/></li>
				<%-- 客户姓名 --%>
				<li><s:property value="customerName" /></li>
				<%-- 手机号码 --%>
				<li><s:property value="mobilePhone" /></li>
				<%-- 客户生日 --%>
				<li><s:property value="birthDay" /></li>
				<%--客户性别 --%>
				<li><s:property value='#application.CodeTable.getVal("1006", gender)'/></li>
				<%--预约操作日期--%>
				<li><s:property value="campaignOrderDate" /></li>
				<%--预约服务日期--%>
				<li><s:property value="bookDate" /></li>
				<%--实际领日期--%>
				<li><s:property value="finishTime" /></li>
				<%--操作员--%>
				<li><s:property value="optPerson" /></li>
				<%--状态--%>
				<li><s:if test='state != null && "OK".equals(state)'>
	                <span><s:text name="WRCRP01_ok"></s:text></span>
	            </s:if>
	            <s:elseif test='state != null && "AR".equals(state)'>
	            	<span><s:text name="WRCRP01_order"></s:text></span>
	            </s:elseif>
	            <s:else>&nbsp;</s:else></li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>