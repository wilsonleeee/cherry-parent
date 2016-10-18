//查询
function search(){
	if (!$('#mainForm').valid()) {
		return false;
	};
	var url = $("#searchUrl").attr("href");
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
			 aaSorting : [[ 0, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [	
			              	{ "sName": "counterActId"},											// 2
							{ "sName": "activeName"}, 			            					// 3
							{ "sName": "prmCounter"},                          					// 4
							{ "sName": "prmPro"},												// 5
							{ "sName": "sendDate"},              // 6
							{ "sName": "startDate"},             // 7
							{ "sName": "endDate"},					// 9      
							{ "sName": "validFlag", "bVisible" : false,"sClass":"center"}
							],
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}
/**
 * 日历初始化事件绑定
 * @return
 */
function activeSearchCalEventBind(){	
	$('#startDate').cherryDate({
		holidayObj: holidays
	});
	$('#endDate').cherryDate({
		holidayObj: holidays,
		// 结束时间大于起始时间
		beforeShow: function(input){										
			var minDateStr = $('#startDate').val();	
			var dateStr = minDateStr.split("-");
			var minDate = new Date(parseInt(dateStr[0],10),parseInt(dateStr[1],10)-1,parseInt(dateStr[2],10));
			var newtimems=minDate.getTime()+(parseInt($('#spaceDays').html(),10)*24*60*60*1000);
			var maxDate = new Date();
			maxDate.setTime(newtimems);
			var maxDateStr = changeDateToString(maxDate);
			return [minDateStr,maxDateStr];									
		}
	});
}