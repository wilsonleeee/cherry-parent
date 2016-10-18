<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/st/jcs/BINOLSTJCS12.js"></script>

<s:i18n name="i18n.st.BINOLSTJCS12">
	<s:text name="global.page.select" id="select_default"/>
    <div class="panel-header">
	  <div class="clearfix"> 
	 	<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="STJCS12_breadcrumb" />&nbsp;&gt;&nbsp;<s:text name="STJCS12_title" /></span>
	  </div>
    </div>
   
    <div class="panel-content">
      <div class="box">
        <cherry:form id="mainForm" method="post" class="inline" onsubmit="binolstjcs12.search();return false;">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
	              <%-- 所属品牌 --%>
	               <p>
			           	<s:if test="%{brandInfoList.size()> 1}">
			               	<label><s:text name="STJCS12_brand"></s:text></label>
			               	<s:text name="global.page.select" id="STJCS12_select"/>
			               	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#STJCS12_select}" cssStyle="width:100px;"></s:select>
			           	</s:if><s:else>
			           		<label><s:text name="STJCS12_brand"></s:text></label>
			               	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
			           	</s:else>
	              </p>
	              <%-- 宝贝编码 --%>
	              <p>
			        <label><s:text name="STJCS12_outCode"/></label>
			        <s:textfield name="outCode" cssClass="text"/>
			      </p>
			      <%-- SKU编码 --%>
	              <p>
			        <label><s:text name="STJCS12_skuCode"/></label>
			        <s:textfield name="skuCode" cssClass="text"/>
			      </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
	            <%-- 厂商编码 --%>
	            <p>
	              <label><s:text name="STJCS12_unitCode" /></label>
	               <s:textfield name="unitCode" cssClass="text"/>
	            </p>
	            <%-- 产品条码 --%>
	            <p>
			      <label><s:text name="STJCS12_barCode"/></label>
			      <s:textfield name="barCode" cssClass="text"/>
			    </p>
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" onclick="binolstjcs12.search();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>
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
        <div class="section-content">
          <div class="toolbar clearfix">
          </div>
          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>  
                <%-- No. --%> 
                <th><s:text name="STJCS12_num"/></th>   
                <%-- 电商商品标题 --%>
                <th><s:text name="STJCS12_esProductTitleName" /></th>
                <%-- SKU编码 --%>
                <th><s:text name="STJCS12_skuCode"/></th>
                <%-- 宝贝编码 --%>
                <th><s:text name="STJCS12_outCode"/></th>
                <%-- 厂商编码 --%>
                <th><s:text name="STJCS12_unitCode" /></th>
                <%-- 产品编码--%>
                <th><s:text name="STJCS12_barCode" /></th>
                <%-- 产品名称 --%>
                <th><s:text name="STJCS12_nameTotal" /></th>
                <%-- 业务日期 --%>
                <th><s:text name="STJCS12_getDate" /></th>
                <%-- 产品对应关系是否改变  --%>
                <th><s:text name="STJCS12_isRelationChange" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div> 
<div style="display: none;">
	<div id="showDetailTitle"><s:text name="binolmowat11_showComments" /></div>

    <div id="dialogClose"><s:text name="global.page.close" /></div>
</div>     
</s:i18n>

<%-- 电商产品对应关系一览查询URL --%>
<s:url id="search_url" value="BINOLSTJCS12_search"/>
<s:hidden name="search_url" value="%{search_url}"/>
<div class="hide" id="dialogInit"></div> 
<div id="dialogDetail" class="hide">
  <div class="box2-active">
	<div class="box2 box2-content ui-widget" >
		<div style="word-break:break-all;word-wrap:break-word;">
			<label style="display: block;margin-right: 0px;" id="dialogContent"></label>
		</div>
	</div>
  </div>  
</div>       		 
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>