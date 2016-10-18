<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/cct/BINOLMBCCT10.js"></script>
<#if (currentMenuID?? && currentMenuID == "BINOLMBCCT10") >
<link rel="stylesheet" href="/Cherry/css/common/blueprint/crm_web.css" type="text/css">
<link rel="stylesheet" href="/Cherry/css/cherry/gadgets.css" type="text/css">
</#if>
<@s.i18n name="i18n.mb.BINOLMBCCT10">
<@s.url id="issueSearch_Url" action="BINOLMBCCT10_search"/>
<@s.url id="addIssue_Url" action="BINOLMBMBM28_init"/>
<@s.url id="export_url" action="BINOLMBCCT10_export"/>
<div class="hide">
	<a id="issueSearchUrl" href="${issueSearch_Url}"></a>
	<a id="addIssueUrl" href="${addIssue_Url}"></a>
</div>
<div class="clearfix" id="issueRptDiv">
	<div class="panel-content clearfix">
		<div class="box">
			<form id="getIssueForm" method="post" class="inline">
				<#if (currentMenuID?? && currentMenuID == "BINOLMBCCT10") >
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
						<label style="width:120px"><@s.text name="mbcct.cNo"/></label>
						<input name="classNo" class="text" type="text" />
					</p>
					<p>
						<label style="width:120px"><@s.text name="mbcct.customerCode"/></label>
						<input name="customerCode" class="text" type="text" />
					</p>
	               	<p>
						<label style="width:120px"><@s.text name="mbcct.customerNumber"/></label>
						<input name="customerNumber" class="text" type="text" />
					</p>
					<p>
						<label style="width:120px"><@s.text name="mbcct.issueSource"/></label>
						<select id="issueSource" name="issueSource">
							<option value=""><@s.text name="mbcct.allType"/></option>
							<option value="1"><@s.text name="mbcct.fromCallCenter"/></option>
							<option value="2"><@s.text name="mbcct.fromMemberManage"/></option>
						</select>
					</p>
					<p>
						<label style="width:120px"><@s.text name="mbcct.createTime"/></label>
						<span><@s.textfield  id="createTimeStart" name="createTimeStart"  cssClass="date" style="width:90px"/></span>
						- 
						<span><@s.textfield  id="createTimeEnd" name="createTimeEnd" cssClass="date" style="width:90px"/></span>
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
						<label style="width:90px"><@s.text name="mbcct.customerName"/></label>
						<input name="customerName" class="text" type="text" />
					</p>
					<p>
						<label style="width:90px"><@s.text name="mbcct.issueType"/></label>
						<@s.select name="issueType" list='#application.CodeTable.getCodes("1272")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#allvalue}" value="" />
					</p>
					<p>
						<label style="width:90px"><@s.text name="mbcct.resolution"/></label>
						<@s.select name="resolution" list='#application.CodeTable.getCodes("1117")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#allvalue}" value="" />
					</p>
	                </div>
				</div>
				<p class="clearfix">
					<button class="right search"  onclick="BINOLMBCCT10.search();return false;">
		              	<span class="ui-icon icon-search-big"></span>
		              	<span class="button-text"><@s.text name="mbcct.search" /></span>
					</button>
				</p>
			</form>
		</div>
		<div id="issueDetail" class="section hide">
			<div class="section-header">
				<strong>
		        	<span class="ui-icon icon-ttl-section-search-result"></span>
		        	<@s.text name="mbcct.showFindResult" />
		        </strong>
			</div>
			<div class="section-content">
				<div class="toolbar clearfix">
					<span class="left">
						<@cherry.show domId="MBCCT10EXPORT">
							<a href="${export_url}" id="exportExcel" class="export" onclick="BINOLMBCCT10.exportExcel(this,'0');return false;">
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><@s.text name="mbcct.exportExcelBtn" /></span>
							</a>
							<a href="${export_url}" id="exportCsv" class="export" onclick="BINOLMBCCT10.exportExcel(this,'1');return false;">
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><@s.text name="mbcct.exportCsvBtn" /></span>
							</a>
						</@cherry.show>
					</span>
					<span class="right">
						<a href="#" class="setting" id="issueInfoTable_colSetting">
							<span class="button-text"><@s.text name="global.page.colSetting"/></span>
				 			<span class="ui-icon icon-setting"></span>
						</a>
					</span>
				</div>
				<table id="issueInfoTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
				<thead>
					<tr>
						<th><@s.text name="mbcct.callId" /></th>
						<th><@s.text name="mbcct.customerCode" /></th>
						<th><@s.text name="mbcct.customerName" /></th>
						<th><@s.text name="mbcct.customerNumber" /></th>
						<th><@s.text name="mbcct.issueSummary" /></th>
						<th><@s.text name="mbcct.issueType" /></th>
						<th><@s.text name="mbcct.issueSubType" /></th>
						<th><@s.text name="mbcct.resolution" /></th>
						<th><@s.text name="mbcct.callTime" /></th>
						<th><@s.text name="mbcct.cNo" /></th>
						<th><@s.text name="mbcct.createTime" /></th>
						<th><@s.text name="mbcct.resolutionDate" /></th>
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
<#if (currentMenuID?? && currentMenuID == "BINOLMBCCT10") >
	<div class="hide" id="issueDetailDialog"></div>
	<div class="hide" id="issueDetailDialogTitle"><@s.text name="mbcct.issueDetailDialogTitle" /></div>
</#if>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
<#include "/WEB-INF/ftl/common/popExportDialog.ftl">
</@s.i18n>