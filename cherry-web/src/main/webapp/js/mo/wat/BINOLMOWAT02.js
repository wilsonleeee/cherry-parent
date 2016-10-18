var BINOLMOWAT02 = function () {};

BINOLMOWAT02.prototype = {
    /* 
     * 初始化查询
     */
    "search":function(){
        if (!$('#mainForm').valid()) {
            return false;
        };
        $("#mainForm").find(":input").each(function() {
            $(this).val($.trim(this.value));
        });
        var checkedlen = $("#divRule").find(":input[checked]").length;
        if(checkedlen==0){
 			$('#errorDiv2 #errorSpan2').html($("#errmsg1").val());
 			$('#errorDiv2').show();
 			return false;
 		}
         var url = $("#searchUrl").attr("href");
         // 查询参数序列化
         //var params= $("#mainForm").serialize();
		 var params= $("#mainForm").find("div.column").find(":input").serialize();
         params = params + "&csrftoken=" +$("#csrftoken").val();
         params = params + "&" +getRangeParams();
         url = url + "?" + params;
         // 显示结果一览
         $("#section").show();
         // 表格设置
         var tableSetting = {
                 // 表格ID
                 tableId : '#dataTable',
                 // 数据URL
                 url : url,
                 // 表格默认排序
                 aaSorting : [[ 2, "asc" ]],
                 // 表格列属性设置
                 aoColumns : [  { "sName": "no","sWidth": "1%","bSortable": false},
                                { "sName": "regionNameChinese","sWidth": "25%"},
                                { "sName": "counterCode","sWidth": "25%"},
                                { "sName": "counterNameIF","sWidth": "15%"},
								{ "sName": "counterStatus","bVisible": false,"sWidth": "15%"},
								{ "sName": "employeeName","sWidth": "15%"},
								{ "sName": "saleDate","sWidth": "15%"},
                                { "sName": "sumAmount","sWidth": "25%","sClass":"alignRight"},
								{ "sName": "sumQuantity","sWidth": "25%","sClass":"alignRight"}
                             ],          
                                
                // 不可设置显示或隐藏的列  
                aiExclude :[0, 1, 2],
                // 横向滚动条出现的临界宽度
                sScrollX : "100%",
                // 滚动体的宽度
                sScrollXInner:"",
                // 固定列数
                fixedColumns : 3
         };
         // 调用获取表格函数
         getTable(tableSetting);

    },
	
	/* 
     * 导出Excel
     */
	"exportExcel" : function(){
		//无数据不导出
        if($(".dataTables_empty").length==0){
	        if (!$('#mainForm').valid()) {
	            return false;
	        };
            var url = $("#downUrl").attr("href");
            var params= $("#mainForm").find("div.column").find(":input").serialize();
            params = params + "&csrftoken=" +$("#csrftoken").val();
            params = params + "&" +getRangeParams();
            url = url + "?" + params;
            window.open(url,"_self");
        }
    },
	
    /* 
     * 设置规则文本框是否可用
     */
	"setTextEnableDisable":function(obj,textName){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none')
		if($(obj).prop("checked")){
            $("#"+textName).prop("disabled",false);
			$("#"+textName).rules("add", {required: true});
			$("#"+textName).valid();
        }else{
            $("#"+textName).prop("disabled",true);
			$("#"+textName).rules("remove", "required");
			$("#"+textName).valid();
			//由于digits验证界面显示有问题，所有不需要验证如下两个元素时界面上屏蔽错误
			if(textName=="maxQuantity"){
				$("#divQuantity p:eq(0) span").removeClass("error");
				$("#divQuantity p:eq(0) span span").remove();
			}else if(textName=="minQuantity"){
				$("#divQuantity p:eq(1) span").removeClass("error");
				$("#divQuantity p:eq(1) span span").remove();
			}
        }
	}
};

var BINOLMOWAT02 = new BINOLMOWAT02();

$(function(){
	// 表单验证配置
    cherryValidate({
        formId: 'mainForm',
        rules: {
            startDate: {required:true,dateValid:true},    // 开始日期
            endDate: {required:true,dateValid:true},   // 结束日期
            maxLimit: {required:true,decimalValid: [14,2]},//最大阈值(可负的浮点数)
            minLimit: {required:true,decimalValid: [14,2]},//最小阈值(可负的浮点数)
            maxQuantity: {required:true,validIntNum:true},//最大数量(可负的整数)
            minQuantity: {required:true,validIntNum:true}//最小数量(可负的整数)
        }
    });

    //BINOLMOWAT02.search();
	
	$("#export").live('click',function(){
        BINOLMOWAT02.exportExcel();
    });
	
});