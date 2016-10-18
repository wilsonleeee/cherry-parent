<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTSFH02">
	<div id="headInfo">
		<s:text name="SFH02_sumQuantity"/>
		<span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="SFH02_sumAmount"/>
	    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	<div id="aaData"><s:iterator value="productOrderList" id="productOrderList">
		<s:url id="detailsUrl" action="BINOLWSMNG01_detailInit">
			<%-- 订单单号   --%>
			<s:param name="productOrderID">${binProductOrderID}</s:param>
		</s:url>
		<ul>
			<%-- 选择 --%>
			<li><input type="checkbox" id="checkbill" value='<s:property value="binProductOrderID" />' onclick="checkBill(this);"/></li>
			<li><%-- 订单单号  --%>
			<a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;">
			<s:property	value="orderNoIF" /></a></li>
			<li><%-- 订货日期  --%><s:property value="tradeDateTime" /></li>
			<li><%-- 总数量  --%><s:property value="totalQuantity" /></li>
			<li><%-- 总金额  --%><s:property value="totalAmount" /></li>
            <li><%-- 处理状态  --%>
                <s:if test='tradeStatus != null && !"".equals(tradeStatus)'>
                    <span class=" <s:if test="tradeStatus == 10">verified_unsubmit</s:if><s:elseif test="tradeStatus == 12">verifying</s:elseif><s:elseif test="tradeStatus == 13">verified</s:elseif>">
                        <span><s:property value='#application.CodeTable.getVal("1142",#productOrderList.tradeStatus)' /></span>
                    </span>
                </s:if>
                <s:else>&nbsp;</s:else>
            </li>
			<li><%-- 审核区分  --%><s:if test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_UNSUBMIT">
					<span class="verified_unsubmit">
						<span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
					</span>
				</s:if>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_SUBMIT1">
					<span class="verifying">
						<span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
					</span>
				</s:elseif>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_SUBMIT2">
					<span class="verifying">
						<span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
					</span>
				</s:elseif>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_AGREE">
					<span class="verified">
						<span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
					</span>
				</s:elseif>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_AGREE1">
                    <span class="verified">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
				</s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_DISAGREE2">
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_DISAGREE1">
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_SUBMIT_10">
                    <span class="verifying">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_AGREE_11">
                    <span class="verified">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_DISAGREE_12">
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_SUBMIT3">
                    <span class="verifying">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_AGREE3">
                    <span class="verified">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_DISAGREE3">
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_SUBMIT4">
                    <span class="verifying">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_AGREE4">
                    <span class="verified">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@ODAUDIT_FLAG_DISAGREE4">
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD">
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
				<s:else>
				    <span><s:property value='#application.CodeTable.getVal("1146", verifiedFlag)'/></span>
                </s:else>
                </li>
            <li><%-- 制单员工  --%><s:property value="binEmployeeID" /></li>            
		</ul>
	</s:iterator></div>
</s:i18n>
