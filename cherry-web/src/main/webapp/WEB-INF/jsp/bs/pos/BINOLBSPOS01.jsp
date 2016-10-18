<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>


<script type="text/javascript">

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
		formId: '#positionCherryForm'
	});
}

function addPosition(url) {
	var param = $('#positionCherryForm').find(':input[name=csrftoken]').serialize();
	if($("#positionTree").find('a.jstree-clicked').length > 0) {
		param += "&path=" + $("#positionTree").find('a.jstree-clicked').parent().attr('id');
	}
	url = url + '?' + param;
	popup(url);
}

</script>




<s:i18n name="i18n.bs.BINOLBSPOS01">
  <div class="panel-header">
    <h2><s:text name="position_mng"></s:text></h2>
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="base_mng"></s:text>&nbsp;&gt;&nbsp;<s:text name="position_mng"></s:text> </span> 
    <span class="right">
    <cherry:show domId="BSPOS0101ADDPO">
    <s:url action="BINOLBSPOS04_init" id="addPositionInitUrl"></s:url>
    <a class="add" onclick="addPosition('${addPositionInitUrl}');return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="add_position"></s:text></span></a>
    </cherry:show>
    <%-- 
    <s:url action="BINOLBSPOS01_treeInit" id="treeModeInitUrl"></s:url>
    <s:url action="BINOLBSPOS01_listInit" id="listModeInitUrl"></s:url>
    <small><s:text name="display_mode"></s:text>:</small>
    <a class="display display-tree display-tree-on" title='<s:text name="tree_mode"></s:text>' onclick="changeTreeOrTable(this,'${treeModeInitUrl}');return false;"></a><a class="display display-table" title='<s:text name="list_mode"></s:text>' onclick="changeTreeOrTable(this,'${listModeInitUrl}');return false;"></a>
    --%>
    </span>
    </div>
  </div>
  <div id="treeOrtableId">
  	<s:action name="BINOLBSPOS01_treeInit" executeResult="true"></s:action>
  </div>
</s:i18n>  























 

    

 