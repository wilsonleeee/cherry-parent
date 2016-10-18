var WPCOM_dialogBody ="";
var BINOLWPSAL08_1_GLOBAL = function() {

};

BINOLWPSAL08_1_GLOBAL.prototype = {
		"cancel":function(){
			BINOLWPSAL02.removePromotionList();
			closeCherryDialog('dialogInit',WPCOM_dialogBody);
			$("#activityProductDiv").find("div").remove();
		},
		"confirm":function(){
			if($("#activityTbody tr").length>0){
				var b=false;
				$.each($("#activityTbody tr"),function(){
					var flag = $(this).find("#flag").val();
					var maincode = $(this).find("#maincode").val();
					var mainname = $(this).find("#mainname").val();
					var ipt = $(this).first("td").find("input");
					if(ipt.is(":checked") && flag=="1"){
						var rmaincode = $("#activityProductDiv").find("#"+maincode);
						if(rmaincode.length<1 || rmaincode == undefined || rmaincode == null || rmaincode == ""){
							b=true;
							BINOLWPSAL02.showMessageDialog({
								message:"请选择产品再参加【"+mainname+"】活动!", 
								type:"MESSAGE"
							});
							return false;
						}
					}
				});
				if(b){
					return;
				}else {
					BINOLWPSAL02.collect();
				}
			}
		},
		"appendNewRow":function(){
			$('#inputSaledetail').val("");
			var saleDetailList=$('#saleDetailList').val();
			var baCode=$('#baCode').val();
			var memberCode=$('#spanMemberCode').text();
			var totalDiscountRate=$('#totalDiscountRate').val();
			var getMatchRuleUrl=$('#getMatchRuleUrl').attr("href");
			var memberLevel=$('#memberLevel').val();
			var params="saleDetailList="+saleDetailList+"&baCode="+baCode+"&memberCode="+memberCode+"&memberLevel="+memberLevel;
			cherryAjaxRequest({
				url: getMatchRuleUrl,
				param: params,
				callback: function(data) {
					if(data == null || data == "" || data == undefined || data == "ERROR"){
						BINOLWPSAL08_1.rule_alertMsg('2');
						return false;
					}
					var param_map = eval("("+data+")");
					var outresult=param_map.outresult;
					var promotionInfo_new=param_map.promotionInfo_new;
					var promotion_all_new=param_map.promotion_all_new;
					var promotion_all_old=param_map.promotion_all_old;
					var inputSaledetail=param_map.inputSaledetail;
					//调用接口状态的编码
					var num_back=param_map.num_back;
					//处理返回编码为不成功的情况
					if(num_back != '1'){
						BINOLWPSAL08_1.rule_alertMsg(num_back);
						return false;
					}
					//清空之前的列表
					var last_promotionInfo=$('#promotionInfo').val();
					if(last_promotionInfo != ""){
						var last_promotionInfo = eval('(' + last_promotionInfo + ')');
						$.each(last_promotionInfo,function(i){
							// 删除指定的促销活动行
							BINOLWPSAL02.deletePromotionRow(last_promotionInfo[i].mainCode);
						});
					}
					//写入新的detail数据
					$('#promotionInfo').val(JSON.stringify(promotionInfo_new));	
					$('#outresult').val(JSON.stringify(outresult));	
					$('#inputSaledetail').val(JSON.stringify(inputSaledetail));	
					//生成智能促销菜单
					$.each(outresult,function(i){
						var flag=outresult[i].flag;//是否存在需要交互的产品，0表示不需要交互，1表示需要交互
						var times=outresult[i].times;//活动数量
						var matchtimes=outresult[i].matchtimes;//最大匹配数量
						var maincode=outresult[i].maincode;//活动主码
						var mainname=outresult[i].mainname;//主活动名称
						var ismust=outresult[i].ismust;//可选不可选  0：可选；1：不可选
						var rulecondtype=outresult[i].rulecondtype;//促销条件类型 1：整单类，2：非整单类
						var subcampaignvalid=outresult[i].subcampaignvalid;//校验方式[0(无需校验) 1（本地校验）2（在线校验）]
						var level=outresult[i].level;//规则等级
						var checkflag=outresult[i].checkflag;//是否选中，0是未选中，1是选中
						var activitycode=outresult[i].activitycode;//活动code
						var ruleamount=outresult[i].ruleamount;//规则赠送的总金额
						var rulebcj=outresult[i].rulebcj;//默认值为0 ，1表示可以购买超出奖励金额的产品
						var originalBrand=outresult[i].originalBrand;//产品自身品牌,（Code类别：1299）
						var activityType=outresult[i].activityType;//活动类型
					html="<tr><td><input type='checkbox' onclick='BINOLWPSAL08_1.addOrDelLine(this)'></td> " ;
					if(rulecondtype == 1){
						html+="<td>整单类型促销</td>";
					}else{
						html+="<td>非整单类型促销</td>";
					}
					html+="<td>"
						if(flag=="1"){
							html+="<a href='#' style='color:blue;' onclick='BINOLWPSAL08_1.addMainCode(this);return false;'>"+mainname+"</a>";
						}else {
							html+=mainname;
						}
					html+="</td>";
					html+="<td style='display:none'>";
					html+="<input type='hidden' value='"+flag+"'id='flag' name='flag'> ";
					html+="<input type='hidden' value='"+times+"' id='times' name='times'>";
					html+="<input type='hidden' value='"+matchtimes+"' id='matchtimes' name='matchtimes'>";
					html+="<input type='hidden' value='"+maincode+"' id='maincode' name='maincode'>";
					html+="<input type='hidden' value='"+mainname+"' id='mainname' name='mainname'>";
					html+="<input type='hidden' value='"+ismust+"' id='ismust' name='ismust'>";
					html+="<input type='hidden' value='"+rulecondtype+"' id='rulecondtype' name='rulecondtype'>";
					html+="<input type='hidden' value='"+subcampaignvalid+"' id='subcampaignvalid' name='subcampaignvalid'>";
					html+="<input type='hidden' value='"+level+"' id='level' name='level'>";
					html+="<input type='hidden' value='"+checkflag+"' id='checkflag' name='checkflag'>";
					html+="<input type='hidden' value='"+activitycode+"' id='activitycode' name='activitycode'>";
					html+="<input type='hidden' value='"+ruleamount+"' id='ruleamount' name='ruleamount'>";
					html+="<input type='hidden' value='"+rulebcj+"' id='rulebcj' name='rulebcj'>";
					html+="<input type='hidden' value='"+originalBrand+"' id='originalBrand' name='originalBrand'>";
					html+="<input type='hidden' value='"+activityType+"' id='activityType' name='activityType'>";
					html+="</td></tr>";
					$('#activityTbody').append(html);
					if(ismust == 1){//不可选
						$("#activityTbody tr:last td:first input:first").attr("disabled","disabled");
//						$("#activityTbody tr:last").attr("style","background:#f0f0f0");	
						}
					if(checkflag == 1){//表示选中状态
						$("#activityTbody tr:last td:first input:first").attr("checked","checked");
						} 
					if (checkflag == 2){//表示推荐
						$("#activityTbody tr:last td:first input:first").attr("disabled","disabled");
//						$("#activityTbody tr:last").attr("style","background:#f0f0f0");
						$("#activityTbody tr:last").attr("class","even");
						}
					});
					//经过计算过的数据，替换原来购物车的数据，新增逻辑。当mainCode为空的时候再替换
					if(promotion_all_old != undefined && promotion_all_old != ""){
						//迭代选定模式，根据有没有maincode来确定
						var main_flag=0;
						$.each(promotion_all_old,function(i){
							var promotionProduct=promotion_all_old[i].promotionProduct;
							var mainCode=promotionProduct.mainCode;
							
							if(mainCode == null || mainCode == 'null' || mainCode == undefined || mainCode == ""){
								main_flag=1;
							}
						});
						if(main_flag == 1){//有maincode等于空的情况
							$.each(promotion_all_old,function(i){
								var promotionProduct_a=promotion_all_old[i].promotionProduct;
								var unitCode_a=promotionProduct_a.unitCode;
								var quantity_a=promotionProduct_a.quantity;
								var mainCode_a=promotionProduct_a.mainCode;
								var price_a=Number(promotionProduct_a.price).toFixed(2);
								var discount_a=promotionProduct_a.discount;
								if(mainCode_a == null || mainCode_a == 'null' || mainCode_a == undefined || mainCode_a == ""){
									$("#databody tr").each(function(){
										//对应到指定的购物车中的产品
										if($(this).find("input[name='unitCode']").val() == unitCode_a && $(this).find("input[name='isPromotion']").val() != "1"){
										$(this).find("#realPriceArr").val(price_a);
										$(this).find("#discountRateArr").val(discount_a);
										$(this).find("#quantityuArr").val(quantity_a);
										$(this).find("#tdAmount").html(Number(price_a*quantity_a).toFixed(2));
										$last=$(this).find("td:last");
										$last.find("#payAmount").val(Number(price_a*quantity_a).toFixed(2));
										//其中有替换过的数据标识，之后用来删除多余的数据
										var promotionDelFlagHtml="<input id='promotionDelFlag' type='hidden' value='1' name='promotionDelFlag'>";
										$last.append(promotionDelFlagHtml);
										}
									});
								}else{
									BINOLWPSAL08_1.addProduct(promotionProduct_a);
								}
							});
							$("#databody tr").each(function(){
								//对应到指定的购物车中的产品
								$last=$(this).find("td:last");
								var flag2=$last.find("input[name='promotionDelFlag']").val();
								var activityTypeCode=$last.find("input[name='activityTypeCode']").val();
								//保留一开始加入的活动明细
								if(flag2 != 1 && (activityTypeCode == null || activityTypeCode == 'null' || activityTypeCode == undefined || activityTypeCode == "")){
									$(this).remove();
								}
							});
							
						}else{//没有maincode等于空的情况
							//清空原始购物车中的产品
							$.each(promotion_all_old,function(i){
								var promotionProduct_b=promotion_all_old[i].promotionProduct;
								var unitCode_b=promotionProduct_b.unitCode;
								var quantity_b=promotionProduct_b.quantity;
								var mainCode_b=promotionProduct_b.mainCode;
								var price_b=Number(promotionProduct_b.price).toFixed(2);
								var discount_b=promotionProduct_b.discount;
								$("#databody tr").each(function(){
									//对应到指定的购物车中的产品,加入右边列表活动除外
									if($(this).find("input[name='unitCode']").val() == unitCode_b){
										$(this).remove();
									}
								});
							});
							//添加产品（所有参加活动的）
							$.each(promotion_all_old,function(i){
								var promotionProduct_b=promotion_all_old[i].promotionProduct;
								BINOLWPSAL08_1.addProduct(promotionProduct_b);
							});
						}
						BINOLWPSAL02.calcuTatol();
					}
//					根据主活动码对产品和活动信息进行分组
					if(promotion_all_new != undefined && promotion_all_new != ""){
							BINOLWPSAL02.deleteEmptyRow();
							$.each(promotion_all_new,function(i){
								BINOLWPSAL02.appendPromotionInfo(promotion_all_new[i].promotionInfo);
								BINOLWPSAL02.appendPromotionProduct(promotion_all_new[i].promotionProduct);
							});
							BINOLWPSAL02.addNewLine();
							BINOLWPSAL02.setBillClassify();
							BINOLWPSAL02.changeOddEvenColor();
							BINOLWPSAL02.calcuTatol();
						}
					$("#btnConfirm").focus();
				}
			});
		},
		"addOrDelLine":function(obj){
				//清空之前的列表
				var last_promotionInfo=$('#promotionInfo').val();
				if(last_promotionInfo != ""){
					var last_promotionInfo = eval('(' + last_promotionInfo + ')');
					$.each(last_promotionInfo,function(i){
						// 删除指定的促销活动行
						BINOLWPSAL02.deletePromotionRow(last_promotionInfo[i].mainCode);
					});
				}
				//清空智能促销列表
				$('#activityTbody').empty();
				var checkflag_back;
				if($(obj).is(':checked')){
					checkflag_back=1;
				}else{
					checkflag_back=0;
				}
				
				var activitycode_check=$(obj).parent().parent().find("#activitycode").val();
				var maincode=$(obj).parent().parent().find("#maincode").val();
				var getComputeRuleUrl=$('#getComputeRuleUrl').attr("href");
				var outresult=$('#outresult').val();
				var inputSaledetail_str=$('#inputSaledetail').val();
				var baCode=$('#baCode').val();
				var memberCode=$('#spanMemberCode').text();
				var totalDiscountRate=$('#totalDiscountRate').val();
				var memberLevel=$("#memberLevel").val();
				var outproduct_back = "";
				if($("#activityProductDiv").find("#"+maincode).find("input").val()!=undefined){
					outproduct_back = $("#activityProductDiv").find("#"+maincode).find("input").val();
				}
				var params="outresult_back="+outresult+"&checkflag="+
				checkflag_back+"&maincode="+maincode+"&activitycode_check="+activitycode_check+"&inputdetail_back="+inputSaledetail_str+
				"&baCode="+baCode+"&memberCode="+memberCode+"&memberLevel="+memberLevel+"&outproduct_back="+outproduct_back;
				cherryAjaxRequest({
					url: getComputeRuleUrl,
					param: params,
					callback: function(data) {
						if(data == null || data == "" || data == undefined || data == "ERROR"){
							BINOLWPSAL08_1.rule_alertMsg('2');
							return false;
						}
						var param_map = eval("("+data+")");
						var outresult=param_map.outresult;
						var promotionInfo_new=param_map.promotionInfo_new;
						var promotion_all_new=param_map.promotion_all_new;
//						var promotionInfo_old=param_map.promotionInfo_old;
						var promotion_all_old=param_map.promotion_all_old;
						var num_back=param_map.num_back;
						//处理返回编码为不成功的情况
						if(num_back != '1'){
							BINOLWPSAL08_1.rule_alertMsg(num_back);
							return false;
						}
						//写入新的数据
						$('#promotionInfo').val(JSON.stringify(promotionInfo_new));	
						$('#outresult').val(JSON.stringify(outresult));	
						//生成智能促销菜单
						$.each(outresult,function(i){
							var flag=outresult[i].flag;//是否存在需要交互的产品，0表示不需要交互，1表示需要交互
							var times=outresult[i].times;//活动数量
							var matchtimes=outresult[i].matchtimes;//最大匹配数量
							var maincode=outresult[i].maincode;//活动主码
							var mainname=outresult[i].mainname;//主活动名称
							var ismust=outresult[i].ismust;//可选不可选  0：可选；1：不可选
							var rulecondtype=outresult[i].rulecondtype;//促销条件类型 1：整单类，2：非整单类
							var subcampaignvalid=outresult[i].subcampaignvalid;//校验方式[0(无需校验) 1（本地校验）2（在线校验）]
							var level=outresult[i].level;//规则等级
							var checkflag=outresult[i].checkflag;//是否选中，0是未选中，1是选中
							var activitycode=outresult[i].activitycode;//活动code
							var ruleamount=outresult[i].ruleamount;//规则赠送的总金额
							var rulebcj=outresult[i].rulebcj;//默认值为0 ，1表示可以购买超出奖励金额的产品
							var originalBrand=outresult[i].originalBrand;//产品自身品牌,（Code类别：1299）
							var activityType=outresult[i].activityType;//活动类型
						html="<tr><td><input type='checkbox' onclick='BINOLWPSAL08_1.addOrDelLine(this)'></td> " ;
						if(rulecondtype == 1){
							html+="<td>整单类型促销</td>";
						}else{
							html+="<td>非整单类型促销</td>";
						}
						html+="<td>"
							if(flag=="1"){
								html+="<a href='#' style='color:blue;' onclick='BINOLWPSAL08_1.addMainCode(this);return false;'>"+mainname+"</a>";
							}else {
								html+=mainname;
							}
						html+="</td>";
						html+="<td style='display:none'>";
						html+="<input type='hidden' value='"+flag+"'id='flag' name='flag'> ";
						html+="<input type='hidden' value='"+times+"' id='times' name='times'>";
						html+="<input type='hidden' value='"+matchtimes+"' id='matchtimes' name='matchtimes'>";
						html+="<input type='hidden' value='"+maincode+"' id='maincode' name='maincode'>";
						html+="<input type='hidden' value='"+mainname+"' id='mainname' name='mainname'>";
						html+="<input type='hidden' value='"+ismust+"' id='ismust' name='ismust'>";
						html+="<input type='hidden' value='"+rulecondtype+"' id='rulecondtype' name='rulecondtype'>";
						html+="<input type='hidden' value='"+subcampaignvalid+"' id='subcampaignvalid' name='subcampaignvalid'>";
						html+="<input type='hidden' value='"+level+"' id='level' name='level'>";
						html+="<input type='hidden' value='"+checkflag+"' id='checkflag' name='checkflag'>";
						html+="<input type='hidden' value='"+activitycode+"' id='activitycode' name='activitycode'>";
						html+="<input type='hidden' value='"+ruleamount+"' id='ruleamount' name='ruleamount'>";
						html+="<input type='hidden' value='"+rulebcj+"' id='rulebcj' name='rulebcj'>";
						html+="<input type='hidden' value='"+originalBrand+"' id='originalBrand' name='originalBrand'>";
						html+="<input type='hidden' value='"+activityType+"' id='activityType' name='activityType'>";
						html+="</td></tr>";
						$('#activityTbody').append(html);
						if(ismust == 1){//不可选
							$("#activityTbody tr:last td:first input:first").attr("disabled","disabled");
							}
						if(checkflag == 1){//表示选中状态
							$("#activityTbody tr:last td:first input:first").attr("checked","checked");
							} 
						if (checkflag == 2){//表示推荐
							$("#activityTbody tr:last td:first input:first").attr("disabled","disabled");
							$("#activityTbody tr:last").attr("class","even");
							}
						});
						//经过计算过的数据，替换原来购物车的数据，新增逻辑。当mainCode为空的时候再替换
						//返回原始购物车，在原始购物车基础上进行替换
						BINOLWPSAL02.removePromotionList();
						if(promotion_all_old != undefined && promotion_all_old != ""){
							//迭代选定模式，根据有没有maincode来确定
							var main_flag=0;
							$.each(promotion_all_old,function(i){
								var promotionProduct=promotion_all_old[i].promotionProduct;
								var mainCode=promotionProduct.mainCode;
								
								if(mainCode == null || mainCode == 'null' || mainCode == undefined || mainCode == ""){
									main_flag=1;
								}
							});
							if(main_flag == 1){//有maincode等于空的情况
								$.each(promotion_all_old,function(i){
									var promotionProduct_a=promotion_all_old[i].promotionProduct;
									var unitCode_a=promotionProduct_a.unitCode;
									var quantity_a=promotionProduct_a.quantity;
									var mainCode_a=promotionProduct_a.mainCode;
									var price_a=Number(promotionProduct_a.price).toFixed(2);
									var discount_a=promotionProduct_a.discount;
									if(mainCode_a == null || mainCode_a == 'null' || mainCode_a == undefined || mainCode_a == ""){
										$("#databody tr").each(function(){
											//对应到指定的购物车中的产品
											if($(this).find("input[name='unitCode']").val() == unitCode_a && $(this).find("input[name='isPromotion']").val() != "1"){
											$(this).find("#realPriceArr").val(price_a);
											$(this).find("#discountRateArr").val(discount_a);
											$(this).find("#quantityuArr").val(quantity_a);
											$(this).find("#tdAmount").html(Number(price_a*quantity_a).toFixed(2));
											$last=$(this).find("td:last");
											$last.find("#payAmount").val(Number(price_a*quantity_a).toFixed(2));
											//其中有替换过的数据标识，之后用来删除多余的数据
											var promotionDelFlagHtml="<input id='promotionDelFlag' type='hidden' value='1' name='promotionDelFlag'>";
											$last.append(promotionDelFlagHtml);
											}
										});
									}else{
										BINOLWPSAL08_1.addProduct(promotionProduct_a);
									}
								});
									$("#databody tr").each(function(){
										//对应到指定的购物车中的产品
										$last=$(this).find("td:last");
										var flag2=$last.find("input[name='promotionDelFlag']").val();
										var activityTypeCode=$last.find("input[name='activityTypeCode']").val();
										//保留一开始加入的活动明细
										if(flag2 != 1 && (activityTypeCode == null || activityTypeCode == 'null' || activityTypeCode == undefined || activityTypeCode == "")){
											$(this).remove();
										}
									});
							}else{//没有maincode等于空的情况
								//清空原始购物车中的产品
								$.each(promotion_all_old,function(i){
									var promotionProduct_b=promotion_all_old[i].promotionProduct;
									var unitCode_b=promotionProduct_b.unitCode;
									var quantity_b=promotionProduct_b.quantity;
									var mainCode_b=promotionProduct_b.mainCode;
									var price_b=Number(promotionProduct_b.price).toFixed(2);
									var discount_b=promotionProduct_b.discount;
									$("#databody tr").each(function(){
										//对应到指定的购物车中的产品,加入右边列表活动除外
										if($(this).find("input[name='unitCode']").val() == unitCode_b){
											$(this).remove();
										}
									});
								});
								//添加产品（所有参加活动的）
								$.each(promotion_all_old,function(i){
									var promotionProduct_b=promotion_all_old[i].promotionProduct;
									BINOLWPSAL08_1.addProduct(promotionProduct_b);
								});
							}
							BINOLWPSAL02.calcuTatol();
						}
						//根据主活动码对产品和活动信息进行分组
						if(promotion_all_new != undefined && promotion_all_new != ""){
								BINOLWPSAL02.deleteEmptyRow();
								$.each(promotion_all_new,function(i){
									BINOLWPSAL02.appendPromotionInfo(promotion_all_new[i].promotionInfo);
									BINOLWPSAL02.appendPromotionProduct(promotion_all_new[i].promotionProduct);
								});
								BINOLWPSAL02.addNewLine();
								BINOLWPSAL02.setBillClassify();
								BINOLWPSAL02.changeOddEvenColor();
								BINOLWPSAL02.calcuTatol();
							}
						
					}
			});
	},
	"rule_alertMsg":function(obj){
		if(obj != '1'){
			var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 500,
					height: 300,
					title: "智能促销消息提示",
					closeEvent:function(){
						BINOLWPSAL08_1.cancel();
						/*// 还原按钮样式
						$("#btnCollect").attr("class","btn_top");
						// 最后一个文本框获得焦点
						BINOLWPSAL02.lastInputSelect();
						// 可见文本框回车事件解绑
						$("#collectPageDiv").find("input:text:visible").unbind();
						// 关闭弹出窗口
						removeDialog("#dialogInit");
						// 解除退货和补登按钮禁用
						$("#btnReturnsGoods").removeAttr("disabled");
						$("#btnReturnsGoods").attr("class","btn_top");
						$("#btnAddHistoryBill").removeAttr("disabled");
						$("#btnAddHistoryBill").attr("class","btn_top");
						$("#btnCollect").removeAttr("disabled");
						$("#btnCollect").attr("class","btn_top");
						// 解除清空购物车禁用
						$("#btnEmptyShoppingCart").removeAttr("disabled");
						$("#btnEmptyShoppingCart").attr("class","btn_top");*/
					}
			};
			openDialog(dialogSetting);
			var ruleErrorUrl=$('#ruleErrorUrl').attr("href");
			cherryAjaxRequest({
				url: ruleErrorUrl,
				callback: function(data) {
					$("#dialogInit").html(data);
				}
			});
		}
	}
	,
	"addProduct":function(promotionProduct){
    	// 处理子活动编码
    	var mainCode = promotionProduct.mainCode;
    	if(mainCode == null || mainCode == 'null' || mainCode == undefined){
    		mainCode = "";
    	}
    	// 处理厂商编码
    	var unitCode = promotionProduct.unitCode;
    	if(unitCode == null || unitCode == 'null' || unitCode == undefined){
    		unitCode = "";
    	}
    	// 处理产品条码
    	var barCode = promotionProduct.barCode;
    	if(barCode == null || barCode == 'null' || barCode == undefined){
    		barCode = "";
    	}
    	// 处理产品名称
    	var productName = promotionProduct.productName;
    	if(productName == null || productName == 'null' || productName == undefined){
    		productName = "";
    	}
    	var isStock = promotionProduct.isStock;
    	if(isStock == null || isStock == 'null' || isStock == undefined){
    		isStock = "";
    	}
    	// 处理单价
    	var price = promotionProduct.price;
    	if((price == null || price == 'null' || price == undefined || price == "") && price != 0.00){
    		price = "";
    	}else{
    		price = Number(price).toFixed(2);
    	}
    	// 处理输入数量
    	var quantity = promotionProduct.quantity;
    	if(quantity == null || quantity == 'null' || quantity == undefined || quantity == ""){
    		quantity = "";
    	}else{
    		quantity = Number(quantity).toFixed(0);
    	}
    	
    	// 处理折扣率数据
    	var discount = promotionProduct.discount;
    	if((discount == null || discount == 'null' || discount == undefined || discount == "") && discount != 0.00){
    		discount = "";
    	}else{
    		discount = Number(discount).toFixed(2);
    	}
    	// 处理实收金额数据
    	var amount = Number(price)*Number(quantity);
    	if((amount == null || amount == 'null' || amount == undefined || amount == "") && amount != 0.00){
    		amount = "";
    	}else{
    		amount = Number(amount).toFixed(2);
    	}
    	// 处理产品厂商ID数据
    	var productVendorId = promotionProduct.productVendorId;
    	if(productVendorId == null || productVendorId == 'null' || productVendorId == undefined){
    		productVendorId = "";
    	}
    	
    	var proType = promotionProduct.proType;
    	if(proType == null || proType == 'null' || proType == undefined){
    		proType = "";
    	}
    	
    	var isDiscountFlag=$('#isDiscountFlag').val();
		if(isDiscountFlag == null || isDiscountFlag == 'null' || isDiscountFlag == undefined || isDiscountFlag == ""){
			isDiscountFlag = 'Y';
		}
    	var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);

		// 购物车正常销售商品记录
		var rowIndex = parseInt($("#rowCode").val())+1;
		$("#rowCode").val(rowIndex);
		// 非折扣记录的情况下追加行
		var html = '<tr id="dataRow'+nextIndex+'">';
		html += '<td id="indexNo">'+rowIndex+'</td>';
		html += '<td><span id="spanUnitCode">'+ unitCode +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value="'+ unitCode +'"/><input id="unitCode" name="unitCode" type="hidden" value="'+ unitCode +'"/></td>';
		html += '<td><span id="spanBarCode">'+ barCode +'</span><input id="barCode" name="barCode" type="hidden" value="'+ barCode +'"/></td>';
		html += '<td><span id="spanProductName">'+ productName +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ productName +'"/></td>';
		html += '<td><span id="spanPrice">'+ price +'</span><input id="pricePay" name="pricePay" type="hidden" value="'+ price +'"/></td>';
		html += '<td><span id="spanMemberPrice"></span><input id="memberPrice" name="memberPrice" type="hidden" value=""/></td>';
		if($("#isPlatinumPrice").val()=="Y"){
			html += '<td><span id="spanPlatinumPrice"></span><input id="platinumPrice" name="platinumPrice" type="hidden" value=""/></td>';
		}
		html += '<td class="center"><button id="btnLessen" class="wp_minus" onclick="BINOLWPSAL02.lessenQuantity(this);return false;"></button>'
				+'<input id="quantityuArr" name="quantityuArr" type="text" class="text" style="width:50px;margin:.2em 0;" value="'+quantity+'" maxlength="4" onchange="BINOLWPSAL02.changeQuantity(this)" onkeyup="BINOLWPSAL02.changeQuantity(this)"/>'
				+'<button id="btnAdd" class="wp_plus" onclick="BINOLWPSAL02.addQuantity(this);return false;"></button></td>';
		if("N" == isDiscountFlag){
			//折扣率不可用时，改成一般显示
			html += '<td><span>'+ discount +'%</span><input class="hide" name="discountRateArr" id="discountRateArr" value=""/></td>';
		}else{
			html += '<td><input id="discountRateArr" name="discountRateArr" value="'+ discount +'" class="text" style="width:40px;" maxlength="6" onchange="BINOLWPSAL02.changeDiscountRate(this)" onkeyup="BINOLWPSAL02.keyUpChangeDiscountRate(this)"/>%</td>';
		}
		html += '<td><input id="realPriceArr" name="realPriceArr" value="'+price+'" class="text" style="width:60px;" maxlength="10" onchange="BINOLWPSAL02.changeRealPrice(this)" onkeyup="BINOLWPSAL02.keyUpchangeRealPrice(this)"/></td>';
		html += '<td id="tdAmount">'+ amount +'</td>';
		html += '<td><span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL02.deleteRow(this);return false;"></button></span></td>';
		html += '<td style="display:none">'
		+'<input type="hidden" id="orderId" name="orderId" value=""/>'
		+'<input type="hidden" id="couponCode" name="couponCode" value=""/>'
		+'<input type="hidden" id="isStock" name="isStock" value="'+isStock+'"/>'
		+'<input type="hidden" id="mainCode" name="mainCode" value=""/>'
		+'<input type="hidden" id="counterActCode" name="counterActCode" value=""/>'
		+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value=""/>'
		+'<input type="hidden" id="activitySign" name="activitySign" value=""/>'
        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ productVendorId +'"/>'
        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+ productVendorId +'"/>'
        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ price +'"/>'
        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value=""/>'
        +'<input type="hidden" id="proType" name="proType" value="'+ proType +'"/>'
        +'<input type="hidden" id="exPoint" name="exPoint" value=""/>'
        +'<input type="hidden" id="stockQuantity" name="stockQuantity" value=""/>'
        +'<input type="hidden" id="isPromotion" name="isPromotion" value="1"/>'
        +'<input type="hidden" id="promotionDelFlag"  value="1" name="promotionDelFlag">'
        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
		$("#databody").append(html);
	
		// 设置记录行样式
		var saleType = $('#saleType').val();
		if(saleType == "SR"){
			$('#txtSaleType').text($('#saleTypeSR').val());
			$("#saleTypeTotalQuantity").show();
			$("#saleTypeTotalAmount").show();
			$("#saleTypeQuantity").show();
			$("#saleTypeAmount").show();
			//检查有无行，若有则更改样式
			if($('#databody >tr').length > 0){
				$.each($('#databody >tr'), function(i){
					$(this).attr("class","red");
				});
			}
		}else{
			$('#txtSaleType').text($('#saleTypeNS').val());
			$("#saleTypeTotalQuantity").hide();
			$("#saleTypeTotalAmount").hide();
			$("#saleTypeQuantity").hide();
			$("#saleTypeAmount").hide();
			// 重新设置间隔行样式
			BINOLWPSAL02.changeOddEvenColor();
		}
		
		var rowId = "dataRow"+nextIndex;
		BINOLWPSAL02.bindInput(rowId);
    
	},
	"addMainCode":function(t){
		var $tr = $(t).parents("tr");
		if($tr.find("input:checkbox").is(":checked")){
			BINOLWPSAL08_1.openChkPrtDialogInit(t);
			var maincode = $(t).parents("tr").find("#maincode").val();
			var productBrand = $(t).parents("tr").find("#originalBrand").val();
			$("#prtSaveDiv #maincode2").val(maincode);
			$("#prtSaveDiv #productBrand").val(productBrand);
			if($("#activityProductDiv").children("div").length>0){
				$.each($("#activityProductDiv").children("div"),function(){
					var divMaincode = $(this).attr("id");
					if(maincode==divMaincode){
						var htmlTR = $(this).children("div").html();
						$("#newDatabody").append(htmlTR);
						if($("#newDatabody tr").length>0){
							$.each($("#newDatabody tr"),function(){
								var quantity_title = $(this).find("#trQuantity").attr("title");
								$(this).find("#trQuantity").val(quantity_title);
							})
						}
						return false;
					}
				});
			}
		}else {
			BINOLWPSAL02.showMessageDialog({
				message:"请先选中该活动再选择产品", 
				type:"MESSAGE"
			});
		}
	},
    /** ------------以下为添加智能促销的产品------------------------------------------           */
	"openChkPrtDialogInit" : function(t){
		var maxAmount = $(t).parents("tr").find("#ruleamount").val();
		var $addPrtTable = $("#addPrtTable").clone();
		$addPrtTable.find("#productdatabody").attr('id','newDatabody');
		var text = '<div id="prtSaveDiv">'  + $addPrtTable.html() + '</div>';
		var title = "";
		title = '添加产品';
		var dialogSetting = {
				dialogInit: "#dialogChkPrtInitDIV",
				text: text,
				width: 	1050,
				height: 500,
				title: 	title,
				confirm: '添加',
				confirmEvent: function(){
					BINOLWPSAL08_1.addActivityProduct();
				}
		};
		openDialog(dialogSetting);
		$("#prtSaveDiv").find("#addProductId").attr("id","addProductInput");
		// 绑定产品下拉列表
		BINOLWPSAL08_1.setProductBinding1(t);
		$("#addProductInput").focus();
		$("#prtSaveDiv").find("#productMemo").text("(您可以选择的产品最大优惠金额为"+maxAmount+"元)");
	},
	"addProductLine":function(info){
		var b = true;
		if($("#newDatabody tr").length>0){
			$.each($("#newDatabody tr"),function(){
				var unitCode = $(this).find("#tdUnitCode").text();
				if(unitCode==info.unitCode){
					var quantity = $(this).find("#trQuantity").val();
					if(isNaN(quantity)){
						quantity = 1;
					}
					$(this).find("#trQuantity").val(Number(quantity)+1);
					$(this).find("#trQuantity").attr("title",Number(quantity)+1);
					b=false;
					return;
				}
			})
		}
		if(b){
			var productBrand = $("#prtSaveDiv #productBrand").val();
			if(productBrand==undefined || productBrand==null){
				productBrand="";
			}
			var trHTML = "<tr>" +
			"<td id='tdUnitCode'>"+info.unitCode+"</td>" +
			"<td id='tdBarCode'>"+info.barCode+"</td>" +
			"<td id='tdBrand'>"+productBrand+"</td>" +
			"<td id='trProductName'>"+info.productName +"</td>" +
			"<td id='trPrice'>"+info.price+"</td>" +
			"<td ><input class='text' style='width:80px;' onkeyup='BINOLWPSAL08_1.checkNumber(this);' id='trQuantity' value='1' title='1'></td>" +
			"<td id='trAct'><span class='but_delbox'><button id='AAAA' class='wp_del' onclick='BINOLWPSAL08_1.deleteRowPro(this);return false;'></button></span></td>" +
			"</tr>";
			$("#newDatabody").append(trHTML);
		}
		
		BINOLWPSAL08_1.changeColor();
		$("#addProductInput").val("");
		$("#addProductInput").focus();
	},
	"checkNumber":function(t){
		var ex = /^\d+$/;
		var quantity = $(t).val();
		if(!ex.test(quantity) || Number(quantity)<1){
			$(t).val(1);
			$(t).attr("title",1);
		}
		quantity = $(t).val();
		$(t).attr("title",quantity);
	},
	"setProductBinding1":function(t){
		var originalBrand = $(t).parents("tr").find("#originalBrand").val();
		if(originalBrand==undefined || originalBrand==null || originalBrand==""){
			originalBrand = "";
		}
		var counterCode = $("#counterCode").val();
		cntProductBinding({elementId:"addProductInput",showNum:20,counterCode:counterCode,targetDetail:true,afterSelectFun:BINOLWPSAL08_1.addProductLine,originalBrandStr:originalBrand});
	},
	"addActivityProduct":function(){
		// 规则code
		var maincode = $("#prtSaveDiv #maincode2").val();
		var totalAmount = 0;
		var b=false;
		if($("#newDatabody tr").length>0){
			var activityProduct = new Array();
			$.each($("#newDatabody tr"),function(){
				// 厂商编码
				var unicode = $(this).find("#tdUnitCode").text();
				if(unicode!=undefined && unicode!=null && unicode!=""){
					// 商品条码
					var barcode = $(this).find("#tdBarCode").text();
					// 数量
					var quantity = $(this).find("#trQuantity").val();
					// 优惠后的价格
					var price = $(this).find("#trPrice").text();
					// 原价
					var ori_price = $(this).find("#trPrice").text();
					// 产品名称
					var proname = $(this).find("#trProductName").text();
					var obj = {"maincode":maincode,"barcode":barcode,"unicode":unicode,"quantity":quantity,"price":price,"ori_price":ori_price,"proname":proname}
					activityProduct.push(obj);
					if(!isNaN(price)){
						totalAmount+=Number(price)*Number(quantity);
					}
				}
			});
			$.each($("#activityTbody tr"),function(){
				var ruleamount = $(this).find("#ruleamount").val();
				var rulebcj = $(this).find("#rulebcj").val();
				var rmaincode = $(this).find("#maincode").val();
				if(rmaincode==maincode && Number(totalAmount)>Number(ruleamount) && rulebcj!="1"){
					BINOLWPSAL02.showMessageDialog({
						message:"您选择的产品不允许大于"+ruleamount+"元!", 
						type:"MESSAGE"
					});
					b=true;
					return;
				}
			});
		}
		if(!b){
			if($("#newDatabody tr").length>0){
				$("#activityProductDiv").find("#"+maincode).remove();
				activityProduct = JSON.stringify(activityProduct);
				var htmlTR = $("#newDatabody").html();
				var html = "<div id='"+maincode+"'><input value='"+activityProduct+"'/><div id='htmlTR'></div></div>";
				$("#activityProductDiv").append(html);
				$("#activityProductDiv").find("#"+maincode).find("#htmlTR").html(htmlTR);
				removeDialog("#dialogChkPrtInitDIV");
			}
			$.each($("#activityTbody tr"),function(){
				var rmaincode = $(this).find("#maincode").val();
				if(maincode==rmaincode){
					var obj = $(this).first("td").find("input");
					BINOLWPSAL08_1.addOrDelLine(obj);
				}
			});
			// 关闭产品选择界面
			removeDialog("#dialogChkPrtInitDIV");
		}
	},
	"changeColor":function(){
		// 根据销售状态确定行样式
		$("#newDatabody tr:odd").attr("class","even");
		$("#newDatabody tr:even").attr("class","odd");
	},
	/**
	 * 删除选中行
	 */
	"deleteRowPro":function(t){
		var b = $(t).is(':focus');
		if(b){
			var $tr = $(t).parents("tr");
			var unitCode =$tr.find("#tdUnitCode").text();
			if(unitCode!=undefined && unitCode!=null && unitCode!=""){
				$tr.remove();
			}
			BINOLWPSAL08_1.changeColor();
		}
		
	}
};

var BINOLWPSAL08_1 = new BINOLWPSAL08_1_GLOBAL();

$(document).ready(function(){
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#dialogInit').html();
	BINOLWPSAL08_1.appendNewRow();
	
//	var refresh = function() {
//		$("#btnConfirm").focus();
//	}
//	setTimeout(refresh,1000);
});
