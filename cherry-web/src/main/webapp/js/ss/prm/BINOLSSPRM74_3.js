function BINOLSSPRM74_3() {};

BINOLSSPRM74_3.prototype = {
		"showhidediv":function(id){
			var display =$('#'+id).css('display');
			if(display == 'none'){
				$('#'+id).show();
			}else{
				$('#'+id).hide();
			}
			if(id == "dzq2"){
				var couponCode=$("#dzq2 #couponCode2").html();
				$("#rule_table tr").each(function(){
					var couponCode_old=$(this).find("input[name='couponCode']").val();
					if(couponCode == couponCode_old){
						if(display == 'none'){
							$(this).find("input:checkbox").attr("checked","checked");
						}else{
							$(this).find("input:checkbox").attr("checked",false);
						}
					}
				});
			}
		},
		"showResultMessage":function(){
			var resultCode=$("#resultCode").val();
			var resultMessage=$("#resultMessage").val();
			if(resultCode != 0){
				$("#showResultMessage").html(resultMessage);
				$("#err").show();
			}
		},
		"showDescriptionDtl":function(obj){
			var descriptionDtl=$(obj).find("td:last [name='descriptionDtl']").val();
			$(".explainbox p").html(descriptionDtl);
			var couponCode=$(obj).find("td:last input[name='couponCode']").val();
			$("#activityProTable "+"#"+couponCode).siblings("tbody").hide();
			$("#activityProTable "+"#"+couponCode).show();
		},
		"showErrorMessage":function(message){
			$("#showResultMessage").html(message);
			$("#err").show();
			$("#showResultMessage").parent().find(".btn_qr").focus();
		},
		"checkCoupon":function(obj){
			var checkFlag=$(obj).is(":checked");
			var couponCode=$(obj).parents("tr").find("td:hidden input[name='couponCode']").val();
			var password=$(obj).parents("tr").find("td:hidden input[name='password']").val();
			var fullFlag=$(obj).parents("tr").find("td:hidden input[name='fullFlag']").val();
			$("#dzq2Commit").unbind();
			if(checkFlag){
				//打开输入密码提示框
				if(password == null || password == 'null' || password == undefined || password ==''){
					$("#dzq2Commit").bind("click",function(){
						BINOLSSPRM74_3.commitCoupon2(obj);
					});
					$("#dzq2 #couponCode2").html(couponCode);
					$("#dzq2 #couponPassword2").val("");
					$("#dzq2").show();
				}else{
					//如果是并且的情况
					if(fullFlag == 1){
						$("#activityProTable "+"#"+couponCode).find("tr").each(function(){
							$(this).find("input[type='checkbox']").attr("checked","checked");
						});
					}
				}
			}else{
				$("#activityProTable "+"#"+couponCode).find("tr").each(function(){
					$(this).find("input[type='checkbox']").attr("checked",false);
				});
			}
			
			
		},
		"checkPro":function(obj){
			var couponCode=$(obj).parents("tbody").attr("id");
			var sumlength=0;
			//对应的产品选择了多少
			$("#activityProTable "+"#"+couponCode).find("tr").each(function(){
				var checkFlag=$(this).find("input[type='checkbox']").is(":checked");
				if(checkFlag == true){
					sumlength += 1;
				}
			});
			//该代物券允许的最大选择数
			var maxcount=1;
			var password;
			var $check;
			var $fullFlag;
			$("#rule_table tbody tr").each(function(){
				var $couponCode=$(this).find("input[name='couponCode']").val();
				var $maxCount=$(this).find("input[name='maxCount']").val();
				if($couponCode == couponCode){
					$fullFlag=$(this).find("input[name='fullFlag']").val();
					password=$(this).find("input[name='password']").val();
					if(password == null || password == 'null' || password == undefined || password ==''){
						$(this).find("input[type='checkbox']").attr("checked","checked");
					}
					$check=$(this).find("input[type='checkbox']");
					maxcount = $maxCount;
				}
			});
			if($fullFlag == 1 && !$(obj).is(":checked")){
				$(obj).attr("checked","checked");
				BINOLSSPRM74_3.showErrorMessage("您选择的产品为套装组合，不能改变组合形式");
				return false;
			}
			//产品一起被选中
			if($fullFlag == 1 && $(obj).is(":checked")){
				$("#activityProTable "+"#"+couponCode).find("tr").each(function(){
					$(this).find("input[type='checkbox']").attr("checked","checked");
				});
			}
			
			if(password == null || password == 'null' || password == undefined || password ==''){
				$(obj).attr("checked",false);
				BINOLSSPRM74_3.checkCoupon($check);
				return false;
			}
			if(maxcount == null || maxcount == 'null' || maxcount == undefined || maxcount ==''){
				maxcount=1;
			}
			if(sumlength == 0){
				$("#rule_table tbody tr").each(function(){
					var $couponCode=$(this).find("td:hidden input[name='couponCode']").val();
					if($couponCode == couponCode){
						$(this).find("input[type='checkbox']").attr("checked",false);
					}
				});
			}else if(sumlength <= maxcount){
				$("#rule_table tbody tr").each(function(){
					var rule_couponCode=$(this).find("td:hidden input[name='couponCode']").val();
					if(rule_couponCode == couponCode){
						$(this).find("input[type='checkbox']").attr("checked","checked");
					}
				});
			}else if(sumlength > maxcount){
				$(obj).attr("checked",false);
				BINOLSSPRM74_3.showErrorMessage("当前您至多可以选择"+maxcount+"个产品，请重新勾选");
			}
		},
		"appendCoupon":function(){
			var couponCode_input=$("#couponCode_input").val();
			var memberCode=$("#memberCode").html();
			if(couponCode_input == null || couponCode_input == 'null' || couponCode_input == undefined || couponCode_input ==''){
				BINOLSSPRM74_3.showErrorMessage("请输入代物券券号");
				return false;
			}else if(couponCode_input.length != 15){
				BINOLSSPRM74_3.showErrorMessage("请输入正确的代物券券号");
				return false;
			}else{
				var memberPhone_param=$("#memberPhone_param").val();
				if(memberPhone_param){
					$("#couponPassword").val("");
					$("#mobileNo").empty();
					$("#mobileNo").append(memberPhone_param);
				}
				var flag=0;
				$("#rule_table tbody tr").each(function(){
					var couponCode=$(this).find("input[name='couponCode']").val();
					if(couponCode_input == couponCode){
						flag = 1;
					}
				});
				if(flag == 1){
					BINOLSSPRM74_3.showErrorMessage("该优惠券已添加，请勿重复添加");
					return false;
				}
				$("#dzq #couponName").html(couponCode_input);
				$("#dzq #coupnoMemberCode").html(memberCode);
				$("#dzq #couponCode").html(couponCode_input);
				$("#dzq #couponPassword").html('');
			}
			$("#dzq").show();
			$("#couponCode_input").val("");
		},
		"commitCoupon":function(){
			var memberPhone_param=$("#memberPhone_param").val();
			var mobileNo;
			if(memberPhone_param){
				mobileNo=$("#mobileNo").html();
			}else{
				mobileNo=$("#mobileNo input").val();
			}
			var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
			 if (!reg.test(mobileNo)) {
//				 $("#dzq").hide();
			      BINOLSSPRM74_3.showErrorMessage("您输入的手机号码有误，请确认后再提交");
			      return false;
			 }
			
			
			//获取需要校验的Coupon号与密码
			var couponCode=$("#dzq #couponCode").html();
			var couponPassword=$("#dzq #couponPassword").val();
			var main_json=$("#main_json").val();
			var datasourceName=$("#datasourceName").val();
			var map={
				"couponCode":couponCode,
				"password":couponPassword
			};
			var checkedCoupon_json=JSON2.stringify(map);
			var params="coupon_json="+checkedCoupon_json+"&datasourceName="+datasourceName+"&main_json="+main_json;
			var promotionProSendCheckUrl=$("#promotionProSendCheckUrl").attr("href");
			$.ajax({
				url: promotionProSendCheckUrl,
				data: params,
				timeout: 3000,
				type:'post',
				success: function(data) {
					if(data == null || data == "" || data == undefined || data == "ERROR"){
						return;
					}
					var param_map = eval("("+data+")");
					var resultCode=param_map.ResultCode;
					var resultMsg=param_map.ResultMsg;
					if(resultCode == 0){
						$("#memberPhone_param").val(mobileNo);
						var content=param_map.Content;
						var rule_map=content.Rule_map;
						var productList=content.ProductList;
						var length=$("#rule_table tr").length+1;
						//添加左侧活动列表数据
						var html='<tr onmouseover="BINOLSSPRM74_3.onmouseoverEvent(this,1);" onmouseout="BINOLSSPRM74_3.onmouseoutEvent(this);" onclick="BINOLSSPRM74_3.showDescriptionDtl(this);">';
						html +='<td><div class="checkbox">';
						html +='<input name="checkbox-'+length+'" type="checkbox" class="regular-checkbox" id="checkbox-'+length+'" onclick="BINOLSSPRM74_3.checkCoupon(this);" checked="checked">';
						html +='<label for="checkbox-'+length+'"><span></span></label>';
						html +='</div></td>';
//						html +='<td>'+rule_map.typeName+'</td>';
						html +='<td width="20%">'+rule_map.couponCode+'</td>';
						html +='<td width="25%">'+rule_map.couponName+'</td>';
						html +='<td>'+rule_map.startTime+'</td>';
						html +='<td>'+rule_map.endTime+'</td>';
						html +='<td  style="display:none;">';
						html +='<input name="couponName" value="'+rule_map.couponName+'">';
						html +='<input name="maincode" value="'+rule_map.maincode+'">';
						html +='<input name="couponType" value="'+rule_map.couponType+'">';
						html +='<input name="descriptionDtl" value="'+rule_map.descriptionDtl+'">';
						html +='<input name="couponCode" value="'+rule_map.couponCode+'">';
						html +='<input name="maxCount" value="'+rule_map.maxCount+'">';
						html +='<input name="fullFlag" value="'+rule_map.fullFlag+'">';
						html +='<input name="password" value="'+couponPassword+'"/>';
						html +='</td></tr>';
						$("#rule_table tbody").append(html);
						//添加右侧产品列表数据
						var html2='<tbody id="'+rule_map.couponCode+'" style="display:none;">';
						$(productList).each(function(i){
							html2 +='<tr onmouseover="BINOLSSPRM74_3.onmouseoverEvent(this,2)" onmouseout="BINOLSSPRM74_3.onmouseoutEvent(this);">';
							html2 +='<td><div class="checkbox" >';
							if(rule_map.fullFlag == 1){
								html2 +='<input checked="checked" onclick="BINOLSSPRM74_3.checkPro(this);" name="checkbox-b'+i+'" type="checkbox" class="regular-checkbox" id="checkbox-b'+i+'">';
							}else{
								html2 +='<input onclick="BINOLSSPRM74_3.checkPro(this);" name="checkbox-b'+i+'" type="checkbox" class="regular-checkbox" id="checkbox-b'+i+'">';
							}
							html2 +='<label for="checkbox-b'+i+'"><span></span></label>';
							html2 +='</div></td>';
							html2 +='<td>'+productList[i].unitCode+'</td>';
							html2 +='<td>'+productList[i].proName+'</td>';
							html2 +='<td>'+productList[i].ProQuantity+'</td>';
							html2 +='<td>'+productList[i].price+'</td>';
							html2 +='<td style="display:none;">';
							html2 +='<input name="unitCode" value="'+productList[i].unitCode+'"/>';
							html2 +='<input name="barCode" value="'+productList[i].barCode+'"/>';
							html2 +='<input name="price" value="'+productList[i].price+'"/>';
							html2 +='<input name="quantity" value="'+productList[i].ProQuantity+'"/>';
							html2 +='</tr>';
						});
						html2 +="</tbody>";
						$("#activityProTable").append(html2);
						$("#dzq #couponName").html("");
						$("#dzq #coupnoMemberCode").html("");
						$("#dzq #couponCode").html("");
						$("#dzq").hide();
					}else{
						$("#dzq #couponPassword").val("");
						BINOLSSPRM74_3.showErrorMessage(resultMsg);
					}
					
				},
				complete : function(XMLHttpRequest,status){
					var connectTimes=Number($("#connectTimes").val());
					if(status=='timeout' ){//超时,status还有success,error等值的情况
			　　　　　  	if(connectTimes <=0){
			　　　　　  		$("#connectTimes").val("1");
			　　　　　  		BINOLSSPRM74_3.showErrorMessage($("#connectNetErr").val());
			　　　　　  	}else{
			　　　　　  		for(connectTimes;connectTimes>0;connectTimes--){
			　　　　　  			$("#connectTimes").val(0);
			　　　　　  			BINOLSSPRM74_3.commitCoupon();
			　　　　　  		}
			　　　　　  	}
			　　　　}
				}
			});
		},
		"onmouseoverEvent":function(obj,num){
			if(num == 1){
				$(obj).attr("class","tron");
			}else if(num == 2){
				$(obj).attr("class","tron3");
			}
		},
		"onmouseoutEvent":function(obj){
			$(obj).removeAttr("class");
		},
		"scannerEvent":function(obj){
			var scannerinput=$(obj).val();
			$("#activityProTable tbody:visible tr").each(function(){
				var unitCode=$(this).find("input[name='unitCode']").val();
				if(scannerinput == unitCode){
					var $checkbox=$(this).find("input[type='checkbox']");
					if($checkbox.is(":checked")){
						$checkbox.attr("checked",false);
					}else{
						$checkbox.attr("checked","checked");
					}
					BINOLSSPRM74_3.checkPro($checkbox);
					return false;
				}
			});
			$(obj).val("");
		},
		"closeWindow":function(){
			var promotionSendProCollectUrl=$("#promotionSendProCollectUrl").attr("href");
			var main_json=$("#main_json").val();
			var datasourceName=$("#datasourceName").val();
			//关闭窗口前删除该单的表中所有数据
			var params="main_json="+main_json+"&closeFlag=1&datasourceName="+datasourceName;
			$.ajax({
				url: promotionSendProCollectUrl,
				data: params,
				timeout: 3000,
				type :'post',
				success: function(data) {
					if(data == 0){
						window.close();
					}else if(data == 1){
						BINOLSSPRM74_3.showErrorMessage($("#closeWindowErr").val());
					}
				},
				complete : function(XMLHttpRequest,status){
					var connectTimes=Number($("#connectTimes").val());
					if(status=='timeout' ){//超时,status还有success,error等值的情况
			　　　　　  	if(connectTimes <=0){
			　　　　　  		$("#connectTimes").val("1");
			　　　　　  		BINOLSSPRM74_3.showErrorMessage($("#connectNetErr").val());
			　　　　　  	}else{
			　　　　　  		for(connectTimes;connectTimes>0;connectTimes--){
			　　　　　  			$("#connectTimes").val(0);
			　　　　　  			BINOLSSPRM74_3.closeWindow();
			　　　　　  		}
			　　　　　  	}
			　　　　}
				}
			});
		},
		"collect":function(){
			var promotionSendProCollectUrl=$("#promotionSendProCollectUrl").attr("href");
			//获取已经计算完毕的代物券活动以及选择的产品
			var cart_all=new Array();
			var coupon_all=new Array();
			var checkProCount_flag=0;
			$("#rule_table tr").each(function(){
				var $checkbox=$(this).find("input[type='checkbox']");
				if($checkbox.is(":checked")){
					var maincode=$(this).find("input[name='maincode']").val();
					var couponCode=$(this).find("input[name='couponCode']").val();
					var couponType=$(this).find("input[name='couponType']").val();
					var couponName=$(this).find("input[name='couponName']").val();
					var map1={
							"Unitcode":"",
							"Barcode":"",
							"CouponCode":couponCode,
							"Price":"0",
							"ActualAmount":"0",
							"CouponType":couponType,
							"Maincode":maincode,
							"ActivityCode":maincode,
							"ActivityName":couponName,
							"RuleDetailType":"P",
							"Quantity":"1",
							"TagPrice":"0",
							"computePoint":"0"
					}
					coupon_all.push(map1);
					var checkProCount=0;
					$("#activityProTable "+"#"+couponCode).find("tr").each(function(){
						if($(this).find("input[type='checkbox']").is(":checked")){
							checkProCount += 1;
							var unitCode=$(this).find("input[name='unitCode']").val();
							var barCode=$(this).find("input[name='barCode']").val();
							var price=$(this).find("input[name='price']").val();
							var quantity=$(this).find("input[name='quantity']").val();
							var map={
									"unitCode":unitCode,
                                    "barCode":barCode,
                                    "price":price,
                                    "salePrice": "0",
                                    "quantity": quantity,
                                    "maincode": maincode
							};
							cart_all.push(map);
						}
					});
					if(checkProCount == 0){
						checkProCount_flag=1;
						BINOLSSPRM74_3.showErrorMessage("您选择了代物券但没有选择相对应的产品，请核实后提交");
					    return false;
					}
				}
			});
			if(checkProCount_flag == 1){
				return false;
			}
			var cart_json=JSON2.stringify(cart_all);
			var coupon_json=JSON2.stringify(coupon_all);
			var main_json=$("#main_json").val();
			var datasourceName=$("#datasourceName").val();
			var memberPhone_param=$("#memberPhone_param").val();
			var params="main_json="+main_json+"&shoppingcart_json="+cart_json+"&coupon_json="+coupon_json+"&closeFlag=0"+"&datasourceName="+datasourceName+"&memberPhone="+memberPhone_param;
			$.ajax({
				url: promotionSendProCollectUrl,
				data: params,
				timeout: 3000,
				type :'post',
				success: function(data) {
					if(data == null || data == "" || data == undefined || data == "ERROR"){
						return;
					}
					if(data == 0){
						window.close();
					}else if(data == 1){
						BINOLSSPRM74_3.showErrorMessage($("#collectErr").val());
					}
				},
				complete : function(XMLHttpRequest,status){
					var connectTimes=Number($("#connectTimes").val());
					if(status=='timeout' ){//超时,status还有success,error等值的情况
			　　　　　  	if(connectTimes <=0){
			　　　　　  		$("#connectTimes").val("1");
			　　　　　  		BINOLSSPRM74_3.showErrorMessage($("#connectNetErr").val());
			　　　　　  	}else{
			　　　　　  		for(connectTimes;connectTimes>0;connectTimes--){
			　　　　　  			$("#connectTimes").val(0);
			　　　　　  			BINOLSSPRM74_3.collect();
			　　　　　  		}
			　　　　　  	}
			　　　　}
				}
			});
		},//页面上直接验证的情况
		"commitCoupon2":function(obj){
			var memberPhone_param=$("#memberPhone_param").val();
			var mobileNo;
			if(memberPhone_param){
				mobileNo=$("#mobileNo2").html();
			}else{
				mobileNo=$("#mobileNo2 input").val();
			}
			var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
			 if (!reg.test(mobileNo)) {
//				 $("#dzq2").hide();
			      BINOLSSPRM74_3.showErrorMessage("您输入的手机号码有误，请确认后再提交");
			      return false;
			 }
			
			
			//获取需要校验的Coupon号与密码
			var couponCode=$("#dzq2 #couponCode2").html();
			var couponPassword=$("#dzq2 #couponPassword2").val();
			var main_json=$("#main_json").val();
			var datasourceName=$("#datasourceName").val();
			var map={
				"couponCode":couponCode,
				"password":couponPassword
			};
			var checkedCoupon_json=JSON2.stringify(map);
			var params="coupon_json="+checkedCoupon_json+"&datasourceName="+datasourceName+"&main_json="+main_json;
			var promotionProSendCheckUrl=$("#promotionProSendCheckUrl").attr("href");
			$.ajax({
				url: promotionProSendCheckUrl,
				data: params,
				timeout: 3000,
				type:'post',
				success: function(data) {
					if(data == null || data == "" || data == undefined || data == "ERROR"){
						return;
					}
					var param_map = eval("("+data+")");
					var resultCode=param_map.ResultCode;
					var resultMsg=param_map.ResultMsg;
					if(resultCode == 0){
						var fullFlag=$(obj).parents("tr").find("input[name='fullFlag']").val();
						var couponCode=$(obj).parents("tr").find("input[name='couponCode']").val();
						$(obj).parents("tr").find("input[name='password']").val(couponPassword);
						if(fullFlag == 1){
							$("#activityProTable "+"#"+couponCode).find("tr").each(function(){
								$(this).find("input[type='checkbox']").attr("checked","checked");
							});
						}
//						$("#rule_table tbody tr").each(function(){
//							var $couponCode=$(this).find("td:hidden input[name='couponCode']").val();
//							if($couponCode == couponCode){
//								$(this).find("input[name='password']").val(couponPassword);
//							}
//						});
						$("#dzq2").hide();
					}else{
						$("#rule_table tbody tr").each(function(){
							var $couponCode=$(this).find("td:hidden input[name='couponCode']").val();
							if($couponCode == couponCode){
								$(this).find("input[type='checkbox']").attr("checked",false);
							}
						});
						$("#activityProTable "+"#"+couponCode).find("tr").each(function(){
							$(this).find("input[type='checkbox']").attr("checked",false);
						});
						$("#dzq2 #couponPassword2").val("");
						BINOLSSPRM74_3.showErrorMessage(resultMsg);
					}
					
				},
				complete : function(XMLHttpRequest,status){
					var connectTimes=Number($("#connectTimes").val());
					if(status=='timeout' ){//超时,status还有success,error等值的情况
			　　　　　  	if(connectTimes <=0){
			　　　　　  		$("#connectTimes").val("1");
			　　　　　  		BINOLSSPRM74_3.showErrorMessage($("#connectNetErr").val());
			　　　　　  	}else{
			　　　　　  		for(connectTimes;connectTimes>0;connectTimes--){
			　　　　　  			$("#connectTimes").val(0);
			　　　　　  			BINOLSSPRM74_3.commitCoupon();
			　　　　　  		}
			　　　　　  	}
			　　　　}
				}
			});
		}
		
};

var BINOLSSPRM74_3 =  new BINOLSSPRM74_3();

$(document).ready(function() {
	BINOLSSPRM74_3.showResultMessage();
	var height=document.body.clientHeight;
	var width=document.body.clientWidth;
	$(".tablebox").css("height",height*0.55);
	$(".tablebox.gift").css("height",height*0.6);
	$(".leftbox").css("height",height*0.74);
	$(".rightbox").css("height",height*0.74);
	$(".bottom_item.promotion").css("font-size",width*0.015);
	$(".bottom_left").css("width",width*0.62);
	$(".bottom_right").css("width",width*0.3);
	$("#inputCouponCode").css("width",width*0.2);
	$(".bottom_right .jh_btn2").css("font-size",width*0.01);
	$(".bottom_right .jh_btn2").css("width",width*0.09);
});
