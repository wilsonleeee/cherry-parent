<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>

<s:i18n name="i18n.mb.BINOLMBVIS02">
<s:text name="global.page.select" id="select_default"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
	  <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="mbvis02_visitMng"/> &gt; <s:text name="mbvis02_visitPlanDetail"/>
	       </span>
	    </div>
	  </div>
	  <div id="actionResultDisplay"></div>
	  <div class="panel-content clearfix">
        <cherry:form id="saveForm" class="inline" csrftoken="false" action="BINOLMBVIS02_updateInit">
			<s:hidden name="visitPlanId" value="%{visitPlanInfo.visitPlanId}"></s:hidden>
			<input type="hidden" id="parentCsrftoken" name="csrftoken"/>
			<div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="mbvis02_planTitle"/>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
	                <tr>
				      <th style="width: 15%"><s:text name="mbvis02_visitCategoryId"></s:text></th>
				      <td style="width: 85%"><span>
				      	<s:property value="visitPlanInfo.visitTypeName"/>
				      </span></td>
				    </tr>
				    <tr>
				      <th><s:text name="mbvis02_visitObjType" /></th>
					  <td colspan="3">
					    <span>
						    <s:hidden name="visitObjType" value="%{visitPlanInfo.visitObjType}"></s:hidden>
						    <s:if test="%{visitPlanInfo.visitObjType == 1}">
						      <s:text name="mbvis02_visitObjType1" />：
						      <label id="visitObjLabel"></label>
						      <s:hidden name="visitObjJson" value="%{visitPlanInfo.visitObjJson}"></s:hidden>
						    </s:if>
						    <s:elseif test="%{visitPlanInfo.visitObjType == 2}">
						      <s:text name="mbvis02_visitObjType2" />：
						      <label id="visitObjNameLabel">
						        <s:property value="visitPlanInfo.visitObjName"/>
						      </label>
						      <s:hidden name="visitObjCode" value="%{visitPlanInfo.visitObjCode}"></s:hidden>
						    </s:elseif>
					    </span>
					    <span style="float:right;">
					    	<label></label>
					    	<a id="searchVisitObj" class="search">
					          <span class="ui-icon icon-search"></span><span class="button-text"><s:text name="mbvis02_searchVisitObj" /></span>
					        </a>
					    </span>
					  </td>
				    </tr>
				    <tr>
				      <th><s:text name="mbvis02_visitDateType"></s:text></th>
					  <td><span>
					    <s:if test="%{visitPlanInfo.visitDateType == 1}">
					      <s:text name="mbvis02_visitDateType1" />：
					      <s:property value="visitPlanInfo.visitStartDate"/>~<s:property value="visitPlanInfo.visitEndDate"/>
					    </s:if>
					    <s:elseif test="%{visitPlanInfo.visitDateType == 2 || visitPlanInfo.visitDateType == 3}">
					      <s:if test="%{visitPlanInfo.visitDateType == 2}"><s:text name="mbvis02_visitDateType2" />：</s:if>
					      <s:elseif test="%{visitPlanInfo.visitDateType == 3}"><s:text name="mbvis02_visitDateType3" />：</s:elseif>
					      
					      <s:if test="%{visitPlanInfo.visitDateRelative == 1}"><s:text name="mbvis02_visitDateRelative1" /></s:if>
					      <s:elseif test="%{visitPlanInfo.visitDateRelative == 2}"><s:text name="mbvis02_visitDateRelative2" /></s:elseif>
					      <s:property value="visitPlanInfo.visitDateValue"/>
					      <s:if test="%{visitPlanInfo.visitDateUnit == 1}"><s:text name="mbvis02_visitDateUnit1" /></s:if>
					      <s:elseif test="%{visitPlanInfo.visitDateUnit == 2}"><s:text name="mbvis02_visitDateUnit2" /></s:elseif>
					      <label><s:text name="mbvis02_endDate" /></label>
					      <s:property value="visitPlanInfo.validValue"/>
					      <s:if test="%{visitPlanInfo.validUnit == 1}"><s:text name="mbvis02_visitDateUnit1" /></s:if>
					      <s:elseif test="%{visitPlanInfo.validUnit == 2}"><s:text name="mbvis02_visitDateUnit2" /></s:elseif>
					    </s:elseif>
					  </span></td>
				    </tr>
				    <tr>
				      <th><s:text name="mbvis02_paperId"></s:text></th>
				      <td><span>
				      	<s:property value="visitPlanInfo.paperName"/>
				      </span></td>
				    </tr>
				    <tr>
				      <th><s:text name="mbvis02_visitDes"></s:text></th>
				      <td><span>
				        <s:property value="visitPlanInfo.visitDes"/>
				      </span></td>
				    </tr>
                </table>
			  </div>
			</div>
			<div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="mbvis02_planStrategy"/>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
	                <tr>
				      <th style="width: 15%"><s:text name="mbvis02_startDate"></s:text></th>
				      <td style="width: 85%"><span>
				      	<s:property value="visitPlanInfo.startDate"/>
				      </span></td>
				    </tr>
				    <tr>
				      <th><s:text name="mbvis02_endDate"></s:text></th>
				      <td><span>
					    <s:if test="%{visitPlanInfo.endDate != null}"><s:property value="visitPlanInfo.endDate"/></s:if>
					    <s:else><s:text name="mbvis02_planDate1" /></s:else>
					  </span></td>
				    </tr>
                </table>
			  </div>
			</div>
			
			<div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="mbvis02_otherInfo"/>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
	                <tr>
				      <th style="width: 15%"><s:text name="mbvis02_planDateTime"></s:text></th>
				      <td style="width: 85%"><span>
				      	<s:property value="visitPlanInfo.planDateTime"/>
				      </span></td>
				    </tr>
				    <tr>
				      <th><s:text name="mbvis02_employeeName"></s:text></th>
				      <td><span>
					    <s:property value="visitPlanInfo.employeeName"/>
					  </span></td>
				    </tr>
                </table>
			  </div>
			</div>
		</cherry:form>	
            
            <div class="center clearfix">
	            <button class="edit" type="button" id="editButton">
            		<span class="ui-icon icon-edit-big"></span>
            		<span class="button-text"><s:text name="global.page.edit"/></span>
             	</button>
	            <button class="close" type="button" onclick="window.close();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
      </div>
</div>
</div>

<div class="hide">
<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<div id="dialogClose"><s:text name="global.page.dialogClose" /></div>
<div id="dialogTitle"><s:text name="mbvis02_setVisitObj" /></div>
<div id="dialogTitle1"><s:text name="mbvis02_visitObjType" /></div>
</div>
</s:i18n>

<script type="text/javascript">
$(function(){
	
	// 取得查询条件描述信息
	function getConInfo(json) {
		var url = getBaseUrl()+'/common/BINOLCM33_conditionDisplay';
		var params = "reqContent=" + json;
		cherryAjaxRequest({
			url: url,
			param : params,
			callback: function(msg) {
				$("#visitObjLabel").html(msg);
			}
		});
	}
	
	// 查询回访对象
	$("#searchVisitObj").click(function(){
		var visitObjType = $("#visitObjType").val();
		var visitObjJson = $("#visitObjJson").val();
		var visitObjCode = $("#visitObjCode").val();
		if(visitObjType) {
			if(visitObjType == '1') {
				if(!visitObjJson) {
					return false;
				}
			} else if(visitObjType == '2') {
				if(!visitObjCode) {
					return false;
				}
			}
		} else {
			return false;
		}
		if($("#searchMemDialog").length == 0) {
			$("body").append('<div style="display:none" id="searchMemDialog"></div>');
		} else {
			$("#searchMemDialog").empty();
		}
		var dialogSetting = {
			dialogInit: "#searchMemDialog",
			text: '',
			width: 	800,
			height: 430,
			title: 	$("#dialogTitle1").text(),
			confirm: $("#dialogClose").text(),
			confirmEvent: function(){removeDialog("#searchMemDialog");}
		};
		openDialog(dialogSetting);
		var callback = function(msg) {
			$("#searchMemDialog").html(msg);
			
			if(oTableArr[10]) {
				oTableArr[10] = null;
			}
			var searchUrl = getBaseUrl()+'/mb/BINOLMBVIS02_searchMem' + '?' + getSerializeToken() + '&' + $("#visitObjType").serialize();
			if(visitObjType == '1') {
				searchUrl += '&' + $("#visitObjJson").serialize();
			} else if(visitObjType == '2') {
				searchUrl += '&' + $("#visitObjCode").serialize();
			}
			var tableSetting = {
					 // 表格ID
					 tableId : '#memSearchDataTable',
					 // 一页显示页数
					 iDisplayLength:10,
					 // 数据URL
					 url : searchUrl,
					 // 表格默认排序
					 aaSorting : [[ 4, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false}, 	
					                { "sName": "memCode", "sWidth": "15%", "bSortable": false},
									{ "sName": "memName", "sWidth": "15%", "bSortable": false},    
									{ "sName": "mobilePhone", "sWidth": "15%", "bSortable": false},
									{ "sName": "joinDate", "sWidth": "15%"},
									{ "sName": "birthDay", "sWidth": "15%", "bSortable": false},
									{ "sName": "counterCode", "sWidth": "15%", "bSortable": false}],               
					index:10
			 };
			// 调用获取表格函数
			getTable(tableSetting);
		};
		var url = getBaseUrl()+'/mb/BINOLMBVIS02_searchMemInit';
		cherryAjaxRequest({
			url: url,
			param: null,
			callback: callback
		});
	});
	
	$("#editButton").click(function(){
		var parentToken = parentTokenVal();
		$("#parentCsrftoken").val(parentToken);
		$("#saveForm").submit();
	});
	
	if($("#visitObjJson").val()) {
		getConInfo($("#visitObjJson").val());
	}
});

</script>
