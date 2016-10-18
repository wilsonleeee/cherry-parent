<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS06.js"></script>
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
       	<form id="toEditForm" action="BINOLPTJCS07_init" method="post">
        	<input type="hidden" name="productId" value='<s:property value="productId"/>'/><%-- 产品ID --%>
        	<input type="hidden" name="brandInfoId" value='<s:property value="proMap.brandInfoId"/>'/><%-- 品牌ID --%>
        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
        </form>
        <div class="tabs">
        <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
          <ul>
            <li><a href="#tabs-1"><s:text name="global.page.title"/></a></li>
            <s:if test="bomList != null && bomList.size > 0">
            	<li><a href="#tabs-5"><s:text name="JCS06.bomInfo"/></a></li>
            </s:if>
            <li><a href="#tabs-2"><s:text name="JCS06.cateInfo"/></a></li>
            <li><a href="#tabs-3"><s:text name="JCS06.sellPrice"/></a></li>
            <li><a href="#tabs-6"><s:text name="JCS06.ubHistory"/></a></li>
            <li><a href="#tabs-4"><s:text name="JCS06.image"/></a></li> 
          </ul>
          <%-- ===================== 基本信息开始  =========================== --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS06_1.jsp" flush="true"/>
          <%-- ===================== 基本信息结束  =========================== --%>
          <%-- ===================== 分类信息开始  =========================== --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS06_2.jsp" flush="true"/>
          <%-- ===================== 分类信息结束  =========================== --%>
          <%-- ===================== 价格信息开始  =========================== --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS06_3.jsp" flush="true"/>
          <%-- ===================== 价格信息结束  =========================== --%>
          <%-- ===================== 图片信息开始  =========================== --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS06_4.jsp" flush="true"/>
          <%-- ===================== 图片信息结束  =========================== --%>
          <%-- ===================== BOM组件信息开始  =========================== --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS06_5.jsp" flush="true"/>
          <%-- ===================== BOM组件信息结束  =========================== --%>
          <%-- ===================== 编码条码履历开始  =========================== --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS06_6.jsp" flush="true"/>
          <%-- ===================== 编码条码履历结束  =========================== --%>
          
          <div class="center clearfix"  style="margin-top: 10px;">
           <%-- 产品编辑 --%>
       		<cherry:show domId="BINOLPTJCS0601">
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
