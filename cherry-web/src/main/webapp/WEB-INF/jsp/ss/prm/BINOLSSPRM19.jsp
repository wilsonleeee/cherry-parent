<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.*" %>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM19.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script language="javascript">
</script>
<%
UserInfo userinfo = (UserInfo)request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日�
String dateTime = dateFm.format(new java.util.Date());
%>
<s:i18n name="i18n.ss.BINOLSSPRM19">
<s:url id="url_getInDepart" value="/ss/BINOLSSPRM19_GETINDEPART"/>
<span id ="s_getInDepart" style="display:none">${url_getInDepart}</span>

<s:url id="url_getDepot" value="/ss/BINOLSSPRM19_GETDEPOT" />
<span id ="s_getDepot" style="display:none">${url_getDepot}</span>

<s:url id="url_getOutDepart" value="/ss/BINOLSSPRM19_GETOUTDEPART" />
<span id ="s_getOutDepart" style="display:none">${url_getOutDepart}</span>

<s:url id="url_saveURL" value="/ss/BINOLSSPRM19_SAVE" />
<span id ="s_saveURL" style="display:none">${url_saveURL}</span>

<s:url id="url_sendURL" value="/ss/BINOLSSPRM19_SEND" />
<span id ="s_sendURL" style="display:none">${url_sendURL}</span>
<div class="hide">
<s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
</div>
      <div class="panel-header">
        <div class="clearfix"> 		
        <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
		</div>
      </div>
      <div id="actionResultDisplay"></div>
      <div id="errorDiv2" class="actionError" style="display:none">
		      <ul>
		         <li><span id="errorSpan2"></span></li>
		      </ul>			
      </div>
		<s:if test="hasActionErrors()">
			<div class="actionError" id="actionResultDiv">
			  	<s:actionerror/>
			  	<cherry:form id="mainForm" action="" class="inline"></cherry:form>
			</div>
			<div style="height:20px"></div>
		</s:if>
		<s:else>
      <div class="panel-content">  
      <%-- ========概要部分============= --%> 
      <cherry:form id="mainForm" action="" class="inline">
      <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="lblgeneral"/></strong></div>
          <div class="section-content">
          	<table class="detail">
          		<tr>
              		<th><s:text name="lbldate"/></th>
              		<td><%= dateTime %><input type="hidden" id="brandInfoId" value="<%= userinfo.getBIN_BrandInfoID() %>"/></td>
              		<th><s:text name="lbloperator"/></th>
              		<td><%=userinfo.getEmployeeName() %></td>
              	</tr>
              	<tr>
              		<th><s:text name="lblInDepart"/></th>
              		<td>
              			 <input type="hidden" name="inOrganizationId" id="inOrganizationId" value="${organizationId}">
		                 <label id="inOrgName" class="left"><s:property value="departInit"></s:property></label>
		                 <a class="add right" onclick="SSPRM19_openInDepartBox(this);">
		                     <span class="ui-icon icon-search"></span>
		                     <span class="button-text"><s:text name="lblselect"/></span>
		                 </a>
              		</td>
              		<th><s:text name="lblInDepot"/></th>
              		<td>
              			<select style="width:200px;" name="inDepotId" id="inDepotId" >
	                  		<s:iterator value="inDepotList">
				         		<option value="<s:property value="BIN_DepotInfoID" />">
				         			<s:property value="DepotCodeName"/>
				         		</option>
				      		</s:iterator>
	               		</select>
              		</td>
              	</tr>
              	<tr>
              		<th><s:text name="lblOutorganization"/></th>
              		<td>
              			<input type="hidden" name="outOrganizationId" id="outOrganizationId">
		                <label id="outOrgName" class="left"></label>
		                <a class="add right" onclick="SSPRM19_openOutDepartBox(this);">
		                    <span class="ui-icon icon-search"></span>
		                    <span class="button-text"><s:text name="lblselect"/></span>
		                </a>
              		</td>
              		<s:if test='null != logicInventoryList && logicInventoryList.size()>0'>
	              		<th><s:text name="lblInLogicDepot"/></th>
	              		<td>
	              			<s:select name="inLoginDepotId" list="logicInventoryList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" cssStyle="width:200px;" />
	              		</td>
              		</s:if>
              		<s:else>
						<th></th>
						<td></td>              		
              		</s:else>
              	</tr>
              	<tr>
              		<th><s:text name="lblReasonAll"/></th>
                    <td colspan=3><input class="text" type="text" name="reasonAll" id="reasonAll" maxlength="100" style="width:95%;"/></td>
              	</tr>
          	</table>
          </div>
      </div>
     		
      <div id="mydetail" class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="lbldetail"/></strong></div>
		  <div class="section-content">
          <div class="toolbar clearfix"><span class="left">
             <input id="allSelect" type="checkbox"  class="checkbox" onclick="SSPRM19_selectAll()"/>
             <s:text name="btnallselect"/><a class="add" onclick="SSPRM19_openPromotionSearchBox(this);"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="btnadd"/></span></a> <a class="delete" onclick="SSPRM19_deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="btndelete"/></span></a></span></div>
            <div style="width:100%;overflow-x:scroll;">
            <input type="hidden" id="rowNumber" value="1"/>
            <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
              <thead>
                <tr>
                <th class="tableheader" width="5%"><s:text name="lblselect"/></th>
                  <th class="tableheader" width="10%"><s:text name="lblCode"/></th>
                  <th class="tableheader" width="10%"><s:text name="lblBarcode"/></th>
                  <th class="tableheader" width="20%"><s:text name="lblProname"/></th>
                  <th class="tableheader" width="8%"><s:text name="lblPrice"/></th>
                  <th class="tableheader" width="12%"><s:text name="lblCount"/></th>               
                  <th class="tableheader" width="10%"><s:text name="lblMoney"/></th>
                  <th class="tableheader" width="20%"><s:text name="lblReason"/></th>
                  <th style="display:none">
                </tr>
              </thead>
              <tbody id="databody"></tbody>
            </table>
            </div>
            <hr class="space" />
            <div class="center clearfix">    
            	<button class="save" type="button" onclick="SSPRM19_submitSave()"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="btnSave"/></span></button>        	
            	<cherry:show domId="BINOLSSPRM1901">
            	<button class="confirm" type="button" onclick="SSPRM19_submitSend()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnOK"/></span></button>
                </cherry:show>
            </div>
          </div>
        </div>
      </cherry:form>
     </div>
     </s:else>
</s:i18n>
<div id="errmessage" style="display:none">
	<input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
	<input type="hidden" id="errmsg3" value='<s:text name="ESS00025"/>'/>
	<input type="hidden" id="errmsgESS00042" value='<s:text name="ESS00042"/>'/>
	<input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
	<input type="hidden" id="errmsgESS00025" value='<s:text name="ESS00025"/>'/>
</div>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>