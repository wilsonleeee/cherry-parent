
function BINOLPTJCS44() {
	
};

BINOLPTJCS44.prototype = {
		"searchList":function(){
			var url = $("#searchUrl").attr("href");
			 // 查询参数序列化
			 var params= $("#mainForm").find("div.column").find(":input").serialize();
			 params = params + "&csrftoken=" +$("#csrftoken").val();
			 url = url + "?" + params;
			// 显示结果一览
			 $("#section").show();
//			 表格设置
			 var tableSetting = {
					 // datatable 对象索引
					 index : 1,
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 2, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [	
					              	{ "sName": "checkbox","bSortable": false,"sWidth": "2%"},			
									{ "sName": "number"},
									{ "sName": "nameTotal"},
									{ "sName": "barCode"},
									{ "sName": "spec"},
									{ "sName": "memPrice"},
									{ "sName": "salePrice"}
									//,
									//{ "sName": "printStatus","sClass":"center"}
									],			
									
					// 不可设置显示或隐藏的列	
					aiExclude :[0, 1],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 滚动体的宽度
					sScrollXInner:"",
					//默认显示的数据条数
					iDisplayLength : 21,
					//可选的显示页数
					aLengthMenu: [[21], [21]],
					// 固定列数
					fixedColumns : 2,
					// html转json前回调函数
					callbackFun: function(msg){
				 		var $msg = $("<div></div>").html(msg);
				 		var $headInfo = $("#headInfo",$msg);
				 		if($headInfo.length > 0){
				 			$("#headInfo").html($headInfo.html());
				 		}else{
				 			$("#headInfo").empty();
				 		}
			 		},
			 		fnDrawCallback:function(){cleanPrintBill();getPrintTip("a.printed");}
			 };
			// 调用获取表格函数
			getTable(tableSetting);
		},
		"printAmount":function(obj){
//			var length=$('#print_param_hide input[name="billId"]').length;
//			if(length > 1){
				$('#print_param_hide input[name="pageId"]').val("BINOLPTJCS44_1");
				var originalBrand=$("#originalBrand").val();
				if(obj == 2){
					var tr_count=$("#dataTable_Cloned").find(".dataTables_empty").length;
					if(tr_count > 0){
						return false;
					}
				}
				printWebPosProductBill(obj,originalBrand);
//			}else{
//				$('#print_param_hide input[name="pageId"]').val("BINOLPTJCS44");
//				openPrintApp('Print','#result_list');
//			}
			
		},
		"searchPrint":function(obj){
			var dialogSetting = {
				dialogInit: "#printDialogInit",
				width: 1200,
				height: 750,
				title: $("#printDialogTitle").text(),
				closeEvent:function(){
					// 关闭弹出窗口
					removeDialog("#printDialogInit");
				}
			};
			openDialog(dialogSetting);
			
			var searchPrintPageUrl = $("#searchPrintPageUrl").attr("href");
			cherryAjaxRequest({
				url: searchPrintPageUrl,
				param: null,
				callback: function(data) {
					$("#printDialogInit").html(data);
					$("#productSearchStr").focus();
				}
			});
		}
};

var BINOLPTJCS44 =  new BINOLPTJCS44();
//初始化
$(document).ready(function() {
	
	
});

