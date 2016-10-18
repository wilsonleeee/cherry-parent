var TEMP002_dialogBody ="";
var TEMP002_locationType = null;
var TEMP002_GLOBAL = function () {

};

TEMP002_GLOBAL.prototype = {
		"tempVal" : {},
		"tree" : null,
		//提交参数
		"extendOption" : {
			"BASE000015_INFO" : function (id){
				var m =[];
				var $id = $("#" + id);
				$id.find(':input').not("#segmePoints :input").each(function(){
					if (!$(this).is(":radio") || $(this).is(":radio[checked]")) {
						if(this.value != '') {
							var name = this.name;
//							if (0 == name.indexOf("pointFlag")) {
//								name = "pointFlag";
//							}
							m.push('"'+name+'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						}
					}
				});
				var segmePoints = [];
				$id.find("#segmePoints").find("div.segmeContent:visible").each(function(){
					var p = [];
					$(this).find(':input').each(function (){
						var param = CAMPAIGN_TEMPLATE.convertParam(this.name, this.value);
						p.push(param);
					});
					segmePoints.push("{" + p.toString() + "}");
				});
				m.push('"segmePoints":' + "[" + segmePoints.toString() + "]");
				var suffix = id.replace("BASE000015", "");
				var defselId = "#DEFAULTRULESEL" + suffix;
				if ($(defselId).is(":visible")) {
					$(defselId).find(':input').each(function(){
						if (!$(this).is(":radio") || $(this).is(":radio[checked]")) {
							if(this.value != '') {
								var name = this.name;
								if (0 == name.indexOf("defaultExecSel")) {
									name = "defaultExecSel";
								}
								m.push('"'+name+'":"'+
										$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
							}
						}
					});
				}
				return m.toString();
			},
			// 活动范围
			"BASE000016_INFO" : function (id){
				var m = [];
				var n = [];
				var $id = $("#" + id);
				$id.find('.memberList').each(function(){
					var p = [];
					$(this).find(':input').each(function (){
						if(!$(this).is(':checkbox') || ($(this).is(':checkbox') && $(this).is(':checked'))){
							p.push('"'+this.name+'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						}
					});
					n.push("{" + p.toString() + "}");
				});
				var extraInfo = '{"memberList":[]}';
				if($("#member").val() != 0){
					m.push('"memberLevelList":[' + n.toString() +  "]");
					extraInfo = '{"memberList":[' + n.toString() +  "]}";
				}
				$id.find(':input').not('#memberInfo input').each(function(){
					if($(this).parents('#treeDemo').length ==  0){
						if(!$(this).is(':checkbox') || ($(this).is(':checkbox') && $(this).is(':checked'))){
							m.push('"'+this.name+'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						}
					}
				});
				if($("#channelSel").val() == 2){
					var ch = [];
					$("#channelCk").find(':input').each(function(){
						if(!$(this).is(':checkbox') || ($(this).is(':checkbox') && $(this).is(':checked'))){
							ch.push('{"'+this.name+'":"'+
									$.trim(this.value) + '"}');
						}
					});
					m.push('"chlCdList":[' + ch.toString() +  "]");
				}
				if(null != TEMP002.tree){
					var nodes = TEMP002.tree.getNodes();
					var nodesList = [];
					var checkNodes = [];
					TEMP002.getTreeNodes(nodes, nodesList, checkNodes, 0);
					m.push('"nodesList":[' + nodesList.toString() +  "]");
					m.push('"checkNodes":[' + checkNodes.toString() +  "]");
				}
				// 规则类型
				$("#pointRuleType").val($("#pointRuleKbnSel").val());
				// 规则模板
				$("#templateType").val($("#templateType_" + id).val());
				// 已选等级信息
				$("#extraInfo").val(extraInfo);
				return m.toString();
			},

			/*
			 *  计算设置信息
			 */
//			"BASE000017_INFO" : function(id) {
//				var baseInfo;
//				var combTemps = [];
//				var $id = $("#" + id);
//				if ($("#checkFlag").is(":checked")) {
//					baseInfo = eval("CAMPAIGN_TEMPLATE.BASE_INFO('" + id + "')");
//				}
//				return baseInfo;
//				
//			},
			
			/*
			 * 购买产品信息
			 */
			"BUS000021_INFO" : function (id){
				var m = [];
				var n = [];
				var $id = $("#" + id);
				$id.find(':input').not($(":input", "[id^='BASE']")).filter(function() {
					return $(this).parents("[id^='BUS']").first().attr("id") == id;
				}).not($(":input", "#prtSel_" + id)).each(function(){
					if (!$(this).is(":radio") || $(this).is(":radio[checked]")) {
						m.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					}
				});
				if($("#product").val() == "5") {
					$("#prtImptBody").find("tr").each(function (){
						var p = [];
						$(this).find(':input').each(function (){
							p.push('"'+ this.name +'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						});
						n.push("{" + p.toString() + "}");
					});
				} else {$("#prtSel_" + id).find(".span_BASE000001").each(function (){
						var p = [];
						$(this).find(':input').each(function (){
							p.push('"'+ this.name +'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						});
						n.push("{" + p.toString() + "}");
					});
				}
				m.push('"productList":[' + n.toString() + "]");
				m = eval("CAMPAIGN_TEMPLATE.BUS_INFO('" + id + "', m)");
				return m.toString();
			},
			
			/*
			 * 促销活动信息
			 */
			"BUS000051_INFO" : function (id){
				var m = [];
				var n = [];
				var $id = $("#" + id);
				$id.find(':input').not($(":input", "[id^='BASE']")).filter(function() {
					return $(this).parents("[id^='BUS']").first().attr("id") == id;
				}).not($(":input", "#prtSel_" + id)).each(function(){
					if (!$(this).is(":radio") || $(this).is(":radio[checked]")) {
						m.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					}
				});
			    $("#campaignDiv").find(".span_act").each(function (){
					var p = [];
					$(this).find(':input').each(function (){
						p.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					});
					n.push("{" + p.toString() + "}");
				});
				m.push('"actList":[' + n.toString() + "]");
				m = eval("CAMPAIGN_TEMPLATE.BUS_INFO('" + id + "', m)");
				return m.toString();
			},
			
			/*
			 * 赠品不计积分信息
			 */
			"BASE000042_INFO" : function (id){
				var m = [];
				var n = [];
				var $id = $("#" + id);
				$id.find(':input').not($(":input", "[id^='#prtSel_']")).each(function(){
					if (!$(this).is(':radio') || !$(this).is(":checkbox") || $(this).is(":checked")) {
						m.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					}
				});
			    $("#prtSel_" + id).find(".span_BASE000001").each(function (){
					var p = [];
					$(this).find(':input').each(function (){
						p.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					});
					n.push("{" + p.toString() + "}");
				});
				m.push('"productList":[' + n.toString() + "]");
				var h = [];
				$("#prtSel_" + id + "_1").find(".span_BASE000001").each(function (){
					var p = [];
					$(this).find(':input').each(function (){
						p.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					});
					h.push("{" + p.toString() + "}");
				});
				m.push('"prmProductList":[' + h.toString() + "]");
				return m.toString();
			},
			
			/*
			 *  升级信息
			 */
			"BUS000040_INFO" : function(id) {
				var m = [];
				var $id = $("#" + id);
				if($id.parents(".no_submit").length > 0) {
					return null;
				}
				$id.find(':input').not($(":input", "[id^='BASE']")).filter(function() {
					return $(this).parents("[id^='BUS']").first().attr("id") == id;
				}).not($(":input", "[id^='member_']")).each(function(){
					if(this.value != '') {
						var name = this.name;
						var value = this.value;
						if (0 == name.indexOf("levelDate")) {
							var suffindex = value.lastIndexOf("_");
							value = value.substring(0,suffindex);
						}
						m.push('"'+name+'":"'+
								$.trim(value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					}
				});
				$id.find("[id^='member_']").each(function (){
					if($(this).is(":visible")){
						$(this).find(":input").each(function (){
							var name = this.name;
							var value = this.value;
							var valueId = "";
							var valueGrade = "";
							var suffindex = value.lastIndexOf("_");
							valueId = value.substring(0,suffindex);
							valueGrade = value.substring(suffindex + 1, value.length);
							m.push('"'+name+'":"'+
									$.trim(valueId.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
							m.push('"' + name + '_grade":"'+
									$.trim(valueGrade.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						});
					}
				});
				eval("CAMPAIGN_TEMPLATE.BUS_INFO('" + id + "', m)");
				return m.toString();
			},
			
			/*
			 * 积点产品
			 * 
			 */
			"BASE000048_INFO" : function (id){
				var m = [];
				var n = [];
				var $id = $("#" + id);
				$id.find(':input').not($(":input", "[id^='#prtSel_']")).each(function(){
					m.push('"'+ this.name +'":"'+
							$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				});
			    $("#prtSel_" + id).find(".span_BASE000001").each(function (){
					var p = [];
					$(this).find(':input').each(function (){
						p.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					});
					n.push("{" + p.toString() + "}");
				});
				m.push('"productList":[' + n.toString() + "]");
				n = [];
			    $("#prtSel_includePro").find(".span_pro").each(function (){
					var p = [];
					$(this).find(':input').each(function (){
						p.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					});
					n.push("{" + p.toString() + "}");
				});
				m.push('"includeProList":[' + n.toString() + "]");
				n = [];
			    $("#prtSel_onePro").find(".span_pro").each(function (){
					var p = [];
					$(this).find(':input').each(function (){
						p.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					});
					n.push("{" + p.toString() + "}");
				});
				m.push('"oneProList":[' + n.toString() + "]");
				return m.toString();
			},
			// 清零范围
			"BASE008000_INFO" : function (id){
				var m = [];
				var n = [];
				var $id = $("#" + id);
				$id.find('.memberList').each(function(){
					var p = [];
					$(this).find(':input').each(function (){
						if(!$(this).is(':checkbox') || ($(this).is(':checkbox') && $(this).is(':checked'))){
							p.push('"'+this.name+'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						}
					});
					n.push("{" + p.toString() + "}");
				});
				var extraInfo = '{"memberList":[]}';
				if($("#member").val() != 0){
					m.push('"memberLevelList":[' + n.toString() +  "]");
					extraInfo = '{"memberList":[' + n.toString() +  "]}";
				}
				$id.find(':input').not('#memberInfo input').each(function(){
					if($(this).parents('#treeDemo').length ==  0){
						if(!$(this).is(':checkbox') || ($(this).is(':checkbox') && $(this).is(':checked'))){
							m.push('"'+this.name+'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						}
					}
				});
				if(null != TEMP002.tree){
					var nodes = TEMP002.tree.getNodes();
					var nodesList = [];
					var checkNodes = [];
					TEMP002.getTreeNodes(nodes, nodesList, checkNodes, 0);
					m.push('"nodesList":[' + nodesList.toString() +  "]");
					m.push('"checkNodes":[' + checkNodes.toString() +  "]");
				}
				// 已选等级信息
				$("#extraInfo").val(extraInfo);
				return m.toString();
			},
			// 默认规则
			"BASE000063_INFO" : function (id){
				var m = [];
				$("#defcom_param").find(':input').each(function(){
					m.push('"'+this.name+'":"'+
							$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				});
				if ("1" == $("#multFlag").val()) {
					var n = [];
					$("#deft_cont_1").find('tr').each(function(){
						var p = [];
						$(this).find(':input').each(function (){
							if(!$(this).is(':checkbox') || ($(this).is(':checkbox') && $(this).is(':checked'))){
								p.push('"'+this.name+'":"'+
										$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
							}
						});
						n.push("{" + p.toString() + "}");
					});
					m.push('"memLevelList":[' + n.toString() +  "]");
				} else {
					$("#deft_cont_0").find(':input').each(function(){
						m.push('"'+this.name+'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					});
				}
				return m.toString();
			},
			// 积分清零规则
			"BASE008001_INFO" : function (id){
				var $id = $("#" + id);
				var m = [];
				var n = [];
				$id.find(':input').not('#prtSel_' + id + ' :input').each(function(){
					if(!$(this).is(':checkbox') || ($(this).is(':checkbox') && $(this).is(':checked'))){
						m.push('"'+this.name+'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					}
				});
				$("#prtSel_" + id).find(".span_BASE000001").each(function (){
					var p = [];
					$(this).find(':input').each(function (){
						p.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					});
					n.push("{" + p.toString() + "}");
				});
				m.push('"productList":[' + n.toString() + "]");
				return m.toString();
			}
		},
		
		//验证信息
		"extendRules" : {
			"BASE000016" : {
				startHH: {required: true, number:true},		// 开始时
				startMM: {required: true, number:true},		// 开始分
				startSS: {required: true, number:true},		// 开始秒
				endHH: {required: true, number:true},		// 结束时
				endMM: {required: true, number:true},		// 结束分
				endSS: {required: true, number:true},		// 结束秒
				minpt: {number:true},
				maxpt: {number:true}
			},
			"BASE000063" : {
				pointMultiple: {floatValid: [2,2]},
				ptmlt: {floatValid: [2,2]}	// 默认积分倍数
			},
			"BASE008001" : {
				clearNum: {number:true},	// 间隔时长
				yearNo: {number:true},		// 间隔年数
				disMonth: {number:true},	// 失效月
				disDay: {number:true},		// 失效日
				execMonth: {number:true},	// 执行月
				execDay: {number:true}		// 执行日
			},
			"BUS000021" : {
				rangeNum : {number:true}
			},
			"BUS000019" : {
				bindDayLimit : {number:true}
			}
		},
		
		"changeLabel" : function (obj){
			var $tr = $(obj).parents("tr.giveClass").first();
			if($(obj).val() == "1"){
				$tr.find("#pointTxt").hide();
				$tr.find("#point").hide();
				$tr.find("#mulTxt").show();
				$tr.find("#mul").show();
			}else{
				$tr.find("#mulTxt").hide();
				$tr.find("#mul").hide();
				$tr.find("#pointTxt").show();
				$tr.find("#point").show();
			}
		},
		
		 // AJAX文件上传
	    "ajaxFileUpload" : function (){
			var url = $("#importCounterUrl").attr("href");
	    	// AJAX登陆图片
	    	$ajaxLoading = $("#loading");
	    	// 错误信息提示区
	    	$errorMessage = $('#actionResultDisplay');
	    	// 清空错误信息
			$("#errorDiv").hide();
			$errorMessage.hide();
	    	$("#locationType").val(5);
	    	if($('#upExcel').val()==''){
	    		$('#pathExcel').val('');
	    		var errHtml = $('#errmsg1').text();
				$("#errorDiv").show();
				$("#errorSpan").text(errHtml);
	    		return false;
	    	}
	    	$ajaxLoading.ajaxStart(function(){$(this).show();});
	    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
	    	$.ajaxFileUpload({
		        url: url,
		        secureuri:false,
		        data:{'csrftoken':parentTokenVal(),'brandInfoId':$('#brandInfoId').val()},
		        fileElementId:'upExcel',
		        dataType: 'html',
		        success: function (msg){
		        	$errorMessage.html(msg);
		        	if($errorMessage.find("#actionResultDiv").length > 0){
		        		$errorMessage.show();
		        	}else if (window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
						var counterNodes = msgJson['trueCounterList'];
						if(msgJson['errorMsg'] != undefined){
							$("#errorDiv").show();
							$("#errorSpan").text(msgJson['errorMsg']);
						}else{
							$("#errorDiv").hide();
						}
						TEMP002.loadTree(counterNodes);
		        	}
		        }
	        });
	    },
	    
	 // AJAX文件上传(特定商品)
	    "prtFileUpload" : function (){
			var url = $("#importSpecPrtUrl").attr("href");
	    	// AJAX登陆图片
	    	$ajaxLoading = $("#loading");
	    	// 错误信息提示区
	    	$errorMessage = $('#actionResultDisplay');
	    	// 清空错误信息
			$("#errorDiv").hide();
			$errorMessage.hide();
	    	$("#locationType").val(5);
	    	if($('#upExcel').val()==''){
	    		$('#pathExcel').val('');
	    		var errHtml = $('#errmsg1').text();
				$("#errorDiv").show();
				$("#errorSpan").text(errHtml);
	    		return false;
	    	}
	    	$ajaxLoading.ajaxStart(function(){$(this).show();});
	    	//$ajaxLoading.ajaxComplete(function(){$(this).hide();});
	    	$.ajaxFileUpload({
		        url: url,
		        secureuri:false,
		        data:{'csrftoken':parentTokenVal(),'brandInfoId':$('#brandInfoId').val()},
		        fileElementId:'upExcel',
		        dataType: 'html',
		        success: function (msg){
		        	$ajaxLoading.hide();
		        	$errorMessage.html(msg);
		        	if($errorMessage.find("#actionResultDiv").length > 0){
		        		$errorMessage.show();
		        	}else if (window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
						var prts = msgJson['prtList'];
						if(msgJson['errorMsg'] != undefined){
							$("#errorDiv").show();
							$("#errorSpan").text(msgJson['errorMsg']);
						}else{
							$("#errorDiv").hide();
						}
						if (prts) {
							var totalNum = $("#prtImptBody > tr").length;
							var size = 200 - totalNum;
							if (size > 0) {
								for (var i in prts){
									if (i == size) {
										 break;
									}
									var trhtml = "";
									var nameTotal = prts[i]["nameTotal"];
									var barCode = prts[i]["barCode"];
									var unitCode = prts[i]["unitCode"];
									var saleType = prts[i]["saleType"];
									var typeName = ("P" == saleType)? "促销品" : "产品";
									trhtml += '<tr><td>' + nameTotal + 
									'<input type="hidden" name="nameTotal" value="' + nameTotal + '" />' +
									'<input type="hidden" name="barCode" value="' + barCode + '" />' +
									'<input type="hidden" name="unitCode" value="' + unitCode + '" />' +
									'<input type="hidden" name="saleType" value="' + saleType + '" />' +
									'<input type="hidden" name="proId" value="' + prts[i]["proId"] + '" />' +
									'</td><td>' + barCode + '</td><td>' + unitCode + '</td><td>' + typeName + '</td><td>' +
									'<a href="javascript:void(0)" class="delete" onclick="javascript:TEMP002.delSepcPrt(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a></td></tr>'
									$("#prtImptBody").append(trhtml);
								}
								totalNum = $("#prtImptBody > tr").length;
								$("#imptPrtNum").html('<strong>' + totalNum + '</strong>');
							}
						}
		        	}
		        }
	        });
	    },
	    
	    "showImptRst" : function() {
	    	var dialogSetting = {
					dialogInit: "#prtImptDialog",
					width:860, 
					height:600, 
					zIndex: 9999, 
					resizable:false,
					title: 	"导入商品一览",
					cancel: "关闭",
					cancelEvent: function(){
						removeDialog($("#prtImptDialog"));
					}
				};
			openDialog(dialogSetting);
	    },
	    
	    "delSepcPrt" : function(obj) {
	    	$(obj).parent().parent().remove();
	    	var totalNum = $("#prtImptBody > tr", "#prtImptDialog").length;
			$("#prtImptDialog #imptPrtNum").html('<strong>' + totalNum + '</strong>');
	    	$("#dialogInitMessage").html($("#prtImptDialog").html());
	    },
	    
		"showTree" : function(obj){
			var that = this;
			var url = $("#getTreeurl").attr("href");
			var param = "locationType=" + $(obj).val();
	    	$("#locationType").val($(obj).val());
			var callback = function(msg) {
				that.loadTree(msg);
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
		},
		
		"loadTree" : function(nodes) {
			var treeNodes = null;
			var checkedFlag = 0;
			if(typeof(nodes) != "object"){
				treeNodes = eval("(" + nodes + ")");
			}else{
				treeNodes = nodes;
				checkedFlag = 1;
			}
			var treeSetting = {
				check : {
					enable:true,
					chkboxType: { "Y": "ps", "N": "ps" }
				},
				data : {
					key:{
						name : "name",
						children : "nodes"
					}
				},
				view : {
					showLine : true
				},
				callback:{
					beforeExpand: TEMP002.expendTreeNodesByAjax
				}
			};
			this.tree = null;
			this.tree = $.fn.zTree.init($('#treeDemo'),treeSetting,treeNodes);
			if(checkedFlag == 1){
				this.tree.checkAllNodes(true);
			}
		},
		
		/**
		 * 展开节点并通过ajax查询柜台
		 * @param treeId
		 * @param treeNode
		 * @return
		 */
		"expendTreeNodesByAjax" : function (treeId, treeNode){
			if (treeNode.nodes!=null){
				var length = treeNode.nodes.length;
				if (length==0){
					return false;
				}else if (length==1){
					var node = treeNode.nodes[0];
					if (node.name=="artificialCounter"){
						TEMP002.getCounterByAjax(treeNode);
						return false;
					}else{
						return true;
					}
				}
			}else {
				return false;
			}
		},
		

		/**
		 * 通过Ajax取得柜台
		 * @param treeNode
		 * @return
		 */
		"getCounterByAjax" : function (treeNode){
			if (typeof treeNode.nodes == "undefined"){
				return;
			}
			var node = treeNode.nodes[0];
			var locationType = $("#choicePlace").val();
			if (locationType == "2"){
				var param = "cityID="+treeNode.id;
			}else if (locationType == "4"){
				var param = "channelID="+treeNode.id;
			}
			
			var name = treeNode.name;
			if (treeNode.name.indexOf("loading...")<0){
				treeNode.name +="(loading...)";
			}
			TEMP002.tree.updateNode(treeNode);
			
			url = $('#getCounterDetailByAjaxUrl').attr("href");
			var reloadFlg = true;
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:expendTreeNodesCallBack,
				reloadFlg:reloadFlg
			});
			
			function expendTreeNodesCallBack(data){
				var dataArr = eval('('+data+')');
				var selectNode = TEMP002.tree.getNodeByParam("name","(loading...)");
				TEMP002.tree.removeNode (node);
				if(node.checked){
					var length = dataArr.length;
					for(var i = 0 ; i < length ; i++){
						dataArr[i].checked = true;
					}
				}
				TEMP002.tree.addNodes(treeNode,dataArr);
				TEMP002.tree.refresh();
				TEMP002.tree.expandNode(treeNode, true, false);
				treeNode.name = name;
				TEMP002.tree.updateNode(treeNode);
			}
		},
		
		/**
		 * 获得树节点
		 * 
		 */
		"getTreeNodes" : function (nodes, nodesList, checkNodes, checkedFlag){
			if (!nodes) return nodesList; 
			locationType = $("#locationType").val();
			for (var i=0;i<nodes.length;i++){
				var node = nodes[i];
				if(node.checked == true || locationType == 5){
					if(node.checked == true){
						var map = [];
						map.push('"id":"' + node.id + '"');
						map.push('"name":"' + node.name + '"');
						if(!checkedFlag && node.getCheckStatus().half != true){
							checkNodes.push("{" + map.toString() + "}");
						}
					}
					var childNodes = node.nodes;
					if (childNodes ==null){
						if (locationType == "1"){
							nodesList.push ('{"city":"'+node.id + '"}');
						}else if (locationType == "3"){
							nodesList.push ('{"channel":"'+node.id + '"}');
						}else if (locationType == "2" || locationType == "4"){
							nodesList.push ('{"counter":"'+node.id + '"}');
						}else if(locationType == "5"){
							nodesList.push ('{"counter":"'+node.id + '","checked":"'+ node.checked +'"}');
						}
					}else if (childNodes.length == 1 && childNodes[0].name =="artificialCounter"){
						if (locationType == "2"){
							nodesList.push ('{"city":"'+node.id + '"}');
						}else if (locationType == "4"){
							nodesList.push ('{"channel":"'+node.id + '"}');
						}
						if(childNodes[0].checked != true){
							checkedFlag = 0;
						}else{
							checkedFlag = 1;
						}
					}else {
						for(var j in childNodes){
							if(childNodes[j].checked != true){
								checkedFlag = 0;
								break;
							}
							if(j == childNodes.length - 1){
								checkedFlag = 1;
							}
						}
						nodesList = TEMP002.getTreeNodes(childNodes, nodesList, checkNodes, checkedFlag);
					}
				}
				if(nodes[i].getParentNode() == null){
					checkedFlag = 0;
				}
			}
			return nodesList;
		},
		
		"showMember" : function(obj){
			if($(obj).val() == 1){
				$("#memberInfo").show();
			}else if($(obj).val() == 0){
				$("#memberInfo").hide();
			}
		},
		
		"showChannel" : function(obj){
			if($(obj).val() == "2"){
				$("#channelCk").show();
			}else {
				$("#channelCk").hide();
			}
		},
		
		"showOrHidePanel" : function(obj, panelId, compVal){
			if($(obj).val() == compVal){
				$(panelId).show();
			}else {
				$(panelId).hide();
			}
		},
		"showDate" : function(obj,value,flag){
			if ($(obj).val() == "5") {
				//$(obj).parent().next().hide();
				$("#prtselbtn").hide();
				$(obj).parent().next().next().show();
				$("#prtImptPanel").show();
			} else {
				if((flag && $(obj).val() == value) || (!flag && $(obj).val() != value)){
					$(obj).parent().next().next().hide();
					$(obj).parent().next().show();
					$("#prtselbtn").show();
					$("#prtImptPanel").hide();
					$("#prtImptBody").empty();
				}else{
					$(obj).parent().next().hide();
				}
			}
			if(!flag){
				// 取索引值
				index = $("input[name='selPrtIndex']", $(obj).parents(".box4-content")).val();
				$("#prtSel_" + index).empty();
			}
		},
		/*
		 * 根据选择显示面板
		 */
		"showPanel" : function(obj, id) {
			var val = $(obj).val();
			var $id = "#" + id + val;
			var $parent = $(obj).parents("td:first");
			$parent.find('[id^="' + id + '"]').hide();
			$parent.find($id).show();
		},
		
		"showCalDiv" : function(obj){
			if($(obj).is(":checked")){
				$(obj).parents(".box4").find(".box4-content").show();
			}else{
				$(obj).parents(".box4").find(".box4-content").hide();
			}
		},
		"showDiv" : function(obj){
			var vl = $(obj).val();
			var $fs = $("select[name='firstBillSel']");
			if (vl == "5") {
				$fs.attr("disabled", true);
				$fs.parent().append('<input type="hidden" name="firstBillSel" value="0" />');
			} else {
				$fs.attr("disabled", false);
				$('input[name="firstBillSel"]').remove();
			}
			if(vl == "2"){
				$("#appointMonth").show();
				$("#appointDate").hide();
				$("#appointDay").hide();
				$("#bindDaySpan").hide();
			}else if(vl == "4"){
				$("#appointDate").show();
				$("#appointDay").hide();
				$("#appointMonth").hide();
				$("#bindDaySpan").hide();
			}else if(vl == "3"){
				$("#appointDay").show();
				$("#appointMonth").hide();
				$("#appointDate").hide();
				$("#bindDaySpan").hide();
			}else if(vl == "6"){
				$("#bindDaySpan").show();
				$("#appointMonth").hide();
				$("#appointDate").hide();
				$("#appointDay").hide();
			}else{
				$("#appointMonth").hide();
				$("#appointDate").hide();
				$("#appointDay").hide();
				$("#bindDaySpan").hide();
			}
		},
		
		"showjonDiv" : function(obj){
			if($(obj).val() == "0"){
				$("#pointYear").show();
				$("#pointDay").hide();
			}else if($(obj).val() == "1"){
				$("#pointDay").show();
				$("#pointYear").hide();
			}else{
				$("#pointYear").hide();
				$("#pointDay").hide();
			}
		},
		
		"showLimitDiv" : function (obj){
			var $id = $(obj).attr("id");
			var $divId = $("#" + $id + "Div");
			if($(obj).is(":checked")){
				$divId.show();
			}else{
				$divId.hide();
			}
		},
		
		"showSearch" : function(obj){
			if($(obj).val() == 0){
				$("#searchTree").hide();
				$("#importDiv").hide();
				$("#treeArea").hide();
		    	$("#locationType").val(0);
		    	$('#treeDemo').empty();
			}else if($(obj).val() == 5){
				$("#searchTree").hide();
				$("#importDiv").show();
				$("#treeArea").show();
			}else{
				$("#importDiv").hide();
				$("#searchTree").show();
				$("#treeArea").show();
			}
		},

		"showTable" : function (obj, diagType, index){
				// 取索引值
				TEMP002.selPrtIndex = $("input[name='selPrtIndex']", $(obj).parents(".box4-content")).val();
				if(diagType == null){
					diagType = $("#product").val() - 1;
				}
				if(index != null){
					TEMP002.selPrtIndex = TEMP002.selPrtIndex + '_' + index;
				}
				if ('4' == $("#product").val()){
					var targetId = "prtSel_" + TEMP002.selPrtIndex;
					var prmOpt = {
							targetId : targetId,
							checkType : "checkbox",
							//prmCate: 'CXLP',
							mode : 2,
							brandInfoId : $("#brandInfoId").val(),
							popValidFlag : 2,
							getHtmlFun : function(info) {
								var proId = info.proId;
								if ($('input[name="prmId"][value="'+ proId + '"]', '#' + targetId).length == 0) {
									var prmInfo = '<span class="span_BASE000001">\
										<input type="hidden" name="prmId" value="' + proId + '" />\
										<input type="hidden" name="prmNameTotal" value="' + info.nameTotal + '" />\
										<span style="margin:0px 5px;">' + info.nameTotal + '</span>\
										<span class="close" onclick="CAMPAIGN_TEMPLATE_delete(this);return false;">\
										<span class="ui-icon ui-icon-close"></span></span></span>';
									return prmInfo;
								}
							}
					};
					// AJAX促销品弹出框
					popAjaxPrmDialog(prmOpt);
					return false;
				}
				// 弹出信息框
				CAMPAIGN_TEMPLATE.openProPopup(obj,diagType, TEMP002.selPrtIndex);
				},
		// 选择活动弹出框
		"popActivityList": function(url, object) {
			var callback = function(tableId) {
				var $checkedBox = $("#"+tableId).find(":input[name='campaignCode'][checked]");
				if($checkedBox.length > 0) {
					$checkedBox.each(function(){
						var actName = $(this).parent().next().text();
						var htm = '<span class="span_act"><input type="hidden" name="actCode" value="'+$(this).val()+'"/><input type="hidden" name="actName" value="' + actName + '"/>' + actName
						+ '<span class="close" style="margin: 0 10px 2px 5px;" onclick="TEMP002.delActHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span></span>';
						$("#campaignDiv").append(htm);
					});
				}
			}
			var params = "param=1&param2=CXHD&checkType=checkbox";
			popDataTableOfCampaignList(url, params, null, callback);
		},
		// 删除单个活动
		"delActHtml" : function (obj){
			$(obj).parent().remove();
		},
		"showChange" : function (obj){
					var value = $(obj).val();
					var suffindex = value.lastIndexOf("_");
					var index = value.substring(suffindex+1, value.length);
					$("#member_" + index).show();
					$("#member").find("[id^='member_']").not("#member_" + index).each(function (){
						$(this).hide();
					});
				},
		"addPointPro" : function (obj){
					var spanHtml = $(obj).parent().html();
					spanHtml = '<span class="span_pro" style="width:600px;float:left;">' + spanHtml + '</span>';
					$(obj).parents("td").first().append(spanHtml);
					$(obj).parents("td").first().find(".deleteClass").show();
				},
		"deletePointPro" : function (obj){
					if($(obj).parents("td").first().find(".deleteClass").length < 3){
						$(obj).parents("td").first().find(".deleteClass").hide();
					}
					$(obj).parent().remove();
				},
	    "validSubRuleKbn" : function (obj, suffix) {
					if($(obj).val() == "2") {
						$("#subRuleKbnSel").attr("disabled",false);
					} else {
						$("#subRuleKbnSel").attr("disabled",true);
					}
					var $typeSel = $("#templateType_" + suffix);
					if ($(obj).val() == "4") {
						$typeSel.val("PT16");
						//$typeSel.attr("disabled", "true");
						//$("#hidePanel").html('<input type="hidden" name="templateType" id="templateType_' + suffix + '" value="PT16"/>');
					} else {
						if ($typeSel.val() == "PT16") {
							$typeSel.val("PT03");
						}
						//$typeSel.removeAttr("disabled");
						//$("#hidePanel").empty();
					}
				},
		"showTimeSetting" : function(obj) {
			if ($(obj).is(':checked')) {
				$("#timeTemplate").show();
			} else {
				$("#timeTemplate").hide();
			}
		},
		"showDefRule" : function (obj, value) {
			var suffix;
			var cid = $(obj).attr("id");
			if ("rangeFlag" == cid) {
				if ('0' == $(obj).val()) {
					$("#rangeNumSpan").show();
				} else {
					$("#rangeNum").val("");
					$("#rangeNum").removeClass("error");
					$("#rangeNumSpan").hide();
					$("#rangeNumSpan").removeClass("error");
					$("#rangeNumSpan").find("#errorText").remove();
				}
			}
			if (cid && 0 == cid.indexOf("calcuKbnSel")) {
				suffix = cid.replace("calcuKbnSel", "");
			} else {
				suffix = "_0_0";
			}
			var $id = $("#DEFAULTRULESEL" + suffix);
			if ($id.length == 0) {
				return false;
			}
			if ($("#rangeFlag").val() == "0") {
				$("#allPrtDesp" + suffix).hide();
				$("#specPrtDesp" + suffix).show();
			} else {
				$("#allPrtDesp" + suffix).show();
				$("#specPrtDesp" + suffix).hide();
			}
			if (value == $(obj).val()) {
				$id.show();	
			} else {
				if ($("#calcuKbnSel" + suffix).val() == 1 && $("#rangeFlag").val() != "0") {
					$id.hide();
				}
			}
		},
		"selRuleKbn" : function(obj) {
			if ($(obj).val() == "PT16") {
				$("#pointRuleKbnSel").val("4");
			} else {
				if ($("#pointRuleKbnSel").val() == "4") {
					$("#pointRuleKbnSel").val("1");
				}
			}
			if($("#pointRuleKbnSel").val() == "2") {
				$("#subRuleKbnSel").attr("disabled",false);
			} else {
				$("#subRuleKbnSel").attr("disabled",true);
			}
		},
		
		/*
		 * 积分分段
		 */
		"segmePoint" : function(obj) {
			var $parent = $(obj).parent();
			// 积分分段设置
			var $segmeMul = $parent.next("td");
			var $segmePanel = $segmeMul.find("#segmePanel");
			// 增加分段内容
			$segmePanel.before($segmePanel.html());
			$parent.hide();
			$parent.find("input[name='multipleMark']").val("");
			$segmeMul.show();
		},
		/*
		 * 添加分段
		 */
		"addSegme" : function(obj) {
			// 积分分段设置
			var $segmeMul = $(obj).parent();
			var $segmePanel = $segmeMul.find("#segmePanel");
			// 增加分段内容
			$segmePanel.before($segmePanel.html());
		},
		/*
		 * 删除分段
		 */
		"delSegme" : function(obj) {
			// 积分分段设置
			var $panel = $(obj).parent();
			var $segmeMul = $panel.parent();
			$panel.remove();
			// 检查是否分段都被删除
			if ($segmeMul.find("div.segmeContent").length <= 1) {
				$segmeMul.hide();
				$segmeMul.prev("td").show();
			}
		},
		/*
		 * 改变值
		 */
		"changeVal" : function(obj, name) {
			// 积分分段设置
			$(obj).parent().find('input[name="' + name + '"]').val($(obj).val());
		},
		/*
		 * 显示指定清零日期输入框
		 */
		"showExecDate" : function(obj) {
			if ($(obj).val() == "2") {
				$("#execDate3").hide();
				$("#execDate").show();
			} else {
				$("#execDate").hide();
				if ($(obj).val() == "3") {
					$("#execDate3").show();
				} else {
					$("#execDate3").hide();
				}
			}
		},
		/*
		 * 切换清零时间长度
		 */
		"changePCLength" : function(obj) {
			if ($(obj).val() == "0") {
				$("#pcLength_0").show();
				$("#pcLength_1").hide();
			} else {
				$("#pcLength_1").show();
				$("#pcLength_0").hide();
			}
		},
		/*
		 * 显示特殊参数输入框
		 */
		"showSpeciKeys" : function(obj) {
			if ($(obj).is(":checked")) {
				$("#keysText").fadeIn();
			} else {
				$("#keysText").fadeOut();
			}
		},
		/*
		 * 默认规则切换方式
		 */
		"changeDefRule" : function(num) {
			$("#multFlag").val(num)
			$("span[id^='defbtn_']").hide();
			$("#defbtn_" + num).show();
			$("div[id^='deft_cont_']").hide();
			$("#deft_cont_" + num).show();
		},
		"changeYanqiPrtType" : function(obj){
			var selval = $(obj).val();
			var idx = $("input[name='selPrtIndex']", $(obj).parents(".box4")).val();
			$("#prtSel_" + idx).empty();
			// 未选择
			if (selval == "") {
				$("#prtselbtnSpan").hide();
			} else {
				$("#prtselbtnSpan").show();
			}
		},
		"showYanqiPrtTable" : function(obj){
			var selval = $("#yanqiprt").val();
			// 取索引值
			TEMP002.selPrtIndex = $("input[name='selPrtIndex']", $(obj).parents(".box4")).val();
			// 产品
			if (selval == "1") {
				// 弹出信息框
				CAMPAIGN_TEMPLATE.openProPopup(obj,'0', TEMP002.selPrtIndex);
				// 促销礼品
			} else if (selval == "4"){
				var targetId = "prtSel_" + TEMP002.selPrtIndex;
				var prmOpt = {
						targetId : targetId,
						checkType : "checkbox",
						//prmCate: 'CXLP',
						mode : 2,
						brandInfoId : $("#brandInfoId").val(),
						popValidFlag : 2,
						getHtmlFun : function(info) {
							var proId = info.proId;
							if ($('input[name="prmId"][value="'+ proId + '"]', '#' + targetId).length == 0) {
								var prmInfo = '<span class="span_BASE000001">\
									<input type="hidden" name="prmId" value="' + proId + '" />\
									<input type="hidden" name="prmNameTotal" value="' + info.nameTotal + '" />\
									<span style="margin:0px 5px;">' + info.nameTotal + '</span>\
									<span class="close" onclick="CAMPAIGN_TEMPLATE_delete(this);return false;">\
									<span class="ui-icon ui-icon-close"></span></span></span>';
								return prmInfo;
							}
						}
				};
				// AJAX促销品弹出框
				popAjaxPrmDialog(prmOpt);
			}
		},
		"showYanqi" : function(obj){
			if ($(obj).is(":checked")) {
				$("#yanqiSpan").show();
				$("#yanqiPanel").show();
			} else {
				$("#yanqiSpan").hide();
				$("#yanqiPanel").hide();
			}
		},
		"showMonthDay" : function (obj, month, day) {
			if (!obj) {
				var mhtml = "";
				for (var i = 1; i <= 12; i++) {
					mhtml += '<option value="'+i+'"';
					if (month && i == month) {
						mhtml += " selected";
					}
					mhtml += '>'+i+'</option>';
				}
				$("#specBirthMonth").html(mhtml);
			} else {
				month = $(obj).val();
			}
			var max = 0;
			if(month == '2') {
				max = 29;
			} else if(month == '4' || month == '6' || month == '9' || month == '11') {
				max = 30;
			} else {
				max = 31;
			}
			var dhtml = "";
			for(i = 1; i <= max; i++) {
				dhtml += '<option value="'+i+'"';
				if (day &&  i == day) {
					dhtml += " selected";
				}
				dhtml += '>'+i+'</option>';
			}
			$("#specBirthDay").html(dhtml);
		}
};

var TEMP002 = new TEMP002_GLOBAL();
CAMPAIGN_TEMPLATE.addOption(TEMP002.extendOption);
CAMPAIGN_TEMPLATE.addTempRules(TEMP002.extendRules);


$(document).ready(function() {
	// 产品popup初始化
	TEMP002_dialogBody = $('#productDialog').html();
	if($(".box4-content-content [name='deleteJon']").not($(".box4-content-content [name='deleteJon']", ".no_submit")).length == 1){
		$(".box4-content-content [name='deleteJon']").not($(".box4-content-content [name='deleteJon']", ".no_submit")).hide();
	}
	if($(".box4-content-content [name='deleteOneBuy']").not($(".box4-content-content [name='deleteOneBuy']", ".no_submit")).length == 1){
		$(".box4-content-content [name='deleteOneBuy']").not($(".box4-content-content [name='deleteOneBuy']", ".no_submit")).hide();
	}
	if($(".box4-content-content [name='deleteUp']").not($(".box4-content-content [name='deleteUp']", ".no_submit")).length == 1){
		$(".box4-content-content [name='deleteUp']").not($(".box4-content-content [name='deleteUp']", ".no_submit")).hide();
	}
});
