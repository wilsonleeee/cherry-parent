<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/cp/act/BINOLCPACT12_1.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:set name="language" value="session.language" />
<s:i18n name="i18n.cp.BINOLCPACT12">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
        <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="ACT12_titleDetail"/></span>
        </div>
        </div>
		<div class="panel-content">
			<div class="section">
				<div class="section-header">
				<strong>
				<span class="ui-icon icon-ttl-section-info"></span>
          			<%-- 基本信息 --%>
          			<s:text name="global.page.title"/>
          		</strong>
          		</div>
          		<div class="section-content">
                    <div class="box-header"></div>
          			<table class="detail" cellpadding="0" cellspacing="0">
          				<tr>
          					<%-- 主题活动 --%>
          					<th><s:text name="ACT12_mainCampaign"/></th>
          					<td><s:property value="campaignStockDetail.campaign"/></td>
          					<%-- 柜台名称--%>
                            <th><s:text name="ACT12_counterName"/></th>
                            <td><s:property value="campaignStockDetail.counter"/></td>
          				</tr>
              		    <tr>
                            <%-- 活动名称 --%>
          					<th><s:text name="ACT12_subcampName"/></th>
          					<td>${campaignStockDetail.subcamp}</td>
                        	<th></th>
                        	<td></td>
                        </tr>
          			</table>
                    <div class="clearfix"></div>
          		</div>
			</div>
			<div class="section">
         		 <div class="section-header">
         		 	<strong>
         		 		<span class="ui-icon icon-ttl-section-search-result"></span>
          				<%-- 产品库存明细一览 --%>
            			<s:text name="ACT12_results_list"/>
            		</strong>
          		</div>
          		<div class="section-content">
            		<div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            			<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              				<thead>
                				<tr>
                  					<th class="center"><s:text name="ACT12_num"/></th>          <%-- 编号 --%>
                  					<th class="center"><s:text name="ACT12_unitCode"/></th>     <%-- 厂商编码 --%>
                  					<th class="center"><s:text name="ACT12_productName"/></th>  <%-- 产品名称 --%>
                  					<th class="center"><s:text name="ACT12_barCode"/></th>      <%-- 产品条码 --%>
                  					<th class="center"><s:text name="ACT12_prtType"/></th> 		<%-- 产品类型 --%>
                  					<th class="center"><s:text name="ACT12_totalQuantity"/></th>  <%-- 分配数量 --%>
                  					<th class="center"><s:text name="ACT12_currentQuantity"/></th>  <%-- 剩余数量 --%>
                  					<th class="center"><s:text name="ACT12_safeQuantity"/></th>     <%-- 安全数量 --%>
                				</tr>
              				</thead>
              				<tbody>
                				<s:iterator value="campaignStockProductDetail" status="status">
                				<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  				<td><s:property value="#status.index+1"/></td>
	                  				<td><span><s:property value="unitCode"/></span></td>
	                  				<td><span><s:property value="productName"/></span></td>
	                  				<td><span><s:property value="barCode"/></span></td>
	                  				<td><span>
		                  				<s:if test='"N".equals(prtType)'><s:text name="ACT12_pro"/></s:if>
		                  				<s:elseif test='"P".equals(prtType)'><s:text name="ACT12_promp"/></s:elseif>
	                  				</span></td>
	                  				<td class="alignRight"><span><s:property value="totalQuantity"/></span></td>
	                  				<td class="alignRight"><span><s:property value="currentQuantity"/></span></td>
	                  				<td class="alignRight"><span><s:property value="safeQuantity"/></span></td>
	                			</tr>
                				</s:iterator>
              				</tbody>
            			</table>
           			</div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="ACT12_sumTotalQuantity"/></td><%-- 总分配数量 --%>
                <td class="center"><s:text name="ACT12_sumCurrentQuantity"/></td><%-- 总剩余数量--%>
              </tr>
              <tr>
                <td class="center">
                	<s:if test="campaignStockDetail.sumTotalQuantity >= 0"><s:text name="format.number"><s:param value="campaignStockDetail.sumTotalQuantity"></s:param></s:text></s:if>
                	<s:else><span class="highlight"><s:text name="format.number"><s:param value="campaignStockDetail.sumTotalQuantity"></s:param></s:text></span></s:else>
                </td>
             	<td class="center">
             		<s:if test="campaignStockDetail.sumCurrentQuantity >= 0"><s:text name="format.number"><s:param value="campaignStockDetail.sumCurrentQuantity"></s:param></s:text></s:if>
             		<s:else><span class="highlight"><s:text name="format.number"><s:param value="campaignStockDetail.sumCurrentQuantity"></s:param></s:text></span></s:else>
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