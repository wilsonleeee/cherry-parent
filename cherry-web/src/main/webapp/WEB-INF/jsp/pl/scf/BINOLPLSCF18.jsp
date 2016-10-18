<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF18.js"></script>
<s:i18n name="i18n.pl.BINOLPLSCF18">
  <div class="panel-header">
	<%-- 基本配置管理  --%>
	<div class="clearfix"> 
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
	</div>
  </div>
  <cherry:form id="mainForm" class="inline">
	  <div class="panel-content">
	    <div id="actionResultDisplay"></div>
	    <div class="section">
	      <s:if test="null != brandInfoList">
		      <div class="section-header">
			      <strong>
			      <span class="ui-icon icon-ttl-section-search-result"></span>
			      <span>
			     	<%-- 品牌名称 --%>
			   		<s:text name="scf_brandName"/>
			      </span>
			      </strong>
			      <s:url action="BINOLPLSCF18_searchBsCf" id="searchBsCfUrl"></s:url>
			      <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="plscf_changeBrand('%{#searchBsCfUrl}',this);"/>
		      </div>
	      </s:if>
	      <div id="baseConfInfo">
	      	<jsp:include page="/WEB-INF/jsp/pl/scf/BINOLPLSCF18_1.jsp" flush="true" />
	      </div>
	    </div>
	  </div>
  </cherry:form>
</s:i18n>