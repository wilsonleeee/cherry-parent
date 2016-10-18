<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM12.js"></script>

<s:i18n name="i18n.ss.BINOLSSPRM12">
<div class="main container clearfix">
 <div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
        	<s:text name="prm12_manage"/> &gt; <s:text name="prm12_details"/>
       </span>
    </div>
  </div>
      <div class="panel-content">
       <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
        <%-- 促销品编辑URL --%>
        <s:url id="eidt_url" value="/ss/BINOLSSPRM11_init">
           <s:param name="prmCategoryId">${prmCategoryInfo.prmCategoryId}</s:param>
           <s:param name="fromPage">1</s:param>
        </s:url>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- global.page.title --%>
          		<s:text name="prm12_basicInfo"/>
          	</strong>
          </div>
          <div class="section-content">
            <table id="basicInfo" class="detail" cellpadding="0" cellspacing="0">
              <caption>
                <%-- 所属品牌 --%>
                <s:text name="prm12.brandName"/>: <s:property value="prmCategoryInfo.brandName"/>&nbsp;&nbsp;&nbsp;
		        <%-- 直属上级类别--%>
		        <s:if test='%{prmCategoryInfo.higherCategoryName != null && prmCategoryInfo.higherCategoryName != ""}'>
		        <s:text name="prm12.higherCtegoryName" />：<s:property value="prmCategoryInfo.higherCategoryName"/>
		        </s:if>
              </caption>
              <tr>
                 <th>
                	<%-- 类别码--%>
                	<s:text name="prm12.itemClassCode"/>
                </th>
                <td>
                   <span>
            		<s:property value="prmCategoryInfo.itemClassCode"/>
            	   </span>
                </td>
                <th>
                	<%-- 类别中文名称 --%>
                	<s:text name="prm12.itemClassNameChinese"/>
                </th>
                <td>
                   <span>
                	<s:property value="prmCategoryInfo.itemClassNameCN"/>
                   </span>
                </td>
              </tr>
              <tr>
               <th>
                	<%-- 类别特征码 --%>
                	<s:text name="prm12.curClassCode"/>
                </th>
                <td>
                   <span>
                    <s:property value="prmCategoryInfo.curClassCode"/>
                   </span>
                </td>
                <th>
                	<%-- 类别英文名称--%>
                	<s:text name="prm12.itemClassNameForeign"/>
                </th>
                <td>
                	<s:property value="prmCategoryInfo.itemClassNameEN"/>
                </td>
                
              </tr>
            </table>
          </div>
         <div class="center clearfix">
          <%-- 促销品类别编辑按钮 --%>
       	  <cherry:show domId="SSPRM0112EDIT">
       		  <button class="edit" onclick="editPrmCategory('${eidt_url}');return false;">
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
       </cherry:form>
      </div>
    </div>
  </div>
</s:i18n>
