function BINOLSTSFH06() {
};


BINOLSTSFH06.prototype = {
    "sortFlag": "",
    "submitflag": true,
    "saveOrSubmitFlag": true,
    "canceldetailHtml": "",
    "getHtmlFun": function (info, negativeFlag) {

        var productVendorId = info.productVendorId;//产品厂商Id
        var unitCode = info.unitCode;//厂商编码
        var barCode = info.barCode;//产品条码
        var nameTotal = info.nameTotal;//产品名称
        var price = info.salePrice;//销售价格
        var costPrice = info.costPrice;//成本价
        var totalCostPrice = info.totalCostPrice;//总成本价

        var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算

        var sysConfigUsePrice = $("#sysConfigUsePrice").val();
        if (sysConfigUsePrice == "MemPrice") {
            price = info.memPrice;//会员价格
        } else if (sysConfigUsePrice == "StandardCost") {
            price = info.standardCost;// 结算价
        }
        if (isEmpty(price)) {
            price = parseFloat("0.0");
        } else {
            price = parseFloat(price);
        }
        price = price.toFixed(2);

        if (costPrice != undefined) {
            if (!isEmpty(costPrice)) {
                costPrice = parseFloat(costPrice).toFixed(2);
            }
        } else {
            costPrice = "";
        }

        if (totalCostPrice != undefined) {
            if (!isEmpty(totalCostPrice)) {
                totalCostPrice = parseFloat(totalCostPrice).toFixed(2);
            }
        } else {
            totalCostPrice = "";
        }

        var curStock = info.curStock;//当前库存
        if (isEmpty(curStock)) {
            curStock = 0;
        }
        var inDepartStock = info.inDepartStock;//发货方库存
        if (isEmpty(inDepartStock)) {
            inDepartStock = 0;
        }
        var quantity = info.quantity;//数量
        if (isEmpty(quantity)) {
            quantity = "";
        }
        if (!isEmpty(quantity) && negativeFlag) {
            quantity = Number(quantity) * (-1);
        }

        var amount = parseFloat("0.0");
        if (useCostPrice == "0") {//使用参考价
            amount = price * Number(quantity);//金额
        }
        else {//使用成本价
            if (isEmpty(costPrice)) {
                costPrice = "";
                amount = 0 * Number(quantity);//金额
            }
            else {
                amount = costPrice * Number(quantity);//金额
            }
        }


        amount = amount.toFixed(2);
        var reason = escapeHTMLHandle(info.reason);//备注
        if (quantity == 0) {
            quantity = "";
        }
        if (isEmpty(reason)) {
            reason = "";
        }
        var nextIndex = parseInt($("#rowNumber").val()) + 1;
        $("#rowNumber").val(nextIndex);
        var configVal = $("#configVal").val();
        var html = '<tr id="dataRow' + nextIndex + '">';
        html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH06.changechkbox(this);"/></td>';
        html += '<td>' + unitCode + '<input type="hidden" id="unitCodeArr"  value="' + unitCode + '"/></td>';
        if (configVal == "1") {
            html += '<td style="text-align:right;"><input type="text" id="erpCodeArr" name="erpCodeArr"/></td>';
        }
        html += '<td>' + barCode + '<input type="hidden" id="barCodeArr" value="' + barCode + '"/></td>';
        html += '<td>' + nameTotal + '</td>';
        html += '<td class="center"><input value="' + quantity + '" name="quantityuArr" id="quantityuArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9"  onchange="STSFH06_calcuAmount(this)" onkeyup="STSFH06_calcuAmount(this)" cssStyle="width:98%"/></td>';
        html += '<td id="money" class="center" style="text-align:right;">' + amount + '</td>';
        html += '<td style="text-align:right;" >' + BINOLSTSFH06.formateMoney(price, 2) + '</td>';
        html += '<td style="text-align:right;" ><div id="costPrice">' + costPrice + '</div></td>';
        html += '<td style="text-align:right;" ><div id="totalCostPrice">' + totalCostPrice + '</div></td>';
        if (useCostPrice == "1") {
            if (!isEmpty(costPrice)) {
                html += '<td style="text-align:right;" ><input type="text" id="priceUnitArr" class="price" name= "priceUnitArr" value="' + costPrice + '" onchange="BINOLSTSFH06.setPrice(this);"/></td>';
            } else {
                html += '<td style="text-align:right;" ><input type="text" id="priceUnitArr" class="price" name= "priceUnitArr" value="0.00" onchange="BINOLSTSFH06.setPrice(this);"/></td>';
            }
        }
        else {
            html += '<td style="text-align:right;" ><input type="text" id="priceUnitArr" class="price" name= "priceUnitArr" value="' + price + '" onchange="BINOLSTSFH06.setPrice(this);"/></td>';
        }
        html += '<td id="nowCount" style="text-align:right;">' + BINOLSTSFH06.formateMoney(curStock, 0) + '</td>';
        html += '<td id="receiveStock" style="text-align:right;">' + BINOLSTSFH06.formateMoney(inDepartStock, 0) + '</td>';
        html += '<td class="center"><input value="' + reason + '" name="reasonArr"  type="text" id="reasonArr" size="25" maxlength="200"  /></td>';
        html += '<td style="display:none">'
            + '<input type="hidden" name="prtVendorId" value="' + productVendorId + '"/>'
            + '<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="' + productVendorId + '"/>'
            + '<input type="hidden" id="costPriceArr" name="costPriceArr" value="' + costPrice + '"/>'
            + '<input type="hidden" id="referencePriceArr" name="referencePriceArr" value="' + price + '"/></td></tr>';
        return html;
    },
    /**
     * AJAX取得产品当前库存量
     *
     * */
    "getPrtStock": function (productVendorID) {
        var url = $("#s_getStockCount").html();
        var lenTR = $("#databody").find("tr");
        var params = getSerializeToken();
        params += "&" + $("#outDepotId").serialize();
        params += "&" + $("#outLoginDepotId").serialize();
        if (productVendorID == undefined) {
            //查明细全部所有
            params += "&" + $("#databody").find("[name='productVendorIDArr']").serialize();
        } else {
            //查当前行的产品
            params += "&productVendorIDArr=" + productVendorID;
        }
        if (lenTR.length > 0) {
            ajaxRequest(url, params, function (json) {
                for (var one in json) {
                    var prtVendorId = json[one].prtVendorId;
                    if (prtVendorId == "") {
                        continue;
                    }
                    var stock = BINOLSTSFH06.formateMoney(json[one].stock, 0);
                    $("#databody").find("tr").each(function () {
                        var $tr = $(this);
                        if ($tr.find("@[name='prtVendorId']").val() == prtVendorId) {
                            $tr.find("#nowCount").text(stock);
                        }
                    });
                }
            }, "json");
        }
    },

    /**
     * AJAX取得当前产品在收货方的库存量
     */
    "getReceivePrtStock": function (productVendorID) {
        var url = $("#s_getReceiveStockCount").html();
        var lenTR = $("#databody").find("tr");
        var params = getSerializeToken();
        params += "&" + $("#inOrganizationId").serialize();
        if (productVendorID == undefined) {
            //查明细全部所有
            params += "&" + $("#databody").find("[name='productVendorIDArr']").serialize();
        } else {
            //查当前行的产品
            params += "&productVendorIDArr=" + productVendorID;
        }
        if (lenTR.length > 0) {
            ajaxRequest(url, params, function (json) {
                for (var one in json) {
                    var prtVendorId = json[one].prtVendorId;
                    if (prtVendorId == "") {
                        continue;
                    }
                    var stock = BINOLSTSFH06.formateMoney(json[one].stock, 0);
                    $("#databody").find("tr").each(function () {
                        var $tr = $(this);
                        if ($tr.find("@[name='prtVendorId']").val() == prtVendorId) {
                            $tr.find("#receiveStock").text(stock);
                        }
                    });
                }
            }, "json");
        }
    },

    /**
     * 产品弹出框
     *
     * */
    "openProPopup": function () {
        if ($("#spanBtnadd.ui-state-disabled").length > 0) {
            return;
        }
        var that = this;
        STSFH06_clearActionMsg();
        if ($('#outOrganizationId').val() == null || $('#outOrganizationId').val() == "" || $("#outOrganizationId").val() == "0") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
            $('#errorDiv2').show();
            return false;
        }
        if ($("#outDepotId option:selected").val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
            $('#errorDiv2').show();
            return false;
        }
        /**
         * 产品一览中要显示收货方库存，必须有收货方部门参数
         */
        if ($('#inOrganizationId').val() == null || $('#inOrganizationId').val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noinDepart').val());
            $('#errorDiv2').show();
            return false;
        }

        // 产品弹出框属性设置
        var option = {
            targetId: "databody",//目标区ID
            checkType: "checkbox",// 选择框类型
            mode: 2, // 模式
            brandInfoId: $("#brandInfoId").val(),// 品牌ID
            getHtmlFun: that.getHtmlFun,// 目标区追加数据行function
            click: function () {//点击确定按钮之后的处理
                var checkbox = $('#dataTableBody').find(':input').is(":checked");
                //改变奇偶行的样式
                STSFH06_changeOddEvenColor();
                // 拖动排序,禁用拖动排序
                //$("#mainTable #databody").sortable({
                //	update: function(event,ui){
                //		BINOLSTSFH06.changeOddEvenColor();
                //		//计算总金额总数量
                //		BINOLSTSFH06.calcuTatol();
                //		//拖动后重新绑定
                //		BINOLSTSFH06.bindInput();
                //	},
                //	beforeStop: function( event, ui ) {
                //		BINOLSTSFH06.setPrice(ui.item.find("[name='priceUnitArr']"));
                //	}
                //});
                if ($("#databody > tr").length > 0) {//禁掉生成发货单按钮
                    $("#spanBtnStart").addClass("ui-state-disabled");
                    $("#spanBtnSuggest").addClass("ui-state-disabled");
                } else {
                    $("#spanBtnStart").removeClass("ui-state-disabled");
                    $("#spanBtnSuggest").removeClass("ui-state-disabled");

                    BINOLSTSFH06.canceProductResult(true);
                    BINOLSTSFH06.canceProductResultSuggest(true);
                }
                // 设置全选状态
                var $checkboxs = $('#databody').find(':checkbox');
                var $unChecked = $('#databody').find(':checkbox:not(":checked")');
                if ($unChecked.length == 0 && $checkboxs.length > 0) {
                    $('#allSelect').prop("checked", true);
                } else {
                    $('#allSelect').prop("checked", false);
                }
                // AJAX取得产品当前库存量
                that.getPrtStock();
                // AJAX取得产品在收货部门的库存量
                that.getReceivePrtStock();

                //计算总金额总数量
                BINOLSTSFH06.calcuTatol();

                BINOLSTSFH06.bindInput();

                //重新绑定下拉框
                $("#databody tr [name='unitCodeBinding']").each(function () {
                    var unitCode = $(this).attr("id");
                    BINOLSTSFH06.setProductBinding(unitCode);
                })
            }
        };
        // 调用产品弹出框共通
        popAjaxPrtDialog(option);

    },
    "changeOddEvenColor": function () {
        $("#databody tr:odd").attr("class", "even");
        $("#databody tr:even").attr("class", "odd");
    },

    "changeOddEvenColor1": function () {
        $("#databody tr:odd").attr("class", function () {
            if ($(this).attr("class") == "errTRbgColor") {

            } else {
                $(this).addClass("odd");
            }

        });
        $("#databody tr:even").attr("class", function () {
            if ($(this).attr("class") == "errTRbgColor") {

            } else {
                $(this).addClass("even");
            }

        });
    },
    "changechkbox": function (obj) {
        var chked = $(obj).prop("checked");
        if (!chked) {
            $('#allSelect').prop("checked", false);
            return false;
        }
        var flag = true;
        $("#databody :checkbox").each(function (i) {
            if (i >= 0) {
                if ($(this).prop("checked") != true) {
                    flag = false;
                }
            }

        });
        if (flag) {
            $('#allSelect').prop("checked", true);
        }
    },
    /**
     * 计算总数量，总金额
     * */
    "calcuTatol": function () {
        var rows = $("#databody").children();
        var totalQuantity = 0;
        var totalAmount = 0.00;
        if (rows.length > 0) {
            rows.each(function (i) {
                var $tds = $(this).find("td");
                var $inputVal = $tds.find("#quantityuArr").val();
                if (isNaN($inputVal)) {
                    $inputVal = 0;
                }
                totalQuantity += Number($inputVal);

                if ($(this).find("#money").length > 0) {
                    totalAmount += parseFloat($(this).find("#money").html().replace(/,/g, ""));
                }
            });
        }
        $("#totalQuantity").html(BINOLSTSFH06.formateMoney(totalQuantity, 0));
        $("#totalAmount").html(BINOLSTSFH06.formateMoney(totalAmount, 2));
    },
    /**
     * 生成空白单
     */
    "STSFH06_addblankbill": function () {
        if (!($("#spanBtnStart").is(":visible") && $("#spanBtnStart.ui-state-disabled").length == 0)) {
            return;
        }
        STSFH06_clearActionMsg();
        if ($('#outOrganizationId').val() == null || $('#outOrganizationId').val() == "" || $("#outOrganizationId").val() == "0") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
            $('#errorDiv2').show();
            return false;
        }
        if ($("#outDepotId option:selected").val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
            $('#errorDiv2').show();
            return false;
        }
        /**
         * 产品一览中要显示收货方库存，必须有收货方部门参数
         */
        if ($('#inOrganizationId').val() == null || $('#inOrganizationId').val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noinDepart').val());
            $('#errorDiv2').show();
            return false;
        }
        var url = $("#addBlankBillUrl").attr("href");
        var params = $("#mainForm").serialize();
        params = params + "&csrftoken=" + $("#csrftoken").val();
        $('#spanBtnStart').hide();
        $('#spanBtnCancel').show();
        $("#spanBtnadd").addClass("ui-state-disabled");
        //$("#spanBtdelete").addClass("ui-state-disabled");
//			$('#spanBtnadd').attr('disabled',true);
        $("#billCopy").addClass("ui-state-disabled");
//			$("#billCopy").attr('disabled',true);
        $("#spanBtnAddNewLine").addClass("ui-state-disabled");
//			$("#spanBtnAddNewLine").attr('disabled',true);
        $("#spanBtnSuggest").addClass("ui-state-disabled");
        $("#btnState").val("addblankbill");
        if (BINOLSTSFH06.submitflag) {
            BINOLSTSFH06.submitflag = false;
            $("#suggestBill_processing").show();
            ajaxRequest(url, params, BINOLSTSFH06.ShowProductResult);
        }
    },
    /**
     * 显示空白单
     * @return
     */
    "ShowProductResult": function (msg) {
        //判断是否已在空白状态
        if ($("#btnState").val() == "") {
            return;
        }
        $("#suggestBill_processing").hide();
        $("#inquirelblcode").val("");
        $('#inquiretab').show();
        STSFH06_clearActionMsg();
        $('#ajaxerrdiv').empty();
        $('#mydetail').show();
        $('#canceldetail').empty();
        submitflag = true;
        $('#mydetail').html(msg);
        BINOLSTSFH06.changeOddEvenColor();
        BINOLSTSFH06.bindInput();
        BINOLSTSFH06.calcuTatol();
    },

    /**
     * 取消自动生成单据（空白单、建议单）
     */
    "cancelAutoCreate": function (flag) {
        $("#suggestBill_processing").hide();
        $('#inquiretab').hide();
        if (flag) {
            $("#canceldetail").html(BINOLSTSFH06.canceldetailHtml);
        }
        if ($('#errorDiv2 #errorSpan2').is(":visible")) {
            STSFH06_clearActionMsg();
        }
        $('#spanBtnCancel').hide();
        $('#spanBtnStart').show();
        $('#spanCancelSuggest').hide();
        $('#spanBtnCancelSuggest').hide();
        $('#spanBtnDelNoNeed').hide();
        $("#spanBtnSuggest").show();
        $("#spanBtnadd").removeClass("ui-state-disabled");
        $("#billCopy").removeClass("ui-state-disabled");
        $("#spanBtnAddNewLine").removeClass("ui-state-disabled");
        $("#spanBtnStart").removeClass("ui-state-disabled");
        $("#spanBtnSuggest").removeClass("ui-state-disabled");
        $('#mydetail').empty();
        $("#totalQuantity").html("0");
        $("#totalAmount").html("0.00");
        BINOLSTSFH06.submitflag = true;
        $("#btnState").val("");
    },

    /**
     * 取消空白单
     * @return
     */
    "canceProductResult": function (flag) {
        if (!($("#spanBtnCancel").is(":visible") && $("#spanBtnCancel.ui-state-disabled").length == 0)) {
            return;
        }
        BINOLSTSFH06.cancelAutoCreate(flag);
    },
    /**
     * 复制发货单
     * @return
     */
    "copyDeliver": function () {
        if ($("#billCopy.ui-state-disabled").length > 0) {
            return;
        }
        if ($('#outOrganizationId').val() == null || $('#outOrganizationId').val() == "" || $("#outOrganizationId").val() == "0") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
            $('#errorDiv2').show();
            return false;
        }
        if ($("#outDepotId option:selected").val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
            $('#errorDiv2').show();
            return false;
        }
        /**
         * 产品一览中要显示收货方库存，必须有收货方部门参数
         */
        if ($('#inOrganizationId').val() == null || $('#inOrganizationId').val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noinDepart').val());
            $('#errorDiv2').show();
            return false;
        }
        STSFH06_clearActionMsg();
        var callback = function () {
            var $selected = $('#deliverDataTable').find(':input[checked]').parents("tr");
            //原单数量取反
            var negativeFlag = $('#checkboxType').is(":checked");
            if ($selected.length > 0) {
                var deliverId = $selected.find("[name=deliverId]").val();
                var receiveDepId = $selected.find("[name=receiveDepId]").val();
                var departNameReceive = $selected.find("[name=departNameReceive]").val();
                //清空原先发货单
                STSFH06_clearDetailData(true);
                BINOLSTSFH06.getProDeliverDetail(negativeFlag, deliverId, receiveDepId, departNameReceive);
            }
        };
        var param = "organizationId=" + $('#outOrganizationId').val() + "&depotInfoId=" + $('#outDepotId').val();
        popDataTableOfProDeliver(null, param, callback);
    },
    /**
     * 取得促销品发货单详单
     * @return
     */
    "getProDeliverDetail": function (negativeFlag, deliverId, receiveDepId, departNameReceive) {
        var that = this;
        var callback = function (msg) {
            var jsons = eval(msg);
            for (var i = 0; i < jsons.length; i++) {
                var html = that.getHtmlFun(jsons[i], negativeFlag);
                $("#databody").append(html);
            }
            //添加了新行，去除全选框的选中状态
            $('#allSelect').prop("checked", false);
//				$("#spanBtnStart").unbind("click");
            $("#spanBtnStart").addClass("ui-state-disabled");
            $("#spanBtnSuggest").addClass("ui-state-disabled");
            BINOLSTSFH06.changeOddEvenColor();
            //计算总金额总数量
            BINOLSTSFH06.calcuTatol();
            BINOLSTSFH06.bindInput();
        };
        //ajax取得选中的发货单详细
        /**
         * 新增的organizationIdReceive用于显示收货方库存情况
         * **/
        var param = "deliverId=" + deliverId + "&organizationId="
            + $("#outOrganizationId").val() + "&depotInfoId="
            + $("#outDepotId").val() + "&logicDepotInfoId="
            + $("#outLoginDepotId").val() + "&organizationIdReceive="
            + $("#inOrganizationId").val();
        cherryAjaxRequest({
            url: $("#s_getDeliverDetail").html(),
            reloadFlg: true,
            param: param,
            callback: callback
        });
    },

    /**
     * 提交前对数据进行检查
     * 检查发货数量是否大于库存
     * @returns {Boolean}
     */
    "hasWarningData": function () {
        var flag = false;
        //检查有无空行
        $.each($('#databody >tr'), function (i) {
            if (i >= 0) {
                if (Number($(this).find("#quantityuArr").val()) > Number($(this).find("#nowCount").text().replace(/,/g, ""))) {
                    flag = true;
                    $(this).attr("class", "errTRbgColor");
                } else {
                    $(this).removeClass('errTRbgColor');
                }
            }
        });
        return flag;
    },

    /**
     * 产品下拉输入框选中后执行方法
     */
    "pbAfterSelectFun": function (info) {
        $('#' + info.elementId).val(info.unitCode);
        // 判斷是否扫描枪输入
        var isScanMachineFlag = $('#' + info.elementId).data("scanMachineFlag") == undefined ? false : $('#' + info.elementId).data("scanMachineFlag");
        //验证产品厂商ID是否重复
        var flag = true;

        $.each($('#databody >tr'), function (i) {
            var productVendorID = $(this).find("[name='productVendorIDArr']").val();

            if(productVendorID != undefined && productVendorID != null && productVendorID != "") {

                if (productVendorID == info.prtVendorId) {
                    flag = false;
                    // 给重复行添加样式
                    $(this).attr("class","errTRbgColor");
                    var $this = $(this).find("#quantityuArr");
                    var quantity = $(this).find("#quantityuArr").val();
                    var newQuantity = Number(quantity) + 1;
                    // 给重复行的数量加1
                    $this.val(newQuantity);
                    // 计算折扣后金额
                    STSFH06_calcuAmount($this);
                } else {
                    // 恢复非重复行的样式
                    $(this).removeClass('errTRbgColor');
                }
            }
        });

        if(flag) {
            if(isScanMachineFlag) {
                $("#errorDiv2").hide();

                var price = info.price;//销售价格
                var sysConfigUsePrice = $("#sysConfigUsePrice").val();
                if (sysConfigUsePrice == "MemPrice") {
                    price = info.memPrice;//会员价格
                } else if (sysConfigUsePrice == "StandardCost") {
                    price = info.standardCost;// 结算价
                }

                //设置隐藏值
                $('#' + info.elementId).parent().parent().find("[name='prtVendorId']").val(info.prtVendorId);
                $('#' + info.elementId).parent().parent().find("[name='productVendorIDArr']").val(info.prtVendorId);
                $('#' + info.elementId).parent().parent().find("[name='referencePriceArr']").val(price);
                $('#' + info.elementId).parent().parent().find("#unitCodeArr").val(info.unitCode);
                $('#' + info.elementId).parent().parent().find("#barCodeArr").val(info.barCode);

                //设置显示值
                $('#' + info.elementId).parent().parent().find("#spanUnitCode").html(info.unitCode);
                $('#' + info.elementId).parent().parent().find("#spanBarCode").html(info.barCode);
                $('#' + info.elementId).parent().parent().find("#spanProductName").html(info.productName);
                $('#' + info.elementId).parent().parent().find("#spanPrice").html(BINOLSTSFH06.formateMoney(price, 2));
                $('#' + info.elementId).parent().parent().find("#priceUnitArr").val(price);

                //取消该文本框的自动完成并隐藏。
                $('#' + info.elementId).unautocomplete();
                $('#' + info.elementId).hide();

                //跳到数量文本框
                var $quantityInputText = $('#' + info.elementId).parent().parent().find("#quantityuArr");

                //选中一个产品后后默认发货数量1
                var quantity = $('#' + info.elementId).parent().parent().find("#quantityuArr").val();
                if (quantity == "") {
                    $('#' + info.elementId).parent().parent().find("#quantityuArr").val("1");
                }
                //计算金额（防止先输入数量后输入产品）
                STSFH06_calcuAmount($quantityInputText);
                //查询库存
                BINOLSTSFH06.getPrtStock(info.prtVendorId);
                //查询接收方库存
                BINOLSTSFH06.getReceivePrtStock(info.prtVendorId);

                // 扫码枪模式下,前面输入的产品无重复产品,此时新增一行
                BINOLSTSFH06.addNewLine(true);
                // 最后一行第一个可见的文本框获得焦点
                //$('#databody >tr').find("input:text:visible:first").select();

            } else {
                //alert("this is old model");
                // 普通输入模式
                //$.each($('#databody >tr'), function (i) {
                //    var productVendorID = $(this).find("[name='productVendorIDArr']").val();
                //    if(productVendorID != null && productVendorID != "" && productVendorID != undefined) {
                //        if (productVendorID == info.prtVendorId) {
                //            flag = false;
                //            $("#errorSpan2").html($("#errmsg_EST00035").val());
                //            $("#errorDiv2").show();
                //            return;
                //        }
                //    }
                //});
                $("#errorDiv2").hide();
                var price = info.price;//销售价格
                var sysConfigUsePrice = $("#sysConfigUsePrice").val();
                if (sysConfigUsePrice == "MemPrice") {
                    price = info.memPrice;//会员价格
                } else if (sysConfigUsePrice == "StandardCost") {
                    price = info.standardCost;// 结算价
                }
                //设置隐藏值
                $('#' + info.elementId).parent().parent().find("[name='prtVendorId']").val(info.prtVendorId);
                $('#' + info.elementId).parent().parent().find("[name='productVendorIDArr']").val(info.prtVendorId);
                $('#' + info.elementId).parent().parent().find("[name='referencePriceArr']").val(price);
                $('#' + info.elementId).parent().parent().find("#unitCodeArr").val(info.unitCode);
                $('#' + info.elementId).parent().parent().find("#barCodeArr").val(info.barCode);

                //设置显示值
                $('#' + info.elementId).parent().parent().find("#spanUnitCode").html(info.unitCode);
                $('#' + info.elementId).parent().parent().find("#spanBarCode").html(info.barCode);
                $('#' + info.elementId).parent().parent().find("#spanProductName").html(info.productName);
                $('#' + info.elementId).parent().parent().find("#spanPrice").html(BINOLSTSFH06.formateMoney(price, 2));
                $('#' + info.elementId).parent().parent().find("#priceUnitArr").val(price);

                //取消该文本框的自动完成并隐藏。
                $('#' + info.elementId).unautocomplete();
                $('#' + info.elementId).hide();

                ////跳到下一个文本框
                //var nxtIdx = $('#'+info.elementId).parent().parent().find("input:text").index($('#'+info.elementId)) + 1;
			    //var $nextInputText =  $('#'+info.elementId).parent().parent().find("input:text:eq("+nxtIdx+")");
                //跳到数量文本框
                var $quantityInputText = $('#' + info.elementId).parent().parent().find("#quantityuArr");
                //选中一个产品后后默认发货数量1
                var quantity = $('#' + info.elementId).parent().parent().find("#quantityuArr").val();
                if (quantity == "") {
                    $('#' + info.elementId).parent().parent().find("#quantityuArr").val("1");
                }
                //计算金额（防止先输入数量后输入产品）
                STSFH06_calcuAmount($quantityInputText);
                // 金额文本框中获取焦点
                $quantityInputText.select();
                // 给输入框绑定事件等操作
                //BINOLSTSFH06.bindInput(isScanMachineFlag);
                //查询库存
                BINOLSTSFH06.getPrtStock(info.prtVendorId);
                //查询接收方库存
                BINOLSTSFH06.getReceivePrtStock(info.prtVendorId);
            }
        } else {
            // 当前输入框信息清空
            $('#' + info.elementId).val("");
        }

    },

    /**
     * 添加新行
     */
    "addNewLine": function (isScanMachineFlag) {
        if ($("#spanBtnAddNewLine.ui-state-disabled").length > 0) {
            return;
        }
        STSFH06_clearActionMsg();
        if ($('#outOrganizationId').val() == null || $('#outOrganizationId').val() == "" || $("#outOrganizationId").val() == "0") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
            $('#errorDiv2').show();
            return false;
        }
        if ($("#outDepotId option:selected").val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
            $('#errorDiv2').show();
            return false;
        }
        /**
         * 产品一览中要显示收货方库存，必须有收货方部门参数
         */
        if ($('#inOrganizationId').val() == null || $('#inOrganizationId').val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noinDepart').val());
            $('#errorDiv2').show();
            return false;
        }

        // 设置全选状态
        $('#allSelect').prop("checked", false);
        //禁掉生成发货单按钮
//		$("#spanBtnStart").unbind("click");	
        $("#spanBtnStart").addClass("ui-state-disabled");
        $("#spanBtnSuggest").addClass("ui-state-disabled");

        //已有空行不新增一行
        var addNewLineFlag = true;
        $.each($('#databody >tr'), function (i) {
            if ($(this).find("[name='unitCodeBinding']").is(":visible")) {
                addNewLineFlag = false;
                $(this).find("[name='unitCodeBinding']").focus();
                return;
            }
        });

        if (addNewLineFlag) {
            var nextIndex = parseInt($("#rowNumber").val()) + 1;
            $("#rowNumber").val(nextIndex);
            var configVal = $("#configVal").val();
            var html = '<tr id="dataRow' + nextIndex + '">';
            html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH06.changechkbox(this);"/></td>';
            html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_' + nextIndex + '" name="unitCodeBinding"  value=""/><input type="hidden" id="unitCodeArr"></td>';
            if (configVal == "1") {
                html += '<td><span id="spanErpCode"></span><input type="text" id="erpCodeArr" name="erpCodeArr" /></td>';
            }
            html += '<td><span id="spanBarCode"></span><input type="hidden" id="barCodeArr"></td>';
            html += '<td><span id="spanProductName"></span></td>';
            html += '<td class="center"><input  value="" name="quantityuArr" id="quantityuArr" cssClass="text-number" style="width:120px;text-align:right;" size="10"  onchange="STSFH06_calcuAmount(this)" onkeyup="STSFH06_calcuAmount(this)"  cssStyle="width:98%"/></td>';
            html += '<td id="money" class="center" style="text-align:right;">0.00</td>';
            html += '<td style="text-align:right;"><span id="spanPrice"/></span></td>';
            html += '<td style="text-align:right;"><div id="costPrice"></div></td>';
            html += '<td style="text-align:right;"><div id="totalCostPrice"></div></td>';
            html += '<td style="text-align:right;"><input type="text" id="priceUnitArr" class="price" name= "priceUnitArr" value="" onchange="BINOLSTSFH06.setPrice(this);"/></td>';
            html += '<td id="nowCount" style="text-align:right;"></td>';
            html += '<td id="receiveStock" style="text-align:right;"></td>';
            html += '<td class="center"><input value="" name="reasonArr"  type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
            html += '<td style="display:none">'
                + '<input type="hidden" name="prtVendorId" value=""/>'
                + '<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
                + '<input type="hidden" id="costPriceArr" name="costPriceArr" value=""/>'
                + '<input type="hidden" id="referencePriceArr" name="referencePriceArr" value=""/></td></tr>';
            $("#databody").append(html);

            var unitCode = "unitCodeBinding_" + nextIndex;
            // 绑定产品下拉列表
            BINOLSTSFH06.setProductBinding(unitCode);
            // 给输入框绑定事件等操作
            BINOLSTSFH06.bindInput(isScanMachineFlag);
            // 厂商编码输入框获得焦点
            $("#unitCodeBinding_" + nextIndex).focus();
            //改变奇偶行的样式
            STSFH06_changeOddEvenColor();
        }
    },

    "setProductBinding": function (id) {
        scanMachineProductBinding({elementId: id, showNum: 20, afterSelectFun: BINOLSTSFH06.pbAfterSelectFun});
    },

    "bindInput": function (isScanMachineFlag) {
        var tableName = "mainTable";
        if ($("#spanBtnadd.ui-state-disabled").length > 0) {
            //空白单
            tableName = "blankTable";
        }
        $("#" + tableName + " #databody tr").each(function (i) {
            var trID = $(this).attr("id");
            var trParam = "#" + tableName + " #" + trID;
            BINOLSTSFH06.bindInputTR(trParam,isScanMachineFlag);
        })
    },

    "bindInputTR": function (trParam,isScanMachineFlag) {
        //按Enter键到下一个文本框，备注按Enter键到下一行或新增行。
        var $inp = $(trParam + " input:text");
        // 扫描枪模式
        $inp.bind('keydown', function (e) {
            if ($("#currentMenuID").val() == "BINOLSTSFH06") {
                var key = e.which;
                if (key == 13) {
                    //回车键
                    e.preventDefault();
                    //当前文本框为下拉框可见且输入有问题如重复，停止跳到下一个
                    var nextFlag = true;
                    if(isScanMachineFlag != undefined && isScanMachineFlag) {
                        // 扫描枪模式
                        if($(this).attr("name") == "unitCodeBinding"){
                            // 最后一行第一个可见的文本框获得焦点
                            //$('#databody >tr').find("input:text:visible:first").select();
                            nextFlag = false;
                        }
                        //如果是发货数量或者是实际执行价的话
                        if ($(this).attr("name") == "quantityuArr" || $(this).attr("name") == "priceUnitArr") {
                            var content = $(this).val();
                            var　$this = $(this);
                            if(content.length > 8){
                                //如果超过了价格的最大限制的话,鼠标离开触发
                                var dialogSetting = {
                                    dialogInit: "#dialogInit",
                                    width: 300,
                                    height: 180,
                                    confirm: $("#dialogConfirm").text(),
                                    title:$("#dialogTitle").text(),
                                    confirmEvent: function () {
                                        removeDialog("#dialogInit");
                                        //清空数据
                                        $this.val("");
                                        $this.focus();
                                    }
                                };
                                openDialog(dialogSetting);
                                //获取焦点
                                $($("#dialogInit").parent().attr("role","button")[0]).select();
                                $("#dialogInit").html($("#errmsg5").val()).addClass("highlight");
                                nextFlag = false;
                            }
                        }
                    } else {
                        // 非扫描枪模式

                        if ($(this).attr("name") == "unitCodeBinding" && $(this).is(":visible")) {
                            nextFlag = false;
                        }
                        //如果是发货数量或者是实际执行价的话
                        if ($(this).attr("name") == "quantityuArr" || $(this).attr("name") == "priceUnitArr") {
                            var content = $(this).val();
                            var　$this = $(this);
                            if(content.length > 8){
                                //如果超过了价格的最大限制的话,鼠标离开触发
                                var dialogSetting = {
                                    dialogInit: "#dialogInit",
                                    width: 300,
                                    height: 180,
                                    confirm: $("#dialogConfirm").text(),
                                    title:$("#dialogTitle").text(),
                                    confirmEvent: function () {
                                        removeDialog("#dialogInit");
                                        //清空数据
                                        $this.val("");
                                        $this.focus();
                                    }
                                };
                                openDialog(dialogSetting);
                                //获取焦点
                                $($("#dialogInit").parent().attr("role","button")[0]).select();
                                $("#dialogInit").html($("#errmsg5").val()).addClass("highlight");
                                nextFlag = false;
                            }
                        }
                       //if(nextFlag  here)
                    }
                    if (nextFlag) {
                        var nxtIdx = $inp.index(this) + 1;
                        var $nextInputText = $(trParam + " input:text:eq(" + nxtIdx + ")");
                        if ($nextInputText.length > 0) {
                            //跳到下一个文本框
                            $nextInputText.select();
                        } else {
                            //跳到下一行第一个文本框，如果没有下一行，新增一行
                            if ($(trParam).next().length == 0) {
                                BINOLSTSFH06.addNewLine(false);
                            } else {
                                $(trParam).next().find("input:text:visible:eq(0)").focus();
                            }
                        }
                    }
                }
            }
        });
    },

    // 删除空行
    "deleteEmptyRow":function(){
        STSFH06_clearActionMsg();
        //检查有无行
        if($('#databody >tr').length > 0){
            $.each($('#databody >tr'), function(i){
                // 跳过折扣行
                if($(this).find("#productVendorIDArr").val()==""){
                    //移除空白行
                    $(this).remove();
                }
            });
        }
    },

    /*
     * 用逗号分割金额、数量
     */
    "formateMoney": function (money, num) {
        money = parseFloat(money);
        money = String(money.toFixed(num));
        var re = /(-?\d+)(\d{3})/;
        while (re.test(money))
            money = money.replace(re, "$1,$2");
        return money;
    },

    // 格式化价格及重新计算金额
    "setPrice": function (obj) {
        var $this = $(obj);
        var $tr_obj = $this.parent().parent();
        // 折扣价
        var $discountPrice = $tr_obj.find("input[name='priceUnitArr']");
        var discountPrice = parseFloat($this.val());

        if (isNaN(discountPrice)) {
            discountPrice = 0;
        }
        $discountPrice.val(discountPrice.toFixed("2"));
        //数量
        var count = $tr_obj.find("#quantityuArr").val();
        //画面显示的金额
        $tr_obj.find("#money").html(BINOLSTSFH06.formateMoney(count * discountPrice, 2));
        BINOLSTSFH06.calcuTatol();
    },

    /**
     * 生成建议发货单（柜台）
     */
    "suggestBill": function () {
        if (!($("#spanBtnSuggest").is(":visible") && $("#spanBtnSuggest.ui-state-disabled").length == 0)) {
            return;
        }
        STSFH06_clearActionMsg();
        if ($('#outOrganizationId').val() == null || $('#outOrganizationId').val() == "" || $("#outOrganizationId").val() == "0") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
            $('#errorDiv2').show();
            return false;
        }
        if ($("#outDepotId option:selected").val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
            $('#errorDiv2').show();
            return false;
        }
        if ($('#inOrganizationId').val() == null || $('#inOrganizationId').val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noinDepart').val());
            $('#errorDiv2').show();
            return false;
        }
        var url = $("#searchSuggestBillUrl").attr("href");
        var params = $("#mainForm").serialize();
        params = params + "&csrftoken=" + $("#csrftoken").val();
        $("#spanBtnSuggest").hide();
        $("#spanCancelSuggest").show();
        $('#spanBtnCancelSuggest').show();
        $('#spanBtnDelNoNeed').show();
        //$("#spanBtnadd").addClass("ui-state-disabled");
        $("#spanBtnStart").addClass("ui-state-disabled");
        $("#billCopy").addClass("ui-state-disabled");
        //$("#spanBtnAddNewLine").addClass("ui-state-disabled");
        $("#btnState").val("suggestBill");
        if (BINOLSTSFH06.submitflag) {
            BINOLSTSFH06.submitflag = false;
            $("#suggestBill_processing").show();
            cherryAjaxRequest({
                url: url,
                param: params,
                reloadFlg: true,
                callback: BINOLSTSFH06.ShowProductResult
            });
        }
    },

    /**
     * 取消建议
     * @return
     */
    "canceProductResultSuggest": function (flag) {
        if (!($("#spanBtnCancelSuggest").is(":visible") && $("#spanBtnCancelSuggest.ui-state-disabled").length == 0)) {
            return;
        }
        BINOLSTSFH06.cancelAutoCreate(flag);
    },

    /**
     * 检查明细里产品是否重复并标记提示
     */
    "checkDuplicate": function () {
        var flag = true;
        var productVendorIDArr = $("#databody >tr [name='productVendorIDArr']");
        for (var i = 0; i < productVendorIDArr.length; i++) {
            $("#databody > tr:eq(" + i + ")").removeClass("errTRbgColor");
        }
        for (var i = 0; i < productVendorIDArr.length; i++) {
            var prtVendorID = $(productVendorIDArr[i]).val();
            for (var j = 0; j < productVendorIDArr.length; j++) {
                var curPrtVendorID = $(productVendorIDArr[j]).val();
                if (prtVendorID == curPrtVendorID && i != j) {
                    $("#errorSpan2").html($("#errmsg_EST00035").val());
                    $("#errorDiv2").show();
                    $("#databody > tr:eq(" + j + ")").attr("class", "errTRbgColor");
                    flag = false;
                }
            }
        }
        return flag;
    },

    // 根据折扣率计算折扣价格（批量/单个）
    "setDiscountPrice": function (obj) {
        var curRateIndex = $("#curRateIndex").val();
        var findTRParam = "tr:eq(" + curRateIndex + ")";
        var $priceRate = $("#priceRate");
        var priceRate = $priceRate.val();
        var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算

        if (isNaN(priceRate) || priceRate == "") {
            priceRate = 100;
        }

        if (curRateIndex == "") {
            //批量计算折扣价格
            $("#batchPriceRate").val(parseFloat(priceRate).toFixed("2"));
            findTRParam = "tr";
        }

        $.each($('#databody').find(findTRParam), function (n) {
            var $tr_obj = $(this);

            if (useCostPrice == "0") {//折扣率按参考价计算
                //原价
                var $costPrice = $tr_obj.find("input[name='referencePriceArr']");
                var costPrice = parseFloat($costPrice.val());
                if (isNaN(costPrice)) {
                    costPrice = 0;
                }

                // 折扣价
                var $discountPrice = $tr_obj.find("input[name='priceUnitArr']");
                var discountPrice = parseFloat($discountPrice.val());
                if (isNaN(discountPrice)) {
                    discountPrice = 0;
                }
                discountPrice = costPrice * priceRate / 100;
                $discountPrice.val(discountPrice.toFixed("2"));
                //数量
                var count = $tr_obj.find("#quantityuArr").val();

                $tr_obj.find("#money").html(BINOLSTSFH06.formateMoney(count * discountPrice, 2));
            } else {//折扣率按成本价计算

                //原价
                var costPrice = $tr_obj.find("input[name='costPriceArr']").val();
                if (costPrice != "") {
                    if (isNaN(costPrice)) {
                        costPrice = "";
                    }
                }

                // 折扣价
                var discountPrice = $tr_obj.find("input[name='priceUnitArr']").val();
                if (discountPrice != "") {
                    if (isNaN(discountPrice)) {
                        discountPrice = "";
                    }
                }


                if (costPrice != "") {
                    // 折扣价
                    var $discountPrice = $tr_obj.find("input[name='priceUnitArr']");
                    discountPrice = costPrice * priceRate / 100;
                    $discountPrice.val(discountPrice.toFixed("2"));
                    //数量
                    var count = $tr_obj.find("#quantityuArr").val();

                    $tr_obj.find("#money").html(BINOLSTSFH06.formateMoney(count * discountPrice, 2));
                } else {
                    $tr_obj.find("input[name='priceUnitArr']").val("0.00");
                    $tr_obj.find("#money").html("0.00");
                }
            }


        });

        BINOLSTSFH06.calcuTatol();
    },

    // 初始化折扣率弹出框
    "initRateDiv": function (_this, type) {
        var $this = $(_this);
        // 折扣率
        var $priceRate = $("#priceRate");
        var $rateDiv = $priceRate.parent();

        if (type == "batch") {
            //批量处理折扣价格
            $("#curRateIndex").val("");
            var batchPriceRate = $("#batchPriceRate").val();
            if (batchPriceRate == "") {
                batchPriceRate = "100";
            }
            $priceRate.val(parseFloat(batchPriceRate).toFixed("2"));
        } else {
            //折扣率弹出框的行数
            var $tr_obj = $this.parent().parent().parent();
            var curRateIndex = $("#databody tr").index($tr_obj);
            $("#curRateIndex").val(curRateIndex);

            var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算
            // 原价
            var $costPrice = parseFloat("0.0");
            if (useCostPrice == "1") {
                if (!isEmpty($costPrice)) {
                    $costPrice = $tr_obj.find("[name='costPriceArr']").val();
                }
            } else {
                $costPrice = $tr_obj.find("[name='referencePriceArr']").val();
            }
            // 折扣价格
            var $discountPrice = $tr_obj.find("[name='priceUnitArr']").val();
            var costPrice = parseFloat($costPrice).toFixed("2");
            var discountPrice = parseFloat($discountPrice).toFixed("2");
            if (isNaN(costPrice)) {
                costPrice = 0;
            }
            if (isNaN(discountPrice)) {
                discountPrice = 0;
            }
            if (costPrice != 0) {
                var priceRate = discountPrice * 100 / costPrice;
                $priceRate.val(priceRate.toFixed("2"));
            }
        }

        // 弹出折扣率设置框
        $rateDiv.show();
        //$priceRate.focus();
        $priceRate.trigger("select");

        // 设置弹出框位置
        var offset = $this.offset();
        $rateDiv.offset({"top": offset.top - $rateDiv.height() - 2, "left": offset.left});
    },

    // 关闭折扣率弹出框
    "closeDialog": function (_this) {
        $("#curRateIndex").val("");
        var $rateDiv = $(_this).parent();
        $rateDiv.hide();
    },

    "delNoNeed": function () {
        STSFH06_clearActionMsg();

        var delQuantityLT = $("#delQuantityLT").val();
        $.each($('#databody >tr'), function (i) {
            if (Number($(this).find("#quantityuArr").val()) < delQuantityLT) {
                $(this).remove();
            }
        });

        var allSelectFlag = true;
        $.each($('#databody >tr'), function (i) {
            if ($(this).find("#chkbox").prop("checked") == false) {
                allSelectFlag = false;
                return;
            }
        });
        if ($('#databody >tr').length == 0) {
            allSelectFlag = false;
        }

        $('#allSelect').prop("checked", allSelectFlag);
        STSFH06_changeOddEvenColor();

        if ($('#databody >tr').length == 0) {
            BINOLSTSFH06.canceProductResult(true);
            BINOLSTSFH06.canceProductResultSuggest(true);
        }

        BINOLSTSFH06.calcuTatol();
    },

    /**
     * 判断数量是否已经更正，如果是明细行移除红色背景
     */
    "isCorrectQuantity": function (obj) {
        var $tr_obj = $(obj).parent().parent();
        if ($tr_obj.find("#quantityuArr").val() != "") {
            if (Number($tr_obj.find("#nowCount").text().replace(/,/g, "")) - Number($tr_obj.find("#quantityuArr").val()) >= 0) {
                $tr_obj.removeClass('errTRbgColor');
                //恢复原先背景色
                if ($("#databody tr:odd").index($tr_obj) == -1) {
                    $tr_obj.attr("class", "odd");
                } else {
                    $tr_obj.attr("class", "even");
                }
            }
        }
    },

    /**
     * 执行发货
     */
    "submitSend": function () {
        if (BINOLSTSFH06.saveOrSubmitFlag) {
            BINOLSTSFH06.saveOrSubmitFlag = false;
            $("#submit_processing").show();
            $("#submitDiv").hide();
            var url = document.getElementById("submitURL").innerHTML;
            var param = $("#mainForm").serialize();
            var callback = function (msg) {
                if (window.opener && window.opener.oTableArr[1] != null) {
                    window.opener.oTableArr[1].fnDraw();
                }
                if (msg.indexOf("actionMessage") > -1) {
                    STSFH06_clearPage(true);
                }
                BINOLSTSFH06.saveOrSubmitFlag = true;
                $("#submit_processing").hide();
                $("#submitDiv").show();
            };
            cherryAjaxRequest({
                url: url,
                param: param,
                callback: callback
            });
        }
    },

    /**
     * 发货数量大于库存弹出框
     */
    "submitConfirm": function () {
        var dialogSetting = {
            dialogInit: "#dialogInit",
            width: 300,
            height: 180,
            confirm: $("#dialogConfirm").text(),
            cancel: $("#dialogCancel").text(),
            confirmEvent: function () {
                BINOLSTSFH06.submitSend();
                removeDialog("#dialogInit");
            },
            cancelEvent: function () {
                removeDialog("#dialogInit");
            }
        };
        openDialog(dialogSetting);
        $("#dialogInit").html($("#errmsg3").val());
    },

    /**
     * 收货方库存弹出框
     */
    "openProStockPopup": function (obj) {
        STSFH06_clearActionMsg();
        if ($('#outOrganizationId').val() == null || $('#outOrganizationId').val() == "" || $("#outOrganizationId").val() == "0") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
            $('#errorDiv2').show();
            return false;
        }
        if ($("#outDepotId option:selected").val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
            $('#errorDiv2').show();
            return false;
        }
        if ($('#inOrganizationId').val() == null || $('#inOrganizationId').val() == "") {
            $('#errorDiv2 #errorSpan2').html($('#noinDepart').val());
            $('#errorDiv2').show();
            return false;
        }
        var option = {};
        option.initType = "select";
        option.brandInfoID = $("#brandInfoId").val();
        option.departID = $("#inOrganizationId").val();
        option.param = "&" + $('[name=productVendorIDArr]').serialize();
        option.DepartName = $("#inOrgName").text();
        popAjaxProStockDialog(option);
    }
};


var BINOLSTSFH06 = new BINOLSTSFH06();

/**
 * 发货部门弹出框
 * @return
 */
function STSFH06_openSendDepartBox(thisObj) {
    STSFH06_clearActionMsg();

    //取得所有部门类型
    var departType = "";
    for (var i = 0; i < $("#departTypePop option").length; i++) {
        var departTypeValue = $("#departTypePop option:eq(" + i + ")").val();
        //发货排除4柜台
        if (departTypeValue != "4") {
            departType += "&departType=" + departTypeValue;
        }
    }
    var param = "checkType=radio&privilegeFlg=1&businessType=1" + departType;
    param += "&valid=1";//不允许查询停用部门
    var callback = function () {
        var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
        if ($selected.length > 0) {
            var departId = $selected.find("input@[name='organizationId']").val();
            var departCode = $selected.find("td:eq(1)").text();
            var departName = $selected.find("td:eq(2)").text();
            $("#outOrganizationId").val(departId);
            $("#outOrgName").text("(" + departCode + ")" + departName);
            STSFH06_chooseDepart();
        }

    };
    popDataTableOfDepart(thisObj, param, callback);
}

/**
 * 更改了部门
 * @param thisObj
 */
function STSFH06_chooseDepart(thisObj) {
    STSFH06_clearActionMsg();
    $('#mydetail').hide();
    $('#canceldetail').show();
    var organizationid = $('#outOrganizationId').val();
    var param = "organizationid=" + organizationid;

    //更改了部门后，取得该部门所拥有的仓库
    var url = $('#urlgetdepotAjax').html() + "?csrftoken=" + $('#csrftoken').val();
    ajaxRequest(url, param, STSFH06_changeDepotDropDownList);

    //清空收货部门
    $("#inOrgName").text("");
    $("#inOrganizationId").val("");

    STSFH06_clearDetailData(true);
}

/**
 * 修改仓库下拉框
 * @param data
 * @return
 */
function STSFH06_changeDepotDropDownList(data) {
    //data为json格式的文本字符串
    var member = eval("(" + data + ")");    //包数据解析为json 格式
    $("#outDepotId").children().each(function (i) {
        if (i > 0) {
            $(this).remove();
        }
    });
    $.each(member, function (i) {
        $("#outDepotId").append("<option value='" + member[i].BIN_InventoryInfoID + "'>" + escapeHTMLHandle(member[i].InventoryName) + "</option>");
    });
    if ($("#outDepotId option").length > 1) {
        $("#outDepotId").get(0).selectedIndex = 1;
        if ($("#outOrganizationId").val() != null && $("#outOrganizationId").val() != "") {
            $("#outDepotId").attr("disabled", false);
            $("#outLoginDepotId").attr("disabled", false);
            STSFH06_clearActionMsg();
        }
    } else {
        $("#outLoginDepotId").attr("disabled", true);
        var allChildren = $("#outDepotId").children();
        $("#outDepotId").html("").append($(allChildren[0]).clone());
    }

}

/**
 * 删除掉画面头部的提示信息框
 * @return
 */
function STSFH06_clearActionMsg() {
    $('#actionResultDisplay').html("");
    $('#errorDiv2').attr("style", 'display:none');
}

/**
 * 接收部门弹出框
 * @return
 */
function STSFH06_openReceiveDepartBox(thisObj) {
    STSFH06_clearActionMsg();
    STSFH06_changeOddEvenColor();
    if ($("#outOrganizationId").val() == "" || $("#outOrganizationId").val() == "0") {
        $("#errorSpan2").html($("#noOutDepart").val());
        $("#errorDiv2").show();
        return false;
    } else if ($("#outDepotId option:selected").val() == "") {
        $("#errorSpan2").html($("#noOutDepot").val());
        $("#errorDiv2").show();
        return false;
    }

    var param = "checkType=radio&businessType=40&depotId=" + $("#outDepotId option:selected").val() + "&inOutFlag=OUT&departId=" + $("#outOrganizationId").val();
    var callback = function () {
        var $selected = $('#_departDataTable').find(':input[checked]').parents("tr");
        var departId = $selected.find("input@[name='organizationId']").val();
        var departCode = $selected.find("td:eq(1)").text();
        var departName = $selected.find("td:eq(2)").text();
        $("#inOrgName").html("(" + departCode + ")" + departName);
        $("#inOrganizationId").val(departId);
    };
    popDepartTableByBusinessType(thisObj, param, callback);
}

function STSFH06_changeOddEvenColor() {
    $("#databody tr:odd").attr("class", "even");
    $("#databody tr:even").attr("class", "odd");
}
function STSFH06_changeOddEvenColor1() {
    $("#mydetail #databody tr:odd").attr("class", "odd");
    $("#mydetail #databody tr:even").attr("class", "even");
}
function STSFH06_getIndex() {
    var index = $('#databody').find('#inOrganizationIDArr').last().get(0).selectedIndex;
    return index;
}

function STSFH06_getTableHtmlObj() {
    var htmlObjArr = [];
    var children = $("#databody").children();
    for (var i = 0; i < children.length; i++) {
        var child = children[i];
        var quantityuArr = $(child).find("#quantityuArr").val();
        if (isEmpty(quantityuArr)) {
            quantityuArr = "";
        } else {
            quantityuArr = Number($(child).find("#quantityuArr").val());
        }
        var priceArr = "0.00";
        if ($(child).find("#priceArr").length > 0) {
            priceArr = $(child).find("#priceArr").val();
        } else {
            priceArr = $(child).find("#priceUnitArr").val();
        }

        var o = {
            _trID: $(child).attr("id"),
            _html: child.innerHTML.escapeHTML(),
            _style: child.style,
            _quantityuArr: quantityuArr,
            _priceArr: priceArr,
            _blankBarArr: $(child).find("#barCodeArr").val(),
            _blankUnitArr: $(child).find("#unitCodeArr").val(),
            _unitCodeArr: $(child).find("#unitCodeArr").val(),
            _barCodeArr: $(child).find("#barCodeArr").val(),
            _quantArr: quantityuArr,
            _reasonArr: $(child).find("#reasonArr").val()
        };
        htmlObjArr.push(o);
    }
    return htmlObjArr;
}

/**
 * 排序功能
 * @param sortFlag
 * @returns {boolean}
 * @constructor
 */
function STSFH06_sortTable(sortFlag) {
    if (sortFlag) {
        BINOLSTSFH06.sortFlag = sortFlag;
    } else {
        return false;
    }
    var htmlObjArr = STSFH06_getTableHtmlObj();
    if (htmlObjArr.length > 0) {
        htmlObjArr.sort(STSFH06_sortObject);
        if ($("#" + sortFlag).attr("class").indexOf("ui-icon-triangle-1-n") > -1) {
            htmlObjArr.reverse();
            $("#" + sortFlag).attr("class", "css_right ui-icon ui-icon-triangle-1-s");
        } else {
            $("#" + sortFlag).attr("class", "css_right ui-icon ui-icon-triangle-1-n");
        }
        $("#databody").html("");
        for (var i = 0; i < htmlObjArr.length; i++) {
            var trHtml = "<tr></tr>";
            $('#databody').append(trHtml);
            $('#databody tr:last-child').append(htmlObjArr[i]._html.unEscapeHTML()).find("#quantityuArr").val(htmlObjArr[i]._quantityuArr);
            $('#databody tr:last-child').find("#reasonArr").val(htmlObjArr[i]._reasonArr);
            $('#databody tr:last-child').find("#priceArr").val(htmlObjArr[i]._priceArr);
            $('#databody tr:last-child').find("#priceUnitArr").val(htmlObjArr[i]._priceArr);
            $('#databody tr:last-child').attr("id", htmlObjArr[i]._trID);
        }
        STSFH06_changeOddEvenColor();
//		$("#blankTable #databody").find("tr").each(function (i){
//			$(this).find("td").eq(0).html(i+1);
//		});
    }

    //重新绑定下拉框
    $("#databody tr [name='unitCodeBinding']").each(function () {
        var unitCode = $(this).attr("id");
        BINOLSTSFH06.setProductBinding(unitCode);
    })
    BINOLSTSFH06.bindInput();
}

function STSFH06_sortObject(obj1, obj2) {
    var sortFlag = BINOLSTSFH06.sortFlag;
    if (obj1[sortFlag] < obj2[sortFlag]) {
        return -1;
    } else if (obj1[sortFlag] > obj2[sortFlag]) {
        return 1;
    } else {
        return 0;
    }
}

/**
 * 计算金额
 *
 * */
function STSFH06_calcuAmount(obj) {
    var $this = $(obj);
    var $thisTd = $this.parent();
    var TrID = $thisTd.parent().attr("id");
    var $thisVal = $this.val().toString();
    var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算

    if ($thisVal == "") {
        $("#" + TrID).find(":input[name='costPriceArr']").val("");
        $("#" + TrID).find("div[id=costPrice]").html("");
        $("#" + TrID).find("#totalCostPrice").html("");
        $("#" + TrID).find("#money").html("0.00");

        if (useCostPrice == "1") {
            $("#" + TrID).find("#priceUnitArr").val("0.00");
        }

    } else if (isNaN($thisVal)) {
        if ($thisVal != "-") {
            $this.val("0");
            $("#" + TrID).find(":input[name='costPriceArr']").val("");
            $("#" + TrID).find("div[id=costPrice]").html("");
            $("#" + TrID).find("#totalCostPrice").html("");
            $("#" + TrID).find("#money").html("0.00");
            if (useCostPrice == "1") {
                $("#" + TrID).find("#priceUnitArr").val("0.00");
            }
        }
    } else {
        var price = $this.parent().parent().find(":input[name='priceUnitArr']").val();
        if (price == undefined || price == "") {
            price = "0.00";
        }
        //IE、Chrome下，由onkeyup事件触发给文本框设置值会影响切换到下一文本框后无法全选，所以作如下处理。
        var quantity = parseInt(Number($this.val()));
        if ($thisVal.toString() != quantity.toString()) {
            $this.val(parseInt(Number($this.val())));
        }
        var amount = price * Number($this.val());
        $thisTd.next().text(BINOLSTSFH06.formateMoney(amount, 2));
    }
    BINOLSTSFH06.calcuTatol();

    BINOLSTSFH06.isCorrectQuantity(obj);

    /**以下是 根据发货数量动态取对应的成本价**/
    var getCostPriceURL = $("#getCostPriceURL").html();//得到产品对应的平均成本价
    var productVendorId = $("#" + TrID).find(":input[name='prtVendorId']").val();
    var priceUnitArr = $("#" + TrID).find(":input[name='priceUnitArr']").val();//实际执行价
    var quantityuArr = $("#" + TrID).find(":input[name='quantityuArr']").val();//发货数量

    var param = "productVendorId=" + productVendorId;
    param += "&outDepotId=" + $("#outDepotId option:selected").val();
    param += "&outLoginDepotId=" + $("#outLoginDepotId option:selected").val();
    param += "&prtCount=" + quantityuArr;


    if (quantityuArr != "" && quantityuArr != null && quantityuArr.length < 8
            && quantityuArr != undefined && !isNaN(quantityuArr)
            && productVendorId != "") {//表示有发货数量，此时需要计算出对应的成本价

        cherryAjaxRequest({
            url: getCostPriceURL,
            param: param,
            callback: function (msg) {
                var data = eval("(" + msg + ")");    //包数据解析为json 格式


                if (data.costPrice != null && data.costPrice != "" && !isNaN(data.costPrice)) {
                    $("#" + TrID).find(":input[name='costPriceArr']").val(parseFloat(data.costPrice).toFixed(2));
                    $("#" + TrID).find("div[id=costPrice]").html(parseFloat(data.costPrice).toFixed(2));
                    if (useCostPrice == "1") {
                        $("#" + TrID).find(":input[name='priceUnitArr']").val(parseFloat(data.costPrice).toFixed(2));
                    }
                } else {
                    $("#" + TrID).find(":input[name='costPriceArr']").val("");
                    $("#" + TrID).find("div[id=costPrice]").html("");
                    if (useCostPrice == "1") {
                        $("#" + TrID).find(":input[name='priceUnitArr']").val(parseFloat("0.0").toFixed(2));
                    }
                }

                if (data.totalCostPrice != null && data.totalCostPrice != "" && !isNaN(data.totalCostPrice)) {
                    $("#" + TrID).find("div[id=totalCostPrice]").html(parseFloat(data.totalCostPrice).toFixed(2));
                } else {
                    $("#" + TrID).find("div[id=totalCostPrice]").html("");
                }
                var price = $this.parent().parent().find(":input[name='priceUnitArr']").val();
                if (price == undefined || price == "") {
                    price = "0.00";
                }
                //IE、Chrome下，由onkeyup事件触发给文本框设置值会影响切换到下一文本框后无法全选，所以作如下处理。
                var quantity = parseInt(Number($this.val()));
                if ($thisVal.toString() != quantity.toString()) {
                    $this.val(parseInt(Number($this.val())));
                }
                var amount = price * Number($this.val());
                $thisTd.next().text(BINOLSTSFH06.formateMoney(amount, 2));


                BINOLSTSFH06.calcuTatol();

                BINOLSTSFH06.isCorrectQuantity(obj);
            }
        });
        }

}

/**
 * 删除选中行
 */
function STSFH06_deleterow() {
    if ($("#databody #chkbox:checked").length == 0) {
        return;
    }
    STSFH06_clearActionMsg();
    $("#databody :checkbox").each(function () {
        var $this = $(this);
        if ($this.prop("checked")) {
            $this.parent().parent().remove();
            $("#spanBtnStart").removeClass("ui-state-disabled");
            $("#spanBtnSuggest").removeClass("ui-state-disabled");
        }
    });
    $('#allSelect').prop("checked", false);
    STSFH06_changeOddEvenColor();
    $('#allSelect').prop("checked", false);

    $("input[type=checkbox]").prop("checked", false);
    if ($("#spanBtnStart").is(":visible") || $("#spanBtnSuggest").is(":visible")) {
        $.each($('#databody >tr'), function (i) {
            if (i >= 0) {
                var productVendorIDArr = $(this).find("input[name=productVendorIDArr]").val();
                if (productVendorIDArr == "" || productVendorIDArr == null) {
                    $("#spanBtnStart").removeClass("ui-state-disabled");
                    $("#spanBtnSuggest").removeClass("ui-state-disabled");
                } else {
                    $("#spanBtnStart").addClass("ui-state-disabled");
                    $("#spanBtnSuggest").addClass("ui-state-disabled");
                }
            }
        });
    }

    if ($('#databody >tr').length == 0) {
        BINOLSTSFH06.canceProductResult(true);
        BINOLSTSFH06.canceProductResultSuggest(true);
    }

    BINOLSTSFH06.calcuTatol();
}

/**
 * 保存发货单
 *
 */
function STSFH06_save() {
    BINOLSTSFH06.deleteEmptyRow();
    if (!STSFH06_checkData()) {
        return false;
    }
    var checkStockFlag = $("#checkStockFlag").val();
    //为1库存允许为负，为0库存不允许为负
    if (checkStockFlag == "0") {
        if (BINOLSTSFH06.hasWarningData()) {
            $("#errorSpan2").html($("#errmsg_EST00034").val());
            $("#errorDiv2").show();
            return false;
        }
    }

    var errorQuantityFlag = false;
    $.each($('#databody >tr'), function (i) {
        if ($(this).find("#quantityuArr").val() != "") {
            if (Number($(this).find("#quantityuArr").val()) > Number($(this).find("#nowCount").text().replace(/,/g, ""))) {
                errorQuantityFlag = true;
                $(this).attr("class", "errTRbgColor");
            } else {
                $(this).removeClass('errTRbgColor');
            }
        }
    });

    if (errorQuantityFlag) {
        $("#errorSpan2").html($("#errmsg_EST00034").val());
        $("#errorDiv2").show();
        return false;
    } else {
        //发货数量不比当前库存多才执行下面的操作
        if (BINOLSTSFH06.saveOrSubmitFlag) {
            BINOLSTSFH06.saveOrSubmitFlag = false;
            $("#submitDiv").hide();
            $("#submit_processing").show();
            if (!STSFH06_checkData()) {
                return false;
            }
            var url = document.getElementById("saveURL").innerHTML;
            var param = $("#mainForm").serialize();
            var callback = function (msg) {
                if (window.opener && window.opener.oTableArr[1] != null) {
                    window.opener.oTableArr[1].fnDraw();
                }
                if (msg.indexOf("actionMessage") > -1) {
                    STSFH06_clearPage(true);
                }
                BINOLSTSFH06.saveOrSubmitFlag = true;
                $("#submit_processing").hide();
                $("#submitDiv").show();
            };
            cherryAjaxRequest({
                url: url,
                param: param,
                callback: callback
            });
        }

    }

}

function STSFH06_btnSendClick() {
    BINOLSTSFH06.deleteEmptyRow();
    if (!STSFH06_checkData()) {
        return false;
    }
    var checkStockFlag = $("#checkStockFlag").val();
    //为1库存允许为负，为0库存不允许为负
    if (checkStockFlag == "0") {
        if (BINOLSTSFH06.hasWarningData()) {
            $("#errorSpan2").html($("#errmsg_EST00034").val());
            $("#errorDiv2").show();
            return false;
        }
    }

    var errorQuantityFlag = false;
    $.each($('#databody >tr'), function (i) {
        if ($(this).find("#quantityuArr").val() != "") {
            if (Number($(this).find("#quantityuArr").val()) > Number($(this).find("#nowCount").text().replace(/,/g, ""))) {
                errorQuantityFlag = true;
                $(this).attr("class", "errTRbgColor");
            } else {
                $(this).removeClass('errTRbgColor');
            }
        }
    });

    if (errorQuantityFlag) {
        BINOLSTSFH06.submitConfirm();
    } else {
        BINOLSTSFH06.submitSend();
    }

}

/**
 * 提交前对数据进行检查
 * @returns {Boolean}
 */
function STSFH06_checkData() {
    STSFH06_clearActionMsg();
    var flag = true;
    var count = 0;
    //发货数量不能为空（空白发货单数量可以为空，但至少有一条数量不为空也不等于0的明细）
    if ($("#btnState").val() != "addblankbill") {
        $.each($('#databody >tr'), function (i) {
            if (i >= 0) {
                count += 1;
                if ($(this).find("#quantityuArr").val() == "" || $(this).find("#quantityuArr").val() == "0" || $(this).find("#quantityuArr").val() == "-" || $(this).find("[name='productVendorIDArr']").val() == "" || $(this).find("#priceUnitArr").val() == "") {
                    flag = false;
                    $("#errorSpan2").html($("#errmsg1").val());
                    $("#errorDiv2").show();
                    return;
                }
            }
        });
    } else {
        var hasQuantityFlag = false;
        $.each($('#databody >tr'), function (i) {
            if ($(this).find("#quantityuArr").val() != "" && $(this).find("#quantityuArr").val() != "0" && $(this).find("#quantityuArr").val() != "-") {
                hasQuantityFlag = true;
            }
        });
        if (!hasQuantityFlag) {
            flag = false;
            $("#errorSpan2").html($("#errmsg1").val());
            $("#errorDiv2").show();
            return;
        }
    }
    if ($('#outOrganizationId').val() == null || $('#outOrganizationId').val() == "" || $("#outOrganizationId").val() == "0") {
        $('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
        $('#errorDiv2').show();
        return false;
    }
    if (Number($("#outDepotId option:selected").val()) == 0) {
        flag = false;
        $("#errorSpan2").html($("#errmsg4").val());
        $("#errorDiv2").show();
        return;
    }
    if ($("#inOrganizationId").val() == "") {
        $('#errorDiv2 #errorSpan2').html($('#noinDepart').val());
        $('#errorDiv2').show();
        return false;
    }
    //检查有无空行
    if ($('#databody >tr').length <= 0) {
        flag = false;
        $('#errorDiv2 #errorSpan2').html($('#errmsg2').val());
        $('#errorDiv2').show();
        return false;
    }
    //检查重复
    if (flag) {
        flag = BINOLSTSFH06.checkDuplicate();
    }
    return flag;
}

/**
 * 清理发货页面(保存或者提交成功后调用)
 * @return
 */
function STSFH06_clearPage(flag) {
    $("#inOrgName").html(" ");
    $("#inOrganizationId").val("");
    $.each($('#databody >tr'), function (i) {
        if (i > 0) {
            $(this).remove();
        }
    });
    $("#reasonAll").val("");
    $("#totalQuantity").html("0");
    $("#totalAmount").html("0.00");
    $("#allSelect").prop("checked", false);
    BINOLSTSFH06.cancelAutoCreate(true);
}
function STSFH06_clearDetailData(flag) {
    $.each($('#databody >tr'), function (i) {
        if (i > 0) {
            $(this).remove();
        }
    });
    $("#totalQuantity").html("0");
    $("#totalAmount").html("0.00");
    $("#canceldetail").html(BINOLSTSFH06.canceldetailHtml);
    BINOLSTSFH06.canceProductResult(flag);
    BINOLSTSFH06.canceProductResultSuggest(flag);
    $("#allSelect").prop("checked", false);
}

/**
 *  厂商编码查询事件
 * @return dataTd
 */
function inquireLblcodeBox() {
    var inquirelblcode = $("#inquirelblcode").val();
    var number = $("#databody").find("tr");


    for (var i = 0; i <= number.length; i++) {
        var id = "dataRow" + i;
        var inquireTd = $("#" + id).find("#dataTd1").text();
        var inquireTd2 = $("#" + id).find("#dataTd2").text();
        var inquireTd3 = $("#" + id).find("#dataTd3").text();

        //过滤厂商编码
        if (inquireTd.indexOf(inquirelblcode) != -1) {
            if (inquirelblcode == "") {
                $("#" + id).find("#dataTd1").css({"background": ""});
            } else {
                $("#" + id).find("#dataTd1").css({"background": "#FFFCC2"});
            }
        } else {
            $("#" + id).find("#dataTd1").css({"background": ""});

        }

        //过滤厂商条码
        if (inquireTd2.indexOf(inquirelblcode) != -1) {
            if (inquirelblcode == "") {
                $("#" + id).find("#dataTd2").css({"background": ""});
            } else {
                $("#" + id).find("#dataTd2").css({"background": "#FFFCC2"});
            }
        } else {
            $("#" + id).find("#dataTd2").css({"background": ""});

        }

        //过滤名称
        if (inquireTd3.indexOf(inquirelblcode) != -1) {
            if (inquirelblcode == "") {
                $("#" + id).find("#dataTd3").css({"background": ""});
            } else {
                $("#" + id).find("#dataTd3").css({"background": "#FFFCC2"});
            }
        } else {
            $("#" + id).find("#dataTd3").css({"background": ""});

        }
    }
}
/**
 * 页面初期处理
 */

$(function () {
    if ($("#actionResultDiv.actionError").is(":visible")) {
        return;
    }

    BINOLSTSFH06.canceldetailHtml = $("#canceldetail").html();
    STSFH06_chooseDepart();

    $('#planArriveDate').cherryDate({
        beforeShow: function (input) {
            var value = $('#dateToday').val();
            return [value, 'minDate'];
        }
    });
});
