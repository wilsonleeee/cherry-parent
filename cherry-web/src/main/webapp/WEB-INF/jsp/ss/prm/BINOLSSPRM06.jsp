<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM06.js"></script>
<style>
table.detail th{width:11%}
table.detail td{width:22%}
</style>
<%-- 保存按钮 --%>
<s:url id="save_url" action="BINOLSSPRM06_save"/>
<%-- 大分类List查询 --%>
<s:url id="queryCate" action="BINOLCM05_queryCate" namespace="/common"/>
<%-- 中分类List查询 --%>
<s:url id="querySecondCate" action="BINOLCM05_querySecondCate" namespace="/common"/>
<s:i18n name="i18n.ss.BINOLSSPRM06">
<s:text name="prm06_select" id="prm06_select"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"> 
                <span class="ui-icon icon-breadcrumb"></span>
                <s:text name="prm06_manage"/> &gt; <s:text name="prm06_add"/>
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
            <table class="detail" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <%-- 品牌 --%>
                        <s:text name="prm06.brandName"/>
                    </th>
                    <td colspan="5">
                        <span>
                            <s:select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="getSelOpts('%{queryCate}');"/>
                        </span>
                    </td>
                </tr>
                <tr>
                    <th>
                        <%-- 分类类型 --%>
                        <s:text name="prm06_categoryType"/>
                    </th>
                    <td colspan="5">
                        <span>
                            <select id="categoryType" onchange="clearAddCategory();addCategory();return false;" >
                                <option value="0"><s:text name="prm06_primary"/></option>
                                <option value="1"><s:text name="prm06_secondry"/></option>
                                <option value="2"><s:text name="prm06_small"/></option>
                            </select>
                        </span>
                    </td>
                </tr>
                <tr id="categoryType0" class="">
                    <th>
                        <%-- 大分类代码 --%>
	                	<s:text name="prm06.primaryCategoryCode"/>
	                	<span class="highlight"><s:text name="global.page.required"/></span>
	                </th>
	                <td>
                        <span>
	                       <s:textfield name="primaryCategoryCode" cssClass="text" maxlength="4" />
                        </span>
	                </td>
                    <th>
                        <%-- 大分类中文名称--%>
                        <s:text name="prm06.primaryCategoryNameCN"/>
                    </th>
                    <td>
                        <span>
                            <s:textfield id="primaryCateCN" name="primaryCategoryNameCN" cssClass="text" maxlength="20" />
                        </span>
                    </td>
                    <th>
                        <%-- 大分类英文名称--%>
                        <s:text name="prm06.primaryCategoryNameEN"/>
                    </th>
                    <td>
                        <span>
                            <s:textfield id="primaryCateEN" name="primaryCategoryNameEN" cssClass="text" maxlength="20" />
                        </span>
                    </td>
                </tr>
                <tr id="primaryRow" class="hide">
                    <th>
                        <%-- 大分类 --%>
                        <s:text name="prm06_primary"/>
                        <span class="highlight"><s:text name="global.page.required"/></span>
                    </th>
                    <td colspan="5">
                        <span>
                            <s:if test="null != primaryCateList && !primaryCateList.isEmpty()">
                                <s:select id="primarySel" name="primaryCateCode" list="primaryCateList" listKey="primaryCateCode" listValue="primaryCateName" headerKey="" headerValue="%{prm06_select}" onchange="doSecondCate(this, '%{querySecondCate}');"/>
                            </s:if>
                            <s:else>
                                <select id="primarySel" name="primaryCateCode">
                                    <option value=""><s:text name="prm06_select"/></option>
                                </select>
                            </s:else>
                        </span>
                    </td>
                </tr>
                <tr id="categoryType1" class="hide">
                    <th>
                        <%-- 中分类代码 --%>
                        <s:text name="prm06.secondryCategoryCode"/>
                        <span class="highlight"><s:text name="global.page.required"/></span>
                    </th>
                    <td>
                        <span>
                            <s:textfield name="secondryCategoryCode" cssClass="text" maxlength="4" />
                        </span>
                    </td>
                    <th>
                        <%-- 中分类中文名称--%>
                        <s:text name="prm06.secondryCategoryNameCN"/>
                    </th>
                    <td>
                        <span>
                            <s:textfield id="secondryCateCN" name="secondryCategoryNameCN" cssClass="text" maxlength="20" />
                        </span>
                    </td>
                    <th>
                        <%-- 中分类英文名称--%>
                        <s:text name="prm06.secondryCategoryNameEN"/>
                    </th>
                    <td>
                        <span>
                            <s:textfield id="secondryCateEN" name="secondryCategoryNameEN" cssClass="text" maxlength="20" />
                        </span>
                    </td>
                </tr>
                <tr id="secondryRow" class="hide">
                    <th>
                        <%-- 中分类--%>
                        <s:text name="prm06_secondry"/>
                        <span class="highlight"><s:text name="global.page.required"/></span>
                    </th>
                    <td colspan="5">
                        <span>
                            <select id="secondrySel" name="secondCateCode" >
                                <option value=""><s:text name="prm06_select"/></option>
                            </select>
                        </span>
                    </td>
                </tr>
                <tr id="categoryType2" class="hide">
                    <th>
                        <%-- 小分类代码--%>
                        <s:text name="prm06.smallCategoryCode"/>
                        <span class="highlight"><s:text name="global.page.required"/></span>
                    </th>
                    <td>
                        <span>
                        <s:textfield name="smallCategoryCode" cssClass="text" maxlength="4" />
                      </span>
                    </td>
                    <th>
                    	<%--小分类中文名称--%>
                    	<s:text name="prm06.smallCategoryNameCN"/>
                    </th>
                    <td>
                        <span>
                            <s:textfield id="smallCateCN" name="smallCategoryNameCN" cssClass="text" maxlength="20" />
                        </span>
                    </td>
                    <th>
                        <%-- 小分类英文名称--%>
                        <s:text name="prm06.smallCategoryNameEN"/>
                    </th>
                    <td>
                        <span>
                            <s:textfield id="smallCateEN" name="smallCategoryNameEN" cssClass="text" maxlength="20" />
                        </span>
                    </td>
                </tr>
            </table>
            </div>
        </div>
    </cherry:form>
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
</div>
</div>
</s:i18n>
