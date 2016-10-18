<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM18">
    <div id="headInfo">
        <s:text name="PRM18_sumQuantity"/>
        <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="PRM18_sumAmount"/>
        <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
        </span>
    </div>
	<div id="aaData">
	<s:iterator value="deliverList" id="deliver">
	<s:url id="detailsUrl" action="BINOLSSPRM52_init">
		<%-- 促销产品收发货ID --%>
		<s:param name="deliverId">${deliver.deliverId}</s:param>
	</s:url>	
	<ul>
		<%-- No. --%>
		<li><s:property value="RowNumber"/>
		</li>
		<li>
			<%-- 发货单号 --%>
			<a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;">
			<s:property value="deliverRecNo"/>
			</a>
		</li>
		<li>
			<%-- 发货部门 --%>
			<s:if test='departName != null && !"".equals(departName)'>
				<s:property value="departName"/>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>		
		<li>
			<%-- 收货部门 --%>
			<s:if test='departNameReceive != null && !"".equals(departNameReceive)'>
				<s:property value="departNameReceive"/>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>
			
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
		<li>
			<%-- 发货日期 --%>
			<s:if test='deliverDate != null && !"".equals(deliverDate)'>
				<s:if test='DateRange > 5'>
					<span STYLE="color:red">
						<s:property value="deliverDate"/>
					</span>
				</s:if>
				<s:else>
            		<s:property value="deliverDate"/>
           		 </s:else>
			</s:if>
            <s:else>
            	&nbsp;
            </s:else>
		</li>
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
	</ul>
	</s:iterator>

	</div>
</s:i18n>