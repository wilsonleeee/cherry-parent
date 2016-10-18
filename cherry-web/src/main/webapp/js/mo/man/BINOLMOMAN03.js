var BINOLMOMAN03 = function () {
    
};

BINOLMOMAN03.prototype = {
	/* 
	 * 初始化查询
	 */
	"search":function(){
	    var csrftoken = $('#csrftoken').serialize();
	    if(!csrftoken) {
	       csrftoken = $('#csrftoken',window.opener.document).serialize();
	    }
	    var url = $("#searchUrl").attr("href");
		var params= $("#bindCounterForm").serialize();
		url = url + "?" + csrftoken+'&' +params;
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格列属性设置
	             aoColumns : [  { "sName": "checkbox","sWidth": "1%","bSortable": false},         // 0
	                            { "sName": "CounterCode","sWidth": "25%"},                        // 1
	                            { "sName": "CounterNameIF","sWidth": "74%"}]   // 2			
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
		
	/* 
	 * 绑定机器
	 */
	"moman03_bind":function(url,param){
		if(!this.validCheckBox()){
	        return false;
	    };
		
		var param = $("#bindCounterForm").serialize();
		param += "&CounterCode="+$.trim($(":radio[checked]").parent().parent().find("td:eq(1)").text());
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		var callback = function(msg) {
			if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
		};
	
		cherryAjaxRequest({
			url: $('#bind').attr('href'),
			param: param,
			callback: callback
		});
	},
		
	/* 
	 * 验证勾选
	 */
	"validCheckBox":function (){
		var flag = true;
		var checksize = $("input@[name=counterInfoId][checked]").length;
		if (checksize == 0){
		    //没有勾选
	        $('#errorDiv2 #errorSpan2').html($('#errmsg1').val());
	        $("#errorMessage").html($('#errorDiv2').html());
	        flag = false;
	        //设置滚动条回到顶端
	        $('html,body').animate({scrollTop: '0px'}, 0);
		}else{
			$("#errorMessage").empty();
		}
		return flag;
	}	
};
var BINOLMOMAN03 = new BINOLMOMAN03();

window.onbeforeunload = function(){
    if (window.opener) {
        window.opener.unlockParentWindow();
    }
};

$(function(){
	BINOLMOMAN03.search();
    if (window.opener) {
        window.opener.lockParentWindow();
        
    }
    
	// 柜台选择
	$("input[name='counterInfoId']").live('click',function(){
		if(this.checked) {
			$("#errorMessage").empty();
		}
	});
});