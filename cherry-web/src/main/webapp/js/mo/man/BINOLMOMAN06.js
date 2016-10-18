/**
 * @author zgl
 *
 */
function BINOLMOMAN06() {
};

BINOLMOMAN06.prototype = {
		
	"employeeTableHtml":null,	
	
	"search":function(flag){
//	alert("1");
		$("#checkAll").prop("checked",false);
		this.bindMethod();
		if(flag == undefined){
			$("#actionResultDisplay").html("");
		}
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		var url = $("#search").html();
		var $form = $("#mainForm");
		// 表单验证
		if(!$form.valid()){
			return;
		}	 
		 var params= $form.serialize();
		 url = url + "?" + params;
		 // 表格设置
		 var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 index:1,
				 // 表格列属性设置
				// 表格默认排序
				 aaSorting : [[ 2, "asc" ]],
				 aoColumns : [{ "sName": "checkbox", "sWidth": "1%", "bSortable": false},
				              	{ "sName": "no.","sWidth": "5%","bSortable":false}, 			 // 0
								{ "sName": "udiskSn","sWidth": "25%"},					     // 1
								{ "sName": "employeeName","sWidth": "15%"},	                     // 2
								{ "sName": "ownerRight","sWidth": "15%"},                       // 3
								{ "sName": "brandName","sWidth": "15%"},					     // 4
								{ "sName": "ownerRight","sWidth": "15%"}],       // 5
		
				// 不可设置显示或隐藏的列	
				//aiExclude :[0,1,2],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%"
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	},
	
	"windOpen":function(url){
		var options = {
				fullscreen:"no",                    
				name : "",							// 弹出窗口的文件名
				height: 680,						// 窗口高度
				width: 720,						    // 窗口宽度
				top: 0,								// 窗口距离屏幕上方的象素值
				left: 0,							// 窗口距离屏幕左侧的象素值
				toolbar: "no",						// 是否显示工具栏，yes为显示
				menubar: "no",						// 是否显示菜单栏，yes为显示
				scrollbars: "yes",					// 是否显示滚动栏，yes为显示
				resizable: "no",					// 是否允许改变窗口大小，yes为允许
				location: "no",						// 是否显示地址栏，yes为允许
				status: "no",						// 是否显示状态栏内的信息（通常是文件已经打开），yes为允许
				center: "yes",						// 页面是否居中显示
				childModel: 1						// 弹出子页面的模式(1:只弹出一个子页面，2:可以弹出多个子页面，3:在一个窗口里切换页面)
			};
		popup(url,options);
	},
	
	"popDataTableOfEmployee":function(thisObj, param){
		//$("#actionResultDisplay").html("");
		var that = this;
		var url = $('#popEmployeeUrl').text();
		var csrftoken = $('#csrftoken').serialize();
		if(!csrftoken) {
			csrftoken = $('#csrftoken',window.opener.document).serialize();
		}
		url += "?" + csrftoken + (param?('&'+param):'');
		var tableSetting = {
				 // 一页显示页数
				 iDisplayLength:5,
				 // 表格ID
				 tableId : '#employee_dataTable',
				 // 数据URL
				 url : url,
				 // 表格列属性设置
				 aoColumns : [  { "sName": "employeeId","sWidth": "10%","bSortable": false}, 	  
								{ "sName": "employeeName","sWidth": "30%"},
								{ "sName": "departName","sWidth": "30%"},
								{ "sName": "categoryName","sWidth": "30%"}],
				index : 4,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback : function() {
					var value = $(thisObj).parent().next().find('#higherResult').find(':input[name=higher]').val();
					$('#employee_dataTable').find(':input').each(function(){
						if(value == $(this).val()) {
							$(this).prop('checked', true);
							return false;
						}
					});
					$("#employeeDialog").find("input[type=radio]").each(function(){
						$(this).change(function(){
							that.clearPopMessage();
						});
					});
				}
		 };
		
		oTableArr[tableSetting.index]=null;
		
		// 调用获取表格函数
		getTable(tableSetting);

		var dialogSetting = {
			bgiframe: true,
			width:650, 
			height:360,
			minWidth:650,
			minHeight:360,
			zIndex: 9999,
			modal: true, 
			resizable: false,
			title:'员工信息',
			close: function(event, ui) { 
				$('#employeeDialog').dialog( "destroy" );
				$('#employeeDialog').remove();
				$("#employeeBind_main").html(that.employeeTableHtml);
			}
		};
		
		$('#employeeDialog').dialog(dialogSetting);
		var id = $(thisObj).attr("id");
		$('#employeeDialog').find("#_udiskInfoId").val(id);
		$("#employeeConfirm").click(function(){
			that.employeeBind();
		});
	},
	
	"clearPopMessage":function(){
		$("#actionResultDisplay_pop").html("");
		$("#errorSpan_pop").html("");
		$("#errorDiv_pop").hide();
	},
	
	"employeeBind":function(){
		$("#errorDiv_pop #errorSpan_pop").html("");
		$("#errorDiv_pop").hide();
		if($("#employeeDialog").find("input[type=radio]:checked").length <=0){
			$("#errorDiv_pop #errorSpan_pop").html($("#errmsg1").val());
			$("#errorDiv_pop").show();
			return false;
		}
		var that = this;
		var url = $("#employeeBind").html();
		var param = "udiskInfoId=" + $("#_udiskInfoId").val()
				+ "&employeeCode="
				+ $("#employeeDialog").find("input[type=radio]:checked").val()
				+ "&brandInfoId=" + $("#brandInfoId").val()
				+ "&csrftoken=" + getTokenVal();
		var callback=function(msg){
			if(msg.indexOf('class="actionMessage"') > -1){
				that.search("flag");
				$('#employeeDialog').dialog( "destroy" );
				$('#employeeDialog').remove();
				$("#employeeBind_main").html(that.employeeTableHtml);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"employeeUnbind":function(){
		var that = this;
		var url = $("#employeeUnbind").html();
		var udiskIdArr = [];
		$("input[name=validFlag]:checked").each(function(){
			var o = {
					udiskInfoId:$(this).attr("id")
			};
			udiskIdArr.push(o);
		});
		var callback=function(msg){
			if(msg.indexOf('class="actionMessage"') > -1){
//				that.search("flag");
				that.refreshUdiskInfo(udiskIdArr);
				removeDialog("#dialogInit");
			}
		};
		var udiskIdStr = JSON2.stringify(udiskIdArr);
		var param = "udiskIdStr=" + udiskIdStr + "&brandInfoId="
				+ $("#brandInfoId").val() + "&csrftoken=" + getTokenVal();
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"refreshUdiskInfo":function(udiskIdArr){
		for(var index = 0 ; index < udiskIdArr.length ; index++){
			var id = udiskIdArr[index].udiskInfoId;
			var $tr = $("#"+id).parent().parent();
			var aHtml = '<a id="'+id+'" class="add"><span class="ui-icon icon-add"></span><span class="button-text">'+$("#bind").val()+'</span></a>';
			$("#"+id).prop("checked",false);
			$tr.find("#option").html(aHtml).find("#"+id).click(function(){
				binOLMOMAN06.popDataTableOfEmployee(this,$('#brandInfoId').serialize());
				return false;
			});
			var noEmployeeHtml = '<label class="highlight">'+$("#noEmployee").val()+'</label>';
			$tr.find("#employee").html(noEmployeeHtml);
			var disabledHtml = '<label class="highlight">'+$("#beDisabled").val()+'</label>';
			$tr.find("#ownerRight").html(disabledHtml);
			$tr.find("input:checkbox").val("0");
		}
	},
	
	"checkSelectAll":function(obj){
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		var $this = $(obj);
		if($this.prop("checked")){
			$("input[name=validFlag]").prop("checked",true);
		}else{
			$("input[name=validFlag]").prop("checked",false);
		}
		this.bindMethod();
	},
	
	"checkSelect":function(obj){
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		var $this = $(obj);
		if(!$this.prop("checked")){
			$("#checkAll").prop("checked",false);
		}else{
			var length = $("input[name=validFlag]").length;
			if($("input[name=validFlag]:checked").length == length){
				$("#checkAll").prop("checked",true);
			}
		}
		this.bindMethod();
	},
	
	"bindMethod":function(){
		var that = this;
		if($("input[name=validFlag]:checked").length == 0){
			$("#unbindBtn").unbind("click");
			$("#deleteBtn").unbind("click");
			$("#unbindBtn").bind("click",function(){
				$("#actionResultDisplay").html("");
				$("#errorDiv #errorSpan").html($("#noChecked").val());
				$("#errorDiv").show();
			});
			$("#deleteBtn").bind("click",function(){
				$("#actionResultDisplay").html("");
				$("#errorDiv #errorSpan").html($("#noChecked").val());
				$("#errorDiv").show();
			});
		}else{
			$("input[name=validFlag]:checked").each(function(){
				if($(this).val() == "0"){
					$("#unbindBtn").unbind("click");
					
					$("#unbindBtn").bind("click",function(){
						$("#actionResultDisplay").html("");
						$("#errorDiv #errorSpan").html($("#errChecked").val());
						$("#errorDiv").show();
					});
					return false;
				}else{
					$("#unbindBtn").unbind("click");
					$("#unbindBtn").bind("click",function(){
						that.confirmInit("unbind");
					});
				}
			});
			$("input[name=validFlag]:checked").each(function(){
				if($(this).val() == "0"){
					$("#deleteBtn").unbind("click");
					$("#deleteBtn").bind("click",function(){
						that.confirmInit("delete");
					});
				}else{
					$("#deleteBtn").unbind("click");
					
					$("#deleteBtn").bind("click",function(){
						$("#actionResultDisplay").html("");
						$("#errorDiv #errorSpan").html($("#errChecked").val());
						$("#errorDiv").show();
					});
					return false;
				}
			});
		}
	},
	
	"confirmInit":function(optType){
		var that = this;
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		if(optType == "unbind"){
			var text = '<p class="message"><span>'+$("#unbindText").val();
			var title = $("#unbindTitle").val();
		}else{
			var text = '<p class="message"><span>'+$("#deleteText").val();
			var title = $("#deleteTitle").val();
		}
		var dialogSetting = {
				dialogInit: "#dialogInit",
				text: text,
				width: 	500,
				height: 300,
				title: 	title,
				confirm: $("#dialogConfirm").val(),
				cancel: $("#dialogCancel").val(),
				confirmEvent: function(){that.udiskOpt(optType);},
				cancelEvent: function(){removeDialog("#dialogInit");}
			};
		openDialog(dialogSetting);
	},
	
	"udiskOpt":function(optType){
		if(optType == "unbind"){
			this.employeeUnbind();
		}else{
			this.employeeDelete();
		}
	},
	
	"employeeDelete":function(){
//		alert(1);
		var that = this;
		var url = $("#deleteUdisk").html();
		var udiskIdArr = [];
		$("input[name=validFlag]:checked").each(function(){
			var o = {
					udiskInfoId:$(this).attr("id")
			};
			udiskIdArr.push(o);
		});
		var callback=function(msg){
			if(msg.indexOf('class="actionMessage"') > -1){
				that.search("flag");
				removeDialog("#dialogInit");
			}
		};
		var udiskIdStr = JSON2.stringify(udiskIdArr);
		var param = "udiskIdStr=" + udiskIdStr + "&brandInfoId="
				+ $("#brandInfoId").val() + "&csrftoken=" + getTokenVal();
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	}
};

var binOLMOMAN06 = new BINOLMOMAN06();

function getToken(){
	return $("#csrftoken").val();
}
$(document).ready(function(){
	binOLMOMAN06.search();
	binOLMOMAN06.employeeTableHtml=$("#employeeBind_main").html();
	$("#addUdisk").click(function(){
		var href = $('#addInit').html()+"?csrftoken=" + $('#csrftoken').val();
		binOLMOMAN06.windOpen(href);
		return false;
	});
});