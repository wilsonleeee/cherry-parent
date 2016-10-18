<#include "/WEB-INF/ftl/common/head.inc.ftl">
<script type="text/javascript" src="/Cherry/js/jn/man/BINOLJNMAN08.js"></script>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
	<div class="main container clearfix">
 <div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
          <@s.text name="cp.ruleConfig" /> &gt; <@s.text name="cp.ruleConfigDetail" />
       </span>
    </div>
  </div>
  <div class="panel-content">
  <div class="tabs hide">
          <ul>
            <li><a href="#tabs-1"><@s.text name="cp.configBase" /></a></li>
            <li><a href="#tabs-2"><@s.text name="cp.comRule" /></a></li>
          </ul>
          <div id="tabs-1">
          <div id="actionResultDisplay"></div>
          <div class="section" >
              <div class="section-header">
              <strong><span class="ui-icon icon-ttl-section-info"></span>
              	<@s.text name="cp.configBase" />
              </strong>
              </div>
              <div class="section-content">
              <@c.form id="mainForm" method="post" class="inline" csrftoken="false">
              <table id="baseInfo" class="detail" cellpadding="0" cellspacing="0">
                <tr>
                	<th>
                       <label>
                       <@s.text name="cp.brandName" />
                       </label>
                     </th>
                     <td>
                       <span>
                         ${(brandName)!?html}
                       </span>
                     </td>
                  <th>
                  		
                        <label><@s.text name="cp.ruleConfigName" /></label>
                      </th>
                      <td>
                        <span>
                        ${(groupName)!?html}
                        </span>
                      </td>
                </tr>
                <tr>
                	<th>
                       <label>
                       	<@s.text name="cp.configDep" />
                       </label>
                     </th>
                     <td >
                       <span>
                         ${(descriptionDtl)!?html}
                       </span>
                     </td>
                     <#if (memberClubName??) >
				         	<th><label><@s.text name="cp.memclub" /></label></th>
	             		  	<td>
	             		  	 <span>
	             		  		${(memberClubName)!?html}
		            			 </span>
					        </td>
                     <#else>
                   <th>
                      </th>
                      <td>
                      </td>
                      </#if>
                </tr>
                </table>
              </div>
              </@c.form>
              <div class="section-header">
              <strong><span class="ui-icon icon-ttl-section-info"></span>
              	<@s.text name="cp.setPriority" />
              </strong>
              </div>
              <div class="section-content">
              
				<div id="prioContent">
		
		<div class="box4" >
		<div class="box4-header" >
			<Strong><@s.text name="cp.hasConfigRule" /></Strong>
		</div>
		<div class="box4-content" >
		
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="proTable">
		<tbody id="rulePriorityTable">
            <tr class="thClass">
               <th width="5%"><@s.text name="cp.hanghao" /></th>
               <th width="25%"><@s.text name="cp.ruleName" /></th>
               <th width="10%"><@s.text name="cp.startDateTxt" /></th>
               <th width="10%"><@s.text name="cp.endDateTxt" /></th>
               <th width="10%"><@s.text name="cp.configRuledef" /></th>
               <th width="20%"><@s.text name="cp.priority" /></th>
               <th width="20%"><@s.text name="cp.configRuleMatch" /></th>
            </tr>
            <#if (usedRuleList?? && usedRuleList?size>0) >
			<#list usedRuleList as usedRule>
				<tr><td>${(usedRule_index  + 1)!}</td>
				<td id="prio_${(usedRule.campaignId)!}">
					${(usedRule.campaignName)!}
					<#if (usedRule.extraRuleList?? && usedRule.extraRuleList?size>0) >
					<div class="extraRulePanel">
						<span style="display:block;margin-left:15px;"><label class="gray"><@s.text name="cp.affilRuleTxt" /></label></span>
							<#list usedRule.extraRuleList as extraRule>
							<span style="display:block;margin-left:15px;"><label class="gray"><label class="gray">${(extraRule.campaignName)!}</label></span>
						</#list>
						</#if>
					</div>
				</td>
				<td>${(usedRule.campaignFromDate)!}</td>
				<td>${(usedRule.campaignToDate)!}</td>
				<td>
				<#if (usedRule.strategy??) >
				${application.CodeTable.getVal("1199","${(usedRule.strategy)!?html}")}
				</#if>
				</td>
				<td>
				</td>
				<td>
				<#if (usedRule.afterMatch??) >
				${application.CodeTable.getVal("1191","${(usedRule.afterMatch)!?html}")}
				</#if>
				</td>
			</tr>
			</#list>
			</#if>
		</tbody>
			
		</table>
		</div>
		<#if (deftRuleInfo??) >
		<div class="box4-content" style="padding-top:0;" >
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tbody id="deftRuleTable">
			<tr style="background:#E7E7E7;"><td width="5%">－</td>
			<td id="prio_${(deftRuleInfo.campaignId)!}" width="25%">
				${(deftRuleInfo.campaignName)!} <Strong><@s.text name="cp.deftRule" /></Strong>
				<input type="hidden" name="campaignId" value="${(deftRuleInfo.campaignId)!}"/>
				
				<#if (deftRuleInfo.extraRuleList?? && deftRuleInfo.extraRuleList?size>0) >
					<div class="extraRulePanel">
					<span style="display:block;margin-left:15px;"><label class="gray"><@s.text name="cp.affilRuleTxt" /></label></span>
						<#list deftRuleInfo.extraRuleList as extraRule>
							<span style="display:block;margin-left:15px;"><label class="gray"><label class="gray">${(extraRule.campaignName)!}</label></span>
						</#list>
					</#if>
				</div>
			</td>
			<td width="10%">${(deftRuleInfo.campaignFromDate)!}</td>
			<td width="10%">${(deftRuleInfo.campaignToDate)!}</td>
			<td width="10%">
			－
			</td>
			<td width="20%">
			</td>
			<td width="20%">
			</td>
			</tr>
		</tbody>
		</table>
		</div>
		</#if>
		 </div>
		 <div class="box4" >
		<div class="box4-header" >
			<Strong><@s.text name="cp.notConPriority" /></Strong>
		</div>
		<div class="box4-content">
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="proTable">
		<tbody id="rulePriorityDotConTable">
            <tr class="thClass">
               <th width="5%"><@s.text name="cp.hanghao" /></th>
               <th width="25%"><@s.text name="cp.ruleName" /></th>
               <th width="10%"><@s.text name="cp.startDateTxt" /></th>
               <th width="10%"><@s.text name="cp.endDateTxt" /></th>
               <th width="10%"><@s.text name="cp.configRuledef" /></th>
               <th width="20%"><@s.text name="cp.priority" /></th>
               <th width="20%"><@s.text name="cp.configRuleMatch" /></th>
            </tr>
		<#if (unusedList?? && unusedList?size>0) >
			<#list unusedList as unusedRule>
				<tr><td>${(unusedRule_index  + 1)!}</td>
				<td id="prio_${(unusedRule.campaignId)!}">
					${(unusedRule.campaignName)!}
					<#if (unusedRule.extraRuleList?? && unusedRule.extraRuleList?size>0) >
					<div class="extraRulePanel">
						<span style="display:block;margin-left:15px;"><label class="gray"><@s.text name="cp.affilRuleTxt" /></label></span>
						<#list unusedRule.extraRuleList as extraRule>
							<span style="display:block;margin-left:15px;"><label class="gray"><label class="gray">${(extraRule.campaignName)!}</label></span>
					</#list>
					</#if>
					</div>
				</td>
				<td>${(unusedRule.campaignFromDate)!}</td>
				<td>${(unusedRule.campaignToDate)!}</td>
				<td>
				<#if (unusedRule.strategy??) >
				${application.CodeTable.getVal("1199","${(unusedRule.strategy)!?html}")}
				</#if>
				</td>
				<td></td>
				<td></td>
			</tr>
			</#list>
			</#if>
		</tbody>
		</table>
		</div>
	</div>
	 <div class="box4" >
		<div class="box4-header" >
			<Strong><@s.text name="cp.configExecType" /></Strong>
		</div>
		<div class="box4-content">
			<span class="bg_title">${application.CodeTable.getVal("1166","${(execType)!?html}")}</span>
			<span class="gray"><@s.text name="cp.configExecTypeDep" /></span>
		</div>
	</div>
	<div class="box4" >
		<div class="box4-header" >
			<Strong><@s.text name="cp.pointLimit" /></Strong>
		</div>
		<div class="box4-content">
		<#if (pointLimitInfo.oneLimit)! == "1">
			<#if (pointLimitInfo.onePoint)! == "2">
				<p>
					<label><@s.text name="cp.oneUsePoint" /></label>
					<label style="color:red;">${(pointLimitInfo.maxPoint)!?html}</label>
					<label><@s.text name="cp.passPoint1" /></label>
				</p>
			<#elseif (pointLimitInfo.onePoint)! == "3">
				<p>
					<label><@s.text name="cp.oneUsePoint" /></label>
					<label style="color:red;">${(pointLimitInfo.maxGivePoint)!?html}</label>
					<label><@s.text name="cp.passMoneyGive" /></label>
					<label style="color:red;">${(pointLimitInfo.mulGive)!?html}</label>
					<label><@s.text name="cp.mulComPoint" /></label>
				</p>
			</#if>
		</#if>
		<#if (pointLimitInfo.allLimit)! == "1">
			<#if (pointLimitInfo.allPoint)! == "2">
				<p>
					<label><@s.text name="cp.allUsePoint" /></label>
					<label style="color:red;">${(pointLimitInfo.allLimitPoint)!?html}</label>
					<label><@s.text name="cp.passPoint1" /></label>
				</p>
			<#elseif (pointLimitInfo.allPoint)! == "3">
				<p>
					<label><@s.text name="cp.allUsePoint" /></label>
					<label style="color:red;">${(pointLimitInfo.maxAllGivePoint)!?html}</label>
					<label><@s.text name="cp.passMoneyGive" /></label>
					<label style="color:red;">${(pointLimitInfo.mulAllGive)!?html}</label>
					<label><@s.text name="cp.mulComPoint" /></label>
				</p>
			</#if>
		</#if>
		</div>
	</div>
	<div class="box4" >
		<div class="box4-header" >
			<Strong><@s.text name="cp.confPayTypeTxt" /></Strong>
		</div>
		<div class="box4-content">
		<p>	
			<#if "" == (payTypeKbn)! || (payTypeKbn)! == "0"><@s.text name="cp.confPayTypePtTxt" />
			<#else>
				<span><@s.text name="cp.confPayTypeNoPtTxt" /> </span>
				
			</#if>
		</p>
		<#if (payTypeKbn)! == "1">
		<p>
			<#list payTypeList as code>
				 	<#if (code.ckFlag)! == "1"><span class="bg_title" style="margin-top:20px;">${code.Value!}</span></#if> 
				</#list>
		</p>
		</#if>
		</div>
	</div>
		<div class="box4" >
	<div class="box4-header" >
		<Strong><@s.text name="cp.otherSetting" /></Strong>
	</div>
		<div class="box4-content">
		<p>
			<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.zkPrtTxt" /></span>
			<span style="margin-left:20px;">
			<#if "1" == (zkPrt)!> <@s.text name="cp.zkPrtNopt" />  <#else> <@s.text name="cp.zkPrtpt" /> </#if>
		  	</p>
		</div>
	</div>
	<div class="center clearfix" id="pageConfig">
        <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
             <span class="button-text"><@s.text name="cp.close" /></span>
         </button>
     </div>
           </div>
              </div>
           </div>
          </div>
           <div id="tabs-2" class="ui-tabs-panel">
            <div class="section">
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
             	<@s.text name="cp.comRule" /></strong></div>
              
		
		<div class="section-content" >
		<span class="bg_yew"><span class="highlight" style="line-height:25px;"><@s.text name="cp.affilExplain" /></span></span>
			<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
            <thead>
              <tr>    
                <th width="5%"><@s.text name="cp.hanghao" /></th>       
                <th width="30%"><@s.text name="cp.configMainRule" /></th>
                <th width="20%"><@s.text name="cp.startDateTxt" /></th>
                <th width="20%"><@s.text name="cp.endDateTxt" /></th>
                <th width="25%"><@s.text name="cp.extraRule" /></th>
              </tr>
            </thead>
            <tbody id="dataTable">
            	<#if (generalRuleList?? && generalRuleList?size>0) >
            	<#list generalRuleList as generalRule>
            		<tr id="${generalRule_index}">
            			<td>${generalRule_index + 1}</td>
            			<td>
            				<span id="campaignName" >${(generalRule.campaignName)!}</span>
            			</td>
					    <td>${(generalRule.campaignFromDate)!}</td>
					    <td>${(generalRule.campaignToDate)!}</td>
            			<td id="extra_${generalRule_index}">
							<#if (generalRule.extraRuleList?? && generalRule.extraRuleList?size>0) >
							<#list generalRule.extraRuleList as extraRule>
								<span><br>${(extraRule.campaignName)!}</span>
							</#list>
							<#else>
								<@s.text name="cp.noAffilRule" />
							</#if>
            			</td>
            		</tr>
            	</#list>
            	</#if>
            </tbody>
	</table>
              </div>
            </div>
          </div>
         </div>
            </div>
          </div>
          </div>
</@s.i18n>