<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<@s.i18n name="i18n.ct.BINOLCTRPT05">
	<@s.url id="export_url" action="BINOLCTRPT05_export"/>
	<div class="hide">
		<@s.url id="searchSaleDetail_url" action="BINOLCTRPT05_searchSaleDetail"/>
		<a id="searchSaleDetailUrl" href="${searchSaleDetail_url}"></a>
	</div>
	<input class="hide" id="organizationId" value="${(organizationId)!?html}"/>
	<div class="toolbar clearfix">
		<span>
			<a  class="export" onclick="javascript:BINOLCTRPT05.exportExcel(this,'5');return false;"  href="${export_url }">
				<span class="ui-icon icon-export"></span>
				<span class="button-text"><@s.text name="ctrpt.exportSaleEXCELBtn" /></span>
			</a>
		</span>
		<span>
			<a  class="export" onclick="javascript:BINOLCTRPT05.exportExcel(this,'6');return false;"  href="${export_url }">
				<span class="ui-icon icon-export"></span>
				<span class="button-text"><@s.text name="ctrpt.exportSaleCSVBtn" /></span>
			</a>
		</span>
		<span id="saleTotalInfoSpan" style="margin-left:10px">
			<@s.text name="ctrpt.joinTotal" />
			<strong id="joinTotalNumber"  class="green"></strong>
			<@s.text name="ctrpt.amountTotal" />
			<strong id="saleAmountTotal"  class="green"></strong>
			<@s.text name="ctrpt.quantityTotal" />
			<strong id="saleQuantityTotal"  class="green"></strong>
		</span>
	</div>
	<table id="saleDetailTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<th><@s.text name="ctrpt.rowNumber" /></th>
				<th><@s.text name="ctrpt.memCode" /></th>
				<th><@s.text name="ctrpt.memName" /></th>
				<th><@s.text name="ctrpt.mobilePhone" /></th>
				<th><@s.text name="ctrpt.billCode" /></th>
				<th><@s.text name="ctrpt.amount" /></th>
				<th><@s.text name="ctrpt.quantity" /></th>
				<th><@s.text name="ctrpt.saleDate" /></th>
			</tr>
		<thead>
		<tbody>
		</tbody>
	</table>
</@s.i18n>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">