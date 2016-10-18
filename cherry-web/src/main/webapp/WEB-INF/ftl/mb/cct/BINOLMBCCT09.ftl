<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/cct/BINOLMBCCT09.js"></script>
<#if (currentMenuID?? && currentMenuID == "BINOLMBCCT09") >
<link rel="stylesheet" href="/Cherry/css/common/blueprint/crm_web.css" type="text/css">
<link rel="stylesheet" href="/Cherry/css/cherry/gadgets.css" type="text/css">
</#if>
<@s.i18n name="i18n.mb.BINOLMBCCT09">
<@s.url id="callLogSearch_Url" action="BINOLMBCCT09_search"/>
<@s.url id="addIssue_Url" action="BINOLMBMBM28_init"/>
<@s.url id="export_url" action="BINOLMBCCT09_export"/>
<div class="hide">
	<a id="callLogSearchUrl" href="${callLogSearch_Url}"></a>
	<a id="addIssueUrl" href="${addIssue_Url}"></a>
</div>
<div class="clearfix" id="callLogRptDiv">
	<div class="panel-content clearfix">
		<div class="box">
			<form id="getCallLogForm" method="post" class="inline">
				<#if (currentMenuID?? && currentMenuID == "BINOLMBCCT09") >
					<input type="hidden" id="csrftoken" name="csrftoken" value="${(csrftoken)!}">
				</#if>
	        	<div class="box-header">
	    			<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition" /></strong>
	    		</div>
	    		<div class="box-content clearfix">
					<div class="column" style="width:49%;">
					<@s.text name='mbcct.allvalue' id="allvalue"/>
	               	<#if (brandList?? && brandList?size > 0) >
	         			<p>
	         				<label><@s.text name="mbcct.brandName"/></label>
	         				<@s.select name="brandInfoId" list="brandList" listKey="brandInfoId" listValue="brandName" value="" />
	         			</p>
	         		<#else>
	         			<input type="hidden" name="brandInfoId" value="${(brandInfoId)!}"/>
	        		</#if>
					<p>
						<label style="width:90px"><@s.text name="mbcct.cNo"/></label>
						<input name="classNo" class="text" type="text" />
					</p>
					<p>
						<label style="width:90px"><@s.text name="mbcct.callTime"/></label>
						<span><@s.textfield  id="callTimeStart" name="callTimeStart"  cssClass="date" style="width:90px"/></span>
						- 
						<span><@s.textfield  id="callTimeEnd" name="callTimeEnd" cssClass="date" style="width:90px"/></span>
					</p>
	                </div>
					<div class="column last" style="width:50%;">
					<p>
						<label style="width:90px"><@s.text name="mbcct.isMember"/></label>
						<select id="isMember" name="isMember">
							<option value=""><@s.text name="mbcct.allType"/></option>
							<option value="1"><@s.text name="mbcct.membersCall"/></option>
							<option value="2"><@s.text name="mbcct.notMembersCall"/></option>
						</select>
					</p>
					<p>
						<label style="width:90px"><@s.text name="mbcct.customerNumber"/></label>
						<input name="customerNumber" class="text" type="text" />
					</p>
	                </div>
				</div>
				<p class="clearfix">
					<button class="right search"  onclick="BINOLMBCCT09.search();return false;">
		              	<span class="ui-icon icon-search-big"></span>
		              	<span class="button-text"><@s.text name="mbcct.search" /></span>
					</button>
				</p>
			</form>
		</div>
		<div id="callLogDetail" class="section hide">
			<div class="section-header">
				<strong>
		        	<span class="ui-icon icon-ttl-section-search-result"></span>
		        	<@s.text name="mbcct.showFindResult" />
		        </strong>
			</div>
			<div class="section-content">
				<div class="toolbar clearfix">
					<span class="left">
						<@cherry.show domId="MBCCT09EXPORT">
							<a href="${export_url}" id="exportExcel" class="export" onclick="BINOLMBCCT09.exportExcel(this,'0');return false;">
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><@s.text name="mbcct.exportExcelBtn" /></span>
							</a>
							<a href="${export_url}" id="exportCsv" class="export" onclick="BINOLMBCCT09.exportExcel(this,'1');return false;">
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><@s.text name="mbcct.exportCsvBtn" /></span>
							</a>
						</@cherry.show>
					</span>
					<span class="right">
						<a href="#" class="setting" id="callLogDataTable_colSetting">
							<span class="button-text"><@s.text name="global.page.colSetting"/></span>
				 			<span class="ui-icon icon-setting"></span>
						</a>
					</span>
				</div>
				<table id="callLogDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
				<thead>
					<tr>
						<th><@s.text name="mbcct.callId" /></th>
						<th><@s.text name="mbcct.cNo" /></th>
						<th><@s.text name="mbcct.customerNumber" /></th>
						<th><@s.text name="mbcct.customerCode" /></th>
						<th><@s.text name="mbcct.customerName" /></th>
						<th><@s.text name="mbcct.isMember" /></th>
						<th><@s.text name="mbcct.callType" /></th>
						<th><@s.text name="mbcct.callTime" /></th>
						<th><@s.text name="mbcct.act" /></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<#if (currentMenuID?? && currentMenuID == "BINOLMBCCT09") >
	<div class="hide" id="issueDetailDialog"></div>
	<div class="hide" id="issueDetailDialogTitle"><@s.text name="mbcct.issueDetailDialogTitle" /></div>
</#if>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
<#include "/WEB-INF/ftl/common/popExportDialog.ftl">
</@s.i18n>