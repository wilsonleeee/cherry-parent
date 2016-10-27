function BINOLSSPRM74() {};

BINOLSSPRM74.prototype = {
		"changeCheck":function(obj){
			var maincode_check=$(obj).parents("tr").find("input[name='maincode']").val();
			//判断是否需要资格券来参加活动
			var ZGQFlag_check=$(obj).parents("tr").find("input[name='ZGQFlag']").val();
			//判断是否同时可以使用电子优惠券 0为不可以 1为可以
			var couponFlag=$(obj).parents("tr").find("input[name='couponFlag']").val();
			var ZGQcheck=1;
			var ZGQCount=0;
			if(ZGQFlag_check == 1){
				//需要资格券的情况进行校验
				$("#promotion_table #coupon_table tr").each(function(){
					var $obj=$(this);
					var $couponType=$obj.find("td:hidden input[name='couponType']").val();
					var $ZGQArr=$obj.find("td:hidden input[name='ZGQArr']").val();
					var $checked=$obj.find("input[type='checkbox']").is(":checked");
					if($checked && $couponType == 3){
						var ZGQArr=$ZGQArr.split(",");
						for(var i=0;i<ZGQArr.length;i++){
							if(maincode_check == ZGQArr[i]){
								ZGQcheck=0;
							}
							//校验是否一张资格券对应了多个活动的情况
							$("#promotion_table #rule_table tr").each(function(){
								var $$this=$(this);
								var $$checked=$$this.find("input[type='checkbox']").is(":checked");
								var $$ZGQFlag=$$this.find("td:hidden input[name='ZGQFlag']").val();
								var $$maincode=$$this.find("td:hidden input[name='maincode']").val();
								if($$ZGQFlag == 1 && $$checked && ZGQArr[i] == $$maincode){
									ZGQCount += 1;
								}
							});
						}
					}
				});
				if(ZGQcheck == 1){
					$(obj).removeAttr("checked");
					$(obj).parents("tr").find("input[name='checkFlag']").val("0");
					BINOLSSPRM74.showErrorMessage("请选择对应的资格券再参加活动");
					return false;
				}
				if(ZGQCount > 1){
					$(obj).removeAttr("checked");
					$(obj).parents("tr").find("input[name='checkFlag']").val("0");
					BINOLSSPRM74.showErrorMessage("您已经参与了对应资格券的活动");
					return false;
				}
			}
			//如果点选活动不能与电子优惠券同时使用，给予提示
			if(couponFlag == 0){
				var couponChecdedCount=0;
				$("#coupon_table tr").each(function(){
					var $checkbox=$(this).find("input:checkbox");
					if($checkbox.is(":checked")){
						couponChecdedCount += 1;
					}
				});
				if(couponChecdedCount > 0){
					$(obj).removeAttr("checked");
					$(obj).parents("tr").find("input[name='checkFlag']").val("0");
					BINOLSSPRM74.showErrorMessage("您选择的活动不能与电子优惠券同时使用,请了解活动详情");
					return false;
				}
			}
			
			
			//判断如果是交互活动的情况下，没有选择产品的情况下给予提示
			var flag=$(obj).attr("checked");
			if(flag == "checked"){
				$(obj).parents("tr").find("input[name='checkFlag']").val("1");
			}else{
				$(obj).parents("tr").find("input[name='checkFlag']").val("0");
			}
			var id=BINOLSSPRM74.getId();
			//如果选中的是积分兑换活动直接去除当前选中状态的缓存
			var activityType=$(obj).parents("tr").find("input[name='activityType']").val();
			if(activityType == "JFDK" && $(obj).is(":checked")){
				$("#"+id).removeAttr("value");
			}
			var result_json=$("#"+id).val();
			var getComputeRuleUrl=$("#getComputeRuleUrl").attr("href");
			
			if(result_json == null || result_json == 'null' || result_json == undefined || result_json ==''){//页面上没有保存的情况，去后台调用计算方法
				//加入需要计算规则的Json串
				var main_json=$("#main_json").val();
				var shoppingcart_json=$("#shoppingcartOrder_json").val();
				var ruleArr=BINOLSSPRM74.getAllRule();
				var datasourceName=$("#datasourceName").val();
				var couponAll_json=BINOLSSPRM74.getAllCoupon();
				var couponall=new Array();
				$("#coupon_table tr").each(function(){
					var $that=$(this).find("td:hidden");
					var checkFlag=$that.find("input[name='checkFlag']").val();
					if(checkFlag == "1"){
						var couponCode=$that.find("input[name='couponCode']").val();
						var couponName=$that.find("input[name='couponName']").val();
						var couponType=$that.find("input[name='couponType']").val();
						var unicode=$that.find("input[name='unicode']").val();
						var barcode=$that.find("input[name='barcode']").val();
						var checkFlag=$that.find("input[name='checkFlag']").val();
						var passwordFlag=$that.find("input[name='passwordFlag']").val();
						var password=$that.find("input[name='password']").val();
						var descriptionDtl=$that.find("input[name='descriptionDtl']").val();
						var map={
								"couponCode":couponCode,
								"couponName":couponName,
								"couponType":couponType,
								"unicode":unicode,
								"barcode":barcode,
								"checkFlag":checkFlag,
								"passwordFlag":passwordFlag,
								"password":password,
								"descriptionDtl":descriptionDtl
							};
						
						couponall.push(map);
					}
				});
				var coupon_json=JSON2.stringify(couponall);
				var usePoint_main=$("#usePoint_main").val();
				var params="main_json="+main_json+"&shoppingcart_json="+shoppingcart_json+"&rule_json="+ruleArr+"&couponAll_json="+couponAll_json+"&coupon_json="+coupon_json+"&datasourceName="+datasourceName+"&usePoint="+usePoint_main;
				BINOLSSPRM74.changeStateButton(0);
				$.ajax({
					url: getComputeRuleUrl,
					data: params,
					timeout: 3000,
					type:'post',
					success: function(data) {
						BINOLSSPRM74.changeStateButton(1);
						if(data == null || data == "" || data == undefined || data == "ERROR"){
							return;
						}
						var param_map = eval("("+data+")");
						var resultCode=param_map.resultCode;
						var resultMsg=param_map.resultMsg;
						if(resultCode != 0){
							var errorCoupon=param_map.errorCoupon;
							if(errorCoupon){
								$("#promotion_table #coupon_table tr").each(function(){
									var $obj=$(this);
									var $couponCode=$obj.find("td:hidden input[name='couponCode']").val();
									if(errorCoupon == $couponCode){
										$obj.find("input[type='checkbox']").attr('checked',false);
										$obj.find("input[name='checkFlag']").val('0');
										$obj.find("input[name='actualDiscountPrice']").val("");
										$obj.parents("tr").find("td:eq(5)").html("");
									}
								});
							}
								
							BINOLSSPRM74.showErrorMessage(resultMsg);
//							return false;
						}
						var content=param_map.content;
						var rule_list=content.rule_list;
						var coupon_list=content.coupon_list;
						var computedCart=content.computedCart;
						var computedRule=content.computedRule;
						var rule_id=content.rule_id;
						var rule_list_json=JSON2.stringify(content);
						var computedCart_json=JSON2.stringify(computedCart);
						var computedRule_json=JSON2.stringify(computedRule);
						if(resultCode == 0){
							var html="<input id='"+rule_id+"cart' value='"+computedCart_json+"'/>";
							html +="<input id='"+rule_id+"Rule' value='"+computedRule_json+"'/>";
							html +="<input id='"+rule_id+"' value='"+rule_list_json+"'/>";
							$("#param").append(html);
						}
						//匹配优惠券
						$(coupon_list).each(function(i){
							var actualDiscountPrice=coupon_list[i].actualDiscountPrice;
							var couponCode=coupon_list[i].couponCode;
							var checkFlag=coupon_list[i].checkFlag;
							var couponType=coupon_list[i].couponType;
							$("#coupon_table tr").each(function(i){
								var $this=$(this).find("td:last");
								if($this.find("input[name='couponCode']").val() == couponCode ){
									if(checkFlag == 1){
										if(couponType == 3){
											$this.find("input[name='actualDiscountPrice']").val('0');
											$this.find("input[name='checkFlag']").val('1');
											$this.parents("tr").find("td:eq(5)").html('');
										}else{
											$this.find("input[name='actualDiscountPrice']").val(actualDiscountPrice);
											$this.find("input[name='checkFlag']").val('1');
											$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(actualDiscountPrice));
										}
									}else{
										$this.find("input[name='checkFlag']").val('0');
										$this.find("input[name='actualDiscountPrice']").val("");
										$this.parents("tr").find("td:eq(5)").html("");
									}
								}
							});
						});
						
						//匹配规则
						$(rule_list).each(function(i){
							var flag=rule_list[i].flag;
							var maincode_rule=rule_list[i].maincode;
							var checkFlag=rule_list[i].checkFlag;
							var actualDiscountPrice=rule_list[i].actualDiscountPrice;
							var barcode=rule_list[i].barcode;
							var unicode=rule_list[i].unicode;
							var computePoint=rule_list[i].computePoint;
							var fullFlag=rule_list[i].fullFlag;
							var times=parseFloat(rule_list[i].times);
							$("#rule_table tr").each(function(){
								var $this=$(this).find("td:last");
								if($this.find("input[name='maincode']").val() == maincode_rule ){
									if(checkFlag == 1){
										$this.find("input[name='checkFlag']").val("1");
										$this.parents("tr").find("input[type='checkbox']").attr("checked","checked");
										$this.find("input[name='computePoint']").attr("value",computePoint);
										if(flag == 1){
											if(fullFlag == 1){
												var priceTotal=0;
												var salePriceTotal=0;
												$("#"+maincode_rule+" tr").each(function(){
													var $$this=$(this);
													var price=$$this.find("input[name='price']").val();
													var salePrice=$$this.find("input[name='salePrice']").val();
													if(price == null || price == 'null' || price == undefined || price ==''){
														price=0;
													}else{
														price=parseFloat(price)*times;
													}
													if(salePrice == null || salePrice == 'null' || salePrice == undefined || salePrice ==''){
														salePrice=0;
													}else{
														salePrice=parseFloat(salePrice)*times;
													}
													if(priceTotal == null || priceTotal == 'null' || priceTotal == undefined || priceTotal ==''){
														priceTotal=0;
													}else{
														priceTotal=parseFloat(priceTotal);
													}
													if(salePriceTotal == null || salePriceTotal == 'null' || salePriceTotal == undefined || salePriceTotal ==''){
														salePriceTotal=0;
													}else{
														salePriceTotal=parseFloat(salePriceTotal);
													}
													priceTotal += price;
													salePriceTotal += salePrice;
													$$this.find("input:checkbox").attr("checked","checked");
//													BINOLSSPRM74.changeCheck($(this).find("input:checkbox"));
													if($$this.is(":visible")){
														var quantity=$$this.find("input[name='quantity']").val();
														$$this.find("td:eq(3)").html(quantity*times);
													}
												});
												$("#"+maincode_rule+" input[name='priceTotal']").val(priceTotal);
												$("#"+maincode_rule+" input[name='salePriceTotal']").val(salePriceTotal);
												$this.find("input[name='actualDiscountPrice']").val(priceTotal-salePriceTotal);
												$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
											}else{
												var priceTotal=$("#"+maincode_rule+" input[name='priceTotal']").val();
												var salePriceTotal=$("#"+maincode_rule+" input[name='salePriceTotal']").val();
												$this.find("input[name='actualDiscountPrice']").attr("value",parseFloat(priceTotal-salePriceTotal));
												$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
												$this.parents("tr").find("td:eq(3)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
											}
										}else{
											$this.find("input[name='actualDiscountPrice']").attr("value",actualDiscountPrice);
											$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(actualDiscountPrice));
										}
									}else{
										if(flag == 1){
											$("#"+maincode_rule+" input[type='checkbox']").attr("checked",false);
											$("#"+maincode_rule+" input[name='priceTotal']").val("");
											$("#"+maincode_rule+" input[name='salePriceTotal']").val("");
											$this.parents("tr").find("td:eq(3)").html("0.0");
										}
										$this.parents("tr").find("td:eq(5)").html("");
										$this.parents("tr").find("input[type='checkbox']").removeAttr("checked");
										$this.find("input[name='checkFlag']").attr("value",0);
										$this.find("input[name='actualDiscountPrice']").removeAttr("value");
										$this.find("input[name='computePoint']").removeAttr("value");
									}
									
								}
							});
						});
						BINOLSSPRM74.calculateDiscountTotal();
					},
					complete : function(XMLHttpRequest,status){
						var connectTimes=Number($("#connectTimes").val());
						if(status=='timeout' ){//超时,status还有success,error等值的情况
				　　　　　  	if(connectTimes <=0){
				　　　　　  		$("#connectTimes").val("1");
				　　　　　  		BINOLSSPRM74.showErrorMessage($("#connectNetErr").val());
				　　　　　  		BINOLSSPRM74.changeStateButton(1);
				　　　　　  	}else{
				　　　　　  		for(connectTimes;connectTimes>0;connectTimes--){
				　　　　　  			$("#connectTimes").val(0);
				　　　　　  			BINOLSSPRM74.changeCheck(obj);
				　　　　　  		}
				　　　　　  	}
				　　　　}
					}
				});
			}else{//页面上有保存的情况，提取Json串进行还原
				var param_map = eval("("+result_json+")");
				var rule_list=param_map.rule_list;
				var coupon_list=param_map.coupon_list;
				//还原优惠券列表
				$(coupon_list).each(function(i){
					var couponCode=coupon_list[i].couponCode;
					var actualDiscountPrice=coupon_list[i].actualDiscountPrice;
					var checkFlag=coupon_list[i].checkFlag;
					var couponType=coupon_list[i].couponType;
					$("#coupon_table tr").each(function(){
						var $this=$(this).find("td:last");
						if($this.find("input[name='couponCode']").val() == couponCode ){
							if(checkFlag == "1"){
								if(couponType == 3){
									$this.parents("tr").find("input[type='checkbox']").attr("checked","checked");
									$this.find("input[name='checkFlag']").attr("checkFlag","1");
									$this.find("input[name='actualDiscountPrice']").attr("value",'0');
									$this.parents("tr").find("td:eq(5)").html('');
								}else{
									$this.parents("tr").find("input[type='checkbox']").attr("checked","checked");
									$this.find("input[name='checkFlag']").attr("checkFlag","1");
									$this.find("input[name='actualDiscountPrice']").attr("value",actualDiscountPrice);
									$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(actualDiscountPrice));
								}
							}else{
								$this.find("input[name='actualDiscountPrice']").removeAttr("value");
								$this.find("input[name='checkFlag']").attr("value","0");
								$this.parents("tr").find("td:eq(5)").html("");
								$this.parents("tr").find("input[type='checkbox']").removeAttr("checked");
							}
						}
					});
				});
				//还原规则列表
				$(rule_list).each(function(i){
					var maincode=rule_list[i].maincode;
					var actualDiscountPrice_old=rule_list[i].actualDiscountPrice;
					var checkFlag=rule_list[i].checkFlag;
					var flag=rule_list[i].flag;
					var computePoint=rule_list[i].computePoint;
					var fullFlag=rule_list[i].fullFlag;
					var times=rule_list[i].times;
					$("#rule_table tr").each(function(){
						var $this=$(this).find("td:last");
						if($this.find("input[name='maincode']").val() == maincode ){
							if(checkFlag == "1"){
								$this.parents("tr").find("input[type='checkbox']").attr("checked","checked");
								$this.find("input[name='checkFlag']").val("1");
								$this.find("input[name='computePoint']").attr("value",computePoint);
								if(flag == 1){
									if(fullFlag == 1){
										var priceTotal=0;
										var salePriceTotal=0;
										$("#"+maincode+" tr").each(function(){
											var $$this=$(this);
											var price=$$this.find("input[name='price']").val();
											var salePrice=$$this.find("input[name='salePrice']").val();
											if(price == null || price == 'null' || price == undefined || price ==''){
												price=0;
											}else{
												price=parseFloat(price)*times;
											}
											if(salePrice == null || salePrice == 'null' || salePrice == undefined || salePrice ==''){
												salePrice=0;
											}else{
												salePrice=parseFloat(salePrice)*times;
											}
											if(priceTotal == null || priceTotal == 'null' || priceTotal == undefined || priceTotal ==''){
												priceTotal=0;
											}else{
												priceTotal=parseFloat(priceTotal);
											}
											if(salePriceTotal == null || salePriceTotal == 'null' || salePriceTotal == undefined || salePriceTotal ==''){
												salePriceTotal=0;
											}else{
												salePriceTotal=parseFloat(salePriceTotal);
											}
											priceTotal += price;
											salePriceTotal += salePrice;
											$$this.find("input:checkbox").attr("checked","checked");
//											BINOLSSPRM74.changeCheck($(this).find("input:checkbox"));
										});
										$("#"+maincode+" input[name='priceTotal']").val(priceTotal);
										$("#"+maincode+" input[name='salePriceTotal']").val(salePriceTotal);
										$this.find("input[name='actualDiscountPrice']").val(priceTotal-salePriceTotal);
										$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
									}else{
										var priceTotal=$("#"+maincode+" input[name='priceTotal']").val();
										var salePriceTotal=$("#"+maincode+" input[name='salePriceTotal']").val();
										$this.find("input[name='actualDiscountPrice']").attr("value",parseFloat(priceTotal-salePriceTotal));
										$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
										$this.parents("tr").find("td:eq(3)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
									}
								}else{
									$this.find("input[name='actualDiscountPrice']").attr("value",actualDiscountPrice_old);
									$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(actualDiscountPrice_old));
								}
							}else{
								if(flag == 1){
									$("#"+maincode+" input[type='checkbox']").attr("checked",false);
									$("#"+maincode+" input[name='priceTotal']").val("");
									$("#"+maincode+" input[name='salePriceTotal']").val("");
									$this.parents("tr").find("td:eq(3)").html("0.0");
								}
								$this.parents("tr").find("td:eq(5)").html("");
								$this.find("input[name='actualDiscountPrice']").removeAttr("value");
								$this.find("input[name='computePoint']").removeAttr("value");
								$this.find("input[name='checkFlag']").val("0");
								$this.parents("tr").find("td:eq(5)").html("");
								$this.parents("tr").find("input[type='checkbox']").removeAttr("checked");
							}
						}
					});
				});
				BINOLSSPRM74.calculateDiscountTotal();
			}
		}
		,
		"showDescriptionDtl":function(obj){
			var descriptionDtl=$(obj).find("td:last [name='descriptionDtl']").val();
			$(".explainbox p").html(descriptionDtl);
			var flag=$(obj).find("td:last input[name='flag']").val();
			var maincode=$(obj).find("td:last input[name='maincode']").val();
			if(flag == 1){
				$("#activityProTable "+"#"+maincode).siblings("tbody").hide();
				$("#activityProTable "+"#"+maincode).show();
			}else{
				$("#activityProTable tbody").hide();
			}
			
		},
		"calculateDiscountTotal":function(){
			var sum=0;
			var sumSalePrice=0;
			//累计规则优惠
			$("#rule_table tr").each(function(){
				var checkedFlag=$(this).find("input:checkbox").attr("checked");
				if(checkedFlag == 'checked'){
					var $this=$(this).find("td:last");
					var flag=$this.find("input[name='flag']").val();
					var maincode=$this.find("input[name='maincode']").val();
					var actualDiscountPrice=$this.find("input[name='actualDiscountPrice']").val();
					if(flag == 1){
						var salePrice=$("#activityProTable "+"#"+maincode).find("input[name='salePriceTotal']").val();
						if(salePrice == null || salePrice == 'null' || salePrice == undefined || salePrice ==''){
							sumSalePrice += 0;
						}else{
							sumSalePrice += parseFloat(salePrice);
						}
					}
					if(actualDiscountPrice == null || actualDiscountPrice == 'null' || actualDiscountPrice == undefined || actualDiscountPrice ==''){
						return ;
					}else{
						sum += parseFloat(actualDiscountPrice);
					}
				}
			});
			//累计代金券优惠
			$("#coupon_table tr").each(function(){
				var checkedFlag=$(this).find("input:checkbox").attr("checked");
				if(checkedFlag == 'checked'){
					var $this=$(this).find("td:last");
					var maincode=$this.find("input[name='maincode']").val();
					var actualDiscountPrice=$this.find("input[name='actualDiscountPrice']").val();
					if(actualDiscountPrice == null || actualDiscountPrice == 'null' || actualDiscountPrice == undefined || actualDiscountPrice ==''){
						return ;
					}else{
						sum += parseFloat(actualDiscountPrice);
					}
				}
			});
			var receivableTotal=$("#receivableTotal_old").val();
			if(receivableTotal == null || receivableTotal == 'null' || receivableTotal == undefined || receivableTotal ==''){
				receivableTotal=0;
			}else{
				receivableTotal=parseFloat(receivableTotal);
			}
			var price1=BINOLSSPRM74.toDecimal2(receivableTotal+sumSalePrice);//应收
			var price2=BINOLSSPRM74.toDecimal2(parseFloat(sum));//优惠价格
			var price3=parseFloat((receivableTotal+sumSalePrice)+parseFloat(sum));//实收价格
			var price4=BINOLSSPRM74.toDecimal2(price3);
			$("#receivableTotal").html(price1);
			if(price1 + price2 <0){
				$('#discountTotal').html(price1);
			}else{
				$('#discountTotal').html(price2);
			}
			if(price4 <0){
				$("#actualTotal").html(0.0);
			}else{
				$("#actualTotal").html(price4);
			}
		},
		"showhidediv":function(id){
			var display =$('#'+id).css('display');
			if(display == 'none'){
				$('#'+id).show();
			}else{
				$('#'+id).hide();
			}
			if(id == "dzq"){
				var couponCode=$("#dzq #couponCode1").html();
				$("#coupon_table tr").each(function(){
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
		"getId":function(){//获取当前状态下的对应ID
			var id="";
			$("#promotion_table tr input[type='checkbox']").each(function(){
				var checkedFlag=$(this).attr("checked");
				if(checkedFlag == "checked"){
					id += "1";
				}else{
					id += "0";
				}
			});
			return id;
		},
		"collect":function(){
			//校验存在资格券情况下是否存在没有选取对应活动的情况，并给予提示
			var ZGQFlag=0;
			var ZGQRuleFlag=0;
			$("#promotion_table #coupon_table tr").each(function(){
				var $obj=$(this);
				var $couponType=$obj.find("td:hidden input[name='couponType']").val();
				var $checked=$obj.find("input[type='checkbox']").is(":checked");
				var $ZGQArr=$obj.find("td:hidden input[name='ZGQArr']").val();
				if($couponType == 3 && $checked){
					ZGQFlag += 1;
					var ZGQArr=$ZGQArr.split(",");
					for(var i=0;i<ZGQArr.length;i++){
						$("#promotion_table #rule_table tr").each(function(){
							var $$obj=$(this);
							var $$maincode=$$obj.find("td:hidden input[name='maincode']").val();
							var $$checked=$$obj.find("input[type='checkbox']").is(":checked");
							if($$checked && ZGQArr[i] == $$maincode){
								ZGQRuleFlag += 1;
							}
						});
					}
				}
			});
			if(ZGQRuleFlag == 0 && ZGQFlag > 0){
				BINOLSSPRM74.showErrorMessage("您选择了资格券但没有选择对应的资格券活动，请同时勾选资格券对应的活动");
				return false;
			}
			
			var collectUrl=$("#collectUrl").attr("href");
			var coupon_all=new Array();
			//获取已经计算完毕的优惠券(最后选中的优惠券)
			$("#coupon_table tr").each(function(){
				var $this=$(this).find("td:hidden");
				var checkFlag=$this.find("input[name='checkFlag']").val();
				if(checkFlag == 1 ){
					var couponCode=$this.find("input[name='couponCode']").val();
					var price=$this.find("input[name='actualDiscountPrice']").val();
					var unicode=$this.find("input[name='unicode']").val();
					var barcode=$this.find("input[name='barcode']").val();
					var couponType=$this.find("input[name='couponType']").val();
					var maincode=$this.find("input[name='maincode']").val();
					if(price == null || price == "" || price == undefined ){
						return;
					}
					var map={
							"CouponCode":couponCode,
							"Barcode":barcode,
							"Unitcode":unicode,
							"Price":price,
							"ActualAmount":price,
							"CouponType":couponType,
							"Maincode":maincode
						};
					coupon_all.push(map);
					}
			});
			//获取已经计算完毕的规则（最后选中的规则）
			var rule_all=new Array();
			//存在积分兑换的P类型
			var pointRule_all=new Array();
			var check_flag=0;
			$("#rule_table tr").each(function(){
				var $this=$(this).find("td:hidden");
				var checkFlag=$this.find("input[name='checkFlag']").val();
				if(checkFlag == 1 ){
					var activityMainCode=$this.find("input[name='maincode']").val();
					var activityCode=$this.find("input[name='activitycode']").val();
					var activityName=$this.find("input[name='mainname']").val();
					var price=$this.find("input[name='actualDiscountPrice']").val();
					var flag=$this.find("input[name='flag']").val();
					var unicode=$this.find("input[name='unicode']").val();
					var barcode=$this.find("input[name='barcode']").val();
					var times=$this.find("input[name='times']").val();
					var computePoint=$this.find("input[name='computePoint']").val();
					var activityType=$this.find("input[name='activityType']").val();
					var RuleDetailType;
					if(flag == 0){
						RuleDetailType="P";
					}else{
						RuleDetailType="N";
					}
					var map;
					if(flag == 1){
						var check_length=$("#"+activityMainCode).find("input:checkbox[checked='checked']").length;
						if(check_length == 0){
							BINOLSSPRM74.showErrorMessage("请选择右下角活动对应的产品，再参加活动");
							check_flag=1;
							return false;
						}
						$("#"+activityMainCode+" input:checkbox[checked='checked']").each(function(){
							$tr=$(this).parents("tr");
							var unicode_pro=$tr.find("input[name='unicode']").val();
							var barcode_pro=$tr.find("input[name='barcode']").val();
							var quantity_pro=$tr.find("td:eq(3)").html();
							var price_pro=$tr.find("input[name='price']").val();//售价
							var salePrice_pro=$tr.find("input[name='salePrice']").val();//原价
							map={
									"maincode":activityMainCode,
									"activitycode":activityCode,
									"mainname":activityName,
									"price":salePrice_pro,
									"type":RuleDetailType,
									"unitCode":unicode_pro,
									"barCode":barcode_pro,
									"quantity":quantity_pro,
									"salePrice":price_pro,
									"computePoint":computePoint,
									"flag":flag,
									"ruleType":activityType
								};
							rule_all.push(map);
						});
						
					}else{
						if(computePoint != 0){
							map={
									"maincode":activityMainCode,
									"activitycode":activityCode,
									"mainname":activityName,
									"price":'0',
									"type":RuleDetailType,
									"unitCode":unicode,
									"barCode":barcode,
									"quantity":times,
									"salePrice":price,
									"computePoint":computePoint,
									"flag":flag,
									"ruleType":activityType
							};
							pointRule_all.push(map);
						}
					}
				}
			});
			if(check_flag == 1){
				return false;
			}
			var coupon_json=JSON2.stringify(coupon_all);
			var pointRule_json=JSON2.stringify(pointRule_all);
			var rule_json=JSON2.stringify(rule_all);
			var main_json=$("#main_json").val();
			//页面上没有选中的状态获取原始购物车数据,有存在优惠券选中的情况，获取计算完成的购物和数据
			var shoppingcart_json="";
			var id=BINOLSSPRM74.getId();
			var coupon_all_length=$("#coupon_table input[type='checkbox']:checked").length;
			var rule_all_length=$("#rule_table input[type='checkbox']:checked").length;
			if(coupon_all_length >0 || rule_all_length >0){
				var id=BINOLSSPRM74.getId();
				shoppingcart_json=$("#"+id+"cart").val();
			}else{
				shoppingcart_json=$("#shoppingcartOrder_json").val();
			}
			var promotionRule_json=$("#"+id+"Rule").val();
			var receivableTotal=$("#receivableTotal").html();
			var discountTotal=$("#discountTotal").html();
			var actualTotal=$("#actualTotal").html();
			var datasourceName=$("#datasourceName").val();
			var memberPhone_param=$("#memberPhone_param").val();
			var params="main_json="+main_json+"&shoppingcart_json="+shoppingcart_json+"&rule_json="+rule_json+"&coupon_json="+coupon_json+"&receivableTotal="
			+receivableTotal+"&discountTotal="+discountTotal+"&actualTotal="+actualTotal+"&closeFlag=0"+"&datasourceName="+datasourceName+"&memberPhone="+memberPhone_param
			+"&promotionRule_json="+promotionRule_json+"&pointRule_json="+pointRule_json;
			BINOLSSPRM74.changeStateButton(0);
			$.ajax({
				url: collectUrl,
				data: params,
				timeout: 3000,
				type :'post',
				success: function(data) {
				    BINOLSSPRM74.changeStateButton(1);
					if(data == null || data == "" || data == undefined || data == "ERROR"){
						return;
					}
					if(data == 0){
						window.close();
					}else if(data == 1){
						BINOLSSPRM74.showErrorMessage($("#collectErr").val());
					}
				},
				complete : function(XMLHttpRequest,status){
					var connectTimes=Number($("#connectTimes").val());
					if(status=='timeout' ){//超时,status还有success,error等值的情况
			　　　　　  	if(connectTimes <=0){
			　　　　　  		$("#connectTimes").val("1");
			　　　　　  		BINOLSSPRM74.showErrorMessage($("#connectNetErr").val());
			　　　　　  		BINOLSSPRM74.changeStateButton(1);
			　　　　　  	}else{
			　　　　　  		for(connectTimes;connectTimes>0;connectTimes--){
			　　　　　  			$("#connectTimes").val(0);
			　　　　　  			BINOLSSPRM74.collect();
			　　　　　  		}
			　　　　　  	}
			　　　　}
				}
			});
		},
		"closeWindow":function(){
			window.close();
//			var collectUrl=$("#collectUrl").attr("href");
//			var main_json=$("#main_json").val();
//			var datasourceName=$("#datasourceName").val();
//			//关闭窗口前删除该单的表中所有数据
//			var params="main_json="+main_json+"&closeFlag=1&datasourceName="+datasourceName;
//			BINOLSSPRM74.changeStateButton(0);
//			$.ajax({
//				url: collectUrl,
//				data: params,
//				timeout: 3000,
//				type :'post',
//				success: function(data) {
//					BINOLSSPRM74.changeStateButton(1);
//					if(data == 0){
//						window.close();
//					}else if(data == 1){
//						BINOLSSPRM74.showErrorMessage($("#closeWindowErr").val());
//					}
//				},
//				complete : function(XMLHttpRequest,status){
//					var connectTimes=Number($("#connectTimes").val());
//					if(status=='timeout' ){//超时,status还有success,error等值的情况
//			　　　　　  	if(connectTimes <=0){
//			　　　　　  		$("#connectTimes").val("1");
//			　　　　　  		BINOLSSPRM74.showErrorMessage($("#connectNetErr").val());
//			　　　　　  		BINOLSSPRM74.changeStateButton(1);
//			　　　　　  	}else{
//			　　　　　  		for(connectTimes;connectTimes>0;connectTimes--){
//			　　　　　  			$("#connectTimes").val(0);
//			　　　　　  			BINOLSSPRM74.closeWindow();
//			　　　　　  		}
//			　　　　　  	}
//			　　　　}
//				}
//			});
		},
		"collectWithoutRule":function(){
			window.close();
//			var collectUrl=$("#collectUrl").attr("href");
//			var main_json=$("#main_json").val();
//			var datasourceName=$("#datasourceName").val();
//			var params="main_json="+main_json+"&closeFlag=1&datasourceName="+datasourceName;
//			BINOLSSPRM74.changeStateButton(0);
//			$.ajax({
//				url: collectUrl,
//				data: params,
//				type: 'post',
//				timeout: 3000,
//				success: function(data) {
//					BINOLSSPRM74.changeStateButton(1);
//					if(data == null || data == "" || data == undefined || data == "ERROR"){
//						return;
//					}
//					if(data == 0){
//						window.close();
//					}else if(data == 1){
//						BINOLSSPRM74.showErrorMessage($("#closeWindowErr").val());
//					}
//				},
//				complete : function(XMLHttpRequest,status){
//					var connectTimes=Number($("#connectTimes").val());
//					if(status=='timeout' ){//超时,status还有success,error等值的情况
//			　　　　　  	if(connectTimes <=0){
//			　　　　　  		$("#connectTimes").val("1");
//			　　　　　  		BINOLSSPRM74.showErrorMessage($("#connectNetErr").val());
//			　　　　　  		BINOLSSPRM74.changeStateButton(1);
//			　　　　　  	}else{
//			　　　　　  		for(connectTimes;connectTimes>0;connectTimes--){
//			　　　　　  			$("#connectTimes").val(0);
//			　　　　　  			BINOLSSPRM74.collectWithoutRule();
//			　　　　　  		}
//			　　　　　  	}
//			　　　　}
//				}
//			});
		},
		"showResultMessage":function(){
			var resultCode=$("#resultCode").val();
			var resultMessage=$("#resultMessage").val();
			if(resultCode != 0){
				$("#showResultMessage").html(resultMessage);
				$("#err").show();
			}
		},
		"changeProductCheck":function(obj){
			var $this=$(obj);
			var maincode=$this.parents("tbody").attr("id");
			if($this.attr("checked") == "checked"){
				var maxCount;
				//检验选择的产品总数量是否大于该活动最大允许数量
				//获取对应规则的最大允许数量
				$("#rule_table tr").each(function(){
					var input_checkbox=$(this).find("input:checkbox");
					var $that=$(this).find("td:last");
					var activityMainCode=$that.find("input[name='maincode']").val();
					var checkFlag=$that.find("input[name='checkFlag']").val();
					if(activityMainCode == maincode){
						var times=parseFloat($that.find("input[name='times']").val());
						var productNumber=$that.find("input[name='productNumber']").val();
						maxCount=productNumber*times;
						//获取当前选择后的数量
						var chooseCountAll=0;
						$("#"+activityMainCode+" tr").each(function(){
							if($(this).find("input[type='checkbox']").is(":checked")){
								var chooseCount=$(obj).parents("tr").find("td:eq(3)").html();
								if(chooseCount == null || chooseCount == 'null' || chooseCount == undefined || chooseCount ==''){
									chooseCount=0;
								}else{
									chooseCount=parseFloat(chooseCount);
								}
								chooseCountAll += chooseCount;
							}
						});
						//获取选择产品的金额
						var price=parseFloat($(obj).parents("tr").find("input[name='price']").val())*times;//销售价格
						var salePrice=parseFloat($(obj).parents("tr").find("input[name='salePrice']").val())*times;//产品原价
						if(chooseCountAll > maxCount){
							BINOLSSPRM74.showErrorMessage("当前您至多可以选择"+maxCount+"个产品，请重新勾选");
							$this.attr("checked",false);
							return false;
						}else{
//							BINOLSSPRM74.changeCheck(input_checkbox);
							var priceTotal=$("#"+activityMainCode+" input[name='priceTotal']").val();
							var salePriceTotal=$("#"+activityMainCode+" input[name='salePriceTotal']").val();
							//修改页面上显示的数量
							$("#"+activityMainCode+" tr:visible").each(function(){
								var quantity=$(this).find("input[name='quantity']").val();
								$(this).find("td:eq(3)").html(quantity*times);
							});
							if(priceTotal == null || priceTotal == 'null' || priceTotal == undefined || priceTotal ==''){
								priceTotal=0;
							}else{
								priceTotal=parseFloat(priceTotal);
							}
							if(salePriceTotal == null || salePriceTotal == 'null' || salePriceTotal == undefined || salePriceTotal ==''){
								salePriceTotal=0;
							}else{
								salePriceTotal=parseFloat(salePriceTotal);
							}
							priceTotal += price;
							salePriceTotal += salePrice;
							$this.parents("tbody").find("tr:hidden input[name='priceTotal']").val(priceTotal);//销售价
							$this.parents("tbody").find("tr:hidden input[name='salePriceTotal']").val(salePriceTotal);//原价
//							var quantity=$this.parents("tbody").find("input[name='quantity']").val();
//							$this.parents("tbody").find("input[name='quantity']").val(parseFloat(quantity)*times);
							if(checkFlag == 1){
								$that.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
							}
							$(this).find("input:checkbox").attr("checked","checked");
							BINOLSSPRM74.changeCheck($(this).find("input:checkbox"));
							BINOLSSPRM74.calculateDiscountTotal();
						}
					}
				});
			}else{
				$("#rule_table tr").each(function(){
					var $that=$(this).find("td:last");
					var activityMainCode=$that.find("input[name='maincode']").val();
					var priceTotal=$("#"+activityMainCode+" input[name='priceTotal']").val();
					var salePriceTotal=$("#"+activityMainCode+" input[name='salePriceTotal']").val();
					var checkFlag=$that.find("input[name='checkFlag']").val();
					var fullFlag=$that.find("input[name='fullFlag']").val();
					if(activityMainCode == maincode){
						if(fullFlag == 1){
							$(obj).attr("checked","checked");
							BINOLSSPRM74.showErrorMessage("您选择的产品为套装组合，不能改变组合形式");
							return false;
						}
						var times=parseFloat($that.find("input[name='times']").val());
						//获取选择产品的金额
						var price=parseFloat($(obj).parents("tr").find("input[name='price']").val())*times;//销售价
						var salePrice=parseFloat($(obj).parents("tr").find("input[name='salePrice']").val())*times;//原价
						if(priceTotal == null || priceTotal == 'null' || priceTotal == undefined || priceTotal ==''){
							priceTotal=0;
						}else{
							priceTotal=parseFloat(priceTotal);
						}
						if(salePriceTotal == null || salePriceTotal == 'null' || salePriceTotal == undefined || salePriceTotal ==''){
							salePriceTotal=0;
						}else{
							salePriceTotal=parseFloat(salePriceTotal);
						}
						priceTotal -= price;
						salePriceTotal -= salePrice;
						$this.parents("tbody").find("tr:hidden input[name='priceTotal']").val(priceTotal);
						$this.parents("tbody").find("tr:hidden input[name='salePriceTotal']").val(salePriceTotal);
						$this.parents("tr").find("td:eq(3)").html(1);
						var checklength=$("#"+activityMainCode).find("input:checkbox[checked='checked']").length;
						if(checklength == 0){
							$that.find("input[name='actualDiscountPrice']").val(0);
							$that.parents("tr").find("td:eq(5)").html("");
							$that.parents("tr").find("td:eq(3)").html("0.0");
							$(this).find("input:checkbox").attr("checked",false);
							$(this).parent().find("input[name='checkFlag']").val(0);
						}else{
							$that.find("input[name='actualDiscountPrice']").val(priceTotal-salePriceTotal);
							if(checkFlag == 1){
								$that.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
								$that.parents("tr").find("td:eq(3)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
							}
						}
						BINOLSSPRM74.calculateDiscountTotal();
					}
				});
			}
		},
		"showErrorMessage":function(message){
			$("#showResultMessage").html(message);
			$("#err").show();
			$("#showResultMessage").parent().find(".btn_qr").focus();
		},
		"pbAfterSelectFun":function(){
			var length=$("#activityProTable tbody:visible").length;
			if(length == 0){
				// 清空扫描输入框
				$("#productSearchStr").val("");
				return false;
			}else{
				var $this=$("#activityProTable tbody:visible");
				var flag=0;
				var unicode_input=$("#productSearchStr").val();
				$this.find("tr:visible").each(function(){
					var unicode=$(this).find("input[name='unicode']").val();
					if(unicode_input == unicode){
						var checkFlag=$(this).find("input:checkbox").attr("checked");
						if(checkFlag == "checked"){
							$(this).find("input:checkbox").attr("checked",false);
						}else{
							$(this).find("input:checkbox").attr("checked",true);
						}
						BINOLSSPRM74.changeProductCheck($(this).find("input:checkbox").get(0));
						flag=1;
						return false;
					}
				});
				if(flag == 0){
					BINOLSSPRM74.showErrorMessage("请输入正确的5位产品编码");
				}
				// 清空扫描输入框
				$("#productSearchStr").val("");
			}
		},
		"changeStateButton":function(state){
			if(state == 1){
				$(".bottom_right button").removeAttr("disabled");
			}else{
				$(".bottom_right button").attr("disabled","disabled");
			}
		},
		"addCoupon":function(){
			var couponCode_input=$("#inputCouponCode").val();
			//没有输入券号点击添加给出错误提示
			if(couponCode_input == null || couponCode_input == 'null' || couponCode_input == undefined || couponCode_input ==''){
				BINOLSSPRM74.showErrorMessage("请输入电子优惠券号");
				return false;
			}
			//券号长度校验
//			if(couponCode_input.length != 15){
//				BINOLSSPRM74.showErrorMessage("请输入正确的电子优惠券号");
//				return false;
//			}
			var memberPhone_param=$("#memberPhone_param").val();
			if(memberPhone_param){
				$("#mobileNo").empty();
				$("#mobileNo").append(memberPhone_param);
			}
			//已经存在优惠券的情况下再添加券(同一种类的券只能添加一张)
//			var couponLength=$("#coupon_table input:checkbox[checked='checked']").length;
//			if(Number(couponLength) >= 1){
//				BINOLSSPRM74.showErrorMessage("您至多可以选择一张优惠券");
//				return false;
//			}
			var flag=0;
			//如果列表中已经存在相同的优惠券了就不允许添加
			$("#coupon_table tr").each(function(){
				var couponCode_old=$(this).find("input[name='couponCode']").val();
				if(couponCode_input == couponCode_old){
					flag=1;
					return false;
				}
			});
			if(flag == 1){
				BINOLSSPRM74.showErrorMessage("该优惠券已添加，请勿重复添加");
				return false;
			}
			$("#dzq2 #couponCode2").html(couponCode_input);
			$("#dzq2 #couponName2").html(couponCode_input);
			$("#dzq2 #password").val("");
			$("#dzq2").show();

		},
		"onmouseoverEvent":function(obj){
			$(obj).attr("class","tron");
		},
		"onmouseoutEvent":function(obj){
			$(obj).attr("class","");
		},
		"openCouponWindow":function(obj){
			//校验选择后每个类型的券总数
			var couponType1=0;
			var couponType2=0;
			var couponType3=0;
			var couponType4=0;
			$("#promotion_table #coupon_table tr").each(function(){
				var $obj=$(this);
				var $couponType=$obj.find("td:hidden input[name='couponType']").val();
				var $checked=$obj.find("input[type='checkbox']").is(":checked");
				if($couponType == 1 && $checked){
					couponType1 += 1;
				}else if($couponType == 2 && $checked){
					couponType2 += 1;
				}else if($couponType == 3 && $checked){
					couponType3 += 1;
				}else if($couponType == 4 && $checked){
					couponType4 += 1;
				}
			});
			if(couponType1 > 1 || couponType2 > 1 || couponType3 > 1 || couponType4 > 1 ){
				$(obj).attr("checked",false);
				BINOLSSPRM74.showErrorMessage("您至多可以选择一张相同类型的优惠券");
				return false;
			}
			var $this=$(obj);
			var $that=$this.parents("tr").find("td:hidden");
			var couponName=$that.find("input[name='couponName']").val();
			var couponCode=$that.find("input[name='couponCode']").val();
			var passwordFlag=$that.find("input[name='passwordFlag']").val();
			var password=$that.find("input[name='password']").val();
			var couponType=$that.find("input[name='couponType']").val();
			var ZGQArr=$that.find("input[name='ZGQArr']").val();
			var memberCode=$("#memberCode").html();
			if($this.parents("tr").find("input[type='checkbox']").is(":checked")){
				$this.parents("tr").find("input[name='checkFlag']").val("1");
			}else{
				//非选中状态的资格券，需要将所有对应的资格券活动进行移除
				if(couponType == 3){
					var ZGQArr=ZGQArr.split(",");
					for(var i=0;i<ZGQArr.length;i++){
						$("#promotion_table #rule_table tr").each(function(){
							var $$obj=$(this);
							var $$maincode=$$obj.find("td:hidden input[name='maincode']").val();
							if(ZGQArr[i] == $$maincode){
								$$obj.find("input[type='checkbox']").removeAttr("checked");
								$$obj.find("input[name='checkFlag']").attr("0");
								$$obj.find("input[name='actualDiscountPrice']").val("");
								$$obj.find("td:eq(5)").html("");
							}
						});
					}
				}
				$this.parents("tr").find("input[name='checkFlag']").val("0");
			}
			//如果页面上存在已经选中并且是不能和优惠券同时存在的促销活动时，给予提示
			var couponFlagSum=0;
			$("#promotion_table #rule_table tr").each(function(){
				var $obj=$(this);
				var $couponFlag=$obj.find("td:hidden input[name='couponFlag']").val();
				var $checkFlag=$obj.find("input:checked").is(":checked");
				if($couponFlag == 0 && $checkFlag){
					couponFlagSum += 1;
				}
			});
			if(couponFlagSum > 0){
				$(obj).attr("checked",false);
				$(obj).find("input[name='checkFlag']").attr("0");
				BINOLSSPRM74.showErrorMessage("该活动不能与优惠券共享，请了解活动详情");
				return false;
			}
			
			var checkFlag=$that.find("input[name='checkFlag']").val();
			//需要密码的情况弹出窗口让用户输入密码
			if(passwordFlag == 1 && checkFlag == 1 && (password == null || password == 'null' || password == undefined || password =='')){
				$("#dzq #couponCode1").html(couponCode);
				$("#dzq #password1").val("");
				$("#dzq").show();
			}else{//不需要密码直接走后台校验流程
				var map={
					"couponCode":couponCode,
					"password":password,
					"checkFlag":checkFlag,
					"showFlag":1
				}
				BINOLSSPRM74.checkCoupon(map);
			}
		},
		"getAllRule":function(){
			var all=new Array();
			$("#rule_table tr").each(function(){
				var $obj=$(this).find("td:last");
				var flag=$obj.find("input[name='flag']").val();
				var times=$obj.find("input[name='times']").val();
				var maincode=$obj.find("input[name='maincode']").val();
				var mainname=$obj.find("input[name='mainname']").val();
				var rulecondtype=$obj.find("input[name='rulecondtype']").val();
				var ismust=$obj.find("input[name='ismust']").val();
				var subcampaignvalid=$obj.find("input[name='subcampaignvalid']").val();
				var matchtimes=$obj.find("input[name='matchtimes']").val();
				var planDiscountPrice=$obj.find("input[name='planDiscountPrice']").val();
				var actualDiscountPrice=$obj.find("input[name='actualDiscountPrice']").val();
				var activityType=$obj.find("input[name='activityType']").val();
				var checkFlag=$obj.find("input[name='checkFlag']").val();
				var ZGQFlag=$obj.find("input[name='ZGQFlag']").val();
				var fullFlag=$obj.find("input[name='fullFlag']").val();
				var couponFlag=$obj.find("input[name='couponFlag']").val();
				var unitPoint=$obj.find("input[name='unitPoint']").val();
				var maxPoint=$obj.find("input[name='maxPoint']").val();
				var activitycode=$obj.find("input[name='activitycode']").val();
				var map={
						"flag":flag,
						"times":times,
						"maincode":maincode,
						"mainname":mainname,
						"rulecondtype":rulecondtype,
						"ismust":ismust,
						"subcampaignvalid":subcampaignvalid,
						"planDiscountPrice":planDiscountPrice,
						"actualDiscountPrice":actualDiscountPrice,
						"matchtimes":matchtimes,
						"activityType":activityType,
						"checkFlag":checkFlag,
						"ZGQFlag":ZGQFlag,
						"fullFlag":fullFlag,
						"couponFlag":couponFlag,
						"unitPoint":unitPoint,
						"maxPoint":maxPoint,
						"activitycode":activitycode
					};
				all.push(map);
			});
			return JSON2.stringify(all);
		},
		"getAllCoupon":function(){
			var all=new Array();
			$("#coupon_table tr").each(function(){
				var $obj=$(this).find("td:last");
				var couponCode=$obj.find("input[name='couponCode']").val();
				var couponName=$obj.find("input[name='couponName']").val();
				var couponType=$obj.find("input[name='couponType']").val();
				var unicode=$obj.find("input[name='unicode']").val();
				var barcode=$obj.find("input[name='barcode']").val();
				var checkFlag=$obj.find("input[name='checkFlag']").val();
				var passwordFlag=$obj.find("input[name='passwordFlag']").val();
				var password=$obj.find("input[name='password']").val();
				var descriptionDtl=$obj.find("input[name='descriptionDtl']").val();
				var map={
						"couponCode":couponCode,
						"couponName":couponName,
						"couponType":couponType,
						"unicode":unicode,
						"barcode":barcode,
						"checkFlag":checkFlag,
						"passwordFlag":passwordFlag,
						"password":password,
						"descriptionDtl":descriptionDtl
					};
				all.push(map);
			});
			return JSON2.stringify(all);
		},
		"checkCoupon":function(obj){
			var id=BINOLSSPRM74.getId();
			var result_json=$("#"+id).val();
			var rule_json=BINOLSSPRM74.getAllRule();//全部规则
			var main_json=$("#main_json").val();//主单信息
			var shoppingcart_json=$("#shoppingcartOrder_json").val();//购物车信息
			var datasourceName=$("#datasourceName").val();
			var showFlag=obj.showFlag;
			//保存用户输入的密码
			var password_input=obj.password;
			var couponCode_input=obj.couponCode;
			if(result_json == null || result_json == 'null' || result_json == undefined || result_json ==''){//页面上没有保存的情况，去后台调用计算方法
				var getComputeRuleUrl=$("#getComputeRuleUrl").attr("href");
				var couponall=new Array();
				$("#coupon_table tr").each(function(){
					var checkFlag=obj.checkFlag;
					var $that=$(this).find("td:hidden");
					var $checkFlag=$(this).find("input[type='checkbox']").is(":checked");
					if(($that.find("input[name='couponCode']").val() == couponCode_input) && checkFlag == "1"){
//						$that.find("input[name='password']").val(password_input);
						var couponCode=$that.find("input[name='couponCode']").val();
						var couponName=$that.find("input[name='couponName']").val();
						var couponType=$that.find("input[name='couponType']").val();
						var unicode=$that.find("input[name='unicode']").val();
						var barcode=$that.find("input[name='barcode']").val();
						var checkFlag=$that.find("input[name='checkFlag']").val();
						var passwordFlag=$that.find("input[name='passwordFlag']").val();
						var password=$that.find("input[name='password']").val();
						var descriptionDtl=$that.find("input[name='descriptionDtl']").val();
						var map={
								"couponCode":couponCode,
								"couponName":couponName,
								"couponType":couponType,
								"unicode":unicode,
								"barcode":barcode,
								"checkFlag":checkFlag,
								"passwordFlag":passwordFlag,
								"password":password,
								"descriptionDtl":descriptionDtl
							};
						
						couponall.push(map);
					}else if($checkFlag){
						var couponCode=$that.find("input[name='couponCode']").val();
						var couponName=$that.find("input[name='couponName']").val();
						var couponType=$that.find("input[name='couponType']").val();
						var unicode=$that.find("input[name='unicode']").val();
						var barcode=$that.find("input[name='barcode']").val();
						var checkFlag=$that.find("input[name='checkFlag']").val();
						var passwordFlag=$that.find("input[name='passwordFlag']").val();
						var password=$that.find("input[name='password']").val();
						var descriptionDtl=$that.find("input[name='descriptionDtl']").val();
						var map={
								"couponCode":couponCode,
								"couponName":couponName,
								"couponType":couponType,
								"unicode":unicode,
								"barcode":barcode,
								"checkFlag":checkFlag,
								"passwordFlag":passwordFlag,
								"password":password,
								"descriptionDtl":descriptionDtl
							};
						couponall.push(map);
					}
				});
				var coupon_json=JSON2.stringify(couponall);
				var couponAll_json=BINOLSSPRM74.getAllCoupon();
				var params="main_json="+main_json+"&shoppingcart_json="+shoppingcart_json+"&rule_json="+rule_json+"&coupon_json="+coupon_json+"&couponAll_json="+couponAll_json+"&datasourceName="+datasourceName;
				BINOLSSPRM74.changeStateButton(0);
				$.ajax({
					url: getComputeRuleUrl,
					data: params,
					type: 'post',
					timeout: 3000,
					success: function(data) {
						BINOLSSPRM74.changeStateButton(1);
						if(data == null || data == "" || data == undefined || data == "ERROR"){
//							$("#coupon_table tr").each(function(){
//								var $that=$(this).find("td:hidden");
//								if($that.find("input[name='couponCode']").val() == couponCode_input){
//									$(this).find("input:checkbox").attr("checked",false);
//								}
//							});
							return;
						}
						var check_map = eval("("+data+")");
						var resultCode=check_map.resultCode;
						var resultMsg=check_map.resultMsg;
						if(resultCode != 0){
							$("#coupon_table tr").each(function(){
								var checkFlag=obj.checkFlag;
								var $that=$(this).find("td:hidden");
								var $checkFlag=$(this).find("input[type='checkbox']").is(":checked");
								if(($that.find("input[name='couponCode']").val() == couponCode_input) && checkFlag == "1"){
									$that.find("input[name='password']").val("");
									$that.find("input[name='checkFlag']").val("0");
									if(showFlag == 1){
										$(this).find("input[type='checkbox']").removeAttr("checked");
									}
								}
							});
							$("#password1").val("");
							BINOLSSPRM74.showErrorMessage(resultMsg);
							BINOLSSPRM74.calculateDiscountTotal();
							return false;
						}
//						$("#dzq input[name='couponTypeFlag']").val("0");
						var param_map=check_map.content;
						var rule_list=param_map.rule_list;
						var coupon_list=param_map.coupon_list;
						var rule_id=param_map.rule_id;
						var rule_list_json=JSON2.stringify(param_map);
						var computedCart=param_map.computedCart;
						var computedCart_json=JSON2.stringify(computedCart);
//						if(resultCode == 0){
//							var html="<input id='"+rule_id+"cart' value='"+computedCart_json+"'/>";
//							html +="<input id='"+rule_id+"' value='"+rule_list_json+"'/>";
//						}
						$("#"+rule_id+"cart").remove();
						var html="<input id='"+rule_id+"cart' value='"+computedCart_json+"'/>";
						$("#param").append(html);
						//匹配优惠券
						$(coupon_list).each(function(i){
							var actualDiscountPrice=coupon_list[i].actualDiscountPrice;
							var couponCode=coupon_list[i].couponCode;
							var checkFlag=coupon_list[i].checkFlag;
							var unicode=coupon_list[i].unicode;
							var barcode=coupon_list[i].barcode;
							var couponType=coupon_list[i].couponType;
							$("#coupon_table tr").each(function(i){
								var $this=$(this).find("td:last");
								if($this.find("input[name='couponCode']").val() == couponCode ){
									if(checkFlag == 1){
										if(couponType == 3){
											$this.parents("tr").find("td:eq(5)").html('');
										}else{
											$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(actualDiscountPrice));
										}
										$this.find("input[name='unicode']").val(unicode);
										$this.find("input[name='barcode']").val(barcode);
										$this.find("input[name='actualDiscountPrice']").val(actualDiscountPrice);
										$this.find("input[name='checkFlag']").val('1');
									}else{
										$this.find("input[name='unicode']").val("");
										$this.find("input[name='barcode']").val("");
										$this.find("input[name='checkFlag']").val('0');
										$this.find("input[name='actualDiscountPrice']").val("");
										$this.parents("tr").find("td:eq(5)").html("");
									}
								}
							});
						});
						
						//匹配规则
						$(rule_list).each(function(i){
							var flag=rule_list[i].flag;
							var maincode_rule=rule_list[i].maincode;
							var checkFlag=rule_list[i].checkFlag;
							var actualDiscountPrice=rule_list[i].actualDiscountPrice;
							var barcode=rule_list[i].barcode;
							var unicode=rule_list[i].unicode;
							$("#rule_table tr").each(function(){
								var $this=$(this).find("td:last");
								if($this.find("input[name='maincode']").val() == maincode_rule ){
									if(checkFlag == 1){
										$this.find("input[name='checkFlag']").attr("value",1);
										$this.parents("tr").find("input[type='checkbox']").attr("checked","checked");
										if(flag == 1){
											var priceTotal=$("#"+maincode_rule+" input[name='priceTotal']").val();
											var salePriceTotal=$("#"+maincode_rule+" input[name='salePriceTotal']").val();
											$this.find("input[name='actualDiscountPrice']").attr("value",parseFloat(priceTotal-salePriceTotal));
											$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
										}else{
											$this.find("input[name='actualDiscountPrice']").attr("value",actualDiscountPrice);
											$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(actualDiscountPrice));
										}
									}else{
										$this.parents("tr").find("input[type='checkbox']").removeAttr("checked");
										$this.find("input[name='checkFlag']").attr("value",0);
										$this.find("input[name='actualDiscountPrice']").removeAttr("value");
										$this.parents("tr").find("td:eq(5)").html("");
									}
									
								}
							});
						});
						$("#dzq").hide();
						BINOLSSPRM74.calculateDiscountTotal();
				},
				complete : function(XMLHttpRequest,status){
					var connectTimes=Number($("#connectTimes").val());
					if(status=='timeout' ){//超时,status还有success,error等值的情况
			　　　　　  	if(connectTimes <=0){
			　　　　　  		$("#connectTimes").val("1");
			　　　　　  		BINOLSSPRM74.showErrorMessage($("#connectNetErr").val());
			　　　　　  		BINOLSSPRM74.changeStateButton(1);
			　　　　　  		$("#coupon_table tr").each(function(){
								var $that=$(this).find("td:hidden");
								if($that.find("input[name='couponCode']").val() == couponCode){
									$(this).find("input:checkbox").attr("checked",false);
								}
			　　　　　  		});
			　　　　　  		$("#dzq .dzq_input").val("");
							$("#dzq input[name='couponTypeFlag']").val("0");
							$("#couponCode").html("");
							$("#couponName").html("");
							$("#dzq").hide();
			　　　　　  	}else{
			　　　　　  		for(connectTimes;connectTimes>0;connectTimes--){
			　　　　　  			$("#connectTimes").val(0);
			　　　　　  			BINOLSSPRM74.checkCoupon(obj);
			　　　　　  		}
			　　　　　  	}
			　　　　}
				}
			});
		}else{//页面上有保存的情况，提取Json串进行还原
			var param_map = eval("("+result_json+")");
			var rule_list=param_map.rule_list;
			var coupon_list=param_map.coupon_list;
			//还原优惠券列表
			$(coupon_list).each(function(i){
				var couponCode=coupon_list[i].couponCode;
				var actualDiscountPrice=coupon_list[i].actualDiscountPrice;
				var checkFlag=coupon_list[i].checkFlag;
				var couponType=coupon_list[i].couponType;
				$("#coupon_table tr").each(function(){
					var $this=$(this).find("td:last");
					if($this.find("input[name='couponCode']").val() == couponCode ){
						if(checkFlag == "1"){
							if(couponType == 3){
								$this.parents("tr").find("td:eq(5)").html('');
							}else{
								$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(actualDiscountPrice));
							}
							$this.parents("tr").find("input[type='checkbox']").attr("checked","checked");
							$this.find("input[name='checkFlag']").attr("checkFlag","1");
							$this.find("input[name='actualDiscountPrice']").attr("value",actualDiscountPrice);
						}else{
							$this.find("input[name='actualDiscountPrice']").removeAttr("value");
							$this.find("input[name='checkFlag']").attr("value","0");
							$this.parents("tr").find("td:eq(5)").html("");
							$this.parents("tr").find("input[type='checkbox']").removeAttr("checked");
						}
					}
				});
			});
			//还原规则列表
			$(rule_list).each(function(i){
				var maincode=rule_list[i].maincode;
				var actualDiscountPrice_old=rule_list[i].actualDiscountPrice;
				var checkFlag=rule_list[i].checkFlag;
				var flag=rule_list[i].flag;
				$("#rule_table tr").each(function(){
					var $this=$(this).find("td:last");
					if($this.find("input[name='maincode']").val() == maincode ){
						if(checkFlag == "1"){
							$this.parents("tr").find("input[type='checkbox']").attr("checked","checked");
							$this.find("input[name='checkFlag']").attr("checkFlag","1");
							if(flag == 1){
								var priceTotal=$("#"+maincode+" input[name='priceTotal']").val();
								var salePriceTotal=$("#"+maincode+" input[name='salePriceTotal']").val();
								$this.find("input[name='actualDiscountPrice']").attr("value",parseFloat(priceTotal-salePriceTotal));
								$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
							}else{
								$this.find("input[name='actualDiscountPrice']").attr("value",actualDiscountPrice_old);
								$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(actualDiscountPrice_old));
							}
						}else{
							$this.find("input[name='actualDiscountPrice']").removeAttr("value");
							$this.find("input[name='checkFlag']").attr("value","0");
							$this.parents("tr").find("td:eq(5)").html("");
							$this.parents("tr").find("input[type='checkbox']").removeAttr("checked");
						}
					}
				});
			});
			BINOLSSPRM74.calculateDiscountTotal();
			
		}
	},
	"checkCouponTo":function(){
		var memberCode=$("#memberCode").html();
		var mobileNo;
		if(memberCode == "VBC000000001" || memberCode == null || memberCode == "" || memberCode == undefined){
			mobileNo=$("#mobileNo input").val();
		}else{
			mobileNo=$("#mobileNo").html();
		}
		
		
		var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
		 if (!reg.test(mobileNo)) {
//			 $("#dzq2").hide();
		      BINOLSSPRM74.showErrorMessage("您输入的手机号码有误，请确认后再提交");
		      return false;
		 }
		
		var couponCode=$("#couponCode2").html();
		var password_input=$("#dzq2 #password").val();
		$("#coupon_table tr").each(function(){
			var $that=$(this).find("td:hidden");
			if($that.find("input[name='couponCode']").val() == couponCode){
				$that.find("input[name='password']").val(password_input);
			}
		});
        var coupon_check= new Array();
        var map={
              "couponCode":couponCode,
              "password":password_input,
              "appendFlag":1
       };
        coupon_check.push(map);
        //需要把原来页面上已经选择的优惠券信息传给后台
        $("#promotion_table #coupon_table tr").each(function(){
        	var couponType=$(this).find("td:hidden input[name='couponType']").val();
        	var checkFlag=$(this).find("input[type='checkbox']").is(":checked");
        	if(checkFlag){
        		var map2={
        	              "couponCode":couponCode,
        	              "password":password_input,
        	              "couponType":couponType
        	       };
        		 coupon_check.push(map2);
        	}
        });
        var couponCheck_json=JSON2.stringify(coupon_check);
        var main_json=$("#main_json").val();
		var shoppingcart_json=$("#shoppingcartOrder_json").val();
		var rule_json=BINOLSSPRM74.getAllRule();
		var couponAll_json=BINOLSSPRM74.getAllCoupon();
		var datasourceName=$("#datasourceName").val();
        var getCouponInfoUrl=$("#getCouponInfoUrl" ).attr("href" );
        var getComputeRuleUrl=$("#getComputeRuleUrl" ).attr("href" );
        var params="main_json="+main_json+"&shoppingcart_json=" +shoppingcart_json+"&rule_json=" +rule_json+"&coupon_json=" +couponCheck_json+"&couponAll_json="+couponAll_json+"&datasourceName=" +datasourceName+"&memberPhone="+mobileNo;
        BINOLSSPRM74.changeStateButton(0);
        $.ajax({
             url: getComputeRuleUrl,
             data: params,
             type: 'post',
             timeout: 3000,
             success: function(data) {
            	 	BINOLSSPRM74.changeStateButton(1);
                    if(data == null || data == "" || data == undefined || data == "ERROR" ){
                          return ;
                   }
                    var check_map = eval( "("+data+ ")");
                    var resultCode=check_map.resultCode;
                    var resultMsg=check_map.resultMsg;
                    if(resultCode != 0){
                    	$( "#dzq2 #password").val("");
                         BINOLSSPRM74.showErrorMessage(resultMsg);
                         BINOLSSPRM74.calculateDiscountTotal();
                          return false;
                   }
                    $( "#dzq2 .dzq_input").val("");
                    $( "#dzq2 input[name='couponTypeFlag']").val("0");
                    $( "#couponCode2").html("");
                    $( "#couponName2").html("");
                    $( "#dzq2").hide();
                    $("#memberPhone_param").val(mobileNo);
                    var content=check_map.content;
                    var computedCart=content.computedCart;
					var rule_list=content.rule_list;
					var rule_id=content.rule_id;
//					var rule_list_json=JSON2.stringify(rule_list);
					var computedCart_json=JSON2.stringify(computedCart);
					var html="<input id='"+rule_id+"cart' value='"+computedCart_json+"'/>";
//					html +="<input id='"+rule_id+"' value='"+rule_list_json+"'/>";
					$("#param").append(html);
                    
                    
                    
                    var index=$("#coupon_table tr").length+1;
                    var coupon_map=content.coupon_map;
                    var couponType=coupon_map.couponType;
                    var typeName=coupon_map.typeName;
                    var maincode=coupon_map.maincode;
                    var couponCode=coupon_map.couponCode;
                    var couponName=coupon_map.couponName;
                    var planDiscountPrice=BINOLSSPRM74.toDecimal2(coupon_map.planDiscountPrice);
                    var actualDiscountPrice=BINOLSSPRM74.toDecimal2(coupon_map.actualDiscountPrice);
                    var endTime=coupon_map.endTime;
                    var descriptionDtl=coupon_map.descriptionDtl;
                    var unicode=coupon_map.unicode;
                    var barcode=coupon_map.barcode;
                    var passwordFlag=coupon_map.passwordFlag;
                    var checkFlag=coupon_map.checkFlag;
                    var $ZGQArr=coupon_map.ZGQArr;
                    //校验是否存在多张同一类型的券
                    var flag=0;
                    $("#promotion_table #coupon_table tr").each(function(){
                    	var couponType_old=$(this).find("td:hidden input[name='couponType']").val();
                    	var checkFlag_old=$(this).find("input[type='checkbox']").is(":checked");
                    	if(couponType == couponType_old && checkFlag_old){
                    		flag=1;
                    		return false;
                    	}
                    });
                    if(flag == 1){
                    	BINOLSSPRM74.showErrorMessage("同一类型的券您只能使用一张");
        				return false;
                    }
                    
                	if(couponType == 3){
                		var flag1=0;
                        //检验添加为资格券类型时，有没有对应的活动可以勾选
                        var ZGQArr=$ZGQArr.split(",");
    					for(var i=0;i<ZGQArr.length;i++){
    						$("#promotion_table #rule_table tr").each(function(){
    							var maincode_old=$(this).find("td:hidden input[name='maincode']").val();
    							if(maincode_old == ZGQArr[i]){
    								flag1 = 1;
    							}
    						});
    					}
    					if(couponType == 3 && flag1 == 0){
    						BINOLSSPRM74.showErrorMessage("不存在资格券对应的活动");
    						return false;
    					}
                	}
                    var html  = '<tr onmouseover="BINOLSSPRM74.onmouseoverEvent(this);" onmouseout="BINOLSSPRM74.onmouseoutEvent(this);" onclick="BINOLSSPRM74.showDescriptionDtl(this);">';
                               html +=      '<td>'
                                                 + '<div class="checkbox" >'
                                                 + '<input onclick="BINOLSSPRM74.openCouponWindow(this);"name="checkbox-c'+index+ '" type="checkbox" checked="checked" class="regular-checkbox" id="checkbox-c'+index+'">';
                                                 + '<label for="checkbox-c'+index+'"><span></span></label></div>'
                                      +'</td>';
                               html +='<td>'+typeName+'</td>' ;
                               html +='<td>'+couponName+'('+couponCode+')</td>' ;
                               if(couponType == 5){//折扣券的情况
                            	   html +='<td>'+planDiscountPrice+'%</td>' ;
                               }else{
                            	   html +='<td>'+planDiscountPrice+'</td>' ;
                               }
                               html += '<td>'+endTime+ '</td>';
                               html +='<td>'+actualDiscountPrice+'</td>' ;
                               html += '<td style="display:none;">'
                                           + '<input name="couponCode" value="'+couponCode+ '"/>'
                                           + '<input name="couponName" value="'+couponName+ '"/>'
                                           + '<input name="maincode" value="'+maincode+'"/>'
                                           + '<input name="couponType" value="'+couponType+ '"/>'
                                           + '<input name="unicode" value="'+unicode+'"/>'
                                           + '<input name="barcode" value="'+barcode+'"/>'
                                           + '<input name="planDiscountPrice" value="'+planDiscountPrice+ '"/>'
                                           + '<input name="actualDiscountPrice" value="'+actualDiscountPrice+ '"/>'
                                           + '<input name="checkFlag" value="'+checkFlag+ '"/>'
                                           + '<input name="passwordFlag" value="'+passwordFlag+ '"/>'
                                           + '<input name="password" value="'+password_input+'"/>'
                                           + '<input name="descriptionDtl" value="'+descriptionDtl+ '"/>'
                                           + '<input name="ZGQArr" value="'+$ZGQArr+ '"/>'
                               + '</td>';
                         html += '</tr>';
                         $( "#coupon_table").append(html);
                         //更新活动状态
                        var rule_list=content.rule_list;
 						var coupon_list=content.coupon_list;
 						var rule_id=content.rule_id;
 						var rule_list_json=JSON2.stringify(content);
 						var html="<input id='"+rule_id+"' value='"+rule_list_json+"'/>";
 						$("#param").append(html);
 						//匹配优惠券
 						$(coupon_list).each(function(i){
 							var actualDiscountPrice=coupon_list[i].actualDiscountPrice;
 							var couponCode=coupon_list[i].couponCode;
 							var checkFlag=coupon_list[i].checkFlag;
 							var unicode=coupon_list[i].unicode;
 							var barcode=coupon_list[i].barcode;
 							$("#coupon_table tr").each(function(i){
 								var $this=$(this).find("td:last");
 								if($this.find("input[name='couponCode']").val() == couponCode ){
 									if(checkFlag == 1){
 										$this.find("input[name='unicode']").val(unicode);
 										$this.find("input[name='barcode']").val(barcode);
 										$this.find("input[name='actualDiscountPrice']").val(actualDiscountPrice);
 										$this.find("input[name='checkFlag']").val('1');
 										$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(actualDiscountPrice));
 									}else{
 										$this.find("input[name='unicode']").val("");
 										$this.find("input[name='barcode']").val("");
 										$this.find("input[name='checkFlag']").val('0');
 										$this.find("input[name='actualDiscountPrice']").val("");
 										$this.parents("tr").find("td:eq(5)").html("");
 									}
 								}
 							});
 						});
 						
 						//匹配规则
 						$(rule_list).each(function(i){
 							var flag=rule_list[i].flag;
 							var maincode_rule=rule_list[i].maincode;
 							var checkFlag=rule_list[i].checkFlag;
 							var actualDiscountPrice=rule_list[i].actualDiscountPrice;
 							var barcode=rule_list[i].barcode;
 							var unicode=rule_list[i].unicode;
 							$("#rule_table tr").each(function(){
 								var $this=$(this).find("td:last");
 								if($this.find("input[name='maincode']").val() == maincode_rule ){
 									if(checkFlag == 1){
 										$this.find("input[name='checkFlag']").attr("value",1);
 										$this.parents("tr").find("input[type='checkbox']").attr("checked","checked");
 										if(flag == 1){
 											var priceTotal=$("#"+maincode_rule+" input[name='priceTotal']").val();
 											var salePriceTotal=$("#"+maincode_rule+" input[name='salePriceTotal']").val();
 											$this.find("input[name='actualDiscountPrice']").attr("value",parseFloat(priceTotal-salePriceTotal));
 											$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(priceTotal-salePriceTotal));
 										}else{
 											$this.find("input[name='actualDiscountPrice']").attr("value",actualDiscountPrice);
 											$this.parents("tr").find("td:eq(5)").html(BINOLSSPRM74.toDecimal2(actualDiscountPrice));
 										}
 									}else{
 										$this.parents("tr").find("input[type='checkbox']").removeAttr("checked");
 										$this.find("input[name='checkFlag']").attr("value",0);
 										$this.find("input[name='actualDiscountPrice']").removeAttr("value");
 										$this.parents("tr").find("td:eq(5)").html("");
 									}
 									
 								}
 							});
 						});
                         BINOLSSPRM74.calculateDiscountTotal();
             },
             complete : function(XMLHttpRequest,status){
            	 var connectTimes=Number($("#connectTimes" ).val());
                 if(status== 'timeout' ){ //超时,status还有success,error等值的情况
	    　　　　　         if(connectTimes <=0){
	    　　　　　              $( "#connectTimes").val("1");
	    　　　　　              $("#dzq2 .dzq_input").val("");
	    　　　　　              $("#dzq2 input[name='couponTypeFlag']").val("0");
	    　　　　　              $("#couponCode2").html("");
	    　　　　　              $("#couponName2").html("");
	    　　　　　              $("#dzq2").hide();
	    　　　　　              BINOLSSPRM74.showErrorMessage($( "#connectNetErr").val());
	    　　　　　              BINOLSSPRM74.changeStateButton(1);
	    　　　　　        }else{
	    　　　　　               for(connectTimes;connectTimes>0;connectTimes--){
	    　　　　　                    $( "#connectTimes").val(0);
	    　　　　　                    BINOLSSPRM74.checkCouponTo();
	    　　　　　               }
	    　　　　　        }
	    　　　　	}
       　　　　}
       });   
	},
	"commitCoupon":function(){
		var couponCode=$("#couponCode1").html();
		var password_input=$("#dzq #password1").val();
		var checkFlag;
		$("#coupon_table tr").each(function(){
			var $that=$(this).find("td:hidden");
			if($that.find("input[name='couponCode']").val() == couponCode){
				$that.find("input[name='password']").val(password_input);
				checkFlag=$that.find("input[name='checkFlag']").val();
			}
		});
        var map={
              "couponCode":couponCode,
              "password":password_input,
              "checkFlag":checkFlag
       }
        BINOLSSPRM74.checkCoupon(map);
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
            s += '0';  
        }  
        return s;
	},
	"checkMobile":function(obj){
		var mobile=$(obj).val();
		var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
		 if (!reg.test(mobile)) {
		      BINOLSSPRM74.showErrorMessage("您输入的手机号码有误，请更正");
		 }
	},
	"openPointWindow":function(obj){
		var $this=$(obj);
		var $that=$this.parents("tr");
		if($this.is(":checked")){
			var mainname=$that.find("input[name='mainname']").val();
			var unitPoint=$that.find("input[name='unitPoint']").val();
			var maxPoint=$that.find("input[name='maxPoint']").val();
			$(obj).parents("tr").find("input[name='checkFlag']").val("1");
			$("#pointRule").html(mainname);
			$("#maxPoint").html(maxPoint);
			$("#unitPoint").html(unitPoint);
			$("#usePoint").val("");
			$("#usePoint").unbind();
			$("#jfdhConfirm").unbind();
			$("#jfdhCancel").unbind();
			$("#jfdhClose").unbind();
			
			$("#usePoint").bind("blur",function(){
				BINOLSSPRM74.checkPoint();
			});
			$("#jfdhConfirm").bind("click",function(){
				BINOLSSPRM74.jfdhConfirm(obj);
				return false;
			});
			$("#jfdhCancel").bind("click",function(){
				BINOLSSPRM74.jfdhCancel(obj);
				return false;
			});
			$("#jfdhClose").bind("click",function(){
				BINOLSSPRM74.jfdhCancel(obj);
				return false;
			});
			$("#jfdh").show();
		}else{
			BINOLSSPRM74.changeCheck(obj);
		}
	},
	"checkPoint":function(){
		var usePoint=$("#usePoint").val();
		var unitPoint=parseFloat($("#unitPoint").html());
		var maxPoint=parseFloat($("#maxPoint").html());
		if(usePoint == null || usePoint == "" || usePoint == undefined ){
			BINOLSSPRM74.showErrorMessage("请输入您需要兑换的积分");
			return "您输入您需要兑换的积分";
		}else if(isNaN(usePoint)){
			BINOLSSPRM74.showErrorMessage("请您输入数字");
			$("#usePoint").val("");
			return "请您输入数字";
		}else if(usePoint > maxPoint){
			BINOLSSPRM74.showErrorMessage("您输入的积分不能大于当前活动设置最大积分数");
			$("#usePoint").val("");
			return "您输入的积分不能大于您所拥有的最大积分数";
		}else if(parseFloat(usePoint) < unitPoint){
			BINOLSSPRM74.showErrorMessage("您输入的积分不能小于当前活动设置最小积分数");
			$("#usePoint").val("");
			return "您输入的积分不能小于该活动的最小积分数";
		}else if(parseFloat(usePoint) % unitPoint != 0){
			BINOLSSPRM74.showErrorMessage("请您输入单位积分的整数倍");
			$("#usePoint").val("");
			return "请您输入单位积分的整数倍";
		}else{
			$("#usePoint_main").val(usePoint);
			return 0;
		}
	},
	"jfdhConfirm":function(obj){
		var result=BINOLSSPRM74.checkPoint();
		if(result != 0){
			BINOLSSPRM74.showErrorMessage(result);
			return false;
		}else{
			BINOLSSPRM74.changeCheck(obj);
			$("#jfdh").hide();
		}
	},
	"jfdhCancel":function(obj){
		$(obj).removeAttr("checked");
		$(obj).parents("tr").find("input[name='checkFlag']").val("0");
		$("#jfdh").hide();
	}
};

var BINOLSSPRM74 =  new BINOLSSPRM74();

$(document).ready(function() {
	BINOLSSPRM74.showResultMessage();
	var height=document.body.clientHeight;
	var width=document.body.clientWidth;
	$(".tablebox").css("height",height*0.55);
	$(".tablebox.gift").css("height",height*0.3);
	$(".leftbox").css("height",height*0.74);
	$(".rightbox").css("height",height*0.74);
	$(".bottom_item.promotion").css("font-size",width*0.015);
	$(".bottom_left").css("width",width*0.62);
	$(".bottom_right").css("width",width*0.3);
	$("#inputCouponCode").css("width",width*0.2);
	$(".bottom_right .jh_btn2").css("font-size",width*0.01);
	$(".bottom_right .jh_btn2").css("width",width*0.09);
});
