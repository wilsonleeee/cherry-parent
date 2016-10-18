<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/tpl/BINOLCTTPL01.js"></script>
<@s.i18n name="i18n.ct.BINOLCTTPL01">
<@s.url id="searchtpl_url" action="BINOLCTTPL01_search"/>
<@s.url id="binolcttpl02Url" action="BINOLCTTPL02_init" namespace="/ct"/>
<@s.url id="disableTemplateUrl" action="BINOLCTTPL01_stop"/>
<div class="hide">
	<a id="searchTplUrl" href="${searchtpl_url}"></a>
</div>
<div class="panel-header">
    <div class="clearfix"> 
        <span class="breadcrumb left"> 
        	<span class="ui-icon icon-breadcrumb"></span>
        	<a href="#"><@s.text name="cttpl.module" /></a> > 
        	<a href="#"><@s.text name="cttpl.submodule" /></a> > 
        	<a href="#"><@s.text name="cttpl.title" /></a>
        </span>
		<@cherry.show domId="BINOLCTTPL01ADD">
			<a href="${(binolcttpl02Url)!}" class="add right" onclick="javascript:openWin(this);return false;">
				<span class="ui-icon icon-add"></span>
				<span class="button-text"><@s.text name="cttpl.addtemplate" /></span>
			</a>
		</@cherry.show>      	
    </div>
</div>
<div class="panel-content clearfix">
	<div class="box">
		<@cherry.form id="mainForm" class="inline">
        	<div class="box-header">
    			<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition" /></strong>
    		</div>
    		<div class="box-content clearfix">
				<div class="column" style="width:49%;">
				<@s.text name='cttpl.allvalue' id="allvalue"/>
               	<#if (brandList?? && brandList?size > 0) >
         			<p>
         				<label><@s.text name="cttpl.brandName"/></label>
         				<@s.select name="brandInfoId" list="brandList" listKey="brandInfoId" listValue="brandName" value="" />
         			</p>
         		<#else>
         			<input type="hidden" name="brandInfoId" value="${(brandInfoId)!}"/>
        		</#if>
				<p>
					<label><@s.text name="cttpl.templateStatus"/></label>
					<@s.select name="status" list='#application.CodeTable.getCodes("1196")' listKey="CodeKey" listValue="Value"
					headerKey="" headerValue="%{#allvalue}" value="1" />
				</p>
				<p>
					<label><@s.text name="cttpl.templateName"/></label>
					<input name="templateName" class="text" type="text" />
				</p>
                </div>
				<div class="column last" style="width:50%;">
				<p>
					<label style="width:90px"><@s.text name="cttpl.templateUse"/></label>
               		<@s.select name="templateUse" list='#application.CodeTable.getCodes("1197")' listKey="CodeKey" listValue="Value" 
               		headerKey="" headerValue="%{#allvalue}" value="" />
               	</p>
				<p>
					<label style="width:90px"><@s.text name="cttpl.customerType"/></label>
					<@s.select name="customerType" list='#application.CodeTable.getCodes("1198")' listKey="CodeKey" listValue="Value"
					headerKey="" headerValue="%{#allvalue}" value="" />
				</p>
                </div>
			</div>
			<p class="clearfix">
				<button class="right search"  onclick="BINOLCTTPL01.search();return false;">
	              	<span class="ui-icon icon-search-big"></span>
	              	<span class="button-text"><@s.text name="cttpl.search" /></span>
				</button>
			</p>
		</@cherry.form>
	</div>	
	<div id="templatedetail" class="section">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="cttpl.showFindResult" />
	        </strong>
			<span class="right">
				<a href="#" class="setting">
					<span class="button-text"><@s.text name="global.page.colSetting"/></span>
		 			<span class="ui-icon icon-setting"></span>
				</a>
			</span>
		</div>
		<div class="section-content">
			<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th><@s.text name="cttpl.rowNumber" /></th>
					<th><@s.text name="cttpl.templateCode" /></th>
					<th><@s.text name="cttpl.templateName" /></th>
					<th><@s.text name="cttpl.templateUse" /></th>
					<th><@s.text name="cttpl.contents" /></th>
					<th><@s.text name="cttpl.customerType" /></th>
					<th><@s.text name="cttpl.templateStatus" /></th>
					<th><@s.text name="cttpl.act" /></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			</table>
		</div>
	</div>
</div>
<div class="dialog2 clearfix" style="display:none" id="disable_template_dialog">
	<p class="clearfix">
		<p class="message"><span><@s.text name="cttpl.disableTemplateMessage"/></span></p>
	</p>
</div>
<div id="dialogTitle" class="hide"><@s.text name="cttpl.disableTemplate"/></div>
<div id="dialogConfirm" class="hide"><@s.text name="cttpl.ok"/></div>
<div id="dialogCancel" class="hide"><@s.text name="cttpl.cancel"/></div>
<span id ="disableTemplateUrl" style="display:none">${disableTemplateUrl}</span>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>



