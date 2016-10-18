<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM44.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.ss.BINOLSSPRM44">
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="PRM44_title"/>&nbsp;(<s:text name="PRM44_deliverRecNo"/>:<s:property value="deliverInfo.deliverRecNo"/>)</span>
        </div>
    </div>
    <div class="tabs">
    	 <ul>	
            <li><a href="#tabs-1"><s:text name="global.page.title"/></a></li><%-- 基本信息 --%>
            <li><a href="#tabs-2" onclick="clickTab_2WorkFlow();return false;"><s:text name="global.page.work"/></a>
            </li><%-- 单据流程 --%>
    </ul>
      <div id="tabs-1"  class="panel-content">
        <div class="section">
          <div class="section-header">
          	<strong>
          		<span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="PRM44_header"/>
          	</strong>
         
          	<cherry:show domId="BINOLSSPRM44PNT">
          	<div id="print_param_hide" class="hide">
          		<input type="hidden" name="pageId" value="BINOLSSPRM44"/>
          		<input type="hidden" name="billId" value="${deliverId}"/>
          	</div>
          	<button onclick="openPrintApp('Print');return false;" class="confirm right">
				<span class="ui-icon icon-file-print"></span>
				<span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
			</button>
			</cherry:show>
			<cherry:show domId="BINOLSSPRM44VEW">
			<button onclick="openPrintApp('View');return false;" class="confirm right">
				<span class="ui-icon icon-file-view"></span>
				<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
			</button>
			</cherry:show>
          	<cherry:show domId="BINOLSSPRM44EXP">
            <div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${deliverId}"/></div>
            <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSSPRM44',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
             	<span class="ui-icon icon-file-export"></span>
             	<span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
            </button>
            </cherry:show>
          </div>
          <div class="section-content">
            <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
              <input type="hidden" id="entryID" name="entryID" value='<s:property value="deliverInfo.WorkFlowID"/>'/>
                <div class="box-header"></div>
                    <table class="detail" cellpadding="0" cellspacing="0">
                      <tr>
                        <th>
                            <%-- 收货单号 --%>
                            <s:text name="PRM44_deliverRecNoIF"/>
                        </th>
                        <td>
                            <span><s:property value="deliverInfo.deliverRecNoIF"/></span>
                        </td>
                        <th>
                            <%-- 收货日期 --%>
                            <s:text name="PRM44_deliverDate"/>
                        </th>
                        <td>
                            <span><s:property value="deliverInfo.deliverDate"/></span>
                        </td>
                      </tr>
                      <tr>
                        <th>
                            <%-- 关联单号 --%>
                            <s:text name="PRM44_relevanceNo"/>
                        </th>
                        <td>
                            <span><s:property value="deliverInfo.relevanceNo"/></span>
                        </td>
                        <th>
                            <%-- 制单员 --%>
                            <s:text name="PRM44_employeeName"/>
                        </th>
                        <td><span><s:property value="deliverInfo.employeeName"/></span></td>
                      </tr>
                        </table>
                        <table class="detail" cellpadding="0" cellspacing="0">
                            <tr>
                                <th>
                                    <%-- 发货部门 --%>
                                    <s:text name="PRM44_departName"/>
                                </th>
                                <td>
                                    <span><s:property value="deliverInfo.DepartName"/></span>
                                </td>
                                <th>
                                    <%-- 入库状态 --%>
                                    <s:text name="PRM44_stockInFlag"/>
                                </th>
                                <td>
                                    <%-- 入库状态 --%>
                                    <span><s:property value='#application.CodeTable.getVal("1017", deliverInfo.stockInFlag)'/></span>
                                </td>
                            </tr>
                            <tr>
                             		<th>
                                    <%-- 收货部门--%>
                                    <s:text name="PRM44_departNameReceive"/>
                                    </th>
                                    <td>
                                        <span><s:property value="deliverInfo.DepartNameReceive"/></span>
                                    </td>
                                <th>
                                    <%-- 收货仓库 --%>
                                    <s:text name="PRM44_inventNameReceive"/>
                                </th>
                                <td>
                                    <span><s:property value="deliverInfo.InventoryName"/></span>
                                </td>
                            </tr>
                            <tr>
                             	<th>
		                      		<%-- 收货逻辑仓库 --%>
		                       		<s:text name="PRM44_logicInventName"/>
		                      	</th>
		                       	<td>
                       				<span><s:property value="deliverInfo.LgcInventoryName"/></span>
                     			</td>
                     			<th></th>
                     			<td></td>
                            </tr>
                            <tr>
                                <%-- 收货地址 --%>
                                <th><s:text name="PRM44_ReceiveDepartAddress"/></th>
                                <td colspan=3><s:property value="deliverInfo.ReceiveDepartAddress"/></td>
                            </tr>
                      </table>
                      <div class="clearfix"></div>
                </cherry:form>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 收货明细一览 --%>
            <s:text name="PRM44_results_list"/>
            </strong>
          </div>
          <div class="section-content">
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th width="1%"><s:text name="PRM44_num"/></th><%-- No. --%>
                  <th width="18%"><s:text name="PRM44_nameTotal"/></th><%-- 促销品名称 --%>
                  <th width="15%"><s:text name="PRM44_unitCode"/></th><%-- 厂商编码 --%>
                  <th width="15%"><s:text name="PRM44_barCode"/></th><%-- 促销品条码 --%>
                  <th width="3%"><s:text name="PRM44_packageName"/></th><%-- 单位 --%>
                  <th width="8%"><s:text name="PRM44_quantity"/></th><%-- 收货数量 --%>
                  <%--<th width="10%"><s:text name="PRM44_price"/></th>--%><%-- 单价 --%>
                  <th width="10%"><s:text name="PRM44_detailAmount"/></th><%-- 总金额 --%>
                  <th width="20%"><s:text name="PRM44_reason"/></th><%-- 理由 --%>
                </tr>
              </thead>
              <tbody>
              <s:iterator value="deliverDetailList" id="deliverDetail" status="R">
                <tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>">
                  <td><s:property value="#R.index+1"/></td>
                  <td>
                  <s:if test='nameTotal != null && !"".equals(nameTotal)'>
                  	<s:property value="nameTotal"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td>
                  <s:if test='unitCode != null && !"".equals(unitCode)'>
                  	<s:property value="unitCode"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td>
                  <s:if test='barCode != null && !"".equals(barCode)'>
                  	<s:property value="barCode"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td>
                  <s:if test='packageName != null && !"".equals(packageName)'>
                  	<s:property value="packageName"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="alignRight">
                  <s:if test='quantity != null'>
                  	<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="alignRight">
                  <s:if test='detailAmount != null'>
                  	<s:text name="format.price"><s:param value="detailAmount"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td>
                  <span><s:property value='reason'/></span>
                  </td>
                </tr>
               </s:iterator>
              </tbody>
            </table>
            </div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="PRM44_total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="PRM44_summQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="PRM44_summAmount"/></td><%-- 总金额 --%>
              </tr>
              <tr>
               	<td class="center">
               	<s:if test='deliverInfo.totalQuantity != null'>
               		<s:text name="format.number"><s:param value="deliverInfo.totalQuantity"></s:param></s:text>
               	</s:if>
                <s:else>
                	&nbsp;
                </s:else>
               	</td>
               	<td class="center">
               	<s:if test='deliverInfo.totalAmount != null'>
               		<s:text name="format.price"><s:param value="deliverInfo.totalAmount"></s:param></s:text>
               	</s:if>
                <s:else>
                	&nbsp;
                </s:else>
               	</td>
              </tr>
            </table>
            <hr class="space" />
            <div class="center">
              <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
              <%-- 关闭 --%>
              <span class="button-text"><s:text name="PRM44_close"/></span>
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
</s:i18n>
<%-- ================== 报表打印共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
<%-- ==================  报表打印共通导入 End ======================== --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>