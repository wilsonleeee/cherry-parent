var BINOLSTSFH23 = function () {};

BINOLSTSFH23.prototype = {

		"search" : function(){
			var url = $("#searchUrlID").val();
			// 查询参数序列化
			var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			// 表格设置
			var tableSetting = {
				// 表格ID
				tableId : '#mainTable',
				// 数据URL
				url : url,
				aaSorting : [[ 1, "asc" ]],
				// 表格列属性设置
				aoColumns : [
				        { "sName": "checkbox","sWidth": "2%"},
				        { "sName": "orderNo", "sWidth": "2%", "sClass":"center" },
				        { "sName": "departName","sWidth": "5%"},
				        { "sName": "orderDate","sWidth": "12%"},
				        { "sName": "applyQuantity"},
				        { "sName": "totalAmount"},
				        { "sName": "expectDeliverDate"},
						{ "sName": "totalQuantity"},
						{ "sName": "deliverDate","sClass":"alignRight","sWidth": "5%"},
						{ "sName": "VerifiedFlag","sClass":"alignRight","sWidth": "5%"}],
				// 不可设置显示或隐藏的列
				aiExclude :[0, 1],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index : 1,
				// html转json前回调函数
				callbackFun: function(msg){
			 		var $msg = $("<div></div>").html(msg);
			 		var $headInfo = $("#headInfo",$msg);
			 		if($headInfo.length > 0){
			 			$("#headInfo").html($headInfo.html());
			 		}else{
			 			$("#headInfo").empty();
			 		}
		 		}
			};
		
			// 调用获取表格函数
			getTable(tableSetting);
				
		},
		// 查询订单主表信息
		"initSearch" : function(){
			var url = $("#initSearchUrlID").val();
			// 查询参数序列化
			var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			// 表格设置
			var tableSetting = {
				// 表格ID
				tableId : '#dataTable',
				// 数据URL
				url : url,
				aaSorting : [[ 3, "asc" ]],
				// 表格列属性设置
				aoColumns : [
				        { "sName": "checkbox","sWidth": "2%","bSortable": false},
				        { "sName": "orderNo", "sClass":"center" },
				        { "sName": "departName"},
				        { "sName": "orderDate"},
				        { "sName": "applyQuantity","sClass":"alignRight"},
				        { "sName": "totalAmount","sClass":"alignRight"},
				        { "sName": "expectDeliverDate"},
						{ "sName": "totalQuantity","sClass":"alignRight"},
						{ "sName": "deliverDate","sClass":"alignRight"},
						{ "sName": "VerifiedFlag","sClass":"center"}],
				// 不可设置显示或隐藏的列
				aiExclude :[0, 1],
				// 横向滚动条出现的临界宽度
				sScrollX : "99%",
				index : 1,
				// html转json前回调函数
				callbackFun: function(msg){
			 		var $msg = $("<div></div>").html(msg);
			 		var $headInfo = $("#headInfo",$msg);
			 		if($headInfo.length > 0){
			 			$("#headInfo").html($headInfo.html());
			 		}else{
			 			$("#headInfo").empty();
			 		}
		 		}
			};
		
			// 调用获取表格函数
			getTable(tableSetting);
		},
		/* 
	     * 导出Excel
	     */
		"exportExcel" : function(){
			//无数据不导出
	        if($(".dataTables_empty:visible").length==0){
			    if (!$('#mainForm').valid()) {
	                return false;
	            };
	            var url = $("#exportUrlID").val();
	            var params= $("#mainForm").serialize();
	            params = params + "&csrftoken=" +$("#csrftoken").val();
	            url = url + "?" + params;
	            window.open(url,"_self");
	        }
	    },
		/* 
	     * 订单复制
	     */
		"copyOrder" : function(){
			var url=$("#copyOrderUrlID").val();
			var params="";
			var obj = document.getElementsByName("valid");
			var orderNoList = document.getElementsByName("orderNo");
			var VerifiedFlagList = document.getElementsByName("VerifiedFlag");
			var checkOrderNoId = [];
			// 选中条数
			var a=0;
			for(var k in obj){ 
				if(obj[k].checked){
					    a=a+1;
					    checkOrderNoId.push(orderNoList[k].value);
				}
			}
			if(a==0){
				$("#errorMessage").html($("#EBS00145").html());//选中订单后才能操作相关订单！
				return false;
			}
			if(a>1){
				$("#errorMessage").html($("#EBS00147").html());//只能选择一个订单进行订单复制操作
				return false;
			}
			params="?checkOrderNoIdCopy="+checkOrderNoId;
			url=url+params;
			popup(url);
			
	    },
		/* 
	     * 订单删除
	     */
		"deleteOrder" : function(){
			var url=$("#deleteOrderUrlID").val();
			var params="";
			var obj = document.getElementsByName("valid");
			var orderNoList = document.getElementsByName("orderNo");
			var VerifiedFlagList = document.getElementsByName("VerifiedFlag");
			var checkOrderNoId = [];
			// 选中条数
			var a=0;
			for(var k in obj){ 
				if(obj[k].checked){
					    a=a+1;
					    checkOrderNoId.push(orderNoList[k].value);
						if(VerifiedFlagList[k].value != 0){
							$("#errorMessage").html($("#EBS00144").html());//只能删除新建状态的订单
							return false;
						}
				}
			}
			if(a==0){
				$("#errorMessage").html($("#EBS00145").html());//选中订单后才能操作相关订单！
				return false;
			}
			params="checkOrderNoId="+checkOrderNoId;
			var callback = function(msg){
				// 页面刷新
				BINOLSTSFH23.search();
				// 计算发货数量，订货数量，订货金额
				BINOLSTSFH23.calcuTatol();
				
			};
			cherryAjaxRequest(
					{
						url:url,
						param:params,
						callback:callback
					}
			);
	    },
		/* 
	     * 款已付
	     */
		"payMoney" : function(){
			var url=$("#payMoneyUrlID").val();
			var params="";
			var obj = document.getElementsByName("valid");
			var orderNoList = document.getElementsByName("orderNo");
			var VerifiedFlagList = document.getElementsByName("VerifiedFlag");//订单审核状态
			var checkOrderNoId = []; // 订单单号
			var b=0;			// 选中条数
			for(var k in obj){ 
				if(obj[k].checked){
					    b=b+1;
					    checkOrderNoId.push(orderNoList[k].value);
						if( VerifiedFlagList[k].value == 0){
							$("#errorMessage").html($("#EBS00146").html());//新建订单不能能使用款已付功能
							return false;
						}
						if( VerifiedFlagList[k].value == 'P'){
							$("#errorMessage").html($("#EBS00148").html());//款已付状态的订单不能再经行款已付操作
							return false;
						}
				}
			}
			if(b==0){
				$("#errorMessage").html($("#EBS00145").html());//选中订单后才能操作相关订单！
				return false;
			}
			params="checkOrderNoId="+checkOrderNoId;
			var callback = function(msg){
				// 页面刷新
				BINOLSTSFH23.search();
				// 操作成功
				
			};
			cherryAjaxRequest(
					{
						url:url,
						param:params,
						callback:callback
					}
			);
	    },
	    "calcuTatol":function(){
			var rows = $("#databody").children();
			var totalQuantity = 0;
			var totalQuantity1 = 0;
			var totalAmount = 0.00;
			if(rows.length > 0){
				rows.each(function(i){
					var $tds = $(this).find("td");
					totalQuantity +=Number($tds.eq(4).text());
					totalAmount +=Number($tds.eq(5).text());
					totalQuantity1 +=Number($tds.eq(7).text());
				});
			}
			$("#totalQuantity").html(totalQuantity);
			$("#totalQuantity1").html(totalQuantity1);
			$("#totalAmount").html(totalAmount.toFixed(2));
		}
	    
	    
};

var BINOLSTSFH23 = new BINOLSTSFH23();

$(document).ready(function(){

	// 日期控件
	$("#fromDate").cherryDate({
		beforeShow: function(input){
			var value = $(input).parents("p").find("input[name='toDate']").val();
			return [value, "maxDate"];
		}
	});

	$("#toDate").cherryDate({
		beforeShow: function(input){
			var value = $(input).parents("p").find("input[name='fromDate']").val();
			return [value, "minDate"];
		}
	});
	// 日期校验
	cherryValidate({
		formId: "mainForm",		
		rules: {
			fromDate: {dateValid: true},
			toDate: {dateValid: true}
	   }		
	});
	
	// 初始化查询
	BINOLSTSFH23.initSearch();
//	sugggestDayPopBody = $('#sugggestDayPop').html();
//	BINOLSTSFH23.addNewLine();
});