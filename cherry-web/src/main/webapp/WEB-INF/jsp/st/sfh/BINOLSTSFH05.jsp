<%--发货单明细 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<%@ page import = "java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import="com.cherry.cm.core.CherryConstants"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH05.js"></script>
<script type="text/javascript" src="/Cherry/js/st/common/BINOLSTCM15.js"></script>
<script type="text/javascript">
//来自订单，刷新
var currentUnitID = $('#currentUnitID',window.opener.document).val();
if(currentUnitID == "BINOLSTSFH02"){
    if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
}
var referrer = document.referrer;
//来自订单执行生成发货单Action后跳转，同时又是在首页，需要刷新。
if(referrer.indexOf("BINOLSTSFH03")>-1){
	refreshGadgetTask();
}
window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
	    if (binOLSTSFH05_global.needUnlock) {
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

<%
	//当前操作员工ID
	String optionEmployeeId = (String)request.getAttribute("optionEmployeeId");
	//发货单主表数据
	Map mainData = (Map)request.getAttribute("productDeliverMainData");
	//审核区分	未提交审核
	final String verifiedFlag = CherryConstants.AUDIT_FLAG_UNSUBMIT;
	
	SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日期
	String dateTime = dateFm.format(new java.util.Date());
%>


<s:i18n name="i18n.st.BINOLSTSFH05">
<div class="main container clearfix">
<s:url id="doaction_url" value="/st/BINOLSTSFH05_doaction"/>
<s:url id="submit_url" value="/st/BINOLSTSFH05_submit"/>
<s:url id="save_url" value="/st/BINOLSTSFH05_save"/>
<s:url id="delete_url" value="/st/BINOLSTSFH05_delete"/>
<s:url id="url_getdepotAjax" value="/st/BINOLSTSFH03_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_getStockCount" value="/st/BINOLSTSFH03_getStockCount" />
<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>
<s:url id="getCostPriceURL" value="/st/BINOLSTSFH06_getCostPriceURL"/>
<span id ="getCostPriceURL" class="hide">${getCostPriceURL}</span>

<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="submitUrl" href="${submit_url}"></a>
    <a id="saveUrl" href="${save_url}"></a>
    <a id="deleteUrl" href="${delete_url}"></a>
    <s:text name="SFH05_pleaseSelect" id="pleaseSelect"></s:text>
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
    <div id="dialogConfirm"><s:text name="SFH05_dialog_confirm" /></div>
    <div id="dialogCancel"><s:text name="SFH05_dialog_cancel" /></div>
    <input type="hidden"  id="sysConfigUsePrice" value='<s:property value='sysConfigUsePrice'/>'>
    <input type="hidden"  id="useCostPrice" value='<s:property value='useCostPrice'/>'>
    <input type="hidden"  id="dateToday" value='<%= dateTime %>'>
</div>
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="SFH05_title"/>&nbsp;(<s:text name="SFH05_num"/>:<s:property value="productDeliverMainData.DeliverNo"/>)</span>
        </div>
    </div>
     <div class="tabs">
   <ul>
      <li><a href="#tabs-1"><s:text name="global.page.title"/></a></li>
      <li>
          <a href="#tabs-2" onclick="clickTab_2WorkFlow();return false;"><s:text name="global.page.work"/></a>
      </li>
    </ul>

   <!-- 将画面上的div 设置id="tabs-1" 此div一般是指class="panel-content"的div，如： -->
   <div id="tabs-1" class="panel-content">
      <div>
      <form id="mainForm" method="post" class="inline">
        <%--防止有button的form在text框输入后按Enter键后自动submit --%>
        <button type="submit" onclick="return false;" class="hide"></button>
        <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
        <input type="hidden" id="entryID" name="entryID" value='<s:property value="productDeliverMainData.WorkFlowID"/>'/>
        <input type="hidden" id="actionID" name="actionID"/>
        <input type="hidden" id="deliverType" name="deliverType" value='<s:property value="productDeliverMainData.DeliverType"/>'/>
        <div class="section">
        <div id="actionResultDisplay"></div>
        <%-- ================== 错误信息提示 START ======================= --%>
        <div id="errorDiv2" class="actionError" style="display:none">
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
          		<input type="hidden" name="billType" value="SD"/>
          		<input type="hidden" name="pageId" value="BINOLSTSFH05"/>
          		<input type="hidden" name="billId" value="${productDeliverId}"/>
          	</div>
          	<%-- <cherry:show domId="BINOLSTSFH05PNT">
          	<button onclick="openPrintApp('Print');return false;" class="confirm right">
				<span class="ui-icon icon-file-print"></span>
				<span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
			</button>
			</cherry:show> --%>
			<cherry:show domId="BINOLSTSFH05VEW">
			<button onclick="openPrintApp('View');return false;" class="confirm right">
				<span class="ui-icon icon-file-view"></span>
				<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
			</button>
			</cherry:show>
            <cherry:show domId="BINOLSTSFH05EXP">
            	<div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${productDeliverId}"/></div>
	             <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTSFH05',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
	             	<span class="ui-icon icon-file-export"></span>
	             	<span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
	             </button>
	        </cherry:show>
          </div>
          
            <div class="section-content">
                <div>
                <div class="box-header"></div>
                <table class="detail">
                    <tbody>
                        <tr>
                            <%-- 发货单单号 --%>
                            <th><s:text name="SFH05_deliverNo"/></th>
                            <td><s:property value="productDeliverMainData.DeliverNoIF"/></td>
                            <%-- 发货单日期 --%>
                            <th><s:text name="SFH05_date"/></th>
                            <td><s:property value="productDeliverMainData.Date"/></td>
                        </tr>
                        <tr>
                            <%-- 关联单号 --%>
                            <th><s:text name="SFH05_relevantNo"/></th>
                            <td><s:property value="productDeliverMainData.RelevanceNo"/></td>
                            <%-- 处理状态 --%>
                            <th><s:text name="SFH05_tradeStatus"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1141", productDeliverMainData.TradeStatus)'/></td>
                        </tr>
                        <tr>
                            <%-- 发货部门 --%>
                            <th><s:text name="SFH05_depart"/></th>
                            <s:if test='"34".equals(operateType) || "32".equals(operateType) || "31".equals(operateType) || ("40".equals(operateType) && productDeliverMainData.WorkFlowCode.equals("proFlowOD"))'>
                                <td>
                                    <span id="outOrgName" class="left"><s:property value="productDeliverMainData.DepartCodeName"/></span>
                                    <div id="showBtnopenDepartBox" class="hide">
                                        <a class="add right hide" onclick="BINOLSTSFH05.openDepartBox(this);">
                                            <span class="ui-icon icon-search"></span>
                                            <span class="button-text"><s:text name="SFH05_select"/></span>
                                        </a>
                                    </div>
                                </td>
                            </s:if>
                            <s:else>
                                <td><s:property value="productDeliverMainData.DepartCodeName"/></td>
                            </s:else>
                            <%-- 操作员 --%>
                            <th><s:text name="SFH05_employeeName"/></th>
                            <td><s:property value="productDeliverMainData.EmployeeName"/></td>
                        </tr>
                        <tr>
                            <%--实体仓名称 --%>
                            <th><s:text name="SFH05_DepotCodeName"/></th>
                            <s:if test='"34".equals(operateType) || "32".equals(operateType) || "31".equals(operateType) || ("40".equals(operateType) && productDeliverMainData.WorkFlowCode.equals("proFlowOD"))'>
                                <td>
	                                <input id="outDepot" type="hidden" value='<s:property value="productDeliverMainData.DepotCodeName"/>'/>
	                                <s:if test='null!=depotsInfoList'>
	                                    <div id="showAcceptDepotCodeName" class="hide">
	                                        <s:select id="outDepotInfoId" name="outDepotInfoId" onchange="BINOLSTSFH05.refreshStockCount();"  list="depotsInfoList" value="%{productDeliverMainData.BIN_DepotInfoID}" listKey="BIN_DepotInfoID" listValue="DepotCodeName" headerKey="" cssStyle="width:200px;">
	                                        </s:select>
	                                    </div>
	                                    <div id="hideAcceptDepotCodeName">
	                                       <s:property value="productDeliverMainData.DepotCodeName"/>
	                                    </div>
	                                </s:if>
                                </td>
                            </s:if>
                            <s:else>
                                <td><s:property value="productDeliverMainData.DepotCodeName"/></td>
                            </s:else>
                            <%-- 逻辑仓名称 --%>
                            <th><s:text name="SFH05_LogicInventoryName"/></th>
                            <s:if test='"34".equals(operateType) || "32".equals(operateType) || "31".equals(operateType) || ("40".equals(operateType) && productDeliverMainData.WorkFlowCode.equals("proFlowOD"))'>
                                <td>
	                                <s:if test='null!=logicDepotsInfoList'>
	                                    <div id="showAcceptLogicInventoryName" class="hide">
	                                        <s:select id="outLogicDepotsInfoId" name="outLogicDepotsInfoId" list="logicDepotsInfoList" onchange="BINOLSTSFH05.refreshStockCount();" value="%{productDeliverMainData.BIN_LogicInventoryInfoID}"  listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" cssStyle="width:200px;">
	                                        </s:select>
	                                    </div>
	                                    <div id="hideAcceptLogicInventoryName">
	                                        <s:property value="productDeliverMainData.LogicInventoryName"/>
	                                    </div>
	                                </s:if>
                                </td>
                            </s:if>
                            <s:else>
                                <td><s:property value="productDeliverMainData.LogicInventoryName"/></td>
                            </s:else>
                        </tr>
                        <tr>
                            <%--发货类型--%>
                            <th><s:text name="SFH05_deliverType"/></th>
                            <td>
                                <div id="showDeliverTypeSelect" class="hide">
                                    <s:select onchange="BINOLSTSFH05.refreshDeliverType(this);" list="#application.CodeTable.getCodes('1168')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{pleaseSelect}" cssStyle="width:200px" value="%{productDeliverMainData.DeliverType}"></s:select>
                                </div>
                                <div id="hideDeliverType">
                                    <s:property value='#application.CodeTable.getVal("1168", productDeliverMainData.DeliverType)'/>
                                </div>
                            </td>
                            <%-- 导入批次 --%>
                            <th><s:text name="SFH05_importBatch"/></th>
                            <td><s:property value='productDeliverMainData.ImportBatch'/></td>
                        </tr>
                        <tr>
                            <%--预计到货日期 --%>
                            <th><s:text name="SFH05_PlanArriveDate"/></th>
                            <td>
                                <div id="hideEditPlanArriveDate">
                                    <s:property value="productDeliverMainData.PlanArriveDate"/>
                                </div>
                                <div id="showEditPlanArriveDate" class="hide">
                                    <s:textfield id="planArriveDate" cssStyle="width:80px;" name="planArriveDate"
                                        value="%{productDeliverMainData.PlanArriveDate}" cssClass="date"/>
                                </div>
                            <th></th>
                            <td></td>
                        </tr>
                        <tr>
                        	<%-- 发货理由 --%>
                            <th><s:text name="SFH05_reason"/></th>
                            <td colspan="3"><s:property value='productDeliverMainData.Comments'/></td>
                        </tr>
                    </tbody>
                </table>
                <table class="detail">
                    <tbody>
                    <s:if test='"50".equals(operateType)'>
                     <tr>
                            <%-- 收货部门 --%>
                            <th><s:text name="SFH05_receiveDepart"/></th>
                            <td><span id="orderDepartCodeName"><s:property value="productDeliverMainData.ReceiveDepartCodeName"/></span></td>
                           	<%-- 收货实体仓库 --%>
                            <th><s:text name="SFH05_receiveDepot"/></th>
                            <td>
                                <s:select disabled="true" name="receiveDepotSelect" list="receiveDepotList" listKey="BIN_DepotInfoID" 
                                    listValue="DepotCodeName" cssStyle="width:200px;color:black;" onchange="BINOLSTSFH05.setVal('receiveDepotSelect','receiveDepot')"></s:select>
                                <input type="hidden" id="receiveDepot" name="receiveDepot" value="<s:property value="receiveDepotList.get(0).get('BIN_DepotInfoID')"/>"></input>
                                <a onclick="BINOLSTSFH05.openEdit('receiveDepotSelect')" class="delete">
                                    <span class="ui-icon icon-edit"></span>
                                    <span class="button-text"><s:text name="SFH05_edit"/></span>
                                </a>
                            </td>
                        </tr>
                        <tr>
                        	<%-- 收货逻辑仓库 --%>
                        	<th><s:text name="SFH05_receiveLogiInven"/></th>
                        	<td>
                        	   <s:select disabled="true" name="receiveLogiInvenSelect" list="receiveLogiInvenList" listKey="BIN_LogicInventoryInfoID" 
                        	       listValue="LogicInventoryCodeName" cssStyle="width:200px;color:black;" onchange="BINOLSTSFH05.setVal('receiveLogiInvenSelect','receiveLogiInven')"></s:select>
                        	   <input type="hidden" id="receiveLogiInven" name="receiveLogiInven" value="<s:property value="receiveLogiInvenList.get(0).get('BIN_LogicInventoryInfoID')"/>"></input>
                        	   <a onclick="BINOLSTSFH05.openEdit('receiveLogiInvenSelect')" class="delete">
                        	       <span class="ui-icon icon-edit"></span>
                        	       <span class="button-text"><s:text name="SFH05_edit"/></span>
                        	   </a>
                        	</td>
                        	<th></th>
                        	<td></td>
                        </tr>
                    </s:if>
                    <s:else>
                    <tr>
                            <%-- 收货部门 --%>
                            <th><s:text name="SFH05_receiveDepart"/></th>
                            <td><span id="orderDepartCodeName"><s:property value="productDeliverMainData.ReceiveDepartCodeName"/></span></td>
                            <th></th>
                        	<td></td>
                        </tr>
                    </s:else>
                        <tr>
                            <%--收货地址 --%>
                            <th><s:text name="SFH05_address"/></th>
                            <td colspan=3><s:property value="productDeliverMainData.ReceiveDepartAddress"/></td>
                        </tr>
                    </tbody>
                </table>
                <table class="detail" style="margin-bottom: 5px;">
                    <tbody>
                        <tr>
                            <%-- 审核状态 --%>
                            <th><s:text name="SFH05_verifiedFlag"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1180", productDeliverMainData.VerifiedFlag)'/></td>
                            <%-- 审核者 --%>
                            <th><s:text name="SFH05_employeeNameAudit"/></th>
                            <td><s:property value="productDeliverMainData.EmployeeNameAudit"/></td>
                        </tr>
                        <s:if test='"50".equals(operateType)'>
		                   <tr>
		                       <%-- 收货备注 --%>
		                       <th><s:text name="SFH05_remark"/></th>
		                       <td colspan=3><input class="text" type="text" name="comments" id="comments" maxlength="200" style="width:95%;"/></td>
		                       
		                   </tr>
		                </s:if>
                    </tbody>
                </table>
                
                <div class="clearfix"></div>
            </div>
          </div>
        </div>
        <div class="section">
          <div class="section-header">
          	<strong>
          		<span class="ui-icon icon-ttl-section-search-result"></span>
	            <%-- 入出库明细一览 --%>
	            <s:text name="SFH05_results_list"/>
            </strong>
          </div>
          <div class="section-content">
            <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
	          <s:if test='"2".equals(operateType)'>
	                <%
	                if(optionEmployeeId.equals(String.valueOf(mainData.get("BIN_EmployeeID"))) && verifiedFlag.equals(String.valueOf(mainData.get("VerifiedFlag")))) 
	                	{
	                %>
	                <div id="showToolbar" class="">
	                <div class="toolbar clearfix">
	                    <span class="left">
                            <a class="add" onclick="BINOLSTSFH05.openProStockPopup(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="SFH05_ReceiverStock"/></span>
                            </a>
                            <span id="showAddBtn" class="hide">
	                        <a class="add" onclick="BINOLSTSFH05.openProPopup(this);">
	                            <span class="ui-icon icon-add"></span>
	                            <span class="button-text"><s:text name="SFH05_add"/></span>
	                        </a>
	                        </span>
	                        <span id="showDelBtn" class="hide">
	                         <a class="delete" onclick="BINOLSTSFH05.deleteLine();">
	                            <span class="ui-icon icon-delete"></span>
	                            <span class="button-text"><s:text name="SFH05_delete"/></span>
	                        </a>
	                        </span>
	                    </span>
	                </div>
	                </div>
	                <%} %>
	            </s:if>
	            <s:elseif test='"41".equals(operateType) || "42".equals(operateType) || "46".equals(operateType) || "34".equals(operateType) || "32".equals(operateType) || "31".equals(operateType) || "40".equals(operateType)'>
                    <div class="toolbar clearfix">
                        <span class="left">
                            <s:if test='"true".equals(productDeliverMainData.showRecStockFlag)'>
	                            <a class="add" onclick="BINOLSTSFH05.openProStockPopup(this);">
	                                <span class="ui-icon icon-search"></span>
	                                <span class="button-text"><s:text name="SFH05_CounterStock"/></span>
	                            </a>
                            </s:if>
                            <span id="showAddBtn" class="hide">
                            <a class="add" onclick="BINOLSTSFH05.openProPopup(this);">
                                <span class="ui-icon icon-add"></span>
                                <span class="button-text"><s:text name="SFH05_add"/></span>
                            </a>
                            </span>
                            <span id="showDelBtn" class="hide">
                            <a class="delete" onclick="BINOLSTSFH05.deleteLine();">
                                <span class="ui-icon icon-delete"></span>
                                <span class="button-text"><s:text name="SFH05_delete"/></span>
                            </a>
                            </span>
                            <span class="highlight"><s:text name="global.page.snow"/></span>
                        	<s:text name="SFH05_abnormalInfo"/>
                        </span>
                    </div>
	            </s:elseif>
	            <s:else>
	               <div class="toolbar clearfix">
	                   <span class="left">
                            <s:if test='"true".equals(productDeliverMainData.showRecStockFlag)'>
                                <a class="add" onclick="BINOLSTSFH05.openProStockPopup(this);">
                                    <span class="ui-icon icon-search"></span>
                                    <span class="button-text"><s:text name="SFH05_CounterStock"/></span>
                                </a>
                                <span class="highlight"><s:text name="global.page.snow"/></span>
                        		<s:text name="SFH05_abnormalInfo"/>
                            </s:if>
	                   </span>
	               </div>
	            </s:else>
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                	<s:if test='"2".equals(operateType)'>
                		<%
		                if(optionEmployeeId.equals(String.valueOf(mainData.get("BIN_EmployeeID"))) && verifiedFlag.equals(String.valueOf(mainData.get("VerifiedFlag")))) 
		                	{
		                %>
                		<th id="showCheckbox" class="center hide" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTSFH05.selectAll(this);"/><s:text name="SFH05_select"/></th><%-- 选择 --%>
                		<%} %>
                	</s:if>
                	<s:elseif test='"40".equals(operateType) || "41".equals(operateType) || "42".equals(operateType) || "46".equals(operateType) || "34".equals(operateType) || "32".equals(operateType) || "31".equals(operateType)'>
                	    <th id="showCheckbox" class="center hide" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTSFH05.selectAll(this);"/><s:text name="SFH05_select"/></th><%-- 选择 --%>
                	</s:elseif>
                    <th id="th_No" class="center"><s:text name="SFH05_no"/></th><%-- 编号 --%>
                    <th id="th_UnitCode" class="center"><s:text name="SFH05_UnitCode"/></th><%-- 厂商编码 --%>
                    <th id="th_BarCode" class="center"><s:text name="SFH05_BarCode"/></th><%-- 产品条码 --%>
                    <th id="th_ProductName" class="center"><s:text name="SFH05_ProductName"/></th><%-- 产品名称 --%>
                    <th id="th_ReferencePrice" class="center" width="10%"><s:text name="SFH05_ReferencePrice"/></th><%-- 参考价格 --%>
                    <th class="center" width="8%"><s:text name="SFH05_costPrice"/></th><%--成本价 --%>
                  	<th class="center" width="8%"><s:text name="SFH05_totalCostPrice"/></th><%--总成本价 --%>
                    <th id="th_Price" class="center" width="10%"><s:text name="SFH05_Price"/>
                        <span id="spanBatchCalc" style="display:none;" class="calculator" onclick="BINOLSTSFH05.initRateDiv(this,'batch');" title="<s:text name="SFH05_BatchCalTitle"/>"></span>
                        <input type="hidden" id="batchPriceRate" value="100.00">
                    </th><%-- 实际执行价 --%>
                    <%--<s:if test='"40".equals(operateType)'>--%>
                        <%-- 折扣价格 --%>
                        <%--
                        <th id="thDiscountPrice" class="center hide"><s:text name="SFH05_discountPrice"/>
                        <span class="calculator" onclick="BINOLSTSFH05.initRateDiv(this,'batch');" title="<s:text name="SFH05_BatchCalTitle"/>"></span>
                        <input type="hidden" id="batchPriceRate">
                        </th>
                         --%>
                    <%--</s:if>--%>
                    <s:if test='"2".equals(operateType)'>
                    	<%
		                if(optionEmployeeId.equals(String.valueOf(mainData.get("BIN_EmployeeID"))) && verifiedFlag.equals(String.valueOf(mainData.get("VerifiedFlag")))) 
		                	{
		                %>
                    	<th id="th_ProductQuantity" class="center"><s:text name="SFH05_ProductQuantity"/></th><%-- 库存 --%>
                    <%} %>
                    </s:if>
                    <s:elseif test='"40".equals(operateType) || "41".equals(operateType) || "42".equals(operateType) || "46".equals(operateType)'>
                		<th id="th_ProductQuantity" class="center"><s:text name="SFH05_ProductQuantity"/></th><%-- 库存 --%>
                	</s:elseif>
                	<s:elseif test='"34".equals(operateType) || "32".equals(operateType) || "31".equals(operateType)'>
                        <th id="th_ProductQuantity"  class="center"><s:text name="SFH05_ProductQuantity"/></th><%-- 库存 --%>
                        <th id="th_OrderQuantity" class="center"><s:text name="SFH05_OrderQuantity"/></th><%-- 订单数量 --%>
                	</s:elseif>
                    <th id="th_Quantity" class="center"><s:text name="SFH05_Quantity"/></th><%-- 数量 --%>
                    <th id="th_Amount" class="center"><s:text name="SFH05_Amount"/></th><%-- 金额 --%>
                    <th id="th_Remark" class="center"><s:text name="SFH05_remark"/></th><%-- 备注 --%>
                </tr>
              </thead>
              <tbody id="databody">
                <s:iterator value="productDeliverDetailData" status="status">
                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else> <s:if test="abnormalQuantityFlag.equals('true')">abnormal</s:if>">
                        <s:if test='"2".equals(operateType)'>
                            <%
			                if(optionEmployeeId.equals(String.valueOf(mainData.get("BIN_EmployeeID"))) && verifiedFlag.equals(String.valueOf(mainData.get("VerifiedFlag")))) 
			                	{
			                %>
                            <td id="dataTd0" class="hide"><input name="chkbox" name="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTSFH05.changechkbox(this);"/></td>
                        	<%} %>
                        </s:if>
                        <s:elseif test='"40".equals(operateType) || "41".equals(operateType) || "42".equals(operateType) || "46".equals(operateType)|| "34".equals(operateType) || "32".equals(operateType) || "31".equals(operateType)'>
                            <td id="dataTd0" class="hide"><input name="chkbox" name="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTSFH05.changechkbox(this);"/></td>
                        </s:elseif>
                        <td id="dataTd1"><s:property value="#status.index+1"/></td>
                        <td id="dataTd2"><span><s:property value="UnitCode"/></span></td>
                        <td id="dataTd4"><span><s:property value="BarCode"/></span></td>
                        <td id="dataTd3"><span><s:property value="ProductName"/></span></td>
                        <td id="dataTdReferencePrice" class="alignRight">
                            <s:if test='null!=ReferencePrice'>
                                <s:text name="format.price"><s:param value="ReferencePrice"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        
                        <!-- 显示总成本价，平均成本价 -->
                        <s:if test='null!=totalCostPrice1 && ""!=totalCostPrice1'>
	                        <td style="text-align:right;" >
	                        	<div id="costPrice">${costPrice1}</div>
	                        </td>
	                        <td style="text-align:right;" >
	                        <div id="totalCostPrice">${totalCostPrice1}</div>
	                        </td>
                        </s:if>
                        <s:else>
                        	<s:if test='null!=totalCostPrice && ""!=totalCostPrice'>
		                        <td style="text-align:right;" >
		                        	<div id="costPrice">${costPrice}</div>
		                        </td>
		                        <td style="text-align:right;" >
		                        <div id="totalCostPrice">${totalCostPrice}</div>
		                        </td>
	                        </s:if>
	                        <s:else>
	                        	<td style="text-align:right;" ><div id="costPrice"></div></td>
		                        <td style="text-align:right;"><div id="totalCostPrice"></div></td>
	                        </s:else>
                        </s:else>
                        
                        <td id="dataTd5" class="alignRight">
                                <s:textfield name="priceUnitArr" cssClass="price hide" size="10" maxlength="9" value="%{getText('{0,number,##0.00}',{Price})}" onchange="BINOLSTSFH05.setPrice(this);return false;"></s:textfield>
                                <span id="spanCalPrice" class="hide"><span class="calculator" onclick="BINOLSTSFH05.initRateDiv(this);" title="<s:text name="SFH05_CalTitle"/>"></span></span>
                                <div id="hidePriceArr">
                                    <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                                </div>
                                <%-- <input type="hidden" name="costPrice" value="<s:property value="Price"/>" />--%>
                        </td>
                        <%--<s:if test='"40".equals(operateType)'>--%>
                            <%--
                            <td id="tdDiscountPrice" class="alignRight hide" style="width:10%;">
                                <s:textfield name="priceArr" cssClass="price hide" size="10" maxlength="9" value="%{getText('{0,number,##0.00}',{Price})}" onchange="BINOLSTSFH05.setPrice(this);return false;"></s:textfield>
                                <span id="spanCalPrice" class="hide"><span class="calculator" onclick="BINOLSTSFH05.initRateDiv(this);" title="<s:text name="SFH05_CalTitle"/>"></span></span>
                                <div id="hidePriceArr">
                                    <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                                </div>
                                <input type="hidden" name="costPrice" value="<s:property value="Price"/>" />
                            </td>
                            --%>
                        <%--</s:if>--%>
                        <s:if test='"2".equals(operateType)'>
                            <%
			                if(optionEmployeeId.equals(String.valueOf(mainData.get("BIN_EmployeeID"))) && verifiedFlag.equals(String.valueOf(mainData.get("VerifiedFlag")))) 
			                	{
			                %>
                            <td id="dataTd6" class="alignRight">
                                <s:if test='null!=ProductQuantity'>
                                    <s:text name="format.number"><s:param value="ProductQuantity"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <%} %>
                        </s:if>
                        <s:elseif test='"40".equals(operateType) || "41".equals(operateType) || "42".equals(operateType) || "46".equals(operateType)'>
                         	<td id="dataTd6" class="alignRight">
                                <s:if test='null!=ProductQuantity'>
                                    <s:text name="format.number"><s:param value="ProductQuantity"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                        </s:elseif>
                        <s:elseif test='"34".equals(operateType) || "32".equals(operateType) || "31".equals(operateType)'>
                            <td id="dataTd6" class="alignRight">
                                <s:if test='null!=ProductQuantity'>
                                    <s:text name="format.number"><s:param value="ProductQuantity"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td id="orderQuantity" class="alignRight"><span><s:text name="format.number"><s:param value="OrderQuantity"/></s:text></span></td>
                        </s:elseif>
                        <td id="newCount" class="alignRight" style="width:10%;">
                             <s:if test='"2".equals(operateType)'>
	                        	<%
				                if(optionEmployeeId.equals(String.valueOf(mainData.get("BIN_EmployeeID"))) && verifiedFlag.equals(String.valueOf(mainData.get("VerifiedFlag")))) 
				                	{
				                %>
	                        	<s:textfield name="quantityArr" cssClass="text-number hide" size="10" maxlength="9" value="%{Quantity}" onkeyup="BINOLSTSFH05.changeCount(this);" onchange="BINOLSTSFH05.changeCount(this);"></s:textfield>
                                <div id="hideQuantiyArr">
                                    <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                                </div>
	                       		<%} else{%>
	                       			<s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
	                       		<%} %>
	                        </s:if>
	                        <s:elseif test='"41".equals(operateType) || "42".equals(operateType) || "46".equals(operateType) || "34".equals(operateType) || "32".equals(operateType) || "31".equals(operateType) || "40".equals(operateType)'>
                                <s:textfield name="quantityArr" cssClass="text-number hide" size="10" maxlength="9" value="%{Quantity}"  onkeyup="BINOLSTSFH05.changeCount(this);" onchange="BINOLSTSFH05.changeCount(this);"></s:textfield>
                                <div id="hideQuantiyArr">
                                    <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                                </div>
                            </s:elseif>
                            <s:elseif test='null!=Quantity'>
                                <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                            </s:elseif>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="money" class="alignRight">
                            <s:if test='null!=Price && null!=Quantity'>
                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="dataTd9">
	                        <s:if test='"2".equals(operateType)'>
	                        	<%
				                if(optionEmployeeId.equals(String.valueOf(mainData.get("BIN_EmployeeID"))) && verifiedFlag.equals(String.valueOf(mainData.get("VerifiedFlag")))) 
				                {
				                %>
	                        	<s:textfield name="commentsArr" size="25" maxlength="200" value="%{Comments}" cssStyle="width:98%" cssClass="hide"></s:textfield>
                                <div id="hideComments">
                                    <p><s:property value="Comments"/></p>
                                </div>
	                        	<%} else{%>
	                        		<p><s:property value="Comments"/></p>
	                        	<%} %>
	                        </s:if>
	                        <s:elseif test='"41".equals(operateType) || "42".equals(operateType) || "46".equals(operateType)|| "34".equals(operateType) || "32".equals(operateType) || "31".equals(operateType) || "40".equals(operateType)'>
                                <s:textfield name="commentsArr" size="25" maxlength="200" value="%{Comments}" cssStyle="width:98%" cssClass="hide"></s:textfield>
                                <div id="hideComments">
                                    <p><s:property value="Comments"/></p>
                                </div>
	                        </s:elseif>
	                        <s:else>
	                        	<p><s:property value="Comments"/></p>
	                        </s:else>
                        </td>
                        <td style="display:none" id="dataTd10">
	                       <input type="hidden" id="referencePriceArr<s:property value='#status.index+1'/>" name="referencePriceArr" value="<s:property value='ReferencePrice'/>"/>                   
	                       <input type="hidden" id="suggestedQuantityArr<s:property value='#status.index+1'/>" name="suggestedQuantityArr" value="<s:property value='SuggestedQuantity'/>"/>
	                       <input type="hidden" id="productVendorIDArr<s:property value='#status.index+1'/>" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
	                       
	                       <!-- 显示总成本价，平均成本价 -->
	                        <s:if test='null!=costPrice1 && ""!=costPrice1'>
		                        	<input type="hidden" id="costPriceArr" name="costPriceArr" value='${costPrice1}'/>
	                        </s:if>
	                        <s:else>
	                        	<s:if test='null!=costPrice && ""!=costPrice'>
		                        	<input type="hidden" id="costPriceArr" name="costPriceArr" value='${costPrice}'/>	    
		                        </s:if>
		                        <s:else>
			                        <input type="hidden" id="costPriceArr" name="costPriceArr" value=""/>	    
		                        </s:else>
	                        </s:else>                       
	                       <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
	                   </td>
                    </tr>
                </s:iterator>
              </tbody>
            </table>
            </div>
            <div style="display:none">
                <input type="hidden" id="rowNumber" value="<s:property value='productDeliverDetailData.size()'/>"/>
                <input type="hidden" value="<s:property value="productDeliverMainData.UpdateTime"/>" name="updateTime" id="updateTime">
                <input type="hidden" value="<s:property value="productDeliverMainData.ModifyCount"/>" name="modifyCount" id="modifyCount">
                <input type="hidden" value="<s:property value="productDeliverMainData.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
                <%-- 发货部门 --%>
                <input type="hidden" value="<s:property value="productDeliverMainData.BIN_OrganizationID"/>" id="outOrganizationID" name="outOrganizationID">
                <%-- 发货实体仓库 --%>
                <input type="hidden" value="<s:property value="productDeliverMainData.BIN_DepotInfoID"/>" id="outDepotInfoID" name="outDepotInfoID">
                <%-- 发货逻辑仓库 --%>
                <input type="hidden" value="<s:property value="productDeliverMainData.BIN_LogicInventoryInfoID"/>" id="outLogicInventoryInfoID" name="outLogicInventoryInfoID">
                <%-- 收货部门 --%>
                <input type="hidden" value="<s:property value="productDeliverMainData.BIN_OrganizationIDReceive"/>" id="inOrganizationID" name="inOrganizationID">              
                
                <s:hidden id="productDeliverID" name="productDeliverId" value="%{#request.productDeliverId}"></s:hidden>
                <input type="hidden" id="inTestType" value="<s:property value='productDeliverMainData.TestType'/>"></input>
                <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
                <input type="hidden" id="checkStockFlag" name="checkStockFlag" value="<s:property value='checkStockFlag'/>"/>
                <input type="hidden" id="lockSection" name="lockSection" value="<s:property value='lockSection'/>"/>
                <input type="hidden" id="totalAmountCheck" name="totalAmountCheck" value="<s:property value='productDeliverMainData.TotalAmount'/>"/>
            </div>
            
            <hr class="space" />
            	<table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="SFH05_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="SFH05_totalAmount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td id="totalQuantity" class="center">
                    <%-- 总数量 --%>
                    <s:if test='null!=productDeliverMainData.TotalQuantity'>
                        <span><s:text name="format.number"><s:param value="productDeliverMainData.TotalQuantity"></s:param></s:text></span>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </td>
                <td id="totalAmount" class="center">
                    <%-- 总金额--%>
                    <s:if test='null!=productDeliverMainData.TotalAmount'>
                        <span><s:text name="format.price"><s:param value="productDeliverMainData.TotalAmount"></s:param></s:text></span>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </td>
              </tr>
            </table>
            <hr class="space" />
            <div class="center">
             <s:if test='"2".equals(operateType)'>
                <%
                if(optionEmployeeId.equals(String.valueOf(mainData.get("BIN_EmployeeID"))) && verifiedFlag.equals(String.valueOf(mainData.get("VerifiedFlag")))) 
                	{
                %>
                	<button id="saveBut" class="save" style="display:none;" onclick="BINOLSTSFH05.save();return false;"><span class="ui-icon icon-save"></span>
                    <%-- 保存 --%>
                    <span class="button-text"><s:text name="global.page.save"/></span>
	                </button>
	                <button class="confirm" onclick="BINOLSTSFH05.submit();return false;"><span class="ui-icon icon-confirm"></span>
	                    <%-- 提交 --%>
	                    <span class="button-text"><s:text name="global.page.submit"/></span>
	                </button>
	                <button id="btn-icon-edit-big" class="confirm" onclick="BINOLSTSFH05.modifyOrder();return false;">
                        <span class="ui-icon icon-edit-big"></span>
                        <%-- 修改 --%>
                        <span class="button-text"><s:text name="os.edit"/></span>
	                </button>
                    <button class="delete" onclick="BINOLSTSFH05.deleteBill();return false;"><span class="ui-icon icon-delete-big"></span>
                        <%-- 删除 --%>
                        <span class="button-text"><s:text name="global.page.delete"/></span>
                    </button>
                <%} %>
            </s:if>
            <s:elseif test='"34".equals(operateType) || "32".equals(operateType) || "40".equals(operateType) || "41".equals(operateType) || "42".equals(operateType) || "46".equals(operateType) || "50".equals(operateType) || "31".equals(operateType)'>
                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
                <s:if test='"42".equals(operateType) || "32".equals(operateType)'>
                    <button id="saveBut" style="display:none;" class="save" onclick="BINOLSTSFH05.save();return false;">
                        <span class="ui-icon icon-save"></span>
                        <%-- 保存 --%>
                        <span class="button-text"><s:text name="global.page.save"/></span>
                    </button>
                    <button id="btn-icon-edit-big" class="confirm" onclick="BINOLSTSFH05.modifyOrder();return false;">
                        <span class="ui-icon icon-edit-big"></span>
                        <%-- 修改 --%>
                        <span class="button-text"><s:text name="os.edit"/></span>
                    </button>
                </s:if>
                <s:elseif test='"31".equals(operateType)'>
                    <button id="saveBut" style="display:none;" class="save" onclick="BINOLSTSFH05.save();return false;">
                        <span class="ui-icon icon-save"></span>
                        <%-- 保存 --%>
                        <span class="button-text"><s:text name="global.page.save"/></span>
                    </button>
                </s:elseif>
                <s:if test='"40".equals(operateType)'>
                    <button id="btnSDSubmit" class="confirm" style="display:none;" onclick="doaction(<s:property value="productDeliverMainData.WorkFlowID"/>,804,&quot;&quot;,&quot;&quot;);return false;"><span class="ui-icon icon-confirm"></span>
                    <%-- 提交 --%>
                    <span class="button-text"><s:text name="global.page.submit"/></span>
                    </button>
                </s:if>
            </s:elseif>
            <button id="btnReturn" class="close" onclick="BINOLSTSFH05.back();return false;" style="display:none;">
                <span class="ui-icon icon-back"></span>
                <%--返回 --%>
                <span class="button-text"><s:text name="global.page.back"/></span>
            </button>
              <button class="close" onclick="window.close();return false;"><span class="ui-icon icon-close"></span>
              <%-- 关闭 --%>
              <span class="button-text"><s:text name="global.page.close"/></span>
             </button>
            </div>
          </div>
        </div>
        </form>
      </div>
      </div>
      <div id="tabs-2">
      <strong><s:text name="global.page.worksProcessing"/></strong>
   </div>
</div>
    </div>
    </div>
    <div class="rateDialog hide">
        <span id="spanCalTitle" style="display:none;"><s:text name="SFH05_CalTitle"/></span>
        <s:text name="SFH05_discountRate"/><%-- 折扣率 --%>
        <input class="number" id="priceRate" value="100" onblur="BINOLSTSFH05.closeDialog(this);return false;"  
            onkeyup="BINOLSTSFH05.setDiscountPrice(this);return false;"/><s:text name="global.page.percent"/>
        <input type ="hidden" id="curRateIndex" value=""/>
    </div>
</s:i18n>
<form action="BINOLSTSFH05_init" id="productDeliverDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="productDeliverId" value="%{#request.productDeliverId}"></s:hidden>
</form>
<div class="hide" id="dialogInit"></div>
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
    <input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
    <input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="ESS00040"/>'/>
    <input type="hidden" id="errmsg_EST00013" value='<s:text name="EST00013"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
    <input type="hidden" id="errmsg_EST00025" value='<s:text name="EST00025"/>'/>
    <input type="hidden" id="errmsg_EST00034" value='<s:text name="EST00034"/>'/>
</div>
<%-- ================== 工作流提示弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOSDialog.jsp" flush="true" />
<%-- ==================  工作流提示弹出窗口End ======================== --%>
