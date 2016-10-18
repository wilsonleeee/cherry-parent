<#-- ========================================== 模板宏定义开始  =========================================-->
<#-- ************* 活动基本信息确认画面 *************-->
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
			  <td><span><@s.property value="#application.CodeTable.getVal('1247','${(campMap.subCampType)!?html}')"/></span></td>
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
		<@getLYHDCampGiftList camp=camp index=camp_index virtPrmFlag=tempMap.virtPrmFlag groupFlag=tempMap.groupFlag! needBuyFlag=tempMap.needBuyFlag!/><#-- 体验客领用 -->
	</#list>
</#if>
</#macro>
<#-- =========活动奖励确认=========== -->
<#-- camp 	:	子活动信息		  -->
<#-- index 	:	子活动索引		  -->
<#-- **************************** -->
<#macro getCampGiftList_CFM campMap>
<@getPriceControl_CFM priceControl=campMap.priceControl! saleBatchNo=campMap.saleBatchNo! needBuyFlag=campMap.needBuyFlag!/>
<@getDeliveryPrice_CFM points=(campMap.deliveryPoints)!'' price=(campMap.deliveryPrice)!'' />
<div class="box4" style="margin-top:0px;">
	<div class="box4-header"><strong><@s.text name="cp.step105" /></strong></div>
	<div class="box4-content clearfix">
		<@getLYHDCampGiftList_CFM campMap=campMap /><#-- 体验客领用-->
	</div>
</div>
</#macro>
<#-- *********活动对象类型******** -->
<#-- index 	:子活动列表索引		  -->
<#-- **************************** -->
<#macro getCampMebType camp index enEditFlag>
<#assign campMebType = ''/>
<#if camp.campMebType?exists><#assign campMebType = camp.campMebType/></#if>
<select id="campMebType_${index}" name="campMebType" onchange="ACT.changeMebType(this,${index});" <#if !enEditFlag>disabled="disabled"</#if> style="width:110px;">
		<option value=""><@s.text name="cp.selectDefault" /></option>
		<option <#if '5'==campMebType>selected="true"</#if> value="5"><@s.text name="cp.campMebType_5" /></option>
		<option <#if '6'==campMebType>selected="true"</#if> value="6"><@s.text name="cp.campMebType_6" /></option>
		<option <#if '0'==campMebType>selected="true"</#if> value="0"><@s.text name="cp.campMebType_0" /></option>
		<#if camp.orderFromDate! == ''>
		<option <#if '3'==campMebType>selected="true"</#if> value="3"><@s.text name="cp.campMebType_3" /></option>
		</#if>
</select>
<#if !enEditFlag><input type="hidden" name="campMebType" value="${(camp.campMebType)!}" /></#if>
</#macro>
<#-- ========================================== 模板宏定义结束  =========================================-->
