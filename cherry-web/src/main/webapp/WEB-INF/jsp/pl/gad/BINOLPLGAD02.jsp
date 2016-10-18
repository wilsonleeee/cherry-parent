<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/pl/gad/BINOLPLGAD02.js"></script>

<s:i18n name="i18n.pl.BINOLPLGAD02">
<div class="main container clearfix">
<div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
      <span class="breadcrumb left"> 
        <span class="ui-icon icon-breadcrumb"></span>
		<s:text name="binolplscf02.dadgetTitle"/> &gt; <s:text name="binolplscf02.title"/>
      </span>
    </div>
  </div>
  
  <div class="panel-content clearfix">
    <div id="actionResultDisplay"></div>
    <div class="section"> 
      <div class="section-header">
	    <form id="mainForm" class="inline">
	    <strong>
	      <span class="ui-icon icon-ttl-section-search-result"></span>
	      <span><s:text name="binolplscf02.pageId"/></span>
	    </strong>
	    <s:select name="pageId" list="#application.CodeTable.getCodes('1130')" listKey="CodeKey" listValue="Value" onchange="binolplgad02.searchGadgetList();"></s:select>
        </form>
      </div>
	  <div id="gadgetInfoListDiv"></div>
    </div>
  </div>
</div>
</div>  
</s:i18n>

<div class="hide">
<s:url action="BINOLPLGAD02_search" id="searchGadgetListUrl"></s:url>
<a href="${searchGadgetListUrl }" id="searchGadgetListUrl"></a>
</div>
