var BINOLMOWAT01 = function () {};

BINOLMOWAT01.prototype = {
	/*
	 * 用户查询
	 */
	"search": function(){
		if (!$('#mainForm').valid()) {
			return false;
		};
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		 var url = $("#searchUrl").attr("href");
		 // 查询参数序列化
		 var params= $("#mainForm").serialize();
		 url = url + "?" + params;
		 // 显示结果一览
		 $("#section").show();
		 
		 var searchType = $("[name='searchType']:checked").val();
		 var tableId = "#dataTable1";
		 var index = 1;
		 var sortIndex = 19;
		 var sort = "asc";
		 if(searchType == "date"){
			 tableId = "#dataTable2";
			 index = 2;
			 sortIndex = 18;
			 sort = "desc";
			 
			 $("#dataTable1").addClass("hide");
			 $("#dataTable2").removeClass("hide");
			 $("#dataTable1_wrapper").addClass("hide");
			 $("#dataTable2_wrapper").removeClass("hide");
			 
			 $("#setting2").addClass("setting2");
			 $("#setting2").removeClass("setting");
			 $("#setting2").addClass("hide");
			 $("#setting1").removeClass("setting1");
			 $("#setting1").addClass("setting");
			 $("#setting1").removeClass("hide");
		 }else{
			 $("#dataTable2").addClass("hide");
			 $("#dataTable1").removeClass("hide");
			 $("#dataTable2_wrapper").addClass("hide");
			 $("#dataTable1_wrapper").removeClass("hide");
			 
			 $("#setting1").addClass("setting1");
			 $("#setting1").removeClass("setting");
			 $("#setting1").addClass("hide");
			 $("#setting2").removeClass("setting2");
			 $("#setting2").addClass("setting");
			 $("#setting2").removeClass("hide");
		 }
		 // 表格设置
		 var tableSetting = {
				 // datatable 对象索引
				 index : index,
				 // 表格ID
				 tableId : tableId,
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ sortIndex, sort ]],
				 // 表格列属性设置
				 aoColumns : [	{ "sName": "no","sWidth": "1%","bSortable": false},
								{ "sName": "machineCode","sWidth": "10%"},
								{ "sName": "BrandNameChinese","bVisible": false,"sWidth": "10%"},
								{ "sName": "machineType","sWidth": "5%"},
								{ "sName": "softWareVersion","bVisible": false,"sWidth": "10"},
								{ "sName": "capacity","bVisible": false,"sWidth": "10%","sClass":"alignRight"},
								{ "sName": "internetFlow","bVisible": false,"sWidth": "10%","sClass":"alignRight"},
								{ "sName": "internetTime","bVisible": false,"sWidth": "10%","sClass":"alignRight"},
								{ "sName": "internetTimes","bVisible": false,"sWidth": "10%","sClass":"alignRight"},
								{ "sName": "uploadLasttime","bVisible": false,"sWidth": "10%"},
								{ "sName": "syncLasttime","bVisible": false,"sWidth": "10%"},
								{ "sName": "phoneCode","sWidth": "10%","sClass":"alignRight"},
								{ "sName": "provinceName","sWidth": "10%"},
								{ "sName": "cityName","sWidth": "10%"},
								{ "sName": "counterCodeName","sWidth": "10%"},
								{ "sName": "iMSIcode","bVisible": false,"sWidth": "10%","sClass":"alignRight"},
								{ "sName": "startTime","bVisible": false,"sWidth": "10%"},
								{ "sName": "lastStartTime","sWidth": "10%"},
								{ "sName": "lastConnTime","sWidth": "10%"}
							],
								
				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 滚动体的宽度
				sScrollXInner:"",
				// 固定列数
				fixedColumns : 2,
				fnDrawCallback : function() {
					//给每行设置各自的背景色
					$("tbody tr").each(function() {
						var connStatus = $(this).find("#connStatus").val();
						if (connStatus != null && connStatus != "") {
							switch(connStatus) {
								case '1'://正常
								    $(this).css("background-color", "#EBF5CC");
									break;
								case '2'://故障
								    $(this).css("background-color", "#FFE5E5");
									break;
                                case '3'://停用
								    $(this).css("background-color", "#D5D8DF");
                                    break;
							}
						}
					});
		 		},
		 		callbackFun:function(msg){
		 			var $msg = $("<div></div>").html(msg);
			 		var $headInfo = $("#headInfo",$msg);
			 		if($headInfo.length > 0){
			 			$("#headInfo").html($headInfo.html());
			 		}else{
			 			$("#headInfo").empty();
				 	}
		 		}
		 };
		 
		 if(searchType == "date"){
			 tableSetting.aoColumns.push({ "sName": "connStatus","bVisible": false,"sWidth": "5%"});
			 tableSetting.aoColumns.push({ "sName": "connDays","sWidth": "5%"});
		 }else{
			 tableSetting.aoColumns.push({ "sName": "connStatus","sWidth": "5%"});
		 }
		 
		 // 调用获取表格函数
		 getTable(tableSetting);
	},
	
	/* 
     * 导出Excel
     */
    "exportExcel" : function(){
        //无数据不导出
        if($(".dataTables_empty").length==0){
            if (!$('#mainForm').valid()) {
                return false;
            };
            var url = $("#downUrl").attr("href");
            var params= $("#mainForm").find("div.column").find(":input").serialize();
            params = params + "&csrftoken=" +$("#csrftoken").val();
            url = url + "?" + params;
            window.open(url,"_self");
        }
    }
};

var BINOLMOWAT01 = new BINOLMOWAT01();

$(function(){
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			dateDiff:{required:true,digits:true}, //最后联络时差
    		startDate: {dateValid:true}, // 开始日期
    		endDate: {dateValid:true} // 结束日期
		}
	});
	
	$("[name='searchType']").live('click',function(){
		if($("[name='searchType']:checked").val()=="minute"){
			$("#nuberOfDays").prop("disabled",true);
			$("#dateDiff").prop("disabled",false);
			$("#startDate").prop("disabled",true);
			$("#endDate").prop("disabled",true);
			
			$("#startDate").parent().find("#errorText").remove();
            $("#startDate").parent().removeClass("error");
			$("#endDate").parent().find("#errorText").remove();
            $("#endDate").parent().removeClass("error");
            
			$("#dateDiff").rules("add", {required: true});
			$("#startDate").rules("remove", "required");
			$("#endDate").rules("remove", "required");
		}else if($("[name='searchType']:checked").val()=="day"){
			$("#nuberOfDays").prop("disabled",false);
			$("#dateDiff").prop("disabled",true);
			$("#startDate").prop("disabled",true);
			$("#endDate").prop("disabled",true);
			
			$("#dateDiff").parent().find("#errorText").remove();
            $("#dateDiff").parent().removeClass("error");
			$("#startDate").parent().find("#errorText").remove();
            $("#startDate").parent().removeClass("error");
			$("#endDate").parent().find("#errorText").remove();
            $("#endDate").parent().removeClass("error");
            
			$("#dateDiff").rules("remove", "required");
			$("#startDate").rules("remove", "required");
			$("#endDate").rules("remove", "required");
		}else if($("[name='searchType']:checked").val()=="date"){
			$("#startDate").prop("disabled",false);
			$("#endDate").prop("disabled",false);
			$("#nuberOfDays").prop("disabled",true);
			$("#dateDiff").prop("disabled",true);
			
			$("#dateDiff").parent().find("#errorText").remove();
            $("#dateDiff").parent().removeClass("error");
            
			$("#dateDiff").rules("remove", "required");
			$("#startDate").rules("add", {required: true});
			$("#endDate").rules("add", {required: true});
		}
    });
	
	counterBinding({elementId:"counterCodeName",showNum:20,selected:"name"});
	
	BINOLMOWAT01.search();

});