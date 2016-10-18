<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/tpl/BINOLCTTPL03.js"></script>
<@s.i18n name="i18n.ct.BINOLCTTPL03">
<div class="hide">
	<@s.url id="search_url" action="BINOLCTTPL03_search" namespace="/ct" />
	<a id="searchUrl" href="${search_url}"></a>
	<@s.url id="disOrEnable_url" action="BINOLCTTPL03_disOrEnable" namespace="/ct" />
	<a id="disOrEnableUrl" href="${disOrEnable_url}"></a>
	<@s.url id="update_url" action="BINOLCTTPL03_update" namespace="/ct"  />
	<a id="updateUrl" href="${update_url}"></a>
	<@s.url id="edit_url" action="BINOLCTTPL03_edit" namespace="/ct"  />
	<a id="editUrl" href="${edit_url}"></a>
	<a id="updateUrl" href="${update_url}"></a>
	<@s.text id="allvalue" name="cttpl.allvalue" />
</div>
<div class="panel-header">
    <div class="clearfix"> 
        <span class="breadcrumb left"> 
        	<span class="ui-icon icon-breadcrumb"></span>
        	<a href="#"><@s.text name="cttpl.module" /></a> > 
        	<a href="#"><@s.text name="cttpl.submodule" /></a> >
        	<a href="#"><@s.text name="cttpl.title" /></a>  
        </span>   	
    </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
	<div class="section-header">
		<strong>
		<span class="ui-icon icon-ttl-section-info"></span>
			<@s.text name="cttpl.outline" />
		</strong>
	</div>
	<div class="box3 clearfix">
		<@cherry.form id="mainForm" class="inline">
			<div class="column" style="width:50%;">
				<p>
					<label><@s.text name="cttpl.queryType"/></label>
					<select name="queryType" id="queryType" style="width:120px" onchange="BINOLCTTPL03.queryTypeChange();return false;">
						<option value="1"><@s.text name="cttpl.queryType1"/></option>
						<option value="2"><@s.text name="cttpl.queryType2"/></option>		
					<select>
				</p>
			</div>
			<div class="column last hide" style="width:49%;" id="templateUseDiv">
				<p>
					<label><@s.text name="cttpl.templateUse"/></label>
					<@s.select name="templateUse" id="templateUse" list='#application.CodeTable.getCodes("1197")' listKey="CodeKey" listValue="Value"
					 onChange="BINOLCTTPL03.delActionResult();BINOLCTTPL03.search();return false;" />
				</p>
			</div>
		</@cherry.form>
	</div>	
	<div id="variableDetail" class="section hide">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="cttpl.result.list" />
	        </strong>
		</div>
		<div class="section-content" id="variableDetailTable">

		</div>
	</div>
</div>
<div style="display:none" id="dialog">
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
	<div id="editDialogTitle"><@s.text name="cttpl.editDialogTitle" /></div>
	<div id="dialogConfirm"><@s.text name="global.page.ok"/></div>
	<div id="dialogCancel"><@s.text name="global.page.cancle"/></div>
	<div id="errorMsg1"><@s.text name="cttpl.errorMsg1" /></div>
	<div id="errorMsg2"><@s.text name="cttpl.errorMsg2"/></div>
	<div id="errorMsg3"><@s.text name="cttpl.errorMsg3"/></div>
</div>
</@s.i18n>



