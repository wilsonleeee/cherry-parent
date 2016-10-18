<%--订货单明细 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH03.js?v=20160904"></script>
<script type="text/javascript" src="/Cherry/js/st/common/BINOLSTCM15.js"></script>
<script type="text/javascript">
window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
	    if (binOLSTSFH03_global.needUnlock) {
	        if (window.opener) {
	            window.opener.unlockParentWindow();
	        }
	    }
    }
};
</script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
    tr.abnormal{
        background-color:#FFE5E5;
    }
</style>
<s:i18n name="i18n.st.BINOLSTSFH03">
<div class="main container clearfix">
<s:url id="doaction_url" value="/st/BINOLSTSFH03_doaction"/>
<s:url id="save_url" value="/st/BINOLSTSFH03_save"/>
<s:url id="saveTemp_url" value="/st/BINOLSTSFH03_saveTemp"/>
<s:url id="submit_url" value="/st/BINOLSTSFH03_submit"/>
<s:url id="delete_url" value="/st/BINOLSTSFH03_delete"/>
<s:url id="url_getdepotAjax" value="/st/BINOLSTSFH03_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_getStockCount" value="/st/BINOLSTSFH03_getStockCount" />
<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>
<s:url id="getCostPriceURL" value="/st/BINOLSTSFH06_getCostPriceURL"/>
<span id ="getCostPriceURL" class="hide">${getCostPriceURL}</span>
<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="saveUrl" href="${save_url}"></a>
    <a id="saveTempUrl" href="${saveTemp_url}"></a>
    <a id="submitUrl" href="${submit_url}"></a>
    <a id="deleteUrl" href="${delete_url}"></a>
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
    <s:text name="SFH03_selectAll" id="SFH03_selectAll"/>
    <input type="hidden" id="maxPercent" value="<s:property value="maxPercent"/>"/>
    <input type="hidden" id="minPercent" value="<s:property value="minPercent"/>"/>
    <input type="hidden"  id="useCostPrice" value='<s:property value='useCostPrice'/>'>
</div>
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="SFH03_title"/>&nbsp;(<s:text name="SFH03_num"/>:<s:property value="productOrderMainData.OrderNo"/>)</span>
        </div>
    </div>
    <div class="tabs">
        <ul>
            <li><a href="#tabs-1"><s:text name="global.page.title"/></a></li>
            <li>
               <a href="#tabs-2" onclick="clickTab_2WorkFlow();return false;"><s:text name="global.page.work"/></a>
            </li>
        </ul>
    
      <div id="tabs-1" class="panel-content">
      <div>
        <div class="section">
        <div id="actionResultDisplay"></div>
        <%-- ================== 错误信息提示 START ======================= --%>
        <div id="errorDiv2" class="actionError" style="display:none;">
            <ul>
                <li><span id="errorSpan2"></span></li>
            </ul>
        </div>
        <%-- ================== 错误信息提示   END  ======================= --%>
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
                <%-- 基本信息 --%>
                <s:text name="global.page.title"/>
            </strong>
            <div id="print_param_hide" class="hide">
          		<input type="hidden" name="billType" value="OD"/>
          		<input type="hidden" name="pageId" value="BINOLSTSFH03"/>
          		<input type="hidden" name="billId" value="${productOrderID}"/>
          	</div>
          	<!-- 订货单处理状态为已发货时才提供打印发货单的功能 -->
          	<s:if test='null != productDeliverID && !"".equals(productDeliverID)'>
          		<div id="print_param_hideSD" class="hide">
	          		<input type="hidden" name="billType" value="SD"/>
	          		<input type="hidden" name="pageId" value="BINOLSTSFH05"/>
	          		<input type="hidden" name="billId" value="${productDeliverID}"/>
	          	</div>
	          	<cherry:show domId="STSFH03VIEWSD">
					<button onclick="openPrintApp('View',null,'SD');return false;" class="confirm right">
						<span class="ui-icon icon-file-view"></span>
						<span class="button-text" style="font-size:12px;"><s:text name="SFH03_printSD"/></span>
					</button>
				</cherry:show>
	            <cherry:show domId="STSFH03EXPSD">
					<div id="export_param_hideSD" class="hide"><input type="hidden" name="billId" value="${productDeliverID}"/></div>
					<button id="exportButton" disabled="disabled"  class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTSFH05',1);BINOLCM99.setExportParams('#print_param_hideSD');return false;">
						<span class="ui-icon icon-file-export"></span>
						<span class="button-text" style="font-size:12px;"><s:text name="SFH03_exportSD"/></span>
					</button>
		        </cherry:show>
	        </s:if>
			<cherry:show domId="BINOLSTSFH03VEW">
			<button onclick="openPrintApp('View');return false;" class="confirm right">
				<span class="ui-icon icon-file-view"></span>
				<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
			</button>
			</cherry:show>
            <cherry:show domId="BINOLSTSFH03EXP">
            <div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${productOrderID}"/></div>
	             <button id="exportButton" disabled="disabled"  class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTSFH03',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
	             	<span class="ui-icon icon-file-export"></span>
	             	<span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
	             </button>
	        </cherry:show>
          	</div>
          </div>
          <div class="section-content">
            <div>
                <div class="box-header"></div>
                <table class="detail">
                    <tbody>
                        <tr>
                            <%-- 订单单号 --%>
                            <th><s:text name="SFH03_orderNo"/></th>
                            <td><s:property value="productOrderMainData.OrderNoIF"/></td>
                            <%-- 订单日期 --%>
                            <th><s:text name="SFH03_date"/></th>
                            <td><s:property value="productOrderMainData.Date"/></td>
                            <s:hidden id="orderTime" value="%{productOrderMainData.OrderDateTime}"/>
                        </tr>
                        <tr>
                            <%-- 申请部门 --%>
                            <th><s:text name="SFH03_depart"/></th>
                            <td>
                            	<input type="hidden" id="inTestType" value="<s:property value='productOrderMainData.TestType'/>"></input>
                                <span id="orderDepartCodeName"><s:property value="productOrderMainData.DepartCodeName"/></span>
                            </td>
                            <%-- 申请人 --%>
                            <th><s:text name="SFH03_employeeName"/></th>
                            <td><s:property value="productOrderMainData.EmployeeName"/></td>
                        </tr>
                        <tr>
                            <%-- 订货仓库 --%>
                            <th><s:text name="SFH03_DepotCodeName"/></th>
                            <td><s:property value="productOrderMainData.DepotCodeName"/></td>
                            <%-- 订货逻辑仓库 --%>
                            <th><s:text name="SFH03_LogicInventoryName"/></th>
                            <td><s:property value="productOrderMainData.LogicInventoryName"/></td>
                        </tr>
                        <tr>
                            <%-- 订货单类型 --%>
                            <th><s:text name="SFH03_OrderType"/></th>
                            <td>
                                <div id="showOrderTypeSelect" class="hide">
                                    <s:select onchange="BINOLSTSFH03.refreshOrderType(this);" list="#application.CodeTable.getCodes('1168')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{SFH03_selectAll}" cssStyle="width:200px" value="%{productOrderMainData.OrderType}"></s:select>
                                </div>
                                <div id="hideOrderType">
                                    <s:property value='#application.CodeTable.getVal("1168", productOrderMainData.OrderType)'/>
                                </div>
                            </td>
                            <%-- 期望发货日期 --%>
                            <th><s:text name="SFH03_ExpectDeliverDate"/></th>
                            <td>
                                <div id="hideEditExpectDeliverDate">
                                    <s:property value="productOrderMainData.ExpectDeliverDate"/>
                                </div>
                                <div id="showEditExpectDeliverDate" class="hide">
                                    <s:textfield id="selExpectDeliverDate" cssStyle="width:80px;" name="selExpectDeliverDate"
                                        value="%{productOrderMainData.ExpectDeliverDate}" cssClass="date" onchange="BINOLSTSFH03.expectDeliverDateChange();"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <%-- 订货理由 --%>
                            <th><s:text name="SFH03_reason"/></th>
                            <td><s:property value='productOrderMainData.Comments'/></td>
                            <%-- 导入批次 --%>
                            <th><s:text name="SFH03_importBatchCode"/></th>
                            <td><s:property value='productOrderMainData.ImportBatch'/></td>
                        </tr>
                    </tbody>
                </table>
                <table class="detail">
                    <tbody>
                        <tr>
                            <%-- 发货部门 --%>
                            <th><s:text name="SFH03_AcceptDepartCodeName"/></th>
                            <s:if test=' productOrderMainData.OperateTypeAudit || "32".equals(operateType) || "36".equals(operateType) || "40".equals(operateType)'>
                                <td>
                                    <span id="outOrgName" class="left"><s:property value="productOrderMainData.AcceptDepartCodeName"/></span>
                                    <div id="showBtnopenDepartBox" class="hide">
                                    <a class="add right hide" onclick="BINOLSTSFH03.openDepartBox(this);">
                                        <span class="ui-icon icon-search"></span>
                                        <span class="button-text"><s:text name="SFH03_select"/></span>
                                    </a>
                                    </div>
                                </td>
                            </s:if>
                            <s:else>
                                <td><s:property value="productOrderMainData.AcceptDepartCodeName"/></td>
                            </s:else>
                            <%-- 处理状态 --%>
                            <th><s:text name="SFH03_tradeStatus"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1142", productOrderMainData.TradeStatus)'/></td>
                        </tr>
                        <tr>
                            <%-- 发货实体仓库 --%>
                            <th><s:text name="SFH03_AcceptDepotCodeName"/></th>
                            <s:if test=' productOrderMainData.OperateTypeAudit || "32".equals(operateType) || "36".equals(operateType) || "40".equals(operateType)'>
                                <td>
                                	<input id="outDepot" type="hidden" value='<s:property value="productOrderMainData.BIN_InventoryInfoIDAccept"/>'/>
                                	<s:if test='null!=depotsInfoList'>
                                        <div id="showAcceptDepotCodeName" class="hide">
	                                        <s:select id="outDepotInfoId" name="outDepotInfoId" onchange="BINOLSTSFH03.refreshStockCount();"  list="depotsInfoList" value="%{productOrderMainData.BIN_InventoryInfoIDAccept}" listKey="BIN_DepotInfoID" listValue="DepotCodeName" headerKey="" cssStyle="width:200px;">
	                                        </s:select>
                                        </div>
                                        <div id="hideAcceptDepotCodeName">
                                            <s:property value="productOrderMainData.AcceptDepotCodeName"/>
                                        </div>
                                	</s:if>
                                </td>
                            </s:if>
                            <s:else>
                                <td>
                                    <s:property value="productOrderMainData.AcceptDepotCodeName"/>
                                    <input id="outDepotInfoId" type="hidden" value='<s:property value="productOrderMainData.BIN_InventoryInfoIDAccept"/>'/>
                                </td>
                            </s:else>
                            <%-- 发货逻辑仓库 --%>
                            <th><s:text name="SFH03_AcceptLogicInventoryName"/></th>
                            <s:if test=' productOrderMainData.OperateTypeAudit ||"32".equals(operateType) || "36".equals(operateType) || "40".equals(operateType)'>
                                <td>
                                	<s:if test='null!=logicDepotsInfoList'>
                                        <div id="showAcceptLogicInventoryName" class="hide">
	                                        <s:select id="outLogicDepotsInfoId" name="outLogicDepotsInfoId" list="logicDepotsInfoList" onchange="BINOLSTSFH03.refreshStockCount();" value="%{productOrderMainData.BIN_LogicInventoryInfoIDAccept}"  listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" cssStyle="width:200px;">
	                                        </s:select>
                                        </div>
                                        <div id="hideAcceptLogicInventoryName">
                                            <s:property value="productOrderMainData.AcceptLogicInventoryName"/>
                                        </div>
                                    </s:if>
                                </td>
                            </s:if>
                            <s:else>
                                <td>
                                    <s:property value="productOrderMainData.AcceptLogicInventoryName"/>
                                    <input id="outLogicDepotsInfoId" type="hidden" value='<s:property value="productOrderMainData.BIN_LogicInventoryInfoIDAccept"/>'/>
                                </td>
                            </s:else>
                        </tr>
						<tr id="showMainComments" class="hide">
                        	<%-- 发货理由 --%>
                            <th><font color=red ><s:text name="SFH03_comment"/></font></th>
                            <td colspan="3">
                            <div >
                            <s:textfield name="deliveryComments" size="120" maxlength="200" onkeyup="BINOLSTSFH03.transforValueComment()" value="%{productOrderMainData.delieveComments}"></s:textfield>
                            </div>                    
                            </td>
                        </tr>
                    </tbody>
                </table>
                <table class="detail" style="margin-bottom: 5px;">
                    <tbody>
                        <tr>
                            <%-- 审核状态 --%>
                            <th><s:text name="SFH03_verifiedFlag"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1146", productOrderMainData.VerifiedFlag)'/></td>
                            <%-- 审核者 --%>
                            <th><s:text name="SFH03_employeeNameAudit"/></th>
                            <td><s:property value="productOrderMainData.EmployeeNameAudit"/></td>
                        </tr>
                    </tbody>
                </table>
                <div class="clearfix"></div>
            </div>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
            <%-- 订单明细一览 --%>
            <s:text name="SFH03_results_list"/>
            </strong>
          </div>
          <div class="section-content">
            <s:if test=' productOrderMainData.OperateTypeAudit ||"2".equals(operateType) || "32".equals(operateType) || "36".equals(operateType) || "40".equals(operateType)'>
                <div id="showToolbar" class="">
                <div class="toolbar clearfix">
                    <span class="left">
                        <s:if test='"true".equals(productOrderMainData.showRecStockFlag)'>
                            <a class="add" onclick="BINOLSTSFH03.openProStockPopup(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="SFH03_CounterStock"/></span>
                            </a>
                        </s:if>
                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="productOrderMainData.BIN_BrandInfoID"/>'></input>
                        <span id="showAddBtn" class="hide">
                        <a class="add" onclick="BINOLSTSFH03.openProPopup(this);">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="SFH03_add"/></span>
                        </a>
                        </span>
                        <span id="showDelBtn" class="hide">
                        <a class="delete" onclick="BINOLSTSFH03.deleteRow();">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="SFH03_delete"/></span>
                        </a>
                        </span>
                        <span class="highlight"><s:text name="global.page.snow"/></span>
                        <s:text name="SFH03_abnormalInfo"/>
                    </span>
                </div>
                </div>
            </s:if>
            <s:else>
	            <s:if test='"true".equals(productOrderMainData.showRecStockFlag)'>
	                <div class="toolbar clearfix">
	                    <a class="add" onclick="BINOLSTSFH03.openProStockPopup(this);">
	                        <span class="ui-icon icon-search"></span>
	                        <span class="button-text"><s:text name="SFH03_CounterStock"/></span>
	                    </a>
	                    <span class="highlight"><s:text name="global.page.snow"/></span>
                        <s:text name="SFH03_abnormalInfo"/>
	                </div>
	            </s:if>
            </s:else>
            <form id="mainForm" method="post" class="inline" action="BINOLSTSFH03_doaction">
                <%--防止有button的form在text框输入后按Enter键后自动submit --%>
                <button type="submit" onclick="return false;" class="hide"></button>
            <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
            <input type="hidden" id="entryID" name="entryID" value='<s:property value="productOrderMainData.WorkFlowID"/>'/>
            <input type="hidden" id="actionID" name="actionID"/>
            <input type="hidden" id="orderType" name="orderType" value='<s:property value="productOrderMainData.OrderType"/>'/>
            <input type="hidden" id="entryid" name="entryid"/>
            <input type="hidden" id="actionid" name="actionid"/>
            <input id="csrftokenForm" name="csrftokenForm" type="hidden"/>
            <input type="hidden" id="comments" name="comments"/>
            <input type="hidden" id="modifiedFlag" name="modifiedFlag" value='<s:property value="modifiedFlag"/>'/>
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                    <s:if test=' productOrderMainData.OperateTypeAudit ||"2".equals(operateType) || "32".equals(operateType) || "36".equals(operateType) || "40".equals(operateType)'>
                        <th id="showCheckbox" class="center hide" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTSFH03.selectAll();"/><s:text name="SFH03_select"/></th><%-- 编号 --%>
                    </s:if>
                    <th id="th_No" class="center"><s:text name="SFH03_no"/></th><%-- 编号 --%>
                    <th id="th_UnitCode" class="center"><s:text name="SFH03_UnitCode"/></th><%-- 厂商编码 --%>
                    <th id="th_BarCode" class="center"><s:text name="SFH03_BarCode"/></th><%-- 产品条码 --%> 
                    <th id="th_ProductName" class="center"><s:text name="SFH03_ProductName"/></th><%-- 产品名称 --%>
                    <th id="th_PrimaryCategoryBig" class="center"><s:text name="SFH03_PrimaryCategoryBig"/></th><%-- 大分类 --%>
                    <%--<th id="th_PrimaryCategoryMedium" class="center"><s:text name="SFH03_PrimaryCategoryMedium"/></th>--%>
                    <th id="th_PrimaryCategorySmall" class="center"><s:text name="SFH03_PrimaryCategorySmall"/></th><%-- 小分类 --%>
                    <th id="th_Price" class="center"><s:text name="SFH03_Price"/></th><%-- 单价 --%>                                       
                    <th id="tdCostPrice" class="center"  style="display: none;"><s:text name="SFH03_costPrice"/></th><%--成本价 --%>
                  	<th id="tdTotalCostPrice" class="center"  style="display: none;"><s:text name="SFH03_totalCostPrice"/></th><%--总成本价 --%>
                    <s:if test='"40".equals(operateType) || "1".equals(useCostPrice)'>
                        <%-- 折扣价格 --%>
                        <th id="thDiscountPrice" class="center hide"><s:text name="SFH03_discountPrice"/>
                        <span class="calculator" onclick="BINOLSTSFH03.initRateDiv(this,'batch');" title="<s:text name="SFH03_BatchCalTitle"/>"></span>
                        <input type="hidden" id="batchPriceRate">
                        </th>
                    </s:if>
                    <s:if test=' productOrderMainData.OperateTypeAudit || "36".equals(operateType) '>
                    	<!-- 提交订单或者审核步骤 -->
                        <th id="th_OrderStock"  class="center"><s:text name="SFH03_OrderStock"/></th><%-- 订货方库存 --%>
                        <cherry:show domId="STSFH03CHKST">
                        	<th id="th_ProductQuantity" class="center"><s:text name="SFH03_ProductQuantity"/></th><%-- 发货方库存 --%>
                        </cherry:show>
                        <th id="th_SaleQuantity" class="center"><s:text name="SFH03_SaleQuantity"/></th><%-- 近30天销量 --%>
                    </s:if>
                    <s:elseif test='"40".equals(operateType) '>
                    <cherry:show domId="STSFH03CHKST"><!-- 希熙彩妆一审的审核者不允许看发货方库存 -->
                        <th id="th_ProductQuantity" class="center"><s:text name="SFH03_ProductQuantity"/></th><%-- 发货方库存 --%>
                    </cherry:show>
                    </s:elseif>
                    <th id="th_SuggestedQuantity" class="center"><s:text name="SFH03_suggestedQuantity"/></th><%-- 建议数量 --%>
                    <th id="th_ApplyQuantity" class="center"><s:text name="SFH03_ApplyQuantity"/></th><%-- 计划订量 --%>
                    <s:if test='"40".equals(operateType) '>
                        <th id="th_Quantity" class="center"><s:text name="SFH03_DeliverQuantity"/></th><%-- 发货数量 --%>
                    </s:if>
                    <s:else>
                        <th id="th_Quantity" class="center"><s:text name="SFH03_Quantity"/></th><%-- 核准订货数量 --%>
                    </s:else>
                    <th id="th_Amount" class="center"><s:text name="SFH03_Amount"/></th><%-- 金额 --%>
                    <th id="th_Remark" class="center"><s:text name="SFH03_remark"/></th><%-- 备注 --%>
                </tr>
              </thead>
              
              <tbody id="databody">
                <s:iterator value="productOrderDetailData" status="status">
                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else> <s:if test="abnormalQuantityFlag.equals('true')">abnormal</s:if>">
                        <s:if test='productOrderMainData.OperateTypeAudit || "2".equals(operateType) || "32".equals(operateType) || "36".equals(operateType) || "40".equals(operateType)'>
                            <td id="dataTd0" class="hide"><input id="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTSFH03.changechkbox(this);"/></td>
                        </s:if>
                        <td id="dataTd1"><s:property value="#status.index+1"/></td>
                        <td id="dataTd2"><span><s:property value="UnitCode"/></span></td>
                        <td id="dataTd4"><span><s:property value="BarCode"/></span></td>
                        <td id="dataTd3"><span><s:property value="ProductName"/></span></td>
                        <td id="dataTdPCB"><span><s:property value="PrimaryCategoryBig"/></span></td>
                        <%--<td id="dataTdPCM"><span><s:property value="PrimaryCategoryMedium"/></span></td>--%>
                        <td id="dataTdPCS"><span><s:property value="PrimaryCategorySmall"/></span></td>
                        <td id="dataTd5" class="alignRight" style="width:10%;">
                            <s:if test='null!=Price'>
                                <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        	<!-- 平均成本价和总成本价字段 -->
                        	<s:if test='null!=totalCostPrice && !"".equals(totalCostPrice)'>
		                        <td id="tdCostPrice" style="display: none;" >
		                        	<div id="costPrice">${costPrice}</div>
		                        </td>
		                        <td  id="tdTotalCostPrice" style="display: none;" >
		                        	<div id="totalCostPrice">${totalCostPrice}</div>
		                        </td>
	                        </s:if>
	                        <s:else>
	                        	<td id="tdCostPrice"  style="display: none;" ><div id="costPrice"></div></td>
		                        <td id="tdTotalCostPrice"  style="display: none;"><div id="totalCostPrice"></div></td>
	                        </s:else>
                        <s:if test='"40".equals(operateType) || "1".equals(useCostPrice)'>
                            <td id="tdDiscountPrice" class="alignRight hide" style="width:10%;">
                                <input id="priceArr" class="price hide" size="10" maxlength="9" value="<s:property value="getText('{0,number,##0.00}',{Price})"/>" onchange="BINOLSTSFH03.setPrice(this);return false;"/>
                                <span id="spanCalPrice" class="hide"><span class="calculator" onclick="BINOLSTSFH03.initRateDiv(this);" title="<s:text name="SFH03_CalTitle"/>"></span></span>
                                <div id="hidePriceArr">
                                    <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                                </div>
                            </td>
                        </s:if>
                        <s:if test='productOrderMainData.OperateTypeAudit || "36".equals(operateType)'>
                        	<!-- 提交订单或者审核步骤中 -->
                            <td id="dataTdOrderStock" class="alignRight">
                                <s:if test='null!=OrderStock'>
                                    <s:text name="format.number"><s:param value="OrderStock"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <cherry:show domId="STSFH03CHKST">
                            <td id="dataTd6" class="alignRight">
                                <s:if test='null!=ProductQuantity'>
                                    <s:text name="format.number"><s:param value="ProductQuantity"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            </cherry:show>
                            <td id="dataTdSaleQuantity" class="alignRight">
                                <s:if test='null!=SaleQuantity'>
                                    <s:text name="format.number"><s:param value="SaleQuantity"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                        </s:if>
                        <s:elseif test='"40".equals(operateType)'>
                        <cherry:show domId="STSFH03CHKST">
                            <td id="dataTd6" class="alignRight">
                                <s:if test='null!=ProductQuantity'>
                                    <s:text name="format.number"><s:param value="ProductQuantity"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                        </cherry:show>
                        </s:elseif>
                        <td id="dataTd7" class="alignRight">
                            <s:if test='null!=SuggestedQuantity'>
                                <s:text name="format.number"><s:param value="SuggestedQuantity"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <!-- 计划订量 -->
                        <td id="dataTdApplyQuantity" class="alignRight">
                            <s:if test='null != ApplyQuantity'>
                                <s:text name="format.number"><s:param value="ApplyQuantity"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <!-- 核准订货量 -->
                        <td id="newCount" class="alignRight" >
                            <s:if test='productOrderMainData.OperateTypeAudit || "2".equals(operateType) || "32".equals(operateType) || "36".equals(operateType) || "40".equals(operateType)'>
                                <s:textfield name="quantityArr" cssClass="text-number hide" size="10" maxlength="9" value="%{Quantity}" onchange="BINOLSTSFH03.changeCount(this);"  onkeyup="BINOLSTSFH03.changeCount(this);"></s:textfield>
                                <div id="hideQuantiyArr">
	                                <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                                </div>
                            </s:if>
                            <s:else>
	                            <s:if test='null!=Quantity'>
	                                <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
	                                <s:hidden name="quantityArr" value="%{Quantity}"/>
	                            </s:if>
                            </s:else>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="money" class="alignRight">
                            <s:if test='null!=Price && null!=Quantity'>
                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="dataTd9">
                            <s:if test='productOrderMainData.OperateTypeAudit || "2".equals(operateType) || "32".equals(operateType) || "36".equals(operateType) || "40".equals(operateType)'>
                                <s:textfield name="commentsArr" size="25" maxlength="200" value="%{Comments}" cssClass="hide"></s:textfield>
                                <div id="hideComments">
                                    <p><s:property value="Comments"/></p>
                                </div>
                            </s:if>
                            <s:else>
                                <p><s:property value="Comments"/></p>
                            </s:else>
                        </td>
                        <td style="display:none" id="dataTd10">
                            <input type="hidden" name="referencePriceArr" value="<s:property value="Price"/>" />
                            <input type="hidden" id="priceUnitArr<s:property value='#status.index+1'/>" name="priceUnitArr" value="<s:property value='Price'/>"/>                   
                            <input type="hidden" id="suggestedQuantityArr<s:property value='#status.index+1'/>" name="suggestedQuantityArr" value="<s:property value='SuggestedQuantity'/>"/>
                            <input type="hidden" id="applyQuantityArr<s:property value='#status.index+1'/>" name="applyQuantityArr" value="<s:property value='ApplyQuantity'/>"/>
                            <input type="hidden" id="productVendorPackageIDArr<s:property value='#status.index+1'/>" name="productVendorPackageIDArr" value="<s:property value='BIN_ProductVendorPackageID'/>"/>
                            <input type="hidden" id="inventoryInfoIDArr<s:property value='#status.index+1'/>" name="inventoryInfoIDArr" value="<s:property value='BIN_InventoryInfoID'/>"/>
                            <input type="hidden" id="logicInventoryInfoIDArr<s:property value='#status.index+1'/>" name="logicInventoryInfoIDArr" value="<s:property value='BIN_LogicInventoryInfoID'/>"/>
                            <input type="hidden" id="storageLocationInfoIDArr<s:property value='#status.index+1'/>" name="storageLocationInfoIDArr" value="<s:property value='BIN_StorageLocationInfoID'/>"/>
                            <input type="hidden" id="productVendorIDArr<s:property value='#status.index+1'/>" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
                            <input type="hidden" id="inventoryInfoIDAcceptArr<s:property value='#status.index+1'/>" name="inventoryInfoIDAcceptArr" value="<s:property value='BIN_InventoryInfoIDAccept'/>"/>
                            <input type="hidden" id="logicInventoryInfoIDAcceptArr<s:property value='#status.index+1'/>" name="logicInventoryInfoIDAcceptArr" value="<s:property value='BIN_LogicInventoryInfoIDAccept'/>"/>
                            <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
                            <s:if test='null!=costPrice && ""!=costPrice'>
		                        <input type="hidden" id="costPriceArr<s:property value='#status.index+1'/>" name="costPriceArr" value='${costPrice}'/>	    
		                    </s:if>
		                    <s:else>
			                   <input type="hidden" id="costPriceArr<s:property value='#status.index+1'/>" name="costPriceArr" value=""/>	    
		                     </s:else>
		                    <s:if test='null!=EditPrice && ""!=EditPrice'>
		                        <input type="hidden" id="editPriceArr<s:property value='#status.index+1'/>" name="editPriceArr" value='${EditPrice}'/>	    
		                    </s:if>
		                    <s:else>
			                   <input type="hidden" id="editPriceArr<s:property value='#status.index+1'/>" name="editPriceArr" value=""/>	    
		                     </s:else>
                        </td>
                    </tr>
                </s:iterator>
              </tbody>
            </table>
            </div>
            <div style="display:none">
                <input type="hidden" id="rowNumber" value="<s:property value='productOrderDetailData.size()'/>"/>
                <input type="hidden" id=inventoryInfoID value='<s:property value="productOrderMainData.BIN_InventoryInfoID"/>'/>
                <input type="hidden" id="logicInventoryInfoID" value='<s:property value="productOrderMainData.BIN_LogicInventoryInfoID"/>'/>
                <input type="hidden" value="<s:property value="productOrderMainData.UpdateTime"/>" name="updateTime" id="updateTime">
                <input type="hidden" value="<s:property value="productOrderMainData.ModifyCount"/>" name="modifyCount" id="modifyCount">
                <input type="hidden" value="<s:property value="productOrderMainData.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
                <input type="hidden" value="<s:property value="productOrderMainData.BIN_OrganizationID"/>" id="inOrganizationID" name="inOrganizationID">
                <input type="hidden" value="<s:property value="productOrderMainData.BIN_OrganizationIDAccept"/>" id="outOrganizationID" name="outOrganizationID">
                <input type="hidden" value="<s:property value="productOrderMainData.BIN_InventoryInfoIDAccept"/>" id="depotInfoIdAccept" name="depotInfoIdAccept">
                <input type="hidden" value="<s:property value="productOrderMainData.BIN_LogicInventoryInfoIDAccept"/>" id="logicDepotsInfoIdAccept" name="logicDepotsInfoIdAccept">
                <s:hidden id="productOrderID" name="productOrderID" value="%{#request.productOrderID}"></s:hidden>
                <input type="hidden" value="" name="editFlag" id="editFlag">
                <input type="hidden" id="expectDeliverDate" name="expectDeliverDate" value='<s:property value="productOrderMainData.ExpectDeliverDate"/>'/>
                <input type="hidden" id="operateTypeAudit"  value="<s:property value="productOrderMainData.OperateTypeAudit"/>"/>
                <input type="hidden" id="lockSection" name="lockSection" value="<s:property value='lockSection'/>"/>
                <input type="hidden" id="totalAmountCheck" name="totalAmountCheck" value="<s:property value='productOrderMainData.TotalAmount'/>"/>            
            </div>
            </form>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="SFH03_suggestedQuantity"/></td><%-- 建议数量 --%>
                <td class="center"><s:text name="SFH03_ApplyTotalQuantity"/></td><%-- 申请总数量 --%>
                <td class="center"><s:text name="SFH03_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="SFH03_totalAmount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td class="center">
                    <%-- 建议数量 --%>
                    <span id="totalSuggestedQuantity">
                        <s:if test='null!=productOrderMainData.SuggestedQuantity'>
                            <s:text name="format.number"><s:param value="productOrderMainData.SuggestedQuantity"></s:param></s:text>
                        </s:if>
                        <s:else>&nbsp;</s:else>
                    </span>
                </td>
                <td class="center">
                    <%-- 申请订货数量 --%>
                    <span id="totalApplyQuantity">
                        <s:if test='null!=productOrderMainData.ApplyQuantity'>
                            <s:text name="format.number"><s:param value="productOrderMainData.ApplyQuantity"></s:param></s:text>
                        </s:if>
                        <s:else>&nbsp;</s:else>
                    </span>
                </td>
                <td class="center">
                    <%-- 总数量 --%>
                    <s:if test='null!=productOrderMainData.TotalQuantity'>
                        <span id="totalQuantity"><s:text name="format.number"><s:param value="productOrderMainData.TotalQuantity"></s:param></s:text></span>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </td>
                <td class="center">
                    <%-- 总金额--%>
                    <s:if test='null!=productOrderMainData.TotalAmount'>
                        <span id="totalAmount"><s:text name="format.price"><s:param value="productOrderMainData.TotalAmount"></s:param></s:text></span>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </td>
              </tr>
            </table>
            <hr class="space" />
            <div class="center">
            <s:if test='"2".equals(operateType)'>
                <button id="btnSave" class="save" style="display:none;" onclick="BINOLSTSFH03.saveOrder();return false;"><span class="ui-icon icon-save"></span>
                    <%-- 保存 --%>
                    <span class="button-text"><s:text name="global.page.save"/></span>
                </button>
                <button id="btn-icon-edit-big" class="confirm" onclick="BINOLSTSFH03.modifyOrder();return false;"><span class="ui-icon icon-edit-big"></span>
                    <%-- 修改 --%>
                    <span class="button-text"><s:text name="os.edit"/></span>
                </button>
                <button class="confirm" onclick="BINOLSTSFH03.submitOrder();return false;"><span class="ui-icon icon-confirm"></span>
                    <%-- 提交 --%>
                    <span class="button-text"><s:text name="global.page.submit"/></span>
                </button>
            </s:if>
            <s:elseif test=' productOrderMainData.OperateTypeAudit || "36".equals(operateType) || "40".equals(operateType) || "44".equals(operateType)'>
                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
                <s:if test='"36".equals(operateType)'>
                    <button id="btnSave" class="save" style="display:none;" onclick="BINOLSTSFH03.saveOrder();return false;"><span class="ui-icon icon-save"></span>
                    <%-- 保存 --%>
                    <span class="button-text"><s:text name="global.page.save"/></span>
                    </button>
                </s:if>
                <s:if test='"40".equals(operateType)'>
                    <button id="btnSDSubmit" class="confirm" style="display:none;" onclick="doaction(<s:property value="productOrderMainData.WorkFlowID"/>,804,&quot;&quot;,&quot;&quot;);return false;"><span class="ui-icon icon-confirm"></span>
                    <%-- 提交 --%>
                    <span class="button-text"><s:text name="global.page.submit"/></span>
                    </button>
                </s:if>
            </s:elseif>
            	<button id="btnSaveTemp" class="save" onclick="BINOLSTSFH03.saveTemp();return false;" style="display:none;"><span class="ui-icon icon-save"></span>
                    <%-- 暂存 --%>
                    <span class="button-text"><s:text name="global.page.saveTemp"/></span>
				</button>
				<button id="btnReturn" class="close" onclick="BINOLSTSFH03.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
					<%--返回 --%>
					<span class="button-text"><s:text name="global.page.back"/></span>
				</button>
				<button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
					<%-- 关闭 --%>
					<span class="button-text"><s:text name="global.page.close"/></span>
				</button>
            </div>
          </div>
        </div>
        </div>
        <div id="tabs-2">
        <strong><s:text name="global.page.worksProcessing"/></strong>
        </div>
      </div>
    </div>
    </div>
    <div class="rateDialog hide">
        <span id="spanCalTitle" style="display:none;"><s:text name="SFH03_CalTitle"/></span>
        <s:text name="SFH03_discountRate"/><%-- 折扣率 --%>
        <input class="number" id="priceRate" value="100" onblur="BINOLSTSFH03.closeDialog(this);return false;"  
            onkeyup="BINOLSTSFH03.setDiscountPrice(this);return false;"/><s:text name="global.page.percent"/>
        <input type ="hidden" id="curRateIndex" value=""/>
    </div>
</s:i18n>
<form action="BINOLSTSFH03_init" id="productOrderDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="productOrderID" value="%{#request.productOrderID}"></s:hidden>
</form>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 弹出datatable -- 产品共通START ======================= --%>
<%--<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />--%>
<%-- ================== 弹出datatable -- 产品共通START ======================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 报表打印共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
<%-- ==================  报表打印共通导入 End ======================== --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
</div>
<%-- ================== 工作流提示弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOSDialog.jsp" flush="true" />
<%-- ==================  工作流提示弹出窗口End ======================== --%>
