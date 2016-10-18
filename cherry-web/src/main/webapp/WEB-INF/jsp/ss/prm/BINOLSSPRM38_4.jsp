<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:i18n name="i18n.ss.BINOLSSPRM13">
<div class="box4">
	<div class="box4-header clearfix"><strong class="left"><s:text name="prmRewards"/></strong></div>
	<div id="actProList" class="box4-content clearfix">
		<s:if test="prmActiveRelList != null">
	    	<s:iterator value="prmActiveRelList">
	         	<div class="box2 box2-active">
	   				<div class="box2-header clearfix">
	   					<strong class="active left">
	         				<s:if test='groupType.equals("1")'>
	   						<span class="ui-icon icon-medal"></span><s:text name="handselGift"/>
	   						</s:if>
	   						<s:if test='groupType.equals("2")'>
	   						<span class="ui-icon icon-medal"></span><s:text name="combinationDiscount"/>
	   						</s:if>
	   						<s:if test='groupType.equals("3")'>
	   						<span class="ui-icon icon-medal"></span><s:text name="pointExchange"/>
	   						</s:if>
	   					</strong>
	   				</div>
	   				<div class="box2-content">
						<table style="width: 100%;">
		              		<thead>
								<tr>
								  <th style="width:20%;"><s:text name="global.page.unitCode"/></th>
								  <th style="width:20%;"><s:text name="global.page.barCode"/></th>
								  <th style="width:30%;"><s:text name="global.page.nameTotal"/></th>
								  <th style="width:10%;"><s:text name="global.page.price"/></th>
								  <th style="width:10%;"><s:text name="global.page.quantity"/></th>
								</tr>
							</thead>
						  	<tbody>
							  	<s:iterator value="list">
							  		<tr <s:if test="%{prmCate == 'TZZK' || prmCate == 'DHCP'}">style="color:gray;"</s:if>
							  		<s:if test='!"11".equals(validFlag)'>
							  			class="red" title="<s:text name="tip9"><s:param><s:property value="unitCode"/></s:param><s:param><s:property value="barCode"/></s:param></s:text>"
							  		</s:if>>
							  			<td><s:property value="unitCode"/></td>
							  			<td><s:property value="barCode"/></td>
							  			<td><s:property value="nameTotal"/></td>
							  			<td>
							  				<s:if test="%{prmCate == 'TZZK' || prmCate == 'DHCP'}"><s:property value="price * -1"/></s:if>
							  				<s:else><s:property value="price"/></s:else>
							  			</td>
							  			<td><s:property value="quantity"/></td>
							  		</tr>
							  	</s:iterator>
						  	</tbody>
						</table>
						<div class="clearfix" style="margin-top:10px;">
							<s:if test='map.bindProFlag == 1 && map.configTZZK == 2 && groupType.equals("2")'>
								<span>
								<s:text name="tzOldPrice" />
								<strong style="color:#ff0000;font-size:15px;">
								<s:text name="format.price"><s:param value="sumOldPrice"></s:param></s:text>
								</strong>
								<s:text name="yuan"/>
								<strong style="color:green;font-size:17px;">-</strong>
								<s:text name="tzPrice" />
								<strong style="color:#ff0000;font-size:15px;">
								<s:text name="format.price"><s:param value="sumPrice"></s:param></s:text>
								</strong>
								<s:text name="yuan"/>
								<strong style="color:green;font-size:17px;">=</strong>
								<s:text name="discount" />
								<strong style="color:#ff0000;font-size:15px;">
								<s:text name="format.price"><s:param value="sumOldPrice-sumPrice"></s:param></s:text>
								</strong>
								<s:text name="yuan"/>
			              		</span>
		              		</s:if>
							<span class="right" style="margin-right:15px">
		              			<s:text name="global.page.sumQuantity"/>：
		              			<strong style="color:green;font-size:15px;"><s:property value="sumQuantity"/></strong>
		              		</span>
	              			<span class="right" style="margin-right:25px">
		              			<s:text name="global.page.sumAmount"/>：
		              			<strong style="color:red;font-size:15px;">
		              				<s:text name="format.price"><s:param value="sumPrice"></s:param></s:text>
		              			</strong>
		              			<s:text name="yuan"/>
							</span>
		           		</div>
	  				</div>
	   			</div>
	        </s:iterator>
	    </s:if>
	</div>
</div>
</s:i18n>