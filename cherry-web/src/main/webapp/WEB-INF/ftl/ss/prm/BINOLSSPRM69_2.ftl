<#include "/WEB-INF/ftl/common/head.inc.ftl" />
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM69_2.js"></script>
<@s.i18n name="i18n.ss.BINOLSSPRM69">
<form id="basicForm">
<div class="main container clearfix" id="div_main">
	<div class="panel ui-corner-all">
		<div class="panel-header">
			<div class="clearfix">
				<span class="breadcrumb left"> 
					<span class="ui-icon icon-breadcrumb"></span>
	    			<a href="#"><@s.text name="SSPRM69_title2" /></a> > 
	    			<a href="#"><@s.text name="SSPRM69_relationSetTitle" /></a>
	    		</span>
			</div>
		</div>
		 <div id="errorDiv" class="actionError" style="display:none;">
			<ul>
				<li>
					<span id="errorSpan"></span>
				</li>
			</ul>
		</div>
		<div class="panel-content">
			<div class="section">
				<div class="section-header">
					<strong class="left"><span class="ui-icon icon-ttl-section-list"></span><@s.text name="SSPRM69_basicInfo"/></strong>
				</div>
				<div class="section-content" id="basicInfo">
					<#if ruleRelationGroupMap?exists>
						<@relationGroupInfo ruleRelationGroupMap = ruleRelationGroupMap  />
						<input type="hidden" id="groupNo" name="groupNo" value="${ruleRelationGroupMap.groupNo! }" />
					<#else>
						<@relationGroupInfo ruleRelationGroupMap = {}  />
					</#if>
				</div>
			</div>
			<#-- ========================= 排他关系表 =============================-->
	    	<#include "/WEB-INF/ftl/ss/prm/BINOLSSPRM69_4.ftl" />
	    	<div class="center">
		    	<button class="save" onclick="PRM69_2.saveRelation();return false;">
		    		<span class="ui-icon icon-save"></span>
		    		<span class="button-text"><@s.text name="global.page.save"/></span>
		    	</button>
		    	<#if ruleRelationGroupMap?exists>
			    	<button class="back" onclick="PRM69_2.initRelation();return false;">
			    		<span class="ui-icon icon-back"></span>
			    		<span class="button-text"><@s.text name="global.page.back"/></span>
			    	</button>
		    	</#if>
		    	<button class="close" onclick="window.close();return false;">
		    		<span class="ui-icon icon-close"></span>
		    		<span class="button-text"><@s.text name="global.page.close"/></span>
		    	</button>
		    </div>
	    </div>
	</div>
</div>
</form>
<#-- ========================= 基础信息 =============================-->
<#macro relationGroupInfo ruleRelationGroupMap = {}>
	<table class="detail">
		<tr>
			<th><@s.text name="SSPRM69_brandName"/></th>
			<td>
				${brandName!}
				<input type="hidden" id="brandId" value="${brandId! }" />
			</td>
			<th><@s.text name="SSPRM69_relationGroupName"/><span class="highlight">*</span></th>
			<td><span><input type="text" name="groupName" value="${ruleRelationGroupMap.groupName!}"></span></td>
		</tr>
		<tr>
			<th><@s.text name="SSPRM69_relationGroupComments"/></th>
			<td colspan="3">
				<textarea style="width:90%;height:40px;" name="comments" rows="" cols="">${ruleRelationGroupMap.comments! }</textarea>
			</td>
		</tr>
	</table>
</#macro>
<div class="hide">
	<#-- ========================= 添加排他关系HTML =============================-->
	<table>
		<tbody id="ruleRelationAddHtml">
			<tr>
		 		<td></td>
		      	<td>
			      	<span class="classification_3" onclick="PRM69_2.selectRelationVal(this,'A');return false;" style="cursor: pointer;">
			      		<input class="TYPEA" name="relationTypeA" type="hidden" />
			      		<input class="ICODEA" name="relationValueA" type="hidden" />
			      		<span class="DISPLAYA"><@s.text name="SSPRM69_selectAct" />...</span>
			      	</span>
			      	<span><@s.text name="SSPRM69_and" /></span>
			      	<span  class="classification_3" onclick="PRM69_2.selectRelationVal(this,'B');return false;" style="cursor: pointer;">
			      		<input class="TYPEB" name="relationTypeB" type="hidden" />
			      		<input class="ICODEB" name="relationValueB" type="hidden" />
			      		<span class="DISPLAYB"><@s.text name="SSPRM69_selectAct" />...</span>
			      	</span>
			      	<span><@s.text name="SSPRM69_mutex" /></span>
		      	</td>
		      	<td class="center">
					<a class="delete" onclick="PRM69_2.cancelAddRelaton(this);return false;">
						<span class="ui-icon icon-delete"></span>
						<span class="button-text"><@s.text name="global.page.delete"/></span>
					</a>
		      	</td>
		 	</tr>
	 	</tbody>
 	</table>
  	<#-- ========================= 选择关联值弹出框标题按钮 =============================-->
	<div id="selectRelationValTitle1"><@s.text name="SSPRM69_selectRelationValTitle1" /></div>
	<div id="selectRelationValTitle2"><@s.text name="SSPRM69_selectRelationValTitle2" /></div>
	<div id="confirm_btn"><@s.text name="global.page.dialogConfirm" /></div>
	<div id="cancel_btn"><@s.text name="global.page.dialogCancel" /></div>
	<#-- ============================ 错误信息 =============================== -->
	<div id="errMsg1"><@s.text name="SSPRM69_errMsg1" /></div>
	<div id="errMsg2"><@s.text name="SSPRM69_errMsg2" /></div>
	<div id="errMsg3"><@s.text name="SSPRM69_errMsg3" /></div>
	<div id="errMsg4"><@s.text name="SSPRM69_errMsg4" /></div>
</div>
<#-- ========================= 选择关联值弹出框 =============================-->
<div id="relationValDialog" class="hide">
	<span>
		<@s.text name="SSPRM69_selectAct" />：
		<input onclick="PRM69_2.selectRelationType(this);" type="radio" name="relationType" id="relationType1" value="1" checked="true" />
		<@s.text name="SSPRM69_relationCate" />
		<input onclick="PRM69_2.selectRelationType(this);"  type="radio" name="relationType" id="relationType2" value="2" />
		<@s.text name="SSPRM69_relationSingle" />
		<input type="hidden" id="relationValue" />
	</span>
	<span class="right">
		<input class="text" name="searchText" onKeyup ="PRM69_2.relationTableFilter();return false;" maxlength="50"/>
    	<a class="search" onclick="PRM69_2.relationTableFilter();return false;">
    		<span class="ui-icon icon-search"></span>
    		<span class="button-text"><@s.text name="global.page.searchfor"/></span>
    	</a>
	</span>
	<hr class="space" />
	<table id="relationValTable" class="jqueryTable" border="0" cellpadding="0" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th><@s.text name="SSPRM69_select"/></th>
				<th><@s.text name="SSPRM69_optionCode"/></th>
				<th><@s.text name="SSPRM69_optionName"/></th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>