<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri = "/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/st/jcs/BINOLSTJCS06.js"></script>

<s:url id="search_Url" action="BINOLSTJCS06_search"/>
<span class="hide" id="search">${search_Url}</span>

<%-- 新增初始化 --%>
<s:url id="adddepot_Url" action="BINOLSTJCS06_add"/>
<%-- 新增保存--%>
<s:url id="adddSave_Url" action="BINOLSTJCS06_addSave"/>
<%-- 编辑初始化--%>
<s:url id="editdepot_Url" action="BINOLSTJCS06_edit"/>
<%-- 编辑保存--%>
<s:url id="editSave_Url" action="BINOLSTJCS06_editSave"/>

<div class="hide">
		<a id="add_depot" href="${adddepot_Url}"></a>
		<a id="addSave_depot" href="${adddSave_Url}"></a>
		<a id="edit_depot" href="${editdepot_Url}"></a>
		<a id="editSave_depot" href="${editSave_Url}"></a>
</div>

<%-- 品牌ID --%>
<s:set id="brandId" value="brandInfoId" />
<s:i18n name="i18n.st.BINOLSTJCS06">
	<div class="panel-header">
		<div class="clearfix">
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>  
	     	<cherry:show domId="BINOLSTJCS0601">
		       	<a href="#" class="delete right"  id="add" onclick="BINOLSTJCS06.popAddDialog();return false;">
			 		<span class="ui-icon icon-add"></span>
			 		<span class="button-text"><s:text name="JCS06_add"/></span>
			 	</a>
		 	</cherry:show>
	   	</div>
	</div>
	 <div id="actionResultDisplay"></div>
	 <div id="errorMessage"></div>    
	 <%-- ================== 错误信息提示 START ======================= --%>
     <div style="display: none" id="errorMessageTemp1">
	    <div class="actionError">
	       <ul><li><span id="errorSpan"><s:text name="JCS06.noCheckedWarm"/></span></li></ul>         
	    </div>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
	    <div class="box3">
			<label>
			<s:text name="JCS06.brandName"/>
			</label>
			<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"
			onchange="BINOLSTJCS06.search(false);return false;" value="%{brandInfoId}" />
        </div>
        <div id="section" class="section">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="JCS06.results_list"/>
        	</strong>
        </div>
        <div class="section-content">
           <div class="section-content" style="overflow-x: auto;overflow-y:hidden;">
	        	<cherry:form id="mainForm">
	        		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
			            <thead>
			              <tr>        
			                <%-- 废除checkbox框     
			                <th> <input type="checkbox" id="checkAll" onclick="BINOLSTJCS06.checkSelectAll(this)"></input></th>
			                --%> 
			                <th ><span>NO.</span></th>
			                <%-- 仓库类型编码 --%>
			                <th  class="center"><s:text name="JCS06.LogicInventoryCode"/></th>
			                <%-- 仓库类型名称 --%>
			                <th  class="center"><s:text name="JCS06.InventoryNameCN"/></th>
			                <%-- 仓库类型名称 英文名--%>
			                <th  class="center"><s:text name="JCS06.InventoryNameEN"/></th>
			                <%-- 描述 --%>
			                <th  class="center"><s:text name="JCS06.Comments"/></th>
			                <%-- 类型 --%>
			                <th  class="center"><s:text name="JCS06.Type"/></th>
			                <%-- 排序 --%>
			                <th  class="center"><s:text name="JCS06.OrderNO"/></th>
			                <%-- 默认仓库 --%>
			                <th  class="center"><s:text name="JCS06.DefaultFlag"/></th>
			                <%-- 有效区分 --%>
			                <th  class="center"><s:text name="JCS06.ValidFlag"/></th>
			                <%-- 操作 --%>
			                <th  class="center"><s:text name="JCS06.Operate"/></th>
			              </tr>
			            </thead>   
			            <tbody id="dataTable">
			            	<s:set id="size" value="logInvList.size()"/>
			            	<%-- flag --%>
			            	<s:set id="optFlag" value="0"/>
				            <s:iterator value="logInvList" status="status">
				            	<tr id="${status.index + 1}" class="<s:if test='#status.odd == true'>odd</s:if><s:else>even</s:else>" >
				            	    <%-- 废除checkbox框
				            	    <td>
				            	        <input type="checkbox" onclick="BINOLSTJCS06.checkBoxClick()"></input>
				            	    	<input type="hidden" name="logInvId" value="<s:property value='logInvId'/>"/>
			            	    	 	<span class="hide"><input type="hidden" name="updateTime" value="<s:property value='updateTime'/>"/></span>
									 	<span class="hide"><input type="hidden" name="modifyCount" value="<s:property value='modifyCount'/>"/></span>
				            	    </td>
				            	    --%>
				            	    <td>
				            	    	<span>${status.index + 1}</span>
				            	    	<input type="hidden" name="logInvId" value="<s:property value='logInvId'/>"/><%-- 逻辑仓库ID --%>
			            	    	 	<span class="hide"><input type="hidden" name="updateTime" value="<s:property value='updateTime'/>"/></span><%-- 更新时间  --%>
									 	<span class="hide"><input type="hidden" name="modifyCount" value="<s:property value='modifyCount'/>"/></span><%-- 更新次数 --%>
				            	    </td>
				            		<td>
				            			<span><s:property value="logInvCode"/></span>
										<span class="hide"><input name="logInvCode" type="text" class="text" value="<s:property value='logInvCode'/>" style="width:80px" maxlength="10"/>
										      <span class="highlight"><s:text name="global.page.required"/></span></span><%-- 仓库编码 --%>
				            		</td>
				            		<td>
				            		    <span><s:property value="logInvNameCN"/></span>
				            		    <span class="hide"><input name="logInvNameCN" type="text" class="text" value="<s:property value='logInvNameCN'/>" style="width:105px" maxlength="30" />
	                                          <span class="highlight"><s:text name="global.page.required"/></span></span><%-- 仓库名称 --%>
	                                </td>
				            		<td>
					            		<span><s:property value="logInvNameEN"/></span>
					            		<span class="hide"><input name="logInvNameEN" type="text" class="text" value="<s:property value='logInvNameEN'/>" style="width:105px" maxlength="30" />
		                                </span><%-- 仓库名称英文 --%>
				            		</td>
				            		<td>
				            		   <span><s:property value="comments"/></span>
				            		   <span class="hide"><input name="comments" type="text" class="text" value="<s:property value='comments'/>" maxlength="100" /></span>
				            		</td>
				            		<td class="center">
				            		    <span>
									 		<s:if test="type == 0">
					                              <s:text name="JCS06.BackType"></s:text>
					            			</s:if>
					            			<s:if test="type == 1">
					                              <s:text name="JCS06.FrontType"></s:text>
					            			</s:if>
				            			</span>
				            			<span class="hide"><s:select name="type" list="#application.CodeTable.getCodes('1143')"
			                                    listKey="CodeKey" listValue="Value"  value="type"/></span>
								    </td>
				            		<td class="center">
				            		   <span><s:property value="orderNo"/></span>
				            		   <span class="hide"><input name="orderNo" type="text" class="text" value="<s:property value='orderNo'/>" maxlength="10" onchange="BINOLSTJCS06.isInteger(this);return false;" /></span>
				            		</td>
				            		<td class="center">
				            		    <span>
									 		<s:if test="defaultFlag == 0">
					                            <s:property value='#application.CodeTable.getVal("1135",defaultFlag)'/>
					            			</s:if>
					            			<s:if test="defaultFlag == 1">
					            				 <s:property value='#application.CodeTable.getVal("1135",defaultFlag)'/>
					            			</s:if>
				            			</span>
				            			<span class="hide"><s:select name="defaultFlag" list="#application.CodeTable.getCodes('1135')"
			                                   listKey="CodeKey" listValue="Value" value="defaultFlag"/></span>
								    </td>
								    <td class="center">
										<s:if test="validFlag == 0">
				                             <span class="ui-icon icon-invalid"></span>
				            			</s:if>
				            			<s:if test="validFlag == 1">
				            				<span class="ui-icon icon-valid"></span>
				            			</s:if>
								    </td>
		                            <td class="center">
				            			<%-- 编辑URL --%>
										<span>
											<cherry:show domId="BINOLSTJCS0605">
										       	<a href="#" class="delete"  id="<s:property value='logInvId'/>" onclick="BINOLSTJCS06.popEditDialog('<s:property value="logInvId"/>');return false;">
											 		<span class="ui-icon icon-edit"></span>
											 		<span class="button-text"><s:text name="global.page.edit"/></span>
											 	</a>
										 	</cherry:show>
										</span>
			            	    	</td>
								   
				            	</tr>
				            </s:iterator>
			              </tbody>        
                    </table>
	        	</cherry:form>
	        </div>
           </div>
        </div>
    </div>
    
	<div class="hide" id="dialogInit"></div>
      <div style="display: none;">
      <div id="disableTitle"><s:text name="JCS06.disable"/></div>
      <div id="confirIsDisable"><s:text name="JCS06.confirIsDisable"/></div>
      <div id="enableTitle"><s:text name="JCS06.enable"/></div>
      <div id="confirIsEnable"><s:text name="JCS06.confirIsEnable"/></div>
      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      <div id="addMsgTitle"><s:text name="JCS06_addTitle" /></div>
      <div id="saveSuccessTitle"><s:text name="save_success_message" /></div>
      <div id="dialogClose"><s:text name="JCS06_close" /></div>
      <div id="updateMsgTitle"><s:text name="JCS06_updateTitle" /></div>
    </div>
</s:i18n>
