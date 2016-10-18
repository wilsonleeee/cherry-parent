var BINOLJNMAN15_GLOBAL = function () {
    
};
BINOLJNMAN15_GLOBAL.prototype = {
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
				BINOLJNMAN15.search();
			});
			
	},
		//用户查询
		"search" : function (){
			var $form = $('#mainForm');
			// 查询参数序列化
			var params= $form.serialize() + "&" + $("#campaignType").serialize();
			
			var aoColumnsArr = [	{ "sName": "no","sWidth": "5%","bSortable": false}, 				// 1
			                    	{ "sName": "campaignName","sWidth": "20%","bSortable": true},			// 2
			                    	{ "sName": "campaignFromDate","sWidth": "10%","bSortable": true},			// 2
			                    	{ "sName": "campaigntoDate","sWidth": "10%","bSortable": true},			// 2
			                    	{ "sName": "descriptionDtl","sWidth": "20%","bSortable": true},		// 3	
									{ "sName": "vdFlag","sWidth": "15%","bSortable": true},			// 4
            						{ "sName": "action","sWidth": "20%","bSortable": false}]; 			// 5
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
					 aaSorting : [[1, "desc"]],
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
		
		//启用/停用规则
		"ruleValid" : function(kbn, obj) {
			var url = $("#ruleValidUrl").attr("href");
			var param = $(obj).closest("tr").find("input[name='campaignId']").serialize();
			var option = {
					dialogId : "#dialogClear",
					cancelTxt : $("#cancelTxt").text(),
					confirmTxt : $("#sureTxt").text(),
					url : url
			};
			// 启用
			if ('1' == kbn) {
				option.title=$("#onRuleTitle").text();
				option.text = "<p class='message'><span>" + $("#onRuleDesp").text() + "</span></p>";
				option.param = param + "&validFlag=1";
			} else {
				option.title=$("#offRuleTitle").text();
				option.text = "<p class='message'><span>" + $("#offRuleDesp").text() + "</span></p>";
				option.param = param + "&validFlag=0";
			}
			BINOLJNMAN15.dialogPanel(option);
		},
		//执行停用/启用
		"execRuleValid" : function(url, param) {
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : function(msg){
					removeDialog("#dialogConfig");
					BINOLJNMAN15.search();
				}
			});
		},
		
		//弹出确认框
		"dialogPanel" : function(option) {
			var dialogSetting = {
					dialogInit: option.dialogId,
					text: option.text,
					width: 	500,
					height: 300,
					title: 	option.title,
					cancel: option.cancelTxt,
					cancelEvent: function(){
						removeDialog($(option.dialogId));
					}
				};
			if (option.confirmTxt) {
				dialogSetting.confirm = option.confirmTxt;
				dialogSetting.confirmEvent =  function(){
					BINOLJNMAN15.execRuleValid(option.url, option.param);
					removeDialog($(option.dialogId));
				};
				
			}
			openDialog(dialogSetting);
		}
}

var BINOLJNMAN15 = new BINOLJNMAN15_GLOBAL();
function search () {
	BINOLJNMAN15.search();
}

$(document).ready(function() {
	
	BINOLJNMAN15.search();
});