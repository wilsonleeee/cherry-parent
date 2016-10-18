<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="com.cherry.cm.cmbeans.ControlOrganization" %>
<%@ page import="com.cherry.cm.cmbeans.RoleInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.SimpleDateFormat" %>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM51.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	window.onbeforeunload = function(){
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	};		
	if (window.opener) {	
	  window.opener.lockParentWindow();	
	}	
} );
/**
 * 点击收货入库按钮
 */
function submitForm(){
	var params=$('#mainForm').formSerialize();
	cherryAjaxRequest({
		url:$("#submitURL").html(),
		param:params,
		callback:submitSuccessJbpm
	});
}

function submitSuccessJbpm(msg){
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$("#btnOK").hide();
	}
	submitflag =true;
}
function ClosePopupForm(){
	var ob = window.opener.document.getElementById("currentUnitID");
	if(ob){
		if(ob.value=='TOP'){
			window.opener.refreshTaskList();
		}
	}
	window.close();
}
</script>
<cherry:form id="mainForm" action="" csrftoken="false">
  <input type="hidden" value="" name="csrftoken" id="csrftoken">
<s:i18n name="i18n.ss.BINOLSSPRM51">
<%
UserInfo userinfo = (UserInfo)request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日�
String dateTime = dateFm.format(new java.util.Date());

%>
<s:url id="s_submitURL" value="/ss/BINOLSSPRM51_JBPMSUBMIT" />
<span id ="submitURL" style="display:none">${s_submitURL}</span>
	  <div id="hidden1Result1Div1" style="display: none"></div>
      <div class="panel-header">
        <h2><s:text name="lbltitle"/></h2>
        <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="lbltitle"/> > <s:text name="lbltitle2"/></span> </div>
      </div>
      <s:if test="hasActionErrors()">
		<div class="actionError" id="errorMsgDivTOP">
		  	<s:actionerror/>
		</div>
	 </s:if>
      <s:if test="hasActionMessages()">
		<div class="actionSuccess" id="actionMsgDivTOP">
		  <s:actionmessage/>
		</div>
	 </s:if>	
	 <div id="actionResultDisplay"></div>
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
                        <input type="hidden" id="inOrganizationId" name="inOrganizationId" value="<s:property value='inOrganizationIdHid'/>"/>               
                    	<s:property value="inOrganizationNameHid" default="haha"/>
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
                </div>
              </div>
            </div>
        <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="lbldetail"/></strong></div>
       <%-- ================== 错误信息提示 START ======================= --%>
       <%-- ================== 错误信息提示   END  ======================= --%>
       <div class="section-content">
       <div class="toolbar clearfix">         
        </div>
         <div style="width:100%;overflow-x:scroll;">         
         <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
           <thead>
             <tr>              
               <th class="tableheader" width="5%">No.</th>
               <th class="tableheader" width="10%"><s:text name="lblDeliverNo"/></th>
               <th class="tableheader" width="10%"><s:text name="lblUnitCode"/></th>
               <th class="tableheader" width="10%"><s:text name="lblBarCode"/></th>
               <th class="tableheader" width="18%"><s:text name="lblProname"/></th>
               <th class="tableheader" width="5%"><s:text name="lblPrice"/></th>
               <th class="tableheader" width="12%"><s:text name="lblOutcount"/></th>
               <th class="tableheader" width="5%" style="display:none"><s:text name="lblIncount"/></th>
               <th class="tableheader" width="20%" style="display:none"><s:text name="lblReason"/></th>
               <th style="display:none">
             </tr>
           </thead>
           <tbody id="databody">
           <% int i = 0; %>
            <s:iterator value="deliverDataDetailList" >            
             <tr id="dataRow<%= i %>" > 
               <td id="dataTd0" style="text-align:center"><%= i+1 %></td>   
               <td id="dataTd1"><s:property value='DeliverReceiveNoIF'/></td>            
               <td id="dataTd2"><s:property value='UnitCode'/></td>
               <td id="dataTd3"><s:property value='BarCode' /></td>
               <td id="dataTd4"><s:property value='NameTotal'/></td>
               <td id="dataTd5"><s:text name="format.price"><s:param value="Price"></s:param></s:text></td>
               <td id="dataTd6" style="text-align:right"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></td>    
               <td id="dataTd7" style="display:none;text-align:right">
					<input type="text" id="quantityuArr"  name="quantityuArr" Class="text-number" size="10" maxlength="9" onchange="PRM51_changeCount(this)" value="<s:property value='Quantity' />" />
				</td>
               <td id="dataTd8" style="display:none"><s:textfield name="reasonArr" size="25" maxlength="200"/></td>             
               <td style="display:none" id="dataTd9">
                <input type="hidden" id="promotionDeliverIDArr" name="promotionDeliverIDArr" value="<s:property value='BIN_PromotionDeliverID'/>"/>
				<input type="hidden" id="outOrganizationIDArr" name="outOrganizationIDArr" value="<s:property value='BIN_OrganizationID'/>"/>
				<input type="hidden" id="deliverNoArr" name="deliverNoArrIF" value="<s:property value='DeliverReceiveNoIF'/>"/>
				<input type="hidden" id="outUpdateTimeArr" name="outUpdateTimeArr" value="<s:property value='UpdateTime'/>"/>
				<input type="hidden" id="outModifyCountArr" name="outModifyCountArr" value="<s:property value='ModifyCount'/>"/>
                <input type="hidden" id="stockInFlagArr" name="stockInFlagArr"  value="<s:property value='StockInFlag'/>"/>
                <input type="hidden" id="barCodeArr" name="barCodeArr"  value="<s:property value='BarCode' />"/>
                <input type="hidden" id="priceUnitArr" name="priceUnitArr" value="<s:property value='Price' />"/>
                <input type="hidden" id="taskInstanceIDArr" name="taskInstanceIDArr" value="<s:property value='TaskInstanceID' />"/>
				<input type="hidden" id="promotionProductVendorIDArr" name="promotionProductVendorIDArr"  value="<s:property value='BIN_PromotionProductVendorID' />"/>              
               </td>
             </tr>
             <%  i += 1; %>
            </s:iterator>   
           </tbody>
         </table>
         </div>
         <hr class="space" />
         <div class="center clearfix">          
         	<button class="confirm" type="button" onclick="submitForm()" id="btnOK" style="display:none"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnOK"/></span></button>
            <button class="close" onclick="ClosePopupForm();" id="btnClose"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="btnClose"/></span></button>
         </div>
       </div>
	<script type="text/javascript">
		$("#databody tr:odd").addClass("even");
		$("#databody tr:even").addClass("odd");
		var stockInFlag = $('#dataRow0 #stockInFlagArr').val();
		if(stockInFlag=='2'){
			$('#btnOK').show();
		}
		$("#csrftoken").val(window.opener.document.getElementById("csrftoken").value);
	</script>     
        </div>
</s:i18n>
</cherry:form>
<div id="errmessage" style="display:none">
	<input type="hidden" id="errmsg1" value='<s:text name="ESS00013"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="ESS00014"/>'/>
</div>
