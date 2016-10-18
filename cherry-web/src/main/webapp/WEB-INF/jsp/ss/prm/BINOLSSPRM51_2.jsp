<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%> 

<%@ page import="java.util.List" %>

<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>

<script language="javascript">
</script>
<s:i18n name="i18n.ss.BINOLSSPRM51">
	   <div id="hidden1Result1Div1" style="display: none"></div>
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
               <td id="dataTd1"><s:property value='DeliverReceiveNoIF' /></td>            
               <td id="dataTd2"><s:property value='UnitCode'/></td>
               <td id="dataTd3"><s:property value='BarCode' /></td>
               <td id="dataTd4"><s:property value='NameTotal'/></td>
               <td id="dataTd5" style="text-align:right"><s:text name="format.price"><s:param value="Price"></s:param></s:text></td>
               <td id="dataTd6" style="text-align:right"><s:property value='Quantity' /></td>    
               <td id="dataTd7" style="display:none;text-align:right" >
					<input type="text" id="quantityuArr"  name="quantityuArr" Class="text-number" size="10" maxlength="9" onchange="PRM51_changeCount(this)" value="<s:property value='Quantity' />" />
				</td>
               <td id="dataTd8" style="display:none"><s:textfield name="reasonArr" size="25" maxlength="200"/></td>             
               <td style="display:none" id="dataTd9">
                <input type="hidden" id="promotionDeliverIDArr" name="promotionDeliverIDArr" value="<s:property value='BIN_PromotionDeliverID'/>"/>
				<input type="hidden" id="outOrganizationIDArr" name="outOrganizationIDArr" value="<s:property value='BIN_OrganizationID'/>"/>
				<input type="hidden" id="deliverNoArr" name="deliverNoArrIF" value="<s:property value='DeliverReceiveNoIF'/>"/>
				<input type="hidden" id="outUpdateTimeArr" name="outUpdateTimeArr" value="<s:property value='UpdateTime'/>"/>
				<input type="hidden" id="outModifyCountArr" name="outModifyCountArr" value="<s:property value='ModifyCount'/>"/>
                <input type="hidden" id="unitCodeArr" name="unitCodeArr"  value="<s:property value='UnitCode'/>"/>
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
         	<button class="confirm" type="button" onclick="submitForm51()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnOK"/></span></button>
         </div>
       </div>
	<script type="text/javascript">
		$("#databody tr:odd").addClass("even");
		$("#databody tr:even").addClass("odd");
	</script>
</s:i18n>