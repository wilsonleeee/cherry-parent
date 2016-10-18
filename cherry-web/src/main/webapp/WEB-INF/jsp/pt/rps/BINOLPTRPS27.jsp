<%--进销存操作统计 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS27.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/cherry_timepicker.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<style>
    .box-header {
        padding: 0.2em 0;
    }
</style>

<s:i18n name="i18n.pt.BINOLPTRPS27">
<div class="hide">
	<%--查询URL --%>
	<s:url action="BINOLPTRPS27_search" id="searchUrl" />
	<a id="search_Url" href="${searchUrl }"></a>
	<%--统计参数URL --%>
	<s:url action="BINOLPTRPS27_initParameter"  id="initParameter" />
	<%--Excel导出URL --%>
	<s:url action="BINOLPTRPS27_export"  id="export_url" />
	<%--需要统计的部门 --%>
	<s:set id="departSize" value="departList.size()"/>
	<s:iterator value="departList" id="depart">
		<input type="hidden" name="depart" value="<s:property value="parameterData" />" />
	</s:iterator>
	<s:iterator value="dpParameterList" id="depart">
		<input type="hidden" name="depart" value="<s:property value="parameterData" />" />
	</s:iterator>
	<%--弹出窗属性 --%>
	<div id="dialogTitle"><s:text name="PTRPS27_dialogTitle"/></div>
	<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
	<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
	<s:set id="departSize" value="departList.size()"/>
	<%--计算表格列数 --%>
	<s:set id="totalBussinessTypeSize" value="0"/>
	<s:iterator id="fMap" value="bussinessParameterList">
		<s:if test='%{#fMap.validFlag == "1" && #fMap.parameterParent != "DPT"}'>
			<s:set id="totalBussinessTypeSize" value="%{#totalBussinessTypeSize+childrenListSize}"/>
		</s:if>
		<s:if test='%{#fMap.validFlag == "1" && #fMap.parameterParent == "DPT"}'>
			<s:set id="totalBussinessTypeSize" value="%{#totalBussinessTypeSize+childrenListSize*#departSize}"/>
		</s:if>
	</s:iterator>
	<input type="hidden" value="${totalBussinessTypeSize }" id="totalBussinessTypeSize">
</div>
<cherry:form id="mainForm">
	<s:text name="global.page.all" id="select_default"/>
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left">
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<span class="right"> 
				<%--当有参数设置的权限时，显示统计参数设置按钮 --%>
				<cherry:show domId="PTRPS27SET01"><s:set id="menu1" value="1"></s:set></cherry:show>
				<cherry:show domId="PTRPS27SET02"><s:set id="menu2" value="1"></s:set></cherry:show>
				<cherry:show domId="PTRPS27SET03"><s:set id="menu3" value="1"></s:set></cherry:show>
				<s:if test="%{#menu1 == 1 || #menu2 == 1 || #menu3 == 1}" >
					<a onclick="javascript:BINOLPTRPS27.popParameter(this);return false;" href="${initParameter }" class="add">
						<span class="button-text"><s:text name="PTRPS27_popDialog_btn"/></span>
						<span class="ui-icon icon-add"></span>
					</a>
				</s:if>
			</span>
		</div>
	</div>
	<div id="errorMessage"></div>
 	<div class="panel-content clearfix">
 		<div class="box">
 			<div class="box-header">
 				<hr class="space">
 				<strong>
 					<span class="ui-icon icon-ttl-search"></span>
 					<s:text name="global.page.condition"/>
 				</strong>
 			</div>
 			<div class="box-content clearfix">
 			<!-- 
 				<div class="column" style="width:50%; height: auto;">
 					<p>
 						<label><s:text name="PTRPS27_counter" /></label>
 						<span>
 							<s:textfield name="departName" cssClass="text" maxlength="25"></s:textfield>
 						</span>
 					</p>
 				</div>
 			 -->
 				<div class="column last" style="width:49%; height: auto;">
 					<p>
 						<label><s:text name="PTRPS27_date" /></label>
 						<span>
 							<input id="startTime" name="startTime" class="date"  value="<s:property value="lastStatisticDate" />">
 						</span> 
 						- 
 						<span>
 							<input id="endTime" name="endTime" class="date"  value="<s:property value="lastStatisticDate" />" >
 						</span>
 					</p>
 				</div>
 				<%-- ======================= 组织联动共通导入开始  ============================= --%>
	           	<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
		           	<s:param name="businessType">1</s:param>
		           	<s:param name="operationType">1</s:param>
	           	</s:action>
	           	<%-- ======================= 组织联动共通导入结束  ============================= --%>
 			</div>
 			<p class="clearfix">
		      <button class="right search" onclick="BINOLPTRPS27.search();return false;">
			      <span class="ui-icon icon-search-big"></span>
				      <span class="button-text">
				      <s:text name="global.page.search"></s:text>
			      </span>
		      </button>
		    </p>
 		</div>
 		<div class="section hide" id="resultInfo">
 			<div class="section-header">
 				<strong>
 					<span class="ui-icon icon-ttl-section-search-result"></span>
	           		<s:text name="global.page.list"/>
	            </strong>
	         </div>
	         <div class="section-content">
	         	<div class="toolbar clearfix">
	         		<cherry:show domId="PTRPS27EXPORT">
						<a  class="export left" onclick="javascript:BINOLPTRPS27.exportExcel(this);return false;"  href="${export_url}">
							<span class="ui-icon icon-export"></span>
							<span class="button-text"><s:text name="global.page.export"></s:text></span>
						</a>
					</cherry:show>
				</div>
         		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dataTable">
         			<thead>
         				<tr>
         					<!-- 基础数据 -->
         					<th rowspan="2" colspan="5" class="center">
         						<span class="highlight">
         							<s:if test='%{taskMap != null}'>
	         							<div>
		         							<s:text name="PTRPS27_statisticTime1" />
		         							<span id="taskRunTime"><s:property value="taskMap.taskCode" /></span>
		         						</div>
	         						</s:if>
	         						<s:if test='%{lastStatisticDate != null && lastStatisticDate != ""}'>
		         						<div>
	         								<s:text name="PTRPS27_statisticTime4" />
	         								<span id="lastStatisticDate"><s:property value="lastStatisticDate" /></span>
	         							</div>
         							</s:if>
         						</span>
         					</th>
         					<s:iterator id="fMap" value="bussinessParameterList">
         						<!-- 柜台、柜台主管、工作流 -->
         						<s:if test='%{#fMap.validFlag == "1" && #fMap.parameterParent != "DPT"}'>
         							<th colspan="<s:property value="#fMap.childrenListSize"/>" class="center">
         								<s:property value="#fMap.parameterName"/>
         							</th>
         						</s:if>   
         						<!-- 其他部门 -->   
         						<s:if test='%{#fMap.validFlag == "1" && #fMap.parameterParent == "DPT"}'>  	
         							<s:iterator value="departList" id="depart">
		         						<th colspan="<s:property value="#fMap.childrenListSize"/>" class="center">
		         							<s:if test='%{#application.CodeTable.getVal("1000",#depart.parameterData) == null || #application.CodeTable.getVal("1000",#depart.parameterData) =="" }'>
		         								<s:text name="PTRPS27_unknowDepartType" />
		         							</s:if>
		         							<s:else>
		         								<s:property value="#application.CodeTable.getVal('1000',#depart.parameterData) "/>
		         							</s:else>
		         						</th>
	         						</s:iterator>
         						</s:if>					
         					</s:iterator>
         				</tr>
         				<tr>
         					<!-- 基础数据 -->
         					<th class="hide"></th>
         					<th class="hide"></th>
         					<th class="hide"></th>
         					<th class="hide"></th>
         					<th class="hide"></th>
         					<s:iterator id="fMap" value="bussinessParameterList">
         						<!-- 柜台与柜台主管 -->
         						<s:if test='%{#fMap.validFlag == "1" && #fMap.parameterParent != "DPT"}'>
         							<s:iterator id="sMap" value="#fMap.childrenList" status="sStatus" >
         								<th colspan="<s:property value="#sMap.childrenListSize"/>" class="center <s:if test='#sStatus.index%2 == 0'>green2</s:if><s:if test='#sStatus.index%2 != 0'>red2</s:if>">
         									<s:property value="#sMap.parameterName"/>
         								</th>
         							</s:iterator>
         						</s:if> 
         						<!-- 其他部门 -->   
         						<s:if test='%{#fMap.validFlag == "1" && #fMap.parameterParent == "DPT"}'>  	
         							<s:iterator value="departList" id="depart">
		         						<s:iterator id="sMap" value="#fMap.childrenList" status="sStatus" >
	         								<th colspan="<s:property value="#sMap.childrenListSize"/>" class="center <s:if test='#sStatus.index%2 == 0'>green2</s:if><s:if test='#sStatus.index%2 != 0'>red2</s:if>">
	         									<s:property value="#sMap.parameterName"/>
	         								</th>
         								</s:iterator>
	         						</s:iterator>
         						</s:if>	
         					</s:iterator>
         				</tr>
         				<tr>
         					<!-- 基础数据 -->
         					<th><s:text name="No."  /></th>
	         				<th><s:text name="PTRPS27_date" /></th>
	         				<th><s:text name="PTRPS27_week" /></th>
	         				<th><s:text name="PTRPS27_depart" /></th>
	         				<th><s:text name="PTRPS27_departType" /></th>
         					<s:iterator id="fMap" value="bussinessParameterList">
         						<!-- 柜台与柜台主管 -->
         						<s:if test='%{#fMap.validFlag == "1" && #fMap.parameterParent != "DPT"}'>
         							<s:iterator id="sMap" value="#fMap.childrenList" status="sStatus" >
         								<s:iterator id="tMap" value="#sMap.childrenList" status="tStatus" >
         									<th  class="<s:if test='#sStatus.index%2 == 0'>green2</s:if><s:if test='#sStatus.index%2 != 0'>red2</s:if>">
         										<s:property value="#tMap.parameterName"/>
         									</th>
         								</s:iterator>
         							</s:iterator>
         						</s:if> 
         						<!-- 其他部门 -->   
         						<s:if test='%{#fMap.validFlag == "1" && #fMap.parameterParent == "DPT"}'>  	
         							<s:iterator value="departList" id="depart">
		         						<s:iterator id="sMap" value="#fMap.childrenList" status="sStatus" >
		         							<s:iterator id="tMap" value="#sMap.childrenList" status="tStatus" >
		         								<th class="<s:if test='#sStatus.index%2 == 0'>green2</s:if><s:if test='#sStatus.index%2 != 0'>red2</s:if>">
		         									<s:property value="#tMap.parameterName"/>
		         								</th>
	         								</s:iterator>
         								</s:iterator>
	         						</s:iterator>
         						</s:if>	
         					</s:iterator>
	         			</tr>
         			</thead>
         			<tbody>
         			</tbody>
         		</table>
	         </div>
 		</div>
 	</div>
</cherry:form>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<script>
var holidays = '${holidays }';
$('#startTime').cherryDate({
    holidayObj: holidays,
    beforeShow: function(input){
        var value = $('#endTime').val();
        return [value,'maxDate'];
    }
});
$('#endTime').cherryDate({
    holidayObj: holidays,
    beforeShow: function(input){
        var value = $('#startTime').val();
        return [value,'minDate'];
    }
});
</script>