<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM01.js"></script>

<script type="text/javascript">
$(document).ready(function() {

	$("#positionTree").jstree({
		"plugins" : [ "themes", "json_data", "ui", "crrm" ],
		"core": {"animation":0},
		"themes" : {
	        "theme" : "sunny",
	        "dots" : true,
	        "icons" : false
	    },
		"json_data" : {
			"ajax" : {
				"url" : $("#positionInitUrl").attr("href"),
				"data" : function(n){var param = n.attr ? {"path" : n.attr("id")} : {}; 
				param.csrftoken = $('#positionCherryForm').find(':input[name=csrftoken]').val(); return param;}
			}
		}
	}).delegate('a','click', function() {
		positionDetail(this.id);
	});

} );

function positionDetail(value) {
	var param = "positionId=" + value;
	var callback = function(msg){
		$("#positionInfo").html(msg);
		if(bscom01_layout != null) {
			var height = $("#positionInfo").height();
			var _height = $("#positionInfo").parent().height();
			if(height > _height) {
				var thisHeight = $("#treeLayoutDiv").height();
				$("#treeLayoutDiv").css({height: (thisHeight+height-_height+20)});
				bscom01_layout.resizeAll();
			}
		}
	};
	cherryAjaxRequest({
		url: $("#positionInfoUrl").attr("href")+'?modeFlg='+'1',
		param: param,
		callback: callback,
		formId: '#positionCherryForm'
	});
}

function refreshPosNode(higherNode) {
	$node = $(document.getElementById(higherNode));
	if($node.length > 0) {
		$("#positionTree").jstree("refresh",$node);
	} else {
		$("#positionTree").jstree({
			"plugins" : [ "themes", "json_data", "ui", "crrm" ],
			"core": {"animation":0},
			"themes" : {
		        "theme" : "sunny",
		        "dots" : true,
		        "icons" : false
		    },
			"json_data" : {
				"ajax" : {
					"url" : $("#positionInitUrl").attr("href"),
					"data" : function(n){var param = n.attr ? {"path" : n.attr("id")} : {}; 
					param.csrftoken = $('#positionCherryForm').find(':input[name=csrftoken]').val(); return param;}
				}
			}
		}).delegate('a','click', function() {
			positionDetail(this.id);
		});
	}
}

</script>

<s:i18n name="i18n.bs.BINOLBSPOS01">
<cherry:form id="positionCherryForm"></cherry:form>
<div id="treeLayoutDiv" class="div-layout-all">
<div class="ui-layout-west">
<div style="min-width: 160px;">
  <div class="treebox2-header">
  <strong><span class="ui-icon icon-tree"></span><s:text name="position"></s:text></strong>
  </div>
  <dl><dt class="jquery_tree" id="positionTree"></dt></dl>
</div>
</div>
<div class="ui-layout-center">
<div style="min-width: 450px;" id="positionInfo">
</div>
</div>
</div>
</s:i18n>

<div class="hide">
<s:url action="BINOLBSPOS01_next" id="positionInitUrl"></s:url>
<a href="${positionInitUrl }" id="positionInitUrl"></a>
<s:url action="BINOLBSPOS02_init" id="positionInfoUrl"></s:url>
<a href="${positionInfoUrl }" id="positionInfoUrl"></a>
</div>