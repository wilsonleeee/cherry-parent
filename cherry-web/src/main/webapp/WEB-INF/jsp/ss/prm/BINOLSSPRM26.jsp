<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM26.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.ss.BINOLSSPRM26">
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="PRM26_title"/>&nbsp;(<s:text name="PRM26_stockTakingNo"/>:<s:property value="takingInfo.stockTakingNo"/>)</span>
        </div>
    </div>
      <div class="panel-content">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="PRM26_header"/>
          	</strong>
          	 <div id="print_param_hide" class="hide">
	          		<input type="hidden" name="billType" value='<s:property value="takingInfo.Type"/>'/>
	          		<input type="hidden" name="pageId" value="BINOLSSPRM26"/>
	          		<input type="hidden" name="billId" value="${stockTakingId}"/>
          		 </div>
	          	<cherry:show domId="BINOLSSPRM26PNT">
	          	<button onclick="openPrintApp('Print');return false;" class="confirm right">
					<span class="ui-icon icon-file-print"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
				</button>
				</cherry:show>
				<cherry:show domId="BINOLSSPRM26VEW">
				<button onclick="openPrintApp('View');return false;" class="confirm right">
					<span class="ui-icon icon-file-view"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
				</button>
				</cherry:show>
            <cherry:show domId="BINOLSSPRM26EXP">
                <div id="export_param_hide" class="hide">
                    <input type="hidden" name="billId" value="${stockTakingId}"/>
                    <input type="hidden" name="profitKbn" value="${profitKbn}"/>
                </div>
                <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSSPRM26',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
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
                        <th>
                            <%-- 盘点单号 --%>
                            <s:text name="PRM26_stockTakingNoIF"/>
                        </th>
                        <td>
                            <span><s:property value="takingInfo.stockTakingNoIF"/></span>
                        </td>
                        <th>
                            <%-- 盘点时间 --%>
                            <s:text name="PRM26_tradeDateTime"/>
                        </th>
                        <td>
                            <span><s:property value="takingInfo.tradeDateTime"/></span>
                        </td>
                      </tr>
                      <tr>
                        <th>
                            <%-- 关联单号 --%>
                            <s:text name="PRM26_relevanceNo"/>
                        </th>
                        <td>
                            <span><s:property value="takingInfo.relevanceNo"/></span>
                        </td>
                        <th>
                            <%-- 盘点员 --%>
                            <s:text name="PRM26_employeeName"/>
                        </th>
                        <td><span><s:property value="takingInfo.employeeName"/></span></td>
                      </tr>
                      <tr>
                      </tr>
                      <tr>
                        <th>
                            <%-- 盘点类型 --%>
                            <s:text name="PRM26_type"/>
                        </th>
                        <td>
                            <%-- 盘点类型 --%>
                            <span><s:property value='#application.CodeTable.getVal("1019", takingInfo.takingType)'/></span>
                        </td>
                        <th></th>
                        <td></td>
                      </tr>
                      <tr>  
                        <%--<th>
                                                                       审核状态
                            <s:text name="PRM26_verifiedFlag"/>
                        </th>
                        <td>
                            <span><s:property value='#application.CodeTable.getVal("1007", takingInfo.verifiedFlag)'/></span>
                        </td> --%>
                        
                        <th>
                            <%-- 盘点理由 --%>
                            <s:text name="PRM26_reason"/>
                        </th>
                        <td colspan=3>
                            <span><s:property value="takingInfo.reason"/></span>
                        </td>
                      </tr>
                        </table>
                        <table class="detail" cellpadding="0" cellspacing="0">
                            <tr>
                                <th>
                                    <%-- 部门名称 --%>
                                    <s:text name="PRM26_departName"/>
                                </th>
                                <td>
                                    <span><s:property value="takingInfo.departName"/></span>
                                </td>
                                <th>
                                    <%-- 仓库名称 --%>
                                    <s:text name="PRM26_inventName"/>
                                </th>
                                <td>
                                    <span><s:property value="takingInfo.inventoryName"/></span>
                                </td>
                            </tr>

                       </table>
                       <div class="clearfix"></div>
                </cherry:form>
            </div> 
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 盘点明细一览 --%>
            <s:text name="PRM26_results_list"/>
            </strong>
            <%-- 已剔除不需要管理库存的促销品 --%>
            <span class="red"><s:text name="PRM26_detailNoStockNotice"/></span>
          </div>
          <div class="section-content">
          	 <%--<div class="toolbar clearfix">
          	 
          	 <span class="left">
              <a class="export"><span class="ui-icon icon-export"></span>
              	<span class="button-text">
         
              	<s:text name="PRM26_export"/>
              	</span></a> <a class="import"><span class="ui-icon icon-export"></span>
              	<span class="button-text">
   
              	<s:text name="PRM26_detailExport"/>
              	</span>
              	</a>
              	<a class="import"><span class="ui-icon icon-export"></span>
       
              	<span class="button-text"><s:text name="PRM26_gainExport"/></span></a></span> 
              </div>--%>
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table id="sort_table" cellpadding="0" cellspacing="0" border="0" width="100%">
              <thead>
                <tr>
                  <th width="3%"><s:text name="PRM26_num"/></th><%-- No. --%>
                  <th style="min-width:90px;" class="th-sort-alpha"><span class="left"><s:text name="PRM26_nameTotal"/></span></th><%-- 促销品名称 --%>
                  <th><s:text name="PRM26_unitCode"/></th><%-- 厂商编码 --%>
                  <th><s:text name="PRM26_barCode"/></th><%-- 促销品条码 --%>
                  <th><s:text name="PRM26_packageName"/></th><%-- 单位 --%>
                  <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="PRM26_quantity"/></span></th><%-- 账面数量 --%>
                  <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="PRM26_realQuantity"/></span></th><%-- 实盘数量 --%>
                  <th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="PRM26_gainQuantity"/></span></th><%-- 盈亏数量 --%>
                  <%--<th style="min-width:45px;*width:45px;" class="th-sort-numeric"><span class="left"><s:text name="PRM26_price"/></span></th>--%><%-- 单价 --%>
                  <th style="min-width:50px;*width:50px;" class="th-sort-numeric"><span class="left"><s:text name="PRM26_detailAmount"/></span></th><%-- 盘差金额 --%>
                  <th style="min-width:45px;*width:45px;"><s:text name="PRM26_lblLCategory"/></th><%-- 大分类 --%>
                  <th style="min-width:45px;*width:45px;"><s:text name="PRM26_lblMCategory"/></th><%-- 中分类 --%>
                  <th style="min-width:45px;*width:45px;"><s:text name="PRM26_lblSCategory"/></th><%-- 小分类 --%>
                  <th><s:text name="PRM26_handleType"/></th><%-- 处理方式 --%>
                  <th><s:text name="PRM26_reason"/></th><%-- 盘点理由 --%>
                </tr>
              </thead>
              <tbody>
              <s:iterator value="takingDetailList" id="takingDetail" status="R">
              <s:if test='"0".equals(profitKbn)'>
              	<tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>" style="background-color:<s:if test='gainQuantity > 0'>#FFF7D7</s:if><s:else>#E7E7E7</s:else>" onMouseOver="javascript:this.style.backgroundColor='#FAD163'"  onMouseOut="javascript:this.style.backgroundColor=<s:if test='gainQuantity > 0'>'#FFF7D7'</s:if><s:else>'#E7E7E7'</s:else>">
                  <td class="rowNum"> <s:property value="#R.index+1"/></td>
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
                  <s:if test="quantity !=null">
					<s:if test="quantity >= 0"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
				 </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="alignRight">
                  <s:if test='realQuantity != null'>
                  	<s:text name="format.number"><s:param value="realQuantity"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="alignRight">
                  <s:if test="gainQuantity !=null">
					<s:if test="gainQuantity >= 0"><s:text name="format.number"><s:param value="gainQuantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="gainQuantity"></s:param></s:text></span></s:else>
				 </s:if>
				 <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <%--
                  <td class="alignRight">
                  <s:if test='price != null'>
                  	<s:text name="format.price"><s:param value="price"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  --%>
                  <td class="alignRight">
                  <s:if test="detailAmount !=null">
					<s:if test="detailAmount >= 0"><s:text name="format.price"><s:param value="detailAmount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="detailAmount"></s:param></s:text></span></s:else>
				 </s:if>
				 <s:else>
                  	&nbsp;
                  </s:else>
				  </td>
				  <%--大中小分类 --%>
                  <td class="alignRight">
                  <s:if test='PrimaryCategoryName != null && !"".equals(PrimaryCategoryName)'>
                  	<s:property value="PrimaryCategoryName"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                   <td class="alignRight">
                  <s:if test='SecondCategoryName != null && !"".equals(SecondCategoryName)'>
                  	<s:property value="SecondCategoryName"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                   <td class="alignRight">
                  <s:if test='SmallCategoryName != null && !"".equals(SmallCategoryName)'>
                  	<s:property value="SmallCategoryName"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="alignRight">
                  	<%-- 盘点处理方式 --%>
					<span><s:property value='#application.CodeTable.getVal("1020", handleType)'/></span>
                  </td>
                  <td>
                  <s:if test='reason != null && !"".equals(reason)'>
                  	<p style="max-width:200px;"><s:property value="reason"/></p>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                </tr>
              </s:if>
              <s:elseif test='"1".equals(profitKbn)'>
              	<tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>" style="background-color:<s:if test='gainQuantity < 0'>#FFF7D7</s:if><s:else>#E7E7E7</s:else>" onMouseOver="javascript:this.style.backgroundColor='#FAD163'"  onMouseOut="javascript:this.style.backgroundColor=<s:if test='gainQuantity < 0'>'#FFF7D7'</s:if><s:else>'#E7E7E7'</s:else>">
                  <td class="rowNum"> <s:property value="#R.index+1"/></td>
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
                  <s:if test="quantity !=null">
					<s:if test="quantity >= 0"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
				 </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="alignRight">
                  <s:if test='realQuantity != null'>
                  	<s:text name="format.number"><s:param value="realQuantity"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="alignRight">
                  <s:if test="gainQuantity !=null">
					<s:if test="gainQuantity >= 0"><s:text name="format.number"><s:param value="gainQuantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="gainQuantity"></s:param></s:text></span></s:else>
				 </s:if>
				 <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <%--
                  <td class="alignRight">
                  <s:if test='price != null'>
                  	<s:text name="format.price"><s:param value="price"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  --%>
                  <td class="alignRight">
                  <s:if test="detailAmount !=null">
					<s:if test="detailAmount >= 0"><s:text name="format.price"><s:param value="detailAmount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="detailAmount"></s:param></s:text></span></s:else>
				 </s:if>
				 <s:else>
                  	&nbsp;
                  </s:else>
				  </td>
				 <%--大中小分类 --%>
                  <td class="alignRight">
                  <s:if test='PrimaryCategoryName != null && !"".equals(PrimaryCategoryName)'>
                  	<s:property value="PrimaryCategoryName"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                   <td class="alignRight">
                  <s:if test='SecondCategoryName != null && !"".equals(SecondCategoryName)'>
                  	<s:property value="SecondCategoryName"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                   <td class="alignRight">
                  <s:if test='SmallCategoryName != null && !"".equals(SmallCategoryName)'>
                  	<s:property value="SmallCategoryName"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="alignRight">
                  	<%-- 盘点处理方式 --%>
					<span><s:property value='#application.CodeTable.getVal("1020", handleType)'/></span>
                  </td>
                  <td>
                  <s:if test='reason != null && !"".equals(reason)'>
                  	<p style="max-width:200px;"><s:property value="reason"/></p>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                </tr>
              </s:elseif>
              <s:else>
              	<tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>">
                  <td class="rowNum"> <s:property value="#R.index+1"/></td>
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
                  <s:if test="quantity !=null">
					<s:if test="quantity >= 0"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
				 </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="alignRight">
                  <s:if test='realQuantity != null'>
                  	<s:text name="format.number"><s:param value="realQuantity"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="alignRight">
                  <s:if test="gainQuantity !=null">
					<s:if test="gainQuantity >= 0"><s:text name="format.number"><s:param value="gainQuantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="gainQuantity"></s:param></s:text></span></s:else>
				 </s:if>
				 <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <%--
                  <td class="alignRight">
                  <s:if test='price != null'>
                  	<s:text name="format.price"><s:param value="price"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  --%>
                  <td class="alignRight">
                  <s:if test="detailAmount !=null">
					<s:if test="detailAmount >= 0"><s:text name="format.price"><s:param value="detailAmount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="detailAmount"></s:param></s:text></span></s:else>
				 </s:if>
				 <s:else>
                  	&nbsp;
                  </s:else>
				  </td>
				  <%--大中小分类 --%>
                  <td class="alignRight">
                  <s:if test='PrimaryCategoryName != null && !"".equals(PrimaryCategoryName)'>
                  	<s:property value="PrimaryCategoryName"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                   <td class="alignRight">
                  <s:if test='SecondCategoryName != null && !"".equals(SecondCategoryName)'>
                  	<s:property value="SecondCategoryName"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                   <td class="alignRight">
                  <s:if test='SmallCategoryName != null && !"".equals(SmallCategoryName)'>
                  	<s:property value="SmallCategoryName"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
				  <td class="alignRight">
                  	<%-- 盘点处理方式 --%>
					<span><s:property value='#application.CodeTable.getVal("1020", handleType)'/></span>
                  </td>
                  <td>
                  <s:if test='reason != null && !"".equals(reason)'>
                  	<p style="max-width:200px;"><s:property value="reason"/></p>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                </tr>
              </s:else>
               </s:iterator>
              </tbody>
            </table>
           	</div>
            <hr class="space" />
	            <table cellpadding="0" cellspacing="0" width="60%" border="0" class="right editable">
	              <tr>
	                <th rowspan="2" scope="row" class="center"><s:text name="PRM26_total"/></th><%-- 合计 --%>
	                 <td class="center"><s:text name="PRM26_Sumquantity"/></td><%-- 账面数量--%>
	               <td class="center"><s:text name="PRM26_SumrealQuantity"/></td><%-- 实盘数量 --%>
	                <td class="center"><s:text name="PRM26_overQuantity"/></td><%-- 盘盈数 --%>
	                <%--<td class="center"><s:text name="PRM26_overAmount"/></td>--%><%-- 盘盈金额 --%>
	                <td class="center"><s:text name="PRM26_shortQuantity"/></td><%-- 盘亏数 --%>
	                <%--<td class="center"><s:text name="PRM26_shortAmount"/></td>--%><%-- 盘亏金额 --%>
	              </tr>
	              <tr>
	             	 <td class="center">
	              		<s:text name="format.number"><s:param value="sumInfo.Sumquantity"></s:param></s:text>
	              	</td>
	              	<td class="center">
	              		<s:text name="format.number"><s:param value="sumInfo.SumrealQuantity"></s:param></s:text>
	              	</td>
	              	<td class="center">
	              		<s:text name="format.number"><s:param value="sumInfo.OverQuantity"></s:param></s:text>
	              	</td>
                    <%--
	              	<td class="center">
	              		<s:text name="format.price"><s:param value="takingInfo.overAmount"></s:param></s:text>
	              	</td>
                    --%>
	              	<td class="highlight center">
	              		<s:text name="format.number"><s:param value="sumInfo.ShortQuantity"></s:param></s:text>
	              	</td>
                    <%--
	              	<td class="highlight center">
	              		<s:text name="format.price"><s:param value="takingInfo.ShortAmount"></s:param></s:text>
	              	</td>
                    --%>
	              </tr>
	            </table>
            <hr class="space" />
            <div class="center">
              <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
              <%-- 关闭 --%>
              <span class="button-text"><s:text name="PRM26_close"/></span>
             </button>
            </div>
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