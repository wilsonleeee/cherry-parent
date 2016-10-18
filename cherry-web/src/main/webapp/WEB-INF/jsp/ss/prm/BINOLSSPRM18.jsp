<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM18.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.ss.BINOLSSPRM18">
	<s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart"/>
	<s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory"/>
	<s:url id="search_url" value="/ss/BINOLSSPRM18_search"/>
	<s:url id="delete_url" action="BINOLSSPRM18_delete"/>
	<s:url id="send_url" action="BINOLSSPRM18_send"/>
	<s:text name="PRM18_selectAll" id="PRM18_selectAll"/>
	<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
		<s:text name="PRM18_select" id="defVal"/>
		<div id="PRM18_select">${defVal}</div>
	</div>
	      <div class="panel-header">
	      	<%-- 发货单查询 --%>
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
	          <cherry:form id="mainForm" class="inline" onsubmit="search(); return false;">
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLSSPRM18"/>
	            <div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="PRM18_condition"/>
	            </strong>
	               </div>
	            <div class="box-content clearfix">
	            <%-- 总部或者办事处用户登录 --%>
	              <div class="column" style="width:50%;height:110px;">
	                <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
	               	<%-- 发货单号 --%>
	                  <label><s:text name="PRM18_deliverRecNo"/></label>
	                  <s:textfield name="deliverRecNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
	                </div>
                    <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
                        <%-- 促销品名称 --%>
                        <label class="left"><s:text name="PRM18_promotionProductName"/></label>
                        <%--<s:hidden name="prmVendorId" id="prmVendorId"/>--%>
                        <%--<s:textfield name="promotionProductName" cssClass="text" maxlength="30"/>--%>
                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                        <table class="all_clean left"><tbody id="promotion_ID"></tbody></table>
                        <a class="add" onclick="BINOLSSPRM18.openPrmPopup();">
                           <span class="ui-icon icon-search"></span>
                           <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
                    </div>
                    <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
                        <%--部门联动条 查询 发货部门/收货部门 标志--%>
                        <label class="left"><s:text name="PRM18_departInOutFlag"/></label>
                        <input type="radio" id="departInOutFlag" name="departInOutFlag" value="outOrgan" onclick="BINOLSSPRM18.changeDepartInOutFlag()" checked/><s:text name="PRM18_departInOutFlag_out"/>
                        <input type="radio" id="departInOutFlag" name="departInOutFlag" value="inOrgan" onclick="BINOLSSPRM18.changeDepartInOutFlag()"/><s:text name="PRM18_departInOutFlag_in"/>
                    </div>
                    <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
                        <%-- 收货部门 --%>
                        <label id="lblDepReceive" class="left"><s:text name="PRM18_departNameReceive"/></label>
                        <label id="lblDepDeliver" class="left hide"><s:text name="PRM18_departNameDeliver"/></label>
                        <table class="all_clean left"><tbody id="inOrganization_ID"></tbody></table>
                        <%--<input type="hidden" name="inOrganizationId" id="inOrganizationId" value=''>--%>
                        <%--<span id="departNameReceive" ></span>--%>
                        <a class="add" onclick="openDepartBox();">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
                    </div>
	              </div>
	              <div class="column last" style="width:49%;">	             
	               <p id="dateCover" class="date">
	                <%-- 发货日期 --%>
	                  <label><s:text name="PRM18_deliverDate"/></label>
	                  <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
	                </p>
                    <p>
                        <%-- 审核状态 --%>
                        <label><s:text name="PRM18_verifiedFlag"/></label>
                        <s:select name="verifiedFlag" list='#application.CodeTable.getCodes("1007")' 
                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM18_selectAll}"/>
                    </p>
                    <p>
	                   <%-- 处理状态 --%>
	                  <label><s:text name="PRM18_stockInFlag"/></label>
	                 <s:select name="stockInFlag" list='#application.CodeTable.getCodes("1017")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM18_selectAll}"/>
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
	              	<button class="right search" type="submit" onclick="search();return false">
	              			<span class="ui-icon icon-search-big"></span>
	              			<%-- 查询 --%>
	              			<span class="button-text"><s:text name="PRM18_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <div id="section" class="section hide">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<%-- 查询结果一览 --%>
	          	<s:text name="PRM18_results_list"/>
	         </strong>
	        </div>
	          <div class="section-content" id="result_list">
	            <div class="toolbar clearfix">
	            <span id="headInfo" style=""></span>
	            <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
	            <span class="button-text">
	             <%-- 设置列 --%>
	             <s:text name="PRM18_colSetting"/>
	             </span></a></span></div>
	          </div>
	        </div>
	      </div>
	      <div id="result_table" class="hide">
	      	<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	              <thead>
	                <tr>	               
	                  <th><s:text name="PRM18_num"/></th><%-- No. --%>
	                  <th><s:text name="PRM18_deliverRecNo"/><span class="ui-icon ui-icon-document"></span></th><%-- 发货单号 --%>
	                  <th><s:text name="PRM18_departNameDeliver"/></th><%-- 发货部门  --%>
	                  <th><s:text name="PRM18_departNameReceive"/></th><%-- 收货部门 --%>
	                  <th><s:text name="PRM18_totalQuantity"/></th><%-- 总数量 --%>
	                  <th><s:text name="PRM18_totalAmount"/></th><%-- 总金额 --%>
	                  <th><s:text name="PRM18_deliverDate"/></th><%-- 发货日期 --%>
	                  <th><s:text name="PRM18_verifiedFlag"/></th><%-- 审核状态 --%>
	                  <th><s:text name="PRM18_stockInFlag"/></th><%-- 入库状态 --%>
	                </tr>
	              </thead>
	            </table>
	      </div>
	 </s:i18n>
	    <%-- ================== dataTable共通导入 START ======================= --%>
		<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
		<%-- ================== dataTable共通导入    END  ======================= --%>
        <%-- ============ 弹出dataTable 促销产品共通导入 START ================= --%>
        <%--<jsp:include page="/WEB-INF/jsp/common/prmProductTable.jsp" flush="true" />--%>
        <%-- ============ 弹出dataTable 促销产品共通导入 END =================== --%>
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