<#-- 会员查询工具栏宏定义 -->
<#macro getMemberSearchBox>
<div class="clearfix">
	<span class="left">
	    <strong>
			<span><@s.text name="mbcct.phoneIn"/>${(customerNumber)!?html}</span>
		</strong>
	</span>
	<span class="right" id="searchButton">
		<@s.text name="mbcct.memCode" />
		<input id="ccMemCode" name="ccMemCode" type="text" style="width:90px"/>
		<@s.text name="mbcct.memName" />
		<input id="ccMemName" name="ccMemName" type="text" style="width:60px"/>
		<@s.text name="mbcct.birthDay" />
		<input id="ccBirth" name="ccBirth" type="text" style="width:60px"/>
		<@s.text name="mbcct.telephone" />
		<input id="ccTelephone" name="ccTelephone" type="text" value="${(ccTelephone)!}" style="width:90px"/>
		<@s.text name="mbcct.mobilePhone" />
		<input id="ccMobilePhone" name="ccMobilePhone" type="text" value="${(ccMobilePhone)!}" style="width:90px"/>
		<button class="search"  onclick="BINOLMBCCT01.memberSearch();return false;">
          	<span class="ui-icon icon-search-big"></span>
          	<span class="button-text"><@s.text name="mbcct.search" /></span>
		</button>
	</span>
</div>
</#macro>
<#-- 会员列表页面宏定义 -->
<#macro memberListPage>
	<@memberListTemplate/>
</#macro>
<#-- 会员来电页面宏定义 -->
<#macro memberCallPage>
	<div class="box-content clearfix" style="margin:0;padding:0;" id="issueBox">
		<@addIssueTemplate/>
		<@issueListTemplate/>
	</div>
	<div class="callcenter_box" onclick="BINOLMBCCT01.showPage();return false;">
		<input type="hidden" id="showPageValue" name="showPageValue" value="0"/>
		<span class="callcenter_info" id="showText"><@s.text name="mbcct.hideText" /></span>
	</div>
	<@memberInfoTemplate/>
</#macro>
<#-- 非会员来电页面宏定义 -->
<#macro nonMemberCallPage>
	<div class="box-content clearfix" style="margin:0;padding:0;" id="issueBox">
		<@addIssueTemplate/>
		<@issueListTemplate/>
	</div>
</#macro>
<#-- 第一次来电的非会员来电页面宏定义 -->
<#macro newNonMemberCallPage>
	<div class="box-content clearfix" style="margin:0;padding:0;" id="issueBox">
		<@addIssueTemplate/>
		<@addCustomerTemplate/>
	</div>
</#macro>

<#-- 会员列表宏-->
<#macro memberListTemplate>
	<div class="box-content" style="margin:0;">
		<div class="clearfix" id="getMemberListDiv">
		</div>
	</div>
</#macro>
<#-- 增加问题宏-->
<#macro addIssueTemplate>
	<div id="leftPageDiv" class="column" style="width:50%;height:430px;overflow-y: auto;padding: 5px 0 0 0;">
		<div class="clearfix" style="padding: 0 1em;">
			<button class="right edit" id="addIssueButton" onclick="BINOLMBCCT01.newIssue();return false;">
	          	<span class="ui-icon icon-edit"></span>
	          	<span class="button-text"><@s.text name="mbcct.addIssue" /></span>
			</button>
		</div>
		<div class="clearfix" id="addIssueDiv" style="border:none;background: none;margin: 0px;">
		</div>
	</div>
</#macro>
<#-- 问题列表宏-->
<#macro issueListTemplate>
	<div id="rightPageDiv" class="column last" style="width:49%;height:430px;overflow-y: auto;padding: 5px 0 0 0;">
		<div id="getIssueDiv" style="padding: 0 1em;">
		</div>
	</div>
</#macro>
<#-- 增加非会员宏-->
<#macro addCustomerTemplate>
	<div id="rightPageDiv" class="column last" style="width:49%;overflow-y: auto;padding: 5px 0 0 0;">
		<div class="clearfix" id="addCustomerDiv" style="padding: 1em 0;">
		</div>
	</div>
</#macro>
<#-- 会员详细信息宏-->
<#macro memberInfoTemplate>
	<div id="memberInfoPage" class="box-content" style="height:450px; overflow-y: auto;padding: 0;margin: 0;">
		<div class="clearfix" id="getMemberInfoDiv">
		</div>
	</div>
</#macro>





