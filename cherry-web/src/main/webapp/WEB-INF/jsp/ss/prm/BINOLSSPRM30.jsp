<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM30.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.ss.BINOLSSPRM30">
<div id="div_main" class="main container clearfix">
<div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="PRM30_title"/>&nbsp;(<s:text name="PRM30_sequenceNo"/>:<s:property value="returnInfo.number"/>)</span>
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
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          	<div id="print_param_hide" class="hide">
	          		<input type="hidden" name="billType" value='<s:property value="returnInfo.tradeType"/>'/>
	          		<input type="hidden" name="pageId" value="BINOLSSPRM30"/>
	          		<input type="hidden" name="billId" value="${proAllocationId}"/>
          		 </div>
	          	<cherry:show domId="BINOLSSPRM30PNT">
	          	<button onclick="openPrintApp('Print');return false;" class="confirm right">
					<span class="ui-icon icon-file-print"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
				</button>
				</cherry:show>
				<cherry:show domId="BINOLSSPRM30VEW">
				<button onclick="openPrintApp('View');return false;" class="confirm right">
					<span class="ui-icon icon-file-view"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
				</button>
				</cherry:show>
            <cherry:show domId="BINOLSSPRM30EXP">
                <div id="export_param_hide" class="hide">
                    <input type="hidden" name="billId" value="${proAllocationId}"/>
                </div>
                <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSSPRM30',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
                    <span class="ui-icon icon-file-export"></span>
                    <span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
                </button>
            </cherry:show>
          </div>
          <div class="section-content">
          <div>
            <div class="box-header"></div>
                <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
                 <input type="hidden" id="entryID" name="entryID" value='<s:property value="returnInfo.WorkFlowID"/>'/>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 调拨单号 --%>
                        <th><s:text name="PRM30_allocationNo"/></th>
                        <td><s:property value="returnInfo.allocationNo"/></td>
                        <%-- 调拨日期 --%>
                        <th><s:text name="PRM30_date"/></th>
                        <td><s:property value="returnInfo.allocationDate"/></td>
                    </tr>
                    <tr>
                        <%-- 关联单号 --%>
                        <th><s:text name="PRM30_relevanceNo"/></th>
                        <td><s:property value="returnInfo.relevanceNo"/></td>
                        <%-- 业务类型 --%>
                        <th><s:text name="PRM30_tradeType"/></th>
                        <td><s:property value='#application.CodeTable.getVal("1034", returnInfo.tradeType)'/></td>
                    </tr>
                    <tr>
                        <%-- 操作员 --%>
                        <th><s:text name="PRM30_employeeName"/></th>
                        <td><s:property value="returnInfo.employeeName"/></td>
                        <th></th>
                        <td></td>
                    </tr>
                    <tr>
                        <%-- 调拨理由 --%>
                        <th><s:text name="PRM30_reason"/></th>
                        <td colspan=3><s:property value="returnInfo.reason"/></td>
                    </tr>
                </table>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 申请部门 --%>
                        <th><s:text name="PRM30_sendOrg"/></th>
                        <td>(<s:property value="returnInfo.sendDepartCode"/>)<s:property value="returnInfo.sendDepart"/></td>
                        <%-- 接受部门 --%>
                        <th><s:text name="PRM30_receiveOrg"/></th>
                        <td>(<s:property value="returnInfo.receiveDepartCode"/>)<s:property value="returnInfo.receiveDepart"/></td>
                    </tr>
                </table>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 审核状态 --%>
                        <th><s:text name="PRM30_verifiedFlag"/></th>
                        <td><s:property value='#application.CodeTable.getVal("1007", returnInfo.verifiedFlag)'/></td>
                        <th></th><td></td>
                    </tr>
                </table>
                </cherry:form>
            <div class="clearfix"></div>
          </div>

          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 调拨明细一览 --%>
            <s:text name="PRM30_results_list"/>
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
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th><s:text name="PRM30_num"/></th><%-- 编号 --%>
                  <th><s:text name="PRM30_unitCode"/></th><%-- 厂商编码 --%>
                  <th><s:text name="PRM30_nameTotal"/></th><%-- 产品名称 --%>
                  <th><s:text name="PRM30_barCode"/></th><%-- 产品条码 --%>
                  <th><s:text name="PRM30_packageName"/></th><%-- 单位 --%>
                  <th><s:text name="PRM30_inventoryName"/></th><%-- 调拨仓库 --%>
                  <th><s:text name="PRM30_quantity"/></th><%-- 数量 --%>
                  <th><s:text name="PRM30_amount"/></th><%-- 金额 --%>
                  <th><s:text name="PRM30_reason"/></th><%-- 备注 --%>
                </tr>
              </thead>
              <tbody>
                <s:iterator value="returnList" status="status">
                	<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  <td><s:property value="#status.index+1"/></td>
	                  <td><span><s:property value="unitCode"/></span></td>
	                  <td><span><s:property value="nameTotal"/></span></td>	
	                  <td><span><s:property value="barCode"/></span></td>
	                  <td><span><s:property value="packageName"/></span></td>
	                  <td><span><s:property value="inventoryName"/></span></td>
	                  <td class="alignRight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></td>
	                  <td class="alignRight"><s:text name="format.price"><s:param value="price * quantity"></s:param></s:text></td>
	                  <td><p><s:property value="reason"/></p></td>
	                </tr>
                </s:iterator>
              </tbody>
            </table>
           	</div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="PRM30_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="PRM30_totalAmount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td class="center"><s:text name="format.number"><s:param value="returnInfo.totalQuantity"></s:param></s:text></td>
             	<td class="center"><s:text name="format.price"><s:param value="returnInfo.totalAmount"></s:param></s:text></td>
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
       <div id="tabs-2">
        <strong><s:text name="global.page.worksProcessing"/></strong>
        </div>
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