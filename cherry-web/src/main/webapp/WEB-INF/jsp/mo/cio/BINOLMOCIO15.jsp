<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO15.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="i18n.mo.BINOLMOCIO15">
<s:url id="search_url" value="/mo/BINOLMOCIO15_search"/>
<s:url id="addRivalUrl" value="/mo/BINOLMOCIO15_addRival"/>
<s:url id="editRivalUrl" value="/mo/BINOLMOCIO15_editRival"/>
<s:url id="deleteRivalUrl" value="/mo/BINOLMOCIO15_deleteRival"/>
<div class="hide">
	<a id="searchUrl" href="${search_url}"></a>
	<a id="addRivalUrl" href="${addRivalUrl}"></a>
	<a id="editRivalUrl" href="${editRivalUrl}"></a>
	<a id="deleteRivalUrl" href="${deleteRivalUrl}"></a>
	<div id="addRivalTitle"><s:text name="CIO15_addRival"/></div>
	<div id="dialogConfirm"><s:text name="CIO15_dialogConfirm"/></div>
	<div id="dialogCancel"><s:text name="CIO15_dialogCancel"/></div>
	<div id="editRivalTitle"><s:text name="CIO15_editRival"/></div>
	<div id="deleteRivalTitle"><s:text name="CIO15_delete"></s:text></div>
	<div id="delConfirmMessage"><p class="message"><span><s:text name="CIO15_deleteConfirm"/></span></p></div>
</div>
<div class="panel-header">
	<div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
	    <%-- 添加按钮 --%>
		<span class="right"> 
		<cherry:show domId="MOCIO15ADD">
		<a class="add" id="addRivalButton">
       	<span class="button-text"><s:text name="CIO15_addRivalButton"/></span>
       	<span class="ui-icon icon-add"></span>
       	</a>
		</cherry:show> 
		</span>  
	</div> 
</div>
	<div id="errorMessage"></div>           
			<%-- ================== 错误信息提示 START ======================= --%>
			<div id="errorDiv2" class="actionError" style="display:none">
				<ul>
					<li><span id="errorSpan2"></span></li>
				</ul>         
			</div>
			<%-- ================== 错误信息提示   END  ======================= --%>        
  		  	<div id="actionResultDisplay"></div>
			<div class="panel-content">
			<div class="box">
				<cherry:form id="mainForm" class="inline">
					<input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLMOCIO15"/>
				<div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="CIO15_condition"/>
	            </strong>
	            </div>
	            <div class="box-content clearfix">
	              <div class="column" style="width:49%;">
	              	<p>
                	<%-- 所属品牌 --%>
                	<label><s:text name="CIO15_brand"></s:text></label>
                	<s:if test="%{brandInfoList.size()> 1}">
                    	<s:text name="CIO15_select" id="CIO15_select"/>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#CIO15_select}" cssStyle="width:100px;" onchange="BINOLMOCIO15.search();return false;"></s:select>
                	</s:if><s:else>
                    	<s:text name="CIO15_select" id="CIO15_select"/>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
                	</p>
	              </div>
	              <div class="column last" style="width:50%;">
        			<p><%-- 名称 --%>
						<label><s:text name="CIO15_rivalNameCN" /></label>
						<s:textfield name="rivalNameCN" cssClass="text" maxlength="20"/>
					</p>     
	              </div>
	            </div>       
				<p class="clearfix">
	                <%-- 查询 --%>
	              	<button class="right search" onclick="BINOLMOCIO15.search();return false;">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="CIO15_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <%-- ====================== 结果一览开始 ====================== --%>
			<div id="section" class="section hide">
         		<div class="section-header">
         		<strong>
         			<span class="ui-icon icon-ttl-section-search-result"></span>
         			<s:text name="global.page.list"/>
        		</strong>
       		</div>
         	<div class="section-content">
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" >
	              <thead>
	                <tr>
	                  <th><s:text name="CIO15_num"/></th><%-- No. --%>
	                  <th><s:text name="CIO15_rivalNameCN"/></th>
	                  <th><s:text name="CIO15_rivalNameEN"/></th><!-- 英文名称 -->
	                  <th><s:text name="CIO15_operate"/></th><!-- 操作 -->
	                </tr>
	              </thead>
	            </table>
       		</div>
		</div>
	</div>
    <%-- ====================== 结果一览结束 ====================== --%>
    <%-- ====================== 新增竞争对手弹出框开始 ====================== --%>
	 <div id="addRival" class="hide">
    	<div class="clearfix">
    	 <form id="cherryvalidate">
    	 <div id="actionResultDisplay"></div>
    		<div>
    		<table style="margin:auto; width:100%;" class="detail">
    		<tr>
    		<s:if test="%{brandInfoList.size()> 1}"> 
    		<th><s:text name="CIO15_brand"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    		<td><span>                	
                    <s:text name="CIO15_select" id="CIO15_select"/>
                    <s:select name="addbrandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#CIO15_select}" cssStyle="width:100px;"></s:select>
                </span>
            </td>
            </s:if>
            </tr>
    		<tr>
    		<th><s:text name="CIO15_rivalNameCN"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    		<td><span><s:textfield id="addRivalNameCN" name="addRivalNameCN" cssClass="text" maxlength="20"/></span></td>
    		</tr>
    		<tr>
    		<th><s:text name="CIO15_rivalNameEN"/></th>
    		<td><span><s:textfield id="addRivalNameEN" name="addRivalNameEN" cssClass="text" maxlength="20"/></span></td>
    		</tr>
    		</table>
    		</div>
    	</form>
    	</div>
    </div>
    <%-- ====================== 新增竞争对手弹出框结束 ====================== --%>
    
    <%-- ====================== 编辑竞争对手弹出框开始 ====================== --%>
	 <div id="editRival" class="hide">
    	<div class="clearfix">
    	 <form id="editRivalForm">
    	 <div id="actionResultDisplay"></div>
    		<div>
    		<table style="margin:auto; width:100%;" class="detail">
	    		<tr>
	    			<th><s:text name="CIO15_brand"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		    		<td>
		    		<s:if test="%{brandInfoList.size()> 1}"> 
			    		<span>                	
		                    <s:text name="CIO15_select" id="CIO15_select"/>
		                    <s:select id="editBrandInfoId" name="editBrandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#CIO15_select}" cssStyle="width:100px;"></s:select>
			            </span>
		            </s:if><s:else>
			            <span>
			               	<s:text name="CIO15_select" id="CIO15_select"/>
			               	<s:select id="editBrandInfoId" name="editBrandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
			           	</span>
		            </s:else>
		            </td>
	            </tr>
	    		<tr>
		    		<th><s:text name="CIO15_rivalNameCN"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		    		<td><span><s:textfield id="editRivalNameCN" name="editRivalNameCN" cssClass="text" maxlength="20"/></span></td>
	    		</tr>
	    		<tr>
		    		<th><s:text name="CIO15_rivalNameEN"/></th>
		    		<td><span><s:textfield id="editRivalNameEN" name="editRivalNameEN" cssClass="text" maxlength="20"/></span></td>
	    		</tr>
    		</table>
    		</div>
    	</form>
    	</div>
    </div>
    <%-- ====================== 编辑竞争对手弹出框结束 ====================== --%>
    <div class="hide" id="dialogInit"></div>
    </s:i18n>
	    <%-- ================== dataTable共通导入 START ======================= --%>
		<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
		<%-- ================== dataTable共通导入    END  ======================= --%>
	<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="EMO00031"/>'/>
    </div>