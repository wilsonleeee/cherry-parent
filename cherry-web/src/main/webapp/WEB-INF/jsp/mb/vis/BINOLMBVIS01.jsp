<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/vis/BINOLMBVIS01.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$('#startDate').cherryDate();
	$('#endDate').cherryDate({
		beforeShow: function(input){
			var value = $('#startDate').val();
			return [value,'minDate'];
		}
	});
	
	$('#visitTimeStart').cherryDate();
	$('#visitTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#visitTimeStart').val();
			return [value,'minDate'];
		}
	});

});
</script>

<s:i18n name="i18n.mb.BINOLMBVIS01">
<s:text name="global.page.select" id="global.page.select"></s:text>
<s:text name="global.page.select" id="select_default"/>
    <div class="hide">
        <s:url id="details_url" value="/mb/BINOLMBVIS01_details"/>
        <a id="detailsurl" href="${details_url}"></a>
    </div>
    <div class="hide">
        <s:url id="taskDetailsurl_url" value="/mb/BINOLMBVIS01_taskDetails"/>
        <a id="taskDetailsurl" href="${taskDetailsurl_url}"></a>
    </div>
    <div class="hide">
        <s:url id="changePerformerConfirm_url" value="/mb/BINOLMBVIS01_changePerformerConfirm"/>
        <a id="changePerformerConfirmurl" href="${changePerformerConfirm_url}"></a>
    </div>
    <%-- EXCEL导出URL --%>
	<s:url id="xls_url" value="/mb/BINOLMBVIS01_export" />
<s:text name="global.page.select" id="taskDetailsurl_url"/>
      <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorMessage"></div>    
    <div style="display: none" id="errorMessageTemp">
    <div class="actionError">
       <ul><li><span><s:text name="binolmbvis01_deleteError" /></span></li></ul>         
    </div>
    </div>
     <div style="display: none" id="errorMessageTempTwo">
    <div class="actionError">
       <ul><li><span><s:text name="binolmbvis01_deleteErrorTwo" /></span></li></ul>         
    </div>
    </div>
    <div style="display: none" id="errorMessageTempTwos">
    <div class="actionError">
       <ul><li><span><s:text name="binolmbvis01_deleteErrorTwos" /></span></li></ul>         
    </div>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <jsp:include
					page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
	</span>
    </div>
</div>
<div class="panel-content clearfix">
<div class="box">
  <cherry:form id="VisitTaskInfoCherryForm" class="inline" onsubmit="binolmbvis01.search();return false;">
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
    </div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%; height: auto;">
        <p>
          <label style="width:60px;"><s:text name="binolmbvis01_memCode" /></label>
          <s:textfield name="memberCode" cssClass="text"></s:textfield>
        </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbvis01_counterNameIF" /></label>
          <s:hidden name="counterCode"/>
          <s:textfield name="counterCodeName" cssClass="text" maxlength="30"/>
       </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbvis01_visitType" /></label>
          <s:select list='#application.CodeTable.getCodes("1208")' listKey="CodeKey" listValue="Value" name="visitType" headerKey="" headerValue="%{#select_default}"></s:select>
        </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbvis01_BAName" /></label>
          <s:hidden name="employeeID"/>
          <s:textfield name="employeeName" cssClass="text" maxlength="30"/>
       </p>
      </div>
      <div class="column last" style="width:49%; height: auto;">
        <p>
          <label style="width:60px;"><s:text name="binolmbvis01_changeDate" /></label>
          <s:textfield name="startDate" cssClass="date"></s:textfield>-<s:textfield name="endDate" cssClass="date"></s:textfield>
        </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbvis01_taskVisitTime" /></label>
          <s:textfield name="visitTimeStart" cssClass="date"></s:textfield>-<s:textfield name="visitTimeEnd" cssClass="date"></s:textfield>
        </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbvis01_birthDay" /></label>
          <s:select list="monthList" name="birthDayMonth" headerKey="" headerValue="%{global.page.select}" cssStyle="width:auto;"></s:select><s:text name="binolmbvis01_month" />
          <s:select list="dateList" name="birthDayDate" headerKey="" headerValue="%{global.page.select}" cssStyle="width:auto;"></s:select><s:text name="binolmbvis01_date" />
        </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbvis01_visitResult" /></label>
          <s:select list='#application.CodeTable.getCodes("1209")' listKey="CodeKey" listValue="Value" name="visitResult" headerKey="" headerValue="%{#select_default}"></s:select>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" type="button" onclick="binolmbvis01.search();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
    </p>
  </cherry:form>
</div>
<div class="section hide" id="pointInfo">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
  <div class="section-content">
  <div class="toolbar clearfix">
  <cherry:show domId="BINOLMBVIS01CAN">
    <span class="left">    
        <s:url action="BINOLMBVIS01_cancelTask" id="cancelUrl"></s:url>
          <a class="delete" onclick="binolmbvis01.cancelTask('${cancelUrl}');return false;">
              <span class="ui-icon icon-disable"></span>
              <span class="button-text"><s:text name="binolmbvis01_cancel"/></span>
          </a>      
    </span>
  </cherry:show>
  <cherry:show domId="BINOLMBVIS01PER">
     <span class="left">    
        <s:url action="BINOLMBVIS01_changePerformer" id="changePerformerUrl"></s:url>
          <a class="add" onclick="binolmbvis01.changePerformer('${changePerformerUrl}');return false;">
              <span class="ui-icon icon-enable"></span>
              <span class="button-text"><s:text name="binolmbvis01_changePerformer"/></span>
          </a>      
    </span>
   </cherry:show>
   <cherry:show domId="BINOLMBVIS01PEX">
	<span style="margin-right: 10px;"> <a id="export"
		class="export"
		onclick="binolmbvis01.exportExcel('${xls_url}');return false;">
			<span class="ui-icon icon-export"></span> <span
			class="button-text"><s:text name="global.page.exportExcel" /></span>
	</a>
	</span>
	</cherry:show>
  	<span id="headInfo" style="">
  	</span> <span class="right">
  	<a class="setting"><span class="ui-icon icon-setting">
  	</span> <span class="button-text"> <%-- 设置列 --%> 
  	<s:text name="binolmbvis01_colSetting" />
	</span></a></span>
  </div>
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dataTable">
      <thead>
        <tr>
          <th><input type="checkbox"  name="allSelect" id="allSelect" onclick="checkSelectAll(this)"/></th><%-- 选择 --%>
          <th><s:text name="binolmbvis01_visitType"></s:text></th>
          <th><s:text name="binolmbvis01_memberName"></s:text></th>
          <th><s:text name="binolmbvis01_birthDay"></s:text></th>
          <th><s:text name="binolmbvis01_memCode"></s:text></th>
          <th><s:text name="binolmbvis01_counterNameIF"></s:text></th>
          <th><s:text name="binolmbvis01_sData"></s:text></th>
          <th><s:text name="binolmbvis01_eData" /></th>
          <th><s:text name="binolmbvis01_BAName"></s:text></th>
          <th><s:text name="binolmbvis01_taskState"></s:text></th>
          <th><s:text name="binolmbvis01_visitTime"></s:text></th>
          <th><s:text name="binolmbvis01_visitResult"></s:text></th>
          <th><s:text name="binolmbvis01_operate"></s:text></th>
        </tr>
      </thead>
    </table>
  </div>
</div>
</div>
<div class="hide" id="dialogInit">

</div>
	<div id="confirm"  class="hide">
		<s:text name="binolmbvis01_affirm" />
	</div>
	<div id="cancel"  class="hide">
		<s:text name="binolmbvis01_cancels" />
	</div>
	<div id="detailsTitle"  class="hide">
		<s:text name="binolmbvis01_detailsTitle" />
	</div>
	<div id="taskDetailsTitle"  class="hide">
		<s:text name="binolmbvis01_taskDetailsTitle" />
	</div>
	<div id="cancelTaskConfirm"  class="hide">
		<span><s:text name="binolmbvis01_cancel" /></span>
	</div>
	<div id="cancelConfirm"  class="hide">
		<p class="message"><span><s:text name="binolmbvis01_cancelTaskConfirm" /></span>
	</div>
	<div id="changePerformerTitle"  class="hide">
		<s:text name="binolmbvis01_changePerformer" />
	</div>
	<div id="changePerformerConfirm"  class="hide">
		<p class="message"><span><s:text name="binolmbvis01_changePerformerConfirm" /></span>
	</div>
</s:i18n>  

<div class="hide">
<s:url action="BINOLMBVIS01_search" id="VisitTaskListUrl"></s:url>
<a href="${VisitTaskListUrl }" id="VisitTaskListUrl"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== 弹出datatable共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popCounterTable.jsp" flush="true" />
<%-- ================== 弹出datatable共通END ======================= --%>
