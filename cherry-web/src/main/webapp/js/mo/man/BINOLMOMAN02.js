/*
 * 全局变量定义
 */
var binolmoman02_global = {};

//当前点击对象
binolmoman02_global.thisClickObj = {};

var BINOLMOMAN02 = function () {
    
};

BINOLMOMAN02.prototype = {
	// 参数序列化
	"getParams" : function (obj){
		return $(obj).find(":input").serialize();
	},

//	"getEmployeeParams":function(){
//		// 参数(品牌信息ID)
//		var params = $("#brandInfoId").serialize();
//		var parentToken = getParentToken();
//		params += "&" + parentToken;
//		return params;
//	},
	
	// 保存机器
	"save":function(){
		$("#actionResultDiv").hide();
		$('#errorDiv2').hide();
		if($("#machineInfoArr #machineCodeCol").length == 0){
			$('#errorDiv2 #errorSpan2').html($('#errmsg5').val());
			$('#errorDiv2').show();
			return false;
		}
		var param = $("#mainForm").formSerialize();
		param += "&"+$("#brandInfoId").serialize();
	    var callback = function(msg) {
	    	if(msg.indexOf('actionMessage"') > -1){
	    		$("#machineInfoArr").remove();
	    		$("#upExcel").val("");
	    	}
	    	//设置滚动条回到顶端
	        $('html,body').animate({scrollTop: '0px'}, 0);
        };
		cherryAjaxRequest({
			url: $('#addMachineInfoUrl').attr("href"),
			param: param,
			callback: callback,
			formId: '#mainForm'
	    });
    },

//    /* 
//     * 显示员工选择页面
//     * 
//     * Inputs:  Object:obj		选择员工按钮
//     * 			
//     * 
//     */
//    "showEmployeeDialog":function(obj){
//    	// 参数
//    	var params = this.getEmployeeParams();
//    	var $employeeId = $("#mainForm").find("input[name='employeeId']");
//    	if ($employeeId.length > 0) {
//    		var idParam = "";
//    		$employeeId.each(function (){
//    			if ("" != $(this).val()) {
//    				idParam += "&" + $(this).serialize();
//    			}
//    		});
//    		params += idParam;
//    	}
//    	popDataTableOfEmployee(obj, params);
//    	binolmoman02_global.thisClickObj = obj;
//    },
    
    //添加机器到表格
    "addMachine":function(obj){
    	var $form = $("#mainForm");
    	// 表单验证
    	if(!$form.valid()){
    		return;
    	}
		//截空格
		$form.find(":input").each(function() {
			//排除上传文件地址框
			if(this.id!=="upExcel"){
				$(this).val($.trim(this.value));
			}
		});
    	//重复比较
    	if(!this.checkDuplicate()){
    		return false;
    	}
    	//判断是否显示机器信息表格
    	$("#machineDiv").removeClass("hide");
    	if($("#machineDiv #machineInfoArr").length == 0){
    		$("#machineDiv").append($("#mydetail #tabledefault").html());
    	}
    	
    	//取序号
    	var index = $("#machineInfoArr").find("tr").length;
    	var selectedText = $("#machineType option:selected").text(); 
    	var strComment = $("#comment").val().escapeHTML();
    	var newRow = "";
    	newRow = "<tr><td id='row'>"+index+
	    	"</td><td id='machineCodeCol'>"+
	    	$("#machineCode").val()+
	    	"<input type='hidden' name ='machineCodeArr' value='"+$("#machineCode").val().escapeHTML()+"'>"+
	    	"<input type='hidden' name ='importFlagArr' value='0'>"+
	    	"</td><td id='phoneCodeCol'>"+
	    	$("#phoneCode").val()+
	    	"<input type='hidden' name ='phoneCodeArr' value='"+$("#phoneCode").val().escapeHTML()+"'>"+
	    	"</td><td id='machineTypeCol'>"+
	    	selectedText+
	    	"<input type='hidden' name='machineTypeArr' value='"+$("#machineType").val().escapeHTML()+"'>"+
	    	"</td><td id='commentCol'><p>"+strComment+"</p>"+
	    	"<input type='hidden' name ='commentArr' value='"+strComment+"'>"
	    	+"</td><td>"+$("#delMachine").html()+"</td></tr>";
	    $("#machineInfoArr").append(newRow);
	    //$("#machineInfoArr #commentCol p").last().text(strComment);
	    //$("#machineInfoArr #commentCol input@[name='commentArr']").last().val(strComment);
	    
    	//清空输入
    	$("#machineCode").val("");
    	$("#phoneCode").val("");
    	//$("#machineType").get(0).selectedIndex=0;
    	$("#comment").val("");
    	
    	$('#errorDiv2').hide();
    	$('#actionResultDiv').hide();
    },
     
    //从表格中删除一行机器信息
    "delDiv":function(obj){
    	if($("#machineInfoArr #machineCodeCol").length == 1){
    		$(obj).parent().parent().remove();
    		$("#machineDiv").addClass("hide");
    	}else{
        	$(obj).parent().parent().remove();
        	//重新显示序号
        	$("#machineInfoArr #row").each(function(i){
        		$(this).text(i+1);
        	});
    	}
    },
    
    //验证新增的编号和表格中的编号是否有重复
    "checkDuplicate":function(){
    	var flag = true;
    	$('#actionResultDisplay').html("");
		$("#machineInfoArr").find("#machineCodeCol").each(function() {
    		if($.trim($(this).text()) == $("#machineCode").val()){
    			$('#errorDiv2 #errorSpan2').html($('#errmsg1').val());
    			$('#errorDiv2').show();
    			flag = false;
    			return false;
    		}
    	});
    	if(flag && ($('#errorDiv2 #errorSpan2').text() == $('#errmsg1').val())){
    		$('#errorDiv2').hide();
    	}
    	return flag;
    },
    
    "ajaxFileUpload":function (){
    	BINOLMOMAN02.deleteActionMsg();
    	if($('#upExcel').val()==''){
    		$('#errorDiv2 #errorSpan2').html($('#errmsg3').val());
    		$('#errorDiv2').show();
    		$("#pathExcel").val("");
    		return false;
    	}
    	
    	$("#loading").show();
    	var params = "?"+$('#csrftoken').serialize();
    	params += "&"+$('#brandInfoId').serialize();
    	$.ajaxFileUpload(
            {
		         url:'/Cherry/mo/BINOLMOMAN02_UPLOAD.action'+params,            //需要链接到服务器地址
		         secureuri:false,
		         data:{'csrftoken':$('#csrftoken').val()},
		         fileElementId:'upExcel',                        //文件选择框的id属性
		         dataType: 'html',                                     //服务器返回的格式，可以是json
		         success: function (data, status)            //相当于java中try语句块的用法
		         {
		        	 $("#loading").hide();
		        	 $("#upExcel").val("");
		        	 if($(data).find("ul@[class='errorMessage']").length==0){//正常解析
			        	 $('#machineDiv').html(data);
			        	 $("#machineDiv").removeClass("hide");
		        	 }else{//错误信息
		        		 $('#machineDiv').html("");
		        		 $('#errorDiv2').attr("style",'');
		        		 $('#errorDiv2 #errorSpan2').html($(data).text());
		        	 }
		         },
		         error: function (data, status, e)            //相当于java中catch语句块的用法
		         {
		        	 $("#loading").hide();
		         }
            }
        );
    },
    
    /**
     * 删除掉画面头部的提示信息框
     * @return
     */
    "deleteActionMsg":function (){
    	$('#actionResultDisplay').html("");
    	$('#errorDiv2').attr("style",'display:none');
    }
};

var BINOLMOMAN02 = new BINOLMOMAN02();



$(function(){
	//机器编号验证
	jQuery.validator.addMethod("machineCodeValid", function(value, element) {
		if(this.optional(element)) {
			return true;
		}
		var number = 0;
		var number1 =0;
		var number2 =0;
		for(var i = 0;i<value.length;i++){
			number  = value.charAt(i);
			number1 += parseInt(number);
			if((i+1)==value.length){
				number1 =number1- parseInt(number);
				number2 = parseInt(number);
			}
		}

		var number3=""+number1+"";
		for(var i = 0;i<number3.length;i++){
			if((i+1)==number3.length){
				number1 = parseInt(number3.charAt(i));
			}
		}

		var curCodeRule = "";
		var ok= false;
		if(number1==number2){
			ok=true;
		}else{
			ok=false;
		}
		if ($.trim($("#machineType").val()).length>0){
			var regRule = "#machineTypeRule ul[id='"+$("#brandInfoId").val() +"'] span[id='"+$("#machineType").val() +"']";
			curCodeRule = $(regRule).html();
		}
		value = $.trim(value);
		var reg = new RegExp(curCodeRule,"g");
		if(ok==true && reg.test(value)==true){
			ok =true;
		}else{
			ok =false;
		}
		
		return this.optional(element) || ok;
	}, $("#errMsgText1").text());
	
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
            machineCode:{required: true, machineCodeValid:true},// 机器编号
		    phoneCode:{phoneValid: $("#mobileRule").val()},// 手机
            machineType:{required: true},
		    comment: {maxlength: 200}// 备注
		}
	});

});
	
//function selectEmployee(){
//	// 选中的员工
//	var $selRadio = $("#employee_dataTable").find(":radio[checked]");
//	if ($selRadio.length > 0) {
//		var selVal = $selRadio.val();
//		// 员工名称
//		var employeeName = $selRadio.parents("tr").find("td").first().next().next().text();
//		var $parentObj = $(binolmoman02_global.thisClickObj).parents("td");
//		$parentObj.find("#errorText").remove();
//		// 员工ID
//		$parentObj.find("input[name='employeeId']").val(selVal);
//		$(binolmoman02_global.thisClickObj).prev("span").text(employeeName);
//	}
//	$('#employeeDialog').dialog("destroy" );
//}
