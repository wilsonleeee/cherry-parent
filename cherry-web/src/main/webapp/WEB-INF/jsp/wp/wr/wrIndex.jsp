<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/common/BINOLWPCOM01.js"></script>
<body>
	<div class="header container clearfix">
		<jsp:include page="/WEB-INF/jsp/common/header.inc.jsp" flush="true" />			
	</div> 
	<div class="menu">
		<ul class="ui-widget ui-widget-header ui-corner-all clearfix">
		  	<jsp:include page="/WEB-INF/jsp/common/topmenu.inc.jsp" flush="true" />
		</ul>
	</div>		
	<div class="main container clearfix">
		<div class="sidebar left">		    
	   		<jsp:include page="/WEB-INF/jsp/common/leftmenu.inc.jsp" flush="true" />
		</div>
		<div class="maincontent">
			<div class="panel ui-corner-all">
				<div id ="div_main">
				<jsp:include page="/WEB-INF/jsp/wp/wr/wrtop.jsp" flush="true" />
				</div>
			</div>
		</div>
	</div>
	<div class="footer">
		<%@ include file="/WEB-INF/jsp/common/foot.inc.jsp" %>
	</div>
	<script type=text/javascript>
		$('#u0leftmenu > li > #amenuA0').click();
		$('#u0leftmenu > li   #amenuA0B0').click();	
	</script>
 </body>
</html>