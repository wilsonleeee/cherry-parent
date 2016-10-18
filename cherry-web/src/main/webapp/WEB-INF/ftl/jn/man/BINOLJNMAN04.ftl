<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/jn/man/BINOLJNMAN04.js"></script>
<script type="text/javascript" src="/Cherry/js/cp/common/campaignTemplate.js"></script>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<@s.url id="search_url" action="BINOLJNMAN04_search"/>
<@s.url id="checkDefRule_url" action="BINOLJNMAN04_checkDefRule"/>
<@s.url id="changeBrand_url" action="BINOLJNMAN04_changeBrand"/>
<@s.url id="addUrl" action="BINOLCPCOM02_topInit" namespace="/cp"/>
<@s.url id="addCombUrl" action="BINOLJNMAN06_addComb" namespace="/jn"/>
<@s.url id="binolcpcom02Url" action="BINOLCPCOM02_init" namespace="/cp"/>
<@s.url id="binoljncom03Url" action="BINOLJNCOM03_detailInit" namespace="/jn"/>
<@s.url id="editUrl" action="BINOLCPCOM02_editInit" namespace="/cp"/>
<@s.url id="searchClub_url" action="BINOLCM05_queryClub" namespace="/common"/>
<div class="hide" id="dialogInit"></div>
<div class="hide">
     <a id="changeBrandUrl" href="${changeBrand_url}"></a>
     <a id="searchUrl" href="${search_url}"></a>
     <a id="checkDefRuleUrl" href="${checkDefRule_url}"></a>
     <a id="searchClubUrl" href="${searchClub_url}"></a>
</div>
<div class="hide" id="comParams">
	<input type="hidden" name="campaignType" value="3" id="campaignType"/> 
	<input type="hidden" name="campInfo.campaignType" value="${(campaignType)!}" id="campType"/> 
</div>
	 <div class="panel-header">
        <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><a href="#"><@s.text name="cp.pointManage" /></a> > <a href="#"><@s.text name="cp.ruleSetting" /></a> </span> </div>
      </div>
<div id="actionResultDisplay"></div>
      <div class="panel-content">
        <div class="box">
          <@cherry.form id="mainForm" class="inline">
         <div class="box-header">
            	<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="cp.findCondition" /></strong>
	        	<input type="hidden" name="actionKbn" id="actionKbn" value="next"/>
	        	<input type="hidden" name="actionId" id="actionId"/>
	        	<input type="hidden" name="configKbn" id="configKbn" value="${(configKbn)!}"/> 
	        	<input type="hidden" name="campaignType" value="3" id="campaignType"/> 
            </div>
          <div class="box-content clearfix">
          <div class="column" style="width:50%;height:55px;">
            <p class="clearfix">
              <label><@s.text name="cp.brandName" /></label>
             	<#if (brandInfoList?? && brandInfoList?size > 0) >
         			<@s.select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="BINOLJNMAN04.changeBrand()"/>
         		<#else>
          		${(brandName)!?html}
         		<input type="hidden" id="brandSel" name="brandInfoId" value="${(brandInfoId)!}"/>
        		</#if>
            </p>
            <p class="clearfix">
              <label><@s.text name="cp.pointRuleType" /></label>
              <@s.text name='cp.allValue' id="allValue"/>
              <@s.select name="pointRuleType" list='#application.CodeTable.getCodes("1193")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#allValue}"/>
            </p>
            </div>
            <div class="column last" style="width:49%;height:55px;"> 
            <#if (clubList?? && clubList?size > 0) >
             <p class="clearfix">
              <label><@s.text name="cp.memclub" /></label>
              <@s.select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="BINOLJNMAN04.search()"/>
            </p> 
            </#if>
             <p class="date">
              		<label><@s.text name="cp.actTime" /></label>
              		<span><@s.textfield name="fromDate" cssClass="date"></@s.textfield></span> - 
              		<span><@s.textfield name="toDate" cssClass="date"></@s.textfield></span>
             </p>
            </div>
            </div>
            <p class="clearfix">
            	 <@cherry.show domId="JNMAN04FIND">
	              	<button class="right search"  onclick="BINOLJNMAN04.search();return false;">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><@s.text name="cp.find" /></span>
	              	</button>
	              </@cherry.show>
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
	            <div class="toolbar clearfix">
                </span>
	            <span class="right">
	            <@cherry.show domId="JNMAN04ADD">
		            <a href="${addUrl}" class="add left" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '1', '0');return false;" >
		            <span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="cp.addNewRule" /></span></a>
				</@cherry.show>
				<@cherry.show domId="JNMAN04COMB">
				<a href="${addCombUrl}" class="add right" onclick="javascript:BINOLJNMAN04.addComb(this);return false;" >
		            <span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="cp.combRuleAdd" /></span></a>
				</@cherry.show>
				</span></div>
	          </div>
        <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
   		<thead>
            <tr>
               <th><@s.text name="cp.actName" /></th>
               <th><@s.text name="cp.pointRuleType" /></th>
               <th><@s.text name="cp.startTime" /></th>
               <th><@s.text name="cp.endTime" /></th>
               <th><@s.text name="cp.act" /></th>
            </tr>
           </thead>
        <tbody id="dataTableBody" >
        </tbody>
   		</table>
   		</div>
      </div>
       <div class="hide">
	       <span id="title"><@s.text name="cp.title" /></span>
	       <span id="sure"><@s.text name="cp.sure" /></span>
	       <span id="cancel"><@s.text name="cp.cancel" /></span>      
	       <span id="testMes"><p class='message'><span><@s.text name="cp.doSure" /></span></p></span>
      </div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>