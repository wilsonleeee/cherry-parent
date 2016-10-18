$(document).ready(function() {
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
	search();
} );

//查询
function search(){
	if (!$('#mainForm').valid()) {
		return false;
	};
	var url = $("#searchUrl").text();
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
			 aaSorting : [[ 1, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [	
			              	{ "sName": "no","sWidth": "4%","bSortable": false}, 			// 0
							{ "sName": "primaryCategoryCode","sWidth": "14%"}, 			    // 1
							{ "sName": "primaryCategoryName","sWidth": "14%"},              // 2
							{ "sName": "secondryCategoryCode","sWidth": "14%"},				// 3
							{ "sName": "secondryCategoryName ","sWidth": "14%"},			// 4
							{ "sName": "smallCategoryCode","sWidth": "14%"},	            // 5
							{ "sName": "smallCategoryName ","sWidth": "14%", "bVisible" : false},	            // 6
							{ "sName": "validFlag","sWidth": "12%","sClass":"center", "bVisible" : false}],		// 7
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

//开启、停用促销品分类
/*function operate_type(dom,url,optFlag){
	// 判断控件是否可用
	if(isEnable(dom)){
		// 所有选中的CHECKBOX
		var checkedBox = $(".jquery_table").find(":checkbox[checked]");
		var params = checkedBox.serialize();
		// 没有CHECKBOX被选中
		if(params==''){
			alert("没有促销品分类被选中");
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
}*/

// 判断DOM控件的是否可用，可用返回true，反之false
/*function isEnable(dom){
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
	// 所有选中有效的分类CHECKBOX
	var dom1 = $("#dataTable.FixedColumns_Cloned").find(":checkbox[checked][value^='1']");
	// 所有选中无效的分类CHECKBOX
	var dom2 = $("#dataTable.FixedColumns_Cloned").find(":checkbox[checked][value^='0']");
	// 包含有效分类
	if(dom1.length > 0){
		$("#enable_type").addClass(disClass);
	}else{
		$("#enable_type").removeClass(disClass);
	}
	// 包含无效分类
	if(dom2.length > 0){
		$("#disable_type").addClass(disClass);
	}else{
		$("#disable_type").removeClass(disClass);
	}
}*/
