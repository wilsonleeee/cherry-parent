<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.st.BINOLSTYLDZ01">
<div id="aaData">
    <s:iterator value="saleList" status="status">
     <ul>
     	<li><s:property value="#status.index+iDisplayStart+1" /></li>
       	<li><s:property value="tradeDate"/></li>
       	<li><s:property value="tradeTime"/></li>
       	<li><s:property value="cardCode"/></li>
       	<li><s:property value="companyCode"/></li>
       	<li><s:property value="companyName"/></li>
       	<li><s:property value="posCode"/></li>
       	<li><s:property value="posBillCode"/></li>
       	<li><s:property value="sysBillCode"/></li>
       	<li><s:property value="hedgingBillCode"/></li>
       	<li><s:property value="referenceCode"/></li>
       	<li><s:property value="tradeType"/></li>
       	<li><s:property value="amount"/></li>
       	<li><s:property value="tradeResult"/></li>
       	<li><s:property value="tradeAnswer"/></li>
     	<li><s:property value="batchID"/></li>
       	<li>
			<a href="/Cherry/st/BINOLSTYLDZ01_editInit.action?billId=${billId}" class="delete" onclick="javascript:openWin(this);return false;">
                <span class="ui-icon icon-edit"></span>
                <span class="button-text"><s:text name="styldz01.edit"/></span>
            </a>
            <a href="javascript:void(0)" onclick="BINOLSTYLDZ01.delete('${billId}');return false;" class="delete">
                <span class="ui-icon icon-delete"></span>
                <span class="button-text"><s:text name="styldz01.delete"/></span>
            </a>
		</li>
     </ul>
    </s:iterator>
</div>
</s:i18n>