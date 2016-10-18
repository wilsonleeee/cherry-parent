<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF08.js"></script>
<%-- 语言环境 --%>
<s:i18n name="i18n.pl.BINOLPLSCF06">
<s:url id="deleteCode_url" value="/pl/BINOLPLSCF08_deleteCode"></s:url>
<s:url id="saveEdit_url" value="/pl/BINOLPLSCF08_saveEdit"></s:url>
<s:url id="doBack_url" value="/pl/BINOLPLSCF08_doBack"></s:url>


<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="scf_code_title"/> &gt; <s:text name="codeM_detail"/><%-- 导航栏 --%>
		       </span>
		    </div>
	  	</div>
	  	<div id="actionResultDisplay"></div>
	  	<div id="errorDiv" class="actionError" style="display:none">
            <ul>
                <li><span id="errorSpan"></span></li>
            </ul>         
        </div>
	  	<form id="mainForm" method="post" class="inline" action="BINOLPLSCF08_init">
	        <s:hidden name="codeManagerID" value="%{codeManagerID}"/>
	        <input type="hidden" id="parentCsrftoken" name="csrftoken"/>
        </form>
	  	<form id="coderForm" method="post" class="inline">
	        <s:hidden name="codeManagerID" value="%{codeMDetail.codeManagerID}"/>
	        <s:hidden name="orgCode" value="%{codeMDetail.orgCode}"/>
	        <s:hidden name="brandCode" value="%{codeMDetail.brandCode}"/>
	        <s:hidden name="orgName" value="%{codeMDetail.orgName}"/>
	        <s:hidden name="brandName" value="%{codeMDetail.brandName}"/>	        
	        <s:hidden name="codeType" value="%{codeMDetail.codeType}"/>	        
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
			                <th><s:text name="codeType"/></th><%-- Code类别 --%>
			                <td><span><s:property value="codeMDetail.codeType"/></span></td>
			                <th><s:text name="codeName"/></th><%-- Code类别名称 --%>
			                <td><span><s:property value="codeMDetail.codeName"/></span></td>
		             	</tr>
		             	<tr>
			                <th><s:text name="keyDescription"/></th><%-- Key说明  --%>
			                <td><span><s:property value="codeMDetail.keyDescription"/></span></td>
			                <th><s:text name="value1Description"/></th><%-- Value1说明 --%>
			                <td><span><s:property value="codeMDetail.value1Description"/></span></td>
		             	</tr>
		             	<tr>
			                <th><s:text name="value2Description"/></th><%-- Value2说明 --%>
			                <td><span><s:property value="codeMDetail.value2Description"/></span></td>
			                <th><s:text name="value3Description"/></th><%-- Value3说明 --%>
			                <td><span><s:property value="codeMDetail.value3Description"/></span></td>
		             	</tr>

	         		</table>
                </div>
                <form id="editMainInfo" method="post" class="inline hide">
                <div class="section-content">
	                <table class="detail" cellpadding="0" cellspacing="0">
			             	<tr>
				                <th><s:text name="codeType"/></th><%-- Code类别 --%>
				                <td>
					                <span>
					                	<s:property value="codeMDetail.codeType"/>
					                </span>
				                </td>
				                <th><s:text name="codeName"/></th><%-- Code类别名称 --%>
				                <td>
					                <span>
					                	<s:textfield name="codeName" value="%{codeMDetail.codeName}" cssClass="text" maxlength="20"></s:textfield>
					                </span>
				                </td>
			             	</tr>
			             	<tr>
				                <th><s:text name="keyDescription"/></th><%-- Key说明  --%>
				                <td>
				                	<span>
				                		<s:textfield name="keyDescription" value="%{codeMDetail.keyDescription}" cssClass="text" maxlength="20"></s:textfield>
				                	</span>
				                </td>
				                <th><s:text name="value1Description"/></th><%-- Value1说明 --%>
				                <td>
				                	<span>
				                		<s:textfield name="value1Description" value="%{codeMDetail.value1Description}" cssClass="text" maxlength="20"></s:textfield>
				                	</span>
				                </td>
			             	</tr>
			             	<tr>
				                <th><s:text name="value2Description"/></th><%-- Value2说明 --%>
				                <td>
				                	<span>
				                		<s:textfield name="value2Description" value="%{codeMDetail.value2Description}" cssClass="text" maxlength="20"></s:textfield>
				                	</span>
				                </td>
				                <th><s:text name="value3Description"/></th><%-- Value3说明 --%>
				                <td>
				                	<span>
				                		<s:textfield name="value3Description" value="%{codeMDetail.value3Description}" cssClass="text" maxlength="20"></s:textfield>
				                	</span>
				                </td>
			             	</tr>
	
		         		</table>
                </div>
                </form>
            </div>
			<div class="section">
		        <%--dataTabel area --%>
		        <div class="section" id="coderSection">
		          <div class="section-header">
			          <strong><span class="ui-icon icon-ttl-section-search-result"></span>
			          <s:text name="codeList" />
			          </strong>
		          </div>
		          <div class="section-content">
		          <div id="shwoListInfo">
		          
		            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		              
		              <tbody>
		                <tr>
		                  <%-- <th class="center"><input type="checkbox" name="allSelect" onclick="scf08AllSelected(this);" id="allSelect"/></th>--%>
		                
		                  <%-- <th><s:text name="brandInfo" /></th> --%>
		                  <th class="center" style="width:11%"><s:text name="codeType" /></th>
		                  <th class="center" style="width:11%"><s:text name="codeKey" /></th>
		                  <th class="center" style="width:19%"><s:text name="value1" /></th>
		                  <th class="center" style="width:19%"><s:text name="value2" /></th>
		                  <th class="center" style="width:19%"><s:text name="value3" /></th>
		                  <th class="center" style="width:11%"><s:text name="grade" /></th>
		                  <th class="center" style="width:10%"><s:text name="codeOrder" /></th>                  
		                </tr>
		              	<s:iterator value="coderList" id="coder">
		              		<tr>
		              		<%-- 
		              		<td style="width:5%" class="center">
		              			<span>
		              				<s:checkbox name="checkbox" id="%{codeType+'*'+codeKey}" onclick="scf08SingleSelected(this);"></s:checkbox>
		              			</span>
		              		</td>
		              		--%>
		              		<%-- CodeType --%>
							<td style="width:11%" class="center"><span><s:property value="#coder.codeType"/></span></td>
							<%-- CodeKey--%>
							<td style="width:11%" class="center"><span>
								<s:if test='#coder.codeKey != null && !"".equals(#coder.codeKey)'>
								      <s:property value="#coder.codeKey"/>
								</s:if>
								<s:else>&nbsp;</s:else>
								</span>
							</td>	
							<%-- Value1 --%>
							<td style="width:19%">
							<span>
								<s:if test='#coder.value1 !=null && !"".equals(#coder.value1)'><s:property value="#coder.value1"/></s:if>
								<s:else>&nbsp;</s:else>
							</span>
							</td>
							<%-- value2 --%>
							<td style="width:19%">
							<span>
								<s:if test='#coder.value2 !=null && !"".equals(#coder.value2)'><s:property value="#coder.value2"/></s:if>
								<s:else>&nbsp;</s:else>
							</span>
							</td>
							<%-- value3 --%>
							<td style="width:19%">
							<span>
								<s:if test='#coder.value3 !=null && !"".equals(#coder.value3)'><s:property value="#coder.value3"/></s:if>
								<s:else>&nbsp;</s:else>
								</span>
							</td>
							<%-- Grade --%>
							<td style="width:11%" class="center">
							<span>
								<s:if test='#coder.grade !=null && !"".equals(#coder.grade)'><s:property value="#coder.grade"/></s:if>
								<s:else>&nbsp;</s:else>
								</span>
							</td>
							<%-- CodeOrder --%>
							<td style="width:10%" class="center">
							<span>
								<s:if test='#coder.codeOrder !=null && !"".equals(#coder.codeOrder)'><s:property value="#coder.codeOrder"/></s:if>
								<s:else>&nbsp;</s:else>
								</span>
							</td>
							</tr>
						</s:iterator>
		              </tbody>
		            </table>
		            </div>
		            <form id="editListInfo" method="post" class="inline hide">
		            <div class="toolbar clearfix">
			           <a id="addBtn" class="add" onclick="addNewCode();return false;" >
		                		<span class="ui-icon icon-add"></span>
		                		<span class="button-text"><s:text name="add_coder"/></span>
	               	 	   </a>
			          </div>
		            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table"  width="100%" id="editListTable">

		              	<tr>
		                  <%-- <th><s:text name="brandInfo" /></th> --%>
		                  <th class="center" style="width:10%"><s:text name="codeType" /></th>
		                  <th class="center" style="width:9%"><s:text name="codeKey" /></th>
		                  <th class="center" style="width:18%"><s:text name="value1" /></th>
		                  <th class="center" style="width:18%"><s:text name="value2" /></th>
		                  <th class="center" style="width:18%"><s:text name="value3" /></th>
		                  <th class="center" style="width:9%"><s:text name="grade" /></th>
		                  <th class="center" style="width:9%"><s:text name="codeOrder" /></th>
		                  <th class="center" style="width:9%"><s:text name="SCF06_option" /></th>                  
		                </tr>
		     
		              	<s:iterator value="coderList" id="coder">
		              		<tr>
			              		<%-- CodeType --%>
								<td style="width:10%" class="center"><span><s:property value="#coder.codeType"/></span></td>
								<%-- CodeKey--%>
								<td style="width:9%" class="center"><span>
									<s:textfield name="codeKeyArr" value="%{codeKey}" cssClass="text" cssStyle="width:65px" maxlength="8"></s:textfield>
									</span>
								</td>	
								<%-- Value1 --%>
								<td style="width:18%">
								<span>
									<s:textfield name="value1Arr" value="%{value1}" cssClass="text" cssStyle="width:95%" maxlength="20"></s:textfield>
								</span>
								</td>
								<%-- value2 --%>
								<td style="width:18%">
								<span>
									<s:textfield name="value2Arr" value="%{value2}" cssClass="text" cssStyle="width:95%" maxlength="20"></s:textfield>
								</span>
								</td>
								<%-- value3 --%>
								<td style="width:18%">
								<span>
									<s:textfield name="value3Arr" value="%{value3}" cssClass="text" cssStyle="width:95%" maxlength="20"></s:textfield>
									</span>
								</td>
								<%-- Grade --%>
								<td style="width:9%" class="center">
								<span>
									<s:textfield name="gradeArr" value="%{grade}" cssClass="text" cssStyle="width:90%" maxlength="10" onchange="scf08IsInteger(this);return false;"></s:textfield>
									</span>
								</td>
								<%-- CodeOrder --%>
								<td style="width:9%" class="center">
								<span>
									<s:textfield name="codeOrderArr" value="%{codeOrder}" cssClass="text" cssStyle="width:90%" maxlength="10" onchange="scf08IsInteger(this);return false;"></s:textfield>
									</span>
								</td>
								<td style="width:9%" class="center">
									<a class="center" style="cursor:pointer" title="<s:text name='SCF06_delete'></s:text>" onclick="scf08Delete(this);return false;">
									<s:text name="SCF06_delete"/>
									</a>
								</td>
							</tr>
						</s:iterator>
		            </table>
		            </form>
		          </div>
		        </div>
            </div>

           	<%-- 操作按钮 --%>
            <div id="editOption" class="center clearfix">
       			<button class="edit" onclick="scf08Edit();return false;">
            		<span class="ui-icon icon-edit-big"></span>
            		<span class="button-text"><s:text name="global.page.edit"/></span>
            	</button>          	
	            <button id="close" class="close" type="button"  onclick="doClose();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
            
            <div id="saveOption" class="center clearfix" style="display:none">
	         	<button class="save" onclick="scf8SaveEdit();return false;">
              		<span class="ui-icon icon-save"></span>
              		<span class="button-text"><s:text name="code_save"/></span>
              	</button>
          		<button id="back" class="back" type="button" onclick="scf08DoBack()">
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

<%--=============================================添加Code的部分Start=================================== --%>

<table id="addNewCode" class="hide">
	<tr>          		
		<td style="width:10%" class="center"><span><s:property value="codeMDetail.codeType"/></span></td>
		
		<td class="center" style="width:9%"><span>
			<input type="text" style="width:65px" class="text" id="codeKeyArr" value="" maxlength="8" name="codeKeyArr">
			</span>
		</td>	
		
		<td style="width:18%">
		<span>
			<input type="text" style="width:95%" class="text" id="value1Arr" value="" maxlength="20" name="value1Arr">
		</span>
		</td>
		
		<td style="width:18%">
		<span>
			<input type="text" style="width:95%" class="text" id="value2Arr" value="" maxlength="20" name="value2Arr">
		</span>
		</td>
		
		<td style="width:18%">
		<span>
			<input type="text" style="width:95%" class="text" id="value3Arr" value="" maxlength="20" name="value3Arr">
			</span>
		</td>
		
		<td class="center" style="width:9%">
		<span>
			<input type="text" style="width:90%" class="text" id="gradeArr" value="" maxlength="10" name="gradeArr" onchange="scf08IsInteger(this);return false;">
			</span>
		</td>
		
		<td class="center" style="width:9%">
		<span>
			<input type="text" style="width:90%" class="text" id="codeOrderArr" value="" maxlength="10" name="codeOrderArr" onchange="scf08IsInteger(this);return false;">
			</span>
		</td>
		<td style="width:9%" class="center">
			<a class="center" style="cursor:pointer" title="<s:text name='SCF06_delete'></s:text>" onclick="scf08Delete(this);return false;">
				<s:text name="SCF06_delete"/>
			</a>
		</td>
	</tr>
</table>
<%--=============================================添加Code的部分End=================================== --%>

<div class="hide" id="dialogInit"></div>
<div style="display: none;">
	<div id="error1"><s:text name="SCF06_Error1" /></div>
	<div id="error2"><s:text name="SCF06_Error2" /></div>
</div>


</s:i18n>
<span id="deleteCode" style="display:none">${deleteCode_url}</span>
<span id="saveEdit" style="display:none">${saveEdit_url}</span>
<span id="doBack" style="display:none">${doBack_url}</span>



