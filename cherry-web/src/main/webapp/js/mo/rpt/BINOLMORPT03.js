
function BINOLMORPT03() {};

BINOLMORPT03.prototype = {
	
	// 考核报表查询
	"search" : function(){
		//清空报错信息
		$('#errorDiv2 #errorSpan2').html("");
		$('#errorDiv2').hide();
		var $form = $("#mainForm");
		// 表单验证
		if(!$form.valid()){
			return;
		}	 
		var url = $("#search_url").val();
		var paperCountUrl = $("#paperCountUrl").attr("href");
		$form.find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		// 查询参数序列化
		var params= binolmorpt03.getSearchParams();
		url = url + "?" + params;
		// 显示结果一览
		$("#section").show();
		//查询结果所属问卷总数
		var callback = function(msg){
			$("#paperCount").val(msg);
		};
		cherryAjaxRequest({
			url : paperCountUrl,
			param : params,
			callback : callback
		});
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [{ "sName": "number","sWidth": "5%","bSortable": false},
					{ "sName": "paperName","sWidth": "20%"},
					{ "sName": "paperType","sWidth": "10%"},
					{ "sName": "departName","sWidth": "20%"},
					{ "sName": "employeeName","sWidth": "20%"},
					{ "sName": "answerDate","sWidth": "15%"},
					{ "sName": "realTotalPoint","sWidth": "10%","bSortable": false},	
					{ "sName": "button","sWidth": "10%","bSortable": false,"sClass":"center"}],	
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"popCheckPaper" : function(object){
		var confirmClick = function(){
			var $checked = $('#checkPaper_dataTable').find(":input[checked]");
			if($checked.length > 0) {
				$("#paperId").val($checked.val());
				$("#checkPaperType").val($checked.parent().find("#paperType").val());
				$("#paperName").val($checked.parent().next().text());
			} else {
				$("#paperId").val("");
				$("#checkPaperType").val("");
				$("#paperName").val("");
			}
			var paperType = $("#paperType").val();
			var checkPaperType = $("#checkPaperType").val();
			// 非会员问卷时才显示过滤功能
			if(paperType == '' || paperType == checkPaperType){
				// 只有普通问卷与商场问卷为【非会员问卷】
				if(checkPaperType == '0' || checkPaperType == '2'){
					$("#filterSpan").show();
					$("#showMode0").prop("checked",true);
				}else {
					$("#showMode0").prop("checked",true);
					$("#filterSpan").hide();
				}
			} else if(paperType != checkPaperType) {
				// 问卷名称对应的问卷类型与选择的问卷类型不一致统一都不显示过滤
				$("#showMode0").prop("checked",true);
				$("#filterSpan").hide();
			}
		};
		popDataTableOfPaper(object,null,confirmClick);
	},
	/* 
     * 导出Excel
     */
	"exportExcel" : function(){
		//无数据不导出,不是同一张问卷不导出
		if(parseInt($("#paperCount").val()) > 1){
			//显示错误
			$('#errorDiv2 #errorSpan2').html($('#errmsgESS00063').val());
			$('#errorDiv2').show();
			return false;
		}
		if($(".dataTables_empty:visible").length==0){
		    if (!$('#mainForm').valid()) {
                return false;
            };
            var url = $("#downUrl").attr("href");
            var params= binolmorpt03.getSearchParams();
            url = url + "?" + params;
            window.open(url,"_self");
        }
    },
    
    /**
     * 更改显示模式重新查询
     */
    "changeShowMode" : function(flag) {
    	var that = this;
    	$("#showMode"+flag).prop("checked",true);
    	that.search();
    },
    
    /**
     * 查询参数序列化
     * @returns
     */
    "getSearchParams" : function() {
    	// 查询参数序列化
    	var params= $("#mainForm").find("div.column").find(":input").serialize();
    	var showMode = $('input:radio[name="showMode"]:checked').val();
    	params += "&showMode=" + showMode + "&csrftoken=" +$("#csrftoken").val();
    	params = params + "&" + getRangeParams();
    	return params;
    }
};

var binolmorpt03 =  new BINOLMORPT03();

$(document).ready(function() {
	//日期验证
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			checkDateStart: {dateValid: true},
			checkDateEnd: {dateValid: true}
		}
	});

	$("#paperType").live('change',function(){
		var paperType = $("#paperType").val();
		// 选择的问卷所属的问卷类型
		var checkPaperType = $("#checkPaperType").val();
		// 非会员问卷时才显示过滤功能
		if(paperType != ''){//选择了指定问卷类型
			// 问卷名称未选择或者问卷名称对应的类型与选择的问卷类型是一致的情况
			if(checkPaperType == '' || paperType == checkPaperType){
				if(paperType == '0' || paperType == '2'){
					$("#filterSpan").show();
					$("#showMode0").prop("checked",true);
				}else if(paperType != ''){
					$("#showMode0").prop("checked",true);
					$("#filterSpan").hide();
				}
			} else if(paperType != checkPaperType){
				// 问卷名称对应的类型与选择的问卷类型不一致时统一不显示过滤功能
				$("#showMode0").prop("checked",true);
				$("#filterSpan").hide();
			}
		} else {
			// 未选择问卷类型时直接按问卷名称对应的问卷类型来判断是否显示
			if(checkPaperType == '0' || checkPaperType == '2'){
				$("#filterSpan").show();
				$("#showMode0").prop("checked",true);
			}else {
				$("#showMode0").prop("checked",true);
				$("#filterSpan").hide();
			}
		}
	});
	// 考核报表查询
	binolmorpt03.search();

	$("#export").live('click',function(){
		binolmorpt03.exportExcel();
	});
});

