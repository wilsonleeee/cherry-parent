<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTUNQ01">
<div id="aaData">
		<s:iterator value="unqViewList" id="unqViewInfo" >
			<ul>
				<li>
				<s:checkbox name="validFlag" fieldValue="%{#unqViewInfo.prtUniqueCodeID}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
			    <s:hidden name="relexportExcelCount" value="%{#unqViewInfo.relexportExcelCount}"></s:hidden>
			    </li>
			    <li><span><s:property value="#unqViewInfo.RowNumber"/></span></li>
			    <li><span><s:property value="#unqViewInfo.prtUniqueCodeBatchNo" /></span></li>
			    <li><span><s:property value="#unqViewInfo.generateDate"/></span></li>
			    <li><span><s:property value="#unqViewInfo.nameTotal"/></span></li>
			    <li><span><s:property value="#unqViewInfo.unitCode"/></span></li>
				<li><span><s:property value="#unqViewInfo.baCode"/></span></li>
				<li><span><s:text name="format.number"><s:param value="#unqViewInfo.generateCount"></s:param></s:text></span></li><%--生成数量数字每三位用逗号隔开--%>
				<li><span><s:property value="#unqViewInfo.exportExcelCount"/></span></li>
				<li><span><s:property value="#unqViewInfo.lastExportExcelTime"/></span></li>
			    <li><%--描述过多时只显示20字段，鼠标放上去时有显示效果 --%>
				 <a class="description" style="cursor: pointer;"  title="<s:text name="PTUNQ.describe" /> | <s:property value="#unqViewInfo.prtUniqueCodeBatchDescribe" />">
			        <s:if test="%{null!=#unqViewInfo.prtUniqueCodeBatchDescribe&&#unqViewInfo.prtUniqueCodeBatchDescribe.length()>24}">
			          <s:property value="%{#unqViewInfo.prtUniqueCodeBatchDescribe.substring(0, 20)}" />...
			 		</s:if>
			 		<s:else>
			          <s:property value="#unqViewInfo.prtUniqueCodeBatchDescribe" />
			   		</s:else>
		          </a>
			    </li>
			   	<li>
				  <span>
				  <cherry:show domId="BINOLPTUNQ0102"> <a href="#" onclick="BINOLPTUNQ01.exportExcel('${prtUniqueCodeID }','${relexportExcelCount }','${prtUniqueCodeBatchNo }');return false;"><s:text name="PTUNQ.exportExcel"/></a></cherry:show> 
				  <cherry:show domId="BINOLPTUNQ0103"><span> / </span><a href="#" onclick="BINOLPTUNQ01.exportPDF('${prtUniqueCodeID }');return false;"><s:text name="PTUNQ.exportPDF"/></a> </cherry:show>
			      </span>
			    </li>
			</ul>
		</s:iterator>	
</div>
</s:i18n>