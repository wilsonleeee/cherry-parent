<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM32.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.ss.BINOLSSPRM32">
<div id="div_main" class="main container clearfix">
<div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="PRM32_title"/>&nbsp;</span>
        </div>
    </div>
      <div class="panel-content">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          	<div id="export_param_hide" class="hide">
          		<input type="hidden" name="prmVendorId" value="<s:property value="prmVendorId"/>"/>
          		<input type="hidden" name="startDate" value="<s:property value="startDate"/>"/>
          		<input type="hidden" name="endDate" value="<s:property value="endDate"/>"/>
          		<input type="hidden" name="cutOfDate" value="<s:property value="cutOfDate"/>"/>
          		<input type="hidden" name="date1" value="<s:property value="date1"/>"/>
          		<input type="hidden" name="date2" value="<s:property value="date2"/>"/>
          		<input type="hidden" name="flag" value="<s:property value="flag"/>"/>
          		<input type="hidden" name="flagB" value="<s:property value="flagB"/>"/>
          		<div><input type="hidden" name="params" value="<s:property value="params"/>"/></div>
          	</div>
          	<cherry:show domId="BINOLSSPRM32EXP">
             <button class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSSPRM32',1);setRPM32Params('#export_param_hide');return false;">
             	<span class="ui-icon icon-file-export"></span>
             	<span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
             </button>
            </cherry:show>
          </div>
          <div class="section-content">
          <div>
            <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
            <div class="box-header"></div>
            <table class="detail" cellpadding="0" cellspacing="0">
                <tr>
                    <%-- 产品名称 --%>
                    <th><s:text name="PRM32_name"/></th>
                    <td><s:property value="proInfo.nameTotal"/></td>
                    <%-- 日期范围 --%>
                    <th><s:text name="PRM32_date"/></th>
                    <td><s:property value="startDate"/>&nbsp;~&nbsp;<s:property value="endDate"/></td>
                </tr>
                <tr>
                    <%-- 厂商编码 --%>
                    <th><s:text name="PRM32_unitCode"/></th>
                    <td><s:property value="proInfo.unitCode"/></td>
                    <%-- 产品条码 --%>
                    <th><s:text name="PRM32_barCode"/></th>
                    <td><s:property value="proInfo.barCode"/></td>
                </tr>
                <tr>
                    <%-- 期初结存 --%>
                    <th><s:text name="PRM32_startQuantity"/></th>
                    <td>
                        <s:if test="startQuantity >= 0">
                            <s:text name="format.number"><s:param value="startQuantity"></s:param></s:text>
                        </s:if>
                        <s:else><span class="highlight"><s:text name="format.number"><s:param value="startQuantity"></s:param></s:text></span></s:else>
                    </td>
                    <%-- 期末结存 --%>
                    <th><s:text name="PRM32_endQuantity"/></th>
                    <td>
                        <s:if test="endQuantity >= 0">
                            <s:text name="format.number"><s:param value="endQuantity"></s:param></s:text>
                        </s:if>
                        <s:else><span class="highlight"><s:text name="format.number"><s:param value="endQuantity"></s:param></s:text></span></s:else>
                    </td>
                </tr>
            </table>
            <div class="clearfix"></div>  
            </cherry:form>
          </div>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 库存明细一览 --%>
            <s:text name="PRM32_results_list"/>
            </strong>
          </div>
          <div class="section-content">
<%--
          	 <div class="toolbar clearfix">
          	 	<span class="left">
	          	 	<%-- 导出 --%
	              	<a class="export">
	              		<span class="ui-icon icon-export"></span>
	              		<span class="button-text"><s:text name="global.page.export"/></span>
	              	</a>
              	</span> 
             </div>
--%>
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dataTable">
              <thead>
                <tr>
                  <th rowspan="2" class="center"><s:text name="PRM32_num"/></th><%-- 编号 --%>
                  <th rowspan="2" class="center"><s:text name="PRM32_departName"/></th><%-- 部门 --%>	
                  <th rowspan="2" class="center"><s:text name="PRM32_inventory"/></th><%-- 仓库 --%>	
                  <th rowspan="2" class="center"><s:text name="PRM32_lgcInventory"/></th><%-- 逻辑仓库 --%>		
                  <th rowspan="2" class="center"><s:text name="PRM32_startQuantity"/></th><%-- 期初结存 --%>	
                  <th colspan="7" class="center green"><s:text name="PRM32_in"/></th><%-- 本期收入明细 --%>
                  <th colspan="8" class="center red"><s:text name="PRM32_out"/></th><%-- 本期发出明细 --%>
                  <th rowspan="2" class="center"><s:text name="PRM32_endQuantity"/></th><%-- 期末结存 --%>	
                </tr>
                <tr>
                  <th class="center green"><s:text name="PRM32_in1"/></th><%-- 仓库收货 --%>
                  <th class="center green"><s:text name="PRM32_in2"/></th><%-- 接收退库--%>
                  <th class="center green"><s:text name="PRM32_in3"/></th><%-- 调入申请 --%>
                  <th class="center green"><s:text name="PRM32_in4"/></th><%-- 自由入库  --%>
                  <th class="center green"><s:text name="PRM32_in5"/></th><%-- 盘盈 --%>
                  <th class="center green"><s:text name="PRM32_in6"/></th><%-- 销售入库 --%>
                  <th class="center green"><s:text name="PRM32_in7"/></th><%-- 礼品入库 --%>
                  <th class="center red"><s:text name="PRM32_out1"/></th><%-- 仓库发货 --%>
                  <th class="center red"><s:text name="PRM32_out2"/></th><%-- 仓库退货 --%>
                  <th class="center red"><s:text name="PRM32_out3"/></th><%-- 调出确认 --%>
                  <th class="center red"><s:text name="PRM32_out4"/></th><%-- 自由出库 --%>
                  <th class="center red"><s:text name="PRM32_out5"/></th><%-- 盘亏--%>
                  <th class="center red"><s:text name="PRM32_out6"/></th><%-- 销售出库 --%>
                  <th class="center red"><s:text name="PRM32_out7"/></th><%-- 礼品领用 --%>
                  <th class="center red"><s:text name="PRM32_out8"/></th><%-- 积分兑换 --%>
                </tr>
              </thead>
              <tbody>
                <s:iterator value="stockList" status="status">
                	<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  <td><s:property value="#status.index+1"/></td>
                	  <td><span><s:property value="departName"/></span></td>
	                  <td><span><s:property value="inventoryName"/></span></td>
	                  <td><span><s:property value="lgcInventoryName"/></span></td>
	                  <td class="center">
		                  <s:if test="startQuantity >= 0">
		                  	<s:text name="format.number"><s:param value="startQuantity"></s:param></s:text>
		                  </s:if>
		                  <s:else><span class="highlight"><s:text name="format.number"><s:param value="startQuantity"></s:param></s:text></span></s:else>
	                  </td>
	                  
	                  
	                  
	                    <!-- 库存明细收货 -->
	                  <td>
	                  	<s:if test="inQuantity1 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">2</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:property value='inQuantity1'/></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="inQuantity1"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细接收退库 -->
	           	      <td>
	                  	<s:if test="inQuantity2 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">4</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="inQuantity2"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="inQuantity2"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细调入-->
	                   <td>
	                  	<s:if test="inQuantity3 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">5</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="inQuantity3"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="inQuantity3"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细自由入库 -->
	           		  <td>
	                  	<s:if test="inQuantity4 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">7</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="inQuantity4"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="inQuantity4"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		 <!-- 库存明细盘盈 -->
	           		 <td>
	                  	<s:if test="inQuantity5 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">P</s:param>
							<s:param name="StockType">0</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="inQuantity5"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="inQuantity5"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细销售入库 -->
	           		  <td>
	                  	<s:if test="inQuantity6 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">R</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="inQuantity6"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="inQuantity6"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细礼品入库 -->
	           		  <td>
	                  	<s:if test="inQuantity7 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">S</s:param>
							<s:param name="StockType">0</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="inQuantity7"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="inQuantity7"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细发货 -->
	                  <td>
	                  	<s:if test="outQuantity1 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">1</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="outQuantity1"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="outQuantity1"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细退货 -->
	           		  <td>
	                  	<s:if test="outQuantity2 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">3</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="outQuantity2"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="outQuantity2"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细调出 -->
	           		  <td>
	                  	<s:if test="outQuantity3 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">6</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="outQuantity3"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="outQuantity3"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细自由出库 -->
	           		  <td>
	                  	<s:if test="outQuantity4 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">8</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="outQuantity4"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="outQuantity4"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细盘亏 -->
	           		  <td>
	                  	<s:if test="outQuantity5 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">P</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
							<s:param name="StockType">1</s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="outQuantity5"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="outQuantity5"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细销售出库 -->
	           		  <td>
	                  	<s:if test="outQuantity6 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">N</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="outQuantity6"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="outQuantity6"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	           		  <!-- 库存明细礼品领用 -->
	           		  <td>
	                  	<s:if test="outQuantity7 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">S</s:param>
							<s:param name="StockType">1</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="outQuantity7"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="outQuantity7"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	                  <!-- 库存明细礼品领用 -->
	           		  <td>
	                  	<s:if test="outQuantity8 != 0">
	                  	<s:url id="inventoryDetail" action="BINOLSSPRM32_getdetailed">
							<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
							<s:param name="inventoryInfoID"><s:property value="BIN_InventoryInfoID"/></s:param>
							<s:param name="logicInventoryInfoID"><s:property value="BIN_LogicInventoryInfoID"/></s:param>
							<s:param name="tradeType">PX</s:param>
							<s:param name="StockType">1</s:param>
							<s:param name="startDate"><s:property value="startDate"/></s:param>
							<s:param name="endDate"><s:property value="endDate"/></s:param>
						</s:url>
							<a class="inventory" href="${inventoryDetail}" rel="${inventoryDetail}" title='<s:text name="RPS32_detailed"/>'>
							<span><s:text name="format.number"><s:param value="outQuantity8"></s:param></s:text></span>
							</a>
	                  	</s:if>
	                 	<s:else>
							<span class="center">
								<span><s:text name="format.number"><s:param value="outQuantity8"></s:param></s:text></span>
							</span>
						</s:else>
	           		  </td>
	                  
	                  
	                  <td class="center">
		                  <s:if test="endQuantity >= 0">
		                  	<s:text name="format.number"><s:param value="endQuantity"></s:param></s:text>
		                  </s:if>
		                  <s:else><span class="highlight"><s:text name="format.number"><s:param value="endQuantity"></s:param></s:text></span></s:else>
	                  </td>
	                </tr>
                </s:iterator>
              </tbody>
            </table>
           	</div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="PRM32_inQuantity"/></td><%-- 本期收入 --%>
                <td class="center"><s:text name="PRM32_outQuantity"/></td><%-- 本期发出--%>
                <td class="center"><s:text name="PRM32_variation"/></td><%-- 本期变化--%>
              </tr>
              <tr>
                <td class="center"><s:text name="format.number"><s:param value="currentIn"></s:param></s:text></td>
             	<td class="center"><s:text name="format.number"><s:param value="currentOut"></s:param></s:text></td>
             	<s:if test="currentIn >= currentOut">
             	<td class="center"><s:text name="format.number"><s:param value="currentIn - currentOut"></s:param></s:text></td>	
             	</s:if>
             	<s:else>
             	<td class="highlight center"><s:text name="format.number"><s:param value="currentIn - currentOut"></s:param></s:text></td>
             	</s:else>
              </tr>
            </table>
            <hr class="space" />
            <div class="center">
              <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
              <%-- 关闭 --%>
              <span class="button-text"><s:text name="global.page.close"/></span>
             </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    </div>
    <%-- ================== 报表打印共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
	<%-- ==================  报表打印共通导入 End ======================== --%>
</s:i18n>
