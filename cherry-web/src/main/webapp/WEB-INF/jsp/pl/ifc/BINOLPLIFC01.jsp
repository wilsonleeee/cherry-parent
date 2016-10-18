<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLIFC01.js"></script>
<s:i18n name="i18n.pl.BINOLPLIFC01">
<div class="panel-header">
	<%-- 沟通接口配置管理  --%>
	<div class="clearfix"> 
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
	</div>
</div>
<cherry:form id="mainForm" class="inline">
	<div class="panel-content">
		<div class="section-header" style="height:30px">
			<div style="width:20%; float:left">
				<span>
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<span><s:text name="plifc.brandName"/>：</span>
				</strong>
				<s:if test='%{brandList != null && !brandList.isEmpty()}'>
					<s:select name="brandInfoId" Cssstyle="width:100px" list="brandList" listKey="brandInfoId" listValue="brandName" onchange=""/>
				</s:if><s:else>
					<input type="hidden" name="brandInfoId" value='<s:property value="brandInfoId"/>'/>
				    <input name="brandName" class="text disabled" style="width:100px" disabled="disabled" value='<s:property value="brandName"/>' type="text" />
			    </s:else>
			    </span>
			</div>
			<div style="width:20%; float:left">
				<span>
			    <strong>
					<span><s:text name="plifc.messageType"/>：</span>
				</strong>
				<s:select name="messageType" list='#application.CodeTable.getCodes("1203")' listKey="CodeKey" listValue="Value" value="" onchange="" />
				</span>
			</div>
			<div style="width:59%; float:left">
				<span>
			    <strong>
					<span><s:text name="plifc.supplier"/>：</span>
				</strong>
				<s:select name="supplier" list='#application.CodeTable.getCodes("1258")' listKey="CodeKey" listValue="Value" value="" onchange="" />
				</span>
			</div>
		</div>
		<div class="section-content">
			<div class="toolbar clearfix section-header">
				<strong>
				<span class="left">
					<span class="ui-icon icon-ttl-section-info"></span>
					<%-- 配置信息  --%>
					<s:text name="plifc.configTitle"/>
				</span>
				</strong>
			</div>
			<div class="boxes">
				<div class="box-header"></div>
			    <table class="detail" style="width:100%;" border="0" cellpadding="0" cellspacing="0">
				    <s:iterator value="ifConfigList" id="ifConfigMap">
					<tr>
					    <th style="height:30px;">
					    	
					    </th>
					    <td>
					    	
					    </td>
					</tr>
					</s:iterator>
			    </table>
		    </div>
		</div>
	</div>
</cherry:form>
</s:i18n>

