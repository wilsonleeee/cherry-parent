<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:i18n name="i18n.wp.BINOLWPSAL07">
<div class="hide">
	<input name="returnbussinessDateStart" id="returnbussinessDateStart" value='<s:property value="returnbussinessDateStart"/>'/>
	<input name="returnbussinessDateEnd" id="returnbussinessDateEnd" value='<s:property value="returnbussinessDateEnd"/>'/>
</div>

<s:if test="billDetailList!=null">
<div id="getBillDetailData">
	<s:iterator value="billDetailList" id="getBillDetail">
		<s:if test="'SR'.equals(saleType)">
		<tr class="red">
		</s:if>
		<s:else>
			<s:if test="'CXHD'.equals(activityTypeCode)">
				<s:if test="productVendorId != -9999">
					<tr class="green">
				</s:if>
				<s:else>
					<tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
			</s:else>
		</s:else>
			<s:if test="'CXHD'.equals(activityTypeCode)">
				<s:if test="productVendorId != -9999">
					<td><s:text name="wpsal07.promotionDetailTitle"/><s:property value="unitCode" /></td>
					<td><s:property value="barCode" /></td>
					<td><s:property value="productName" /></td>
				</s:if>
				<s:else>
					<td><s:text name="wpsal07.promotionInfoTitle"/></td>
					<td style="white-space: normal; line-height: 1.5;"><s:property value="barCode" /><s:text name="wpsal07.promotionCodeTitle"/></td>
					<td style="white-space: normal; line-height: 1.5;"><s:property value="productName" /><s:text name="wpsal07.promotionNameTitle"/></td>
				</s:else>
			</s:if>
			<s:else>
				<td><s:property value="unitCode" /></td>
				<td><s:property value="barCode" /></td>
				<td><s:property value="productName" /></td>
			</s:else>
			<s:if test="'DETAIL'.equals(billDetailShowType)">
			<td>
				<s:if test="'SR'.equals(saleType)">
					<s:text name="wpsal07.billStateSr" />
				</s:if>
				<s:else>
					<s:text name="wpsal07.billStateNs" />
				</s:else>
			</td>
			<td><%-- 数量 --%>
				<s:if test="quantity != null">
					<s:if test="quantity >= 0"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</td>
			</s:if>
			<td><%-- 原价 --%>
				<s:if test="price != null">
					<s:if test="price >= 0"><s:text name="format.price"><s:param value="price"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="price"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</td>
			<td><%-- 会员价 --%>
				<s:if test="memberPrice != null">
					<s:if test="memberPrice >= 0"><s:text name="format.price"><s:param value="memberPrice"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="memberPrice"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</td>
			<s:if test="isPlatinumPrice=='Y'.toString()">
				<td><%-- 白金价 --%>
					<s:if test="platinumPrice != null">
						<s:if test="platinumPrice >= 0"><s:text name="format.price"><s:param value="platinumPrice"></s:param></s:text></s:if>
						<s:else><span class="highlight"><s:text name="format.price"><s:param value="platinumPrice"></s:param></s:text></span></s:else>
					</s:if>
					<s:else>&nbsp;</s:else>
				</td>
			</s:if>
			<td><%-- 折扣率 --%>
				<s:if test="discountRate != null">
					<s:if test="discountRate >= 0"><s:text name="format.price"><s:param value="discountRate"></s:param></s:text>%</s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="discountRate"></s:param></s:text>%</span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</td>
			<td><%-- 实价 --%>
				<s:if test="realPrice != null">
					<s:if test="realPrice >= 0"><s:text name="format.price"><s:param value="realPrice"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="realPrice"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</td>
			<td><%-- 折扣率 --%>
				<s:if test="amount != null">
					<s:if test="amount >= 0"><s:text name="format.price"><s:param value="amount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="amount"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</td>
			<s:if test="!'DETAIL'.equals(billDetailShowType)">
			<td><%-- 可退货数量 --%>
				<s:if test="quantity != null">
					<s:if test="quantity >= 0"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</td>
			<td>
				<s:if test="!'CXHD'.equals(activityTypeCode) && !'ZDZK'.equals(activityTypeCode) && !'ZDQL'.equals(activityTypeCode) && !'DXJE'.equals(activityTypeCode)">
					<input id="returnsQuantity" name="returnsQuantity" type="text" class="text" style="width:50px;margin:.2em 0;" maxlength="7" value="<s:property value='quantity'/>"  onchange="BINOLWPSAL07.changeSrQuantity(this)" onkeyup="BINOLWPSAL07.changeSrQuantity(this)" />
				</s:if>
				<s:else>
					<s:if test="productVendorId == -9999">
						<input id="returnsQuantity" name="returnsQuantity" type="text" class="text" style="width:50px;margin:.2em 0;" maxlength="7" value="<s:property value='quantity'/>"  onchange="BINOLWPSAL07.changeSrQuantity(this)" onkeyup="BINOLWPSAL07.changeSrQuantity(this)" />
					</s:if>
					<s:elseif test="'DXJE'.equals(activityTypeCode)">
						<input id="returnsQuantity" name="returnsQuantity" type="hidden" value="1" />
					</s:elseif>
				</s:else>
			</td>
			<td>
				<s:if test="!'CXHD'.equals(activityTypeCode) && !'ZDZK'.equals(activityTypeCode) && !'ZDQL'.equals(activityTypeCode) && !'DXJE'.equals(activityTypeCode)">
					<span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL07.deleteRow(this);return false;"></button></span>
				</s:if>
				<s:else>
					<s:if test="productVendorId == -9999">
						<span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL07.deleteRow(this);return false;"></button></span>
					</s:if>
				</s:else>
				<input type="hidden" id="dgsaleId" name="dgsaleId" value="<s:property value='saleId'/>"/>
				<input type="hidden" id="dgRelevantCode" name="dgRelevantCode" value="<s:property value='relevantCode'/>"/>
				<input type="hidden" id="dgOrderId" name="dgOrderId" value="<s:property value='orderId'/>"/>
				<input type="hidden" id="dgCouponCode" name="dgCouponCode" value="<s:property value='couponCode'/>"/>
				<input type="hidden" id="dgIsStock" name="dgIsStock" value="<s:property value='isStock'/>"/>
				<input type="hidden" id="dgActivityCode" name="dgActivityCode" value="<s:property value='activityCode'/>"/>
				<input type="hidden" id="dgCounterActCode" name="dgCounterActCode" value="<s:property value='counterActCode'/>"/>
				<input type="hidden" id="dgProductVendorId" name="dgProductVendorId" value="<s:property value='productVendorId'/>"/>
				<input type="hidden" id="dgUnitCode" name="dgUnitCode" value="<s:property value='unitCode'/>"/>
				<input type="hidden" id="dgBarCode" name="dgBarCode" value="<s:property value='barCode'/>"/>
				<input type="hidden" id="dgProductName" name="dgProductName" value="<s:property value='productName'/>"/>
				<input type="hidden" id="dgPrice" name="dgPrice" value="<s:property value='price'/>"/>
				<input type="hidden" id="dgMemberPrice" name="dgMemberPrice" value="<s:property value='memberPrice'/>"/>
				<input type="hidden" id="dgPlatinumPrice" name="dgPlatinumPrice" value="<s:property value='platinumPrice'/>"/>
				<input type="hidden" id="dgQuantity" name="dgQuantity" value="<s:property value='quantity'/>"/>
				<input type="hidden" id="dgDiscountRate" name="dgDiscountRate" value="<s:property value='discountRate'/>"/>
				<input type="hidden" id="dgRealPrice" name="dgRealPrice" value="<s:property value='realPrice'/>"/>
				<input type="hidden" id="dgAmount" name="dgAmount" value="<s:property value='amount'/>"/>
				<input type="hidden" id="dgActivityTypeCode" name="dgActivityTypeCode" value="<s:property value='activityTypeCode'/>"/>
				<input type="hidden" id="dgProType" name="dgProType" value="<s:property value='proType'/>"/>
				<input type="hidden" id="dgRowNumber" name="dgRowNumber" value="<s:property value='RowNumber'/>"/>
				<input type="hidden" id="dgMQState" name="dgMQState" value="<s:property value='MQState'/>"/>
				<input type="hidden" id="dgServiceType" name="dgServiceType" value="<s:property value='serviceType'/>"/>
			</td>
			</s:if>
		</tr>
	</s:iterator>
</div>
</s:if>

<s:if test="savingsList!=null">
<div id="getServiceBillDetailData">
	<s:iterator value="savingsList" id="getServiceBillDetail">
		<s:if test="'SR'.equals(saleType)">
		<tr class="red">
		</s:if><s:else>
		<tr>
		</s:else>
				<td><s:property value="serviceName" /></td>
				<td><s:property value="ServiceQuantity" /></td>
			<s:if test="'DETAIL'.equals(billDetailShowType)">
			<td>
				<s:if test="'SR'.equals(saleType)">
					<s:text name="wpsal07.billStateSr" />
				</s:if>
				<s:else>
					<s:text name="wpsal07.billStateNs" />
				</s:else>
			</td>
			</s:if>
			<s:if test="!'DETAIL'.equals(billDetailShowType)">
			<td><%-- 可退次数 --%>
				<s:if test="ServiceQuantity != null">
					<s:if test="ServiceQuantity >= 0"><s:text name="format.number"><s:param value="ServiceQuantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="ServiceQuantity"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</td>
			<td class="hide"><input id="returnsQuantity" name="returnsQuantity" type="text" class="text" style="width:50px;margin:.2em 0;" maxlength="7" value="<s:property value='ServiceQuantity'/>"  onchange="BINOLWPSAL07.changeSrQuantity(this)" onkeyup="BINOLWPSAL07.changeSrQuantity(this)" /></td>
			<td class="hide">
				<span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL07.deleteRow(this);return false;"></button></span>
				<input type="hidden" id="dgRelevantCode" name="dgRelevantCode" value="<s:property value='relevantCode'/>"/>
				<input type="hidden" id="dgQuantity" name="dgQuantity" value="<s:property value='ServiceQuantity'/>"/>
				<input type="hidden" id="dgServiceType" name="dgServiceType" value="<s:property value='ServiceType'/>"/>
			</td>
			</s:if>
		</tr>
	</s:iterator>
</div>
</s:if>

<s:if test="paymentTypeList!=null">
<div id="getpayTypeDetailData">
	<s:iterator value="paymentTypeList" id="paymentType">
		<tr>
			<td><s:property value="value"/></td>
			<td><s:property value="payAmount"/></td>
			<td><s:property value="payAmount2"/></td>
		</tr>
	</s:iterator>
</div>
</s:if>
</s:i18n>
