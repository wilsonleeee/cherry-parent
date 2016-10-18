<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS09.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	//节日
	var holidays = '${holidays }';
	$('#startDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#endDate').val();
			return [value,'maxDate'];
		}
	});
	$('#endDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#startDate').val();
			return [value,'minDate'];
		}
	});
</script>
<%--出入库记录查询URL --%>
<s:url id="search_url" value="/pt/BINOLPTRPS09_search"/>
<input type="hidden" value="${search_url}" id="search_url"/>
<div class="hide">
<s:url id="exportUrl" action="BINOLPTRPS09_export" ></s:url>
<a id="exportUrl" href="${exportUrl}"></a>
<s:url id="exportCsvUrl" action="BINOLPTRPS09_exportCsv" ></s:url>
<a id="exportCsvUrl" href="${exportCsvUrl}"></a>
<s:url id="exporChecktUrl" action="BINOLPTRPS09_exportCheck" ></s:url>
<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
</div>
<s:i18n name="i18n.pt.BINOLPTRPS09">
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
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>	
            </div>
            <div class="box-content clearfix">
               	<div class="column" style="width:50%;height: 85px;">
	                <p>
	               		<%-- 单据号 --%>
	                  	<label><s:text name="RPS09_tradeNo"/></label>
	                  	<s:textfield name="tradeNo" cssClass="text" onblur="ignoreCondition(this);return false;"/>
	                </p>
	                <p>
	               		<%-- 关联单号 --%>
	                  	<label><s:text name="RPS09_relevanceNo"/></label>
	                  	<s:textfield name="relevanceNo" cssClass="text" onblur="ignoreCondition(this);return false;"/>
	                </p>
	                 <p>
	               		<%-- 产品名称 --%>
	                  	<label><s:text name="RPS09_prtName"/></label>
	                  	<s:textfield name="nameTotal" cssClass="text"/>
	                  	<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>
	                </p>
        		</div>
        		<div class="column last" style="width:49%;height: 85px;">
        		 	<p  id="dateCover" class="date">
	                	<%-- 日期范围 --%>
	                  	<label><s:text name="RPS09_date"/></label>
	                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
	                  	- 
	                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
	                </p>
	                <p>
	                	<%-- 业务类型 --%>
	                  	<label><s:text name="RPS09_tradeType"/></label>
	                  	<s:text name="global.page.all" id="RPS09_selectAll"/>
	                  	<s:select name="tradeType" list="#application.CodeTable.getCodes('1263')"  onchange="setValue(this);return false;"
               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS09_selectAll}" value="'CA'"/>
	                </p>
	                <p>
	               		<%-- 审核状态 --%>
	                  	<label><s:text name="RPS09_verifiedFlag"/></label>
	                  	<s:select name="verifiedFlag" list="#application.CodeTable.getCodes('1007')"
               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS09_selectAll}"/>
	                </p>        
	            </div>
	          <%-- ======================= 组织联动共通导入开始  ============================= --%>
           	  <s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
           	  		<s:param name="showLgcDepot">1</s:param>
           	  		<s:param name="businessType">1</s:param>
           	  		<s:param name="operationType">1</s:param>
           	  		<s:param name="mode">dpat,area,chan,dpot</s:param>
           	  </s:action>
           	  <%-- ======================= 组织联动共通导入结束  ============================= --%>
              </div>
              <p class="clearfix">
           			<%-- 查询 --%>
           			<button class="right search" type="button" onclick="search('<s:property value="#search_url"/>')">
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
           		<cherry:show domId="BINOLPTRPS09EXP">
	           		<span style="margin-right:10px;">
	                   <a id="export" class="export" onclick="ptrps09_exportExcel();return false;">
	                      <span class="ui-icon icon-export"></span>
	                      <span class="button-text"><s:text name="global.page.exportExcel"/></span>
	                   </a>
	                   <a id="export" class="export" onclick="ptrps09_exportCsv();return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="global.page.exportCsv"/></span>
				       </a>
	                </span>
	            </cherry:show>
           		<span id="headInfo" style=""></span>
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
	                  	<th><s:text name="RPS09_num"/></th>
	                  	<%-- 单据号 --%>
	                  	<th><s:text name="RPS09_tradeNo"/><span class="ui-icon ui-icon-document"></span></th>
	                  	<%-- 部门 --%>
	                  	<th><s:text name="RPS09_departName"/></th>
	                  	<%-- 业务类型   --%>
	                  	<th><s:text name="RPS09_tradeType"/></th>
	                  	<%-- 入出库状态  --%>
	                  	<th><s:text name="RPS09_stockType"/></th>
	                  	<%-- 总数量 --%>
	                  	<th><s:text name="RPS09_totalQuantity"/></th>
	                  	<%-- 总金额  --%>
	                  	<th><s:text name="RPS09_totalAmount"/></th>
	                  	<%-- 时间 --%>
	                  	<th><s:text name="RPS09_time"/></th>
	                  	<%-- 审核状态 --%>
	                  	<th><s:text name="RPS09_verifiedFlag"/></th>
	                  	<%-- 操作员 --%>
	                  	<th><s:text name="RPS09_employeeName"/></th>
	                </tr>
              	</thead>
            </table>
         	</div>
       </div>
       <%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<%-- ===================隐藏所要保存的值=============================== --%>
<input type="hidden"  id="defTradeType" value='CA'/>
<input type="hidden"  id="defStartDate" value=''/>
<input type="hidden"  id="defEndDate"	value=''/>   
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== Csv导出弹出框共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<%-- ================== Csv导出弹出框共通导入  END ======================= --%>