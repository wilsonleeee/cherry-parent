<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@page import="java.util.*" %>
<%@page import="com.googlecode.jsonplugin.JSONUtil" %>
<%@page import="java.text.*" %>
<%@page import="com.cherry.cm.util.ConvertUtil" %>

<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO04.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
var BINOLMOCIO04_questionList = "";
if (window.opener) {
    window.opener.lockParentWindow();
}
$().ready(function(){

	var questionList = $("#questionList").val();
	var quesList = eval('(' + questionList + ')');
	if(quesList.length == 1){
		showQuestionDetail(quesList[0],"#questionDetail");
	}else{
		BINOLMOCIO04_questionList = quesList;
		var count = BINOLMOCIO04_questionList.length;
		for(var i = 0 ; i < count ; i++){
			showQuestionByGroup(i);
		}
	}
});
</script>
<s:i18n name="i18n.mo.BINOLMOCIO04">
<%
	Map paperMap = (Map)request.getAttribute("paperMap");
	String paperQuestion = (String)request.getAttribute("paperQuestionList");
	List<List> paperQuestionList = (List<List>)JSONUtil.deserialize(paperQuestion);
	/* int validCount = 0;
	String _startTime = "";
	String _endTime = "";
	if(null != paperQuestionList && !paperQuestionList.isEmpty()){
		validCount = paperQuestionList.size();
		if(validCount == 1){
			List<Map<String,Object>> tempList = (List<Map<String,Object>>)paperQuestionList.get(0);
			_startTime = ((String)tempList.get(0).get("startTime")).replace("T"," ");
			_endTime = ((String)tempList.get(0).get("endTime")).replace("T"," ");
		}
	} */
%>
<script type="text/javascript">
	var BINOLMOCIO04_js_i18n={
			required:'<s:text name="CIO04_required"/>'
	};
</script>
<div class="panel ui-corner-all">
<div class="main clearfix">
<div class="panel-header">
     	<%-- ###问卷查询 --%>
       <div class="clearfix"> 
       	<span class="breadcrumb left"> 
       		<span class="ui-icon icon-breadcrumb"></span>
       		<s:text name="CIO04_manage"/>&gt; <s:text name="CIO04_title"/> 
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
      			<!-- 品牌名称 -->
      			<th><s:text name="CIO04_brandName"/></th>
      			<td><s:property value="%{paperMap.brandName}"/></td>
      			<!-- 总分 -->
      			<th><s:text name="CIO04_maxPoint"/></th>
      			<td>
      				<%if(null==paperMap.get("maxPoint")||"".equals(ConvertUtil.getString(paperMap.get("maxPoint")))||"0.00".equals(ConvertUtil.getString(paperMap.get("maxPoint")))) 
	    			{
	    			%>
	    				<label><label class="highlight">0</label></label>
	    			<%}else{%>
	    				<label><%out.print(paperMap.get("maxPoint")); %></label>
	    			<%} %>
      			</td>
      		</tr>
      		<tr>
      			<!-- 问卷类型 -->
      			<th><s:text name="CIO04_paperType"/></th>
      			<td><s:property value='#application.CodeTable.getVal("1107", paperMap.paperType)'/></td>
      			<!-- 发布时间 -->
      			<th><s:text name="CIO04_publishTime"></s:text></th>
      			<td class="date">
      				<%if(null==paperMap.get("publishTime")||"".equals(paperMap.get("publishTime"))) 
	    			{
	    			%>
	    				<label><span class="highlight"><s:text name="CIO04_unPublish"></s:text></span></label>
	    			<%}else{%>
	    				<label><%out.print(paperMap.get("publishTime")); %></label>
	    			<%} %>
      			</td>
      		</tr>
      		<tr>
      			<!-- 开始时间 -->
      			<th><s:text name="CIO04_startDate"/></th>
      			<%if(null != paperMap.get("startTime") && !"".equals(paperMap.get("startTime"))) {%>
		      	  	<td><%out.print(paperMap.get("startTime"));%></td>
		        <%}else{ %>
		        	<td></td>
		        <%} %>
		        <!-- 结束时间 -->
		        <th><s:text name="CIO04_endDate"/></th>
		        <%if(null != paperMap.get("endTime") && !"".equals(paperMap.get("endTime"))) {%>
		      	  		<td><%out.print(paperMap.get("endTime"));%></td>
		        <%}else{ %>
		        	<td></td>
		        <%} %>
      		</tr>
      		<tr>
      			<%-- 问卷名称 --%>
      			<th><s:text name="CIO04_paperName"/></th>
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
              		<s:text name="CIO04_questionDetail"/><%-- 问题详细 --%>
              	</strong>
             </div>
       </div>
       <div class="section-content clearfix" id="questionDetail">
	       <%if(null != paperQuestionList && paperQuestionList.size() > 1){
	       		int groupCount = paperQuestionList.size();
	       		for(int i = 0 ; i < groupCount ; i++){
	       			List<Map<String,Object>> tempList = (List<Map<String,Object>>)paperQuestionList.get(i);
	       			String startTime = ((String)tempList.get(0).get("startTime")).replace("T"," ");
	       			String endTime = ((String)tempList.get(0).get("endTime")).replace("T"," ");
	       			%>
	       			<div id="group_<%out.print(i);%>" class="box4">
	       				<div id="<%out.print(i);%>" class="box4-header clearfix">
							<strong><s:text name="CIO04_validTime"></s:text>：<%out.print(startTime+" ");%><s:text name="CIO04_to"></s:text><%out.print(" "+endTime);%></strong>
							<span id="expand_<%out.print(i);%>" class="expand right" style="cursor:pointer;margin:5px 0" onclick="showQuestionByGroup(this);"><span class="ui-icon ui-icon-triangle-1-s"></span></span>
						</div>
						<div class="box4-content clearfix" id="question"></div>
	       			</div>
	       		<%
	       		}
	       } %>
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
<input id="questionList" name="questionList" type="hidden" value="<s:property value='%{questionList}'/>"/>
</s:i18n>