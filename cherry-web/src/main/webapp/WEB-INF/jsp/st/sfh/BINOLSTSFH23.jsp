<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- 产品订货--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH23.js?201610061427"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js?201610061427"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js?201610061427"></script>
<s:i18n name="i18n.st.BINOLSTSFH23">
<cherry:form id="mainForm" class="inline" onsubmit="return false;">
    <div class="hide">
    
    <s:url id="search_url" value="/st/BINOLSTSFH23_search"/> <%-- 查询订单记录URL --%>
    <s:hidden name="searchUrlID" value="%{search_url}"/>
    
    <s:url id="initSearch_url" value="/st/BINOLSTSFH23_initSearch"/> <%-- 初始化时订单记录URL --%>
    <s:hidden name="initSearchUrlID" value="%{initSearch_url}"/>
    
    <s:url id="export_url" value="/st/BINOLSTSFH23_export"/> <%-- 订单Excel导出 URL--%>
    <s:hidden name="exportUrlID" value="%{export_url}"/>
    
    <s:url id="copyOrder_url" value="/st/BINOLSTSFH23_copyOrder"/> <%-- 订单复制 URL--%>
    <s:hidden name="copyOrderUrlID" value="%{copyOrder_url}"/>
    
    <s:url id="deleteOrder_url" value="/st/BINOLSTSFH23_deleteOrder"/> <%-- 订单删除URL--%>
    <s:hidden name="deleteOrderUrlID" value="%{deleteOrder_url}"/>
    
    <s:url id="payMoney_url" value="/st/BINOLSTSFH23_payMoney"/> <%-- 款已付URL--%>
    <s:hidden name="payMoneyUrlID" value="%{payMoney_url}"/>
    
    </div>
    <div class="panel-header">
        <div class="clearfix"> 
            <span class="breadcrumb left">      
                <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
            </span>
        </div>
    </div>
    <div id="actionResultDisplay"></div>
    <div id="errorMessage"></div>      
    <%-- ==================隐藏域参数======================= --%>
<%--     <div id ="123" style="display:none">
           <input type="hidden" name="inOrganizationId" id="inOrganizationId" value='${initInfoMap.defaultDepartID}'>
           <input type="hidden" id="inTestType" value="<s:property value='initInfoMap.defaultTestType'/>"></input>
           <input type="hidden" name="outOrganizationId" id="outOrganizationId" value='${initInfoMap.defaultOutDepartID}'>
           <s:select name="inLogicDepotId" list="inLogicDepotList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" disabled="true" cssStyle="width:200px;" onchange="BINOLSTSFH23.clearDetailData('flag');return false;"/>
	       <select style="width:200px;" name="inDepotId" id="inDepotId" disabled="true" onchange="BINOLSTSFH23.clearDetailData();return false;">
	              <s:iterator value="inDepotList">
	                  <option value="<s:property value="BIN_DepotInfoID" />">
	                      <s:property value="DepotCodeName"/>
	                  </option>
	              </s:iterator>
	       </select>
	          
     </div> --%>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>
    </div>
    <div style="display: none" id="EBS00144"><!-- 只能删除订单状态为新建的订单！ -->
	    <div class="actionError">
	       <ul><li><span><s:text name="SFH23_EBS00144"/></span></li></ul>         
	    </div>
	 </div>
	 <div style="display: none" id="EBS00145"><!-- 选中订单后才能进行相关操作！ -->
	    <div class="actionError">
	       <ul><li><span><s:text name="SFH23_EBS00145"/></span></li></ul>         
	    </div>
	 </div>
    <div style="display: none" id="EBS00146"><!-- 新建订单不能使用款已付功能 -->
	    <div class="actionError">
	       <ul><li><span><s:text name=" SFH23_EBS00146"/></span></li></ul>         
	    </div>
	 </div>
    <div style="display: none" id="EBS00147"><!-- 只能选择一条订单进行订单复制操作-->
	    <div class="actionError">
	       <ul><li><span><s:text name="SFH23_EBS00147"/></span></li></ul>         
	    </div>
	 </div>
	     <div style="display: none" id="EBS00148"><!-- 款已付状态的订单不能再经行款已付操作-->
	    <div class="actionError">
	       <ul><li><span><s:text name="SFH23_EBS00148"/></span></li></ul>         
	    </div>
	 </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <s:if test="hasActionErrors()">
        <div class="actionError" id="actionResultDiv">
            <s:actionerror/>
        </div>
        <div style="height:20px"></div>
    </s:if>
    <s:else>
        <div class="panel-content">
            <div class="section">
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-info"></span><s:text name="SFH23_general"/>
                    </strong>
                </div>
            <div class="section-content">
                <table class="detail">
                    <tr>
                        <th><s:text name="SFH23_expectDeliverDate"/></th>
                        <td>		               		
                            <span><s:textfield name="fromDate" cssClass="date" readOnly="readOnly"></s:textfield>-<s:textfield name="toDate" cssClass="date" readOnly="readOnly"></s:textfield></span>
		              	</td>
                        <th><s:text name="SFH23_orderNumber"/></th>
                        <td> <span class="left"><input name="orderNum" id="orderNum" value=""></span></td>    
                    </tr>
                    <tr>
                        <th><s:text name="SFH23_orderStatus"/></th>
                          <td><span><s:select list='#application.CodeTable.getCodes("1146")' listKey="CodeKey" listValue="Value" id="orderStatus" name="orderStatus" headerKey="" headerValue="请选择"></s:select></span></td>
                        <th><s:text name="SFH23_orderEmployee"/></th>
                        <td><span class="left"><input  name="employeeName" id="employeeName" value=""></span></td>
                    </tr>
                </table>
            </div>
            	 <p class="clearfix"> <!-- 查询按钮 -->
						<button class="right search" type="button" onclick="BINOLSTSFH23.search();return false;">
							<span class="ui-icon icon-search-big"></span>
							<span class="button-text"><s:text name="SFH23_select" /></span>
						</button>
	            </p>
            </div>
        <div class="section">
            <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="SFH23_detail"/></strong></div>
            <div class="section-content">
            <div class="toolbar clearfix">
                <span class="left">
                <input type="button" value="<s:text name="SFH23_excelExport"/>" onclick="BINOLSTSFH23.exportExcel();return false;" id="upload"/>
                <input type="button" value="<s:text name="SFH23_copyOrder"/>" onclick="BINOLSTSFH23.copyOrder();return false;" id="upload1" />
                <input type="button" value="<s:text name="SFH23_orderDelete"/>" onclick="BINOLSTSFH23.deleteOrder();return false;" id="upload2"/>
                <input type="button" value="<s:text name="SFH23_payMoney"/>" onclick="BINOLSTSFH23.payMoney();return false;" id="upload3"/>
                <span id="headInfo" style=""></span>
                </span>
            </div>
            <div id="canceldetail">
                <div style="width:100%;overflow-x:scroll;">
                <input type="hidden" id="rowNumber" value="0"/>
                <table id="dataTable" cellpadding="0" cellspacing="0" border="0" width="100%" class="background_1">
                    <thead>
                       <tr>
                            <th class="tableheader" width="3%">
                                <input id="checkAll" type="checkbox"  class="checkbox" onclick="bscom03_checkRecord(this,'#dataTable');"/>
                                <s:text name="SFH23_allselect"/>
                            </th>
                            <th class="tableheader" width="15%"><s:text name="SFH23_orderNumber"/></th>
                            <th class="tableheader" width="10%"><s:text name="SFH23_orderEmployeeName"/></th>
                            <th class="tableheader" width="10%"><s:text name="SFH23_orderDate"/></th>
                            <th class="tableheader" width="8%"><s:text name="SFH23_orderQuantity"/></th>
                            <th class="tableheader" width="5%"><s:text name="SFH23_orderAmount"/></th>
                            <th class="tableheader" width="10%"><s:text name="SFH23_expectDeliverDate"/> </th>
                            <th class="tableheader" width="10%"><s:text name="SFH23_deliverDelete"/></th>
                            <th class="tableheader" width="8%"><s:text name="SFH23_deliverDate"/></th>
                            <th class="tableheader" width="8%"><s:text name="SFH23_orderStatus"/></th>
 
                        </tr>
                    </thead>
                    <tbody id="databody">
                    </tbody>
                </table>
                </div>
            </div>
          </div>
        </div>
      </div>
      </s:else>
      <div class="hide" id="dialogInit"></div>
<%-- ================== 弹出发货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出收货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/popDepartTableBusinessType.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/BINOLSTCM12.jsp" flush="true" />
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
</cherry:form>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="ESS00040"/>'/>
    <input type="hidden" id="errmsg4" value='<s:text name="ESS00042"/>'/>
    <input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
    <input type="hidden" id="notOnlyOneWarning" value='<s:text name="SFH23_notOnlyOne"/>'/>
    <input type="hidden" id="noOutDepart" value='<s:text name="SFH23_noOutDepart"/>'/>
    <input type="hidden" id="noOutDepot" value='<s:text name="SFH23_noOutDepot"/>'/>
    <input type="hidden" id="noInDepart" value='<s:text name="SFH23_noInDepart"/>'/>
    <input type="hidden" id="noInDepot" value='<s:text name="SFH23_noInDepot"/>'/>
    <input type="hidden" id="noInDepart" value='<s:text name="SFH23_noInDepart"/>'/>
    <input type="hidden" id="configOutDepart" value='<s:text name="SFH23_configOutDepart"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00013" value='<s:text name="EST00013"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
    <input type="hidden" id="errmsg_EST00035" value='<s:text name="EST00035"/>'/>
</div>
    <%-- ====================== 建议订货生成天数弹出框 ====================== --%>
	 <div id="sugggestDayPop" class="hide">
    	<div class="clearfix">
    	 <form id="sugggestDayForm">
    	 <div id="actionResultDisplay"></div>
    		<div>
    		<table style="width:100%;" class="detail">
	            <tr>
					<th><s:text name="SFH23_orderDay"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		    		<td><span><s:textfield id="orderDayNum" name="orderDayNum" cssClass="text" maxlength="20"/></span></td>
	            </tr>
    		</table>
    		</div>
    	</form>
    	</div>
    </div>
    <%-- ====================== 建议订货生成天数弹出框  ====================== --%>
</s:i18n>
<%-- ================== dataTable共通导�?START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导�?   END  ======================= --%>