var BINOLMOMAN10 = function () {
    
};

BINOLMOMAN10.prototype = {
		//读取树的数据
		"posMenu":function(){
			var that = this;			
			var url = $("#searchUrl").attr("href");
			var param = $("#mainForm").serialize();
			var callback = function(msg){
				if(msg=="null"){
					$("#addPosMenuBrand").show();
				}else{
					var nodesObj = eval("(" + msg + ")");
					that.regionTree = null;
					that.loadTree(nodesObj);
				}

			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
			
		},
		//创建树
		"posAddMenu":function(){
			var that = this;			
			var url = $("#add_url").attr("href");
			var param = $("#mainForm").serialize();
			var callback = function(msg){
				if(msg=="null"){
					$("#addPosMenuBrand").show();
				}else{
					//调用加载树事件
					$("#addPosMenuBrand").hide();
					var nodesObj = eval("(" + msg + ")");
					that.regionTree = null;
					that.loadTree(nodesObj);
				}

			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
			
		},
		//编辑 初始化
		"openEdit":function(){
			$("#editButton").hide();
			$("#saveButton").show();
			$("#BrandMenuNames").show();
			$("#BrandMenuName").hide();
			
		},
		//保存修改数据
		"save":function(){
			var that = this;			
			var url = $("#save_url").attr("href");
			var param = $("#mainForm").serialize()+'&brandMenuName='+$("#BrandMenuNames").val();
			var callback = function(msg){

				if(msg=="null"){
					$("#addPosMenuBrand").show();
				}else{
					var treeId = $("#treeId").val();					
					var BrandMenuNames = $("#BrandMenuNames").val();
					if(BrandMenuNames.length!=0){
						$("#"+that.deportTree.getNodeByParam("id",treeId).tId+"_span").html($("#BrandMenuNames").val());
						that.deportTree.getNodeByParam("id",treeId).name=$("#BrandMenuNames").val();
						$("#actionResultDisplay").hide();
					}
				}
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
			
			$("#editButton").show();
			$("#saveButton").hide();
			$("#BrandMenuName").show();
			$("#BrandMenuNames").hide();
			$("#BrandMenuName").val($("#BrandMenuNames").val());
		},
		//取消
		"doClose":function(){
			$("#editButton").show();
			$("#saveButton").hide();
			$("#BrandMenuName").show();
			$("#BrandMenuNames").hide();
			$("#BrandMenuNames").val($("#BrandMenuName").val());
		},
		//显示树
		"loadTree":function(nodesObj){
			var treeSetting = {
					checkable : true,
					showLine : true,
					open : true,
					treeNodeKey : "id",               //在isSimpleData格式下，当前节点id属性  
				    treeNodeParentKey : "pId", 		  //在isSimpleData格式下，当前节点的父节点id属性  
					checkType : {
						"Y" : "",
						"N" : ""
							
					},//绑定点击树节点事件 显示节点数据
					callback: {
						click: function(event, treeId, treeNode){
							$("#posMenuBrand").show();
							$("#editButton").show();
							$("#BrandMenuName").val(treeNode.name);
							$("#BrandMenuNames").val(treeNode.name);
							$("#Container").val(treeNode.Container);
							$("#MenuValue").val(treeNode.MenuValue);
							$("#posMenuBrandID").val(treeNode.BIN_PosMenuBrandID);
							$("#editButton").show();
							$("#saveButton").hide();
							$("#BrandMenuName").show();
							$("#BrandMenuNames").hide();
							$("#treeId").val(treeNode.id);
							return true;
						},
						
						//绑定点击选择框事件
						change:function(event, treeId, treeNode){
							var that = this;	
							var url = $("#edit_url").attr("href");
							$("#posMenuBrandID").val(treeNode.BIN_PosMenuBrandID);
							var param = $("#mainForm").serialize()+'&menuStatus='+treeNode.MenuStatus;
							
							//重新加载树
							var callback = function(msg){
								$("#actionResultDisplay").hide();
								if(msg=="null"){
									$("#addPosMenuBrand").show();
								}else{
									
									if(treeNode.MenuStatus=="SHOW"){
										treeNode.MenuStatus="HIDE";
									}else{
										treeNode.MenuStatus="SHOW";
									}
								}
							};
							cherryAjaxRequest( {
								url : url,
								param : param,
								callback : callback
							});

						}

					}
			};
			this.deportTree = $("#treeDemo1").zTree(treeSetting, nodesObj);
		}
};

var BINOLMOMAN10 = new BINOLMOMAN10();

/*
 * 全局变量定义
 */
var BINOLMOMAN10_global = {};

// 刷新区分
BINOLMOMAN10_global.refresh = false;

$(function(){
//	// 表格列选中
//    $('thead th').live('click',function(){
//        $("th.sorting").removeClass('sorting');
//        $(this).addClass('sorting');
//    });
	// 画面布局初期化处理
	BINOLMOMAN10.posMenu();
	
	counterBinding({elementId:"counterCodeName",showNum:20,selected:"name"});


});