<%--入库单明细 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL02.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.st.BINOLSTBIL02">
<div class="main container clearfix">
<s:url id="doaction_url" value="/st/BINOLSTBIL02_doaction"/>
<s:url id="getProductStock_url" value="/st/BINOLSTBIL02_getProductStock"/>
<s:url id="saveIndepot_url" value="/st/BINOLSTBIL02_saveIndepot"/>
<s:url id="submitIndepot_url" value="/st/BINOLSTBIL02_submitIndepot"/>
<s:url id="url_getStockCount" value="/st/BINOLSTSFH03_getStockCount" />
<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>

<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="getProductStock" href="${getProductStock_url}"></a>
    <a id="saveIndepot" href="${saveIndepot_url}"></a>
    <a id="submitIndepot" href="${submitIndepot_url}"></a>
</div>
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL02_title"/>&nbsp;(<s:text name="BIL02_num"/>:<s:property value="inDepotMainMap.BillNo"/>)</span>
        </div>
    </div>
      <div class="tabs">
    	 <ul>	
            <li><a href="#tabs-1"><s:text name="global.page.title"/></a></li><%-- 基本信息 --%>
            <li><a href="#tabs-2" onclick="clickTab_2WorkFlow();return false;"><s:text name="global.page.work"/></a>
            </li><%-- 单据流程 --%>
    </ul>
	<div id="tabs-1" class="panel-content">
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
          		<input type="hidden" name="billType" value="GR"/>
          		<input type="hidden" name="pageId" value="BINOLSTBIL02"/>
          		<input type="hidden" name="billId" value="${productInDepotId}"/>
          	</div>
          	<cherry:show domId="BINOLSTBIL02PNT">
          	<button onclick="openPrintApp('Print');return false;" class="confirm right">
				<span class="ui-icon icon-file-print"></span>
				<span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
			</button>
			</cherry:show>
			<cherry:show domId="BINOLSTBIL02VEW">
			<button onclick="openPrintApp('View');return false;" class="confirm right">
				<span class="ui-icon icon-file-view"></span>
				<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
			</button>
			</cherry:show>
            <cherry:show domId="BINOLSTBIL02EXP">
            <div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${productInDepotId}"/></div>
             <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTBIL02',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
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
                            <%-- 入库单号 --%>
                            <th><s:text name="BIL02_billNo"/></th>
                            <td><s:property value="inDepotMainMap.BillNoIF"/></td>
                            <%-- 入库日期 --%>
                            <th><s:text name="BIL02_inDepotDate"/></th>
                            <td><s:property value="inDepotMainMap.InDepotDate"/></td>
                        </tr>
                        <tr>
                            <%-- 往来单位 --%>
                            <th><s:text name="BIL02_bussinessPartner"/></th>
                            <td>
                                <s:property value="inDepotMainMap.PartnerCodeName"/>
                            </td>
                      
                            <%-- 入库部门 --%>
                            <th><s:text name="BIL02_departName"/></th>
                            <td><s:property value="inDepotMainMap.DepartCodeName"/></td>
                        </tr>
                        <tr>
                            <%-- 入库实体仓库 --%>
                            <th><s:text name="BIL02_depotName"/></th>
                            <td>
                                <s:property value="inDepotMainMap.DepotCodeName"/>
                            </td>
                      
                            <%-- 入库逻辑仓库 --%>
                            <th><s:text name="BIL02_logicDepotName"/></th>
                            <td><s:property value="inDepotMainMap.LogicInventoryName"/></td>
                        </tr>
                         <tr>
                            <%-- 审核状态 --%>
                            <th><s:text name="BIL02_verifiedFlag"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1305", inDepotMainMap.VerifiedFlag)'/></td>
                            <%-- 审核者 --%>
                            <th><s:text name="BIL02_auditName"/></th>
                            <td><s:property value="inDepotMainMap.EmployeeAuditName"/></td>
                        </tr>
                        <tr>
                            <%--关联单号 --%>
                            <th><s:text name="BIL02_relevanceNo"/></th>
                            <td><s:property value='inDepotMainMap.RelevanceNo'/></td>
                            <%--导入批次 --%>
                            <th><s:text name="BIL02_importBatch"/></th>
                            <td><s:property value='inDepotMainMap.ImportBatch'/></td>
                        </tr>
                        <tr>
                            <%-- 入库状态 --%>
                            <th><s:text name="BIL02_tradeStatus"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1266", inDepotMainMap.TradeStatus)'/></td>
                        	<%-- 入库理由 --%>
                            <th><s:text name="BIL02_reason"/></th>
                            <td><s:property value='inDepotMainMap.Comments'/></td>
                        </tr>
                    </tbody>
                </table>
                <div class="clearfix"></div>
            </div>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
            <%-- 入库单明细一览 --%>
            <s:text name="BIL02_results_list"/>
            </strong>
          </div>
          <div class="section-content">
            <s:if test='"2".equals(operateType) || "11".equals(operateType) || "12".equals(operateType) || "15".equals(operateType) || "16".equals(operateType)'>
                <div id="showToolbar" class="">
                <div class="toolbar clearfix">
                    <span class="left">
                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                        <span id="showAddBtn" class="hide">
                        <a class="add" onclick="binOLSTBIL02.openProPopup(this);">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="BIL02_addNewLine"/></span>
                        </a>
                        </span>
                        <span id="showDelBtn" class="hide">
                        <a class="delete" onclick="binOLSTBIL02.deleteLine();">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="BIL02_deleteLine"/></span>
                        </a>
                        </span>
                    </span>
                </div>
                </div>
            </s:if>
            <form id="mainForm" method="post" class="inline">
                <%--防止有button的form在text框输入后按Enter键后自动submit --%>
                <button type="submit" onclick="return false;" class="hide"></button>
            <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
            <input type="hidden" id="entryID" name="entryID" value='<s:property value="inDepotMainMap.WorkFlowID"/>'/>
            <input type="hidden" id="actionID" name="actionID"/>
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                    <s:if test='"2".equals(operateType) || "11".equals(operateType) || "12".equals(operateType)  || "15".equals(operateType) || "16".equals(operateType)'>
                        <th id="showCheckbox" class="center hide" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="binOLSTBIL02.selectAll(this);"/><s:text name="BIL02_select"/></th><%-- 编号 --%>
                    </s:if>
                    <th class="center"><s:text name="BIL02_no"/></th><%-- 编号 --%>
                    <th class="center"><s:text name="BIL02_unitCode"/></th><%-- 厂商编码 --%>
                    <th class="center"><s:text name="BIL02_barCode"/></th><%-- 产品条码 --%> 
                    <th class="center"><s:text name="BIL02_productName"/></th><%-- 产品名称 --%>
                    <th class="center"><s:text name="BIL02_batchNo"/></th><%-- 批次号 --%>
                    <th class="center"><s:text name="BIL02_referencePrice"/></th><%-- 参考价 --%>
                    <th class="center">
                        <s:text name="BIL02_price"/>
                        <span id="spanCalPrice" class="hide"><span class="calculator" onclick="binOLSTBIL02.initRateDiv(this,'batch');" title="<s:text name="BIL02_BatchCalTitle"/>"></span></span>
                        <input type="hidden" id="batchPriceRate" value="100.00">
                    </th><%-- 执行价 --%>
                    <s:if test='"11".equals(operateType) || "12".equals(operateType) || "16".equals(operateType)'>
                        <th class="center"><s:text name="BIL02_productStock"/></th><%-- 库存 --%>
                    </s:if>
                    <th class="center"><s:text name="BIL02_preQuantity"/></th><%-- 申请数量 --%>
                    <th class="center"><s:text name="BIL02_preAmount"/></th><%-- 申请金额 --%>
                    <s:if test='"999".equals(operateType)'>
                        <th class="center"><s:text name="BIL02_quanlity"/></th><%-- 数量 --%>
                        <th class="center"><s:text name="BIL02_amount"/></th><%-- 金额 --%>
                    </s:if>
                    <th class="center"><s:text name="BIL02_comments"/></th><%-- 备注 --%>
                </tr>
              </thead>
              
              <tbody id="databody">
                <s:iterator value="inDepotDetailList" status="status">
                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                        <s:if test='"2".equals(operateType) || "11".equals(operateType) || "12".equals(operateType) || "15".equals(operateType) || "16".equals(operateType)'>
                            <td id="dataTd0" class="hide"><input name="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="binOLSTBIL02.changechkbox(this);"/></td>
                        </s:if>
                        <td id="dataTd1"><s:property value="#status.index+1"/></td>
                        <td id="dataTd2"><span><s:property value="UnitCode"/></span></td>
                        <td id="dataTd3"><span><s:property value="BarCode"/></span></td>
                        <td id="dataTd4"><span><s:property value="ProductName"/></span></td>
                        <td id="dataTd5">
	                        <s:if test='"2".equals(operateType) || "11".equals(operateType) || "12".equals(operateType) || "15".equals(operateType) || "16".equals(operateType)'>
                                <s:textfield name="batchNoArr" id="batchNoArr" cssClass="text-number hide" maxlength="20" value="%{BatchNo}" cssStyle=""/>    
                                <div id="hideBatchNoArr">
                                    <s:property value="BatchNo"></s:property>
                                </div>
                            </s:if>
                            <s:else>
                            <s:if test='null!=Quantity'>
                                <s:property value="BatchNo"></s:property>
                            </s:if>
                            </s:else>
                            <s:else>&nbsp;</s:else>
                        </td>                  
                        <td id="tdReferencePrice" class="alignRight">
                            <s:if test='null!=ReferencePrice'>
                                <s:text name="format.price"><s:param value="ReferencePrice"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="dataTd6" class="alignRight">
                            <input type="text" class="price hide" id="priceUnitArr" name="priceUnitArr" onchange="binOLSTBIL02.setPrice(this);"  value="<s:property value='Price'/>"/>
                            <div id="hidePriceArr">
                                <s:if test='null!=Price'>
                                    <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </div>
                        </td>
                        <s:if test='"11".equals(operateType)|| "12".equals(operateType) || "16".equals(operateType)'>
                            <td id="dataTd7" class="alignRight">
                                <s:if test='null!=ProductQuantity'>
                                    <s:text name="format.number"><s:param value="ProductQuantity"></s:param></s:text>
                                </s:if>
                                <s:else>&nbsp;</s:else>
                            </td>
                        </s:if>
                        <td id="newCount" class="alignRight" style="width:10%;">
                            <s:if test='"2".equals(operateType) || "11".equals(operateType) || "12".equals(operateType) || "15".equals(operateType) || "16".equals(operateType)'>
                                <s:textfield name="quantityArr" cssClass="text-number hide" size="10" maxlength="9" value="%{PreQuantity}" onchange="binOLSTBIL02.changeCount(this);"></s:textfield>
                                <div id="hideQuantiyArr">
                                    <s:text name="format.number"><s:param value="PreQuantity"></s:param></s:text>
                                </div>
                            </s:if>
                            <s:else>
                            <s:if test='null!=PreQuantity'>
                                <s:text name="format.number"><s:param value="PreQuantity"></s:param></s:text>
                            </s:if>
                            </s:else>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="preMoney" class="alignRight">
                            <s:if test='null!=Price && null!=PreQuantity'>
                                <s:text name="format.price"><s:param value="Price*PreQuantity"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <s:if test='"999".equals(operateType)'>
	                        <td id="dataTdQuantity" class="alignRight" style="width:10%;">
	                            <s:if test='null!=Quantity'>
	                                <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
	                            </s:if>
	                            <s:else>&nbsp;</s:else>
	                        </td>
	                        <td id="money" class="alignRight">
	                            <s:if test='null!=Price && null!=Quantity'>
	                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
	                            </s:if>
	                            <s:else>&nbsp;</s:else>
	                        </td>
                        </s:if>
                        <td id="dataTd9">
                            <s:if test='"2".equals(operateType) || "12".equals(operateType) || "11".equals(operateType) || "15".equals(operateType) || "16".equals(operateType)'>
                                <s:textfield name="commentsArr" size="25" maxlength="200" value="%{Comments}" cssStyle="" cssClass="hide"></s:textfield>
                                <div id="hideComments">
                                    <p><s:property value="Comments"/></p>
                                </div>
                            </s:if>
                            <s:else>
                                <p><s:property value="Comments"/></p>
                            </s:else>
                        </td>
                        <td style="display:none" id="dataTd10">
                            <input type="hidden" id="referencePriceArr<s:property value='#status.index+1'/>" name="referencePriceArr" value="<s:property value='ReferencePrice'/>"/>
                            <input type="hidden" id="productVendorIDArr<s:property value='#status.index+1'/>" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
                            <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
                        </td>
                    </tr>
                </s:iterator>
              </tbody>
            </table>
            </div>
            <div style="display:none">
                <input type="hidden" id="rowNumber" value="<s:property value='inDepotDetailList.size()'/>"/>
                <input type="hidden" name="depotInfoId" id=inventoryInfoID value='<s:property value="inDepotMainMap.BIN_InventoryInfoID"/>'/>
                <input type="hidden" name="logicDepotsInfoId" id="logicInventoryInfoID" value='<s:property value="inDepotMainMap.BIN_LogicInventoryInfoID"/>'/>
                <input type="hidden" value="${inDepotMainMap.UpdateTime}" name="updateTime" id="updateTime">
                <input type="hidden" value="<s:property value="inDepotMainMap.ModifyCount"/>" name="modifyCount" id="modifyCount">
                <input type="hidden" value="<s:property value="inDepotMainMap.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
                <s:hidden id="productInDepotId" name="productInDepotId" value="%{#request.productInDepotId}"></s:hidden>
            </div>
            </form>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="BIL02_preTotalQuantity"/></td><%-- 申请总数量 --%>
                <td class="center"><s:text name="BIL02_preTotalAmount"/></td><%-- 申请总金额--%>
                <s:if test='"999".equals(operateType)'>
                    <td class="center"><s:text name="BIL02_totalQuantity"/></td><%-- 总数量 --%>
                    <td class="center"><s:text name="BIL02_totalAmount"/></td><%-- 总金额--%>
                </s:if>
              </tr>
              <tr>
                <td class="center">
                    <%-- 申请总数量 --%>
                    <s:if test='null!=inDepotMainMap.PreTotalQuantity'>
                        <span id="preTotalQuantity"><s:text name="format.number"><s:param value="inDepotMainMap.PreTotalQuantity"></s:param></s:text></span>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </td>
                <td class="center">
                    <%-- 申请总金额--%>
                    <s:if test='null!=inDepotMainMap.PreTotalAmount'>
                        <span id="preTotalAmount"><s:text name="format.price"><s:param value="inDepotMainMap.PreTotalAmount"></s:param></s:text></span>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </td>
                <s:if test='"999".equals(operateType)'>
	                <td class="center">
	                    <%-- 总数量 --%>
	                    <s:if test='null!=inDepotMainMap.TotalQuantity'>
	                        <span><s:text name="format.number"><s:param value="inDepotMainMap.TotalQuantity"></s:param></s:text></span>
	                    </s:if>
	                    <s:else>&nbsp;</s:else>
	                </td>
	                <td class="center">
	                    <%-- 总金额--%>
	                    <s:if test='null!=inDepotMainMap.TotalAmount'>
	                        <span><s:text name="format.price"><s:param value="inDepotMainMap.TotalAmount"></s:param></s:text></span>
	                    </s:if>
	                    <s:else>&nbsp;</s:else>
	                </td>
                </s:if>
              </tr>
            </table>
            <hr class="space" />
            <div class="center">
            <s:if test='"2".equals(operateType)'>
                <button class="save" onclick="binOLSTBIL02.saveIndepot();return false;"><span class="ui-icon icon-save"></span>
                    <%-- 保存 --%>
                    <span class="button-text"><s:text name="global.page.save"/></span>
                </button>
                <button id="btn-icon-edit-big" class="confirm" onclick="binOLSTBIL02.modifyForm();return false;"><span class="ui-icon icon-edit-big"></span>
                    <%-- 修改 --%>
                    <span class="button-text"><s:text name="os.edit"/></span>
                </button>
                <button class="confirm" onclick="binOLSTBIL02.submitIndepot();return false;"><span class="ui-icon icon-confirm"></span>
                    <%-- 提交 --%>
                    <span class="button-text"><s:text name="global.page.submit"/></span>
                </button>
            </s:if>
            <s:elseif test='"11".equals(operateType) || "12".equals(operateType) || "15".equals(operateType) || "16".equals(operateType)'>
                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
                <s:if test='"12".equals(operateType)'>
                 <button class="save" onclick="binOLSTBIL02.saveIndepot();return false;" style="display:none;"><span class="ui-icon icon-save"></span>
                    <%-- 保存 --%>
                    <span class="button-text"><s:text name="global.page.save"/></span>
                </button>
                </s:if>
                <%-- 兼容没有修改按钮的老工作流 --%>
                <s:if test='null != inDepotMainMap.get("OSEditFlag") && inDepotMainMap.get("OSEditFlag").equals("false")'>
                    <s:if test='!"15".equals(operateType)'>
	                <button id="btn-icon-edit-big" class="confirm" onclick="binOLSTBIL02.modifyForm();return false;"><span class="ui-icon icon-edit-big"></span>
	                    <%-- 修改 --%>
	                    <span class="button-text"><s:text name="os.edit"/></span>
	                </button>
	                </s:if>
                </s:if>
            </s:elseif>
                <button id="btnReturn" class="close" onclick="binOLSTBIL02.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
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
		   <b><s:text name="global.page.worksProcessing"/></b>
		</div>
  </div>
    </div>
    </div>
    <div class="rateDialog hide">
        <span id="spanCalTitle" style="display:none;"><s:text name="BIL02_CalTitle"/></span>
        <s:text name="BIL02_discountRate"/><%-- 折扣率 --%>
        <input class="number" id="priceRate" value="100" onblur="binOLSTBIL02.closeDialog(this);return false;"  
            onkeyup="binOLSTBIL02.setDiscountPrice(this);return false;"/><s:text name="global.page.percent"/>
        <input type ="hidden" id="curRateIndex" value=""/>
    </div>
</s:i18n>
<form action="BINOLSTBIL02_init" id="productInDepotDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="productInDepotId" value="%{#request.productInDepotId}"></s:hidden>
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
    <input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
    <input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
</div>
<%-- ================== 工作流提示弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOSDialog.jsp" flush="true" />
<%-- ==================  工作流提示弹出窗口End ======================== --%>