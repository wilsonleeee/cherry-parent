<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%> 

<%@ page import="java.util.List" %>

<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>

<script language="javascript">
</script>
<s:i18n name="i18n.ss.BINOLSSPRM20">
	   <div id="hidden1Result1Div1" style="display: none"></div>
       <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="lblDetail"/></strong></div>
		  <div class="section-content">          
            <div style="width:100%;overflow-x:scroll;">
           <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
           <thead>
             <tr>              
               <th class="tableheader" width="5%">No.</th>
               <th class="tableheader" width="10%"><s:text name="lblAllocationNo"/></th>
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
               <td id="dataTd1"><s:property value='AllocationNoIF' /></td>            
               <td id="dataTd2"><s:property value='UnitCode'/></td>
               <td id="dataTd3"><s:property value='BarCode' /></td>
               <td id="dataTd4"><s:property value='NameTotal'/></td>
               <td id="dataTd5"><s:property value='Price'/></td>
               <td id="dataTd6" style="text-align:right"><s:property value='Quantity' /></td>    
               <td id="dataTd7" style="text-align:right;">
               		<s:text name="format.price"><s:param value="Price * Quantity"></s:param></s:text>
					<input type="text" id="quantityuArr"  style="display:none" name="quantityuArr" Class="text-number" size="10" maxlength="9" onchange="changeCount(this)" value="<s:property value='Quantity' />" />
				</td>
               <td id="dataTd8"><s:property value='Reason' />
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
            <div class="center clearfix">            	
            	<button class="confirm" type="button" onclick="SSPRM20_submitFormSave()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnOK"/></span></button>
            </div>
          </div>
	<script type="text/javascript">
		$("#databody tr:odd").addClass("even");
		$("#databody tr:even").addClass("odd");
	</script>
</s:i18n>