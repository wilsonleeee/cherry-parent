var BINOLSSPRM69_2 = function(){
	this.needUnlock = true;
};
BINOLSSPRM69_2.prototype = {
	//编辑排他关系
	"editRelation": function(){
		cherryAjaxRequest({
			url: '/Cherry/ss/BINOLSSPRM69_initPage',
			param: 'params.pageNo=2&params.groupNo=' + $('#groupNo').val() + '&brandId='+$('#brandId').val(),
			callback:function(msg){
				$('body').html(msg);
			}
		});	
	},
	//添加排他关系
	"addRelation": function(){
		var html = $('#ruleRelationAddHtml').html();
		$('#ruleRelationTable tbody').append(html);
		//设置行号和样式
		var No = $('#ruleRelationTable tbody tr').length;
		$('#ruleRelationTable tr').last().find('td').first().html('<span class="No">' + No + '</span>');
	},
	//取消添加排他关系
	"cancelAddRelaton": function(thisObj){
		$(thisObj).parent().parent().remove();
		//重置行号
		$('#ruleRelationBody').find('tr').each(function(i){
			$(this).find('td').first().html('<span class="No">' + (i+1) + '</span>');
		});
	},
	//选择排他关系类型
	"selectRelationVal": function(thisObj,index,isClear){
		//行
		var $rowObj = $(thisObj).parent().parent();
		if(isClear){
			//清除已选值
			$rowObj.find('.ICODE' + index).val('');				
			$rowObj.find('.DISPLAY' + index).html('');
			$rowObj.find('.DISPLAY' + index).parent().attr('class','classification_3');
		}
		//选中值
		var relationVal = $rowObj.find('.ICODE' + index).val();
		$('#relationValue').val(relationVal);
		//选中值类型
		var relationType = $(thisObj).find('.TYPE' + index).val();
		if(relationType){
			$('#relationType'+relationType).attr('checked',true);
		}
		relationType = $('input:radio[name="relationType"]:checked').val();
		//初始化表格
		PRM69_2.selectRelationType('#relationType'+relationType);
		//弹出框
    	var dialogSetting = {
				width: 600,
				height: 'auto',
				minHeight: 250,
				resizable: true,
				title: 	$('#selectRelationValTitle'+ relationType).text(),
				buttons: [
					        {
					        	text: $('#confirm_btn').text(),
								click: function(){
									var $selectObj = $('input:radio[name="code"]:checked');
									var code = $selectObj.val();
									if(code){
										var name = $selectObj.parent().find("[name=name]").val();
										var type = $('input:radio[name="relationType"]:checked').val();
										//回写选中值
										$rowObj.find('.TYPE' + index).val(type);
										$rowObj.find('.ICODE' + index).val(code);				
										$rowObj.find('.DISPLAY' + index).html('（' + code + '）' + name);
										$rowObj.find('.DISPLAY' + index).parent().attr('class','classification_' + type);
										$('#relationValDialog').dialog('destroy');
									}
								}
					        },
					        {
					        	text: $('#cancel_btn').text(),
					        	click: function(){
					        		$('#relationValDialog').dialog('destroy');
								}
					        }
				        ]
		};
    	$('#relationValDialog').dialog(dialogSetting);
    	//清空搜索框
		$(":input[name=searchText]").val("");
	},
	//选择关联值
	"selectRelationType": function(thisObj){
		var relationType = $(thisObj).val();
		$('#relationValDialog').dialog( "option", {
			title: 	$('#selectRelationValTitle'+ relationType).text()
		});
		var url = '/Cherry/ss/BINOLSSPRM69_initPage?params.pageNo=3&params.relationType=' + relationType 
					+ '&params.relationVal=' + $('#relationValue').val()
					+ '&brandId=' + $('#brandId').val() +'&' + getSerializeToken();
		var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#relationValTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","sWidth": "5%","bSortable": false}, 
							{ "sName": "code","sWidth": "15%","bSortable": false},                  
							{ "sName": "name","sWidth": "15%","bSortable": false}],                 
			index:1,
			colVisFlag: true,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"relationTableFilter": function(){
		var searchText = $("#relationValDialog [name=searchText]").val();
		var relationType = $('#relationValDialog input[name="relationType"]:checked').val();
		var url = '/Cherry/ss/BINOLSSPRM69_initPage?params.pageNo=3&params.relationType=' + relationType 
		+ '&params.relationVal=' + $('#relationValue').val()
		+ '&params.searchText=' + searchText
		+ '&brandId=' + $('#brandId').val() +'&' + getSerializeToken();
		var oSettings = oTableArr[1].fnSettings();
//		var url = oSettings.sAjaxSource;
//		url += '&params.searchText=' + searchText;
		oSettings.sAjaxSource = url;
		// 刷新表格数据
		oTableArr[1].fnDraw();
	},
	//保存排他关系
	"saveRelation": function(){
		$('#ruleRelationBody tr').removeClass('errTRbgColor');
		var $form = $('#basicForm');
    	// 分组信息表单验证
    	flag = $form.valid();
    	if(!flag){
    		return;
    	}
		var relationJson = Obj2JSONArr('#ruleRelationBody tr');
		var relationArr = JSON.parse(relationJson);
		if(relationArr.length < 1){
			$('#errorSpan').html($('#errMsg2').html());
			$('#errorDiv').show();
			return;
		}
		//空数据检查
		for(var i = 0; i < relationArr.length; i++){
			var relation = relationArr[i];
			if(!relation.relationTypeA || !relation.relationTypeB 
				|| !relation.relationValueA || !relation.relationValueB ){
				$('#errorSpan').html($('#errMsg1').html());
				$('#errorDiv').show();
				$('#ruleRelationBody tr').eq(i).attr('class','errTRbgColor');
				return;
			}
		}
		//重复数据检查
		flag = PRM69_2.checkRepeat(relationArr);
		if(flag == 1){
			$('#errorSpan').html($('#errMsg3').html());
			$('#errorDiv').show();
			return;
		}else if(flag == 2){
			$('#errorSpan').html($('#errMsg4').html());
			$('#errorDiv').show();
			return;
		}
		var url = '/Cherry/ss/BINOLSSPRM69_saveRelation';
		var relationGroupJson = '{' + Obj2JSON('#basicInfo') + '}';
		var brandId = $('#brandId').val();
		var params = 'params.relationGroupJson=' + relationGroupJson + '&params.relationJson=' + relationJson + '&brandId=' + brandId;
		cherryAjaxRequest({
    		url: url,
    		param: params,
    		callback: function(msg){
    			//操作成功刷新父页面
    			var url = '/Cherry/ss/BINOLSSPRM69_initPage';
    			cherryAjaxRequest({
    				url: url,
    				param: 'params.pageNo=1&brandId=' + brandId,
    				callback:function(msg){
    					$(window.opener.document).find('#relationInfoDiv').html(msg);
    				}
    			});	
    		}
    	});
	},
	//初始化排他关系设置画面
	"initRelation": function(){
		cherryAjaxRequest({
			url: '/Cherry/ss/BINOLSSPRM69_initPage',
			param: 'params.pageNo=5&params.groupNo=' + $('#groupNo').val() + '&brandId='+$('#brandId').val(),
			callback:function(msg){
				$('body').html(msg);
			}
		});	
	},
	//检查明细重复数据
	"checkRepeat": function(array){
		for(var i = 0; i < array.length; i++){
			//获取与值item1.relationValueA相关联的值及行号，并存入数组arr
			var item1 = array[i];
			if(item1.isChecked){
				console.log(item1.relationValueA);
				continue;
			}
			var arr = [{"rowNumber":i,"val":item1.relationValueB}];
			for(var j = i + 1; j < array.length; j++){
				var item2 = array[j];
				if(item1.relationValueA == item2.relationValueA){
					arr.push({"rowNumber":j,"val":item2.relationValueB});
					item2.isChecked = true;
				}else if(item1.relationValueA == item2.relationValueB){
					arr.push({"rowNumber":j,"val":item2.relationValueA});
					item2.isChecked = true;
				}
			}
			//检查数组arr中是否存在重复值，存在将重复的行标记并返回true
			for(var m = 0; m < arr.length; m++){
				var flag = false;
				for(var n = m+1; n < arr.length; n++){
					if(arr[m].val == arr[n].val){
						flag = true;
						$('#ruleRelationBody tr').eq(arr[m].rowNumber).attr('class','errTRbgColor');
						$('#ruleRelationBody tr').eq(arr[n].rowNumber).attr('class','errTRbgColor');
					}
				}
				if(flag){
					return 1;
				}
			}
			//单条促销规则不能排他自己
			if(item1.relationTypeA == '2' && item1.relationTypeB == '2' 
				&& item1.relationValueA == item1.relationValueB){
				$('#ruleRelationBody tr').eq(i).attr('class','errTRbgColor');
				return 2;
			}
		}
		return 0;
	}
};
var PRM69_2 = new BINOLSSPRM69_2();
$(document).ready(function() {
	cherryValidate({//form表单验证
		formId: 'basicForm',		
		rules: {
			groupName:{required: true},
			comments: {maxlength:200}
		}		
	});
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});
window.onbeforeunload = function(){
	if (PRM69_2.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
