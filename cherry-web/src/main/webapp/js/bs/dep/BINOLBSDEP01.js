
// 切换树模式和列表模式
function bsdep01_changeTreeOrTable(object,url) {
	oTableArr = new Array(null,null);
	fixedColArr = new Array(null,null);
	$(window).unbind('resize');
	if($(object).attr('class').indexOf('display-tree') != -1) {
		if($(object).attr('class').indexOf('display-tree-on') != -1) {
			return false;
		} else {
			$(object).siblings().removeClass('display-table-on');
			$(object).addClass('display-tree-on');
		}
	} else {
		if($(object).attr('class').indexOf('display-table-on') != -1) {
			return false;
		} else {
			$(object).siblings().removeClass('display-tree-on');
			$(object).addClass('display-table-on');
		}
	}
	var callback = function(msg) {
		$("#treeOrtableId").html(msg);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: callback,
		formId: '#organizationCherryForm'
	});
}

// 创建部门
function bsdep01_addOrganization(url) {
	var param = $('#organizationCherryForm').find(':input[name=csrftoken]').serialize();
	if($("#organizationTree").find('a.jstree-clicked').length > 0) {
		var organizationId = $("#organizationTree").find('a.jstree-clicked').attr('id');
		if(organizationId) {
			if(organizationId.indexOf('_') != -1) {
				return false;
			} else {
				param += "&organizationId=" + organizationId;
			}
		}
	}
	url = url + '?' + param;
	popup(url);
}

// 树初始化
function bsdep01_orgTreeInit() {
	$("#organizationTree").jstree({
		"plugins" : [ "themes", "json_data", "ui", "crrm","search" ],
		"core": {"animation":0},
		"themes" : {
	        "theme" : "sunny",
	        "dots" : true,
	        "icons" : false
	    },
		"json_data" : {
			"ajax" : {
				"url" : $("#organizationInitUrl").attr("href"),
				"data" : function(n){var param = n.attr ? {"path" : n.attr("id").replace("node_","")} : {}; 
				param.csrftoken = $('#organizationCherryForm').find(':input[name=csrftoken]').val(); return param;}
			}
		},
		"search" : {
			"ajax" : {
				"url" : $("#locateOrgUrl").attr("href"),
				"data" : function (str) {
					var param = {"locationPosition" : str}; 
					param.csrftoken = $('#organizationCherryForm').find(':input[name=csrftoken]').val(); 
					return param;
				}
			}
		}
	}).delegate('a','click', function() {
		bsdep01_organizationDetail(this.id);
	}).bind("search.jstree", function (e, data) {
		$("#organizationTree").find("a.jstree-clicked").removeClass("jstree-clicked");
		var $searchObject = $("#organizationTree").find("a.jstree-search");
		if($searchObject.length > 0) {
			$searchObject.removeClass("jstree-search");
			$searchObject.first().addClass("jstree-clicked");
			bsdep01_organizationDetail($searchObject.first().attr("id"));
		}
	});
}

// 树定位
function bsdep01_locateOrg(value) {
	if(value) {
		$("#organizationTree").jstree("search",value);
	} else {
		var locationPosition = $("#locationPosition").val();
		if(locationPosition != "") {
			$("#organizationTree").jstree("search",locationPosition);
		}
	}
}

// 部门查询
function bsdep01_searchOrganization(){
	var maintainOrgSynergy=$("#maintainOrgSynergy").val();
	var url = $("#organizationListUrl").attr("href");
	$('#organizationCherryForm :input').each(function(){
		$(this).val($.trim(this.value));
	});
	var params= $("#organizationCherryForm").serialize();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	// 显示结果一览
	$("#organizationInfoId").removeClass("hide");
	// 表格设置
	var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [[ 1, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [  { "sName": "no", "sWidth": "5%", "bSortable": false },
							{ "sName": "DepartCode", "sWidth": "10%" },
							{ "sName": "DepartName", "sWidth": "20%" },
							{ "sName": "Type", "sWidth": "5%" },
							{ "sName": "BrandNameChinese", "sWidth": "10%", "bSortable": false },
							{ "sName": "Status", "sWidth": "5%", "bSortable": false },
							{ "sName": "RegionName", "sWidth": "10%", "bSortable": false, "bVisible": false },
							{ "sName": "ProvinceName", "sWidth": "10%", "bSortable": false, "bVisible": false },
							{ "sName": "CityName", "sWidth": "10%", "bSortable": false, "bVisible": false },
							{ "sName": "CountyName", "sWidth": "10%", "bSortable": false, "bVisible": false },
							{ "sName": "ValidFlag", "sWidth": "5%", "bSortable": false, "sClass":"center" }],
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
	};

	// 部门协同区分
	if(maintainOrgSynergy=="true"){
		tableSetting.aoColumns.splice(10,0,{ "sName": "OrgSynergyFlag","sWidth": "5%","bVisible" : false, "sClass":"center"});//部门协同区分
	}
	// 调用获取表格函数
	getTable(tableSetting);
}

// 根据品牌筛选下拉列表
function bsdep01_changeBrandInfo(object,select_default) {
	
	var callback = function(msg){
		var jsons = eval('('+msg+')');
		$('#path').empty();
		$('#path').append('<option value="">'+select_default+'</option>');
		if(jsons.higherOrganizationList.length > 0) {
			for(var i in jsons.higherOrganizationList) {
				$('#path').append('<option value="'+jsons.higherOrganizationList[i].path+'">'+escapeHTMLHandle(jsons.higherOrganizationList[i].departName)+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: $('#filterByBrandInfoUrl').attr("href"),
		param: $(object).serialize(),
		callback: callback,
		formId: '#organizationCherryForm'
	});
}

// 点击树上的节点后显示部门详细
function bsdep01_organizationDetail(value) {
	if(value && value != 'Z') {
		var param = "organizationId=" + value;
		var callback = function(msg){
			$("#organizationInfo").html(msg);
			if(bscom01_layout != null) {
				var height = $("#organizationInfo").height() + 40;
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
			url: $("#organizationInfoUrl").attr("href")+'?modeFlg='+'1',
			param: param,
			callback: callback,
			formId: '#organizationCherryForm'
		});
	}
}

// 根据包含停用部门的选择情况重新生成树
function bsdep01_research(object) {
	var validFlag = false;
	if(object.checked) {
		validFlag = true;
	} else {
		validFlag = false;
	}
	bsdep01_orgTreeInit();
}

// 部门启用停用处理
function bsdep01_editValidFlag(flag, url) {
	
	var param = "";
	if($('#treeLayoutDiv').length > 0) {
	} else {
		if($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		param = $('#dataTable_Cloned :input[checked]').nextAll().serialize();
	}
	var callback = function() {
		if(oTableArr[0] != null)oTableArr[0].fnDraw();
		if($('#treeLayoutDiv').length > 0) {
			bsdep01_organizationDetail($('#organizationId').val());
			if(flag == 'enable') {
				$("#organizationTree").find("#"+$('#organizationId').val()).removeClass('jstree-disable');
			} else {
				$("#organizationTree").find("#"+$('#organizationId').val()).addClass('jstree-disable');
			}
		}
	};
	bscom03_deleteConfirm(flag, url, param, callback);
}

// 更新或者添加部门后刷新树节点
function bsdep01_refreshOrgNode(node) {
	if(node) {
		$("#organizationTree").jstree("refresh", $("#node_"+node));
	} else {
		$("#organizationTree").jstree("refresh", -1);
	}
}

//导出Excel
function bsdep01_exportExcel(urls) {
	// 无数据不导出
	if ($(".dataTables_empty:visible").length == 0) {
		if (!$('#organizationCherryForm').valid()) {
			return false;
		}
		;
		var url = urls;
		var params = $("#organizationCherryForm").find("div.column").find(":input")
				.serialize();
		params = params + "&csrftoken=" + $("#csrftoken").val();
		params = params + "&" + getRangeParams();
		url = url + "?" + params;
		window.open(url, "_self");
	}
}