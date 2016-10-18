
/**
 * 全局变量定义
 */


var binOLSSPRM30_global = {};
//逻辑条件
binOLSSPRM30_global.logicCondition = "";
//规则条件
binOLSSPRM30_global.ruleCondition = "";
//规则位置
binOLSSPRM30_global.conditionPosition = "con_this";
//当前点击对象
binOLSSPRM30_global.thisClickObj = {};
//ul HTML 
binOLSSPRM30_global.ulHTML = '<ul class = "items">';
//li HTML
binOLSSPRM30_global.liHTML = '<li class = "item">';
//促销奖励
binOLSSPRM30_global.rewardType ="rewardProduct";
//活动地点类型
binOLSSPRM30_global.loctionType="";


/**
* 页面初期处理
*/

$(document).ready(function() {

	//IE圆角修复 & IE表单交互
	if((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0))
	{
		//IE圆角修复
		$('.panel').prepend('<b class="xtop"><b class="xb1"></b><b class="xb2"></b><b class="xb3"></b><b class="xb4"></b></b>');
		$('.panel').append('<b class="xbottom"><b class="xb4"></b><b class="xb3"></b><b class="xb2"></b><b class="xb1"></b></b>');
		$('.menu').append('<b class="xtop"><b class="xb1"></b><b class="xb2"></b><b class="xb3"></b><b class="xb4"></b></b><b class="xbottom"><b class="xb4"></b><b class="xb3"></b><b class="xb2"></b><b class="xb1"></b></b>');
		$('.sidemenu a.on').append('<b class="xtop"><b class="xb1"></b><b class="xb2"></b><b class="xb3"></b><b class="xb4"></b></b><b class="xbottom"><b class="xb4"></b><b class="xb3"></b><b class="xb2"></b><b class="xb1"></b></b>');
		
		
		$(":text,textarea").focus(function(){
			$(this).addClass('input-focus');
		});
		$(":text,textarea").blur(function(){
			$(this).removeClass('input-focus');
		});
		
		$('.ui-option').bgiframe();
		
	}	
	
	// 选择促销品
	$('#selectPromotion').click(function(){
		selectPromotion ();
	});
	
	
});


/**
* 打开产品查询box
* @param thisObj
* @return
*/
function openProductSearchBox (thisObj){
	// 如果有产品数据,则不进行初期化查询
	if ($('#dataTableBody').find('tr').length == 0){
		var url = $('#prtSearchUrl').html()+"?csrftoken="+$('#csrftoken').val();
		
		var tableSetting = {
				 // 表格ID
				 tableId : '#prt_dataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 1, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
								{ "sName": "unitCode"},                     // 1
								{ "sName": "nameTotal"},                    // 2
								{ "sName": "salePrice"}]                    // 3
		 };
		
		// 调用获取表格函数
		getTable(tableSetting);
	}

	var opos = $(thisObj).offset();
	var oleft = parseInt(opos.left,10) ;
	var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
	
	$('#productDialog').dialog({position: [ oleft , otop ], width:800, height:300, zIndex: 9999, title:'产品信息', close: function(event, ui) { $(this).dialog( "destroy" ) } });
	binOLSSPRM30_global.thisClickObj = $(thisObj);
}

function addrow(){
	var rowNumber = $('#rowNumber').val();

	var newid = 'dataRow'+rowNumber
	$('#dataRow0').clone().attr('id',newid).removeAttr("style").appendTo("#databody");
	
	$('#'+newid + ' :checkbox').val(rowNumber);
	$('#rowNumber').val(++rowNumber);
	
}

function deleterow(){
	
	$("#databody :checkbox").each(function(){
		if($(this).attr("checked"))
			{
			if($(this).val()!=0){
			 $('#dataRow' + $(this).val()).remove();	
			}		               
			}					
		});
}
function selectAll(){
	if($('#allSelect').attr("checked")){
		$("#databody :checkbox").each(function(){
			if($(this).val()!=0){
				$(this).attr("checked",true);	
				}
			
			})
		}
	else{
		$("#databody :checkbox").each(function(){
			$(this).removeAttr("checked");})
		
	}
}


/**
* 选择产品
* @return
*/
function selectPruducts (){
	if ($('#dataTableBody').find('tr').length == 0){
		// 如果没有数据，则不进行选择
		return ;
	}
	
	// 取得选择的产品
	var selectedProArr = $('#dataTableBody input:checked');
	for (var i=0;i<selectedProArr.length;i++){
		var $selectedPro = $(selectedProArr[i]);
		var $selectedProInfoArr = $selectedPro.val().split('_');
		// 产品全称
		var $selectedProName = $selectedProInfoArr[1];
		// 产品id
		var $selectedProId = $selectedProInfoArr[0];
		var htmlTmpArr = new Array();
		var $this_ul = binOLSSPRM30_global.thisClickObj.parents('ul').eq(0);
		if ($this_ul.find(' > li .close').length==0){
			// 针对当前一条产品进行信息填充
			
			// 取得插入节点
			var $this_li =  binOLSSPRM30_global.thisClickObj.parents('li').eq(0);
			// 显示产品名
			getProductHtml2(htmlTmpArr,$selectedProName,$selectedProId);
			// 插入产品到li节点
			$this_li.append (htmlTmpArr.join(''));
		}else{
			// 需要插入多个产品数据
			var flag =false;
			// 数据过滤
			var li_arr = $this_ul.find('li');
			for (var j=0;j<li_arr.length;j++){
				var tmpProId = $(li_arr[j]).find('.highlight').attr('id');
				if ($selectedProId == tmpProId){
					flag = true;
					break;
				}
			}
			
			if (!flag){
				htmlTmpArr.push(binOLSSPRM30_global.liHTML);
				// 插入产品按钮
				getProductHtml(htmlTmpArr);
				// 插入产品数据
				getProductHtml2(htmlTmpArr,$selectedProName,$selectedProId);
				htmlTmpArr.push('</li>');
				// 插入产品到ul节点
				$this_ul.append (htmlTmpArr.join(''));
			}

		}
	}

	$('#productDialog').dialog("close");
}



/**
* 打开促销产品查询box
* @param thisObj
* @return
*/
function openPromotionSearchBox (thisObj){
	// 如果有产品数据,则不进行初期化查询
	if ($('#prm_dataTableBody').find('tr').length == 0){
		var url = $('#prmSearchUrl').html()+"?csrftoken="+$('#csrftoken').val();
		
		var tableSetting = {
				 // 表格ID
				 tableId : '#prm_dataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 1, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
								{ "sName": "unitCode"},                     // 1
								{ "sName": "barCode"},                      // 2
								{ "sName": "nameTotal"},                    // 3
								{ "sName": "salePrice"}]                    // 4
		 };
		
		// 调用获取表格函数
		getTable(tableSetting);
	}

	var opos = $(thisObj).offset();
	var oleft = parseInt(opos.left,10) ;
	var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
	
	$('#promotionDialog').dialog(
			{position: [ oleft , otop ], 
				width:800, 
				height:300, 
				zIndex: 9999, 
				title:'促销产品信息', 
				close: function(event, ui) { $(this).dialog( "destroy" ); 
				} 
			});
	binOLSSPRM30_global.thisClickObj = $(thisObj);
}

/**
* 选择促销品
* @return
*/
function selectPromotion () {
	if ($('#prm_dataTableBody').find('tr').length == 0){
		// 如果没有数据，则不进行选择
		return ;
	}
	
	var selectedPrmArr = $('#prm_dataTableBody input:checked');
	
	//var $li_obj = binOLSSPRM30_global.thisClickObj.parents('li').eq(0);
	
	var $tr_obj = binOLSSPRM30_global.thisClickObj.parent().parent();
	
	var trID = $tr_obj.attr('id');
	
	
	for (var i=0;i<selectedPrmArr.length;i++){
		var $selectedPrm = $(selectedPrmArr[i]);
		var $selectedPrmInfoArr = window.JSON2.parse($selectedPrm.val());
		
		// 促销品厂商编码
		var selectedPrmUnitCode = $selectedPrmInfoArr.unitCode;
		// 促销品条码
		var selectedPrmBarCode = $selectedPrmInfoArr.barCode;
		// 促销品名称
		var selectedPrmName = $selectedPrmInfoArr.nameTotal;
		// 促销品价格
		var selectedPrice = $selectedPrmInfoArr.standardCost;
		
		$('#'+trID +' > #dataTd1').find('span').eq(0).html(selectedPrmUnitCode);
		$('#'+trID +' #unitCodeArr').val(selectedPrmUnitCode);
		$('#'+trID +' > #dataTd2').html(selectedPrmBarCode);
		$('#'+trID +' #barCodeArr').val(selectedPrmBarCode);
		$('#'+trID +' > #dataTd3').html(selectedPrmName);
		$('#'+trID +' > #dataTd4').html(selectedPrice);
		$('#'+trID +' #priceUnitArr').val(selectedPrice);
		
	}
	$('#promotionDialog').dialog('close');
}


/**
 * 输入了发货数量
 * @param thisObj
 */
function changeCount(thisObj){
	var count = $(thisObj).val();
	$tr_obj = $(thisObj).parent().parent();
	var trID = $tr_obj.attr('id');
	var packge = $('#'+trID +' #productVendorPackageIDArr').val();
	
	$('#'+trID +' > #dataTd8').html(packge*count);
	$('#'+trID +' > #dataTd9').html(packge*count*$('#'+trID +' #priceUnitArr').val());
}

/**
 * 改变了包装类型
 * @param thisObj
 */
function changePackge(thisObj){
	
	$tr_obj = $(thisObj).parent().parent();
	var trID = $tr_obj.attr('id');
	$('#'+trID +' > #dataTd7 >#quantitypArr').val('');
	$('#'+trID +' > #dataTd8').html("0");
	$('#'+trID +' > #dataTd9').html("0.00");
}

/**
* 更改促销品查询checkbox状态
* @param thisObj
* @return
*/
function changeChecked (thisObj){
	// 取得更改checkbox后的状态
	var checkState = $(thisObj).attr('checked');
	if (checkState){
		// 先全部取消check状态
		$('#prm_dataTableBody .checkbox').attr('checked',false);
		// 复原单个选中
		$(thisObj).attr('checked',true);
	}
}
function showTest(thisObj){
	var opos = $(thisObj).offset();
	var oleft = parseInt(opos.left,10) ;
	var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
	$('#testDialog').dialog(
			{position: [ oleft , otop ], 
				width:800, 
				height:300, 
				zIndex: 9999, 
				title:'发货单', 
				close: function(event, ui) { $(this).dialog( "destroy" ); 
				} 
			});
}

function closeWindow(){
	$('#fahuodan').val('FHD2010110400001');
	$('#dataRow100').removeAttr('style');
	$('#dataRow101').removeAttr('style');
	$('#testDialog').dialog('close');
}