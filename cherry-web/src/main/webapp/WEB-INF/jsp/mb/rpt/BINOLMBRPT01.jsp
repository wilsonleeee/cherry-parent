<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/rpt/BINOLMBRPT01.js"></script>

<s:i18n name="i18n.mb.BINOLMBRPT01">

<div class="panel-header">
    <div class="clearfix"> 
      <span class="breadcrumb left">
    	<span class="ui-icon icon-breadcrumb"></span>
    	<s:text name="binolmbrpt01_memberManage"></s:text>&nbsp;&gt;&nbsp;<s:text name="binolmbrpt01_memberSaleRpt"></s:text> 
      </span> 
    </div>
</div>
<div class="panel-content clearfix">
<cherry:form id="memSaleCherryForm" class="inline">
<div>
<strong><s:text name="binolmbrpt01_dateMode" /></strong>
<span>
  <select name="dateMode" id="dateMode" style="width:auto;">
	<option value="0"><s:text name="binolmbrpt01_dateMode0" /></option>
	<option value="1"><s:text name="binolmbrpt01_dateMode1" /></option>
  </select>
</span>
<span>
  <s:select list="yearList" name="fiscalYear" cssStyle="width:auto;"></s:select>
  <s:select list="monthList" name="fiscalMonth" cssStyle="width:auto;"></s:select>
</span>
<span class="hide">
  <span><s:textfield name="saleDateStart" cssClass="date"></s:textfield></span>-<span><s:textfield name="saleDateEnd" cssClass="date"></s:textfield></span>
</span>
<strong><s:text name="binolmbrpt01_channelId" /></strong>
<span>
  <select id="channelId" name="channelId">
    <option value=""><s:text name="global.page.select" /></option>
  </select>
  <s:hidden name="channelName"></s:hidden>
</span>
<strong><s:text name="binolmbrpt01_organizationId" /></strong>
<span>
  <select id="organizationId" name="organizationId">
	<option value=""><s:text name="global.page.select" /></option>
  </select>
  <s:hidden name="organizationName"></s:hidden>
</span>
<a id="export" class="export" onclick="binolmbrpt01.exportExcel();return false;">
  <span class="ui-icon icon-export"></span>
  <span class="button-text"><s:text name="global.page.exportExcel"/></span>
</a>
</div>
</cherry:form>
</div>

</s:i18n>

<div class="hide">
<s:url id="exportUrl" action="BINOLMBRPT01_export" ></s:url>
<a id="exportUrl" href="${exportUrl}"></a>
<s:hidden name="channelCounterJson"></s:hidden>
</div>