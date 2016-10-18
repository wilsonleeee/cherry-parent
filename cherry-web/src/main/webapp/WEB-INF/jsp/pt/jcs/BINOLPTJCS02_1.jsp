<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri = "/cherry-tags"%>
<%-- 保存URL --%>
<s:url id="save_Url" action="BINOLPTJCS02_save"/>
<%-- 一览URL --%>
<s:url id="search_Url" action="BINOLPTJCS02_search"/>
<%-- ========================= 产品扩展属性编辑返回页 ============================= --%>
<s:i18n name="i18n.pt.BINOLPTJCS02">
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
				            	    	<%-- <span class="hide"><input type="hidden" name="viewType" value="<s:property value='viewType'/>"/></span> --><%-- viewType  --%>
				            	    	<span class="hide"><input type="hidden" name="propertyKey" value="<s:property value='propertyKey'/>"/></span><%-- 属性key  --%>
			            	    	 	<span class="hide"><input type="hidden" name="updateTime" value="<s:property value='updateTime'/>"/></span><%-- 更新时间  --%>
									 	<span class="hide"><input type="hidden" name="modifyCount" value="<s:property value='modifyCount'/>"/></span><%-- 更新次数 --%>
				            	    </td>
				            	    <td><span>${status.index + 1}</span></td>
				            		<td>
				            		    <span> 
					            		    <s:if test='viewType != "text"'>
					            		    	<a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;"><s:property value="propertyName"/></a>
					            		    </s:if>
					            		    <s:else>
					            		    	<s:property value="propertyName"/>
					            		    </s:else>
				            		    </span>
				            		    <span class="hide"><input name="propertyName" type="text" class="text" value="<s:property value='propertyName'/>" style="width:105px" maxlength="30" />
	                                          <span class="highlight"><s:text name="global.page.required"/></span></span><%-- 产品扩展属性名称 --%>
	                                </td>
				            		<td>
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
			            					<s:select name="viewType" list="#{'text':'文本框','radio':'单选框','select':'下拉列表','checkbox':'复选框'}" 
			            					listKey="key" listValue="value" value="viewType"/>
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
</s:i18n>