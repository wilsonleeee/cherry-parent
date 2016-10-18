<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
	<s:url id="s_prmSearchOneUrl" value="/common/BINOLCM02_popPrmDialogOne" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="">
<div id ="promotionDialogOne" class="dialog hide">
    <input class="text" value="" id="promotionDialogOneSearch" onKeyup ="datatableFilter(this,23);" maxlength="50"/>
    <a class="search" onclick="datatableFilter('#promotionDialogOneSearch',23)">
    	<span class="ui-icon icon-search"></span>
    	<span class="button-text"><s:text name="global.page.searchfor"/></span>
    </a>
    <hr class="space" />
    <s:if test='%{param == null || "".equals(param)}'>
    <div class="ui-tabs"> 
	    <ul class ="ui-tabs-nav clearfix" id="promotionCateTitleOne">
		    <s:iterator value='#application.CodeTable.getCodes("1139")' status="status">
		    	<li id='<s:property value="CodeKey"/>' type="<s:property value='#application.CodeTable.getVal("1139",CodeKey)' />" 
		    		class='<s:if test="%{#status.index == 0}">ui-tabs-selected</s:if>' onclick="changeTab(this,23);return false;">
		    		<a><s:property value="Value"/></a>
		    	</li>
		    </s:iterator>
	 	</ul>
		<div class="ui-tabs-panel">
	</s:if>
		    <table id="prm_dataTableOne" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
		       <thead>
		            <tr>
		               <th>
		               	<s:if test='%{checkType == null || checkType == "checkbox"}'>
		               	<input type="checkbox" id="prm_checkAll1"/>
		              	</s:if>
               		  	<label for="prm_checkAll1"><s:text name="global.page.Popselect"/></label>
		               </th>         <%-- 选择 --%>
		               <th><s:text name="global.page.prmbarcode"/></th><%--促销产品条码--%>
		               <th><s:text name="global.page.prmproductname"/></th><%--促销产品名称 --%>
		               <th><s:text name="global.page.classification"/></th><%--大分类--%>
		               <th><s:text name="global.page.inclassification"/></th><%--中分类 --%>
		               <th><s:text name="global.page.smallclassification"/></th><%--小分类 --%>
		               <th><s:text name="global.page.standardCost"/></th><%--成本价格--%>
		            </tr>
		        </thead>
		        <tbody id="prm_dataTableBodyOne"></tbody>
		    </table>
	<s:if test='%{param == null || "".equals(param)}'>	    
	   </div>
   </div>
   </s:if>
   <div class="hide" id="promotionDialogOne_temp"></div>
   <span id ="prmSearchOneUrl" style="display:none">${s_prmSearchOneUrl}</span>
   <span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
   <span id ="PopPrmTitle" style="display:none"><s:text name="global.page.PopPrmTitle"/></span><%--促销产品信息 --%>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>