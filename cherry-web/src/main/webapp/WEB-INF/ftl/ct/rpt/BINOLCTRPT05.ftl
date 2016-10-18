<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<#include "/WEB-INF/ftl/common/head.inc.ftl">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/rpt/BINOLCTRPT05.js"></script>
<@s.i18n name="i18n.ct.BINOLCTRPT05">
<div class="hide">
	<@s.url id="search_url" action="BINOLCTRPT05_search"/>
	<a id="searchUrl" href="${search_url}"></a>
	<@s.url id="export_url" action="BINOLCTRPT05_export"/>
</div>
<div class="panel ui-corner-all">
	<div id="div_main">
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
				<form id="mainForm" method="post" class="inline">
		        	<div class="box-header">
		    			<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition" /></strong>
		    		</div>
		    		<div class="box-content clearfix">
		    			<@s.text name='ctrpt.allvalue' id="allvalue"/>
		 				<div class="column last" style="height: auto;width:100%">
		 					<input class="hide" name="batchId" value="${(batchId)!?html}" />
		 					<input class="hide" id="sendTime" name="sendTime" value="${(sendTime)!?html}" />
		 					<p>
		 						<@s.text name="ctrpt.timeRange" />
		 						<select name="queryType" id="queryType" onchange="BINOLCTRPT05.changeQueryType();return false;">
		 							<option value="1"><@s.text name="ctrpt.specifiedTime" /></option>
		 							<option value="2"><@s.text name="ctrpt.RelativeTime" /></option>
		 						</select>
		 						<span id="selectDiv">
		 							<span id="selectDiv_1">
				 						<span><@s.textfield  id="startTime" name="startTime" value="${(startTime)!?html}" cssClass="date"/></span>
				 						- 
				 						<span><@s.textfield  id="endTime" name="endTime" value="${(endTime)!?html}" cssClass="date" /></span>
			 						</span>
			 						<span id="selectDiv_2">
				 						<@s.text name="ctrpt.msgSend" />
				 						<span><@s.textfield cssStyle="width:50px;" id="startDays" name="startDays" value="${(startDays)!?html}" cssClass="text"/></span>
				 						- 
				 						<span><@s.textfield cssStyle="width:50px;"  id="endDays" name="endDays"  value="${(endDays)!?html}" cssClass="text"/></span>
				 						<@s.text name="ctrpt.day" />
			 						</span>
		 						</span>
		 					</p>
		 				</div>
		 				<#-- ===========================组织联动共通导入开始======================= -->
					 	<@s.action name="BINOLCM13_query" namespace="/common" executeResult=true>
					 		<@s.param name="showType">0</@s.param>
					        <@s.param name="businessType">1</@s.param>
					        <@s.param name="operationType">1</@s.param>
					        <@s.param name="mode">dpat,area,chan</@s.param>
					    </@s.action>
					    <#-- ===========================组织联动共通导入结束======================= -->
					</div>
					<p class="clearfix">
						<button class="right search"  onclick="BINOLCTRPT05.search();return false;">
			              	<span class="ui-icon icon-search-big"></span>
			              	<span class="button-text"><@s.text name="global.page.search" /></span>
						</button>
					</p>
				</form>
			</div>
			<div class="section hide" id="searchResult">
				<div class="section-header">
					<strong>
			        	<span class="ui-icon icon-ttl-section-search-result"></span>
			        	<@s.text name="global.page.list" />
			        </strong>
					<span class="right">
						<a href="#" class="setting">
							<span class="button-text"><@s.text name="global.page.colSetting"/></span>
				 			<span class="ui-icon icon-setting"></span>
						</a>
					</span>
				</div>
				<div class="section-content" >
					<div class="toolbar clearfix">
						<select id="exportFormat" class="select" style="width:150px;">
							<option value="1"><@s.text name="ctrpt.exportEXCELBtn" /></option>
							<option value="2"><@s.text name="ctrpt.exportCSVBtn" /></option>
							<option value="3"><@s.text name="ctrpt.exportJoinEXCELBtn" /></option>
							<option value="4"><@s.text name="ctrpt.exportJoinCSVBtn" /></option>
							<option value="5"><@s.text name="ctrpt.exportSaleEXCELBtn" /></option>
							<option value="6"><@s.text name="ctrpt.exportSaleCSVBtn" /></option>
						</select>
						<a  class="export" style="margin-right:10px;" onclick="javascript:BINOLCTRPT05.exportExcel(this,$('#exportFormat').val());return false;"  href="${export_url }">
							<span class="ui-icon icon-export"></span>
							<span class="button-text"><@s.text name="global.page.exportPrint" /></span>
						</a>
						<span id="analysisTotalInfoSpan"></span>
					</div>
					<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
						<tr>
							<th><@s.text name="ctrpt.rowNumber" /></th>
							<th><@s.text name="ctrpt.region" /></th>
							<th><@s.text name="ctrpt.agency" /></th>
							<th><@s.text name="ctrpt.counter" /></th>
							<th><@s.text name="ctrpt.sendNumber" /></th>
							<th><@s.text name="ctrpt.joinNumber" /></th>
							<th><@s.text name="ctrpt.joinRate" /></th>
							<th><@s.text name="ctrpt.billQuantity" /></th>
							<th><@s.text name="ctrpt.amount" /></th>
							<th><@s.text name="ctrpt.quantity" /></th>
							<th class="center"><@s.text name="ctrpt.act" /></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="hide">
	<div id="errorMsg1"><@s.text name="ctrpt.errorMsg1" /></div>
	<div id="errorMsg2"><@s.text name="ctrpt.errorMsg2" /></div>
	<div id="errorMsg3"><@s.text name="ctrpt.errorMsg3" /></div>
	<div id="joinDialogTitle"><@s.text name="ctrpt.joinDialogTitle" /></div>
	<div id="sendDialogTitle"><@s.text name="ctrpt.sendDialogTitle" /></div>
	<div id="saleDialogTitle"><@s.text name="ctrpt.saleDialogTitle" /></div>
	<div id="confirmBtn"><@s.text name="global.page.ok"/></div>
</div>
</@s.i18n>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
<#include "/WEB-INF/ftl/common/popExportDialog.ftl">

