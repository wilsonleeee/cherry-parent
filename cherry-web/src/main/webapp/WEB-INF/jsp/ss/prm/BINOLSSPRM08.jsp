<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM08.js"></script>
<s:i18n name="i18n.ss.BINOLSSPRM08">
<div class="main container clearfix">
 <div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
        	<s:text name="prm08_manage"/> &gt; <s:text name="prm08_details"/>
       </span>
    </div>
  </div>
      <div class="panel-content">
      <form id="toEditForm" action="BINOLSSPRM07_init" method="post">
        	<%-- 促销品ID --%>
        	<input type="hidden" name="prmTypeId" value="${prmTypeInfo.prmTypeId}"/>
        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
        </form>
       <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          </div>
          <div class="section-content">
            <table class="detail" cellpadding="0" cellspacing="0">
              <tr>
                <th>
                	<%-- 所属品牌 --%>
                	<s:text name="prm08.brandName"/>
                </th>
                <td>
                	<span><s:property value="prmTypeInfo.brandName"/></span>
                </td>
                <th>
                	<%-- 中分类中文名称--%>
                	<s:text name="prm08.secondryCategoryNameCN"/>
                </th>
                <td>
                	<span><s:property value="prmTypeInfo.secondryCategoryNameCN"/></span>
                </td>
              </tr>
              <tr>
                <th>
                	<%-- 大分类代码 --%>
                	<s:text name="prm08.primaryCategoryCode"/>
                </th>
                <td>
                	<span><s:property value="prmTypeInfo.primaryCategoryCode"/></span>
                </td>
                <th>
                	<%-- 中分类英文名称--%>
                	<s:text name="prm08.secondryCategoryNameEN"/>
                </th>
                <td>
                	<span><s:property value="prmTypeInfo.secondryCategoryNameEN"/></span>
                </td>
              </tr>
              <tr>
	              <th>
	                	<%-- 大分类中文名称--%>
	                	<s:text name="prm08.primaryCategoryNameCN"/>
	                </th>
	                <td>
	                	<span><s:property value="prmTypeInfo.primaryCategoryNameCN"/></span>
	                </td>
	                <th>
	                	<%-- 小分类代码--%>
	                	<s:text name="prm08.smallCategoryCode"/>
	                </th>
	                <td>
	                	<span><s:property value="prmTypeInfo.smallCategoryCode"/></span>
	                </td>
              </tr>
              <tr>
              	<th>
                	<%-- 大分类英文名称--%>
                	<s:text name="prm08.primaryCategoryNameEN"/>
                </th>
                <td>
            		<span><s:property value="prmTypeInfo.primaryCategoryNameEN"/></span>
                </td>
                <th>
                	<%--小分类中文名称--%>
                	<s:text name="prm08.smallCategoryNameCN"/>
                </th>
                <td>
                	<span><s:property value="prmTypeInfo.smallCategoryNameCN"/></span>
                </td>
              </tr>
              <tr>
              	<th>
                	<%-- 中分类代码 --%>
                	<s:text name="prm08.secondryCategoryCode"/>
                </th>
                <td>
                    <span><s:property value="prmTypeInfo.secondryCategoryCode"/></span>
                </td>
                <th>
                	<%-- 小分类英文名称--%>
                	<s:text name="prm08.smallCategoryNameEN"/>
                </th>
                <td>
                	<span><s:property value="prmTypeInfo.smallCategoryNameEN"/></span>
                </td>
              </tr>
            </table>
          </div>
        </div>
        </cherry:form>
        <div class="center clearfix">
          <%-- 促销品分类编辑按钮 --%>
       	  <cherry:show domId="SSPRM0108EDIT">
       		  <button class="edit" onclick="editPrmType();return false;">
       		 			<span class="ui-icon icon-edit-big"></span>
              			<span class="button-text"><s:text name="global.page.edit"/></span>
       		  </button>
       	  </cherry:show>
       	  <button id="close" class="close" type="button"  onclick="doClose();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
          </button>
        </div>
      </div>
    </div>
   </div>
</s:i18n>
