<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM42.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.ss.BINOLSSPRM42">
<div class="main container clearfix">
<div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="PRM42_title"/>&nbsp;(<s:text name="PRM42_sequenceNo"/>:<s:property value="returnInfo.number"/>)</span>
        </div>
    </div>
      <div id="div_main" class="panel-content">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          	<div id="print_param_hide" class="hide">
	          		<input type="hidden" name="billType" value='<s:property value="returnInfo.tradeType"/>'/>
	          		<input type="hidden" name="pageId" value="BINOLSSPRM42"/>
	          		<input type="hidden" name="billId" value="${proReturnId}"/>
          		 </div>
	          	<cherry:show domId="BINOLSSPRM42PNT">
	          	<button onclick="openPrintApp('Print');return false;" class="confirm right">
					<span class="ui-icon icon-file-print"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
				</button>
				</cherry:show>
				<cherry:show domId="BINOLSSPRM42VEW">
				<button onclick="openPrintApp('View');return false;" class="confirm right">
					<span class="ui-icon icon-file-view"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
				</button>
				</cherry:show>
          	<div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${proReturnId}"/></div>
             <cherry:show domId="BINOLSSPRM42EXP">
             <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSSPRM42',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
             	<span class="ui-icon icon-file-export"></span>
             	<span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
             </button>
             </cherry:show>
          </div>
          <div class="section-content">
            <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
                <div class="box-header"></div>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 退库单号 --%>
                        <th><s:text name="PRM42_returnNo"/></th>
                        <td><s:property value="returnInfo.returnNo"/></td>
                        <%-- 退库日期 --%>
                        <th><s:text name="PRM42_date"/></th>
                        <td><s:property value="returnInfo.returnDate"/></td>
                    </tr>
                    <tr>
                        <%-- 操作员 --%>
                        <th><s:text name="PRM42_employeeName"/></th>
                        <td><s:property value="returnInfo.employeeName"/></td>
                        <th></th><td></td>
                    </tr>
                    <tr>
                        <%-- 退库理由 --%>
                        <th><s:text name="PRM42_reason"/></th>
                        <td colspan=3><s:property value="returnInfo.reason"/></td>
                    </tr>
                </table>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 退库部门 --%>
                        <th><s:text name="PRM42_sendOrg"/></th>
                        <td><s:property value="returnInfo.sendDepart"/></td>
                        <%-- 接收部门 --%>
                        <th><s:text name="PRM42_receiveOrg"/></th>
                        <td><s:property value="returnInfo.receiveDepart"/></td>
                    </tr>
                </table>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 审核状态 --%>
                        <th><s:text name="PRM42_verifiedFlag"/></th>
                        <td><s:property value='#application.CodeTable.getVal("1007", returnInfo.verifiedFlag)'/></td>
                    </tr>
                </table>
                <div class="clearfix"></div>
            </cherry:form>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 退库明细一览 --%>
            <s:text name="PRM42_results_list"/>
            </strong>
          </div>
          <div class="section-content">
<%--
          	 <div class="toolbar clearfix">
          	 	<span class="left">
	          	 	<%-- 导出
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
                  <th><s:text name="PRM42_num"/></th><%-- 编号 --%>
                  <th><s:text name="PRM42_unitCode"/></th><%-- 厂商编码 --%>
                  <th><s:text name="PRM42_nameTotal"/></th><%-- 产品名称 --%>
                  <th><s:text name="PRM42_barCode"/></th><%-- 产品条码 --%>
                  <th><s:text name="PRM42_packageName"/></th><%-- 单位 --%>
                  <th><s:text name="PRM42_quantity"/></th><%-- 数量 --%>
                  <th><s:text name="PRM42_amount"/></th><%-- 金额 --%>
                  <th><s:text name="PRM42_reason"/></th><%-- 备注 --%>
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
	                  <td class="alignRight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></td>
	                  <td class="alignRight"><s:text name="format.price"><s:param value="price*quantity"></s:param></s:text></td>
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
                <td class="center"><s:text name="PRM42_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="PRM42_totalAmount"/></td><%-- 总金额--%>
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
    </div>
    </div>
<%-- ================== 报表打印共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
<%-- ==================  报表打印共通导入 End ======================== --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
</s:i18n>
