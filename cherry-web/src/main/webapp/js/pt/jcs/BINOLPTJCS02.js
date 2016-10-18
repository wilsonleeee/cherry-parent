var BINOLPTJCS02 = function(){
};
BINOLPTJCS02.prototype = {

	"search" : function (flag){
		var that = this;
		if(!flag)that.empty();
		var url=url?url:$("#search").html();
		//var param = $("#brandInfoId").serialize()+ "&csrftoken=" +$('#csrftoken').val();
		var divId = "#dataTable";
		cherryAjaxRequest({
			url: url,
			//param: param,
			callback: function(msg) {
			    $(divId).html(msg);
			    
			    //更新编辑区分 --完成编辑
				$("#flag").val("0");
			}
		});
		$("#checkAll").prop("checked",false);
	},
	// 编辑
	"edit" : function (data){
		var that = this;
		that.empty();
		if(that.disabOptBtn()==false)return;
		$("#"+data).find("[name='propertyName']").parent().prev().addClass("hide");
		$("#"+data).find("[name='propertyName']").parent().removeClass();
		$("#"+data).find("[name='propertyNameForeign']").parent().prev().addClass("hide");
		$("#"+data).find("[name='propertyNameForeign']").parent().removeClass();
		$("#"+data).find("td:last").find(".hide").prev().addClass("hide");
		$("#"+data).find("td:last").find(".hide").next().removeClass();
		
		//更新编辑区分 --编辑中
		$("#flag").val("1");
	},
	// 更新
	"save" : function (_this,url,flag){
		var that = this;
		that.empty();
		var param="";
		if(flag=="-1"){
			param = $("#dataTable tr:last").find(":input").serialize();
		}else{
		    param = $("#"+flag).find(":input").serialize();
		}
		//param += "&" + $("#brandInfoId").serialize();
		var divId = "#dataTable";
		
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				// 返回无错误
				if(msg.indexOf('id="actionResultDiv"') > -1
						&& msg.indexOf('class="errorMessage"') == -1){
					that.search(true);
				}
				
			}
		});
		
	},
	// 添加新行
	"addLine":function (target,source){
		var that = this;
		that.empty();
		
		if(that.disabOptBtn()==false)return;
		//清除所有选中
		$("#dataTable").find(":checkbox").prop("checked",false);
		$("#checkAll").prop("checked",false);
		var $target = $(target);
		if($target.find(":text").length !=0){
//			return;
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
			num = parseInt($cols1.eq(1).find("span").html());
		}
		// 新行添加序号
		$cols2.eq(1).find("span").html(num + 1);
		// 新行添加Class
		if(lastClass == "odd"){
			$newLine.attr("class","even");
		}else{
			$newLine.attr("class","odd");
		}
		// 目标追加新行
		$target.append($source.html());
		
		//更新编辑区分 --编辑中
		$("#flag").val("1");
	},
	//停用或启用      --停用data为0，启用data为1
	"disOrEnable":function(target,url,data){
		var that = this;
		that.empty();
		if(that.disabOptBtn()==false)return;
		$target = $(target);
		var param="";
		$checked=$target.find("tr td input:checked");
		if($checked.length == 0){
			that.selectEmply();
			return;
		}
		$checked.each(function(){
			//param+=$(this).nextAll("[name='extendPropertyID']")[0].value+"join"+data+",";
			var groupID = $(this).parent().parent().find("[name='groupID']").val();
			param+=$(this).nextAll("[name='extendPropertyID']")[0].value+"join"+data+"join"+groupID+",";
		});
		param="requestData="+param;
		var divId = "#dataTable";
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				// 返回无错误
				if(msg.indexOf('id="actionResultDiv"') > -1){
					that.search(true);
				}
			}
		});
	},
	"checkSelectAll":function(checkbox){
		var that = this;
		that.empty();
		if($(checkbox).prop("checked"))
		{
			$("#dataTable").find(":checkbox").prop("checked",true);
		}else
		{
			$("#dataTable").find(":checkbox").prop("checked",false);
		}
	},
	"empty":function(){
		$("#actionResultDisplay").empty();
		$("#errorMessage").empty();
	},
	/**
	 * 弹出停用
	 * 
	 * 
	 * */
	"confirmInit":function(data)
	{
		var that = this;
		that.empty();
		if(that.disabOptBtn()==false)return;
		var $checked = $("#dataTable").find("tr td input:checked");
		if($checked.length == 0){
			that.selectEmply();
			return;
		}
		var url=$("#disOrEnable").html();
		var text = "";
		var title = "";
		if(data=="0"){
		   text = '<p class="message"><span>'+$('#confirIsDisable').text();
		   title = $('#disableTitle').text();
		}else{
		   text = '<p class="message"><span>'+$('#confirIsEnable').text();
		   title = $('#enableTitle').text();
		}
		var dialogSetting = {
			dialogInit: "#dialogInit",
			text: text,
			width: 	300,
			height: 200,
			title: 	title,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){that.disOrEnable('#dataTable',url,data);removeDialog("#dialogInit");},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
	},
	/**
	 * 选择为空
	 * 
	 * 
	 */
	"selectEmply":function (){
		$("#errorMessage").empty();
		$("#actionResultDisplay").empty();
		$("#errorMessage").html($("#errorMessageTemp1").html());
	},
	/**
	 * 设置按钮有效与无效
	 * 
	 * 
	 */
	"disabOptBtn":function (){
		var flag = $("#flag").val();
		if(flag == "1"){
			return false;
		}
		return true;
	},
	"checkBoxClick":function(){
		var that = this;
		that.empty();
		
		var $eachCheck = $("#dataTable").find(":checkbox");
		var flag =true;
		$eachCheck.each(function(){
			if($(this).prop("checked")==false){
				flag = false;
			};
		});
		if(flag){
			$("#checkAll").prop("checked",true);
		}else{
			$("#checkAll").prop("checked",false);
		}
		return;
	}
};
var BINOLPTJCS02 = new BINOLPTJCS02();
