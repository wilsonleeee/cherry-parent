<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS36.js"></script>
<!-- 语言 -->
<s:set id="language" value="#session.language" />

<s:i18n name="i18n.pt.BINOLPTJCS06">
<div class="main container clearfix">
 	<div class="panel ui-corner-all">
	  <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left">
	          <span class="ui-icon icon-breadcrumb"></span>
	          <s:text name="JCS06.manage"/> &gt; <s:text name="JCS06.details"/>
	       </span>
	    </div>
	  </div>
      <div class="panel-content">
       	<form id="toAddForm" action="BINOLPTJCS33_init" method="post">
        	<input type="hidden" name="productId" value='<s:property value="productId"/>'/><%-- 产品ID --%>
        	<input type="hidden" name="brandInfoId" value='<s:property value="proMap.brandInfoId"/>'/><%-- 品牌ID --%>
        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
        </form>
        <div class="tabs">
        <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
          <%-- ===================== 基本信息开始  =========================== --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS36_1.jsp" flush="true"/>
          <%-- ===================== 基本信息结束  =========================== --%>
          <%-- ===================== 分类信息开始  =========================== --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS36_7.jsp" flush="true"/>
          <%-- ===================== 分类信息结束  =========================== --%>
          <%-- ===================== 价格信息开始  =========================== --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS36_3.jsp" flush="true"/>
          <%-- ===================== 价格信息结束  =========================== --%>
          
          <div class="center clearfix"  style="margin-top: 10px;">
           <%-- 产品编辑 --%>
       		<cherry:show domId="BINOLPTJCS3701">
       			<s:if test="validFlag == 1">
       		  	<button class="edit" onclick="editPro();return false;">
       		 			<span class="ui-icon icon-edit-big"></span> <%-- 产品编辑按钮 --%>
              			<span class="button-text"><s:text name="global.page.edit"/></span>
       		  	</button>
       		  	</s:if>
       		</cherry:show>
	       		<button id="close" class="close" type="button"  onclick="doClose();return false;">
	            	<span class="ui-icon icon-close"></span>
	            	<span class="button-text"><s:text name="global.page.close"/></span>
	          </button>
            </div>
        </cherry:form>
       </div>
      </div>
     </div>
   </div>
</s:i18n>
