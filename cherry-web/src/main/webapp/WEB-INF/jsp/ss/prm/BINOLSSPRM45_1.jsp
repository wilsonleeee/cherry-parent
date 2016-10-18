<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM45">
    <div id="headInfo">
  		<s:text name="PRM45_sumrealQuantity"/>
        <span class="<s:if test='sumInfo.sumrealQuantity  < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumrealQuantity "></s:param></s:text></strong>
        </span>
        <s:text name="PRM45_sumQuantity"/>
        <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="PRM45_sumAmount"/>
        <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
        </span>
    </div>
    <div id="aaData">
    <s:iterator value="takingList" id="taking">
    <s:url id="detailsUrl"  value="/ss/BINOLSSPRM26_init">
        <%-- 产品盘点ID --%>
        <s:param name="stockTakingId">${taking.stockTakingId}</s:param>
        <%-- 盈亏 --%>
        <s:param name="profitKbn">${profitKbn}</s:param>
    </s:url>
    <ul>
		<li>
		<%-- 区域 --%>
            <s:if test='region != null && !"".equals(region)'>
                <s:property value="region"/>
            </s:if>
            <s:else>
                -
            </s:else>
		</li>
		<li>
		<%-- 城市 --%>
            <s:if test='city != null && !"".equals(city)'>
                <s:property value="city"/>
            </s:if>
            <s:else>
                -
            </s:else>
		</li>
		<li>   <%-- 部門類型 --%>                 
            <s:if test='Type != null && !"".equals(Type)'>
                 <span><s:property value='#application.CodeTable.getVal("1000", Type)'/></span>
            </s:if>
            <s:else>
                -
            </s:else>          
        </li> 
        <li>
            <%-- 部门名称 --%>
            <s:if test='departName != null && !"".equals(departName)'>
                <s:property value="departName"/>
            </s:if>
            <s:else>
                -
            </s:else>
        </li>
        <li>
            <%-- 盘点单号 --%>
            <s:if test='stockTakingNo != null && !"".equals(stockTakingNo)'>
               <a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;"><s:property value="stockTakingNo"/></a>
            </s:if>
            <s:else>
                -
            </s:else>
        </li>
        <li>
            <%-- 盘点员 --%>
            <s:if test='employeeName != null && !"".equals(employeeName)'>
                <s:property value="employeeName"/>
            </s:if>
            <s:else>
                -
            </s:else>
        </li>
        <li>
            <%-- 盘点日期 --%>
            <s:if test='stockTakingDate != null && !"".equals(stockTakingDate)'>
                <s:property value="stockTakingDate"/>
            </s:if>
            <s:else>
                -
            </s:else>
        </li>
        <li>   <%-- 盘点类型 --%>                 
            <s:if test='takingType != null && !"".equals(takingType)'>
                 <span><s:property value='#application.CodeTable.getVal("1019", takingType)'/></span>
            </s:if>
            <s:else>
                -
            </s:else>          
        </li>
        <li>
            <%-- 盘点原因 --%>
            <s:if test='Comments != null && !"".equals(Comments)'>
                <s:property value="Comments"/>
            </s:if>
            <s:else>
                -
            </s:else>
        </li>  
        <li>
            <%-- 账面库存 --%>
            <s:if test="Quantity !=null">
            <s:if test="Quantity >= 0"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                -
            </s:else>
        </li>
        <li>
            <%-- 实盘数量 --%>
            <s:if test="realQuantity !=null">
            <s:if test="realQuantity >= 0"><s:text name="format.number"><s:param value="realQuantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="realQuantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                -
            </s:else>
        </li>
        <li>
            <%-- 盘差 --%>
            <s:if test="summQuantity !=null">
            <s:if test="summQuantity >= 0"><s:text name="format.number"><s:param value="summQuantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="summQuantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                -
            </s:else>
        </li>
        <li>
            <%-- 盘盈 --%>
            <s:if test="OverQuantity !=null">
            <s:if test="OverQuantity >= 0"><s:text name="format.number"><s:param value="OverQuantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="OverQuantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                -
            </s:else>
        </li> 
        <li>
            <%-- 盘亏 --%>
            <s:if test="ShortQuantity !=null">
            <s:if test="ShortQuantity >= 0"><s:text name="format.number"><s:param value="ShortQuantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="ShortQuantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                -
            </s:else>
        </li>       
    </ul>
    </s:iterator>
    </div>
</s:i18n>