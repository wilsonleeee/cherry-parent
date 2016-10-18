<#assign c=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTCOM08">
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM08.js"></script>
<@s.url id="searchtpl_url" action="BINOLCTCOM08_search"/>
<div class="hide">
	<a id="searchTplUrl" href="${searchtpl_url}"></a>
</div>
<div class="box">
	<div class="box-header">
		<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition" /></strong>
	</div>
	<form id="getTemplateForm" method="post" class="inline"> 
	<div class="box-content clearfix">
		<input name="status" class="hide" id="status" value="1"/>
		<input name="messageType" class="hide" id="messageType" value="${(messageType)!?html}"/>
		<input name="templateUse" class="hide" id="templateUse" value="${(templateUse)!?html}"/>
		<@s.text name='ctcom.allvalue' id="allvalue"/>
		<div class="column" style="width:49%;">
		<p>
			<label><@s.text name="ctcom.templateName"/></label>
			<input name="templateName" class="text" type="text" />
		</p>
        </div>
        <div class="column last" style="width:50%;">
		<p>
			<label style="width:90px"><@s.text name="ctcom.customerType"/></label>
			<@s.select name="customerType" list='#application.CodeTable.getCodes("1198")' listKey="CodeKey" listValue="Value"
			headerKey="" headerValue="%{#allvalue}" value="" />
		</p>
       	</div>
	</div>
	</form>
	<p class="clearfix">
		<button class="right search"  onclick="search();return false;">
          	<span class="ui-icon icon-search-big"></span>
          	<span class="button-text"><@s.text name="ctcom.search" /></span>
		</button>
	</p>
</div>
<div id="templatedetail" class="section">
	<div class="section-header">
		<strong>
        	<span class="ui-icon icon-ttl-section-search-result"></span>
        	<@s.text name="ctcom.showFindResult" />
        </strong>
	</div>
	<div class="section-content">
		<table id="templateDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<th><@s.text name="ctcom.rowNumber" /></th>
				<th><@s.text name="ctcom.templateCode" /></th>
				<th><@s.text name="ctcom.templateName" /></th>
				<th><@s.text name="ctcom.templateUse" /></th>
				<th><@s.text name="ctcom.contents" /></th>
				<th><@s.text name="ctcom.customerType" /></th>
				<th><@s.text name="ctcom.templateStatus" /></th>
				<th><@s.text name="ctcom.act" /></th>
			</tr>
		</thead>
		<tbody id="templateDataTableBody">
		</tbody>
		</table>
	</div>
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>
