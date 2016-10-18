<%-- 产品发货单一览 DataTable--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTSFH04">
    <div id="headInfo">
        <s:text name="SFH04_sumQuantity"/>
        <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="SFH04_sumAmount"/>
        <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
        </span>
    </div>
	<div id="aaData">
    <s:iterator value="productDeliverList" id="productDeliver">
		<s:url id="detailsUrl" action="BINOLSTSFH05_init">
			<%-- 发货单单号   --%>
			<s:param name="productDeliverId">${productDeliverId}</s:param>
		</s:url>
		<ul>
			<%-- 选择 --%>
			<li><input type="checkbox" id="checkbill" value='<s:property value="productDeliverId" />' onclick="checkBill(this);"/></li>
			<li><%-- 订单单号  --%>
			<a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;">
			<s:property	value="deliverNoIF" /></a></li>
			<li><%-- 序号  --%><s:property value="deliverNo" /></li>
			<li><%-- 关联单号  --%><s:property value="relevanceNo" /></li>
			<li><%-- 导入批次  --%><s:property value="importBatch" /></li>
			<li><%-- 发货部门  --%><s:property value="outDepartCodeName"/></li>
			<li><%-- 发货仓库  --%><s:property value="depotName"/></li>
			<li><%-- 收货部门  --%><s:property value="inDepartCodeName"/></li>
			<li><%-- 逻辑仓库  --%><s:property value="logInvName"/></li>
			
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
			<li><%-- 发货日期  --%>
			<s:if test='date != null && !"".equals(date)'>
                <s:if test='dateRange > 5'>
                    <span style="color:red">
                        <s:property value="date"/>
                    </span>
                </s:if>
                <s:else>
                    <s:property value="date"/>
                </s:else>
			</s:if>
			<s:else>
                &nbsp;
			</s:else>
			<li><%-- 审核区分  --%><s:if test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT">
					<span class="verified_unsubmit">
						<span><s:property value='#application.CodeTable.getVal("1180", verifiedFlag)'/></span>
					</span>
				</s:if>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT">
					<span class="verifying">
						<span><s:property value='#application.CodeTable.getVal("1180", verifiedFlag)'/></span>
					</span>
				</s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@SDAUDIT_FLAG_SUBMIT2">
                    <span class="verifying">
                        <span><s:property value='#application.CodeTable.getVal("1180", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE">
					<span class="verified">
						<span><s:property value='#application.CodeTable.getVal("1180", verifiedFlag)'/></span>
					</span>
				</s:elseif>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE">
					<span class="verified_rejected">
						<span><s:property value='#application.CodeTable.getVal("1180", verifiedFlag)'/></span>
					</span>
				</s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@SDAUDIT_FLAG_AGREE2">
                    <span class="verified">
                        <span><s:property value='#application.CodeTable.getVal("1180", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@SDAUDIT_FLAG_DISAGREE2">
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1180", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD">
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1180", verifiedFlag)'/></span>
                    </span>
                </s:elseif>
				<s:else><span></span></s:else></li>
			<li>
                <%-- 处理状态  --%>
                <s:if test='null != tradeStatus && !"".equals(tradeStatus)'>
				    <s:property value='#application.CodeTable.getVal("1141",tradeStatus)' />
			    </s:if>
                <s:else>&nbsp;</s:else>
            </li>
            <li><%-- 制单员工  --%><s:property value="employeeName" /></li>            
            <li><%-- 审核者  --%><s:property value="employeeNameAudit" /></li>
            <li><%-- 发货理由  --%><s:property value="comments" /></li>
             <%-- 打印状态 --%>
			<%-- <li>
				<s:if test="printStatus == @com.cherry.cm.core.CherryConstants@PRINT_STATUS_1">
					<s:url id="queryPrintLog" action="../common/BINOLCM99_queryPrintLog">
					<s:param name="billId">${productDeliverId}</s:param>
					<s:param name="pageId">BINOLSTSFH05</s:param>
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
			</li> --%>
		</ul>
	</s:iterator>
    </div>
</s:i18n>
