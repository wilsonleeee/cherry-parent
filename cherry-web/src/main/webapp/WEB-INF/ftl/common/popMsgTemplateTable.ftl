<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<@s.url id="msg_templateSearchUrl" value="/common/BINOLCM32_popMsgTemplateDialog" />


<div id ="msgTemplateDialog" class="dialog hide">
<@s.i18n name="i18n.ct.BINOLCTTPL02">

	<input type="text" class="text" value="" id="msgTemplateDialogSearch" onKeyup ="datatableFilter(this);" maxlength="50"/>
	<a class="search" onClick="datatableFilter(document.getElementById('msgTemplateDialogSearch'))">
		<span class="ui-icon icon-search"></span>
		<span class="button-text"><@s.text name="global.page.searchfor" /></span>
	</a>

	<hr class="space" />
		<table id="msgTemplateDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<th><@s.text name="cttpl.rowNumber" /></th>
				<th><@s.text name="cttpl.templateCode" /></th>
				<th><@s.text name="cttpl.templateName" /></th>
				<th><@s.text name="cttpl.templateUse" /></th>
				<th><@s.text name="cttpl.contents" /></th>
				<th><@s.text name="cttpl.customerType" /></th>
				<th><@s.text name="cttpl.act" /></th>
			</tr>
		</thead>
		<tbody id="dataTableBody">
		</tbody>
		</table>
	<hr>
	<span id ="msgTemplateSearchUrl" style="display:none">${msg_templateSearchUrl}</span>
	<span id ="PopMsgTemplateTitle" style="display:none">
		<@s.text name="global.page.msgTemplate"/>
	</span>
	
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>
