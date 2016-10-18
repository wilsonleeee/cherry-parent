<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.wp.BINOLWPSAL13">
<div id="aaData">
    <s:iterator value="transactionDetailList">
     <ul>
     	<li><s:property value="transactionTime"/></li>
       	<li><s:property value='#application.CodeTable.getVal("1340", transactionType)'/></li>
       	<li><s:property value="amount"/></li>
       	<li><s:property value="billCode"/></li>
       	<li>
       		<s:if test="transactionType.equals('DE') ">
				<button class="wp_search_s" type="button" style="float:left; margin:0 5px 0 0" onclick="BINOLWPSAL13.revokeInit(this);return false;">
					<span class="icon_search"></span>
					<span class="wp_search_text"><s:text name="wpsal13.revoke" /></span>
				</button>
       		</s:if>
       		
			<s:if test="serviceDetail!=null">
				<button id="showService" class="wp_search_s" type="button" style="float:left; margin:0 5px 0 0" onclick="BINOLWPSAL13.showService(this);return false;">
					<span class="icon_search"></span>
					<span class="wp_search_text"><s:text name="wpsal13.searchService" /></span>
				</button>
				<button id="hideService" class="wp_search_s" type="button" style="display:none; float:left; margin:0 5px 0 0" onclick="BINOLWPSAL13.hideService(this);return false;">
					<span class="icon_search"></span>
					<span class="wp_search_text"><s:text name="wpsal13.hideService" /></span>
				</button>
			
				<div id="serviceDetailDiv" class="hide">
			     	<table class="wp_table" id="serviceDetailTable" border="0" cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<th><s:text name="wpsal13.serviceType"></s:text></th>
							<th><s:text name="wpsal13.remainingNumber"></s:text></th>
						</tr>
						<s:iterator value="serviceDetail">
							<tr>
								<td><s:property value='serviceType'/></td>
								<td><s:property value='serviceQuantity'/></td>
							</tr>
						</s:iterator>
					</table>
		     	</div>
	     	</s:if>
			<input class="hide" name="billCode" value="<s:property value='billCode'/>"/>
			<input class="hide" name="transactionType" value="<s:property value='transactionType'/>"/>
			<input class="hide" name="serviceDetail" value="<s:property value='serviceDetail'/>"/>
		</li>
     </ul>
    </s:iterator>
</div>
</s:i18n>