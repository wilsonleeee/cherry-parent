<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBSVC02">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="cardList" id="card" status="status">
<s:url id="viewUrl" action="BINOLMBSVC02_viewSaleInit" namespace="/mb">
			<s:param name="cardId">${card.bIN_CardId}</s:param>
			<s:param name="cardCode">${card.cardCode}</s:param>
</s:url>
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="cardCode"/></li>
<li><s:property value="mobilePhone"/></li>
<li><s:property value="amount" /></li>
<li><s:property value="depositAmount" /></li>
<li><s:property value="totalAmount" /></li>
<li><s:property value="lastDepositAmount" /></li>
<li><s:property value="lastDepositTime" /></li>
<li><s:property value="state" /></li>
<li><s:property value="departName" /></li>
<li>
	<a href="${viewUrl}" class="edit" onclick="javascript:openWin(this);return false;">
					<span class="ui-icon ui-icon-document"></span>
					<span class="button-text">
						<s:text name="SVC02_saleView" />
					</span>
	</a>
	<!-- 
    <a class="delete" onclick="BINOLMBSVC02.stopCardDialog(${card.bIN_CardId});return false;">
		<span class="ui-icon icon-delete"></span>
		<span class="button-text">
		<s:text name="SVC02_stop" />
    	</span>
   	</a>
   	 -->
	<input type="hidden" id="mobilePhoneC" value='<s:property value="mobilePhone"/>'/>
	<input type="hidden" id="cardCodeC" value='<s:property value="cardCode"/>'/>
	<input type="hidden" id="departCodeC" value='<s:property value="#card.departCode"/>'/>
	<cherry:show domId="BINOLMBSVC0203">
	<a class="search" href="#" onclick="BINOLMBSVC02.resetPsdDialog(this);return false;">
		<span class="ui-icon icon-export"></span>
		<span class="button-text">
			<s:text name="SVC02_reset" />
    	</span>
	</a>
	</cherry:show>
	<cherry:show domId="BINOLMBSVC0204">
	<a class="delete" href="javascript:void(0);" onclick="BINOLMBSVC02.abandonCard(<s:property value='#card.cardId'/>);return false;">
		<span class="ui-icon icon-delete"></span>
		<span class="button-text">废弃</span>
	</a>
	</cherry:show>
	<cherry:show domId="BINOLMBSVC0205">
	<a class="delete" href="javascript:void(0);" onclick="BINOLMBSVC02.modifyPasswordInit(this);return false;">
		<span class="ui-icon icon-edit"></span>
		<span class="button-text">修改密码</span>
	</a>
	</cherry:show>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>