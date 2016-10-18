<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS05">
<s:if test='sumInfo != null'>
	<div id="headInfo">
		<s:text name="RPS05_sumQuantity"/>
		<span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="RPS05_sumAmount"/>
	    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
</s:if> 
<div id="aaData">
	<s:iterator value="allocationList" id="pro">
		<s:url id="detailsUrl" value="/pt/BINOLPTRPS18_init">
			<s:param name="proAllocationId">${pro.proAllocationId}</s:param>
			<s:param name="allocationType">${allocationType}</s:param>
		</s:url>
		<ul>
			<%-- No. --%>
			<li>${pro.RowNumber}</li>
			<%-- 调拨单号 --%>
			<li>
				<a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
					<s:property value="allocationNo"/>
				</a>
			</li>
			<s:if test='%{allocationType == "1"}'>
				<%-- 调拨申请部门   --%>
				<li><span>(<s:property value="bin_OrganizationIDOut"/>)<s:property value="sendDepart"/></span></li>
				<%-- 调拨接受部门 --%>
				<li><span>(<s:property value="bin_OrganizationIDIn"/>)<s:property value="receiveDepart"/></span></li>
			</s:if>
			<s:elseif test='%{allocationType == "2"}'>
				<%-- 调拨接受部门 --%>
				<li><span>(<s:property value="bin_OrganizationIDIn"/>)<s:property value="receiveDepart"/></span></li>
				<%-- 调拨申请部门   --%>
				<li><span>(<s:property value="bin_OrganizationIDOut"/>)<s:property value="sendDepart"/></span></li>
			</s:elseif>
			<%-- 总数量 --%>
			<li><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></li>
			<%-- 总金额  --%>
			<li><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text></li>
			<%-- 调拨日期 --%>
			<li><span><s:property value="allocationDate"/></span></li>
			<%-- 审核区分 --%>
			<li> 
				<s:if test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT">
					<span class="verified_unsubmit">
						<span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
					</span>
				</s:if>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT">
					<span class="verifying">
						<span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
					</span>
				</s:elseif>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE">
					<span class="verified">
						<span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
					</span>
				</s:elseif>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE">
					<span class="verified_rejected">
						<span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
					</span>
				</s:elseif>
                <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE.equals(verifiedFlag)'>
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
				<s:else><span></span></s:else>
			</li>
			<%-- 操作员 --%>
			<li><span><s:property value="employeeName"/></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>