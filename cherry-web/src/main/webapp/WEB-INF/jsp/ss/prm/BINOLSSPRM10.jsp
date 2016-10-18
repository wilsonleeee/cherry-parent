<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM10.js"></script>

<%-- 保存按钮 --%>
<s:url id="save_url" action="BINOLSSPRM10_save"/>

<s:i18n name="i18n.ss.BINOLSSPRM10">
<s:text name="select_default" id="select_default"></s:text>
<div class="main container clearfix">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
        	<s:text name="prm10_manage"/> &gt; <s:text name="prm10_add"/>
       </span>
     </div>
    </div>
    <div id="actionResultDisplay"></div>
    <cherry:form id="addCatrgoryForm" method="post" class="inline" csrftoken="false">
     <div class="panel-content clearfix">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          </div>
           <div class="section-content">
            <table class="detail" cellpadding="0" cellspacing="0">
             <caption>
              <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
     		     <s:a action="BINOLSSPRM10_filter" cssStyle="display:none;" id="filterByBrandInfoUrl"></s:a>
     		     <s:text name="prm10.brandName" />：<s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId" onchange="cate10_changeBrand(this);"></s:select>&nbsp;&nbsp;&nbsp;
     		  </s:if>
     		  <s:if test="%{higherCategoryList != null && !higherCategoryList.isEmpty()}">
     		     <s:text name="prm10.higherCtegoryName" />：<s:select list="higherCategoryList " name="path" listKey="path" listValue="itemClassName"></s:select>
    		  </s:if>
            </caption>
            <tr>
                <th>
                   <%-- 类别码--%>
                   <s:text name="prm10.itemClassCode"/><span class="highlight"><s:text name="global.page.required"/></span>
                </th>
                <td>
                   <span>
                      <s:textfield name="itemClassCode" cssClass="text" maxlength="20" />
                   </span>
                </td>
                <th>
                   <%-- 类别中文名称 --%>
                   <s:text name="prm10.itemClassNameChinese"/>
                </th>
                <td>
                   <span>
                     <s:textfield name="itemClassNameCN" cssClass="text" maxlength="50" />
                   </span>
                </td>
           </tr>
           <tr>
	           <th>
                  <%-- 类别特征码 --%>
                  <s:text name="prm10.curClassCode"/>
               </th>
               <td>
                  <span>
                    <s:textfield name="curClassCode" cssClass="text" maxlength="4"  />
                  </span>
               </td>
	           <th>
                  <%-- 类别英文名称--%>
                  <s:text name="prm10.itemClassNameForeign"/>
               </th>
               <td>
                  <span>
                    <s:textfield name="itemClassNameEN" cssClass="text" maxlength="50" />
                  </span>
               </td>
          </tr>
         </table>
        </div>
       </div>
       <hr/>
           <div class="center clearfix" id="pageButton">
              <button id="save" class="save" type="button" onclick="doSave('${save_url}');return false;" >
            	<span class="ui-icon icon-save"></span>
            	<span class="button-text"><s:text name="global.page.save"/></span>
              </button>
              <button id="close" class="close" type="button"  onclick="doClose();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
              </button>
         </div>
      </div>
     </cherry:form>
    </div>
</s:i18n>
