<#include "/WEB-INF/ftl/common/head.inc.ftl">
<script type="text/javascript" src="/Cherry/js/jn/man/BINOLJNMAN06.js"></script>
<script type="text/javascript" src="/Cherry/js/cp/common/campaignTemplate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>	
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<@s.url id="comb_url" action="BINOLJNMAN06_comb"/>
<@s.url id="save_url" action="BINOLJNMAN06_save"/>
	<div class="main container clearfix">
 <div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
          <@s.text name="cp.ruleConfig" /> &gt; <@s.text name="cp.ruleConfigAdd" />
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
              <strong><span class="ui-icon icon-ttl-section-info-edit"></span>
              	<@s.text name="cp.configBase" />
              </strong>
              </div>
              <div class="section-content">
              <@c.form id="mainForm" method="post" class="inline" csrftoken="false">
              <input type="hidden" name="combInfo" id="combInfo"/>
              <table id="baseInfo" class="detail" cellpadding="0" cellspacing="0">
                <tr>
                	<th>
                       <label>
                       	<@s.text name="cp.brandName" />
                       </label>
                     </th>
                     <td>
                       <span>
                         <@s.select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"  tabindex="1" disabled="true"/>
                         <input type="hidden" name="brandInfoId" value="${(brandInfoId)!}" id="brandInfoId"/>
                       </span>
                     </td>
                    <#if (clubList?? && clubList?size > 0) >
				         	<th><@s.text name="cp.memclub" /></th>
	             		  	<td>
	             		  		<select disabled="true">
		            				<#list clubList as clubInfo>
					            	<option <#if (memberClubId)! == clubInfo.memberClubId.toString() >selected="selected"</#if>  value="${(clubInfo.memberClubId)!}" >
					            	${(clubInfo.clubName)!}
					            	</option>
					            	</#list>
					            	</select>
		            			<input type="hidden" name="memberClubId" value="${(memberClubId)!}" id="memberClubId"/>
					        </td>
                     <#else>
                  <th>
                  		
                        <label><@s.text name="cp.ruleConfigName" /></label>
                      </th>
                      <td>
                        <span>
                        <@s.textfield name="groupName" cssClass = "text" maxlength="50"></@s.textfield>
                        </span>
                      </td>
                      </#if>
                </tr>
                <tr>
                	<th>
                       <label>
                       	<@s.text name="cp.configDep" />
                       </label>
                     </th>
                     <td <#if !(clubList?? && clubList?size > 0) > colspan="3" style="width:80%;" </#if>>
                       <span style="width:100%;">
                         <textarea cols="" rows="" name="descriptionDtl" style="width:90%;height:40px;"></textarea>
                       </span>
                     </td>
                     <#if (clubList?? && clubList?size > 0) >
                     	<th>
                  		
                        <label><@s.text name="cp.ruleConfigName" /></label>
                      </th>
                      <td>
                        <span>
                        <@s.textfield name="groupName" cssClass = "text" maxlength="50"></@s.textfield>
                        </span>
                      </td>
                      </#if>
                </tr>
                </table>
              </div>
              </@c.form>
              <div class="section-header">
              <strong><span class="ui-icon icon-ttl-section-info-edit"></span>
              	<@s.text name="cp.configRuleStart" />
              </strong>
              </div>
              <div class="section-content">
              
				<div id="prioContent">
		
		<div class="box4" >
		<div class="box4-header" >
			<Strong><@s.text name="cp.hasConfigRule" /></Strong>
		</div>
		<div class="box4-content" >
		<div class="toolbar clearfix">
           	<span class="left">
                <a onclick="javascript:BINOLJNMAN06.ruleStart('0');return false;" class="delete" href="javascript:void(0);">
                   <span class="ui-icon icon-disable"></span>
                   <span class="button-text"><@s.text name="cp.disable" /></span>
                </a>
            </span>
          </div>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="proTable">
		<tbody id="rulePriorityTable">
            <tr class="thClass">
               <th width="5%"><input type="checkbox" name="allCheckRule" value="1" onclick="BINOLJNMAN06.selAllRules(this)"/></th>
               <th width="5%"><@s.text name="cp.hanghao" /></th>
               <th width="20%"><@s.text name="cp.ruleName" /></th>
               <th width="10%"><@s.text name="cp.startDateTxt" /></th>
               <th width="10%"><@s.text name="cp.endDateTxt" /></th>
               <th width="10%"><@s.text name="cp.configRuledef" /></th>
               <th width="20%"><@s.text name="cp.priority" /></th>
               <th width="20%"><@s.text name="cp.configRuleMatch" /></th>
            </tr>
		</tbody>
		</table>
		</div>
		<#if (deftRuleInfo??) >
		<div class="box4-content" style="padding-top:0;">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tbody id="deftRuleTable">
			<tr style="background:#e7e7e7;"><td width="5%"></td>
			<td width="5%">－</td>
			<td id="prio_${(deftRuleInfo.campaignId)!}" width="20%">
				${(deftRuleInfo.campaignName)!} <Strong><@s.text name="cp.deftRule" /></Strong>
				<input type="hidden" name="campaignId" value="${(deftRuleInfo.campaignId)!}"/>
				<div class="extraRulePanel hide">
					<span style="display:block;margin-left:15px;"><label class="gray"><@s.text name="cp.affilRuleTxt" /></label></span>
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
		<div class="toolbar clearfix">
           	<span class="left">
                <a onclick="javascript:BINOLJNMAN06.ruleStart('1');return false;" class="add" href="javascript:void(0);">
                   <span class="ui-icon icon-enable"></span>
                   <span class="button-text"><@s.text name="cp.configStart" /></span>
                </a>
            </span>
          </div>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="proTable">
		<tbody id="rulePriorityDotConTable">
            <tr class="thClass">
               <th width="5%"><input type="checkbox" name="allCheckRule" value="1" onclick="BINOLJNMAN06.selAllRules(this)"/></th>
               <th width="5%"><@s.text name="cp.hanghao" /></th>
               <th width="20%"><@s.text name="cp.ruleName" /></th>
               <th width="10%"><@s.text name="cp.startDateTxt" /></th>
               <th width="10%"><@s.text name="cp.endDateTxt" /></th>
               <th width="10%"><@s.text name="cp.configRuledef" /></th>
               <th width="20%"><@s.text name="cp.priority" /></th>
               <th width="20%"><@s.text name="cp.configRuleMatch" /></th>
            </tr>
		<#if (unusedList?? && unusedList?size>0) >
			<#list unusedList as unusedRule>
				<tr>
				<td><input type="checkbox" name="checkRule" value="1" onclick="BINOLJNMAN06.selOneRule(this)"/></td>
				<td>${(unusedRule_index  + 1)!}</td>
				<td id="prio_${(unusedRule.campaignId)!}">
					${(unusedRule.campaignName)!}
					<input type="hidden" name="campaignId" value="${(unusedRule.campaignId)!}"/>
					<#if (unusedRule.combType??) >
						<input type="hidden" name="combType" value="${(unusedRule.combType)!}"/>
						<input type="hidden" name="geneRules" value="${(unusedRule.geneRulesStr)!?html}"/>
						<input type="hidden" name="matchType" value="${(unusedRule.matchType)!}"/>
					</#if>
					<div class="extraRulePanel hide">
						<span style="display:block;margin-left:15px;"><label class="gray"><@s.text name="cp.affilRuleTxt" /></label></span>
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
			<@s.select list='#application.CodeTable.getCodes("1166")' listKey="CodeKey" listValue="Value"  name="execType" id="execType"/>
			<label class="gray"><@s.text name="cp.configExecTypeDep" /></label>
		</div>
	</div>
	<div class="box4" >
		<div class="box4-header" >
			<Strong><@s.text name="cp.pointLimit" /></Strong>
		</div>
		<div class="box4-content" id="limitContent">
			<p style="margin-left:20px;" >
						<span class="bg_title"><input type="checkbox" id="oneLimit" name="oneLimit" onclick="BINOLJNMAN06.showLimit(this);" value="1" />
						<label for="oneLimit" style="cursor:pointer;"><@s.text name="cp.oneLimit" /></label></span>
					</p>
					<div id="oneLimitDiv" class="hide">
					<p style="margin-left:40px">
						<input type="radio"  class="radio"  value="2" checked="checked" id="oneLimitMark" name="onePoint"/>
						<label for="oneLimitMark"><@s.text name="cp.giveMonPass" /></label>
						<span>
							<input type="text" class="number" name="maxPoint" />
						</span>
						<span><label><@s.text name="cp.passPoint1" /></label>
						</span>
					</p>
					<p style="margin-left:40px">
						<input type="radio"  class="radio"  value="3" id="oneLimitGiveMark" name="onePoint"/>
						<label for="oneLimitGiveMark"><@s.text name="cp.giveMonPass" /></label>
						<span>
							<input type="text" class="number" name="maxGivePoint" />
							</span><span><label><@s.text name="cp.passMoneyGive" /></label>
						</span>
						<span>
							<input type="text" class="number" name="mulGive" />
							</span><span><label><@s.text name="cp.mulPoint" /></label>
						</span>
					</p>
					</div>
					<div class="line_Yellow">
					</div>
					<p style="margin-left:20px;">
						<span class="bg_title"><input type="checkbox" id="allLimit" name="allLimit" onclick="BINOLJNMAN06.showLimit(this);" value="1" />
						<label for="allLimit" style="cursor:pointer;"><@s.text name="cp.dayLimit" /></label></span>
					</p>
					<div id="allLimitDiv" class="hide">
					<p style="margin-left:40px">
						<input type="radio" class="radio" value="2" id="allLimitMark" name="allPoint" checked="checked"/>
						<label for="allLimitMark"><@s.text name="cp.giveMonPass" /></label>
						<span>
							<input type="text" class="number" name="allLimitPoint" />
							</span><span><label><@s.text name="cp.passPoint1" /></label>
						</span>
					</p>
					<p style="margin-left:40px">
						<input type="radio"  class="radio"  value="3" id="allLimitGiveMark" name="allPoint"/>
						<label for="allLimitGiveMark"><@s.text name="cp.giveMonPass" /></label>
						<span>
							<input type="text" class="number" name="maxAllGivePoint" />
							</span><span><label><@s.text name="cp.passMoneyGive" /></label>
						</span>
						<span>
							<input type="text" class="number" name="mulAllGive" />
							</span><span><label><@s.text name="cp.mulPoint" /></label>
						</span>
					</p>
					</div>
		</div>
	</div>
	<div class="box4" >
		<div class="box4-header" >
			<Strong><@s.text name="cp.confPayTypeTxt" /></Strong>
		</div>
		<div class="box4-content">
		<p style="margin-left:20px;">
				<input type="radio"  class="radio"  value="0" id="payTypeKbn0" name="payTypeKbn" checked="checked"/>
				<label for="payTypeKbn0" style="cursor:pointer;"><@s.text name="cp.confPayTypePtTxt" /></label>
		</p>
		<p style="margin-left:20px;" >
			<input type="radio"  class="radio"  value="1" id="payTypeKbn1" name="payTypeKbn"/>
			<label for="payTypeKbn1" style="cursor:pointer;"><@s.text name="cp.confPayTypeSelTxt" /></label>
			<span><input type="checkbox" name="payTypeCodeALL" onclick="BINOLJNMAN06.selPayType(this);" style="margin-left:15px;" id="payTypeCode_ALL"/><label for="payTypeCode_ALL" style="cursor:pointer;"><@s.text name="cp.confPayTypeSelAll" /></label></span>
		</p>
		<p style="margin-left:40px" id="payTypeMul">
		
			<#list application.CodeTable.getCodes("1175") as code>
				<input type="checkbox" name="payTypeCodeType" onclick="BINOLJNMAN06.selPayType(this);" style="margin-left:20px;" id="payTypeCode_${code_index}"/><label for="payTypeCode_${code_index}" style="cursor:pointer;">${code.Value!}</label>
				<input id="payTypeCode_${code_index}_key" type="hidden" name="payTypeCode" value="${code.CodeKey!}"/>
			</#list>
		</p>
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
			<input type="radio"  class="radio"  value="0" id="zkPrt0" name="zkPrt" checked="checked"/>
				<label for="zkPrt0" style="cursor:pointer;"><@s.text name="cp.zkPrtpt" /></label>
			<input type="radio"  class="radio"  value="1" id="zkPrt1" name="zkPrt"/>
				<label for="zkPrt1" style="cursor:pointer;"><@s.text name="cp.zkPrtNopt" /></label>
					</span>
		  	</p>
		</div>
	</div>
	<div class="center clearfix" id="pageConfig">
		<button class="save" type="submit" onclick="BINOLJNMAN06.saveConfig();return false;">
           <span class="ui-icon icon-save"></span>
           <span class="button-text"><@s.text name="cp.save" /></span>
        </button>
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
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info-edit"></span>
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
            				<input id="general_${generalRule_index}" type="hidden" name="campaignId" value="${(generalRule.campaignId)!}"/>
            			</td>
					    <td>${(generalRule.campaignFromDate)!}</td>
					    <td>${(generalRule.campaignToDate)!}</td>
            			<td id="extra_${generalRule_index}">
		            		<a class="add right" onClick="BINOLJNMAN06.popExtraRule(this);return false;">
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><@s.text name="cp.add" /></span>
							</a>
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
		<div class="hide">
			<div id="afterMatchCont"><@s.select name="afterMatch" list='#application.CodeTable.getCodes("1191")' listKey="CodeKey" listValue="Value"/></div>
			<a id="combUrl" href="${comb_url}"></a>
			<a id="saveUrl" href="${save_url}"></a>
			<span id="rulediaTitleTxt"><@s.text name="cp.rulediaTitle" /></span>
		</div>
		<div class="hide" id="dialogInit"></div>
		<#include "/WEB-INF/ftl/common/popCampRuleTable.ftl">
</@s.i18n>