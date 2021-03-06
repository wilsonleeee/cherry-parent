<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/rpt/BINOLCTRPT01.js"></script>
<@s.i18n name="i18n.ct.BINOLCTRPT01">
<@s.url id="planRunSearch_url" action="BINOLCTRPT01_search"/>
<@s.url id="export_url" action="BINOLCTRPT01_export"/>
<div class="hide">
	<a id="planRunSearchUrl" href="${planRunSearch_url}"></a>
</div>
<div class="panel-header">
    <div class="clearfix"> 
        <span class="breadcrumb left"> 
        	<span class="ui-icon icon-breadcrumb"></span>
        	<a href="#"><@s.text name="ctrpt.module" /></a> > 
        	<a href="#"><@s.text name="ctrpt.submodule" /></a> > 
        	<a href="#"><@s.text name="ctrpt.title" /></a>
        </span>
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
				<@s.text name='ctrpt.allvalue' id="allvalue"/>
               	<#if (brandList?? && brandList?size > 0) >
         			<p>
         				<label><@s.text name="ctrpt.brandName"/></label>
         				<@s.select name="brandInfoId" list="brandList" listKey="brandInfoId" listValue="brandName" value="" />
         			</p>
         		<#else>
         			<input type="hidden" name="brandInfoId" value="${(brandInfoId)!}"/>
        		</#if>
				<p>
					<label style="width:90px"><@s.text name="ctrpt.planName"/></label>
					<input name="planName" class="text" type="text" />
				</p>
               	<p>
					<label style="width:90px"><@s.text name="ctrpt.campaignName"/></label>
					<input name="campaignName" class="text" type="text" />
				</p>
				<p>
					<label style="width:90px"><@s.text name="ctrpt.runBeginTime"/></label>
					<span><@s.textfield  id="startTime" name="startTime"  cssClass="date"/></span>
					- 
					<span><@s.textfield  id="endTime" name="endTime" cssClass="date" /></span>
				</p>
				<p>
					<label style="width:90px"><@s.text name="ctrpt.channel"/></label>
               		<span><@s.select id="channelId" name="channelId" list="channelList" listKey="channelId" listValue="channelName" headerKey="" headerValue="" onchange="BINOLCTRPT01.changeChannel();"/></span>
               	</p>
                </div>
				<div class="column last" style="width:50%;">
				<p>
					<label><@s.text name="ctrpt.communicationName"/></label>
					<input name="communicationName" class="text" type="text" />
				</p>
				<p>
					<label><@s.text name="ctrpt.mobilePhone"/></label>
					<input name="mobilePhone" class="text" type="text" />
				</p>
				<p>
					<label><@s.text name="ctrpt.commType"/></label>
					<@s.select name="commType" list='#application.CodeTable.getCodes("1203")' listKey="CodeKey" listValue="Value" value="1" />
				</p>
				<p>
					<label><@s.text name="ctrpt.counter"/></label>
					<input id="counterCode" name="counterCode" type="hidden" value="" />
					<span><input id="counterName" name="counterName" type="text" maxlength="20" class="text disabled" disabled="disabled" value="" /></span>
               	</p>
                </div>
			</div>
			<p class="clearfix">
				<button class="right search"  onclick="BINOLCTRPT01.search();return false;">
	              	<span class="ui-icon icon-search-big"></span>
	              	<span class="button-text"><@s.text name="ctrpt.search" /></span>
				</button>
			</p>
		</@cherry.form>
	</div>
	<div id="runDetail" class="section hide">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="ctrpt.showFindResult" />
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
				<@cherry.show domId="CTRPT01EXPORT">
					<a  class="export left" onclick="javascript:BINOLCTRPT01.exportExcel(this,'0');return false;"  href="${export_url }">
						<span class="ui-icon icon-export"></span>
						<span class="button-text"><@s.text name="ctrpt.exportEXCELBtn" /></span>
					</a>
					<a  class="export left" onclick="javascript:BINOLCTRPT01.exportExcel(this,'1');return false;"  href="${export_url }">
						<span class="ui-icon icon-export"></span>
						<span class="button-text"><@s.text name="ctrpt.exportCSVBtn" /></span>
					</a>
				</@cherry.show>
				<span id="totalInfo" style="margin-left:10px;"></span>
			</div>
			<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th><@s.text name="ctrpt.rowNumber" /></th>
					<th><@s.text name="ctrpt.batchId" /></th>
					<th><@s.text name="ctrpt.planCode" /></th>
					<th><@s.text name="ctrpt.planName" /></th>
					<th><@s.text name="ctrpt.activityCode" /></th>
					<th><@s.text name="ctrpt.activityName" /></th>
					<th><@s.text name="ctrpt.communicationCode" /></th>
					<th><@s.text name="ctrpt.communicationName" /></th>
					<th><@s.text name="ctrpt.channelName" /></th>
					<th><@s.text name="ctrpt.counterCode" /></th>
					<th><@s.text name="ctrpt.counterName" /></th>
					<th><@s.text name="ctrpt.commType" /></th>
					<th><@s.text name="ctrpt.runType" /></th>
					<th><@s.text name="ctrpt.runBeginTime" /></th>
					<th><@s.text name="ctrpt.sendMsgNum" /></th>
					<th><@s.text name="ctrpt.sendMsgErrorNum" /></th>
					<th><@s.text name="ctrpt.runStatus" /></th>
					<th><@s.text name="ctrpt.runError" /></th>
					<th><@s.text name="ctrpt.act" /></th>
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
</@s.i18n>



