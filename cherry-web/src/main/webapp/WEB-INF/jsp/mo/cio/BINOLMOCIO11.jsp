<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO11.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
var BINOLMOCIO11_questionList = "";
if (window.opener) {
    window.opener.lockParentWindow();
}
$().ready(function(){
	BINOLMOCIO11_questionList = $("#questionStr").val();
});
</script>
<s:i18n name="i18n.mo.BINOLMOCIO11">
<div class="panel ui-corner-all">
<div class="main clearfix">
<div class="panel-header">
     	<%-- ###问卷查询 --%>
       <div class="clearfix"> 
       	<span class="breadcrumb left"> 
       		<span class="ui-icon icon-breadcrumb"></span>
       		<s:text name="CIO11_manage"/>&gt; <s:text name="CIO11_title"/> 
       	</span>
       </div>
</div>
    <div class="panel-content" >
      <div>
      <div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="global.page.title"/><%-- 基本信息 --%>
              	</strong>
              </div>
      <div class="section-content clearfix" id="paperDetail">
      <table class="detail">
      	<tbody>
      		<tr>
      			<th><s:text name="CIO11_brandName"/></th>
      			<td><s:property value="%{paperMap.brandName}"/></td>
      			
      			<th><s:text name="CIO11_maxPoint"/></th>
      			<td><s:property value="%{paperMap.maxPoint}"/></td>
      		</tr>
      		<tr>
      			<th><s:text name="CIO11_paperRight"/></th>
      			<td><s:property value='#application.CodeTable.getVal("1126", paperMap.paperRight)'/></td>
      			<th><s:text name="CIO11_date"></s:text></th>
      			<td class="date">
      			<span>${paperMap.startDate}</span>
	            <span>&nbsp;&nbsp;至&nbsp;&nbsp;</span>
	            <span>${paperMap.endDate}</span>
	            </td>
      		</tr>
      		<tr>
      			<th><s:text name="CIO11_paperName"/></th>
      			<td><s:property value="%{paperMap.paperName}"/></td>
      			<th></th>
      			<td></td>
      		</tr>
      	</tbody>
      </table>
       </div>
       <div class="section">
       		<div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="CIO11_questionDetail"/><%-- 问题详细 --%>
              	</strong>
             </div>
       </div>
       <div class="section-content clearfix" id="questionDetail">
       		<s:iterator value="groupList" status="status" id="group">
       			<div id="${group.checkQuestionGroupId}" class="box4">
					<div id="" class="box4-header clearfix">
						<strong><s:property value="%{groupName}"/></strong>
						<span class="expand right" style="cursor:pointer;margin:5px 0" onclick="showQuestion(this);"><span class="ui-icon ui-icon-triangle-1-n"></span></span>
					</div>
					<div class="box4-content clearfix" id="question" style="display:none;">
					</div>
				</div>
       		</s:iterator>
       </div>
       </div>
       </div>
       <hr class="space" />
    
    <%-- 操作按钮 --%>
       <div id="button" class="center clearfix">
	       <button id="close" class="close" type="button"  onclick="window.close();return false;">
	       		<span class="ui-icon icon-close"></span>
	       		<span class="button-text"><s:text name="global.page.close"/></span>
	       </button>
       </div>
       <hr class="space" />
   </div>
   </div>
</div>
<input type="hidden" id="questionStr" value="<s:property value='%{questionStr}'/>" name="questionStr"/>
</s:i18n>