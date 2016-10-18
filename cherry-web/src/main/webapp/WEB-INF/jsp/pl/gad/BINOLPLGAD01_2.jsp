<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/gad/BINOLPLGAD01_2.js"></script>
<%-- 语言环境 --%>
<s:i18n name="i18n.pl.BINOLPLGAD01">
<s:url id="save_url" action="BINOLPLGAD01_confCategorySave"/>

<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="GAD01_list"/> &gt; <s:text name="GAD01_config"/><%-- 导航栏 --%>
		       </span>
		    </div>
	  	</div>
	  	<div id="actionResultDisplay"></div>
	  	<div id="errorDiv" class="actionError" style="display:none">
            <ul>
                <li><span id="errorSpan"></span></li>
            </ul>         
        </div>
	  	<div class="panel-content">
	  		<div class="section">
	            <div class="section-header">
	            	<strong>
	            		<span class="ui-icon icon-ttl-section-info"></span>
	            		<s:text name="global.page.title"/><%-- 基本信息 --%>
	            	</strong>
	            </div>
	            <div class="section-content" id="shwoMainInfo">
	                <table class="detail" cellpadding="0" cellspacing="0">
		             	<tr>
			                <th><s:text name="GAD01_gadgetName"/></th><%-- 小工具名称 --%>
			                <td><span><s:property value="gadgetInfoMap.gadgetName"/></span></td>
			                <th><s:text name="GAD01_pageId"/></th><%-- 所属画面 --%>
			                <td><span><s:property value='#application.CodeTable.getVal("1130",gadgetInfoMap.pageId)'/></span></td>
		             	</tr>
	         		</table>
                </div>
            </div>
			<div class="section">
		        <%--dataTabel area --%>
		        <div class="section" id="extDefValSection">
		          <div class="section-header">
			          <strong><span class="ui-icon icon-ttl-section-search-result"></span>
			          <s:text name="GAD01_config" />
			          </strong>
		          </div>
		          <div class="section-content">
		            <form id="editGadgedInfo" method="post">
			            <s:hidden name="gadgetInfoId" value="%{gadgetInfoMap.gadgetInfoId}"/>
			            <table cellpadding="0" cellspacing="0" border="0" class="detail"  width="100%" id="editListTable">
			                <tr>
				                <th><s:text name="GAD01_cate"/></th><%-- 需要显示的产品分类 --%>
				                <td colspan=3>
				                	<span>
			                			<s:checkboxlist name="categoryList" list="confMap.categoryMap" listKey="key" listValue="value" value="confMap.ids" >
			                			</s:checkboxlist>
			                		</span>
			                	</td>
			                </tr>
			                <tr>
				                <th><s:text name="GAD01_cateMax"/></th><%-- 分类属性显示数量 --%>
				                <td colspan=3>
				                	<span>
										<s:select name="cateMax" list="#{'3':'3','5':'5','7':'7'}" listKey="key" listValue="value" value="confMap.cateMax"/>
			                		</span>
			                	</td>
			                </tr>
			            </table>
		            </form>
		          </div>
		        </div>
            </div>

           	<%-- 操作按钮 --%>
			<hr/>
	        <div class="center clearfix" id="pageButton">
	          <button class="save" type="button" onclick="doSave('${save_url}');return false;">
	          	<span class="ui-icon icon-save"></span>
	          	<span class="button-text"><s:text name="global.page.save"/></span><%-- 保存 --%>
	          </button>
	          <button class="close" onclick="doClose();return false;">
	          	<span class="ui-icon icon-close"></span>
	            <span class="button-text"><s:text name="global.page.close"/></span><%-- 关闭 --%>
	          </button>
	        </div>
	  	</div>
	</div>
</div>

</s:i18n>