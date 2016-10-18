<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.ss.BINOLSSPRM13">
<div class="box4">
	<div class="box4-header clearfix"><strong class="left"><s:text name="prmRewards"/></strong></div>
	<div id="actProList" class="box4-content clearfix">
	<s:if test="prmActiveRelList != null">
    	<s:iterator value="prmActiveRelList">
         	<%-- ========================赠送产品设定开始======================== --%>
         	<s:if test='groupType.equals("1")'>
         	<div class="box2 box2-active">
   				<div class="box2-header clearfix">
   					<strong class="active left">
   						<span class="ui-icon icon-medal"></span><s:text name="handselGift"/>
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
					  		<tr <s:if test="%{prmCate == 'TZZK'}">
					  			class="gray" title="<s:text name="tip10"><s:param><s:property value="unitCode"/></s:param><s:param><s:property value="barCode"/></s:param></s:text>"
					  		</s:if>
					  		<s:if test='!"11".equals(validFlag)'>
					  			class="red" title="<s:text name="tip9"><s:param><s:property value="unitCode"/></s:param><s:param><s:property value="barCode"/></s:param></s:text>"
					  		</s:if>>
					  			<td><s:property value="unitCode"/></td>
					  			<td><s:property value="barCode"/></td>
					  			<td><s:property value="nameTotal"/></td>
					  			<td>
					  				<s:if test="%{prmCate == 'TZZK'}"><s:property value="price * -1"/></s:if>
					  				<s:else><s:property value="price"/></s:else>
					  			</td>
					  			<td><s:property value="quantity"/></td>
					  		</tr>
					  	</s:iterator>
					  	</tbody>
					</table>
					<div class="clearfix" style="margin-top:10px;">
						<s:if test="addAmount > 0">
							<span>
			           			<s:text name="addAmount"/>
				           		<strong style="color:#ff0000;font-size:15px;">
				           		<s:text name="format.price"><s:param value="addAmount"></s:param></s:text>
				           		</strong>
				           		<s:text name="yuan"/>
			           		</span>
						</s:if>
		           		<span class="right" style="margin-right:15px">
						<s:text name="global.page.sumQuantity"/>：
						<strong style="color:green;font-size:15px;"><s:property value="sumQuantity"/></strong>
						</span>
	           		</div>
  				</div>
   			</div>
         	</s:if>
         	<%-- ========================赠送产品设定结束======================== --%>
   			<%-- ========================套装折扣设定开始======================== --%>
   			<s:if test='%{groupType == "2"}'>
   			<div class="box2 box2-active">
   				<div class="box2-header clearfix">
   					<strong class="active left">
   						<span class="ui-icon icon-medal"></span><s:text name="combinationDiscount"/>
   					</strong>
   				</div>
   				<div class="box2-content">
   					<%--是否显示全部产品折扣 --%>
   					<s:set var="allDiscountShow" value="0" />
   					<s:if test="list.size() == 1">
	   					<s:iterator value="list">
		   					<s:if test="%{prmCate == 'TZZK'}">
		   						<s:set var="allDiscountShow" value="1" />
		   					</s:if>
	   					</s:iterator>
   					</s:if>
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
					  		<tr <s:if test="%{prmCate == 'TZZK'}">
					  			<s:if test="1 == map.virtualPrmFlag">
					  			class="gray" title="<s:text name="tip10"><s:param><s:property value="unitCode"/></s:param><s:param><s:property value="barCode"/></s:param></s:text>"
					  			</s:if>
					  			<s:else>style="color:gray"</s:else>
					  		</s:if>
					  		<s:if test='!"11".equals(validFlag)'>
					  			class="red" title="<s:text name="tip9"><s:param><s:property value="unitCode"/></s:param><s:param><s:property value="barCode"/></s:param></s:text>"
					  		</s:if>>
					  			<td><s:property value="unitCode"/></td>
					  			<td><s:property value="barCode"/></td>
					  			<td><s:property value="nameTotal"/></td>
					  			<td>
					  				<s:if test="%{prmCate == 'TZZK'}"><s:property value="price * -1"/></s:if>
					  				<s:else><s:property value="price"/></s:else>
					  			</td>
					  			<td><s:property value="quantity"/></td>
					  		</tr>
					  	</s:iterator>
					  	</tbody>
					</table>
					<div class="clearfix" style="margin-top:10px;">
						<s:if test="%{#allDiscountShow == 1}">
							<span>
								<s:text name="tip5"/>
								<strong style="color:#ff0000;font-size:15px;">
								<s:text name="format.price"><s:param value="allDiscount"></s:param></s:text>
								</strong>
								<s:text name="yuan"/>
							</span>
	              		</s:if>
	              		<s:else>
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
								<s:text name="format.price"><s:param value="discount"></s:param></s:text>
								</strong>
								<s:text name="yuan"/>
		              		</span>
						</s:else>	
							
	              		<span class="right" style="margin-right:15px">
							<s:text name="global.page.sumQuantity"/>：
							<strong style="color:green;font-size:15px;"><s:property value="sumQuantity"/></strong>
						</span>
	        		</div>
  				</div>
   			</div>
         	</s:if>
   			<%-- ========================套装折扣设定结束======================== --%>
   			<s:if test='%{groupType == "3"}'>
   			<div class="box2 box2-active">
   				<div class="box2-header clearfix">
   					<strong class="active left">
   						<span class="ui-icon icon-medal"></span><s:text name="pointExchange"/>
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
					  		<tr <s:if test="%{prmCate == 'DHCP'}">
					  		<s:if test="1 == map.virtualPrmFlag">
					  			class="gray" title="<s:text name="tip10"><s:param><s:property value="unitCode"/></s:param><s:param><s:property value="barCode"/></s:param></s:text>"
					  			</s:if>
					  			<s:else>style="color:gray"</s:else>
					  		</s:if>
					  		<s:if test='!"11".equals(validFlag)'>
					  			class="red" title="<s:text name="tip9"><s:param><s:property value="unitCode"/></s:param><s:param><s:property value="barCode"/></s:param></s:text>"
					  		</s:if>>
					  			<td><s:property value="unitCode"/></td>
					  			<td><s:property value="barCode"/></td>
					  			<td><s:property value="nameTotal"/></td>
					  			<td>
					  				<s:if test="%{prmCate == 'DHCP'}"><s:property value="price * -1"/></s:if>
					  				<s:else><s:property value="price"/></s:else>
					  			</td>
					  			<td><s:property value="quantity"/></td>
					  		</tr>
					  	</s:iterator>
					  	</tbody>
					</table>
					<div class="clearfix" style="margin-top:10px;">
						<span>
							<s:text name="pointValue">
							<s:param></s:param>
							<s:param><s:text name="format.price"><s:param value="discount"></s:param></s:text></s:param>
							</s:text>
						</span>
						<span>
			           		<strong style="color:#ff0000;font-size:15px;">
			           		<s:text name="format.number"><s:param value="exPoint"></s:param></s:text>
			           		</strong>
			           	</span>          
		           		<span class="right" style="margin-right:15px">
							<s:text name="global.page.sumQuantity"/>：
							<strong style="color:green;font-size:15px;"><s:property value="sumQuantity"/></strong>
						</span>
	           		</div>
  				</div>
   			</div>
         	</s:if>
   			<%-- ========================积分兑换设定结束======================== --%>
        </s:iterator>
    </s:if>
	</div>
</div>
</s:i18n>