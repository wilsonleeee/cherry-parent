
// 画面布局全局变量
var plrla01_layout = null;
$(document).ready(function() {
	
	// 组织结构树初期化处理
	$("#organizationTree").jstree({
		"plugins" : [ "themes", "json_data", "ui" ],
		"core": {"animation":0},
		"themes" : {
	        "theme" : "sunny",
	        "dots" : true,
	        "icons" : false
	    },
		"json_data" : {
			"ajax" : {
				"url" : $("#organizationInitUrl").attr("href"),
				"data" : function(n){var param = n.attr ? {"path" : n.attr("id")} : {}; 
				param.csrftoken = $('#privilegeCherryForm').find(':input[name=csrftoken]').val(); return param;}
			}
		}
	}).delegate('a','click', function() {
		if(this.id) {
			var m = this.id.split('_');
			if(m.length==2) {
				var param = "organizationId=" + m[0] + "&brandInfoId=" + m[1];
				param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
				var callback = function(msg){
					$("#roleAssignInit").html(msg);
					if(plrla01_layout != null) {
						var height = $("#roleAssignInit").height();
						var _height = $("#roleAssignInit").parent().height();
						if(height > _height) {
							var thisHeight = $("#treeLayoutDiv").height();
							$("#treeLayoutDiv").css({height: (thisHeight+height-_height+20)});
							plrla01_layout.resizeAll();
						}
					}
				};
				cherryAjaxRequest({
					url: $("#roleAssignUrl").attr("href"),
					param: param,
					callback: callback,
					formId: '#privilegeCherryForm'
				});
			}
		}
		
	});
	
	// 画面布局初期化处理
	plrla01_layout = $('#treeLayoutDiv').layout({
		spacing_open: 2, // 边框的间隙 
		spacing_closed: 5, // 关闭时边框的间隙 
		resizerTip: "", // 鼠标移到边框时，提示语
		togglerTip_open: "", // pane打开时，当鼠标移动到边框上按钮上，显示的提示语
		togglerTip_closed: "", // pane关闭时，当鼠标移动到边框上按钮上，显示的提示语
		fxName: "none", // 打开关闭的动画效果
		togglerLength_open: 0, // pane打开时，边框按钮的长度  
		togglerLength_closed: 0, // pane关闭时，边框按钮的长度  
		west__minSize: 120, // 可改变的最小长度
		west__maxSize: 360 // 可改变的最大长度
	});
	
} );