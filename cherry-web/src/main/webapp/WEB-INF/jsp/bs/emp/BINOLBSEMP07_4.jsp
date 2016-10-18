<!-- 批次模式一览LIST -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSEMP07">
<div id="aaData">
		<s:iterator value="batchList" id="batchMap">
			<ul>
				<%-- NO. --%>
				<li>${RowNumber}</li>
				<%-- 批次号 --%>
				<li>
					<s:url action="BINOLBSEMP08_init" id="createCouponBatchUrl">
						<s:param name="batchCode" value="%{#batchMap.batchCode }" />
					</s:url>
					<a href="${createCouponBatchUrl}" class="popup" onclick="javascript:openWin(this);return false;">
						<span><s:property value="batchCode"/></span>
					</a>
				</li>
				<li><span><s:property value="batchName"/></span></li>
				<!-- 优惠券类型 -->
				<li>
				<s:if test='"A".equals(couponType)'>
					<span><s:text name="EMP07_couponTypeA"></s:text></span>
				</s:if><s:elseif test='"B".equals(couponType)'>
					<span><s:text name="EMP07_couponTypeB"></s:text></span>
				</s:elseif>
				</li>
				<!-- 面值 -->
				<li><span><s:property value="parValue"/></span></li>
				<%-- 可使用次数 --%>
				<li><span><s:property value="useTimes"/></span></li>
				<!-- 生效开始日期 -->
				<li><span><s:property value="startDate"/></span></li>
				<!-- 生效结束日期 -->
				<li><span><s:property value="endDate"/></span></li>
				<%-- 批次时间 --%>
				<li><span><s:property value="batchDate"/></span></li>
				<%-- 金额条件 --%>
				<li><span><s:property value="amountCondition"/></span></li>
				<%-- 同步状态--%>
				<%-- <li>
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
				</li> --%>
				<li>
					<s:if test='null != synchFlag && "0".equals(synchFlag)'>
						<!-- 清除未同步的批次优惠券 -->
	                	<cherry:show domId="BINOLBSEMP07DEL">
	                		<s:url action="BINOLBSEMP07_deleteBatchCoupon" id="deleteBatchCouponUrl">
								<s:param name="batchCode" value="%{#batchMap.batchCode }" />
							</s:url>
		                	<a href="${deleteBatchCouponUrl}" id="deleteBtn" class="delete" onclick='BINOLBSEMP07.popDeleteDialog(this);return false;'>
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
