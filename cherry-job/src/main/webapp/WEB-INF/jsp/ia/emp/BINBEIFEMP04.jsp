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
<link rel="stylesheet" href="../css/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="../css/datatable.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="../css/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="../css/cherry.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="../js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="../js/common/commonAjax.js"></script>
<script type="text/javascript" src="../js/common/cherry.js"></script>

<script type="text/javascript">
//把参数拼成json对象形式的字符串
function getJsonParam(object) {
	var m = [];
	$(object).find(':input').each(function(){
		if(this.value && this.value != '') {
			if($(this).attr("type") == 'checkbox') {
				if(this.checked) {
					m.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent($.trim(this.value).replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				}
			} else {
				m.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent($.trim(this.value).replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
			}
		}
	});
	return '{'+m.toString()+'}';
}

function doBatch(url) {
	
    if(confirm("您确定要执行吗？")) {
        $("#actionResultDisplay").empty();
        var param = $('#binbeifemp04Form').serialize();
        var addParam = [];
        $('#levelRelation').find('div').each(function(){
    		addParam.push(getJsonParam(this));
    	});
        param = param + '&levelRelation=' + '[' + addParam.toString() + ']';
        $.ajax({
            url:url, 
            type:'post',
            dataType:'html',
            data:param,
            success:function(msg){
                $('#actionResultDisplay').html(msg);
            }
         });
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
				CPA账号同步
			</span>  
		  </div>
		</div> 
		<div class="panel-content">
		    <div id="actionResultDisplay"></div>
		    <div class="section">
			  <form id="binbeifemp04Form">
			    <s:hidden name="brandInfoId"></s:hidden>
	  			<s:hidden name="brandCode"></s:hidden>
	  			<div style="margin-top:10px;">CPA数据库名：<input name="dbName"/></div>
	  		  </form>  
	  		  <div id="levelRelation" style="margin-top:10px;">
  			  	<s:iterator value="positionList">
  			  		<div>CPA用户权限级别：<input name="level"/>&nbsp;&nbsp;-&nbsp;&nbsp;新后台岗位：<s:select list="positionList" name="positionCategoryId" listKey="positionCategoryId" listValue="categoryName" headerKey="" headerValue="请选择"></s:select></div>
  			  	</s:iterator>
  			  </div>
			  <div style="margin-top:10px;">
			  	<s:url action="if/BINBEIFEMP04_binbeifemp04Exec" id="binbeifemp04Url"></s:url>
			  	<a class="add" onclick="doBatch('${binbeifemp04Url }');return false;">
				  <span class="ui-icon icon-enable"></span><span class="button-text">CPA账号同步</span>
			    </a>	
		      </div>
		    </div>
		</div>
	</div>
	
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>