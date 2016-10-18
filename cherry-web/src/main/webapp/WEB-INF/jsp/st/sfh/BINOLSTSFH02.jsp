<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH02.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript">
<!--
function test_creatOrder(){
	var params=$('#mainForm').formSerialize();
	var curl = $("#creatUrl").attr("href");

	cherryAjaxRequest({
		url:curl,
		param:params,
		callback:function(){alert(tmp);}
	});
}
//-->
</script>
<s:i18n name="i18n.st.BINOLSTSFH02">
	<s:url id="search_url" value="/st/BINOLSTSFH02_search"/>
	<s:url id="create_url" value="/st/BINOLSTSFH02_create"/>
	<s:url id="export_url" action="BINOLSTSFH02_export"/>
	<s:text name="SFH02_selectAll" id="SFH02_selectAll"/>
	<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
		<a id="creatUrl" href="${create_url}"></a>
	</div>
	     <div class="panel-header">
	      	<%-- 订货单一览 --%>
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
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLSTSFH02"/>
	            <div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="SFH02_condition"/>
	            </strong>
	               </div>
	            <div class="box-content clearfix">
	              <div class="column" style="width:50%;height:90px;">
	                <p>
	               	<%-- 订单编号 --%>
	                  <label><s:text name="SFH02_orderNo"/></label>
	                  <s:textfield name="orderNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
	                </p>
	                <p>
                        <%-- 产品名称 --%>
                        <label><s:text name="SFH02_productName"/></label>
                        <s:textfield name="nameTotal" cssClass="text" maxlength="100"/>
	                  	<input type="hidden" id="prtVendorId" name="prtVendorId" value="" />
                     </p>
	              </div>
	              <div class="column last" style="width:49%;height:55px;">      
	              <p id="dateCover" class="date">
	                <%-- 加入日期 --%>
	                  <label><s:text name="SFH02_date"/></label>
	                  <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
	                </p>      
	               <p>
	               	<%-- 审核状态 --%>
	                 <label><s:text name="SFH02_verifiedFlag"/></label>
	                 <s:select name="verifiedFlag" list='#application.CodeTable.getCodes("1146")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{SFH02_selectAll}"/>
	                 </p>
	                 <p>
	               	<%--处理状态--%>
	                 <label><s:text name="SFH02_tradeStatus"/></label>
	                 <s:select name="tradeStatus" list='#application.CodeTable.getCodes("1142")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{SFH02_selectAll}"/>
	                 </p>
	              </div>
	              <%-- ======================= 组织联动共通导入开始  ============================= --%>
					<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
						<s:param name="businessType">4</s:param>
 						<s:param name="operationType">1</s:param>
 						<s:param name="mode">dpat,area,chan,dpot</s:param>
					</s:action>
					<%-- ======================= 组织联动共通导入结束  ============================= --%>
	            </div>
	            <p class="clearfix">
	                <%-- 查询 --%>
	                <%--
                    <button class="right search"  onclick="test_creatOrder();return false">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text">TEST</span>
	              	</button>
                    --%>
	              	<button class="right search"  onclick="search();return false">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="SFH02_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <div id="section" class="section hide">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<%-- 查询结果一览 --%>
	          	<s:text name="SFH02_results_list"/>
	         </strong>
	        </div>
	          <div class="section-content" id="result_list">
	            <div class="toolbar clearfix">
	            <cherry:show domId="BINOLSTSFH02EXP">
                    <a class="export left" onclick="javascript:exportExcel(this);return false;"  href="${export_url}">
						<span class="ui-icon icon-export"></span>
						<span class="button-text"><s:text name="global.page.export"></s:text></span>
					</a>
				</cherry:show>
	            <cherry:show domId="BINOLSTSFH02PNT">
	           		<div id="print_param_hide" class="hide">
	      				<input type="hidden" name="pageId" value="BINOLSTSFH03"/>
	         		</div>
			 		<a style="margin-right:10px;" onclick="openPrintApp('Print','#result_list');return false;" class="prints left">
					<span class="ui-icon icon-prints"></span>
					<span class="button-text"><s:text name="global.page.prints"/></span>
					</a>
   				</cherry:show>
	            <span id="headInfo" style=""></span>
	            <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
	            <span class="button-text">
	             <%-- 设置列 --%>
	             <s:text name="SFH02_colSetting"/>
	             </span></a></span></div>
	          </div>
	        </div>
	      </div>
	      <div id="result_table" class="hide">
	      	<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	              <thead>
	                <tr>	             
	                 <th><input type="checkbox" id="allSelect" onclick="checkBillAll(this);"/><s:text name="global.page.selectAll"/></th><%-- 选择 --%>
	                  <th><s:text name="SFH02_orderNo"/></th><%-- 订单单号--%>
	                  <th><s:text name="SFH02_orderNoIF"/></th><%-- 接口单号 --%>
	                  <th><s:text name="SFH02_relevanceNo"/></th><%-- 关联单号 --%>
	                  <th><s:text name="SFH02_binOrganizationID"/></th><%-- 申请部门--%>
	                  <th><s:text name="SFH02_suggestedQuantity"/></th><%-- 建议数量--%>
	                  <th><s:text name="SFH02_applyQuantity"/></th><%-- 计划定量--%>
	                  <th><s:text name="SFH02_totalQuantity"/></th><%-- 总数量--%>
	                  <th><s:text name="SFH02_totalAmount"/></th><%-- 总金额--%>
	                  <th><s:text name="SFH02_tradeStatus"/></th><%-- 处理状态--%>
	                  <th><s:text name="SFH02_verifiedFlag"/></th><%-- 审核区分--%>
	                  <th><s:text name="SFH02_binLogisticInfoID"/></th><%-- 物流ID--%>
	                  <th><s:text name="SFH02_comments"/></th><%-- 备注--%>
	                  <th><s:text name="SFH02_tradeDateTime"/></th><%-- 订货时间--%>
                      <th><s:text name="SFH02_binEmployeeID"/></th><%-- 制单员工--%>
                      <th><s:text name="SFH02_binEmployeeIDAudit"/></th><%-- 审核者--%>
                      <th><s:text name="global.page.printStatus"/></th> <%-- 打印状态 --%>
	                </tr>
	              </thead>
	            </table>
	      </div>
	 </s:i18n>
	    <%-- ================== dataTable共通导入 START ======================= --%>
		<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
		<%-- ================== dataTable共通导入    END  ======================= --%>
		<%-- ================== 打印预览共通导入 START ======================= --%>
		<jsp:include page="/applet/printer.jsp" flush="true" />
		<%-- ================== 打印预览共通导入 END ========================= --%>
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