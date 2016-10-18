<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/WEB-INF/cherrytld/cherrytags.tld"%>
<%--初始时间参数画面 --%>
<s:url action="BINOLPTRPS27_initPopSchedules"  id="initPopSchedules" />
<%--初始业务参数画面 --%>
<s:url action="BINOLPTRPS27_initBussiness"  id="initBussiness" />
<%--初始部门参数画面 --%>
<s:url action="BINOLPTRPS27_initPopDepartTree"  id="initPopDepartTree" />
<div id="loading" class="hide"><s:text name="table_sProcessing"></s:text> </div>
<s:i18n name="i18n.pt.BINOLPTRPS27">
	 <div id="actionResultDisplay"></div>
	<div id="errorMessage"></div>
	<form id="parameterForm">
		<div class="sidemenu2 left" style="width: 140px;">
			<div class="sidemenu2-header">
				<strong><s:text name="global.page.select" /></strong>
			</div>
			<ul class="u1" id="parameterMenu">
				<cherry:show domId="PTRPS27SET03">
					<li id="bt" onclick="selectSubSys(this,'${initBussiness}','bt')" class="on"><s:text name="PTRPS27_popDialog_bt" /></li>
				</cherry:show>
				<cherry:show domId="PTRPS27SET02">
					<li id="dp" onclick="selectSubSys(this,'${initPopDepartTree}','dp')"><s:text name="PTRPS27_popDialog_dp" /></li>
				</cherry:show>
				<cherry:show domId="PTRPS27SET01">
					<li id="st" onclick="selectSubSys(this,'${initPopSchedules}','st')"><s:text name="PTRPS27_popDialog_st" /></li>
				</cherry:show>
			</ul>
		</div>
		<div class="left" style="width: 78%">
			<div>
				<div class="group-header clearfix">
					<strong id="parameterTitle"></strong>
				</div>
				<div class="group-content clearfix" id="parameterContent">
				
				</div>
			</div>
		</div>
	</form>
</s:i18n>
<script type="text/javascript">
	$(document).ready(function(){
		$('#parameterMenu > li').first().click();
	});

</script>



