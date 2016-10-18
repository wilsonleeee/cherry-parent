<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM07.js"></script>

<%-- 保存按钮 --%>
<s:url id="save_url" action="BINOLSSPRM07_save"/>
<s:i18n name="i18n.ss.BINOLSSPRM07">
<div class="main container clearfix">
<div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
        	<s:text name="prm07_manage"/> &gt; <s:text name="prm07_details"/>
       </span>
     </div>
    </div>
    <div id="actionResultDisplay"></div>
      <div class="panel-content">
      <form id="toDetailForm" action="BINOLSSPRM08_init" method="post">
        	<%-- 促销品ID --%>
        	<input type="hidden" name="prmTypeId" value="${prmType.prmTypeId}"/>
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
                	<s:text name="prm07.brandName"/>
                </th>
                <td>
                  <span>
                   	  <%-- 促销产品分类ID --%>
		              <input type="hidden" name="prmTypeId" value="${prmType.prmTypeId}"/>
		              <%-- 更新日时 --%>
		              <input type="hidden" name="modifyTime" value="${prmType.modifyTime}"/>
		              <%-- 更新次数 --%>
		              <input type="hidden" name="modifyCount" value="${prmType.modifyCount}"/>
		              <s:select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" value="%{prmType.brandInfoId}"/>
                  </span>
                </td>
                <th>
                	<%-- 中分类中文名称--%>
                	<s:text name="prm07.secondryCategoryNameCN"/>
               	</th>
                <td>
                  <span>
                    <s:textfield name="secondryCategoryNameCN" cssClass="text" maxlength="20" value="%{prmType.secondryCategoryNameCN}" tabindex="5"/>
                  </span>
                </td>
              </tr>
              <tr>
                <th>
                	<%-- 大分类代码 --%>
                	<s:text name="prm07.primaryCategoryCode"/>
                </th>
                <td>
                  <span>
                    <s:textfield name="primaryCategoryCode" cssClass="text" maxlength="4" value="%{prmType.primaryCategoryCode}" readonly="true" tabindex="1"/>
                  </span>
                </td>
                <th>
                	<%-- 中分类英文名称--%>
                	<s:text name="prm07.secondryCategoryNameEN"/>
                </th>
                <td>
                  <span>
                    <s:textfield name="secondryCategoryNameEN" cssClass="text" maxlength="20" value="%{prmType.secondryCategoryNameEN}" tabindex="6"/>
                  </span>
                </td>
              </tr>
              <tr>
              	<th>
                	<%-- 大分类中文名称--%>
                	<s:text name="prm07.primaryCategoryNameCN"/>
                </th>
                <td>
                  <span>
                    <s:textfield name="primaryCategoryNameCN" cssClass="text" maxlength="20" value="%{prmType.primaryCategoryNameCN}" tabindex="2"/>
                  </span>
                </td>
                <th>
                	<%-- 小分类代码--%>
                	<s:text name="prm07.smallCategoryCode"/>
                </th>
                <td>
                   <span>
                    <s:textfield name="smallCategoryCode" cssClass="text" maxlength="4" value="%{prmType.smallCategoryCode}" readonly="true" tabindex="7"/>
                  </span>
                </td>
              </tr>
              <tr>
                <th>
                	<%-- 大分类英文名称--%>
                	<s:text name="prm07.primaryCategoryNameEN"/>
                </th>
                <td>
                  <span>
                    <s:textfield name="primaryCategoryNameEN" cssClass="text" maxlength="20" value="%{prmType.primaryCategoryNameEN}" tabindex="3"/>
                  </span>
                </td>
                <th>
                	<%--小分类中文名称--%>
                	<s:text name="prm07.smallCategoryNameCN"/>
                </th>
                <td>
                  <span>
                    <s:textfield name="smallCategoryNameCN" cssClass="text" maxlength="20" value="%{prmType.smallCategoryNameCN}" tabindex="8"/>
                  </span>
                </td>
              </tr>
              <tr>
                <th>
                	<%-- 中分类代码 --%>
                	<s:text name="prm07.secondryCategoryCode"/>
                </th>
                <td>
                  <span>
                    <s:textfield name="secondryCategoryCode" cssClass="text" maxlength="4" value="%{prmType.secondryCategoryCode}" readonly="true" tabindex="4"/>
                  </span>
                </td>
                <th>
                	<%-- 小分类英文名称--%>
                	<s:text name="prm07.smallCategoryNameEN"/>
                </th>
                <td>
                   <span>
                     <s:textfield name="smallCategoryNameEN" cssClass="text" maxlength="20" value="%{prmType.smallCategoryNameEN}" tabindex="9"/>
                   </span>
                  </td>
              </tr>
                </table>
               </div>
              </div>
       </cherry:form>
       <hr/>
       <div class="center clearfix" id="pageButton">
              <button id="save" class="save" type="button" onclick="doSave('${save_url}');return false;" >
            	<span class="ui-icon icon-save"></span>
            	<span class="button-text"><s:text name="global.page.save"/></span>
              </button>
              <button id="back" class="back" type="button" onclick="doBack();return false;">
            	<span class="ui-icon icon-back"></span>
            	<span class="button-text"><s:text name="global.page.back"/></span>
              </button>
              <button id="close" class="close" type="button"  onclick="doClose();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
              </button>
           </div>
      </div>
      </div>
    </div>
</s:i18n>
