<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="js/common/commonAjax.js"></script>
<script type="text/javascript" src="js/common/cherry.js"></script>

<script type="text/javascript">

	function view(url) {
           $("#actionResultDisplay").empty();// 查询参数序列化
  		   var params= $("#brandInfo").serialize();
           $.ajax({
               url :url, 
               type:'get',
               dataType:'html',
               data:params,
               success:function(msg){
               		$('#batchList').show();
               	 	$("#section").hide();
        	  		$('#batchList').html(msg);
               }
            });
	}
</script>


<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="currentSteps" id="workFlowMap">
		<ul>
			<s:url id="url_viewgraph" value="/viewGraph" >
	    		<s:param name="workFlowId">${workFlowMap.workFlowId}</s:param>
	    		<s:param name="workFlowName">${workFlowMap.workFlowName}</s:param>
	    		<s:param name="currentStepId">${workFlowMap.currentStepId}</s:param>
	    		<s:param name="currentActionId">${workFlowMap.currentActionId}</s:param>
	    	</s:url>
		   <%-- 工作流id --%>
			<li>			
				<s:if test='#workFlowMap.workFlowId != null && !"".equals(#workFlowMap.workFlowId)'>
				      <s:property value="workFlowId"/>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 工作流名称 --%>
			<li>
				<s:if test='#workFlowMap.workFlowName !=null && !"".equals(#workFlowMap.workFlowName)'>
					<s:property value="workFlowName"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 开始时间 --%>
			<li>
				<s:if test='#workFlowMap.startTime !=null && !"".equals(#workFlowMap.startTime)'>
					<s:property value="startTime"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 结束时间 --%>
			<li>
				<s:if test='#workFlowMap.finishTime !=null && !"".equals(#workFlowMap.finishTime)'>
					<s:property value="finishTime"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 状态 --%>
			<li>
				<s:if test='#workFlowMap.state == 4'><s:label value="已完成"/></s:if>
				<s:elseif test='#workFlowMap.currentActionId == null'><s:label cssStyle="color:red;" value="初始化异常"/></s:elseif>
				<s:else><s:label cssStyle="color:red;" value="异常结束"/></s:else>
			</li>
			<%-- 操作 --%>
			<li>
				<s:if test='#workFlowMap.state == 4 || #workFlowMap.currentActionId == null'>&nbsp;</s:if>
				<s:else><a class="add" onclick="view('${url_viewgraph}');return false;">
					<span class="ui-icon icon-enable"></span><span class="button-text">查看流程图</span>
				</a></s:else>
			</li>
		</ul>
	</s:iterator>
</div>