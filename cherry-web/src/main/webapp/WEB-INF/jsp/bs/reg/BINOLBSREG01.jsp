<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM01.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/reg/BINOLBSREG01.js"></script>
<script type="text/javascript">
$(document).ready(function() {

	bsreg01_regionTreeInit();

	regionBinding({
		elementId: "locationPosition",
		showNum: 20
	});

	$("#locationPosition").result(function(event, data, formatted){
		bsreg01_locateRegion(data[2]);
	});
	
} );
</script>

<s:i18n name="i18n.bs.BINOLBSREG01">
  <div class="panel-header">
    <div class="clearfix"> 
     	<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
    <cherry:show domId="BINOLBSREG0101">
    <span class="right">
    	<s:url action="BINOLBSREG04_init" id="addRegionInitUrl"></s:url>
    	<a class="add" onclick="bsreg01_addRegion('${addRegionInitUrl}');return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="bsreg01_addButton"></s:text></span></a>
    </span>
    <span class="right">
    	<s:url action="BINOLCM08_refresh" namespace="/common" id="refreshRegionUrl"></s:url>
    	<a class="add" onclick="bsreg01_refreshRegion('${refreshRegionUrl}');return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="bsreg01_refreshButton"></s:text></span></a>
    </span>
    </cherry:show>
    </div>
  </div>

  <cherry:form id="regionCherryForm"></cherry:form>
	
  <div id="treeLayoutDiv" class="div-layout-all">
	<div class="ui-layout-west">
	<div style="min-width: 190px;">
	  <div class="treebox2-header">
		<input id="locationPosition" class="text" type="text" name="locationPosition" style="width: 100px;margin-right: 0;vertical-align: middle;"/>
		<a class="search" onclick="bsreg01_locateRegion();return false;">
			<span class="ui-icon icon-position"></span>
			<span class="button-text"><s:text name="bsreg01_positionButton"></s:text></span>
		</a>
	  </div>
	  <dl><dt class="jquery_tree" id="regionTree"></dt></dl>
	</div>
	</div>
	<div class="ui-layout-center">
	<div style="min-width: 438px;" id="regionInfo">
	</div>
	</div>
  </div>

  <div class="hide">
	<s:url action="BINOLBSREG01_treeNext" id="regionTreeNextUrl"></s:url>
	<a href="${regionTreeNextUrl }" id="regionTreeNextUrl"></a>
	<s:url action="BINOLBSREG02_init" id="regionDetailUrl"></s:url>
	<a href="${regionDetailUrl }" id="regionDetailUrl"></a>
	<s:url action="BINOLBSREG01_locateRegionId" id="locateRegionIdUrl"></s:url>
	<a href="${locateRegionIdUrl }" id="locateRegionIdUrl"></a>
	<s:url action="BINOLBSREG01_locateHigher" id="locateHigherUrl"></s:url>
	<a href="${locateHigherUrl }" id="locateHigherUrl"></a>
  </div>

  <div style="display: none;">
	<div id="disableTitle"><s:text name="bsreg01_disableTitle" /></div>
	<div id="enableTitle"><s:text name="bsreg01_enableTitle" /></div>
	<div id="disableMessage"><p class="message"><span><s:text name="bsreg01_disableMessage" /></span></p></div>
	<div id="enableMessage"><p class="message"><span><s:text name="bsreg01_enableMessage" /></span></p></div>
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
    <div id="privilegeTitle"><s:text name="global.page.privilegeTitle" /></div>
	<div id="privileMessage"><p class="message"><span><s:text name="global.page.privileMessage" /></span></p></div>
  </div>  
  <div class="hide" id="dialogInit"></div>
</s:i18n>  























 

    

 