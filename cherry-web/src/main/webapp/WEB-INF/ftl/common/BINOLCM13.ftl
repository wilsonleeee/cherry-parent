<#-- 部门结构联动画面模板 -->
<#-- struts 标签库 -->
<#global s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"] />
<#if (!modeArr?exists || modeArr?size == 0)>
<#assign showMode=0/>
<#else>
<#assign showMode=1/>
</#if>
<div id="DEPART_DIV" class="clear <#if showMode==0>hide</#if>">
	<div id="hide_comm_div">
		<#-- 业务类型 -->
		<input type="hidden" id="BUSINESSTYPE" value="${businessType!}" name="businessType"/>
		<#-- 操作类型 -->
		<input type="hidden" id="OPERATIONTYPE" value="${operationType!}" name="operationType"/>
	</div>
	<#if showMode==1>
		<#-- ==============直线================== -->
		<hr style="margin: 0 0 0.5em">
		<div class="tabs">
			<#assign modeStr=""/>
			<ul>
				<#list modeArr as mode>
					<#assign modeStr>${modeStr}${mode}<#if mode_has_next>,</#if></#assign>
					<#assign listSize=1 />
					<#if mode == 'dpat'>
						<#assign listSize=gradeList!?size/>
					<#elseif mode == 'area'>
						<#assign listSize=areaList!?size/>
					<#elseif mode == 'chan'>
						<#assign listSize=channelList!?size/>
					</#if>
					<#if listSize gt 0>
					<li><a href="#tabs-${mode}Mode"><@s.property value='#application.CodeTable.getVal("1145","${mode}")'/></a></li>
					</#if>
		        </#list>
		    </ul>
		    <div class="right">
		    	<#if orgValidType ?? && orgValidType == "1">
			    	<input type="checkbox"<#if orgValidAll == "1"> checked="checked"</#if>
			    		name="orgValidAll" id="orgValidAll" value="1" onclick="getDepartBar('${modeStr}','${showType}','${(orgValidType)!0}');">
			    	<span><@s.text name="global.page.orgValidAll"/></span>
		    	</#if>
	    		<input type="checkbox"<#if orgValid == "0"> checked="checked"</#if>
		    		name="orgValid" id="orgValid" value="0" onclick="getDepartBar('${modeStr}','${showType}','${(orgValidType)!0}');">
		    	<span><@s.text name="global.page.orgValid"/></span>
		    	<input type="checkbox"<#if testType== "1"> checked="checked"</#if>
		    		name="testType" id="testType" value="1" onclick="getDepartBar('${modeStr}','${showType}','${(orgValidType)!0}');">
		    	<span><@s.text name="global.page.testType"/></span>
		    </div>
		    <#list modeArr as mode>
				<#if mode == 'dpat'><#if gradeList!?size gt 0><div id="tabs-dpatMode" style="padding:5px 0;"><@getDepart list=gradeList/></div></#if>
				<#elseif mode == 'area'><#if areaList!?size gt 0><div id="tabs-areaMode" style="padding:5px 0;"><@getArea list=areaList/></div></#if>
			    <#elseif mode == 'chan'><#if channelList!?size gt 0><div id="tabs-chanMode" style="padding:5px 0;"><@getChannel channelList=channelList/></div></#if>
			    <#elseif mode == 'dpot'><div id="tabs-dpotMode" style="padding:5px 0;"><@getDepot/></div>
	        	</#if>
	        </#list>
		</div>
	</#if>
</div>
<#-- ========================================= 宏定义开始 ========================================== -->
<#-- 组织模式输出 -->
<#macro getDepart list>
	<#-- 组织级别 -->
	<#if showType != "0"><@getDepartType list=list/></#if>
	<#-- 组织范围 -->
	<@getDepartRange list=list/>
</#macro>
<#-- 仓库模式输出 -->
<#macro getDepot>
<#if showLgcDepot == 0>
	<label><@s.text name="global.page.depot"/></label>
	<p style="width: 170px;"><@getText id="depotModeText" name="depotId" titleType=2/></p>
<#else>
	<label><@s.text name="global.page.depotMode"/></label>
	<table class="LGCINVENTORYLINE">
		<thead>
			<th class="center"><@s.text name="global.page.depot"/></th>
			<th class="center"><@s.text name="global.page.logicInventory"/></th>
		</thead>
		<tbody>
		<tr>
			<td style="width: 170px;"><@getText id="depotModeText" name="depotId" titleType=2/></td>
			<td>
				<select id="LGCINVENTORY" name="lgcInventoryId">
					<option value=""></option>
					<#if lgcInventoryList!?size gt 0>
						<#list lgcInventoryList as item>
							<option value="${(item.lgcInventoryId)?c}">${(item.lgcInventoryName)!?html}</option>
						</#list>
					</#if>
				</select>
				<select id="LGCINVENTORY_OPTION" class="hide" disabled="true">
					<option value=""></option>
					<#if lgcInventoryList!?size gt 0>
						<#list lgcInventoryList as item>
							<option value="${(item.lgcInventoryId)?c}">${(item.lgcInventoryName)!?html}</option>
						</#list>
					</#if>
				</select>
			</td>
		</tr>
		</tbody>
	</table>
</#if>

</#macro>
<#-- 渠道模式输出 -->
<#macro getChannel channelList>
<label><@s.text name="global.page.channelMode"/></label>
<table class="CHANNELLINE">
	<thead>
		<th class="center"><@s.text name="global.page.Channel"/></th>
		<th class="center">${application.CodeTable.getVal("1000", 4)}</th>
	</thead>
	<tbody>
	<tr>
		<td>
			<select id="CHANNEL" name="channelId" onchange="cleanCounter('#channelModeText_4');">
				<option value=""></option>
				<#list channelList as channel>
					<option value="${(channel.channelId)?c}">${(channel.channelName)!?html}</option>
				</#list>
			</select>
		</td>
		<td><@getText id="channelModeText_4" name="departId" titleType=0/></td>
	</tr>
	</tbody>
</table>
</#macro>

<#-- 区域模式输出 -->
<#macro getArea list>
<label><@s.text name="global.page.areaMode"/></label>
<table class="AREALINE">
	<thead>
		<th class="center"><@s.text name="global.page.region"/></th>
		<th class="center"><@s.text name="global.page.province"/></th>
		<th class="center"><@s.text name="global.page.city"/></th>
		<th class="center"><@s.text name="global.page.county"/></th>
		<th class="center"><@s.text name="global.page.depart"/></th>
	</thead>
	<tbody>
	<tr>
		<td>
			<select id="AREA_REGION" name="regionId" onChange="getsubRegionList(this);">
				<option value=""></option>
				<#list list as area>
					<option value="${(area.reginId)?c}">${(area.reginName)!?html}</option>
				</#list>
			</select>
		</td>
		<td>
			<select id="AREA_PROVINCE" name="provinceId" onChange="getsubRegionList(this);">
				<option value=""></option>
			</select>
		</td>
		<td>
			<select id="AREA_CITY" name="cityId" onChange="getsubRegionList(this);">
				<option value=""></option>
			</select>
		</td>
		<td>
			<select id="AREA_COUNTY" name="countyId">
				<option value=""></option>
			</select>
		</td>
		<td><@getText id="areaModeText" name="departId" titleType=1/></td>
	</tr>
	</tbody>
</table>
</#macro>

<#-- 组织类型输出 -->
<#macro getDepartType list>
<#if list?size gt 1>
	<label><@s.text name="global.page.departType"/></label>
	<p id="DEPARTTYPE">
		<span>
			<input id="radio_0" type="radio" name="departType" value="" checked="checked" onclick="setDepartDisable(this)"/>
			<label for="radio_0"><@s.text name='global.page.all'/></label>
		</span>
		<#list list as grade>
			<#if (grade.departList!?size > 0)>
			<span>
				<input id="radio_${grade_index+1}" type="radio" name="departType" onclick="setDepartDisable(this)" value="<#list grade.list as item>${(item.CodeKey)!?html}<#if item_has_next>_</#if></#list>"/>
				<label for="radio_${grade_index+1}"><#list grade.list as item>${(item.Value)!?html}<#if item_has_next><font color="red">|</font></#if></#list></label>
			</span>
			</#if>
		</#list>
	</p>
</#if>
</#macro>
<#-- 组织范围输出 -->
<#macro getDepartRange list>
<#if list?size gt 0>
	<label><@s.text name="global.page.departGrade"/></label>
	<table id="DEPARTLINE" class="DEPARTLINE">
		<thead>
			<#list list as grade>
				<#if (grade.departList!?size > 0)>
					<th class="center">
					<#list grade.list as item>${(item.Value)!?html}<#if item_has_next><font color="red">|</font></#if></#list>
					</th>
				</#if>
			</#list>
		</thead>
		<tbody>
		<tr>
			<#list list as grade>
				<#if (grade.departList!?size > 0)>
					<#assign showCnt=false />
					<#assign id>DEPART_<#list grade.list as item><#if item.CodeKey == '4'><#assign showCnt=true /></#if>${item.CodeKey}<#if item_has_next>_</#if></#list></#assign>
					<td>
					<#if showCnt><@getText id="departModeText_4" name="departId" titleType=0/>
					<#else>
						<select id="${id}" onChange="getDepart(this);" name="departId">
							<option value=""></option>
							<#list grade.departList as depart>
								<option value="${(depart.departId)?c}">[${(depart.departCode)!?html}]${(depart.departName)!?html}</option>
							</#list>
						</select>
					</#if>
					</td>
				</#if>
			</#list>
		</tr>
		</tbody>
	</table>
</#if>
</#macro>
<#-- :TEXT框输出 -->
<#macro getText id name titleType>
<#if titleType == 1><#assign title><@s.text name='global.page.depart'/></#assign>
<#elseif titleType == 2><#assign title><@s.text name='global.page.depot'/></#assign>
<#else><#assign title><@s.text name='global.page.counter'/></#assign>
</#if>
<input id="${id}" style="width: 98%;width: 96%\9" title="<@s.text name='global.page.textTitle'/>${title}"/><input type="hidden" name="${name}" value="" />
</#macro>
<#-- ========================================= 宏定义结束 ========================================== -->
<#-- ====================================== CSS及JS定义开始 ======================================== -->
<#if showMode==1>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<#-- 部门等级级数 -->
<#assign gradeSize = gradeList!?size />
<style>
	#DEPART_DIV {
		margin-bottom: 10px;
	}
	#DEPART_DIV p{
		margin: 10px 0 10px 85px;
	}
	#DEPART_DIV select{
		height:22px;
		margin:0px;
	}
	
	#DEPART_DIV table{
		margin: 5px 0 10px 85px;
	}
	
	table.DEPARTLINE{
	<#if gradeSize == 5>
		width:80%;
	<#elseif gradeSize gt 5>
		width:85%;
	<#else>
		width:${gradeSize*165}px;
	</#if>
	}
	
	table.AREALINE{
		width:80%;
	}
	
	#DEPART_DIV label{
		width: 65px; 
		float:left;
		margin-top: 10px;
		margin-left: 10px;
		text-align: right;
	}
	#DEPARTTYPE label{
		width: auto; 
		float:none;
		margin-top: 2px;
		margin-left: -3px;
	}
	.DEPARTLINE select,.AREALINE select {
		width: 100%;
	}
	.CHANNELLINE select,.LGCINVENTORYLINE select{
		width:170px;
	}
	#DEPART_DIV th{
		height:auto;
		line-height:20px;
		cursor:default;
	}
	#DEPART_DIV th.sorting{
		background: url("../css/common/blueprint/images/bg_sorting.gif") repeat-x scroll 0 0 transparent;
    	color: #CC6600;
    	padding:0px 5px;
	}
	
	#DEPART_DIV td {
		padding: 0;
	}
	.AREALINE td {
		width: 20%;
		max-width:100px;
	}
	<#if gradeSize gt 0>
	.DEPARTLINE td{
		width: ${100/gradeSize}%;
		max-width:100px;
	}
	</#if>
	.tabs {
		background: none repeat scroll 0 0 #F9F9F9;
	}
	.tabs .right{
		margin:-25px 15px 0 0;
	}
	#DEPARTTYPE span.block{
		border:1px dotted red;
	}
</style>
<script>
$(function(){
	$("#DEPART_DIV").find('div.tabs').tabs();
	<#if modeArr??>
		<#list modeArr as mode>
			<#if mode=="dpat">
				<#if gradeList!?size gt 0>departInfoBinding({elementId:"departModeText_4",showNum:20});</#if>
			<#elseif mode=="area">
				<#if areaList!?size gt 0>departInfoBinding({elementId:"areaModeText",showNum:20});</#if>
			<#elseif mode=="chan">
 				<#if channelList!?size gt 0>departInfoBinding({elementId:"channelModeText_4",showNum:20});</#if>
			<#elseif mode=="dpot">	
				depotInfoBinding({elementId:"depotModeText",showNum:20});
			</#if>
		</#list>
	</#if>
});
</script>
</#if>
<#-- ====================================== CSS及JS定义结束 ======================================== -->