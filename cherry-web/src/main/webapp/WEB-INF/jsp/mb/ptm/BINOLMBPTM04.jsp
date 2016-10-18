<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/ptm/BINOLMBPTM04.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<%-- 积分批量导入URL --%>
<s:url id="importInit_url" value="/mb/BINOLMBPTM05_importInit"/>
<s:i18n name="i18n.mb.BINOLMBPTM04">
<s:text name="global.page.all" id="select_default"/>
<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="binolmbptm04_title"></s:text>&nbsp;&gt;&nbsp;<s:text name="binolmbptm04_importPoint"></s:text> </span> 
   	<span class="right"> 
   	  <cherry:show domId="MBPTM04IMPORT">
	    <a onclick="javascript:openWin(this);return false;" class="add"  href="${importInit_url}">
		 		<span class="button-text"><s:text name="binolmbptm04_importExcel_title"></s:text></span>
		 		<span class="ui-icon icon-add"></span>
	    </a>
	   </cherry:show> 
     </span>
    </div>
</div>
 <div id="errorMessage"></div>
 <div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="box">
  <cherry:form id="mainForm"  class="inline">
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
    </div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%; height: auto;">
        <p>
          <label style="width:80px;"><s:text name="binolmbptm04_pointType" /></label>
          <s:select list='#application.CodeTable.getCodes("1214")' listKey="CodeKey" listValue="Value" id="pointType" name="pointType"  
										headerKey=""  cssStyle="width:120px;" headerValue="%{#select_default}"></s:select>
        </p>
        <p>
          <label style="width:80px;"><s:text name="binolmbptm04_importName" /></label>
            <span>
                  <s:textfield name="importBatchName" cssClass="text" maxlength="30"></s:textfield>
			</span>
        </p>
      </div>
      <div class="column last" style="width:49%; height: auto;">
        <p>
        	<label><s:text name="binolmbptm04_date"/></label>
            <span><s:textfield id="startTime" name="startTime" cssClass="date"/></span> - <span><s:textfield id="endTime" name="endTime" cssClass="date"/></span>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" onclick="BINOLMBPTM04.search();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
    </p>
  </cherry:form>
</div>
<div class="section hide" id="pointInfo">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
  <div class="section-content">
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="pointDataTable">
      <thead>
        <tr>
          <th><s:text name="NO."></s:text></th>
          <th><s:text name="binolmbptm04_pointNo"></s:text></th>
          <th><s:text name="binolmbptm04_importName"></s:text></th>
          <th><s:text name="binolmbptm04_pointType"></s:text></th>
          <th><s:text name="binolmbptm04_importMemRepeat" /></th>
          <th><s:text name="binolmbptm04_importTime"></s:text></th>
          <th><s:text name="binolmbptm04_operator"></s:text></th>
          <th><s:text name="binolmbptm04_reason"></s:text></th>
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
<s:url action="BINOLMBPTM04_search" id="searchUrl"></s:url>
<a href="${searchUrl}" id="search_Url"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
 <script type="text/javascript">
        // 节日
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