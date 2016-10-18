$(document).ready(function() {
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
	//判断促销类别是否是促销礼品
	var value=$("#promCate").val();
	validateType(value);
	search();
} );

//判断促销类别是否是促销礼品
function validateType(value){	
	if(value!=''){
		if(value!='CXLP'){
			 $("#validate_mode").css("display","none");
			 $("#mode").get(0).options[0].selected = true;
		}else{
			$("#validate_mode").css("display","");
			$("#mode").get(0).options[0].selected = true;//第一个值被选中  
		}
	}else{
		 $("#validate_mode").css("display","none");
		 $("#mode").get(0).options[0].selected = true;
	}
}

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
			 // 表格列属性设置
			 aoColumns : [	
			              	{ "sName": "no","bSortable": false}, 								// 1
			              	{ "sName": "nameTotal"},											// 2
							{ "sName": "unitCode"}, 			            					// 3
							{ "sName": "barCode"},                          					// 4
							{ "sName": "promCate"},												// 5
							{ "sName": "primaryCategoryName", "bVisible" : false},              // 6
							{ "sName": "secondryCategoryName", "bVisible" : false},             // 7
							{ "sName": "smallCategoryName", "bVisible" : false},                // 8
							{ "sName": "isPosIss", "bVisible" : false},						// 11							
							{ "sName": "standardCost","sClass":"alignRight"},					// 9  
							{ "sName": "mode", "bVisible" : false},						// 12
							{ "sName": "validFlag","sClass":"center"}],							// 10
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}
/* 
 * 导出Excel
 */
 function exportExcel(){
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
}
 
 /**
  * 产品实时下发确认DIV
  * 
  * 
  * */
 function issuedInit(){

 	var _this = this;
 	var text = "";
 	var title = "";
// 	text = '<p class="message"><span>'+$('#confirIsEnable').text();
 	text = '<p class="message"><span>' + $("#issPrmMsg1").text() + '</span></p>';
// 	title = $('#enableValTitle').text();
 	title = $("#issPrmPrt").text();
 	var dialogSetting = {
 			dialogInit: "#dialogIssuedInitDIV",
 			text: text,
 			width: 	300,
 			height: 200,
 			title: 	title,
 			confirm: $("#dialogConfirmIss").text(),
 			cancel: $("#dialogCancelIss").text(),
 			confirmEvent: function(){
 				issuedPrt();
// 				removeDialog("#dialogIssuedInitDIV");
 			},
 			cancelEvent: function(){removeDialog("#dialogIssuedInitDIV");}
 	};
 	openDialog(dialogSetting);
 }
 function issuedPrt(){
 	var url=$("#issuedPrmId").html();
 	
 	var param ="brandInfoId=" + $("#brandInfoId").val();
 	param += "&csrftoken=" +$("#csrftoken").val();
 	
 	// 清空按钮
 	$("#dialogIssuedInitDIV").dialog( "option", {
 		buttons: []
 	});
 	$("#dialogIssuedInitDIV").html('').append($("#issLaunchingMsg").text());
 	
// 	$("#dialogIssuedInitDIV").html('').append("处理中...");
 	cherryAjaxRequest({
 		url: url,
 		param: param,
 		callback: function(msg) {
 			var map = eval("(" + msg + ")");
// 			alert(JSON.stringify(map));
 			if(map.result == "0"){
// 				$("#dialogIssuedInitDIV").empty();
 				// 显示结果信息
 				var ht = $("#operateSuccessId").clone();
 				$("#dialogIssuedInitDIV").html('').append(ht);
 			
 			} else if(map.result == "2"){
 				// 品牌的系统配置项不支持当前功能，请联系管理员！
 				var ht = $("#sysConfigNonSupportId").clone();
 				$("#dialogIssuedInitDIV").html('').append(ht);
 				
 			} else{
// 				$("#dialogIssuedInitDIV").empty();
 				// 显示结果信息
 				var ht= $("#operateFaildId").clone();
 				$("#dialogIssuedInitDIV").html('').append(ht);
 				
 			}
 			
 			$("#dialogIssuedInitDIV").dialog( "option", {
 				buttons: [{
 					text: $("#dialogConfirm").text(),
 				    click: function(){removeDialog("#dialogIssuedInitDIV");}
 				}]
 			});
// 			removeDialog("#dialogIssuedInitDIV");
 		}
 	});
 }
