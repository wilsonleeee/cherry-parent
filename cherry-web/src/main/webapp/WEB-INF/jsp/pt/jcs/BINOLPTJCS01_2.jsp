<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS01.js?version=2011612301446"></script>
<script>
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};
$(function(){
	if (window.opener) {
       window.opener.lockParentWindow();
    }
});
</script>
<!-- 语言 -->
<s:set id="language" value="#session.language" />
<!-- 分类名 -->
<s:if test="#language.equals('en_US')">
<s:set id="propName" value="category.propNameEN" />
</s:if>
<s:else>
<s:set id="propName" value="category.propNameCN" />
</s:else>
<%-- 产品分类属性保存URL --%>
<s:url id="save_Url" action="BINOLPTJCS01_saveVal"><s:param name="brandInfoId"><s:property value="brandInfoId"/></s:param></s:url>
<%--显示停用和非停用URL--%>
<s:url id="showDisabled_Url" action="BINOLPTJCS01_set"><s:param name="brandInfoId"><s:property value="brandInfoId"/></s:param></s:url>
<%-- 产品分类ID --%>
<%-- ========================= 产品分类属性值一览 ============================= --%>

<s:i18n name="i18n.pt.BINOLPTJCS01">
<div class="main clearfix" style="min-width: 680px;">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="JCS01_manage"/> &gt; <s:text name="JCS01_title"/>
		       </span>
		    </div>
	  	</div>
	  	<%-- ================== 错误信息提示 START ======================= --%>
		<div id="actionResultDisplay"></div>
		<%-- ================== 错误信息提示   END  ======================= --%>
		<div class="panel-content">
		 	<div id="section" class="section">
		        <div class="section-header clearfix">
		        	<strong class="left"> 
		        		<span class="ui-icon icon-ttl-section-search-result"></span>
		        		<s:property value="propName"/><s:text name="JCS01.cateVals_list"/>
		        	</strong>
					<!--仅显示停用类别-->
					<s:if test='(category.showDisabled != null && "0"== category.showDisabled)'>
						<input id="checkbox" class="checkbox" style="margin-left: 58%;" type="checkbox" checked="checked" onclick="BINOLPTJCS01.showDisabledOrNot(this)">
					</s:if>
					<s:else>
						<input id="checkbox" class="checkbox" style="margin-left: 58%;" type="checkbox" onclick="BINOLPTJCS01.showDisabledOrNot(this)">
					</s:else>

					<span><s:text name="JCS01.showDisabledOnly"/></span>
		        	<%-- 分类属性值添加 --%>
		        	<cherry:show domId="BINOLPTJCS0104">
			       	<a href="#" class="add right" id="add" onclick="BINOLPTJCS01.addLine('#dataTable','#newLine');">
				 		<span class="ui-icon icon-add"></span>
				 		<span class="button-text"><s:text name="global.page.add"/><s:property value="propName"/><s:text name="JCS01.prop"/></span>
				 	</a>
				 	</cherry:show>
		        </div>
	        	<div class="section-content">
		        	<cherry:form id="mainForm" csrftoken="false" action="">
		        		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
				            <thead>
				              <tr>              
				                <%-- NO. --%>
				                <th style="width: 10%;"><s:text name="JCS01.number"/></th>
				                <%-- 属性代码  --%>
				                <th style="width: 15%;"><s:text name="JCS01.propVal"/></th>
				                <%-- 中文名称 --%>
				                <th style="width: 30%;"><s:text name="JCS01.propValNameCN"/></th>
				                <%-- 英文名称 --%>
				                <th style="width: 30%;"><s:text name="JCS01.propValNameEN"/></th>
				                <%-- 操作 --%>
				                <th style="width: 15%;" class="center"><s:text name="JCS01.option"/></th>
				              </tr>
				            </thead>
				            <tbody id="dataTable">
					            <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS01_3.jsp" flush="true"/>
				            </tbody>
	          			</table>
						<s:hidden name="showDisabled" id="showDisabled"></s:hidden>
						<input type="hidden" name="propId" id ="propId" value='<s:property value="category.propId"/>'/>
		        	</cherry:form>
	        	</div>
	        </div>
	    </div>
	</div>
</div>
<div class="hide">
	<table>
		<tbody id="newLine">
			<tr>
		      	<td></td><%-- NO. --%>
		      	<td><span><input name="propValueCherry" class="date" style="margin-right:0;"/></span></td><%-- 属性代码 --%>
		  		<td>
		  			<span><input name="propValueCN" class="text" style="margin-right:0;"/><span 
		  			class="highlight"><s:text name="global.page.required"/></span></span><%-- 中文名称 --%>
		  		</td>
		  		<td><span><input name="propValueEN" class="text" style="margin-right:0;"/></span></td><%-- 英文名称 --%>
		  		<td class="center"><%-- 操作 --%>
		  			<a href="#" onclick="BINOLPTJCS01.save(this,'${save_Url}',1);return false;"><s:text name="global.page.save"/></a>|<a 
		  				href="#" onclick="BINOLPTJCS01.delLine(this);"><s:text name="global.page.cancle"/></a>
		  		</td>
		    </tr>
	    </tbody>
	</table>
</div>
<div id="actionResultDisplay"></div>
	<div style="display: none;">
<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
<div class="hide" id="dialogInit"></div>
<div id="disableTitle"><s:text name="JCS01.disableCategoryTitle" /></div>
<div id="disableMessage"><p class="message"><span><s:text name="JCS01.disableCategoryMessage" /></span></p></div>
	</div>
</s:i18n>    