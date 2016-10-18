var BINOLMOWAT04 = function () {};

BINOLMOWAT04.prototype = {
    /* 
     * 验证勾选
     */
	"checkData":function(){
		if($("#cbAmount").prop("checked")==false && $("#cbGainQuantityGE").prop("checked")==false){
            $('#errorDiv2 #errorSpan2').html($('#errmsg_EMO00023').val());
            $('#errorDiv2').show();
            return false;
		}
	},
	
    /* 
     * 初始化查询
     */
    "search":function(){
		this.checkData();
        if (!$('#mainForm').valid()) {
            return false;
        };
		
        $("#mainForm").find(":input").each(function() {
            $(this).val($.trim(this.value));
        });
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
         var tableSetting1 = {
                 // datatable 对象索引
                 index : 0,
                 // 表格ID
                 tableId : '#dataTable1',
                 // 数据URL
                 url : url,
                 // 表格默认排序
                 aaSorting : [[ 1, "asc" ]],
                 // 表格列属性设置
                 aoColumns : [  { "sName": "no","sWidth": "1%","bSortable": false},
                                { "sName": "departCode","sWidth": "33%"},
                                { "sName": "departName","sWidth": "33%"},
								{ "sName": "count","sWidth": "33%","sClass":"alignRight"}
                             ],
                                
                // 不可设置显示或隐藏的列  
                aiExclude :[0, 1, 4, 5, 6, 7, 8],
                // 横向滚动条出现的临界宽度
                sScrollX : "100%",
                // 滚动体的宽度
                sScrollXInner:"",
                // 固定列数
                fixedColumns : 2
         };
         var tableSetting2 = {
                 // datatable 对象索引
                 index : 1,
                 // 表格ID
                 tableId : '#dataTable2',
                 // 数据URL
                 url : url,
                 // 表格默认排序
                 aaSorting : [[ 1, "asc" ]],
                 // 表格列属性设置
                 aoColumns : [  { "sName": "no","sWidth": "1%","bSortable": false},
                                { "sName": "stockTakingNo","sWidth": "20%"},
                                { "sName": "departCode","sWidth": "20%"},
                                { "sName": "departName","sWidth": "20%"},
								{ "sName": "summQuantity","sWidth": "20%","sClass":"alignRight"},
								{ "sName": "date","sWidth": "20%"},
								{ "sName": "employeeName","sWidth": "20%"}
                             ],
                                
                // 不可设置显示或隐藏的列  
                aiExclude :[0, 1],
                // 横向滚动条出现的临界宽度
                sScrollX : "100%",
                // 滚动体的宽度
                sScrollXInner:"",
                // 固定列数
                fixedColumns : 2
         };
		 
		 if($("#cbAmount").prop("checked") == true){
		 	$("#dataTable1_wrapper").removeClass("hide");
			$("#dataTable2_wrapper").addClass("hide");
		 	$("#dataTable1").removeClass("hide");
			$("#dataTable2").addClass("hide");
			
			$("#setting2").addClass("setting2");
            $("#setting2").removeClass("setting");
            $("#setting2").addClass("hide");
			$("#setting1").removeClass("setting1");
			$("#setting1").addClass("setting");
            $("#setting1").removeClass("hide");

            // 调用获取表格函数
            getTable(tableSetting1);
		 }else if($("#cbGainQuantityGE").prop("checked")==true){
		 	$("#dataTable2_wrapper").removeClass("hide");
		 	$("#dataTable1_wrapper").addClass("hide");
            $("#dataTable2").removeClass("hide");
            $("#dataTable1").addClass("hide");

            $("#setting1").addClass("setting1");
            $("#setting1").removeClass("setting");
            $("#setting1").addClass("hide");
            $("#setting2").removeClass("setting2");
            $("#setting2").addClass("setting");
            $("#setting2").removeClass("hide");

            // 调用获取表格函数
            getTable(tableSetting2);
		 }
    },
	
	/* 
     * 导出Excel
     */
	"exportExcel" : function(){
		//无数据不导出
        if($(".dataTables_empty:visible").length==0){
			this.checkData();
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
    }
	
};

var BINOLMOWAT04 = new BINOLMOWAT04();

$(function(){
	//正整数及负整数验证
//    jQuery.validator.addMethod("intValid", function(value, element) {
//		return this.optional(element) || /^(0|(-?[1-9]\d*))$/.test(value);
//    }, jQuery.validator.messages.digits);

	// 表单验证配置
    cherryValidate({
        formId: 'mainForm',
        rules: {
            startDate: {required:true,dateValid:true},    // 开始日期
            endDate: {required:true,dateValid:true},   // 结束日期
            maxLimit: {required:true,digits:true},//最大盘点次数阈值
            gainQuantityGE: {required:true,digits:true}//盘差>=
            //gainQuantityLE: {required:true,digits:true}//盘差<=
        }
    });

    //BINOLMOWAT04.search();
	
	$("#export").live('click',function(){
		BINOLMOWAT04.exportExcel();
	});
	
    $("#cbAmount").live('click',function(){
		if($("#cbAmount").prop("checked")==true){
			$('#errorDiv2').hide();
	        $("[name='cbGainQuantity']").prop("checked",false);
	        $("#gainQuantityGE").prop("disabled",true);
	        //$("#gainQuantityLE").prop("disabled",true);
	        $("#maxLimit").prop("disabled",false);
			
		    $("#gainQuantityGE").parent().find("#errorText").remove();
            $("#gainQuantityGE").parent().removeClass("error");
			//$("#gainQuantityLE").parent().find("#errorText").remove();
            //$("#gainQuantityLE").parent().removeClass("error");
		}else{
			$("#maxLimit").prop("disabled",true);
			
            $("#maxLimit").parent().find("#errorText").remove();
            $("#maxLimit").parent().removeClass("error");
		}
    });
	
	$("[name='cbGainQuantity']").live('click',function(){
        $("#cbAmount").prop("checked",false);
		$("#maxLimit").prop("disabled",true);
		if($("#cbGainQuantityGE").prop("checked")==true){
			$('#errorDiv2').hide();
			$("#gainQuantityGE").prop("disabled",false);
		}else{
			$("#gainQuantityGE").prop("disabled",true);
			
			$("#gainQuantityGE").parent().find("#errorText").remove();
            $("#gainQuantityGE").parent().removeClass("error");
		}
        //if($("#cbGainQuantityLE").prop("checked")==true){
		//	$('#errorDiv2').hide();
		//	$("#gainQuantityLE").prop("disabled",false);
		//}else{
		//	$("#gainQuantityLE").prop("disabled",true);
			
        //    $("#gainQuantityLE").parent().find("#errorText").remove();
        //    $("#gainQuantityLE").parent().removeClass("error");
		//}
		
		$("#maxLimit").parent().find("#errorText").remove();
        $("#maxLimit").parent().removeClass("error");
    });
	
});