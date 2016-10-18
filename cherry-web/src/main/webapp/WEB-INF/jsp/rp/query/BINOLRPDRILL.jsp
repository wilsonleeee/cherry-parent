<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>


<script type="text/javascript">
$(function(){
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	searchBIRptDetail();
});
/*
 * 全局变量定义
 */
var binolbsdep02_global = {};
//是否需要解锁
binolbsdep02_global.needUnlock = true;
window.onbeforeunload = function(){
	if (binolbsdep02_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

var drillString = '${drillString }';


function searchBIRptDetail(){
	var url = $("#searchBIRptDetail").attr("href");
	var csrftoken = $('#csrftoken',window.opener.document).serialize();
	url += "&" + csrftoken;
	// 显示结果一览
	$("#BIRptDetail").removeClass("hide");

	var columns = [];
	var drillStrings = drillString.split(',');
	for(var i = 0; i < drillStrings.length; i++) {
		columns.push({"sName" : drillStrings[i], "bSortable" : true});
	}
	
	// 表格设置
	var tableSetting = {
			 // 表格ID
			 tableId : '#dateTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [[ 0, "asc" ]],
			 // 表格列属性设置
			 aoColumns : columns,
			 // 横向滚动条出现的临界宽度
			 sScrollX : "100%"
	};
	// 调用获取表格函数
	getTable(tableSetting);
}
</script>




<s:i18n name="i18n.rp.BINOLRPQUERY">
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span>${biDrlThrough.biRptName }&nbsp;&gt;&nbsp;${biDrlThrough.title } </span>
  </div>
</div>
<div class="panel-content clearfix">

<div class="section">
<div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.condition"></s:text></strong></div>
<div class="section-content">
	<p>${biDrlThrough.condition }</p>
</div>
</div>

<div class="section hide" id="BIRptDetail">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="rpquery_result"></s:text></strong></div>
  <div class="section-content">
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dateTable">
      <thead>
        <tr>
          <s:iterator value="drillStrNames" id="drillStrName">
          	<th>${drillStrName }</th>
          </s:iterator>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>

</div>

</div>
</div>
</div>  
</s:i18n>


<div class="hide">
<s:url action="BINOLRPQUERY_searchBIRptDetail" id="searchBIRptDetail">
<s:param name="biRptCode">${biRptCode}</s:param>
<s:param name="biQuery">${biQuery}</s:param>
<s:param name="drillQuery">${drillQuery}</s:param>
</s:url>
<a href="${searchBIRptDetail }" id="searchBIRptDetail"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />   



















      
 