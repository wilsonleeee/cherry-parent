<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="/cherry-tags" %>
<style>
.box2-header .tip {
    color: #999999;
    float: left;
    margin-left: 10px;
    margin-top: 5px;
}
</style>
<s:i18n name="i18n.ss.BINOLSSPRM13">
<%--显示赠送礼品 --%>
<s:set var="show_1_flag">hide</s:set>
<%--显示套装折扣 --%>
<s:set var="show_2_flag">hide</s:set>
<%--显示积分兑换 --%>
<s:set var="show_3_flag">hide</s:set>
<s:iterator value="prmActiveRelList">
	<s:if test='"1".equals(groupType)'><s:set var="show_1_flag" value=""/></s:if>
	<s:if test='"2".equals(groupType)'><s:set var="show_2_flag" value=""/></s:if>
	<s:if test='"3".equals(groupType)'><s:set var="show_3_flag" value=""/></s:if>
</s:iterator>
<div class="box4">
	<div class="box4-header clearfix"><strong class="left"><s:text name="prmRewards"/></strong></div>
	<div class="box4-content clearfix" id = "rewards_body">
		<s:if test="%{prmActiveRelList != null}">
		<div id="CXHD" class="<s:if test="'DHHD'.equals(map.prmActiveGrpType)">hide</s:if>">
			<div id="rewardsType" class="box2 box2-content ui-widget">
				<span style="margin-right:20px;"><s:text name="rewardType"/></span>
				
				<span style="margin-right:50px;line-height:30px;">
					<input type="checkbox" <s:if test="!'hide'.equals(#show_1_flag)">checked</s:if>
						id="rewardsType_1" name="rewardsType" onclick="PRM13.showBox(this,true);">
					<span><label for="rewardsType_1"><s:text name="handselGift"/></label></span>
				</span>
				
				<span style="margin-right:50px;line-height:30px;">
					<input type="checkbox"  <s:if test="!'hide'.equals(#show_2_flag)">checked</s:if>
					 id="rewardsType_2" name="rewardsType" onclick="PRM13.showBox(this,true);">
					<span><label for="rewardsType_2"><s:text name="combinationDiscount"/></label></span>
				</span>
			</div>
     		<%-- ========================赠送产品设定开始======================== --%>
   			<div id="rewardsType_10" class="box2 box2-active <s:property value="#show_1_flag"/>">
   				<div class="box2-header clearfix">
   					<strong class="active left">
   						<span class="ui-icon icon-medal"></span><s:text name="handselGift"/><span class="highlight">*</span>
   					</strong>
   					<s:if test="map.bindProFlag == 1">
   					<a class="add right" onClick="PRM13.openProDialog(this,'1');" style="margin-top: 5px;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="handselPro"/></span>
					</a>
					</s:if>
					<a class="add right" onClick="PRM13.openPrmDialog('1');" style="margin-top: 5px;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="handselPrm"/></span>
					</a>
   				</div>
   				<div class="box2-content">
   					<div>
						<table style="width: 100%;">
		              		<thead>
								<tr>
								  <th style="width:20%;"><s:text name="global.page.unitCode"/></th>
								  <th style="width:20%;"><s:text name="global.page.barCode"/></th>
								  <th style="width:30%;"><s:text name="global.page.nameTotal"/></th>
								  <th style="width:10%;"><s:text name="global.page.price"/></th>
								  <th style="width:10%;"><s:text name="global.page.quantity"/></th>
								  <th style="width:10%;" class="center"><s:text name="global.page.option"/></th>
								</tr>
							</thead>
						  	<tbody id="bomBody1">
						  	<s:iterator value="prmActiveRelList">
								<s:if test='"1".equals(groupType) && list != null'>
								  	<s:iterator value="list">
								  		<tr <s:if test="%{prmCate == 'TZZK' || prmCate == 'DHCP'}">class="gray"</s:if>>
								  			<td class="hide">		  				
								  				<input type="hidden" value="<s:property value="proId"/>" name="<s:if test="%{prmCate == 'CPCX'}">prtVendorId</s:if><s:else>prmVendorId</s:else>">
								  				<input type="hidden" value="1" name="groupType">
								  				<input type="hidden" value="<s:property value="saleType"/>" name="saleType">
								  				<input type="hidden" value="<s:property value="nameTotal"/>" name="nameTotal">
								  				<input type="hidden" value="<s:property value="unitCode"/>" name="unitCode">
								  				<input type="hidden" value="<s:property value="barCode"/>" name="barCode">
								  				<input type="hidden" value="<s:property value="oldPrice"/>" name="oldPrice">
								  			</td>
								  			<td><s:property value="unitCode"/></td>
								  			<td><s:property value="barCode"/></td>
								  			<td><s:property value="nameTotal"/></td>
								  			<td>
								  			<s:if test="%{prmCate == 'TZZK' || prmCate == 'DHCP'}"><s:property value="price * -1"/></s:if>
							  				<s:else><s:property value="price"/></s:else>
								  			<input type="hidden" name="price" value="<s:property value="price"/>">
								  			</td>
								  			<td><span><input name="quantity" class="number" onchange="PRM13.getSumInfo(this);" value="<s:property value="quantity"/>"/></span></td>
								  			<td class="center"><a onclick="PRM13.deleteHtml(this);return false;" href="javascript:void(0);"><s:text name="global.page.delete"/></a></td>
								  		</tr>
								  	</s:iterator>
								</s:if>
							</s:iterator>		  	
						  	</tbody>
						</table>
						<div class="clearfix" style="margin-top:10px;">
							<s:if test="'hide'.equals(#show_1_flag)">
				           		<span class="right" style="margin-right:15px">
									<s:text name="global.page.sumQuantity"/>：
									<strong id="bomBody1_count" style="color:green;font-size:15px;">0</strong>
								</span>
							</s:if>
							<s:else>
								<s:iterator value="prmActiveRelList">
									<s:if test='"1".equals(groupType)'>	           	
						           		<span class="right" style="margin-right:15px">
											<s:text name="global.page.sumQuantity"/>：
											<strong id="bomBody1_count" style="color:green;font-size:15px;">
											<s:text name="format.number"><s:param value="sumQuantity"></s:param></s:text>
											</strong>
										</span>
									</s:if>	
								</s:iterator>
							</s:else>
							<span class="right" style="margin-right:25px">
		              			<s:text name="global.page.sumAmount"/>：
		              			<strong id="bomBody1_moneyText" style="color:red;font-size:15px;">0.00</strong>
		              			<s:text name="yuan"/>
							</span>
		           		</div>
	           		</div>
  				</div>
   			</div>
			<%-- ========================赠送产品设定结束======================== --%>
  			<%-- ========================套装折扣设定开始======================== --%>
 			<div id="rewardsType_20" class="box2 box2-active <s:property value="#show_2_flag"/>">
   				<div class="box2-header clearfix">
   					<strong class="active left">
   						<span class="ui-icon icon-medal"></span><s:text name="combinationDiscount"/><span class="highlight">*</span>
   					</strong>
   					<s:if test="map.bindProFlag == 1">
   					<div class="tip">
   					<s:text name="tip4"><s:param><s:text name="tip4_%{map.configTZZK}" /></s:param></s:text>
   					</div>
   					<a id="pro_tzzk_btn" class="add right" onClick="PRM13.openProDialog(this,'2',<s:if test="%{map.configTZZK == 2}">true</s:if><s:else>false</s:else>);" style="margin-top: 5px;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="bindPro"/></span>
					</a>
					</s:if>
					<s:if test="map.bindProFlag != 1 || map.configTZZK != 2">
					<a class="add right" onClick="PRM13.openPrmDialog('2','TZZK');" style="margin-top: 5px;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="choiceBarCode"/></span>
					</a>
					</s:if>
   				</div>
       			<div class="box2-content">
		 			<div id="tzzk_type_2">
			 			<table style="width: 100%;">
		              		<thead>
								<tr>
								  <th style="width:20%;"><s:text name="global.page.unitCode"/></th>
								  <th style="width:20%;"><s:text name="global.page.barCode"/></th>
								  <th style="width:30%;"><s:text name="global.page.nameTotal"/></th>
								  <th style="width:10%;"><s:text name="global.page.price"/></th>
								  <th style="width:10%;"><s:text name="global.page.quantity"/></th>
								  <th style="width:10%;" class="center"><s:text name="global.page.option"/></th>
								</tr>
							</thead>
						  	<tbody id="bomBody2">
							  	<s:iterator value="prmActiveRelList">
									<s:if test='"2".equals(groupType) && list != null'>
								  		<s:iterator value="list">
									  		<tr <s:if test="%{prmCate == 'TZZK' || prmCate == 'DHCP'}">class="gray"</s:if>>
									  			<td class="hide">		  				
									  				<input type="hidden" value="<s:property value="proId"/>" name="<s:if test="%{prmCate == 'CPCX'}">prtVendorId</s:if><s:else>prmVendorId</s:else>">
									  				<input type="hidden" value="2" name="groupType">
									  				<input type="hidden" value="<s:property value="saleType"/>" name="saleType">
									  				<input type="hidden" value="<s:property value="nameTotal"/>" name="nameTotal">
									  				<input type="hidden" value="<s:property value="unitCode"/>" name="unitCode">
									  				<input type="hidden" value="<s:property value="barCode"/>" name="barCode">
									  				<input type="hidden" value="<s:property value="oldPrice"/>" name="oldPrice">
									  			</td>
									  			<td><s:property value="unitCode"/></td>
									  			<td><s:property value="barCode"/></td>
									  			<td><s:property value="nameTotal"/></td>
									  			<s:if test="%{map.configTZZK == 2 && prmCate != 'TZZK' && prmCate != 'DHCP'}">
									  			<td><span><input name="price" value="<s:property value="price"/>" class="price" onchange="PRM13.getSumInfo(this);"/></span></td>
									  			</s:if>
									  			<s:else>
									  			<td>
										  			<s:if test="%{prmCate == 'TZZK' || prmCate == 'DHCP'}"><s:property value="price * -1"/></s:if>
									  				<s:else><s:property value="price"/></s:else>
										  			<input type="hidden" name="price" value="<s:property value="price"/>">
										  		</td>
									  			</s:else>
									  			<td><span><input name="quantity" class="number" onchange="PRM13.getSumInfo(this);" value="<s:property value="quantity"/>"/></span></td>
									  			<td class="center"><a onclick="PRM13.deleteHtml(this);return false;" href="javascript:void(0);"><s:text name="global.page.delete"/></a></td>
									  		</tr>
									  	</s:iterator>
								  	</s:if>
							  	</s:iterator>
						  	</tbody>
						</table>
						<div class="clearfix" style="margin-top:10px;">
							<s:if test="'hide'.equals(#show_2_flag)">
								<s:if test="map.bindProFlag == 1 && map.configTZZK == 2">
									<span>
									<s:text name="tzOldPrice" />
									<strong id="bomBody2_oldMoney" style="color:#ff0000;font-size:15px;">0.00</strong>
									<s:text name="yuan"/>
									<strong style="color:green;font-size:17px;">-</strong>
									<s:text name="tzPrice" />
									<strong id="bomBody2_moneyText" style="color:#ff0000;font-size:15px;">0.00</strong>
									<s:text name="yuan"/>
									<strong style="color:green;font-size:17px;">=</strong>
									<s:text name="discount" />
									<strong id="bomBody2_discount" style="color:#ff0000;font-size:15px;">0.00</strong>
									<s:text name="yuan"/>
				              		</span>
			              		</s:if>
			              		<span class="right" style="margin-right:15px">
			              			<s:text name="global.page.sumQuantity"/>：
			              			<strong id="bomBody2_count" style="color:green;font-size:15px;">0</strong>
			              		</span>
			              		<s:else>
			              			<span class="right" style="margin-right:25px">
			              			<s:text name="global.page.sumAmount"/>：
			              			<strong id="bomBody2_moneyText" style="color:red;font-size:15px;">0.00</strong>
			              			<s:text name="yuan"/>
									</span>
			              		</s:else>
		              		</s:if>
		              		<s:else>
			              		<s:iterator value="prmActiveRelList">
									<s:if test='"2".equals(groupType)'>
										<s:if test="map.bindProFlag == 1 && map.configTZZK == 2">
											<span>
											<s:text name="tzOldPrice" />
											<strong id="bomBody2_oldMoney" style="color:#ff0000;font-size:15px;">
											<s:text name="format.price"><s:param value="sumOldPrice"></s:param></s:text>
											</strong>
											<s:text name="yuan"/>
											<strong style="color:green;font-size:17px;">-</strong>
											<s:text name="tzPrice" />
											<strong id="bomBody2_moneyText" style="color:#ff0000;font-size:15px;">
											<s:text name="format.price"><s:param value="sumPrice"></s:param></s:text>
											</strong>
											<s:text name="yuan"/>
											<strong style="color:green;font-size:17px;">=</strong>
											<s:text name="discount" />
											<strong id="bomBody2_discount" style="color:#ff0000;font-size:15px;">
											<s:text name="format.price"><s:param value="sumOldPrice-sumPrice"></s:param></s:text>
											</strong>
											<s:text name="yuan"/>
						              		</span>
					              		</s:if>
					              		<span class="right" style="margin-right:15px">
											<s:text name="global.page.sumQuantity"/>：
											<strong id="bomBody2_count" style="color:green;font-size:15px;">
											<s:text name="format.number"><s:param value="sumQuantity"></s:param></s:text>
											</strong>
										</span>
										<s:else>
										<span class="right" style="margin-right:25px">
					              			<s:text name="global.page.sumAmount"/>：
					              			<strong id="bomBody2_moneyText" style="color:red;font-size:15px;">
					              				<s:text name="format.price"><s:param value="sumPrice"></s:param></s:text>
					              			</strong>
					              			<s:text name="yuan"/>
										</span>
										</s:else>
				              		</s:if>
								</s:iterator>
			              	</s:else>	
		        		</div>
		        	</div>
				</div>
			</div>
			<%-- ========================套装折扣设定结束======================== --%>
		</div>
		<%-- ========================积分兑换设定开始======================== --%>
		<div id="DHHD" class="<s:property value="#show_3_flag"/>">
 			<div id="rewardsType_30" class="box2 box2-active">
   				<div class="box2-header clearfix">
   					<strong class="active left">
   						<span class="ui-icon icon-medal"></span><s:text name="pointExchange"/><span class="highlight">*</span>
   					</strong>
   					<s:if test="map.bindProFlag == 1">
   					<a class="add right" onClick="PRM13.openProDialog(this,'3');" style="margin-top: 5px;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="exchangePro"/></span>
					</a>
					</s:if>
					<a class="add right" onClick="PRM13.openPrmDialog('3');" style="margin-top: 5px;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="exchangePrm"/></span>
					</a>
					<a class="add right" onClick="PRM13.openPrmDialog('3','DHCP');" style="margin-top: 5px;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="choiceBarCode"/></span>
					</a>
   				</div>
       			<div class="box2-content">
		 			<div id="dhcp_type_2">
			 			<table style="width: 100%;">
		              		<thead>
								<tr>
								  <th style="width:20%;"><s:text name="global.page.unitCode"/></th>
								  <th style="width:20%;"><s:text name="global.page.barCode"/></th>
								  <th style="width:30%;"><s:text name="global.page.nameTotal"/></th>
								  <th style="width:10%;"><s:text name="global.page.price"/></th>
								  <th style="width:10%;"><s:text name="global.page.quantity"/></th>
								  <th style="width:10%;" class="center"><s:text name="global.page.option"/></th>
								</tr>
							</thead>
						  	<tbody id="bomBody3">
						  	<s:iterator value="prmActiveRelList">
								<s:if test='"3".equals(groupType) && list != null'>
							  		<s:iterator value="list">
								  		<tr <s:if test="%{prmCate == 'TZZK' || prmCate == 'DHCP'}">class="gray"</s:if>>
								  			<td class="hide">		  				
								  				<input type="hidden" value="<s:property value="proId"/>" name="<s:if test="%{prmCate == 'CPCX'}">prtVendorId</s:if><s:else>prmVendorId</s:else>">
								  				<input type="hidden" value="3" name="groupType">
								  				<input type="hidden" value="<s:property value="saleType"/>" name="saleType">
								  				<input type="hidden" value="<s:property value="nameTotal"/>" name="nameTotal">
								  				<input type="hidden" value="<s:property value="unitCode"/>" name="unitCode">
								  				<input type="hidden" value="<s:property value="barCode"/>" name="barCode">
								  				<input type="hidden" value="<s:property value="oldPrice"/>" name="oldPrice">
								  			</td>
								  			<td><s:property value="unitCode"/></td>
								  			<td><s:property value="barCode"/></td>
								  			<td><s:property value="nameTotal"/></td>
								  			<td>
								  			<s:if test="%{prmCate == 'TZZK' || prmCate == 'DHCP'}"><s:property value="price * -1"/></s:if>
							  				<s:else><s:property value="price"/></s:else>
								  			<input type="hidden" name="price" value="<s:property value="price"/>">
								  			</td>
								  			<td><span><input name="quantity" class="number" onchange="PRM13.getSumInfo(this);" value="<s:property value="quantity"/>"/></span></td>
								  			<td class="center"><a onclick="PRM13.deleteHtml(this);return false;" href="javascript:void(0);"><s:text name="global.page.delete"/></a></td>
								  		</tr>
								  	</s:iterator>
								</s:if>
							</s:iterator>
						  	</tbody>
						</table>
						<div class="clearfix" style="margin-top:10px;">
							<s:if test="'hide'.equals(#show_3_flag)">
								<span class="right" style="margin-right:15px">    
									<s:text name="global.page.sumQuantity"/>：
									<strong id="bomBody3_count" style="color:green;font-size:15px;">0</strong>
								</span>
								<span class="right" style="margin-right:25px">
			              			<s:text name="global.page.sumAmount"/>：
			              			<strong id="bomBody3_moneyText" style="color:red;font-size:15px;">0.00</strong>
			              			<s:text name="yuan"/>
								</span>
							</s:if>
							<s:else>
								<s:iterator value="prmActiveRelList">
									<s:if test='"3".equals(groupType)'>
				              		<span class="right" style="margin-right:15px">
										<s:text name="global.page.sumQuantity"/>：
										<strong id="bomBody3_count" style="color:green;font-size:15px;">
										<s:text name="format.number"><s:param value="sumQuantity"></s:param></s:text>
										</strong>
									</span>
									<span class="right" style="margin-right:25px">
				              			<s:text name="global.page.sumAmount"/>：
				              			<strong id="bomBody3_moneyText" style="color:red;font-size:15px;">
				              				<s:text name="format.price"><s:param value="sumPrice"></s:param></s:text>
				              			</strong>
				              			<s:text name="yuan"/>
									</span>
									</s:if>
								</s:iterator>
							</s:else>
		              	</div>
	        		</div>
	        	</div>	
			</div>
		</div>
		<%-- ========================积分兑换设定结束======================== --%>
		</s:if>
	</div>
</div>
</s:i18n>