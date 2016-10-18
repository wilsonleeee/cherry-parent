function BINOLPTJCS34(){
	this.modeFlg = "listMode";
}
BINOLPTJCS34.prototype= {
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
				              { "sName": "checkbox", "sWidth": "2%","bSortable": false}, 													// 0
				              	{ "sName": "no", "sWidth": "2%","bSortable": false}, 														// 1
				              	{ "sName": "nameTotalCN", "sWidth": "11%"},																	// 2
				              	{ "sName": "NameAlias", "sWidth": "9%"},																	// 3
				              	//{ "sName": "nameTotalEN","bVisible" : false, "sWidth": "6.25%"},	//  
				              	{ "sName": "unitCode", "sWidth": "6%"}, 			            											// 4
								{ "sName": "barCode", "sWidth": "6%"},                          											// 5
								{ "sName": "originalBrand", "sWidth": "4%"},					   	 										// 6
								//{ "sName": "itemType", "sWidth": "6.25%","bVisible" : false}, 						 //  
								{ "sName": "primaryCategoryBig", "sWidth": "4%"},               											// 7
								{ "sName": "primaryCategoryMedium", "sWidth": "4%"},            											// 8
								{ "sName": "primaryCategorySmall", "sWidth": "4%"},             											// 9
								//{ "sName": "mode","bVisible" : false, "sWidth": "6.25%"},							    //  
								//{ "sName": "moduleCode","bVisible" : false, "sWidth": "6.25%","bSortable": false},						//  
								//{ "sName": "standardCost","bVisible" : false,"sClass":"alignRight", "sWidth": "6.25%"}, //   
								//{ "sName": "orderPrice","bVisible" : false,"sClass":"alignRight", "sWidth": "6.25%"},	 // 
								
								{ "sName": "minSalePrice","bVisible" : false,"sClass":"alignRight", "sWidth": "4%"},	 					// 10
								{ "sName": "maxSalePrice","bVisible" : false,"sClass":"alignRight", "sWidth": "4%"},	 					// 11
								
								{ "sName": "salePrice","sClass":"alignRight", "sWidth": "4"},					   	 						// 12
								{ "sName": "memPrice","sClass":"alignRight", "sWidth": "4%"}, 						 						// 13
								{ "sName": "validFlag","sClass":"center","bVisible" : true, "sWidth": "4%"},    						 	// 14
								{ "sName": "global.page.enable","sClass":"center","bSortable": false, "sWidth": "4%","bVisible" : false}	// 15
								],		
				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1,4,15],
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
		var productPriceSolutionIDVal = $("#productPriceSolutionID").val();
		param += "&productPriceSolutionID="+ productPriceSolutionIDVal;
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		bscom03_deleteConfirm(flag, url, param, callback);
	},
	/**
	 * 在柜台的text框上绑定下拉框选项
	 * @param Object:options
	 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
	 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
	 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中柜台名称；"code":表示要选中的是柜台CODE；"codeName":表示的是选中的是(code)name。默认是"name"
	 * 
	 * */
	"originalBrandBinding" : function(options){
		var csrftoken = '';
		if($("#csrftokenCode").length > 0) {
			csrftoken = $("#csrftokenCode").serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftokenCode',window.opener.document).serialize();
			}
		} else {
			csrftoken = $('#csrftoken').serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftoken',window.opener.document).serialize();
			}
		}
		var strPath = window.document.location.pathname;
		var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
		var url = postPath+"/common/BINOLCM21_getCodes.action"+"?"+csrftoken;
		
		$('#'+options.elementId).autocompleteCherry(url,{
			extraParams:{
				originalBrandStr: function() { return $('#'+options.elementId).val();},
				codeType: options.codeType,
				//默认是最多显示50条
				number:options.showNum? options.showNum : 50,
				//默认是选中柜台名称
				selected:options.selected ? options.selected : "value1",
				brandInfoId:function() { return $('#brandInfoId').val();}
			},
		    search: function( event, ui ) {
		        // event 是当前事件对象
//		    	alert("search");
		    	$(":input[name=originalBrand]").val('');
		        // ui对象是空的，只是为了和其他事件的参数签名保持一致
		    },
			loadingClass: "ac_loading",
			minChars:1,
		    matchContains:1,
		    matchContains: true,
		    scroll: false,
		    cacheLength: 0,
		    width:230,
		    max:options.showNum ? options.showNum : 50,
			formatItem: function(row, i, max) {
				return escapeHTMLHandle(row[1]);//+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}).result(function(event, data, formatted){
			$('#originalBrandStr').val(data[1]);
			$('#originalBrand').val(data[0]);
			$('#originalBrandStrOld').val(data[1]);
		});
		
	},
	"clearVal" : function(){
		if($(":input[name=originalBrandStr]").val().trim() == ''){
			$(":input[name=originalBrand]").val('');
		}
		if($(":input[name=originalBrand]").val() == ''){
			$(":input[name=originalBrandStr]").val('');
		}
	},
	"clear" : function(){
		if($(":input[name=originalBrandStr]").val().trim() != $(":input[name=originalBrandStrOld]").val()){
			$(":input[name=originalBrand]").val('');
		}
	},
	
	
    /** ------------以下为添加产品明细维护------------------------------------------           */
	"openChkPrtDialogInit" : function(){
		BINOLPTJCS34.cleanMsgDIV();
		if(BINOLPTJCS34.disabOptBtn()==false)return;
		var _this = this;
		var $addPrtTable = $("#addPrtTable").clone();
		// 非小店云模式，去除条码显示
//		if($("#isPosCloud").val()==0){
//			$addPrtTable.find("#thBarCodeId").remove();
//		}
		$addPrtTable.find("#databody").attr('id','newDatabody');
//		$addPrtTable.find("#allSelect").attr('id','newAllSelect');
		
		var text = '<div id="prtSaveDiv">'  + $addPrtTable.html() + '</div>';
		var title = "";
//				text = '<p class="message"><span>'+$('#confirIsEnable').text();
//				text = '<p class="message"><span>'+'请确认现在下发柜台产品数据吗？';
//				title = $('#enableValTitle').text();
		title = '批量添加产品';
//		if($("#isPosCloud").val()==0){
//			title = '添加方案产品明细';
//		} else if($("#isPosCloud").val()==1){
//			title = '添加产品列表明细';
//		}
		var dialogSetting = {
				dialogInit: "#dialogChkPrtInitDIV",
				text: text,
				width: 	1050,
				height: 500,
				title: 	title,
//					confirm: $("#dialogConfirmIss").text(),
				confirm: '添加',
//					cancel: $("#dialogCancelIss").text(),
				cancel: '取消',
				confirmEvent: function(){
					
					BINOLPTJCS34.addRow();
					//_this.issuedPrt();
					removeDialog("#dialogChkPrtInitDIV");
				},
				cancelEvent: function(){removeDialog("#dialogChkPrtInitDIV");}
		};
		openDialog(dialogSetting);
	},
	"openSelPrtDialogInit" : function(){
		BINOLPTJCS34.cleanMsgDIV();
		if(BINOLPTJCS34.disabOptBtn()==false)return;
		var _this = this;
		var $selPrtTable = $("#selPrtTable").clone(true);
		// 非小店云模式，去除条码显示
//		if($("#isPosCloud").val()==0){
//			$addPrtTable.find("#thBarCodeId").remove();
//		}
		$selPrtTable.find("#databody").attr('id','newDatabody');
//		$addPrtTable.find("#allSelect").attr('id','newAllSelect');
		
		var text = '<div id="prtSelDiv">'  + $selPrtTable.html() + '</div>';
		var title = "";
		title = '根据筛选条件导入产品';
		var dialogSetting = {
				dialogInit: "#dialogSelPrtInitDIV",
				text: text,
				width: 	600,
				height: 260,
				title: 	title,
//					confirm: $("#dialogConfirmIss").text(),
				confirm: '添加',
//					cancel: $("#dialogCancelIss").text(),
				cancel: '取消',
				confirmEvent: function(){
					
					BINOLPTJCS34.addSelPrt();
					//_this.issuedPrt();
//					removeDialog("#dialogSelPrtInitDIV");
				},
				cancelEvent: function(){removeDialog("#dialogSelPrtInitDIV");}
		};
		openDialog(dialogSetting);
	},
	/**
	 * 产品弹出框
	 * 
	 * */
	"openProPopup":function(){
		var that = this;
		// 删除掉画面头部的提示信息框 
//		BINOLSTSFH01.clearActionMsg();
		// 产品弹出框属性设置
		var option = {
			targetId: "newDatabody",//目标区ID
			checkType : "checkbox",// 选择框类型
			mode : 2, // 模式
			ignoreSoluId : $("#productPriceSolutionID").val(),  // 剔除方案中的产品
			isPosCloud : $("#isPosCloud").val(),  // 是否小店云模式（小店云显示barcode）
			brandInfoId : $("#brandInfoId").val(),// 品牌ID
			getHtmlFun:that.getHtmlFun,// 目标区追加数据行function
			click:function(){//点击确定按钮之后的处理
				var checkbox= $('#dataTableBody').find(':input').is(":checked");
				//改变奇偶行的样式
				BINOLPTJCS34.changeOddEvenColor();
				// 拖动排序
				$("#prtSaveDiv #mainTable #databody").sortable({
					update: function(event,ui){BINOLPTJCS34.changeOddEvenColor();}
				});
				// 设置全选状态
				var $checkboxs = $('#prtSaveDiv #databody').find(':checkbox');
				var $unChecked = $('#prtSaveDiv #databody').find(':checkbox:not(":checked")');
				if($unChecked.length == 0 && $checkboxs.length > 0){
					$('#prtSaveDiv #allSelect').prop("checked",true);
				}else{
					$('#prtSaveDiv #allSelect').prop("checked",false);
				}
				// AJAX取得产品当前库存量
//				that.getPrtStock();
				//计算总金额总数量
//				BINOLSTSFH01.calcTotal();
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
	},
	"changeOddEvenColor":function(){
		$("#newDatabody tr:odd").attr("class","even");
		$("#newDatabody tr:even").attr("class","odd");
	},
	"changechkbox":function(obj){
		var chked = $(obj).prop("checked");
		if(!chked){
			$('#prtSaveDiv #allSelect').prop("checked",false);
			return false;
		}
		var flag = true;
		$("#newDatabody :checkbox").each(function(i){
			if(i>=0){
				if($(this).prop("checked")!=true){
					flag=false;
				}
			}
		});
		if(flag){
			$('#prtSaveDiv #allSelect').prop("checked",true);
		}
	},
	"getHtmlFun":function(info,negativeFlag){
//		alert(JSON.stringify(info));
		var prtId = info.prtId;//产品Id
		var productVendorId = info.productVendorId;//产品厂商Id
		var unitCode = info.unitCode;//厂商编码
		var barCode = info.barCode;//产品条码
		var originalBrand = info.originalBrand;// 子品牌
		var nameTotal = info.nameTotal;//产品名称
		var price = info.salePrice;//销售价格
		var memPrice = info.memPrice;//会员价格
		
		var minSalePrice = info.minSalePrice; // 销售最低价
		var maxSalePrice = info.maxSalePrice; // 销售最高价
		
		// 销售价格处理
		if(isEmpty(price)){
			price = parseFloat("0.0");
		}else{
			price = parseFloat(price);
		}
		price = price.toFixed(2);
		
		// 会员价格处理
		if(isEmpty(memPrice)){
			memPrice = parseFloat("0.0");
		}else{
			memPrice = parseFloat(memPrice);
		}
		memPrice = memPrice.toFixed(2);
		
		// 销售最低价处理
		if(isEmpty(minSalePrice)){
			minSalePrice = parseFloat("0.0");
		}else{
			minSalePrice = parseFloat(minSalePrice);
		}
		minSalePrice = minSalePrice.toFixed(2);
		
		// 销售最高价处理
		if(isEmpty(maxSalePrice)){
			maxSalePrice = parseFloat("0.0");
		}else{
			maxSalePrice = parseFloat(maxSalePrice);
		}
		maxSalePrice = maxSalePrice.toFixed(2);
		
//		var curStock = info.curStock;//当前库存
//		if(isEmpty(curStock)){
//			curStock = 0;
//		}
		var quantity=info.quantity;//数量
		if(isEmpty(quantity)){
			quantity = "";
		}
		if(!isEmpty(quantity) && negativeFlag){
			quantity = Number(quantity)*(-1);
		}
		var amount = price * Number(quantity);//金额
		amount = amount.toFixed(2);
		var reason = info.reason;//备注
		if(quantity == 0){quantity = "";}
		if(isEmpty(reason)){reason = "";}
		var html = '<tr>';
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLPTJCS34.changechkbox(this);"/></td>';
		html += '<td>' + unitCode +'<input type="hidden" id="unitCodeArr"  value="'+ unitCode + '"/></td>';
		// 小店云模式，显示条码及可修改方案名称
//		if($("#isPosCloud").val()==1){
			html += '<td>' + barCode +'<input type="hidden" id="barCodeArr"  value="'+ barCode + '"/></td>';
			html += '<td>' + originalBrand + '</td>';
			html += '<td class="center"><input value="' + nameTotal +'" name="soluProductNameArr"  type="text" id="soluProductNameArr"  maxlength="80" cssStyle="width:98%" onchange="" /></td>';
//		}else if($("#isPosCloud").val()==0){
//			html += '<td>' + originalBrand + '</td>';
//			html += '<td>' + nameTotal + '</td>';
//		}
		//html += '<td>' + barCode + '<input type="hidden" id="barCodeArr" value="'+  barCode + '"/></td>';
		html += '<td class="center"><input value="' + minSalePrice +'" name="minSalePriceArr"  type="text" id="minSalePriceArr" class="price" maxlength="17" cssStyle="width:98%" onchange="BINOLPTJCS34.setPrice(this);" /></td>';
		html += '<td class="center"><input value="' + maxSalePrice +'" name="maxSalePriceArr"  type="text" id="maxSalePriceArr" class="price" maxlength="17"  cssStyle="width:98%" onchange="BINOLPTJCS34.setPrice(this)" /></td>';
		
		html += '<td class="center"><input value="' + price +'" name="salePriceArr"  type="text" id="salePriceArr" class="price" maxlength="17" cssStyle="width:98%" onchange="BINOLPTJCS34.setPrice(this);" /></td>';
		html += '<td class="center"><input value="' + memPrice +'" name="memPriceArr"  type="text" id="memPriceArr" class="price" maxlength="17"  cssStyle="width:98%" onchange="BINOLPTJCS34.setPrice(this)" /></td>';
		
		html +='<td style="display:none">'
			+'<input type="hidden" name="prtVendorId" value="'+ productVendorId + '"/>'
			+'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+productVendorId+'"/>'
			+'<input type="hidden" id="prtIdArr" name="prtIdArr" value="'+ prtId +'"/></td></tr>';
		return html;
	},
	/**
	 * 删除选中行
	 */
	"deleterow":function(){
		BINOLPTJCS34.cleanMsgDIV();
//		BINOLSTSFH01.clearActionMsg();
		$("#newDatabody :checkbox").each(function(){
			var $this = $(this);
			if($this.prop("checked")){
				$this.parent().parent().remove();
			}
		});
		$('#prtSaveDiv #allSelect').prop("checked",false);
		BINOLSTSFH01.changeOddEvenColor();
		$('#prtSaveDiv #allSelect').prop("checked",false);
		
		$("#prtSaveDiv input[type=checkbox]").prop("checked",false);
//		BINOLSTSFH01.calcTotal();
	},
	"chkPrice":function(obj){
		var value = $(obj).val();
		var param = [14,2];
		
		var checkFloat = false;
		checkFloat = /^([0-9]\d*)(\.\d+)?$/.test(value);
		if(checkFloat) {
			var ar = value.split('.');
			if(ar.length == 2) {
				if(ar[0].length > param[0] || ar[1].length > param[1]) {
					checkFloat = false;
				}
			} else if(ar.length == 1) {
				if(ar[0].length > param[0]) {
					checkFloat = false;
				}
			} else {
				checkFloat = false;
			}
		}
//		alert(checkFloat);
		if(!checkFloat){
			$(obj).val(0);
		}
	},
	 // 格式化价格及重新计算金额
	"setPrice":function(obj){
		var $this = $(obj);
//		var $tr_obj = $this.parent().parent();

		// 折扣价
//		var $discountPrice = $tr_obj.find("input[name='priceUnitArr']");
		var discountPrice = parseFloat($this.val());

		if(isNaN(discountPrice)){
			discountPrice = 0;
		}
		$this.val(discountPrice.toFixed("2"));
	},
	"addRow":function(){
//		if(!BINOLSTSFH01.checkData()){
//			return false;
//		}
		var that = this;
		
		var url = $("#addRow_Url").attr("href");
		var param = $("#prtSaveDiv #mainForm").serialize();
//		alert(param);
		param += "&productPriceSolutionID="+ $("#productPriceSolutionID").val();
//		param += "&csrftoken="+parentTokenVal();
		param += "&csrftoken="+getTokenVal();
		
//		alert(param);
		var callback = function(msg){
//			alert(msg);
			
			var searchUrl = $("#search_url").val();
//			BINOLPTJCS34.search(url);
			BINOLPTJCS34.search(searchUrl);
//			if(msg.indexOf("actionMessage") > -1){
//				alert('添加成功');
////				BINOLSTSFH01.clearPage(true);
//			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	"addSelPrt":function(){
//		if(!BINOLSTSFH01.checkData()){
//			return false;
//		}
		var that = this;
		
		var url = $("#addSelPrtUrl").attr("href");
		var param = $("#prtSelDiv #selForm").serialize();
//		alert(param);
		param += "&productPriceSolutionID="+ $("#productPriceSolutionID").val();
		param += "&brandInfoId=" + $("#brandInfoId").val();
//		param += "&csrftoken="+parentTokenVal();
		param += "&csrftoken="+getTokenVal();
		
		if($("#prtSelDiv").find("#originalBrandSel").val()==''){
			return false;
		}
		
		var callback = function(msg){
			
			
			var map = eval("(" + msg + ")");
			if(map.result == "0"){
//				$("#dialogIssuedInitDIV").empty();
				// 显示结果信息
				var ht = $("#operateSuccessId").clone();
				$("#dialogSelPrtInitDIV").html('').append(ht);
			} else{
//				$("#dialogIssuedInitDIV").empty();
				// 显示结果信息
				var ht= $("#operateFaildId").clone();
				$("#dialogSelPrtInitDIV").html('').append(ht);
				
			}
			$("#dialogSelPrtInitDIV").dialog( "option", {
				buttons: [{
					text: "确定",
				    click: function(){
				    	removeDialog("#dialogSelPrtInitDIV");
						var searchUrl = $("#search_url").val();
//						BINOLPTJCS34.search(url);
						BINOLPTJCS34.search(searchUrl);
			    	}
				}]
			});
			
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	// 编辑
	"editRowInit" : function (obj){
		BINOLPTJCS34.cleanMsgDIV();
//		var that = this;
//		that.empty();
		var $this = $(obj);
		
		if(BINOLPTJCS34.disabOptBtn()==false)return;
//		alert($this.parent().parent().parent().html());
		if($("#isPosCloud").val()==1){
			$this.parent().parent().parent().find("[name='soluProductName']").parent().prev().hide();
			$this.parent().parent().parent().find("[name='soluProductName']").parent().show();
		}
		
		$this.parent().parent().parent().find("[name='salePrice']").parent().prev().hide();
		$this.parent().parent().parent().find("[name='salePrice']").parent().show();
		
		$this.parent().parent().parent().find("[name='memPrice']").parent().prev().hide();
		$this.parent().parent().parent().find("[name='memPrice']").parent().show();
		
		$this.parent().parent().parent().find(".btnEdit").hide();
		$this.parent().parent().parent().find(".btnSave").show();
		//更新编辑区分 --编辑中
		$("#flag").val("1");
	},
	// 更新
	"editRow" : function (obj){
		var $this = $(obj);
		
		var url = $("#editRow_Url").attr("href");
		var param = $this.parent().parent().parent().find(":input").serialize();
		
		param += "&" + $("#brandInfoId").serialize();
		param += "&productPriceSolutionID="+ $("#productPriceSolutionID").val();
		param += "&csrftoken="+parentTokenVal();
		
//		alert(param);
		
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				var map = eval("(" + msg + ")");
				var $curRow = $this.parent().parent().parent();
				$curRow.find("[name='salePrice']").parent().prev().show();
				$curRow.find("[name='salePrice']").parent().hide();
				
				$curRow.find("[name='memPrice']").parent().prev().show();
				$curRow.find("[name='memPrice']").parent().hide();
				
				$curRow.find("[name='soluProductName']").parent().prev().show();
				$curRow.find("[name='soluProductName']").parent().hide();
				
				$curRow.find(".btnEdit").show();
				$curRow.find(".btnSave").hide();
				if(map.result == 0){
					$curRow.find("[name='salePrice']").parent().prev().text($curRow.find("[name='salePrice']").val());
					$curRow.find("[name='memPrice']").parent().prev().text($curRow.find("[name='memPrice']").val());
					$curRow.find("[name='soluProductName']").parent().prev().text($curRow.find("[name='soluProductName']").val());
				}else{
					//修改异常
				}
				
				$("#flag").val('');
				// 返回无错误
//				if(msg.indexOf('id="actionResultDiv"') > -1
//						&& msg.indexOf('class="errorMessage"') == -1){
//					that.search(true);
//				}
				
			}
		});
		
	},
	/**
	 * 设置按钮有效与无效
	 * 
	 */
	"disabOptBtn":function (){
		var flag = $("#flag").val();
		if(flag == "1"){
			return false;
		}
		return true;
	},
	/**
	 * 批量删除
	 */
	"editBatchValidFlag" : function(flag, url) {
		var param = "";
		if($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		param = $('#dataTable_Cloned :input[checked]').nextAll().serialize();
		
		param += "&" + $("#brandInfoId").serialize();
		param += "&productPriceSolutionID="+ $("#productPriceSolutionID").val();
//		param += "&csrftoken="+parentTokenVal();
//		alert(param);
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		bscom03_deleteConfirm(flag, url, param, callback);
	},
	/**
	 * 清空提示消息
	 */
	"cleanMsgDIV" : function (){
		// 清空消息
		$("#actionResultDisplay").empty();
		$("#errorMessage").empty();
	}
	
};
var BINOLPTJCS34 = new BINOLPTJCS34();

$(function(){
	$("#export").live('click',function(){
		BINOLPTJCS34.exportExcel();
	});
});
