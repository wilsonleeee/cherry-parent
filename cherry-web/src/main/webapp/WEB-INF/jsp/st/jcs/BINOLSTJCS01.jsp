<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/st/jcs/BINOLSTJCS01.js"></script>
<s:url id="search_url" action="BINOLSTJCS01_search"/>
<s:url id="save_Url" action="BINOLSTJCS01_save"/>
<s:url id="disable" action="BINOLSTJCS01_disable"></s:url>
<s:url id="enable" action="BINOLSTJCS01_enable"></s:url>
<s:url id="add_url" action="BINOLSTJCS01_add"/>
<s:url id="testTypeUrl" action="BINOLSTJCS01_testType"/>
<s:url id="depotInfo_Url" action="BINOLSTJCS01_getDepotInfoList"/>
<div class="hide">
	<a id="searchUrl" href="${search_url}"></a>
	<a id="saveUrl" href="${save_Url}"></a>
	<a id="addUrl" href="${add_url}"></a>
	<a id="depotInfoUrl" href="${depotInfo_Url}"></a>
	<a id="testTypeUrl" href="${testTypeUrl}"></a>
</div>
<s:i18n name="i18n.st.BINOLSTJCS01">
	<div class="panel-header">
		<div class="clearfix">
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>  
	     	<span class="right"> 
	     		<cherry:show domId="BINOLSTJCS01ADD">
	     			<s:url action="BINOLSTJCS02_init" id="addCounterUrl"></s:url>
			       	<a href="${addCounterUrl}" class="add" id="add" onclick="javascript:openWin(this);return false;">
				 		<span class="ui-icon icon-add"></span>
				 		<span class="button-text"><s:text name="JCS01_add"/></span>
				 	</a>
			 	</cherry:show>
	     	</span>
	   	</div>
	</div>
	<%-- ================== 错误信息提示 START ======================= --%>
    <div id="actionResultDisplay" class="hide"></div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
    	<div class="box">
    	<cherry:form id="mainForm">
        	<div class="box-header">
	           	<strong style="font-size:13px">
	           		<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
	           	</strong>
	           	<input type="checkbox" name="validFlag" id="validFlag" value="1"/><s:text name="JCS01_includeDisabled"/>
	        </div>
	        
	        	<div class="box-content clearfix">
	        		<div class="column" style="width:49%;">
	        			<%-- 所属品牌 --%>
	        			<p>
		        			<s:if test="brandInfoList.size() > 1">
								<label style="width:80px;"><s:text name="JCS01_brandName"></s:text></label>
								<s:text name="JCS09_select" id="JCS09_select"/>
			                    <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="BINOLSTJCS09.search();return false;" headerKey="" headerValue="%{#JCS09_select}" cssStyle="width:100px;"></s:select>
		        			</s:if>
		        			<s:else>
								<label style="width:80px;"><s:text name="JCS01_brandName"></s:text></label>
			                    <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="BINOLSTJCS09.search();return false;" headerKey="" cssStyle="width:100px;"></s:select>
		        			</s:else>
		        		</p>
		        		<%-- 实体仓库--%>
	        			<p>
	        				<label style="width:80px;"><s:text name="JCS01_depot"></s:text></label>
	        				<s:textfield id="depotName" name="depotName" cssClass="text" maxLength="60"></s:textfield>
	        			</p>
	        		</div>
	        		<div class="column last" style="width:50%;">
	        			<%-- 归属部门--%>
	        			<p>
	        				<label style="width:80px;"><s:text name="JCS01_organizationID"></s:text></label>
	        				<s:textfield id="departName" name="departName" cssClass="text" maxLength="50"></s:textfield>
	        			</p>
	        			<%-- 测试区分--%>
	        			<p>
	        				<label style="width:80px;"><s:text name="JCS01_depotType"></s:text></label>
	        				<select name="testType" style="width:100px;">
	        					<option value=""><s:text name="JCS01_all"></s:text></option>
	        					<option value="0"><s:text name="JCS01_offcerTpye"></s:text></option>
	        					<option value="1"><s:text name="JCS01_testType"></s:text></option>
	        				</select>
	        			</p>
	        		</div>
	        	</div>
	        	<p class="clearfix">
	        		<%-- 查询 --%>
	        		<button class="right search" type="submit" onclick="BINOLSTJCS01.search();return false;" id="searchBut">
	        			<span class="ui-icon icon-search-big"></span>
	        			<span class="button-text" style="font-size:15px"><s:text name="global.page.search"/></span>
	        		</button>
	       		</p> 
	        </cherry:form>
	    </div>
	 	<div id="section" class="section hide">
	        <div class="section-header clearfix">
	        	<strong class="left"> 
	        		<span class="ui-icon icon-ttl-section-search-result"></span>
	        		<s:text name="JCS01_list"/>
	        	</strong>
	        </div>
        	<div class="section-content">
        	<div class="toolbar clearfix">
		       		<span class="left">
			       		<cherry:show domId="BINOLSTJCS01EN">
					       	<a href="${enable}" class="add" id="enable" onclick="BINOLSTJCS01.confirmInit(this,'enable');return false;">
						 		<span class="ui-icon icon-enable"></span>
						 		<span class="button-text"><s:text name="JCS01_enable"/></span>
						 	</a>
					 	</cherry:show>
					 	<cherry:show domId="BINOLSTJCS01DIS">
					       	<a href="${disable}" class="add" id="disable" onclick="BINOLSTJCS01.confirmInit(this,'disable');return false;">
						 		<span class="ui-icon icon-delete"></span>
						 		<span class="button-text"><s:text name="JCS01_disable"/></span>
						 	</a>
					 	</cherry:show>
				 	</span>
				 	<span class="right"> <%-- 设置列 --%>
						<a class="setting"> 
							<span class="ui-icon icon-setting"></span> 
							<span class="button-text"><s:text name="global.page.colSetting" /></span> 
						</a>
					</span>
			 	</div>
        		<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
		            <thead>
		              <tr>
		             	<%-- 选择 --%>
						<th><input type="checkbox" id="selectAll" onclick="BINOLSTJCS01.checkAll(this);"/></th>
		                <%-- NO. --%>
		                <th><s:text name="JCS01_num"/></th>
		                <%-- 仓库编号--%>
		                <th class="center"><s:text name="JCS01_depotCode"/></th>
		                <%-- 仓库名称 --%>
		                <th class="center"><s:text name="JCS01_depotNameCN"/></th>
						<%-- 归属部门 --%>
		                <th class="center"><s:text name="JCS01_organizationID"/></th>
		                <%-- 是否测试仓库  --%>
		                <th class="center" class="center"><s:text name="JCS01_depotType"/></th>
		                <%-- 仓库地址  --%>
		                <th class="center"><s:text name="JCS01_address"/></th>
		                <%-- 有效区分  --%>
		                <th class="center"><s:text name="JCS01_validFlag"/></th>
		              </tr>
		            </thead>
         		</table>
        	</div>
        </div>
    </div>
	<div class="hide">
		<table>
			<tbody id="newLine">
				<tr>
					<td></td><%-- 选择 --%>
			      	<td></td><%-- NO. --%>
			  		<td><span><input name="depotCode" type="text" class="text" maxlength="10" onchange="BINOLSTJCS01.check(this);" style="width:60px;"/></span><%-- 仓库编号 --%>
			  		<span class="highlight"><s:text name="global.page.required"/></span></td>
			  		<td><span><span class="ui-icon icon-search right" style="cursor:pointer" onclick="BINOLSTJCS01.departBox(this,'1');return false;"></span></span></td>
			  		<td><span><input name="depotNameCN" type="text" class="text" maxlength="100" style="width:120px;"/></span></td>
			  		<td><span><input name="depotNameEN" type="text" class="text" maxlength="100" style="width:120px;"/></span></td>
			  		<td><span><input name="address" type="text" class="text" maxlength="180" style="width:200px;"/></span></td>
			  		<td class="center">
						<span><select name="testTypeAdd" id="testTypeAdd"><option value="0"><s:text name="JCS01_offcerTpye"/></option><option value="1"><s:text name="JCS01_testType"/></option></select></span>
					</td>
			  		<td class="center"><span class='ui-icon icon-valid'></span></td>
			  		<td><%-- 操作 --%>
			  		<a href="#" class="add center" id="save" onclick="BINOLSTJCS01.add();return false;">
				 					<span class="ui-icon icon-save-s"></span>
				 					<span class="button-text"><s:text name="JCS01_save"/></span></a>
				 	<a href="#" class="add center" id="cancel" onclick="BINOLSTJCS01.search();return false;">
				 					<span class="ui-icon icon-delete"></span>
				 					<span class="button-text"><s:text name="JCS01_cancel"/></span></a></td>
			    </tr>
		    </tbody>
		</table>
	</div>
	<div class="hide" id="dialogInit"></div>
      <div style="display: none;">
      <div id="disableText"><p class="message"><span><s:text name="JCS01_disableText"/></span></p></div>
      <div id="disableTitle"><s:text name="JCS01_disableTitle"/></div>
      <div id="enableText"><p class="message"><span><s:text name="JCS01_enableText"/></span></p></div>
      <div id="enableTitle"><s:text name="JCS01_enableTitle"/></div>
      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      <div id="dialogClose"><s:text name="global.page.close" /></div>
      <div id="detailTitle"><s:text name="JCS01_detailTitle"/></div>
      <input type="hidden" id="JCSHighlight" value='<span class="highlight"><s:text name="global.page.required"/>'/>
      <input type="hidden" id="JCSSave" value='<s:text name="JCS01_save"/>'/>
      <input type="hidden" id="JCSCancel" value='<s:text name="JCS01_cancel"/>'/>
      <input type="hidden" id="JCSyes" value='<s:text name="JCS01_testType"/>'/>
      <input type="hidden" id="JCSno" value='<s:text name="JCS01_offcerTpye"/>'/>
      </div>
</s:i18n>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>