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
		<@getPXCampGiftList camp=camp index=camp_index virtPrmFlag=tempMap.virtPrmFlag groupFlag=tempMap.groupFlag needBuyFlag=tempMap.needBuyFlag!/><#-- 积分兑礼 -->
	</#list>
</#if>
</#macro>
<#-- =======活动奖励确认画面====== -->
<#-- campMap 	:	子活动信息	  -->
<#-- ============================ -->
<#macro getCampGiftList_CFM campMap>
<@getPriceControl_CFM priceControl=campMap.priceControl! saleBatchNo=campMap.saleBatchNo! needBuyFlag=campMap.needBuyFlag!/>
<@getDeliveryPrice_CFM points=(campMap.deliveryPoints)!'' price=(campMap.deliveryPrice)!'' />
<div class="box4" style="margin-top:0px;">
	<div class="box4-header"><strong><@s.text name="cp.step105" /></strong></div>
	<div class="box4-content clearfix">
		<@getPXCampGiftList_CFM campMap=campMap/><#-- 积分兑礼 -->
	</div>
</div>
</#macro>
<#-- ********积分兑礼奖励********* -->
<#-- camp 	:	子活动信息		  -->
<#-- index 	:	子活动索引		  -->
<#-- **************************** -->
<#macro getPXCampGiftList camp index virtPrmFlag='1' groupFlag='' needBuyFlag='0'>
<#--可编辑Flag-->
<#assign enEditFlag = false/>
<#if !camp.state?exists || camp.state=='0' || camp.state=='3'>
	<#assign enEditFlag = true/>
</#if>
<#--奖励类型-->
<#assign rewardType = '1'/>
<#if camp.rewardType?exists><#assign rewardType = camp.rewardType/></#if>
<div class="<#if index!= camp.curIndex!0>hide</#if>">
	<div class="FORM_CONTEXT">
		<#-- 取得会员活动共通hidden -->
		<@getCampHideDiv camp=camp/>
		<#--购买金额 -->
		<@getPriceControl priceControl=(camp.priceControl)! saleBatchNo=(camp.saleBatchNo)! needBuyFlag=needBuyFlag/>
		<#if virtPrmFlag != '3'>
			<#-- 奖励方式 -->
			<div style="margin-top:0px;" class="relation clearfix UN_SAME">
				<span class="left" style="margin-left:5px">
					<@s.text name="cp.campReward" />
					<select id="rewardType_${index}" name="rewardType" onchange="ACT.changeRewardType(this,${index});" 
						<#if !enEditFlag>disabled="disabled"</#if> style="margin-left:10px;width:140px;">
						<#list application.CodeTable.getCodes("1226") as code>
							<option <#if code.CodeKey==camp.rewardType!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
					  	</#list>
					</select>
					<#if !enEditFlag><input type="hidden" id="rewardType_${index}" name="rewardType" value="${(camp.rewardType)!}" /></#if>
				</span>
			</div>
		<#else>
			<input type="hidden" name="rewardType" id="rewardType_${index}" value="1" />
		</#if>
		<#-- 虚拟促销品生成方式 -->
		<@getBarCodeDiv camp=camp index=index virtPrmFlag=virtPrmFlag prmCate='DHCP'/>
		<input type="hidden" name="virtPrmFlag" value="${virtPrmFlag}" />
		<input type="hidden" name="rewardInfo" id="rewardInfo_A_${index}" value="${(camp.rewardInfo)!?html}" />
		<input type="hidden" name="exPoint" id="rewardInfo_B_${index}" value="${(camp.exPoint)!}">
		<input type="hidden" name="amout" id="rewardInfo_C_${index}" value="${(camp.amout)!}" />
		<@getDeliveryPrice index=index points=(camp.deliveryPoints)!'' price=(camp.deliveryPrice)!'' />
	</div>
	<div id="giftList_${index}" class="group-content box2-active clearfix">
		<#-- 奖励指定礼品块 -->
		<div <#if rewardType !="1">class="hide"</#if> id="giftList_${index}_1">
			<@getComGiftItem camp=camp index=index virtPrmFlag=virtPrmFlag prmCate='DHCP' groupFlag=groupFlag/>
		</div>
		<#-- 奖励电子抵用券 -->
		<div <#if rewardType!="2">class="hide"</#if> id="giftList_${index}_2">
			<div>
				<table style="width: 100%;">
					<tbody>
					  <tr>
					  	<th style="text-align:right; width:20%;padding-right:10px;"><@s.text name="cp.setEleCoupon"/>
					  	<span class="highlight">*</span>
					  </th>
						<td style="width:70%;padding:5px 10px;">
							<span>
								<input id="rewardInfo_BB_${index}_2" name="exPoint" class="price" value="${camp.exPoint!}" style="width:80px;text-align:right;"/>
								<@s.text name="cp.exchangePoint" />
								<input id="rewardInfo_AA_${index}" class="price" value="<#if camp.rewardType! =='2'>${camp.rewardInfo!}</#if>" style="width:80px;text-align:right;"/>
								<@s.text name="cp.yuan" />
							</span>
						</td>
					  </tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
</div>
</#macro>
<#-- =========积分兑礼奖励========= -->
<#-- campMap 	:	子活动信息	  -->
<#-- **************************** -->
<#macro getPXCampGiftList_CFM campMap>
<#if campMap.rewardType == '1'>
<#if campMap.virtPrmFlag == '1'|| campMap.virtPrmFlag == '2'>
	<div>
		<span class="ui-icon icon-arrow-crm"></span>
		<@s.property value="#application.CodeTable.getVal('1226','${(campMap.rewardType)!?html}')"/>
		<span style="margin-left:50px;">
			<span>
				<@s.text name="cp.pointNeed" />：
				<strong style="color:#ff0000;font-size:15px;">${(campMap.exPoint)!?html}</strong>
				<@s.text name="cp.amout" />:
				<strong style="color:#ff0000;font-size:15px;">
					<@s.text name="format.price"><@s.param value="${campMap.amout!'0'}"></@s.param></@s.text>
				</strong>
				<@s.text name="cp.yuan" />
			</span>
		</span>
	</div>
</#if>
<#-- 奖励组合模式显示-->
<#if campMap.rewardMap?exists>
<div class="sortbox_content clearfix">
	<#list (campMap.rewardMap.logicOptArr)![] as box>
		<div class="GIFTBOX box2" style="margin:0px;">
			<div class="PARAMS sortbox_subhead_${box.logicOpt} clearfix">
			    <span><span class="ui-icon icon-ttl-section-list"></span><@s.text name="cp.group_${box.logicOpt}" /></span>
			    <span class="prompt_text"><@s.text name="cp.group_tip_${box.logicOpt}" /></span>
		    </div>
		    <div class="box2-content_AND">
				<@getGiftTable list=box.logicOptArr![] enEditFlag=false tbodyId='boxBody_' + boxNo/>
			</div>
		</div>
		<#if box_has_next>
			<button type="button" class="button_${campMap.rewardMap.logicOpt}">
				<span class="text">${campMap.rewardMap.logicOpt}</span>
			</button>
		</#if>
	</#list>
</div>
<#else><#-- 奖励一般模式显示-->
<div>
	<@getGiftTable list=campMap.prtList![] enEditFlag=false tbodyId=''/>
</div>
</#if>

<#elseif campMap.rewardType == '2'>
	<div style="width:20%;float:left;">
		<span class="ui-icon icon-arrow-crm"></span>
		<@s.property value="#application.CodeTable.getVal('1226','${(campMap.rewardType)!?html}')"/>
	</div>
	<div style="width:80%;float:left;">
		<strong style="color:#ff0000;font-size:15px;">
			<span class="red">${(campMap.exPoint)!?html}</span>
			<@s.text name="cp.point" />
		</strong>
		<strong style="color:#ff0000;font-size:15px;">
			<span class="green">=</span>
		</strong>
		<strong style="color:#ff0000;font-size:15px;">
			<span class="red">${campMap.rewardInfo!}</span>
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
		<option <#if '2'==campMebType>selected="true"</#if> value="2"><@s.text name="cp.campMebType_2" /></option>
		<option <#if '3'==campMebType>selected="true"</#if> value="3"><@s.text name="cp.campMebType_3" /></option>
</select>
<#if !enEditFlag><input type="hidden" name="campMebType" value="${(camp.campMebType)!}" /></#if>
</#macro>
<#-- ========================================== 模板宏定义结束  =========================================-->
