<#include "/WEB-INF/ftl/common/head.inc.ftl">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/cp/common/BINOLCPCOM01.js"></script>
<script type="text/javascript" src="/Cherry/js/cp/common/campaignTemplate.js"></script>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<div class="main container clearfix">
    <div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span>
        <a href="#"><@s.text name="cp.memberAct" /></a> > <a href="#"><@s.text name="cp.baseInfo" /></a> </span> </div>
      </div>
            <div class="panel-content">
            <div class="section">
			  <div class="section-content">
	            <@c.form id="toNextForm" action="BINOLCPCOM02_init" method="post" csrftoken="false" class="block">
	        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/> 
	        	<input type="hidden" name="actionKbn" id="actionKbn" value="fromTop"/>
	        	<input type="hidden" name="actionId" id="actionId"/>
	        	<input type="hidden" name="saveStep" id="saveStep" value="${(saveStep)!?c}"/>
	        	<#-- 会员活动保存信息 -->   
	        	<input type="hidden" name="campSaveInfo" id="campSaveInfo" value="${(campSaveInfo)!?html}"/>
	        	<input type="hidden" name="optKbn" id="optKbn" value="${(optKbn)!}"/> 
	        	<#-- 保存其他有用信息 -->
	        	<input type="hidden" name="extraInfo" id="extraInfo" value="${(extraInfo)!?html}"/>
        		<input type="hidden" name="copyFlag" id="copyFlag" value="${(copyFlag)!}"/> 
        		<#-- 规则类型 -->
        		<input type="hidden" name="campInfo.pointRuleType" id="pointRuleType" value="${(campInfo.pointRuleType)!?html}"/>
        		<#-- 积分类型 -->
		        <input type="hidden" name="campInfo.templateType" id="templateType" value="${(campInfo.templateType)!?html}"/>
	        	<input type="hidden" name="campParamInfo" id="campParamInfo" value="${(campParamInfo)!?html}"/> 
			    <table cellspacing="0" cellpadding="0" class="detail">
			  	  <tbody>
			  	  <tr>
			  	  	  <th><@s.text name="cp.mainActName" /><span class="highlight"><@s.text name="global.page.required"/></span></th>
			  	  	  <td><span><input class="text" type="text" name="campInfo.campaignName" value="${(campInfo.campaignName)!?html}" /></span></td>
			  	  	  <th><@s.text name="cp.brand" /><span class="highlight"><@s.text name="global.page.required"/></span></th>
			  	  	  <td> 
			  	  	  	<#if optKbn == 0>
	            			<#if (brandInfoList?? && brandInfoList?size > 0)>
	            				<select name="brandInfoId">
	            				<#list brandInfoList as brandInfo>
	            				<option <#if (campInfo.brandInfoId)! == brandInfo.brandInfoId.toString() >selected="selected"</#if>  value="${brandInfo.brandInfoId.toString()}" >
	            				${(brandInfo.brandName)!?html}
	            				</option>
	            				</#list>
	            				</select>
	             			</#if>
	             			<#elseif optKbn == 1 || optKbn == 2>
	             				<#if (brandInfoList?? && brandInfoList?size > 0)>
		            				<select disabled="true">
		            				<#list brandInfoList as brandInfo>
					            	<option <#if (brandInfoId)! == brandInfo.brandInfoId.toString() >selected="selected"</#if>  value="${brandInfo.brandInfoId.toString()}" >
					            	${(brandInfo.brandName)!?html}
					            	</option>
					            	</#list>
					            	</select>
		            				<input type="hidden" name="brandInfoId" value="${(brandInfoId)!}"/>
		            			</#if>
             			</#if>
             		  </td></tr>
             		  <tr>
             		  	<th><@s.text name="cp.actDate" /><span class="highlight"><@s.text name="global.page.required"/></span></th>
             		  	<td>
	             		 	<span><input class="date" id="campFromDate" name="campInfo.campaignFromDate" value="${campInfo.campaignFromDate!?html}"/></span>
					  		<span style="margin: 3px 5px;">~</span>
					  		<span><input class="date" id="campToDate" name="campInfo.campaignToDate" value="${campInfo.campaignToDate!?html}"/></span> 	
				        </td>
				        <th><@s.text name="cp.actType" /><span class="highlight"><@s.text name="global.page.required"/></span></th>
             		  	<td>
             		  		<#if optKbn == 0>
				            	<@s.select name="campInfo.campaignType" list='#application.CodeTable.getCodes("1112")' listKey="CodeKey" listValue="Value" value="${(campInfo.campaignType)!?html}"/>
				            <#elseif optKbn == 1 || optKbn == 2>
				            	<@s.select list='#application.CodeTable.getCodes("1112")' listKey="CodeKey" listValue="Value" value="${(campaignType)!?html}" disabled="true" />
				            	<input type="hidden" name="campInfo.campaignType" value="${(campaignType)!?html}"/>
				            </#if>
				        </td>
				     </tr>
				     <tr>
             		  	<th><@s.text name="cp.creater" /></th>
             		  	<td  <#if !(clubList?? && clubList?size > 0) > colspan="3" </#if>>
             		  		<span><input type="text" class="text disabled" name="campInfo.campaignSetBy"  value="${(campInfo.campaignSetByName)!?html}" disabled="disabled" /></span>
				            <input type="hidden" name="campInfo.campaignSetBy" value="${(campInfo.campaignSetBy)!?c}"/>
				            <input type="hidden" name="campInfo.campaignSetByName" value="${(campInfo.campaignSetByName)!?html}"/>
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
		            			<input type="hidden" name="memberClubId" value="${(memberClubId)!}"/>
					        </td>
				         </#if>
				      </tr>
				      <tr>
             		  	<th><@s.text name="cp.actDes" /></th>
             		  	<td colspan="3">
             		  		<span style="width:95%;">
             		  			<textarea cols="" rows="" name="campInfo.descriptionDtl" style="width: 95%;">${(campInfo.descriptionDtl)!?html}</textarea>
             		  		</span>
             		  	</td>
             		  </tr>
			    </tbody></table>
		         </@c.form>      
			  </div>
			</div>
        <hr class="space" />
        <div class="center clearfix">
          <button class="close" onclick="window.close();return false;">
        	<span class="ui-icon icon-close"></span>
            <span class="button-text"><@s.text name="cp.cancel" /></span>
         </button>
          <button class="back" onclick="BINOLCPCOM01.doNext('${(nextAction)!}');return false;">
          <span class="ui-icon icon-mover"></span><span class="button-text"><@s.text name="cp.next" /></span></button>
        </div>
      </div>
    </div>
  </div>
  </@s.i18n>
