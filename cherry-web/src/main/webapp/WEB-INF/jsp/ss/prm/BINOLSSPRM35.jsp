<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM35.js"></script>
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
	//search("BINOLSSPRM35_search");
</script>
<%--出入库记录查询URL --%>
<s:url id="search_url" value="/ss/BINOLSSPRM35_search"/>
<div class="hide">
<s:url id="exportUrl" action="BINOLSSPRM35_export" ></s:url>
<a id="exportUrl" href="${exportUrl}"></a>
<s:url id="exportCsvUrl" action="BINOLSSPRM35_exportCsv" ></s:url>
<a id="exportCsvUrl" href="${exportCsvUrl}"></a>
<s:url id="exporChecktUrl" action="BINOLSSPRM35_exportCheck" ></s:url>
<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
</div>
<s:i18n name="i18n.ss.BINOLSSPRM35">
<div id="PRM35_select" class="hide"><s:text name="global.page.all"/></div>
<div class="panel-header">
     	<%-- ###出入库记录查询 --%>
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
               	<div class="column" style="width:49%; height: 85px;">
	                <p>
	               		<%-- 单据号 --%>
	                  	<label><s:text name="PRM35_tradeNo"/></label>
	                  	<s:textfield name="tradeNo" cssClass="text"  onblur="ignoreCondition(this);return false;"/>
	                </p>
	                <p>
	               		<%-- 关联单号 --%>
	                  	<label><s:text name="PRM35_relevantNo"/></label>
	                  	<s:textfield name="relevantNo" cssClass="text"  onblur="ignoreCondition(this);return false;"/>
	                </p>
	                <p>
	               		<%-- 产品名称 --%>
	                  	<label class="left"><s:text name="PRM35_prmName"/></label>
	                  	<%--<input type="text" class="text" id="nameTotal"/>--%>
	                  	<%--<input type="hidden" id="prmVendorId" name="prmVendorId" value=""/>--%>
	                  	<input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
	                    <table class="all_clean left"><tbody id="promotion_ID"></tbody></table>
	                    <a class="add" onclick="BINOLSSPRM35.openPrmPopup();">
		                    <span class="ui-icon icon-search"></span>
		                    <span class="button-text"><s:text name="global.page.Popselect"/></span>
	                    </a>
	                </p>
        		</div>
        		<div class="column last" style="width:50%; height: 85px;">
        			<p id="dateCover" class="date">
	                	<%-- 日期范围 --%>
	                  	<label><s:text name="PRM35_date"/></label>
	                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
	                  	- 
	                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
	                </p>
	                <p>
	                	<%-- 业务类型 --%>
	                  	<label><s:text name="PRM35_tradeType"/></label>
	                  	<s:text name="global.page.all" id="PRM35_selectAll"/>
	                  	<s:select name="tradeType" list="#application.CodeTable.getCodes('1026')" onchange="setValue(this);return false;"
               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM35_selectAll}" value="'P'"/>
	                </p>
	                <p>
	               		<%-- 审核状态 --%>
	                  	<label><s:text name="PRM35_verifiedFlag"/></label>
	                  	<s:select name="verifiedFlag" list="#application.CodeTable.getCodes('1007')"
               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM35_selectAll}"/>
	                </p>   
	            </div>
	            <%-- ======================= 组织联动共通导入开始  ============================= --%>
           	  	<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
           	  		<s:param name="flag">1</s:param>
           	  		<s:param name="showLgcDepot">1</s:param>
           	  		<s:param name="businessType">1</s:param>
           	  		<s:param name="operationType">1</s:param>
           	  	</s:action>
           	  	<%-- ======================= 组织联动共通导入结束  ============================= --%>
              </div>
              <p class="clearfix">
           	<%--	<cherry:show domId="SSPRM0735QUERY">--%>
           			<%-- 查询 --%>
           			<button class="right search" type="button" onclick="search('<s:property value="#search_url"/>')">
           				<span class="ui-icon icon-search-big"></span>
           				<span class="button-text"><s:text name="global.page.search"/></span>
           			</button>
           	<%--	</cherry:show>--%>
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
           		<cherry:show domId="BINOLSSPRM35EXP">
	           		<span style="margin-right:10px;">
	                   <a id="export" class="export" onclick="ssprm35_exportExcel();return false;">
	                      <span class="ui-icon icon-export"></span>
	                      <span class="button-text"><s:text name="global.page.exportExcel"/></span>
	                   </a>
	                   <a id="export" class="export" onclick="ssprm35_exportCsv();return false;">
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
	                  	<th><s:text name="PRM35_num"/></th>
	                  	<%-- 单据号 --%>
	                  	<th><s:text name="PRM35_tradeNo"/><span class="ui-icon ui-icon-document"></span></th>
	                  	<%-- 部门 --%>
	                  	<th><s:text name="PRM35_departName"/></th>
	                  	<%-- 业务类型   --%>
	                  	<th><s:text name="PRM35_tradeType"/></th>
	                  	<%-- 出入库状态 --%>
	                  	<th><s:text name="PRM35_stockType"/></th>
	                  	<%-- 总数量 --%>
	                  	<th><s:text name="PRM35_totalQuantity"/></th>
	                  	<%-- 总金额  --%>
	                  	<th><s:text name="PRM35_totalAmount"/></th>
	                  	<%-- 日期 --%>
	                  	<th><s:text name="PRM35_date"/></th>
	                  	<%-- 审核状态 --%>
	                  	<th><s:text name="PRM35_verifiedFlag"/></th>
	                  	<%-- 操作员 --%>
	                  	<th><s:text name="PRM35_employeeName"/></th>
	                </tr>
              	</thead>
            </table>
         	</div>
       </div>
       <%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<%-- ===================隐藏所要保存的值=============================== --%>
<input type="hidden"  id="defTradeType" value='P'/>
<input type="hidden"  id="defStartDate" value=''/>
<input type="hidden"  id="defEndDate"	value=''/>
<%-- ============ 弹出dataTable 促销产品共通导入 START ================= --%>
<%--<jsp:include page="/WEB-INF/jsp/common/prmProductTable.jsp" flush="true" />--%>
<%-- ============ 弹出dataTable 促销产品共通导入 END =================== --%>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== Csv导出弹出框共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<%-- ================== Csv导出弹出框共通导入  END ======================= --%>