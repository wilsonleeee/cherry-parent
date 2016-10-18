<div class="box4">
	<div class="box4-header">
 		<strong><@s.text name="ruleCustom" /><@s.text name="setting" /></strong>
	</div>
	<div class="box4-content">
		<input type="hidden" id="prmActiveName" value="${pageTemp.prmActiveName!}"/>
		<#assign memberType = pageTemp.memberType!/>
		
		<div class="FORM_CONTEXT">
			<#-- ======对象类型======  -->
			<@getMebTypeBox pageTemp=pageTemp memberType=memberType/>
		</div>
		<div id="conInfoDiv_0" class="relation clearfix" style="margin-top:0px;<#if memberType == '0' || memberType == '5' || memberType == '6'>display:none;</#if>">
			<p id="searchCondition_0" class="left green" style="text-align:left;margin:5px 10px;">${pageTemp.conInfo!}</p>
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
<div class="hide" id="searchCondialogInit"></div>
<#include '/WEB-INF/ftl/common/dataTable_i18n.ftl'/>

<#macro getMebTypeBox pageTemp index=0 memberType=''>
<#--可编辑Flag-->
<#assign enEditFlag = false/>
<#if !pageTemp.state?exists || pageTemp.state=='0' || pageTemp.state=='3'>
	<#assign enEditFlag = true/>
</#if>
<div class="relation clearfix" style="margin-top:0px;">
	<#-- ======对象类型======  -->
	<span style="margin:3px 5px;" class="left">
		<span style="margin:3px 10px;" class="left">
			<@s.text name="cp.memObjectType" />
		</span>
		<@getMebType memberType=memberType subCampValid=pageTemp.subCampValid! index=index enEditFlag=enEditFlag/>
	</span>
	<#-- 选择搜索条件-->
	<span id="linkMebSearch_${index}" class="<#if !(memberType == '1' || memberType == '2')>hide</#if>">
		<a id="importLink_${index}" onclick="PRM68_3.searchConDialog(this,${index});return false;" class="search left" style="margin: 3px 0 0 20px;">
      		<span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.choice" /></span>
    	</a>
	</span>
	<#-- 导入弹出框-->
	<span id="linkMebImport_${index}" class="<#if memberType != '3'>hide</#if>">
		<a id="memImportLink_${index}" onclick="PRM68_3.popMemImport();return false;" class="search left" style="margin: 3px 0 0 20px;">
      		<span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.choice" /></span>
    	</a>
	</span>
	<#-- 活动对象数量显示-->
	<span id="memCountShow_${index}" class="left ShowCount <#if (memberType == '0' || memberType == '5' || memberType == '6')>hide</#if>" style="margin:5px 20px;">
		<@s.text name="cp.memCount" />
		<strong id="memCount_${index}" class="green"><@s.text name="format.number"><@s.param value="${pageTemp.memberCount!0}"></@s.param></@s.text></strong>
		<span id="yugu_${index}" class="red <#if memberType != '1'>hide</#if>">[<@s.text name="global.page.estimate" />]</span>
	</span>
	<#-- 查询活动对象-->
	<span id="searchMeb_${index}" class="right <#if memberType == '0' || memberType == '5' || memberType =='6'>hide</#if>">
		<a onclick="PRM68_3.memSearch(${index});return false;" class="search" style="margin: 3px 0 0 20px;">
      		<span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.memSearch" /></span>
    	</a>
	</span>
	<#-- 活动对象JSON-->
	<input type="hidden" id="memberJson_${index}" name="pageC.memberJson" value="<#if (memberType == '1' || memberType == '2' || memberType == '3')>${(pageTemp.memberJson)!?html}</#if>"/>
	<div class="SEARCHCODE"><input type="hidden" id="searchCode_${index}" name="pageC.searchCode" value="<#if (memberType == '1' || memberType == '2' || memberType == '3')>${(pageTemp.searchCode)!}</#if>"/></div>
	<div class="hide" id="objDialogTitle"><@s.text name="objDialogTitle" /></div>
	<div class="hide" id="dialogConfirm"><@s.text name="dialogConfirm" /></div>
	<div class="hide" id="dialogCancel"><@s.text name="dialogCancel" /></div>
</div>
</#macro>
<#-- *********活动对象类型******** -->
<#-- index 	:子活动列表索引		  -->
<#-- **************************** -->
<#macro getMebType memberType='' index=0 subCampValid='' enEditFlag=false>
<select id="memberType_${index}" name="pageC.memberType" onchange="PRM68_3.changeMebType(this,${index});" <#if !enEditFlag>disabled="disabled"</#if> style="width:110px;">
			<option <#if '5'==memberType!>selected="true"</#if> value="5"><@s.text name="cp.campMebType_5" /></option>
			<option <#if '0'==memberType!>selected="true"</#if> value="0"><@s.text name="cp.campMebType_0" /></option>
			<option <#if '6'==memberType!>selected="true"</#if> value="6"><@s.text name="cp.campMebType_6" /></option>
			<option <#if '1'==memberType!>selected="true"</#if> value="1"><@s.text name="cp.campMebType_1" /></option>
			<option <#if '2'==memberType!>selected="true"</#if> value="2"><@s.text name="cp.campMebType_2" /></option>
			<option <#if '3'==memberType!>selected="true"</#if> value="3"><@s.text name="cp.campMebType_3" /></option>

</select>
<#if !enEditFlag><input type="hidden" name="pageC.memberType" value="${memberType!}" /></#if>
</#macro>