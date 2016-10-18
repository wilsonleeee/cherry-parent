<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/fac/BINOLBSFAC01.js"></script>
<%-- 厂商查询URL --%>
<s:url id="search_url" value="/basis/BINOLBSFAC01_search"/>
<%-- 厂商添加URL --%>
<s:url id="add_url" value="/basis/BINOLBSFAC04_init"/>
<s:i18n name="i18n.bs.BINOLBSFAC01">
	<div class="panel-header">
		<div class="clearfix">
		 	<span class="breadcrumb left"> 
				<span class="ui-icon icon-breadcrumb"></span>
				<s:text name="BSFAC01.manage"/> &gt; <s:text name="BSFAC01.title"/>
			</span>
			<span class="right"> 
				<cherry:show domId="BSFAC0101ADD">
					<%-- 厂商添加按钮 --%>
				 	<a href="${add_url }" class="add" id="add" onclick="javascript:openWin(this);return false;">
				 		<span class="button-text"><s:text name="BSFAC01.add"/></span>
				 		<span class="ui-icon icon-add"></span>
				 	</a>
				</cherry:show>
			</span>
		</div>
	</div>
	<div class="panel-content">
      	<div class="box">
        	<cherry:form id="mainForm" action="/Cherry/basis/search" method="post" class="inline" >
        		<div class="box-header">
	          		<strong><span class="icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
	          		<input type="checkbox" name="validFlag" value="0"/><s:text name="BSFAC01.invalid"/>
	          	</div>
        		<div class="box-content clearfix">
            		<div class="column" style="width:50%; height: 40px;">
            			<p style="margin: 5px 0;">
		                	<%-- 厂商编码 --%>	
		                	<label><s:text name="BSFAC01.manufacturerCode"/></label>
		                	<s:textfield name="manufacturerCode" cssClass="text"/>
              			</p>
            		</div>
            		<div class="column last" style="width:49%; height: 40px;">
            			<p style="margin: 5px 0;">
		                	<%-- 厂商名称 --%>
		                	<label><s:text name="BSFAC01.factoryName"/></label>
		                	<s:textfield name="factoryName" cssClass="text"/>
              			</p>
            		</div>
            	</div>
            	<p class="clearfix">
	            	<cherry:show domId="BSFAC0101QUERY">
	            		<%-- 厂商查询按钮 --%>
	            		<button class="right search" type="button" onclick="search('<s:property value="#search_url"/>')">
	            			<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
	            		</button>
	            	</cherry:show>
          		</p>
        	</cherry:form>
        </div>
        <div id="section" class="section hide">
	        <div class="section-header">
	        	<strong>
	        		<span class="ui-icon icon-ttl-section-search-result"></span>
	        		<s:text name="global.page.list"/>
	        	</strong>
	        </div>
	        <div class="section-content ">
	          <div class="toolbar clearfix">
	           	<span class="right">
	           		<%-- 列设置按钮  --%>
	           		<a href="#" class="setting">
	           			<span class="button-text"><s:text name="global.page.colSetting"/></span>
	    		 		<span class="ui-icon icon-setting"></span>
	    		 	</a>
	           	</span>
	          </div>
	          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	            <thead>
	              <tr>              
	                <%-- NO. --%>
	                <th><s:text name="number"/></th>
	                <%-- 厂商编码 --%>
	                <th><s:text name="BSFAC01.manufacturerCode"/><span class="ui-icon ui-icon-document"></span></th>
	                <%-- 厂商名称 --%>
	                <th><s:text name="BSFAC01.factoryName"/></th>
	                <%-- 厂商简称 --%>
	                <th><s:text name="BSFAC01.factoryNameShort"/></th>
	                <%-- 法人代表 --%>
	                <th><s:text name="BSFAC01.legalPerson"/></th>
	                <%-- 所属省份 --%>
	                <th><s:text name="BSFAC01.provinceName"/></th>
	                <%-- 所属城市 --%>
	                <th><s:text name="BSFAC01.cityName"/></th>
	                <%-- 有效区分 --%>
	                <th><s:text name="BSFAC01.validFlag"/></th>
	              </tr>
	            </thead>           
	          </table>
	        </div>
	     </div>
  	</div>
</s:i18n>
<%--厂商查询URL --%>
<s:hidden name="search_url" value="%{search_url}"/>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>