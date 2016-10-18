<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS21.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.pt.BINOLPTRPS21">
<div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="RPS21_title"/>&nbsp;(<s:text name="RPS21_stockTakingNo"/>:<s:property value="takingInfo.stockTakingNo"/>)</span>
        </div>
    </div>
      <div class="panel-content">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="RPS21_header"/>
          	</strong>
          </div>
          <div class="section-content">
          <form id="mainForm" method="post" class="inline">
            <div class="box-header"></div>
            <table class="detail" cellpadding="0" cellspacing="0">
              <tr>
                <th><%-- 盘点单号 --%><s:text name="RPS21_stockTakingNoIF"/></th>
                <td><span><s:property value="takingInfo.stockTakingNoIF"/></span></td>
                 <th><%-- 盘点日期 --%><s:text name="RPS21_stockTakingDate"/></th>
                <td><span><s:property value="takingInfo.stockTakingDate"/></span></td>
              </tr>
              <tr>
                <th><%-- 盘点类型 --%><s:text name="RPS21_type"/></th>
                <td><span><s:property value='#application.CodeTable.getVal("1054", takingInfo.takingType)'/></span></td>
                <th><%-- 盘点员 --%><s:text name="RPS21_employeeName"/></th>
                <td><span><s:property value="'('+takingInfo.employeeCode+')'+takingInfo.employeeName"/></span></td>
              </tr>
              <tr>
                <th><%-- 盘点理由 --%><s:text name="RPS21_reason"/></th>
                <td colspan=3><span><s:property value="takingInfo.comments"/></span></td>
              </tr>
               </table>
               <table class="detail" cellpadding="0" cellspacing="0">
               <tr>
                <th><%-- 部门名称 --%><s:text name="RPS21_departName"/></th>
                <td><span>(<s:property value="takingInfo.departCode"/>)<s:property value="takingInfo.departName"/></span></td>
                <th><%-- 实体仓库 --%><s:text name="RPS21_inventName"/></th>
                <td><span><s:property value="'('+takingInfo.depotCode+')'+takingInfo.inventoryName"/></span></td>
               </tr>
              <tr>
                <th><%-- 逻辑仓库 --%><s:text name="RPS21_LogicInventory"/></th>
                <td><span><s:property value="'('+takingInfo.logicInventoryCode+')'+takingInfo.logicInventoryName"/></span></td>
                <th></th><td></td>
              </tr>
               </table>
               <table class="detail" cellpadding="0" cellspacing="0">
                   <tr>
                    <th><%-- 审核状态 --%><s:text name="RPS21_verifiedFlag"/></th>
                    <td><span><s:property value='#application.CodeTable.getVal("1007", takingInfo.verifiedFlag)'/></span></td>
                    <th><%-- 审核人 --%><s:text name="RPS21_auditName"/></th>
                    <td><span><s:property value="'('+takingInfo.auditCode+')'+takingInfo.auditName"/></span></td>
                   </tr>
               </table>
               <div class="clearfix"></div>
              </form>
            </div> 
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 盘点明细一览 --%>
            <s:text name="RPS21_results_list"/>
            </strong>
          </div>
          <div class="section-content">
          	 <%--<div class="toolbar clearfix">
          	 
          	 <span class="left">
              <a class="export"><span class="ui-icon icon-export"></span>
              	<span class="button-text">
         
              	<s:text name="RPS21_export"/>
              	</span></a> <a class="import"><span class="ui-icon icon-export"></span>
              	<span class="button-text">
   
              	<s:text name="RPS21_detailExport"/>
              	</span>
              	</a>
              	<a class="import"><span class="ui-icon icon-export"></span>
       
              	<span class="button-text"><s:text name="RPS21_gainExport"/></span></a></span> 
              </div>--%>
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table id="sort_table" cellpadding="0" cellspacing="0" border="0" width="100%">
              <thead>
                <tr>
                  <th width="3%"><s:text name="RPS21_num"/></th><%-- No. --%>
                  <th><s:text name="RPS21_unitCode"/></th><%-- 厂商编码 --%>
                  <th style="min-width:90px;" class="th-sort-alpha"><span class="left"><s:text name="RPS21_nameTotal"/></span></th><%-- 产品名称 --%>
                  <th><s:text name="RPS21_barCode"/></th><%-- 促销品条码 --%>
                  <th><s:text name="RPS21_packageName"/></th><%-- 单位 --%>
                  <th style="min-width:70px;*width:70px;" class="th-sort-numeric"><span class="left"><s:text name="RPS21_quantity"/></span></th><%-- 账面数量 --%>
                  <th style="min-width:70px;*width:70px;" class="th-sort-numeric"><span class="left"><s:text name="RPS21_realQuantity"/></span></th><%-- 实盘数量 --%>
                  <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="RPS21_gainQuantity"/></span></th><%-- 盈亏数量 --%>
                  <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="RPS21_price"/></span></th><%-- 单价 --%>
                  <th style="min-width:70px;*width:70px;" class="th-sort-numeric"><span class="left"><s:text name="RPS21_detailAmount"/></span></th><%-- 盘差金额 --%>
                  <th width="8%"><s:text name="RPS21_handleType"/></th><%-- 处理方式 --%>
                </tr>
              </thead>
              <tbody>
              <s:iterator value="takingDetailList" id="takingDetail" status="R">
                <tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>">
                  <td class="rowNum"> <s:property value="#R.index+1"/></td>
                  <td>
                  <s:if test='unitCode != null && !"".equals(unitCode)'>
                  	<s:property value="unitCode"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td>
                  <s:if test='nameTotal != null && !"".equals(nameTotal)'>
                 	<s:property value="nameTotal"/>
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
                  <td style=text-align:right;>
                  <s:if test="quantity !=null">
					<s:if test="quantity >= 0"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
				 </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td style=text-align:right;>
                  <s:if test='realQuantity != null'>
                  	<s:text name="format.number"><s:param value="realQuantity"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td style=text-align:right;>
                  <s:if test="gainQuantity !=null">
					<s:if test="gainQuantity >= 0"><s:text name="format.number"><s:param value="gainQuantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="gainQuantity"></s:param></s:text></span></s:else>
				 </s:if>
				 <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td style=text-align:right;>
                  <s:if test='price != null'>
                  	<s:text name="format.price"><s:param value="price"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td style=text-align:right;>
                  <s:if test="detailAmount !=null">
					<s:if test="detailAmount >= 0"><s:text name="format.price"><s:param value="detailAmount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="detailAmount"></s:param></s:text></span></s:else>
				 </s:if>
				 <s:else>
                  	&nbsp;
                  </s:else>
				  </td>
                  <td>
                  	<%-- 盘点处理方式 --%>
					<span><s:property value='#application.CodeTable.getVal("1020", handleType)'/></span>
                  </td>
                </tr>
               </s:iterator>
              </tbody>
            </table>
           	</div>
            <hr class="space" />
            <s:if test='"0".equals(profitKbn)'>
            	<table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
	              <tr>
	                <th rowspan="2" scope="row" class="center"><s:text name="RPS21_total"/></th><%-- 合计 --%>
	                <td class="center"><s:text name="RPS21_overQuantity"/></td><%-- 盘盈数 --%>
	                <td class="center"><s:text name="RPS21_overAmount"/></td><%-- 盘盈金额 --%>
	              </tr>
	              <tr>
	              	<td class="center">
              			<s:text name="format.number"><s:param value="takingInfo.overQuantity"></s:param></s:text>
	              	</td>
	              	<td class="center">
	              		<s:text name="format.price"><s:param value="takingInfo.overAmount"></s:param></s:text>
	              	</td>
	              </tr>
	             </table>
            </s:if>
            <s:elseif test='"1".equals(profitKbn)'>
            	<table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
	              <tr>
	                <th rowspan="2" scope="row" class="center"><s:text name="RPS21_total"/></th><%-- 合计 --%>
	                <td class="center"><s:text name="RPS21_shortQuantity"/></td><%-- 盘亏数 --%>
                <td class="center"><s:text name="RPS21_shortAmount"/></td><%-- 盘亏金额 --%>
	              </tr>
	              <tr>
	              	<td class="highlight center">
              			<s:text name="format.number"><s:param value="takingInfo.shortQuantity"></s:param></s:text>
	              	</td>
	              	<td class="highlight center">
	              		<s:text name="format.price"><s:param value="takingInfo.shortAmount"></s:param></s:text>
	              	</td>
	              </tr>
	             </table>
            </s:elseif>
            <s:else>
	            <table cellpadding="0" cellspacing="0" width="60%" border="0" class="right editable">
	              <tr>
	                <th rowspan="2" scope="row" class="center"><s:text name="RPS21_total"/></th><%-- 合计 --%>
	                <td class="center"><s:text name="RPS21_overQuantity"/></td><%-- 盘盈数 --%>
	                <td class="center"><s:text name="RPS21_overAmount"/></td><%-- 盘盈金额 --%>
	                <td class="center"><s:text name="RPS21_shortQuantity"/></td><%-- 盘亏数 --%>
	                <td class="center"><s:text name="RPS21_shortAmount"/></td><%-- 盘亏金额 --%>
	                <td class="center"><s:text name="RPS21_summQuantity"/></td><%-- 总数量 --%>
	                <td class="center"><s:text name="RPS21_summAmount"/></td><%-- 总金额 --%>
	              </tr>
	              <tr>
	              	<td class="center">
	              		<s:text name="format.number"><s:param value="takingInfo.overQuantity"></s:param></s:text>
	              	</td>
	              	<td class="center">
	              		<s:text name="format.price"><s:param value="takingInfo.overAmount"></s:param></s:text>
	              	</td>
	              	<td class="highlight center">
	              		<s:text name="format.number"><s:param value="takingInfo.shortQuantity"></s:param></s:text>
	              	</td>
	              	<td class="highlight center">
	              		<s:text name="format.price"><s:param value="takingInfo.shortAmount"></s:param></s:text>
	              	</td>
	              <s:if test="takingInfo.summQuantity !=null">
					<s:if test="takingInfo.summQuantity >= 0">
	                	<td class="center"><s:text name="format.number"><s:param value="takingInfo.summQuantity"></s:param></s:text></td>
	                </s:if>
	                <s:else>
	                	<td class="highlight center"><s:text name="format.number"><s:param value="takingInfo.summQuantity"></s:param></s:text></td>
	                </s:else>
	              </s:if>
	              <s:else>
	                <td class="center">&nbsp;</td>
	              </s:else>
	              <s:if test="takingInfo.summAmount !=null">
				<s:if test="takingInfo.summAmount >= 0">
	               	<td class="center"><s:text name="format.price"><s:param value="takingInfo.summAmount"></s:param></s:text></td>
	               </s:if>
	               <s:else>
	               	<td class="highlight center"><s:text name="format.price"><s:param value="takingInfo.summAmount"></s:param></s:text></td>
	               </s:else>
	               </s:if>
	              <s:else>
	             	<td class="center">&nbsp;</td>
	              </s:else>
	              </tr>
	            </table>
            </s:else>
            <hr class="space" />
            <div class="center">
              <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
              <%-- 关闭 --%>
              <span class="button-text"><s:text name="RPS21_close"/></span>
             </button>
            </div>
          </div>
        </div>
      </div>
    </div>
</s:i18n>
