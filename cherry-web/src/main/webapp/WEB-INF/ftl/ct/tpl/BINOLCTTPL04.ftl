<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/tpl/BINOLCTTPL04.js"></script>
<@s.i18n name="i18n.ct.BINOLCTTPL04">
<@s.url id="search_url" action="BINOLCTTPL04_search"/>
<@s.url id="addInit_url" action="BINOLCTTPL04_addInit"/>
<@s.url id="add_url" action="BINOLCTTPL04_add"/>
<@s.url id="update_url" action="BINOLCTTPL04_update"/>
<div class="hide">
	<a id="searchUrl" href="${search_url}"></a>
	<a id="addUrl" href="${add_url}"></a>
	<a id="updateUrl" href="${update_url}"></a>
</div>
<div class="panel-header">
    <div class="clearfix"> 
        <span class="breadcrumb left"> 
        	<span class="ui-icon icon-breadcrumb"></span>
        	<a href="#"><@s.text name="cttpl.module" /></a> > 
        	<a href="#"><@s.text name="cttpl.submodule" /></a> > 
        	<a href="#"><@s.text name="cttpl.title" /></a>
        </span>
        <@cherry.show domId="BINOLCTTPL04ADD">
			<a href="${addInit_url}" class="add right" onclick="javascript:BINOLCTTPL04.initAddDialog(this);return false;">
				<span class="ui-icon icon-add"></span>
				<span class="button-text"><@s.text name="cttpl.addbtn" /></span>
			</a>
		</@cherry.show>	
    </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
	<div class="box">
		<@cherry.form id="mainForm" class="inline">
        	<div class="box-header">
    			<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition" /></strong>
    		</div>
    		<div class="box-content clearfix">
				<div class="column" style="width:49%;">
					<@s.text name='cttpl.allValue' id="allvalue"/>
	               	<#if (brandList?? && brandList?size > 0) >
	         			<p>
	         				<label><@s.text name="cttpl.brandName"/></label>
	         				<@s.select name="brandInfoId" list="brandList" listKey="brandInfoId" listValue="brandName" value="" />
	         			</p>
	         		<#else>
	         			<input type="hidden" name="brandInfoId" value="${(brandInfoId)!}"/>
	        		</#if>
	        		<p>
						<label style="width:80px;"><@s.text name="cttpl.charValue"/></label>
						<input class="text" name="sCharValue" />
					</p>
					<p>
						<label style="width:80px;"><@s.text name="cttpl.commType"/></label>
						<@s.select name="commType" list='#application.CodeTable.getCodes("1203")' listKey="CodeKey" listValue="Value"
						headerKey="" headerValue="%{#allvalue}" value="1" />
					</p>
                </div>
				<div class="column last" style="width:50%;">
					<p>
						<label><@s.text name="cttpl.validFlag"/></label>
						<@s.select name="validFlag" list='#application.CodeTable.getCodes("1196")' listKey="CodeKey" listValue="Value"
						headerKey="" headerValue="%{#allvalue}"  value="1" />
					</p>
                </div>
			</div>
			<p class="clearfix">
				<button class="right search"  onclick="BINOLCTTPL04.delError();BINOLCTTPL04.search();return false;">
	              	<span class="ui-icon icon-search-big"></span>
	              	<span class="button-text"><@s.text name="global.page.search" /></span>
				</button>
			</p>
		</@cherry.form>
	</div>	
	<div id="illegalCharDetail" class="section hide">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="global.page.list" />
	        </strong>
		</div>
		<div class="section-content">
			<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th><@s.text name="No." /></th>
					<th><@s.text name="cttpl.charValue" /></th>
					<th><@s.text name="cttpl.commType" /></th>
					<th><@s.text name="cttpl.remark" /></th>
					<th class="center"><@s.text name="cttpl.validFlag" /></th>
					<th class="center"><@s.text name="cttpl.act" /></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			</table>
		</div>
	</div>
</div>
<div class="hide">
	<div id="disableDialogContent">
		<p class="clearfix">
			<p class="message"><span><@s.text name="cttpl.disableDialogMessage"/></span></p>
		</p>
	</div>
	<div id="disableDialogTitle"><@s.text name="cttpl.disableDialogTitle"/></div>
	<div id="enableDialogContent">
		<p class="clearfix">
			<p class="message"><span><@s.text name="cttpl.enableDialogMessage"/></span></p>
		</p>
	</div>
	<div id="enableDialogTitle"><@s.text name="cttpl.enableDialogTitle"/></div>
	<div id="addDialogTitle"><@s.text name="cttpl.addDialogTitle"/></div>
	<div id="editDialogTitle"><@s.text name="cttpl.editDialogTitle"/></div>
	<div id="dialogConfirm"><@s.text name="global.page.ok"/></div>
	<div id="dialogCancel"><@s.text name="global.page.cancle"/></div>
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>



