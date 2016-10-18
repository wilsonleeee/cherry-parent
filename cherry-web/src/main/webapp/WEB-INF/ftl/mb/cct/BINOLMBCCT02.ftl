<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<@s.i18n name="i18n.mb.BINOLMBCCT02">
<div class="clearfix">
	<form id="getIssueForm" method="post" class="inline">
		<input type="hidden" id="ccBrandInfoId" name="ccBrandInfoId" value="${(ccBrandInfoId)!}"/>
		<input type="hidden" id="customerSysId" name="customerSysId" value="${(customerSysId)!?html}"/>
		<input type="hidden" id="customerType" name="customerType" value="${(customerType)!}"/>
	</form>
	<div id="issueDetail" class="section">
		<div class="clearfix" style="padding: 0 1em;">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="mbctt.callLogTitle" />
	        </strong>
	        <#if (customerType?? && customerType == "0") >
			<button id="btnEditCustomer" class="right edit"  onclick="BINOLMBCCT02.editCustomer();return false;">
	          	<span class="ui-icon icon-edit"></span>
	          	<span class="button-text"><@s.text name="mbctt.editCustomer" /></span>
			</button>
			</#if>
		</div>
		<div class="section-content">
			<table id="issueListTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th><@s.text name="mbctt.rowNumber" /></th>
					<th><@s.text name="mbctt.issueNo" /></th>
					<th><@s.text name="mbctt.callTime" /></th>
					<th><@s.text name="mbctt.phoneNum" /></th>
					<th><@s.text name="mbctt.issueSummary" /></th>
					<th><@s.text name="mbctt.resolution" /></th>
					<th><@s.text name="mbctt.act" /></th>
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