<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>WITPOS店务通</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="css/datatable.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="css/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="css/cherry.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="js/common/commonAjax.js"></script>
<script type="text/javascript" src="js/common/cherry.js"></script>
<%-- dataTable相关js --%>
<script type="text/javascript" src="js/lib/jquery.dataTables.js"></script>
<script type="text/javascript" src="js/lib/ColVis.js"></script>
<script type="text/javascript" src="js/lib/FixedColumns.js"></script>
<script type="text/javascript" src="js/common/cherryDataTable.js"></script>

<script type="text/javascript">
	function searchBatchList(url) {
		$("#actionResultDisplay").empty();
		var param = $("#brandInfo").serialize();
		$.ajax({
            url :url, 
            type:'post',
            dataType:'html',
            data:param,
            success:function(msg){
			 	$("#section").hide();
			 	$("#ruleLogs").hide();
			 	$("#batchList").show();
				$('#batchList').html(msg);
            }
		 });
	}
	function refresh(url) {
		$("#actionResultDisplay").empty();
		$.ajax({
            url :url, 
            type:'post',
            dataType:'html',
            success:function(msg){
				var msgJson = window.JSON.parse(msg);
				alert(msgJson['result']);
            }
		 });
	}
	function viewWorkFlow(url){
		$("#actionResultDisplay").empty();
		$("#batchList").hide();
		 $("#ruleLogs").hide();
		// 显示结果一览
		 $("#section").show();
		 
		 // 查询参数序列化
		 var params= $("#brandInfo").serialize();
		 url = url + "?" + params;
		 // 表格设置
		 var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				// 表格默认排序
				 aaSorting : [[ 2, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [	
				              	{ "sName": "workFlowId"},										
								{ "sName": "workFlowName"}, 			            					
								{ "sName": "startTime"},
								{ "sName": "finishTime"},
								{ "sName": "state"},
								{ "sName": "action" , "bSortable" : false}
								],
				// 不可设置显示或隐藏的列	
				// aiExclude :[0, 1],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%"
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	}
	
	function viewRuleLogs(url) {
		$("#actionResultDisplay").empty();
		$("#batchList").hide();
		$("#section").hide();
		$("#ruleLogs").empty();
	 	$("#ruleLogs").show();
	 	var param = $("#brandInfo").serialize();
		$.ajax({
            url :url, 
            type:'post',
            dataType:'html',
            data:param,
            success:function(msg){
				$('#ruleLogs').html(msg);
            }
		 });
	}
	
	function openLogs(obj, name, flag) {
		// 新页面地址
		var url = $(obj).attr("href");
		if ("0" == flag) {
			var aurl = $("#copyLog_url").html();
			// 文件名
			var logname = name.replace(".html", "");
			var param = "logFileName=" + logname;
			$.ajax({
	            url :aurl, 
	            type:'post',
	            dataType:'html',
	            data:param,
	            success:function(msg){
	            	if(window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
						var ret = msgJson["RESULT"];
						if ("OK" == ret) {
							var tempName = msgJson["fileName"];
							url += tempName;
							
							window.open(url, "", "height=768,width=1380,location=no,scrollbars=yes");
						} else {
							popFlag = false;
							alert(msgJson["RESULTMSG"]);
						}
	            	}
	            }
			 });
		} else {
			url += name;
			window.open(url, "", "height=768,width=1380,location=no,scrollbars=yes");
		}
	}
</script>
</head>
<body>
	<div class="header container clearfix">
		<h1 class="logo left"><a href="#">WITPOS 店务通后台BATCH管理</a></h1>	
	</div> 
	
	<div class="main container clearfix">
		<div class="panel-header">
		  <div class="clearfix"> 
			<span class="breadcrumb left"> 
				<span class="ui-icon icon-breadcrumb"></span>后台BATCH管理
			</span>  
		  </div>
		</div>  
		<div class="panel-content">
		    <div id="actionResultDisplay">
		    	<s:if test="hasActionErrors()">
					<div class="actionError">
					  	<s:actionerror/>
					</div>
				</s:if>
		    </div>
		    <s:if test="%{brandInfoList != null && !brandInfoList.isEmpty()}">
		    <div class="section">
		    <s:url value="batchList_copyLog" id="copyLogUrl"></s:url>
		    <div class="hide"><div id="copyLog_url">${copyLogUrl}</div></div>
		      <s:if test="null != brandInfoList">
			      <div class="section-header">
				      <span class="ui-icon icon-ttl-section-search-result"></span>
				      <span>品牌：</span>
				      <s:url value="batchList_search"  id="searchBatchUrl"></s:url>
				      <s:url value="workFlowFile_refresh"  id="refreshUrl"></s:url>
				      <s:url value="batchList_viewLog"  id="viewLogUrl"></s:url>
				     
				      <s:select name="brandInfo" id="brandInfo" list="brandInfoList" listKey="brandInfo" listValue="brandName" onchange="javascript:searchBatchList('%{#searchBatchUrl}');return false;"/>
					  <span class="green" ><b/>当前业务日期:<s:property value="bussinessDate" /></span>
					  <SPAN><s:url id="url_view" value="/viewWorkFlow" />
					  <button type="button"  class="right" onclick="javascript:refresh('${refreshUrl}');return false;">
            		  <span>刷新工作流文件</span></button>
            		  <button type="button"  class="right" onclick="javascript:searchBatchList('${searchBatchUrl}');return false;">
            		  <span>返回一览表</span></button>
            		  <button type="button"  class="right" id="btnview" onclick="viewRuleLogs('${viewLogUrl}');">
            		  <span>查看规则日志</span></button> 
            		  <button type="button"  class="right" id="btnview" onclick="javascript:viewWorkFlow('${url_view}');return false;">
            		  <span>查看工作流</span></button>
            		  </span>   
			      </div>
		      </s:if>
		      <div id="batchList">
		      	<jsp:include page="/WEB-INF/jsp/webconsole/batchListResult.jsp" flush="true" />
		      </div>
		    <div id="section" class="section hide">
        	<div class="section-content">
	          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	            <thead>
	              <tr>              
	                <th>工作流ID</th>
	      			<th>工作流名称</th>
	      			<th>开始时间</th>
	      			<th>结束时间</th>
	      			<th>状态</th>
	      			<th>操作</th>
	              </tr>
	            </thead>           
	          </table>
	          </div>
	        </div>
	        <div id="ruleLogs" class="hide">
		      </div>
		    </div>
		    </s:if>
		</div>
	</div>
	
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</html>