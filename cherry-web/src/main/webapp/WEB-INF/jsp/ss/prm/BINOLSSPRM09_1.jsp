<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM01.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#categoryTree").jstree({
		"plugins" : [ "themes", "json_data", "ui", "crrm" ],
		"core": {"animation":0},
		"themes" : {
	        "theme" : "sunny",
	        "dots" : true,
	        "icons" : false
	    },
		"json_data" : {
			"ajax" : {
				"url" : $("#categoryInitUrl").attr("href"),
				"data" : function(n){var param = n.attr ? {"path" : n.attr("id")} : {}; 
				param.csrftoken = $('#categoryCherryForm').find(':input[name=csrftoken]').val(); return param;}
			}
		}
	}).delegate('a','click', function() {
		categoryDetail(this.id);
	});
} );

function categoryDetail(value) {
	var param = "prmCategoryId=" + value;
	var callback = function(msg){
		$("#categoryInfo").html(msg);
		if(bscom01_layout != null) {
			var height = $("#categoryInfo").height();
			var _height = $("#categoryInfo").parent().height();
			if(height > _height) {
				var thisHeight = $("#treeLayoutDiv").height();
				$("#treeLayoutDiv").css({height: (thisHeight+height-_height+20)});
				bscom01_layout.resizeAll();
			}
		}
	};
	cherryAjaxRequest({
		url: $("#categoryInfoUrl").attr("href")+'?modeFlg='+'1',
		param: param,
		callback: callback,
		formId: '#categoryCherryForm'
	});
}

function refreshCateNode(higherNode) {
	$node = $(document.getElementById(higherNode));
	if($node.length > 0) {
		$("#categoryTree").jstree("refresh",$node);
	} else {
		$("#categoryTree").jstree({
			"plugins" : [ "themes", "json_data", "ui", "crrm" ],
			"core": {"animation":0},
			"themes" : {
		        "theme" : "sunny",
		        "dots" : true,
		        "icons" : false
		    },
			"json_data" : {
				"ajax" : {
					"url" : $("#categoryInitUrl").attr("href"),
					"data" : function(n){var param = n.attr ? {"path" : n.attr("id")} : {}; 
					param.csrftoken = $('#categoryCherryForm').find(':input[name=csrftoken]').val(); return param;}
				}
			}
		}).delegate('a','click', function() {
			categoryDetail(this.id);
		});
	}
}
</script>

<s:i18n name="i18n.ss.BINOLSSPRM09">
  <cherry:form id="categoryCherryForm"></cherry:form>
  <div id="treeLayoutDiv" class="div-layout-all">
     <div class="ui-layout-west">
       <div style="min-width: 160px;">
         <div class="treebox2-header">
           <strong><span class="ui-icon icon-tree"></span><s:text name="prm09_category"></s:text></strong>
         </div>
         <dl><dt class="jquery_tree" id="categoryTree"></dt></dl>
       </div>
     </div>
     <div class="ui-layout-center">
        <div style="min-width: 450px;" id="categoryInfo"></div>
     </div>
  </div>
</s:i18n>

<div class="hide">
  <s:url action="BINOLSSPRM09_next" id="categoryInitUrl"></s:url>
  <a href="${categoryInitUrl }" id="categoryInitUrl"></a>
  <s:url action="BINOLSSPRM12_init" id="categoryInfoUrl"></s:url>
  <a href="${categoryInfoUrl }" id="categoryInfoUrl"></a>
</div>