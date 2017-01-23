
<#-- 规则基础信息确认 -->
<#macro ruleBaseInfo page={} temp={}>
	<div class="box2 box2-active">
		<div class="box2-header clearfix"><strong class="left active"><span class="ui-icon icon-ttl-section-info"></span><@s.text name="baseInfo" /></strong></div>
		<div class="box2-content clearfix" style="padding:1em 1em 0 1em;">
			<table class="detail">
	           	<tbody>
	               	<tr>
	                   <th><@s.text name="ruleName" /></th><td>${page.prmActiveName!}</td>
	                   <th><@s.text name="brandName" /></th>
	                   <td>
	                   	<#list temp.brandList as brand>
	                   	<#if temp.brandInfoId + '' == brand.brandInfoId?c>${brand.brandName!}</#if>
						</#list>
						</td>
	               	</tr>
				  	<tr>
					  	<th><@s.text name="prmActGrp" /></th>
					  	<td>
					  		<span style="margin-right:50px;">
						    <#list temp.prmActGrpList! as actGrp>
			      				<#if page.prmActGrp! == actGrp.promotionActGrpID?c>
			      				${actGrp.groupName!}<input type="hidden" id="prmActGrp" value="${page.prmActGrp!}"/>
			      				</#if>
			      			</#list>
			      			</span>
					  		<div id="groupInfo"></div>
					  	</td>
					  	<th><@s.text name="useCoupon" /></th>
					  	<td><@getValue list=useCouponList key=page.useCoupon!'1'/></td>
				  	</tr>
					<tr>
					  <th><@s.text name="shortCode" /></th><td>${page.shortCode!}</td>
					  <th><@s.text name="enMessage" /></th><td><@getValue list=enMessageList key=page.enMessage!/></td>
				  	</tr>
					<tr>
					  <th><@s.text name="subCampValid" /></th><td><@getValue list=application.CodeTable.getCodes("1230") key=(pageA.subCampValid)!'0'/></td>
					  <th><@s.text name="isMust" /></th><td><@getValue list=isMustList key=page.isMust!/></td>
				  	</tr>
				  	<tr>
					  <th><@s.text name="maxExecCount" /></th>
					  <td>
					  	<#if !pageA.maxExecCount?exists || pageA.maxExecCount == '0' || pageA.maxExecCount == ''>
					  		<@s.text name="global.page.notLimit" />
					  	<#else>${pageA.maxExecCount}
					  	</#if>
					  </td>
					  <th rowspan="2"><@s.text name="descriptionDtl" /></th><td rowspan="2"><p>${pageA.descriptionDtl!}</p></td>
				  	</tr>
				  	<tr>
					  <th><@s.text name="zgqFlag" /></th><td><@getValue list=zgqFlagList key=page.zgqFlag!'0'/></td>
				  	</tr>
	   			</tbody>
	   		</table>
     	</div>
	</div>
</#macro>
<#-- 时间地点确认 -->
<#macro timePlaceInfo page={} temp={}>
	<div class="box2 box2-active">
		<div class="box2-header clearfix"><strong class="left active"><span class="ui-icon icon-clock"></span><@s.text name="ruleTime" /></strong></div>
		<div class="box2-content clearfix" style="padding:0.5em 1.5em;">
			<div class="clearfix time_box">
				<p class="column"><span><@s.text name="startDate" />：&nbsp;${pageB.startTime}</span></p>
				<p class="column"><span><@s.text name="endDate" />：&nbsp;${pageB.endTime}</span></p>
			</div>
     	</div>
	</div>
	<div class="box2 box2-active">
		<div class="box2-header clearfix">
			<strong class="left active"><span class="ui-icon icon-flag"></span><@s.text name="rulePlace" /></strong>
            <span class="ui-widget breadcrumb" style="position: relative; margin-left:30px;"></span>
			<#if temp.placeJson?exists>
				<#if temp.placeJson.size() gt 12>
                    <a class="right search" onclick="PRM68.showMore(this,'#morePlace');return false;">
                        <span class="ui-icon icon-search"></span>
                        <span class="button-text"><@s.text name="showPlace"/></span>
                    </a>
				</#if>
			</#if>
		</div>
		<div class="box2-content clearfix" style="padding:1em;">
			<div style="width:15%;float:left;">
				<span class="ui-icon icon-arrow-crm"></span>
				<@s.property value="#application.CodeTable.getVal('1156','${(page.locationType)!}')"/>
			</div>
			<#if temp.placeJson?exists>
				<div style="width:85%;float:left;">
                    <ul>
					<#list temp.placeJson as placeMap>
						<#if placeMap_index == 12>
                       		 <ul id="morePlace" class="hide">
						</#if>
                        <li class="left" style="width:25%;white-space: nowrap;list-style-type:none;">

							<#if !placeMap.half!false && !(placeMap.isParent!false)>
								<#if placeMap.level=0>
									<span style="color:#FF3030;white-space: nowrap;margin:5px 0px;width:210px;">${(placeMap.name)!?html}</span>
								</#if>
								<#if placeMap.level=1>
									<span style="color:#FF7F24;white-space: nowrap;margin:5px 0px;width:210px;">${(placeMap.name)!?html}</span>
								</#if>
								<#if placeMap.level=2>
									<span style="color:#4876FF;white-space: nowrap;margin:5px 0px;width:210px;">${(placeMap.name)!?html}</span>
								</#if>
								<#if placeMap.level=3>
									<span style="color:#32CD32;white-space: nowrap;margin:5px 0px;width:210px;">${(placeMap.name)!?html}</span>
								</#if>

							</#if>
                        </li>
					<#if (placeMap_index+1 == placeMap.size())||(placeMap_index == 11)>
                    	</ul>
					</#if>
					</#list>
                    </ul>
				</div>
			</#if>
     	</div>
	</div>
</#macro>
<#-- 规则对象 -->
<#macro memberInfo page={} temp={}>
<#assign memberType = '5'/>
<#if temp.memberType != ''><#assign memberType = temp.memberType/></#if>
	<div class="box2 box2-active">
		<div class="box2-header clearfix">
			<strong class="left active"><span class="ui-icon icon-login"></span><@s.text name="ruleCustom" /></strong>
		</div>
		<div class="box2-content clearfix">
			<div class="relation clearfix" style="margin-top:0px;">
				<span style="margin:3px 10px;" class="left"><@s.text name="cp.memObjectType" /></span>
				 <select class="left" style="width:110px;" disabled="true">
			    	<@getOptionList list=application.CodeTable.getCodes("1224") val= memberType />
			    </select>
			    <#if memberType != '0' && memberType != '5' && memberType !='6'>
				    <#-- 活动对象数量显示-->
					<span id="memCountShow_0" class="left ShowCount" style="margin:5px 20px;">
						<@s.text name="cp.memCount" />
						<strong class="green"><@s.text name="format.number"><@s.param value="${temp.memberCount!0}"></@s.param></@s.text></strong>
						<span class="red <#if temp.memberType != '1'>hide</#if>">[<@s.text name="global.page.estimate" />]</span>
					</span>
					<#-- 查询活动对象-->
					<span id="searchMeb_0" class="right">
						<a onclick="PRM68_3.memSearch(0);return false;" class="search" style="margin: 3px 0 0 20px;">
				      		<span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.memSearch" /></span>
				    	</a>
					</span>
					<#-- 活动对象JSON-->
					<input type="hidden" id="memberJson_0" name="pageC.memberJson" value="${(temp.memberJson)!?html}"/>
					<div class="SEARCHCODE"><input type="hidden" id="searchCode_0" name="pageC.searchCode" value="${(temp.searchCode)!}"/></div>
			    </#if>
			</div>
			<div id="conInfoDiv_0" class="relation clearfix" style="margin-top:0px;<#if temp.conInfo =='' >display:none;</#if>">
				<p id="searchCondition_0" class="left green" style="text-align:left;margin:5px 10px;">${temp.conInfo!}</p>
			</div>
			<div id="mebResult_div_0" class="hide">
		        <div class="section hide" id="memberInfo_0">
				  <div class="section-content">
				    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="memberDataTable_0">
				      <thead>
				        <tr>
				          <th><@s.text name="cp.memObjectType" /></th>
				          <th><@s.text name="cp.memCardCode" /></th>
				          <th><@s.text name="cp.memName" /></th>
				          <th><@s.text name="cp.memMobile" /></th>
				          <th><@s.text name="cp.brithDay" /></th>
				          <th><@s.text name="global.page.joinDate" /></th>
				          <th><@s.text name="global.page.changablePoint" /></th>
				          <th class="center"><@s.text name="cp.isReceiveMsg" /></th>
				        </tr>
				      </thead>
				      <tbody>
				      </tbody>
				    </table>
				  </div>
				</div>
	     	</div>
		</div>
	</div>
</#macro>
<#-- 条件单元 -->
<#macro conditionItemDetail item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
    <@rangeItemDetail item=item index=index rangeTypeList=rangeTypeList/>
    <select style="margin:0px;width:60px;" disabled="true">
	   <@getOptionList list=propNameList val= item.propName! />
    </select>
    <select name="propOpt" style="margin:0px;width:80px;" disabled="true">
    	<@getOptionList list=application.CodeTable.getCodes("1277") val= item.propOpt! />
    </select>
    <input value="${item.propValue!}" style="margin:0px;width:70px;" class="text" disabled="true">
    <span class="<#if item.rangeType?exists && item.rangeType != 'ALL'>hide</#if>">
    	<input type="checkbox" <#if item.propHave! == '1'>checked="true"</#if> disabled="true"/><@s.text name="ruleTip3" />
    </span>
    <#if item.rangeType?exists && item.rangeType == 'ZD'>
     <span>
    	<input type="checkbox" <#if !item.isAll?exists || item.isAll = '1' >checked="checked"</#if> disabled="true"/><@s.text name="rangeType_isAll" />
    </span>
    </#if>
</li>
</#macro>
<#-- 逻辑组合框 -->
<#macro logicBoxDetail logicOpt='AND' logicObjArr=[]>
<div style="margin:0px;" class="box2">
    <div class="sortbox_subhead_${logicOpt} clearfix">
        <span><span class="ui-icon icon-ttl-section-list"></span><@s.text name="box${logicOpt}" /></span>
        <span class="prompt_text"><@s.text name="boxTip${logicOpt}" /></span>
    </div>
    <div class="box2-content_${logicOpt}">
    	<ul>
    		<#if logicObjArr?exists && logicObjArr?size gt 0>
				<#list logicObjArr as item>
					<@conditionItemDetail item=item! index = item_index + 1/>
				</#list>
			</#if>
    	</ul>
    </div>
</div>
</#macro>
<#-- 规则内容确认 -->
<#macro ruleDetailInfo page={} temp={}>
	<#assign condType = (temp.conMap.condType)!'1'/>
	<div class="box2 box2-active">
		<div class="box2-header clearfix">
			<strong class="left active"><span class="ui-icon icon-buy"></span><@s.text name="basePriceInfo" /></strong>
		</div>
		<div style="padding:1em;" class="box2-content clearfix">
			<label><@s.text name="basePriceInfo" /></label>
			<select name="pageD.basePrice" disabled="disabled">
				<@getOptionList list=basePriceList val=pageTemp.basePrice!'0'/>
			</select>
		</div>
	</div>
	<#if pageTemp.exRangeList?exists>
		<div class="box2 box2-active">
			<div class="box2-header clearfix"><strong class="left active"><span class="ui-icon icon-buy"></span><@s.text name="exRanges" /></strong></div>
			<div style="padding:1em;" class="box2-content clearfix">
				<table class="detail">
					<tr>
						<th style="width:10%;"><@s.text name="exRanges" /></th>
						<td style="width:90%;">
							<span><@prtRangeDetail ranges=pageTemp.exRangeList /></span>
						</td>
					</tr>
				</table>
	     	</div>
		</div>
	</#if>

	<div class="box2 box2-active">
		<div class="box2-header clearfix">
			<strong class="left active"><span class="ui-icon icon-buy"></span><@s.text name="shoppingCart" /></strong>
			<span style="position: relative;top: 5px;left:5px;">
				<#--
            	<input type="checkbox" <#if page.execFlag! == '1'>checked="true"</#if> disabled="disabled" />
            	<label><@s.text name="ruleTip1" /></label>
            	 -->
            </span>
		</div>
		<div style="padding:1em;" class="box2-content clearfix">
			<div class="clearfix">
				<span class="sortbox_head"><@s.text name="buyCondition" /></span>
				<select id="condType" name="condType" style="margin:0px;width:100px;" disabled="disabled">
			  		<@getOptionList list=condTypeList val= condType />
			  	</select>
			</div>
			<div id="ruleCondBoxInfo">
				<#if condType == '0'>
				    <@condBoxDetail_0 conMap=temp.conMap/>
				<#elseif condType == '1'>
				    <@condBoxDetail_1 conMap=temp.conMap/>
				<#elseif condType == '2'>
				    <@condBoxDetail_2 conMap=temp.conMap/>
				</#if>
			</div>
		</div>
	</div>
	<div class="box2 box2-active">
		<div class="box2-header clearfix"><strong class="left active"><span class="ui-icon icon-medal"></span><@s.text name="bonusResult" /></strong></div>
		<div style="padding:1em;" class="box2-content clearfix">
			<#list (pageTemp.resMap.logicObjArr)! as result>
				<#if result.rewardType! == 'ZDZK'><@getRewardValDetail_ZDZK map=result/>
				<#elseif result.rewardType! == 'ZDMS'><@getRewardValDetail_ZDMS map=result/>
				<#elseif result.rewardType! == 'DPZK'><@getRewardValDetail_DPZK map=result/>
				<#elseif result.rewardType! == 'DPTJ'><@getRewardValDetail_DPTJ map=result/>
				<#elseif result.rewardType! == 'DNZK'><@getRewardValDetail_DNZK map=result/>
				<#elseif result.rewardType! == 'DPYH'><@getRewardValDetail_DPYH map=result/>
				<#elseif result.rewardType! == 'ZDYH'><@getRewardValDetail_ZDYH map=result/>
				<#elseif result.rewardType! == 'GIFT'><@getRewardValDetail_GIFT map=result/>
				<#elseif result.rewardType! == 'TZZK'><@getRewardValDetail_TZZK map=result/>
				<#elseif result.rewardType! == 'YHTZ'><@getRewardValDetail_YHTZ map=result/>
				<#elseif result.rewardType! == 'JJHG'><@getRewardValDetail_JJHG map=result/>
				<#elseif result.rewardType! == 'SGGJ'><@getRewardValDetail_SGGJ map=result/>
				<#elseif result.rewardType! == 'JFDK'><@getRewardValDetail_JFDK map=result/>
				<#elseif result.rewardType! == 'ZDXL'><@getRewardValDetail_ZDXL map=result/>
				</#if>
				<#if result_has_next><@logicBtn logicOpt=(pageTemp.resMap.logicOpt)!'AND' disabled=true/></#if>
			</#list>
		</div>
	</div>
</#macro>
<#-- ：整单条件 -->
<#macro condBoxDetail_0 conMap={}>
<div class="sortbox_content clearfix"><@s.text name="noBuyTip" />
</div>
</#macro>
<#-- 购买条件：整单条件 -->
<#macro condBoxDetail_1 conMap={}>
<div class="sortbox_content clearfix">
	<ul>
    	<li class="sortsubbox">
            <select name="propName" style="margin:0px;width:80px;" disabled="true">
                <@getOptionList list=propNameList2 val= conMap.propName! />
            </select>
           <select name="propOpt" style="margin:0px;width:80px;" disabled="true">
		    	<@getOptionList list=application.CodeTable.getCodes("1277") val= conMap.propOpt! />
		    </select>
            <input type="text" value="${conMap.propValue!}" name="propValue" style="margin:0px;width:80px;" disabled="true" class="text">
            <span style="margin:0 10px; color:green;"><@s.text name="logicOpt_AND" /></span>
            <span>
            	<@s.text name="rangeType_price" />:
            	<input value="${conMap.minPrice!}" disabled="true" name="minPrice" class="price">-
            	<input value="${conMap.maxPrice!}" disabled="true" name="maxPrice"class="price">
            </span>
            <span>
            	<input type="checkbox" <#if !conMap.isAll?exists || conMap.isAll = '1' >checked="checked"</#if> disabled="true" />
            	<label><@s.text name="rangeType_isAll" /></label>
            </span>
        </li>
     </ul>
</div>
</#macro>
<#-- 购买条件：非整单条件 -->
<#macro condBoxDetail_2 conMap={}>
<div id="ruleConditionBox" class="sortbox_content clearfix">
	<#if conMap.logicObjArr?exists>
		<#list conMap.logicObjArr as box>
			<@logicBoxDetail logicOpt=box.logicOpt! logicObjArr=box.logicObjArr />
			<#if box_has_next><@logicBtn logicOpt=conMap.logicOpt!'AND' disabled=true/></#if>
		</#list>
	</#if>
</div>
</#macro>
<#-- 规则奖励-整单折扣 -->
<#macro getRewardValDetail_ZDZK map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='ZDZK'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<span><@s.text name="discountRate" />：</span>
		<strong class="green">${(map.rewardVal)!}</strong>
		<span><@s.text name="discount" /></span>
		<span style="margin-left:10px;">
	    	<input type="checkbox" <#if map.execFlag! =='1'>checked="true"</#if> id="execFlag_2" name="execFlag" disabled="true">
	    	<label for="execFlag_2"><@s.text name="ruleTip2" /></label>
	    </span>
	</div>
</div> 
</#macro>
<#-- 规则奖励-整单限量优惠 -->
<#macro getRewardValDetail_ZDXL map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='ZDXL'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<span>
			<input type="radio" name="quantityType" id="quantityType_1" <#if map.rewardQty! != '' && map.rewardQty! != '0'>checked="true"</#if> disabled="true"/>
			<label for="quantityType_1"><@s.text name="rewardType_ZDXL_tip1" /></label>
			<strong class="green">${(map.rewardQty)!}</strong>
		</span>
		<span>
			<input type="radio" name="quantityType" id="quantityType_2" <#if map.rewardMtp! != '' && map.rewardMtp! != '0'>checked="true"</#if> disabled="true"/>
			<label for="quantityType_2"><@s.text name="rewardType_ZDXL_tip2" /></label>
			<strong class="green">${(map.rewardMtp)!}</strong>
			<@s.text name="rewardType_ZDXL_tip3" />
		</span>
		<span style="margin-left:30px;">
			<@s.text name="rewardType_ZDXL_tip4" />
			<strong class="green">${(map.rewardVal)!}</strong>
			<@s.text name="yuan" />
		</span>
	</div>
</div> 
</#macro>	
<#-- 规则奖励-整单买送 -->
<#macro getRewardValDetail_ZDMS map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='ZDMS'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<span><@s.text name="rewardType_ZDMS_tip" />：</span>
		<strong class="green">${(map.rewardVal)!}</strong>
		<span><@s.text name="yuan" /></span>
		<span style="margin-left:10px;">
			<input type="checkbox" <#if map.bcj! != '' && map.bcj! != '0'>checked="true"</#if> disabled="true" />
			<label><@s.text name="rewardType_ZDMS_tip4" /></label>
		</span>
		<span style="margin-left:10px;">
			<input type="checkbox" <#if map.rewardQty! != '' && map.rewardQty! != '0'>checked="true"</#if> id="rewardQtyFlag" disabled="true">
			<label for="rewardQtyFlag"><@s.text name="rewardType_ZDMS_tip2" /></label>
		</span>
		<span <#if map.rewardQty! == '' || map.rewardQty! == '0'>class="hide"</#if> style="margin-left:10px;">
			<@s.text name="rewardType_ZDMS_tip3" />
			<strong class="green">${(map.rewardQty)!}</strong>
			<@s.text name="item" />
		</span>
	</div>
</div> 
</#macro>
<#-- 规则奖励-整单折扣 -->
<#macro getRewardValDetail_TZZK map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='TZZK'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<span><@s.text name="discountRate" />：</span>
		<strong class="green">${(map.rewardVal)!}</strong>
		<span><@s.text name="discount" /></span>
	</div>
</div> 
</#macro>
<#-- 规则奖励-单品折扣 -->
<#macro getRewardValDetail_DPZK map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='DPZK'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<#if map.logicObjArr?exists && map.logicObjArr?size gt 0>
			<#list map.logicObjArr as box>
				<@logicRewardDetailBox logicOpt=(box.logicOpt)!'AND' logicObjArr=(box.logicObjArr)![] rewardType='DPZK'/>
				<#if box_has_next><@logicBtn logicOpt=map.logicOpt!'AND' disabled=true/></#if>
			</#list>
		</#if>
	</div>
</div>		
</#macro>
<#-- 规则奖励-单品特价 -->
<#macro getRewardValDetail_DPTJ map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='DPTJ'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<#if map.logicObjArr?exists && map.logicObjArr?size gt 0>
			<#list map.logicObjArr as box>
				<@logicRewardDetailBox logicOpt=(box.logicOpt)!'AND' logicObjArr=(box.logicObjArr)![] rewardType='DPTJ'/>
				<#if box_has_next><@logicBtn logicOpt=map.logicOpt!'AND' disabled=true/></#if>
			</#list>
		</#if>
	</div>
</div>		
</#macro>
<#-- 规则奖励-第N件折扣 -->
<#macro getRewardValDetail_DNZK map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='DNZK'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<#if map.logicObjArr?exists && map.logicObjArr?size gt 0>
			<#list map.logicObjArr as box>
				<@logicRewardDetailBox logicOpt=(box.logicOpt)!'AND' logicObjArr=(box.logicObjArr)![] rewardType='DNZK'/>
				<#if box_has_next><@logicBtn logicOpt=map.logicOpt!'AND' disabled=true/></#if>
			</#list>
		</#if>
	</div>
</div>		
</#macro>
<#-- 规则奖励-第N件折扣 -->
<#macro getRewardValDetail_DPYH map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='DPYH'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<#if map.logicObjArr?exists && map.logicObjArr?size gt 0>
			<#list map.logicObjArr as box>
				<@logicRewardDetailBox logicOpt=(box.logicOpt)!'AND' logicObjArr=(box.logicObjArr)![] rewardType='DPYH'/>
				<#if box_has_next><@logicBtn logicOpt=map.logicOpt!'AND' disabled=true/></#if>
			</#list>
		</#if>
	</div>
</div>		
</#macro>
<#-- 规则奖励-买减优惠 -->
<#macro getRewardValDetail_ZDYH map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='ZDYH'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<span><@s.text name="discountAmount" />：</span>
		<strong class="green"><@s.text name="format.price"><@s.param value="${(map.rewardVal)!}"></@s.param></@s.text></strong>
		<span><@s.text name="yuan" /></span>
	</div>
</div>		
</#macro>
<#-- 规则奖励-赠送礼品 -->
<#macro getRewardValDetail_GIFT map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='GIFT'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<#if map.logicObjArr?exists && map.logicObjArr?size gt 0>
			<#list map.logicObjArr as box>
				<@logicRewardDetailBox logicOpt=(box.logicOpt)!'AND' logicObjArr=(box.logicObjArr)![] rewardType='GIFT'/>
				<#if box_has_next><@logicBtn logicOpt=map.logicOpt!'AND' disabled=true/></#if>
			</#list>
		</#if>
	</div>
</div>	
</#macro>
<#-- 规则奖励-优惠套装 -->
<#macro getRewardValDetail_YHTZ map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='YHTZ'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<span><@s.text name="rewardType_YHTZ" /></span>
		<span><strong class="green"><@s.text name="format.price"><@s.param value="${(map.rewardVal)!?number}"></@s.param></@s.text></strong></span>
		<@s.text name="yuan" />
	</div>
</div>
</#macro>
<#-- 规则奖励-加价购 -->
<#macro getRewardValDetail_JJHG map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="baseInfo" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='JJHG'/>
		</select>
		<span style="margin-left:10px;">
			<@s.text name="ruleTip3A" />
			<strong class="green"><@s.text name="format.price"><@s.param value="${(map.rewardVal)!?number}"></@s.param></@s.text></strong>
			<@s.text name="ruleTip3B" />
		</span>
	</div>
	<div class="sortbox_content clearfix">
		<#if map.logicObjArr?exists && map.logicObjArr?size gt 0>
			<#list map.logicObjArr as box>
				<@logicRewardDetailBox logicOpt=(box.logicOpt)!'AND' logicObjArr=(box.logicObjArr)![] rewardType='JJHG'/>
				<#if box_has_next><@logicBtn logicOpt=map.logicOpt!'AND' disabled=true/></#if>
			</#list>
		</#if>
	</div>
</div>
</#macro>
<#-- 规则奖励-修改价格 -->
<#macro getRewardValDetail_SGGJ map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true" onchange="PRM68_4.changeRewardType(this);">
			<@getOptionList list=rewardTypeList val='SGGJ'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<span><@s.text name="rewardType_SGGJ_tip" />：</span>
		<span>
			<strong class="green"><@s.text name="format.price"><@s.param value="${(map.val1)!}"></@s.param></@s.text></strong>
			-
			<strong class="green"><@s.text name="format.price"><@s.param value="${(map.val2)!}"></@s.param></@s.text></strong>
		</span>
		<span><@s.text name="yuan" /></span>
	</div>
</div>		
</#macro>
<#-- 规则奖励-积分抵扣 -->
<#macro getRewardValDetail_JFDK map={}>
<div class="clearfix REWARDBOX">
	<div class="clearfix PARAMS">
	    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
	    <select name="rewardType" style="margin:0px;width:100px;" disabled="true">
			<@getOptionList list=rewardTypeList val='JFDK'/>
		</select>
	</div>
	<div class="sortbox_content clearfix">
		<span><@s.text name="tip.jfdk1" /></span>
		<strong class="red">${(map.point)!}</strong>
		<span><@s.text name="tip.jfdk2" /></span>
		<strong class="green"><@s.text name="format.price"><@s.param value="${(map.rewardVal)!}"></@s.param></@s.text></strong>
		<@s.text name="yuan" />，
		<span><@s.text name="tip.jfdk3" /></span>
		<strong class="red">${(map.maxPoint)!}</strong>
	</div>
</div> 
</#macro>
<#-- 奖励逻辑组合框 -->
<#macro logicRewardDetailBox logicOpt='AND' logicObjArr=[] rewardType=''>
<div style="margin:0px;" class="box2">
    <div class="sortbox_subhead_${logicOpt} clearfix">
        <span><span class="ui-icon icon-ttl-section-list"></span><@s.text name="box${logicOpt}" /></span>
        <span class="prompt_text"><@s.text name="box2Tip${logicOpt}" /></span>
    </div>
    <div class="box2-content_${logicOpt}">
    	<ul>
    		<#if logicObjArr?exists && logicObjArr?size gt 0>
				<#list logicObjArr as item>
					<#if rewardType == 'DPZK'><@rewardItemDetail_DPZK item=item! index = index/>
					<#elseif rewardType == 'DNZK'><@rewardItemDetail_DNZK item=item! index = index/>
					<#elseif rewardType == 'DPYH'><@rewardItemDetail_DPYH item=item! index = index/>
					<#elseif rewardType == 'DPTJ'><@rewardItemDetail_DPTJ item=item! index = index/>
					<#elseif rewardType == 'GIFT'><@rewardItemDetail_GIFT item=item! index = index/>
					<#elseif rewardType == 'YHTZ'><@rewardItemDetail_GIFT item=item! index = index/>
					<#elseif rewardType == 'JJHG'><@rewardItemDetail_GIFT item=item! index = index/>
					</#if>
				</#list>
			</#if>
    	</ul>
    </div>
</div>
</#macro>
<#-- 奖励单元-单品折扣 -->
<#macro rewardItemDetail_DPZK item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
    <@rangeItemDetail item=item index=index rangeTypeList=rangeTypeList />
    <span style="margin-left:5px;">
	<@s.text name="quantityRange" />（<input value="${item.minQuantity!}" disabled="true" class="number" style="margin:0px;">
	-
	<input value="${item.maxQuantity!}" disabled="true" class="number" style="margin:0px;">）
	</span>
	<@getQuantityTypeDetailB val=item.quantityType! rangeType=item.rangeType! optAll = quantityTypeListALL/>
    <input value="${item.rewardVal!}" class="number" style="margin-bottom:0px;" disabled="true">
    <label><@s.text name="discount" /></label>
</li>
</#macro>
<#-- 奖励单元-单品特价 -->
<#macro rewardItemDetail_DPTJ item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
    <@rangeItemDetail item=item index=index rangeTypeList=rangeTypeList />
    <input value="${item.rewardVal!}" name="rewardVal" class="number" style="margin-bottom:0px;" disabled="true">
    <label><@s.text name="yuan" /></label>
</li>
</#macro>
<#-- 奖励单元-单品优惠 -->
<#macro rewardItemDetail_DPYH item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
    <@rangeItemDetail item=item index=index rangeTypeList=rangeTypeList />
    <@getQuantityTypeDetailA val=item.quantityType! rangeType=item.rangeType!/>
    <label><@s.text name="preferential" /></label>
    <input value="${item.rewardVal!}" name="rewardVal" class="number" style="margin-bottom:0px;" disabled="true">
    <label><@s.text name="yuan" /></label>
</li>
</#macro>
<#-- 奖励单元-第N件折扣 -->
<#macro rewardItemDetail_DNZK item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
    <@rangeItemDetail item=item index=index rangeTypeList=rangeTypeList />
    <select style="margin:0px;width:60px;" name="propOpt" disabled="true">
		<@getOptionList list=rangeOptList_DNZK val= item.propOpt! />
    </select>
   	<label><@s.text name="sortNo" /></label>
    <input value="${item.quantity!}" name="quantity" class="number" style="margin-bottom:0px;" disabled="true">
    <label><@s.text name="item" /></label>
    <@getQuantityTypeDetailB val=item.quantityType! rangeType=item.rangeType!/>
    <input value="${item.rewardVal!}" name="rewardVal" class="number" style="margin-bottom:0px;" disabled="true">
    <label><@s.text name="discount" /></label>
</li>
</#macro>
<#-- 奖励单元-赠送礼品 -->
<#macro rewardItemDetail_GIFT item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
    <@rangeItemDetail item=item index=index rangeTypeList=rangeTypeList extList=rangeTypePrmList/>
    <input value="${item.quantity!}" name="quantity" class="number" style="margin-bottom:0px;" disabled="true">
    <label><@s.text name="item" /></label>
</li>
</#macro>
<#-- 产品范围元素 -->
<#macro rangeItemDetail rangeTypeList item index extList=[]>
<select name="rangeType" style="margin:0px;width:95px;" onchange="PRM68_4.changeRangeType(this);" disabled="true">
    <@getOptionList list=rangeTypeList val= item.rangeType! />
    <@getOptionList list=extList val= item.rangeType! />
</select>
<select name="rangeOpt" style="margin:0px;width:60px;" <#if !item.rangeType!?ends_with('CODE')>disabled="true"</#if> onchange="PRM68_4.changeRangeOpt(this);" disabled="true">
   <@getOptionList list=rangeOptList val= item.rangeOpt! />
</select>
<span>
	<#if item.rangeOpt! == 'EQUAL'>
		<#if item.rangeType!?ends_with('CLASS') || item.rangeType!?ends_with('PRODUCT')>
		<div class="tag">
			<span style="margin:0 5px;">${item.rangeText!}</span>
		</div>
		<#elseif item.rangeType!?ends_with('CODE')>
			<div class="tag"><span style="margin:0 5px;">${item.rangeVal!}</span></div>
		<#elseif item.rangeType! == 'PRICERANGE'>
			<#assign rangeValArr= item.rangeVal?split('+')>
			<div class="tag">
				<span style="margin-left:5px;"><@s.text name="minValue" />：</span>
				<input class="price" disabled="true" value="${rangeValArr[0]}" />
				<@s.text name="maxValue" />：
				<input class="price" disabled="true" value="${rangeValArr[1]}" />
			</div>
		<#elseif item.rangeType! == 'RANGE'>
			<#if item.ranges?exists && item.ranges != ''>
				<@prtRangeDetail ranges=item.ranges />
			</#if>
		</#if>
	<#elseif item.rangeOpt! == 'LIKE'>
		<#assign rangeValArr= item.rangeVal?split('+')>
		<div class="tag"><span style="margin-left:5px;"><@s.text name="matchContent" /></span>[ ${rangeValArr[0]} ],<@s.text name="matchPosition" /> ${rangeValArr[1]}</div>
	</#if>
</span>
</#macro>
<#macro getQuantityTypeDetailA val='' rangeType=''>
<span <#if rangeType == 'PRODUCT' || rangeType == 'PRMPRODUCT'>class="hide"</#if>>
	<select name="quantityType" style="margin:0px;width:80px;" disabled="true">
	    <@getOptionList list=quantityTypeListA val= val />
	</select>
</span>
</#macro>
<#macro getQuantityTypeDetailB val='' rangeType='' optAll = []>
<span <#if rangeType == 'PRODUCT' || rangeType == 'PRMPRODUCT'>class="hide"</#if>>
	(
	<select name="quantityType" style="margin:0px;width:80px;" disabled="true">
	    <@getOptionList list=optAll + quantityTypeListB val= val />
	</select>
	)
</span>
</#macro>

<#-- 产品范围 -->
<#macro prtRangeDetail ranges=[]>
	<#list ranges as range>
		<div class="tag">
			<div class="tag_l">${range.name!}</div>
			<div class="tag_r">
				<div>
				<#if range.key == 'PRICERANGE'>
					<input disabled="true" type="text" value="${range.val1!}" name="val1" class="number">
					-
					<input disabled="true" type="text" value="${range.val2!}" name="val2" class="number">
				<#else>
					${range.valText!}
				</#if>
				</div>
			</div>
		</div>
	</#list>
</#macro>
