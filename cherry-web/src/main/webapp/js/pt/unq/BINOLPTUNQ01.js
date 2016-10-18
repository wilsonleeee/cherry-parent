
function BINOLPTUNQ01() {};

BINOLPTUNQ01.prototype = {	
		
	// 查询唯一码生成记录
	"search" : function(){
		var url = $("#searchUrlID").val();
		// 查询参数序列化
		var params= $("#selectForm").serialize();
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
			aaSorting : [[ 3, "asc" ]],
			// 表格列属性设置
			aoColumns : [
			        { "sName": "checkbox","bSortable": false,"sWidth": "2%"},
			        { "sName": "boxCount", "sWidth": "2%", "bSortable": false, "sClass":"center" },
			        { "sName": "prtUniqueCodeBatchNo","sWidth": "5%"},
			        { "sName": "generateDate","sWidth": "12%"},
					{ "sName": "nameTotal"},
					{ "sName": "baCode","bVisible" : false},
					{ "sName": "unitCode","bVisible" : false},
					{ "sName": "generateCount","sClass":"alignRight","sWidth": "5%"},
					{ "sName": "exportExcelCount","sWidth": "5%"},
					{ "sName": "lastExportExcelTime","sWidth": "12%"},
					{ "sName": "prtUniqueCodeBatchDescribe","sWidth": "12%"},
					{ "sName": "boxCount","sClass":"center"}],
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			index : 1
		};
	
		// 调用获取表格函数
		getTable(tableSetting);
	},
	/* 
     * 导出Excel
     */
	"exportExcel" : function(prtUniqueCodeID,relexportExcelCount,prtUniqueCodeBatchNo){
		var _this = this;
		if(relexportExcelCount==0){
			// 未导出的数据直接导出
			_this.relExportExcel(prtUniqueCodeID,prtUniqueCodeBatchNo);
		}else{
			// 已导出的数据，弹框警示
			_this.excelConfirm(prtUniqueCodeID,prtUniqueCodeBatchNo);
		}
    },
    
	/* 
     * Excel导出确认（已导出的数据再次导出时需要确认）画面
     */
	"excelConfirm" : function(prtUniqueCodeID,prtUniqueCodeBatchNo){
		var _this = this;
		var title =$('#deleteTitle14').text(); 
		var text = $('#deleteMessage14').html();
		var dialogSetting = {
				dialogInit: "#dialogInitUnq01",
				text: text,
				width: 	500,
				height: 300,
				title: 	title,
				confirm: $("#dialogConfirm14").text(),
				cancel: $("#dialogCancel14").text(),
				confirmEvent: function(){_this.relExportExcel(prtUniqueCodeID,prtUniqueCodeBatchNo);},
				cancelEvent: function(){removeDialog("#dialogInitUnq01");}
			};
			openDialog(dialogSetting);
		
	},
	/* 
     * 正真导出Excel
     */
	"relExportExcel" : function(prtUniqueCodeID,prtUniqueCodeBatchNo){
//		//无数据不导出
//        if($(".dataTables_empty:visible").length==0){
//		    if (!$('#selectForm').valid()) {
//                return false;
//            };
//            var url = $("#exportExcelUrlID").val();
//            url=url+"?prtUniqueCodeID="+prtUniqueCodeID ;
//            var params= $("#selectForm").serialize();
//            params = params + "&csrftoken=" +$("#csrftoken").val();
//            url = url + "&" + params ;
//            window.open(url,"_self");          
//            setTimeout(function(){  BINOLPTUNQ01.search(); },10000); // 点击导出Excel10秒后刷新页面。可以实现相关功能，但是有缺陷
//            removeDialog("#dialogInitUnq01");   
//            BINOLPTUNQ01.search();
		
		var url = $("#updExlExpUrlID").val();
        url=url+"?prtUniqueCodeID=" + prtUniqueCodeID + "&prtUniqueCodeBatchNo=" + prtUniqueCodeBatchNo;
        var params= $("#selectForm").serialize();
        var callback = function(msg) {
        	var msgJson = eval("("+msg+")");
        	if(msgJson.errorCode == "0"){
        		removeDialog("#dialogInitUnq01");
        		// 导出Excel
        		var url1 = $("#exportExcelUrlID").val();
//        		// 导出Csv
//        		var url1 = $("#exportCsvUrlID").val();
        		url1=url1+"?prtUniqueCodeID="+prtUniqueCodeID + "&prtUniqueCodeBatchNo="+prtUniqueCodeBatchNo;
        		var params1= $("#selectForm").serialize();
        		url1 = url1 + "&" + params1 ;
        		window.open(url1,"_self");
        		
        	}
        	//页面刷新
        	BINOLPTUNQ01.search();
        };
        cherryAjaxRequest({
			url: url,
			param: params,
			callback: callback
		});
    },
    
/* ************************************************************************************************************* */
    
	// 取得错误信息HTML
	"getErrHtml" : function (text){
		var errHtml = '<div class="actionError"><ul><li><span>';
		errHtml += text;
		errHtml += '</span></li></ul></div>';
		return errHtml;
	},
	
	"getSuccHtml" : function(text){
		var errHtml = '<div style="" id="actionResultDiv" class="actionSuccess">';
		errHtml += '<ul class="actionMessage"><li><span>';
		errHtml += text;
		errHtml += '</span></li></ul></div>';
		return errHtml;
	},
    
    /**
     * 生成
     */
    "generateCode" : function(){
    	var that = this;
    	
		var url = $("#generateUnqCodeUrlID").val();
        var param = $('#genForm').serialize();
        
		// 清空按钮
		$("#dialogGenCodeInitDIV").dialog( "option", {
			buttons: []
		});
        $("#dialogGenCodeInitDIV").html('').append("执行中...，请稍候！");
        
        //移除错误提示信息和样式
        $("#errorText").remove();
        $("#nameTotals").removeClass('error');
        
        // 生成条件校验不通过的时候，一秒后自动隐藏"执行中...，请稍候！"对话框   	
     	setTimeout(function(){ if ($("#errorText").length > 0){removeDialog("#dialogGenCodeInitDIV"); }},1000);
    	
		var callback = function(msg) {
			
//			requestEnd();

			var msgJson = eval("("+msg+")");

			if(msgJson.errorCode == "0"){

				var ht = $("#operateSuccessId").clone();
				$("#dialogGenCodeInitDIV").html('').append(ht);
				
				// 刷新一览
				BINOLPTUNQ01.search();
				
				// 初始化生成条件
				$('#genForm')[0].reset();
				$("#prtVendorId").val('');
				$("#generateCountVal").val('');
				$("#generateCountID").text('');
				 
				// 取得新的批次号（再次生成时使用） 
				var cback = function(data){
					 $("#prtUniqueCodeBatchNo").val(data);
					 $("#prtUniqueCodeBatchNoSpan").text(data);
				};
				cherryAjaxRequest({
					url: $("#getNewPrtUnqBatchNoUrlID").val(),
					callback: cback
				});
				
			}else{
				// 显示结果信息
				var ht= $("#operateFaildId").clone();
				$("#dialogGenCodeInitDIV").html('').append(ht);
			}
			
			$("#dialogGenCodeInitDIV").dialog( "option", {
				buttons: [{
					text: "确定",
				    click: function(){
				    	   		removeDialog("#dialogGenCodeInitDIV");
			    		   }
				}]
			});
		};
		
//		loadImage();
		
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
    },
    
	/**
	 * 生成唯一码确认DIV
	 * 
	 * 
	 * */
	"generateCodeInit":function()
	{
		
		// 表单验证
		if (!$('#genForm').valid()) {
//			alert("valid faild");
			return false;
		}
		
		var _this = this;
		var text = "";
		var title = "";
//		text = '<p class="message"><span>'+$('#confirIsEnable').text();
		text = '<p class="message"><span>'+'请确认使用当前生成条件生成唯一码吗？';
//		title = $('#enableValTitle').text();
		title = '生成唯一码';
		var dialogSetting = {
				dialogInit: "#dialogGenCodeInitDIV",
				text: text,
				width: 	350,
				height: 200,
				title: 	title,
				confirm: $("#dialogConfirmIss").text(),
				cancel: $("#dialogCancelIss").text(),
				confirmEvent: function(){
					_this.generateCode();
//					removeDialog("#dialogIssuedInitDIV"); 
				},
				cancelEvent: function(){removeDialog("#dialogGenCodeInitDIV");}
		};
		openDialog(dialogSetting);
		
	},
	/* 
     * 导出Excel
     */
	"exportPDF" : function(prtUniqueCodeID,relexportExcelCount){
		var _this = this;
		
		var url = '/Cherry/common/BINOLCM99_exportPrtUnqQRPDF';
		url += "?" + getSerializeToken();
		url += "&exportType=pdf&prtUniqueCodeID=" + prtUniqueCodeID;
//		alert(url);
		window.open(url,"_self");
//		popup(url,{center:"no",height: 650,width: 880});
		return false;
    }
	
};

var BINOLPTUNQ01 =  new BINOLPTUNQ01();

$(document).ready(function() {
	
	// 日期控件
	$("#fromDate").cherryDate({
		beforeShow: function(input){
			var value = $(input).parents("p").find("input[name='toDate']").val();
			return [value, "maxDate"];
		}
	});
	$("#toDate").cherryDate({
		beforeShow: function(input){
			var value = $(input).parents("p").find("input[name='fromDate']").val();
			return [value, "minDate"];
		}
	});
	// 日期校验
	cherryValidate({
		formId: "selectForm",		
		rules: {
			fromDate: {dateValid: true},
			toDate: {dateValid: true}
	   }		
	});
	// 查询唯一码生成记录
	BINOLPTUNQ01.search();
	
	/* ************************************************************************************************************* */
});
