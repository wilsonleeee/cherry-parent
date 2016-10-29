<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS44.js"></script>
<s:i18n name="i18n.pt.BINOLPTJCS44">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="dropList" id="drop" status="status">
<ul>
<%-- 选择 --%>
<li><input type="checkbox" id="checkbill" value='<s:property value="productId" />' onclick="checkBill(this);"/></li>
<li><s:property value="RowNumber"/></li>
<li><s:property value="nameTotal"/></li>
<li><s:property value="barCode"/></li>
<li><s:property value="spec"/></li>
<li><s:property value="memPrice" /></li>
<li><s:property value="salePrice"/></li>
</ul>
</s:iterator>
</div>
</s:i18n>