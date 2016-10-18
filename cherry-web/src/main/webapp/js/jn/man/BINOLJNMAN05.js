var BINOLJNMAN05_GLOBAL = function () {
    
};

BINOLJNMAN05_GLOBAL.prototype = {
		
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
				BINOLJNMAN05.search();
			});
			
	},
	
		//用户查询
		"search" : function (){
			var $form = $('#mainForm');
			// 查询参数序列化
			var params= $form.serialize();
			
			var aoColumnsArr = [	{ "sName": "no","sWidth": "5%","bSortable": false}, 				// 1
			                    	{ "sName": "groupName","sWidth": "30%","bSortable": true},			// 2
			                    	{ "sName": "descriptionDtl","sWidth": "30%","bSortable": true},		// 3	
									{ "sName": "validFlag","sWidth": "15%","bSortable": true},			// 4
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
		//添加配置
		"openWinConfig" : function(obj, id) {
			var url = $(obj).attr("href");
			// 品牌ID
			var params = $("#brandSel").serialize() + "&csrftoken=" + $("#csrftoken").val();
			if ($("#memberClubId").length > 0) {
				params += '&' + $("#memberClubId").serialize();
			}
			if (id) {
				params += "&campaignGrpId=" + id;
			}
			url = url + "?" + params;
			popup(url);
		},
		//启用/停用配置
		"configValid" : function(kbn, id) {
			var url = $("#confValidUrl").attr("href");
			var param = $("#brandSel").serialize() + "&campaignGrpId=" + id;
			if ($("#memberClubId").length > 0) {
				param += '&' + $("#memberClubId").serialize();
			}
			var option = {
					dialogId : "#dialogConfig",
					cancelTxt : $("#cancelTxt").text(),
					url : url
			};
			// 启用
			if ('1' == kbn) {
				option.title=$("#useConfigTxt").text();
				var checkUrl = $("#confCheckUrl").attr("href");
				cherryAjaxRequest({
					url: checkUrl,
					param: param,
					callback: function(msg) {
						if(window.JSON && window.JSON.parse) {
							var msgJson = window.JSON.parse(msg);
							var ret = msgJson["RESULT"];
							if ("NG" == ret) {
								option.cancelTxt = $("#closeTxt").text();
								option.text = "<p class='message'><span>" + $("#stopConfFirstTxt").text() + msgJson["groupName"] + "</span></p>";
							} else {
								option.confirmTxt = $("#sureTxt").text();
								option.text = "<p class='message'><span>" + $("#useConfSureTxt").text() + "</span></p>";
								option.param = param + "&validFlag=1";
							}
							BINOLJNMAN05.dialogPanel(option);
						}
					}
				});
			} else {
				option.title=$("#unuseConfigTxt").text();
				option.confirmTxt = $("#sureTxt").text();
				option.text = "<p class='message'><span>" + $("#unuseConfSureTxt").text() + "</span></p>";
				option.param = param + "&validFlag=0";
				BINOLJNMAN05.dialogPanel(option);
			}
		},
		//执行停用/启用
		"execValid" : function(url, param) {
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : function(msg){
					removeDialog("#dialogConfig");
					BINOLJNMAN05.search();
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
					BINOLJNMAN05.execValid(option.url, option.param);
					removeDialog($(option.dialogId));
				};
				
			}
			openDialog(dialogSetting);
		}
};
var BINOLJNMAN05 = new BINOLJNMAN05_GLOBAL();

function search () {
	BINOLJNMAN05.search();
}


$(document).ready(function() {
	
	BINOLJNMAN05.search();
});