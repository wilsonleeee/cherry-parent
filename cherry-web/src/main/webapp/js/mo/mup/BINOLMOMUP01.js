$(function(){

	search();

});

function search(){
		if (!$('#mainForm1').valid()) {
			return false;
		};
		$("#mainForm1").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		 var url = $("#searchUrl").attr("href");
         // 查询参数序列化
         var params= $("#mainForm1").find(":input").serialize();
         params = params + "&csrftoken=" +$("#csrftoken").val();
         url = url + "?" + params;
		 // 显示结果一览
		 $("#section1").show();
		 // 表格设置
		 var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable1',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[1,"desc"],[4,"desc"]],//
				 // 表格列属性设置
				 aoColumns : [	{ "sName": "BIN_SoftwareVersionInfoID","bSortable": false},
								{ "sName": "Version"},
								{ "sName": "DownloadURL"},
								{ "sName": "MD5Key"},
								{ "sName": "OpenUpdateTime"},
					 			{ "sName": "ValidFlag"}
							],
								
				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1,2],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 固定列数
				fixedColumns : 3,
				
				index:2
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	}
