<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/jn/man/BINOLJNMAN15.js"></script>
<script type="text/javascript" src="/Cherry/js/cp/common/campaignTemplate.js"></script>
<@s.i18n name="i18n.jn.BINOLJNMAN15">
<@s.url id="search_url" action="BINOLJNMAN15_search"/>
<@s.url id="ruleValid_url" action="BINOLJNMAN15_ruleValid"/>
<@s.url id="addUrl" action="BINOLCPCOM02_topInit" namespace="/cp"/>
<@s.url id="searchClub_url" action="BINOLCM05_queryClub" namespace="/common"/>
<div class="hide">
     <a id="searchUrl" href="${search_url}"></a>
     <a id="ruleValidUrl" href="${ruleValid_url}"></a>
      <a id="searchClubUrl" href="${searchClub_url}"></a>
</div>
<div class="hide" id="comParams">
	<input type="hidden" name="campaignType" value="${(campaignType)!}" id="campaignType"/> 
	<input type="hidden" name="campInfo.campaignType" value="${(campaignType)!}" id="campType"/> 
</div>
<div class="panel-header">
    <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><a href="#"><@s.text name="MAN15_ptclear" /></a> &gt; <a href="#"><@s.text name="MAN15_ptclearView" /></a> </span> </div>
 </div>
 <div id="actionResultDisplay"></div>
 <div class="panel-content">
 		<div style="margin-bottom:5px;height:25px;">
 				<a href="${addUrl}" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '1', '0');return false;" class="add right"><span class="ui-icon icon-add"></span>
				 <span class="button-text"><@s.text name="MAN15_clearRuleAdd" /></span></a>
 		</div>
 		 <div class="box">
          <@cherry.form id="mainForm" class="inline">
         <div class="box-header">
            	<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition" /></strong>
	        	
            </div>
            <div class="box-content clearfix">
          <div class="column" style="width:50%;height:55px;">
            <p class="clearfix">
              <label><@s.text name="MAN15_brandName" /></label>
             	<#if (brandInfoList?? && brandInfoList?size > 0) >
         			<@s.select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="BINOLJNMAN15.changeBrand()"/>
         		<#else>
          		${(brandName)!?html}
         		<input type="hidden" id="brandSel" name="brandInfoId" value="${(brandInfoId)!}" />
         		</#if>
            </p>
            <#if (clubList?? && clubList?size > 0) >
             <p class="clearfix">
              <label><@s.text name="MAN15_memclub" /></label>
             	<span><@s.select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="BINOLJNMAN15.search()"/></span>
          		
            </p>
            </#if>
            </div>
            <div class="column last" style="width:49%;height:55px;">  
             <p class="date">
              		<label><@s.text name="MAN15_ruleName" /></label>
              		<span><@s.textfield name="ruleName" cssClass = "text"></@s.textfield></span>
             </p>
            </div>
            </div>
            <p class="clearfix">
	              	<button class="right search"  onclick="BINOLJNMAN15.search();return false;">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><@s.text name="global.page.search" /></span>
	              	</button>
	            </p>
          </@cherry.form>
        </div>
        
        <div id="section" class="section">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<@s.text name="global.page.list" />
	         </strong>
	         </div>
	         <div class="section-content" id="result_list">
	          </div>
<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
   		<thead>
            <tr>
               <th><@s.text name="MAN15_hanghao" /></th>
               <th><@s.text name="MAN15_ruleName" /></th>
               <th><@s.text name="MAN15_fromDate" /></th>
               <th><@s.text name="MAN15_toDate" /></th>
               <th><@s.text name="MAN15_ruleDep" /></th>
               <th><@s.text name="MAN15_ruleStatus" /></th>
               <th><@s.text name="MAN15_act" /></th>
            </tr>
           </thead>
        <tbody id="dataTableBody" >
        </tbody>
   		</table>
   		</div>
      </div>
       <div class="hide" id="dialogClear"></div>
       <div class="hide">
       <span id="cancelTxt"><@s.text name="global.page.cancle" /></span>
       <span id="sureTxt"><@s.text name="global.page.dialogConfirm" /></span>
       <span id="onRuleTitle"><@s.text name="MAN15_onRule" /></span>
       <span id="offRuleTitle"><@s.text name="MAN15_offRule" /></span>
       <span id="onRuleDesp"><@s.text name="MAN15_onRuleDesp" /></span>
       <span id="offRuleDesp"><@s.text name="MAN15_offRuleDesp" /></span>
       </div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>