function BINOLWRSRP02_global(){
	
};

BINOLWRSRP02_global.prototype = {
		"printALL":function(){
			if (!$("#queryParam").valid()) {
				return false;
			}
			var getPrintALLUrl=$("#getPrintALLUrl").attr("href");
			var params=$("#queryParam").serialize();
			cherryAjaxRequest({
				url:getPrintALLUrl,
				param:params,
				callback:function(data){
					if(data == null || data == undefined || data == "" || data == "error"){
						return false;
					}
					var param_map = eval("("+data+")");
					var saleRecord_list=param_map.saleRecord_list;
					var saleRecord_info=param_map.saleRecord_info;
					var sumAmount=saleRecord_info.sumAmount;
					var departName=saleRecord_info.departName;
					var employeeName=saleRecord_info.employeeName;
					var startDate=saleRecord_info.startDate;
					var endDate=saleRecord_info.endDate;
					$("#detailPrint tr:gt(0)").remove();
					if(saleRecord_list.length == 0){
						return false;
					}
					for (var one in saleRecord_list){
						var saleTime=saleRecord_list[one].saleTime;
						var amount=BINOLWRSRP02.toDecimal2(saleRecord_list[one].amount);
						var html='<tr>'+
							'<td width="60%" height="10" class="center" style="border:solid 0px black"><font size="2px">'+saleTime+'</font></td>'+
							'<td width="40%" height="10" class="center" style="border:solid 0px black"><font size="2px">'+amount+'</font></td>'+
							'</tr>';
						$("#detailPrint").append(html);
					}
					$("#saleAmountPrint").html(sumAmount);
					$("#counterNamePrint").html(departName);
					$("#employeeNamePrint").html(employeeName);
					$("#startTimePrint").html(startDate);
					$("#endTimePrint").html(endDate);
					
					/**
					 * SET_PRINT_PAGESIZE(intOrient,intPageWidth,intPageHeight,strPageName);
					 *	参数说明：
					 *	intOrient：打印方向及纸张类型
					 *	    1---纵向打印，固定纸张； 
					    2---横向打印，固定纸张；  
					    3---纵向打印，宽度固定，高度按打印内容的高度自适应(见样例18)；
					    0---方向不定，由操作者自行选择或按打印机缺省设置。
					intPageWidth：
					    纸张宽，单位为0.1mm 譬如该参数值为45，则表示4.5mm,计量精度是0.1mm。
					intPageHeight：
					    固定纸张时该参数是纸张高；高度自适应时该参数是纸张底边的空白高，计量单位与纸张宽一样。
					strPageName：
					    纸张类型名， intPageWidth等于零时本参数才有效，具体名称参见操作系统打印服务属性中的格式定义。
					    关键字“CreateCustomPage”会在系统内建立一个名称为“LodopCustomPage”自定义纸张类型。
					 * 
					 * 
					 * 
					 * */
					LODOP=getLodop();  
					LODOP.PRINT_INIT("销售汇总");
					// 3:纵向；800：纸张宽度；10：纸张底边的空白高（0.1mm）；
					LODOP.SET_PRINT_PAGESIZE(3,800,10,"");
					// 根据打印机自适应,控制位置基点
//					LODOP.SET_PRINT_MODE("POS_BASEON_PAPER",true);
					// 字段大小（文本）
					LODOP.SET_PRINT_STYLE("FontSize",8);
					// 线的粗细（文本）
					LODOP.SET_PRINT_STYLE("Bold",1);
					// 底色
					//		LODOP.SET_SHOW_MODE("SKIN_CUSTOM_COLOR",'#FFFFFF');	
								// 直接文本
					//		LODOP.ADD_PRINT_TEXT(50,10,260,39,"销售");
					// 页面打印
					/**
					 * ADD_PRINT_HTM(intTop,intLeft,intWidth,intHeight,strHtml)增加超文本项
					 * intTop:距离顶部距离
					 * intLeft：左边距
					 * intWidth：宽度，可用百分比
					 * intHeight：高度，可用百分比
					 * strHtml：页面
					 */
							LODOP.ADD_PRINT_HTM(1,0,"100%","100%",$("#printAllHTML").html());
								// 打印
							LODOP.PRINT();
								// 设计
					//		LODOP.PRINT_DESIGN();
					// 预览
//					LODOP.PREVIEW();
				}
			});
			
		},
		"toDecimal2":function(x){
			var f = parseFloat(x);  
	        if (isNaN(f)) {  
	            return false;  
	        }  
	        var f = Math.round(x*100)/100;  
	        var s = f.toString();  
	        var rs = s.indexOf('.');  
	        if (rs < 0) {  
	            rs = s.length;  
	            s += '.';  
	        }  
	        while (s.length <= rs + 1) {  
	            s += '00';  
	        }  
	        return s;
		}
}

var BINOLWRSRP02 = new BINOLWRSRP02_global();

$(document).ready(function() {
	// 表单验证配置
    cherryValidate({
        formId: 'queryParam',
        rules: {
        		startDate: {required: true,dateValid:true},	// 开始日期
				endDate: {required: true,dateValid:true}	// 结束日期
        }
    });
    
	
})