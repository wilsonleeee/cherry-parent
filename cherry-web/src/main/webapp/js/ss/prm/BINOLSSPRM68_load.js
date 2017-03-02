function BINOLSSPRM68_load() {};

BINOLSSPRM68_load.prototype = {
    //产品导入
    "productExeclLoad": function () {
        var $parentDiv = $("#productLoadDialog");
        var $productUpExcel = $("#productUpExcel");
        var upMode =$parentDiv.find('select[name="upMode"]').val();
        var productUpExcelId = 'productUpExcel';
        var $productPathExcel = $('#productPathExcel');

        var url = $("#productExeclLoad").attr("href");

        // AJAX登陆图片
        var $ajaxLoading = $parentDiv.find("#productLoading");
        // 错误信息提示区
        var $errorMessage = $('#productLoadDialog #actionResultDisplay');
        // 清空错误信息
        $errorMessage.hide();
        $errorMessage.empty();
        if($productUpExcel.val()==''){
            $productPathExcel.val('');
            $("#errorDiv").show();
            $("#errorSpan").text("请选择文件");
            return false;
        }
        $("#productFail").addClass("hide");
        var execLoadType = $("#productLoadDialog #execLoadType").val();
        var excelProductShopping = $("#productLoadDialog #excelProductShopping").val();
        var excelProductAward = $("#productLoadDialog #excelProductAward").val();
        var excelProductALL = $("#productLoadDialog #excelProductALL").val();
        var productPageSize = $("#productLoadDialog #productPageSize").val();

        $ajaxLoading.ajaxStart(function(){$(this).show();});
        $.ajaxFileUpload({
            url:url,
            secureuri:false,
            data:{'csrftoken':parentTokenVal(),
                'brandInfoId':$('#brandInfoId').val(),
                'upMode': upMode,
                'execLoadType':execLoadType,
                'searchCode':searchCode,
                'excelProductShopping':excelProductShopping,
                'excelProductAward':excelProductAward,
                'excelProductALL' :excelProductALL,
                'productPageSize' :productPageSize
            },
            fileElementId:productUpExcelId,
            //服务端直接响应html文本
            dataType: 'html',
            success: function (msg){
                var map = JSON.parse(msg);
                //TODO
                //var productJson = map.productJson;

                $ajaxLoading.ajaxComplete(function(){$(this).hide();});
                var resultCode = map.resultCode;
                var searchCode = map.searchCode;
                if (resultCode == '0' || resultCode == '1') {
                    $("#searchCode").val(searchCode) ;
                    $("#excelProductShopping").val(JSON2.stringify(map.productJson));
                    $("#excelProductAward").val(JSON2.stringify(map.productJson));
                    // {"searchCode":"EC010031612050000037","resultCode":0,"successCount":3}
                    var tips = '<span>导入成功的数量: <span class="green">' + map.successCount + '</span></span> ';
                    tips += '<span>导入失败的数量: <span class="red">'  + map.failCount + '</span></span> ';
                    $errorMessage.empty().append(tips);
                    $errorMessage.show();
                }else {
                    $errorMessage.empty().append(map.resultMsg);
                    $errorMessage.show();
                }
                if(resultCode >0){
                    if($("#productFail").find(".dataTables_wrapper").length>0){
                        //$("#productLoadDialog #conditionForm").find("#searchCode").remove();
                        //var html='<input type="hidden" id="searchCode" name="searchCode" value="'+searchCode+'"/>';
                        //$("#productLoadDialog #conditionForm").append(html);
                        var htmlStr = '<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="productFailDataTable"><thead>';
                        if (execLoadType=="shoppingCart"){
                            htmlStr +='<tr><th>厂商编码*</th><th>产品条码*</th><th>产品名称</th><th>数量金额条件*</th><th>比较条件*</th><th>比较值*</th>';
                        }else if(execLoadType=="GIFT") {
                            htmlStr +='<tr><th>厂商编码*</th><th>产品条码*</th><th>产品名称</th><th>数量</th>';
                        }else if(execLoadType=="DPZK") {
                            htmlStr +='<tr><th>厂商编码*</th><th>产品条码*</th><th>产品名称</th><th>折扣数量大于等于*</th><th>折扣数量小于等于*</th><th>折扣</th>';
                        }else if(execLoadType=="DPTJ") {
                            htmlStr +='<tr><th>厂商编码*</th><th>产品条码*</th><th>产品名称</th><th>特价(元)*</th>';
                        }
                        htmlStr +='<th>备注</th></tr>';
                        htmlStr += '</thead><tbody></tbody></table>';
                        $("#productFail").find(".section-content").html(htmlStr);

                        binolssprm68_load.productFailSearch();
                    }else{
                        //$("#productLoadDialog #conditionForm").find("#searchCode").remove();
                        //var html='<input type="hidden" id="searchCode" name="searchCode" value="'+searchCode+'"/>';
                        //$("#productLoadDialog #conditionForm").append(html);
                        binolssprm68_load.productFailSearch();
                    }
                }
            }
        });
    },

    //ajax dateTable加载失败数据(产品)
    "productFailSearch": function(){
        var url = $("#productFailSearch").attr("href");
        var csrftoken = parentTokenVal();
        //var params = $("#productLoadDialog #conditionForm").serialize();

        var execLoadType = $("#productLoadDialog #execLoadType").val();
        var searchCode = $("#productLoadDialog #searchCode").val();
        url = url + '?csrftoken='+ csrftoken + '&execLoadType='+ execLoadType+'&searchCode='+searchCode;
        // 显示结果一览
        $("#productFail").removeClass("hide");
        // 表格设置
        if(execLoadType=="shoppingCart"){

            var tableSetting = {
                // 表格ID
                tableId : '#productFailDataTable',
                // 数据URL
                url : url,
                // 一页显示页数
                iDisplayLength:5,
                // 表格列属性设置
                aoColumns : [
                    { "sName": "unitCode", "sWidth": "20%" , "bSortable": false},
                    { "sName": "barCode", "sWidth": "5%", "bSortable": false},
                    { "sName": "productName", "sWidth": "5%", "bSortable": false},
                    { "sName": "quantityOrAmount", "sWidth": "5%", "bSortable": false},
                    { "sName": "compareCondition", "sWidth": "5%", "bSortable": false},
                    { "sName": "compareValue", "sWidth": "5%", "bSortable": false},
                    { "sName": "errorMsg", "sWidth": "20%", "bSortable": false }],
                // 横向滚动条出现的临界宽度
                sScrollX : "100%",
                index:96
            };
        }else if(execLoadType=="GIFT"){
            var tableSetting = {
                // 表格ID
                tableId : '#productFailDataTable',
                // 数据URL
                url : url,
                // 一页显示页数
                iDisplayLength:5,
                // 表格列属性设置
                aoColumns : [
                    { "sName": "unitCode", "sWidth": "20%", "bSortable": false },
                    { "sName": "barCode", "sWidth": "20%", "bSortable": false },
                    { "sName": "productName", "sWidth": "25%" , "bSortable": false},
                    { "sName": "productNum", "sWidth": "15%" , "bSortable": false},
                    { "sName": "errorMsg", "sWidth": "20%", "bSortable": false }],
                // 横向滚动条出现的临界宽度
                sScrollX : "100%",
                index:96
            };
        }else if(execLoadType=="DPZK"){
            var tableSetting = {
                // 表格ID
                tableId : '#productFailDataTable',
                // 数据URL
                url : url,
                // 一页显示页数
                iDisplayLength:5,
                // 表格列属性设置
                aoColumns : [
                    { "sName": "unitCode", "sWidth": "10%", "bSortable": false },
                    { "sName": "barCode", "sWidth": "10%", "bSortable": false },
                    { "sName": "productName", "sWidth": "15%" , "bSortable": false},
                    { "sName": "discountNumGtEq", "sWidth": "15%" , "bSortable": false},
                    { "sName": "discountNumLtEq", "sWidth": "15%" , "bSortable": false},
                    { "sName": "discountNum", "sWidth": "10%" , "bSortable": false},
                    { "sName": "errorMsg", "sWidth": "20%", "bSortable": false }],
                // 横向滚动条出现的临界宽度
                sScrollX : "100%",
                index:96
            };
        }else if(execLoadType=="DPTJ"){
            var tableSetting = {
                // 表格ID
                tableId : '#productFailDataTable',
                // 数据URL
                url : url,
                // 一页显示页数
                iDisplayLength:5,
                // 表格列属性设置
                aoColumns : [
                    { "sName": "unitCode", "sWidth": "20%", "bSortable": false },
                    { "sName": "barCode", "sWidth": "20%", "bSortable": false },
                    { "sName": "productName", "sWidth": "25%" , "bSortable": false},
                    { "sName": "specialPrice", "sWidth": "15%" , "bSortable": false},
                    { "sName": "errorMsg", "sWidth": "20%", "bSortable": false }],
                // 横向滚动条出现的临界宽度
                sScrollX : "100%",
                index:96
            };
        }


        oTableArr[96] = null;
        fixedColArr[96] = null;
        // 调用获取表格函数
        getTable(tableSetting);
    },
    "export":function(){
        var url =$("#exportExecl").attr("href");
        var execLoadType = $("#productLoadDialog #execLoadType").val();
        var searchCode = $("#productLoadDialog #searchCode").val();
        //	var csrftoken = parentTokenVal();
        var params = "execLoadType="+execLoadType+"&searchCode="+searchCode;
        url = url + "?" + params;
        window.open(url,"_self");
    },
    "cleanSearchCode":function(obj){
        var $searchCode = $(obj).parent('.loadDialog').find('#searchCode');
        $searchCode.val('');
    }
};

var binolssprm68_load =  new BINOLSSPRM68_load();