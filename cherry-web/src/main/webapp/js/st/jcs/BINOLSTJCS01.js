var BINOLSTJCS01 = function(){
};

BINOLSTJCS01.prototype = {
	// 添加新行
	"addLine":function (target,source){
	$("#actionResultDisplay").empty();
		var $target = $(target);
		if($target.find(":text").length !=0){
			return;
		}
		var $source = $(source);
		// 新添加行
		var $newLine = $source.find("tr");
		// 新行所有列
		var $cols2 = $newLine.find("td");
		// 目标行class
		var lastClass ="";
		// 目标最后一行序号
		var num=0;
		// 目标最后一行
		var $lastLine = $target.find("tr:last");
		// 无行
		if($lastLine.length == 0){
			lastClass = "even";
		}else{
			// 目标行class
			lastClass = $lastLine.attr("class");
			// 目标最后一行所有列
			var $cols1 = $lastLine.find("td");
			// 目标最后一行序号
			num = parseInt($cols1.eq(1).html());
		}
		// 新行添加序号
		$cols2.eq(1).html(num + 1);
		// 新行添加Class
		if(lastClass == "odd"){
			$newLine.attr("class","even");
		}else{
			$newLine.attr("class","odd");
		}
		// 目标追加新行
		$target.append($source.html());
		$("a[name=edit]").attr("onclick","");
	},
	
	//取消
//	"cancel":function (_this){
//		var $this = $(_this);
//		// 目标行
//		var $targetLine = $this.parent().parent();
//		// 删除目标行
//		$targetLine.remove();
//	},
	
	//刷新
	"search" : function (){
		var $form = $('#mainForm');
		var url = $("#searchUrl").attr("href");
		 // 查询参数序列化
		var param= $form.serialize();
		 url = url + "?" + param;

		 // 显示结果一览
		 $("#section").show();
		// 表格设置
		 var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 排序列
				 aaSorting : [[2, "desc"]],
				 // 表格列属性设置
				 aoColumns : [	{ "sName": "checkbox", "sWidth": "3%","bSortable": false}, 		// 0
		                    	{ "sName": "no", "sWidth": "3%","bSortable": false,"sClass":"center"},			// 1
		                    	{ "sName": "depotCode", "sWidth": "10%"},// 4
		                    	{ "sName": "depotName", "sWidth": "20%"},	// 2
								{ "sName": "departCode", "sWidth": "20%"},	// 2
								{ "sName": "testType", "sWidth": "5%","sClass":"center"},	// 2
								{ "sName": "address", "sWidth": "30%","bVisible": false},// 3
								{ "sName": "validFlag", "sWidth": "5%","sClass":"center"}],// 7
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 不可设置显示或隐藏的列	
				aiExclude :[0,1,2],
				// 固定列数
				fixedColumns : 3,
				index : 0
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
			
	},
	
	//编辑保存
	"save":function(lineId){
		var checkbox="checkbox_"+lineId;
		var param = "&csrftoken=" +$('#csrftoken').val();
		param = param+"&"+$("#"+checkbox+" #depotInfoID").serialize();
		param = param+"&"+$('#dataTable').find("input[name=depotCode]").serialize();
		param = param+"&"+$('#dataTable').find("input[name=organizationID]").serialize();
		param = param+"&"+$('#dataTable').find("input[name=depotNameCN]").serialize();
		param = param+"&"+$('#dataTable').find("input[name=depotNameEN]").serialize();
		param = param+"&"+$('#dataTable').find("input[name=address]").serialize();
		var url = $("#saveUrl").attr("href");
		var callback=function(msg){
			if(msg.indexOf('id="fieldErrorDiv"')==-1){
				$("#actionResultDisplay").empty();
				BINOLSTJCS01.search(msg);
			}
		};
	
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	//添加
	"add":function(){
		var param = "&csrftoken=" +$('#csrftoken').val();
		param = param+"&"+$('#dataTable').find("input[name=depotCode]").serialize();
		param = param+"&"+$('#dataTable').find("input[name=organizationID]").serialize();
		param = param+"&"+$('#dataTable').find("input[name=depotNameCN]").serialize();
		param = param+"&"+$('#dataTable').find("input[name=depotNameEN]").serialize();
		param = param+"&testTypeAdd="+$("#testTypeAdd option:selected").val();
		param = param+"&"+$('#dataTable').find("input[name=address]").serialize();
		var url = $("#addUrl").attr("href");
		var callback=function(msg){
			if(msg.indexOf('id="fieldErrorDiv"')==-1){
				$("#actionResultDisplay").empty();
				BINOLSTJCS01.search(msg);
			}
		};
	
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	// 编辑
	"edit" : function (lineId){
		$("#actionResultDisplay").empty();
		var JCSHighlight=$("#JCSHighlight").val();
		var JCSSave=$("#JCSSave").val();
		var JCSCancel=$("#JCSCancel").val();
//		var JCSyes=$("#JCSyes").val();
//		var JCSno=$("#JCSno").val();
		var depotCode = $("#depotCode_"+lineId).find("span").html();
		$("#depotCode_"+lineId).html('<input type="text" maxlength="10" class="text" name="depotCode" style="width:60px;" onchange="BINOLSTJCS01.check(this);" value="'+depotCode+'"/>'+JCSHighlight+'</span>');
		var organizationID= $("#organizationID_"+lineId).find("span").html();
		$("#organizationID_"+lineId).find("span").html('<span class="left">'+organizationID+'</span><span class="ui-icon icon-search right" style="cursor:pointer" onclick="BINOLSTJCS01.departBox(this);return false;"></span>');
		var depotNameCN = $("#depotNameCN_"+lineId).find("span").html();
		$("#depotNameCN_"+lineId).find("span").html('<input name="depotNameCN" type="text" class="text" maxlength="60" style="width:120px;" value="'+depotNameCN+'"/>');
		var depotNameEN = $("#depotNameEN_"+lineId).find("span").html();
		$("#depotNameEN_"+lineId).find("span").html('<input name="depotNameEN" type="text" class="text" maxlength="60" style="width:120px;" value="'+depotNameEN+'"/>');
		var address = $("#address_"+lineId).find("span").html();
		$("#address_"+lineId).find("span").html('<input name="address" type="text" class="text" maxlength="100" style="width:200px;" value="'+address+'"/>');
//		var testType=$("#testType_"+lineId).find("input").val();
//		if(testType==1){
//			$("#testType_"+lineId).find("span").html('<select name="testType" id="testType"><option value="1">'+JCSyes+'</option><option value="0">'+JCSno+'</option></select>');
//		}else{
//			$("#testType_"+lineId).find("span").html('<select name="testType" id="testType"><option value="0">'+JCSno+'</option><option value="1">'+JCSyes+'</option></select>');
//		}
//		
		$("#option_"+lineId).find("span").html('<a href="#" class="add center" id="save" onclick="BINOLSTJCS01.save('+lineId+');return false;"><span class="ui-icon icon-save-s"></span><span class="button-text">'+JCSSave+'</span></a><a href="#" class="add center" id="cancel" onclick="BINOLSTJCS01.search();return false;"><span class="ui-icon icon-delete"></span><span class="button-text">'+JCSCancel+'</span></a>');
		$("a[name=edit]").attr("onclick","");
	},
	
	/*
	 * 确认画面
	 */
	"confirmInit" : function (object,confirmname){
		$("#actionResultDisplay").empty();
		if(!this.validCheckBox()){
	        return false;
	    };
	    var depotInfoIDArr = [];
	    $("input[name='depotInfoIDArr']:checked").each(function(){
	    	depotInfoIDArr.push(this.value);
	    });
		var param = "depotInfoIDArr="+JSON2.stringify(depotInfoIDArr);
		var strText = '';
		var strTitle = '';
		if (confirmname == 'disable') {
			strText="#disableText";
			strTitle="#disableTitle";	
		}else if(confirmname == 'enable'){
			strText="#enableText";
			strTitle="#enableTitle";
		}
		var dialogSetting = {
			dialogInit: "#dialogInit",
			text:  $(strText).html(),
			width: 	500,
			height: 300,
			title: 	$(strTitle).text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){BINOLSTJCS01.confirmProcess($(object).attr("href"), param);},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
	},
	
	"confirmProcess":function (url,param) {
		var callback = function(msg) {
			removeDialog("#dialogInit");
			$("#actionResultDisplay").empty();
			BINOLSTJCS01.search(msg);
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback,
			formId: '#mainForm'
		});
	},

	/*
	 * 验证勾选，显示隐藏错误提示
	 */
	"validCheckBox":function(){
		var flag = true;
		var checksize = $("input@[id=flag][checked]").length;
		if (checksize == 0){
		    //没有勾选
			$("#errorMessage").html($("#errorMessageTemp").html());
	        $('#errorMessage').show();
	        flag = false;
	        }
		return flag;
	},

	/*
	 * 关闭Dialog
	 */
	"dialogClose":function(){
		removeDialog("#dialogInit");
	},
	
	"checkSelect":function(obj){
		$("#actionResultDisplay").empty();
		var $this = $(obj);
		var checkedFlag = $this.prop("checked");
		if(checkedFlag){
			var subAllNum = $("#dataTable_Cloned").find("input[name='depotInfoIDArr']").length;
			var subCheckedNum = $("#dataTable_Cloned").find("input[name='depotInfoIDArr']:checked").length;
			$(".FixedColumns_Cloned #selectAll").prop("checked",subAllNum==subCheckedNum? checkedFlag:false);
		}else{
			$(".FixedColumns_Cloned #selectAll").prop("checked",checkedFlag);
		}
	},
	"checkAll":function(obj){
		$("#actionResultDisplay").empty();
		var $this = $(obj);
		$("#dataTable_Cloned").find("input[name='depotInfoIDArr']").prop("checked",$this.prop("checked"));
	},
	/**
	 * 部门弹出框
	 * @return
	 */
	"departBox":function (thisObj,add){
		var param = "checkType=radio&privilegeFlg=1&businessType=1&levelFlg=1";
		var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
				if(add=="1"){
					var html ='<span class="left">('+departCode+')'+departName+'</span><span class="ui-icon icon-search right" onclick="BINOLSTJCS01.departBox(this,\'1\');return false;"></span><input name="organizationID" type="hidden" value="'+departId+'"/>';
					$('span[class="ui-icon icon-search right"]').parent().html(html);
				}else{
					var html ='<span class="left">('+departCode+')'+departName+'</span><span class="ui-icon icon-search right" onclick="BINOLSTJCS01.departBox(this);return false;"></span><input name="organizationID" type="hidden" value="'+departId+'"/>';
					$('span[class="ui-icon icon-search right"]').parent().html(html);
				}
				
			}
			var url= $("#testTypeUrl").attr("href");
			var param = "&csrftoken=" +$('#csrftoken').val();
			param=param+"&organizationID="+departId;
			var callbackajax= function(msg){
				if (msg == "1"){
					if(add=="1"){
					$($("[name=testTypeAdd]")[0]).get(0).selectedIndex=1;
					$($("[name=testTypeAdd]")[0]).attr("disabled",true);
					}else{
					$("#testType").get(0).selectedIndex=1;
					$("#testType").attr("disabled",true);
					}
			    }else if(msg == "0"){
					if(add=="1"){
					$($("[name=testTypeAdd]")[0]).get(0).selectedIndex=0;
					$($("[name=testTypeAdd]")[0]).attr("disabled",true);
					}else{
					$("#testType").get(0).selectedIndex=0;
					$("#testType").attr("disabled",true);
					}
				}
			};
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callbackajax
			});
		};
		popDataTableOfDepart(thisObj,param,callback);
	},
	
	"check":function(obj){
		var $this = $(obj);
		var patrn=/^\w{1,10}$/;
		if (!patrn.exec($this.val())){
			$this.val("");
		}
	},
	
	/**
	 * 详细画面弹出框
	 * @return
	 */
	"detail":function(depotInfoID){
		var param = "&csrftoken=" +$('#csrftoken').val();
		param=param+"&depotInfoID="+depotInfoID;
		var url = $("#depotInfoUrl").attr("href");
		var callback = function(msg) {
			var dialogSetting = {
					dialogInit: "#dialogInit",
					bgiframe: true,
					width: 	650,
					height: 500,
					text: msg,
					title: 	$("#detailTitle").text(),
					cancel: $("#dialogClose").text(),
					cancelEvent: function(){removeDialog("#dialogInit");}
				};
				openDialog(dialogSetting);
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	}
};
var BINOLSTJCS01 = new BINOLSTJCS01();

$(document).ready(function(){
	departBinding({
		elementId:"departName",
		privilegeFlag:1
	});
	
	deportBinding({
		elementId:"depotName",
		onlyNoneCounterDeport:true
	});
	
	BINOLSTJCS01.search();
});



