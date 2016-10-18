var holidays;
var BINOLSSPRM14_js_i18n= {};
$(document).ready(function(){	
	// 促销主活动绑定
	prmGrpNameBinding();
	search();
});

/**
 * 促销活动查询
 * @return
 */
function search (){
	$('#errorMessage2').html('');
	$('#section').show();
	// 查询参数序列化
	var params= $("#mainForm").serialize();
	// 取得url
	var url = $('#searchActiveUrl').html();
	url = url + "?" + params;
	var tableSetting = {
			 // 表格ID
			 tableId : '#act_dataTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [[ 0, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [  
			                { "sName": "activityName","sWidth": "25%"},                                    // 0
			                { "sName": "activityCode","sWidth": "25%"},                                    // 0
			                { "sName": "groupName","sWidth": "25%","bVisible": false},                                       // 1
							{ "sName": "startTime","sWidth": "10%","bSortable": false},                    // 2
							{ "sName": "endTime","sWidth": "10%","bSortable": false},                      // 3
							{ "sName": "createUserName","sWidth": "10%"},                                  // 4
							{ "sName": "validFlag","sWidth": "5%","sClass":"center"},                      // 5
							{ "sName": "operator","sWidth": "15%","bSortable": false}],  // 6
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function(){
				var $this = $('#act_dataTable');
				var $tip = $this.find("a.gray");
				if($tip.length > 0){
					var $tr = $tip.parent().parent();
					$tr.addClass('gray');
					$tr.attr('title',$tip.attr('title'));
					$tip.removeAttr('title');
					$tr.cluetip({width: '380',splitTitle:'|',showTitle:false});
				}
			}
	 };
	// 调用获取表格函数
	getTable(tableSetting);
}

/**
 * 促销主活动选择绑定
 * @return
 */
function prmGrpNameBinding(){
	var url = $('#indSearchPrmGrpNameUrl').html()+"?csrftoken="+$('#csrftoken').val();
	textBinding({elementId:"groupName",urlSrc:url,useId:"groupCode",index:3});
}
/**
 * 促销品活动过滤
 * @return
 */
function prmActDateFilter (filterType,thisObj){
	$('#ui-tabs').find('li').removeClass('ui-tabs-selected');
	$(thisObj).addClass('ui-tabs-selected');
	$('#actState').val(filterType);
	search();
}

function stopPrmActive (activeID,type){
	
	var url = $('#stopPrmActiveUrl').html();
	var param = "activeID="+activeID;
	if(type == 1){
		param += "&sendFlag=2";
	}
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:callBack,
		formId:"#mainForm"
	});
	
	function callBack(data){
		oTableArr[0].fnDraw();
		closeCherryDialog("stop_act_dialog");
	}
}

/**
 * 日历初始化事件绑定
 * @return
 */
function activeSearchCalEventBind(){		
	$('#searchPrmStartDate').cherryDate({
		holidayObj: holidays,
		onSelectEvent:calSelect
	});
	
	$('#searchPrmEndDate').cherryDate({
		holidayObj: holidays,
		// 结束时间大于起始时间
		beforeShow: function(input){										
			var minDateStr = $('#searchPrmStartDate').val();	
			var dateStr = minDateStr.split("-");
			var minDate = new Date(parseInt(dateStr[0],10),parseInt(dateStr[1],10)-1,parseInt(dateStr[2],10));
			var newtimems=minDate.getTime()+(parseInt($('#spaceDays').html(),10)*24*60*60*1000);
			var maxDate = new Date();
			maxDate.setTime(newtimems);
			var maxDateStr = changeDateToString(maxDate);
			return [minDateStr,maxDateStr];									
		}
	});
}
/**
 * 日历选中事件
 * @param dateText
 * @param inst
 * @return
 */
function calSelect(dateText, inst){
	var $input = inst.input;

	// 选择了开始时间
	if ($input.attr('id') == 'searchPrmStartDate'){
		var endDateStr = $('#searchPrmEndDate').val();
		var startDateStrArr = $input.val().split("-");
		var startDate =  new Date(parseInt(startDateStrArr[0],10),parseInt(startDateStrArr[1],10)-1,parseInt(startDateStrArr[2],10));
		var endDateStrArr = endDateStr.split("-");
		var endDate = new Date(parseInt(endDateStrArr[0],10),parseInt(endDateStrArr[1],10)-1,parseInt(endDateStrArr[2],10));
		var spaceTime = (parseInt($('#spaceDays').html(),10)*24*60*60*1000);
		if ((endDate.getTime()-startDate.getTime())>spaceTime){
			var newtimems=startDate.getTime()+spaceTime;
			endDate.setTime(newtimems);
			endDateStr = changeDateToString(endDate);
			$('#searchPrmEndDate').val(endDateStr);
		}	
	}
}
/**
 * 停用促销活动
 * @return
 */
function stopActive(activeID,type){
	var $p = $("#stop_act_dialog").find('p.message');
	var $message = $p.find('span');
	var $loading = $p.find('img');
	$loading.hide();
	var option = { 
			autoOpen: false,  
			width: 350, 
			height: 250, 
			title:BINOLSSPRM13_js_i18n.stopActiveTitle, 
			zIndex: 1,  
			modal: true,
			resizable:false,
			buttons: [{
				text:BINOLSSPRM13_js_i18n.ok,
				click: function() {
					$loading.show();
					stopPrmActive(activeID,type);
				}
			},
			{	text:BINOLSSPRM13_js_i18n.cancle,
				click: function() {
					closeCherryDialog("stop_act_dialog");
				}
			}],
			close: function() {
				closeCherryDialog("stop_act_dialog");
			}
		};
	if(type == 0){
		$message.text(BINOLSSPRM13_js_i18n.delActiveMsg);
	}else if(type == 1){
		$message.text(BINOLSSPRM13_js_i18n.stopActiveMsg);
	}
	$("#stop_act_dialog").dialog(option);
	$("#stop_act_dialog").dialog("open");
}
/**
 * 发布促销活动
 * @return
 */
function publishActive(url){
	var $p = $("#public_act_dialog").find('p.message');
	var $message = $p.find('span');
	var $loading = $p.find('img');
	$loading.hide();
	var $message2 = $('#errorMessage2');
	var option = { 
			autoOpen: false,  
			width: 350, 
			height: 250, 
			title:BINOLSSPRM13_js_i18n.stopActiveTitle, 
			zIndex: 1,  
			modal: true,
			resizable:false,
			buttons: [{
				text:BINOLSSPRM13_js_i18n.ok,
				click: function(_this) {
					$message2.html('');
					$loading.show();
					cherryAjaxRequest({
						url:url,
						callback:function(data){
							var json=JSON.parse(data);
							var html = '';
							var msg = json.ERRORMSG;
							if(msg == undefined || msg == null || msg == ''){
								msg = $('#publishMsg_' + json.ERRORCODE).val();
							}
							if(json.ERRORCODE != '0'){
								html = showMsg(msg + '[' + json.ERRORCODE + ']',1);
							}else{
								html = showMsg(msg,json.ERRORCODE);	
							}
							$message2.html(html);
							oTableArr[0].fnDraw();
							closeCherryDialog("public_act_dialog");
						}
					});
				}
			},
			{	text:BINOLSSPRM13_js_i18n.cancle,
				click: function() {
					closeCherryDialog("public_act_dialog");
				}
			}],
			close: function() {
				closeCherryDialog("public_act_dialog");
			}
		};
	$message.text(BINOLSSPRM13_js_i18n.publicActiveMsg);
	$("#public_act_dialog").dialog(option);
	$("#public_act_dialog").dialog("open");
}
function loadRule(url){
	$.ajax({
        url :url, 
        dataType:'json',
        data:{'csrftoken':$('#csrftoken').val()},
        success:function(code){
        	var $msg = $('#errorMessage');
        	if(code == '1'){
        		$msg.html(showMsg($('#loadRuleMsg_1').val(),0));
        	}else {
        		$msg.html(showMsg($('#loadRuleMsg_2').val() + '[CODE:'+code+']',1));
        	}
        }
	 });
}

/**
 * 审核促销活动
 * @return
 */
function checkActive(url){
	var $p = $("#check_act_dialog").find('p.message');
	var $message = $p.find('span');
	var $loading = $p.find('img');
	$loading.hide();
	var $message2 = $('#errorMessage2');
	var option = { 
			autoOpen: false,  
			width: 350, 
			height: 250, 
			title:BINOLSSPRM13_js_i18n.stopActiveTitle, 
			zIndex: 1,  
			modal: true,
			resizable:false,
			buttons: [{
				text:BINOLSSPRM13_js_i18n.ok,
				click: function(_this) {
					$message2.html('');
					$loading.show();
					cherryAjaxRequest({
						url:url,
						callback:function(data){
							var json=JSON.parse(data);
							var html = '';
							var msg = json.ERRORMSG;
							if(msg == undefined || msg == null || msg == ''){
								msg = $('#checkMsg_' + json.ERRORCODE).val();
							}
							if(json.ERRORCODE != '0'){
								html = showMsg(msg + '[' + json.ERRORCODE + ']',1);
							}else{
								html = showMsg(msg,json.ERRORCODE);	
							}
							$message2.html(html);
							oTableArr[0].fnDraw();
							closeCherryDialog("check_act_dialog");
						}
					});
				}
			},
			{	text:BINOLSSPRM13_js_i18n.cancle,
				click: function() {
					closeCherryDialog("check_act_dialog");
				}
			}],
			close: function() {
				closeCherryDialog("check_act_dialog");
			}
		};
	$message.text(BINOLSSPRM13_js_i18n.checkActiveMsg);
	$("#check_act_dialog").dialog(option);
	$("#check_act_dialog").dialog("open");
}