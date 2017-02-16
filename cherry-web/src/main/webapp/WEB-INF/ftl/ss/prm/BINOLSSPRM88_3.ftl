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
            <div class="ui-tabs">
                <ul class ="ui-tabs-nav clearfix">
                    <li value="1" class="ui-tabs-selected" onclick="PRM88_3.changeFilterType(this);return false;"><a>活动对象白名单</a></li>
                    <li value="2" onclick="PRM88_3.changeFilterType(this);return false;"><a>活动对象黑名单</a></li>
                </ul>
			</div>
	        <div class="section hide" id="memberInfo_0">
			  <div class="section-content">
			    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="memberDataTable_0">
			      <thead>
			        <tr>
                        <th>品牌*</th>
                        <th>会员卡号</th>
                        <th>会员手机号*</th>
                        <th>BP号</th>
                        <th>会员等级</th>
                        <th>会员姓名</th>
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
    <div class="hide" id="memberDivDialog"></div> <!-- 用于弹出导入产品DIV -->
	<#-- ======对象类型======  -->
	<span style="margin:3px 5px;" class="left">
		<span style="margin:3px 10px;" class="left">
			<@s.text name="cp.memObjectType" />
		</span>
		<@getMebType memberType=memberType subCampValid=pageTemp.subCampValid! index=index enEditFlag=enEditFlag/>
	</span>
	<#-- 选择搜索条件-->
	<span id="linkMebSearch_${index}" class="<#if !(memberType == '1' || memberType == '2')>hide</#if>">
		<a id="importLink_${index}" onclick="PRM88_3.searchConDialog(this,${index});return false;" class="search left" style="margin: 3px 0 0 20px;">
      		<span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.choice" /></span>
    	</a>
	</span>
	<#-- 导入弹出框-->
	<span id="linkMebImport_${index}" class="<#if memberType != '3'>hide</#if>">
		<a id="memImportLink_${index}" onclick="PRM88_3.popMemberLoadDialog('1');return false;" class="search left" style="margin: 3px 0 0 20px;">
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
	<span id="searchMeb_${index}" class="right <#if memberType =='6'>hide</#if>">
		<a onclick="PRM88_3.memSearch(${index});return false;" class="search" style="margin: 3px 0 0 20px;">
            <span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.memSearch" /></span>
        </a>
	</span>
	<#-- 排除会员导入-->
	<span id="mebImpBlock_${index}" class="right <#if memberType !='6'>show <#else>  hide </#if> ">
		<a id="memImportBlockLink_${index}" onclick="PRM88_3.popMemberLoadDialog('2');return false;" class="search left" style="margin: 3px 0 0 20px;">
            <span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="排除会员导入" /></span>
        </a>
	</span>
	<#-- 活动对象JSON-->
	<input type="hidden" id="memberJson_${index}" name="pageC.memberJson" value="<#if (memberType == '1' || memberType == '2' || memberType == '3')>${(pageTemp.memberJson)!?html}</#if>"/>
	<div class="SEARCHCODE"><input type="hidden" id="searchCode_${index}" name="pageC.searchCode" value="<#if (memberType == '1' || memberType == '2' || memberType == '3')>${(pageTemp.searchCode)!}</#if>"/></div>
	<div class="SEARCHCODEBLACK"><input type="hidden" id="searchCodeBlack_${index}" name="pageC.searchCodeBlack" value="<#if !(memberType == '6') >${(pageTemp.searchCodeBlack)!}</#if>"/></div>
	<div class="hide" id="objDialogTitle"><@s.text name="objDialogTitle" /></div>
	<div class="hide" id="dialogConfirm"><@s.text name="dialogConfirm" /></div>
	<div class="hide" id="dialogCancel"><@s.text name="dialogCancel" /></div>
</div>
</#macro>
<#-- *********活动对象类型******** -->
<#-- index 	:子活动列表索引		  -->
<#-- **************************** -->
<#macro getMebType memberType='' index=0 subCampValid='' enEditFlag=false>
<select id="memberType_${index}" name="pageC.memberType" onchange="PRM88_3.changeMebType(this,${index});" <#if !enEditFlag>disabled="disabled"</#if> style="width:110px;">
			<option <#if '5'==memberType!>selected="true"</#if> value="5"><@s.text name="cp.campMebType_5" /></option>
			<option <#if '0'==memberType!>selected="true"</#if> value="0"><@s.text name="cp.campMebType_0" /></option>
			<option <#if '6'==memberType!>selected="true"</#if> value="6"><@s.text name="cp.campMebType_6" /></option>
			<option <#if '1'==memberType!>selected="true"</#if> value="1"><@s.text name="cp.campMebType_1" /></option>
			<option <#if '2'==memberType!>selected="true"</#if> value="2"><@s.text name="cp.campMebType_2" /></option>
			<option <#if '3'==memberType!>selected="true"</#if> value="3"><@s.text name="cp.campMebType_3" /></option>

</select>
<#if !enEditFlag><input type="hidden" name="pageC.memberType" value="${memberType!}" /></#if>
</#macro>