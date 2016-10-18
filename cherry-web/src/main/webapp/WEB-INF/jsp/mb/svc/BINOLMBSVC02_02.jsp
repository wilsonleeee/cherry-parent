<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBSVC02">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="saleList" id="sale" status="status">
<s:url id="viewUrl" action="BINOLMBSVC02_viewSaleDetailInit" namespace="/mb">
			<s:param name="billCode">${sale.billCode}</s:param>
</s:url>
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="cardCode"/></li>
<li><s:property value="cardType"/></li>
<li><s:property value="departName"/></li>
<li><s:property value="transactionTime" /></li>
<li><s:property value="billCode" /></li>
<li><s:property value="relevantCode" /></li>
<li><s:property value="transactionType" /></li>
<li><s:property value="amount" /></li>
<li><s:property value="giftAmount" /></li>
<li><s:property value="totalAmount" /></li>
<li><s:property value="discount" /></li>
<li><s:property value="validFlag" /></li>
<li>
	<a class="edit" onclick="BINOLMBSVC02_init.initSaleDetail('${sale.billCode}','${sale.cardCode}');return false;">
					<span class="ui-icon ui-icon-document"></span>
					<span class="button-text">
						<s:text name="SVC02_saleViewDetail" />
					</span>
	</a>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>