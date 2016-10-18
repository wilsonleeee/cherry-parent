function BINOLBSWEM04_03() {};

BINOLBSWEM04_03.prototype = {
	//新增
	"insert":function(url) {
		if(!$("#mainForm").valid()) {
			return false;
		}
		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		var callback = function(msg) {
			if($('#actionResultBody').length > 0) {
				if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:$('#mainForm').serialize(),
			callback:callback
		});
	},
	"select":function(url){
		var _this = this;
		var _agentLevel = $('#agentLevel').val();
		if("" != _agentLevel && null != _agentLevel) {
			var callback = function(data) {
				_this.selectDialogInit(data);
				_this.selectSearch();
			};
			cherryAjaxRequest({
				url:url,
				callback:callback
			});
		} else {
			alert("请选择级别！");
			return false;
		}
	},
	
	"selectDialogInit":function(data) {
		var _this = this;
		var $dialogId = $("#dialogInit");
		var dialogSetting = {
				dialogInit:$dialogId,
				text:data,
				width:640,
				height:'auto',
				minHeight:240,
				zIndex:9999,
				title:$("#selectSupreTitle").html(),
				confirm:$("#dialogConfirm").html(),
				confirmEvent:function() {
					_this.handle();
				},
				cancel:$("#dialogCancel").html(),
				cancelEvent:function() {
					removeDialog("#dialogInit");
				}
		};
		openDialog(dialogSetting);
	},
	
	"selectSearch":function() {
		var _agentLevel = $("#agentLevel").val();
		var url = $("#assignSearch_url").val();
		url += "?csrftoken=" + $("#parentCsrftoken").val() + "&agentLevel=" + _agentLevel;
		$('#empForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		var params = $("#empForm").serialize();
		if(params != null && params != "") {
			url = url + "&" + params;
		}
		//过滤自己和直属下级
		var _mainEmployeeId = $.trim($('#mainEmployeeId').val());
		if(null != _mainEmployeeId && "" != _mainEmployeeId) {
			url += "&mainEmployeeId="+_mainEmployeeId;
		}
		var index = 2;
		if(oTableArr[index]){
			oTableArr[index] = null;
		}
		var tableSetting = {
				// 一页显示页数
				iDisplayLength:10,
				// 表格ID
				tableId : '#empDataTable',
				// 数据URL
				url : url,
				// 排序
				//aaSorting:[[1, "asc"]],
				// 表格列属性设置
				aoColumns : [  { "sName": "No","sWidth": "5%","bSortable": false,"sClass":"center"},
				               { "sName": "EmployeeCode","sWidth": "15%","sClass":"center"},
				               { "sName": "employeeName","sWidth": "15%","sClass":"center"},
				               { "sName": "mobilePhone","sWidth": "15%","sClass":"center"},
				               { "sName": "agentLevel","sWidth": "15%","sClass":"center"}],
				index : index,
				colVisFlag: false,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback: function(){
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"handle":function() {
		var $popTable = $("#dialogInit");
		var $popInputs = $popTable.find("tbody").find(":input");
		var superMobile;
		var superName;
		var superLevel;
		var employeeId;
		$popInputs.each(function() {
			var $input = $(this);
			if($input.attr("checked") == "checked") {
				superMobile = $input.val();
				employeeId = $input.next().html();
				superName = $input.parent().next().next().html();
				superLevel = $input.parent().next().next().next().next().html();
			}
		})
		//判断下级人数
		if("" != employeeId && null != employeeId) {
			var getSubAmountUrl = "/Cherry/basis/BINOLBSWEM04_getSubAmount.action";
			var params = "employeeId=" + employeeId;
			var callback = function(data) {
				var dataJson = window.JSON.parse(data);
				if(dataJson.resultCode == "-1") {
					alert("下级人数已满，无法分配！");
					return false;
				} else {
					if("" != superMobile && null != superMobile) {
						$("#superMobileTemp").text(superName + '(' + superMobile + '-' + superLevel + ') ');
						$("#superMobile").attr("value", superMobile);
						removeDialog("#dialogInit");
					} else {
						alert("手机号为空!")
						return false;
					}
				}
			};
			cherryAjaxRequest({
				url:getSubAmountUrl,
				param:params,
				callback:callback
			})
		} else {
			return false;
		}
	}
};

var BINOLBSWEM04_03 =  new BINOLBSWEM04_03();

$(document).ready(function() {
	if(window.opener) {
		window.opener.lockParentWindow();
	}
	// 表单验证初期化
	/*cherryValidate({
		formId: 'mainForm',
		rules: {
			agentMobile: {required: true},
			agentName: {required: true},
			agentLevel: {required: true},
			superMobile: {required: true}
		}
	});*/
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		bscom03_showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		bscom03_showRegin(this,"cityTemp");
	});
});

window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};
