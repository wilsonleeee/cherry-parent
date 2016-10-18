var BINOLCTRPT01_GLOBAL = function() {

};

BINOLCTRPT01_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#mainForm');
		if(!$form.valid()){
			return false;
		}
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "batchId", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "planCode", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "planName", "sWidth" : "10%"},
		                    {"sName" : "activityCode", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "activityName", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "communicationCode", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "communicationName", "sWidth" : "10%"},
		                    {"sName" : "channelName", "bVisible" : false, "sWidth" : "10%"},
		                    {"sName" : "counterCode", "bVisible" : false, "sWidth" : "10%"},
		                    {"sName" : "counterName", "bVisible" : false, "sWidth" : "10%"},
		                    {"sName" : "commType", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "runType", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "runBeginTime", "sWidth" : "5%"},
		                    {"sName" : "sendMsgNum", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "sendErrorNum", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "runStatus", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "runError", "bVisible" : false,"sWidth" : "10%", "bSortable" : false},
		                    {"sName" : "act", "sWidth" : "5%", "bSortable" : false} 
		                    ];
		
		var url = $("#planRunSearchUrl").attr("href");
		var params = $form.serialize();
		url = url + "?" + params;
		$("#runDetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : "#dataTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 13, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 7 , 13 , 14 , 15 ,16],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			callbackFun : function(msg) {
				var $msg = $("<div></div>").html(msg);
		 		var $totalInfo = $("#totalInfo",$msg);
		 		if($totalInfo.length > 0){
		 			$("#totalInfo").html($totalInfo.html());
		 		}else{
		 			$("#totalInfo").empty();
			 	}
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	"changeChannel" : function(){
		$('#counterCode').val("");
		$('#counterName').val("");
		var channelId = $("#channelId option:selected").val();
		if(channelId != null && channelId != "" && channelId != undefined){
			$('#counterName').removeAttr("disabled");
			$('#counterName').prop("class","text");
			// 柜台下拉框绑定
			var option = {
				elementId:"counterName",
				showNum:20,
				targetId:"counterCode",
				paramType:"1",
				paramValue:channelId,
				targetDetail:true,
				afterSelectFun:BINOLCTRPT01.ctAfterSelectFun
			};
			counterSelectBinding(option);
		}else{
			$('#counterName').attr("disabled","true");
			$('#counterName').prop("class","text disabled");
		}
	},
	
	"ctAfterSelectFun":function(info){
		$('#counterCode').val(info.counterCode);
		$('#counterName').val(info.counterName);
	},
	
	"exportExcel" : function(url,exportFormat){
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
            var params= $("#mainForm").serialize();
            params = params + "&" +getSerializeToken() + "&exportFormat=" +exportFormat;
            if(exportFormat == "0"){
	            url = url + "?" +params;
	            document.location.href = url;
            }else{
	            exportReport({
					exportUrl: url,
					exportParam: params
				});
            }
		}
	}
};

var BINOLCTRPT01 = new BINOLCTRPT01_GLOBAL();

$(document).ready(
	function(){
		$("#startTime").cherryDate({
			beforeShow: function(input){
				var value = $("#endTime").val();
				return [value, "maxDate"];
			}
		});
		$("#endTime").cherryDate({
			beforeShow: function(input){
				var value = $("#startTime").val();
				return [value, "minDate"];
			}
		});
		cherryValidate({//form表单验证
			formId: "mainForm",		
			rules: {
				startTime:{dateValid: true},
				endTime:{dateValid: true}
			}		
		});
	}
);
