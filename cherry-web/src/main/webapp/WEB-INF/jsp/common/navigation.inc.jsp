<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<span class="left">
	<span class="ui-icon icon-breadcrumb"></span>
	<s:text name='%{#parameters.parentMenuID}'></s:text>&nbsp;&gt;&nbsp;<s:text name='%{#parameters.currentMenuID}'></s:text> 
</span>
<span class="hide"><input id="currentMenuID" value="<s:property value='#parameters.currentMenuID'/>"/></span><%-- 当前页面ID  --%>
<span class="ui-icon icon-help" style="margin-left:10px; display:inline-block;" title="<s:text name='global.page.help'></s:text>" onclick="javascript:getHelp(this);return false;"></span>

<div id="naviTitle" class="hide">
	<s:text name='global.page.help'></s:text> 
</div>
<div class="hide " id="helpDialogInit"></div>

<script type=text/javascript>

	/**
	 * 弹出帮助页面
	 */
	function getHelp(obj) {
		
		var strPath = window.document.location.pathname;
		var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
		var url = postPath+"/common/BINOLCM34_Action_getHelp.action";
		
		var param = "currentMenuID=" + $("#currentMenuID").val();
		
		var callback = function(msg) {
			var dialogSetting = {
					dialogInit: "#helpDialogInit",
					bgiframe: true,
					width:  800,
					height: 450,
					text: msg,
					title: 	$("#naviTitle").text(),
					resizable : true
				};
				openDialog(dialogSetting);
		};
		
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	}

</script>