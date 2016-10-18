function BINOLBSWEM04_02() {};

BINOLBSWEM04_02.prototype = {
	"save":function(url, flag){
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
		var params = $('#mainForm').serialize();
		if(typeof(flag) != "undefined") {
			params += "&auditFlag=" + flag;
		}
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	//选择预留号
	"selectReservedCode":function(url) {
		var _this = this;
		var callback = function(data) {
			_this.selectReservedCodeInit(data);
			_this.selectReservedCodeSearch();
		};
		cherryAjaxRequest({
			url:url,
			callback:callback
		})
	},
	//弹出框初始化
	"selectReservedCodeInit":function(text) {
		var _this = this;
		var dialogId = $("#dialogInit");
		if($(dialogId).length == 0) {
			$('body').append('<div style="display:none" id="dialogInit"></div> ');
		} else {
			$(dialogId).empty();
		}
		var dialogSetting = {
			dialogInit:dialogId,
			text:text,
			width:560,
			height:'auto',
			minHeight:240,
			zIndex:9999,
			title:$("#selectReservedCode").html(),
			confirm:$("#dialogConfirm").html(),
			confirmEvent:function() {
				_this.selectReservedCodeHandle();
			},
			cancel:$("#dialogClose").html(),
			cancelEvent:function() {
				removeDialog(dialogId);
			}
		};
		openDialog(dialogSetting);
	},
	
	//预留号查询
	"selectReservedCodeSearch":function() {
		var url = $('#selectReservedCodeSearch_url').val();
		url += "?csrftoken=" + $('#parentCsrftoken').val();
		$('#reservedCodeForm:input').each(function() {
			$(this).val($.trim(this.value));
		});
		var params = $('#reservedCodeForm').serialize();
		if(null != params && "" != params) {
			url += "&" + params;
		}
		var index = 3;
		if(oTableArr[index]){
			oTableArr[index] = null;
		}
		var tableSetting = {
				// 一页显示页数
				iDisplayLength:10,
				// 表格ID
				tableId : '#reservedCodeDataTable',
				// 数据URL
				url : url,
				// 排序
				//aaSorting:[[1, "asc"]],
				// 表格列属性设置
				aoColumns : [  { "sName": "No","sWidth": "5%","bSortable": false,"sClass":"center"},
				               { "sName": "reservedCode","sWidth": "15%","sClass":"center"}],
				index : index,
				colVisFlag: false,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback: function(){
					
				}
		};
		getTable(tableSetting);
	},
	"selectReservedCodeFilter":function() {
		var url = $('#selectReservedCodeSearch_url').val();
		url += "?csrftoken=" + $('#parentCsrftoken').val();
		$('#reservedCodeForm:input').each(function() {
			$(this).val($.trim(this.value));
		});
		var params = $('#reservedCodeForm').serialize();
		if(null != params && "" != params) {
			url += "&" + params;
		}
		var oSettings = oTableArr[3].fnSettings();
		oSettings.sAjaxSource = url;
		oTableArr[3].fnDraw();
	},
	//选择预留号
	"selectReservedCodeHandle":function() {
		var $popTable = $('#dialogInit');
		var $popInputs = $popTable.find('tbody').find(':input');
		var departName;
		var reservedCode;
		var pre_suffix;//前后缀
		var pre_suffixArr = [];
		$popInputs.each(function() {
			var $input = $(this);
			if($input.attr('checked') == 'checked') {
				reservedCode = $input.val();
			}
		}) 
		if(null != reservedCode && "" != reservedCode) {
			pre_suffix = $('#pre_suffix').val();
			if(null != pre_suffix && "" != pre_suffix) {
				pre_suffixArr = pre_suffix.split('/');
				departName = pre_suffixArr[0] + reservedCode + pre_suffixArr[1];
				$('#departNameTemp').html(departName);
				$('#departName').val(departName);
			}
		}
		removeDialog($popTable);
	}
};

var BINOLBSWEM04_02 =  new BINOLBSWEM04_02();

$(document).ready(function() {
	if(window.opener) {
		window.opener.lockParentWindow();
	}
	// 表单验证初期化
	/*cherryValidate({
		formId: 'mainForm',
		rules: {
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
