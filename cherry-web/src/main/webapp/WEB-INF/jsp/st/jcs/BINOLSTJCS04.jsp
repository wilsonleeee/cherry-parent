<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/st/jcs/BINOLSTJCS04.js"></script>
<style>
	.treebox_left{
		 background: none repeat scroll 0 0 #FFFFFF;
    	border: 1px solid #D6D6D6;
    	height: 300px; 
	}
</style>
<s:i18n name="i18n.st.BINOLSTJCS04">
<s:url id="search_url" value="/st/BINOLSTJCS04_search"/>
<s:url id="getAddTreeNodes_url" value="/st/BINOLSTJCS04_getAddTreeNodes"/>
<s:url id="setInvDepRelation_url" value="/st/BINOLSTJCS04_setInvDepRelation"/>
<s:url id="deleteInvDepRelation_url" value="/st/BINOLSTJCS04_deleteInvDepRelation"/>
<s:url id="saveEditCom_url" value="/st/BINOLSTJCS04_saveEditCom"/>


<div id="JCS04_select" class="hide"><s:text
		name="global.page.all" /></div> 
<div class="panel-header">
     	<%-- ###问卷查询 --%>
       <div class="clearfix"> 
 	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>  
       	<cherry:show domId="BINOLSTJCS0401">
			 <a id="addBtn" class="delete right" onclick="binOLSTJCS04.popAddDialog();return false;">
              		<span class="ui-icon icon-add"></span>
              		<span class="button-text"><s:text name="JCS04_add"/></span>
             	 	</a>
		</cherry:show>
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
		<cherry:form id="mainForm" class="inline" onsubmit="binOLSTJCS04.search(); return false;">
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
                    	<label><s:text name="JCS04_brand"></s:text></label>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label><s:text name="JCS04_brand"></s:text></label>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
                	</p>
        			<p>
        				<%--部门名称 --%>
        				<label><s:text name="JCS04_departName"/></label>
	               		<s:textfield name="departName" cssClass="text" maxlength="50"/>
        			</p>
		        </div>      
        		<div class="column last" style="width:50%;"> 
        			<p>
	               		<%-- 仓库名称 --%>
	                  	<label><s:text name="JCS04_inventoryName"/></label>
	                  	<s:textfield name="inventoryName" cssClass="text" maxlength="60"/>
	                </p>      
	            </div>
              </div>
              <p class="clearfix">
           			<%-- 查询 --%>
           			<button class="right search" type="submit" onclick="binOLSTJCS04.search(); return false;" id="searchBut">
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
		</div>
		<div class="section-content">
		<div class="toolbar clearfix">
			<span class="left" id="deportOption">
				<cherry:show domId="BINOLSTJCS0403">
					 <a id="deleteBtn" class="delete">
                		<span class="ui-icon icon-delete"></span>
                		<span class="button-text"><s:text name="JCS04_delete"/></span>
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
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<%--选择 --%>
				<%-- <th><s:checkbox name="allSelect" id="checkAll" onclick="checkSelectAll(this)"/></th>--%>
				<th class="center"><input type="checkbox" name="allSelect" id="checkAll" onclick="binOLSTJCS04.checkSelectAll(this)"/></th>
				<%-- No. --%>
				<th class="center"><s:text name="No." /></th>
				<%-- 仓库   --%>
				<th class="center"><s:text name="JCS04_inventory" /></th>
				<%-- 部门   --%>
				<th class="center"><s:text name="JCS04_depart" /></th>
				<%-- 品牌--%>
				<th class="center"><s:text name="JCS04_brand" /></th>
				<%-- 默认仓库  --%>
				<th class="center"><s:text name="JCS04_defaultFlag" /></th>
				<%-- 备注  --%>
				<th class="center"><s:text name="JCS04_comments" /></th>
				<%-- 操作 --%>
				<th class="center"><s:text name="JCS04_option" /></th>
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

<%-- ================== 仓库部门管理绑定弹出框DIV START================= --%>
<div id="reaInvenDepartBind_main" class="hide">
	<div id="reaInvenDepartBind_body">
		<div id="reaInvenDepartBind_body_add">
			<div id="errorDiv1" class="actionError" style="display:none">
		        <ul>
		            <li><span id="errorSpan1"></span></li>
		        </ul>         
		    </div>
			<div id="errorDiv_dialog" class="actionError" style="display:none">
        		<ul>
            		<li><span id="errorSpan"><s:text name="JCS04_noChioce"></s:text></span></li>
        		</ul>         
    		</div>
			<p class="hide">
				<s:if test="%{brandInfoList.size()> 1}">
	                <label><s:text name="JCS04_brand"></s:text></label>
	                <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;" id="brandInfoId" ></s:select>
	             </s:if>
	             <s:else>
	                <label><s:text name="JCS04_brand"></s:text></label>
	                   <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;" id="brandInfoId"></s:select>      	
	            </s:else>
			</p>
			<table class="detail">
				<tbody>
					<tr>
						<th><s:text name="JCS04_inventory"/></th>
						<td>
				        	<s:select name="inventoryInfoId" id="inventoryInfoId" list="inventoryList" listKey="inventoryInfoId+'*'+testType" listValue="'('+inventoryCode+')'+inventoryName" cssStyle="width:300px;"></s:select>
						</td>
					</tr>
					<tr>
						<th><s:text name="JCS04_defaultFlag"/></th>
						<td>
							<input type="radio" name="defaultFlag" value="1" checked="checked"/>是
							<input type="radio" name="defaultFlag" value="0"/>否
						</td>
					</tr>
					<tr>
						<th><s:text name="JCS04_comments"/></th>
						<td>
							<s:textfield id="comments" cssClass="text" maxlength="100" cssStyle="width:300px;"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="box2-header clearfix"> 
		   <strong id="left_tree" class="left active" style="background:#E8E8C8">
       			<span class="ui-icon icon-flag"></span>
       			<s:text name="JCS04_choiceDepart"/>
       	   </strong>
       	   <span class="right">
				<input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 140px;" name="locationPosition" autocomplete="off">
				<a class="search" onclick="binOLSTJCS04.locationDepPosition(this);return false;">
					<span class="ui-icon icon-position"></span>
					<span class="button-text"><s:text name="JCS04_locationPosition"></s:text></span>
				</a>
			</span>
       	</div>
		<div class="jquery_tree">
    		<div class="jquery_tree treebox_left tree" id = "treeDemo" style="overflow: auto;background:#FCFCFC;min-height:350px;max-height:700px;height:auto">
     			 
      		</div>
    	</div>
	</div>
</div>

<%-- ================== 仓库部门管理绑定弹出框DIV END  ================= --%>

<%-- ================== 仓库部门编辑弹出框DIV START ==================== --%>
<div id="reaInvenDepartEdit_main" class="hide">
	<div id="reaInvenDepartEdit_body">
		<div id="errorDiv1" class="actionError" style="display:none">
	        <ul>
	            <li><span id="errorSpan1"></span></li>
	        </ul>         
	    </div>
	    <table class="detail">
		    <tbody>
				<tr>
					<th><s:text name="JCS04_inventory"/></th>
				    <td>
				    	<s:select name="inventoryInfoId" id="inventoryInfoId_edit" list="inventoryList" listKey="inventoryInfoId+'*'+testType" listValue="'('+inventoryCode+')'+inventoryName" cssStyle="width:300px;"></s:select>
					</td>
				</tr>
				<tr>
					<th><s:text name="JCS04_depart"/></th>
				    <td>
				    	<s:select name="organizationId" id="organizationId_edit" list="departList" listKey="id+'*'+testType" listValue="name" cssStyle="width:300px;"></s:select>
					</td>
				</tr>
				<tr>
					<th><s:text name="JCS04_defaultFlag"/></th>
					<td>
						<input type="radio" name="defaultFlag_edit" value="1" checked="checked"/><s:text name="JCS04_true"/>
						<input type="radio" name="defaultFlag_edit" value="0"/><s:text name="JCS04_false"/>
					</td>
				</tr>
				<tr>
					<th><s:text name="JCS04_comments"/></th>
					<td>
						<s:textfield id="comments_edit" cssClass="text" maxlength="100" cssStyle="width:300px;"/>
					</td>
				</tr>
			</tbody>
		</table>
		<input id="identityId_edit" type="hidden" value=""/>
	</div>
</div>
<%-- ================== 仓库部门编辑弹出框DIV END   ==================== --%>



<span id="search" style="display:none">${search_url}</span>
<span id="getAddTreeNodes" style="display:none">${getAddTreeNodes_url}</span>
<span id="setInvDepRelation" style="display:none">${setInvDepRelation_url}</span>
<span id="deleteInvDepRelation" style="display:none">${deleteInvDepRelation_url}</span>
<span id="saveEditCom" style="display:none">${saveEditCom_url}</span>

<input type="hidden" id="errmsg1" value='<s:text name="EMO00051"/>'/>

<input type="hidden" id="yes" value='<s:text name="JCS04_yes"/>'/>
<input type="hidden" id="dialogTitle" value='<s:text name="JCS04_dialogTitle"/>'/>
<input type="hidden" id="deleteTitle" value='<s:text name="JCS04_delete"/>'/>
<input type="hidden" id="deleteWarming" value='<s:text name="JCS04_deleteWarming"/>'/>
<input type="hidden" id="cancel" value='<s:text name="JCS04_cancel"/>'/>
<input type="hidden" id="onlyOne" value='<s:text name="JCS04_onlyOne"/>'/>
<input type="hidden" id="testWarming" value='<s:text name="JCS04_testWarming"/>'/>
<input type="hidden" id="officialWarming" value='<s:text name="JCS04_officialWarming"/>'/>

<div class="hide" id="dialogInit"></div>

</s:i18n>
<%-- ================== 员工弹出框导入 START ========================== --%>
<jsp:include page="/WEB-INF/jsp/mo/common/BINOLMOCM01.jsp" flush="true" />
<%-- ================== 员工弹出框导入 START ========================== --%>