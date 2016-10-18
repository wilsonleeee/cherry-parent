<%-- 产品发货单一览 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH04.js"></script>
<style>
div#DEPART_DIV hr {display:none;}
</style>
<s:i18n name="i18n.st.BINOLSTSFH04">
	<s:url id="search_url" value="/st/BINOLSTSFH04_search"/>
	<s:url id="export_url" action="BINOLSTSFH04_export"/>
	<s:text name="SFH04_selectAll" id="SFH04_selectAll"/>
	<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
	</div>
	     <div class="panel-header">
	      	<%-- 发货单一览 --%>
	        <div class="clearfix"> 
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
	        </div> 
	      </div>
	     <%-- ================== 错误信息提示 START ======================= --%>
   			 <div id="errorMessage"></div>    
   			 <div style="display: none" id="errorMessageTemp">
   			 <div class="actionError">
    		</div>
    	</div>
   		 <%-- ================== 错误信息提示   END  ======================= --%>
	      <div id="actionResultDisplay"></div>
	      <div class="panel-content">
	        <div class="box">
	          <cherry:form id="mainForm" class="inline">
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLSTSFH04"/>
	            <div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="SFH04_condition"/>
	            </strong>
	               </div>
	            <div class="box-content clearfix">
	               <div>
	              <div class="column" style="width:50%;height:120px;">
		            <p>
	               	<%-- 发货单编号 --%>
	                  <label><s:text name="SFH04_deliverNo"/></label>
	                  <s:textfield name="deliverNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
	                </p>
	                <p>
	               	<%-- 订货单号（关联单号） --%>
	                  <label><s:text name="SFH04_orderNo"/></label>
	                  <s:textfield name="relevanceNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
	                </p>
	                <p>
                    	<%--导入批次 --%>
                        <label><s:text name="SFH04_importBatch"/></label>
                        <s:textfield name="importBatch" cssClass="text" maxlength="25"/>
                    </p>
                     <p>
                        <%-- 产品名称 --%>
                        <label><s:text name="SFH04_productName"/></label>
                        <s:hidden name="prtVendorId"/>
                        <s:textfield name="productName" cssClass="text" maxlength="100"/>
                     </p>
                    <%--<p>--%><%-- 收货部门/发货部门选择框 --%>
	                    <%--<label id="lblDepReceive" class="left"><s:text name="SFH04_inOrgan"/></label>--%>
	                    <%--<label id="lblDepDeliver" class="left hide"><s:text name="SFH04_outOrgan"/></label>--%>
	                    <%--<table class="all_clean left"><tbody id="inOrganization_ID"></tbody></table>--%>
	                    <%--<input type="hidden" name="inOrganizationId" id="inOrganizationId" value=''>--%>
	                    <%--<span id="departNameReceive" ></span>--%>
	                    <%--<a class="add" onclick="openDepartBox();">--%>
	                        <%--<span class="ui-icon icon-search"></span>--%>
	                        <%--<span class="button-text"><s:text name="global.page.Popselect"/></span>--%>
	                    <%--</a>
                    <%--</p>--%>
	              </div>
	              <div class="column last" style="width:49%;height:120px;">      
	              <p id="dateCover" class="date">
	                <%-- 发货日期 --%>
	                  <label><s:text name="SFH04_date"/></label>
	                  <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
	                </p>      
	               <p>
	               	<%-- 审核状态 --%>
	                 <label><s:text name="SFH04_verifiedFlag"/></label>
	                 <s:select name="verifiedFlag" list='#application.CodeTable.getCodes("1180")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{SFH04_selectAll}"/>
	                 </p>
	                 <p>
	               	<%--处理状态--%>
	                 <label><s:text name="SFH04_tradeStatus"/></label>
	                 <s:select name="tradeStatus" list='#application.CodeTable.getCodes("1141")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{SFH04_selectAll}"/>
	                 </p>
	              </div>
	              </div>
                <div class="column last" style="width:100%;height:20px;margin-top:-12px;">
                <hr style="margin: 0 0 0.5em">
	                <p>
	                    <%--部门类型 收货部门/发货部门--%>
	                    <label><s:text name="SFH04_departInOutFlag"/></label>
	                    <select id="departInOutFlag" name="departInOutFlag" onchange="BINOLSTSFH04.changeDepartInOutFlag()">
	                        <option value="inOrgan"><s:text name="SFH04_departInOutFlag_in"/></option>
	                        <option value="outOrgan"><s:text name="SFH04_departInOutFlag_out"/></option>
	                    </select>
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
	              	<button class="right search"  onclick="BINOLSTSFH04.search();return false;">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="SFH04_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <div id="section" class="section hide">
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-search-result"></span>
                        <%-- 查询结果一览 --%>
                        <s:text name="SFH04_results_list"/>
                    </strong>
                </div>
                <div class="section-content" id="result_list">
                    <div class="toolbar clearfix">
                    <cherry:show domId="BINOLSTSFH04EXP">
	                    <a  class="export left" onclick="javascript:BINOLSTSFH04.exportExcel(this);return false;"  href="${export_url}">
							<span class="ui-icon icon-export"></span>
							<span class="button-text"><s:text name="global.page.export"></s:text></span>
						</a>
					</cherry:show>
                    <cherry:show domId="BINOLSTSFH04PNT">
		           		<div id="print_param_hide" class="hide">
		      				<input type="hidden" name="pageId" value="BINOLSTSFH05"/>
		         		</div>
				 		<a style="margin-right:10px;" onclick="openPrintApp('Print','#result_list');return false;" class="prints left">
						<span class="ui-icon icon-prints"></span>
						<span class="button-text"><s:text name="global.page.prints"/></span>
						</a>
		   			</cherry:show>
                        <span id="headInfo" style=""></span>
        	            <span class="right">
                        <a class="setting">
                            <span class="ui-icon icon-setting"></span>
            	            <span class="button-text">
                            <%-- 设置列 --%>
                            <s:text name="SFH04_colSetting"/>
                            </span>
                        </a>
                        </span>
                    </div>
                    <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                        <thead>
                            <tr>                 
                             <th><input type="checkbox" id="allSelect" onclick="checkBillAll(this);"/><s:text name="global.page.selectAll"/></th><%-- 选择 --%>
                              <th><s:text name="SFH04_deliverNo"/></th><%-- 发单单号--%>
                              <th><s:text name="SFH04_deliverNoIF"/></th><%-- 接口单号 --%>
                              <th><s:text name="SFH04_relevanceNo"/></th><%-- 关联单号 --%>
                              <th><s:text name="SFH04_importBatch"/></th><%-- 导入批次 --%>
                              <th><s:text name="SFH04_outOrgan"/></th><%-- 发货部门--%>
                              <th><s:text name="SFH04_depotName"/></th><%-- 发货仓库--%>
                              <th><s:text name="SFH04_inOrgan"/></th><%-- 收货部门--%>
                              <th><s:text name="SFH04_logInvName"/></th><%-- 逻辑仓库--%>
                              <th><s:text name="SFH04_totalQuantity"/></th><%-- 总数量--%>
                              <th><s:text name="SFH04_totalAmount"/></th><%-- 总金额--%>
                              <th><s:text name="SFH04_date"/></th><%-- 发货日期--%>
                              <th><s:text name="SFH04_verifiedFlag"/></th><%-- 审核区分--%>
                              <th><s:text name="SFH04_tradeStatus"/></th><%-- 处理状态--%>
                              <th><s:text name="SFH04_employeeName"/></th><%-- 制单员工--%>
                              <th><s:text name="SFH04_employeeNameAudit"/></th><%-- 审核者--%>
                              <th><s:text name="SFH04_comments"/></th><%-- 发货理由--%>
                              <%-- <th><s:text name="global.page.printStatus"/></th> --%> <%-- 打印状态 --%>
                            </tr>
                        </thead>
                    </table>
	          </div>
	        </div>
	      </div>

	 </s:i18n>
	    <%-- ================== dataTable共通导入 START ======================= --%>
		<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
		<%-- ================== dataTable共通导入    END  ======================= --%>
		<%-- ================== 打印预览共通导入 START ======================= --%>
		<jsp:include page="/applet/printer.jsp" flush="true" />
		<%-- ================== 打印预览共通导入 END ========================= --%>
		<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
		<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
		<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
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
		<input type="hidden"  id="defStartDate" value=''/>
		<input type="hidden"  id="defEndDate"	value=''/>   