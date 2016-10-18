var BINOLBSEMP07_global = function(){};

BINOLBSEMP07_global.prototype = {
		
		"changeBaOrCoupon" : function(object,url) {
			oTableArr = new Array(null,null);
			fixedColArr = new Array(null,null);
			$(window).unbind('resize');
			if($(object).attr('class').indexOf('display-tree') != -1) {
				if($(object).attr('class').indexOf('display-tree-on') != -1) {
					return false;
				} else {
					$(object).siblings().removeClass('display-table-on');
					$(object).addClass('display-tree-on');
				}
			} else {
				if($(object).attr('class').indexOf('display-table-on') != -1) {
					return false;
				} else {
					$(object).siblings().removeClass('display-tree-on');
					$(object).addClass('display-table-on');
				}
			}
			var callback = function(msg) {
				$("#baOrBatchId").html(msg);
			};
			cherryAjaxRequest({
				url: url,
				param: null,
				callback: callback//,
				//formId: '#batchModelForm'
			});
		},
		/**
		 * BA模式查询
		 */
		"baSearch" : function() {
			this.clearActionHtml();
			var $form = $('#baModelForm');
			if (!$form.valid()) {
				return false;
			};
			$form.find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			var params = $form.serialize();
			var url = $("#searchUrl").attr("href")+"?"+params;
			// 显示结果一览
			$("#section").show();
			// 表格设置
			var tableSetting = {
					 // datatable 对象索引
					 index : 1,
					 // 表格ID
					 tableId : '#dataTableBaModel',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 2, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "checkbox","sWidth": "1%","bSortable": false},
					                { "sName": "parentResellerName"},
					                { "sName": "resellerName"},
									{ "sName": "batchCount","sClass": "center"},
									{ "sName": "couponCount","sClass": "center"}
									],
					// 不可设置显示或隐藏的列	
					aiExclude :[0,1,2,3],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 固定列数
					fixedColumns : 2
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		
		/*
		 * 代理商类型变化时清空一、二级代理商值
		 */
		"clearResellerVal" : function() {
			$("#parentResellerCode").val("");
			$("#parentResellerName").val("");
			$("#resellerCode").val("");
			$("#resellerName").val("");
			
		},
		
		/**
		 * 批次模式查询
		 */
		"batchSearch" : function() {
			this.clearActionHtml();
			var $form = $('#batchModelForm');
			if (!$form.valid()) {
				return false;
			};
			$form.find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			var params = $form.serialize();
			var url = $("#batchSearchUrl").attr("href")+"?"+params;
			// 显示结果一览
			$("#sectionBatchModel").show();
			// 表格设置
			var tableSetting = {
					 // datatable 对象索引
					 index : 2,
					 // 表格ID
					 tableId : '#dataTableBatchModel',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "no.","sWidth": "1%","bSortable":false},
									{ "sName": "batchCode","sClass": "center"},
									{ "sName": "batchName","sClass": "center"},
									{ "sName": "couponType","sClass": "center","bVisible": false},
									{ "sName": "parValue","sClass": "center","bVisible": false},
									{ "sName": "useTimes","sClass": "center","bVisible": false},
									{ "sName": "startDate","sClass": "center"},
									{ "sName": "endDate","sClass": "center"},
									{ "sName": "batchDate","sClass": "center"},
									{ "sName": "amountCondition","sClass": "center","bVisible": false},
									{ "sName": "operation","sClass": "center","bSortable":false}
									],
					// 不可设置显示或隐藏的列	
					aiExclude :[0,1,6,7],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 固定列数
					fixedColumns : 1
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		
		/**
		 * 代理商优惠券批次数据过滤
		 */
		"baCouponBatchFilter" : function(filterType,thisObj){
			$('#ui-tabs').find('li').removeClass('ui-tabs-selected');
			$(thisObj).addClass('ui-tabs-selected');
			// 数据过滤
			oTableArr[2].fnFilter(filterType);

			$('#th_operator').show();
		},
		
		/**
		 * 选择代理商
		 */
		"checkBaRecord" : function(object,id) {
			$("#errorMessage").empty();
			var $id = $(id);
			if($(object).attr('id') == "checkAll") {
				if(object.checked) {
					$id.find(':checkbox').prop("checked",true);
				} else {
					$id.find(':checkbox').prop("checked",false);
				}
			} else {
				if($id.find(':checkbox:not([checked])').length == 0) {
					$id.prev().find('#checkAll').prop("checked",true);
				} else {
					$id.prev().find('#checkAll').prop("checked",false);
				}
			}
		},
		
		
		/**
		 * 清理执行结果信息
		 */
		"clearActionHtml" : function() {
			$("#errorDiv2 #errorSpan2").html("");
			$("#errorDiv2").hide();
			$("#errorMessage").empty();
			$("#actionResultDisplay").empty();
		},
		
		/**
		 * 弹出新增菜单分组对话框
		 */
		"popCreateCouponDialog" : function() {
			var that = this;
			that.clearActionHtml();
			var checked = $('#dataTableBaModel_Cloned :input[checked]');
			var params = checked.nextAll().serialize();
			var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 	400,
					height: 450,
					title: 	$("#createCouponTitle").text(),
					confirm: $("#dialogConfirm").text(),
					cancel: $("#dialogCancel").text(),
					confirmEvent: function(){// 生成代理商优惠券
			    			that.createCoupon($("#createCouponUrl").attr("href"),params);
			    		},
					cancelEvent: function(){removeDialog("#dialogInit");}
				};
				openDialog(dialogSetting);
				var crtInitUrl = $("#createCouponInitUrl").attr("href");
				var callback = function(msg) {
					$("#dialogInit").html(msg);
				};
				cherryAjaxRequest({
					url: crtInitUrl,
					param: null,
					callback: callback,
					formId: '#baModelForm'
				});
		},
		
		/**
		 * 生成优惠券
		 * @param url
		 * @param params : 选中的BaID
		 */
		"createCoupon" : function(url,params) {
			if(!$('#createForm').valid()) {
				return false;
			}
			$('#createForm :input').each(function(){
				$(this).val($.trim(this.value));
			});
			var param = $("#createForm").serialize();
			if(null != params && params != '') {
				param += '&' + params; 
			}
			var callback = function(msg){
				if(msg.indexOf('id="actionResultDiv"') != -1 || msg.indexOf('id="fieldErrorDiv"') != -1) {
				} else {
					$("#dialogInit").html(msg);
					// 保存成功的场合
					if($("#dialogInit").find("#successDiv").length != 0) {
						$("#dialogInit").dialog("destroy");
		                var dialogSetting = {
							dialogInit: "#dialogInit",
							text: msg,
							width: 500,
							height: 300,
							title: $("#saveSuccessTitle").text(),
							confirm: $("#dialogClose").text(),
							confirmEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();},
	                        closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
						};
						openDialog(dialogSetting);
					} else if($("#dialogInit").find("#errorMessageDiv").length != 0) {
						 $("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			                $("#dialogInit").dialog( "option", {
			                    buttons: [{
			                        text: $("#dialogClose").text(),
			                        click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
			                    }],
								closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
			                });
					}
				}
			};
			cherryAjaxRequest({
				url: url,
				param: param,
				formId: '#createForm',
				callback: callback
			});	
		},
		
		/**
		 * 弹出删除确认框
		 */
		"popDeleteDialog" : function(obj) {
			var that = this;
			that.clearActionHtml();
			var param = "csrftoken=" + getTokenVal();
			var dialogSetting = {
				dialogInit: "#dialogInit",
				text: $("#dialogContent").html(),
				width: 	500,
				height: 300,
				title: 	$('#deleteBaCouponTitle').text(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				confirmEvent: function(){
					that.deleteBatchCoupon($(obj).attr("href"),param);
					removeDialog("#dialogInit");
				},
				cancelEvent: function(){
					removeDialog("#dialogInit");
				}
			};
			openDialog(dialogSetting);
		},
		
		/**
		 * 删除未同步的批次代理商优惠券
		 */
		"deleteBatchCoupon" : function(url,param) {
			var that = this;
			var callback = function(msg){
				if(msg.indexOf('id="actionResultDiv"') > -1){
						if(oTableArr[2] != null)oTableArr[2].fnDraw();
				}
			};
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callback
			});
		}
};

var BINOLBSEMP07 = new BINOLBSEMP07_global();

$(document).ready(function(){
	
});
