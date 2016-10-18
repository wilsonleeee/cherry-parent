function BINOLSTIOS13(){
};

BINOLSTIOS13.prototype={
		"search" : function() {
			if(!$("#mainForm").valid()){
				return;
			}
			$("#mainForm").find(":input").each(function(){
				$(this).val($.trim(this.value));
			});
			var url = $("#search_Url").attr("href");
			var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			// 显示结果一览
			$("#productInDepotExcelInfo").removeClass("hide");
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#productInDepotExcelInfoDataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 2, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false},
					                { "sName": "importBatchCode", "sWidth": "30%" },
									{ "sName": "importDate", "sWidth": "15%" },
									{ "sName": "employeeCode", "sWidth": "20%" },
									{ "sName": "comments", "sWidth": "30%" }],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					fnDrawCallback: function() {
						$("a.description").cluetip({
							splitTitle: '|',
						    width: '300',
						    height: 'auto',
						    cluetipClass: 'default',
						    cursor:'pointer',
						    arrows: false, 
						    dropShadow: false
						});
					}
			};
			// 调用获取表格函数
			getTable(tableSetting);
	    }
};

var BINOLSTIOS13 = new BINOLSTIOS13();

$(document).ready(function(){
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			importStartTime: {dateValid:true},	// 开始日期
			importEndTime: {dateValid:true}	// 结束日期
		}		
	});
});

