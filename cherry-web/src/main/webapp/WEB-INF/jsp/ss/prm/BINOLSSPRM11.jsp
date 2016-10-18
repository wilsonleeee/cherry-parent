<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM11.js"></script>

<%-- 保存按钮 --%>
<s:url id="save_url" action="BINOLSSPRM11_save"/>

<%-- 返回详细页面 --%>
<s:url id="back_url" action="BINOLSSPRM12_init">
   <s:param name="prmCategoryId">${prmCateInfo.prmCategoryId}</s:param>
</s:url>

<s:i18n name="i18n.ss.BINOLSSPRM11">
<div class="main container clearfix">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
        	<s:text name="prm11_manage"/> &gt; <s:text name="prm11_details"/>
       </span>
     </div>
    </div>
    <div id="actionResultDisplay"></div>
     <div class="panel-content">
      <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          </div>
          <div class="section-content">
            <table id="basicInfo" class="detail" cellpadding="0" cellspacing="0">
               <caption>
     		     <s:text name="prm11.brandName"></s:text>：<s:property value="prmCateInfo.brandName"/>&nbsp;&nbsp;&nbsp;
		         <s:hidden name="brandInfoId" value="%{prmCateInfo.brandInfoId }"></s:hidden>
		         <s:if test="%{higherCategoryList != null && !higherCategoryList.isEmpty()}">
     		       <s:text name="prm11.higherCtegoryName"></s:text>：<s:select list="higherCategoryList" name="path" listKey="path" listValue="itemClassName" value="%{prmCateInfo.higherCategoryPath}"></s:select>
    		     </s:if>
    	       </caption>
               <tr>
                 <th>
                 <%-- 类别码--%>
                  <s:text name="prm11.itemClassCode"/><span class="highlight"><s:text name="global.page.required"/></span>
                 </th>
                 <td>
                  <span>
                    <%-- 促销产品类别ID --%>
                     <s:hidden type="hidden" name="prmCategoryId" value="%{prmCateInfo.prmCategoryId}"></s:hidden>
                     <%-- 新促销产品类别节点--%>
                     <s:hidden name="categoryPath" value="%{prmCateInfo.path}"></s:hidden>
                     <%-- 直属上级促销产品类别--%>
                     <s:hidden name="higherCategoryPath" value="%{prmCateInfo.higherCategoryPath}"></s:hidden>
                     <%-- 更新日时 --%>
                     <input type="hidden" name="modifyTime" value="${prmCateInfo.modifyTime}"/>
                     <%-- 更新次数 --%>
                     <input type="hidden" name="modifyCount" value="${prmCateInfo.modifyCount}"/>
                    <s:textfield name="itemClassCode" cssClass="text" maxlength="20" value="%{prmCateInfo.itemClassCode}" />
                  </span>
                 </td>
                 <th>
                  <%-- 类别中文名称 --%>
                  <s:text name="prm11.itemClassNameChinese"/>
                </th>
                <td>
                  <span>
                    <s:textfield name="itemClassNameCN" cssClass="text" maxlength="50" value="%{prmCateInfo.itemClassNameCN}" />
                  </span>
                 </td>
               </tr>
               <tr>
                 <th>
                  <%-- 类别特征码 --%>
                  <s:text name="prm11.curClassCode"/>
                 </th>
                 <td>
                  <span>
                    <s:textfield name="curClassCode" cssClass="text" maxlength="4" value="%{prmCateInfo.curClassCode}" />
                  </span>
                 </td>
                 <th>
                  <%-- 类别英文名称--%>
                  <s:text name="prm11.itemClassNameForeign"/>
                 </th>
                 <td>
                  <span>
                    <s:textfield name="itemClassNameEN" cssClass="text" maxlength="50" value="%{prmCateInfo.itemClassNameEN}" />
                  </span>
                 </td>
                </tr>
               </table>
              </div>
              <hr/>
              <div class="center clearfix" id="pageButton">
              <button id="save" class="save" type="button" onclick="doSave('${save_url}');return false;" >
            	<span class="ui-icon icon-save"></span>
            	<span class="button-text"><s:text name="global.page.save"/></span>
              </button>
              <s:if test='%{fromPage != null && fromPage != ""}'>
                <button id="back" class="back" type="button" onclick="doBack('${back_url}');return false;">
            	  <span class="ui-icon icon-back"></span>
            	  <span class="button-text"><s:text name="global.page.back"/></span>
                </button>
              </s:if>
              <button id="close" class="close" type="button"  onclick="doClose();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
              </button>
           </div>
         </div>
       </cherry:form>
      </div>
    </div>
  
</s:i18n>
