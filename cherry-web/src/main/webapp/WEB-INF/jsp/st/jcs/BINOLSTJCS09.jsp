<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/jcs/BINOLSTJCS09.js"></script>
<s:url id="search_url" value="/st/BINOLSTJCS09_search"/>
<s:url id="adddepot_Url" action="BINOLSTJCS09_add"/>
<s:url id="adddepot1_Url" action="BINOLSTJCS09_add1"/>
<s:url id="editdepot_Url" action="BINOLSTJCS09_edi"/>
<s:url id="deletedepot_Url" action="BINOLSTJCS09_delete"/>
<s:url id="editdepot1_Url" action="BINOLSTJCS09_edit"/>
<s:url id="getLogiDepotByAjax_Url" action="BINOLSTJCS09_getLogiDepotByAjax"></s:url>
<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
		<a id="add1_depot" href="${adddepot1_Url}"></a>
		<a id="add_depot" href="${adddepot_Url}"></a>
		<a id="edit_depot" href="${editdepot_Url}"></a>
		<a id="edit1_depot" href="${editdepot1_Url}"></a>
		<a id="delete_depot" href="${deletedepot_Url}"></a>
		<a id="getLogiDepotByAjax" href="${getLogiDepotByAjax_Url}"></a>
</div>
 <!-- 下发url -->
<s:url id="issued_Url" action="BINOLSTJCS09_issued"/>
<s:i18n name="i18n.st.BINOLSTJCS09">
	<div class="panel-header">
		<div class="clearfix">
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>  
	     	<cherry:show domId="BINOLSTJCS09ADD">
		       	<a href="#" class="delete right"  id="add" onclick="BINOLSTJCS09.popAddDialog();return false;">
			 		<span class="ui-icon icon-add"></span>
			 		<span class="button-text"><s:text name="JCS09_add"/></span>
			 	</a>
		 	</cherry:show>
	   	</div>
	</div>
	<div id="actionResultDisplay"></div>
 <div id="errorMessage"></div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" style="display:none">
        <div class="actionError">
            <ul>
                <li><span id="errorSpan2"></span></li>
            </ul>
        </div>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
        <div class="box">
        	<div class="box-header">
	           	<strong style="font-size:13px">
	           		<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
	           	</strong>
	        </div>
	        <cherry:form id="mainForm" submittoken="false" onsubmit="BINOLSTJCS09.search();return false;">
		        <div class="box-content clearfix">
		        	<div class="column" style="width:49%;">
		        		<div>
			        		<p>
			        			<s:if test="brandInfoList.size() > 1">
			        				<%-- 所属品牌 --%>
									<label style="width:80px;"><s:text name="JCS09_brand"></s:text></label>
									<s:text name="JCS09_select" id="JCS09_select"/>
				                    <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="BINOLSTJCS09.search();return false;" headerKey="" headerValue="%{#JCS09_select}" cssStyle="width:120px;"></s:select>
			        			</s:if>
			        			<s:else>
			        				<%-- 所属品牌 --%>
									<label style="width:80px;"><s:text name="JCS09_brand"></s:text></label>
				                    <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="BINOLSTJCS09.search();return false;" headerKey="" cssStyle="width:120px;"></s:select>
			        			</s:else>
			        		</p>
		        		</div>
		        		<div id="inventoryNameId">
			        		<p>
			        			<%-- 逻辑仓库--%>
								<label style="width:80px;"><s:text name="JSC09_logiDepot"></s:text></label>
								<s:textfield name="inventoryName" cssClass="text" maxlength="30" id="inventoryName"/>
			               </p>
		        		</div>
		        	</div>
		        	<div class="column last" style="width:50%;">
		        		<div id="logicTypeId" >
			        		<p>
			        			<%-- 业务所属 --%>
								<label style="width:80px;"><s:text name="JCS09_logicType"></s:text></label>
								<s:text name="JCS09_select" id="JCS09_select"/>
			                    <s:select name="logicType" list="#application.CodeTable.getCodes('1181')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#JCS09_select}" cssStyle="width:120px;" onchange="BINOLSTJCS09.getBusinessType('mainForm');"></s:select>
			        		</p>
		        		</div>
		        		<div>
			        		<p>
			        			<%-- 产品类型 --%>
								<label style="width:80px;"><s:text name="JCS09_productType"></s:text></label>
								<s:text name="JCS09_select" id="JCS09_select"/>
			                    <s:select name="productType" list="#application.CodeTable.getCodes('1134')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#JCS09_select}" cssStyle="width:120px;"></s:select>
			        		</p>
		        		</div>
		        	</div>
		        </div>
	        </cherry:form>
	        <p class="clearfix">
        		<%-- 查询 --%>
        		<button class="right search" type="submit" onclick="BINOLSTJCS09.search();return false;" id="searchBut">
        			<span class="ui-icon icon-search-big"></span>
        			<span class="button-text" style="font-size:15px"><s:text name="global.page.search"/></span>
        		</button>
       		</p> 
    	</div>
	 	<div id="section" class="section">
	        <div class="section-header clearfix">
	        	<strong> 
					<span class="ui-icon icon-ttl-section-search-result"></span> 
					<s:text name="global.page.list" />
		 		</strong>
	        </div>
        	<div class="section-content">
	        	<div class="toolbar clearfix">
				 	<%-- 删除、下发功能废除，数据在每次数据变更时下发
		        	<span class="left" id="deportOption">
		        	
				 	<cherry:show domId="BINOLSTJCS09DEL">
				       	<a href="#" class="delete"  id="delete" onclick="BINOLSTJCS09.delMsgInit();return false;">
					 		<span class="ui-icon icon-delete"></span>
					 		<span class="button-text"><s:text name="JCS09_delete"/></span>
					 	</a>
				 	</cherry:show>
				 	<cherry:show domId="BINOLSTJCS09ISS">
				       	<a href="#" class="delete"  id="issued" onclick="BINOLSTJCS09.issued('${issued_Url}');return false;">
					 		<span class="ui-icon icon-down"></span>
					 		<span class="button-text"><s:text name="JCS09_issued"/></span>
					 	</a>
				 	</cherry:show>
				 	</span>
				 	--%>
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
			             	<%-- 
							<th><input type="checkbox" name="allSelect" id="checkAll" onclick="BINOLSTJCS09.CheckAllSelect(this)"/></th>
			                --%>
			                <%-- NO. --%>
			                <th class="center"><s:text name="JCS09_num"/></th>
			                <%-- 业务所属  --%>
			                <th class="center"><s:text name="JCS09_logicType"/></th>
			                <%-- 产品类型 --%>
			                <th class="center"><s:text name="JCS09_productType"/></th>
			                <%-- 业务类型--%>
			                <th class="center"><s:text name="JCS09_businessType"/></th>
			                <%-- 子类型--%>
			                <th class="center"><s:text name="JSC09_subType"/></th>
			                <%-- 逻辑仓库  --%>
			                <th class="center"><s:text name="JCS09_logicInventoryInfoId"/></th>
			                <%-- 逻辑仓库类型  --%>
			                <%-- <th class="center"><s:text name="JSC09_type"/></th> --%>
			                <%--优先级--%>
			                <th class="center"><s:text name="JCS09_configOrder"/></th> 
			                <%-- 备注 --%>
			                <th class="center"><s:text name="JCS09_comments"/></th>
			                <%-- 操作 --%>
			                <th class="center"><s:text name="JSC09_opration"/></th>
			              </tr>
			            </thead>
          			</table>
        	</div>
        </div>
    </div>	
<div class="hide" id="dialogInit"></div>
    <div class="hide">
        <div id="addMsgTitle"><s:text name="JCS09_addTitle" /></div>
        <div id="updateMsgTitle"><s:text name="JCS09_updateTitle" /></div>
        <div id="deleteMsgTitle"><s:text name="JCS09_deleteTitle" /></div>
        <div id="deleteMsgText"><p class="message"><span><s:text name="JCS09_deleteText" /></span></p></div>
        <div id="saveSuccessTitle"><s:text name="save_success" /></div>
        <div id="dialogConfirm"><s:text name="JCS09_yes" /></div>
        <div id="dialogCancel"><s:text name="JCS09_cancel" /></div>
        <div id="dialogClose"><s:text name="JCS09_close" /></div>
        <div id="dialogInitMessage"><s:text name="dialog_init_message" /></div>
        <div id="deleteMsgText"><p class="message"><span><s:text name="delete_success" /></span></p></div>
    </div>
<table id = "hide_table" width="100%" class="detail hide" border="1" cellspacing="0" cellpadding="0">
  <tr id="subType_tr">
    <th><s:text name="JSC09_subType"/></th>
    <s:text name="JSC09_pleaseSelect" id="JSC09_pleaseSelect"></s:text>
    <td><span><s:select cssStyle="width:150px" name="subType" id="subType" list="#application.CodeTable.getCodes('1168')" listKey="CodeKey" listValue="Value" /></span></td>
  </tr>
  <tr>
    <th><s:text name="JCS09_inOutFlag"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td><span><s:select name="inOutFlag" list="#application.CodeTable.getCodes('1025')" listKey="CodeKey" listValue="Value"/></span></td>
  </tr>
  <%-- 后台业务类型 --%>
  <tr id="businessType_tr_0">
    <th><s:text name="JCS09_businessType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td><span><s:select cssStyle="width:150px" name="businessType" list="#application.CodeTable.getCodes('1133')" listKey="CodeKey" listValue="Value" onchange="BINOLSTJCS09.getSubType('addForm');"/></span></td>
  </tr>
  <%-- 终端业务业务类型 --%>
  <tr id="businessType_tr_1">
    <th><s:text name="JCS09_businessType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td><span><s:select cssStyle="width:150px" name="businessType" list="#application.CodeTable.getCodes('1184')" listKey="CodeKey" listValue="Value" onchange="BINOLSTJCS09.getSubType('addForm');"/></span></td>
  </tr>
  
  <tr id="logicInvId_tr_1">
  	<th><s:text name="JCS09_logicInventoryInfoId"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td>
    	<span>
    		<s:select cssStyle="width:70px" name="type" id="type" list="#application.CodeTable.getCodes('1143')" listKey="CodeKey" listValue="Value"></s:select>
    		<select style="width:150px" name="logicInvId" id="logicInvId"></select>
    	</span>
    </td>
  </tr>
  <tr id="logicInvId_tr_2">
  	<th><s:text name="JCS09_logicInventoryInfoId"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td>
    	<span>
    		<select style="width:150px" name="logicInvId" id="logicInvId" ></select>
    	</span>
    </td>
  </tr>
</table>
<%-- 查询条件--业务类型 --%>
<div id="hide_div_sel" class="detail hide"> 
	<div id="businessType_0" >
		<p>
		<%-- 后台业务类型 --%>
			<label style="width:80px;"><s:text name="JCS09_businessType"></s:text></label>
			<s:text name="JCS09_select" id="JCS09_select"/>
            <s:select name="businessType" list="#application.CodeTable.getCodes('1133')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#JCS09_select}" cssStyle="width:120px;"></s:select>
		</p>
	</div>
	<div id="businessType_1" >
		<p>
			<%-- 终端业务类型 --%>
			<label style="width:80px;"><s:text name="JCS09_businessType"></s:text></label>
			<s:text name="JCS09_select" id="JCS09_select"/>
            <s:select name="businessType" list="#application.CodeTable.getCodes('1184')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#JCS09_select}" cssStyle="width:120px;"></s:select>
		</p>
	</div>
	<div id="layout_empty">
		<p>
			<%-- 占位布局用 与业务无关--%>
			<label><s:text name=""></s:text></label>
			<label><s:text name=""></s:text></label>
	      </p>
	</div>
</div>
</s:i18n>
   <%-- ================== dataTable共通导入 START ======================= --%>
		<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
		<%-- ================== dataTable共通导入    END  ======================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EMO00036" value='<s:text name="EST00014"/>'/>
</div>
