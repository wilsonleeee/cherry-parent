var BINBESSPRO03 = function () {};

BINBESSPRO03.prototype = {
	/*
	 * 柜台ID数组
	 */
	"counterArr":new Array(),
	
	/*
	 * 实体仓库ID数组
	 */
	"inventoryInfoArr":new Array(),
	
	/*
	 * 逻辑仓库ID数组
	 */
	"logicInventoryInfoArr":new Array(),
	
	/*
	 * 画面显示差异最大数
	 */
	"showMaxCount":500,
	
	/*
	 * 分批加载
	 */
	"getDiff":function(curCounterRow){
        var url = $("#getDiff").attr("href");
		var counterCode = BINBESSPRO03.counterArr[curCounterRow];
		var logicInventoryCode = BINBESSPRO03.logicInventoryInfoArr[curCounterRow];
        var param = "brandInfoId="+$("#brandInfoId").val()+"&counterCode="+counterCode;
        param += "&logicInventoryCode="+logicInventoryCode;
        param += "&filterStockNotExist="+$("#filterStockNotExist").prop("checked");
        param += "&filterPrtNotExist="+$("#filterPrtNotExist").prop("checked");
	    $.ajax({
	        url :url,
	        type:'post',
	        dataType:'html',
	        data:param,
	        success:function(msg){
	            //错误提示
	            if(msg.indexOf('id="actionResultBody"')>-1){
	                $('#resultDiv').html(msg);
	            }else{
	                var jsons = eval(msg);
	                var stopShowFlag = false;
	                for(var i=0;i<jsons.length;i++){
	                    var appendhtml = "";
						var row = $("#aaData tr").length+1;
						if(row-1 >= BINBESSPRO03.showMaxCount){
							stopShowFlag = true;
							break;
						}else{
		                    appendhtml += '<tr>';
		                    appendhtml += '<td>'+row+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].counterName)+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].productName)+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].unitCode)+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].barCode)+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].inventoryName)+'</td>';
		                    if(jsons[i].logicInventoryName.length == 0){
		                        appendhtml += '<td>&nbsp;</td>';
		                    }else{
		                        appendhtml += '<td>'+escapeHTMLHandle(jsons[i].logicInventoryName)+'</td>';
		                    }
		                    appendhtml += '<td class="alignRight">'+escapeHTMLHandle(jsons[i].newQuantity)+'</td>';
		                    appendhtml += '<td class="alignRight">'+escapeHTMLHandle(jsons[i].oldQuantity)+'</td>';
		                    appendhtml += '</tr>';
		                    
		                    $("#aaData").append(appendhtml);
		                    //重新锁定画面
		                    requestStart();
						}
	                }
					var nextRow = curCounterRow+1;
                    if(nextRow < BINBESSPRO03.counterArr.length){
                    	if(stopShowFlag){
     					   $("#dataTable_processing").addClass("hide");
    					   requestEnd();
    					   //显示超过最大提示
    					   $("#maxShow").html("<ul><li><span>由于产品库存差异过多，只显示前"+BINBESSPRO03.showMaxCount+"条差异。</span></li></ul>");
    					   $("#errorMessage").show();
                    	}else{
                            setTimeout(function(){BINBESSPRO03.getDiff(nextRow);},50);
                    	}
					}else{
					   $("#dataTable_processing").addClass("hide");
					   requestEnd();
					}
	            }
	        }
		});
	},
	
	/*
	 * 执行同步
	 */
	"save":function(){
		// 表单验证
		if($("#stockInOutDate").val()==""){
			alert("必须输入日期！");
			return false;
		}else{
			if(!this.checkDate($("#stockInOutDate").val())){
				alert("请输入正确的日期！");
				return false;
			}
		}
		var length = $("tbody tr").length;
		if(length<=0){
			alert("新老后台库存一致，不需要同步。");
			return false;
		}
		var url = $("#exec").attr("href");
		var param = "brandInfoId="+$("#brandInfoId").val();
		param += "&stockInOutDate="+$("#stockInOutDate").val();
		param += "&counterCode="+$("#counterCode").val();
        param += "&filterStockNotExist="+$("#filterStockNotExist").prop("checked");
        param += "&filterPrtNotExist="+$("#filterPrtNotExist").prop("checked");
		requestStart();
        $.ajax({
            url :url,
            type:'post',
            dataType:'html',
            data:param,
            success:function(msg){
                $('#resultDiv').html(msg);
                requestEnd();
            }
        });
	},
	
	"checkDate":function(value){
		var checkDate = false;
		var r = value.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
		if(r != null) {
			var d = new Date(r[1],r[3]-1,r[4]);
			if(d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]) {
				checkDate = true;
			}
		} else {
			var pattern = /^\d{8}$/;
			if(pattern.exec(value)) {
				var d = new Date(value.substring(0,4) + '/' + value.substring(4,6) + '/' + value.substring(6,8));
				if(d.getFullYear()==value.substring(0,4)&&(d.getMonth()+1)==value.substring(4,6)&&d.getDate()==value.substring(6,8)) {
					checkDate = true;
				}
			}
		}
		return checkDate;
	},
	
	"search":function(){
		//清空数据
		$("#aaData").empty();
		BINBESSPRO03.counterArr = new Array();
		BINBESSPRO03.logicInventoryInfoArr = new Array();
	    //显示差异数据
		$("#dataTable_processing").removeClass("hide");
	    var url = $("#getCounter").attr("href");
	    var param = "brandInfoId="+$("#brandInfoId").val()+"&counterCode="+$("#counterCode").val();
        param += "&filterStockNotExist="+$("#filterStockNotExist").prop("checked");
        param += "&filterPrtNotExist="+$("#filterPrtNotExist").prop("checked");
	    requestStart();
	    $.ajax({
	        url :url,
	        type:'post',
	        dataType:'json',
	        data:param,
	        success:function(msg){
				if(msg.length>0){
		            for(var i=0;i<msg.length;i++){
		            	BINBESSPRO03.counterArr.push(msg[i].counterCode);
		            	BINBESSPRO03.logicInventoryInfoArr.push(msg[i].logicInventoryCode);
					}
					//取第一个柜台的差异库存
					BINBESSPRO03.getDiff(0);
				}else{
					$("#dataTable_processing").addClass("hide");
					requestEnd();
				}
	        }
	    });
	}
};

var BINBESSPRO03 = new BINBESSPRO03();

window.onbeforeunload = function(){
    if (window.opener) {
        window.opener.unlockParentWindow();
    }
};

$(function(){
    if (window.opener) {
        window.opener.lockParentWindow();
    }
});