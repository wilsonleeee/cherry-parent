<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS37.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>

<%-- 实时库存预警记录查询URL --%>
<s:url id="search_url" value="/pt/BINOLPTRPS37_search"/>
<%-- 一览明细导出URL --%>
<s:url id="xls_url" value="/pt/BINOLPTRPS37_export"/>
<input type="hidden" value="${search_url}" id="search_url"/>

<s:i18n name="i18n.pt.BINOLPTRPS37">
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
	            <div class="column last" style="width:100%;">
	            	<p>
	                	<%-- 产品名称 --%>
	                  	<label><s:text name="RPS37_productName"/></label>
	                  	<s:textfield id="nameTotal" name="nameTotal" cssClass="text"/>
	                  	<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>
	                </p>
	                <div style="margin:0;" class="box2 box2-active">
	                    <div class="box2-header clearfix">
	                        <strong class="left active">
	                            <s:text name="RPS37_rule"></s:text>
	                        </strong>
	                    </div>
	                    <div style="padding:.5em .5em 0 .5em;" class="box2-content clearfix">
	                        <div id="divRule">
	                            <div id="divAmount" style="float:left;width:100%;">
	                                <p>
	                                    <%-- 库存数量>= --%>
	                                    <span>
	                                    <label><s:text name="RPS37_minLimit"/></label>
	                                    <s:textfield name="minLimit" cssClass="text" maxlength="9"/>
	                                    </span>
	                                </p>
	                            </div>
	                        </div>
	                    </div>
	                </div>
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
        			<button class="right search" type="button" onclick="BINOLPTRPS37.search('<s:property value="#search_url"/>')">
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
           			<cherry:show domId="BINOLPTRPS37EXP">
	           			<!-- 一览明细导出 -->
	                    <a id="export" class="export" onclick="BINOLPTRPS37.exportExcel('${xls_url}');return false;">
	                        <span class="ui-icon icon-export"></span>
	                        <span class="button-text"><s:text name="global.page.exportExcel"/></span>
	                    </a>
	                    <a id="export" class="export" onclick="BINOLPTRPS37.exportCsv();return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="global.page.exportCsv"/></span>
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
	                  	<th><s:text name="RPS37_num"/></th>
	                  	<%-- 所属部门 --%>
                        <th><s:text name="RPS37_departName"/></th>
                        <%-- 实体仓库 --%>
                        <th><s:text name="RPS37_depotName"/></th>
	                  	<%-- 逻辑仓库 --%>
	                  	<th><s:text name="RPS37_logicDepotName"/></th>
	                  	<%-- 产品名称 --%>
	                  	<th><s:text name="RPS37_productName"/></th>
	                  	<%-- 厂商编码 --%>
	                  	<th><s:text name="RPS37_unitCode"/></th>
	                  	<%-- 产品条码 --%>
	                  	<th><s:text name="RPS37_barCode"/></th>
	                  	<%-- 计量单位 --%>
	                  	<th><s:text name="RPS37_moduleCode"/></th>
                        <%-- 库存数量 --%>
                        <th><s:text name="RPS37_stockQuantity"/></th>
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
<div class="hide">
<s:url id="exportCsvUrl" action="BINOLPTRPS37_exportCsv" ></s:url>
<a id="exportCsvUrl" href="${exportCsvUrl}"></a>
<s:url id="exporChecktUrl" action="BINOLPTRPS37_exportCheck" ></s:url>
<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
</div>