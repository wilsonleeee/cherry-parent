var BINOLPTRPS25 = function () {};

BINOLPTRPS25.prototype = {
	"exportExcel" : function(url){
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		//无数据不导出
		if($(".dataTables_empty:visible").length == 0){
		    if (!$('#mainForm').valid()) {
		        return false;
		    };
			// 查询参数序列化
			var params= $("#mainForm").find("div.column").find(":input").serialize();
			params = params + "&csrftoken=" +$("#csrftoken").val();
			params = params + "&" +getRangeParams();
			url = url + "?" + params;
			window.open(url,"_self");
		}
	}
};

var BINOLPTRPS25 = new BINOLPTRPS25();

$(function(){
	// 产品选择绑定
	productBinding({elementId:"nameTotal",showNum:20});
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
		}
	});
	// 输入框trim处理
	$(":text").bind('focusout',function(){ 
		var $this = $(this);$this.val($.trim($this.val()));});
});
// 查询
function search(url){
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	// 显示查询结果
	$("#section").show();
	// 查询参数序列化
	 var params= $form.find("div.column").find(":input").serialize();
	 params = params + "&csrftoken=" +$("#csrftoken").val();
	 params = params + "&" +getRangeParams();
	 url = url + "?" + params;
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			// 排序
			 aaSorting:[[1, "desc"]],
			 // 表格列属性设置
			 aoColumns :   [{ "sName": "no","bSortable":false}, 							// 0
							{ "sName": "deliverNoIF"},										// 1
							{ "sName": "sendDepart"},										// 2
							{ "sName": "depotName"},										// 3
							{ "sName": "receiveDepart"},									// 4
							{ "sName": "quantity","sClass":"alignRight"},					// 5
							{ "sName": "amount","sClass":"alignRight"},						// 6
							{ "sName": "date"},												// 7
						    { "sName": "employeeName","bVisible": false}],					// 8			
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
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
	 		}
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}
function ignoreCondition(_this){
	var $this = $(_this);
	if($.trim($this.val()) == ""){
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
			html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
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
 * 删除显示标签
 * @param obj
 * @return
 */
function delPrmLabel(obj){
	$(obj).parent().parent().parent().remove();
}	