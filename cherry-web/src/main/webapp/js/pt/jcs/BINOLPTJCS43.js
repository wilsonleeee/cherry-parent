var BINOLPTJCS43 = function() {
};

BINOLPTJCS43.prototype = {

	"clearMessage" : function() {
		$('#actionResultDisplay').empty();
		$('#errorMessage').empty();
	},

	//关联详细
	"searchDetail" : function(object) {
		BINOLPTJCS43.clearMessage();
		$(object).parent().find("tr[class=detail]").remove();
		if($(object).attr("class").indexOf("selectedColor") == -1) {
			$(object).addClass("selectedColor");
			$(object).siblings().removeClass("selectedColor");
			if($(object).next() && $(object).next().attr("class") == "detail") {
	    		$(object).next().show();
	    	} else {
	    		var url =  $(object).find("[name=searchConjunctionDetailUrl]").val();
	    		if(url) {
	    			$(object).after('<tr class="detail"><td colspan="'+$(object).find('td').length+'" class="detail box2-active"></td></tr>');
	        		var $td = $(object).next().find("td");
	            	var callback = function(msg) {
	            		$td.html(msg);
	            	};
	            	cherryAjaxRequest({
	            		url: url,
	            		callback: callback
	            	});
	    		}
	    	}
		} else {
			$(object).removeClass("selectedColor");
			BINOLPTJCS43.search();
		}
	},
	
	//查询
	"search" : function() {
		BINOLPTJCS43.clearMessage();
		var url = $("#search_url").val();
		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		// 查询参数序列化
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#section").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			// 表格默认排序
			aaSorting : [[1, "asc"]],
			// 表格列属性设置
			aoColumns : [
		             	{ "sName": "no","bSortable":false,"sWidth": "5%"},
			            { "sName": "BIN_GroupID","sClass":"center","sWidth": "5%","bSortable":false},
			            { "sName": "NameTotal","bSortable":false}
					],
			// 不可设置显示或隐藏的列	
			aiExclude : [ 0, 1 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback:function() {
				$("#dataTable").find('tr').click(function() {
					BINOLPTJCS43.searchDetail(this);
				});
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	//设置全选
	"checkRecord": function(object, id){
		BINOLPTJCS43.clearMessage();
		var $id = $(id);
		var $trs = $("#dataTable").find('tr');
		if($(object).attr('id') == "checkAll") {
			if(object.checked) {
				$trs.find(':checkbox').prop("checked",true);
			} else {
				$trs.find(':checkbox').prop("checked",false);
			}
		} else {
			if($trs.find(':checkbox:not([checked])').length == 0) {
				$("#checkAll").prop('checked', true);
			} else {
				$("#checkAll").prop('checked', false);
			}
		}
	},
	
	//产品关联初始化弹出框
	"associateInit":function(url) {
		BINOLPTJCS43.clearMessage();
		var prtVendorIdArr = [];
		var option = {
			targetId: "dialogInit",//目标区ID
			checkType : "checkbox",// 选择框类型
			mode : 2, // 模式
			brandInfoId : $("#brandInfoId").val(),// 品牌ID
			getHtmlFun:function(info){
				var prtId = info.proId;
				var html = '<tr class="hide">';
				html += '<td><input type="checkbox" onclick="changechkbox(this);" value="'+ prtId +'"></td>';
				html += '<input type="hidden" name="prtVendorId" value="'+prtId+'"/>';
				html += '</tr>';
				return html;
			},
			click:function(){//点击确定按钮之后的处理
				var $trs = $('#productDialog_temp').find('tr');
				$trs.each(function(){
					var prtVendorIdTemp = $(this).find('[name=prtVendorId]').val();
					if(prtVendorIdArr.indexOf(prtVendorIdTemp) < 0) {
						prtVendorIdArr.push(prtVendorIdTemp);
					}
				});
				if(prtVendorIdArr.length > 1) {
					var prtVendorId = JSON.stringify(prtVendorIdArr);
					var param = "csrftoken=" + $("#csrftoken").val();
					param += '&prtVendorId=' + prtVendorId;
					cherryAjaxRequest({
						url:url,
						param:param,/*{"proVendorId":proVendorId},*/
						callback:function(msg) {
							if(oTableArr[0]){
								BINOLPTJCS43.search();
							}
						 }
					});
				} else {
					return false;
				}
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
		$("#productDialog_temp").html("");
	},
	//删除关联组
	"delGroups":function(url) {
		BINOLPTJCS43.clearMessage();
		var groupIdArr = [];
		$('#dataTable').find("[name=groupIdx]").each(function() {
			if($(this).prop("checked")) {
				groupIdArr.push($(this).val());
			}
		});
		if(groupIdArr.length == 0) {
			return false;
		} else {
			var dialogId = '#dialogInit';
			if($(dialogId).length == 0) {
				$('body').append('<div style="display:none" id="confirmDialog"></div>');
			} else {
				$(dialogId).empty();
			}
			var dialogSetting = {
				dialogInit: dialogId,
				text: $("#confirmDialogText").html(),
				width: 360,
				height: 220,
				title: 	$("#confirmDialogTitle").html(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				confirmEvent: function(){
					var params = "groupIdArr=" + JSON.stringify(groupIdArr);
					var callback = function(msg) {
						$("#checkAll").prop('checked', false);
						if(oTableArr[0]){
							oTableArr[0].fnDraw();
						}
						removeDialog(dialogId);
					};
					cherryAjaxRequest({
						url:url,
						param:params,
						callback:callback
					});
				},
				cancelEvent: function(){removeDialog("#dialogInit");}
			};
			openDialog(dialogSetting);
		}
	},
	
	//删除一个产品
	"delOnePrt":function(url, _this) {
		BINOLPTJCS43.clearMessage();
		var callback = function(msg) {
			var dataJson = window.JSON.parse(msg);
			if(dataJson.result == 0) {
				$(_this).parent().parent().remove();
			} else {
				BINOLPTJCS43.search();
			}
		};
		cherryAjaxRequest({
			url:url,
			callback:callback
		});
	},
	
	//在组内添加产品
	"insertIntoGroup":function(){
		BINOLPTJCS43.clearMessage();
		var url = $("#insertIntoGroup_url").val();
		var ignorePrtPrmVendorIdArr = new Array();
		var $trs = $("#detailPrtTable").find('tr');
		$trs.each(function(){
			var tempPrtVendorId = $(this).find('[name=addOnePrtPrtVendorId]').val();
			if(null != tempPrtVendorId && "" != tempPrtVendorId) {
				ignorePrtPrmVendorIdArr.push(tempPrtVendorId+"_N");
			}
		});
		var prtAddVendorIdArr = [];
		//促销品弹出框属性设置
		var option = {
			targetId:"conjunctionDiv",
			checkType : "checkbox",
			prmCate :'CXLP',
			mode : 2,
			brandInfoId:$("#brandInfoId").val(),
			popValidFlag : 2,
			ignorePrtPrmVendorID:ignorePrtPrmVendorIdArr,
			getHtmlFun:function(info) {
				var prtId = info.proId;
				var html = '<tr class="hide">';
				html += '<td><input type="checkbox" onclick="changechkbox(this);" value="'+ prtId +'"></td>';
				html += '<input type="hidden" name="prtVendorId" value="'+prtId+'"/>';
				html += '</tr>';
				return html;
			},
			click:function(){
				var $trs = $('#productDialog_temp').find('tr');
				$trs.each(function(){
					var prtVendorIdTemp = $(this).find('[name=prtVendorId]').val();
					if(prtAddVendorIdArr.indexOf(prtVendorIdTemp) < 0) {
						prtAddVendorIdArr.push(prtVendorIdTemp);
					}
				});
				if(prtAddVendorIdArr.length > 0) {
					var prtVendorIdArr = JSON.stringify(prtAddVendorIdArr);
					var param = "prtVendorId=" + prtVendorIdArr;
					var callback = function(msg){
						var dataJson = window.JSON.parse(msg);
						if(dataJson.result == 0) {//添加成功
							BINOLPTJCS43.search();
						} else {
							return false;
						}
					};
					cherryAjaxRequest({
						url:url,
						param:param,/*{"prmVendorId":prmVendorId},*/
						callback:callback
					});
				} else {
					return false;
				}
			}
		};
		popAjaxPrtDialog(option);
		$("#productDialog_temp").html("");
	}
};

var BINOLPTJCS43 = new BINOLPTJCS43();

$(function() {
	BINOLPTJCS43.clearMessage();
	BINOLPTJCS43.search();
});