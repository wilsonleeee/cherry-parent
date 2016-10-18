<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/mb/clb/BINOLMBCLB01.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM14_15.js"></script>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<@s.url id="search_url" action="BINOLMBCLB01_search"/>
<@s.url id="add_url" action="BINOLMBCLB02_init"/>
<@s.url id="clubValid_url" action="BINOLMBCLB01_clubValid"/>
<@s.url id="sendClub_url" action="BINOLMBCLB01_sendClub"/>
<@s.text id="selectAll" name="global.page.all"/>
<@s.url id="s_indSearchPrmCounterUrl" value="/ss/BINOLSSPRM15_indSearchPrmCounter" />
<span id ="indSearchPrmCounterUrl" style="display:none">${s_indSearchPrmCounterUrl}</span>
<div class="hide">
     <a id="searchUrl" href="${search_url}"></a>
     <a id="clubValidUrl" href="${clubValid_url}"></a>
</div>
<div class="panel-header">
    <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><a href="#"><@s.text name="cp.ruleConfig" /></a> &gt; <a href="#"><@s.text name="cp.ruleConfigView" /></a> </span> </div>
 </div>
 <div id="actionResultDisplay"></div>
 <div class="panel-content">
 		<div style="margin-bottom:5px;height:25px;">
 				<a href="${sendClub_url}" onclick="javascript:BINOLMBCLB01.sendClub(this);return false;" class="add right"><span class="ui-icon icon-add"></span>
				 <span class="button-text"><@s.text name="cp.sendmemclub" /></span></a>
 				<a href="${add_url}" onclick="javascript:BINOLMBCLB01.openWinConfig(this);return false;" class="add right"><span class="ui-icon icon-add"></span>
				 <span class="button-text"><@s.text name="cp.addmemclub" /> </span></a>
				
				 
 		</div>
 		 <div class="box">
          <@cherry.form id="mainForm" class="inline">
         <div class="box-header">
            	<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="cp.findCondition" /></strong>
	        	
            </div>
            <div class="box-content clearfix">
          <div class="column" style="width:50%;height:80px;">
            <p class="clearfix">
              <label><@s.text name="cp.brandName" /></label>
             	<#if (brandInfoList?? && brandInfoList?size > 0) >
         			<@s.select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"/>
         		</#if>
            </p>
             <p class="date">
              		<label><@s.text name="cp.clubValidFlag" /></label>
              		<@s.select name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value"
                  	headerKey="" headerValue="%{selectAll}" value="1" />
             </p>
             <p class="date">
              		<label><@s.text name="cp.clubCounter" /></label>
                  	<@s.textfield name="prmCounter" cssClass="text" id="prmCounter"/>
                  	<@s.hidden name="prmCounterId" id="prmCounterId"></@s.hidden>
             </p>
            </div>
            <div class="column last" style="width:49%;height:80px;">  
             <p class="date">
              		<label><@s.text name="cp.clubName" /></label>
              		<span><@s.textfield name="clubName" cssClass = "text"></@s.textfield></span>
             </p>
             <p class="date">
              		<label><@s.text name="cp.clubCd" /></label>
              		<span><@s.textfield name="clubCode" cssClass = "text"></@s.textfield></span>
             </p>
            </div>
            </div>
            
            <p class="clearfix">
	              	<button class="right search"  onclick="BINOLMBCLB01.search();return false;">
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
               <th><@s.text name="cp.clubName" /></th>
               <th><@s.text name="cp.clubCd" /></th>
               <th><@s.text name="cp.clubDesp" /></th>
               <th><@s.text name="cp.clubValidFlag" /></th>
               <th><@s.text name="cp.act" /></th>
            </tr>
           </thead>
        <tbody id="dataTableBody" >
        </tbody>
   		</table>
   		</div>
      </div>
       <div class="hide" id="dialogInitMessage">
       <div class="hide" id="dialogSendClub"></div>
       	<hr class="space" />
		<span class="bg_yew" style="margin-left:180px;"><span class="highlight" style="line-height:25px;"><strong><@s.text name="cp.clubDelWarn" /></strong></span></span>
		<hr class="space" />
		<#-- 无效原因  -->
 		  	<span class="bg_title"><@s.text name="cp.clubDelReason" /></span>
 		  	<span style="margin-left:20px;">
 		  		<textarea  name="reason" style="height:80px;"></textarea>
 		  	</span>
       </div>
       <div class="hide" id="dialogClubValid"></div>
       <div class="hide">
       <span id="cancelTxt"><@s.text name="cp.cancel" /></span>
       <span id="closeTxt"><@s.text name="cp.close" /></span>
       <span id="sureTxt"><@s.text name="cp.sure" /></span>
        <span id="stopClubTxt"><@s.text name="cp.stopClub" /></span>
        <span id="sdClubTxt"><@s.text name="cp.sendmemclub" /></span>
        <span id="clubSendNG03Txt"><@s.text name="cp.ClubSendNG03" /></span>
        <span id="clubSendNG01Txt"><@s.text name="cp.ClubSendNG01" /></span>
        <span id="clubSendNG02Txt"><@s.text name="cp.ClubSendNG02" /></span>
        <span id="clubSendOKTxt"><@s.text name="cp.ClubSendOK" /></span>
       </div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>