<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<link rel="stylesheet" href="/Cherry/css/cherry/gadgets.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/IEcssrepair.js"></script>
<script type="text/javascript" src="gadgets/js/core:rpc:pubsub:shindig-container.js?c=1&debug=1"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryGadget.js"></script>
<script type=text/javascript>
$(document).ready(function() {

	gadgetLeftMenuInit("u0leftmenu",$("#gadgetInfoId").val());
	initGadget("gadgetContainer",$("#gadgetInfoId").val());
});

function todoProcess(entryID,billType,billID,proType){	
	var url = "/Cherry/TODOPROCESSINSTANCE?OS_EntryID="+entryID+"&OS_BillID="+billID+"&OS_BillType="+billType+"&OS_ProType="+proType;
	var token = $('#csrftoken').val();
	url += "&csrftoken=" + token;	
	popup(url,{height:650, width:990});
}
</script>
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
	 	<%-- 
	 	<div class="sidebar left">		    
	   		<jsp:include page="/WEB-INF/jsp/common/leftmenu.inc.jsp" flush="true" />
		</div>
		<div class="maincontent">			 
			 <div id ="div_main">
				<jsp:include page="/WEB-INF/jsp/lg/top/topmain.jsp" flush="true" />	
			</div>
		</div>	
		
		--%>	
		<div class="sidebar left">	
		<form id="timeoutform" action="/Cherry/init" method="post"></form>	    
	   	<div class="panel ui-corner-all">
	   		<div class="panel-header">
      			<strong>
					<span class="ui-icon icon-ttl-control"></span>&nbsp;
				</strong>
    		</div>
      		<div class="panel-content">  
        		<ul id="u0leftmenu" class="sidemenu">
        		</ul>
            </div>
	   	</div>
		</div>
		<s:i18n name="i18n.lg.TOPPAGE1">
		<div class="maincontent">	
		<cherry:form id="mainForm" onsubmit="javascript:return false;" csrftoken="true"></cherry:form>
		
		<div class="panel ui-corner-all">
      		<div class="panel-header">
      			<div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="title"></s:text></span></div>
    		</div>
      		<div class="panel-content clearfix">  
        		<div id="gadgetContainer" class="section"></div>
        		<div id="gadgetMaxContainer" class="section" style="display: none"></div>
        		<s:hidden id="gadgetInfoId" name="gadgetInfo"></s:hidden>
            </div>
        </div>
        </div>
        </s:i18n>
		
	</div>
	<div class="footer">
		<%@ include file="/WEB-INF/jsp/common/foot.inc.jsp" %>
	</div>
 </body>
</html>