var BINOLJNMAN04_GLOBAL = function () {
    
};

BINOLJNMAN04_GLOBAL.prototype = {
		
		/*
		 * 切换品牌
		 */
		"changeBrand" : function() {
			$("#actionResultDisplay").empty();
			// 品牌ID
			var param = $("#brandSel").serialize();
			param += "&" + $('#csrftoken').serialize();
			if ($("#memberClubId").length > 0) {
				param += "&" + $("#memberClubId").serialize();
			}
			$("#memberClubId").empty();
			var clubUrl = $("#searchClubUrl").attr("href");
			doAjax2(clubUrl, "memberClubId", "clubName", $("#memberClubId"), param, null, function(message){
				if ($("#memberClubId").find("option").length == 0) {
						var str = '<option value="">--未设置--</option>';
						$("#memberClubId").append(str);
				}
				// 更新等级详细信息
				BINOLJNMAN04.search();
			});
			
	},
	
		//用户查询
		"search" : function (){
			var $form = $('#mainForm');
			var defurl = $("#checkDefRuleUrl").attr("href");
			// 查询参数序列化
			var params= $form.serialize();
			cherryAjaxRequest({
				url: defurl,
				param: params
			});
			if (!$form.valid()) {
				return false;
			};
			
			var aoColumnsArr = [	{ "sName": "campaignId","sWidth": "1%","bSortable": true},			// 1						
									{ "sName": "pointRuleType","sWidth": "10%","bSortable": false},						// 3
									{ "sName": "campaignFromDate","sWidth": "10%"},  					// 4
			                        { "sName": "campaigntoDate","sWidth": "4%"}, // 5
            						{ "sName": "action","sWidth": "4%","bSortable": false}]; // 5
			var aiExcludeArr = [0,1];	
			var url = $("#searchUrl").attr("href");
			 
			 params = params + "&csrftoken=" +$("#csrftoken").val();
			 url = url + "?" + params;
			 // 表格设置
			 var tableSetting = {
					 // datatable 对象索引
					 index : 1,
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 排序列
					 aaSorting : [[0, "desc"]],
					 // 表格列属性设置			 
					 aoColumns : aoColumnsArr,
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					callbackFun : function (){		 	
			 		}
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		//显示组合规则详细画面
		"openComb" : function(obj, id) {
			var url = $(obj).attr("href");
			// 品牌ID
			var params = "campaignId=" + id + "&csrftoken=" + $("#csrftoken").val();
			url = url + "?" + params;
			popup(url);
		},
		//显示组合规则详细画面
		"addComb" : function(obj) {
			var url = $(obj).attr("href");
			// 品牌ID
			var params = $("#brandSel").serialize() + "&csrftoken=" + $("#csrftoken").val();
			url = url + "?" + params;
			popup(url);
		}
};
var BINOLJNMAN04 = new BINOLJNMAN04_GLOBAL();

function search () {
	BINOLJNMAN04.search();
}


$(document).ready(function() {
	$('#fromDate').cherryDate();
	$("#toDate").cherryDate();
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			fromDate: {dateValid: true},
			toDate: {dateValid: true}
	   }		
	});
	BINOLJNMAN04.search();
});