<#assign index = 0/>
<div class="box4">
	<div class="box4-header">
 		<strong><@s.text name="ruleContent" /><@s.text name="setting" /></strong>
	</div>
	<div class="box4-content">
        <div class="box2 box2-active">
            <div class="box2-header clearfix">
                <strong class="left active"><span class="ui-icon icon-buy"></span><@s.text name="basePriceInfo" /><@s.text name="setting" /></strong>
            </div>
            <div style="padding:1em;" class="box2-content clearfix">
                <label><@s.text name="basePriceInfo" /></label>
                <select name="pageD.basePrice">
				<@getOptionList list=basePriceList val=pageTemp.basePrice!'0'/>
                </select>
            </div>
        </div>
		<#if pageTemp.exRangesFlag == '1'>
		<div class="box2 box2-active">
			<div class="box2-header clearfix"><strong class="left active"><span class="ui-icon icon-buy"></span><@s.text name="exRanges" /></strong></div>
			<div style="padding:1em;" class="box2-content clearfix">
				<table class="detail">
					<tr>
						<th style="width:10%;"><@s.text name="exRanges" /></th>
						<td style="width:90%;">
							<span>
								<#if pageTemp.exRangeList?exists>
									<@prtRange ranges=pageTemp.exRangeList />
								</#if>
							</span>
							<a class="left" href="#" onclick="PRM68_4.popRangeDialog(this);return false;"><@s.text name="global.page.add" /></a>
						</td>
					</tr>
				</table>
				<input type="hidden" value="" id="exRangesJson" name="pageD.exRanges">
			</div>
		</div>
		</#if>

		<div class="box2 box2-active">
			<div class="box2-header clearfix"> 
				<strong class="left active"><span class="ui-icon icon-buy"></span><@s.text name="shoppingCart" /><@s.text name="setting" /></strong>
                <span style="position: relative;top: 5px;left:5px;">
                	<input type="hidden" name="pageD.execFlag" value="1" />
                </span>
			</div>
			<input type="hidden" id="ruleCondJson" name="pageD.ruleCondJson" value='' />
			<div style="padding:1em;" class="box2-content clearfix">
			<@shoppingCart conMap=pageTemp.conMap!/>
			</div>
		</div>
		<div class="box2 box2-active">
			<div class="box2-header clearfix"> 
				<strong class="left active"><span class="ui-icon icon-medal"></span><@s.text name="bonusResult" /><@s.text name="setting" /></strong>
				<#--
				<a class="add right" onclick="PRM68_4.addReward('GIFT');" style="margin-top:5px;">
		            <span class="ui-icon icon-add"></span><span class="button-text">添加奖励</span>
		        </a>
		        -->
		        <input type="hidden" id="ruleResultJson" name="pageD.ruleResultJson" value='' />
		        <input type="hidden" name="pageD.unitCode" value='${(pageTemp.resMap.unitCodeTzzk)!}' />
		        <input type="hidden" name="pageD.barCode" value='${(pageTemp.resMap.barCodeTzzk)!}' />
		        <input type="hidden" name="pageD.prmVendorId" value='${(pageTemp.resMap.prmVendorId)!}' />
			</div>
			<div class="PARAMS">
				<input type="hidden" name="logicOpt" value="${(pageTemp.resMap.logicOpt)!'AND'}" />
				<input type="hidden" id="logicObjArr_box_1" name="logicObjArr" value='' />
			</div>
			<div id="ruleReslutDiv" style="padding:1em;" class="box2-content clearfix">
				<#list (pageTemp.resMap.logicObjArr)![{"rewardType":"GIFT"}] as result>
					<div class="clearfix REWARDBOX">
						<div class="clearfix PARAMS">
						    <span class="sortbox_head"><@s.text name="bonusContent" /></span>
						    <select id="rewardType" name="rewardType" style="margin:0px;width:100px;" onchange="PRM68_4.changeRewardType(this);">
								<@getOptionList list=rewardTypeList val=result.rewardType/>
							</select>
							<span id="rewardHead"><@getRewardHead rewardType=(result.rewardType)! rewardVal=(result.rewardVal)!/></span>
							<span id="boxBtn"><@getRewardBtn rewardType=result.rewardType! /></span>
						    <input type="hidden" name="logicOpt" value="${(map.logicOpt)!'AND'}" />
							<input type="hidden" name="logicObjArr" value='' />
						</div>
						<div class="sortbox_content clearfix">
							<@getRewardValBody map=result />
						</div>
					</div>
					<#if result_has_next><@logicBtn logicOpt=(pageTemp.resMap.logicOpt)!'AND' /></#if>
				</#list>
			</div>
		</div>	
	</div>
</div>
<#-- 规则条件 -->
<div class="hide">
	<#-- 固定组合框 -->
	<div id="logicBox_AND"><@logicBox logicOpt='AND' /></div>
	<#-- 选择组合框 -->
	<div id="logicBox_OR"><@logicBox logicOpt='OR' /></div>
	<#-- 逻辑关系 -->
    <div id="logicBtn"><@logicBtn logicOpt='AND' /></div>
    <#-- 条件单元 -->
    <div id="conditionItem"><@conditionItem /></div>
    <#-- 条件值相似单元 -->
    <div id="likeRangeVal"><@likeRangeVal /></div>
    <#-- 条件范围单元 -->
    <div id="priceRangeVal"><@priceRangeVal /></div>
    
    <div id="condBox_hide_0"><@condBox_0/></div>
    <div id="condBox_hide_1"><@condBox_1/></div>
    <div id="condBox_hide_2"><@condBox_2/></div>
</div>
<#-- 产品范围弹出框 -->
<div id ="popRangeDialog" class="dialog hide">
	<@getPopRangeDialog rangeTypeList=rangeTypeList2 />
</div>
<span id="global_page_ok" class="hide"><@s.text name="global.page.ok"></@s.text></span>
<#-- 规则结果 -->
<div class="hide">
    <#-- 固定组合框 -->
	<div id="logicRewardBox_AND"><@logicRewardBox logicOpt='AND' /></div>
	<#-- 选择组合框 -->
	<div id="logicRewardBox_OR"><@logicRewardBox logicOpt='OR' /></div>
	
    <div id="quantityTypeA"><@getQuantityTypeA /></div>
    <div id="quantityTypeB"><@getQuantityTypeB /></div>
    
	<#-- 单品折扣单元 -->
    <div id="rewardItem_DPZK"><@rewardItem_DPZK /></div>
    <#-- 单品特价单元 -->
    <div id="rewardItem_DPTJ"><@rewardItem_DPTJ /></div>
    <#-- 第N件折扣单元 -->
    <div id="rewardItem_DNZK"><@rewardItem_DNZK /></div>
    <#-- 单品优惠 -->
    <div id="rewardItem_DPYH"><@rewardItem_DPYH /></div>
    <#-- 赠送礼品单元 -->
    <div id="rewardItem_GIFT"><@rewardItem_GIFT /></div>
    <#-- 加价购单元 -->
    <div id="rewardItem_JJHG"><@rewardItem_GIFT /></div>
    
	<div id="rewardValBody_hide_ZDZK"><@getRewardValBody map={"rewardType":"ZDZK"}/></div>
	<div id="rewardValBody_hide_ZDMS"><@getRewardValBody map={"rewardType":"ZDMS"}/></div>
	<div id="rewardValBody_hide_ZDYH"><@getRewardValBody map={"rewardType":"ZDYH"}/></div>
	<div id="rewardValBody_hide_YHTZ"><@getRewardValBody map={"rewardType":"YHTZ"}/></div>
	<div id="rewardValBody_hide_TZZK"><@getRewardValBody map={"rewardType":"TZZK"}/></div>
	<div id="rewardValBody_hide_SGGJ"><@getRewardValBody map={"rewardType":"SGGJ"}/></div>
	<div id="rewardValBody_hide_JFDK"><@getRewardValBody map={"rewardType":"JFDK"}/></div>
	<div id="rewardValBody_hide_ZDXL"><@getRewardValBody map={"rewardType":"ZDXL"}/></div>
	
	<div id="rewardBtn_hide"><@getRewardBtn /></div>
	
	<div id="rewardHead_hide_JJHG"><@getRewardHead rewardType='JJHG'/></div>
</div>
<#--============================条件宏定义=================================-->
<#-- 产品范围元素 -->
<#macro rangeItem rangeTypeList item index extList=[]>
<select name="rangeType" style="margin:0px;width:95px;" onchange="PRM68_4.changeRangeType(this);">
    <@getOptionList list=rangeTypeList val= item.rangeType! />
    <@getOptionList list=extList val= item.rangeType! />
</select>
<select name="rangeOpt" style="margin:0px;width:60px;" <#if !item.rangeType!?ends_with('CODE')>disabled="true"</#if> onchange="PRM68_4.changeRangeOpt(this);">
   <@getOptionList list=rangeOptList val= item.rangeOpt! />
</select>
<span id="rangeVal_${index}" onclick="PRM68_4.popConDialog(this);" class="RANGEVAL">
	<#if item.rangeOpt! == 'EQUAL'>
		<#if item.rangeType!?ends_with('CLASS') || item.rangeType!?starts_with('PR')>
		<#assign popName="" />
		<#assign popValue="" />
		<#if item.rangeType! == 'BCLASS' || item.rangeType! == 'MCLASS' || item.rangeType! == 'LCLASS'>
			<#assign popName="cateValId" />
			<#assign popValue=item.cateValId! />
		<#elseif item.rangeType! == 'PBCLASS' || item.rangeType! == 'PMCLASS' || item.rangeType! == 'PLCLASS'>
			<#assign popName="cateCode" />
			<#assign popValue=item.cateCode! />
		<#elseif item.rangeType! == 'PRODUCT'>
			<#assign popName="prtVendorId" />
			<#assign popValue=item.prtVendorId! />
		<#elseif item.rangeType! == 'PRMPRODUCT'>
			<#assign popName="prmVendorId" />
			<#assign popValue=item.prmVendorId! />
		</#if>
		<#if item.rangeType! == 'PRICERANGE'>
			<@priceRangeVal rangeVal=item.rangeVal!'+'/>
		<#else>
			<div class="tag">
				<input type="hidden" name="${popName}" value="${popValue}"/>
				<span style="margin:0 5px;">${item.rangeText!}</span>
				<input type="hidden" value="${item.rangeText!}" name="rangeText">
				<input type="hidden" value="${item.rangeVal!}" name="rangeVal">
			</div>
		</#if>
		<#elseif item.rangeType!?ends_with('CODE')>
			<div class="tag"><input style="width:344px;margin:0;" class="text" value="${item.rangeVal!}" name="rangeVal"></div>
		</#if>
	<#elseif item.rangeOpt! == 'LIKE'>
		<@likeRangeVal rangeVal=item.rangeVal!'+'/>
	
	</#if>
</span>

<span id="ranges_${index}" onclick="" class="RANGES">
<#if item.ranges?exists && item.ranges != ''>
	<@prtRange ranges=item.ranges />
</#if>
</span>

<a href="#" class="<#if item.rangeType! != 'RANGE'>hide</#if>" onclick="PRM68_4.popRangeDialog(this);return false;"><@s.text name="global.page.add" /></a>
<input type="hidden" name="ranges" value="" />
</#macro>
<#macro getQuantityTypeA val='ALL' rangeType=''>
<span <#if rangeType == 'PRODUCT' || rangeType == 'PRMPRODUCT'>class="hide"</#if>>
	<select name="quantityType" style="margin:0px;width:80px;">
	    <@getOptionList list=quantityTypeListA val= val />
	</select>
</span>
</#macro>
<#macro getQuantityTypeB val='' rangeType='' optAll = []>
<span <#if rangeType == 'PRODUCT' || rangeType == 'PRMPRODUCT'>class="hide"</#if>>
	(
	<select name="quantityType" style="margin:0px;width:80px;">
	    <@getOptionList list=optAll + quantityTypeListB val= val />
	</select>
	)
</span>
</#macro>
<#-- 条件值相似单元 -->
<#macro likeRangeVal rangeVal = '+'>
	<#assign rangeValArr= rangeVal?split('+')>
	<div class="tag">
		<span style="margin-left:5px;"><@s.text name="matchContent" /></span>
		<input class="text" style="width:120px;" onchange="PRM68_4.setRangeVal(this);" value="${rangeValArr[0]}"><@s.text name="matchPosition" />
		<input class="number" onchange="PRM68_4.setRangeVal(this);" value="${rangeValArr[1]}">
		<input type="hidden" value="${rangeVal}" name="rangeVal">
	</div>
</#macro>
<#-- 条件值范围单元 -->
<#macro priceRangeVal rangeVal = '+'>
	<#assign rangeValArr= rangeVal?split('+')>
	<div class="tag">
		<span style="margin-left:5px;"><@s.text name="minValue" />：</span>
		<input class="price"  onchange="PRM68_4.setRangeVal(this);" value="${rangeValArr[0]}">
		<@s.text name="maxValue" />：
		<input class="price" onchange="PRM68_4.setRangeVal(this);" value="${rangeValArr[1]}">
		<input type="hidden" value="${rangeVal}" name="rangeVal">
	</div>
</#macro>

<#-- 逻辑组合框 -->
<#macro logicBox logicOpt='AND' logicObjArr=[]>
<div style="margin:0px;" class="box2">
	<div class="PARAMS">
		<input type="hidden" name="logicOpt" value="${logicOpt}" />
		<input type="hidden" name="logicObjArr" value='' />
	</div>
    <div class="sortbox_subhead_${logicOpt} clearfix">
        <span><span class="ui-icon icon-ttl-section-list"></span><@s.text name="box${logicOpt}" /></span>
        <span class="prompt_text"><@s.text name="boxTip${logicOpt}" /></span>
        <a class="add right" onclick="PRM68_4.popProductDialog(this,'shoppingCart');return false;"><span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="productImport" /></span></a>
        <a class="delete right" onclick="PRM68_4.delBox(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><@s.text name="deleteBox" /></span></a>
    	<a class="add right" onclick="PRM68_4.addItem(this,0);"><span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="addCondition" /></span></a>
    </div>
    <div class="box2-content_${logicOpt}">
    	<ul>
    		<#if logicObjArr?exists && logicObjArr?size gt 0>
				<#list logicObjArr as item>
					<#assign index = index + 1/>
					<@conditionItem item=item! index = index/>
				</#list>
			</#if>
    	</ul>
    </div>
</div>
</#macro>
<#-- 条件单元 -->
<#macro conditionItem item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
	<@rangeItem item=item index=index rangeTypeList=rangeTypeList />
    <select name="propName" style="margin:0px;width:60px;">
	     <@getOptionList list=propNameList val= item.propName! />
    </select>
    <select name="propOpt" style="margin:0px;width:80px;">
    	<@getOptionList list=application.CodeTable.getCodes("1277") val= item.propOpt! />
    </select>
    <input type="text" value="${item.propValue!}" name="propValue" style="margin:0px;width:70px;" class="text">
    <span class="PROPHAVE <#if item.rangeType?exists && item.rangeType != 'ALL'>hide</#if>">
    	<input type="checkbox" <#if item.propHave! == '1'>checked="true"</#if> name="propHave"/><@s.text name="ruleTip3" />
    </span>
    <span class="ISALL <#if !item.rangeType?exists || item.rangeType != 'ZD'>hide</#if>">
    	<input type="checkbox" name="isAll" id="isAll_${index}" <#if !item.isAll?exists || item.isAll = '1' >checked="checked"</#if> />
    	<label for="isAll_${index}"><@s.text name="rangeType_isAll" /></label>
    </span>
    <a href="#" class="right" onclick="$(this).parent().remove();return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>
    
</li>
</#macro>
<#-- 购物车设置 -->
<#macro shoppingCart conMap={}>
<#assign condType = (conMap.condType)!'1'/>
<div id="shoppingCart" class="clearfix">
	<div class="clearfix">
		<span class="sortbox_head"><@s.text name="buyCondition" /></span>
		<select id="condType" style="margin:0px;width:100px;" onchange="PRM68_4.changeCondType(this);">
	  		<@getOptionList list=condTypeList val= condType />
	  	</select>
	  	<span class="<#if condType != '2'>hide</#if>">
		  	<a style="margin-top:3px;" onclick="PRM68_4.addBox('OR');" class="add right">
	            <span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="global.page.add" /><@s.text name="boxOR" /></span>
	        </a>
	        <a style="margin-top:3px;" onclick="PRM68_4.addBox('AND');" class="add right">
	            <span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="global.page.add" /><@s.text name="boxAND" /></span>
	        </a>
	  	</span>
	</div>
	<div id="ruleCondBoxInfo">
		<#if condType == '0'>
		    <@condBox_0 conMap=conMap/>
		<#elseif condType == '1'>
		    <@condBox_1 conMap=conMap/>
		<#elseif condType == '2'>
		    <@condBox_2 conMap=conMap/>
		</#if>
	</div>
</div>
</#macro>
<#-- 购买条件：整单条件 -->
<#macro condBox_0 conMap={}>
<div class="sortbox_content clearfix">
	<input type="hidden" name="condType" value="0"/><@s.text name="noBuyTip" />
</div>
</#macro>
<#-- 购买条件：整单条件 -->
<#macro condBox_1 conMap={}>
<div class="sortbox_content clearfix">
	<input type="hidden" name="condType" value="1"/>
	<ul>
    	<li class="sortsubbox">
            <select name="propName" style="margin:0px;width:80px;">
                <@getOptionList list=propNameList2 val= conMap.propName! />
            </select>
           <select name="propOpt" style="margin:0px;width:80px;">
		    	<@getOptionList list=application.CodeTable.getCodes("1277") val= conMap.propOpt! />
		    </select>
            <input type="text" value="${conMap.propValue!}" name="propValue" style="margin:0px;width:80px;" class="text">
            <span style="margin:0 10px; color:green;"><@s.text name="logicOpt_AND" /></span>
            <span>
            	<@s.text name="rangeType_price" />:
            	<input value="${conMap.minPrice!}" name="minPrice" class="price">-
            	<input value="${conMap.maxPrice!}" name="maxPrice"class="price">
            </span>
            <span>
            	<input type="checkbox" name="isAll" id="isAll" <#if !conMap.isAll?exists || conMap.isAll = '1' >checked="checked"</#if> />
            	<label for="isAll"><@s.text name="rangeType_isAll" /></label>
            </span>
        </li>
     </ul>
</div>
</#macro>
<#-- 购买条件：非整单条件 -->
<#macro condBox_2 conMap={}>
<div>
	<input type="hidden" name="condType" value="2"/>
	<input type="hidden" id="logicOpt_box" name="logicOpt" value="${conMap.logicOpt!'AND'}" />
	<input type="hidden" id="logicObjArr_box" name="logicObjArr" value='' />
	
</div>
<div class="sortbox_content clearfix">
	<#if conMap.logicObjArr?exists>
		<#list conMap.logicObjArr as box>
			<@logicBox logicOpt=box.logicOpt! logicObjArr=box.logicObjArr />
			<#if box_has_next><@logicBtn logicOpt=conMap.logicOpt!'AND' /></#if>
		</#list>
	</#if>
</div>
</#macro>
<#--=================================活动奖励宏定义=================================-->

<#-- 奖励单元-单品折扣 -->
<#macro rewardItem_DPZK item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
    <@rangeItem item=item index=index rangeTypeList=rangeTypeList />
	<span style="margin-left:5px;">
	<@s.text name="quantityRange" />（<input value="${item.minQuantity!}" name="minQuantity" class="number" style="margin:0px;">
	-
	<input value="${item.maxQuantity!}" name="maxQuantity" class="number" style="margin:0px;">）
	</span>
	<@getQuantityTypeB val=item.quantityType! rangeType=item.rangeType! optAll = quantityTypeListALL/>
    <input value="${item.rewardVal!}" name="rewardVal" class="number" style="margin-bottom:0px;">
    <label><@s.text name="discount" /></label>
    <a href="#" class="right" onclick="$(this).parent().remove(); return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>
</li>
</#macro>
<#-- 奖励单元-单品特价 -->
<#macro rewardItem_DPTJ item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
    <@rangeItem item=item index=index rangeTypeList=rangeTypeList />
    <label><@s.text name="item" /></label>
    <input value="${item.rewardVal!}" name="rewardVal" class="number" style="margin-bottom:0px;">
    <label><@s.text name="yuan" /></label>
    <a href="#" class="right" onclick="$(this).parent().remove(); return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>
</li>
</#macro>
<#-- 奖励单元-单品优惠 -->
<#macro rewardItem_DPYH item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
    <@rangeItem item=item index=index rangeTypeList=rangeTypeList />
    <@getQuantityTypeA val=item.quantityType!'ALL' rangeType=item.rangeType!/>
    <label><@s.text name="preferential" /></label>
    <input value="${item.rewardVal!}" name="rewardVal" class="number" style="margin-bottom:0px;">
    <label><@s.text name="yuan" /></label>
    <a href="#" class="right" onclick="$(this).parent().remove(); return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>
</li>
</#macro>
<#-- 奖励单元-第N件折扣 -->
<#macro rewardItem_DNZK item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
    <@rangeItem item=item index=index rangeTypeList=rangeTypeList />
    <select style="margin:0px;width:60px;" name="propOpt">
		<@getOptionList list=rangeOptList_DNZK val= item.propOpt! />
    </select>
    <label><@s.text name="sortNo" /></label>
    <input value="${item.quantity!}" name="quantity" class="number" style="margin-bottom:0px;">
    <label><@s.text name="item" /></label>
    <@getQuantityTypeB val=item.quantityType! rangeType=item.rangeType!/>
    <input value="${item.rewardVal!}" name="rewardVal" class="number" style="margin-bottom:0px;">
    <label><@s.text name="discount" /></label>
    <a href="#" class="right" onclick="$(this).parent().remove(); return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>
</li>
</#macro>
<#-- 奖励单元-赠送礼品 -->
<#macro rewardItem_GIFT item={} index=1>
<li style="margin-bottom:10px;" class="sortsubbox">
	<@rangeItem item=item index=index rangeTypeList=rangeTypeList extList=rangeTypePrmList/>
    <input value="${item.quantity!}" name="quantity" class="number" style="margin-bottom:0px;">
    <label><@s.text name="item" /></label>
    <a href="#" class="right" onclick="$(this).parent().remove(); return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>
</li>
</#macro>
<#-- 奖励逻辑组合框 -->
<#macro logicRewardBox logicOpt='AND' logicObjArr=[] rewardType=''>
<div style="margin:0px;" class="box2">
	<div class="PARAMS">
		<input type="hidden" name="logicOpt" value="${logicOpt}" />
		<input type="hidden" name="logicObjArr" value='' />
	</div>
    <div class="sortbox_subhead_${logicOpt} clearfix">
        <span><span class="ui-icon icon-ttl-section-list"></span><@s.text name="box${logicOpt}" /></span>
        <span class="prompt_text"><@s.text name="box2Tip${logicOpt}" /></span>
        <a class="add right" id="productImportRewardType"
		   onclick="PRM68_4.popProductDialog(this,'award');return false;"><span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="productImport" /></span></a>
        <a class="delete right" onclick="PRM68_4.delRewardBox(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><@s.text name="deleteBox" /></span></a>
    	<a class="add right" onclick="PRM68_4.addItem(this,1);"><span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="addResult" /></span></a>
    </div>
    <div class="box2-content_${logicOpt}">
    	<ul>
    		<#if logicObjArr?exists && logicObjArr?size gt 0>
				<#list logicObjArr as item>
					<#assign index = index + 1 />
					<#if rewardType == 'DPZK'><@rewardItem_DPZK item=item! index = index/>
					<#elseif rewardType == 'DNZK'><@rewardItem_DNZK item=item! index = index/>
					<#elseif rewardType == 'DPYH'><@rewardItem_DPYH item=item! index = index/>
					<#elseif rewardType == 'DPTJ'><@rewardItem_DPTJ item=item! index = index/>
					<#elseif rewardType == 'GIFT'><@rewardItem_GIFT item=item! index = index/>
					<#elseif rewardType == 'JJHG'><@rewardItem_GIFT item=item! index = index/>
					</#if>
				</#list>
			</#if>
    	</ul>
    </div>
</div>
</#macro>
<#macro getRewardValBody map={}>
	<#if map.rewardType! == 'ZDZK' || map.rewardType! == 'TZZK'>
		<span><@s.text name="discountRate" />：</span>
		<span><input class="text" name="rewardVal" value="${(map.rewardVal)!}" style="width:70px;"/></span>
		<#if map.rewardType! == 'ZDZK'>
			<span style="margin-left:10px;">
				<input type="checkbox" <#if map.execFlag! == '1'>checked="true"</#if> id="execFlag_2" name="execFlag">
				<label for="execFlag_2"><@s.text name="ruleTip2" /></label>
			</span>
		</#if>
	<#elseif map.rewardType! == 'ZDMS'>
		<span><@s.text name="rewardType_ZDMS_tip" />：</span>
		<span><input class="text" name="rewardVal" value="${(map.rewardVal)!}" style="width:70px;"/></span><@s.text name="yuan" />
		<span style="margin-left:10px;">
			<input type="checkbox" <#if map.bcj! != '' && map.bcj! != '0'>checked="true"</#if> id="bcjFlag"/>
			<label for="bcjFlag"><@s.text name="rewardType_ZDMS_tip4" /></label>
		</span>
		<span style="margin-left:10px;">
			<input type="checkbox" <#if map.rewardQty! != '' && map.rewardQty! != '0'>checked="true"</#if> id="rewardQtyFlag" onclick="PRM68_4.showRewardQty(this);">
			<label for="rewardQtyFlag"><@s.text name="rewardType_ZDMS_tip2" /></label>
		</span>
		<span <#if map.rewardQty! == '' || map.rewardQty! == '0'>class="hide"</#if> style="margin-left:10px;">
			<@s.text name="rewardType_ZDMS_tip3" />
			<input type="text" value="${(map.rewardQty)!}" class="number" name="rewardQty">
			<@s.text name="item" />
		</span>
	<#elseif map.rewardType! == 'ZDYH'>
		<span><@s.text name="discountAmount" />：</span>
		<span><input class="text" name="rewardVal" value="${(map.rewardVal)!}" style="width:70px;"/></span><@s.text name="yuan" />
	<#elseif map.rewardType! == 'ZDXL'>
		<span>
			<input type="radio" onclick="PRM68_4.changeZDXL('#rewardQty_ZDXL','#rewardMtp_ZDXL');" name="quantityType" id="quantityType_1" <#if map.rewardQty! != '' && map.rewardQty! != '0'>checked="true"</#if>/>
			<label for="quantityType_1"><@s.text name="rewardType_ZDXL_tip1" /></label>
			<input type="text" value="${(map.rewardQty)!}" class="number" name="rewardQty" id="rewardQty_ZDXL">
		</span>
		<span>
			<input type="radio" onclick="PRM68_4.changeZDXL('#rewardMtp_ZDXL','#rewardQty_ZDXL');" name="quantityType" id="quantityType_2" <#if map.rewardMtp! != '' && map.rewardMtp! != '0'>checked="true"</#if> />
			<label for="quantityType_2"><@s.text name="rewardType_ZDXL_tip2" /></label>
			<input type="text" value="${(map.rewardMtp)!}" class="number" name="rewardMtp" id="rewardMtp_ZDXL"><@s.text name="rewardType_ZDXL_tip3" />
		</span>
		<span style="margin-left:30px;">
			<@s.text name="rewardType_ZDXL_tip4" />
			<input class="text" name="rewardVal" value="${(map.rewardVal)!}" style="width:70px;"/><@s.text name="yuan" />
		</span>
	<#elseif map.rewardType! == 'YHTZ'>
		<span><@s.text name="rewardType_YHTZ" />：</span>
		<span><input class="text" name="rewardVal" value="${(map.rewardVal)!}" style="width:70px;"/></span><@s.text name="yuan" />
	<#elseif map.rewardType! == 'SGGJ'>
		<span><@s.text name="rewardType_SGGJ_tip" />：</span>
		<span>
			<input class="text" name="val1" value="${(map.val1)!}" style="width:70px;"/>
			-
			<input class="text" name="val2" value="${(map.val2)!}" style="width:70px;"/>
		</span>
		<@s.text name="yuan" />
	<#elseif map.rewardType! == 'JFDK'>
		<span><@s.text name="tip.jfdk1" /></span>
		<input class="text" name="point" value="${(map.point)!}" style="width:70px;"/>
		<span><@s.text name="tip.jfdk2" /></span>
		<input class="text" name="rewardVal" value="${(map.rewardVal)!}" style="width:70px;"/>
		<@s.text name="yuan" />	，
		<span><@s.text name="tip.jfdk3" /></span>
		<input class="text" name="maxPoint" value="${(map.maxPoint)!}" style="width:70px;"/>
	<#else>
		<#if map.logicObjArr?exists && map.logicObjArr?size gt 0>
			<#list map.logicObjArr as box>
				<@logicRewardBox logicOpt=(box.logicOpt)!'AND' logicObjArr=(box.logicObjArr)![] rewardType=map.rewardType! />
				<#if box_has_next><@logicBtn logicOpt=map.logicOpt!'AND' /></#if>
			</#list>
		</#if>
	</#if>
</#macro>

<#macro getRewardBtn rewardType=''>
<#if rewardType !='JFDK' && rewardType !='SGGJ' && rewardType !='ZDYH' && rewardType !='ZDZK' && rewardType !='TZZK' && rewardType !='YHTZ' && rewardType !='ZDMS' && rewardType !='ZDXL'>
	<#--
	<a onclick="PRM68_4.delReward(this);return false;" class="delete right" style="margin-top:5px;">
		<span class="ui-icon icon-delete"></span><span class="button-text">删除奖励</span>
	</a>
	 -->
	<a class="add right" onclick="PRM68_4.addRewardBox('OR',this);" style="margin-top:5px;">
	    <span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="global.page.add" /><@s.text name="boxOR" /></span>
	</a>
	<a class="add right" onclick="PRM68_4.addRewardBox('AND',this);" style="margin-top:5px;">
	    <span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="global.page.add" /><@s.text name="boxAND" /></span>
	</a>
</#if>
</#macro>
<#macro getRewardHead rewardType='' rewardVal=''>
<#if rewardType=='JJHG'>
	<span style="margin-left:10px;">
	<@s.text name="ruleTip3A" />
	<input class="number" name="rewardVal" value="${rewardVal}"/>
	<@s.text name="ruleTip3B" />
	</span>
</#if>
</#macro>

<#-- 产品范围 -->
<#macro prtRange ranges=[]>
	<#list ranges as range>
		<#-- 全局控件索引 -->
		<#assign index = index + 1 />
		<div class="tag">
			<input class="NOSUBMIT" type="hidden" value="${range.key!}" name="key" />
			<input class="NOSUBMIT" type="hidden" value="${range.name!}" name="name" />
			<div class="tag_l">${range.name!}</div>
			<div class="tag_r" id="conVal_${index}">
				<div>
				<#if range.key == 'PRICERANGE'>
					<input class="NOSUBMIT" type="text" value="${range.val1!}" name="val1" class="number">
					-
					<input class="NOSUBMIT" type="text" value="${range.val2!}" name="val2" class="number">
				<#else>
					<input class="NOSUBMIT" type="hidden" value="${range.val!}" name="val" />
					<input class="NOSUBMIT" type="hidden" value="${range.valText!}" name="valText" />
					${range.valText!}
				</#if>
				</div>
			</div>
			<span onclick="$(this).parent().remove();return false;" class="icon_del"></span>
		</div>
	</#list>
</#macro>
<#-- 产品范围弹出框 -->
<#macro getPopRangeDialog rangeTypeList=[]>
	<table width="100%">
	    <thead>
	         <tr>
	            <th><@s.text name="global.page.Popselect"></@s.text></th>
	            <th><@s.text name="范围类型码"></@s.text></th>
	            <th><@s.text name="范围类型名"></@s.text></th>
	         </tr>
	     </thead>
	     <tbody id="popRangeBody">
		<#list rangeTypeList as item>
			<tr>
				<td><input type="checkbox" value='{"id":"${(item.CodeKey)!}","name":"<@s.text name="${(item.Value)!}"></@s.text>"}'/></td>
				<td>${(item.CodeKey)!}</td>
				<td><@s.text name="${(item.Value)!}"></@s.text></td>
			</tr>
		</#list>
	     </tbody>
	</table>
</#macro>
