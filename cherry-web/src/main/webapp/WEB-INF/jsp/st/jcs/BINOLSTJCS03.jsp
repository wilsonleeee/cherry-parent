<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/st/jcs/BINOLSTJCS03.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	binOLSTJCS03.testType = '${depotInfoMap.testType}';
	$("#testType_select option").each(function(){
			if($(this).val() == binOLSTJCS03.testType){
				this.selected = true;
			}
	});
});
</script>
<s:i18n name="i18n.st.BINOLSTJCS03">
<s:text name="global.page.select" id="select_default"/>
<div class="hide">
	<s:url action="BINOLSTJCS03_save" id="save_url"></s:url>
	<a id="saveUrl" href="${save_url}"></a>
</div>
<div id="div_main" class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="show">
			<div class="panel-header">
			    <div class="clearfix">
			       <span class="breadcrumb left"> 
			          <span class="ui-icon icon-breadcrumb"></span>
						<s:text name="JSC03_manage"/> &gt; <s:text name="JSC03_title_1"/>
			       </span>
			    </div>
			</div>
			<div class="panel-content clearfix">
				<%-- ====================== 基本信息DIV开始 ===================== --%>
				<div class="section">
	              	<div class="section-header">
	              		<strong>
	              			<span class="ui-icon icon-ttl-section-info-edit"></span>
	              			<s:text name="global.page.title"/><%-- 基本信息 --%>
	              		</strong>
	              	</div>
	              	<div class="section-content">
		            	<table class="detail" cellpadding="0" cellspacing="0">
		            		<tr>
		            			  <%--仓库code --%>
							      <th><s:text name="JSC03_depotCode"></s:text></th>
							      <td><span><s:property value="%{depotInfoMap.depotCode}"/></span></td>
							      <%--测试区分 --%>
								  <th><s:text name="JSC03_testType"></s:text></th>
							      <td>
							      	<span>
							      		<s:if test='0==depotInfoMap.testType'>
							      			<s:text name="JSC03_offcerTpye"></s:text>
							      		</s:if>
							      		<s:else>
							      			<s:text name="JSC03_testType"></s:text>
							      		</s:else>
				        			</span>
			        			  </td>
						    </tr>
						    <tr>
						    	  <%--仓库名称 --%>
							      <th><s:text name="JSC03_depotNameCN"></s:text></th>
								  <td>
								  	<span><s:property value="%{depotInfoMap.depotNameCN}"/></span>
								  </td>
		            			  <%--品牌 --%>
							      <th><s:text name="JSC03_brand"></s:text></th>
							      <td><span><s:property value="%{depotInfoMap.brandName}"/></span></td>
							      
						    </tr>
						    <tr>
							      <%--仓库英文名 --%>
							      <th><s:text name="JSC03_depotNameUS"></s:text></th>
							      <td><span><s:property value="%{depotInfoMap.depotNameEN}"/></span></td>
							      <%-- 归属部门 --%>
								  <th><s:text name="JSC03_belongTo"></s:text></th>
							      <td>
							      	<span>
							      	<s:property value="depotInfoMap.departName"/>
							      	</span>
							      </td>
						    </tr>
							<tr>
								<%--地址 --%>
								<th><s:text name="JSC03_address"></s:text></th>
								<td colspan="3">
									<span>
										<s:property value="%{depotInfoMap.address}"/>
									</span>
								</td>
							</tr>
		            	</table>
		            </div>
	            </div>
				<%-- ====================== 基本信息DIV结束 ===================== --%>
				
				<%-- ====================== 添加部门仓库关系DIV开始 ===================== --%>
				<div class="section">
					<div class="section-header clearfix">
						<strong class="left"> 
			        		<span class="ui-icon icon-ttl-section-search-result"></span>
			        		<s:text name="JSC03_relDepart"/>
			        	</strong>
		            </div>
		            <div class="section-content">
		            	<table id="bindDepart" border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
		            		<thead>
							    <tr>
							      <th width="20%"><s:text name="JSC03_departCode" /></th>
							      <th width="20%"><s:text name="JSC03_departName" /></th>
							      <th width="10%"><s:text name="JSC03_departType" /></th>
							      <th width="10%"><s:text name="JSC03_defaultFlag" /></th>
							      <th width="20%"><s:text name="JSC03_comment" /></th>
							    </tr>
							</thead>
							<tbody id="bindDepart_tbody_show">
							<s:iterator value="relInfoList" id="relInfoMap">
								<tr>
									<td>
										<span><s:property value='departCode'></s:property></span>
										<input type="hidden" name="relOrganizationIDArr" value="<s:property value='departId'></s:property>"></input>
									</td>
									<td>
										<span><s:property value='departName'></s:property></span>
									</td>
									<td>
										<span><s:property value='#application.CodeTable.getVal("1000", #relInfoMap.type)'></s:property></span>
									</td>
									<td>
										<span><s:property value='#application.CodeTable.getVal("1135",#relInfoMap.defaultFlag)'></s:property></span>
									</td>
									<td>
										<span><s:property value='comments'></s:property></span>
									</td>
								</tr>
							</s:iterator>
							</tbody>
		            	</table>
		            </div>
				</div>
				<%-- ====================== 添加部门仓库关系DIV结束 ===================== --%>
				<%-- 操作按钮 --%>
	            <div id="editButton" class="center clearfix">
	       			<button class="edit" onclick="binOLSTJCS03.edit();return false;">
	            		<span class="ui-icon icon-edit"></span>
	            		<span class="button-text"><s:text name="global.page.edit"/></span>
	             	</button>
		            <button id="close" class="close" type="button"  onclick="binOLSTJCS03.close();return false;">
		           		<span class="ui-icon icon-close"></span>
		           		<span class="button-text"><s:text name="global.page.close"/></span>
		         	</button>
	            </div>
			</div>
		</div>
		<div id="edit" class="hide">
		    <div class="panel-header">
			    <div class="clearfix">
			       <span class="breadcrumb left"> 
			          <span class="ui-icon icon-breadcrumb"></span>
						<s:text name="JSC03_manage"/> &gt; <s:text name="JSC03_title"/>
			       </span>
			    </div>
			</div>
			<div id="actionResultDisplay"></div>
			<form id="mainForm" class="inline" method="post">
			<s:hidden name="depotId" value="%{depotInfoMap.depotId}"></s:hidden>
			<s:hidden name="testType" value="%{depotInfoMap.testType}"></s:hidden>
			<div class="panel-content clearfix">
				<%-- ====================== 基本信息DIV开始 ===================== --%>
				<div class="section">
	              	<div class="section-header">
	              		<strong>
	              			<span class="ui-icon icon-ttl-section-info-edit"></span>
	              			<s:text name="global.page.title"/><%-- 基本信息 --%>
	              		</strong>
	              	</div>
	              	<div class="section-content">
		            	<table class="detail" cellpadding="0" cellspacing="0">
		            		<tr>
		            			  <%--仓库code --%>
							      <th><s:text name="JSC03_depotCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
							      <td><span><s:textfield name="depotCode" cssClass="text" maxlength="10" value="%{depotInfoMap.depotCode}"></s:textfield></span></td>
							      <%--测试区分 --%>
								  <th><s:text name="JSC03_testType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
							      <td>
								      	<span>
								      		<select id="testType_select" name="testType" style="width:100px;" disabled="disabled" onchange="binOLSTJCS03.clearDepartInfo();">
					        					<option value="0"><s:text name="JSC03_offcerTpye"></s:text></option>
					        					<option value="1"><s:text name="JSC03_testType"></s:text></option>
					        				</select>
				        				</span>
			        			  </td>
						    </tr>
						    <tr>
							      <%--仓库名称 --%>
							      <th><s:text name="JSC03_depotNameCN"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
								  <td><span><s:textfield name="depotNameCN" cssClass="text" maxlength="60" value="%{depotInfoMap.depotNameCN}"></s:textfield></span></td>
								  <%--品牌 --%>
							      <th><s:text name="JSC03_brand"></s:text></th>
							      <td><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"></s:select></span></td>
						    </tr>
						    <tr>
							      <%--仓库英文名 --%>
							      <th><s:text name="JSC03_depotNameUS"></s:text></th>
							      <td><span><s:textfield name="depotNameEN" cssClass="text" maxlength="60" value="%{depotInfoMap.depotNameEN}"></s:textfield></span></td>
							      <%-- 归属部门 --%>
								  <th><s:text name="JSC03_belongTo"></s:text></th>
							      <td>
							      	<input type="hidden" name="organizationID" id="organizationID" value="<s:property value='depotInfoMap.departId'/>"></input>
							      	<span id="showRelDepartName">
							      		<s:property value="depotInfoMap.departName"/>
							      	</span>
							      	<a class="add" style="margin-left:50px" onclick="binOLSTJCS03.popBelongToDepart(this);return false;">
			                            <span class="ui-icon icon-search"></span>
			                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
			                        </a>
							      </td>
						    </tr>
							<tr>
								<%--地址 --%>
								<th><s:text name="JSC03_address"></s:text></th>
								<td colspan="3">
									<s:textfield name="address" cssClass="text" cssStyle="width:70%" value="%{depotInfoMap.address}" maxLength="100"></s:textfield>
								</td>
							</tr>
		            	</table>
		            </div>
	            </div>
				<%-- ====================== 基本信息DIV结束 ===================== --%>
				
				<%-- ====================== 添加部门仓库关系DIV开始 ===================== --%>
				<div class="section">
					<div class="section-header clearfix">
		              	<a class="add left" href="#" onclick="binOLSTJCS03.popBindDepart(this,$('#brandInfoId').serialize());return false;">
			       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="JSC03_bindDepart" /></span>
			       		</a>
		            </div>
		            <div class="section-content">
		            	<table id="bindDepart" border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
		            		<thead>
							    <tr>
							      <th width="20%"><s:text name="JSC03_departCode" /></th>
							      <th width="20%"><s:text name="JSC03_departName" /></th>
							      <th width="10%"><s:text name="JSC03_departType" /></th>
							      <th width="10%"><s:text name="JSC03_defaultFlag" /></th>
							      <th width="20%"><s:text name="JSC03_comment" /></th>
							      <th width="10%"><s:text name="JSC03_operation" /></th>
							    </tr>
							</thead>
							<tbody id="bindDepart_tbody">
							<s:iterator value="relInfoList" id="relInfoMap">
								<tr>
									<input type="hidden" name="departId" value="<s:property value='departId'></s:property>" />
									<td>
										<span><s:property value='departCode'></s:property></span>
										<input type="hidden" name="relOrganizationIDArr" value="<s:property value='departId'></s:property>"></input>
									</td>
									<td>
										<span><s:property value='departName'></s:property></span>
									</td>
									<td>
										<span><s:property value='#application.CodeTable.getVal("1000", #relInfoMap.type)'></s:property></span>
									</td>
									<td>
										<s:select name="defaultFlagArr" list="#application.CodeTable.getCodes('1135')" listKey="CodeKey" listValue="Value" value="defaultFlag"/>
									</td>
									<td>
										<span><s:textfield name="commentArr" cssClass="text" maxlength="100" value="%{comments}" cssStyle="width:90%"></s:textfield></span>
									</td>
									<td>
										<a href="#" id="<s:property value='DepartId'></s:property>" class="delete center" onclick="binOLSTJCS03.deleteDepart(this);return false;">
											<span class="ui-icon icon-delete"></span>
											<span class="button-text"><s:text name="global.page.delete"></s:text></span>
										</a>
									</td>
								</tr>
							</s:iterator>
							</tbody>
		            	</table>
		            </div>
				</div>
				<%-- ====================== 添加部门仓库关系DIV结束 ===================== --%>
				<%-- 操作按钮 --%>
	            <div id="editButton" class="center clearfix">
	       			<s:url action="BINOLBSCNT04_save" id="addCounterUrl"></s:url>
	       			<button class="save" onclick="binOLSTJCS03.save();return false;">
	            		<span class="ui-icon icon-save"></span>
	            		<span class="button-text"><s:text name="global.page.save"/></span>
	             	</button>
	             	<button class="back" type="button" onclick="binOLSTJCS03.back();return false;">
	             		<span class="ui-icon icon-back"></span>
	             		<span class="button-text"><s:text name="global.page.back"/></span>
	             	</button>
		            <button id="close" class="close" type="button"  onclick="binOLSTJCS03.close();return false;">
		           		<span class="ui-icon icon-close"></span>
		           		<span class="button-text"><s:text name="global.page.close"/></span>
		         	</button>
	            </div>
			</div>
			</form>
		</div>
	</div>
	
<%-- ================== 省市选择DIV START ======================= --%>
<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<div class="clearfix">
		<span class="label"></span>
		<ul class="u2"><li onclick="binOLSTJCS03.getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="%{#reginMap.reginId}" />_<s:property value="provinceId" />" onclick="binOLSTJCS03.getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
</div>
<div id="cityTemp" class="ui-option hide"></div>
<div id="countyTemp" class="ui-option hide"></div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>
</div>
<%-- ================== 部门仓库关系默认区分DIV Start==========================--%>
<div id="defaultFlag_div" class="hide">
	<s:select name="defaultFlagArr" list="#application.CodeTable.getCodes('1135')" listKey="CodeKey" listValue="Value"/>
</div>
<%-- ================== 部门仓库关系默认区分DIV END==========================--%>
<div class="hide">
<s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
<input type="hidden" id="deleteTitle" value='<s:text name="global.page.delete"></s:text>'></input>
</div>
</s:i18n>