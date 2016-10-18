
$(document).ready(function() {
	//类别树形结构
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
	
	//表格交互
//	$('thead th').click(function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});//表格列选中
	$('tbody tr').mouseover(function(){
		$(this).addClass('highlighted');
	});
	$('tbody tr').mouseout(function(){
		$(this).removeClass('highlighted');
	});//表格行高亮
	
	// 选择CHECKBOX
	/*$("#dataTable.FixedColumns_Cloned").find(":checkbox").live("click",function(){
		var notChecked = $("#dataTable.FixedColumns_Cloned").find(":checkbox:not([checked])");
		if(notChecked.length == 0){
			$("#selectAll").attr("checked",true);
		}else{
			$("#selectAll").attr("checked",false);
		}
		// 按钮可用性设置
		setDom();
	});*/
} );

//列表与树形模式的切换
function changeTreeOrTable(object,url) {
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
		formId: '#categoryCherryForm'
	});
}

//类别添加按钮
function addCategory(url) {
	var param = $('#categoryCherryForm').find(':input[name=csrftoken]').serialize();
	if($("#categoryTree").find('a.jstree-clicked').length > 0) {
		param += "&path=" + $("#categoryTree").find('a.jstree-clicked').parent().attr('id');
	}
	url = url + '?' + param;
	popup(url);
}

//类别详细
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

//刷新类别节点
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

//查询
function search(url){
	if (!$('#mainForm').valid()) {
		return false;
	};
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
			 // 表格默认排序
			 aaSorting : [[ 2, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [	
			              	{ "sName": "no","sWidth": "5%","bSortable": false},              //0
			              	{ "sName": "itemClassCode","sWidth": "30%"},					 //2
			              	{ "sName": "itemClassName","sWidth": "40%"},                     //1
							{ "sName": "curClassCode","sWidth": "15%"},	                     //3
							{ "sName": "validFlag","sWidth": "10%","sClass":"center"}],		 //4 
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

//根据品牌改变类别
function cate_changeBrand(object,select_default) {
	
	var callback = function(msg){
		var jsons = eval('('+msg+')');
		$('#path').empty();
		$('#path').append('<option value="">'+select_default+'</option>');
		if(jsons.higherCategoryList.length > 0) {
			for(var i in jsons.higherCategoryList) {
				$('#path').append('<option value="'+jsons.higherCategoryList[i].path+'">'+jsons.higherCategoryList[i].itemClassName+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: $('#filterByBrandInfoUrl').attr("href"),
		param: $(object).serialize(),
		callback: callback,
		formId: '#mainForm'
	});
	
}
//全选
/*function select_All(){
	if($("#selectAll").attr("checked")) {
		$("#dataTable.FixedColumns_Cloned").find(":checkbox").attr("checked",true);
	}else{
		$("#dataTable.FixedColumns_Cloned").find(":checkbox").removeAttr("checked");
	}
	// 按钮可用性设置
	setDom();
}*/

//开启、停用促销品
/*function operate_prm(dom,url,optFlag){
	// 判断控件是否可用
	if(isEnable(dom)){
		// 所有选中的CHECKBOX
		var checkedBox = $(".jquery_table").find(":checkbox[checked]");
		var params = checkedBox.serialize();
		// 没有CHECKBOX被选中
		if(params==''){
			alert("没有促销品类别被选中");
		 	return;
		}
		var url = url + "?optFlag=" + optFlag + "&" + params;
		url = url + "&csrftoken=" + $("#csrftoken").val() ;
		$.ajax({
	        url :url, 
	        type:'post',
	        dataType:'html',
	        success:function(res){
				if(res!=null && res!=''){
					// 添加、替换错误信息
					$("#errorMessage").html(res);
				}else{
					// 刷新表格数据
					oTableArr[0].fnDraw();
				}
			}
	    });
	}
}

// 判断DOM控件的是否可用，可用返回true，反之false
function isEnable(dom){
	var className = $(dom).attr("class");
	var index = className.indexOf("ui-state-disabled");
	if(index != -1){
		return false;
	}else{
		return true;
	}
}

//启用、禁用按钮 可用性设置
function setDom(){
	// 按钮不可以样式名
	var disClass = "ui-state-disabled";
	// 所有选中有效的促销品CHECKBOX
	var dom1 = $("#dataTable.FixedColumns_Cloned").find(":checkbox[checked][value^='1']");
	// 所有选中无效的促销品CHECKBOX
	var dom2 = $("#dataTable.FixedColumns_Cloned").find(":checkbox[checked][value^='0']");
	// 包含有效促销品
	if(dom1.length > 0){
		$("#enable_cate").addClass(disClass);
	}else{
		$("#enable_cate").removeClass(disClass);
	}
	// 包含无效促销品
	if(dom2.length > 0){
		$("#disable_cate").addClass(disClass);
	}else{
		$("#disable_cate").removeClass(disClass);
	}
}
*/