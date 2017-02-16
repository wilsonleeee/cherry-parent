function BINOLSSPRM88_proBlack(){};
BINOLSSPRM88_proBlack.prototype={
    //柜台导入
    "proBlackExeclLoad": function () {
        var $parentDiv = $("#proBlackLoadDialog");
        var $counterUpExcel = $("#proBlackUpExcel");
        var upMode =$parentDiv.find('select[name="upMode"]').val();
        var proBlackUpExcelId = 'proBlackUpExcel';
        var $counterPathExcel = $('#proBlackPathExcel');
        var operateType=$parentDiv.find("#operateType").val();
        var operateFlag=$parentDiv.find("#operateFlag").val();
        var filterType=$parentDiv.find("#filterType").val();

        var url = "/Cherry/ss/BINOLSSPRM88_proBlackExeclLoad";
        // AJAX登陆图片
        var $ajaxLoading = $parentDiv.find("#proBlackLoading");
        // 错误信息提示区
        var $errorMessage = $('#proBlackLoadDialog #actionResultDisplay');
        // 清空错误信息
        $errorMessage.hide();
        $errorMessage.empty();
        if($counterUpExcel.val()==''){
            $counterPathExcel.val('');
            $("#errorDiv").show();
            $("#errorSpan").text("请选择文件");
            return false;
        }
        $("#proBlackFail").addClass("hide");
        //var conditionType = $("#conditionType").val();
        var targetDiv = $("#targetDiv").val();
        $ajaxLoading.ajaxStart(function(){$(this).show();});
        $.ajaxFileUpload({
            url: url,
            secureuri:false,
            data:{'csrftoken':parentTokenVal(),
                'couponRule.brandInfoId':$('#brandInfoId').val(),
                'upMode': upMode,
                'filterType':filterType,
                'operateType':operateType,
                'operateFlag':operateFlag,
            },
            fileElementId:proBlackUpExcelId,
            dataType: 'html',
            success: function (msg){
                $ajaxLoading.ajaxComplete(function(){$(this).hide();});
                if(msg == null || msg == "" || msg == undefined || msg == "ERROR"){
                    return;
                }else{
                    var param_map = eval("("+msg+")");
                    var $errorMessage=$("#proBlackResult");
                    $errorMessage.empty();
                    var tips = '<span>导入成功的数量: <span class="green">' + param_map.successCount + '</span></span> ';
                    tips += '<span>导入失败的数量: <span class="red">'  + param_map.failCount + '</span></span> ';
                    $errorMessage.append(tips);
                    $errorMessage.show();
                    var productJson=param_map.productJson;
                    $("#excelProductExcept").val(productJson);
                }

                PRM88_proBlack.proBlackFailSearch();
                }
        });
    },
    //ajax dateTable加载失败数据(柜台)
    "proBlackFailSearch": function(){
        alert(1);
        var url = $("#proBlackFailSearch").attr("href");
        var csrftoken = parentTokenVal();
        var params = $("#proBlackLoadDialog #conditionForm").serialize();

        url = url + '?csrftoken='+ csrftoken + '&'+ params;
        // 显示结果一览
        $("#proBlackFail").removeClass("hide");
        // 表格设置
        var tableSetting = {
            // 表格ID
            tableId : '#proBlackFailDataTable',
            // 一页显示页数
            iDisplayLength:5,
            // 数据URL
            url : url,
            // 表格列属性设置
            aoColumns : [  { "sName": "brandCode", "sWidth": "25%", "bSortable": false },
                { "sName": "unitCode", "sWidth": "25%", "bSortable": false  },
                { "sName": "barCode", "sWidth": "15%" , "bSortable": false },
                { "sName": "productName", "sWidth": "25%", "bSortable": false},
                { "sName": "errorMsg", "sWidth": "25%", "bSortable": false }],
            // 横向滚动条出现的临界宽度
            sScrollX : "100%",
            index:98
        };
        oTableArr[98] = null;
        fixedColArr[98] = null;
        // 调用获取表格函数
        getTable(tableSetting);
    },
    "export":function(){
        var url =$("#exportExecl").attr("href");
        var params = $("#conditionForm").serialize();
        url = url + "?" + params;
        window.open(url,"_self");
    }
}
var PRM88_proBlack = new BINOLSSPRM88_proBlack();