<@s.i18n name="i18n.ss.BINOLSSPRM68">
<#include "/WEB-INF/ftl/common/head.inc.ftl" />
<#include "/WEB-INF/ftl/ss/prm/BINOLSSPRM68_0.ftl" />
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryTree.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM68.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM68_${pageNo!}.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/template.css" type="text/css">
<#assign useCouponList = [{'CodeKey':'0','Value':'useCoupon_0'},{'CodeKey':'1','Value':'useCoupon_1'}] />
<#assign enMessageList = [{'CodeKey':'0','Value':'enMessage_0'},{'CodeKey':'1','Value':'enMessage_1'}] />
<#assign isMustList = [{'CodeKey':'0','Value':'isMust_0'},{'CodeKey':'1','Value':'isMust_1'}] />
<#assign ruleTypeList = [{'CodeKey':'1','Value':'ruleType_1'},{'CodeKey':'2','Value':'ruleType_2'}] />
<#assign condTypeList = [{'CodeKey':'0','Value':'ruleCondType_0'},{'CodeKey':'1','Value':'ruleCondType_1'},{'CodeKey':'2','Value':'ruleCondType_2'}] />
<#assign zgqFlagList = [{'CodeKey':'0','Value':'zgqFlag_0'},{'CodeKey':'1','Value':'zgqFlag_1'}] />
<#assign needBuyFlagList = [{'CodeKey':'1','Value':'needBuyFlagTxt_1'},{'CodeKey':'0','Value':'needBuyFlagTxt_0'}] />

<#assign rangeTypeList = [{'CodeKey':'ALL','Value':'rangeType_a'},
							{'CodeKey':'PRODUCT','Value':'rangeType_p'},
							{'CodeKey':'RANGE','Value':'rangeType_r'},
							{'CodeKey':'PRICERANGE','Value':'rangeType_price'},
							{'CodeKey':'BCLASS','Value':'rangeType_b'},
							{'CodeKey':'MCLASS','Value':'rangeType_m'},
							{'CodeKey':'LCLASS','Value':'rangeType_l'},
							{'CodeKey':'UNITCODE','Value':'global.page.unitCode'},
							{'CodeKey':'BARCODE','Value':'global.page.barcode'},
							{'CodeKey':'ZD','Value':'rangeType_zd'}]/>
							
<#assign rangeTypeList2 = [{'CodeKey':'ALL','Value':'rangeType_a'},
							{'CodeKey':'PRODUCT','Value':'rangeType_p'},
							{'CodeKey':'PRICERANGE','Value':'rangeType_price'},
							{'CodeKey':'BCLASS','Value':'rangeType_b'},
							{'CodeKey':'MCLASS','Value':'rangeType_m'},
							{'CodeKey':'LCLASS','Value':'rangeType_l'}]/>

<#assign rangeOptList_DNZK = [{'CodeKey':'EQ','Value':'rangeOpt_EQ'},{'CodeKey':'GE','Value':'rangeOpt_GE'}]/>
<#assign rangeOptList = [{'CodeKey':'','Value':''},{'CodeKey':'EQUAL','Value':'rangeOpt_e'},{'CodeKey':'LIKE','Value':'rangeOpt_l'}]/>
<#assign propNameList = [{'CodeKey':'QUANTITY','Value':'global.page.quantity'},{'CodeKey':'AMOUNT','Value':'global.page.amount'}]/>
<#assign propNameList2 = [{'CodeKey':'SUMAMOUNT','Value':'global.page.sumAmount'},{'CodeKey':'SUMQUANTITY','Value':'global.page.sumQuantity'}]/>
<#assign rewardTypeList = [{'CodeKey':'GIFT','Value':'rewardType_GIFT'},
							{'CodeKey':'ZDMS','Value':'rewardType_ZDMS'},
							{'CodeKey':'ZDYH','Value':'rewardType_ZDYH'},
							{'CodeKey':'DPYH','Value':'rewardType_DPYH'},
							{'CodeKey':'ZDZK','Value':'rewardType_ZDZK'},
							{'CodeKey':'DPZK','Value':'rewardType_DPZK'},
							{'CodeKey':'DPTJ','Value':'rewardType_DPTJ'},
							{'CodeKey':'DNZK','Value':'rewardType_DNZK'},
							{'CodeKey':'TZZK','Value':'rewardType_TZZK'},
							{'CodeKey':'YHTZ','Value':'rewardType_YHTZ'},
							{'CodeKey':'JJHG','Value':'rewardType_JJHG'},
							{'CodeKey':'JFDK','Value':'rewardType_JFDK'},
							{'CodeKey':'SGGJ','Value':'rewardType_SGGJ'},
							{'CodeKey':'ZDXL','Value':'rewardType_ZDXL'}]/>
<#assign rangeTypePrmList = [{'CodeKey':'PBCLASS','Value':'rangeTypePrm_b'},
								{'CodeKey':'PMCLASS','Value':'rangeTypePrm_m'},
								{'CodeKey':'PLCLASS','Value':'rangeTypePrm_l'},
								{'CodeKey':'PRMPRODUCT','Value':'rangeTypePrm_p'}]/>
<#assign quantityTypeListB = [{'CodeKey':'RANDOM','Value':'quantityType_RANDOM1'},
								{'CodeKey':'MIN','Value':'quantityType_MIN'},
								{'CodeKey':'MAX','Value':'quantityType_MAX'}]/>
<#assign quantityTypeListA = [{'CodeKey':'ALL','Value':'quantityType_ALL'},
								{'CodeKey':'RANDOM','Value':'quantityType_RANDOM2'}]/>
								
<#assign quantityTypeListALL = [{'CodeKey':'ALL','Value':'quantityType_ALL'}]/>

<#assign basePriceList = [{'CodeKey':'0','Value':'basePrice_0'},
	{'CodeKey':'1','Value':'basePrice_1'},
	{'CodeKey':'2','Value':'basePrice_2'}]/>

<div class="main container clearfix" id="div_main">
	<div class="panel ui-corner-all">
		<@pageTitle/>
		<div id="actionResultDisplay" class=""><@s.actionerror/><@s.fielderror/></div>
		<div class="panel-content">
			<@navigate pageNo=pageNo!/>
			<@c.form id="mainForm" method="post" class="inline" csrftoken="false" action="BINOLSSPRM68_init">
				<@comHidden pageNo=pageNo! opt=opt! csrftoken=csrftoken! brandInfoId=pageTemp.brandInfoId! activeID=activeID!/>
				<#include "/WEB-INF/ftl/ss/prm/BINOLSSPRM68_${pageNo!}.ftl" />
			</@c.form>
			<@button pageNo=pageNo! />
		</div>
	</div>
</div>
</@s.i18n>
<#-- 标题 -->
<#macro pageTitle>
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left"> 
				<span class="ui-icon icon-breadcrumb"></span>
    			<a href="#"><@s.text name="titleA" /></a> > 
    			<a href="#"><@s.text name="titleB" /></a>
    		</span>
		</div>
	</div>
</#macro>
<#-- 导航 -->
<#macro navigate pageNo=1>
	<#if pageNo != 6>
		<ol id="ol_steps" class="steps clearfix">
			<li style="width:20%" class="<#if pageNo == 1>on<#elseif pageNo == 2>prev</#if>"><span><@s.text name="baseInfo" /></span></li>
			<li style="width:20%" class="<#if pageNo == 2>on<#elseif pageNo == 3>prev</#if>"><span><@s.text name="TimePlace" /></span></li>
			<li style="width:20%" class="<#if pageNo == 3>on<#elseif pageNo == 4>prev</#if>"><span><@s.text name="ruleCustom" /></span></li>
			<li style="width:20%" class="<#if pageNo == 4>on<#elseif pageNo == 5>prev</#if>"><span><@s.text name="ruleContent" /></span></li>
			<li style="width:20%" class="<#if pageNo == 5>on</#if> last"><span><@s.text name="preview" /></span></li>
		</ol>
	</#if>
</#macro>
<#-- 共通参数 -->
<#macro comHidden pageNo=1 opt='1' csrftoken='' brandInfoId=0 activeID=''>
	<input type="hidden" id="csrftoken" name="csrftoken" value="${csrftoken}" />
	<input type="hidden" id="brandInfoId" name="brandInfoId" value="${brandInfoId}"/>
	<input type="hidden" id="opt" name="opt" value="${opt}"/>
	<input type="hidden" id="pageNo" name="pageNo" value="${pageNo}"/>
	<input type="hidden" name="activeID" value="${activeID!}"/>
	<input type="hidden" id="step" name="step" value=""/>
	<input type ="hidden" name ="templateFlag" value="" id="templateFlag"/>
</#macro>
<#-- 步骤按钮 -->
<#macro button pageNo=1>
	<div class="center clearfix">
		<#if pageNo == 6>
			<button onclick="window.close();return false;" class="back" type="button">
				<span class="ui-icon icon-close"></span>
				<span class="button-text"><@s.text name="global.page.close" /></span>
			</button>
		<#else>
			<#if pageNo == 1>
				<button onclick="window.close();return false;" class="back" type="button">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><@s.text name="global.page.cancle" /></span>
				</button>
			<#else>
				<button onclick="PRM68.toNext(${pageNo},-1);return false;" class="save" type="button">
					<span class="ui-icon icon-movel"></span>
					<span class="button-text"><@s.text name="global.page.previous" /></span>
				</button>
			</#if>
			<#if pageNo == 5>
				<#if opt == '1'>
					<button onclick="PRM68.setTemplateFlag(1);PRM68.toNext(${pageNo},1);return false;" class="back" type="button">
						<span class="ui-icon icon-save"></span>
						<span class="button-text"><@s.text name="saveTemplate" /></span>
					</button>
				</#if>
				<button onclick="PRM68.toNext(${pageNo},1);return false;" class="back" type="button">
					<span class="ui-icon icon-save"></span>
					<span class="button-text"><@s.text name="global.page.save" /></span>
				</button>
			<#else>
				<button onclick="PRM68.toNext(${pageNo},1);return false;" class="save" type="button">
					<span class="ui-icon icon-mover"></span>
					<span class="button-text"><@s.text name="global.page.next" /></span>
				</button>
			</#if>
		</#if>
	</div>
</#macro>

<#macro logicBtn logicOpt='AND' disabled=false>
<span id="logicOpt_text_AND" class="hide"><@s.text name="logicOpt_AND" /></span>
<span id="logicOpt_text_OR" class="hide"><@s.text name="logicOpt_OR" /></span>
<div class="LOGICBTN">
	<button onclick="PRM68.changeLogicOpt(this);return false;" <#if disabled>disabled="disabled"</#if> class="button_${logicOpt}" type="button">
		<span class="text"><@s.text name="logicOpt_${logicOpt!}" /></span>
	</button>
</div>
</#macro>

<#macro getOptionList list val>
<#list list as code>
	<option <#if val == code.CodeKey>selected="selected"</#if> value="${code.CodeKey!}"><@s.text name="${code.Value!}" /></option>
</#list>
</#macro>

<#macro getRadioList list name val>
<#list list as code>
	<input type="radio" id="${name}_${code.CodeKey!}" name="${name}" value="${code.CodeKey!}" <#if val == code.CodeKey>checked="true"</#if> /><label for="${name}_${code.CodeKey!}" style="width:50px;"><@s.text name="${code.Value!}" /></label>
</#list>
</#macro>

<#macro getValue list key>
<#list list as code>
	<#if key ==code.CodeKey><@s.text name="${code.Value!}" /></#if>
</#list>
</#macro>