<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/cct/BINOLMBCCT08.js"></script>
<#if (currentMenuID?? && currentMenuID == "BINOLMBCCT08") >
<link rel="stylesheet" href="/Cherry/css/common/blueprint/crm_web.css" type="text/css">
<link rel="stylesheet" href="/Cherry/css/cherry/gadgets.css" type="text/css">
</#if>
<@s.i18n name="i18n.mb.BINOLMBCCT08">
<@s.url id="customerSearch_Url" action="BINOLMBCCT08_search"/>
<@s.url id="addCustomer_Url" action="BINOLMBCCT05_init"/>
<@s.url id="export_url" action="BINOLMBCCT08_export"/>
<div class="hide">
	<a id="customerSearchUrl" href="${customerSearch_Url}"></a>
	<a id="addCustomerUrl" href="${addCustomer_Url}"></a>
</div>
<div class="clearfix" id="customerRptDiv">
	<div class="panel-content clearfix">
		<div class="box">
			<form id="getCustomerForm" method="post" class="inline">
				<#if (currentMenuID?? && currentMenuID == "BINOLMBCCT08") >
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
						<label style="width:90px"><@s.text name="mbcct.customerName"/></label>
						<input name="customerName" class="text" type="text" />
					</p>
	               	<p>
						<label style="width:90px"><@s.text name="mbcct.industry"/></label>
						<input name="industry" class="text" type="text" />
					</p>
					<p>
						<label style="width:90px"><@s.text name="mbcct.mobilePhone"/></label>
						<input name="mobilePhone" class="text" type="text" />
					</p>
	                </div>
					<div class="column last" style="width:50%;">
					<p>
						<label style="width:90px"><@s.text name="mbcct.customerType"/></label>
						<@s.select name="customerType" list='#application.CodeTable.getCodes("1286")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#allvalue}" value="" />
					</p>
					<p>
						<label style="width:90px"><@s.text name="mbcct.birthMonth"/></label>
						<select id="birthMonth" name="birthMonth">
							<option value=""><@s.text name="mbcct.allType"/></option>
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
						</select>
					</p>
					<p>
						<label style="width:90px"><@s.text name="mbcct.joinTime"/></label>
						<span><@s.textfield  id="joinTimeStart" name="joinTimeStart"  cssClass="date" style="width:90px"/></span>
						- 
						<span><@s.textfield  id="joinTimeEnd" name="joinTimeEnd" cssClass="date" style="width:90px"/></span>
					</p>
	                </div>
				</div>
				<p class="clearfix">
					<button class="right search"  onclick="BINOLMBCCT08.search();return false;">
		              	<span class="ui-icon icon-search-big"></span>
		              	<span class="button-text"><@s.text name="mbcct.search" /></span>
					</button>
				</p>
			</form>
		</div>
		<div id="customerDetail" class="hide">
			<div class="section-header">
				<strong>
		        	<span class="ui-icon icon-ttl-section-search-result"></span>
		        	<@s.text name="mbcct.showFindResult" />
		        </strong>
			</div>
			<div class="section-content">
				<div class="toolbar clearfix">
					<span class="left">
						<@cherry.show domId="MBCCT08EXPORT">
							<a href="${export_url}" id="exportExcel" class="export" onclick="BINOLMBCCT08.exportExcel(this,'0');return false;">
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><@s.text name="mbcct.exportExcelBtn" /></span>
							</a>
							<a href="${export_url}" id="exportCsv" class="export" onclick="BINOLMBCCT08.exportExcel(this,'1');return false;">
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><@s.text name="mbcct.exportCsvBtn" /></span>
							</a>
						</@cherry.show>
					</span>
					<span class="right">
						<a href="#" class="setting" id="customerDataTable_colSetting">
							<span class="button-text"><@s.text name="global.page.colSetting"/></span>
				 			<span class="ui-icon icon-setting"></span>
						</a>
					</span>
				</div>
				<table id="customerDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
				<thead>
					<tr>
						<th><@s.text name="mbcct.customerCode" /></th>
						<th><@s.text name="mbcct.customerName" /></th>
						<th><@s.text name="mbcct.gender" /></th>
						<th><@s.text name="mbcct.mobilePhone" /></th>
						<th><@s.text name="mbcct.telephone" /></th>
						<th><@s.text name="mbcct.birthDay" /></th>
						<th><@s.text name="mbcct.joinTime" /></th>
						<th><@s.text name="mbcct.customerType" /></th>
						<th><@s.text name="mbcct.company" /></th>
						<th><@s.text name="mbcct.post" /></th>
						<th><@s.text name="mbcct.industry" /></th>
						<th><@s.text name="mbcct.zip" /></th>
						<th><@s.text name="mbcct.messageId" /></th>
						<th><@s.text name="mbcct.email" /></th>
						<th><@s.text name="mbcct.province" /></th>
						<th><@s.text name="mbcct.city" /></th>
						<th><@s.text name="mbcct.address" /></th>
						<th><@s.text name="mbcct.memo" /></th>
						<th><@s.text name="mbcct.isReceiveMsg" /></th>
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
<#if (currentMenuID?? && currentMenuID == "BINOLMBCCT08") >
	<div class="hide" id="customerDialog"></div>
	<div class="hide" id="customerDialogTitle"><@s.text name="mbcct.customerDialogTitle" /></div>
</#if>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
<#include "/WEB-INF/ftl/common/popExportDialog.ftl">
</@s.i18n>