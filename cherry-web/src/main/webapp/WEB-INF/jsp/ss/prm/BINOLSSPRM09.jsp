<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" >

function changeTreeOrTable(object,url) {
	oTableArr = new Array(null,null);
	fixedColArr = new Array(null,null);
	$(window).unbind('resize');
	if($(object).attr('class').indexOf('display-tree') != -1) {
		if($(object).attr('class').indexOf('display-tree-on') != -1) {
			return false;
		} else {
			$(object).siblings().removeClass('display-table-on');
			$(object).addClass('display-tree-on');
		}
	} else {
		if($(object).attr('class').indexOf('display-table-on') != -1) {
			return false;
		} else {
			$(object).siblings().removeClass('display-tree-on');
			$(object).addClass('display-table-on');
		}
	}
	var callback = function(msg) {
		$("#treeOrtableId").html(msg);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: callback,
		formId: '#categoryCherryForm'
	});
}

function addCategory(url) {
	var param = $('#categoryCherryForm').find(':input[name=csrftoken]').serialize();
	if($("#categoryTree").find('a.jstree-clicked').length > 0) {
		param += "&path=" + $("#categoryTree").find('a.jstree-clicked').parent().attr('id');
	}
	url = url + '?' + param;
	popup(url);
}
</script>

<s:i18n name="i18n.ss.BINOLSSPRM09">
  <div class="panel-header">
    <div class="clearfix"> 
      <span class="breadcrumb left"> 
         <span class="ui-icon icon-breadcrumb"></span>
         <s:text name="prm09_manage"/> &gt; <s:text name="prm09_inquiry"/>
      </span>
      <span class="right">
        <cherry:show domId="SSPRM0109ADD">
          <%-- 促销品类别添加URL --%>
          <s:url id="addCateUrl" action="BINOLSSPRM10_init"/>
       	  <a class="add" onclick="addCategory('${addCateUrl}');return false;">
       		 <span class="ui-icon icon-add"></span>
       		 <span class="button-text"><s:text name="prm09_add"/></span>
       	  </a>
       	</cherry:show>
        <s:url action="BINOLSSPRM09_treeInit" id="treeModeInitUrl"/>
        <s:url action="BINOLSSPRM09_listInit" id="listModeInitUrl"/>
        <small><s:text name="prm09_displayMode"></s:text>:</small>
        <a class="display display-tree display-tree-on" title='<s:text name="prm09_treeMode"></s:text>' onclick="changeTreeOrTable(this,'${treeModeInitUrl}');return false;"></a><a class="display display-table" title='<s:text name="prm09_listMode"></s:text>' onclick="changeTreeOrTable(this,'${listModeInitUrl}');return false;"></a>
      </span>
    </div>
  </div>
  <div id="treeOrtableId">
  	<s:action name="BINOLSSPRM09_treeInit" executeResult="true"></s:action>
  </div>
</s:i18n>  























 

    

 