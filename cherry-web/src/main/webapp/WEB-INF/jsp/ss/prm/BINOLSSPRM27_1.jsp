<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM27">
	<div id="headInfo">
		<s:text name="PRM27_sumQuantity"/>
		<span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="PRM27_sumAmount"/>
	    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>

	<div id="aaData">
	<s:iterator value="deliverList" id="deliver">
	<s:url id="detailsUrl" action="BINOLSSPRM28_init">
		<%-- 促销产品收发货ID --%>
		<s:param name="deliverId">${deliver.deliverId}</s:param>
	</s:url>
	<s:url id="editUrl" action="BINOLSSPRM34_init">
		<%-- 促销产品收发货ID --%>
		<s:param name="deliverId">${deliver.deliverId}</s:param>
	</s:url>
	<ul>
		<li><input type="checkbox" id="checkbill" value='<s:property value="deliverId" />' onclick="checkBill(this);"/></li><%-- 选择 --%>
		<li><a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;"><s:property value="deliverRecNo"/></a></li><%-- 单据号 --%>
		<li><span><s:property value="departName"/></span></li><%-- 发货部门 --%>
		<li><span><s:property value="inventoryName"/></span></li><%-- 发货仓库 --%>
		<li><span><s:property value="departNameReceive"/></span></li><%-- 收货部门 --%>
		<li>
			<%-- 总数量 --%>
			<s:if test='totalQuantity != null'>
				<s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>
		<li>
			<%-- 总金额 --%>
			<s:if test='totalAmount != null'>
				<s:text name="format.price"><s:param value="totalAmount"></s:param></s:text>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
			
		</li>
		<li><span><s:property value="deliverDate"/></span></li><%-- 发货日期 --%>
		<li>
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
			<%-- 入库状态 --%>
			<span><s:property value='#application.CodeTable.getVal("1017", stockInFlag)'/></span>
		</li>
		<li>
			<%-- 制单员 --%>
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
					<s:param name="billId">${deliverId}</s:param>
					<s:param name="pageId">BINOLSSPRM28</s:param>
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