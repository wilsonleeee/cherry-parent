<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%>
<script language="javascript">
</script>
<s:i18n name="i18n.ss.BINOLSSPRM22">
	   <div id="hidden1Result1Div1" style="display: none"></div>
       <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="lbldetail"/></strong></div>
       <div class="section-content">
       <div class="toolbar clearfix">         
        </div>
         <div style="width:100%;overflow-x:scroll;">
         <input type="hidden" id="rowNumber" value="1"/>
         <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
           <thead>
             <tr>
               <th class="tableheader" width="5%">No.</th>
               <th class="tableheader" width="10%"><s:text name="lblCode"/></th>
               <th class="tableheader" width="10%"><s:text name="lblBarcode"/></th>
               <th class="tableheader" width="18%"><s:text name="lblProname"/></th>
               <th class="tableheader" width="5%"><s:text name="lblPrice"/></th>
               <th class="tableheader" width="12%"><s:text name="lblBookCount"/></th>            
               <th class="tableheader" width="5%"><s:text name="lblCheckCount"/></th>
               <th class="tableheader" width="5%"><s:text name="lblGainQuantity"/></th>
               <th class="tableheader" width="10%"><s:text name="lblMoney"/></th>
               <th class="tableheader" width="20%"><s:text name="lblReason"/></th>
               <th style="display:none">
             </tr>
           </thead>
               <tbody id="databody">
           <% int i = 0; %>
            <s:iterator value="stockPromotionList" >            
             <tr id="dataRow<%= i %>" > 
               <td  style="text-align:center"><%= i+1 %></td>   
               <td><s:property value='UnitCode' /></td>            
               <td><s:property value='BarCode'/></td>
               <td><s:property value='promotionName' /></td>
               <td><s:property value='SalePrice'/></td>
               <td id="dataTd5" style="text-align:right"><s:property value='Quantity'/></td>   
               <td id="dataTd6" style="text-align:right">
					<input type="text" id="quantityuArr<%= i %>"  name="quantityuArr" Class="text-number" size="10" maxlength="9" onchange="SSPRM22_changeCount(this,'<%= i %>')" />
				</td>
				<td id="count<%= i %>" style="text-align:right"></td>
				<td id="money<%= i %>" style="text-align:right"></td>
               <td><s:textfield name="reasonArr" size="25" maxlength="200"/></td>             
               <td style="display:none">
                <input type="hidden" id="bookCount<%= i %>" name="bookCountArr" value="<s:property value='Quantity' />"/>
				<input type="hidden" id="gainCount<%= i %>" name="gainCountArr" />
				<input type="hidden" id="gainMoney<%= i %>" name="gainMoneyArr" />
                <input type="hidden" id="priceUnit<%= i %>" name="priceUnitArr" value="<s:property value='SalePrice' />"/>
				<input type="hidden" id="promotionProductVendorID<%= i %>" name="promotionProductVendorIDArr"  value="<s:property value='BIN_PromotionProductVendorID' />"/>              
               </td>
             </tr>
             <%  i += 1; %>
            </s:iterator>   
           </tbody>
         </table>
         </div>
         <hr class="space" />
         <div class="center clearfix">            
            <button class="confirm" type="button" onclick="SSPRM22_submitFormSave()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnOK"/></span></button>
         </div>      
       </div>
	<script type="text/javascript">
		$("#databody tr:odd").addClass("even");
		$("#databody tr:even").addClass("odd");
	</script>
</s:i18n>