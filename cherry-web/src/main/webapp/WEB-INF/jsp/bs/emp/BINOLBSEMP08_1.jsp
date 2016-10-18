<!-- 批次模式一览LIST -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSEMP08">
<div id="aaData">
		<s:iterator value="baCouponList" id="baCouponMap">
			<ul>
				<%-- NO. --%>
				<li>${RowNumber}</li>
				<%-- 一级代理商 --%>
				<li><span><s:property value="parentResellerNameShow"/></span></li>
				<%-- 二级代理商 --%>
				<li><span><s:property value="resellerNameShow"/></span></li>
				<%-- 优惠券 --%>
				<li><span><s:property value="couponCode"/></span></li>
				<%-- 优惠券 --%>
				<li>
					<s:if test='0==useFlag'>
				        <span><s:text name="EMP08_unUsed"></s:text></span>
					</s:if>
					<s:if test='1==useFlag'>
				        <span><s:text name="EMP08_used"></s:text></span>
					</s:if>
				</li>
				<%-- 同步状态--%>
				<li>
					<s:if test='0==synchFlag'>
				        <span class="verified_unsubmit">
				        	<span><s:property value="#application.CodeTable.getVal('1312',synchFlag)"/></span>
				        </span>
					</s:if>
					<s:if test='1==synchFlag'>
				        <span class="verifying">
				        	<span><s:property value="#application.CodeTable.getVal('1312',synchFlag)"/></span>
				        </span>
					</s:if>
					<s:if test='2==synchFlag'>
				        <span class="task-verified">
				        	<span><s:property value="#application.CodeTable.getVal('1312',synchFlag)"/></span>
				        </span>
					</s:if>
				</li>
				<li>
					<s:if test='null != synchFlag && "0".equals(synchFlag)'>
						<!-- 清除未同步的批次优惠券 -->
	                	<cherry:show domId="BINOLBSEMP07DEL">
	                		<s:url action="BINOLBSEMP08_deleteBaCoupon" id="deleteBaCouponUrl">
								<s:param name="baCouponId" value="%{#baCouponMap.baCouponId}" />
							</s:url>
		                	<a href="${deleteBaCouponUrl}" id="deleteBtn" class="delete" onclick='BINOLBSEMP08.popDeleteDialog(this);return false;'>
				                <span class="ui-icon icon-delete"></span>
				                <span class="button-text"><s:text name="global.page.delete"/></span>
			                </a>
		                </cherry:show>
					</s:if>
				</li>
			</ul>
		</s:iterator>
</div>
</s:i18n>
