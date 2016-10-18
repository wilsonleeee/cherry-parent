<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/ptm/BINOLMBPTM06.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#changeDateStart').cherryDate();
	$('#changeDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#changeDateStart').val();
			return [value,'minDate'];
		}
	});
});
</script>

<s:i18n name="i18n.mb.BINOLMBPTM06">
<s:text name="global.page.select" id="select_default"/>
<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="JNPOINT"></s:text>&nbsp;&gt;&nbsp;<s:text name="BINOLMBPTM06"></s:text> </span> 
    </div>
</div>
<div class="panel-content clearfix">
<div class="box">
  <cherry:form id="reportInfoCherryForm" class="inline" onsubmit="BINOLMBPTM06.searchReportInfo();return false;">
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
    </div>
    <div class="box-content clearfix">
      <div class="column last" style="width:99%; height: auto;">      	
        <p>
          <label style="width:80px;"><s:text name="BINOLMBPTM06_scanDate" /></label>
          <span><s:textfield name="startDate" cssClass="date"></s:textfield></span><span>-<s:textfield name="endDate" cssClass="date"></s:textfield></span>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" onclick="binolmbptm06.searchReportSummyInfo();binolmbptm06.searchReportDetailInfo();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
    </p>
  </cherry:form>
</div>
<%-- 报表汇总 --%>
<div class="section" id='reportSummyInfo'></div>    
<%-- 报表明细 --%>
<div class="section hide" id="reportDetailInfo">
  <div class="section-header">
  	<strong>
     <span class="ui-icon icon-ttl-section-search-result"></span>
     <s:text name="global.page.reportDetail"></s:text>
    </strong>
  </div>
  <div class="section-content">
  	<div class="toolbar clearfix">
		<cherry:show domId="BINOLMBPTM06EXP">
			<span style="margin-right:10px;">
				<a id="export" class="export" onclick="binolmbptm06.exportExcel();return false;">
				<span class="ui-icon icon-export"></span>
				<span class="button-text"><s:text name="global.page.exportExcel"/></span>
				</a>
			</span>
		</cherry:show>
  	</div>
  	<span class="right">
           		<%-- 列设置按钮  --%>
           		<a href="#" class="setting">
           			<span class="button-text"><s:text name="global.page.colSetting"/></span>
    		 		<span class="ui-icon icon-setting"></span>
    		 	</a>
           	</span>
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="reportDetailDataTable">
      <thead>
        <tr>
        	<!-- No. -->	
          <th><s:text name="BINOLMBPTM06_no">No.</s:text></th>
        	<!-- 会员卡号 -->	
          <th><s:text name="BINOLMBPTM06_memberCode"></s:text></th>
        	<!-- 姓名 -->	
          <th><s:text name="BINOLMBPTM06_memberName"></s:text></th>
        	<!-- 手机号码 -->	
          <th><s:text name="BINOLMBPTM06_mobile"></s:text></th>
        	<!-- 发卡时间 -->	
          <th><s:text name="BINOLMBPTM06_grantCardTime"></s:text></th>
        	<!-- 奖励积分 -->	
          <th><s:text name="BINOLMBPTM06_grantPoints"></s:text></th>
        	<!-- 奖励时间 -->	
          <th><s:text name="BINOLMBPTM06_grantPointTime"></s:text></th>
        	<!-- 新老会员区分 -->	
          <th><s:text name="BINOLMBPTM06_newOldMemberFlag"></s:text></th>
        	<%-- 所属大区 --%>
          <th><s:text name="BINOLMBPTM06_region"/></th>
          <%-- 所属省份 --%>
          <th><s:text name="BINOLMBPTM06_province"/></th>
          <%-- 所属城市 --%>
          <th><s:text name="BINOLMBPTM06_city"/></th>
          <%-- 柜台号 --%>
          <th><s:text name="BINOLMBPTM06_counterCode"/></th>
          <%-- 柜台名称 --%>
          <th><s:text name="BINOLMBPTM06_counterName"/></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>
</div>
</s:i18n>  

<div class="hide">
<s:url action="BINOLMBPTM06_searchReportSummy" id="reportSummyInfoUrl"></s:url>
<a href="${reportSummyInfoUrl }" id="reportSummyInfoUrl"></a>
<s:url action="BINOLMBPTM06_searchReportDetailList" id="reportDetailInfoListUrl"></s:url>
<a href="${reportDetailInfoListUrl }" id="reportDetailInfoListUrl"></a>
<s:url id="exportUrl" action="BINOLMBPTM06_export" ></s:url>
<a id="exportUrl" href="${exportUrl}"></a>
<s:url id="exporChecktUrl" action="BINOLMBPTM06_exportCheck" ></s:url>
<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== 弹出datatable共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popCounterTable.jsp" flush="true" />
<%-- ================== 弹出datatable共通END ======================= --%>
<%-- ================== Csv导出弹出框共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<%-- ================== Csv导出弹出框共通导入  END ======================= --%>