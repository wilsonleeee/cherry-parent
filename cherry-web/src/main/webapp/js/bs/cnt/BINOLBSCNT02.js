
function BINOLBSCNT02() {
	this.needUnlock = true;
};

BINOLBSCNT02.prototype = {	
	"editCounter" : function() {
		var parentToken = parentTokenVal();
		$("#parentCsrftoken").val(parentToken);
		binolbscnt02.needUnlock = false;
		$("#mainForm").submit();
	},
	"close" : function() {
		window.close();
	},
	"searchCouEvent" : function() {
	     var url = $("#searchCouEventUrl").attr("href");
	     var params = getSerializeToken() + "&" + $("#counterInfoId").serialize();
	     url = url + "?" + params;
	     // 表格设置
	     var tableSetting = {
	             // 表格ID
	             tableId : '#couEventDataTable',
	             // 数据URL
	             url : url,
	             // 表格默认排序
	             aaSorting : [[ 2, "desc" ]],
	             // 表格列属性设置
	             aoColumns : [  { "sName": "no","bSortable": false,"sWidth": "1%"},
	                            { "sName": "eventNameId","sWidth": "49%"},
	                            { "sName": "fromDate","sWidth": "50%"}
	                        ],
	                            
	            // 不可设置显示或隐藏的列  
	            //aiExclude :[0, 1],
	            // 横向滚动条出现的临界宽度
	            sScrollX : "100%",
	            index: 2
	            // 固定列数
	            //fixedColumns : 2
	     };
	     // 调用获取表格函数
	     getTable(tableSetting);
	},
	
	"searchCouSolution" : function() {
	     var url = $("#searchCouSolutionUrl").attr("href");
	     var params = getSerializeToken() + "&" + $("#counterInfoId").serialize()+ "&" + $("#counterCode").serialize();
	     url = url + "?" + params;
	     // 表格设置
	     var tableSetting = {
	             // 表格ID
	             tableId : '#couSolutionDataTable',
	             // 数据URL
	             url : url,
	             // 表格默认排序
	             aaSorting : [[ 2, "desc" ]],
	             // 表格列属性设置
	             aoColumns : [  { "sName": "no","bSortable": false,"sWidth": "1%"},
	                            { "sName": "solutionCode","sWidth": "19%"},
	                            { "sName": "solutionName","sWidth": "20%"},
	                            { "sName": "startDate","sWidth": "20%"},
	                            { "sName": "endDate","sWidth": "20%"},
	                            { "sName": "validFlag","sWidth": "10%"}
	                        ],
	                            
	            // 不可设置显示或隐藏的列  
	            //aiExclude :[0, 1],
	            // 横向滚动条出现的临界宽度
	            sScrollX : "100%",
	            index: 3
	            // 固定列数
	            //fixedColumns : 2
	     };
	     // 调用获取表格函数
	     getTable(tableSetting);
	}
};

var binolbscnt02 =  new BINOLBSCNT02();



$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	$('.tabs').tabs();
	$("a@[href='#tabs-3']").click(function() {
		binolbscnt02.searchCouSolution();
	});
	
	$("a@[href='#tabs-2']").click(function() {
		binolbscnt02.searchCouEvent();
    });
});

window.onbeforeunload = function(){
	if (binolbscnt02.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};



