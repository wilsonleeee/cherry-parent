function BINOLCPACT05() {
	this.needUnlock = true;
};

BINOLCPACT05.prototype = {
		// 活动对象类型
		"changeMode":function(){
			var selectMode_1 = $("#orderMode_1");
			var selectMode_2 = $("#orderMode_2");
			// 预约档次
			var $selectImport = $("#selectImport");
			// 会员数量显示
			var $meberTotal = $("#meberTotal");
			var $confirmbtn = $("#confirmbtn");
			// 选择EXCEL文件
			var $excelImport = $("#excelImport");
			if(selectMode_1.is(":checked")){// 活动对象选择
				$selectImport.show();
				$confirmbtn.show();
			}else{
				$selectImport.hide();
				$confirmbtn.hide();
			}
			if(selectMode_2.is(":checked")){// Excel导入
				$excelImport.show();
			}else{
				$excelImport.hide();
			}
			BINOLCPACT05.deleteActionMsg();
		},
		"searchDetail" : function(_this) {
			var $this = $(_this).parent();
//			$this.find(':input').prop('checked',true);
			var $next = $this.next();
			$this.parent().find("tr.detail").hide();
			if(!$this.hasClass('red')) {
				$this.addClass("red");
				$this.siblings().removeClass("red");
				if($next && $next.hasClass("detail")) {
					$next.show('normal');
		    	}
			} else {
				$this.removeClass("red");
				$next.hide();
			}
		},
		"closeDetail" :function(_this){
			var $this = $(_this);
			var $tr = $this.parents('tr.detail');
			$tr.hide('normal');
			$tr.prev().removeClass("red");
		},
		// 活动对象弹出框
		"popMemDialog" :function(){
			var $subCamp = $('#selectImport').find(':input:checked');
			var $times = $subCamp.parent().parent().find(':input[name="times"]');
			var option = {
			 	   	targetId: 'mem_table',
			 	   	subCampCode: $subCamp.val(),
			 	   	times : $times.val(),
			 	   	checkType : "checkbox",
			        mode : 2,
			       	getHtmlFun:function(info){
			       		var html = '<tr>';
			       		html += '<td>'+info.memCode+'<input type="hidden" name="memCode" value="'
			       			+info.memCode+'"/><input type="hidden" name="memId" value="'+info.memId+'"/></td>';
			       		html += '<td>'+info.memName+'</td>';
			       		html += '<td>'+info.counterCode+'</td>';
			       		html += '<td>'+info.changablePoint+'</td>';
			       		html += '<td>'+info.joinDate+'</td>';
			       		html += '<td>'+info.birthDay+'</td>';
			       		html += '<td>'+info.mobilePhone+'</td>';
			       		html += '<td class="center"><a href="javascript:void(0);" onClick="$(this).parent().parent().remove();return false;">删除</a></td>';
			       		html += '</tr>';
			       		return html;
				   	}
			    };
			popAjaxActMemDialog(option);
		},
		"searchConDialog" :function(obj){
			var that = this;
			var url = '/Cherry/common/BINOLCM33_init';
			var $subCamp = $('#selectImport').find(':input:checked');
			var reqContent = $("#campMebJson").val();
			var $tr = $subCamp.parent().parent();
			var $times = $tr.find(':input[name="times"]');
			var disConInfo = $tr.find(':input[name="conInfo"]').val();
			if(isEmpty(reqContent)){
				reqContent = disConInfo;
			}
			var params = "reqContent=" + reqContent;
			//params += "&disableCondition=" + disConInfo;
			var dialogSetting = {
				dialogInit: "#searchCondialogInit",
				width: 900,
				height: 580,
				title: $("#objDialogTitle").text(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				//确认按钮
				confirmEvent: function(){
					//将搜索条件弹出框信息json格式化
					var campMebInfo = $("#searchCondialogInit").find(":input").serializeForm2Json(false);
					if(!isEmpty(campMebInfo)){
						$("#campMebJson").val(campMebInfo);
						// 关闭搜索条件弹出框
						removeDialog("#searchCondialogInit");
						that.memSearch(campMebInfo);
					}
				},
				//关闭按钮
				cancelEvent: function(){removeDialog("#searchCondialogInit");}
			};
			openDialog(dialogSetting);
			cherryAjaxRequest({
				url: url,
				param : params,
				callback: function(msg) {
					$("#searchCondialogInit").html(msg);
				}
			});
		},
		"changeSubCamp":function(){
			$('#mem_table').parent().hide();
			$('#campMebJson').val('');
		},
		/*
		 * 提交确认画面
		 */
		"confirmInit":function (url){
			$('#errorDiv').hide();
			 // 提交前对数据进行检查
	    	if(!this.checkData()){
	    		return false;
	    	}
			var params= 'csrftoken='+parentTokenVal();
			var $orderMode = $('#mainForm').find(':input[name="orderMode"]:checked');
			var $subCampCode = $('#selectImport').find(':input[name="subCampCode"]:checked');
			var $times = $subCampCode.parent().parent().find(':input[name="times"]');
			var $campMebJson = $('#campMebJson');
			var $cntCode = $('#orderCntCode');
			var $cntId = $('#organizationId');
			var $gotCounter = $('#gotCounter');
			params += '&' + $orderMode.serialize();
			params += '&' + $subCampCode.serialize();
			params += '&' + $times.serialize();
			params += '&' + $campMebJson.serialize();
			params += '&' + $cntCode.serialize();
			params += '&' + $cntId.serialize();
			params += '&' + $gotCounter.serialize();
	    	cherryAjaxRequest({
				url: url,
				param : params,
				callback:function(msg){
					$('#div_main').empty();
				}
			});
		},
		/*
		 * 批量导入Excel
		 */
		"ajaxFileUpload":function (url){
			var that = this;
			var params = {};
			// 档次礼品信息
			params.csrftoken = parentTokenVal();
	    	var $loadingImg = $("#loadingImg");
	    	var $errorDiv = $('#errorDiv');
	    	var $errorSpan = $('#errorSpan');
	    	$errorMessage = $('#actionResultDisplay');
	    	// 清空错误信息
	    	$errorDiv.hide();
	    	$errorMessage.hide();
	    	 // 提交前对数据进行检查
	    	if(!that.checkData()){
	    		return false;
	    	}
	    	//文件加载
	    	$loadingImg.ajaxStart(function(){$(this).show();});
	    	$loadingImg.ajaxComplete(function(){$(this).hide();});
	    	// 导入按钮
	    	var $excelBtn = $("#upload");
	    	// 禁用导入按钮
	    	$excelBtn.prop("disabled",true);
	    	$excelBtn.addClass("ui-state-disabled");
	    	$.ajaxFileUpload({
		        url: url,
		        secureuri:false,
		        data:params,
		        fileElementId:'upExcel',
		        dataType: 'html',
		        success: function (msg){
		        	//释放按钮
					$excelBtn.removeAttr("disabled",false);
					$excelBtn.removeClass("ui-state-disabled");
		        	var msgJson = window.JSON.parse(msg);
		        	var html = showMsg(msgJson.msg,msgJson.level);
		        	$errorMessage.html(html);
		        	$errorMessage.show();
		        }
	        });
		},
		/**
		 * 提交前对数据进行检查
		 * @returns {Boolean}
		 */
		"checkData":function (){
			var $orderMode = $('#mainForm').find(':input[name="orderMode"]:checked');
			if($orderMode.val() == '1'){
				var $mems = $('#mem_table').find('tbody').children();
				var $campMebJson = $('#campMebJson');
				if($campMebJson.val() == '' || $mems.length == 0 || $mems.find('td.dataTables_empty').length != 0){
					$('#errorSpan').text($("#errMsg_2").text());
					$('#errorDiv').show();
		    		return false;
				}
				var $cntId = $('#organizationId');
				var $gotCounter = $('#gotCounter');
				// 领用柜台为发卡柜台
				if(isEmpty($cntId.val()) && $gotCounter.val() == '3'){
					$('#errorSpan').text($("#errMsg_3").text());
					$('#errorDiv').show();
		    		return false;
				}
			}else{
				// 上传文件不存在
		    	if($('#upExcel').val()==''){
		    		$('#pathExcel').val("");
		    		$('#errorSpan').text($("#errMsg_1").text());
					$('#errorDiv').show();
					return false;
				}
			}
			return true;
		},
	    // 清空错误信息
	    "deleteActionMsg" : function (){
	    	var $errorDiv = $('#errorDiv');
	    	var $errorMessage = $('#actionResultDisplay');
	    	$errorDiv.hide();
	    	$errorMessage.hide();
	    },
	 // 查询
		"memSearch" : function(campMebInfo) {
			var url = '/Cherry/cp/BINOLCPCOM03_memInfosearch';
			var params= getSerializeToken();
			params +="&campMebInfo="+campMebInfo;
			url = url + "?" + params;
			$('#mem_table').parent().show();
			// 表格设置
			var tableSetting = {
					 index: 200000,
					 // 表格ID
					 tableId : '#mem_table',
					 // 数据URL
					 url : url,
					 iDisplayLength:10,
					 // 表格默认排序
					 aaSorting : [[ 2, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  
									{ "sName": "customerType","bSortable": false},
							        { "sName": "memCode"},
							        { "sName": "memName"},
							        { "sName": "mobilePhone"},
							        { "sName": "birthDay","bSortable": false},
									{ "sName": "joinDate","bSortable": false},
							        { "sName": "changablePoint","sClass":"alignRight","bSortable": false},
							        { "sName": "receiveMsgFlg","sClass":"center","bSortable": false}]
			};
			// 调用获取表格函数
			getTable(tableSetting);
	    },
	    // 打开柜台弹窗口
	    "openCntBox":function(_this){
	    	
	    	var callback = function(){
	    		var $input = $('#counterDialog').find(':input:checked');
	    		var id = $input.val();
	    		if(!isEmpty(id)){
	    			var $code = $input.parent().next();
	    			var $name = $code.next();
	    			var code = $.trim($code.text());
	    			var name = $.trim($name.text())
	    			$('#orderCntCode').val(code);
	    			$('#orderCntCode_text').text('['+code + ']'+name);
	    			$('#organizationId').val(id);
	    			
	    		}else{
	    			$('#orderCntCode').val('');
	    			$('#orderCntCode_text').text('');
	    			$('#organizationId').val('');
	    		}
	    	}
	    	popDataTableOfCounter(_this,null,callback);
	    },
	    "changeTimes" : function(){
	    	$('#campMebJson').val('');
	    	$('#mem_table').parent().hide();
	    }
};

var BINOLCPACT05 =  new BINOLCPACT05();
$(document).ready(function() {
	var $trs = $('#sumCamp_table').children().not('.detail');
	var $tds = $trs.find('td.showDetail');
	$tds.css('cursor','pointer');
	$tds.click(function(){
		BINOLCPACT05.searchDetail(this);
	});
});