<#include "/WEB-INF/ftl/common/head.inc.ftl">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/cp/common/BINOLCPCOM05.js"></script>
<script type="text/javascript" src="/Cherry/js/cp/common/campaignTemplate.js"></script>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<#if brandCode == 'BEN'>
<#include "/WEB-INF/ftl/cp/common/BINOLCPCOM05_BENE.ftl">
<#else>
<#include "/WEB-INF/ftl/cp/common/BINOLCPCOM05_COM.ftl">	
</#if>
</@s.i18n>
<#-- *******取得活动阶段模块****** -->
<#-- time  		活动阶段                            -->
<#-- **************************** -->
<#macro getTimeItem time>
<#assign required = false/>
<#assign show = false/>
<#assign editFlag = true/>
<#if (campInfo.state! =='1' || campInfo.state! == '2') && copyFlag != 1><#assign editFlag = false/></#if>
<#if time.CodeKey == '1'>
	<#assign fromDate = "campaignOrderFromDate"/>
	<#assign toDate = "campaignOrderToDate"/>
	<#assign fromDateValue = campInfo.campaignOrderFromDate!/>
	<#assign toDateValue = campInfo.campaignOrderToDate!/>
<#elseif time.CodeKey == '2'>
	<#assign fromDate = "campaignStockFromDate"/>
	<#assign toDate = "campaignStockToDate"/>
	<#assign fromDateValue = campInfo.campaignStockFromDate!/>
	<#assign toDateValue = campInfo.campaignStockToDate!/>
<#elseif time.CodeKey == '3'>
	<#assign fromDate = "obtainFromDate"/>
	<#assign toDate = "obtainToDate"/>
	<#assign fromDateValue = campInfo.obtainFromDate!/>
	<#assign toDateValue = campInfo.obtainToDate!/>
	<#assign required = true/>
</#if>
<#if time.CodeKey == '3' || (fromDateValue != '' || toDateValue !='')>
<#assign show = true/>
</#if>
<li id="${time.CodeKey}" <#if !show>class="hide"</#if> style="margin:5px 0px;">
	<table cellpadding="0" cellspacing="0" style="width:100%;">
        <tr>
          <th style="width:15%;">
          	<span class="ui-icon icon-arrow-crm"></span>
          	<span>${time.Value!}</span>
          	<#if required><span class="highlight">*</span></#if>
          </th>
          <td style="width:85%;">
      		<@getReferTime codeKey=time.CodeKey! fromDate=fromDate toDate=toDate fromDateValue=fromDateValue toDateValue=toDateValue editFlag=editFlag/>
	      	<#if !required && editFlag>
			  	<a style="display: block; margin-top:5px;" onClick="BINOLCPCOM05.removeCampTime(this);" class="delete right">
					<span class="ui-icon icon-delete"></span>
					<span class="button-text"><@s.text name="global.page.delete" /></span>                    
				</a>
	  		</#if>
          </td>
        </tr>
    </table>
    <div class="clearfix"></div>
</li>
</#macro>
<#--时间阶段开始结束块-->
<#macro getReferTime codeKey fromDate='' toDate='' fromDateValue='' toDateValue='' editFlag=true>
<#assign referType = '0'/>
<span>
	<#if codeKey== '3'>
		<#if campInfo.referType! !=''><#assign referType = campInfo.referType/></#if>
		<select name="campInfo.referType" class="left" style="width:160px;" <#if !editFlag>disabled</#if>
			onchange="BINOLCPCOM05.changeReferType(this,'${codeKey}');">
			<option <#if referType== '0'>selected</#if> value='0'><@s.text name="cp.referType0"/></option>
			<option <#if referType== '1'>selected</#if> value='1'><@s.text name="cp.referType1"/></option>
			<option <#if referType== '2'>selected</#if> value='2'><@s.text name="cp.referType2"/></option>
			<option <#if referType== '3'>selected</#if> value='3'><@s.text name="cp.referType3"/></option>
			<option <#if referType== '4'>selected</#if> value='4'><@s.text name="cp.referType4"/></option>
			<option <#if referType== '9'>selected</#if> value='9'><@s.text name="cp.referType9"/></option>
			<option <#if referType== '6'>selected</#if> value='6'><@s.text name="cp.referType6"/></option>
			<option <#if referType== '7'>selected</#if> value='7'><@s.text name="cp.referType7"/></option>
			<option <#if referType== '8'>selected</#if> value='8'><@s.text name="cp.referType8"/></option>
			<option <#if referType== '5'>selected</#if> value='5' class="hide"><@s.text name="cp.referType5"/></option>
		</select>
		<#if !editFlag><input type="hidden" name="campInfo.referType" value="${referType}"/></#if>
		<div id="referType_${codeKey}_1" class="left <#if referType=='0'>hide</#if>">
			<select name="campInfo.attrA" style="width:45px;">
				<option <#if campInfo.attrA! == '2'>selected</#if> value="2"><@s.text name="cp.attrA2"/></option>
				<option <#if campInfo.attrA! == '1'>selected</#if> value="1"><@s.text name="cp.attrA1"/></option>
			</select>
			<span style="float:none;"><input class="number" name="campInfo.valA" value="${campInfo.valA!0}"/></span>
			<select name="campInfo.attrB" style="width:45px;">
				<option <#if campInfo.attrB! == '1'>selected</#if> value='1'><@s.text name="cp.attrB1"/></option>
				<option <#if campInfo.attrB! == '2'>selected</#if> value='2'><@s.text name="cp.attrB2"/></option>
			</select>
			<@s.text name="cp.valB"/>
			<span style="float:none;"><input class="number" name="campInfo.valB" value="${campInfo.valB!0}"/></span>
			<select name="campInfo.attrC" style="width:45px;">
				<option <#if campInfo.attrC! == '1'>selected</#if> value='1'><@s.text name="cp.attrB1"/></option>
				<option <#if campInfo.attrC! == '2'>selected</#if> value='2'><@s.text name="cp.attrB2"/></option>
			</select>
		</div>
	<#else>
		<select class="left" style="width:160px;" disabled>
			<option value='0'><@s.text name="cp.referType0"/></option>
		</select>
	</#if>
	<div id="referType_${codeKey}_0" class="left <#if referType !='0'>hide</#if>">
      	<span><input class="date" id="${fromDate!}" name="campInfo.${fromDate!}" value="${fromDateValue}"/></span>
		<span style="margin: 0 5px;"><@s.text name="cp.to" /></span>
		<span><input class="date" id="${toDate!}" name="campInfo.${toDate!}" value="${toDateValue}"/></span>
	</div>
</span>
</#macro>
