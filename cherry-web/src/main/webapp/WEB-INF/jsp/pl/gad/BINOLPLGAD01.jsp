<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/gad/BINOLPLGAD01.js"></script>

<s:url id="search_Url" action="BINOLPLGAD01_search"/>
<span class="hide" id="search_Url">${search_Url}</span>

<s:i18n name="i18n.pl.BINOLPLGAD01">
    <div class="panel-header">
        <div class="clearfix">
        <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span> 
		<span class="right">
      		<s:url action="BINOLPLGAD02_init" id="searchGadgetInitUrl"></s:url>
       		<a class="edit" href="${searchGadgetInitUrl }" onclick="javascript:openWin(this);return false;"><span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="布局配置"></s:text></span></a>
    	</span>
        </div>
    </div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="actionResultDisplay" class="hide"></div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
        <div class="box">
        	<div class="box-header">
	           	<strong style="font-size:13px">
	           		<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
	           	</strong>
	        </div>
	        <cherry:form id="mainForm" onsubmit="binOLPLGAD01.search();return false;">
		        <div class="box-content clearfix">
		        	<div class="column" style="width:49%;">
		        		<div>
			        		<p>
		        				<%-- 所属画面 --%>
								<label style="width:80px;"><s:text name="GAD01_pageId"></s:text></label>
								<s:text name="GAD01_select" id="GAD01_select"/>
			                    <s:select id="pageId" name="pageId" list="#application.CodeTable.getCodes('1130')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#GAD01_select}" cssStyle="width:120px;"></s:select>
			        		</p>
		        		</div>
		        	</div>
		        	<div class="column last" style="width:50%;">
		        		<div>
			        		<p>
			        			<%-- 小工具名称 --%>
				            	<label style="width: 80px;"><s:text name="GAD01_gadgetName"/></label>
				            	<s:textfield id="gadgetName" name="gadgetName" cssClass="text" maxlength="30" />
			        		</p>
		        		</div>
		        	</div>
		        </div>
	        </cherry:form>
	        <p class="clearfix">
        		<%-- 查询 --%>
        		<button class="right search" id="searchBut">
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
        		<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
		            <thead>
		              <tr>
		                <%-- NO. --%>
		                <th class="center"><s:text name="GAD01_num"/></th>
		                <%-- 小工具名称  --%>
		                <th class="center"><s:text name="GAD01_gadgetName"/></th>
		                <%-- 小工具代码 --%>
		                <th class="center"><s:text name="GAD01_gadgetCode"/></th>
		                <%-- 所属画面--%>
		                <th class="center"><s:text name="GAD01_pageId"/></th>
		                <%-- 操作 --%>
		                <th class="center"><s:text name="GAD01_option"/></th>
		              </tr>
		            </thead>
       			</table>
        	</div>
        </div>
    </div>	
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
