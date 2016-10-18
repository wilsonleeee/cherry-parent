<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%> 

<%@ page import="java.util.List" %>

<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>

<script language="javascript">
</script>
<s:i18n name="i18n.ss.BINOLSSPRM50">
<s:url id="s_saveURL" value="/ss/BINOLSSPRM50_SAVE" />
<span id ="saveURL" style="display:none">${s_saveURL}</span>
       <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="lbldetail"/></strong></div>
       <div class="section-content">
       <div class="toolbar clearfix">         
        </div>
         <div style="width:100%;overflow-x:scroll;">
         <input type="hidden" id="rowNumber" value="1"/>
         <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
           <thead>
             <tr>
             <th class="tableheader" width="3%" style="display:none"><s:text name="lblselect"/></th>
               <th class="tableheader" width="5%">No.</th>
               <th class="tableheader" width="10%"><s:text name="lblcode"/></th>
               <th class="tableheader" width="10%"><s:text name="lblbarcode"/></th>
               <th class="tableheader" width="18%"><s:text name="lblproname"/></th>
               <th class="tableheader" width="5%"><s:text name="lblprice"/></th>
               <th class="tableheader" width="12%"><s:text name="lblrecdepartment"/></th>
               <th class="tableheader" style="display:none"><s:text name="lblpackagingtype"/></th>
               <th class="tableheader" width="5%"><div style="line-height:13px;"><s:text name="lbloutcount"/><br /><s:text name="lbloutcountb"/></div></th>
               <th class="tableheader" style="display:none"><div style="line-height:13px;"><s:text name="lbloutcount"/><br /><s:text name="lbloutcountb"/></div></th>
               <th class="tableheader" width="10%"><div style="line-height:13px;"><s:text name="lblmoney"/><br /><s:text name="lblmoneyu"/></div></th>
               <th class="tableheader" width="20%"><s:text name="lblreason"/></th>
               <th style="display:none">
             </tr>
           </thead>
           <tbody id="databody">
            <s:iterator value="%{#request.parseddata}" id="parseMap" status="rn">
                <tr id='dataRow<s:property value="#rn.index"/>'>
                    <td id="dataTd0" style="text-align:center"><s:property value="#rn.index+1"/></td>                
                    <td id="dataTd1" style="white-space:nowrap"><s:property value="#parseMap.UnitCode"/></td>               
                    <td id="dataTd2">
                        <s:if test='#parseMap.BarCode != null && !"".equals(#parseMap.BarCode)'>
                            <s:property value="#parseMap.BarCode"/>
                        </s:if>
                    </td>
                    <td id="dataTd3">
                        <s:if test='#parseMap.PromotionName != null && !"".equals(#parseMap.PromotionName)'>
                            <s:property value="#parseMap.PromotionName"/>
                        </s:if>
                    </td>
                    <td id="dataTd4" style="text-align:right;">
                        <s:if test='#parseMap.SalePrice != null && !"".equals(#parseMap.SalePrice)'>
                            <s:property value="#parseMap.SalePrice"/>
                        </s:if>
                    </td>
                    <td id="dataTd5">
                        <s:if test='#parseMap.DepartName != null && !"".equals(#parseMap.DepartName)'>
                            <s:property value="#parseMap.DepartName"/>
                        </s:if>
                    </td>    
                    <td id="dataTd7"><input type="text" id="quantityuArr"  name="quantityuArr" Class="text-number" size="10" maxlength="9" onchange="changeCount(this)" value='<s:property value="#parseMap.quantity"/>' /></td>
                    <td id="dataTd8" style="text-align:right;"><s:property value="#parseMap.RowTotalMoney"/></td>
                    <td id="dataTd10"><s:select name="reasonArr" list='#application.CodeTable.getCodes("1051")' listKey="CodeKey" listValue="Value"/></td>
                    <td style="display:none" id="dataTd11">
                    <input type="hidden" id="inOrganizationIDArr" name="inOrganizationIDArr" value='<s:property value="#parseMap.InOrganizationID"/>'/>
                    <input type="hidden" id="unitCodeArr" name="unitCodeArr"  value='<s:property value="#parseMap.UnitCode"/>'/>
                    <input type="hidden" id="barCodeArr" name="barCodeArr"  value='<s:property value="#parseMap.BarCode"/>'/>
                    <input type="hidden" id="priceUnitArr" name="priceUnitArr"  value='<s:property value="#parseMap.SalePrice"/>'/>
                    <input type="hidden" id="deliverReceiveNoIFArr" name="deliverReceiveNoIFArr"  value='<s:property value="#parseMap.DeliverReceiveNoIF"/>'/>
                    <input type="hidden" id="priceTotalArr" name="priceTotalArr"/>
                    <input type="hidden" id="promotionProductVendorIDArr" name="promotionProductVendorIDArr"  value='<s:property value="#parseMap.BIN_PromotionProductVendorID"/>'/>
               </td>
             </tr>            
           </s:iterator>
                    
           </tbody>
         </table>
         </div>
         <hr class="space" />
         <div class="center clearfix">
            <button class="save" type="button" onclick="submitFormSave();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="btnSave"/></span></button>
         	<button class="confirm" type="button" onclick="submitFormSend();return false;"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnOK"/></span></button>
         </div>
       </div>
	<script type="text/javascript">
		$("#databody tr:odd").addClass("even");
		$("#databody tr:even").addClass("odd");
	</script>
</s:i18n>