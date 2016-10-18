<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM25">
    <div id="headInfo">
	    <s:text name="PRM25_realsumQuantity"/>
	        <span class="<s:if test='sumInfo.realQuantity < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.realQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="PRM25_sumQuantity"/>
        <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="PRM25_sumAmount"/>
        <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
        </span>
    </div>
	<div id="aaData">
	<s:iterator value="takingList">
	<%--<s:if test="((!''.equals(profitKbn) && null != profitKbn) && summQuantity != 0) || (''.equals(profitKbn) || null == profitKbn)"> --%>
	<s:url id="detailsUrl" action="BINOLSSPRM26_init">
		<%-- 促销产品盘点ID --%>
		<s:param name="stockTakingId"><s:property value="stockTakingId" /></s:param>
		<%-- 盈亏 --%>
		<s:param name="profitKbn"><s:property value="profitKbn"/></s:param>
	</s:url>
	<ul>
		<%-- 选择 --%>
			<li><input type="checkbox" id="checkbill" value='<s:property value="stockTakingId" />' onclick="checkBill(this);"/></li>
		<li>
			<%-- 盘点单号 --%>
			<a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;"><s:property value="stockTakingNo"/></a>
		</li>
		<li>
			<%-- 部门名称 --%>
			<s:if test='departName != null && !"".equals(departName)'>
				<s:property value="departName"/>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>
		<li>
			<%-- 仓库名称 --%>
			<s:if test='inventoryName != null && !"".equals(inventoryName)'>
				<s:property value="inventoryName"/>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>
		<li>
			<%-- 实盘数量 --%>
			<s:if test="sumrealQuantity !=null">
			<s:if test="sumrealQuantity >= 0"><s:text name="format.number"><s:param value="sumrealQuantity"></s:param></s:text></s:if>
			<s:else><span class="highlight"><s:text name="format.number"><s:param value="sumrealQuantity"></s:param></s:text></span></s:else>
			</s:if>
			<s:else>
            	&nbsp;
            </s:else>
		</li>
		<li>
			<%-- 盘差 --%>
			<s:if test="summQuantity !=null">
			<s:if test="summQuantity >= 0"><s:text name="format.number"><s:param value="summQuantity"></s:param></s:text></s:if>
			<s:else><span class="highlight"><s:text name="format.number"><s:param value="summQuantity"></s:param></s:text></span></s:else>
			</s:if>
			<s:else>
            	&nbsp;
            </s:else>
		</li>
		<li>
			<%-- 盘差金额 --%>
			<s:if test="summAmount !=null">
			<s:if test="summAmount >= 0"><s:text name="format.price"><s:param value="summAmount"></s:param></s:text></s:if>
			<s:else><span class="highlight"><s:text name="format.price"><s:param value="summAmount"></s:param></s:text></span></s:else>
			</s:if>
			<s:else>
            	&nbsp;
            </s:else>
		</li>
		<li>
			<%-- 盘点类型 --%>
            <span><s:property value='#application.CodeTable.getVal("1019", takingType)'/></span>
		</li>
		<li>
			<%-- 盘点时间 --%>
			<s:if test='tradeDateTime != null && !"".equals(tradeDateTime)'>
				<s:property value="tradeDateTime"/>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>
		<%--<li>
			 审核状态
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
			<s:else><span></span></s:else>
		</li> --%>
		<li>
			<%-- 盘点员 --%>
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
					<s:param name="billId">${stockTakingId}</s:param>
					<s:param name="pageId">BINOLSSPRM26</s:param>
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
		<%-- </s:if>--%>
	</s:iterator>
	</div>
</s:i18n>