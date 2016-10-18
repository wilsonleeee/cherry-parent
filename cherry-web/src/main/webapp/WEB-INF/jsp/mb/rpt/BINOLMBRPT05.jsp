<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/mb/rpt/BINOLMBRPT05.js"></script>

<s:i18n name="i18n.mb.BINOLMBRPT05">
<s:text name="global.page.select" id="select_default"/>
<div class="panel-header">
    <div class="clearfix"> 
      <span class="breadcrumb left"> 
    	<span class="ui-icon icon-breadcrumb"></span>
    	<s:text name="RPT05_memberManage"></s:text>&nbsp;&gt;&nbsp;<s:text name="RPT05_memConsumeRpt"></s:text> 
      </span> 
    </div>
</div>
<div class="panel-content clearfix">
	<cherry:form id="queryForm">
	<div class="box">
	  <div class="box-header">
	    <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>  
	    <input type="checkbox" name="testFlag" id="testFlag" value="1"/><s:text name="RPT05_testFlag"/> 
	  </div>
	  <div class="box-content clearfix">
	    <div class="column" style="width:49%; height: auto;">
	    	<p>
           	<label style="width:100px;"><s:text name="RPT05_date"/></label>
           	<span><s:textfield name="startDate" cssClass="date"/></span>-<span><s:textfield name="endDate" cssClass="date"/></span>
          </p>
          <p>
       		<label style="width:100px;"><s:text name="RPT05_belongFaction"></s:text></label>
			<span><s:select list='#application.CodeTable.getCodes("1309")' listKey="CodeKey" listValue="Value" name="belongFaction" headerKey="" headerValue="%{#select_default}" value=""></s:select></span>
	      </p>
	    </div>
	    <div class="column last" style="width:50%; height: auto;">
	      <p> 	
           	<label><s:text name="RPT05_channel"/></label>
           	<select id="channelId" name="channelId">
           	  <option value=""><s:text name="global.page.select" /></option>
           	</select>
           	<s:hidden name="channelName"></s:hidden>
          </p>
          <p> 	
           	<label><s:text name="RPT05_organizationId"/></label>
           	<select id="organizationId" name="organizationId">
           	  <option value=""><s:text name="global.page.select" /></option>
           	</select>
           	<s:hidden name="organizationName"></s:hidden>
          </p>
	    </div>
	  </div>
	  <p class="clearfix">
        <input id="statisticsType1" name="statisticsType" type="radio" value="1" checked/><s:text name="RPT05_consumeTimesRegion"></s:text>
        <input id="statisticsType2" name="statisticsType" type="radio" value="2" /><s:text name="RPT05_consumeAmountRegion"></s:text>
       	<input id="statisticsType3" name="statisticsType" type="radio" value="3" /><s:text name="RPT05_memberAgeRegion"></s:text>
       	<button class="right search" id="searchButton">
       	  <span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
       	</button>
       	<cherry:show domId="BINOLMBRPT05EXP">
	       	<button class="right search" id="exportExcel">
	       	  <span class="ui-icon icon-file-export"></span><span class="button-text"><s:text name="global.page.exportExcel"/></span>
	       	</button>
       	</cherry:show>		
      </p>
	</div>
	</cherry:form>
	
	<div class="section">
	  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
	  <div class="section-content">
	    <div id="memDevelopRptDiv"></div>
	  </div>
  	</div>
</div>
</s:i18n>

<div style="display: none;">
<s:a action="BINOLMBRPT05_search" id="searchUrl"></s:a>
<s:a action="BINOLMBRPT05_exportExcel" id="exportExcelUrl"></s:a>
<div id="table_sProcessing"><s:text name="table_sProcessing"/></div>
<s:hidden name="channelCounterJson"></s:hidden>
</div>
