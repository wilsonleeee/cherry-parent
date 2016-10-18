<!-- 生成的空白单 -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.st.BINOLSTSFH06">
 		<div  class="section">
 			 <div  style="width:100%;overflow-x:scroll;">
            <input type="hidden" id="rowNumber" value="1"/>
            <table id="blankTable" cellpadding="0" cellspacing="0" border="0" width="100%" class="background_1">
              <thead>
                <tr>
                  <th class="tableheader" width="3%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/>
                  <s:text name="btnallselect"/></th>
                   <th class="tableheader" width="15%" onclick="STSFH06_sortTable('_blankUnitArr');">
                  <div class="DataTables_sort_wrapper"><s:text name="lblcode"/><span id="_blankUnitArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                  <s:if test='"1".equals(configVal)'>
	                  <th class="tableheader" width="15%"><s:text name="erpCode"/></th>
                  </s:if>
                  <th class="tableheader" width="15%" onclick="STSFH06_sortTable('_blankBarArr');">
                  <div class="DataTables_sort_wrapper"><s:text name="lblbarcode"/><span id="_blankBarArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                  <th class="tableheader" width="18%"><s:text name="lblproname"/></th>
                  <th class="tableheader" width="5%"><s:text name="SFH06_ReferencePrice"/></th>
                  <th class="tableheader" width="5%"><s:text name="SFH06_costPrice"/></th><%--成本价 --%>
                  <th class="tableheader" width="5%"><s:text name="SFH06_totalCostPrice"/></th><%--总成本价 --%>
                  <th class="tableheader" width="5%">
                    <s:text name="lblprice"/>
                    <span class="calculator" onclick="BINOLSTSFH06.initRateDiv(this,'batch');" title="<s:text name="SFH06_BatchCalTitle"/>"></span>
                    <input type="hidden" id="batchPriceRate" value="100.00">
                  </th>
                  <th class="tableheader" width="5%"><s:text name="lblNowCount"/></th> 
                  <!-- 收货方库存 --> 
                  <th class="tableheader" width="5%"><s:text name="SFH06_ReceiveStock"/></th>                
                  <th class="tableheader" width="15%" onclick="STSFH06_sortTable('_quantityuArr');">
                  <div class="DataTables_sort_wrapper"><s:text name="lbloutcount"/><span id="_quantityuArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                  <th class="tableheader" width="10%"><div style="line-height:13px;"><s:text name="lblmoney"/><s:text name="lblmoneyu"/></div></th>
                  <th class="tableheader" width="20%"><s:text name="lblreason"/></th>
                  <th style="display:none">
                </tr>
              </thead>
              <tbody id="databody" >
               <s:iterator value="productList" id="productMap" status="rn">
                    <tr id='dataRow<s:property value="#rn.index+1"/>' >
                        <td class="center" id="dataTd0"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH06.changechkbox(this);"/></td>
                        <td id="dataTd1" style="white-space:nowrap"><s:property value='UnitCode' /><input type="hidden" id="unitCodeArr" value="<s:property value='UnitCode'/>"/></td>
                        <s:if test='"1".equals(configVal)'>
                        	<td class="center" id="dataTd12"><input type="text" name="erpCodeArr" maxlength="50"  id="erpCodeArr" value=""  size="25"></input></td>
                        </s:if>
                        <td id="dataTd2"><s:property value='BarCode' /><input type="hidden" id="barCodeArr" value="<s:property value='BarCode'/>"/></td>
                        <td id="dataTd3"><s:property value='prodcutName' /></td>
                        <!-- 执行价是否取会员价 -->
                        <s:if test="sysConfigUsePrice.equals('MemPrice')">
                            <td id="" style="text-align:right;"><s:text name="format.price"><s:param value="MemPrice"></s:param></s:text></td>
                            <!-- 平均成本价 -->
                            <s:if test="null != costPrice">
                            	<td style="text-align:right;"><div id="costPrice"><s:param value="costPrice"></s:param></div></td>
                            </s:if>
                            <s:else>
                            	<td style="text-align:right;"><div id="costPrice"></div></td>
                            </s:else>
                            <!-- 总成本价 -->
                            <s:if test="null != totalCostPrice">
                            	<td style="text-align:right;"><div id="totalCostPrice"><s:param value="totalCostPrice"></s:param></div></td>
                            </s:if>
                            <s:else>
                            	<td style="text-align:right;"><div id="totalCostPrice"></div></td>
                            </s:else>
                            <s:if test="useCostPrice.equals('1')">
                            	<s:if test="null != costPrice">                            		
                            		<td id="dataTd4" style="text-align:right;"><input type="text" id="priceUnitArr" name="priceUnitArr"   value="<s:property value='costPrice'/>"  onchange="BINOLSTSFH06.setPrice(this);"/></td>
                            	</s:if>
                            	<s:else>
                            		<td id="dataTd4" style="text-align:right;"><input type="text" id="priceUnitArr" name="priceUnitArr" class="price"  value="0.00"  onchange="BINOLSTSFH06.setPrice(this);"/></td>
                            	</s:else>
                        	</s:if>
                        	<s:else>
                        		<td id="dataTd4" style="text-align:right;"><input type="text" id="priceUnitArr" name="priceUnitArr"   value="<s:property value='MemPrice'/>"  onchange="BINOLSTSFH06.setPrice(this);"/></td>
                        	</s:else>
                        </s:if>
                        <s:elseif test="sysConfigUsePrice.equals('StandardCost')">
                        	<td id="" style="text-align:right;"><s:text name="format.price"><s:param value="StandardCost"></s:param></s:text></td>
                        	<!-- 平均成本价 -->
                            <s:if test="null != costPrice">
                            	<td style="text-align:right;"><div id="costPrice"><s:param value="costPrice"></s:param></div></td>
                            </s:if>
                            <s:else>
                            	<td style="text-align:right;"><div id="costPrice"></div></td>
                            </s:else>
                            <!-- 总成本价 -->
                            <s:if test="null != totalCostPrice">
                            	<td style="text-align:right;"><div id="totalCostPrice"><s:param value="totalCostPrice"></s:param></div></td>
                            </s:if>
                            <s:else>
                            	<td style="text-align:right;"><div id="totalCostPrice"></div></td>
                            </s:else>
                            <s:if test="useCostPrice.equals('1')">
                            	<s:if test="null != costPrice">                            		
                            		<td id="dataTd4" style="text-align:right;"><input type="text" id="priceUnitArr" name="priceUnitArr"  value="<s:property value='costPrice'/>"  onchange="BINOLSTSFH06.setPrice(this);"/></td>
                            	</s:if>
                            	<s:else>
                            		<td id="dataTd4" style="text-align:right;"><input type="text" id="priceUnitArr" name="priceUnitArr" class="price"  value="0.00"  onchange="BINOLSTSFH06.setPrice(this);"/></td>
                            	</s:else>
                        	</s:if>
                        	<s:else>
                           	 	<td id="dataTd4" style="text-align:right;"><input type="text" id="priceUnitArr" name="priceUnitArr"  value="<s:property value='StandardCost'/>"  onchange="BINOLSTSFH06.setPrice(this);"/></td>
                        	</s:else>
                        </s:elseif>
                        <!-- 暂时还不支持采购价 -->
                        <%-- <s:elseif test="sysConfigUsePrice.equals('OrderPrice')">
                        	<td id="" style="text-align:right;"><s:text name="format.price"><s:param value="OrderPrice"></s:param></s:text></td>
                            <td id="dataTd4" style="text-align:right;"><input type="text" id="priceUnitArr" name="priceUnitArr" class="price"  value="<s:property value='OrderPrice'/>"  onchange="BINOLSTSFH06.setPrice(this);"/></td>
                        </s:elseif> --%>
                         <!-- 执行价是否取销售价 -->
                        <s:else>
                            <td id="" style="text-align:right;"><s:text name="format.price"><s:param value="SalePrice"></s:param></s:text></td>
                            <!-- 平均成本价 -->
                            <s:if test="null != costPrice">
                            	<td style="text-align:right;"><div id="costPrice"><s:param value="costPrice"></s:param></div></td>
                            </s:if>
                            <s:else>
                            	<td style="text-align:right;"><div id="costPrice"></div></td>
                            </s:else>
                            <!-- 总成本价 -->
                            <s:if test="null != totalCostPrice">
                            	<td style="text-align:right;"><div id="totalCostPrice"><s:param value="totalCostPrice"></s:param></div></td>
                            </s:if>
                            <s:else>
                            	<td style="text-align:right;"><div id="totalCostPrice"></div></td>
                            </s:else>
                            <s:if test="useCostPrice.equals('1')">
                            	<s:if test="null != costPrice">                            		
                            		<td id="dataTd4" style="text-align:right;"><input type="text" id="priceUnitArr" name="priceUnitArr"  value="<s:property value='costPrice'/>"  onchange="BINOLSTSFH06.setPrice(this);"/></td>
                            	</s:if>
                            	<s:else>
                            		<td id="dataTd4" style="text-align:right;"><input type="text" id="priceUnitArr" name="priceUnitArr" class="price"  value="0.00"  onchange="BINOLSTSFH06.setPrice(this);"/></td>
                            	</s:else>
                        	</s:if>
                        	<s:else>
                            	<td id="dataTd4" style="text-align:right;"><input type="text" id="priceUnitArr" name="priceUnitArr"   value="<s:property value='SalePrice'/>"  onchange="BINOLSTSFH06.setPrice(this);"/></td>
                       		</s:else>
                        </s:else>
                        <td id="nowCount" style="text-align:right;"><s:text name="format.number"><s:param value='StockQuantity'/></s:text></td>
                        <!-- 收货方库存 -->
                        <td id="receiveStock" style="text-align:right;"><s:text name="format.number"><s:param value='receiveStockQuantity'/></s:text></td>
                        <td class="center" id="dataTd7"><s:textfield name="format.number"  cssClass="text-number" cssStyle="width:120px;" name="quantityuArr" id="quantityuArr" cssClass="text-number" size="10" maxlength="9" onkeyup="STSFH06_calcuAmount(this)" onchange="STSFH06_calcuAmount(this)"  value="%{Quantity}"/>
                        </td>
		                <td id="money" style="text-align:right;">
                            <s:if test="sysConfigUsePrice.equals('MemPrice')">
                                <s:if test="null != MemAmount"><s:text name="format.price"><s:param value="MemAmount"></s:param></s:text></s:if>
                                <s:else>0.00</s:else>
                            </s:if>
                            <s:elseif test="sysConfigUsePrice.equals('StandardCost')">
                            	<s:if test="null != StandardCostAmount"><s:text name="format.price"><s:param value="StandardCostAmount"></s:param></s:text></s:if>
                                <s:else>0.00</s:else>
                            </s:elseif>
                            <!-- 采购价暂时未予支持 -->
                            <%-- <s:elseif test="sysConfigUsePrice.equals('OrderPrice')">
                            	<s:if test="null != Amount"><s:text name="format.price"><s:param value="OrderAmount"></s:param></s:text></s:if>
                                <s:else>0.00</s:else>
                            </s:elseif> --%>
                            <s:else>
                                <s:if test="null != Amount"><s:text name="format.price"><s:param value="Amount"></s:param></s:text></s:if>
                                <s:else>0.00</s:else>
                            </s:else>
                        </td>
		                <td class="center" id="dataTd10"><input type="text" name="reasonArr" maxlength="200"  id="reasonArr" value=""  size="25"></input></td>
                        <td style="display:none" id="dataTd11">
                            <input type="hidden"  name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>">
		                    <%--<input type="hidden" id="priceUnitArr<s:property value="#rn.index+1"/>" name="priceUnitArr" value="<s:property value='SalePrice'/>"/> --%>
		                    <s:if test="sysConfigUsePrice.equals('MemPrice')">
                                <input type="hidden" id="referencePriceArr<s:property value="#rn.index+1"/>" name="referencePriceArr" value="<s:property value='MemPrice'/>"/>
		                    </s:if>
		                    <s:elseif test="sysConfigUsePrice.equals('StandardCost')">
	                        	<input type="hidden" id="referencePriceArr<s:property value="#rn.index+1"/>" name="referencePriceArr" value="<s:property value='StandardCost'/>"/>
	                        </s:elseif>
		                    <s:else>
		                      <input type="hidden" id="referencePriceArr<s:property value="#rn.index+1"/>" name="referencePriceArr" value="<s:property value='SalePrice'/>"/>
		                    </s:else>
			                <%--<input type="hidden" id="nowCountArr<s:property value="#rn.index+1"/>"" name="nowCountArr" value="<s:property value='Quantity'/>"/>--%>
                            <input type="hidden" id="productVendorIDArr<s:property value="#rn.index+1"/>" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
                            <s:if test="costPriceArr != null">
                            	<input type="hidden" id="costPriceArr" name="costPriceArr" value="<s:property value='costPriceArr'/>"/>
                            </s:if>
                            <s:else>
                            	<input type="hidden" id="costPriceArr" name="costPriceArr" value=""/>
                            </s:else>
                            <%--<input type="hidden" id="priceTotalArr<s:property value="#rn.index+1"/>" name="priceTotalArr"/>--%>
                        </td>
                    </tr>
                </s:iterator>
              </tbody>

            </table>
            </div>
         </div> 
</s:i18n>