<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.cp.BINOLCPACT01">
<div style="margin:0px;" class="GIFTBOX box2">
<div class="PARAMS sortbox_subhead_<s:property value="groupType"/> clearfix">
	<s:if test='"AND".equals(groupType)'>
    <span><span class="ui-icon icon-ttl-section-list"></span><s:text name="ACT04_fixedCom"/></span>
   	<span class="prompt_text"><s:text name="ACT04_rewardAllGifts"/></span>
   </s:if>
   <s:else>
    <span><span class="ui-icon icon-ttl-section-list"></span><s:text name="ACT04_choiceCom"/></span>
    <span class="prompt_text"><s:text name="ACT04_rewardOneGifts"/></span>
   </s:else>
   </div>
   <div class="box2-content_AND">
<table style="width: 100%;margin-top:5px;">
	<thead>
		<tr>
	  		<th><s:text name="ACT04_actGiftUnitCode"/></th><%--礼品编码--%>
	  		<th><s:text name="ACT04_actGiftName"/></th><%--礼品名称--%>
	  		<th><s:text name="ACT04_actGiftBarCode"/></th><%--礼品条码--%>
	  		<th><s:text name="ACT04_point"/></th><%--积分值--%>
	  		<th>
	  		<s:if test='"1".equals(subCampInfo.rewardType)'>
	  			<s:text name="ACT04_actGiftPrice"/><%--礼品价格--%>
	  		</s:if>
	  		<s:else>
	  			<s:text name="ACT04_deductions"/><%--抵扣金额--%>
	  		</s:else>
	  		</th><%--礼品单价--%>
	  		<th><s:text name="ACT04_actGiftQuantity"/></th><%--礼品数量--%>
	  		<th><s:text name="ACT04_delivery"/></th><%--配送方式--%>
		</tr>
	</thead>
 	<tbody> 
	 	<s:iterator value="list">
		 		<s:if test='"DHMY".equals(prmCate)||"DHCP".equals(prmCate)||"TZZK".equals(prmCate)'>
		 		<tr class="gray">
		 		</s:if>
			    <s:else><tr></s:else>
					<td><s:property value="unitCode"/></td>
					<td><s:property value="nameTotal"/></td>
					<td><s:property value="barCode"/></td>
					<td><s:property value="exPoint"/></td>
					<td>
					<s:if test='"1".equals(subCampInfo.rewardType)'>
						<s:text name="format.price"><s:param value="price"></s:param></s:text>
					</s:if>
					<s:else>
		  				<s:text name="format.price"><s:param value="price*-1"></s:param></s:text>
		  			</s:else>
					</td>
					<td><s:property value="quantity"/></td>
					<td>
					<s:iterator value="deliveryList">
						<s:if test='checked'>
							<s:property value="Value"/><br/>
						</s:if>
					</s:iterator>
				</td>
				</tr>
		</s:iterator>	
  	</tbody>
</table>					
    </div>
</div>
</s:i18n>
