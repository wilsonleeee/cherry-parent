<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#if showType?exists && showType == 'detail'>
<#include "/WEB-INF/ftl/common/head.inc.ftl">
</#if>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/rpt/BINOLCTRPT04.js"></script>
<@s.i18n name="i18n.ct.BINOLCTRPT04">
<@s.url id="search_url" action="BINOLCTRPT04_search"/>
<@s.url id="export_url" action="BINOLCTRPT04_export"/>
<#if showType?exists && showType == 'detail'>
<div class="panel ui-corner-all">
<div id="div_main">
</#if>
<input name="showViewType" class="hide" id="showViewType" value="${(showType)!?html}"/>
<div class="hide">
	<a id="searchUrl" href="${search_url}"></a>
</div>
<div class="panel-header">
    <div class="clearfix"> 
        <span class="breadcrumb left"> 
        	<span class="ui-icon icon-breadcrumb"></span>
        	<a href="#"><@s.text name="ctrpt.title" /></a> > 
        	<a href="#"><@s.text name="ctrpt.detailTitle" /></a>
        </span>
    </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
	<div class="box">
	<#if showType?exists && showType == 'detail'>
		<form id="mainForm" method="post" class="inline">
			<input name="brandInfoId" class="hide" id="brandInfoId" value="${(brandInfoId)!?html}"/>
			<input name="organizationInfoId" class="hide" id="organizationInfoId" value="${(organizationInfoId)!?html}"/>
			<input name="batchId" class="hide" id="batchId" value="${(batchId)!?html}"/>
        	<div class="box-header">
    			<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition" /></strong>
    		</div>
    		<div class="box-content clearfix">
    			<@s.text name='ctrpt.allvalue' id="allvalue"/>
				<div class="column" style="width:49%;">
					<p>
						<label style="width:90px"><@s.text name="ctrpt.memCode"/></label>
						<input name="memCode" class="text" type="text" />
					</p>
					<p>
						<label style="width:90px"><@s.text name="ctrpt.mobilePhone"/></label>
						<input name="mobilePhone" class="text" type="text" />
					</p>
                </div>
				<div class="column last" style="width:50%;">
					<p>
						<label style="width:90px"><@s.text name="ctrpt.messageType"/></label>
						<@s.select name="commType" list='#application.CodeTable.getCodes("1203")' listKey="CodeKey" listValue="Value" value="1" 
						headerKey="" headerValue="%{#allvalue}" value="" />
					</p>
					<p>
						<label style="width:90px"><@s.text name="ctrpt.errorType"/></label>
						<@s.select name="errorType" list='#application.CodeTable.getCodes("1273")' listKey="CodeKey" listValue="Value" value="1" 
						headerKey="" headerValue="%{#allvalue}" value=""/>
					</p>
                </div>
			</div>
			<p class="clearfix">
				<button class="right search"  onclick="BINOLCTRPT04.search();return false;">
	              	<span class="ui-icon icon-search-big"></span>
	              	<span class="button-text"><@s.text name="ctrpt.search" /></span>
				</button>
			</p>
		</form>
	<#else>	
		<@cherry.form id="mainForm" class="inline">
			<input name="brandInfoId" class="hide" id="brandInfoId" value="${(brandInfoId)!?html}"/>
			<input name="organizationInfoId" class="hide" id="organizationInfoId" value="${(organizationInfoId)!?html}"/>
        	<div class="box-header">
    			<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition" /></strong>
    		</div>
    		<div class="box-content clearfix">
    			<@s.text name='ctrpt.allvalue' id="allvalue"/>
				<div class="column" style="width:49%;">
					<p>
						<label style="width:90px"><@s.text name="ctrpt.memCode"/></label>
						<input name="memCode" class="text" type="text" />
					</p>
					<p>
						<label style="width:90px"><@s.text name="ctrpt.mobilePhone"/></label>
						<input name="mobilePhone" class="text" type="text" />
					</p>
					<p>
						<label style="width:90px"><@s.text name="ctrpt.sendTime"/></label>
						<span><@s.textfield name="sendBeginDate" cssClass="date" value="${(sendBeginDate)!}"></@s.textfield></span> - 
              			<span><@s.textfield name="sendEndDate" cssClass="date" value="${(sendEndDate)!}"></@s.textfield></span>
					</p>
                </div>
				<div class="column last" style="width:50%;">
					<p>
						<label style="width:90px"><@s.text name="ctrpt.messageType"/></label>
						<@s.select name="commType" list='#application.CodeTable.getCodes("1203")' listKey="CodeKey" listValue="Value" value="1" 
						headerKey="" headerValue="%{#allvalue}" value="" />
					</p>
					<p>
						<label style="width:90px"><@s.text name="ctrpt.errorType"/></label>
						<@s.select name="errorType" list='#application.CodeTable.getCodes("1273")' listKey="CodeKey" listValue="Value" value="1" 
						headerKey="" headerValue="%{#allvalue}" value=""/>
					</p>
                </div>
			</div>
			<p class="clearfix">
				<button class="right search"  onclick="BINOLCTRPT04.search();return false;">
	              	<span class="ui-icon icon-search-big"></span>
	              	<span class="button-text"><@s.text name="ctrpt.search" /></span>
				</button>
			</p>
		</@cherry.form>
	</#if>
	</div>
	<div id="sendMsgDetail" class="section hide">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="ctrpt.showDetailResult" />
	        </strong>
			<span class="right">
				<a href="#" class="setting">
					<span class="button-text"><@s.text name="global.page.colSetting"/></span>
		 			<span class="ui-icon icon-setting"></span>
				</a>
			</span>
		</div>
		<div class="section-content">
			<div class="toolbar clearfix">
				<a  class="export left" onclick="javascript:BINOLCTRPT04.exportExcel(this,'0');return false;"  href="${export_url }">
					<span class="ui-icon icon-export"></span>
					<span class="button-text"><@s.text name="ctrpt.exportEXCELBtn" /></span>
				</a>
				<a  class="export left" onclick="javascript:BINOLCTRPT04.exportExcel(this,'1');return false;"  href="${export_url }">
					<span class="ui-icon icon-export"></span>
					<span class="button-text"><@s.text name="ctrpt.exportCSVBtn" /></span>
				</a>
			</div>
			<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th><@s.text name="ctrpt.rowNumber" /></th>
					<th><@s.text name="ctrpt.memCode" /></th>
					<th><@s.text name="ctrpt.memName" /></th>
					<th><@s.text name="ctrpt.mobilePhone" /></th>
					<th><@s.text name="ctrpt.messageType" /></th>
					<th><@s.text name="ctrpt.message" /></th>
					<th><@s.text name="ctrpt.sendTime" /></th>
					<th><@s.text name="ctrpt.errorType" /></th>
					<th><@s.text name="ctrpt.errorMsg" /></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			</table>
		</div>
	</div>
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
<#include "/WEB-INF/ftl/common/popExportDialog.ftl">
<#if showType?exists && showType == 'detail'>
</div>
</div>
</#if>
</@s.i18n>
