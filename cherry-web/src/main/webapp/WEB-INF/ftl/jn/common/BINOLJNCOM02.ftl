<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/jn/common/BINOLJNCOM02.js"></script>
<#-- 活动组添加URL -->
<@s.url id="addUrl" action="BINOLJNCOM03_init" namespace="/jn"/>
<#-- 活动组查询URL -->
<@s.url id="search_url" action="BINOLJNCOM02_search" namespace="/jn"/>
<div class="hide">
    <a id="searchUrl" href="${search_url}"></a>
 </div>
 
 <@s.text id="selectAll" name="global.page.all"/>
<div class="panel-header">
        <div class="clearfix">
        	<span class="breadcrumb left"> 
        		<span class="ui-icon icon-breadcrumb"></span>
        		规则组管理 &gt; 规则组一览
        	</span>
       		<span class="right"> 
       		<#-- 活动组添加按钮 -->
       		 	<a href="${addUrl}" class="add" onclick="javascript:openWin(this);return false;">
       		 		<span class="button-text">添加规则组</span>
       		 		<span class="ui-icon icon-add"></span>
       		 	</a>
       		</span>
       	</div>
      </div>
      <div class="panel-content">
        <div class="box">
          <@cherry.form id="mainForm" class="inline" onsubmit="search(); return false;">
          <div class="box-header">
            	<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition"/></strong>
            </div>
            <div class="box-content clearfix">
               <div class="column" style="width:49%;">
               <p>
               		<label style="width: 80px;">品牌</label>
               		<#if (brandInfoList?? && brandInfoList?size > 1)>
               			<@s.select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"/>
               		</#if>
               		<#if (brandInfoList?? && brandInfoList?size == 1)>
               			<@s.select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" disabled="true"/>
               			<#list brandInfoList as brandInfo>
		             		<input type="hidden" name="brandInfoId" value="${brandInfo.brandInfoId.toString()}"/>
		             	</#list>
               		</#if>
               	</p>
                 <p>
               		<label style="width: 80px;"> 规则类型</label>
               		<@s.select name="campaignType" list='#application.CodeTable.getCodes("1112")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}"/>
               	</p>
              </div>
              <div class="column last" style="width:50%;"> 
	   			<p>
                 <#-- 活动组名称-->
                  <label style="width: 80px;">规则组名称</label>
                  <input type="text" class="text" name="groupName" maxlength="50" />
                 </p>
                 <p>
               		<label style="width: 80px;"> 有效状态</label>
               		<@s.select name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value" headerKey="" headerValue='%{selectAll}' value="1"/>
               	</p>
              </div>
            </div>
            <p class="clearfix">
              
              		<#-- 查询按钮 -->
              		<button class="right search" type="submit" onclick="BINOLJNCOM02.search();return false;">
              			<span class="ui-icon icon-search-big"></span><span class="button-text"><@s.text name="global.page.search"/></span>
              		</button>
            </p>
          </@cherry.form>
        </div>
                <div id="section" class="section hide">
          <div class="section-header">
          	<strong>
          		<span class="ui-icon icon-ttl-section-search-result"></span>
          		<@s.text name="global.page.list"/>
          	</strong>
          </div>
          
          <div class="section-content">
            
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <#-- 编号 -->
                  <th>编号</th>
                  <#-- 促销品全名-->
                  <th>规则组名称<span class="ui-icon ui-icon-document"></span></th>
                  <#-- 促销品厂商编码 -->
                  <th>规则组类型</th>
                  <#-- 有效区分 -->
                  <th>有效区分</th>
                  <#--操作 -->
			      <th>操作</th>
                </tr>
               </thead>           
              </table>
             </div>
            </div>
</div>
 <#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">