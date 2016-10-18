<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS02.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<s:i18n name="i18n.pt.BINOLPTRPS02">
	<s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart"/>
	<s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory"/>
	<s:url id="search_url" value="/pt/BINOLPTRPS02_search"/>
	<s:url id="delete_url" action="BINOLPTRPS02_delete"/>
	<s:url id="send_url" action="BINOLPTRPS02_send"/>
	<s:text name="RPS02_selectAll" id="RPS02_selectAll"/>
	<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
		<s:text name="RPS02_select" id="defVal"/>
		<div id="RPS02_select">${defVal}</div>
	</div>
	      <div class="panel-header">
	      	<%-- 发货单查询 --%>
	        <div class="clearfix"> 
	        	<span class="breadcrumb left"> 
	        		<span class="ui-icon icon-breadcrumb"></span>
	        		<%--仓库管理--%>
	        		<s:text name="RPS02_title2"/>
	        		<a> &gt; 
	        		<%-- 发货单查询 --%>
	        		<s:text name="RPS02_title3"/></a> 
	        	</span>  
	        </div>
	      </div>
	      <div id="errorMessage"></div>
	      <div id="actionResultDisplay"></div>
	      <div class="panel-content">
	        <div class="box">
	          <cherry:form id="mainForm" class="inline">
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLPTRPS02"/>
	            <div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="RPS02_condition"/>
	            </strong>
	               </div>
	            <div class="box-content clearfix">
	            <%-- 总部或者办事处用户登录 --%>
	              <div class="column" style="width:50%;height:55px;">
	                <p>
	               	<%-- 发货单号 --%>
	                  <label><s:text name="RPS02_deliverRecNo"/></label>
	                  <s:textfield name="deliverRecNo" cssClass="text" maxlength="30" />
	                </p>
	                <p>
                        <%-- 产品名称 --%>
                        <label><s:text name="RPS02_productName"/></label>
                        <s:textfield name="nameTotal" cssClass="text" maxlength="30"/>
	                  	<input type="hidden" id="prtVendorId" name="prtVendorId" value="" />
                     </p>
	              </div>
	              <div class="column last" style="width:49%;height:55px;">
	              <p class="date">
	                <%-- 发货日期 --%>
	                  <label><s:text name="RPS02_deliverDate"/></label>
	                  <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
	                </p>
	              	 <p>
	               	<%-- 审核状态 --%>
	                  <label><s:text name="RPS02_verifiedFlag"/></label>
	                 <s:select name="verifiedFlag" list='#application.CodeTable.getCodes("1007")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS02_selectAll}"/>
	                 </p> 
	              </div>
					<%-- ======================= 组织联动共通导入开始  ============================= --%>
					<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
						<s:param name="businessType">1</s:param>
 						<s:param name="operationType">1</s:param>
						</s:action>
					<%-- ======================= 组织联动共通导入结束  ============================= --%>
				</div>
	            <p class="clearfix">
	                <%-- 查询 --%>
	              	<button class="right search"  onclick="search();return false">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="RPS02_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <div id="section" class="section hide">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<%-- 查询结果一览 --%>
	          	<s:text name="RPS02_results_list"/>
	         </strong>
	        </div>
	          <div class="section-content" id="result_list">
	            <div class="toolbar clearfix">
	            <span id="headInfo" style=""></span>
	            <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
	            <span class="button-text">
	             <%-- 设置列 --%>
	             <s:text name="RPS02_colSetting"/>
	             </span></a></span></div>
	          </div>
	        </div>
	      </div>
	      <div id="result_table" class="hide">
	      	<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	              <thead>
	                <tr>	               
	                  <th><s:text name="RPS02_num"/></th><%-- No. --%>
	                  <th><s:text name="RPS02_deliverRecNo"/><span class="ui-icon ui-icon-document"></span></th><%-- 发货单号 --%>
	                  <th><s:text name="RPS02_departNameDeliver"/></th><%-- 发货部门  --%>
	                  <th><s:text name="RPS02_departNameReceive"/></th><%-- 收货部门 --%>
	                  <th><s:text name="RPS02_totalQuantity"/></th><%-- 总数量 --%>
	                  <th><s:text name="RPS02_totalAmount"/></th><%-- 总金额 --%>
	                  <th><s:text name="RPS02_deliverDate"/></th><%-- 发货日期 --%>
	                  <th><s:text name="RPS02_verifiedFlag"/></th><%-- 审核状态 --%>
	                </tr>
	              </thead>
	            </table>
	      </div>
	 </s:i18n>
	    <%-- ================== dataTable共通导入 START ======================= --%>
		<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
		<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ============ 弹出dataTable 产品共通导入 START ================= --%>
<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />
<%-- ============ 弹出dataTable 产品共通导入 END =================== --%>
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