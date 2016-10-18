<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/rpt/BINOLMBRPT02_04.js"></script>

<s:i18n name="i18n.mb.BINOLMBRPT02">
<div class="panel-header">
    <div class="clearfix"> 
      <span class="breadcrumb left"> 
    	<span class="ui-icon icon-breadcrumb"></span>
    	<s:text name="mbrpt_memberManage"></s:text>&nbsp;&gt;&nbsp;<s:text name="mbrpt_TotalGetCountRpt"></s:text> 
      </span> 
    </div>
</div>
<div class="panel-content clearfix">
	<cherry:form id="bindCountRptForm">
	<div class="box">
	  <div class="box-header">
	    <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>   
	  </div>
	  <div class="box-content clearfix">
	    <div class="column last" style="width:50%; height: auto;">
	      <p> 	
           	<label><s:text name="mbrpt_campaignCode"/></label>
           	<s:select list="campaignList" name="campaignCode" listKey="campaignCode" listValue="campaignName"></s:select>
          </p>
	      <p> 	
           	<label><s:text name="mbrpt_wechatBindTime"/></label>
           	<span><s:textfield name="wechatBindTimeStart" cssClass="date"/></span>-<span><s:textfield name="wechatBindTimeEnd" cssClass="date"/></span>
          </p>
	    </div>
	  </div>
	  <p class="clearfix">	
       	<button class="right search" id="refreshBindCountRpt">
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
	    <div id="bindCountRptDiv"></div>
	  </div>
  	</div>
</div>
</s:i18n>

<div style="display: none;">
<s:a action="BINOLMBRPT02_searchTotalGetCount" id="searchTotalGetCountUrl"></s:a>
<s:a action="BINOLMBRPT02_eptCampRpt" id="eptCampRptUrl"></s:a>
<div id="table_sProcessing"><s:text name="table_sProcessing"/></div>
</div>
