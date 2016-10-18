<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/rpt/BINOLCTRPT06.js"></script>
<@s.i18n name="i18n.ct.BINOLCTRPT06">
<@s.url id="search_url" action="BINOLCTRPT06_search"/>
<div class="hide">
	<a id="searchUrl" href="${search_url}"></a>
</div>
<div class="panel-header">
    <div class="clearfix"> 
        <span class="breadcrumb left"> 
        	<span class="ui-icon icon-breadcrumb"></span>
        	<a href="#"><@s.text name="ctrpt06.module" /></a> > 
        	<a href="#"><@s.text name="ctrpt06.submodule" /></a> > 
        	<a href="#"><@s.text name="ctrpt06.title" /></a>
        </span>
    </div>
</div>
 <div id="errorMessage"></div>
<div id="errorDiv2" class="actionError" style="display:none">
    <ul>
        <li><span id="errorSpan2"></span></li>
    </ul>         
</div>
<div class="panel-content clearfix">
	<div class="box">
		<@cherry.form id="mainForm" class="inline">
        	<div class="box-header">
    			<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="ctrpt06.statisticsCondition" /></strong>
    		</div>
    		<div class="box-content clearfix">
				<div class="column" style="width:49%;">
					<@s.text name='ctrpt06.allvalue' id="allvalue"/>
					<p>
						<#if (brandList?? && brandList?size > 0) >
		     				<label style="width:90px"><@s.text name="ctrpt06.brandName"/></label>
		     				<@s.select name="brandInfoId" list="brandList" listKey="brandInfoId" listValue="brandName" value=""></@s.select>
		         		<#else>
		         			<label style="width:90px"><@s.text name="ctrpt06.brandName"/></label>
		         			<@s.select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"></@s.select>
		        		</#if>
	        		</p>
	        		<p>
						<label style="width:90px"><@s.text name="ctrpt06.channel"/></label>
	               		<span><@s.select id="channelId" name="channelId" list="channelList" listKey="channelId" listValue="channelName" headerKey="" headerValue="" onchange="BINOLCTRPT06.changeChannel();"/></span>
	               	</p>
	        		<p>
	        			<label style="width:90px"><@s.text name="ctrpt06.communicationName"/></label>
	        			<span>
	        				<input name="communicationName" class="text" type="text" />
	        			</span>
	        		</p>
                </div>
				<div class="column last" style="width:50%;">
					<p>
						<label style="width:90px"><@s.text name="ctrpt06.date"/></label>
						<span><@s.textfield  id="startDate" name="startDate"  cssClass="date"/></span>
						- 
						<span><@s.textfield  id="endDate" name="endDate" cssClass="date" /></span>
					</p>
					<p>
					<label style="width:90px"><@s.text name="ctrpt06.counter"/></label>
						<input id="counterCode" name="counterCode" type="hidden" value="" />
						<span><input id="counterName" name="counterName" type="text" maxlength="20" class="text disabled" disabled="disabled" value="" /></span>
	               	</p>
                </div>
			</div>
			<p class="clearfix">
				<button class="right search"  onclick="BINOLCTRPT06.search();return false;">
	              	<span class="ui-icon icon-search-big"></span>
	              	<span class="button-text"><@s.text name="ctrpt06.search" /></span>
				</button>
			</p>
		</@cherry.form>
	</div>
	<div id="section" class="section hide">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="ctrpt06.showFindResult" />
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
					<th><@s.text name="ctrpt06.rowNumber" /></th>
					<th><@s.text name="ctrpt06.channelName" /></th>
					<th><@s.text name="ctrpt06.counterCode" /></th>
					<th><@s.text name="ctrpt06.counterName" /></th>
					<th><@s.text name="ctrpt06.communicationName" /></th>
					<th><@s.text name="ctrpt06.sendMemCount" /></th>
					<th><@s.text name="ctrpt06.sendTime" /></th>
				</tr>
			</thead>
			</table>
		</div>
	</div>
</div>
<div class="hide">
	<div id="errorMsg1"><@s.text name="ctrpt06.errorMsg1" /></div>
	<div id="errorMsg2"><@s.text name="ctrpt06.errorMsg2" /></div>
	<div id="errorMsg3"><@s.text name="ctrpt06.errorMsg3" /></div>
	<div id="detailDialogTitle"><@s.text name="ctrpt06.detailDialogTitle" /></div>
	<div id="confirmBtn"><@s.text name="global.page.ok"/></div>
</div>
<div id="div_main"><div class="hide" id="dialogInit"></div></div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>



