<#-- ========================================== 模板宏定义开始  =========================================-->
<#-- =======取得子活动确认模块======= -->
<#-- campMap    : 子活动 			 -->
<#-- =============================== -->
<#macro getCampBaseInfo_CFM  campMap>
<div class="box4" style="margin-top:0px;">
	<div class="box4-header">
		<strong><@s.text name="cp.actBaseInfo" /></strong>
		<span class="ui-widget" style="position: relative; margin-left:30px;"></span>
	</div>
	<div class="box4-content clearfix">
		<table style="margin-bottom: 0px;" class="detail">
          <tbody>
            <tr>
              <th><@s.text name="cp.actName" /></th>
              <td><span>${(campMap.campName)}</span></td>
			  <th><@s.text name="cp.actType" /></th>
			  <td><span><@s.property value="#application.CodeTable.getVal('1229','${(campMap.subCampType)!?html}')"/></span></td>
		    </tr>
		     <tr>
		      <th><@s.text name="cp.actDes"/></th><td colspan="3" style="word-wrap:break-word;word-break:break-all"><p>${(campMap.description)}</p></td>
		    </tr>
          </tbody>
        </table>
	</div>
</div>
</#macro> 
<#-- *********活动奖励************ -->
<#-- camp 	:	子活动信息		  -->
<#-- index 	:	子活动索引		  -->
<#-- **************************** -->
<#macro getCampGiftList tempMap>
<#if tempMap.campList?exists && tempMap.campList?size gt 0>
	<#list tempMap.campList as camp>
		<@getPYCampGiftList camp=camp index=camp_index virtPrmFlag=tempMap.virtPrmFlag/><#-- 积分兑现 -->
	</#list>
</#if>
</#macro>
<#-- =======活动奖励确认画面====== -->
<#-- campMap 	:	子活动信息	  -->
<#-- ============================ -->
<#macro getCampGiftList_CFM campMap>
<@getPriceControl_CFM priceControl=campMap.priceControl! saleBatchNo=campMap.saleBatchNo! needBuyFlag=campMap.needBuyFlag!/>
<div class="box4" style="margin-top:0px;">
	<div class="box4-header"><strong><@s.text name="cp.step105" /></strong></div>
	<div class="box4-content clearfix">
		<@getPYCampGiftList_CFM campMap=campMap/><#-- 积分兑现-->
	</div>
</div>
</#macro>
<#-- ********积分兑现奖励********* -->
<#-- camp 	:	子活动信息		  -->
<#-- index 	:	子活动索引		  -->
<#-- **************************** -->
<#macro getPYCampGiftList camp index virtPrmFlag='1'>
<#--可编辑Flag-->
<#assign enEditFlag = false/>
<#if !camp.state?exists || camp.state=='0' || camp.state=='3'>
	<#assign enEditFlag = true/>
</#if>

<div class="<#if index!= camp.curIndex!0>hide</#if>">
	<div class="FORM_CONTEXT">
		<input type="hidden" name="virtPrmFlag" value="${virtPrmFlag}" />
		<#-- 取得会员活动共通hidden -->
		<@getCampHideDiv camp=camp/>
		<#-- 虚拟促销品生成方式 -->
		<@getBarCodeDiv camp=camp index=index virtPrmFlag=virtPrmFlag prmCate='DHMY'/>
		<#if virtPrmFlag != '3'>
			<div id="giftList_${index}" class="group-content box2-active clearfix">
				<div>
					<table style="width: 100%;">
						<tbody>
						  <tr>
						  	<th style="text-align:right; width:20%;padding-right:10px;"><@s.text name="cp.redeemCash"/>
						  	<span class="highlight">*</span>
						  </th>
							<td style="width:70%;padding:5px 10px;">
								<span>
									<#if enEditFlag>
										<input name="exPoint" class="price" style="width:80px;text-align:right;" value="${camp.exPoint!}">
									<#else>
										<input type="hidden" name="exPoint" value="${camp.exPoint!}">
										<strong style="color:#ff0000;font-size:15px;">${camp.exPoint!}</strong>
									</#if>
									
									<@s.text name="cp.exchangePoint" />
									<strong class="red">1.00</strong>
									<@s.text name="cp.yuan" />
								</span>
							</td>
						  </tr>
						</tbody>
					</table>
				</div>
			</div>
		<#else>
			<input type="hidden" name="rewardInfo" id="rewardInfo_A_${index}" value="${(camp.rewardInfo)!?html}" />	
		</#if>	
	</div>
	<#if virtPrmFlag == '3'>
		<div id="giftList_${index}" class="group-content box2-active clearfix">
			<@getComGiftItem camp=camp index=index virtPrmFlag='3' prmCate='DHMY' />
		</div>
	</#if>
</div>
</#macro>
<#-- ********积分兑现奖励********* -->
<#-- camp 	:	子活动信息		  -->
<#-- index 	:	子活动索引		  -->
<#-- **************************** -->
<#macro getPYCampGiftList_CFM campMap>
<#if campMap.virtPrmFlag == '3'>
<div>
	<table cellspacing="0" cellpadding="0" style="width: 100%;margin-top:5px;">
	    <thead>
			<tr>
			  <th><@s.text name="cp.giftunitCode" /></th>
			  <th><@s.text name="cp.giftName" /></th>
			  <th><@s.text name="cp.giftbarCode" /></th>
			  <th><@s.text name="cp.giftPrice" /></th>
			  <th><@s.text name="cp.giftNum" /></th>
			</tr>
	  	</thead>
	  	<tbody> 
		 	<#if campMap.prtList?exists>
				<#list campMap.prtList as prtMap >
					<tr  <#if (prtMap.prmCate?exists&&prtMap.prmCate="DHMY")>class="gray"</#if>>
						<td>${(prtMap.unitCode)!?html}</td>
						<td>${(prtMap.nameTotal)!?html}</td>
						<td>${(prtMap.barCode)!?html}</td>
						<td><#if (prtMap.prmCate="DHMY")><@s.text name="format.price"><@s.param value="${(prtMap.price!'0')?number * -1}"></@s.param></@s.text >
						<#else>
						<@s.text name="format.price"><@s.param value="${(prtMap.price!'0')?number}"></@s.param></@s.text >
						</#if></td>
						<td>${(prtMap.quantity)!?html}</td>
					</tr>
				</#list>
		  	</#if>	
	  	</tbody>
	</table>
</div>
<#else>
<div style="width:20%;float:left;">
	<span class="ui-icon icon-arrow-crm"></span>
	<@s.text name="cp.redeemCash" />
</div>
<div style="width:80%;float:left;">
	<strong style="color:#ff0000;font-size:15px;">
		<span class="red">${campMap.exPoint}</span>
		<@s.text name="cp.point" />
	</strong>
	<strong style="color:#ff0000;font-size:15px;">
		<span class="green">=</span>
	</strong>
	<strong style="color:#ff0000;font-size:15px;">
		<span class="red">1.00</span>
		<@s.text name="cp.yuan" />
	</strong>
</div>
</#if>
</#macro>
<#-- *********活动对象类型******** -->
<#-- index 	:子活动列表索引		  -->
<#-- **************************** -->
<#macro getCampMebType camp index enEditFlag>
<#assign campMebType = ''/>
<#if camp.campMebType?exists><#assign campMebType = camp.campMebType/></#if>
<select id="campMebType_${index}" name="campMebType" onchange="ACT.changeMebType(this,${index});" <#if !enEditFlag>disabled="disabled"</#if>  style="width:110px;">
		<option value=""/><@s.text name="cp.selectDefault" /></option>
		<option <#if '0'==campMebType>selected="true"</#if> value="0"><@s.text name="cp.campMebType_0" /></option>
		<option <#if '1'==campMebType>selected="true"</#if> value="1"><@s.text name="cp.campMebType_1" /></option>
		<#if camp.orderFromDate! == ''>
		<option <#if '2'==campMebType>selected="true"</#if> value="2"><@s.text name="cp.campMebType_2" /></option>
		<option <#if '3'==campMebType>selected="true"</#if> value="3"><@s.text name="cp.campMebType_3" /></option>
		</#if>
</select>
<#if !enEditFlag><input type="hidden" name="campMebType" value="${(camp.campMebType)!}" /></#if>
</#macro>
<#-- ========================================== 模板宏定义结束  =========================================-->
