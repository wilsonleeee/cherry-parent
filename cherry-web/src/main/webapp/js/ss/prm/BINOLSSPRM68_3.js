function BINOLSSPRM68_3(){};
BINOLSSPRM68_3.prototype={
		"nextBefore":function(){
		},
		// 活动对象类型
		"changeMebType":function(_this,index){
			var $this = $(_this);
			// 选择导入批次号
			var $linkSearchCode = $("#linkSearchCode_" + index);
			// 选择搜索条件
			var $linkMebSearch = $("#linkMebSearch_" + index);
			// 选择EXCEL文件
			var $linkExcel = $("#linkExcel_" + index);
			// 查询活动对象
			var $searchMeb= $("#searchMeb_" + index);
			// 活动对象一览
			var $mebResult= $("#mebResult_div_" + index);
			//导入弹出按钮
			var $linkMebImport= $("#linkMebImport_" + index);
			// 预估提示
			var $yugu= $("#yugu_" + index);
			$mebResult.hide();
			$("#memberInfo_"+index).hide();
			// 清空对象信息
			$("#memberJson_" + index).val("");
			$("#searchCode_" + index).val("");
			$('#conInfoDiv_' + index).hide();
			$('#memCountShow_' + index).hide();
			if($this.val() == '0' || $this.val() == '5'|| $this.val() == '6'){// 全部会员,不限对象,非会员
				$linkSearchCode.hide();
				$linkMebSearch.hide();
				$linkMebImport.hide();
				$linkExcel.hide();
				$searchMeb.hide();
			}else if($this.val() == '1' || $this.val() == '2'){// 条件或结果
				$linkMebSearch.show();
				$searchMeb.show();
				$linkSearchCode.hide();
				$linkMebImport.hide();
				$linkExcel.hide();
				if($this.val() == '1'){
					$yugu.show();
				}else{
					$yugu.hide();
				}
			}else if($this.val() == '3'){// excel导入
				$searchMeb.show();
				$linkSearchCode.hide();
				$linkMebSearch.hide();
				$linkMebImport.show();
				$yugu.hide();
			}else if($this.val() == '4'){// 外部接口导入
				$searchMeb.show();
				$linkSearchCode.show();
				$linkExcel.hide();
				$linkMebSearch.hide();
				$linkMebImport.hide();
				$yugu.hide();
			}
		},
		//活动对象Excel导入
		"popMemImport":function(){
			var $searchCode = $('.SEARCHCODE').filter(':visible').find(':input[name="pageC.searchCode"]');
			//弹出框属性设置
			var option = {
					searchCode : $searchCode.val(),
					click:function(){
		        		 var $dialogSearchCode = $('#memImportDialog').find(':input[name="searchCode"]');
		        		 $searchCode.val($dialogSearchCode.val());
					}
			};
			// 调用会员导入弹出框共通
			popAjaxMemImportDialog(option);
		},
		// 活动对象查询
		"memSearch" : function(index) {
			var url = '/Cherry/cp/BINOLCPCOM03_memInfosearch';
			var searchCode= $("#searchCode_" + index).val();
			if(!isEmpty(searchCode)){
				var params= getSerializeToken();
				var brandInfoId=$("#brandInfoId").val();
				params +="&brandInfoId="+brandInfoId+"&searchCode="+searchCode;
				url = url + "?" + params;
				// 显示结果一览
				$("#mebResult_div_"+index).show();
				$("#memberInfo_"+index).show();
				// 表格设置
				var tableSetting = {
						 index: 100000 + index,
						 // 表格ID
						 tableId : '#memberDataTable_' + index,
						 // 数据URL
						 url : url,
						 iDisplayLength:5,
						 // 表格默认排序
						 aaSorting : [[ 1, "asc" ]],
						 // 表格列属性设置
						 aoColumns : [  
										{ "sName": "customerType","bSortable": false},
										{ "sName": "memCode"},
										{ "sName": "memName"},
										{ "sName": "mobilePhone"},
										{ "sName": "birthDay","bSortable": false},
										{ "sName": "joinDate","bSortable": false},
										{ "sName": "changablePoint","sClass":"alignRight","bSortable": false},
										{ "sName": "receiveMsgFlg","sClass":"center","bSortable": false}]
				};
				// 调用获取表格函数
				getTable(tableSetting);
			}
	    },// 活动对象搜索条件弹出框
		"searchConDialog" :function(obj,index){
			var that = this;
			var url = '/Cherry/common/BINOLCM33_init';
			var params = "reqContent=" + $("#memberJson_"+index).val();
			var dialogSetting = {
				dialogInit: "#searchCondialogInit",
				width: 900,
				height: 550,
				title: $("#objDialogTitle").text(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				//确认按钮
				confirmEvent: function(){
					$('#conInfoDiv_' + index).hide();
					//将搜索条件弹出框信息json格式化
					var searchParam = $("#searchCondialogInit").find(":input").serializeForm2Json(false);
					if(searchParam != undefined && null != searchParam && searchParam != ""){
						$("#memberJson_"+index).val(searchParam);
						that.getConInfo(searchParam,index);
						// 关闭搜索条件弹出框
						removeDialog("#searchCondialogInit");
					}
					$('#mebResult_div_' + index).hide();
				},
				//关闭按钮
				cancelEvent: function(){removeDialog("#searchCondialogInit");}
			};
			openDialog(dialogSetting);
			cherryAjaxRequest({
				url: url,
				param : params,
				callback: function(msg) {
					$("#searchCondialogInit").html(msg);
				}
			});
		},
		// 取得查询条件
		"getConInfo":function(json,index){
			var that = this;
			var url = '/Cherry/common/BINOLCM33_conditionDisplay';
			var $json = $('#memberJson_' + index);
			var params = "reqContent=" + json;
			cherryAjaxRequest({
				url: url,
				param : params,
				callback: function(msg) {
					$("#searchCondition_"+index).text(msg);
					that.getSearchCode(json,index);
				}
			});
		},
		//生成searchCode
		"getSearchCode":function(json,index){
			var that = this;
			var url = '/Cherry/cp/BINOLCPCOM03_queryMemSearchCode';
			var params = "campMebInfo=" + json;
			params += "&recordName=" + $('#prmActiveName').val();
			var campMebType= $("#memberType_"+index).val();
			if(campMebType=='1'){//动态搜索条件
				params+="&recordType=1";
			}else if(campMebType=='2'){//当前搜索结果
				params+="&recordType=2&saveFlag=1";
			}
			cherryAjaxRequest({
				url: url,
				param : params,
				callback: function(msg) {
					$("#searchCode_"+index).val(msg);
					// 取得活动对象数量
					that.getMemCount(json,index);
				}
			});
		},
		// 取得活动对象数量
		"getMemCount":function(json,index){
			var url = '/Cherry/cp/BINOLCPCOM03_searchMemCount';
			var params = "campMebInfo=" + json;
			cherryAjaxRequest({
				url: url,
				param : params,
				callback: function(msg) {
					$("#memCount_"+index).text(msg);
					$('#memCountShow_' + index).show();
					$('#conInfoDiv_' + index).show();
				}
			});
		}
}
var PRM68_3 = new BINOLSSPRM68_3();