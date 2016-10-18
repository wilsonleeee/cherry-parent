<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS38.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<%-- 取得部门URL --%>
<s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart"/>
<%-- 取得仓库URL --%>
<s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory"/>
<%-- 库存记录查询URL --%>
<s:url id="search_url" value="/pt/BINOLPTRPS38_search"/>
<%-- 一览明细导出URL --%>
<s:url id="xls_url" value="/pt/BINOLPTRPS38_export"/>
<%-- 一览概要导出URL --%>
<s:url id="summaryXls_url" value="/pt/BINOLPTRPS38_exportSummary"/>

<input type="hidden" value="${search_url}" id="search_url"/>
<s:i18n name="i18n.pt.BINOLPTRPS38">
<s:text id="selectAll" name="global.page.all"/>
<div class="panel-header">
       <div class="clearfix"> 
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>  
       </div>
</div>
<%-- ================== 错误信息提示 START ======================= --%>
<div id="errorMessage"></div>
<%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline">
			<div class="box-header">
				<%-- 查询条件  --%>
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span>
           			<s:text name="global.page.condition"/>
           		</strong>
               </div>
               <div class="box-content clearfix">
               	<div class="column" style="width:50%;height: 65px;">
	                <p>
	                	<%-- 产品名称 --%>
	                  	<label><s:text name="RPS38_nameTotal"/></label>
	                  	<s:textfield name="nameTotal" cssClass="text"/>
	                  	<s:if test='"2".equals(type)'><input type="hidden" id="productId" name="productId" value=""/></s:if>
	                  	<s:else><input type="hidden" id="prtVendorId" name="prtVendorId" value=""/></s:else>
	                  	<input type="hidden" id="groupType" name="type" value="<s:property value='type'/>"/>
	                </p>
	                <p>
	                	<%-- 产品有效状态 --%>
	                  	<label><s:text name="RPS38_validFlag"/></label>
	                  	<s:iterator value='#application.CodeTable.getCodes("1137")'>
		                	<input type="radio" name="validFlag" value='<s:property value="CodeKey" />' <s:if test="1 == CodeKey">checked</s:if>/><s:property value="Value" />
		                </s:iterator>
	                </p>
        		</div>
	            <%-- ======================= 组织联动共通导入开始  ============================= --%>
           	  	<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
           	  		<s:param name="showLgcDepot">1</s:param>
           	  		<s:param name="businessType">1</s:param>
           	  		<s:param name="operationType">1</s:param>
           	 	</s:action>
           	  	<%-- ======================= 组织联动共通导入结束  ============================= --%>
               </div>
               <p class="clearfix">
        			<%-- 查询 --%>
        			<button class="right search" type="button" onclick="search('${search_url}')">
        				<span class="ui-icon icon-search-big"></span>
        				<span class="button-text"><s:text name="global.page.search"/></span>
        			</button>
           	   </p>
		</cherry:form>
	</div>
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section hide">
       	<div class="section-header">
       		<strong>
       			<span class="ui-icon icon-ttl-section-search-result"></span>
       			<s:text name="global.page.list"/>
      		</strong>
       	</div>
        <div class="section-content">
           	<div class="toolbar clearfix">
           		<span style="margin-right:10px;">
	                <!-- 一览概要导出 -->
	                <cherry:show domId="BINOLPTRPS38EXP">
	                    <a id="export" class="export" onclick="exportSummaryExcel('${summaryXls_url}');return false;">
	                        <span class="ui-icon icon-export"></span>
	                        <span class="button-text"><s:text name="global.page.exportExcel01"/></span>
	                    </a>
                	</cherry:show>
                </span>
           		<span class="right">
           			<%-- 设置列 --%>
           			<a class="setting">
           				<span class="ui-icon icon-setting"></span>
           				<span class="button-text"><s:text name="global.page.colSetting"/></span>
           			</a>
           		</span>
           	</div>
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              	<thead>
	                <tr>
	                	<%-- No. --%>
	                  	<th><s:text name="RPS38_num"/></th>
	                  	<%-- 产品名称 --%>
	                  	<th><s:text name="RPS38_nameTotal"/><span class="ui-icon ui-icon-document"></span></th>
	                  	<%-- 厂商编码 --%>
	                  	<th><s:text name="RPS38_unitCode"/></th>
	                  	<%-- 产品条码 --%>
	                  	<th><s:text name="RPS38_barCode"/></th>
	                  	<%-- 品牌	 --%>
<%-- 	                  	<th><s:text name="RPS38_originalBrand"/></th> --%>
	                  	<%-- 计量单位 --%>
	                  	<th><s:text name="RPS38_moduleCode"/></th>
                        <%-- 销售价格 --%>
                        <th><s:text name="RPS38_price"/></th>
                        <%-- 库存 --%>
                        <th><s:text name="RPS38_quantity"/></th>
	                </tr>
              	</thead>
            </table>
         	</div>
       </div>
       <%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
