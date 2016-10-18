<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS18.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.pt.BINOLPTRPS18">
<div class="main container clearfix">
<div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span>
            <s:if test='"1".equals(allocationType)'>
	            <s:text name="RPS18_titleOUT"/>
            </s:if>
            <s:else>
	            <s:text name="RPS18_titleIN"/>
            </s:else>
            &nbsp;(<s:text name="RPS18_allocationNo"/>:<s:property value="returnInfo.allocationNo"/>)</span>
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
                        <%-- 调拨单号 --%>
                        <th>
                        	<s:if test='"1".equals(allocationType)'>
                        		<s:text name="RPS18_allocationOUTNoIF"/>
                        	</s:if>
                        	<s:else>
                        		<s:text name="RPS18_allocationINNoIF"/>
                        	</s:else>
                        </th>
                        <td><s:property value="returnInfo.allocationNoIF"/></td>
                        <%-- 调拨日期 --%>
                        <th><s:text name="RPS18_date"/></th>
                        <td><s:property value="returnInfo.allocationDate"/></td>
                    </tr>
                    <tr>
                        <%-- 关联单号 --%>
                        <th>
                        	<s:if test='"1".equals(allocationType)'>
		                        <s:text name="RPS18_relevanceNoIN"/>
                        	</s:if>
                        	<s:else>
		                        <s:text name="RPS18_relevanceNoOUT"/>
                        	</s:else>
                        </th>
                        <td><s:property value="returnInfo.relevanceNo"/></td>
                        <%-- 操作员 --%>
                        <th><s:text name="RPS18_employeeName"/></th>
                        <td><s:property value="returnInfo.employeeName"/></td>
                    </tr>
                    <tr>
                        <%-- 调拨理由 --%>
                        <th><s:text name="RPS18_reason"/></th>
                        <td colspan=3><s:property value="returnInfo.reason"/></td>
                    </tr>
                </table>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 申请部门 --%>
                        <th><s:text name="RPS18_sendOrg"/></th>
                        <td>(<s:property value="returnInfo.bin_OrganizationIDOut"/>)<s:property value="returnInfo.sendDepart"/></td>
                        <%-- 接受部门 --%>
                        <th><s:text name="RPS18_receiveOrg"/></th>
                        <td>(<s:property value="returnInfo.bin_OrganizationIDIn"/>)<s:property value="returnInfo.receiveDepart"/></td>
                    </tr>
                </table>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 审核状态 --%>
                        <th><s:text name="RPS18_verifiedFlag"/></th>
                        <td><s:property value='#application.CodeTable.getVal("1007", returnInfo.verifiedFlag)'/></td>
                        <th></th>
                        <td></td>
                    </tr>
                </table>
                <div class="clearfix"></div>
            </form>
          </div>
        </div>
        <div class="section">
          <div class="section-header">
          <strong>
          	<span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 调拨明细一览 --%>
          	<s:if test='"1".equals(allocationType)'>
	            <s:text name="RPS18_results_OUTlist"/>
          	</s:if>
          	<s:else>
	            <s:text name="RPS18_results_INlist"/>
          	</s:else>
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
                  <th class="center"><s:text name="RPS18_num"/></th><%-- 编号 --%>
                  <th class="center"><s:text name="RPS18_unitCode"/></th><%-- 厂商编码 --%>
                  <th class="center"><s:text name="RPS18_nameTotal"/></th><%-- 产品名称 --%>
                  <th class="center"><s:text name="RPS18_barCode"/></th><%-- 产品条码 --%>
                  <th class="center"><s:text name="RPS18_moduleCode"/></th><%-- 计量单位 --%>
                  <th class="center"><s:text name="RPS18_inventoryName"/></th><%-- 调拨仓库 --%>
                  <th class="center"><s:text name="RPS18_price"/></th><%-- 价格 --%>
                  <th class="center"><s:text name="RPS18_quantity"/></th><%-- 数量 --%>
                  <th class="center"><s:text name="RPS18_amount"/></th><%-- 金额 --%>
                  <th class="center"><s:text name="RPS18_reason"/></th><%-- 备注 --%>
                </tr>
              </thead>
              <tbody>
                <s:iterator value="returnList" status="status">
                	<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  <td><s:property value="#status.index+1"/></td>
	                  <td><span><s:property value="unitCode"/></span></td>
	                  <td><span><s:property value="nameTotal"/></span></td>	
	                  <td><span><s:property value="barCode"/></span></td>
	                  <td><span><s:property value='#application.CodeTable.getVal("1190", moduleCode)'/></span></td>
	                  <td><span><s:property value="inventoryName"/></span></td>
	                  <td style=text-align:right;><s:text name="format.price"><s:param value="price"></s:param></s:text></td>
	                  <td style=text-align:right;><s:text name="format.number"><s:param value="quantity"></s:param></s:text></td>
	                  <td style=text-align:right;><s:text name="format.price"><s:param value="price * quantity"></s:param></s:text></td>
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
                <td class="center"><s:text name="RPS18_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="RPS18_totalAmount"/></td><%-- 总金额--%>
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
</s:i18n>
