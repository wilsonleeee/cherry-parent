

// 树模式和列表模式切换
function bsemp01_changeTreeOrTable(object,url) {
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
		formId: '#employeeCherryForm'
	});
}

//树初始化
function bsemp01_empTreeInit() {
	
	$("#employeeTree").jstree({
		"plugins" : [ "themes", "json_data", "ui", "crrm","search" ],
		"core": {"animation":0},
		"themes" : {
	        "theme" : "sunny",
	        "dots" : true,
	        "icons" : false
	    },
		"json_data" : {
			"ajax" : {
				"url" : $("#employeeInitUrl").attr("href"),
				"data" : function(n){var param = n.attr ? {"path" : n.attr("id").replace("node_","")} : {}; 
				param.csrftoken = $('#employeeCherryForm').find(':input[name=csrftoken]').val(); return param;}
			}
		},
		"search" : {
			"ajax" : {
				"url" : $("#locateEmpUrl").attr("href"),
				"data" : function (str) {
					var param = {"locationPosition" : str}; 
					param.csrftoken = $('#employeeCherryForm').find(':input[name=csrftoken]').val(); 
					return param;
				}
			}
		}
	}).delegate('a','click', function() {
		bsemp01_employeeDetail(this.id);
	}).bind("search.jstree", function (e, data) {
		$("#employeeTree").find("a.jstree-clicked").removeClass("jstree-clicked");
		var $searchObject = $("#employeeTree").find("a.jstree-search");
		if($searchObject.length > 0) {
			$searchObject.removeClass("jstree-search");
			$searchObject.first().addClass("jstree-clicked");
			bsemp01_employeeDetail($searchObject.first().attr("id"));
		} else {
			if($("#employeeTree").jstree("is_closed","#node_-9")) {
				$("#employeeTree").jstree("open_node","#node_-9", function(){bsemp01_locateEmp()});
			}
		}
	});
}

//树定位
function bsemp01_locateEmp(value) {
	if(value) {
		$("#employeeTree").jstree("search",value);
	} else {
		var locationPosition = $("#locationPosition").val();
		if(locationPosition != "") {
			$("#employeeTree").jstree("search",locationPosition);
		}
	}
}

//用户查询
function bsemp01_search() {
	var url = $("#search_url").val();
	$('#mainForm :input').each(function(){
		$(this).val($.trim(this.value));
	});
	// 查询参数序列化
	var params= $("#mainForm").serialize();
	url = url + "?" + params;
	// 显示结果一览
	$("#section").show();
	// 表格设置
	var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [{ "sName": "no","sWidth": "5%","bSortable": false},
						{ "sName": "employeeCode","sWidth": "10%"},
						{ "sName": "longinName","sWidth": "10%"},
						{ "sName": "employeeName","sWidth": "10%"},
						{ "sName": "provinceName","bVisible": false,"sWidth": "8%"},
						{ "sName": "cityName","bVisible": false,"sWidth": "7%"},
						{ "sName": "categoryName","sWidth": "7%"},
						{ "sName": "brandName","bVisible": false,"sWidth": "8%","bSortable": false},		
						{ "sName": "departName","sWidth": "10%"},
						{ "sName": "mobilePhone","sWidth": "10%","bVisible": false,"bSortable": false},	
						{ "sName": "email","sWidth": "10%","bVisible": false,"bSortable": false},	
						{ "sName": "validFlag","sWidth": "5%","bSortable": false}],	
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

// 点击树上的节点后显示用户详细
function bsemp01_employeeDetail(value) {
	if(value && value != "-9") {
		var param = "employeeId=" + value;
		var callback = function(msg){
			$("#employeeInfo").html(msg);
			if(bscom01_layout != null) {
				var height = $("#employeeInfo").height() + 40;
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
			url: $("#employeeInfoUrl").attr("href")+'?modeFlg='+'1',
			param: param,
			callback: callback,
			formId: '#employeeCherryForm'
		});
	}
}

// 更新或者添加用户后刷新树节点
function bsemp01_refreshNode(node) {
	if(node) {
		$("#employeeTree").jstree("refresh", $("#node_"+node));
	} else {
		$("#employeeTree").jstree("refresh", -1);
	}
}

// 根据包含停用用户的选择情况重新生成树
function bsemp01_research(object) {
	var validFlag = false;
	if(object.checked) {
		validFlag = true;
	} else {
		validFlag = false;
	}
	bsemp01_empTreeInit();
}

//用户启用停用处理
function bsemp01_editValidFlag(flag, url) {
	
	var param = "";
	if($('#treeLayoutDiv').length > 0) {
	} else {
		if($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		var checked = $('#dataTable_Cloned :input[checked]');
		for(var i=0 ; i < checked.length; i++){
			var validFlag = $(checked[i]).val();
			if((flag=="enable"&&validFlag=="1")||(flag=="disable"&&validFlag=="0")){
				$("#errorMessage").html($("#errorMessageTemp1").html());
				return false;
			}
		}
//		checked.each(function(){
//			var validFlag = $(this).val();
//			if((flag=="enable"&&validFlag=="1")||(flag=="disable"&&validFlag=="0")){
//				$("#errorMessage").html($("#errorMessageTemp1").html());
//				return false;
//			}
//		});
		
		param = checked.nextAll().serialize();
	}
	var refreshPrivilegeUrl = $("#refreshPrivilegeUrl").val();
	var callback = function() {
		if(oTableArr[0] != null)oTableArr[0].fnDraw();
		if($('#treeLayoutDiv').length > 0) {
			bsemp01_employeeDetail($('#employeeId').val());
			if(flag == 'enable') {
				$("#employeeTree").find("#"+$('#employeeId').val()).removeClass('jstree-disable');
			} else {
				$("#employeeTree").find("#"+$('#employeeId').val()).addClass('jstree-disable');
			}
		}
		if(refreshPrivilegeUrl) {
			cherryAjaxRequest({
				url: refreshPrivilegeUrl,
				param: null,
				reloadFlg : true,
				callback: function(msg) {
				}
			});
		}
	};
	bscom03_deleteConfirm(flag, url, param, callback);
}

/**
 * 收货部门弹出框
 * @param thisObj
 * */
function openDepartBox(thisObj){
	// 促销品弹出框属性设置
	var option = {
		targetId: "inOrganization_ID",//目标区ID
		checkType : "radio",// 选择框类型
		mode : 2, // 模式
		brandInfoId :$("#BIN_BrandInfoID").val(),// 品牌ID
		getHtmlFun:function(info){// 目标区追加数据行function
			var $selected = $('#departDataTableInit').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
//				$("#inOrganizationId").val(departId);
//				$("#departNameReceive").text("("+departCode+")"+departName);
//				chooseDepart();
				
				var departNameReceive = "("+departCode+")"+departName;
				var html = '<tr><td  id="inOrganization"><span class="list_normal" t>';
				html += '<span class="text" style="line-height:19px;">' + departNameReceive + '</span>';
				html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
	 			html += '<input type="hidden" name="organizationId" value="' + departId + '"/>';
	 			html += '</span></td></tr>';
	 			return html;
	 			
			}
			
		}
	};
	// 调用促销品弹出框共通
	popAjaxDepDialog(option);
}
/**
 * 删除显示标签
 * @param thisObj
 * */
function delPrmLabel(obj){
	$(obj).parent().parent().parent().remove();
	
}
/*
 * 导出Excel
 */
function exportExcel(urls) {
	// 无数据不导出
	if ($(".dataTables_empty:visible").length == 0) {
		if (!$('#mainForm').valid()) {
			return false;
		}
		;
		var url = urls;
		var params = $("#mainForm").find("div.column").find(":input")
				.serialize();
		params = params + "&csrftoken=" + $("#csrftoken").val();
		params = params + "&" + getRangeParams();
		url = url + "?" + params;
		window.open(url, "_self");
	}
}
