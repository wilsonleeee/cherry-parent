<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTBIL11">
	<div id="headInfo">
		<s:text name="BIL05_sumQuantity"/>
		<span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="BIL05_sumAmount"/>
	    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
    <div id="aaData">
    <s:iterator value="ReturnList" id="Return">
        <s:url id="detailsUrl" action="BINOLSTBIL12_init">
            <%-- 退库单ID --%>
            <s:param name="productReturnID">${productReturnID}</s:param>
        </s:url>
    <ul>
       <%-- 选择 --%>
		<li><input type="checkbox" id="checkbill" value='<s:property value="productReturnID" />' onclick="checkBill(this);"/></li>
        <li>
            <%-- 退库单号--%>
            <a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;"><s:property value="returnNo"/></a>
        </li>
        <li>
            <%-- 部门 --%>
            <s:if test='departName != null && !"".equals(departName)'>
                <s:property value="departName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 退库仓库 --%>
            <s:if test='inventoryName != null && !"".equals(inventoryName)'>
                <s:property value="inventoryName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
          <li>
            <%-- 逻辑仓库 --%>
            <s:if test='logicInventoryName != null && !"".equals(logicInventoryName)'>
                <s:property value="logicInventoryName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 总数量 --%>
            <s:if test="totalQuantity !=null">
            <s:if test="totalQuantity >= 0"><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 总金额 --%>
            <s:if test="totalAmount !=null">
            <s:if test="totalAmount >= 0"><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 退库时间 --%>
            <s:if test='date != null && !"".equals(date)'>
                <s:property value="date"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 审核状态 --%>
            <s:if test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT.equals(verifiedFlag)'>
            <span class="verified_unsubmit">
                <span><s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT)'/></span>
            </span>
            </s:if>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE.equals(verifiedFlag)'>
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
            <%-- 操作员 --%>
            <s:if test='employeeName != null && !"".equals(employeeName)'>
                <s:property value="employeeName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <%-- 打印状态 --%>
		<li>
			<s:if test="printStatus == @com.cherry.cm.core.CherryConstants@PRINT_STATUS_1">
				<s:url id="queryPrintLog" action="../common/BINOLCM99_queryPrintLog">
					<s:param name="billId">${productReturnID}</s:param>
					<s:param name="pageId">BINOLSTBIL12</s:param>
				</s:url>
				<a class="printed" href="${queryPrintLog}" rel="${queryPrintLog}" title='<s:text name="global.page.printLog"/>'>
					<span><s:property value='#application.CodeTable.getVal("1169", printStatus)'/></span>
				</a>
			</s:if>
			<s:else>
				<span class="unprinted">
					<span><s:property value='#application.CodeTable.getVal("1169", printStatus)'/></span>
				</span>
			</s:else>
		</li>
    </ul>  
    </s:iterator>
    </div>
</s:i18n>