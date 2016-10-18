<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/mb/rpt/BINOLMBRPT04.js"></script>

<s:i18n name="i18n.mb.BINOLMBRPT04">
<s:text name="global.page.select" id="select_default"/>
<div class="panel-header">
    <div class="clearfix"> 
      <span class="breadcrumb left"> 
    	<span class="ui-icon icon-breadcrumb"></span>
    	<s:text name="RPT04_memberManage"></s:text>&nbsp;&gt;&nbsp;<s:text name="RPT04_memDevelopRpt"></s:text> 
      </span> 
    </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
	<cherry:form id="queryForm">
	<div class="box">
	  <div class="box-header">
	    <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
	    <input type="checkbox" name="testFlag" id="testFlag" value="1"/><s:text name="RPT04_testFlag"/>   
	  </div>
	  <div class="box-content clearfix">
	    <div class="column" style="width:49%; height: auto;">
	    	<p>
           	<label style="width:100px;"><s:text name="RPT04_date"/></label>
           	<span><s:textfield name="startDate" cssClass="date"/></span>-<span><s:textfield name="endDate" cssClass="date"/></span>
          </p>
          <p>
       		<label style="width:100px;"><s:text name="RPT04_belongFaction"></s:text></label>
			<span><s:select list='#application.CodeTable.getCodes("1309")' listKey="CodeKey" listValue="Value" name="belongFaction" headerKey="" headerValue="%{#select_default}" value=""></s:select></span>
	      </p>
	    </div>
	    <div class="column last" style="width:50%; height: auto;">
	      <p> 	
           	<label><s:text name="RPT04_channel"/></label>
           	<select id="channelId" name="channelId">
           	  <option value=""><s:text name="global.page.select" /></option>
           	</select>
           	<s:hidden name="channelName"></s:hidden>
          </p>
          <p> 	
           	<label><s:text name="RPT04_organizationId"/></label>
           	<select id="organizationId" name="organizationId">
           	  <option value=""><s:text name="global.page.select" /></option>
           	</select>
           	<s:hidden name="organizationName"></s:hidden>
          </p>
	    </div>
	  </div>
	  <p class="clearfix">	
       	<button class="right search" id="searchButton" onclick="search();return false;">
       	  <span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
       	</button>
      </p>
	</div>
	</cherry:form>
	
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section hide">
		<div class="section-header">
			<strong> 
				<span class="ui-icon icon-ttl-section-search-result"></span> 
				<s:text name="global.page.list" />
		 	</strong>
		</div>
		<div class="section-content">
			<div class="toolbar clearfix">
				<cherry:show domId="BINOLMBRPT04EXP">
				<span style="margin-right:10px;">
			     	<a id="export" class="export" onclick="exportExcel();return false;">
			          <span class="ui-icon icon-export"></span>
			          <span class="button-text"><s:text name="global.page.exportExcel"/></span>
			        </a>
			    </span>
			    </cherry:show>
			    <span id="headInfo"></span>
				<span class="right"> 
					<%-- 设置列 --%>
					<a class="setting"> 
						<span class="ui-icon icon-setting"></span> 
						<span class="button-text"><s:text name="global.page.colSetting" /></span>
					</a>
				</span>
			</div>
			<div id="memDevelopRptDiv">
				<table class="jquery_table" style="width: 100%" id="dataTable">
					<thead>
						<tr>
						  <th><s:text name="No." /></th>
						  <th><s:text name="RPT04_organizationId"></s:text></th>
						  <th><s:text name="RPT04_busniessPrincipal"></s:text></th>
						  <th><s:text name="RPT04_totalMemberNum"></s:text></th>
					      <th><s:text name="RPT04_totalMemberSaleAmount"></s:text></th>
					      <th><s:text name="RPT04_newMemberNum"></s:text></th>
					      <th><s:text name="RPT04_newMemSaleAmount"></s:text></th>
					      <th><s:text name="RPT04_buyBackMemberNum"></s:text></th>
					      <th><s:text name="RPT04_buyBackMemSaleAmount"></s:text></th>
					      <th><s:text name="RPT04_newMemberProportion"></s:text></th>
					      <th><s:text name="RPT04_newMemConsumeAverage"></s:text></th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>

<div style="display: none;">
<s:a action="BINOLMBRPT04_search" id="searchUrl"></s:a>
<s:a action="BINOLMBRPT04_exportExcel" id="exportExcelUrl"></s:a>
<s:hidden name="channelCounterJson"></s:hidden>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>