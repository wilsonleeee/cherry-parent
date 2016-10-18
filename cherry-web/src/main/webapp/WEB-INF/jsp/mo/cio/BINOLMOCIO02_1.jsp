<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOCIO02">

<div id="aaData">
	<s:iterator value="paperList" status="status">
		<s:url id="detailsUrl" value="/mo/BINOLMOCIO04_init">
			<s:param name="paperId">${paperId}</s:param>
		</s:url>
		<ul>
			<%--选择 --%>
			<%-- <li> 
           <s:checkbox id="%{paperId}" name="validFlag" value="false" fieldValue="%{paperStatus+'*'+issuedStatus+'*'+paperType}" onclick="checkSelect(this);"/>
        	</li> --%>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- 问卷名称 --%>
			<li>
				<a href="${detailsUrl}" class="popup" onclick="javascript:popupWind(this);return false;">
					<span><s:property value="paperName"/></span>
				</a>
			</li>
			<%-- 问卷类型 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1107", paperType)'/></span></li>
			<%-- 开始日期 --%>
			<li><span><s:property value="startTime"/></span></li>
			<%-- 结束日期 --%>
			<li><span><s:property value="endTime"/></span></li>
			<%-- 发布日期--%>
			<li>
				<s:if test='"".equals(publishTime) || publishTime == null'>
					<span><label class="highlight"><s:text name="CIO02_unPublish"></s:text></label></span>
				</s:if>
				<s:else>
					<span>${publishTime}</span>
				</s:else>
			</li>
			<%-- 发布人 --%>
			<li><span><s:property value="publisher"/></span></li>
			<%-- 问卷状态  --%>
			<li><span><s:property value='#application.CodeTable.getVal("1108", paperStatus)'/></span></li>
			<%-- 操作 --%>
			<li>
			<input id="${paperId}" name="paperStatus" value="${paperStatus}*${issuedStatus}*${paperType}" type="hidden"></input>
			<!-- 制作中的问卷不能操作 -->
			<s:if test='!"0".equals(paperStatus)'>
				<!-- 未下发（发布时间为空） -->
				<s:if test='"0".equals(issuedStatus)'>
					<!-- pastState(0：未过期 1：过期 -->
					<s:if test='"0".equals(pastState)'>
						<s:if test='"2".equals(paperStatus)'>
							<!-- 停用 -->
							<cherry:show domId="BINOLMOCIO0202">
				                <a id="disableBtn" class="delete" onclick='popOptPaperDialog("1",this);return false;'>
					                <span class="ui-icon icon-disable"></span>
					                <span class="button-text"><s:text name="CIO02_disable"/></span>
				                </a>
			                </cherry:show>
		                </s:if><s:elseif test='"1".equals(paperStatus)'>
			                <!-- 启用 -->
			                <cherry:show domId="BINOLMOCIO0203">
				                <a id="enableBtn" class="delete" onclick='popOptPaperDialog("2",this);return false;'>
					                <span class="ui-icon icon-enable"></span>
					                <span class="button-text"><s:text name="CIO02_enable"/></span>
				                </a>
			                </cherry:show>
		                </s:elseif>
	                </s:if>
	                <!-- 编辑 -->
	                <cherry:show domId="BINOLMOCIO0204">
		                <a id="${paperId}" class="delete" onclick="cio02EditOrCopyPaper('0',this);return false;">
			                <span class="ui-icon icon-edit"></span>
			                <span class="button-text"><s:text name="CIO02_editPaper"/></span>
		                </a>
                	</cherry:show>
                	<!-- 删除 -->
                	<cherry:show domId="BINOLMOCIO0205">
	                	<a id="deleteBtn" class="delete" onclick='popOptPaperDialog("0",this);return false;'>
			                <span class="ui-icon icon-delete"></span>
			                <span class="button-text"><s:text name="CIO02_delete"/></span>
		                </a>
	                </cherry:show>
	                <!-- 复制 -->
	                <cherry:show domId="BINOLMOCIO0207">
		                <a id="copyBtn" class="delete" onclick="cio02EditOrCopyPaper('1',this);return false;">
			                <span class="ui-icon icon-copy"></span>
			                <span class="button-text"><s:text name="CIO02_copy"/></span>
		                </a>
	                </cherry:show>
	                <!-- 下发 (问卷状态可用)-->
	                <s:if test='"0".equals(pastState)'>
	                	<s:if test='"2".equals(paperStatus)'>
			                <cherry:show domId="BINOLMOCIO0206">
				                <a id="${paperId}*${issuedStatus}*${paperType}" class="delete"  onclick="cio02IssuedPaper(this);return false;">
					                <span class="ui-icon icon-down "></span>
					                <span class="button-text"><s:text name="CIO02_issuedPaper"/></span>
				                </a>
			                </cherry:show>
		                </s:if>
	                </s:if>
				</s:if>
				<%-- 已下发（发布时间非空）--%>
				<s:elseif test='"1".equals(issuedStatus)'>
					<!-- pastState(0：未过期 1：过期 -->
					<s:if test='"0".equals(pastState)'>
						<s:if test='"2".equals(paperStatus)'>
							<!-- 停用 -->
							<cherry:show domId="BINOLMOCIO0202">
				                <a id="disableBtn" class="delete" onclick='popOptPaperDialog("1",this);return false;'>
					                <span class="ui-icon icon-disable"></span>
					                <span class="button-text"><s:text name="CIO02_disable"/></span>
				                </a>
			                </cherry:show>
		                </s:if><s:elseif test='"1".equals(paperStatus)'>
			                <!-- 启用 -->
			                <cherry:show domId="BINOLMOCIO0203">
				                <a id="enableBtn" class="delete" onclick='popOptPaperDialog("2",this);return false;'>
					                <span class="ui-icon icon-enable"></span>
					                <span class="button-text"><s:text name="CIO02_enable"/></span>
				                </a>
			                </cherry:show>
		                </s:elseif>
	                </s:if>
	                <!-- 复制 -->
	                <cherry:show domId="BINOLMOCIO0207">
		                <a id="copyBtn" class="delete" onclick="cio02EditOrCopyPaper('1',this);return false;">
			                <span class="ui-icon icon-copy"></span>
			                <span class="button-text"><s:text name="CIO02_copy"/></span>
		                </a>
	                </cherry:show>
	                <!-- 下发(问卷状态为可用) -->
	                <s:if test='"0".equals(pastState)'>
	                	<s:if test='"2".equals(paperStatus)'>
			                <cherry:show domId="BINOLMOCIO0206">
				                <a id="${paperId}*${issuedStatus}*${paperType}" class="delete"  onclick="cio02IssuedPaper(this);return false;">
					                <span class="ui-icon icon-down "></span>
					                <span class="button-text"><s:text name="CIO02_issuedPaper"/></span>
				                </a>
			                </cherry:show>
		                </s:if>
	                </s:if>
	                <%-- 查看发布（已下发的） --%>
	                <a id='${paperId}' class="delete" onclick="cio02PopRegion(this);return false;">
		                <span class="ui-icon icon-publish "></span>
		                <span class="button-text"><s:text name="CIO02_region"/></span>
	                </a>
				</s:elseif>
			</s:if>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>