
function BINOLCTPMS01() {
	
};

BINOLCTPMS01.prototype = {
		"searchList":function(){
			var url = $("#searchUrl").attr("href");
			var params = $("#smsForm").serialize();
			if (params != null && params != "") {
				url = url + "?" + params;
			}
			$("#resultList").show();
			// 表格设置
			var tableSetting = {
				// 表格ID
				tableId : '#resultParamDataTable',
				// 数据URL
				url : url,
				// 表格默认排序
				aaSorting : [ [ 3, "desc" ]],
				// 表格列属性设置
				aoColumns : [{
					"sName" : "number",
					"sWidth" : "2%"
				}, {
					"sName" : "paramName",
					"sWidth" : "10%",
					"bSortable" : true
				}, {
					"sName" : "paramCode",
					"sWidth" : "10%",
					"bSortable" : true
				}, {
					"sName" : "configGroup",
					"sWidth" : "10%",
					"bSortable" : true
				}, {
					"sName" : "paramKey",
					"sWidth" : "15%",
					"bSortable" : true
				}, {
					"sName" : "paramValue",
					"sWidth" : "15%",
					"bSortable" : true
				}, {
					"sName" : "operator",
					"sWidth" : "15%",
					"bSortable" : true
				} ],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback : function() {

				}
			};
			// 调用获取表格函数
			getTable(tableSetting);
		},
		"editParam":function(type,obj){
			var dialogId = "editParam_dialog";
			var $dialog = $('#' + dialogId);
			$dialog.dialog({ 
				//默认不打开弹出框
				autoOpen: false,  
				//弹出框宽度
				width: 350, 
				//弹出框高度
				height: 250, 
				//弹出框标题
				title:$('#editParamTitle').text(), 
				//弹出框索引
				zIndex: 1,  
				modal: true, 
				resizable:false,
				//弹出框按钮
				buttons: [{
					text:$("#dialogConfirm").text(),//确认按钮
					click: function() {
						if(type == '1'){
							BINOLCTPMS01.editParams();
							$dialog.dialog("close");
						}else if(type == '2'){
							BINOLCTPMS01.confirm(obj);
							$dialog.dialog("close");
						}
					}
				},
				{	
					text:$("#dialogCancel").text(),//取消按钮
					click: function() {
						$dialog.dialog("close");
					}
				}],
				//关闭按钮
				close: function() {
					closeCherryDialog(dialogId);
				}
			});
			$dialog.dialog("open");
		},
		"confirm":function(obj){
			var editParamUrl=$('#editParamUrl').attr('href');
			var $this=$(obj).parents("tr");
			var configGroup=$('#configGroup').val();
			var paramKey=$this.find("input[name='paramKey']").val();
			var paramValue=$this.find("input[name='paramValue']").val();
			var supplierType=$this.find("input[name='supplierType']").val();
			var paramCode=$this.find("input[name='paramCode']").val();
			var params="configGroup="+configGroup+"&paramKey="+paramKey+"&paramValue="+paramValue+"&supplierType="+supplierType+"&paramCode="+paramCode;
			cherryAjaxRequest({
				url: editParamUrl,
				param:params,
				callback: function(data) {
					closeCherryDialog("dialogInit");
					BINOLCTPMS01.searchList();
				}
			});
		},	
		"cancel":function(){
			closeCherryDialog("dialogInit");
		},
		"editParams":function(){
			var editParamManyUrl=$('#editParamManyUrl').attr("href");
			var configGroup=$('#configGroup').val();
			var all=new Array();
			$('#resultParamDataTable tbody tr').each(function(){
				var $this=$(this);
				var paramKey=$this.find("input[name='paramKey']").val();
				var paramValue=$this.find("input[name='paramValue']").val();
				var supplierType=$this.find("input[name='supplierType']").val();
				var paramCode=$this.find("input[name='paramCode']").val();
				var map={
					"configGroup":configGroup,
					"paramKey":paramKey,
					"paramValue":paramValue,
					"supplierType":supplierType,
					"paramCode":paramCode
				};
				all.push(map);
			});
			var paramArr=JSON.stringify(all);
			var params="paramArr="+paramArr + "&" + getSerializeToken();
			cherryAjaxRequest({
				url: editParamManyUrl,
				param:params,
				callback: function(data) {
					closeCherryDialog("dialogInit");
					BINOLCTPMS01.searchList();
				}
			});
		}
};

var BINOLCTPMS01 =  new BINOLCTPMS01();
//初始化
$(document).ready(function() {
	BINOLCTPMS01.searchList();
});
