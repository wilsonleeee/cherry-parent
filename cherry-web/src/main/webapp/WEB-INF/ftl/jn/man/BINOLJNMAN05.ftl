<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/jn/man/BINOLJNMAN05.js"></script>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<@s.url id="search_url" action="BINOLJNMAN05_search"/>
<@s.url id="add_url" action="BINOLJNMAN06_init"/>
<@s.url id="confValid_url" action="BINOLJNMAN05_confValid"/>
<@s.url id="confCheck_url" action="BINOLJNMAN05_confCheck"/>
<@s.url id="searchClub_url" action="BINOLCM05_queryClub" namespace="/common"/>
<div class="hide">
     <a id="searchUrl" href="${search_url}"></a>
     <a id="confValidUrl" href="${confValid_url}"></a>
     <a id="confCheckUrl" href="${confCheck_url}"></a>
     <a id="searchClubUrl" href="${searchClub_url}"></a>
</div>
<div class="panel-header">
    <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><a href="#"><@s.text name="cp.ruleConfig" /></a> &gt; <a href="#"><@s.text name="cp.ruleConfigView" /></a> </span> </div>
 </div>
 <div id="actionResultDisplay"></div>
 <div class="panel-content">
 		<div style="margin-bottom:5px;height:25px;">
 				<@cherry.show domId="JNMAN05ADD">
 				<a href="${add_url}" onclick="javascript:BINOLJNMAN05.openWinConfig(this);return false;" class="add right"><span class="ui-icon icon-add"></span>
				 <span class="button-text"><@s.text name="cp.ruleConfigAdd" /></span></a>
				</@cherry.show>
				 
 		</div>
 		 <div class="box">
          <@cherry.form id="mainForm" class="inline">
         <div class="box-header">
            	<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="cp.findCondition" /></strong>
	        	
            </div>
            <div class="box-content clearfix">
          <div class="column" style="width:50%;height:55px;">
            <p class="clearfix">
              <label><@s.text name="cp.brandName" /></label>
             	<#if (brandInfoList?? && brandInfoList?size > 0) >
         			<@s.select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="BINOLJNMAN05.changeBrand()"/>
         		<#else>
          		${(brandName)!?html}
         		<input type="hidden" id="brandSel" name="brandInfoId" value="${(brandInfoId)!}"/>
         		</#if>
            </p>
            <#if (clubList?? && clubList?size > 0) >
             <p class="clearfix">
              <label>会员俱乐部</label>
             	<span><@s.select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="BINOLJNMAN05.search()"/></span>
          		
            </p>
            </#if>
            </div>
            <div class="column last" style="width:49%;height:55px;">  
             <p class="date">
              		<label><@s.text name="cp.ruleConfigName" /></label>
              		<span><@s.textfield name="groupName" cssClass = "text"></@s.textfield></span>
             </p>
            </div>
            </div>
            <p class="clearfix">
	              	<button class="right search"  onclick="BINOLJNMAN05.search();return false;">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><@s.text name="cp.find" /></span>
	              	</button>
	            </p>
          </@cherry.form>
        </div>
        
        <div id="section" class="section">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<@s.text name="cp.showFindRes" />
	         </strong>
	         </div>
	         
	         <div class="section-content" id="result_list">
	         	<span class="bg_yew"><span class="highlight" style="line-height:25px;"><@s.text name="cp.configExplain" /></span></span>
	          </div>
<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
   		<thead>
            <tr>
               <th><@s.text name="cp.hanghao" /></th>
               <th><@s.text name="cp.ruleConfigName" /></th>
               <th><@s.text name="cp.configDep" /></th>
               <th><@s.text name="cp.configStatus" /></th>
               <th><@s.text name="cp.act" /></th>
            </tr>
           </thead>
        <tbody id="dataTableBody" >
        </tbody>
   		</table>
   		</div>
      </div>
       <div class="hide" id="dialogConfig"></div>
       <div class="hide">
       <span id="cancelTxt"><@s.text name="cp.cancel" /></span>
       <span id="useConfigTxt"><@s.text name="cp.useConfig" /></span>
       <span id="closeTxt"><@s.text name="cp.close" /></span>
       <span id="stopConfFirstTxt"><@s.text name="cp.stopConfFirst" /></span>
       <span id="sureTxt"><@s.text name="cp.sure" /></span>
       <span id="useConfSureTxt"><@s.text name="cp.useConfSure" /></span>
       <span id="unuseConfigTxt"><@s.text name="cp.unuseConfig" /></span>
       <span id="unuseConfSureTxt"><@s.text name="cp.unuseConfSure" /></span>
       </div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>