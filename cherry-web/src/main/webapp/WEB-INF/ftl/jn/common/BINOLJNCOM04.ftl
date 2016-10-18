<#include "/WEB-INF/ftl/common/head.inc.ftl">
<script type="text/javascript" src="/Cherry/js/cp/common/campaignTemplate.js"></script>
<script type="text/javascript" src="/Cherry/js/jn/common/BINOLJNCOM03.js"></script>
<@s.url id="changeTemplate_url" action="BINOLJNCOM03_change"/>
<#-- 保存 -->
<@s.url id="save_url" action="BINOLJNCOM03_save"/>
<#-- 活动查询URL -->
<@s.url id="search_url" action="BINOLJNCOM03_searchCamp" namespace="/jn"/>
<@s.url id="RuleTest_url" value="/cp/BINOLCPCOM04_init"/>
<div class="hide">
    <a id="searchUrl" href="${search_url}"></a>
    <a id="changeTemplateUrl" href="${changeTemplate_url}"></a>
    <a id="RuleTestUrl" href="${RuleTest_url}"></a>
 </div>
	
<div class="main container clearfix" id="div_main">
    <div class="panel ui-corner-all">
    <div class="panel-header">
	   <div class="clearfix">
	    <span class="breadcrumb left"> 
	     <span class="ui-icon icon-breadcrumb"></span>
	     规则组管理 &gt; 规则组编辑
	    </span>
	   </div>
	  </div>
	  <div class="panel-content">
	  <div class="tabs hide">
	  <ul>
            <li><a href="#tabs-1">基本信息</a></li>
            <li><a href="#tabs-2" onclick="BINOLJNCOM03.searchCamp();return false;">活动一览</a></li>
          </ul>
	  <div id="tabs-1">
	  	<div class="section">
	  		<div class="section-header"><strong><span class="ui-icon icon-ttl-section-info-edit"></span>
            <#-- 基本信息 -->
            基本信息</strong></div>
            <div class="section-content">
            	<@c.form id="mainForm" method="post" class="inline" csrftoken="false">
            	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
            	<input type="hidden" name="ruleDetail" id="ruleDetail" value='<@s.property value="campaignGrpInfo.ruleDetail"/>'/>
            	<input type="hidden" name="pageId" value="P00000000009"/>
            	<input type="hidden" name="campaignGrpId" id="campaignGrpId" value='<@s.property value="campaignGrpInfo.campaignGrpId"/>'/>
            	<input type="hidden" name="campaignTypeOld" value='<@s.property value="campaignGrpInfo.campaignType"/>'/>
            	<input type="hidden" name="grpModifyCount" value='<@s.property value="campaignGrpInfo.grpModifyCount"/>'/>
            	<input type="hidden" name="grpUpdateTime" value='<@s.property value="campaignGrpInfo.grpUpdateTime"/>'/>
            	<table id="baseInfo" class="detail" cellpadding="0" cellspacing="0">
                <tr>
                	<th>
                		<#-- 品牌 -->
                       <label>
                       品牌
                       <span class="highlight"><@s.text name="global.page.required"/></span>
                       </label>
                     </th>
                     <td>
                       <span>
                        <@s.select list="brandInfoList" listKey="brandInfoId" listValue="brandName" value="%{brandInfoId}" disabled="true"/>
                        <input type="hidden" id="brandInfoId" name="brandInfoId" value="${(brandInfoId)!?html}" />
                       </span>
                     </td>
                     <th>
                  		<#-- 活动组名称 -->
                        <label>规则组名称
                        <span class="highlight"><@s.text name="global.page.required"/></span>
                        </label>
                      </th>
                      <td>
                        <span>
                        <input type="text" class="text" name="groupName" maxlength="50" value='<@s.property value="campaignGrpInfo.groupName"/>'/>
                        </span>
                      </td>
                    </tr>
                    <tr>
                	<th>
                		<#-- 活动类型 -->
                       <label>
                       活动类型
                       </label>
                     </th>
                     <td>
                       <span>
                         <@s.select list='#application.CodeTable.getCodes("1112")' listKey="CodeKey" listValue="Value" 
                         onchange="BINOLJNCOM03.change();" value="%{campaignGrpInfo.campaignType}" disabled="true"/>
                       </span>
                       <input type="hidden" id="campaignType" name="campaignType" value="${campaignGrpInfo.campaignType}" />      
                     </td>
                    </tr>
                  </table>
            	</@c.form>
            </div>
	  	</div>
	  		
	  		<div class="section" id="grpRules">
            </div>
            <hr class="space" />
        <div class="hide center" id="save-button">
         	<button class="save" type="submit" onclick="BINOLJNCOM03.doRuleTest();">
            	<span class="ui-icon icon-search-big"></span>
            	<#--  规则测试 -->
            	<span class="button-text">规则测试</span>
            </button>
          <button class="save" type="submit" onclick="BINOLJNCOM03.saveGrp('${save_url}');">
            	<span class="ui-icon icon-save"></span>
            	<#--  保存 -->
            	<span class="button-text"><@s.text name="global.page.save"/></span>
            </button>
            <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
              <#-- 关闭 -->
              <span class="button-text"><@s.text name="global.page.close"/></span>
             </button>
        </div>
        </div>
        <div id="tabs-2">
	  	<div class="section">
	  		<div class="section-header"><strong><span class="ui-icon icon-ttl-section-info-edit"></span>
            <#-- 活动一览 -->
           活动一览</strong></div>
            <div class="section-content" id="sectionContent">
            	<table id="campDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <#-- 编号 -->
                  <th>编号</th>
                  <#-- 活动名称-->
                  <th>活动名称<span class="ui-icon ui-icon-document"></span></th>
                  <#-- 有效期开始日 -->
                  <th>有效期开始日</th>
                  <#-- 有效期结束日 -->
                  <th>有效期结束日</th>
                  <#-- 活动状态 -->
                  <th>活动状态</th>
                  <#-- 活动设定者 -->
                  <th>活动设定者</th>
                  <#--操作 -->
			      <th>操作</th>
                </tr>
               </thead>           
              </table>
            </div>
          </div>
         </div>
         </div>
      </div>
            
        </div>
        
        </div>
	  </div>
	 </div>
</div>
 <#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
 <#include "/WEB-INF/ftl/common/popRuleTable.ftl">