
function BINOLBSRES01() {};

BINOLBSRES01.prototype = {	
	// 柜台查询
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
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [
			            { "sName": "checkBox", "sWidth": "1%", "bSortable": false},
			            { "sName": "resellerCode"},
						{ "sName": "resellerName"},
						{ "sName": "type"},
						{ "sName": "levelCode"},
						{ "sName": "parentResellerCode"},						
						{ "sName": "parentResellerName"},
						{ "sName": "regionName"},
						{ "sName": "provinceName"},
						{ "sName": "cityName"},						
						{ "sName": "legalPerson","bVisible": false},
						{ "sName": "mobile","bVisible": false},
						{ "sName": "telePhone","bVisible": false},
						{ "sName": "validFlag","sClass":"center"}
					],	
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	/* 
     * 导出Excel
     */
	/*"exportExcel" : function(){
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
    },*/
	
	/*
	 * Excel,CSV数据导出
	 * */
	"exportExcel" : function(url,exportFormat){
		//无数据不导出
        if($(".dataTables_empty:visible").length==0){
        	var params= $("#mainForm").serialize();
            params = params + "&" +getSerializeToken() + "&exportFormat=" +exportFormat;
            if(exportFormat=="0"){
            	url=url+"?"+params;
            	document.location.href=url;
            }else{
            	exportReport({
            		exportUrl:url,
            		exportParam:params            	
            	});
            }
        }
    },
	"checkSelectAll": function(checkbox){
		$("#errorMessage").empty();
		$('#dataTable').find(":checkbox").prop("checked", $(checkbox).prop("checked"));
		if ($(checkbox).prop("checked") && $("input@[id=validFlag][checked]").length>0){
		}
	},
	"checkSelect": function(checkbox){
		$("#errorMessage").empty();
		if($(checkbox).prop("checked")) {
	        if($("#dataTable input@[id=validFlag]").length == $("#dataTable input@[id=validFlag][checked]").length) {
	            $("input@[id=allSelect]").prop("checked",true);
	        }
	    } else {
	        $("input@[id=allSelect]").prop("checked",false);
	    }
	}
};

var BINOLBSRES01 =  new BINOLBSRES01();


$(document).ready(function() {
	BINOLBSRES01.search();
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


/*
 * 确认画面
 */
function confirmInit(object,confirmname){
	if(!this.validCheckBox()){
        return false;
    };
	var param = $(object).parent().find(':input').serialize();
	param += "&"+$("#dataTable").find(":checkbox[checked]").nextAll("#resellerInfoIdArr").serialize();
	var strText = '';
	var strTitle = '';
	if (confirmname == 'disable') {
		strText="#disableText";
		strTitle="#disableTitle";
	}else if(confirmname == 'enable'){
		strText="#enableText";
		strTitle="#enableTitle";
	}
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text:  $(strText).html(),
		width: 	500,
		height: 300,
		title: 	$(strTitle).text(),
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){confirmProcess($(object).attr("href"), param);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
};


function confirmProcess(url,param) {
	var callback = function(msg) {
		$("#dialogInit").html(msg);
		if($("#errorMessageDiv").length > 0) {
			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			$("#dialogInit").dialog( "option", {
				buttons: [{
					text: $("#dialogClose").text(),
				    click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
				}]
			});
		} else {
			removeDialog("#dialogInit");
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: '#mainForm'
	});
};

/*
 * 验证勾选，显示隐藏错误提示
 */
function validCheckBox(){
	var flag = true;
	var checksize = $("input@[id=validFlag][checked]").length;
	if (checksize == 0){
	    //没有勾选
		$("#errorMessage").html($("#errorMessageTemp").html());
        $('#errorMessage').show();
        flag = false;
        }
	return flag;
};

/*
 * 关闭Dialog
 */
function dialogClose(){
	this.doClose('#dialogInit');
	if (binOLBSCHA01_global.refresh) {
		this.search();
		binOLBSCHA01_global.refresh = false;
	}
};


