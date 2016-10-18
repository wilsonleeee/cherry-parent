/**
 * @author zgl
 *
 */
function BINOLMOMAN05(){};

BINOLMOMAN05.prototype={
		"ajaxFileUpload":function (){
			$("#actionResultDisplay").html("");
			$('#errorDiv #errorSpan').html("");
			$('#errorDiv').hide();
			if($('#upExcel').val()==''){
				$('#pathExcel').val("");
				$('#errorDiv #errorSpan').html($('#errmsg3').val());
				$('#errorDiv').show();
				return false;
			}
			var url = $("#importUdisk").html();
			$("#loading").show();
			$.ajaxFileUpload(
		        {
			         url:url,            //需要链接到服务器地址
			         secureuri:false,
			         data:{'csrftoken':getTokenVal()},
			         fileElementId:'upExcel',                        //文件选择框的id属性
			         dataType: 'html',                                     //服务器返回的格式，可以是json
			         success: function (data, status)            //相当于java中try语句块的用法
			         {
			        	 $("#loading").hide();
			        	 $("#upExcel").val("");
			        	 $("#actionResultDisplay").html("");
			        	 if(data.indexOf('class="actionMessage"') > -1){
			        		 $("#actionResultDisplay").html(data);
			        		 if(window.opener.oTableArr[0] != null)window.opener.binOLMOMAN06.search();
			        	 }else{
			        		 $("#actionResultDisplay").html(data);
			        	 }
			         },
			         error: function (data, status, e)            //相当于java中catch语句块的用法
			         {
			        	 $("#loading").hide();
			         }
		        }
		    );
		},
		"selectEmployee":function(){
			$checked = $("#employeeDialog").find("input[type=radio]:checked");
			$("#employeeCode").val( $checked.val());
		},
		"popDataTableOfEmployee":function(thisObj, param){
			$("#actionResultDisplay").html("");
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
					}
			 };
			
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
				close: function(event, ui) { $(this).dialog( "destroy" ); }
			};
			$('#employeeDialog').dialog(dialogSetting);
			$("#employeeConfirm").click(function(){
				binOLMOMAN05.selectEmployee();
				$('#employeeDialog').dialog( "destroy" );
			});
		},
		"getInformation":function(){
			$("#actionResultDisplay").html("");
			$('#errorDiv #errorSpan').html("");
			$('#errorDiv').hide();
			if(!$('#mainForm').valid()) {
				return false;
			}
			try{
				this.checkUdisk();
			}catch(e){
				if(e == "ex"){
					return false;
				}
			}
			var url = $("#getInformation").html();
			var param = $("#mainForm").serialize();
			var callback = function(html){
				if(html.indexOf('id="_udiskSn"') > -1){
					$("#udiskArr").find("tbody").append(html);
					$("#mainForm").find("#udiskSn").val("");
					$("#mainForm").find("#employeeCode").val("");
				}
			};
			var opt={
					url:url,
					param:param,
					callback:callback
			};
			cherryAjaxRequest(opt);
		},
		"checkUdisk":function(){
			$("#udiskArr").find("#_udiskSn").each(function(){
				var val = $.trim($("#mainForm").find("#udiskSn").val());
				if($(this).html() == val){
					$('#errorDiv #errorSpan').html($('#errmsg1').val());
					$('#errorDiv').show();
					throw "ex";
				}
			});
		},
		"saveUdisk":function(){
			$("#actionResultDisplay").html("");
			$('#errorDiv #errorSpan').html("");
			$('#errorDiv').hide();
//			if(!$('#mainForm').valid()) {
//				return false;
//			}
			if($("#udiskArr").find("tr").length==1){
				$('#errorDiv #errorSpan').html($("#errmsg2").val());
				$('#errorDiv').show();
				return false;
			}
			var url = $("#saveUdisk").html();
			var param = $("#mainForm").formSerialize();
			callback=function(msg){
				if(msg.indexOf('class="success"') > -1){
					$("div").removeClass("container");
	       		 	if(window.opener.oTableArr[0] != null)window.opener.binOLMOMAN06.search();
				}
			};
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:callback
			});
		},
		"deleteUdiskDetail":function(obj){
			var $this = $(obj);
			$this.parent().parent().remove();
		}
};

var binOLMOMAN05 = new BINOLMOMAN05();

window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};

$(document).ready(function(){
	// 取得父页面的token值
	$("#csrftoken").val(window.opener.getToken());
	cherryValidate({
		formId: "mainForm",		
		rules: {
		udiskSn:{required: true, alphanumeric:true, maxlength: 100}
		}
	});
});