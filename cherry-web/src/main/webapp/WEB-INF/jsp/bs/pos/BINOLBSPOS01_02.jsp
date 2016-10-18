<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>	
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript">

function searchPosition(){
	var url = $("#positionListUrl").attr("href");
	var params= $("#positionCherryForm").serialize();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	// 显示结果一览
	$("#positionInfoId").removeClass("hide");
	// 表格设置
	var tableSetting = {
			 // 表格ID
			 tableId : '#dateTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [[ 1, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [//{ "sName": "checkbox", "sWidth": "1%", "bSortable": false },
			                { "sName": "no", "sWidth": "5%", "bSortable": false },
							{ "sName": "PositionName", "sWidth": "10%" },
							{ "sName": "PositionDESC", "sWidth": "15%", "bSortable": false },
							{ "sName": "CategoryName", "sWidth": "10%", "bSortable": false },
							{ "sName": "BrandNameChinese", "sWidth": "10%", "bSortable": false },
							{ "sName": "DepartName", "sWidth": "10%", "bSortable": false },
							{ "sName": "isManager", "sWidth": "5%", "bSortable": false },
							{ "sName": "PositionType", "sWidth": "10%", "bSortable": false },
							{ "sName": "ResellerName", "sWidth": "10%", "bSortable": false },
							{ "sName": "FoundationDate", "sWidth": "10%", "bSortable": false },
							{ "sName": "ValidFlag", "sWidth": "5%", "bSortable": false }],
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

function bspos01_changeBrand(object,select_default) {
	
	var callback = function(msg){
		var jsons = eval('('+msg+')');
		$('#organizationId').empty();
		$('#organizationId').append('<option value="">'+select_default+'</option>');
		if(jsons.orgList.length > 0) {
			for(var i in jsons.orgList) {
				$('#organizationId').append('<option value="'+jsons.orgList[i].departId+'">'+jsons.orgList[i].departName+'</option>');
			}
		}
		$('#path').empty();
		$('#path').append('<option value="">'+select_default+'</option>');
		if(jsons.higherPositionList.length > 0) {
			for(var i in jsons.higherPositionList) {
				$('#path').append('<option value="'+jsons.higherPositionList[i].path+'">'+jsons.higherPositionList[i].positionName+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: $('#filterByBrandInfoUrl').attr("href"),
		param: $(object).serialize(),
		callback: callback,
		formId: '#positionCherryForm'
	});
	
}

function bspos01_changeOrg(object, select_default) {
	var callback = function(msg){
		var jsons = eval('('+msg+')');
		$('#path').empty();
		$('#path').append('<option value="">'+select_default+'</option>');
		if(jsons.higherPositionList.length > 0) {
			for(var i in jsons.higherPositionList) {
				$('#path').append('<option value="'+jsons.higherPositionList[i].path+'">'+jsons.higherPositionList[i].positionName+'</option>');
			}
		}
	};
	var param = $('#brandInfoId').serialize();
	if(param) {
		param += '&' + $(object).serialize();
	} else {
		param = $(object).serialize();
	}
	cherryAjaxRequest({
		url: $('#filterByOrgUrl').attr("href"),
		param: param,
		callback: callback,
		formId: '#positionCherryForm'
	});
}

</script>
	
<s:i18n name="i18n.bs.BINOLBSPOS01">	
<s:text name="select_default" id="select_default"></s:text>
<div class="panel-content clearfix">
<div class="box">
  <cherry:form id="positionCherryForm" class="inline" onsubmit="searchPosition();return false;">
    <div class="box-header"> <strong><span class="icon icon-ttl-search"></span><s:text name="search_condition"></s:text></strong>
      <%--<s:checkbox name="validFlag" fieldValue="1"></s:checkbox>包含无效岗位信息--%> </div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%;">
        <p>
          <label><s:text name="positionName"></s:text></label>
          <s:textfield name="positionName" cssClass="text"></s:textfield>
        </p>
        <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
        <p>
          <label><s:text name="brandInfo"></s:text></label>
          <s:a action="BINOLBSPOS01_filter" cssStyle="display:none;" id="filterByBrandInfoUrl"></s:a>
          <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#select_default}" onchange="bspos01_changeBrand(this,'%{#select_default }');"></s:select>
        </p>
        </s:if>
      </div>
      <div class="column last" style="width:49%;">
        <p>
          <label><s:text name="organizationId"></s:text></label>
          <s:a action="BINOLBSPOS01_filterByOrg" cssStyle="display:none;" id="filterByOrgUrl"></s:a>
          <s:select list="orgList" name="organizationId" listKey="departId" listValue="departName" headerKey="" headerValue="%{#select_default}" onchange="bspos01_changeOrg(this,'%{#select_default}')"></s:select>
        </p>
        <p>
          <label><s:text name="higherPosition"></s:text></label>
          <s:select list="higherPositionList" name="path" listKey="path" listValue="positionName" headerKey="" headerValue="%{#select_default}" cssStyle="width:150px;"></s:select>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" onclick="searchPosition();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="search_button"></s:text></span></button>
    </p>
  </cherry:form>
</div>
<div class="section hide" id="positionInfoId">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="search_result"></s:text></strong></div>
  <div class="section-content">
    <div class="toolbar clearfix"><%--<span class="left">
      <input name="" type="checkbox" value="" class="checkbox" />全选 
      <a href="#" class="delete"><span class="ui-icon icon-disable"></span><span class="button-text">停用选中</span></a> <a href="#" class="add"><span class="ui-icon icon-enable"></span><span class="button-text">启用选中</span></a> </span>--%>
   	  <span class="right">
  		<%-- 列设置按钮  --%>
  		<a href="#" class="setting">
  			<span class="button-text"><s:text name="setting"></s:text></span>
			<span class="ui-icon icon-setting"></span>
 		</a>
      </span>
    </div>
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dateTable">
      <thead>
        <tr>
          <%-- <th>选择</th>--%>
          <th><s:text name="number"></s:text></th>
          <th><s:text name="positionName"></s:text><span class="ui-icon-document css_right ui-icon ui-icon-triangle-1-n"></span><span class="css_right ui-icon ui-icon-triangle-1-n"></span></th>
          <th><s:text name="positionDESC"></s:text></th>
          <th><s:text name="categoryName"></s:text></th>
          <th><s:text name="brandInfo"></s:text></th>
          <th><s:text name="organizationId"></s:text></th>
          <th><s:text name="isManager"></s:text></th>
          <th><s:text name="positionType"></s:text></th>
          <th><s:text name="resellerName"></s:text></th>
          <th><s:text name="foundationDate"></s:text></th>
          <th><s:text name="validFlag"></s:text></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>
</div>
</s:i18n>

<div class="hide">
<s:url action="BINOLBSPOS01_list" id="positionListUrl"></s:url>
<a href="${positionListUrl }" id="positionListUrl"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />   