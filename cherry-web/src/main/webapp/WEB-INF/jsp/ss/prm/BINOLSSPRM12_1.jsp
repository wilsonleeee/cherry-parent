<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>


<s:i18n name="i18n.ss.BINOLSSPRM12">

    <div class="clearfix">
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
          </div>
         <hr class="space" />
         <%-- 促销品编辑URL --%>
         <s:url id="eidt_url" action="BINOLSSPRM11_init">
             <s:param name="prmCategoryId">${prmCategoryInfo.prmCategoryId}</s:param>
         </s:url>
         <a href="${eidt_url}" id="eidt_url" style="display: none;"></a>
         <div class="center">
          <%-- 促销品类别编辑按钮 --%>
       	  <cherry:show domId="SSPRM0112EDIT">
       		  <button class="edit" onclick="openWin('#eidt_url');return false;">
       		 			<span class="ui-icon icon-edit-big"></span>
              			<span class="button-text"><s:text name="global.page.edit"/></span>
       		  </button>
       	  </cherry:show>
         </div>
  </div>
</s:i18n>
