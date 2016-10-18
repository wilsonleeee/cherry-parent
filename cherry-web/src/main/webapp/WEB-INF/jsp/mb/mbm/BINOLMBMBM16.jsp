<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript">
	// 提示框初期化
	$(function () {
		searchLevel();
	});
	function searchLevel() {
		var url = $("#searchLevelUrl").attr("href");
		var param = $("#memberInfoId").serialize();
		if ($("#memberClubId").length > 0) {
			param +=  '&' + $("#memberClubId").serialize();
		}
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				$('#levelBody').html(msg);
			}
		});
	}
</script>
<style type="text/css">
th {
	background: #eee;
}

.commons {
	color: #3366FF;
}

.commons:hover {
	color: #ff6d06;
	text-decoration: underline;
}
</style>
<s:i18n name="i18n.mb.BINOLMBMBM16">

	<div class="crm_content_header">
		<span class="icon_crmnav"></span> <span><s:text name="binolmbmbm16_memHistory" /></span>
	</div>
	<div class="panel-content">
		<div class="section">
			<div class="section-header">
				<strong><span class="ui-icon icon-ttl-section-list"></span>
					<%-- 历史记录--%> <s:text name="binolmbmbm16_changeRecord" /></strong><span class="red"><s:text name="binolmbmbm16_includeHistory" /></span>
					<s:if test='%{!"3".equals(clubMod)}'>
						<s:if test='%{clubList != null && clubList.size() != 0}'>
						<s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="searchLevel();return false;" Cssstyle="width:150px;"/>
					  </s:if>
					 </s:if>
					 <input type = "hidden" name="memberInfoId" id="memberInfoId" value='<s:property value="memberInfoId" />'/>
			</div>
			<div class="section-content" style="width: 100%; overflow-x: auto;">
				<table width="100%" cellspacing="0" cellpadding="0" border="0"
					class="jquery_table" id="dataTable">
					<thead>
						<tr>
							<th width="15%" class="center" rowspan="2">
							<strong><s:text name="binolmbmbm16_changeTime" /></strong><%--等级变化时间 --%></th>
							<th width="10%" class="center" colspan="2">
							<strong><s:text name="binolmbmbm16_memBerLevel" /></strong><%-- 会员等级 --%></th>
							<th width="5%" class="center" rowspan="2">
							<strong><s:text name="binolmbmbm16_changeType" /></strong><%-- 变化类型 --%></th>
							<th width="15%" class="center" rowspan="2"><strong>
							<s:text name="binolmbmbm16_billNo"/></strong><%-- 关联单号--%></th>
							<th width="5%" class="center" rowspan="2">
							<strong><s:text name="binolmbmbm16_tradeType" /></strong><%-- 业务类型--%></th>
							<th width="10%" class="center" rowspan="2">
							<strong><s:text name="binolmbmbm16_rule" /></strong><%-- 匹配规则--%>
							<th width="10%" class="center" rowspan="2"><strong>
							<s:text name="binolmbmbm16_reason" /></strong><%-- 变化原因 --%></th>
						</tr>
						<tr>
							<th class="center"><s:text name="binolmbmbm16_beforeLevel" /></th>
							<%-- 修改前 --%>
							<th class="center"><span><s:text name="binolmbmbm16_afterLevel" /></span></th>
							<%-- 修改后 --%>
						</tr>
					</thead>
					<tbody id="levelBody">
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
	 <div class="hide">
	<s:url action="BINOLMBMBM16_searchLevel" id="searchLevel_Url"></s:url>
	<a href="${searchLevel_Url}" id="searchLevelUrl"></a>
</div>
</s:i18n>