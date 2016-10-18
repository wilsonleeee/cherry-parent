<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri = "/cherry-tags"%>
<%-- 保存URL --%>
<s:url id="save_Url" action="BINOLSTJCS06_save"/>
<%-- 一览URL --%>
<s:url id="search_Url" action="BINOLSTJCS06_search"/>
<s:i18n name="i18n.st.BINOLSTJCS06">
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
				            		   <span class="hide"><input name="orderNo" type="text" class="text" value="<s:property value='orderNo'/>" maxlength="10" onchange="BINOLSTJCS06.isInteger(this);return false;"  /></span>
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
				            			<%-- 以下代码废除 --%>
				            			<s:if test="validFlag == 12234254">
				            				<span>
						            			<cherry:show domId="BINOLSTJCS0605">
						            				<a class="delete" onclick="BINOLSTJCS06.edit('${status.index + 1}')">
								                		<span class="ui-icon icon-edit"></span>
								                		<span class="button-text"><s:text name="global.page.edit"/></span>
								               	 	</a>
						            			</cherry:show>
					            			</span>
					            			<span class="hide">
					            				<a class="delete" onclick="BINOLSTJCS06.save(this,'${save_Url}','${status.index + 1}');return false;">
							                		<span class="ui-icon icon-save-s"></span>
							                		<span class="button-text"><s:text name="global.page.save"/></span>
							               	 	</a>
					            				<a class="delete" onclick="BINOLSTJCS06.search(false);return false;">
							                		<span class="ui-icon icon-delete"></span>
							                		<span class="button-text"><s:text name="global.page.cancle"/></span>
							               	 	</a>
					            			</span>
				            			</s:if>
										
			            	    	</td>
								   
				            	</tr>
				            </s:iterator>
</s:i18n>
