var BINOLPLSCF14 = function () {
};
BINOLPLSCF14.prototype = {
    // 清空错误信息
    "deleteActionMsg" : function (){
    	$('#errorMessage').empty();
    },
    // AJAX文件上传
    "ajaxFileUpload" : function (url){
    	// AJAX登陆图片
    	$ajaxLoading = $("#loading");
    	// 错误信息提示区
    	$errorMessage = $('#errorMessage');
    	// 清空错误信息
    	$errorMessage.empty();
    	var orgUrl = $("#orgUrl").val();
    	if($('#upfile').val()==''){
    		$("#errorMessage").html($("#errMsg").html());
    		return false;
    	}
    	var file = $("#upfile").val();
    	var indexSlant = file.lastIndexOf("\\");
    	var fileName = file.substring(indexSlant+1);
    	var indexPoint = fileName.lastIndexOf(".");
    	var fileCode = fileName.substring(0,indexPoint);
    	if(fileName.substring(indexPoint+1,fileName.length) != "xml" && 
    			fileName.substring(indexPoint+1,fileName.length) != "drl"){
    		$("#errorMessage").html($("#errfileMsg").html());
    	}else{
	    	$ajaxLoading.ajaxStart(function(){$(this).show();});
	    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
	    	$.ajaxFileUpload({ 
		        url: url,
		        secureuri:false,
		        data:{'csrftoken':$('#csrftoken').val(),'fileName':fileName},
		        fileElementId:'upfile',
		        dataType: 'html',
		        success: function (msg){
		        	var index = 0;
		        	if($("#dataTableBody").find("#indexFlag").length > 0){
		        		index = $("#dataTableBody").find("#indexFlag").last().val();
		        		index = parseInt(index)+1;
		        	}
		        	var tempDiv = document.createElement("div");
		        	$(tempDiv).append(msg);
		        	$(tempDiv).find("#fileName").val(fileName);
		        	$(tempDiv).find("#fileCode").val(fileCode);
		        	$(tempDiv).find("#indexFlag").val(index);
		        	$(tempDiv).find("#orgCode").attr("id","orgCode_" + index);
		        	$(tempDiv).find("#brandInfoId").attr("id","brandInfoId_" + index);
		        	var msgHtml = $(tempDiv).find("tr");
		        	$("#checkAll").prop("checked",true);
		        	$("#dataTableBody").append(msgHtml);
		        	$("#fileInfoTable").show();
		        }
	        });
    	}
    },
  //根据组织查询品牌List
    "searchBrand" : function (obj) {
    	var index = $(obj).parents("tr").find("#indexFlag").val();
    	var param = "orgCode=" + $("#orgCode_" + index).val();
    	param += (param ? "&":"") + "csrftoken=" + getTokenVal();
    	var url = $("#brandUrl").val();
    	$("#brandInfoId_" + index).empty();
    	cherryAjaxRequest({
    		url: url,
    		param: param,
    		callback: function(msg) {
			if (window.JSON2 && window.JSON2.parse) {
				var msgJson = window.JSON2.parse(msg);
				for ( var one in msgJson) {
					var selVal = msgJson[one]['brandCode'];
					var selLab = msgJson[one]['brandName'];
					var str = '<option value="' + selVal + '">'
							+ escapeHTMLHandle(selLab) + '</option>';
					$("#brandInfoId_" + index).append(str);
				}
			}
		}
    	});
    },
    "deleteFile" : function (obj){
    	$(obj).parents("tr").remove();
    },
    "save" : function (){
    	var url = $("#saveUrl").val();
    	var param = "";
    	var m = [];
    	if($('#dataTableBody :input[checked]').length == 0) {
    		$("#errorMessage").html($("#errMsg").html());
    		return false;
    	}
    	$('#dataTableBody :input[checked]').each(function (){
        	var n = [];
    		$(this).parents("tr").find(":input").each(function (){
    			n.push('"'+this.name+'":"' + $.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"')) + '"');
    		});
    		m.push("{" + n.toString() + "}");
    	});
    	param = "fileInfoStr=[" + m.toString() + "]";
    	param += (param ? "&":"") + "csrftoken=" + getTokenVal();
    	var callback = function(msg) {
    		$("#errorMessage").html(msg);
    		if($("#errorMessage").find(".actionError").length == 0){
    			$("#fileInfoTable").find("#dataTableBody").empty();
        		$("#fileInfoTable").hide();
    		}
    	};
    	cherryAjaxRequest({
    		url: url,
    		param: param,
    		callback: callback
    	});
    }

};
var BINOLPLSCF14 = new BINOLPLSCF14();