<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM06.js"></script>
<@s.i18n name="i18n.ct.BINOLCTPLN01">
<@s.url id="showCustomer_Url" action="BINOLCTCOM06_search"/>
<div class="hide">
	<a id="showCustomerUrl" href="${showCustomer_Url}"></a>
</div>
<div class="panel-content clearfix">
	<div id="customerDetail" class="section">
		<form id="customerForm" method="post" class="inline" >
		<input id="userId" name="userId" value="${(userId)!?html}" class="hide" type="text" />
		<input id="recordCode" name="recordCode" value="${(recordCode)!?html}" class="hide" type="text" />
		<input id="recordName" name="recordName" value="${(recordName)!?html}" class="hide" type="text" />
		<input id="recordType" name="recordType" value="${(recordType)!?html}" class="hide" type="text" />
		<input id="customerType" name="customerType" value="${(customerType)!?html}" class="hide" type="text" />
		<input id="conditionInfo" name="conditionInfo" value="${(conditionInfo)!?html}" class="hide" type="text" />
		<div class="section-header" style="height:30px">
			<span class="left">
				<@s.text name="ctpln.memCode" />：
				<input id="memCode" name="memCode" type="text" style="width:90px"/>&nbsp;
				<@s.text name="ctpln.memName" />：
				<input id="memName" name="memName" type="text" style="width:60px"/>&nbsp;
				<@s.text name="ctpln.mobilePhone" />：
				<input id="mobilePhone" name="mobilePhone" type="text" style="width:90px"/>&nbsp;
				<button class="search" onclick="BINOLCTCOM06.search();return false;">
		          	<span class="ui-icon icon-search-big"></span>
		          	<span class="button-text"><@s.text name="ctpln.search" /></span>
				</button>
			</span>
			<span class="right">
				<a href="#" class="setting">
					<span class="button-text"><@s.text name="global.page.colSetting"/></span>
		 			<span class="ui-icon icon-setting"></span>
				</a>
			</span>
		</div>
		</form>
		<div class="section-content">
			<table id="customerDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th><@s.text name="ctpln.memCode" /></th>
					<th><@s.text name="ctpln.memName" /></th>
					<th><@s.text name="ctpln.gender" /></th>
					<th><@s.text name="ctpln.mobilePhone" /></th>
					<th><@s.text name="ctpln.email" /></th>
					<th><@s.text name="ctpln.telephone" /></th>
					<th><@s.text name="ctpln.totalPoint" /></th>
					<th><@s.text name="ctpln.changablePoint" /></th>
					<th><@s.text name="ctpln.counterCode" /></th>
					<th><@s.text name="ctpln.counterName" /></th>
					<th><@s.text name="ctpln.receiveMsgFlg" /></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			</table>
		</div>
	</div>
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>