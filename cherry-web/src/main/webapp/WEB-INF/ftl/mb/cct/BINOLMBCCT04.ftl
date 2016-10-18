<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<script type="text/javascript" src="/Cherry/js/mb/cct/BINOLMBCCT04.js"></script>
<@s.i18n name="i18n.mb.BINOLMBCCT04">
<div class="clearfix" id="memberListDiv">
	<div id="memberList" class="section">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="mbctt.memberSearchTitle" />
	        </strong>
	        <span class="right">
		        <@s.text name="mbctt.notMember" />
		        <button id="addCustomerBtn" class="edit"  onclick="BINOLMBCCT04.addCustomerInfo();return false;">
		          	<span class="ui-icon icon-add"></span>
		          	<span class="button-text"><@s.text name="mbctt.addCustomerInfo" /></span>
				</button>
			</span>
		</div>
		<div class="section-content">
			<input type="hidden" id="searchEvent" name="searchEvent" value="S"/>
			<table id="memberDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th><@s.text name="mbctt.memCode" /></th>
					<th><@s.text name="mbctt.memName" /></th>
					<th><@s.text name="mbctt.gender" /></th>
					<th><@s.text name="mbctt.mobilePhone" /></th>
					<th><@s.text name="mbctt.counterName" /></th>
					<th><@s.text name="mbctt.birthDay" /></th>
					<th><@s.text name="mbctt.joinDate" /></th>
					<th><@s.text name="mbctt.cardValidFlag" /></th>
					<th><@s.text name="mbctt.act" /></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			</table>
		</div>
	</div>
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>