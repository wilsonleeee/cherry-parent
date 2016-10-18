<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM01.js"></script>

<script type="text/javascript">

$(document).ready(function() {

	var privilegeFlag = '<s:property value="#session.privilegeFlag"/>';

	bsemp01_empTreeInit();

	employeeBinding({
		elementId: "locationPosition",
		showNum: 20,
		privilegeFlag: privilegeFlag
	});

	$("#locationPosition").result(function(event, data, formatted){
		bsemp01_locateEmp(data[1]);
	});

} );

</script>

<s:i18n name="i18n.bs.BINOLBSEMP01">
<cherry:form id="employeeCherryForm"></cherry:form>
<div id="treeLayoutDiv" class="div-layout-all">
<div class="ui-layout-west">
<div style="min-width: 190px;">
  <div class="treebox2-header">
    <input id="locationPosition" class="text" type="text" name="locationPosition" style="width: 100px;margin-right: 0;vertical-align: middle;"/>
	<a class="search" onclick="bsemp01_locateEmp();return false;">
		<span class="ui-icon icon-position"></span>
		<span class="button-text">定位</span>
	</a>
  </div>
  <dl><dt class="jquery_tree" id="employeeTree"></dt></dl>
</div>
</div>
<div class="ui-layout-center">
<div style="min-width: 438px;" id="employeeInfo">
</div>
</div>
</div>
</s:i18n>

<div class="hide">
<s:url action="BINOLBSEMP01_next" id="employeeInitUrl"></s:url>
<a href="${employeeInitUrl }" id="employeeInitUrl"></a>
<s:url action="BINOLBSEMP02_init" id="employeeInfoUrl"></s:url>
<a href="${employeeInfoUrl }" id="employeeInfoUrl"></a>
<s:url action="BINOLBSEMP01_locateEmp" id="locateEmpUrl"></s:url>
<a href="${locateEmpUrl }" id="locateEmpUrl"></a>
</div>