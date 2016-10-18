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
<div class="box4">
	<div class="box4-header clearfix"><strong class="left"><s:text name="prmRewards"/></strong></div>
	<div class="box4-content clearfix" id = "rewards_body">
		<div id="CXHD" class="<s:if test="'DHHD'.equals(map.prmActiveGrpType)">hide</s:if>">
			<div id="rewardsType" class="box2 box2-content ui-widget">
				<span style="margin-right:20px;"><s:text name="rewardType"/></span>
				<span style="margin-right:50px;line-height:30px;">
					<input type="checkbox" id="rewardsType_1" name="rewardsType" onclick="PRM13.showBox(this,true);"><span><label for="rewardsType_1"><s:text name="handselGift"/></label></span>
				</span>
				<span style="margin-right:50px;line-height:30px;">
					<input type="checkbox" id="rewardsType_2" name="rewardsType" onclick="PRM13.showBox(this,true);"><span><label for="rewardsType_2"><s:text name="combinationDiscount"/></label></span>
				</span>
			</div>
     		<%-- ========================赠送产品设定开始======================== --%>
   			<div id="rewardsType_10" class="box2 box2-active hide">
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
						  	<tbody id="bomBody1"></tbody>
						</table>
						<div class="clearfix" style="margin-top:10px;">
			           		
			           		<span>
				           		<span><input type="checkbox" name="addAmountFlag" value="1" id="addAmountFlag" onclick="PRM13.showBox(this);"/><label for="addAmountFlag"><s:text name="addAmountFlag"/></label></span>
				           		<span id="addAmountFlag0" style="margin-left:15px; display:none;"><s:text name="addAmount"/>&nbsp;<input name="addAmount" value="" class="price"/><s:text name="yuan"/></span>
			           		</span>
			           		
			           		<span class="right" style="margin-right:15px">
								<s:text name="global.page.sumQuantity"/>：
								<strong id="bomBody1_count" style="color:green;font-size:15px;">0</strong>
							</span>
		           		</div>
	           		</div>
  				</div>
   			</div>
   			<%-- ========================赠送产品设定结束======================== --%>
   			<%-- ========================套装折扣设定开始======================== --%>
 			<div id="rewardsType_20" class="box2 box2-active hide">
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
						  	<tbody id="bomBody2"></tbody>
						</table>
						<div class="clearfix" style="margin-top:10px;">
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
		        		</div>
	        		</div>
				</div>
			</div>
		<%-- ========================套装折扣设定结束======================== --%>
		</div>
		<%-- ========================积分兑换设定开始======================== --%>
		<div id="DHHD" class="<s:if test="!'DHHD'.equals(map.prmActiveGrpType)">hide</s:if>">
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
						  	<tbody id="bomBody3"></tbody>
						</table>
						<div class="clearfix" style="margin-top:10px;">
		              		<span class="right" style="margin-right:15px">    
								<s:text name="global.page.sumQuantity"/>：
								<strong id="bomBody3_count" style="color:green;font-size:15px;">0</strong>
							</span>
							<span class="right" style="margin-right:25px">
		              			<s:text name="global.page.sumAmount"/>：
		              			<strong id="bomBody3_moneyText" style="color:red;font-size:15px;">0.00</strong>
		              			<s:text name="yuan"/>
							</span>
		              	</div>
	              	</div>
        		</div>
			</div>
		</div>
		<%-- ========================积分兑换设定结束======================== --%>
	</div>
</div>
</s:i18n>