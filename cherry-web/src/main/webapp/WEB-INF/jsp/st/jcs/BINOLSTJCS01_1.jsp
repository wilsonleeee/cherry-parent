<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTJCS01">
<div id="aaData">
	<s:iterator value="depotInfoList" id="dil" >
		<s:url id="detailsUrl" value="/st/BINOLSTJCS03_init">
			<s:param name="depotId">${depotInfoID}</s:param>
		</s:url>
		<ul>
			<%-- 选择--%>
			<li>
				<input type="checkbox" id="flag" name="depotInfoIDArr" onclick="BINOLSTJCS01.checkSelect(this);" value="<s:property value="depotInfoID"/>"/>
			</li>
			<%-- 序号--%>
			<li>
				<s:property value="RowNumber"/>
			</li>
			<%-- 仓库编号--%>
			<li>
				<s:if test='null!=depotCode'>
					<a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
						<span><s:property value="depotCode"/></span>
					</a>
				</s:if>
				<s:else>
					&nbsp;
				</s:else>
			</li>
            <%-- 仓库名称 --%>
            <li>
            	<s:if test='null!=depotName'>
					<s:property value="depotName"/>
				</s:if>
				<s:else>
					&nbsp;
				</s:else>
			</li>
			<%-- 归属部门 --%>
            <li>
            	<s:if test='null!=departCode'>
					<s:property value="'('+departCode+')'+departName"/>
				</s:if>
				<s:else>
					<s:property value="departName"/>
				</s:else>
			</li>
            <%-- 是否测试仓库  --%>
            <li>
            	<s:if test='testType==1'>
           			<s:text name="JCS01_testType"></s:text>
           		</s:if>
           		<s:else>
           			<s:text name="JCS01_offcerTpye"></s:text>
           		</s:else>
			</li>
            <%-- 仓库地址  --%>
            <li>
            	<s:if test='null!=address'>
					<s:property value="address"/>
				</s:if>
				<s:else>
					&nbsp;
				</s:else>
			</li>
            <%-- 有效区分 --%>
			<li>
				<s:if test="validFlag ==1"><span class='ui-icon icon-valid'></span></s:if>
				<s:elseif test="validFlag ==0"><span class='ui-icon icon-invalid'></span></s:elseif>
				<s:else><span></span></s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>