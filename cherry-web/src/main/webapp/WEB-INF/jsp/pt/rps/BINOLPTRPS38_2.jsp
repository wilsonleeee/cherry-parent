<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS38.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.pt.BINOLPTRPS38">
<div id="div_main" class="main container clearfix">
<div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="RPS38_title"/>&nbsp;</span>
        </div>
    </div>
      <div class="panel-content">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          	<div id="export_param_hide" class="hide">
          		<input type="hidden" name="prtVendorId" value="<s:property value="prtVendorId"/>"/>
          		<input type="hidden" name="validFlag" value="<s:property value="validFlag"/>"/>
          		<input type="hidden" name="productId" value="<s:property value="productId"/>"/>
          		
          		<div><input type="hidden" name="params" value="<s:property value="params"/>"/></div>
          	</div>
          	<cherry:show domId="PTRPS38DETEXP">
             <button class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLPTRPS38',1);setRPS38Params('#export_param_hide');return false;">
             	<span class="ui-icon icon-file-export"></span>
             	<span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
             </button>
            </cherry:show>
          </div>
          <div class="section-content">
            <form id="mainForm" method="post" class="inline">
                <div class="box-header"></div>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 产品名称 --%>
                        <th><s:text name="RPS38_name"/></th>
                        <td><s:property value="proInfo.nameTotal"/></td>
                        <%-- 日期范围 --%>
                        <th></th>
                        <td></td>
                    </tr>
                    <tr>
                        <%-- 厂商编码 --%>
                        <th><s:text name="RPS38_unitCode"/></th>
                        <td><s:property value="proInfo.unitCode"/></td>
                        <%-- 产品条码 --%>
                        <th><s:text name="RPS38_barCode"/></th>
                        <td>
                        <s:property value="proInfo.barCode"/>
                        <s:iterator value="proInfo.list" id="barcode" status="st">
                        	<s:property value="barcode"/><s:if test="!#st.isLast()">,</s:if>
                        </s:iterator>
                        </td>
                    </tr>
                    <tr>
                        <%-- 计量单位 --%>
                        <th><s:text name="RPS38_moduleCode"/></th>
                        <td><s:property value='#application.CodeTable.getVal("1190", proInfo.moduleCode)'/></td>
                        <th> </th>
                        <td> </td>
                    </tr>
                    <tr>
                     <th><s:text name="RPS38_quantity"/></th>
                        <td>
                            <s:if test="quantity >= 0">
                            <s:text name="format.number"><s:param value="quantity"></s:param></s:text>
                            </s:if>
                            <s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
                        </td>
                        <th></th>
                        <td></td>
                    </tr>
                </table>
                <div class="clearfix"></div>
            </form>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 库存明细一览 --%>
            <s:text name="RPS38_results_list"/>
            </strong>
          </div>
          <div class="section-content">
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th rowspan="2" class="center"><s:text name="RPS38_num"/></th><%-- 编号 --%>
                  <th rowspan="2" class="center"><s:text name="RPS38_departName"/></th><%-- 部门 --%>	
                  <th rowspan="2" class="center"><s:text name="RPS38_inventory"/></th><%-- 仓库 --%>
                  <th rowspan="2" class="center"><s:text name="RPS38_lgcInventory"/></th><%-- 逻辑仓库 --%>		
                  <th rowspan="2" class="center"><s:text name="RPS38_quantity"/></th><%-- 库存 --%>	
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
		                  <s:if test="quantity >= 0">
		                  	<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
		                  </s:if>
		                  <s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
	                  </td>
	                 
	                </tr>
                </s:iterator>
              </tbody>
              </table>
           	</div>
            <hr class="space" />
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
</s:i18n>
<%-- ================== 报表打印共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
<%-- ==================  报表打印共通导入 End ======================== --%>