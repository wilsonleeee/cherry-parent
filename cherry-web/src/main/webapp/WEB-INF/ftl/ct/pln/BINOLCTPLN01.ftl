<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/pln/BINOLCTPLN01.js"></script>
<@s.i18n name="i18n.ct.BINOLCTPLN01">
<@s.url id="searchpln_url" action="BINOLCTPLN01_search"/>
<@s.url id="binolctcom01Url" action="BINOLCTCOM01_init" namespace="/ct" />
<@s.url id="stopCommPlanUrl" action="BINOLCTPLN01_stop"/>
<div class="hide">
	<a id="searchPlnUrl" href="${searchpln_url}"></a>
</div>
<div class="panel-header">
    <div class="clearfix"> 
        <span class="breadcrumb left"> 
        	<span class="ui-icon icon-breadcrumb"></span>
        	<a href="#"><@s.text name="ctpln.module" /></a> > 
        	<a href="#"><@s.text name="ctpln.submodule" /></a> > 
        	<a href="#"><@s.text name="ctpln.title" /></a>
        </span>
		<@cherry.show domId="BINOLCTPLN01ADD">
			<a href="${(binolctcom01Url)!}" class="add right" onclick="javascript:openWin(this);return false;">
				<span class="ui-icon icon-add"></span>
				<span class="button-text"><@s.text name="ctpln.addcommunicationplan" /></span>
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
				<@s.text name='ctpln.allvalue' id="allvalue"/>
               	<#if (brandList?? && brandList?size > 0) >
         			<p>
         				<label><@s.text name="ctpln.brandName"/></label>
         				<@s.select name="brandInfoId" list="brandList" listKey="brandInfoId" listValue="brandName" value="" />
         			</p>
         		<#else>
         			<input type="hidden" name="brandInfoId" value="${(brandInfoId)!}"/>
        		</#if>
				<p>
					<label style="width:90px"><@s.text name="ctpln.planName"/></label>
					<input name="planName" class="text" type="text" />
				</p>
				<p class="date">
					<label style="width:90px"><@s.text name="ctpln.createTime"/></label>
               		<span><@s.textfield name="fromDate" cssClass="date"></@s.textfield></span> - 
              		<span><@s.textfield name="toDate" cssClass="date"></@s.textfield></span>
               	</p>
               	<p>
					<label style="width:90px"><@s.text name="ctpln.channel"/></label>
               		<span><@s.select id="channelId" name="channelId" list="channelList" listKey="channelId" listValue="channelName" headerKey="" headerValue="" onchange="BINOLCTPLN01.changeChannel();"/></span>
               	</p>
               	<p>
					<label style="width:90px"><@s.text name="ctpln.validFlag"/></label>
					<@s.select name="validFlag" list='#application.CodeTable.getCodes("1196")' listKey="CodeKey" listValue="Value"
					headerKey="" headerValue="%{#allvalue}" value="1" />
				</p>
                </div>
				<div class="column last" style="width:50%;">
				<p>
					<label><@s.text name="ctpln.campaignName"/></label>
					<input name="campaignName" class="text" type="text" />
				</p>
				<p>
					<label><@s.text name="ctpln.status"/></label>
					<@s.select name="status" list='#application.CodeTable.getCodes("1202")' listKey="CodeKey" listValue="Value"
					headerKey="" headerValue="%{#allvalue}" value="" />
				</p>
				<p>
					<label><@s.text name="ctpln.counter"/></label>
					<input id="counterCode" name="counterCode" type="hidden" value="" />
					<span><input id="counterName" name="counterName" type="text" maxlength="20" class="text disabled" disabled="disabled" value="" /></span>
               	</p>
                </div>
			</div>
			<p class="clearfix">
				<button class="right search"  onclick="BINOLCTPLN01.search();return false;">
	              	<span class="ui-icon icon-search-big"></span>
	              	<span class="button-text"><@s.text name="ctpln.search" /></span>
				</button>
			</p>
		</@cherry.form>
	</div>	
	<div id="plandetail" class="section">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="ctpln.showFindResult" />
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
					<th><@s.text name="ctpln.rowNumber" /></th>
					<th><@s.text name="ctpln.planCode" /></th>
					<th><@s.text name="ctpln.planName" /></th>
					<th><@s.text name="ctpln.campaignCode" /></th>
					<th><@s.text name="ctpln.campaignName" /></th>
					<th><@s.text name="ctpln.channelName" /></th>
					<th><@s.text name="ctpln.counterCode" /></th>
					<th><@s.text name="ctpln.counterName" /></th>
					<th><@s.text name="ctpln.status" /></th>
					<th><@s.text name="ctpln.lastRunTime" /></th>
					<th><@s.text name="ctpln.createTime" /></th>
					<th><@s.text name="ctpln.memo1" /></th>
					<th><@s.text name="ctpln.validFlag" /></th>
					<th><@s.text name="ctpln.act" /></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			</table>
		</div>
	</div>
</div>
 <div class="dialog2 clearfix" style="display:none" id="stop_plan_dialog">
	<p class="clearfix">
		<p class="message"><span><@s.text name="ctpln.stopPlanMessage"/></span></p>
	</p>
</div>
<div id="dialogTitle" class="hide"><@s.text name="ctpln.stopPlan"/></div>
<div id="dialogConfirm" class="hide"><@s.text name="ctpln.ok"/></div>
<div id="dialogCancel" class="hide"><@s.text name="ctpln.cancel"/></div>
<span id ="stopCommPlanUrl" style="display:none">${stopCommPlanUrl}</span>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>



