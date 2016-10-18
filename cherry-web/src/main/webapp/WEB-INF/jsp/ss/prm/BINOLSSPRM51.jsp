<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%> 
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.SimpleDateFormat" %>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM51.js"></script>
<script language="javascript">
</script>

<ct:form id="mainForm" action="/Cherry/ss/BINOLSSPRM51_SUBMIT">
<s:i18n name="i18n.ss.BINOLSSPRM51">
<%
UserInfo userinfo = (UserInfo)request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日�
String dateTime = dateFm.format(new java.util.Date());

%>
<s:url id="url_getdepotAjax" value="/ss/BINOLSSPRM51_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_popup" value="/ss/BINOLSSPRM51_OPENPOPUP" />
<span id ="s_popup" style="display:none">${url_popup}</span>

<s:url id="s_submitURL" value="/ss/BINOLSSPRM51_SUBMIT" />
<span id ="submitURL" style="display:none">${s_submitURL}</span>
<div class="hide">
<s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
</div>
	  <div id="hidden1Result1Div1" style="display: none"></div>
      <div class="panel-header">
        <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="lbltitle"/> &gt; <s:text name="lbltitle2"/></span> </div>
      </div>
            	       <%-- ================== 错误信息提示 START ======================= --%>
       <div id="errorDiv2" class="actionError" style="display:none">
		   <ul>
		         <li><span id="errorSpan2"></span></li>
		   </ul>			
      </div>
      <s:if test="hasActionErrors()">
		<div class="actionError" id="errorMsgDivTOP">
		  	<s:actionerror/>
		</div>
	 </s:if>
      <s:elseif test="hasActionMessages()">
		<div class="actionSuccess" id="actionMsgDivTOP">
		  <s:actionmessage/>
		</div>
	 </s:elseif>
	 <s:else>
	 <div id="ajaxerrdiv"></div>
      <div id="genDiv" class="panel-content">  
      <%-- ========概要部分============= --%> 
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
                    	<select style="width:100px;" name="inOrganizationId" id="inOrganizationId" onchange="chooseDepart(this)" >		                  		
	                  		<s:iterator value="inOrganizationList">
				         		<option value="<s:property value="OrganizationID" />">
				         			<s:property value="DepartName"/>
				         		</option>
				      		</s:iterator>
	                  	</select>
                         --%>
                    <span>
                        <input type="hidden" name="inOrganizationId" id="inOrganizationId">
                        <label id="inOrgName"></label>
                        <a class="add right" onclick="SSPRM51_openReceiveDepartBox(this);">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="PRM51_Select"/></span>
                        </a>
                    </span>
                    </div>
                    <div class="th"><s:text name="lblInDepot"/></div>
                    <div class="td">                    		
		                  	<select style="width:100px;" name="inDepot" id="inDepot" >
		                  	<s:iterator value="inDepotList">
					         		<option value="<s:property value="BIN_InventoryInfoID" />">
					         			<s:property value="InventoryName"/>
					         		</option>
					      		</s:iterator>
		                  	</select>		                  	
					</div>
                  </div>
                  <div class="tr">
                    <div class="th"><s:text name="lblChooseFile"/></div>
                    <div class="td"><a href="javascript:openPopup1();void(0);"><s:text name="lblChooseFileLink"/></a></div>
                    <div class="th">
                  </div>
                </div>
              </div>
            </div>
        <div id="mydetail" class="section">       
        </div>
      </div>
     </div>
     </s:else>
</s:i18n>
</ct:form>
<div id="errmessage" style="display:none">
	<input type="hidden" id="errmsg1" value='<s:text name="ESS00013"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="ESS00014"/>'/>
	<input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
	<input type="hidden" id="errmsgESS00042" value='<s:text name="ESS00042"/>'/>
</div>

<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>