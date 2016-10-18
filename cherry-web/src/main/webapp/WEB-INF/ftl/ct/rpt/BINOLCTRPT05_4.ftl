<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<@s.i18n name="i18n.ct.BINOLCTRPT05">
	<div class="hide">
		<@s.url id="searchSendDetail_url" action="BINOLCTRPT05_searchSendDetail"/>
		<a id="searchSendDetailUrl" href="${searchSendDetail_url}"></a>
	</div>
	<input class="hide" id="organizationId" value="${(organizationId)!?html}"/>
	<table id="sendDetailTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<th><@s.text name="ctrpt.rowNumber" /></th>
				<th><@s.text name="ctrpt.memCode" /></th>
				<th><@s.text name="ctrpt.memName" /></th>
				<th><@s.text name="ctrpt.mobilePhone" /></th>
				<th><@s.text name="ctrpt.message" /></th>
				<th><@s.text name="ctrpt.sendTime" /></th>
				<th><@s.text name="ctrpt.coupon" /></th>
			</tr>
		<thead>
		<tbody>
		</tbody>
	</table>
</@s.i18n>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">