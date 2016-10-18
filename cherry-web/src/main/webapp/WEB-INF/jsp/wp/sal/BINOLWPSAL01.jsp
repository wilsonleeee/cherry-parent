<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL01.js"></script>
<s:i18n name="i18n.wp.BINOLWPSAL01">
<div class="hide">
	<s:url id="counterSaleUrl" action="BINOLWPCM01_init" namespace="/wp" >
		<s:param name="pageType">SALE</s:param>
	</s:url>
	<s:url id="counterSetUrl" action="BINOLWPCM01_init" namespace="/wp" >
		<s:param name="pageType">SET</s:param>
	</s:url>
	<s:url id="cherryRefreshSession_Url" action="BINOLCM05_isSessionTimeout" namespace="/common">
	</s:url>
	<a id="cherryRefreshSessionUrl" href="${cherryRefreshSession_Url}"></a>
</div>
<cherry:form id="mainForm" class="inline">
	<div class="panel-header">
		<div class="clearfix"> 
			<span class="breadcrumb left">      
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
		</div>
	</div>
	<div class="panel-content">
		<div class="section">
			<a id="counterSaleBtn" class="add" href="${counterSaleUrl}" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
				<span class="button-text"><s:text name="wpsal01.counterSale"/></span>
			</a>
		</div>
	</div>
</cherry:form>
</s:i18n>