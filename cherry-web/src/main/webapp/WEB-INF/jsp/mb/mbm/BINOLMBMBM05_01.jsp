<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="saleRecordList" id="saleRecordMap">
<ul>
<li><span>
<s:url action="BINOLMBMBM05_searchSaleDetail" id="searchSaleDetailUrl">
<s:param name="saleRecordId" value="%{#saleRecordMap.saleRecordId}"></s:param>
</s:url>
<input value="${searchSaleDetailUrl }" type="hidden"/>
<s:property value="memberCode"/>
</span></li>
<li><span><s:property value="billCode"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1055", #saleRecordMap.saleType)' /></span></li>
<cherry:show domId="BINOLMBMBM10_29">
<li><span><s:property value="departCode"/></span></li>
</cherry:show>
<li><span><s:property value="saleTime"/></span></li>
<li><span>
<s:if test='%{#saleRecordMap.quantity != null && !"".equals(#saleRecordMap.quantity)}'>
	<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
</s:if>
</span></li>
<li><span>
<s:if test='%{#saleRecordMap.amount != null && !"".equals(#saleRecordMap.amount)}'>
	<s:text name="format.price"><s:param value="amount"></s:param></s:text>
</s:if>
</span></li>
<li><span><s:property value='#application.CodeTable.getVal("1310", #saleRecordMap.billState)' /></span></li>
</ul>
</s:iterator>
</div>
<s:if test="%{saleCountInfo != null}">
	<s:i18n name="i18n.mb.BINOLMBMBM05">
	<div id="saleCountInfo">
		<table class="detail" cellpadding="0" cellspacing="0">
          <tr>
		  	<th><s:text name="binolmbmbm05_sumQuantity" /></th>
		  	<td><span>
		  	<s:if test="%{saleCountInfo.quantity != null}">
				<s:text name="format.number"><s:param value="saleCountInfo.quantity"></s:param></s:text>
			</s:if>
			<s:else>0</s:else>
		  	</span></td>
		  	<th><s:text name="binolmbmbm05_sumAmount" /></th>
		  	<td><span>
		  	<s:if test="%{saleCountInfo.amount != null}">
				<s:text name="format.price"><s:param value="saleCountInfo.amount"></s:param></s:text>
			</s:if>
			<s:else>0</s:else>
		  	</span></td>
	      </tr>
	  	</table>
	</div>
	</s:i18n>
</s:if> 