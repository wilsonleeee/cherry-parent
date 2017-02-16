function BINOLSSPRM88_counter(){};
BINOLSSPRM88_counter.prototype={
    //柜台导入
    "counterExeclLoad": function () {
        var $parentDiv = $("#counterLoadDialog");
        var $counterUpExcel = $("#counterUpExcel");
        var upMode =$parentDiv.find('select[name="upMode"]').val();
        var counterUpExcelId = 'counterUpExcel';
        var $counterPathExcel = $('#counterPathExcel');
        var operateType=$parentDiv.find("#operateType").val();
        var searchCode=$parentDiv.find("#searchCode").val();
        var counterList=$parentDiv.find("#counterList").val();
        var filterType=$parentDiv.find("#filterType").val();
        var excelCounter_w=$parentDiv.find("#excelCounter_w").val();
        var excelCounter_b=$parentDiv.find("#excelCounter_b").val();

        var url = "/Cherry/ss/BINOLSSPRM88_counterExeclLoad";
        // AJAX登陆图片
        var $ajaxLoading = $parentDiv.find("#counterLoading");
        // 错误信息提示区
        var $errorMessage = $('#counterLoadDialog #actionResultDisplay');
        // 清空错误信息
        $errorMessage.hide();
        $errorMessage.empty();
        if($counterUpExcel.val()==''){
            $counterPathExcel.val('');
            $("#errorDiv").show();
            $("#errorSpan").text("请选择文件");
            return false;
        }
        $("#counterFail").addClass("hide");
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
                'searchCode':searchCode,
                'counterList':counterList,
                'excelCounter_w':excelCounter_w,
                'excelCounter_b':excelCounter_b
            },
            fileElementId:counterUpExcelId,
            dataType: 'html',
            success: function (msg){
                $ajaxLoading.ajaxComplete(function(){$(this).hide();});
                var map = JSON.parse(msg);
                var resultCode = map.resultCode;
                var searchCode = map.searchCode;
                $errorMessage.append(map.resultMsg);
                $errorMessage.show();
                //if(msg == null || msg == "" || msg == undefined || msg == "ERROR"){
                //    return;
                //}else{
                if (resultCode == '0' || resultCode == '1') {
                    $("#searchCode").val(searchCode);
                    var param_map = eval("(" + msg + ")");
                    //$errorMessage=$("#counterResult");
                    //$errorMessage.empty();
                    var tips = '<span>导入成功的数量: <span class="green">' + param_map.successCount + '</span></span> ';
                    tips += '<span>导入失败的数量: <span class="red">' + param_map.failCount + '</span></span> ';
                    $errorMessage.append(tips);
                    $errorMessage.show();
                    if (filterType=='1'){
                        var trueCounterList=param_map.trueCounterList;
                        $("#excelCounter_w").val(JSON2.stringify(trueCounterList));
                    }else{
                        var counterSuccessList=param_map.counterSuccessList;
                        $("#excelCounter_b").val(JSON2.stringify(counterSuccessList));
                    }

                }
                if(resultCode>0){
                    //var failCount=param_map.failCount;
                    //if (failCount>0){
                    //    PRM88_counter.counterFailSearch();
                    //}
                    if($("#counterFail").find(".dataTables_wrapper").length>0){
                        //$("#counterLoadDialog #conditionForm").find("#operateFlag").remove();
                        //var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
                        //$("#counterLoadDialog #conditionForm").append(html);
                        var htmlStr = '<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="counterFailDataTable"><thead>';
                        htmlStr += ' <tr><th>品牌*</th><th>柜台号*</th><th>柜台名称</th><th>备注</th></tr>';
                        htmlStr += '</thead><tbody></tbody></table>';
                        $("#counterFail").find(".section-content").html(htmlStr);
                        PRM88_counter.counterFailSearch();
                    }else {
                        //$("#counterLoadDialog #conditionForm").find("#operateFlag").remove();
                        //var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
                        //$("#counterLoadDialog #conditionForm").append(html);
                        PRM88_counter.counterFailSearch();
                    }
                }
            }
        });
    },
    //ajax dateTable加载失败数据(柜台)
    "counterFailSearch": function(){
        var url = $("#counterFailSearch").attr("href");
        var csrftoken = parentTokenVal();
        var params = $("#counterLoadDialog #conditionForm").serialize();

        url = url + '?csrftoken='+ csrftoken + '&'+ params;
        // 显示结果一览
        $("#counterFail").removeClass("hide");
        // 表格设置
        var tableSetting = {
            // 表格ID
            tableId : '#counterFailDataTable',
            // 一页显示页数
            iDisplayLength:5,
            // 数据URL
            url : url,
            // 表格列属性设置
            aoColumns : [  { "sName": "brandCode", "sWidth": "25%", "bSortable": false },
                { "sName": "counterCode", "sWidth": "25%", "bSortable": false  },
                { "sName": "counterName", "sWidth": "15%" , "bSortable": false },
                { "sName": "errorMsg", "sWidth": "25%", "bSortable": false }],
            // 横向滚动条出现的临界宽度
            sScrollX : "100%",
            index:98
        };
        oTableArr[98] = null;
        fixedColArr[98] = null;
        // 调用获取表格函数
        //alert("FFF");
        getTable(tableSetting);
    },
    "export":function(){
        var url =$("#exportExecl").attr("href");
        var params = $("#conditionForm").serialize();
        url = url + "?" + params;
        window.open(url,"_self");
    }
}
var PRM88_counter = new BINOLSSPRM88_counter();