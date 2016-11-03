var BINBESSPRO04 = function () {};

BINBESSPRO04.prototype = {
	//部门ID数组
	"departArr":new Array(),
	
	//实体仓库ID数组
	"inventoryInfoArr":new Array(),
	
	//逻辑仓库ID数组
	"logicInventoryInfoArr":new Array(),
	
	//部门编号数组
	"departCodeNameArr":new Array(),
	
	//实体仓库编号数组
	"depotCodeNameArr":new Array(),
	
	//逻辑仓库编号数组
	"logicCodeNameArr":new Array(),
	
	//画面显示差异最大数
	"showMaxCount":500,
	
	/*
	 * 分批加载
	 */
	"getDiff":function(curCounterRow){
        var url = $("#getDiff").attr("href");
		var departID = BINBESSPRO04.departArr[curCounterRow];
		var inventoryInfoID = BINBESSPRO04.inventoryInfoArr[curCounterRow];
		var logicInventoryInfoID = BINBESSPRO04.logicInventoryInfoArr[curCounterRow];
        var param = "brandInfoId="+$("#brandInfoId").val()+"&organizationID="+departID;
        param += "&inventoryInfoID="+inventoryInfoID;
        param += "&logicInventoryInfoID="+logicInventoryInfoID;
        param += "&departCodeName="+BINBESSPRO04.departCodeNameArr[curCounterRow];
        param += "&depotCodeName="+BINBESSPRO04.depotCodeNameArr[curCounterRow];
        param += "&logicCodeName="+BINBESSPRO04.logicCodeNameArr[curCounterRow];
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
						if(row-1 >= BINBESSPRO04.showMaxCount){
							stopShowFlag = true;
							break;
						}else{
		                    appendhtml += '<tr>';
		                    appendhtml += '<td>'+row+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].DepartCodeName)+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].DepotCodeName)+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].LogicCodeName)+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].productName)+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].unitCode)+'</td>';
		                    appendhtml += '<td>'+escapeHTMLHandle(jsons[i].barCode)+'</td>';
		                    appendhtml += '<td class="alignRight">'+escapeHTMLHandle(jsons[i].actualStockQuantity)+'</td>';
		                    appendhtml += '<td class="alignRight">'+escapeHTMLHandle(jsons[i].endInOutQuantity)+'</td>';
		                    appendhtml += '</tr>';
		                    
		                    $("#aaData").append(appendhtml);
		                    //重新锁定画面
		                    requestStart();
						}
	                }
					var nextRow = curCounterRow+1;
                    if(nextRow < BINBESSPRO04.departArr.length){
                    	if(stopShowFlag){
     					   $("#dataTable_processing").addClass("hide");
    					   requestEnd();
    					   //显示超过最大提示
    					   $("#maxShow").html("<ul><li><span>由于产品库存差异过多，只显示前"+BINBESSPRO04.showMaxCount+"条差异。</span></li></ul>");
    					   $("#errorMessage").show();
                    	}else{
                            setTimeout(function(){BINBESSPRO04.getDiff(nextRow);},50);
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
		var length = $("tbody tr").length;
		if(length<=0){
			alert("请先查询！");
			return false;
		}
		if($("#stockInOutDate").val()==""){
			alert("请输入日期！");
			return false;
		}
		var url = $("#exec").attr("href");
		var param = "brandInfoId="+$("#brandInfoId").val();
		param += "&testType="+$("#testType").val();
		param += "&stockInOutDate="+$("#stockInOutDate").val();
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
	
	/*
	 * 查询差异库存
	 */
	"search":function(){
		$("#errorMessage").hide();
		$("#aaData").empty();
		BINBESSPRO04.departArr = new Array();
		BINBESSPRO04.inventoryInfoArr = new Array();
		BINBESSPRO04.logicInventoryInfoArr = new Array();
		BINBESSPRO04.departCodeNameArr = new Array();
		BINBESSPRO04.depotCodeNameArr = new Array();
		BINBESSPRO04.logicCodeNameArr = new Array();
		
		var url = $("#getDepart").attr("href");
		var param = "brandInfoId="+$("#brandInfoId").val();
		param += "&testType="+$("#testType").val();
		$("#dataTable_processing").removeClass("hide");
		requestStart();
	    $.ajax({
	    	url :url,
	    	type:'post',
	    	dataType:'json',
	    	data:param,
	    	success:function(msg){
				if(msg.length>0){
		            for(var i=0;i<msg.length;i++){
		            	BINBESSPRO04.departArr.push(msg[i].BIN_OrganizationID);
		            	BINBESSPRO04.inventoryInfoArr.push(msg[i].BIN_InventoryInfoID);
		            	BINBESSPRO04.logicInventoryInfoArr.push(msg[i].BIN_LogicInventoryInfoID);
		            	
		            	BINBESSPRO04.departCodeNameArr.push(msg[i].DepartCodeName);
		            	BINBESSPRO04.depotCodeNameArr.push(msg[i].DepotCodeName);
		            	BINBESSPRO04.logicCodeNameArr.push(msg[i].LogicCodeName);
					}
					//取第一个部门仓库的差异库存
					BINBESSPRO04.getDiff(0);
				}else{
					$("#dataTable_processing").addClass("hide");
					requestEnd();
				}
	    	}
	    });
	},
	
	"exportExcel":function(){
        var url = $("#downUrl").attr("href");
        var params = "brandInfoId="+$("#brandInfoId").val();
        params += "&testType="+$("#testType").val();
        //params = params + "&csrftoken=" +$("#csrftoken").val();
        url = url + "?" + params;
        window.open(url,"_self");
	}
};

var BINBESSPRO04 = new BINBESSPRO04();

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