<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL10.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>

<s:i18n name="i18n.st.BINOLSTBIL10">
<div id="div_main" class="panel ui-corner-all">
<s:url id="doaction_url" value="/st/BINOLSTBIL10_doaction"/>
<s:url id="save_url" value="/st/BINOLSTBIL10_save"/>
<s:url id="submit_url" value="/st/BINOLSTBIL10_submit"/>
<s:url id="delete_url" value="/st/BINOLSTBIL10_delete"/>
<s:url id="url_getStockCount" value="/st/BINOLSTIOS05_getStockCount" />
<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>
<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="saveUrl" href="${save_url}"></a>
    <a id="submitUrl" href="${submit_url}"></a>
    <a id="deleteUrl" href="${delete_url}"></a>
    <input type="hidden" id="allowNegativeFlag" name="allowNegativeFlag" value='<s:property value="allowNegativeFlag" />'/>
</div>
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL10_title"/>&nbsp;(<s:text name="BIL10_stockTakingNo"/>:<s:property value="takingInfo.StockTakingNo"/>)</span>
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
        <div class="section">
        	<div id="actionResultDisplay"></div>
            <%-- ================== 错误信息提示 START ======================= --%>
		    <div id="errorDiv2" class="actionError" style="display:none;">
		          <ul>
		            <li><span id="errorSpan2"></span></li>
		          </ul>
		     </div>
		        
		        <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    			<input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
		        <%-- ================== 错误信息提示   END  ======================= --%>
            <div class="section-header">
                  <strong>
                      <span class="ui-icon icon-ttl-section-info"></span>
                      <%-- 基本信息 --%>
                      <s:text name="BIL10_header"/>
                  </strong>
                  <div id="print_param_hide" class="hide">
	          		<input type="hidden" name="billType" value='<s:property value="takingInfo.Type"/>'/>
	          		<input type="hidden" name="pageId" value="BINOLSTBIL10"/>
	          		<input type="hidden" name="billId" value="${stockTakingId}"/>
	          		
	          		<s:hidden name="brandInfoId" id="brandInfoId" value="%{brandInfoId}"></s:hidden>
	          		<input type="hidden"  id="depotInfoID" name="depotInfoID"  value='<s:property value="takingInfo.depotInfoID"/>'/>
	          		<input type="hidden" id="logicInventoryInfoID"  name="logicInventoryInfoID"  value='<s:property value="takingInfo.logicInventoryInfoID"/>'/>
          		 </div>
	          	<cherry:show domId="BINOLSTBIL10PNT">
	          	<button onclick="openPrintApp('Print');return false;" class="confirm right">
					<span class="ui-icon icon-file-print"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
				</button>
				</cherry:show>
				<cherry:show domId="BINOLSTBIL10VEW">
				<button onclick="openPrintApp('View');return false;" class="confirm right">
					<span class="ui-icon icon-file-view"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
				</button>
				</cherry:show>
                 <cherry:show domId="BINOLSTBIL10EXP">
            	<div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${stockTakingId}"/><input type="hidden" name="profitKbn" value="${profitKbn}"/></div>
	              <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTBIL10',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
	             	<span class="ui-icon icon-file-export"></span>
	             	<span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
	              </button>
	               </cherry:show>

            </div>
            <div class="section-content">
                <div class="box-header"></div>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <th><%-- 盘点单号 --%><s:text name="BIL10_stockTakingNoIF"/></th>
                        <td><span><s:property value="takingInfo.StockTakingNoIF"/></span></td>
                        <th><%-- 盘点时间--%><s:text name="BIL10_tradeDateTime"/></th>
                        <td><span><s:property value="takingInfo.TradeDateTime"/></span></td>
                    </tr>
                    <tr>
                        <th><%-- 盘点类型 --%><s:text name="BIL10_type"/></th>
                        <td><span><s:property value='#application.CodeTable.getVal("1054", takingInfo.Type)'/></span></td>
                        <th><%-- 盘点员 --%><s:text name="BIL10_employeeName"/></th>
                        <td><span><s:property value="'('+takingInfo.EmployeeCode+')'+takingInfo.EmployeeName"/></span></td>
                    </tr>
                    <tr>
                        <th><%-- 盘点理由 --%><s:text name="BIL10_reason"/></th>
                        <td colspan=3><span><s:property value="takingInfo.Comments"/></span></td>
                    </tr>
                </table>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <th><%-- 部门名称 --%><s:text name="BIL10_departName"/></th>
                        <td><span>(<s:property value="takingInfo.DepartCode"/>)<s:property value="takingInfo.DepartName"/></span></td>
                        <th><%-- 实体仓库 --%><s:text name="BIL10_inventName"/></th>
                        <td><span><s:property value="'('+takingInfo.DepotCode+')'+takingInfo.InventoryName"/></span></td>
                       </tr>
                    <tr>
                        <th><%-- 逻辑仓库 --%><s:text name="BIL10_LogicInventory"/></th>
                        <td><span><s:property value="'('+takingInfo.LogicInventoryCode+')'+takingInfo.LogicInventoryName"/></span></td>
                        <th></th>
                        <td></td>
                    </tr>
                </table>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <th><%-- 审核状态 --%><s:text name="BIL10_verifiedFlag"/></th>
                        <td><span><s:property value='#application.CodeTable.getVal("1322", takingInfo.VerifiedFlag)'/></span></td>
                        <th><%-- 审核人 --%><s:text name="BIL10_auditName"/></th>
                        <td><span><s:property value="'('+takingInfo.AuditCode+')'+takingInfo.AuditName"/></span></td>
                    </tr>
                </table>
                <div class="clearfix"></div>
            </div>
        </div>
        <div class="section">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <%-- 盘点明细一览 --%>
                    <s:text name="BIL10_results_list"/>
                </strong>
                <%-- 已剔除不需要管理库存的产品 --%>
                <span class="red"><s:text name="BIL10_detailNoStockNotice"/></span>
            </div>
            <div class="section-content">
              <div id="showToolbar" class="hide">
                <div class="toolbar clearfix">
                    <span class="left">
                        <span id="showAddBtn">
                        <a class="add" onclick="BINOLSTBIL10.openProPopup(this);">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="BIL10_addProduct"/></span>
                        </a>
                        </span>
                        <span id="showDelBtn">
                        <a class="delete" onclick="BINOLSTBIL10.deleteRow();">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="BIL10_delete"/></span>
                        </a>
                        </span>
                    </span>
                </div>
                </div>
                <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
                <input type="hidden" id="entryID" name="entryID" value='<s:property value="takingInfo.WorkFlowID"/>'/>
                <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
                <table id="sort_table" cellpadding="0" cellspacing="0" border="0" width="100%">
                    <thead>
                        <tr>
                        	 <%--
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
                                <th class="center" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTBIL10.selectAll();"/><s:text name="BIL10_select"/></th>
                            </s:if>
                            --%>
                            <th width="3%"><s:text name="BIL10_num"/></th><%-- No. --%>
                            <th><s:text name="BIL10_unitCode"/></th><%-- 厂商编码 --%>
                            <th style="min-width:90px;" class="th-sort-alpha"><span class="left"><s:text name="BIL10_nameTotal"/></span></th><%-- 产品名称 --%>
                            <th><s:text name="BIL10_barCode"/></th><%-- 促销品条码 --%>
                            <th><s:text name="BIL10_packageName"/></th><%-- 单位 --%>
                            <s:if test='"1".equals(takingInfo.IsBatch)'>
                                <th><s:text name="BIL10_batchNo"/></th><%-- 批次号 --%>
                            </s:if>
                            <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="BIL10_quantity"/></span></th><%-- 账面数量 --%>
                            <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="BIL10_realQuantity"/></span></th><%-- 实盘数量 --%>
                            <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="BIL10_gainQuantity"/></span></th><%-- 盈亏数量 --%>
                            <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="BIL10_price"/></span></th><%-- 单价 --%>
                            <th style="min-width:70px;*width:70px;" class="th-sort-numeric"><span class="left"><s:text name="BIL10_detailAmount"/></span></th><%-- 盘差金额 --%>
                            <th style="min-width:70px;*width:70px;" class="th-sort-numeric"><span class="left"><s:text name="BIL10_AvgCostPrice"/></span></th><%-- 总成本 --%>
                            <th style="min-width:70px;*width:70px;" class="th-sort-numeric"><span class="left"><s:text name="BIL10_TotalCostPrice"/></span></th><%-- 总成本 --%>
                            <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><s:text name="BIL10_bigCateInfo"/></th><%--大分类 --%>
                            <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><s:text name="BIL10_midCateInfo"/></th><%--中分类 --%>
                            <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><s:text name="BIL10_smallCateInfo"/></th><%--小分类 --%>
                            <th width="8%"><s:text name="BIL10_handleType"/></th><%-- 处理方式 --%>
                        </tr>
                    </thead>
                    <tbody>
                    <s:iterator value="takingDetailList" id="takingDetail" status="R">
                       <s:if test='"0".equals(profitKbn)'>
              			<tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>" style="background-color:<s:if test='GainQuantity > 0'>#FFF7D7</s:if><s:else>#E7E7E7</s:else>" onMouseOver="javascript:this.style.backgroundColor='#FAD163'"  onMouseOut="javascript:this.style.backgroundColor=<s:if test='GainQuantity > 0'>'#FFF7D7'</s:if><s:else>'#E7E7E7'</s:else>">
                            <%--
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
                                <td id="dataTd0"><input id="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTBIL10.changechkbox(this);"/></td>
                            </s:if>
                            --%>
                            <td class="rowNum"><s:property value="#R.index+1"/></td>
                            <td>
                                <s:if test='UnitCode != null && !"".equals(UnitCode)'>
                                    <s:property value="UnitCode"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='NameTotal != null && !"".equals(NameTotal)'>
                                    <s:property value="NameTotal"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='BarCode != null && !"".equals(BarCode)'>
                                    <s:property value="BarCode"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='PackageName != null && !"".equals(PackageName)'>
                                    <s:property value="PackageName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <s:if test='"1".equals(takingInfo.IsBatch)'>
	                            <td style=text-align:right;>
	                             	<%--
	                               <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
	                                   <s:textfield name="batchNoArr" cssClass="text-number" size="10" maxlength="9" value="%{BatchNo}" onchange=""></s:textfield>
	                               </s:if>
	                               <s:else>
	                                --%>  
	                                   <s:property value="BatchNo"/>
	                                   <%--
	                               </s:else>
	                                --%> 
	                            </td>
                            </s:if>
                            <td style=text-align:right;>
                                <s:if test="Quantity !=null">
                                    <s:if test="Quantity >= 0"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                           		<%--
	                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
                                    <s:textfield name="quantityArr" cssClass="text-number" size="10" maxlength="9" value="%{RealQuantity}" onchange="BINOLSTBIL10.changeCount(this);"></s:textfield>
	                            </s:if>
	                            <s:else>
	                           	--%>  
	                                <s:if test='RealQuantity != null'>
	                                    <s:text name="format.number"><s:param value="RealQuantity"></s:param></s:text>
	                                </s:if>
	                                <s:else>&nbsp;</s:else>
	                             <%--    
	                            </s:else>
	                             --%> 
                            </td>
                            <td style=text-align:right;>
                                <s:if test="GainQuantity !=null">
                                    <s:if test="GainQuantity >= 0"><s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                                </td>
                            <td style=text-align:right;>
                                <s:if test='Price != null'>
                                    <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="DetailAmount !=null">
                                    <s:if test="DetailAmount >= 0"><s:text name="format.price"><s:param value="DetailAmount"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.price"><s:param value="DetailAmount"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="AvgCostPrice !=null">
                                    <s:if test="AvgCostPrice >= 0"><s:text name="format.price"><s:param value="AvgCostPrice"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.price"><s:param value="AvgCostPrice"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="TotalCostPrice !=null">
                                    <s:if test="TotalCostPrice >= 0"><s:text name="format.price"><s:param value="TotalCostPrice"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.price"><s:param value="TotalCostPrice"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <%--大中小分类 --%>
                           	<td>
                                <s:if test='bigCateInfoName != null && !"".equals(bigCateInfoName)'>
                                   &nbsp;<s:property value="bigCateInfoName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='midCateInfoName != null && !"".equals(midCateInfoName)'>
                                    &nbsp;<s:property value="midCateInfoName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='smallCateInfoName != null && !"".equals(smallCateInfoName)'>
                                    &nbsp;<s:property value="smallCateInfoName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <%-- 盘点处理方式 --%>
                                <span>&nbsp;<s:property value='#application.CodeTable.getVal("1020", HandleType)'/></span>
                            </td>
                        </tr>
                        </s:if>
                        <s:elseif test='"1".equals(profitKbn)'>
                        <tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>" style="background-color:<s:if test='GainQuantity < 0'>#FFF7D7</s:if><s:else>#E7E7E7</s:else>" onMouseOver="javascript:this.style.backgroundColor='#FAD163'"  onMouseOut="javascript:this.style.backgroundColor=<s:if test='GainQuantity < 0'>'#FFF7D7'</s:if><s:else>'#E7E7E7'</s:else>">
                            <td class="rowNum"><s:property value="#R.index+1"/></td>
                            <td>
                                <s:if test='UnitCode != null && !"".equals(UnitCode)'>
                                    <s:property value="UnitCode"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='NameTotal != null && !"".equals(NameTotal)'>
                                    <s:property value="NameTotal"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='BarCode != null && !"".equals(BarCode)'>
                                    <s:property value="BarCode"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='PackageName != null && !"".equals(PackageName)'>
                                    <s:property value="PackageName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <s:if test='"1".equals(takingInfo.IsBatch)'>
	                            <td style=text-align:right;>
	                            	 <%--
	                               <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
	                                   <s:textfield name="batchNoArr" cssClass="text-number" size="10" maxlength="9" value="%{BatchNo}" onchange=""></s:textfield>
	                               </s:if>
	                               <s:else>
	                               --%>
	                                   <s:property value="BatchNo"/>
	                                 <%--
	                               </s:else>
	                                --%>
	                            </td>
                            </s:if>
                            <td style=text-align:right;>
                                <s:if test="Quantity !=null">
                                    <s:if test="Quantity >= 0"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                             	<%--
	                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
                                    <s:textfield name="quantityArr" cssClass="text-number" size="10" maxlength="9" value="%{RealQuantity}" onchange="BINOLSTBIL10.changeCount(this);"></s:textfield>
	                            </s:if>
	                            <s:else>
	                            --%>
	                                <s:if test='RealQuantity != null'>
	                                    <s:text name="format.number"><s:param value="RealQuantity"></s:param></s:text>
	                                </s:if>
	                                <s:else>&nbsp;</s:else>
	                            <%--
	                            </s:else>
	                            --%>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="GainQuantity !=null">
                                    <s:if test="GainQuantity >= 0"><s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                                </td>
                            <td style=text-align:right;>
                                <s:if test='Price != null'>
                                    <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="DetailAmount !=null">
                                    <s:if test="DetailAmount >= 0"><s:text name="format.price"><s:param value="DetailAmount"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.price"><s:param value="DetailAmount"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="AvgCostPrice !=null">
                                    <s:if test="AvgCostPrice >= 0"><s:text name="format.price"><s:param value="AvgCostPrice"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.price"><s:param value="AvgCostPrice"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                              <td style=text-align:right;>
                                <s:if test="TotalCostPrice !=null">
                                    <s:if test="TotalCostPrice >= 0"><s:text name="format.number"><s:param value="TotalCostPrice"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.number"><s:param value="TotalCostPrice"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="AvgCostPrice !=null">
                                    <s:if test="AvgCostPrice >= 0"><s:text name="format.price"><s:param value="AvgCostPrice"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.price"><s:param value="AvgCostPrice"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="TotalCostPrice !=null">
                                    <s:if test="TotalCostPrice >= 0"><s:text name="format.price"><s:param value="TotalCostPrice"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.price"><s:param value="TotalCostPrice"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <%--大中小分类 --%>
                           	<td>
                                <s:if test='bigCateInfoName != null && !"".equals(bigCateInfoName)'>
                                    &nbsp;<s:property value="bigCateInfoName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='midCateInfoName != null && !"".equals(midCateInfoName)'>
                                    &nbsp;<s:property value="midCateInfoName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='smallCateInfoName != null && !"".equals(smallCateInfoName)'>
                                    &nbsp;<s:property value="smallCateInfoName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <%-- 盘点处理方式 --%>
                                <span>&nbsp;<s:property value='#application.CodeTable.getVal("1020", HandleType)'/></span>
                            </td>
                        </tr>
                        </s:elseif>
                         <s:else>
                        <tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>">
                            <td class="rowNum"><s:property value="#R.index+1"/></td>
                            <td>
                                <s:if test='UnitCode != null && !"".equals(UnitCode)'>
                                    <s:property value="UnitCode"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='NameTotal != null && !"".equals(NameTotal)'>
                                    <s:property value="NameTotal"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='BarCode != null && !"".equals(BarCode)'>
                                    <s:property value="BarCode"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='PackageName != null && !"".equals(PackageName)'>
                                    <s:property value="PackageName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <s:if test='"1".equals(takingInfo.IsBatch)'>
	                            <td style=text-align:right;>
	                              <%--
	                               <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
	                                   <s:textfield name="batchNoArr" cssClass="text-number" size="10" maxlength="9" value="%{BatchNo}" onchange=""></s:textfield>
	                               </s:if>
	                               <s:else>
	                               --%>
	                                   <s:property value="BatchNo"/>
	                               <%--
	                               </s:else>
	                               --%>
	                            </td>
                            </s:if>
                            <td style=text-align:right;>
                                <s:if test="Quantity !=null">
                                    <s:if test="Quantity >= 0"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                            	 <%--
	                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
                                    <s:textfield name="quantityArr" cssClass="text-number" size="10" maxlength="9" value="%{RealQuantity}" onchange="BINOLSTBIL10.changeCount(this);"></s:textfield>
	                            </s:if>
	                            <s:else>
	                             --%>
	                                <s:if test='RealQuantity != null'>
	                                    <s:text name="format.number"><s:param value="RealQuantity"></s:param></s:text>
	                                </s:if>
	                                <s:else>&nbsp;</s:else>
	                            <%--  
	                            </s:else>
	                            --%>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="GainQuantity !=null">
                                    <s:if test="GainQuantity >= 0"><s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                                </td>
                            <td style=text-align:right;>
                                <s:if test='Price != null'>
                                    <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="DetailAmount !=null">
                                    <s:if test="DetailAmount >= 0"><s:text name="format.price"><s:param value="DetailAmount"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.price"><s:param value="DetailAmount"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                         <td style=text-align:right;>
                                <s:if test="AvgCostPrice !=null">
                                    <s:if test="AvgCostPrice >= 0"><s:text name="format.price"><s:param value="AvgCostPrice"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.price"><s:param value="AvgCostPrice"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td style=text-align:right;>
                                <s:if test="TotalCostPrice !=null">
                                    <s:if test="TotalCostPrice >= 0"><s:text name="format.price"><s:param value="TotalCostPrice"></s:param></s:text></s:if>
                                    <s:else><span class="highlight"><s:text name="format.price"><s:param value="TotalCostPrice"></s:param></s:text></span></s:else>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <%--大中小分类 --%>
                           	<td>
                                <s:if test='bigCateInfoName != null && !"".equals(bigCateInfoName)'>
                                    &nbsp;<s:property value="bigCateInfoName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='midCateInfoName != null && !"".equals(midCateInfoName)'>
                                    &nbsp;<s:property value="midCateInfoName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <s:if test='smallCateInfoName != null && !"".equals(smallCateInfoName)'>
                                    &nbsp;<s:property value="smallCateInfoName"/>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                            <td>
                                <%-- 盘点处理方式 --%>
                                <span>&nbsp;<s:property value='#application.CodeTable.getVal("1020", HandleType)'/></span>
                            </td>
                        </tr>
                          </s:else>
                    </s:iterator>
                    </tbody>
                </table>
                
                
                <table id="sort_table1" cellpadding="0" cellspacing="0" border="0" width="100%" style="display: none;">
                    <thead>
                        <tr>
                            <th id="dataTh0"  width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTBIL10.selectAll();"/><s:text name="BIL10_select"/></th>
                            <th><s:text name="BIL10_unitCode"/></th><%-- 厂商编码 --%>
                            <th><s:text name="BIL10_barCode"/></th><%-- 促销品条码 --%>
                            <th style="min-width:90px;" class="th-sort-alpha"><span class="left"><s:text name="BIL10_nameTotal"/></span></th><%-- 产品名称 --%>
                            <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="BIL10_quantity"/></span></th><%-- 账面数量 --%>
                            <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="BIL10_realQuantity"/></span></th><%-- 实盘数量 --%>
                            <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="BIL10_gainQuantity"/></span></th><%-- 盈亏数量 --%>
                            <th><s:text name="BIL10_comments"/></th><%-- 备注 --%>
                        </tr>
                    </thead>
                    <tbody id="databody">
                    	<s:iterator value="takingDetailList" id="takingDetail" status="R">
	                    	<tr id="tr_<s:property value='#R.index+1'/>"  class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>">
									<td class="center">
									<input type="hidden" name="prtVendorId" value="<s:property value='prtVendorId'/>"></input>
									<input id="chkbox" type="checkbox" onclick="BINOLSTBIL10.changechkbox(this);"/></td>
									<td id="dataTd1" style="white-space:nowrap"><s:property value="UnitCode"/></td>
									<td id="dataTd2"><s:property value="BarCode"/></td>
									<td id="dataTd3"><s:property value="NameTotal"/></td>
									<td id="dataTd5" style="text-align:right;">
										<s:if test="Quantity !=null">
		                                    <s:if test="Quantity >= 0"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></s:if>
		                                    <s:else><span class="highlight"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></span></s:else>
		                                </s:if>
		                                <s:else>&nbsp;</s:else>
									</td>
									<td id="dataTd6"><span><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" value='<s:text name="format.number"><s:param value="RealQuantity"></s:param></s:text>' onchange="BINOLSTBIL10.changeCount(this);"></input></span></td>
									<td id="gainCount" style="text-align:right;">
										<s:if test="GainQuantity !=null">
	                                    <s:if test="GainQuantity >= 0"><s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text></s:if>
	                                    <s:else><span class="highlight"><s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text></span></s:else>
	                                </s:if>
	                                <s:else>&nbsp;</s:else>
									</td>
									<td id="dataTd8"><span><input name="commentsArr" style="width:95%;" id="commentsArr" size="25" maxlength="200" value="<s:property value='Comments'/>"></input></span></td>
									<td id="dataTd11" style="display:none">
									<input type="hidden" name="bookQuantityArr" id="bookQuantityArr" value="<s:property value='Quantity'/>"></input>
									<input type="hidden" name="gainQuantityArr" id="gainQuantityArr" value="<s:property value='GainQuantity'/>"></input>
									<input type="hidden" name="productVendorIDArr" id="productVendorIDArr" value="<s:property value='prtVendorId'/>"></input>
									<input type="hidden" name="hasproductflagArr" id="hasproductflagArr" value="0"></input>
									<input type="hidden" name="priceUnitArr" id="priceUnitArr" value="<s:property value='Price'/>"></input>
									</td>
							</tr>
						</s:iterator>
                    </tbody>
                   </table>
                
                </div>
                <div style="display:none">
                	<input type="hidden" id="rowNumber" value="<s:property value='takingDetailList.size()'/>"/>
                    <input type="hidden" value="<s:property value="takingInfo.UpdateTime"/>" name="updateTime" id="updateTime">
                    <input type="hidden" value="<s:property value="takingInfo.ModifyCount"/>" name="modifyCount" id="modifyCount">
                    <input type="hidden" value="<s:property value="takingInfo.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
                    <input type="hidden" value="<s:property value="takingInfo.BIN_OrganizationID"/>" id="organizationID" name="organizationID">
                    <s:hidden id="BIN_ProductStockTakingID" name="BIN_ProductStockTakingID" value="%{#request.stockTakingId}"></s:hidden>
                    <s:hidden name="stockTakingId" value="%{#request.stockTakingId}"></s:hidden>
                </div>
            <hr class="space" />
            <%--
            <s:if test='!"2".equals(operateType) && !@com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
            <s:if test='"0".equals(profitKbn)'>
                <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
                    <tr>
                        <th rowspan="2" scope="row" class="center"><s:text name="BIL10_total"/></th><%-- 合计 
                       	<td class="center"><s:text name="BIL10_Sumquantity"/></td> 账面数量
                        <td class="center"><s:text name="BIL10_SumrealQuantity"/></td>实盘数量
                        <td class="center"><s:text name="BIL10_overQuantity"/></td><%-- 盘盈数 
                        <td class="center"><s:text name="BIL10_overAmount"/></td> 盘盈金额 
                    </tr>
                    <tr>
                   	     <td class="center">
                            <s:text name="format.number"><s:param value="sumInfo.Sumquantity"></s:param></s:text>
                        </td>
                        <td class="center">
                            <s:text name="format.number"><s:param value="sumInfo.SumrealQuantity"></s:param></s:text>
                        </td>
                        <td class="center">
                            <s:text name="format.number"><s:param value="sumInfo.OverQuantity"></s:param></s:text>
                        </td>
                        <td class="center">
                            <s:text name="format.price"><s:param value="sumInfo.OverAmount"></s:param></s:text>
                        </td>
                    </tr>
                </table>
            </s:if>
            --%>
            <%--
            <s:elseif test='"1".equals(profitKbn)'>
                <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
                    <tr>
                        <th rowspan="2" scope="row" class="center"><s:text name="BIL10_total"/></th><%-- 合计 
                        <td class="center"><s:text name="BIL10_Sumquantity"/></td><%-- 账面数量
                        <td class="center"><s:text name="BIL10_SumrealQuantity"/></td><%-- 实盘数量
                        <td class="center"><s:text name="BIL10_shortQuantity"/></td><%-- 盘亏数
                        <td class="center"><s:text name="BIL10_shortAmount"/></td><%-- 盘亏金额 
                    </tr>
                    <tr>
                    	<td class="center">
                            <s:text name="format.number"><s:param value="sumInfo.Sumquantity"></s:param></s:text>
                        </td>
                        <td class="center">
                            <s:text name="format.number"><s:param value="sumInfo.SumrealQuantity"></s:param></s:text>
                        </td>
                        <td class="highlight center">
                            <s:text name="format.number"><s:param value="sumInfo.ShortQuantity"></s:param></s:text>
                        </td>
                        <td class="highlight center">
                            <s:text name="format.price"><s:param value="sumInfo.ShortAmount"></s:param></s:text>
                        </td>
                    </tr>
                </table>
            </s:elseif>
            <s:else>
            --%>
                <table cellpadding="0" cellspacing="0" width="40%" border="0" class="right editable">
                    <tr>
                        <th rowspan="2" scope="row" class="center"><s:text name="BIL10_total"/></th><%-- 合计 --%>
                        <td class="center"><s:text name="BIL10_Sumquantity"/></td><%-- 账面数量 --%>
                        <td class="center"><s:text name="BIL10_SumrealQuantity"/></td><%-- 实盘数量--%>
                        <td class="center"><s:text name="BIL10_overQuantity"/></td><%-- 盘盈数 --%>
                        <td class="center"><s:text name="BIL10_overAmount"/></td><%-- 盘盈金额 --%>
                        <td class="center"><s:text name="BIL10_shortQuantity"/></td><%-- 盘亏数 --%>
                        <td class="center"><s:text name="BIL10_shortAmount"/></td><%-- 盘亏金额 --%>
                    </tr>
                    <tr>
                    <td class="center" id="totalStackQuantity">
                            <s:text name="format.number"><s:param value="sumInfo.Sumquantity"></s:param></s:text>
                        </td>
                        <td class="center" id="totalSuggestedQuantity">
                            <s:text name="format.number"><s:param value="sumInfo.SumrealQuantity"></s:param></s:text>
                        </td>
                        <td class="center" id="y_totalQuantity">
                            <s:text name="format.number"><s:param value="sumInfo.OverQuantity"></s:param></s:text>
                        </td>
                        <td class="center" id="y_totalAmount">
                            <s:text name="format.price"><s:param value="sumInfo.OverAmount"></s:param></s:text>
                        </td>
                        <td class="highlight center" id="k_totalQuantity">
                            <s:text name="format.number"><s:param value="sumInfo.ShortQuantity"></s:param></s:text>
                        </td>
                        <td class="highlight center" id="k_totalAmount">
                            <s:text name="format.price"><s:param value="sumInfo.ShortAmount"></s:param></s:text>
                        </td>
                    </tr>
                </table>
             <%-- 
            </s:else>
            </s:if>
             --%>
            <hr class="space" />
            <div class="center">
            <s:if test='@com.cherry.cm.core.CherryConstants@OPERATE_CA_AUDIT.equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_CA_AUDIT2.equals(operateType)  || @com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
                 <%-- 
                <s:if test='@com.cherry.cm.core.CherryConstants@OPERATE_CA_EDIT.equals(operateType)'>
                    <button class="save" onclick="BINOLSTBIL10.saveForm();return false;"><span class="ui-icon icon-save"></span>
                    --%>
                    <%-- 保存 --%>
                    <%-- 
                    <span class="button-text"><s:text name="global.page.save"/></span>
                </button>
                </s:if>
               --%>
            </s:if>
            
            <s:if test='"2".equals(operateType)'>
                <button id="btnSave" class="save" style="display:none;" onclick="BINOLSTBIL10.saveOrder();return false;"><span class="ui-icon icon-save"></span>
                    <%-- 暂存 --%>
                    <span class="button-text"><s:text name="global.page.saveTemp"/></span>
                </button>
                <button id="btn-icon-edit-big" class="confirm" onclick="BINOLSTBIL10.modifyOrder();return false;"><span class="ui-icon icon-edit-big"></span>
                    <%-- 修改 --%>
                    <span class="button-text"><s:text name="os.edit"/></span>
                </button>
                <button class="confirm" onclick="BINOLSTBIL10.submitOrder();return false;"><span class="ui-icon icon-confirm"></span>
                    <%-- 提交 --%>
                    <span class="button-text"><s:text name="global.page.submit"/></span>
                </button>
            </s:if>
				<button id="btnReturn" class="close" onclick="BINOLSTBIL10.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
					<%--返回 --%>
					<span class="button-text"><s:text name="global.page.back"/></span>
				</button>
            <button class="close" onclick="window.close();return false;">
                <span class="ui-icon icon-close"></span>
                <%-- 关闭 --%>
                <span class="button-text"><s:text name="BIL10_close"/></span>
            </button>
            </div>
        </div>
        </div>
        </form>
    </div>
    </div>
   <!-- 此处再加入tabs-2  div-->
   <div id="tabs-2">
      <strong><s:text name="global.page.worksProcessing"/></strong>
   </div>
</div>
    </div>
</s:i18n>

<form action="BINOLSTBIL10_init" id="productOrderDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="stockTakingId" value="%{#request.stockTakingId}"></s:hidden>
</form>
<%-- ================== 报表打印共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
<%-- ==================  报表打印共通导入 End ======================== --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 工作流提示弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOSDialog.jsp" flush="true" />
<%-- ==================  工作流提示弹出窗口End ======================== --%>