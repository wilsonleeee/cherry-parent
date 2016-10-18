<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/man/BINOLMOMAN06.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>

<s:i18n name="i18n.mo.BINOLMOMAN06">
<s:url id="search_url" value="/mo/BINOLMOMAN06_search"/>
<s:url id="addInit_url" value="/mo/BINOLMOMAN05_init"/>
<s:url id="employeeBind_url" value="/mo/BINOLMOMAN06_employeeBind"/>
<s:url id="employeeUnbind_url" value="/mo/BINOLMOMAN06_employeeUnbind"/>
<s:url id="deleteUdisk_url" value="/mo/BINOLMOMAN06_deleteUdisk"/>

<div id="MAN06_select" class="hide"><s:text
		name="global.page.all" /></div> 
<div class="panel-header">
     	<%-- ###问卷查询 --%>
       <div class="clearfix"> 
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
       	<cherry:show domId="BINOLMOMAN0601">
       	<a class="add right" id="addUdisk" target="_blank">
	        <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="MAN06_addUdisk"/></span>
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
		<cherry:form id="mainForm" class="inline" onsubmit="binOLMOMAN06.search(); return false;">
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
                    	<label><s:text name="MAN06_brandName"></s:text></label>
                    	<s:text name="MAN06_select" id="MAN06_select"/>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#MAN06_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label><s:text name="MAN06_brandName"></s:text></label>
                    	<s:text name="MAN06_select" id="MAN06_select"/>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
                	</p>
	                <p>
        				<%--U盘SN --%>
        				<label><s:text name="MAN06_udiskSn"/></label>
	               		<s:textfield name="udiskSn" cssClass="text" maxlength="100"/>
        			</p>
        			<% /*
	               	<p>
	               		<label><s:text name="MAN06_ownerRight"/></label>
	               		<s:text name="global.page.all" id="MAN06_selectAll"/>
	               		<s:select name="ownerRight" list="#application.CodeTable.getCodes('1107')"
               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{MAN06_selectAll}"/>
	               	</p>
	               	*/ %>
		        </div>      
        		<div class="column last" style="width:50%;">
        			<p>
	               		<%-- 员工工号 --%>
	                  	<label><s:text name="MAN06_employeeCode"/></label>
	                  	<s:textfield name="employeeCode" cssClass="text" maxlength="50"/>
	                </p>  
        			<p>
	               		<%-- 员工名称 --%>
	                  	<label><s:text name="MAN06_employeeName"/></label>
	                  	<s:textfield name="employeeName" cssClass="text" maxlength="50"/>
	                </p>      
	            </div>
              </div>
              <p class="clearfix">
           			<%-- 查询 --%>
           			<button class="right search" type="submit" onclick="binOLMOMAN06.search(); return false;" id="searchBut">
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
			<span class="left" id="udiskOption">
				<cherry:show domId="BINOLMOMAN0602">
					 <a id="unbindBtn" class="delete" title="<s:text name='MAN06_optionMutUnbind'></s:text>">
                		<span class="ui-icon icon-disable"></span>
                		<span class="button-text"><s:text name="MAN06_unbindTitle"/></span>
               	 	</a>
				</cherry:show>
				<cherry:show domId="BINOLMOMAN0603">
					 <a id="deleteBtn" class="delete" title="<s:text name='MAN06_optionMutDelete'></s:text>">
                		<span class="ui-icon icon-disable"></span>
                		<span class="button-text"><s:text name="MAN06_deleteTitle"/></span>
               	 	</a>
				</cherry:show>
			</span>
			
		</div>
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0"
		class="jquery_table" width="100%">
		<thead>
			<tr>
				<%--选择 --%>
				<%-- <th><s:checkbox name="allSelect" id="checkAll" onclick="checkSelectAll(this)"/></th>--%>
				<th><input type="checkbox" name="allSelect" id="checkAll" onclick="binOLMOMAN06.checkSelectAll(this)"/></th>
				<%-- No. --%>
				<th><s:text name="No." /></th>
				<%-- U盘SN --%>
				<th><s:text name="MAN06_udiskSn" /></th>
				<%-- 员工姓名   --%>
				<th><s:text name="MAN06_employeeName" /></th>
				<%-- 权限级别   --%>
				<th><s:text name="MAN06_ownerRight" /></th>
				<%-- 品牌  --%>
				<th><s:text name="MAN06_brandName" /></th>
				<%-- 发布人  --%>
				<th><s:text name="MAN06_bindOption" /></th>
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
<span id="search" style="display:none">${search_url}</span>
<span id="addInit" style="display:none">${addInit_url}</span>
<span id="employeeBind" style="display:none">${employeeBind_url}</span>
<span id="employeeUnbind" style="display:none">${employeeUnbind_url}</span>
<span id="deleteUdisk" style="display:none">${deleteUdisk_url}</span>

<input type="hidden" id="errmsg1" value='<s:text name="EMO00051"/>'/>

<div class="hide" id="dialogInit"></div>
<input type="hidden" id="unbindTitle" value='<s:text name="MAN06_unbindTitle"/>'/>
<input type="hidden" id="unbindText" value='<s:text name="MAN06_unbindText"/>'/>
<input type="hidden" id="deleteTitle" value='<s:text name="MAN06_deleteTitle"/>'/>
<input type="hidden" id="deleteText" value='<s:text name="MAN06_deleteText"/>'/>
<input type="hidden" id="dialogConfirm" value='<s:text name="MAN06_Confirm"/>'/>
<input type="hidden" id="dialogCancel" value='<s:text name="MAN06_cancel"/>'/>
<input type="hidden" id="noChecked" value='<s:text name="MAN06_noChecked"/>'/>
<input type="hidden" id="errChecked" value='<s:text name="MAN06_errChecked"/>'/>
<input type="hidden" id="noEmployee" value='<s:text name="MAN06_noEmployee"/>'/>
<input type="hidden" id="beDisabled" value='<s:text name="MAN06_beDisabled"/>'/>
<input type="hidden" id="bind" value='<s:text name="MAN06_bind"/>'/>


</s:i18n>
<div id="employeeBind_main">
<%-- ================== 员工弹出框导入 START ========================== --%>
<jsp:include page="/WEB-INF/jsp/mo/common/BINOLMOCM01.jsp" flush="true" />
<%-- ================== 员工弹出框导入 START ========================== --%>
</div>