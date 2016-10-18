var BINOLSSPRM69 = function(){
};
BINOLSSPRM69.prototype = {
	// 移动行
	"move" : function (_this,flag){
		var $tbody = $(_this).parent().parent().parent();
		var $tr = $(_this).parent().parent();
		if(flag == 0){			// 移到最前
			$tbody.prepend($tr);
		}else if(flag == 1){	// 上移
			var $prevTr = $tr.prev();
			$prevTr.before($tr);
		}else if(flag == 2){	// 下移
			var $nextTr = $tr.next();
			$nextTr.after($tr);
		}else if(flag == 3){	// 移到最后
			var $lastTr= $tbody.find('.ENMOVE').last();
			$lastTr.after($tr);
		}
		this.initLevelTab();
	},
	//等级表格初始化
	"initLevelTab": function(){
		//设置移动按钮
		var upHtml = $('#up').html();		 	//上移按钮
		var spaceHtml = $('#space').html(); 	//空格
		var downHtml = $('#down').html();   	//下移按钮
		$('.ENMOVE').each(function(i){
//			//设置编号
//			var No = i+1;
//			$(this).find('.No').html(No);
			//设置等级
			var lv = $('.ENMOVE').length - i;
			$(this).find('[name=level]').val(lv);
		});
		$(".ruleTable").each(function(i){
			var $ENMOVE_SORT = $("#ruleTable" + i).find('.ENMOVE .SORT'); 	//需要添加按钮的DOM
			if($ENMOVE_SORT.length > 1 ){
				$ENMOVE_SORT.html(upHtml + downHtml);
				$ENMOVE_SORT.first().html(spaceHtml + downHtml); //第一行只有下移
				$ENMOVE_SORT.last().html(upHtml + spaceHtml);	//最后一行只有上移
			}
		});
	},
	//等级调整编辑画面初始化
	"editLevel": function(){
		$('#saveLevelBtn').show();
		$('#tips1').show();
		$('#editLevelBtn').hide();
		$('[name=enContinue]:disabled').attr('disabled',false);
		PRM69.initLevelTab();
		//初始化拖拽表格
		$(".ruleTable").each(function(i){
			$( '#ruleTable' + i ).sortable({
				helper: function(e, tr) {
					 var $originals = tr.children();
					 var $helper = tr.clone();
					 $helper.children().each(function(i) {
						 $(this).width($originals.eq(i).width());
					 });
					 return $helper;
				},
				//start: function(e, tr){},
				stop:function (e, tr){PRM69.initLevelTab();},
				connectWith: 'tbody',
				items: 'tr:not(.DISMOVE)',
				cursor: 'move',
				grid: [500, 1]
			});
		});
	},
	//保存等级设置
	"saveLevel": function(){
		var params = Obj2JSONArr2('.ruleBody tr');
		if(!isEmpty(params)){
			var url = '/Cherry/ss/BINOLSSPRM69_saveLevel';
			param = 'params.ruleJson=' + params + '&brandId='+$('#brandId-1').val(),
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: function(msg){
					$('#actionResultDiv-1').html(msg);
					PRM69.initLevel();
				}
			});	
		}
	},
	//初始化等级设置画面
	"initLevel": function(){
		$('#editLevelBtn').show();
		$('#saveLevelBtn').hide();
		$('#tips1').hide();
		$('#ruleTable').sortable('destroy');
		var url = '/Cherry/ss/BINOLSSPRM69_initPage';
		cherryAjaxRequest({
			url: url,
			param: 'params.pageNo=0&brandId='+$('#brandId-1').val(),
			callback:function(msg){
				$('#ruleDiv').html(msg);
			}
		});	
	},
	//初始化排他关系设置画面
	"initRelation": function(){
		var url = '/Cherry/ss/BINOLSSPRM69_initPage';
		cherryAjaxRequest({
			url: url,
			param: 'params.pageNo=1&brandId='+$('#brandId-2').val(),
			callback:function(msg){
				$('#relationInfoDiv').html(msg);
			}
		});	
	},
	//停用或启用排他关系组
	"disOrEnableDialog": function(url,disOrEnable){
		var dialogId = '#disOrEnableDialog';
		if($(dialogId).length == 0) {
    		$('body').append('<div style="display:none" id="disOrEnableDialog"></div>');
    	} else {
    		$(dialogId).empty();
    	}
		//弹出框标题和内容
		var title = $('#disableTitle').text();
		var text = $('#disableMsg').html();
		if(disOrEnable == '1'){
			title = $('#enableTitle').text();
			text = $('#enableMsg').html();
		}
		var dialogSetting = {
    			dialogInit: dialogId,
    			text: text,
    			width: 350,
    			height: 250,
    			resizable: true,
    			title: 	title,
    			confirm: $('#confirm_btn').text(),
    			confirmEvent: function(){
    				var param = 'params.validFlag=' + disOrEnable + '&brandId=' + $('#brandId-2').val();
    				cherryAjaxRequest({
    					url: url,
    					param: param,
    					callback: function(msg){
    						PRM69.initRelation();
    						$('#actionResultDiv-2').html(msg);
    						removeDialog(dialogId);
    					}
    				});	
    			},
    			cancel: $('#cancel_btn').text(),
    			cancelEvent: function(){
    				removeDialog(dialogId);
    			}
    	};
		//启用时若含有启用的分组，提示停用后才能启用新的分组
		if(disOrEnable == '1'){
			var enable = $('#ruleRelationGroupBody .icon-valid');
			if(enable.length > 0){
				dialogSetting = {
	    			dialogInit: dialogId,
	    			text: '<p class="message"><span>' + $('#warnMsg').text() + '：【'+$('#ruleRelationGroupBody .icon-valid').text()+'】</span></p>',
	    			width: 350,
	    			height: 250,
	    			resizable: true,
	    			title: 	title,
	    			confirm: $('#confirm_btn').text(),
	    			confirmEvent: function(){
	    				removeDialog(dialogId);
	    			}
		    	};
			}
		}
		openDialog(dialogSetting);
	}
};
var PRM69 = new BINOLSSPRM69();
$(document).ready(function() {
	//初始化选项卡
	$('.tabs').tabs();
	$('a@[href="#tabs-1"]').click(function(){
		PRM69.initLevel();
	});
	$('a@[href="#tabs-2"]').click(function(){
		PRM69.initRelation();
	});
	//默认等级调整选项卡选中
	$('a@[href="#tabs-1"]').click();
});
