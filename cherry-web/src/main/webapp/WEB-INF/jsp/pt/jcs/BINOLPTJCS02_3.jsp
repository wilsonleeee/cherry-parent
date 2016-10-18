<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 语言环境 --%>
<s:i18n name="i18n.pt.BINOLPTJCS02">
<s:url id="saveEditExtDefVal_url" value="/pt/BINOLPTJCS02_saveEditExtDefVal"></s:url>
<s:url id="doBack_url" value="/pt/BINOLPTJCS02_doBack"></s:url>


<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="pro.manage"/> &gt; <s:text name="pro.extDefVal"/><%-- 导航栏 --%>
		       </span>
		    </div>
	  	</div>
	  	<div id="actionResultDisplay"></div>
	  	<div id="actionResultDisplay" class="hide">
			<div id="actionResultDiv" class="actionSuccess">
		  		<ul class="actionMessage">
	                <li><span><s:text name="JSC02_optionSuccess"></s:text></span></li>
				</ul>
			</div>
		</div>
		<div id="errorDiv" class="actionError" style="display:none">
            <ul>
                <li><span id="errorSpan"></span></li>
            </ul>         
        </div>
	  	<cherry:form id="mainForm" method="post" class="inline" csrftoken="false" action="BINOLPTJCS02_extDefValInit">
	        <s:hidden name="extendPropertyID" value="%{extendPropertyID}"/>
	        <input type="hidden" id="parentCsrftoken" name="csrftoken"/>
        </cherry:form>
	  	<form id="extDefValForm" class="inline" >
	        <s:hidden name="extendPropertyID" value="%{extPropertyMap.extendPropertyID}"/>
	        <s:hidden name="organizationInfoID" value="%{extPropertyMap.organizationInfoID}"/>
	        <s:hidden name="brandInfoID" value="%{extPropertyMap.brandInfoID}"/>
	        <s:hidden name="extendedTable" value="%{extPropertyMap.extendedTable}"/>
	        <s:hidden name="groupID" value="%{extPropertyMap.groupID}"/>	        
	        <s:hidden name="groupName" value="%{extPropertyMap.groupName}"/>
	        <s:hidden name="groupNameForeign" value="%{extPropertyMap.groupNameForeign}"/>
	        <s:hidden name="viewType" id="viewType" value="%{extPropertyMap.viewType}"/>	         
	        <input type="hidden" id="parentCsrftoken1" name="csrftoken"/>
        </form>
        
	  	<div class="panel-content">
	  		<div class="section">
	            <div class="section-header">
	            	<strong>
	            		<span class="ui-icon icon-ttl-section-info"></span>
	            		<s:text name="global.page.title"/><%-- 基本信息 --%>
	            	</strong>
	            </div>
	            <div class="section-content" id="shwoMainInfo">
	                <table class="detail" cellpadding="0" cellspacing="0">
		             	<tr>
			                <th><s:text name="JCS02.propertyName"/></th><%-- 扩展属性名称 --%>
			                <td><span><s:property value="extPropertyMap.propertyName"/></span></td>
			                <th><s:text name="JCS02.viewType"/></th><%-- 扩展属性画面控件类型 --%>
			                <td>
			                	<span>
							 		<s:if test='extPropertyMap.viewType == "text"'>
			                              <s:text name="JCS02.validFlagText"></s:text>
			            			</s:if>
			            			<s:if test='extPropertyMap.viewType == "radio"'>
			                              <s:text name="JCS02.validFlagRadio"></s:text>
			            			</s:if>
			            			<s:if test='extPropertyMap.viewType == "select"'>
			                              <s:text name="JCS02.validFlagSelect"></s:text>
			            			</s:if>
			            			<s:if test='extPropertyMap.viewType == "checkbox"'>
			                              <s:text name="JCS02.validFlagCheckBox"></s:text>
			            			</s:if>	
		                		</span>
			                </td>
		             	</tr>
	         		</table>
                </div>
                <form id="editMainInfo" class="inline hide" >
                <div class="section-content">
	                <table class="detail" cellpadding="0" cellspacing="0">
			             	<tr>
				                <th><s:text name="JCS02.propertyName"/></th><%-- 扩展属性名称 --%>
				                <td>
					                <span>
					                	<s:property value="extPropertyMap.propertyName"/>
					                </span>
				                </td>
				                <th><s:text name="JCS02.viewType"/></th><%-- 扩展属性画面控件类型 --%>
				                <td>
				                	<span>
								 		<s:if test='extPropertyMap.viewType == "text"'>
				                              <s:text name="JCS02.validFlagText"></s:text>
				            			</s:if>
				            			<s:if test='extPropertyMap.viewType == "radio"'>
				                              <s:text name="JCS02.validFlagRadio"></s:text>
				            			</s:if>
				            			<s:if test='extPropertyMap.viewType == "select"'>
				                              <s:text name="JCS02.validFlagSelect"></s:text>
				            			</s:if>
				            			<s:if test='extPropertyMap.viewType == "checkbox"'>
				                              <s:text name="JCS02.validFlagCheckBox"></s:text>
				            			</s:if>	
			                		</span>
				                </td>
			             	</tr>
		         		</table>
                </div>
                </form>
            </div>
			<div class="section">
		        <%--dataTabel area --%>
		        <div class="section" id="extDefValSection">
		          <div class="section-header">
			          <strong><span class="ui-icon icon-ttl-section-search-result"></span>
			          <s:text name="extDefValList" />
			          </strong>
		          </div>
		          <div class="section-content">
		          <div id="shwoListInfo">
		          
		            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		              
		              <tbody>
		                <tr>
		                  <%-- <th class="center"><s:text name="codeKey" /></th>--%> <%-- 选项值 --%>
		                  <th class="center"><s:text name="value1" /></th><%-- 选项值（中文） --%>
		                  <th class="center"><s:text name="value2" /></th><%-- 选项值（英文） --%>
		                  <%--<th class="center"><s:text name="value3" /></th>--%><%-- 选项值（繁体） --%>
		                  <%--<th class="center"><s:text name="orderNumber" /></th>--%><%-- 选项值排序 --%>              
		                </tr>
		              	<s:iterator value="extDefValueList" id="edv">
		              		<tr>
		              		<%-- CodeType --%>
							<%-- <td style="width:8%" class="center"><span><s:property value="#edv.codeKey"/></span></td> --%>
							<%-- value1 --%>
							<td style="width:20%">
							<span>
								<s:if test='#edv.value1 !=null && !"".equals(#edv.value1)'><s:property value="#edv.value1"/></s:if>
								<s:else>&nbsp;</s:else>
							</span>
							</td>
							<%-- value2 --%>
							<td style="width:20%">
							<span>
								<s:if test='#edv.value2 !=null && !"".equals(#edv.value2)'><s:property value="#edv.value2"/></s:if>
								<s:else>&nbsp;</s:else>
							</span>
							</td>
							<%-- value3 --%>
							<%--
							<td style="width:10%">
							<span>
								<s:if test='#edv.value3 !=null && !"".equals(#edv.value3)'><s:property value="#edv.value3"/></s:if>
								<s:else>&nbsp;</s:else>
								</span>
							</td>
							--%>
							<%-- orderNumber --%>
							<%--
							<td style="width:5%" class="center">
							<span>
								<s:if test='#edv.orderNumber !=null && !"".equals(#edv.orderNumber)'><s:property value="#edv.orderNumber"/></s:if>
								<s:else>&nbsp;</s:else>
								</span>
							</td>
							--%>
							</tr>
						</s:iterator>
		              </tbody>
		            </table>
		            </div>
		            <form id="editListInfo" class="inline hide" >
		            <div class="toolbar clearfix">
			           <a id="addBtn" class="add" onclick="addNewCode();return false;" >
		                		<span class="ui-icon icon-add"></span>
		                		<span class="button-text"><s:text name="add_extDefVal"/></span>
	               	 	   </a>
			          </div>
		            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table"  width="100%" id="editListTable">
		              
		              <tbody>
		                <tr>
		                  <%-- <th class="center"><s:text name="codeKey" /></th>  --%> <%-- 选项值 --%>
		                  <th class="center"><s:text name="value1" /></th><%-- 选项值（中文） --%>
		                  <th class="center"><s:text name="value2" /></th><%-- 选项值（英文） --%>
		                  <%-- <th class="center"><s:text name="value3" /></th> --%><%-- 选项值（繁体） --%>
		                  <%-- <th class="center"><s:text name="orderNumber" /></th> --%><%-- 选项值排序 --%>
		                  <th class="center"><s:text name="JCS02.Operate" /></th>                  
		                </tr>
		              	<s:iterator value="extDefValueList" id="edv">
		              		<tr>
								<%-- CodeKey--%>
								<%--
								<td style="width:5%" class="center"><span>
									<s:textfield name="codeKeyArr" value="%{codeKey}" cssClass="text" cssStyle="width:50px" maxlength="4"></s:textfield>
									</span>
								</td>	
								
								<%-- Value1 --%>
								<td style="width:20%">
								<span>
									<s:textfield name="value1Arr" value="%{value1}" cssClass="text" cssStyle="width:60%" maxlength="20"></s:textfield>
									<span class="highlight"><s:text name="global.page.required"/></span>
								</span>
								</td>
								<%-- value2 --%>
								<td style="width:20%">
								<span>
									<s:textfield name="value2Arr" value="%{value2}" cssClass="text" cssStyle="width:60%" maxlength="20"></s:textfield>
								</span>
								</td>
								<%-- value3 --%>
								<%--
								<td style="width:10%">
								<span>
									<s:textfield name="value3Arr" value="%{value3}" cssClass="text" cssStyle="width:98%" maxlength="20"></s:textfield>
									</span>
								</td>
								--%>
								<%-- orderNumber --%>
								<%--
								<td style="width:5%" class="center">
								<span>
									<s:textfield name="orderNumberArr" value="%{orderNumber}" cssClass="text" cssStyle="width:98%" maxlength="10" onchange="jcs02_2IsInteger(this);return false;"></s:textfield>
									</span>
								</td>
								--%>
								<td style="width:1%" class="center">
									<a class="left" style="cursor:pointer" title="<s:text name='JCS02_delete'></s:text>" onclick="jcs02_2Delete(this);return false;">
										<span class="ui-icon icon-delete"></span>
									</a>
								</td>
							</tr>
						</s:iterator>
		              </tbody>
		            </table>
		            </form>
		          </div>
		        </div>
            </div>

           	<%-- 操作按钮 --%>
            <div id="editOption" class="center clearfix">
       			<button class="edit" onclick="jcs02_2Edit();return false;">
            		<span class="ui-icon icon-edit-big"></span>
            		<span class="button-text"><s:text name="global.page.edit"/></span>
            	</button>          	
	            <button id="close" class="close" type="button"  onclick="doClose();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
            
            <div id="saveOption" class="center clearfix" style="display:none">
	         	<button class="save" onclick="jcs02_2SaveEdit();return false;">
              		<span class="ui-icon icon-save"></span>
              		<span class="button-text"><s:text name="extDefVal_save"/></span>
              	</button>
          		<button id="back" class="back" type="button" onclick="jcs02_2DoBack()">
	            	<span class="ui-icon icon-back"></span>
	            	<span class="button-text"><s:text name="global.page.back"/></span>
	            </button>
	            <button id="close" class="close" type="button"  onclick="doClose();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
	  	</div>
	</div>
</div>

<%--=============================================添加extDefVal的部分Start=================================== --%>

<table id="addNewCode" class="hide">
	<tr>          		
		<%--
		<td class="center" style="width:5%"><span>
			<input type="text" style="width:50px" class="text" id="codeKeyArr" value="" maxlength="4" name="codeKeyArr">
			</span>
		</td>	
		--%>
		<td style="width:20%">
		<span>
			<input type="text" style="width:60%" class="text" id="value1Arr" value="" maxlength="20" name="value1Arr">
			<span class="highlight"><s:text name="global.page.required"/></span>
		</span>
		</td>
		
		<td style="width:20%">
		<span>
			<input type="text" style="width:60%" class="text" id="value2Arr" value="" maxlength="20" name="value2Arr">
		</span>
		</td>
		<%--
		<td style="width:10%">
		<span>
			<input type="text" style="width:98%" class="text" id="value3Arr" value="" maxlength="20" name="value3Arr">
			</span>
		</td>
		
		<td class="center" style="width:5%">
		<span>
			<input type="text" style="width:98%" class="text" id="orderNumberArr" value="" maxlength="10" name="orderNumberArr" onchange="jcs02_2IsInteger(this);return false;">
			</span>
		</td>
		--%>
		<td style="width:1%" class="center">
			<a class="left" style="cursor:pointer" title="<s:text name='JCS02_delete'></s:text>" onclick="jcs02_2Delete(this);return false;">
				<span class="ui-icon icon-delete"></span>
			</a>
		</td>
	</tr>
</table>
<%--=============================================添加extDefVal的部分End=================================== --%>

<div class="hide" id="dialogInit"></div>
<div style="display: none;">
	<div id="error1"><s:text name="JSC02_Error1" /></div>
	<div id="error2"><s:text name="JSC02_Error2" /></div>
	<div id="error3"><s:text name="JSC02_Error3" /></div>
</div>
</s:i18n>
<span id="saveEdit" style="display:none">${saveEditExtDefVal_url}</span>
<span id="doBack" style="display:none">${doBack_url}</span>