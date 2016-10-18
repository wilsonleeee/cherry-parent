function BINOLSSPRM71() {};

BINOLSSPRM71.prototype = {	
		
	/**
	 * 清理执行结果信息
	 */
	"clearActionHtml" : function() {
		$("#errorMessage").empty();
		$("#actionResultDisplay").empty();
	},
	
	"searchDetail" : function(object) {
		BINOLSSPRM71.clearActionHtml();
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
			BINOLSSPRM71.search();
		}
	},
	"search" : function(){
		BINOLSSPRM71.clearActionHtml();
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
			aaSorting : [[1, "asc"]],
			// 表格列属性设置
			aoColumns : [
		             	{ "sName": "checkbox","bSortable": false,"sWidth": "5%"},
			            { "sName": "groupId","sClass":"center","sWidth": "5%","bSortable": false},
			            { "sName": "nameTotal","bSortable": false,"sWidth": "80%"}
					],	
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1,2],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback:function() {
				$("#dataTable").find('tr').click(function() {
					BINOLSSPRM71.searchDetail(this);
				});
			}
			
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	//促销品弹出框
	"associateInit":function(url) {
		BINOLSSPRM71.clearActionHtml();
		var prmVendorIdArr = [];
		//促销品弹出框属性设置
		var option = {
			targetId:"conjunctionDiv",
			checkType : "checkbox",
			prmCate :'CXLP',
			mode : 2,
			brandInfoId:$("#brandInfoId").val(),
			popValidFlag : 2,
			getHtmlFun:function(info) {
				var prmId = info.proId;
				var html = '<tr class="hide">';
				html += '<td><input type="checkbox" onclick="changechkbox(this);" value="'+ prmId +'"></td>';
				html += '<input type="hidden" name="prmVendorId" value="'+prmId+'"/>';
				html += '</tr>';
				return html;
			},
			click:function(){
				var $trs = $('#promotionDialog_temp').find('tr');
				$trs.each(function(){
					var prmVendorIdTemp = $(this).find('[name=prmVendorId]').val();
					if(prmVendorIdArr.indexOf(prmVendorIdTemp) < 0) {
						prmVendorIdArr.push(prmVendorIdTemp);
					}
				});
				if(prmVendorIdArr.length > 1) {
					var prmVendorId = JSON.stringify(prmVendorIdArr);
					var param = "csrftoken=" + $("#csrftoken").val();
					param += '&prmVendorId=' + prmVendorId;
					cherryAjaxRequest({
						url:url,
						param:param,/*{"prmVendorId":prmVendorId},*/
						callback:function(msg) {
							if(oTableArr[0]){
								BINOLSSPRM71.search();
							}
						 }
					});
				} else {
					return false;
				}
			}
		};
		popAjaxPrmDialog(option);
		$("#promotionDialog_temp").html("");
	},
	"checkRecord": function(object, id){
		BINOLSSPRM71.clearActionHtml();
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
	"delGroup":function(url){
		BINOLSSPRM71.clearActionHtml();
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
	"delOnePrm":function(url, _this) {
		BINOLSSPRM71.clearActionHtml();
		var callback = function(msg){
			var dataJson = window.JSON.parse(msg);
			if(dataJson.result == 0) {//原数据大于两条，这把这条数据删除即可
				$(_this).parent().parent().remove();
			} else {//原数据只有两条，直接删除整组数据
				BINOLSSPRM71.search();
			}
		};
		cherryAjaxRequest({
			url:url,
			callback:callback
		});
	},
	"insertIntoGroup":function(){
		BINOLSSPRM71.clearActionHtml();
		var url = $("#insertIntoGroup_url").val();
		var ignorePrtPrmVendorIdArr = new Array();
		var $trs = $("#detailPrmTable").find('tr');
		$trs.each(function(){
			var tempPrmVendorId = $(this).find('[name=addOnePrmPrmVendorId]').val();
			if(null != tempPrmVendorId && "" != tempPrmVendorId) {
				ignorePrtPrmVendorIdArr.push(tempPrmVendorId+"_P");
			}
		});
		var prmAddVendorIdArr = [];
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
				var prmId = info.proId;
				var html = '<tr class="hide">';
				html += '<td><input type="checkbox" onclick="changechkbox(this);" value="'+ prmId +'"></td>';
				html += '<input type="hidden" name="prmVendorId" value="'+prmId+'"/>';
				html += '</tr>';
				return html;
			},
			click:function(){
				var $trs = $('#promotionDialog_temp').find('tr');
				$trs.each(function(){
					var prmVendorIdTemp = $(this).find('[name=prmVendorId]').val();
					if(prmAddVendorIdArr.indexOf(prmVendorIdTemp) < 0) {
						prmAddVendorIdArr.push(prmVendorIdTemp);
					}
				});
				if(prmAddVendorIdArr.length > 0) {
					var prmVendorIdArr = JSON.stringify(prmAddVendorIdArr);
					var param = "prmVendorId=" + prmVendorIdArr;
					var callback = function(msg){
						var dataJson = window.JSON.parse(msg);
						if(dataJson.result == 0) {//添加成功
							
							BINOLSSPRM71.search();
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
		popAjaxPrmDialog(option);
		$("#promotionDialog_temp").html("");
	}
};

var BINOLSSPRM71 =  new BINOLSSPRM71();

$(document).ready(function() {
	BINOLSSPRM71.search();
});
