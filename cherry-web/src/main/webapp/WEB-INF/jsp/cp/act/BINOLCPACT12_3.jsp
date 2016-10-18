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
<div id="actionResultDisplay"></div>
<form id="updateForm" class="inline">
	<s:hidden name="brandInfoId"></s:hidden>
	<s:hidden name="subCampCode"></s:hidden>
	<s:hidden name="organizationId"></s:hidden>
	<%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
	<div class="main container clearfix">
		<div class="panel ui-corner-all">
		<div id="div_main">
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
		                  				<td><input class="alignRight" id="totalQuantityArr<s:property value="#status.index+1"/>" name="totalQuantityArr" value="<s:property value='totalQuantity'/>"/></td>
		                  				<td><input class="alignRight" id="safeQuantityArr<s:property value="#status.index+1"/>" name="safeQuantityArr" value="<s:property value='safeQuantity'/>"/></td>
		                				<td style="display:none">
			                                <input type="hidden" id="productVendorIDArr<s:property value="#status.index+1"/>" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
			                            </td>
		                			</tr>
	                				</s:iterator>
	              				</tbody>
	            			</table>
	           			</div>
		            <hr class="space" />
		            <div class="center">
		            	<button class="save" type="button" onclick="BINOLCPACT12_1.saveToDB();">
		                    <span class="ui-icon icon-confirm"></span>
		                    <span class="button-text"><s:text name="global.page.save"></s:text>
		                    </span>
		                </button>
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
	</div>
</form>
<div class="hide">
<s:url id="url_save" value="/cp/BINOLCPACT12_saveUpdate" />
<a id="saveUrl" href="${url_save}"></a>
</div>
</s:i18n>