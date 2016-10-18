function BINOLPTJCS24(){
	this.modeFlg = "treeMode";
}
BINOLPTJCS24.prototype= {
	//查询
	"search" : function (url){
		 // 查询参数序列化
		 var params= $("#mainForm").serialize();
		 url = url + "?" + params;
		 // 表格设置
		 var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格列属性设置
				 aoColumns : [	
				              { "sName": "checkbox", "sWidth": "2%","bSortable": false}, 			// 0
				              	{ "sName": "no", "sWidth": "2%","bSortable": false}, 			// 1
				              	{ "sName": "nameTotalCN", "sWidth": "11%"},						// 2
				              	{ "sName": "NameAlias", "sWidth": "9%"},						// 3.....
				              	{ "sName": "nameTotalEN","bVisible" : false, "sWidth": "6.25%"},	// 4
				              	{ "sName": "unitCode", "sWidth": "11%"}, 			            // 5
								{ "sName": "barCode", "sWidth": "6%"},                          // 6
								{ "sName": "originalBrand", "sWidth": "6.25%"},					   	 // 7
								{ "sName": "itemType", "sWidth": "6.25%"}, 						 // 8
								{ "sName": "primaryCategoryBig", "sWidth": "6.25%"},               // 9
								{ "sName": "primaryCategoryMedium", "sWidth": "6.25%"},            // 10
								{ "sName": "primaryCategorySmall", "sWidth": "6.25%"},             // 11
								{ "sName": "mode","bVisible" : false, "sWidth": "6.25%"},							    // 12
								{ "sName": "moduleCode","bVisible" : false, "sWidth": "6.25%","bSortable": false},						// 13
								{ "sName": "standardCost","bVisible" : false,"sClass":"alignRight", "sWidth": "6.25%"}, // 14
								{ "sName": "orderPrice","bVisible" : false,"sClass":"alignRight", "sWidth": "6.25%"},	 // 15
								{ "sName": "salePrice","sClass":"alignRight", "sWidth": "6.25%"},					   	 // 16
								{ "sName": "memPrice","sClass":"alignRight", "sWidth": "6.25%"}, 						 // 17
								{ "sName": "validFlag","sClass":"center","bVisible" : false, "sWidth": "6.25%"},    						 // 18
								{ "sName": "global.page.enable","sClass":"center","bSortable": false, "sWidth": "6.25%","bVisible" : false} 					 // 19
								],		
				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1,4,12,14,15,18,19],
				aaSorting : [[2, "asc"]],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 固定列数
				fixedColumns : 2,
				//,
				//fnDrawCallback : function() {
				// 合并一列中相同的数值
				//	tableRowspan($("#dataTable"),[2]);
				//}
				// 显示title
//				fnDrawCallback: function() {
//					$("a.description").cluetip({
//						splitTitle: '|',
//					    width: '300',
//					    height: 'auto',
//					    cluetipClass: 'default',
//					    cursor:'pointer',
//					    arrows: false, 
//					    dropShadow: false
//					});
//				}
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	},
	// 模式切换
	"changeTreeOrTable" : function (object,url) {
		oTableArr = new Array(null,null);
		fixedColArr = new Array(null,null);
		$(window).unbind('resize');
		var $obj = $(object);
		this.modeFlg = $obj.attr('id');
		if($obj.attr('class').indexOf('display-tree') != -1) {
			if($obj.attr('class').indexOf('display-tree-on') != -1) {
				return false;
			} else {
				$obj.siblings().removeClass('display-table-on');
				$obj.addClass('display-tree-on');
			}
		} else {
			if($obj.attr('class').indexOf('display-table-on') != -1) {
				return false;
			} else {
				$obj.siblings().removeClass('display-tree-on');
				$obj.addClass('display-table-on');
			}
		}
		var callback = function(msg) {
			$("#treeOrtableId").html(msg);
		};
		cherryAjaxRequest({
			url: url,
			param: null,
			callback: callback,
			formId: '#mainForm'
		});
	},
	/*
	 * 数结构初始化
	 */
	"initTree" : function (){
		// 产品结果一览隐藏
		$("#resultByTree").hide();
		var that = this;
		$("#categoryTree").jstree({
			"plugins" : [ "themes", "json_data", "ui", "crrm","search" ],
			"core": {"animation":0},
			"themes" : {
		        "theme" : "sunny",
		        "dots" : true,
		        "icons" : false
		    },
			"json_data" : {
				"ajax" : {
					"url" : $("#categoryInitUrl").val(),
					"data" : function(n){var param = n.attr ? {"path" : n.attr("id")} : {}; 
					param.csrftoken = $('#csrftoken').val(); return param;}
				}
			},
			"search" : {
				"ajax" : {
					"url" : $("#locateCatUrl").attr("href"),
					"data" : function (str) {
						var param = {"locationPosition" : str}; 
						param.csrftoken = $('#mainForm').find(':input[name=csrftoken]').val(); 
						return param;
					}
				}
			}
		}).delegate('a','click', function() {
			that.searchByTree(this.id);
		}).bind("search.jstree", function (e, data) {
			$("#categoryTree").find("a.jstree-clicked").removeClass("jstree-clicked");
			var $searchObject = $("#categoryTree").find("a.jstree-search");
			if($searchObject.length > 0) {
				$searchObject.removeClass("jstree-search");
				$searchObject.addClass("jstree-clicked");
				// 打开选中的树形节点对应的右侧画面
				//that.searchByTree($searchObject.first().attr("id"));
			}
		});
	},
    // 树定位（定位产品分类）
    "locateCategory" :function(value){
		if(value) {
			$("#categoryTree").jstree("search",value);
		} else {
			var locationPosition = $("#locationPosition").val();
			if(locationPosition != "") {
				$("#categoryTree").jstree("search",locationPosition);
			}
		}
    },
	// 添加产品
	"addprt" : function (url) {
		var param = $('#mainForm').find(':input[name=csrftoken]').serialize();
		if($("#categoryTree").find('a.jstree-clicked').length > 0) {
			param += "&path=" + $("#categoryTree").find('a.jstree-clicked').parent().attr('id');
		}
		url = url + '?' + param;
		popup(url);
	},
	// 通过树查询产品
	"searchByTree" : function (value) {
		// 查询参数序列化
		var params= $("#mainForm").serialize();
		params += "&path=" + value;
		// 产品结果一览显示
		$("#resultByTree").show();
		var url = $("#search_url").val() + "?" + params;
		 // 表格设置
		 var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格列属性设置
				 aoColumns : [	
				                { "sName": "checkbox","bSortable": false,"bVisible" : false}, 			// 0
				              	{ "sName": "no","bSortable": false}, 			// 1
				              	{ "sName": "nameTotalCN"},						// 2
				              	{ "sName": "NameAlias" },						// 3.....
				              	{ "sName": "nameTotalEN","bVisible" : false},	// 4
				              	{ "sName": "unitCode"}, 			            // 5
								{ "sName": "barCode"},                          // 6
								{ "sName": "originalBrand","bVisible" : false},					   	 // 7
								{ "sName": "itemType","bVisible" : false}, 						 // 8
								{ "sName": "primaryCategoryBig","bVisible" : false},               // 9
								{ "sName": "primaryCategoryMedium","bVisible" : false},            // 10
								{ "sName": "primaryCategorySmall","bVisible" : false},             // 11
								{ "sName": "mode","bVisible" : false},			//12
								{ "sName": "moduleCode","bVisible" : false},    // 13
								{ "sName": "standardCost","bVisible" : false},	// 14
								{ "sName": "orderPrice","bVisible" : false},	// 15
								{ "sName": "salePrice","bVisible" : false},		// 16
								{ "sName": "memPrice","bVisible" : false},		// 17
								{ "sName": "validFlag","sClass":"center","bVisible" : false}, // 18
								{ "sName": "global.page.enable","sClass":"center","bVisible" : false} // 19
								],	// 11
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				aaSorting : [[2, "asc"]],
				fnDrawCallback:function(){
			 		// 样式设定
					if(bscom01_layout != null) {
						var $Info = $("#categoryInfo");
						var $treeDiv = $("#treeLayoutDiv");
						var height = $Info.height();
						if(height > 700) {
							$treeDiv.height(height+40);
						}else{
							$treeDiv.css({height: (700)});
						}
						bscom01_layout.resizeAll();
					}
					//tableRowspan($("#dataTable"),[2]);
		 		}
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	},
	
	/* 
     * 导出Excel
     */
	"exportExcel" : function(){
		//无数据不导出
        if($(".dataTables_empty:visible").length==0){
		    if (!$('#mainForm').valid()) {
                return false;
            };
            var url = $("#downUrl").attr("href");
            var params= $("#mainForm").find("div.column").find(":input").serialize();
            params = params + "&csrftoken=" +$("#csrftoken").val();
            url = url + "?" + params;
            window.open(url,"_self");
        }
    },

	/** 弹出启用按钮对话框 */
	"popDisOrEnableDialog" : function(validFlag,productId,unitCode,barCode,updateTime,modifyCount,prtVendorId) {
		var _this = this;
		var isU2M = $('#isU2M').val();
		// 一品多码
		if(isU2M == "true"){
			var url = $("#prtBarCodeVFUrl").html();
			var param = "productId=" + productId;
			param += "&barCode=" + barCode;
			param += '&' + $("#brandInfoId").serialize();
			param += "&csrftoken=" +$("#csrftoken").val();
			
			var callback = function(msg) {
				// 启用时，若存在有效产品编码条码时，则openDialog,否则直接调用_this.editValidFlag（）方法
				if(msg.indexOf("showUBMapDIV") != -1){
					var dialogSetting = {
							dialogInit: "#dialogInitDIV",
							bgiframe: true,
							width:  380,
							height: 150,
							text: msg,
							title: 	$("#enableValTitle").text(),
							confirm: $("#dialogConfirmOK").text(),
							confirmEvent: function(){removeDialog("#dialogInitDIV");}
					};
					openDialog(dialogSetting);
				} else{
					_this.confirmInit(validFlag,productId,unitCode,barCode,updateTime,modifyCount,null,prtVendorId);
				}
				
			};
			
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callback
			});
		} else {
			// 一品一码
			var url = $("#prtDetailUrl").html();
			var param = "validFlag=" + validFlag + "&productId=" + productId;
			param += "&unitCode=" + unitCode + "&barCode=" + barCode;
			param += '&' + $("#brandInfoId").serialize();
			param += "&csrftoken=" +$("#csrftoken").val();
			var callback = function(msg) {
				// 启用时，若存在有效产品编码条码时，则openDialog,否则直接调用_this.editValidFlag（）方法
				if(msg.indexOf("showUBMapDIV") != -1){
					var dialogSetting = {
							dialogInit: "#dialogInitDIV",
							bgiframe: true,
							width:  400,
							height: 150,
							text: msg,
							title: 	$("#enableValTitle").text()
							//,
							//confirm: $("#dialogConfirm").text(),
							//cancel: $("#dialogCancel").text(),
							//confirmEvent: function(){_this.editValidFlag(validFlag,productId,unitCode,barCode,updateTime,modifyCount,"1",prtVendorId);},
							//cancelEvent: function(){removeDialog("#dialogInitDIV");}
					};
					
					if(msg.indexOf("showUBMapDIV_enbale") != -1){
						dialogSetting.confirm = $("#dialogConfirmOK").text(),
						dialogSetting.confirmEvent = function(){removeDialog("#dialogInitDIV");}
					} else if (msg.indexOf("showUBMapDIV_extistBarcode") != -1) {
						dialogSetting.confirm = $("#dialogConfirmOK").text(),
						dialogSetting.confirmEvent = function(){removeDialog("#dialogInitDIV");}
					} else {
						dialogSetting.confirm = $("#dialogConfirm").text();
						dialogSetting.cancel = $("#dialogCancel").text();
						dialogSetting.confirmEvent = function(){_this.editValidFlag(validFlag,productId,unitCode,barCode,updateTime,modifyCount,"1",prtVendorId);},
						dialogSetting.cancelEvent = function(){removeDialog("#dialogInitDIV");}
					}
					
					openDialog(dialogSetting);
				} else{
					_this.confirmInit(validFlag,productId,unitCode,barCode,updateTime,modifyCount,"0",prtVendorId);
				}
				
			};
			
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callback
			});
		}
		
	},
	
	/**
	 * 产品实时下发确认DIV
	 * 
	 * 
	 * */
	"issuedInit":function()
	{
		var _this = this;
		var text = "";
		var title = "";
//		text = '<p class="message"><span>'+$('#confirIsEnable').text();
		text = '<p class="message"><span>'+'请确认现在下发产品数据吗？';
//		title = $('#enableValTitle').text();
		title = '产品实时下发';
		var dialogSetting = {
				dialogInit: "#dialogIssuedInitDIV",
				text: text,
				width: 	300,
				height: 200,
				title: 	title,
				confirm: $("#dialogConfirmIss").text(),
				cancel: $("#dialogCancelIss").text(),
				confirmEvent: function(){
					_this.issuedPrt();
//					removeDialog("#dialogIssuedInitDIV");
				},
				cancelEvent: function(){removeDialog("#dialogIssuedInitDIV");}
		};
		openDialog(dialogSetting);
	},
	"issuedPrt":function(){
		var url=$("#issuedPrtId").html();
		
		var param ="brandInfoId=" + $("#brandInfoId").val();
		param += "&csrftoken=" +$("#csrftoken").val();
		
		// 清空按钮
		$("#dialogIssuedInitDIV").dialog( "option", {
			buttons: []
		});
		$("#dialogIssuedInitDIV").html('').append("执行中...，请稍候！");
		
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				var map = eval("(" + msg + ")");
				if(map.result == "0"){
//					$("#dialogIssuedInitDIV").empty();
					// 显示结果信息
					var ht = $("#operateSuccessId").clone();
					$("#dialogIssuedInitDIV").html('').append(ht);
				} else if(map.result == "2"){
					// 品牌的系统配置项不支持当前功能，请联系管理员！
					var ht= $("#sysConfigNonSupportId").clone();
					$("#dialogIssuedInitDIV").html('').append(ht);
				} else{
//					$("#dialogIssuedInitDIV").empty();
					// 显示结果信息
					var ht= $("#operateFaildId").clone();
					$("#dialogIssuedInitDIV").html('').append(ht);
					
				}
				$("#dialogIssuedInitDIV").dialog( "option", {
					buttons: [{
						text: "确定",
					    click: function(){removeDialog("#dialogIssuedInitDIV");}
					}]
				});
//				removeDialog("#dialogIssuedInitDIV");
			}
		});
	},
	
	
	/**
	 * 弹出停用/启用确认DIV
	 * 
	 * 
	 * */
	"confirmInit":function(validFlag,productId,unitCode,barCode,updateTime,modifyCount,invlidUBFlag,prtVendorId)
	{
		var _this = this;
		var text = "";
		var title = "";
		if(validFlag=="0"){
		   text = '<p class="message"><span>'+$('#confirIsDisable').text();
		   title = $('#disableValTitle').text();
		}else{
		   text = '<p class="message"><span>'+$('#confirIsEnable').text();
		   title = $('#enableValTitle').text();
		}
		var dialogSetting = {
			dialogInit: "#dialogInitDIV",
			text: text,
			width: 	300,
			height: 200,
			title: 	title,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){_this.editValidFlag(validFlag,productId,unitCode,barCode,updateTime,modifyCount,"0",prtVendorId);},
			cancelEvent: function(){removeDialog("#dialogInitDIV");}
		};
		openDialog(dialogSetting);
	},
    
    /**
     * 停用/启用
     * invlidUBFlag 0:全部无效编码条码  1:存在有效编码条码
     */
    "editValidFlag" : function(validFlag,productId,unitCode,barCode,updateTime,modifyCount,invlidUBFlag,prtVendorId){
		var url=$("#disOrEnable").html();
		var brandInfoId = $("#brandInfoId").val();
		var param="brandInfoId=" + brandInfoId + "&validFlag=" + validFlag + "&productId=" + productId + "&unitCode=" + unitCode+ "&barCode=" + barCode;
		param += "&prtUpdateTime=" + updateTime + "&prtModifyCount=" + modifyCount + "&invlidUBFlag=" + invlidUBFlag + "&prtVendorId=" + prtVendorId;
		param += "&csrftoken=" +$("#csrftoken").val();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				removeDialog("#dialogInitDIV");
				if(oTableArr[0] != null){
					oTableArr[0].fnDraw();
				}
				
			}
		});
    },
	
	/**
	 * 批量停用（暂不包括启用）
	 */
	"editBatchValidFlag" : function(flag, url) {
		var param = "";
		if($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		param = $('#dataTable_Cloned :input[checked]').nextAll().serialize();
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		bscom03_deleteConfirm(flag, url, param, callback);
	}
	
};
var BINOLPTJCS24 = new BINOLPTJCS24();

$(function(){
	$("#export").live('click',function(){
		BINOLPTJCS24.exportExcel();
	});
});
