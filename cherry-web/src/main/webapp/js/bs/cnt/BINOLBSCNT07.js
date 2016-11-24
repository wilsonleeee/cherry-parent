
function BINOLBSCNT07() {};

BINOLBSCNT07.prototype = {
	// 积分计划柜台查询
	"search" : function(){
		var url = $("#search_url").val();
		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		// 查询参数序列化
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#section").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 排序列
			aaSorting : [[2, "desc"]],
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [

				{ "sName": "checkbox","bSortable": false,"sClass":"center"},
				{ "sName": "no","sWidth": "1%","bSortable": false,"sClass":"center"},
				{ "sName": "CounterCode","sClass":"center"},
				{ "sName": "counterName","sClass":"center"},
				{ "sName": "pointPlan","bSortable": false,"sClass":"center"},
				{ "sName": "explain","bSortable": false},
				{ "sName": "StartDate","sClass":"center"},
				{ "sName": "EndDate","sClass":"center"},
				{ "sName": "CurrentPointLimit","sClass":"center"},
				{ "sName": "employeeName","sClass":"center"},
				{ "sName": "Comment"}
			],
			// 不可设置显示或隐藏的列
			aiExcludeArr :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},

	/*
	 * 积分计划导出Excel
	 */
	"exportExcel" : function(){
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
			if (!$('#mainForm').valid()) {
				return false;
			};
			var url = $("#downUrl").attr("href");
			var params= $("#mainForm").serialize();
			params = params + "&csrftoken=" +$("#csrftoken").val();
			url = url + "?" + params;
			window.open(url,"_self");
		}
	},
	/*"operatePointPlan" : function(param,url) {
		var callback = function(msg) {
			removeDialog("#dialogInit");
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},*/
	"operatePointPlanPop" : function(operateType,url) {
/*		$("#errorMessage").html("");
		//判断执行结果div是否隐藏
		if ($('#actionResultDiv').css('display') == 'block') {
			$('#actionResultDiv').css('display', 'none');
		}

		var param = "";
		if ($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#EMO00031").html());
			return false;
		}

		var callback = function () {
			if (oTableArr[0] != null)oTableArr[0].fnDraw();
		};*/
		//得到所有被选中的员工ID
		var obj = document.getElementsByName("validFlag");
		if(obj.size>1){
			$("#errorMessage").html($("#ECNT001").html());	//只能选择一个柜台进行该操作，请重新选择！
			return false;
		}

		var organizationIdArr = document.getElementsByName("organizationId");
		var startDateArr = document.getElementsByName("startDate");
		var organizationId = '';
		var startDate = '';

		for(var k in obj){
			if(obj[k].checked){
				organizationId=organizationIdArr[k].value;
				startDate=startDateArr[k].value;
			}
		}
		var param = "organizationId="+organizationId;
		var title = '';
		var operateInitUrl = '';
		if (operateType == 'enable'){
			title = $("#enableTitle").text();
			operateInitUrl = $("#enableInitUrl").attr("href");
		}else if(operateType == 'disable'){
			title = $("#disableTitle").text();
			operateInitUrl = $("#disableInitUrl").attr("href");
			param = param+"&startDate="+startDate;
		}else {
			title = $("#pointChangeTitle").text();
			operateInitUrl = $("#pointChangeInitUrl").attr("href");
			param = param+"&startDate="+startDate;
		}

		var dialogSetting = {
			dialogInit: "#dialogInit",
			width: 	480,
			height: 300,
			title: 	title,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){
				if (operateType == 'enable'){
					param = param+"&startDate="+$("#startDate").val();
					alert(param);
				}else if(operateType == 'disable'){
					param = param+"&endDate="+$("#endDate").val();
				}else {
					param = param+"&pointChange="+$("#pointChange").val()+"&comment="+$("#comment").val();
				}
				var callbackOp = function(msg) {
					removeDialog("#dialogInit");
				};
				cherryAjaxRequest({
					url: url,
					param: param,
					callback: callbackOp
				});
			},
			cancelEvent: function(){
				removeDialog("#dialogInit");
			}
		};
		openDialog(dialogSetting);
		var callback = function(msg) {
			$("#dialogInit").html(msg);
		};
		cherryAjaxRequest({
			url: operateInitUrl,
			param: null,
			callback: callback,
			formId: '#mainForm'
		});
	}
};

var binolbscnt07 =  new BINOLBSCNT07();



$(document).ready(function() {

	binolbscnt07.search();

});

