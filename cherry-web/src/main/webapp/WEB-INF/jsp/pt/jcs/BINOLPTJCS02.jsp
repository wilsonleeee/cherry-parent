<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri = "/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS02.js"></script>

<s:url id="search_Url" action="BINOLPTJCS02_search"/>
<span class="hide" id="search">${search_Url}</span>
<!-- 编辑url -->
<s:url id="edit_url" action="BINOLPTJCS02_edit"/>
 <!-- 保存url -->
<s:url id="save_Url" action="BINOLPTJCS02_save"/>
<!-- 停用或启用url -->
<s:url id="disOrEnable_Url" action="BINOLPTJCS02_disOrEnable"/>
<span class="hide" id="disOrEnable">${disOrEnable_Url}</span>

<%-- 编辑或添加区分 --%>
<input type="hidden" id="flag" value="0"></input>

<s:i18n name="i18n.pt.BINOLPTJCS02">
	<div class="panel-header">
		<div class="clearfix">
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
	   	</div>
	</div>
	 <div id="actionResultDisplay"></div>
	 <div id="errorMessage"></div>    
	 <%-- ================== 错误信息提示 START ======================= --%>
     <div style="display: none" id="errorMessageTemp1">
	    <div class="actionError">
	       <ul><li><span><s:text name="JCS02.noCheckedWarm"/></span></li></ul>         
	    </div>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
        <div id="section" class="section">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="JCS02.results_list"/>
        	</strong>
        </div>
        
        <div class="section-content">
          <div class="toolbar clearfix">
           	<span class="left" id="extPropOption">
           		<cherry:show domId="BINOLPTJCS0201">
	                <a id="add_ExtProp" href="" class="delete" onclick="BINOLPTJCS02.addLine('#dataTable','#newLine');return false;">
	                   <span class="ui-icon icon-add"></span>
	                   <span class="button-text"><s:text name="global.page.add"/></span>
	                </a>
                </cherry:show>
                <cherry:show domId="BINOLPTJCS0202">
                <a id="disable_extProp"  href="" class="delete" onclick="BINOLPTJCS02.confirmInit('0');return false;">
                   <span class="ui-icon icon-disable"></span>
                   <span class="button-text"><s:text name="global.page.disable"/></span>
                </a>
                </cherry:show>
                <cherry:show domId="BINOLPTJCS0203">
                <a id="enable_extProp"  href="" class="delete" onclick="BINOLPTJCS02.confirmInit('1');return false;">
                   <span class="ui-icon icon-enable"></span>
                   <span class="button-text"><s:text name="global.page.enable"/></span>
                </a>
                </cherry:show>
            </span>
           </div>
           <div class="section-content" style="overflow-x: auto;overflow-y:hidden;">
	        	<cherry:form id="mainForm">
	        		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
			            <thead>
			              <tr>              
			                <th> <input type="checkbox" id="checkAll" onclick="BINOLPTJCS02.checkSelectAll(this)"></input></th>
			                <th ><span>NO.</span></th>
			                <%-- 产品属性名称 --%>
			                <th  class="center"><s:text name="JCS02.propertyName"/></th>
			                <%-- 产品属性名称(英文) --%>
			                <th  class="center"><s:text name="JCS02.propertyNameForeign"/></th>
			                <%-- 扩展属性画面控件类型 --%>
			                <th  class="center"><s:text name="JCS02.viewType"/></th>
			                <%-- 有效区分 --%>
			                <th  class="center"><s:text name="JCS02.ValidFlag"/></th>
			                <%-- 操作 --%>
			                <th  class="center"><s:text name="JCS02.Operate"/></th>
			              </tr>
			            </thead>   
			            <tbody id="dataTable">
			            	<s:set id="size" value="extPropertyList.size()"/>
			            	<%-- flag --%>
			            	<s:set id="optFlag" value="0"/>
				            <s:iterator value="extPropertyList" status="status">
								<s:url id="detailsUrl" value="/pt/BINOLPTJCS02_extDefValInit.action">
									<s:param name="extendPropertyID">${extendPropertyID}</s:param>
									<s:param name="viewType">${viewType}</s:param>
									<s:param name="groupID">${groupID}</s:param>
								</s:url>
				            	<tr id="${status.index + 1}" class="<s:if test='#status.odd == true'>odd</s:if><s:else>even</s:else>" >
				            	    <td>
				            	        <input type="checkbox" onclick="BINOLPTJCS02.checkBoxClick()"></input>
				            	    	<input type="hidden" name="extendPropertyID" value="<s:property value='extendPropertyID'/>"/><%-- 扩展属性ID --%>
			            	    	 	<span class="hide"><input type="hidden" name="groupID" value="<s:property value='groupID'/>"/></span><%-- checkbox groupID  --%>
			            	    	 	<span class="hide"><input type="hidden" name="groupName" value="<s:property value='groupName'/>"/></span><%-- checkbox groupName  --%>
			            	    	 	<span class="hide"><input type="hidden" name="groupNameForeign" value="<s:property value='groupNameForeign'/>"/></span><%-- checkbox groupNameForeign  --%>
			            	    	 	<%-- <span class="hide"><input type="hidden" name="viewType" value="<s:property value='viewType'/>"/></span> --%><%-- viewType  --%>
			            	    	 	<span class="hide"><input type="hidden" name="propertyKey" value="<s:property value='propertyKey'/>"/></span><%-- 属性key  --%>
			            	    	 	<span class="hide"><input type="hidden" name="updateTime" value="<s:property value='updateTime'/>"/></span><%-- 更新时间  --%>
									 	<span class="hide"><input type="hidden" name="modifyCount" value="<s:property value='modifyCount'/>"/></span><%-- 更新次数 --%>
				            	    </td>
				            	    <td><span>${status.index + 1}</span></td>
				            		<td>
				            		   <!-- <span><s:property value="propertyName"/></span> --> 
				            		    <span> 
				            		    <s:if test='viewType != "text"'>
				            		    	<a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;"><s:property value="propertyName"/></a>
				            		    </s:if>
				            		    <s:else>
				            		    	<s:property value="propertyName"/>
				            		    </s:else>
				            		    </span>
				            		    <span class="hide"><input name="propertyName" type="text" class="text" value="<s:property value='propertyName'/>" style="width:105px" maxlength="30" />
	                                          <span class="highlight"><s:text name="global.page.required"/></span> </span><%-- 产品扩展属性名称 --%>
	                                </td>
				            		<td>
				            		   <!-- <span><s:property value="propertyNameForeign"/></span> --> 
				            		    <span> 
				            		    	<s:property value="propertyNameForeign"/>
				            		    </span>
				            		    <span class="hide"><input name="propertyNameForeign" type="text" class="text" value="<s:property value='propertyNameForeign'/>" style="width:105px" maxlength="30" />
	                                          <%--<span class="highlight"><s:text name="global.page.required"/></span>--%></span><%-- 产品扩展属性名称(中文) --%>
	                                </td>
				            		<td class="center">
				            		    <span>
									 		<s:if test='viewType == "text"'>
					                              <s:text name="JCS02.validFlagText"></s:text>
					            			</s:if>
					            			<s:if test='viewType == "radio"'>
					                              <s:text name="JCS02.validFlagRadio"></s:text>
					            			</s:if>
					            			<s:if test='viewType == "select"'>
					                              <s:text name="JCS02.validFlagSelect"></s:text>
					            			</s:if>
					            			<s:if test='viewType == "checkbox"'>
					                              <s:text name="JCS02.validFlagCheckBox"></s:text>
					            			</s:if>
				            			</span>
				            			<span class="hide">
				            				<s:select name="viewType" 
				            				list="#{'text':'文本框','radio':'单选框','select':'下拉列表','checkbox':'复选框'}" listKey="key" listValue="value"
								 		value="viewType"/>
	                                    </span>
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
				            			<s:if test="validFlag == 1">
				            				<span>
						            			<cherry:show domId="BINOLPTJCS0204">
						            				<a class="delete" onclick="BINOLPTJCS02.edit('${status.index + 1}')">
								                		<span class="ui-icon icon-edit"></span>
								                		<span class="button-text"><s:text name="global.page.edit"/></span>
								               	 	</a>
						            			</cherry:show>
					            			</span>
					            			<span class="hide">
					            				<a class="delete" onclick="BINOLPTJCS02.save(this,'${save_Url}','${status.index + 1}');return false;">
							                		<span class="ui-icon icon-save"></span>
							                		<span class="button-text"><s:text name="global.page.save"/></span>
							               	 	</a>
					            				<a class="delete" onclick="BINOLPTJCS02.search(false);return false;">
							                		<span class="ui-icon icon-delete"></span>
							                		<span class="button-text"><s:text name="global.page.cancle"/></span>
							               	 	</a>
					            			</span>
				            			</s:if>
										
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
    <div class="hide">
		<table>
			<tbody id="newLine">
				<tr>
				    <td><input type="checkbox" onclick="BINOLPTJCS02.checkBoxClick()"></input></td>
				    <td><span></span><%-- NO. --%></td>
			  		<td>
			  			<span><input name="propertyName" type="text" class="text" style="width:80px" maxlength="10" /><span 
			  			class="highlight"><s:text name="global.page.required"/></span></span><%-- 产品属性名称 --%>
			  		</td>
			  		<td>
			  			<span><input name="propertyNameForeign" type="text" class="text" style="width:80px" maxlength="10" />
			  			<%-- <span class="highlight"><s:text name="global.page.required"/></span>--%></span><%-- 产品属性名称（英文） --%>
			  		</td>
					<td class="center">
						<span><s:select name="viewType" 
						list="#{'text':'文本框','radio':'单选框','select':'下拉列表','checkbox':'复选框'}" listKey="key" listValue="value"
								 value="text"/></span><%-- 扩展属性画面控件类型--%>
					</td>
					<td class ="center"><span class="ui-icon icon-valid"></span></td><%-- 有效区分--%>
					<td class="center">
					    <span>
							<a class="delete" onclick="BINOLPTJCS02.save('#newLine','${save_Url}','-1');return false;">
		                		<span class="ui-icon icon-save-s"></span>
		                		<span class="button-text"><s:text name="global.page.save"/></span>
		               	 	</a>
            				<a class="delete" onclick="BINOLPTJCS02.search(false);return false;">
		                		<span class="ui-icon icon-delete"></span>
		                		<span class="button-text"><s:text name="global.page.cancle"/></span>
		               	 	</a>
				       </span>
					</td>
			    </tr>
		    </tbody>
		</table>
	</div>
	  <div class="hide" id="dialogInit"></div>
      <div style="display: none;">
      <div id="disableTitle"><s:text name="JCS02.disable"/></div>
      <div id="confirIsDisable"><s:text name="JCS02.confirIsDisable"/></div>
      <div id="enableTitle"><s:text name="JCS02.enable"/></div>
      <div id="confirIsEnable"><s:text name="JCS02.confirIsEnable"/></div>
      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      </div>
</s:i18n>