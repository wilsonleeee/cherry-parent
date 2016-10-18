<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM24_1.js"></script>
<s:i18n name="i18n.mb.BINOLMBMBM24">
<s:url id="Excel_url" value="BINOLMBMBM24_export"/>
<s:text name="global.page.all" id="select_default"/>
<div class="panel ui-corner-all">
<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="binolmbmbm24_importDetail"></s:text>(<s:text name="binolmbmbm24_billNo"></s:text>：<s:property value="billNo"/>)</span> 
    </div>
</div>
 <div id="errorMessage"></div>
 <div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="box">
  <form id="detailMainForm"  class="inline" onsubmit="BINOLMBMBM24_1.search();return false;">
     <s:hidden name="memImportId"></s:hidden>
     <s:hidden name="billNo"></s:hidden>
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
    </div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%; height: auto;">
        <p>
          <label style="width:80px;"><s:text name="binolmbmbm24_importRes" /></label>
          <s:select list='#application.CodeTable.getCodes("1250")' listKey="CodeKey" listValue="Value" id="resultFlag" name="resultFlag"  onchange="BINOLMBMBM24_1.search();return false;"
										headerKey="" cssStyle="width:120px;" headerValue="%{#select_default}"></s:select>
        </p>
      </div>
      <div class="column last" style="width:49%; height: auto;">
        <p>
        	<label><s:text name="binolmbmbm24_memCode"/></label>
            <span><input type="text"  class="text" id="memberCode" value="" maxlength="40" name="memberCode"></span>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" onclick="BINOLMBMBM24_1.search();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
    </p>
  </form>
</div>
<div class="section hide" id="detailInfo">
<div class="section-header">
          <strong>
          	<span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 查询结果一览 --%>
          	<s:text name="binolmbmbm24_resultShow"/>
         </strong>
         <span id="headInfo"  style="margin: 0px 20px;" ></span>
        </div>
          <div class="section-content" id="result_list">
            <div class="toolbar clearfix">
               <cherry:show domId="MBMBM24EXP">
					<a id="exportPointInfo" class="export left" onclick="BINOLMBMBM24_1.exportExcel('${Excel_url}');return false;">
					     <span class="ui-icon icon-export"></span>
					     <span class="button-text"><s:text name="binolmbmbm24_excelExport"/></span>
				 	</a>
			 	</cherry:show>
              	<span class="right">
              	<a class="setting"><span class="ui-icon icon-setting"></span>
              	<span class="button-text">
              	<%-- 设置列 --%>
              	<s:text name="binolmbmbm24_column"/></span></a></span>
           </div>
	   <table  id="detailTable">
	      <thead>
		        <tr>
			          <th><s:text name="NO."></s:text></th>
			          <th><s:text name="binolmbmbm24_memCode"></s:text></th>
			          <th><s:text name="binolmbmbm24_memName"></s:text></th>
			          <th><s:text name="binolmbmbm24_memLevel"></s:text></th>
			          <th><s:text name="binolmbmbm24_joinDate"></s:text></th>
			          <th><s:text name="binolmbmbm24_curBtimes"></s:text></th>
			          <th><s:text name="binolmbmbm24_resultFlag"></s:text></th>
			          <th><s:text name="binolmbmbm24_common"></s:text></th>
		        </tr>
	      </thead>
	      <tbody>
	      </tbody>
	   </table>
  </div>
</div>
</div>
</div>
</s:i18n>  
<div class="hide">
<s:url action="BINOLMBMBM24_detailSearch" id="detailSearchUrl"></s:url>
<a href="${detailSearchUrl}" id="detailSearch_Url"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />