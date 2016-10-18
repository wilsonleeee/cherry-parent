// 用户查询
function search(url){
	
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
			 aoColumns : [	
							{ "sName": "no","sWidth": "10%","bSortable": false}, 		// 1
							{ "sName": "manufacturerCode","sWidth": "16%"},				// 2
							{ "sName": "factoryName","sWidth": "16%"},					// 3
							{ "sName": "factoryNameShort","sWidth": "11%"},				// 4
							{ "sName": "legalPerson","sWidth": "12%"},					// 5
							{ "sName": "provinceName","sWidth": "12%"},					// 6
							{ "sName": "cityName","sWidth": "12%"},						// 7
							{ "sName": "validFlag","sWidth": "11%","sClass":"center"}],	// 8			
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
