<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/st/jcs/BINOLSTJCS02.js"></script>

<s:i18n name="i18n.st.BINOLSTJCS02">
<s:text name="global.page.select" id="select_default"/>
<div class="hide">
	<s:url action="BINOLSTJCS02_save" id="save_url"></s:url>
	<a id="saveUrl" href="${save_url}"></a>
</div>
<div id="div_main" class="main container clearfix">
	<div class="panel ui-corner-all">
		    <div class="panel-header">
			    <div class="clearfix">
			       <span class="breadcrumb left"> 
			          <span class="ui-icon icon-breadcrumb"></span>
						<s:text name="JSC02_manage"/> &gt; <s:text name="JSC02_title"/>
			       </span>
			    </div>
			</div>
			<div id="actionResultDisplay"></div>
			<form id="mainForm" class="inline" method="post">
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
							      <th><s:text name="JSC02_depotCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
							      <td><span><s:textfield name="depotCode" cssClass="text" maxlength="10"></s:textfield></span></td>
							      <%--测试区分 --%>
								  <th><s:text name="JSC02_testType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
							      <td>
								      	<span>
								      		<select name="testType" style="width:100px;" id="testType" onchange="binOLSTJCS02.clearDepartInfo();">
					        					<option value="0"><s:text name="JCS02_offcerTpye"></s:text></option>
					        					<option value="1"><s:text name="JCS02_testType"></s:text></option>
					        				</select>
				        				</span>
			        			  </td>
						    </tr>
						    <tr>
							      <%--仓库名称 --%>
							      <th><s:text name="JSC02_depotNameCN"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
								  <td><span><s:textfield name="depotNameCN" cssClass="text" maxlength="60"></s:textfield></span></td>
								  <%--品牌 --%>
							      <th><s:text name="JCS02_brand"></s:text></th>
							      <td><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" ></s:select></span></td>
						    </tr>
						    <tr>
						    	  <%--仓库英文名 --%>
							      <th><s:text name="JSC02_depotNameUS"></s:text></th>
							      <td><span><s:textfield name="depotNameEN" cssClass="text" maxlength="60"></s:textfield></span></td>
							      <%--归属部门 --%>
						    	  <th><s:text name="JSC02_belongTo"></s:text></th>
							      <td>
							      	<input type="hidden" name="organizationID" id="organizationID"></input>
							      	<span id="showRelDepartName">
							      	</span>
							      	<a class="add" style="margin-left:50px" onclick="binOLSTJCS02.popBelongToDepart(this);return false;">
			                            <span class="ui-icon icon-search"></span>
			                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
			                        </a>
							      </td>
						    </tr>
							<tr>
								<%--地址 --%>
								<th><s:text name="JSC02_address"></s:text></th>
								<td colspan="3">
									<s:textfield name="address" cssClass="text" cssStyle="width:70%" maxLength="100"></s:textfield>
								</td>
							</tr>
		            	</table>
		            </div>
	            </div>
				<%-- ====================== 基本信息DIV结束 ===================== --%>
				
				<%-- ====================== 添加部门仓库关系DIV开始 ===================== --%>
				<div class="section">
					<div class="section-header clearfix">
		              	<a class="add left" href="#" onclick="binOLSTJCS02.popBindDepart(this,$('#brandInfoId').serialize());return false;">
			       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="JSC02_bindDepart" /></span>
			       		</a>
		            </div>
		            <div class="section-content">
		            	<table id="bindDepart" border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
		            		<thead>
							    <tr>
							      <th width="20%"><s:text name="JSC02_departCode" /></th>
							      <th width="20%"><s:text name="JSC02_departName" /></th>
							      <th width="10%"><s:text name="JSC02_departType" /></th>
							      <th width="10%"><s:text name="JSC02_defaultFlag" /></th>
							      <th width="20%"><s:text name="JCS02_comment" /></th>
							      <th width="10%"><s:text name="JSC02_operation" /></th>
							    </tr>
							</thead>
							<tbody id="bindDepart_tbody">
							</tbody>
		            	</table>
		            </div>
				</div>
				<%-- ====================== 添加部门仓库关系DIV结束 ===================== --%>
				<%-- 操作按钮 --%>
	            <div id="editButton" class="center clearfix">
	       			<s:url action="BINOLBSCNT04_save" id="addCounterUrl"></s:url>
	       			<button class="save" onclick="binOLSTJCS02.save();return false;">
	            		<span class="ui-icon icon-save"></span>
	            		<span class="button-text"><s:text name="global.page.save"/></span>
	             	</button>
		            <button id="close" class="close" type="button"  onclick="binOLSTJCS02.close();return false;">
		           		<span class="ui-icon icon-close"></span>
		           		<span class="button-text"><s:text name="global.page.close"/></span>
		         	</button>
	            </div>
			</div>
			</form>
	</div>
	
<%-- ================== 省市选择DIV START ======================= --%>
<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<div class="clearfix">
		<span class="label"></span>
		<ul class="u2"><li onclick="binOLSTJCS02.getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="%{#reginMap.reginId}" />_<s:property value="provinceId" />" onclick="binOLSTJCS02.getNextRegin(this, '${select_default }', 1);">
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