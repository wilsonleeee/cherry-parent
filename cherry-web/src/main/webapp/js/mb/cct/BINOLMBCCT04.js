var BINOLMBCCT04_GLOBAL = function () {
};

BINOLMBCCT04_GLOBAL.prototype = {
	"search" : function() {
		var $form = $('#ccMainForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "memCode", "sWidth" : "10%"},
		                    {"sName" : "memName", "sWidth" : "15%"},
		                    {"sName" : "gender", "sWidth" : "5%", "bSortable": false},
		                    {"sName" : "mobilePhone", "sWidth" : "15%", "bSortable": false},
		                    {"sName" : "counterName", "sWidth" : "20%"},
		                    {"sName" : "birthDay", "sWidth" : "10%"},
		                    {"sName" : "joinDate", "sWidth" : "10%"},
		                    {"sName" : "cardValidFlag", "sWidth" : "10%", "bSortable": false},
		                    {"sName" : "act", "sWidth" : "5%", "bSortable" : false} 
		                    ];
		var url = $("#searchMemberUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		var indexNum = $('#pageIndex').val();
		indexNum ++;
		$('#pageIndex').val(indexNum);
		$("#memberList").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#memberDataTable',
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 6, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 设置页面索引
			index : indexNum,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	"showDetail" : function(memId) {
		$("#customerSysId").attr("value",memId);
		$("#memberInfoId").attr("value",memId);
		$("#customerType").attr("value","1");
		$("#customerCode").attr("value","");
		$("#isMember").attr("value",1);
		$("#phoneInBox").html($("#memberCallBox").html());
		BINOLMBCCT01.addIssue();
		BINOLMBCCT01.getIssue();
		BINOLMBCCT01.getMemberInfo();
		// 设置页面样式
		if($("#customerType").val()=="0"){
			$("#leftPageDiv").attr("style","width:50%;overflow-y: auto;padding: 5px 0 0 0;");
			$("#rightPageDiv").attr("style","width:49%;overflow-y: auto;padding: 5px 0 0 0;");
		}else{
			$("#leftPageDiv").attr("style","width:50%;height:430px;overflow-y: auto;padding: 5px 0 0 0;");
			$("#rightPageDiv").attr("style","width:49%;height:430px;overflow-y: auto;padding: 5px 0 0 0;");
		}
		// 更新来电日志
		var $form = $('#ccMainForm');
		var url = $('#updateCallLogUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			reloadFlg:true,
			callback: function(data) {
			}
		});
	},
	
	"addCustomerInfo" : function() {
		var customerSysId = $("#ccBrandInfoId").val() + $("#customerNumber").val();
		$("#customerSysId").attr("value",customerSysId);
		$("#memberInfoId").attr("value","");
		$("#customerType").attr("value","0");
		$("#isMember").attr("value",2);
		$("#customerCode").attr("value",customerSysId);
		$("#phoneInBox").html($("#newNonMemberCallBox").html());
		BINOLMBCCT01.addIssue();
		BINOLMBCCT01.addCustomer();
		// 设置页面样式
		if($("#customerType").val()=="0"){
			$("#leftPageDiv").attr("style","width:50%;overflow-y: auto;padding: 5px 0 0 0;");
			$("#rightPageDiv").attr("style","width:49%;overflow-y: auto;padding: 5px 0 0 0;");
		}else{
			$("#leftPageDiv").attr("style","width:50%;height:430px;overflow-y: auto;padding: 5px 0 0 0;");
			$("#rightPageDiv").attr("style","width:49%;height:430px;overflow-y: auto;padding: 5px 0 0 0;");
		}
		// 更新来电日志
		var $form = $('#ccMainForm');
		var url = $('#updateCallLogUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			reloadFlg:true,
			callback: function(data) {
			}
		});
	}
}

var BINOLMBCCT04 = new BINOLMBCCT04_GLOBAL();

$(document).ready(function() {
	
});
