var BINOLSTSFH04 = function () {};

BINOLSTSFH04.prototype = {
		
	"search": function(){
		if (!$('#mainForm').valid()) {
			return false;
		};
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		 var url = $("#searchUrl").attr("href");
		 // 查询参数序列化
		 var params= $("#mainForm").find("div.column").find(":input").serialize();
		 params = params + "&csrftoken=" +$("#csrftoken").val();
		 params = params + "&" +getRangeParams();
		 url = url + "?" + params;
		 // 显示结果一览
		 $("#section").show();
		 // 表格设置
		 var tableSetting = {
				 // datatable 对象索引
				 index : 1,
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 1, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [	
				              	{ "sName": "checkbox","bSortable": false,"sWidth": "2%"},			
								{ "sName": "deliverNoIF"},
								{ "sName": "deliverNo","bVisible": false},
								{ "sName": "relevanceNo","bVisible": false},
								{ "sName": "importBatch"},
								{ "sName": "outDepartCodeName"},	
								{ "sName": "depotName","bVisible": false},
								{ "sName": "inDepartCodeName"},
								{ "sName": "logInvName", "bVisible":false},
								{ "sName": "totalQuantity","sClass":"alignRight"},
								{ "sName": "totalAmount","sClass":"alignRight","bVisible": false},
								{ "sName": "date"},
								{ "sName": "verifiedFlag","sClass":"center"},
								{ "sName": "tradeStatus"},
								{ "sName": "employeeName","bVisible": false},
								{ "sName": "employeeNameAudit","bVisible": false},
								{ "sName": "comments","bVisible": false}
								//,
								//{ "sName": "printStatus","sClass":"center"}
								],			
								
				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 滚动体的宽度
				sScrollXInner:"",
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
	"exportExcel" : function(url){
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		 // 查询参数序列化
		 var params= $("#mainForm").find("div.column").find(":input").serialize();
		 params += "&"+$("#print_param_hide").find(":input").serialize();
		 params += "&csrftoken=" +$("#csrftoken").val();
		 params = params + "&" +getRangeParams();
		 url = url + "?" + params;
		 window.open(url,"_self");
	},
	
	/**
	 * 删除显示标签
	 * @param obj
	 * @return
	 */
	"delPrmLabel":function(obj){
		$(obj).parent().parent().parent().remove();
	},
	
	/**
	 * 切换联动部门（发货部门/收货部门）
	 */
	/*"changeDepartInOutFlag":function(){
		if($("#departInOutFlag").val() == "outOrgan"){
			//发货部门
//			$("#lblDepReceive").removeClass("hide");
//			$("#lblDepDeliver").addClass("hide");
//			$("#inOrganization_ID").html("");
			
			$("#DEPART_DIV ul li a").each(function() {
				if($(this).attr("href") == "#tabs-dpotMode"){
					$(this).show();
				}
			});
		}else{
			//收货部门
//			$("#lblDepReceive").addClass("hide");
//			$("#lblDepDeliver").removeClass("hide");
//			$("#inOrganization_ID").html("");
			//发货单没有收货部门仓库，无法按仓库模式查询，隐藏仓库模式。
			$("#DEPART_DIV ul li a").each(function() {
				if($(this).attr("href") == "#tabs-dpotMode"){
					$(this).hide();
				}
			});
			if($("#DEPART_DIV ul li.ui-tabs-selected a").attr("href") == "#tabs-dpotMode"){
				$($("#DEPART_DIV ul li a:visible")[0]).click();
			}
		}
	}*/
};

var BINOLSTSFH04 = new BINOLSTSFH04();

$(function(){
	// 产品选择绑定
	productBinding({elementId:"productName",showNum:20});
	
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}		
	});
	
	/*$("#DEPART_DIV ul li a").each(function() {
		if($(this).attr("href") == "#tabs-dpotMode"){
			$(this).hide();
		}
	});*/
	
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
	
	//BINOLSTSFH04.search();
});
function ignoreCondition(_this){
	// 发货单据号
	var deliverNo = $.trim($("#deliverNo").val());
	// 关联单号
	var relevanceNo = $.trim($("#relevanceNo").val());
	
	if(deliverNo == "" && relevanceNo == ""){
		// 单据输入框为空时，日期显示
		$("#startDate").prop("disabled",false);
		$("#endDate").prop("disabled",false);
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(startDate == ""){
			$("#startDate").val($("#defStartDate").val());
		}
		if(endDate == ""){
			$("#endDate").val($("#defEndDate").val());
		}
		$("#COVERDIV_AJAX").remove(); //清除覆盖的DIV块
	}else{
		var $this = $(_this);
		var id = $this.prop("id");
		
		if(id == "deliverNo" && $this.val() != ""){
			$("#relevanceNo").val("");
		}
		if(id == "relevanceNo" && $this.val() != ""){
			$("#deliverNo").val("");
		}	
		// 单据输入框不为空时，日期隐藏
		var datecover=$("#dateCover");  //需要覆盖的内容块的ID
		requestStart(datecover);		//内容块覆盖一块DIV来实现不允许输入日期
		$("#startDate").prop("disabled",true);
		$("#endDate").prop("disabled",true);
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(startDate != ""){
			$("#defStartDate").val(startDate);
			$("#startDate").val("");
		}
		if(endDate != ""){
			$("#defEndDate").val(endDate);
			$("#endDate").val("");
		}
	}
}

/**
 * 收货部门弹出框
 * @param thisObj
 * */
function openDepartBox(thisObj){	 
	//取得所有部门类型
 	var param = "checkType=radio&privilegeFlg=1&businessType=1";
	var callback = function() {
		var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
		if($selected.length > 0) {
			var departId = $selected.find("input@[name='organizationId']").val();
			var departCode = $selected.find("td:eq(1)").text();
			var departName = $selected.find("td:eq(2)").text();
			var departNameReceive = "("+departCode+")"+departName;
			var html = '<tr><td><span class="list_normal">';
			html += '<span class="text" style="line-height:19px;">' + departNameReceive + '</span>';
			html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSTSFH04.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
 			html += '<input type="hidden" name="inOrganizationId" value="' + departId + '"/>';
 			html += '</span></td></tr>';
 			$("#inOrganization_ID").html("");
 			$("#inOrganization_ID").append(html);
		}else{
			$("#inOrganization_ID").val("");
		}
	};
	popDataTableOfDepart(thisObj,param,callback);
}
/**
 * 更改了部门
 * @param thisObj
 */
function chooseDepart(thisObj){	
	$("#databody > tr[id!='dataRow0']").remove();
}