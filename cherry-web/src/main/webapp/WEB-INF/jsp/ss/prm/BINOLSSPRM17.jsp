<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM17.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script language="javascript">

</script>
<cherry:form id="mainForm" action="/Cherry/ss/BINOLSSPRM17_SUBMIT">
<s:i18n name="i18n.ss.BINOLSSPRM17">
<%
UserInfo userinfo = (UserInfo)request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日�
String dateTime = dateFm.format(new java.util.Date());

%>
<div class="hide">
<s:hidden id="depotJson"></s:hidden>
<s:url id="url_getdepotAjax" value="/ss/BINOLSSPRM17_getDepot" />
<span id ="urlgetdepotAjax" >${url_getdepotAjax}</span>
<s:url id="url_getconDepartAjax" value="/ss/BINOLSSPRM17_getDepart" />
<span id ="urlgetdepartAjax" >${url_getconDepartAjax}</span>
<s:url id="s_submitURL" value="/ss/BINOLSSPRM17_SUBMIT" />
<span id ="submitURL" >${s_submitURL}</span>
<s:url id="s_saveURL" value="/ss/BINOLSSPRM17_SAVE" />
<span id ="saveURL" >${s_saveURL}</span>
<s:url id="url_getStockCount" value="/ss/BINOLSSPRM17_GETSTOCKCOUNT" />
<span id ="s_getStockCount" >${url_getStockCount}</span>
<s:url id="url_getDeliverDetail" value="/ss/BINOLSSCM06_getDeliverDetail" />
<span id ="s_getDeliverDetail" >${url_getDeliverDetail}</span>
<input type="hidden"  id="brandInfoId" value='<%=userinfo.getBIN_BrandInfoID() %>'>
<div id="dialogConfirm"><s:text name="dialog_confirm" /></div>
<div id="dialogCancel"><s:text name="dialog_cancel" /></div>
<s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
<input type="hidden" id="checkStockFlag" value='<s:property value="checkStockFlag" />'>
<input type="hidden"  id="dateToday" value='<%= dateTime %>'>
</div>
      <div class="panel-header">
        <div class="clearfix"> 	
       	<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
		 </div>
      </div>
      <div id="actionResultDisplay"></div>
      	       <%-- ================== 错误信息提示 START ======================= --%>
          <div id="errorDiv2" class="actionError" style="display:none">
		      <ul>
		         <li><span id="errorSpan2"></span></li>
		      </ul>			
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
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="lblgeneral"/></strong></div>
              <div class="section-content">
                <div class="table clearfix">
                  <div class="tr">
                    <div class="th"><s:text name="lbldate"/></div>
                    <div class="td"><%= dateTime %></div>
                    <div class="th"><s:text name="lbloperator"/></div>
                    <div class="td"><%=userinfo.getEmployeeName() %></div>
                  </div>                  
                  <div class="tr">
                    <div class="th"><s:text name="lbldepartment"/></div>
                    
                    <div class="td">                    
                    <%-- 
                    <select style="width:100px;" name="outOrganizationId" id="outOrganizationId" onchange="chooseDepart(this)" >		                  		
	                  		<s:iterator value="outOrganizationList">
				         		<option value="<s:property value="OrganizationID" />">
				         			<s:property value="DepartName"/>
				         		</option>
				      		</s:iterator>
	                  	</select>
                    --%>
                    <span>
                        <input type="hidden" name="outOrganizationId" id="outOrganizationId" value="${organizationId}"/>
                        <label id="outOrgName">${departInit}</label>
                        <a class="add right" onclick="SSPRM17_openSendDepartBox(this);">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="lblselect"/></span>
                        </a>
                    </span>
                    </div>
                    <div class="th"><s:text name="lblOutDepot"/></div>
                    <div class="td">                    		
		                  	<select style="width:200px;" name="outDepot" id="outDepot" onchange="SSPRM17_chooseDepot(this)">
                            <option value=""><s:text name="PRM17_Select"/></option>
		                  	<s:iterator value="outDepotList">
					         		<option value="<s:property value="BIN_InventoryInfoID" />">
					         			<s:property value="InventoryName"/>
					         		</option>
					      		</s:iterator>
		                  	</select>		                  	
					</div>
                  </div>
                    <s:if test='null != logicInventoryList && logicInventoryList.size()>0'>
                    <div class="tr">
                        <div class="th"><s:text name="lblOutLoginDepot"/></div>
                        <div class="td"><s:select name="outLoginDepotId" list="logicInventoryList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" cssStyle="width:200px;" onchange="SSPRM17_clearDetailData('flag');return false;"/></div>
                        <div class="th"><s:text name="lblrecdepartment"/></div>
                        <div class="td">
                            <input type="hidden" name="inOrganizationID" id="inOrganizationID"></input>
                            <span id="inOrgName"></span>
                            <a class="add right" onclick="SSPRM17_openReceiveDepartBox(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="lblselect"/></span>
                            </a>
                        </div>
                    </div>
                    <div class="tr">
                        <div class="th"><s:text name="lblReasonAll"/></div>
                        <div class="td"><s:select name="reasonAll" list='#application.CodeTable.getCodes("1051")' listKey="CodeKey" listValue="Value"/></div>
                        <div class="th"><s:text name="PRM17_PlanArriveDate"/></div>
                        <div class="td">
                            <p class="date">
                                <span><s:textfield name="planArriveDate" cssClass="date" id="planArriveDate"/></span>
                            </p>
                        </div>
                    </div>
                    </s:if>
                    <s:else>
                    <div class="tr">
                        <div class="th"><s:text name="lblrecdepartment"/></div>
                        <div class="td">
                            <input type="hidden" name="inOrganizationID" id="inOrganizationID"></input>
                            <span id="inOrgName"></span>
                            <a class="add right" onclick="SSPRM17_openReceiveDepartBox(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="lblselect"/></span>
                            </a>
                        </div>
                        <div class="th"><s:text name="lblReasonAll"/></div>
                        <div class="td"><s:select name="reasonAll" list='#application.CodeTable.getCodes("1051")' listKey="CodeKey" listValue="Value"/></div>
                    </div>
	                    <div class="tr">
	                        <div class="th"><s:text name="PRM17_PlanArriveDate"/></div>
	                        <div class="td">
	                            <p class="date">
	                                <span><s:textfield name="planArriveDate" cssClass="date" id="planArriveDate"/></span>
	                            </p>
	                        </div>
	                        <div class="th"></div>
	                        <div class="td"></div>
	                    </div>
                    </s:else>            
                </div>
              </div>
            </div>        
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="lbldetail"/></strong></div>
		  <div class="section-content">
            <div class="toolbar clearfix">
	            <span class="left">
	                <input id="allSelect" type="checkbox"  class="checkbox" onclick="selectAll()"/>
	                <s:text name="btnallselect"/>
	                <a class="add" onclick="SSPRM17_openPromotionSearchBox(this);"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="btnadd"/></span></a>
                    <a class="delete" onclick="deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="btndelete"/></span></a>
	                <a class="add" onclick="BINOLSSPRM17.copyDeliver();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="PRM17_copy"/></span></a>
	            </span>
            </div>
            <div style="width:100%;overflow-x:scroll;">
            <input type="hidden" id="rowNumber" value="1"/>
            <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
              <thead>
                <tr>
                <th class="tableheader" width="3%"><s:text name="lblselect"/></th>
                  <th class="tableheader" width="15%"><s:text name="lblcode"/></th>
                  <th class="tableheader" width="15%"><s:text name="lblbarcode"/></th>
                  <th class="tableheader" width="20%"><s:text name="lblproname"/></th>
                  <th class="tableheader" width="8%"><s:text name="lblprice"/></th>
                  <%--<th class="tableheader" width="12%" onclick="sortTable();"><div class="DataTables_sort_wrapper"><s:text name="lblrecdepartment"/><span id="sortImage" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>--%>
                  <th class="tableheader" width="8%"><s:text name="lblNowCount"/></th>                
                  <th class="tableheader" width="10%"><div style="line-height:13px;"><s:text name="lbloutcount"/><br /><s:text name="lbloutcountb"/></div></th>
                  <th class="tableheader" width="10%"><div style="line-height:13px;"><s:text name="lblmoney"/><br /><s:text name="lblmoneyu"/></div></th>
                  <th class="tableheader" width="20%"><s:text name="lblreason"/></th>
                  <th style="display:none">
                </tr>
              </thead>
              <tbody id="databody">
            </table>
            </div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
	            <tr>
	                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
	                <td class="center"><s:text name="PRM17_totalQuantity"/></td><%-- 总数量 --%>
	                <td class="center"><s:text name="PRM17_totalAmount"/></td><%-- 总金额--%>
	            </tr>
	            <tr>
                    <td class="center">
	                    <%-- 总数量 --%>
	                    <span id="totalQuantity">0</span>
                    </td>
                    <td class="center">
                        <%-- 总金额--%>
                        <span id="totalAmount">0.00</span>
                    </td>
	            </tr>
            </table>
            <hr class="space" />
            <div class="center clearfix">
            	<button class="save" type="button" onclick="SSPRM17_btnSaveClick()"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="btnSave"/></span></button>
            	<cherry:show domId="BINOLSSPRM1701">
            	<button class="confirm" type="button" onclick="SSPRM17_btnSendClick()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnOK"/></span></button>
            	</cherry:show>
            </div>
          </div>
        </div>
      </div>
      </s:else>
      <div class="hide" id="dialogInit"></div>
</s:i18n>
</cherry:form>
<div id="errmessage" style="display:none">
	<input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
	<input type="hidden" id="errmsg3" value='<s:text name="ESS00040"/>'/>
	<input type="hidden" id="errmsg4" value='<s:text name="ESS00042"/>'/>
	<input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
	<input type="hidden" id="errmsgESS00048" value='<s:text name="ESS00048"/>'/>
	<input type="hidden" id="errmsgESS00025" value='<s:text name="ESS00025"/>'/>
	<input type="hidden" id="errmsg_EST00034" value='<s:text name="EST00034"/>'/>
</div>
<%-- ================== dataTable共通导�?START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导�?   END  ======================= --%>

<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>

<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/ss/common/BINOLSSCM06.jsp" flush="true" />
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>

<%-- ================== 弹出收货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/popDepartTableBusinessType.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>