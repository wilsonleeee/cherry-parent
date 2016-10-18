<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM55">
<div id="aaData">
    <div id="headInfo">
        <s:text name="PRM55_sumQuantity"/>
        <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="PRM55_sumAmount"/>
        <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
        </span>
    </div>
	<s:iterator value="allocationList" id="pro">
		<s:url id="detailsUrl" value="BINOLSSPRM56_init">
			<s:param name="proAllocationId">${pro.proAllocationId}</s:param>
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
			<%-- 调拨申请部门   --%>
			<li><span><s:property value="sendDepart"/></span></li>
			<%-- 调拨接受部门 --%>
			<li><span><s:property value="receiveDepart"/></span></li>
			<%-- 总数量 --%>
			<li><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></li>
			<%-- 总金额  --%>
			<li><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text></li>
			<%-- 调拨日期 --%>
			<li><span><s:property value="allocationDate"/></span></li>
			<%-- 审核区分 --%>
			<li>
				<s:if test="verifiedFlag ==@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT">
					<span class="verified_unsubmit"><span>
					<s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT)'/>
					</span></span>
				</s:if>
				<s:elseif test="verifiedFlag ==@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT">
					<span class="verifying"><span>
					<s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT)'/>
					</span></span>
				</s:elseif>
				<s:elseif test="verifiedFlag ==@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE">
					<span class="verified"><span>
					<s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE)'/>
					</span></span>
				</s:elseif>
				<s:elseif test="verifiedFlag ==@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE">
					<span class="verified_rejected"><span>
					<s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE)'/>
					</span></span>
				</s:elseif>
	            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD.equals(verifiedFlag)'>
	                <span class="verified_rejected">
	                    <span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
	                </span>
	            </s:elseif>
				<s:else><span></span></s:else>
			</li>
			<li>
			<%-- 处理状态 --%>
			<span><s:property value='#application.CodeTable.getVal("1186", tradeStatus)'/></span>
		 </li>	
		</ul>
	</s:iterator>
</div>
</s:i18n>