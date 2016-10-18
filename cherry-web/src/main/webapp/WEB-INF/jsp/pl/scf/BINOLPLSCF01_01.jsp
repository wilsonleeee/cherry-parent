<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.pl.BINOLPLSCF01">
<s:if test="%{systemConfigList != null}">
<!-- ========系统配置项帮助信息弹出框START ========-->
<div class="hide " id="configHelpDialogInit"></div>
<!-- =======系统配置项帮助信息弹出框END ===========-->
<s:url action="BINOLPLSCF01_saveBsCf" id="saveBsCfUrl"></s:url>
<div class="section-content">
	<div class="toolbar clearfix section-header">
	    <strong>
	    <span class="left">
	    	<span class="ui-icon icon-ttl-section-info"></span>
	      	<%-- 基本配置信息  --%>
	      	<s:text name="scf_baseConfInfo"/>
	    </span>
	    </strong>
	</div>
	<s:set id="index" value="0"></s:set>
	<s:iterator value="systemConfigList" id="systemConfigMap" status="status">
		<div class="boxes">
		<div class="box-header"></div>
		<%--<s:hidden name="%{'bsCfInfoList['+#status.index+'].groupNo'}" value="%{#systemConfigMap.groupNo}"></s:hidden>--%>
		<table class="detail" cellpadding="0" cellspacing="0">                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
		  <s:iterator value="%{#systemConfigMap.list}" id="systemConfigMap2" status="status1">
		  	<tr>
		       <th style="width: 15%">
		       	<s:property value="#systemConfigMap2.configDescriptionChinese"/>
		       	<%-- <s:hidden name="%{'bsCfInfoList['+#index+'].configCode'}" value="%{#systemConfigMap2.configCode}"></s:hidden>
		       	<s:hidden name="%{'bsCfInfoList['+#index+'].type'}" value="%{#systemConfigMap2.type}"></s:hidden> --%>
		       </th>
		       <td style="width: 85%">
		         <span>
		         <s:if test='%{#systemConfigMap2.type == "1"}'>
		         	<select name="configValue" id="configValue">
		         		<s:iterator value="%{#systemConfigMap2.list}" id="configValueMap">
		         			<option value='<s:property value="#configValueMap.configValue"/>' <s:if test='%{#configValueMap.configEfficient == "1"}'>selected</s:if>><s:property value="#configValueMap.commentsChinese"/></option>
		         		</s:iterator>
		         	</select>
		         </s:if>
		         <s:if test='%{#systemConfigMap2.type == "2"}'>
		        		<s:iterator value="%{#systemConfigMap2.list}" id="configValueMap">
		        			<input type="checkbox" id="configValue" name="configValue" value='<s:property value="#configValueMap.configValue"/>' <s:if test='%{#configValueMap.configEfficient == "1"}'>checked</s:if>/><s:property value="#configValueMap.commentsChinese"/>
		        		</s:iterator>
		         </s:if>
		         <s:if test='%{#systemConfigMap2.type == "3"}'>
		        		<s:iterator value="%{#systemConfigMap2.list}" id="configValueMap">
		        			<input type="text" id="configValue" name="configValue" value='<s:property value="#configValueMap.configValue"/>' />
		        		</s:iterator>
		         </s:if>
		         </span>
		         <s:hidden id="configCode" name="configCode" value="%{#systemConfigMap2.configCode}"></s:hidden>
		         <s:hidden id="type" name="type" value="%{#systemConfigMap2.type}"></s:hidden>
		         <span class="ui-icon icon-help"
										style="margin-left: 10px; display: inline-block;"
										title="<s:text name='global.page.help'></s:text>"
										onclick="getSystemConfigHelp(this);return false;"></span>
				<div class="hide " id="configHelpText"></div>
				<div class="right">
					<div id="edit">
						<button class="save" type="button" onclick="plscf_edit(this);return false;"><span class="ui-icon icon-edit-big"></span><span class="button-text"><s:text name="global.page.edit"></s:text></span></button>
					</div>
					<div id="save" class="hide">
						<button class="save" type="button" onclick="plscf_back(this);return false;"><span class="ui-icon icon-back"></span><span class="button-text"><s:text name="global.page.back"></s:text></span></button>
						<s:url action="BINOLPLSCF01_saveBsCf" id="saveBsCfUrl"></s:url>
						<button class="save" type="button" onclick="plscf_saveBsCf('${saveBsCfUrl}',this);return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
					</div>
				</div>
				</td>
		     </tr>
		     <s:set id="index" value="%{#index+1}"></s:set>
		  </s:iterator>
		</table>
		<div class="clearfix"></div>
		</div>
	</s:iterator>
</div>

<%-- <div class="center">
<s:url action="BINOLPLSCF01_saveBsCf" id="saveBsCfUrl"></s:url>
	<div id="edit">
		<button class="save" type="button" onclick="plscf_edit(this);return false;"><span class="ui-icon icon-edit-big"></span><span class="button-text"><s:text name="global.page.edit"></s:text></span></button>
	</div>
	<div id="save" class="hide">
		<button class="save" type="button" onclick="plscf_back(this);return false;"><span class="ui-icon icon-back"></span><span class="button-text"><s:text name="global.page.back"></s:text></span></button>
		<s:url action="BINOLPLSCF01_saveBsCf" id="saveBsCfUrl"></s:url>
		<button class="save" type="button" onclick="plscf_saveBsCf('${saveBsCfUrl}');return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
	</div>
</div> --%>
</s:if>
<s:else>
<div class="section-content" style="color: red;font-size: 16px;"><s:text name="scf_noConfigInfo"/></div>
</s:else>
</s:i18n>