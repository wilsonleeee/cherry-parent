<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS06.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript">
	// 节日
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
<%-- 调拨记录查询URL --%>
<s:url id="search_url" value="/pt/BINOLPTRPS06_search"/>
<input type="hidden" value="${search_url}" id="search_url"/>
<s:i18n name="i18n.pt.BINOLPTRPS06">
<div id="selectAll" class="hide"><s:text name="global.page.all"/></div>
<s:text name="global.page.all" id="RPS06_selectAll"/>
<div class="panel-header">
     	<%-- ###调拨记录查询 --%>
       <div class="clearfix"> 
       	<span class="breadcrumb left"> 
       		<span class="ui-icon icon-breadcrumb"></span>
       		<s:text name="RPS06_breadcrumb"/>&gt; <s:text name="RPS06_title"/> 
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
           			<span class="ui-icon icon-ttl-search"></span>
           			<s:text name="global.page.condition"/>
           		</strong>
               </div>
		<div class="box-content clearfix">
		<div class="column" style="width: 50%;height:55px;">
		<p><%-- 调拨单号 --%> <label><s:text name="RPS06_allocationNo" /></label>
		<s:textfield name="allocationNo" cssClass="text" /></p>
		<p><%-- 产品名称 --%>
            <label><s:text name="RPS06_productName"/></label>
                <s:textfield name="nameTotal" cssClass="text" maxlength="30"/>
	            <input type="hidden" id="prtVendorId" name="prtVendorId" value="" />
        </p>
		</div>
		<div class="column last" style="width: 49%;height:55px;">
		<p class="date"><%-- 日期范围 --%> <label><s:text
			name="RPS06_date" /></label> <span><s:textfield id="startDate"
			name="startDate" cssClass="date" /></span> - <span><s:textfield
			id="endDate" name="endDate" cssClass="date" /></span></p>
		<p><%-- 审核状态 --%> <label><s:text name="RPS06_verifiedFlag" /></label>
		<s:select name="verifiedFlag"
			list="#application.CodeTable.getCodes('1007')" listKey="CodeKey"
			listValue="Value" headerKey="" headerValue="%{RPS06_selectAll}" /></p>
		</div>
		<%-- ======================= 组织联动共通导入开始  ============================= --%>
			<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
				<s:param name="businessType">1</s:param>
 				<s:param name="operationType">1</s:param>
			</s:action>
		<%-- ======================= 组织联动共通导入结束  ============================= --%>
		</div>
		<p class="clearfix">
             		<%-- <cherry:show domId="SSPRO0732QUERY">--%>
             			<%-- 查询 --%>
             			<button class="right search" type="button" onclick="search('<s:property value="#search_url"/>')">
             				<span class="ui-icon icon-search-big"></span>
             				<span class="button-text"><s:text name="global.page.search"/></span>
             			</button>
             	<%-- 	</cherry:show>--%>
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
<%--
           		<span class="left">
           			<cherry:show domId="SSPRO0732EXPOT">
            			<%-- 导出 --%
              			<a class="export">
              				<span class="ui-icon icon-export"></span>
              				<span class="button-text"><s:text name="global.page.export"/></span>
              			</a> 
              		</cherry:show>
            		</span> 
--%>
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
	                  	<th><s:text name="RPS06_num"/></th>
	                  	<%-- 调拨单号 --%>
	                  	<th><s:text name="RPS06_allocationNo"/><span class="ui-icon ui-icon-document"></span></th>
	                  	<%-- 调拨申请部门   --%>
	                  	<th><s:text name="RPS06_sendOrg"/></th>
	                  	<%-- 调拨接受部门 --%>
	                  	<th><s:text name="RPS06_receiveOrg"/></th>
	                  	<%-- 总数量 --%>
	                  	<th><s:text name="RPS06_totalQuantity"/></th>
	                  	<%-- 总金额  --%>
	                  	<th><s:text name="RPS06_totalAmount"/></th>
	                  	<%-- 调拨日期 --%>
	                  	<th><s:text name="RPS06_date"/></th>
	                  	<%-- 审核状态 --%>
	                  	<th><s:text name="RPS06_verifiedFlag"/></th>
	                  	<%-- 操作员 --%>
	                  	<th><s:text name="RPS06_employeeName"/></th>
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
<%-- ============ 弹出dataTable 产品共通导入 START ================= --%>
<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />
<%-- ============ 弹出dataTable 产品共通导入 END =================== --%>