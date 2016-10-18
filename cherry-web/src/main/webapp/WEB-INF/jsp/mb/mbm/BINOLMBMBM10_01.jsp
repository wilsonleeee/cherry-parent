<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type=text/javascript>
$(document).ready(function() {
	initGadget("gadgetContainer",$("#gadgetInfoId").val());
});
</script>
<s:i18n name="i18n.mb.BINOLMBMBM10">
<div class="crm_content_header">
  <span class="icon_crmnav"></span>
  <span><s:text name="binolmbmbm10_memTop" /></span>
</div>
</s:i18n>
<div class="panel-content clearfix">
	<div id="gadgetContainer" class="section"></div>
	<div id="gadgetMaxContainer" class="section" style="display: none"></div>
	<s:hidden id="gadgetInfoId" name="gadgetInfo"></s:hidden>
</div>