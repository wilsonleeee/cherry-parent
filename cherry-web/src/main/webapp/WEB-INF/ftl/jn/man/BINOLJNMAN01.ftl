<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/jn/man/BINOLJNMAN01.js"></script>
<script type="text/javascript" src="/Cherry/js/cp/common/campaignTemplate.js"></script>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<@s.url id="changeCampaign_url" action="BINOLJNMAN01_changeCampaign"/>
<@s.url id="changeBrand_url" action="BINOLJNMAN01_changeBrand"/>
<@s.url id="binolcpcom02Url" action="BINOLCPCOM02_init" namespace="/cp"/>
<@s.url id="binoljncom03Url" action="BINOLJNCOM03_detailInit" namespace="/jn"/>
<@s.url id="searchClub_url" action="BINOLCM05_queryClub" namespace="/common"/>
<div class="hide">
	<a id="changeCampaignUrl" href="${changeCampaign_url}"></a>
     <a id="changeBrandUrl" href="${changeBrand_url}"></a>
     <a id="searchClubUrl" href="${searchClub_url}"></a>
</div>
<div class="hide" id="comParams">
<input type="hidden" name="campaignType" value="${(campaignType)!}" id="campaignType"/> 
<input type="hidden" name="campInfo.campaignType" value="${(campaignType)!}" id="campType"/> 
</div>
	 <div class="panel-header">
        <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><a href="#">${(campaignTypeName)!?html}</a> > <a href="#">${(campaignTypeName)!?html}<@s.text name="cp.rule" /></a> </span> </div>
      </div>
	<div id="actionResultDisplay"></div>
      <div class="panel-content">   
      <div class="section">
              <div class="section-header">
              	<strong><span class="ui-icon icon-ttl-section-info"></span><@s.text name="cp.gaiyao" /></strong>
	    		<@cherry.show domId="JNMAN01CONSHOW${(campaignType)!}">
              	<a href="${binoljncom03Url}" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '1', '0', '1', '1');return false;" class="add right"><span class="ui-icon ui-icon-document"></span>
				 <span class="button-text"><@s.text name="cp.showConfig" /></span></a>
				</@cherry.show>
				<@cherry.show domId="JNMAN01CONEDIT${(campaignType)!}">
				<a href="${binolcpcom02Url}" class="add right" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '1', '0', '1', '1');return false;"><span class="ui-icon icon-edit"></span>
				 <span class="button-text"><@s.text name="cp.editConfig" /></span></a>
				 </@cherry.show>
              </div>
              <div class="section-content">
          		<@cherry.form id="mainForm" class="inline">
	        	<input type="hidden" name="actionKbn" id="actionKbn" value="next"/>
	        	<input type="hidden" name="actionId" id="actionId"/>
	        	<input type="hidden" name="configKbn" id="configKbn" value="${(configKbn)!}"/> 
              <table class="detail">
              	<tbody><tr>
              		<th><@s.text name="cp.brandName" /></th>
              		<td>
              			<#if (brandInfoList?? && brandInfoList?size > 0) >
         				<@s.select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="BINOLJNMAN01.changeBrand()"/>
         				<#else>
          				${(brandName)!?html}
         				<input type="hidden" id="brandSel" name="brandInfoId" value="${(brandInfoId)!}"/>
        				</#if></td>
              		<#if (clubList?? && clubList?size > 0) >
              			<th><@s.text name="cp.memclub" /></th>
              			<td>
         				<@s.select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="BINOLJNMAN01.changeCampaign()"/>
         				</td>
              		</#if>
              	</tr>
				
              </tbody></table>
          		</@cherry.form>
              </div>
            </div>
       	<div class="section">
            <div class="section-header">
            	<strong><span class="ui-icon icon-ttl-section-list"></span>${(campaignTypeName)!?html}<@s.text name="cp.rule" /></strong>
			</div>
          <div class="section-content">
         <div class="clearfix" id="camp-tabs">
        </div>
        </div>
      </div>
      
<div class="hide" id="dialogInit"></div>
     <div class="hide">
       <span id="title"><@s.text name="cp.title" /></span>
       <span id="sure"><@s.text name="cp.sure" /></span>
       <span id="cancel"><@s.text name="cp.cancel" /></span>      
       <span id="testMes"><p class='message'><span><@s.text name="cp.doSure" /></span></p></span>
      </div>
</@s.i18n>