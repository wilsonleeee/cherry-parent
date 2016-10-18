<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<link rel="stylesheet" href="/Cherry/css/cherry/gadgets.css" type="text/css">
<script type="text/javascript" src="/Cherry/gadgets/js/core:rpc:pubsub:shindig-container.js?c=1&debug=1"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryGadget.js"></script>

<script type=text/javascript>
$(document).ready(function() {
	
	gadgets.pubsubrouter.init(function(id) {
		var gadgetId = shindig.container.gadgetService.getGadgetIdFromModuleId(id);
		var gadget = shindig.container.getGadget(gadgetId);
		return gadget.specUrl;
	}, {
	    onSubscribe: function(sender, channel) {
	    	return false;
	    },
	    onUnsubscribe: function(sender, channel) {
	    	return false;
	    },
	    onPublish: function(sender, channel, message) {
	    	return false;
	    }
	});
	
	initGadget("gadgetContainer",$("#gadgetInfoId").val());
});
</script>

<cherry:form id="mainForm" csrftoken="true"></cherry:form>
<s:i18n name="i18n.mb.BINOLMBPTM01">
<div class="panel-header">
  <div class="clearfix">
    <span class="breadcrumb left">
      <span class="ui-icon icon-breadcrumb"></span>
      <s:text name="binolmbpmt01_title"></s:text>
    </span>
  </div>
</div>
<div class="panel-content clearfix">  
  <div id="gadgetContainer" class="section"></div>
  <div id="gadgetMaxContainer" class="section" style="display: none"></div>
  <s:hidden id="gadgetInfoId" name="gadgetInfo"></s:hidden>
</div>
</s:i18n>
       

	
