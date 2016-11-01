<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRSRP09">
	<s:text name="global.page.select" id="select_default"></s:text>
	<div class="panel-header">
		<div class="clearfix">
		<span class="breadcrumb left">
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRSRP09_wreport"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRSRP09_saleInfo"></s:text>
		</span>
		</div>
	</div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="queryParam" class="inline">
				<div class="box-header">
					<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
				</div>
				<div class="box-content clearfix">
					<div class="column" style="width:50%; height: auto;">
						<p>
							<label><s:text name="WRSRP09_saleDate"/></label>
							<s:textfield name="saleDateStart" cssClass="date"/>-<s:textfield name="saleDateEnd" cssClass="date"/>
						</p>
					</div>
					<div class="column last" style="width:49%; height: auto;">
						<p>
							<label><s:text name="WRSRP09_employeeId"/></label>
							<s:hidden name="employeeName"></s:hidden>
							<s:select list="employeeList" listKey="baInfoId" listValue="baName" name="employeeId" headerKey="" headerValue="%{#select_default}"></s:select>
						</p>
					</div>
				</div>
				<p class="clearfix">
					<button class="right search" id="searchButton">
						<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="resultList"></div>
	</div>
</s:i18n>

<div class="hide">
	<s:a action="BINOLWRSRP09_search" id="searchSaleUrl"></s:a>
</div>
<div class="hide">
	<div id="loading"><s:text name="global.page.loading" /></div>
</div>
<script type="text/javascript">
	$(function(){

		$('#saleDateStart').cherryDate({
			beforeShow: function(input){
				var value = $('#saleDateEnd').val();
				return [value,'maxDate'];
			}
		});
		$('#saleDateEnd').cherryDate({
			beforeShow: function(input){
				var value = $('#saleDateStart').val();
				return [value,'minDate'];
			}
		});

		$("#employeeId").change(function(){
			if($(this).val() != '') {
				$("#employeeName").val($(this).find("option:selected").text());
			} else {
				$("#employeeName").val('');
			}
		});

		// 表单验证配置
		cherryValidate({
			formId: 'queryParam',
			rules: {
				saleDateStart: {dateValid: true},
				saleDateEnd: {dateValid: true}
			}
		});

		$("#searchButton").click(function(){
			// 表单验证
			if(!$("#queryParam").valid()) {
				return false;
			}
			searchList();
			return false;
		});

		function searchList() {
			var url = $("#searchSaleUrl").attr("href");
			var params = $("#queryParam").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			$("#resultList").html($("#loading").html());
			var callback = function(msg) {
				$("#resultList").html(msg);
			};
			cherryAjaxRequest({
				url: url,
				param: null,
				callback: callback
			});
		}
	});
</script>
