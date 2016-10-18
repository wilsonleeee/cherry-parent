<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
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
 * 点击确定按钮
 */
function submitForm(){
	var params=$('#mainForm').formSerialize();
	cherryAjaxRequest({
		url:$("#s_agree").html(),
		param:params,
		callback:submitSuccessJbpm
	});
}

function submitSuccessJbpm(msg){
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$("#btnAgree").hide();
	}
	submitflag =true;
}

function ClosePopupForm(){
	var ob = window.opener.document.getElementById("currentUnitID")
	if(ob){
		if(ob.value=='TOP'){
			window.opener.refreshTaskList();
		}
	}
	window.close();
}
</script>
<s:i18n name="i18n.ss.BINOLSSPRM20">
<cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
<s:url id="url_agree" value="/ss/BINOLSSPRM20_JBPMSUBMIT" />
<span id ="s_agree" style="display:none">${url_agree}</span>

<div class="main container clearfix" style="width:90%">

<div class="panel ui-corner-all" >
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
      <div class="panel-content">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          </div>
          <div class="section-content">            
           <input type="hidden" id="tradeStatusFlag" value='<s:property value="returnInfo.TradeStatus"/>'/>
            <input type="hidden" id="verifiedFlag" value='<s:property value="returnInfo.verifiedFlag"/>'/>
            <input type="hidden" value="" name="csrftoken" id="csrftoken">
            <input type="hidden" name="outOrganizationId" id="outOrganizationId" value='<s:property value="returnInfo.BIN_OrganizationIDAccept"/>'/>
            <input type="hidden" name="proAllocationId" id="proAllocationId" value="<s:property value='proAllocationId'/>"/>
            <input type="hidden" name="taskInstanceID" id="taskInstanceID" value="<s:property value='taskInstanceID'/>"/>
              <div class="table clearfix" style="margin-bottom: 0">
              <div class="tr">
              	<%-- 调拨单号 --%>
                <div class="th"><s:text name="PRM20_allocationNoIF"/></div>
                <div class="td">
                    <s:if test='%{returnInfo.AllocationNoIF != null && !"".equals(returnInfo.AllocationNoIF)}'>
                        <s:property value="returnInfo.AllocationNoIF"/>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </div>
                <%-- 关联单号 --%>
                <div class="th"><s:text name="PRM20_relevanceNo"/></div>
                <div class="td">
                    <s:if test='%{returnInfo.RelevanceNo != null && !"".equals(returnInfo.RelevanceNo)}'>
                        <s:property value="returnInfo.RelevanceNo"/>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </div>
              </div>  
               <div class="tr">
              	<%-- 序号 --%>
                <div class="th"><s:text name="PRM20_allocationNo"/></div>
                <div class="td">
                    <s:if test='%{returnInfo.AllocationNo != null && !"".equals(returnInfo.AllocationNo)}'>
                        <s:property value="returnInfo.AllocationNo"/>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </div>
                <%-- 调拨日期 --%>
                <div class="th"><s:text name="PRM20_date"/></div>
                <div class="td">
                    <s:if test='%{returnInfo.allocationDate != null && !"".equals(returnInfo.allocationDate)}'>
                        <s:property value="returnInfo.allocationDate"/>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </div>
              </div>  
              <div class="tr">
              	<%-- 申请部门 --%>
                <div class="th"><s:text name="PRM20_sendOrg"/></div>
                <div class="td">
                    <s:if test='%{returnInfo.sendDepart != null && !"".equals(returnInfo.sendDepart)}'>
                        <s:property value="returnInfo.sendDepart"/>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </div>
                 <%-- 操作员 --%>
                <div class="th"><s:text name="PRM20_employeeName"/></div>
                <div class="td">
                    <s:if test='%{returnInfo.employeeName != null && !"".equals(returnInfo.employeeName)}'>
                        <s:property value="returnInfo.employeeName"/>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </div>               
              </div>
              <div class="tr">
                <%-- 调出部门 --%>
                <div class="th"><s:text name="PRM20_receiveOrg"/></div>
                <div class="td">
                    <s:if test='%{returnInfo.receiveDepart != null && !"".equals(returnInfo.receiveDepart)}'>
                        <s:property value="returnInfo.receiveDepart"/>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </div>
              	<%-- 调出仓库 --%>
              	<div class="th"><s:text name="lblOutDepot"/></div>
                <div class="td">
                	<select style="width:120px;" name="outDepotId" id="outDepotId" >		                  		
	                  		<s:iterator value="outDepotList">
				         		<option value="<s:property value="BIN_InventoryInfoID" />">
				         			<s:property value="InventoryName"/>
				         		</option>
				      		</s:iterator>
	              	</select>
                </div>
              </div>
             </div>
             <div class="table" style="border-top:none;">
             	<div class="tr">
	             	<%-- 调拨理由 --%>
	               <div class="th"><s:text name="PRM20_reason"/></div>
	               <div class="td" style="width: 85%;">
                        <s:if test='%{returnInfo.reason != null && !"".equals(returnInfo.reason)}'>
                            <s:property value="returnInfo.reason"/>
                        </s:if>
                        <s:else>&nbsp;</s:else>
                   </div>
	            </div>
             </div>
            
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 调拨明细一览 --%>
            <s:text name="PRM20_results_list"/>
            </strong>
          </div>
          <div class="section-content">          	
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
                       <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
           <thead>
             <tr>              
               <th class="tableheader" width="5%">No.</th>
               <th class="tableheader" width="10%"><s:text name="lblUnitCode"/></th>
               <th class="tableheader" width="10%"><s:text name="lblBarcode"/></th>
               <th class="tableheader" width="18%"><s:text name="lblProname"/></th>
               <th class="tableheader" width="5%"><s:text name="lblPrice"/></th>
               <th class="tableheader" width="12%"><s:text name="lblRequestcount"/></th>
               <th class="tableheader" width="5%" style="display:none"><s:text name="lblOutcount"/></th>
               <th class="tableheader" width="5%" ><s:text name="lblMoney"/></th>
               <th class="tableheader" width="20%"><s:text name="lblReason"/></th>
               <th style="display:none">
             </tr>
           </thead>
           <tbody id="databody">
           <% int i = 0; %>
            <s:iterator value="allocationDetailList" >            
             <tr id="dataRow<%= i %>" > 
               <td id="dataTd0" style="text-align:center"><%= i+1 %></td>           
               <td id="dataTd2"><s:property value='UnitCode'/></td>
               <td id="dataTd3"><s:property value='BarCode' /></td>
               <td id="dataTd4"><s:property value='NameTotal'/></td>
               <td id="dataTd5"><s:property value='Price'/></td>
               <td id="dataTd6" style="text-align:right"><s:property value='Quantity' /></td> 
               <td id="dataTd6" style="text-align:right"><s:text name="format.price"><s:param value="Price * Quantity"></s:param></s:text></td>      
               <td id="dataTd7" style="text-align:right;display:none">
					<input type="text" id="quantityuArr"  name="quantityuArr" Class="text-number" size="10" maxlength="9" onchange="changeCount(this)" value="<s:property value='Quantity' />" />
				</td>
               <td id="dataTd8">
               	<s:property value='Reason' />
               	<s:textfield name="reasonArr" size="25" maxlength="200" cssStyle="display:none"/>
               </td>             
               <td style="display:none" id="dataTd9">
                <input type="hidden" id="allocationIDArr" name="allocationIDArr" value="<s:property value='BIN_PromotionAllocationID'/>"/>
                 <input type="hidden" id="relevanceNoArr" name="relevanceNoArrIF" value="<s:property value='AllocationNoIF'/>"/>
                 <input type="hidden" id="inventoryIDArr" name="inventoryIDArr" value="<s:property value='BIN_InventoryInfoID'/>"/>
                 <input type="hidden" id="inOrganizationIDArr" name="inOrganizationIDArr" value="<s:property value='BIN_OrganizationID'/>"/>
				<input type="hidden" id="outUpdateTimeArr" name="outUpdateTimeArr" value="<s:property value='UpdateTime'/>"/>
				<input type="hidden" id="outModifyCountArr" name="outModifyCountArr" value="<s:property value='ModifyCount'/>"/>
                <input type="hidden" id="priceUnitArr" name="priceUnitArr" value="<s:property value='Price' />"/>
				<input type="hidden" id="promotionProductVendorIDArr" name="promotionProductVendorIDArr"  value="<s:property value='BIN_PromotionProductVendorID' />"/>              
                <input type="hidden" id="taskInstanceIDArr" name="taskInstanceIDArr" value="<s:property value='TaskInstanceID' />"/>
               </td>
             </tr>
             <%  i += 1; %>
            </s:iterator>   
           </tbody>
         </table>
           	</div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="PRM20_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="PRM20_totalAmount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td class="center"><s:text name="format.number"><s:param value="returnInfo.totalQuantity"></s:param></s:text></td>
             	<td class="center"><s:text name="format.price"><s:param value="returnInfo.totalAmount"></s:param></s:text></td>
              </tr>
            </table>
            <hr class="space" />
            
            <div id="buttonarea1" class="center">
            <%-- 调出确认 --%>
             <button type="button" id="btnAgree" class="confirm" onclick="submitForm();"><span class="ui-icon icon-confirm"></span>              
              <span class="button-text"><s:text name="btnAgree"/></span>
             </button>            
             <%-- 关闭 --%>
              <button type="button" id="btnDonothing" class="close" onclick="ClosePopupForm();"><span class="ui-icon icon-close"></span>              
              <span class="button-text"><s:text name="btnDonothing"/></span>
             </button>
            </div>            
          </div>
        </div>
      </div>
    </div>
    </div>
    <script type="text/javascript">
    var value =$('#tradeStatusFlag').val();
    if(value!='1'){
        $('#btnAgree').hide();      
    }
    $("#csrftoken").val(window.opener.document.getElementById("csrftoken").value);
    </script>
    </cherry:form>
</s:i18n>
