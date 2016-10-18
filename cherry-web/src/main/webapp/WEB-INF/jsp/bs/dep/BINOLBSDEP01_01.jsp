<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM01.js"></script>

<script type="text/javascript">

$(document).ready(function() {

	bsdep01_orgTreeInit();

	departBinding({
		elementId: "locationPosition",
		includeCounter: true,
		showNum: 20,
		privilegeFlag: 1
	});

	$("#locationPosition").result(function(event, data, formatted){
		bsdep01_locateOrg(data[1]);
	});

} );

</script>

<s:i18n name="i18n.bs.BINOLBSDEP01">
<cherry:form id="organizationCherryForm"></cherry:form>

<div id="treeLayoutDiv" class="div-layout-all">
<div class="ui-layout-west">
<div style="min-width: 190px;">
  <div class="treebox2-header">
	<input id="locationPosition" class="text" type="text" name="locationPosition" style="width: 100px;margin-right: 0;vertical-align: middle;"/>
	<a class="search" onclick="bsdep01_locateOrg();return false;">
		<span class="ui-icon icon-position"></span>
		<span class="button-text">定位</span>
	</a>
  </div>
  <dl><dt class="jquery_tree" id="organizationTree"></dt></dl>
</div>
</div>
<div class="ui-layout-center">
<div style="min-width: 438px;" id="organizationInfo">
</div>
</div>
</div>
</s:i18n>

<div class="hide">
<s:url action="BINOLBSDEP01_next" id="organizationInitUrl"></s:url>
<a href="${organizationInitUrl }" id="organizationInitUrl"></a>
<s:url action="BINOLBSDEP02_init" id="organizationInfoUrl"></s:url>
<a href="${organizationInfoUrl }" id="organizationInfoUrl"></a>
<s:url action="BINOLBSDEP01_locateOrg" id="locateOrgUrl"></s:url>
<a href="${locateOrgUrl }" id="locateOrgUrl"></a>
</div>