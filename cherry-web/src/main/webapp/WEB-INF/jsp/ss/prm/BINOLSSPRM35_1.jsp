<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM35">
	<div id="headInfo">
		<s:text name="PRM35_inQuantity"/>
		<span class="<s:if test='sumInfo.inSumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.inSumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="PRM35_inSumAmount"/>
	    <span class="<s:if test='sumInfo.inSumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.inSumAmount"></s:param></s:text></strong>
	    </span>
	    <s:text name="PRM35_outQuantity"/>
		<span class="<s:if test='sumInfo.outSumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.outSumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="PRM35_outSumAmount"/>
	    <span class="<s:if test='sumInfo.outSumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.outSumAmount"></s:param></s:text></strong>
	    </span>
	    <s:text name="PRM35_sumQuantity"/>
		<span class="<s:if test='sumInfo.inSumQuantity < sumInfo.outSumQuantity'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.inSumQuantity-sumInfo.outSumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="PRM35_sumAmount"/>
	    <span class="<s:if test='sumInfo.inSumAmount < sumInfo.outSumAmount '>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.inSumAmount-sumInfo.outSumAmount"></s:param></s:text></strong>
	    </span>
	</div>
<div id="aaData">
	<s:iterator value="proStockInOutList" id="pro">
		<s:url id="detailsUrl" value="BINOLSSPRM36_init">
			<s:param name="proStockIOId">${pro.proStockIOId}</s:param>
		</s:url>
		<ul>
			<%-- No. --%>
			<li>${pro.RowNumber}</li>
			<%-- 单据号 --%>
			<li>
				<a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
					<s:property value="tradeNo"/>
				</a>
			</li>
			<%-- 部门 --%>
			<li><span>(<s:property value="departCode"/>)<s:property value="departName"/></span></li>
			<%-- 业务类型   --%>
			<li><span><s:property value='#application.CodeTable.getVal("1026", tradeType)'/></span></li>
			<%-- 出入库状态 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1025", stockType)'/></span></li>
			<%-- 总数量 --%>
           <li><span class="<s:if test='totalQuantity < 0'>highlight</s:if>"><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></span></li>
			<%-- 总金额  --%>
           	<li><span class="<s:if test='totalAmount < 0'>highlight</s:if>"><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text></span></li>
			<%-- 日期 --%>
			<li><span><s:property value="stockInOutDate"/></span></li>
			<%-- 审核状态 --%>
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
	            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD.equals(verifiedFlag)'>
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