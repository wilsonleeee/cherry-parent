var BINOLMBCLB01_GLOBAL = function () {
    
};

BINOLMBCLB01_GLOBAL.prototype = {
		//用户查询
		"search" : function (){
			var $form = $('#mainForm');
			// 查询参数序列化
			var params= $form.serialize();
			
			var aoColumnsArr = [	{ "sName": "no","sWidth": "5%","bSortable": false}, 				// 1
			                    	{ "sName": "clubName","sWidth": "20%","bSortable": true},			// 2
			                    	{ "sName": "clubCode","sWidth": "15%","bSortable": true},			// 3
			                    	{ "sName": "descriptionDtl","sWidth": "25%","bSortable": true},		// 4	
									{ "sName": "validFlag","sWidth": "15%","bSortable": true},			// 5
            						{ "sName": "action","sWidth": "20%","bSortable": false}]; 			// 6
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
		//俱乐部编辑OR详细
		"openWinConfig" : function(obj, id) {
			var url = $(obj).attr("href");
			// 品牌ID
			var params = "&csrftoken=" + $("#csrftoken").val();
			if (id) {
				params += "&memberClubId=" + id;
			}
			url = url + "?" + params;
			popup(url);
		},
		//停用俱乐部
		"clubValid" : function(obj, id) {
			var url = $("#clubValidUrl").attr("href");
			var param = "memberClubId=" + id;
			var option = {
					dialogId : "#dialogClubValid",
					cancelTxt : $("#cancelTxt").text(),
					confirmTxt : $("#sureTxt").text(),
					url : url
			};
			option.title=$("#stopClubTxt").text();
			option.param = param + "&validFlag=0";
			BINOLMBCLB01.dialogPanel(option);
		},
		
		//执行停用/启用
		"execValid" : function(url, param) {
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : function(msg){
					removeDialog("#dialogClubValid");
					BINOLMBCLB01.search();
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
					BINOLMBCLB01.execValid(option.url, option.param + "&" + $(":input[name='reason']", "#dialogClubValid").serialize());
					removeDialog($(option.dialogId));
				};
				
			}
			openDialog(dialogSetting);
		},
		//下发俱乐部
		"sendClub" : function(obj) {
			var url = $(obj).attr("href");
			var param = $("#brandSel").serialize();
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: function(msg) {
					if(window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
						var option = {
								dialogId : "#dialogSendClub",
								title : $("#sdClubTxt").text(), 
								cancelTxt : $("#closeTxt").text()
						};
						var ret = msgJson["RESULT"];
						if ("NG03" == ret) {
							option.text = "<p class='message'><span>" + $("#clubSendNG03Txt").text() + "</span></p>";
						} else if ("NG01" == ret) {
							option.text = "<p class='message'><span>" + $("#clubSendNG01Txt").text() + "</span></p>";
						} else if ("NG02" == ret) {
							option.text = "<p class='message'><span>" + $("#clubSendNG02Txt").text() + "</span></p>";
						} else {
							option.text = "<p class='message'><span>" + $("#clubSendOKTxt").text() + "</span></p>";
						}
						BINOLMBCLB01.dialogPanel(option);
					}
				}
			});
		}
		
		
}

var BINOLMBCLB01 = new BINOLMBCLB01_GLOBAL();

function search () {
	BINOLMBCLB01.search();
}


$(document).ready(function() {
	
	BINOLMBCLB01.search();
});
