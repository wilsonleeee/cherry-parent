/**
 * @author zgl
 *
 */
var BINOLMOBUY01 = function () {};

BINOLMOBUY01.prototype = {
	
	/**
	 * 记录选中的渠道
	 * 
	 * */
	"checkedChannel":[],
	
	"getRegionDepartsAndChannels":function(){
	
		var url = $("#getRegionDepartsAndChannelsUrl").prop("href");
		var param = "brandInfoId="+$("#brandInfoId option:selected").val();
		param += "&csrftoken=" + getTokenVal();
		var callback = function(msg){
			var result = window.JSON2.parse(msg);
			var regionDapart = result.regionDepartList;
			var channelList = result.channelList;
			
			$("#reginDepartID option").each(function(i){
				if(i>0)
					$(this).remove();
			});
			var reginOptionHtml = "";
			for(var i=0 ; i<regionDapart.length ; i++){
				reginOptionHtml = reginOptionHtml + "<option value="+regionDapart[i].departId+">"+regionDapart[i].departName.escapeHTML()+"</option>";
			}
			$("#reginDepartID").append(reginOptionHtml);
			
			$("#channelSelect option").each(function(i){
				if(i>0)
					$(this).remove();
			});
			var channelOptionHtml = "";
			for(var i=0 ; i<channelList.length ; i++){
				channelOptionHtml = channelOptionHtml + "<option value="+channelList[i].channelId+">"+channelList[i].channelName.escapeHTML()+"</option>";
			}
			$("#channelSelect").append(channelOptionHtml);
			
		};
		
		cherryAjaxRequest({
    		url: url,
    		param: param,
    		callback: callback
    	});
		
	},
	
	"getUdiskAttendanceStatisticsList":function(){
		if (!$('#mainForm1').valid()) {
			return false;
		};
		$("#mainForm1").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		 var url = $("#getUdAttStaListurl").attr("href");
         // 查询参数序列化
         var params= $("#mainForm1").find(":input").serialize();
         params = params + "&csrftoken=" +$("#csrftoken").val();
         url = url + "?" + params;
		 // 显示结果一览
		 $("#section1").show();
		 // 表格设置
		 var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable1',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 1, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [	{ "sName": "no","bSortable": false},
								{ "sName": "UdiskSN"},
								{ "sName": "EmployeeCodeName"},
								{ "sName": "DepartCodeName"},
								{ "sName": "CategoryName"},
								{ "sName": "RegionNameChinese"},
								{ "sName": "ArriveCountersNm"},
								{ "sName": "ArriveDays"},
								{ "sName": "ArriNumPerDay","bSortable": false},
								{ "sName": "PerCoutArriTime"},
								{ "sName": "ImportTimeDays"},
								{ "sName": "ArriveDiffCountersNm"},
								{ "sName": "DepartNum"}
							],
								
				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 固定列数
				fixedColumns : 3,
				
				index:2
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	},
	
    
    /**
     * 导出统计结果Excel
     * 
     * */
    "statisticsExport":function(){
		if($(".dataTables_empty:visible").length==0){
	    	if (!$('#mainForm1').valid()) {
	            return false;
	        };
	        var url = $("#statisticsExportUrl").attr("href");
	        var params= $("#mainForm1").find(":input").serialize();
	        params = params + "&csrftoken=" +$("#csrftoken").val();
	        url = url + "?" + params;
	        window.open(url,"_self");
		}
    },
    
    
    // 显示省、市或县级市信息
    "showRegin" : function(object, reginDiv) {
    	var $reginDiv = $('#'+reginDiv);
    	if($reginDiv.is(':hidden') && $reginDiv.find('li').length > 0) {
    		var opos = $(object).offset();
    		var oleft = parseInt(opos.left, 10);
    		var otop = parseInt(opos.top + $(object).outerHeight(), 10);
    		$reginDiv.css({'left': oleft + "px", 'top': otop });
    		$reginDiv.show();
    		
    		if(reginDiv != 'provinceTemp') {
    			$('#provinceTemp').hide();
    		}
    		if(reginDiv != 'cityTemp') {
    			$('#cityTemp').hide();
    		}
    		if(reginDiv != 'countyTemp') {
    			$('#countyTemp').hide();
    		}
    		var firstFlag = true;
    		$("body").unbind('click');
    		// 隐藏弹出的DIV
    		$("body").bind('click',function(event){
    			if(!firstFlag) {
    				$reginDiv.hide();
    				$("body").unbind('click');
    			}
    			firstFlag = false;
    		});
    	}
    },
    
    // 省、市、县级市的联动查询
    "getNextRegin":function(obj, text, grade) {
    	var $obj = $(obj);
    	// 区域ID
    	var id =  $obj.attr("id");
    	// 区域名称
    	var name =  $obj.text();
    	// 下一级标志
    	var nextGrade = 1;
    	$("#cityText").parent().parent().removeClass('error');
    	$("#cityText").parent().parent().find('#errorText').remove();
    	// 选择省的场合
    	if(grade == 1) {
    		// 设置省名称
    		$("#provinceText").text(name);
    		// 省hidden值修改
    		if(id && id.indexOf("_") > 0) {
    			var arrayId = id.split("_");
    			$("#regionId").val(arrayId[0]);
    			$("#provinceId").val(arrayId[1]);
    			id = arrayId[1];
    		} else {
    			$("#provinceId").val(id);
    		}
    		// 城市不存在的场合
    		if($('#cityId').length == 0) {
    			return false;
    		}
    		// 清空城市信息
    		$('#cityId').val("");
    		$("#cityText").text(text);
    		$('#cityTemp').empty();
    		// 清空县级市信息
    		$('#countyId').val("");
    		$("#countyText").text(text);
    		$('#countyTemp').empty();
    		nextGrade = 2;
    	} 
    	// 选择城市的场合
    	else if(grade == 2) {
    		// 设置城市名称
    		$("#cityText").text(name);
    		// 城市hidden值修改
    		$("#cityId").val(id);
    		// 县级市不存在的场合
    		if($('#countyId').length == 0) {
    			return false;
    		}
    		// 清空县级市信息
    		$('#countyId').val("");
    		$("#countyText").text(text);
    		$('#countyTemp').empty();
    		nextGrade = 3;
    	} 
    	// 选择县级市的场合
    	else if(grade == 3) {
    		// 设置县级市名称
    		$("#countyText").text(name);
    		// 县级市hidden值修改
    		$("#countyId").val(id);
    		return false;
    	}
    	if(id == undefined || id == '') {
    		return false;
    	}
    	var url = $('#querySubRegionUrl').val();
    	var param = 'regionId=' + id + "&csrftoken=" + getTokenVal();
    	var callback = function(msg) {
    		if(msg) {
    			// 全部
    			var defaultText = $('#defaultText').text();
    			var json = eval('('+msg+')'); 
    			var str = '<ul class="u2"><li onclick="BINOLMOBUY01.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+defaultText+'</li>';
    		    for (var one in json){
    		        str += '<li id="'+json[one]["regionId"]+'" onclick="BINOLMOBUY01.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+escapeHTMLHandle(json[one]["regionName"])+'</li>';
    		    }
    		    str += '</ul>';
    		    if(grade == 1) {
    		    	$("#cityTemp").html(str);
    		    } else if(grade == 2) {
    		    	$("#countyTemp").html(str);
    		    }
    		}
    	};
    	cherryAjaxRequest({
    		url: url,
    		param: param,
    		callback: callback
    	});
    },
    
    "addChannel":function(obj){
    	var $checked = $(obj).find("option:selected");
    	var channelId = $checked.val();
    	var channeName = $checked.text();
    	var checkedChannel = this.checkedChannel;
    	var flag = true;
    	for(var i = checkedChannel.length-1 ; i>=0 ; i--){
    		if(channelId == "" || channelId == checkedChannel[i]){
    			flag = false;
    			break;
    		}
    	}
    	if(flag){
    		var showCheckedChannelHtml = '<span><input type="hidden" name="checkedChannelNameArr" value='+channeName+'/><span class="bg_title" style="margin: 0px 5px;">'+channeName+'</span><input type="hidden" name="checkedChannelArr" value='+channelId+' /><span onclick="BINOLMOBUY01.deleteChannel(this);return false;" class="close"><span class="ui-icon ui-icon-close"></span></span></span>';
        	$("#checkedChannelDiv").append(showCheckedChannelHtml);
        	this.checkedChannel.push(channelId);
    	}
    	$("#forbiddenChannel").prop("disabled","");
    	var firstOption = $(obj).find("option")[0];
    	$(firstOption).text($("#_select_default").val());
    	firstOption.selected = "selected";
    },
    
    "deleteChannel":function(obj){
    	var $this = $(obj);
    	var channelId = $this.prev().val();
    	var checkedChannel = this.checkedChannel;
    	//删除选中渠道数组中的channelID
    	for(var i = checkedChannel.length-1 ; i>=0 ; i--){
    		if(channelId == checkedChannel[i]){
    			this.checkedChannel.splice(i,1);
    		}
    	}
    	
    	$(obj).parent().remove();
    	
    	if($.trim($("#checkedChannelDiv").html()).length == 0){
    		$("#allowChannel").prop("checked","checked");
    		$("#forbiddenChannel").prop("disabled","disabled");
    		$($("#channelSelect").find("option")[0]).text($("#_BUY01_selectAll").val());
    	}
    },
    
    "addImportantTime":function(){
    	var importantHtml = $("#importantDivDemo").html();
    	$("#importTimeDiv").append(importantHtml);
//    	if($("#importTimeDiv").find("input[name='importTimeStartArr']").length > 2){
//    		$("#import").height($("#import").height()+35);
//    	}
    },
    
    "deleteImportantTime":function(obj){
    	var $this = $(obj);
    	var parent = $this.parent().parent();
    	parent.remove();
//    	if($("#import").height() > 48){
//    		$("#import").height($("#import").height() - 35);
//    	}else{
//    		$("#import").height("48px");
//    	}
    },
    
    "back":function(obj){
    	var $this = $(obj);
    	var parent = $this.parent().parent();
    	var startTime = parent.find("#startTime").text();
    	var endTime = parent.find("#endTime").text();
    	parent.find("#importTimeStartArr").val(startTime);
    	parent.find("#importTimeEndArr").val(endTime);
    	$this.parent().hide().prev().show();
    },
    
    "editImportantTime":function(obj){
    	var $this = $(obj);
    	$this.parent().hide().next().show();
    }
};

var BINOLMOBUY01 = new BINOLMOBUY01();

$(function(){
	
    cherryValidate({
        formId: 'mainForm1',
        rules: {
            startAttendanceDate: {dateValid:true},    // 开始日期
            endAttendanceDate: {dateValid:true}   // 结束日期
        }
    });

	$("#export1").live('click',function(){
		BINOLMOBUY01.statisticsExport();
	});
	
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		BINOLMOBUY01.showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		BINOLMOBUY01.showRegin(this,"cityTemp");
	});
	$("#channel").height($("#import").height());
});