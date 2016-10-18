<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/rpt/BINOLMBRPT03.js"></script>

<s:i18n name="i18n.mb.BINOLMBRPT03">
<div class="panel-header">
    <div class="clearfix"> 
      <span class="breadcrumb left"> 
    	<span class="ui-icon icon-breadcrumb"></span>
    	<s:text name="mbrpt_memberManage"></s:text>&nbsp;&gt;&nbsp;<s:text name="mbrpt_memLevelRpt"></s:text> 
      </span> 
    </div>
</div>
<div class="panel-content clearfix">
	<cherry:form id="queryForm">
	<div class="box">
	  <div class="box-header">
	    <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>   
	  </div>
	  <div class="box-content clearfix">
	    <div class="column" style="width:49%; height: auto;">
	      <p> 	
           	<label><s:text name="mbrpt_saleTime"/></label>
           	<span><s:textfield name="saleDateStart" cssClass="date"/></span>-<span><s:textfield name="saleDateEnd" cssClass="date"/></span>
          </p>
	    </div>
	    <div class="column last" style="width:50%; height: auto;">
	      <p> 	
           	<label><s:text name="mbrpt_channelId"/></label>
           	<select id="channelId" name="channelId">
           	  <option value=""><s:text name="global.page.select" /></option>
           	</select>
           	<s:hidden name="channelName"></s:hidden>
          </p>
          <p> 	
           	<label><s:text name="mbrpt_organizationId"/></label>
           	<select id="organizationId" name="organizationId">
           	  <option value=""><s:text name="global.page.select" /></option>
           	</select>
           	<s:hidden name="organizationName"></s:hidden>
          </p>
	    </div>
	  </div>
	  <p class="clearfix">	
       	<button class="right search" id="searchButton">
       	  <span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
       	</button>
       	<button class="right search" id="exportExcel">
       	  <span class="ui-icon icon-file-export"></span><span class="button-text"><s:text name="global.page.exportExcel"/></span>
       	</button>		
      </p>
	</div>
	</cherry:form>
	
	<div class="section">
	  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
	  <div class="section-content">
	    <div id="memLevelRptDiv"></div>
	  </div>
  	</div>
</div>
</s:i18n>

<div style="display: none;">
<s:a action="BINOLMBRPT03_search" id="searchUrl"></s:a>
<s:a action="BINOLMBRPT03_exportExcel" id="exportExcelUrl"></s:a>
<div id="table_sProcessing"><s:text name="table_sProcessing"/></div>
<s:hidden name="channelCounterJson"></s:hidden>
</div>
