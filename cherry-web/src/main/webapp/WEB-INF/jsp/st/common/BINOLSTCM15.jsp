<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
$(function(){
	// 节日
    var holidays = '${holidays }';
    $('#startDate').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#endDate').val();
            return [value,'maxDate'];
        }
    });
    $('#endDate').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#startDate').val();
            return [value,'minDate'];
        }
    });
    cherryValidate({            
        formId: "searchStockForm",      
        rules: {
            startDate: {required:true,dateValid:true},    // 开始日期
            endDate: {required:true,dateValid:true}   // 结束日期
        }       
    });
});
</script>
<div id="prtStockDialog" class="hide">
<form id="searchStockForm" class="inline box2-active">
    <div class="box2 box2-content ui-widget clearfix">
        <div style="width:100%;float:left;margin-bottom:10px;">
            <div style="float:left; width:400px;margin-right:50px;">
                <span class="ui-icon icon-arrow-crm"></span>
                <span class="bg_title"><s:text name="global.page.depot"/></span>
                <s:if test='initType == "select"'>
                    <select style="width:250px;" name="STCM15_depotId" id="STCM15_depotId"  onchange="BINOLSTCM15.changeHideID()">
                        <s:iterator value="inventoryMap.depotList">
                            <option value="<s:property value="BIN_DepotInfoID" />">
                                <s:property value="DepotCodeName"/>
                            </option>
                        </s:iterator>
                    </select>
                </s:if>
                <s:else>
                    <span id="OD_DepotCodeName" class=""><s:property value="inventoryMap.DepotCodeName"/></span>
                </s:else>
            </div>
            <div style="float:left;width:400px">
                <span class="ui-icon icon-arrow-crm"></span>
                <span class="bg_title"><s:text name="global.page.logicInventory"/></span>
                <s:if test='initType == "select"'>
                    <select style="width:250px;" name="STCM15_logicInventoryId" id="STCM15_logicInventoryId"  onchange="BINOLSTCM15.changeHideID()">
                        <s:iterator value="inventoryMap.logicDepotList">
                            <option value="<s:property value="BIN_LogicInventoryInfoID" />">
                                <s:property value="LogicInventoryCodeName"/>
                            </option>
                        </s:iterator>
                    </select>
                </s:if>
                <s:else>
                    <span id="OD_LogicInventoryCodeName" class=""><s:property value="inventoryMap.LogicInventoryCodeName"/></span>
                </s:else>
            </div>
        </div>
        <div style="width:100%;float:left;">
            <div style="float:left; width:400px;margin-right:50px;">
                <span style="margin-top:5px;" class="ui-icon icon-arrow-crm"></span>
                <span class="bg_title"><s:text name="global.page.product"/></span>
                <input style="margin-left:25px;width:245px" type="text" class="text" onKeyup ="datatableFilter(this,31);" maxlength="50" id="stockKw"/>
            </div>
            <div style="float:left;width:450px">
                <span style="margin-top:5px;" class="ui-icon icon-arrow-crm"></span>
                <span class="bg_title"><s:text name="global.page.stockStartDate"/></span>
                <span><s:textfield name="startDate" cssClass="date" cssStyle="width:80px;"/></span>
                -
                <span class="bg_title"><s:text name="global.page.stockEndDate"/></span>
                <span><s:textfield name="endDate" cssClass="date" cssStyle="width:80px;"/></span>
            </div>
        </div>
    </div>
    <div style="width: 100%; text-align: center; padding-bottom:10px;margin-bottom:10px; border-bottom: 1px dashed rgb(204, 204, 204);" class="clearfix left">
        <a class="search" onclick="BINOLSTCM15.searchStock();return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
        <input type="hidden" id="hideStartDate" value="<s:property value="startDate"/>"/>
        <input type="hidden" id="hideEndDate" value="<s:property value="endDate"/>"/>
        <input type="hidden" id="hideODInventoryInfoID" value="<s:property value="inventoryMap.BIN_InventoryInfoID"/>"/>
        <input type="hidden" id="hideODLogicInventoryInfoID" value="<s:property value="inventoryMap.BIN_LogicInventoryInfoID"/>"/>
    </div>
</form>
<div class="clearfix" style="width:100%;">
    <span class="right">
        <span class="highlight"><s:text name="global.page.snow"/></span>
        <span class="gray">
            <span style="font-weight: bold;" class="green"><s:text name="global.page.stockGreen"/></span><s:text name="global.page.stockIn"/>
            <span style="font-weight: bold;" class="red"><s:text name="global.page.stockRed"/></span><s:text name="global.page.stockOut"/>
        </span>
    </span>
</div>
    <div style="overflow-x:auto;overflow-y:hidden">
    <table id="stockDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
        <thead>
            <tr style="height:31px;">
                <th class="center"><s:text name="os.navigation.num"/></th><%--序号 --%>
                <th class="center"><s:text name="global.page.prtvendorcode"/></th><%--厂商编码 --%>
                <th class="center"><s:text name="global.page.barcode"/></th><%--产品条码--%>
                <th class="center"><s:text name="global.page.productname"/></th><%--产品名称--%>
                <th class="center"><s:text name="global.page.startQuantity"/></th><%--期初库存 --%>
                <th class="center" style="background-color: #FFFCC2;"><s:text name="global.page.endQuantity"/></th><%--期末库存 --%>
                <th class="center green" style="color:green;font-weight: bold;"><s:text name="global.page.stockIn1"/></th><%-- 仓库收货 --%>
                <th class="center green" style="color:green;font-weight: bold;"><s:text name="global.page.stockIn2"/></th><%-- 接收退库 --%>
                <th class="center green" style="color:green;font-weight: bold;"><s:text name="global.page.stockIn3"/></th><%-- 调入 --%>
                <th class="center green" style="color:green;font-weight: bold;"><s:text name="global.page.stockIn4"/></th><%-- 自由入库  --%>
                <th class="center green" style="color:green;font-weight: bold;"><s:text name="global.page.stockIn5"/></th><%-- 盘盈  --%>
                <th class="center green" style="color:green;font-weight: bold;"><s:text name="global.page.stockIn6"/></th><%-- 销售入库  --%>
                <th class="center green" style="color:green;font-weight: bold;"><s:text name="global.page.stockIn7"/></th><%-- 移入  --%>
                <th class="center red" style="color:red;font-weight: bold;"><s:text name="global.page.stockOut1"/></th><%-- 仓库发货  --%>
                <th class="center red" style="color:red;font-weight: bold;"><s:text name="global.page.stockOut2"/></th><%-- 仓库退货  --%>
                <th class="center red" style="color:red;font-weight: bold;"><s:text name="global.page.stockOut3"/></th><%-- 调出  --%>
                <th class="center red" style="color:red;font-weight: bold;"><s:text name="global.page.stockOut4"/></th><%-- 自由出库  --%>
                <th class="center red" style="color:red;font-weight: bold;"><s:text name="global.page.stockOut5"/></th><%-- 盘亏  --%>
                <th class="center red" style="color:red;font-weight: bold;"><s:text name="global.page.stockOut6"/></th><%-- 销售出库 --%>
                <th class="center red" style="color:red;font-weight: bold;"><s:text name="global.page.stockOut7"/></th><%-- 移出  --%>
                <th class="center red" style="color:red;font-weight: bold;"><s:text name="global.page.stockOut8"/></th><%-- 报损  --%>
                <th class="center red" style="color:red;font-weight: bold;"><s:text name="global.page.stockOut9"/></th><%-- 礼品领用  --%>
                <th class="center red" style="color:red;font-weight: bold;"><s:text name="global.page.stockOut10"/></th><%-- 积分兑换  --%>
            </tr>
        </thead>
        <tbody></tbody>
    </table>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLSTCM15_getStock" namespace="/st" id="popPrtStock" />
<span id ="popPrtStockUrl" style="display:none">${popPrtStock}</span>
<span id ="STCM15_global_page_ok"  style="display:none"><s:text name='global.page.ok'/></span><%--确定 --%>
<span id ="OrderDepartTitle" style="display:none"><s:text name="global.page.OrderDepart"/></span><%--订货方 --%>
<span id ="CounterStockTitle" style="display:none"><s:text name="global.page.StockInfo"/></span><%--库存详细 --%>