


// 创建区域
function bsreg01_addRegion(url) {
	var param = $('#regionCherryForm').find(':input[name=csrftoken]').serialize();
	if($("#regionTree").find('a.jstree-clicked').length > 0) {
		var regionId = $("#regionTree").find('a.jstree-clicked').attr('id');
		if(regionId) {
			param += "&regionId=" + regionId;
		}
	}
	url = url + '?' + param;
	popup(url);
}

// 树初始化
function bsreg01_regionTreeInit() {
	$("#regionTree").jstree({
		"plugins" : [ "themes", "json_data", "ui", "crrm","search" ],
		"core": {"animation":0},
		"themes" : {
	        "theme" : "sunny",
	        "dots" : true,
	        "icons" : false
	    },
		"json_data" : {
			"ajax" : {
				"url" : $("#regionTreeNextUrl").attr("href"),
				"data" : function(n){var param = n.attr ? {"regionId" : n.attr("id").replace("node_","")} : {}; 
				param.csrftoken = $('#regionCherryForm').find(':input[name=csrftoken]').val(); return param;}
			}
		},
		"search" : {
			"ajax" : {
				"url" : $("#locateHigherUrl").attr("href"),
				"data" : function (str) {
					var param = {"regionId" : str}; 
					param.csrftoken = $('#regionCherryForm').find(':input[name=csrftoken]').val(); 
					return param;
				}
			},
			"search_method" : "jstree_title_contains"
		}
	}).delegate('a','click', function() {
		bsreg01_regionDetail(this.id);
	}).bind("search.jstree", function (e, data) {
		$("#regionTree").find("a.jstree-clicked").removeClass("jstree-clicked");
		var $searchObject = $("#regionTree").find("a.jstree-search");
		if($searchObject.length > 0) {
			$searchObject.removeClass("jstree-search");
			$searchObject.first().addClass("jstree-clicked");
			bsreg01_regionDetail($searchObject.first().attr("id"));
		}
	});
}

// 树定位
function bsreg01_locateRegion(value) {
	if(value) {
		$("#regionTree").jstree("search",value);
	} else {
		var locationPosition = $("#locationPosition").val();
		if(locationPosition != "") {
			var param = "locationPosition=" + locationPosition;
			var callback = function(msg) {
				if(msg) {
					$("#regionTree").jstree("search",msg);
				}
			};
			cherryAjaxRequest({
				url: $("#locateRegionIdUrl").attr("href"),
				param: param,
				callback: callback,
				formId: '#regionCherryForm'
			});
		}
	}
}

// 点击树上的节点后显示区域详细
function bsreg01_regionDetail(value) {
	if(value) {
		var param = "regionId=" + value;
		var callback = function(msg){
			$("#regionInfo").html(msg);
			if(bscom01_layout != null) {
				var height = $("#regionInfo").height() + 40;
				if(height > 700) {
					$("#treeLayoutDiv").css({height: height});
					bscom01_layout.resizeAll();
				} else {
					if($("#treeLayoutDiv").height() > 700) {
						$("#treeLayoutDiv").css({height: 700});
						bscom01_layout.resizeAll();
					}
				}
			}
		};
		cherryAjaxRequest({
			url: $("#regionDetailUrl").attr("href"),
			param: param,
			callback: callback,
			formId: '#regionCherryForm'
		});
	}
}

// 区域启用停用处理
function bsreg01_editValidFlag(flag, url) {
	var param = "";
	var callback = function() {
		bsreg01_regionDetail($('#regionId').val());
		bsreg01_refreshRegionNode();
	};
	bscom03_deleteConfirm(flag, url, param, callback);
}

// 更新或者添加区域后刷新树节点
function bsreg01_refreshRegionNode(node) {
	if(node) {
		$("#regionTree").jstree("refresh", $("#node_"+node));
	} else {
		$("#regionTree").jstree("refresh", -1);
	}
}

function bsreg01_refreshRegion(url) {
	var callback = function(msg){
		if(msg == "1") {
			alert("刷新成功");
		} else {
			alert("刷新失败");
		}
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: callback
	});
}