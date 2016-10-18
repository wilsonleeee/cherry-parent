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
<%--全部产品折扣金额 --%>
<s:set var="allDiscount" value="0" />
<%--是否显示全部产品折扣 --%>
<s:set var="allDiscountShow" value="0" />
<%--是否显示任意兑换 --%>
<s:set var="allExPointShow" value="0" />
<%--虚拟促销品条码 --%>
<s:set var="barCode_v" value="" />
<%--虚拟促销品厂商ID --%>
<s:set var="prmVendorId_v" value="" />
<div class="box4">
	<div class="box4-header clearfix"><strong class="left"><s:text name="prmRewards"/></strong></div>
	<div class="box4-content clearfix" id = "rewards_body">
		<s:if test="%{prmActiveRelList != null}">
			<s:if test="'CXHD'.equals(map.prmActiveGrpType)">
				
				<s:iterator value="prmActiveRelList">
					<s:if test='"1".equals(groupType)'><s:set var="show_1_flag" value=""/></s:if>
					<s:if test='"2".equals(groupType)'>
						<s:set var="show_2_flag" value=""/>
						<s:iterator value="list">
							<s:if test="%{prmCate == 'TZZK'}">
								<s:set var="allDiscountShow" value="1" />
								<s:set var="allDiscount" value="%{allDiscount}" />
								<s:set var="barCode_v" value="%{barCode}" />
								<s:set var="prmVendorId_v" value="%{proId}" />
							</s:if>
						</s:iterator>
						<s:if test="list.size() != 1">
							<s:set var="allDiscountShow" value="0" />
						</s:if>
					</s:if>
				</s:iterator>
				
				<div id="CXHD">
					<div id="rewardsType" class="box2 box2-content ui-widget">
						<span style="margin-right:20px;"><s:text name="rewardType"/></span>
						
						<span style="margin-right:50px;line-height:30px;">
							<input type="checkbox" <s:if test='%{#show_1_flag != "hide"}'>checked</s:if>
								id="rewardsType_1" name="rewardsType" onclick="PRM13.showBox(this,true);">
							<span><label for="rewardsType_1"><s:text name="handselGift"/></label></span>
						</span>
						
						<span style="margin-right:50px;line-height:30px;">
							<input type="checkbox" <s:if test='%{#show_2_flag != "hide"}'>checked</s:if>
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
										  		<s:if test="%{prmCate != 'TZZK'}">
										  		<tr>
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
										  			<td><s:property value="price"/><input type="hidden" name="price" value="<s:property value="price"/>"></td>
										  			<td><span><input name="quantity" class="number" onchange="PRM13.getSumInfo(this);" value="<s:property value="quantity"/>"/></span></td>
										  			<td class="center"><a onclick="PRM13.deleteHtml(this);return false;" href="javascript:void(0);"><s:text name="global.page.delete"/></a></td>
										  		</tr>
										  		</s:if>
										  	</s:iterator>
										</s:if>
									</s:iterator>		  	
								  	</tbody>
								</table>
								<div class="clearfix" style="margin-top:10px;">
									<s:if test="'hide'.equals(#show_1_flag)">
										 
										<span>
							           		<span>
							           			<input type="checkbox" name="addAmountFlag" value="1" id="addAmountFlag" onclick="PRM13.showBox(this);"/>
							           			<label for="addAmountFlag"><s:text name="addAmountFlag"/></label>
							           		</span>
							           		<span id="addAmountFlag0" style="margin-left:15px; display:none;">
								           		<s:text name="addAmount"/>
								           		<input name="addAmount" value="" class="price"/>
								           		<s:text name="yuan"/>
							           		</span>
						           		</span>
						           		
						           		<span class="right" style="margin-right:15px">
											<s:text name="global.page.sumQuantity"/>：
											<strong id="bomBody1_count" style="color:green;font-size:15px;">0</strong>
										</span>
									</s:if>
									<s:else>
										<s:iterator value="prmActiveRelList">
											<s:if test='"1".equals(groupType)'>
								           		
								           		<span style="<s:if test="!'hide'.equals(#show_1_flag) && !'hide'.equals(#show_2_flag)">display:none;</s:if>">
									           		<span>
									           			<input type="checkbox" <s:if test="addAmount > 0">checked</s:if>
									           				name="addAmountFlag" value="1" id="addAmountFlag" onclick="PRM13.showBox(this);"/>
									           			<label for="addAmountFlag"><s:text name="addAmountFlag"/></label>
									           		</span>
									           		
									           		<span id="addAmountFlag0" style="margin-left:15px; <s:if test="addAmount == 0">display:none;</s:if>">
										           		<s:text name="addAmount"/>
										           		<input name="addAmount" value="<s:property value="addAmount" />" class="price"/>
										           		<s:text name="yuan"/>
									           		</span>
								           		</span>
								           		
								           		<span class="right" style="margin-right:15px">
													<s:text name="global.page.sumQuantity"/>：
													<strong id="bomBody1_count" style="color:green;font-size:15px;">
													<s:text name="format.number"><s:param value="sumQuantity"></s:param></s:text>
													</strong>
												</span>
											</s:if>	
										</s:iterator>
									</s:else>
				           		</div>
			           		</div>
		  				</div>
		   			</div>
					<%-- ========================赠送产品设定结束======================== --%>
		  			<%-- ========================套装折扣设定开始======================== --%>
		 			<div id="rewardsType_20" class="box2 box2-active <s:property value="show_2_flag"/>">
		   				<div class="box2-header clearfix">
		   					<strong class="active left">
		   						<span class="ui-icon icon-medal"></span><s:text name="combinationDiscount"/><span class="highlight">*</span>
		   					</strong>
		   					<s:if test="map.bindProFlag == 1">
		   					<div class="tip"><s:text name="tip4"><s:param><s:text name="tip4_%{map.configTZZK}" /></s:param></s:text></div>
		   					<a class="add right" onClick="PRM13.changeBox('1');" style="margin-top: 5px;">
								<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="tip5"/></span>
							</a>
		   					<a class="add right" onClick="PRM13.changeBox('2');PRM13.openProDialog(this,'2',<s:if test="%{map.configTZZK == 2}">true</s:if><s:else>false</s:else>);" style="margin-top: 5px;">
								<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="bindPro"/></span>
							</a>
							</s:if>
							<s:else><script>$(function(){$("#tzzk_type_1").show();$("#tzzk_type_2").hide();});</script></s:else>
		   				</div>
		       			<div class="box2-content">
				 			<div id="tzzk_type_2" <s:if test='%{#allDiscountShow!= 0}'>style="display:none;"</s:if>>
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
								  	<s:if test='%{#allDiscountShow == 0}'>
									  	<s:iterator value="prmActiveRelList">
											<s:if test='"2".equals(groupType) && list != null'>
										  		<s:iterator value="list">
											  		<s:if test="%{prmCate != 'TZZK'}">
											  		<tr>
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
											  			<s:if test="%{map.configTZZK == 2}">
											  			<td><span><input name="price" value="<s:property value="price"/>" class="price" onchange="PRM13.getSumInfo(this);"/></span></td>
											  			</s:if>
											  			<s:else>
											  			<td><s:property value="price"/><input type="hidden" name="price" value="<s:property value="price"/>"></td>
											  			</s:else>
											  			<td><span><input name="quantity" class="number" onchange="PRM13.getSumInfo(this);" value="<s:property value="quantity"/>"/></span></td>
											  			<td class="center"><a onclick="PRM13.deleteHtml(this);return false;" href="javascript:void(0);"><s:text name="global.page.delete"/></a></td>
											  		</tr>
											  		</s:if>
											  	</s:iterator>
										  	</s:if>
									  	</s:iterator>
								  	</s:if>
								  	</tbody>
								</table>
								<div class="clearfix" style="margin-top:10px;">
									<s:if test="'hide'.equals(#show_2_flag)">
										<span>
											<s:text name="tzOldPrice" />
											<strong id="bomBody2_oldMoney" style="color:#ff0000;font-size:15px;">0.00</strong>
											<s:text name="yuan"/>
											<strong style="color:green;font-size:17px;">-</strong>
											<s:text name="tzPrice" />
											<s:if test="%{map.configTZZK == 2}">
											<strong id="bomBody2_moneyText" style="color:#ff0000;font-size:15px;">0.00</strong>
											</s:if>
											<s:else>
												<input id="bomBody2_money" value="" class="price" onchange="PRM13.setPrice(this);"/>
												<input type="hidden" name="discount" value="0"/>
											</s:else>
											<s:text name="yuan"/>
											<strong style="color:green;font-size:17px;">=</strong>
											<s:text name="discount" />
											<strong id="bomBody2_discount" style="color:#ff0000;font-size:15px;">0.00</strong>
											<s:text name="yuan"/>
					              		</span>
				              			<s:if test="2 == map.virtualPrmFlag && map.configTZZK == 1">
											<span id="barCode_TZZK_2" style="margin-left:20px;">
											<s:text name="barCode_v"/><span class="highlight">*</span><input name="barCode" class="text" value=""/>
											<a onclick="getBarCode('TZZK',2);return false;" class="search">
												<span class="ui-icon icon-search"></span>
												<span class="button-text"><s:text name="autoGet"/></span>
											</a>
											</span>
										</s:if>
					              		<span class="right" style="margin-right:15px">
					              			<s:text name="global.page.sumQuantity"/>：
					              			<strong id="bomBody2_count" style="color:green;font-size:15px;">0</strong>
					              		</span>
					              		</s:if>
					              	<s:else>
				              			<s:iterator value="prmActiveRelList">
											<s:if test='"2".equals(groupType)'>
												<span>
													<s:text name="tzOldPrice" />
													<strong id="bomBody2_oldMoney" style="color:#ff0000;font-size:15px;">
													<s:text name="format.price"><s:param value="sumOldPrice"></s:param></s:text>
													</strong>
													<s:text name="yuan"/>
													<strong style="color:green;font-size:17px;">-</strong>
													<s:text name="tzPrice" />
													<s:if test="%{map.configTZZK == 2}">
														<strong id="bomBody2_moneyText" style="color:#ff0000;font-size:15px;">
														<s:text name="format.price"><s:param value="sumPrice"></s:param></s:text>
														</strong>
													</s:if>
													<s:else>
														<input id="bomBody2_money" value='<s:property value="sumPrice" />' class="price" onchange="PRM13.setPrice(this);"/>
														<input type="hidden" name="discount" value="<s:property value="discount" />"/>
													</s:else>
													<s:text name="yuan"/>
													<strong style="color:green;font-size:17px;">=</strong>
													<s:text name="discount" />
													<strong id="bomBody2_discount" style="color:#ff0000;font-size:15px;">
													<s:text name="format.price"><s:param value="discount"></s:param></s:text>
													</strong>
													<s:text name="yuan"/>
							              		</span>
							              		<s:if test='showType=="edit"'>
													<input type="hidden" name="prmVendorId" value="<s:property value="#prmVendorId_v" />" />
													<input type="hidden" name="barCode" value="<s:property value="#barCode_v" />" />
							              		</s:if>
							              		<s:else>
							              			<s:if test="2 == map.virtualPrmFlag && map.configTZZK == 1">
														<span id="barCode_TZZK_2" style="margin-left:20px;">
														<s:text name="barCode_v"/><span class="highlight">*</span><input name="barCode" class="text" value=""/>
														<a onclick="getBarCode('TZZK',2);return false;" class="search">
															<span class="ui-icon icon-search"></span>
															<span class="button-text"><s:text name="autoGet"/></span>
														</a>
														</span>
													</s:if>
							              		</s:else>
							              		<span class="right" style="margin-right:15px">
							              			<s:text name="global.page.sumQuantity"/>：
							              			<strong id="bomBody2_count" style="color:green;font-size:15px;">
							              			<s:text name="format.number"><s:param value="sumQuantity"></s:param></s:text>
													</strong>
							              		</span>
											</s:if>
										</s:iterator>
				              		</s:else>
				        		</div>
				        	</div>
				        	<div id="tzzk_type_1" <s:if test='%{#allDiscountShow == 0}'>style="display:none;"</s:if>>
					 			<table style="width: 100%;">
									<tbody>
									  <tr>
										<th style="text-align:right; width:20%;padding-right:10px;">
											<s:text name="tip5"/>
										</th>
										<td style="width:70%;padding:5px 10px;">
											<span>
												<s:text name="discount" /><span class="highlight">*</span>
												<input type="hidden" name="groupType" value="2" />
												<input class="price" name="allDiscount" value="<s:property value="#allDiscount" />"/>
												<s:text name="yuan"/>
												
											</span>
											<s:if test='showType=="edit"'>
												<input type="hidden" name="prmVendorId" value="<s:property value="#prmVendorId_v" />" />
												<s:if test="'hide'.equals(#show_2_flag) && 2 == map.virtualPrmFlag">
													<span id="barCode_TZZK_1" style="margin-left:20px;">
														<s:text name="barCode_v"/><span class="highlight">*</span><input name="barCode" class="text" value=""/>
														<a onclick="getBarCode('TZZK',1);return false;" class="search">
															<span class="ui-icon icon-search"></span>
															<span class="button-text"><s:text name="autoGet"/></span>
														</a>
													</span>
												</s:if>
												<s:else>
													<input type="hidden" name="barCode" value="<s:property value="#barCode_v" />" />
												</s:else>
											</s:if>
											<s:else>
												<s:if test="2 == map.virtualPrmFlag">
													<span id="barCode_TZZK_1" style="margin-left:20px;">
													<s:text name="barCode_v"/><span class="highlight">*</span><input name="barCode" class="text" value=""/>
													<a onclick="getBarCode('TZZK',1);return false;" class="search">
														<span class="ui-icon icon-search"></span>
														<span class="button-text"><s:text name="autoGet"/></span>
													</a>
													</span>
												</s:if>
											</s:else>
										</td>
									  </tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<%-- ========================套装折扣设定结束======================== --%>
				</div>
			</s:if>
			<s:if test="'DHHD'.equals(map.prmActiveGrpType)">
				<%-- ========================积分兑换设定开始======================== --%>
				<s:iterator value="prmActiveRelList">
					<s:if test="list.size() == 1"><s:set var="allExPointShow" value="1" /></s:if>	
						<s:iterator value="list">
							<s:if test="%{prmCate == 'DHCP'}">
								<s:set var="allExPoint" value="%{exPoint}" />
								<s:set var="barCode_v" value="%{barCode}" />
								<s:set var="prmVendorId_v" value="%{proId}" />
							</s:if>
						</s:iterator>
				</s:iterator>

				<div id="DHHD">
		 			<div id="rewardsType_30" class="box2 box2-active">
		   				<div class="box2-header clearfix">
		   					<strong class="active left">
		   						<span class="ui-icon icon-medal"></span><s:text name="pointExchange"/><span class="highlight">*</span>
		   					</strong>
		   					<a id="pro_tzzk_btn" class="add right" onClick="PRM13.changeBox('1','dhcp');" style="margin-top: 5px;">
								<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="tip7"/></span>
							</a>
		   					<s:if test="map.bindProFlag == 1">
		   					<a class="add right" onClick="PRM13.changeBox('2','dhcp');PRM13.openProDialog(this,'3');" style="margin-top: 5px;">
								<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="exchangePro"/></span>
							</a>
							</s:if>
							<a class="add right" onClick="PRM13.changeBox('2','dhcp');PRM13.openPrmDialog('3');" style="margin-top: 5px;">
								<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="exchangePrm"/></span>
							</a>
		   				</div>
		       			<div class="box2-content">
				 			<div id="dhcp_type_2" <s:if test='%{#allExPointShow == 1}'>style="display:none;"</s:if>>
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
										<s:if test='list != null'>
									  		<s:iterator value="list">
										  		<s:if test="%{prmCate != 'DHCP'}">
										  		<tr>
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
										  			<td><s:property value="price"/><input type="hidden" name="price" value="<s:property value="price"/>"></td>
										  			<td><span><input name="quantity" class="number" onchange="PRM13.getSumInfo(this);" value="<s:property value="quantity"/>"/></span></td>
										  			<td class="center"><a onclick="PRM13.deleteHtml(this);return false;" href="javascript:void(0);"><s:text name="global.page.delete"/></a></td>
										  		</tr>
										  		</s:if>
										  	</s:iterator>
										</s:if>
									</s:iterator>
								  	</tbody>
								</table>
								<div class="clearfix" style="margin-top:10px;">
									<s:iterator value="prmActiveRelList">
					              		<span><s:text name="pointValue">
					              			<s:param>bomBody3_discount</s:param>
											<s:param><s:text name="format.price"><s:param value="discount"></s:param></s:text></s:param>
										</s:text></span>
										<span><input name="exPoint" value="<s:property value="exPoint"/>" class="price"/></span>
							           	<s:if test='showType=="edit"'>
											<input type="hidden" name="prmVendorId" value="<s:property value="#prmVendorId_v" />" />
											<input type="hidden" name="barCode" value="<s:property value="#barCode_v" />" />
					              		</s:if>
					              		<s:else>
					              			<s:if test="2 == map.virtualPrmFlag">
												<span id="barCode_DHCP_2" style="margin-left:20px;">
												<s:text name="barCode_v"/><span class="highlight">*</span><input name="barCode" class="text" value=""/>
												<a onclick="getBarCode('DHCP',2);return false;" class="search">
													<span class="ui-icon icon-search"></span>
													<span class="button-text"><s:text name="autoGet"/></span>
												</a>
												</span>
											</s:if>
					              		</s:else>
					              		<span class="right" style="margin-right:15px">
											<s:text name="global.page.sumQuantity"/>：
											<strong id="bomBody3_count" style="color:green;font-size:15px;">
											<s:text name="format.number"><s:param value="sumQuantity"></s:param></s:text>
											</strong>
										</span>
									</s:iterator>
				              	</div>
			        		</div>
			        		<div id="dhcp_type_1" <s:if test='%{#allExPointShow == 0}'>style="display:none;"</s:if>>
					 			<table style="width: 100%;">
									<tbody>
									<s:iterator value="prmActiveRelList">
									  <tr>
										<th style="text-align:right; width:20%;padding-right:10px;"><s:text name="tip7"/></th>
										<td style="width:70%;padding:5px 10px;">
											<span>
											<s:text name="pointValue">
												<s:param></s:param>
												<s:param><span><input class="price" name="allExPrice" value="<s:property value="discount" />"/></span></s:param>
											</s:text>
											</span>
											<span class="highlight">*</span>
											<span>
												<input type="hidden" name="groupType" value="3" />
												<input class="price" name="allExPoint" value="<s:property value="exPoint" />"/>
											</span>
											<s:if test='showType=="edit"'>
												<input type="hidden" name="barCode" value="<s:property value="#barCode_v" />" />
												<input type="hidden" name="prmVendorId" value="<s:property value="#prmVendorId_v" />" />
											</s:if>
											<s:else>
												<s:if test="2 == map.virtualPrmFlag">
													<span id="barCode_DHCP_1" style="margin-left:20px;">
													<s:text name="barCode_v"/><span class="highlight">*</span><input name="barCode" class="text" value=""/>
													<a onclick="getBarCode('DHCP',1);return false;" class="search">
														<span class="ui-icon icon-search"></span>
														<span class="button-text"><s:text name="autoGet"/></span>
													</a>
													</span>
												</s:if>
											</s:else>
										</td>
									  </tr>
									</s:iterator>	  
									</tbody>
								</table>
							</div>
			        	</div>	
					</div>
				</div>
				<%-- ========================积分兑换设定结束======================== --%>
			</s:if>
		</s:if>
	</div>
</div>
</s:i18n>