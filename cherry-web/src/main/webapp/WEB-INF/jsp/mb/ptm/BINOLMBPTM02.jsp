<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/ptm/BINOLMBPTM02.js"></script>
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

<s:i18n name="i18n.mb.BINOLMBPTM02">
<s:text name="global.page.select" id="select_default"/>
<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="binolmbpmt02_pointManage"></s:text>&nbsp;&gt;&nbsp;<s:text name="binolmbpmt02_pointInfoList"></s:text> </span> 
    </div>
</div>
<div class="panel-content clearfix">
<div class="box">
  <cherry:form id="pointInfoCherryForm" class="inline" onsubmit="binolmbptm02.searchPointInfo();return false;">
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
    </div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%; height: auto;">
      	<p>
          <label style="width:60px;"><s:text name="binolmbpmt02_memClub" /></label>
          <s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" headerKey="" headerValue="%{#select_default}" Cssstyle="width:150px;"/>
        </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbptm02_memCode" /></label>
          <s:textfield name="memCode" cssClass="text"></s:textfield>
        </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbptm02_mobilePhone" /></label>
          <s:textfield name="mobilePhone" cssClass="text"></s:textfield>
        </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbptm02_departCode" /></label>
          <s:textfield name="departCode" cssClass="text"></s:textfield>
        </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbptm02_prtVendorId" /></label>
          <s:textfield name="nameTotal" cssClass="text" maxlength="30"/>
		  <input type="hidden" id="prtVendorId" name="prtVendorId" value="" />
        </p>
        <p>
          <label style="width:60px;"><s:text name="binolmbptm02_billCode" /></label>
          <s:textfield name="billCode" cssClass="text"></s:textfield>
        </p>
      </div>
      <div class="column last" style="width:49%; height: auto;">
        <p>
          <label style="width:80px;"><s:text name="binolmbptm02_changeDate" /></label>
          <span><s:textfield name="changeDateStart" cssClass="date"></s:textfield></span><span>-<s:textfield name="changeDateEnd" cssClass="date"></s:textfield></span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="binolmbptm02_memPoint" /></label>
          <span><s:textfield name="memPointStart" cssClass="text" cssStyle="width:75px"></s:textfield></span><span>-<s:textfield name="memPointEnd" cssClass="text" cssStyle="width:75px"></s:textfield></span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="binolmbptm02_tradeType" /></label>
          <s:select list='#application.CodeTable.getCodes("1213")' listKey="CodeKey" listValue="Value" name="tradeType" headerKey="" headerValue="%{#select_default}"></s:select>
        </p>
        <p>
          <label style="width:80px;"><s:text name="binolmbptm02_campaignName" /></label>
          <s:select list="campaignNameList" name="subCampaignId" listKey="subCampaignId" listValue="campaignName" headerKey="" headerValue="%{#select_default}"></s:select>
        </p>
        <p>
          <label style="width:80px;"><s:text name="binolmbptm02_relevantSRCode" /></label>
          <s:textfield name="relevantSRCode" cssClass="text"></s:textfield>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" onclick="binolmbptm02.searchPointInfo();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
    </p>
  </cherry:form>
</div>
<div class="section hide" id="pointInfo">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
  <div class="section-content">
  	<div class="toolbar clearfix">
		<cherry:show domId="BINOLMBPTM02EXP">
			<span style="margin-right:10px;">
				<a id="export" class="export" onclick="binolmbptm02.exportExcel();return false;">
				<span class="ui-icon icon-export"></span>
				<span class="button-text"><s:text name="global.page.exportExcel"/></span>
				</a>
				<a id="export" class="export" onclick="binolmbptm02.exportCsv();return false;">
				<span class="ui-icon icon-export"></span>
				<span class="button-text"><s:text name="global.page.exportCsv"/></span>
				</a>
			</span>
		</cherry:show>
  	</div>
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="pointInfoDataTable">
      <thead>
        <tr>
          <th><s:text name="binolmbptm02_number"></s:text></th>
          <th><s:text name="binolmbptm02_memCode"></s:text></th>
          <th><s:text name="binolmbptm02_departCode"></s:text></th>
          <th><s:text name="binolmbptm02_billCode" /></th>
          <th><s:text name="binolmbptm02_changeDate"></s:text></th>
          <th><s:text name="binolmbptm02_amount"></s:text></th>
          <th><s:text name="binolmbptm02_quantity"></s:text></th>
          <th><s:text name="binolmbptm02_point"></s:text></th>
          <th><s:text name="binolmbptm02_operate"></s:text></th>
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
<s:url action="BINOLMBPTM02_search" id="pointInfoListUrl"></s:url>
<a href="${pointInfoListUrl }" id="pointInfoListUrl"></a>
<s:url id="exportUrl" action="BINOLMBPTM02_export" ></s:url>
<a id="exportUrl" href="${exportUrl}"></a>
<s:url id="exportCsvUrl" action="BINOLMBPTM02_exportCsv" ></s:url>
<a id="exportCsvUrl" href="${exportCsvUrl}"></a>
<s:url id="exporChecktUrl" action="BINOLMBPTM02_exportCheck" ></s:url>
<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== 弹出datatable共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popCounterTable.jsp" flush="true" />
<%-- ================== 弹出datatable共通END ======================= --%>
<%-- ================== Csv导出弹出框共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<%-- ================== Csv导出弹出框共通导入  END ======================= --%>