<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTBIL01">
	<div id="headInfo">
		<s:text name="BIL01_sumQuantity"/>
		<span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="BIL01_sumAmount"/>
	    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	<div id="aaData">
	<s:iterator value="prtInDepotList" id="productShiftID">
	<s:url id="detailsUrl" action="BINOLSTBIL02_init">
		<%--入库单ID --%>
		<s:param name="productInDepotId">${productInDepotID}</s:param>
	</s:url>	
	<ul>
		<%-- 选择 --%>
		<li><input type="checkbox" id="checkbill" value='<s:property value="productInDepotID" />' onclick="checkBill(this);"/></li>
		<li><%-- 入库单号 --%><a href="${detailsUrl}" class="left" onclick="openWin(this);return false;">
			<s:property value="billNoIF"/></a></li>
		<li><%-- 导入批次 --%>
			<s:property value="importBatch"/>
		</li>
		<li><%-- 部门 --%><s:if test='departName != null && !"".equals(departName)'>
				(<s:property value="departCode"/>)<s:property value="departName"/>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>	
		<li><%-- 仓库 --%><s:if test='depotName != null && !"".equals(depotName)'>
				(<s:property value="depotCode"/>)<s:property value="depotName"/>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>	
		<li><%-- 总数量 --%><s:if test='totalQuantity != null'><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>
		<li><%-- 总金额 --%><s:if test='totalAmount != null'><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>
		<li><%-- 入库日期 --%>
			<s:if test='inDepotDate != null && !"".equals(inDepotDate)'>
					<s:property value="inDepotDate" />
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>
		<li>
            <s:if test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_UNSUBMIT.equals(verifiedFlag)'>
                <span class="verified_unsubmit">
				    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
			</s:if>
			<s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_SUBMIT.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
				</span>
			</s:elseif>
			<s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_AGREE.equals(verifiedFlag)'>
				<span class="verified">
					<span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
				</span>
			</s:elseif>
			<s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_DISAGREE.equals(verifiedFlag)'>
				<span class="verified_rejected">
				    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
				</span>
			</s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_DISAGREE1.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_SUBMIT1.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_SUBMIT2.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_AGREE2.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_DISAGREE2.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
		</li>
		<%-- 入库状态 --%>
		<li><s:property value='#application.CodeTable.getVal("1266", tradeStatus)'/></li>
		<%-- 备注 --%>
		<li><s:property value="comments"/></li>
		<%-- 打印状态 --%>
		<li>
			<s:if test="printStatus == @com.cherry.cm.core.CherryConstants@PRINT_STATUS_1">
				<s:url id="queryPrintLog" action="../common/BINOLCM99_queryPrintLog">
					<s:param name="billId">${productInDepotID}</s:param>
					<s:param name="pageId">BINOLSTBIL02</s:param>
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