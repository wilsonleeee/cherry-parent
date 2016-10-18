<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/emp/BINOLBSEMP02.js"></script>


<s:i18n name="i18n.bs.BINOLBSEMP02">
<div class="main container clearfix">
<div class="panel ui-corner-all">
	  <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="emp_manage"/> &gt; <s:text name="emp_title"/>
	       </span>
	    </div>
	  </div>
	  <div class="panel-content clearfix">
      <s:include value="/WEB-INF/jsp/bs/emp/BINOLBSEMP02_1.jsp"></s:include>
      </div>
</div>
</div>
</s:i18n>
