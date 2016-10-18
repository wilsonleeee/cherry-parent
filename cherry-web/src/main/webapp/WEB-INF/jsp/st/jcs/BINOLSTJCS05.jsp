<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<%@page import="com.cherry.cm.core.CherryConstants"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/jcs/BINOLSTJCS05.js"></script>
<style>
	.treebox_left{
		 background: none repeat scroll 0 0 #FFFFFF;
    	border: 1px solid #D6D6D6;
    	height: 300px 
	}
</style>

<%String model = String.valueOf(request.getAttribute("configModel"));
%>


<s:i18n name="i18n.st.BINOLSTJCS05">
<s:url id="search_url" value="/st/BINOLSTJCS05_search"/>
<s:url id="getAddTreeNodes_url" value="/st/BINOLSTJCS05_getAddTreeNodes"/>
<s:url id="saveAdd_url" value="/st/BINOLSTJCS05_saveAdd"/>
<s:url id="deleteDepotBusiness_url" value="/st/BINOLSTJCS05_deleteDepotBusiness"/>
<s:url id="getEditInfo_url" value="/st/BINOLSTJCS05_getEditInfo"/>
<s:url id="saveEdit_url" value="/st/BINOLSTJCS05_saveEdit"/>

<div id="JCS05_select" class="hide"><s:text
		name="global.page.all" /></div> 
<div class="panel-header">
     	<%-- 实体仓库业务配置 --%>
       <div class="clearfix"> 
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>  
       	<%
			if(model.equals(CherryConstants.DEPOTBUSINESS_REGION)){
		%>
			<cherry:show domId="BINOLSTJCS0501">
				 <a id="addBtn" class="delete right" onclick="binOLSTJCS05.popAddDialog();return false;">
               		<span class="ui-icon icon-add"></span>
               		<span class="button-text"><s:text name="JCS05_add"/></span>
              	 	</a>
			</cherry:show>
		<%} %>
       </div>
</div>
<div id="actionResultDisplay"></div>
<div id="errorMessage"></div>
 <%-- ================== 错误信息提示 START ======================= --%>
     <div id="errorDiv" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline" onsubmit="binOLSTJCS05.search(); return false;">
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>
            </div>
            <div class="box-content clearfix">
               	<div class="column" style="width:49%;">
               		<p>
                	<%-- 所属品牌 --%>
                	<s:if test="%{brandInfoList.size()> 1}">
                    	<label style="width:90px"><s:text name="JCS05_brandName"></s:text></label>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label style="width:90px"><s:text name="JCS05_brandName"></s:text></label>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
                	</p>
                	<p>
	               		<%-- 仓库/区域名称 --%>
	                  	<label style="width:90px"><s:text name="JCS05_name"/></label>
	                  	<s:textfield name="name" cssClass="text" maxlength="60" id="name"/>
	                </p> 
	                <p style="display:none">
        				<%--仓库/区域编码 --%>
        				<label style="width:90px"><s:text name="JCS05_code"/></label>
	               		<s:textfield name="code" cssClass="text" maxlength="15"/>
        			</p>
		        </div>      
        		<div class="column last" style="width:50%;">
        			<p>
	               		<%-- 业务类型 --%>
	                  	<label style="width:90px"><s:text name="JCS05_businessType"/></label>
	                  	<s:text name="global.page.all" id="selectAll"></s:text>
	                  	
	                  	<s:select name="businessType" list="#application.CodeTable.getCodes('1132')" listKey="CodeKey" listValue="Value" headerKey="" headerValue='%{#selectAll}'/>
	                	
	                </p>  
        			     
	            </div>
              </div>
              <p class="clearfix">
           			<%-- 查询 --%>
           			<button class="right search" type="submit" onclick="binOLSTJCS05.search(); return false;" id="searchBut">
           				<span class="ui-icon icon-search-big"></span>
           				<span class="button-text"><s:text name="global.page.search"/></span>
           			</button>
          		</p> 
          		</cherry:form> 
          </div>
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section">
		<div class="section-header">
			<strong> 
			<span class="ui-icon icon-ttl-section-search-result"></span> 
			<s:text name="global.page.list" />
			
		 	</strong>
		 	<span class="breadcrumb">
		 		（
					<%
						if(model.equals(CherryConstants.DEPOTBUSINESS_REGION)){
					%>
							<s:text name="JCS05_byConfing"></s:text>
					<%   }else{%>
							<s:text name="JCS05_byDepart"></s:text>
					<%   }%>
				）
		 	</span>
		</div>
		<div class="section-content">
		<div class="toolbar clearfix">
		<%
			if(model.equals(CherryConstants.DEPOTBUSINESS_REGION)){
		%>
			<span class="left" id="deportOption">
				<cherry:show domId="BINOLSTJCS0503">
					 <a id="deleteBtn" class="delete">
                		<span class="ui-icon icon-delete"></span>
                		<span class="button-text"><s:text name="JCS05_delete"/></span>
               	 	</a>
				</cherry:show>
			</span>
		<%} %>
		</div>
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0"
		class="jquery_table" width="100%">
		<thead>
			<tr>
				<%
					if(model.equals(CherryConstants.DEPOTBUSINESS_REGION)){
				%>
				<%--选择 --%>
				<%-- <th><s:checkbox name="allSelect" id="checkAll" onclick="checkSelectAll(this)"/></th>--%>
				<th class="center"><input type="checkbox" name="allSelect" id="checkAll" onclick="binOLSTJCS05.checkSelectAll(this)"/></th>
				<%} %>
				<%-- No. --%>
				<th class="center"><s:text name="No." /></th>
				<%-- 出库仓库/区域--%>
				<th class="center"><s:text name="JCS05_out" /></th>
				<%-- 入库仓库/区域  --%>
				<th class="center"><s:text name="JCS05_in" /></th>
				<%-- 业务  --%>
				<th class="center"><s:text name="JCS05_businessType" /></th>
				<%-- 品牌   --%>
				<th class="center"><s:text name="JCS05_brandName" /></th>
				<%
					if(model.equals(CherryConstants.DEPOTBUSINESS_REGION)){
				%>
				<%-- 操作 --%>
				<th class="center"><s:text name="JCS05_option" /></th>
				<%} %>
			</tr>
		</thead>
	</table>
	</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<div id="add_main" class="hide">
	<div id="add_body">
		<%--业务类型 --%>
		<div id="errorDiv_dialog" class="actionError" style="display:none">
       		<ul>
           		<li><span id="errorSpan"><s:text name="JCS05_noChoice"></s:text></span></li>
       		</ul>         
   		</div>
   		<div id="errorDiv2" class="actionError" style="display:none">
	        <ul>
	            <li><span id="errorSpan2"></span></li>
	        </ul>         
	    </div>
	    <div id="businessType_add">
	    	<table class="detail">
		    	<tbody>
			    	<tr>
						<th>
							<s:text name="JCS05_businessType"/>
						</th>
						<td>
							<s:select name="businessType" list="#application.CodeTable.getCodesWithFilter('1132','60')" listKey="CodeKey" listValue="Value" disabled="" onchange="binOLSTJCS05.showAndhideReverseBusiness(this);return false;"/>
					        <input type="checkbox" id="reverseBusiness" value="1"/>
					        <span style="float:none;"><s:text name="JCS05_creatReverseBusiness"/></span>
				        </td>
					</tr>
					<%--出库仓库 --%>
					<tr>
						<th><s:text name="JCS05_outDeport"/></th>
						<td>
							<s:select name="inventoryInfoId" list="inventoryList" listKey="inventoryInfoId+'*'+testType" listValue="'('+inventoryCode+')'+inventoryName" cssStyle="width:300px;" id="inventoryInfoId_add" ></s:select>
						</td>
					</tr>
				</tbody>
			</table>
	    </div>
		<div id="businessType_edit" class="hide">
            <input type="hidden" id="editDepotBusinessId" name="editDepotBusinessId" value=""/>
			<table class="detail">
				<tbody>
					<tr>
						<th><s:text name="JCS05_businessType"/></th>
						<td>
							<s:select name="businessType" id="businessType" list="#application.CodeTable.getCodes('1132')" listKey="CodeKey" listValue="Value" disabled="true"/>
					        <input type="checkbox" id="synchronousReverseBusiness" value="1" checked="checked"/>
					        <span style="float:none;"><s:text name="JCS05_synchronousReverseBusiness"/></span>
				        </td>
					</tr>
					<%--出库仓库 --%>
					<tr>
						<th>
							<span id="edit_outDeport">
								<s:text name="JCS05_outDeport"/>
							</span>
							<span class="hide" id="edit_inDeport" style="display: none;">
								<s:text name="JCS05_inDeport"/>
							</span>
						</th>
						<td>
							<s:select name="inventoryInfoId" list="inventoryList" listKey="inventoryInfoId+'*'+testType" listValue="'('+inventoryCode+')'+inventoryName" cssStyle="width:300px;" id="inventoryInfoId_edit" disabled="disabled"></s:select>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<span class="highlight">※</span>
		<span class="gray"><span id="spanTip"><s:text name="JCS05_tip"></s:text></span><span id="spanTipDepart" class="hide"><s:text name="JCS05_tipDepart"></s:text></span></span>
		<div class="jquery_tree" style="margin-top:5px;">
			<div id="regionTree">
				<div class="box2-header clearfix"> 
				   <strong id="left_tree_in" class="left active" style="background:#E8E8C8;">
		       			<span class="ui-icon icon-flag"></span>
		       			<span id="spanchoiceInRegion"><s:text name="JCS05_choiceInRegion"/></span>
		       			<span id="spanchoiceInDepart" class="hide"><s:text name="JCS05_choiceInDepart"/></span>
		       	   </strong>
		       	   <strong id="left_tree_out" class="left active" style="background:#E8E8C8;display:none;">
		       			<span class="ui-icon icon-flag"></span>
		       			<span id="spanchoiceOutDepor"><s:text name="JCS05_choiceOutDepor"/></span>
		       			<span id="spanchoiceOutDepart" class="hide"><s:text name="JCS05_choiceOutDepart"/></span>
		       	   </strong>
		       	   <span>
		       	      <input type="checkbox" id="configByDepOrg" value="2" onchange="binOLSTJCS05.relodeTree();"/><s:text name="JCS05_configByDepOrg"/>
		       	   </span>
		       	   <span id="spanSearchDepot" class="right">
						<input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 140px;" name="locationPosition" autocomplete="off">
						<a class="search" onclick="binOLSTJCS05.locationDepPosition(this);return false;">
							<span class="ui-icon icon-position"></span>
							<span class="button-text"><s:text name="JCS05_locationPosition"></s:text></span>
						</a>
					</span>
		       	</div>
			    <div class="jquery_tree treebox_left tree" id = "treeDemo1" style="overflow: auto;background:#FCFCFC;">
			    </div>
		     </div>
		</div>
	</div>
</div>

<span id="search" style="display:none">${search_url}</span>
<span id="getAddTreeNodes" style="display:none">${getAddTreeNodes_url}</span>
<span id="saveAdd" style="display:none">${saveAdd_url}</span>
<span id="deleteDepotBusiness" style="display:none">${deleteDepotBusiness_url}</span>
<span id="getEditInfo" style="display:none">${getEditInfo_url}</span>
<span id="saveEdit" style="display:none">${saveEdit_url}</span>

<input type="hidden" id="errmsg1" value='<s:text name="EMO00051"/>'/>

<input type="hidden" id="yes" value='<s:text name="JCS05_yes"/>'/>
<input type="hidden" id="deleteTitle" value='<s:text name="JCS05_delete"/>'/>
<input type="hidden" id="deleteWarming" value='<s:text name="JCS05_deleteWarming"/>'/>
<input type="hidden" id="onlyEditDeliver" value='<s:text name="JCS05_onlyEditDeliver"/>'/>
<input type="hidden" id="dialogTitle" value='<s:text name="JCS05_dialogTitle"/>'></input>
<input type="hidden" id="onlyOne" value='<s:text name="JCS05_onlyOne"/>'></input>
<input type="hidden" id="cancel" value='<s:text name="JCS05_cancel"/>'></input>
<input type="hidden" id="testWarming" value='<s:text name="JCS05_testWarming"/>'></input>
<input type="hidden" id="officialWarming" value='<s:text name="JCS05_officialWarming"/>'></input>
<input type="hidden" id="configModel" value='<s:property value="configModel"/>'></input>

<div class="hide" id="dialogInit"></div>

</s:i18n>