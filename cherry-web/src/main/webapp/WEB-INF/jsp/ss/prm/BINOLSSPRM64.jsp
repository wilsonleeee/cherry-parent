<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM64.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.ss.BINOLSSPRM64">
	<s:url id="search_url" action="BINOLSSPRM64_search"/>
	<s:text name="PRM64_selectAll" id="PRM64_selectAll"/>
	<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
	</div>
	  <div class="panel-header">
	      <%-- 各类单据查询 --%>
	        <div class="clearfix"> 
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span> 
	        </div>
	      </div>
	      <div id="errorMessage"></div>
	      <div id="actionResultDisplay"></div>
	      <div class="panel-content">
	        <div class="box">
	          <cherry:form id="mainForm" class="inline">
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLSSPRM64"/>
	            <div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="PRM64_condition"/>
	            </strong>
	               </div>
	            <div class="box-content clearfix">
	              <div class="column" style="width:50%;height:80px;">
	                <p>
	               	<%-- 单号 --%>
	                  <label><s:text name="PRM64_billNoIF"/></label>
	                  <s:textfield name="billNoIF" cssClass="text" maxlength="40"  onblur="ignoreCondition(this);return false;"/>
	                </p>
	                <%-- 导入批次 --%>
	                  <label><s:text name="PRM64_importBatch"/></label>
	                  <s:textfield name="importBatch" cssClass="text" maxlength="25" />
	                </p>
	                 <p>
                        <%-- 产品名称 --%>
                        <label class="left"><s:text name="PRM64_productName"/></label>
                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                        <table class="all_clean left"><tbody id="promotion_ID"></tbody></table>
                        <a class="add" onclick="BINOLSSPRM64.openPrmPopup();">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
                     </p>
	              </div>
	              <div class="column last" style="width:49%;height:55px;">
	              <p id="dateCover"  class="date">
	                <%-- 日期 --%>
	                  <label><s:text name="PRM64_operateDate"/></label>
	                  <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
	                </p>
	              	 <p>
	               	<%-- 审核状态 --%>
	                  <label><s:text name="PRM64_verifiedFlag"/></label>
	                 <s:select name="verifiedFlag" list='#application.CodeTable.getCodes("1007")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM64_selectAll}"/>
	                 </p>
	                 <p>
                        <%-- 入库状态 --%>
                        <label><s:text name="PRM64_tradeStatus"/></label>
                        <s:select name="tradeStatus" list='#application.CodeTable.getCodes("1266")' 
                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM64_selectAll}"/>
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
	              	<button class="right search"  onclick="BINOLSSPRM64.search();return false">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="PRM64_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <div id="section" class="section hide">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<%-- 查询结果一览 --%>
	          	<s:text name="PRM64_results_list"/>
	         </strong>
	        </div>
	          <div class="section-content" id="result_list">
	           <div class="toolbar clearfix">
	          <cherry:show domId="BINOLSSPRM64PNT">
           		<div id="print_param_hide" class="hide">
       				<input type="hidden" name="pageId" value="BINOLSSPRM64"/>
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
	             <s:text name="PRM64_colSetting"/>
	             </span></a></span></div>
	      	<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	              <thead>
	                <tr>	               
	                  <%-- 选择 --%>
	                  <th><input type="checkbox" id="allSelect" onclick="checkBillAll(this);"/><s:text name="global.page.selectAll"/></th>
	                  <th><s:text name="PRM64_billNoIF"/><span class="ui-icon ui-icon-document"></span></th><%-- 单号 --%>
	                  <th><s:text name="PRM64_importBatch"/></th><%-- 导入批次  --%>
	                  <th><s:text name="PRM64_organizationID"/></th><%-- 部门  --%>
	                  <th><s:text name="PRM64_depot"/></th><%-- 仓库  --%>
	                  <th><s:text name="PRM64_totalQuantity"/></th><%-- 总数量 --%>
	                  <th><s:text name="PRM64_totalAmount"/></th><%-- 总金额 --%>
	                  <th><s:text name="PRM64_operateDate"/></th><%-- 发货日期 --%>
	                  <th><s:text name="PRM64_verifiedFlag"/></th><%-- 审核状态 --%>
	                  <th><s:text name="PRM64_tradeStatus"/></th><%-- 入库状态 --%>
	                  <th><s:text name="PRM64_comments"/></th><%-- 备注--%>   
	                  <th><s:text name="global.page.printStatus"/></th><%-- 打印状态 --%>
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
		