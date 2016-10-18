<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS10.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<%-- 语言环境 --%>
<s:set name="language" value="session.language" />
<s:i18n name="i18n.pt.BINOLPTRPS10">
<div class="main container clearfix">
<div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="RPS10_title"/>&nbsp;(<s:text name="RPS10_sequenceNo"/>:<s:property value="returnInfo.number"/>)</span>
        </div>
    </div>
      <div class="panel-content">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          </div>
          <div class="section-content">
            <form id="mainForm" method="post" class="inline">
            <div class="box-header"></div>
            <table class="detail" cellpadding="0" cellspacing="0">
            	<tr>
            		<%-- 入出库单号 --%>
            		<th><s:text name="RPS10_tradeNo"/></th>
            		<td><s:property value="returnInfo.tradeNo"/></td>
            		<%-- 入出库日期 --%>
            		<th><s:text name="RPS10_date"/></th>
            		<td><s:property value="returnInfo.date"/></td>
            	</tr>
            	<tr>
            		<%-- 关联单号 --%>
            		<th><s:text name="RPS10_relevantNo"/></th>
            		<td><s:property value="returnInfo.relevanceNo"/></td>
                    <%-- 操作员 --%>
                    <th><s:text name="RPS10_employeeName"/></th>
                    <td><s:property value="returnInfo.employeeName"/></td>
            	</tr>
            	<tr>
            		<%-- 入出库理由 --%>
            		<th><s:text name="RPS10_reason"/></th>
            		<td colspan=3><s:property value='returnInfo.comments'/></td>
            	</tr>
            </table>
            <table class="detail" cellpadding="0" cellspacing="0">
                <tr>
                	<%-- 业务类型 --%>
            		<th><s:text name="RPS10_tradeType"/></th>
            		<td><s:property value='#application.CodeTable.getVal("1263", returnInfo.tradeType)'/></td>
                    <%-- 入出库部门 --%>
                    <th><s:text name="RPS10_depart"/></th>
                    <td><s:property value="returnInfo.departName"/></td>
                </tr>
                <tr>
                    <%-- 实体仓库名称 --%>
                    <th><s:text name="global.page.depot"/></th>
                    <td><s:property value="returnInfo.depotName"/></td>
                    <%-- 逻辑仓库名称
                    <th><s:text name="global.page.logicInventory"/></th>
                    <td><s:property value="returnInfo.InventoryName"/></td>
                     --%>
                     <th></th>
                     <td></td>
                </tr>
            </table>
            <table class="detail" cellpadding="0" cellspacing="0">
                <tr>
                    <%-- 审核状态 --%>
                    <th><s:text name="RPS10_verifiedFlag"/></th>
                    <td><s:property value='#application.CodeTable.getVal("1007", returnInfo.verifiedFlag)'/></td>
                    <%-- 入出状态 --%>
                    <th><s:text name="RPS10_stockType"/></th>
                    <td><s:property value='#application.CodeTable.getVal("1025", returnInfo.stockType)'/></td>
                </tr>
            </table>
            <div class="clearfix"></div>
            </form>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 入出库明细一览 --%>
            <s:text name="RPS10_results_list"/>
            </strong>
          </div>
          <div class="section-content">
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th class="center"><s:text name="RPS10_num"/></th><%-- 编号 --%>
                  <th class="center"><s:text name="RPS10_unitCode"/></th><%-- 厂商编码 --%>
                  <th class="center"><s:text name="RPS10_nameTotal"/></th><%-- 产品名称 --%>
                  <th class="center"><s:text name="RPS10_barCode"/></th><%-- 产品条码 --%> 
                  <th class="center"><s:text name="RPS10_stockType"/></th><%-- 入出状态 --%>
                  <th class="center"><s:text name="global.page.logicInventory"/></th><%-- 逻辑仓库名称 --%>
                  <th class="center"><s:text name="RPS10_moduleCode"/></th><%-- 计量单位 --%>
                  <th class="center"><s:text name="RPS10_price"/></th><%-- 单价 --%>
                  <th class="center"><s:text name="RPS10_quantity"/></th><%-- 数量 --%>
                  <th class="center"><s:text name="RPS10_amount"/></th><%-- 金额 --%>
                  <th class="center"><s:text name="RPS10_reason"/></th><%-- 备注 --%>
                </tr>
              </thead>
              <tbody>
                <s:iterator value="returnList" status="status">
                	<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  <td><s:property value="#status.index+1"/></td>
	                  <td><span><s:property value="unitCode"/></span></td>
	                  <td><span><s:property value="nameTotal"/></span></td>
	                  <td><span><s:property value="barCode"/></span></td>
	                  <td><span><s:property value='#application.CodeTable.getVal("1025", stockType)'/></span></td>
	                  <td><span><s:property value="InventoryName"/></span></td>
	                  <td class="center"><span><s:property value='#application.CodeTable.getVal("1190", moduleCode)'/></span></td>
	                  <td class="alignRight"><s:text name="format.price"><s:param value="price"></s:param></s:text></td>
	                  <td class="alignRight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></td>
	                  <td class="alignRight"><s:text name="format.price"><s:param value="price*quantity"></s:param></s:text></td>
	                  <td><p><s:property value="comments"/></p></td>
	                </tr>
                </s:iterator>
              </tbody>
            </table>
           	</div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="RPS10_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="RPS10_totalAmount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td class="center">
	              	<%-- 总数量 --%>
	              	<span class="<s:if test='returnInfo.totalQuantity < 0'>highlight</s:if>"><s:text name="format.number"><s:param value="returnInfo.totalQuantity"></s:param></s:text></span>
                </td>
             	<td class="center">
             		<%-- 总金额--%>
             		<span class="<s:if test='returnInfo.totalAmount < 0'>highlight</s:if>"><s:text name="format.price"><s:param value="returnInfo.totalAmount"></s:param></s:text></span>
             	</td>
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
</s:i18n>
