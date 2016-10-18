<#include "/WEB-INF/ftl/common/head.inc.ftl">
<script type="text/javascript" src="/Cherry/js/cp/common/campaignTemplate.js"></script>
<script type="text/javascript" src="/Cherry/js/jn/common/BINOLJNCOM03.js"></script>
<@s.url id="changeTemplate_url" action="BINOLJNCOM03_change"/>
<#-- 保存 -->
<@s.url id="save_url" action="BINOLJNCOM03_save"/>
<div class="hide">
	<a id="changeTemplateUrl" href="${changeTemplate_url}"></a>
</div>
<div class="main container clearfix" id="div_main">
    <div class="panel ui-corner-all">
    <div class="panel-header">
	   <div class="clearfix">
	    <span class="breadcrumb left"> 
	     <span class="ui-icon icon-breadcrumb"></span>
	     规则组管理 &gt; 规则组添加
	    </span>
	   </div>
	  </div>
	  <div class="panel-content">
	  	<div class="section">
	  		<div class="section-header"><strong><span class="ui-icon icon-ttl-section-info-edit"></span>
            <#-- 基本信息 -->
            基本信息</strong></div>
            <div class="section-content">
            	<@c.form id="mainForm" method="post" class="inline" csrftoken="false">
            	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
            	<input type="hidden" name="ruleDetail" id="ruleDetail"/>
            	<input type="hidden" name="pageId" value="P00000000009"/>
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
                         <#if (brandInfoList?? && brandInfoList?size > 0)>
				            <select name="brandInfoId" <#if (brandInfoList?? && brandInfoList?size == 1)> disabled="true" </#if> >
				            <#list brandInfoList as brandInfo>
				            	<option <#if (brandInfoId)! == brandInfo.brandInfoId.toString() >selected="selected"</#if>  value="${brandInfo.brandInfoId.toString()}" >
				            	${(brandInfo.brandName)!?html}
				            	</option>
				            </#list>
				            </select>
				            <#if (brandInfoList?? && brandInfoList?size == 1)>
				            	<#list brandInfoList as brandInfo>
				             		<input type="hidden" name="brandInfoId" value="${brandInfo.brandInfoId.toString()}"/>
				             	</#list>
				            </#if>
	             		</#if>
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
                        <input type="text" class="text" name="groupName" maxlength="50" />
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
                         <@s.select name="campaignType" list='#application.CodeTable.getCodes("1112")' listKey="CodeKey" listValue="Value" onchange="BINOLJNCOM03.change();"/>              
                       </span>
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
            
        </div>
        
        </div>
	  </div>
	 </div>
</div>
 <#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
 <#include "/WEB-INF/ftl/common/popRuleTable.ftl">