<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.bs.BINOLBSWEM03">
<div class="main container clearfix">
<div id="div_main">
<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="WEM03_Manage" />&nbsp;&gt;&nbsp;<s:text name="WEM03_RebateDivide"></s:text></span> 
  </div>
</div>

<div id="actionResultDisplay"></div>

<cherry:form id="addBrand">
<div class="panel-content clearfix">
<div class="box">
	<div class="box-header">
		<strong><span class="ui-icon icon-ttl-search"></span>
		<s:text name="WEM03_RebateDivide"/>
		</strong>
</div>
	<table class="detail" cellpadding="0" cellspacing="0" style="width: 100%">
	 <tr>
	    <th><s:text name="WEM03_No"></s:text></th>
	    <th><s:text name="WEM03_level"></s:text></th>
	    <th><s:text name="WEM03_DividePer"></s:text></th>
	  </tr>
	  <s:iterator value='agentLevelList' id="code" status="status">
	  <tr align="center">
	  	<td>${status.index+1}</td>
	  	<td>${code.Value}<input type="hidden" id="codeVal"  name="rebateDivideConfList[${status.index}].departType" value="${code.CodeKey}"/></td>
	  	<td><span><input type="text" id="dividePer" name="rebateDivideConfList[${status.index}].dividePer" value="${code.dividePer}" class="aa"/>%</span></td>
  </tr>
	  </s:iterator>
	</table>      
<hr class="space" />
<div class="center">
  <button id="saveButton" class="save" type="button"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="reset"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.reset"/></span></button>
</div>
</div>
 
</div>
</cherry:form>

</div>
</div>  
<script type="text/javascript" >
$(function(){

	$("#saveButton").click(function(){
		clearError();
		if(aa()) {
			function callback(){
				alert("成功");
			};
			cherryAjaxRequest({
				url: "BINOLBSWEM03_save",
				param: $("#addBrand").serialize(),
				callback: callback
			});
			return false;
		}
		return false;
	});
	
	function clearError() {
		$(".aa").each(function(){
			var $parent = $(this).parent();
			$parent.removeClass('error');
			$parent.find('#errorText').remove();
			if($("#cluetip").is(":visible")) {
				$("#cluetip").remove();
				$("#cluetip-waitimage").remove();
			}
		});
	}
	
	function aa(){
		var result = true;
		$(".aa").each(function(){
			if(!checkValue(this)) {
				result = false;
			}
		});
		return result;
	}
	
	function checkValue(obj) {
		var value = $(obj).val();
		
		var cherck= value && checkFloat(value, [4,2]);
		if(cherck) {
			if(value > 100) {
				cherck = false;
			}
		}
		
		if(!cherck) {
			var errormsg = "请输入一个不大于100的正数,整数不能超过4位，小数不能超过2位";
			var $parent = $(obj).parent();
			$parent.addClass("error");
			$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
			$parent.find('#errorText').attr("title",'error|'+errormsg);
			$parent.find('#errorText').cluetip({
		    	splitTitle: '|',
			    width: 150,
			    cluezIndex: 20000,
			    tracking: true,
			    cluetipClass: 'error', 
			    arrows: true, 
			    dropShadow: false,
			    hoverIntent: false,
			    sticky: false,
			    mouseOutClose: true,
			    closePosition: 'title',
			    closeText: '<span class="ui-icon ui-icon-close"></span>'
			});
		}
		return cherck;
	}
	
	function checkFloat(value, param) {
		var checkFloat = false;
		checkFloat = /^([0-9]\d*)(\.\d+)?$/.test(value);
		if(checkFloat) {
			var ar = value.split('.');
			if(ar.length == 2) {
				if(ar[0].length > param[0] || ar[1].length > param[1]) {
					checkFloat = false;
				}
			} else if(ar.length == 1) {
				if(ar[0].length > param[0]) {
					checkFloat = false;
				}
			} else {
				checkFloat = false;
			}
		}
		return checkFloat;
	}
	
});

	
</script>
</s:i18n>


