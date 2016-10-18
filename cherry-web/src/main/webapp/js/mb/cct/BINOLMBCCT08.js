var BINOLMBCCT08_GLOBAL = function () {
};

BINOLMBCCT08_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#getCustomerForm');
		if(!$form.valid()){
			return false;
		}
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "customerCode", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "customerName", "sWidth" : "5%"},
		                    {"sName" : "gender", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "mobilePhone", "sWidth" : "5%"},
		                    {"sName" : "telephone", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "birthDay", "sWidth" : "5%"},
		                    {"sName" : "joinTime", "sWidth" : "5%"},
		                    {"sName" : "customerType", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "company", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "post", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "industry", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "zip", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "messageId", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "email", "bVisible" : false,"sWidth" : "10%"},
		                    {"sName" : "province", "bVisible" : false,"sWidth" : "5%"},
		                    {"sName" : "city", "bVisible" : false,"sWidth" : "5%"},
		                    {"sName" : "address", "bVisible" : false,"sWidth" : "10%", "bSortable" : false},
		                    {"sName" : "memo", "bVisible" : false,"sWidth" : "10%", "bSortable" : false},
		                    {"sName" : "isReceiveMsg", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "act", "sWidth" : "5%", "bSortable" : false} 
		                    ];
		
		var url = $("#customerSearchUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		$("#customerDetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : "#customerDataTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 6, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 1 , 3 , 5 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 设置页面索引
			index : 151,
			// 固定列数
			fixedColumns : 0,
			fnDrawCallback : function(msg) {
				$('#customerDataTable').find("a.description").cluetip({
					splitTitle: '|',
				    width: 500,
				    height: 'auto',
				    cluetipClass: 'default', 
				    cursor: 'pointer',
				    showTitle: false
				});
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	"exportExcel" : function(url,exportFormat){
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
            var params= $("#getCustomerForm").serialize();
            params = params + "&" + getSerializeToken() + "&exportFormat=" + exportFormat;
            if(exportFormat == "0"){
	            url = url + "?" + params;
	            document.location.href = url;
            }else{
	            exportReport({
					exportUrl: url,
					exportParam: params
				});
            }
		}
	},
	
	// 编辑非会员资料
	"edit" :function(customerCode){
		var dialogSetting = {
			dialogInit: "#customerDialog",
			width: 1000,
			height: 500,
			title: $("#customerDialogTitle").text()
		};
		openDialog(dialogSetting);
		
		var setUrl = $("#addCustomerUrl").attr("href");
		var param = "customerSysId=" + customerCode + "&pageType=E";
		var callback = function(msg) {
			$("#customerDialog").html(msg);
		};
		cherryAjaxRequest({
			url: setUrl,
			param: param,
			callback: callback
		});
	},
	
	// 查看非会员资料
	"showDetail" :function(customerCode){
		var dialogSetting = {
			dialogInit: "#customerDialog",
			width: 1000,
			height: 500,
			title: $("#customerDialogTitle").text()
		};
		openDialog(dialogSetting);
		
		var setUrl = $("#addCustomerUrl").attr("href");
		var param = "customerSysId=" + customerCode + "&pageType=S";
		var callback = function(msg) {
			$("#customerDialog").html(msg);
		};
		cherryAjaxRequest({
			url: setUrl,
			param: param,
			callback: callback
		});
	}
	
}

var BINOLMBCCT08 = new BINOLMBCCT08_GLOBAL();

$(document).ready(function() {
	$("#joinTimeStart").cherryDate({
		beforeShow: function(input){
			var value = $("#joinTimeEnd").val();
			return [value, "maxDate"];
		}
	});
	$("#joinTimeEnd").cherryDate({
		beforeShow: function(input){
			var value = $("#joinTimeStart").val();
			return [value, "minDate"];
		}
	});
	cherryValidate({
		//form表单验证
		formId: "getCustomerForm",		
		rules: {
			joinTimeStart:{dateValid: true},
			joinTimeEnd:{dateValid: true}
		}		
	});
});
