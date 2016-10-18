<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS42.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript">
	<%--节日 --%>
	var holidays = '${holidays }';
	$('#prePayStartDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#prePayEndDate').val();
			return [value,'maxDate'];
		}
	});
	$('#prePayEndDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#prePayStartDate').val();
			return [value,'minDate'];
		}
	});
	
	$('#pickUpStartDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#pickUpEndDate').val();
			return [value,'maxDate'];
		}
	});
	$('#pickUpEndDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#pickUpStartDate').val();
			return [value,'minDate'];
		}
	});

	cherryValidate({			
		formId: "mainForm",		
		rules: {
			prePayStartDate: {dateValid:true},	// 预付单开始日期
			prePayEndDate: {dateValid:true},	// 预付单结束日期
			pickUpStartDate: {dateValid:true},	// 提货单开始日期
			pickUpEndDate: {dateValid:true}	// 提货单结束日期
		}		
	});

	
</script>
<style>
	#DEPARTLINE{
		margin-left: 90px;
		margin-right: 5px;
		margin-bottom: 5px;
	}
	#RANGELABLE{
		width: 65px; 
		float:left;
		margin-top: 4px;
		text-align: right;
		padding-left:15px;
	}
	#DEPARTLINE span{
		margin-right: 2px;
	}
</style>

<s:url id="search_url" value="/pt/BINOLPTRPS42_search"/>
<%-- 商品详细URL --%>
<s:url id="getSaleProPrmSum_url" value="/pt/BINOLPTRPS42_getSaleProPrmSum"/>
<%-- EXCEL导出URL --%>
<s:url id="xls_url" value="/pt/BINOLPTRPS42_export"/>
<s:i18n name="i18n.pt.BINOLPTRPS42">
<div class="hide">
	<div id="dialogInitMessage"><s:text name="dialog_init_message" /></div> <%-- 初始化中... --%>
</div>
<div id="RPS42_select" class="hide"><s:text name="global.page.all" /></div>
<div class="panel-header">
     	<%-- ###销售记录查询 --%>
       <div class="clearfix"> 
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>  
       </div>
</div>

<input type="hidden" id="yes" value='<s:text name="RPS42_yes"/>'/>
<input type="hidden" id="dialogTitle" value='<s:text name="RPS42_dialogTitle"/>'/>

<%-- ================== 错误信息提示 START ======================= --%>
<div id="errorMessage"></div>
<%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline">
			<input name="brandInfoId" id="brandInfoId" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>
            </div>
            <div class="box-content clearfix">
		      <div style="padding: 15px 0px 5px;">
		      	<table class="detail" cellpadding="0" cellspacing="0" id="rps42SearchId">
				  <tbody>
		            <tr>
		              <%--预付单编号 --%>
		              <th><s:text name="RPS42_prePayNo" /></th>
		              <td>
		                <span><s:textfield name="prePayNo" cssClass="text" maxlength="35" onblur="ignoreCondition(this);return false;"/></span> 
		                <!-- <span><s:textfield name="billCode" cssClass="text" maxlength="35"/></span> --> 
		              </td>
		              <%--预付时间 --%>
		              <th><s:text name="RPS42_prePayDate" /></th>
		              <td id="dateCover">
		                <span><s:textfield id="prePayStartDate" name="prePayStartDate" cssClass="date" />
		                -<s:textfield id="prePayEndDate" name="prePayEndDate" cssClass="date" /></span>
		              </td>
		            </tr>
		            <tr>		             
		               <%--预付金额 --%>
		              <th><s:text name="RPS42_prePayAmount" /></th>
		              <td>
		                <span><s:textfield name="prePayAmountStart"  maxlength="10" />-<s:textfield name="prePayAmountEnd"  maxlength="10" /></span> 
		              </td>
		              <%--预留手机号 --%>
		              <th><s:text name="RPS42_phoneNo" /></th>
		              <td>
		                <span><s:textfield name="phoneNo" cssClass="text" maxlength="35" /></span> 
		              </td>
		            </tr>
		            <tr>
		              <%--下次提货时间 --%>
		              <th><s:text name="RPS42_pickUpDate" /></th>
		              <td>
		                <span><s:textfield id="pickUpStartDate" name="pickUpStartDate" cssClass="date" />
		                -<s:textfield id="pickUpEndDate" name="pickUpEndDate" cssClass="date" onblur="ignoreCondition(this);return false;"/></span>
		             	<span><input type="checkbox" id="includeNoPickUpTime" name="includeNoPickUpTime" value="1"><s:text name="RPS42_excludeNoTime" /></span>
		              </td>
		              <th></th>
		              <td></td>              
		            </tr>		            		          		            		            					
		          </tbody>
		        </table>
		      </div>

		          <%-- ======================= 组织联动共通导入开始  ============================= --%>
		        	  	<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
		         	  	<s:param name="showType">0</s:param>
		         	  	<s:param name="businessType">3</s:param>
		      			<s:param name="operationType">1</s:param>
		      			<s:param name="mode">dpat,area,chan</s:param>
		        	  	</s:action>
		         <%-- ======================= 组织联动共通导入结束  ============================= --%>
        	  	</div> 
        	  	<p class="clearfix">
        		<%--<cherry:show domId="SSPRO0639QUERY">--%>
        			<%-- 查询 --%>
        			<button class="right search" type="button" onclick="binOLPTRSP42_search('<s:property value="#search_url"/>')">
        				<span class="ui-icon icon-search-big"></span>
        				<span class="button-text"><s:text name="global.page.search"/></span>
        			</button>
        		<%--</cherry:show>--%> 
        		</p> 
        	</cherry:form>  
        	</div>
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section hide">
		<div class="section-header">
			<strong> 
			<span class="ui-icon icon-ttl-section-search-result"></span> 
			<s:text name="global.page.list" />
		 	</strong>
		</div>
		<div class="section-content">
		  <div class="toolbar clearfix">
            <span style="margin-right:10px;">
		        <cherry:show domId="BINOLPTRPS42EXP">
                   <a id="export" class="export" onclick="binOLPTRSP42_exportExcel('${xls_url}');return false;">
                      <span class="ui-icon icon-export"></span>
                      <span class="button-text"><s:text name="global.page.exportExcel"/></span>
                   </a>
                </cherry:show>                
             </span>
         
	         
			<span id="headInfo" ></span>
			<span class="right"> <%-- 设置列 --%>
				<a class="setting"> 
					<span class="ui-icon icon-setting"></span> 
					<span class="button-text"><s:text name="global.page.colSetting" /></span> 
				</a>
			</span>
		  </div>
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0"
		class="jquery_table" width="100%">
		  <thead>
			<tr>
				<%-- No. --%>
				<th>
				<s:text name="RPS42_num"/><%-- <s:text name="No." /> --%>
				</th>
				<%-- 预留手机号 --%>
				<th><s:text name="RPS42_prePayNo" /></th>
				<%-- 预付单编号 --%>
				<th><s:text name="RPS42_phoneNo" /></th>
				<%-- 预付柜台--%>
		        <th><s:text name="RPS42_departName" /></th>
				<%-- 预付时间    --%>
				<th><s:text name="RPS42_prePayDate" /></th>
				<%-- 交易类型   --%>
				<th><s:text name="RPS42_transactionType" /></th>
				<%-- 预付金额   --%>
				<th><s:text name="RPS42_prePayAmount" /></th>			
				<%-- 购买数量   --%>
				<th><s:text name="RPS42_buyQuantity" /></th>
				<%-- 剩余数量   --%>
				<th><s:text name="RPS42_leftQuantity" /></th>
				<%-- 已提单数 --%>
				<th><s:text name="RPS42_pickupTimes" /></th>
				<%-- 下次提货时间  --%>
				<th><s:text name="RPS42_pickUpDate" /></th>
			</tr>
	      </thead>
			<tbody id="databody"></tbody>
		</table>
		</div>
	</div> 
	<div class="hide">
		<div id="deleteButton"><s:text name="global.page.delete" /></div><%-- 商品列表删除按钮 --%>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<input type="hidden"  id="defStartDate" value=''/>
<input type="hidden"  id="defEndDate"	value=''/>   
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<span id="search" style="display:none">${search_url}</span>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<div class="hide">
<s:url id="exporChecktUrl" action="BINOLPTRPS42_exportCheck" ></s:url>
<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
</div>
