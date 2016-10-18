<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<@s.i18n name="i18n.ct.BINOLCTRPT05">
	<@s.url id="export_url" action="BINOLCTRPT05_export"/>
	<div class="hide">
		<@s.url id="searchJoinDetail_url" action="BINOLCTRPT05_searchJoinDetail"/>
		<a id="searchJoinDetailUrl" href="${searchJoinDetail_url}"></a>
	</div>
	<input class="hide" id="organizationId" value="${(organizationId)!?html}"/>
	<div class="toolbar clearfix">
		<span>
			<a  class="export left" onclick="javascript:BINOLCTRPT05.exportExcel(this,'3');return false;"  href="${export_url }">
				<span class="ui-icon icon-export"></span>
				<span class="button-text"><@s.text name="ctrpt.exportJoinEXCELBtn" /></span>
			</a>
		</span>
		<span>
			<a  class="export left" onclick="javascript:BINOLCTRPT05.exportExcel(this,'4');return false;"  href="${export_url }">
				<span class="ui-icon icon-export"></span>
				<span class="button-text"><@s.text name="ctrpt.exportJoinCSVBtn" /></span>
			</a>
		</span>
	</div>
	<table id="joinDetailTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<th><@s.text name="ctrpt.rowNumber" /></th>
				<th><@s.text name="ctrpt.memCode" /></th>
				<th><@s.text name="ctrpt.memName" /></th>
				<th><@s.text name="ctrpt.mobilePhone" /></th>
				<th><@s.text name="ctrpt.billQuantity" /></th>
				<th><@s.text name="ctrpt.amount" /></th>
				<th><@s.text name="ctrpt.quantity" /></th>
			</tr>
		<thead>
		<tbody>
		</tbody>
	</table>
</@s.i18n>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">