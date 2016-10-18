<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM45.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<style>
	#DEPARTLINE{
		margin-left: 90px;
		margin-right: 5px;
		margin-bottom: 5px;
	}
	#RANGELABLE{
		width: 65px; 
		float:left;
		margin-top: 4px;
		text-align: right;
		padding-left:15px;
	}
	#DEPARTLINE span{
		margin-right: 2px;
	}
</style>
<s:i18n name="i18n.ss.BINOLSSPRM45">

	<%--大中小分类 --%>
	<s:url id="url_getMCategoryAjax" value="/ss/BINOLSSPRM21_GETSECONDCATEGORY" />
	<span id ="s_getMCategoryAjax" style="display:none">${url_getMCategoryAjax}</span>
	
	<s:url id="url_getSCategoryAjax" value="/ss/BINOLSSPRM21_GETSMALLCATEGORY" />
	<span id ="s_getSCategoryAjax" style="display:none">${url_getSCategoryAjax}</span>

	<s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart" />
	<s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory" />
	<div class="hide">
		<s:url id="search_url" value="/ss/BINOLSSPRM45_search" />
		<a id="searchUrl" href="${search_url}"></a>
		<s:text name="PRM45_select" id="defVal" />
		<div id="PRM45_select">${defVal}</div>
		<%-- EXCEL导出URL --%>
		<s:url id="xls_url" value="/ss/BINOLSSPRM45_export" />
	</div>
	<div class="panel-header">
		<%-- 盘点单查询 --%>
		<div class="clearfix">
			<span class="breadcrumb left"> <jsp:include
					page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
		</div>
	</div>
	<div id="errorMessage"></div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="mainForm" class="inline" onsubmit="BINOLSSPRM45.search(); return false;">
				<input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
				<div class="box-header">
					<strong> <span class="ui-icon icon-ttl-search"></span> <%-- 查询条件 --%>
						<s:text name="PRM45_condition" />
					</strong>
				</div>
				<div class="box-content clearfix">
					<div style="padding: 15px 0px 5px;">
						<table class="detail" cellpadding="0" cellspacing="0" id="prm45searchId">
							<tbody>
								<tr>
									<th>
										<!-- 盘点日期 -->
										<s:text name="PRM45_date" />
									</th>
									<td>
										<p id="dataCover" class="date">
											<span><s:textfield id="startDate" name="startDate" cssClass="date" /> - 
											<s:textfield id="endDate" name="endDate" cssClass="date" /></span>
										</p>
									</td>
									<th>
										<!-- 大分类 -->
										<s:text name="PRM45_lblLCategory" />
									</th>
									<td>
										<select style="width: 100px;" name="largeCategory" id="largeCategory" onchange="BINOLSSPRM45.choosePrimaryCategory();return false;">
											<option value=""><s:text name="PRM45_textAll" /></option>
											<s:iterator value="largeCategoryList">
												<option value="<s:property value="PrimaryCategoryCode" />">
													<s:property value="PrimaryCategoryName" />
												</option>
											</s:iterator>
										</select>
									</td>
								</tr>
								<tr>
									<th>
										<!-- 合并方式 -->
										<s:text name="PRM45_codeMergeType" />
									</th>
									<td>
										<select id="codeMergeType" name="codeMergeType" style="width:120px;">
			                                <option value=""  <s:if test='"".equals(codeMergeType)'>selected</s:if>><s:text name="PRM45_codeMergeType_no" /></option>
			                                <option value="BarCode" <s:if test='"BarCode".equals(codeMergeType)'>selected</s:if>><s:text name="PRM45_codeMergeType_barcode" /></option>
			                                <option value="UnitCode" <s:if test='"UnitCode".equals(codeMergeType)'>selected</s:if>><s:text name="PRM45_codeMergeType_unitcode" /></option>
			                                <option value="Custom"><s:text name="PRM45_custom"/></option>
			                            </select>
									</td>
									<th>
										<!-- 中分类 -->
										<s:text name="PRM45_lblMCategory" />
									</th>
									<td>
										<select	style="width: 100px;" name="middleCategory" id="middleCategory" onchange="BINOLSSPRM45.chooseSecondCategory();return false;">
											<option value=""><s:text name="PRM45_textAll" /></option>
										</select>
									</td>
								</tr>
								<tr>
									<th>
										<s:text name="global.page.selPrm" />
									</th>
									<td>
										<a class="add" onClick="BINOLSSPRM45.openPrmPopup();return false;">
											<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selPrm" /></span>
										</a>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="checkbox" id="includeFlag" name="includeFlag" value="1"><s:text name="PRM45_exclude" />
									</td>
									<th>
										<!-- 小分类 -->
										<s:text name="PRM45_lblSCategory" />
									</th>
									<td>
										<select style="width: 100px;" name="smallCategory" id="smallCategory">
											<option value=""><s:text name="PRM45_textAll" /></option>
										</select>
									</td>
								</tr>
								<tr id="proPrmTr" class="hide">
									<td style="padding:3px;" colspan="4" class="detail box2-active">
										<div class="detail_box2" >
											<div class="section-content">
												<table cellspacing="0" cellpadding="0" style="width:100%;" id="proTable">
													<thead>
														<tr>
														  <th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>
														  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
														  <th style="width:35%;"><s:text name="global.page.productname" /></th>
														  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
														</tr>
													</thead>
													<tbody id="promotion_ID"></tbody>
												</table>
											</div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<%-- ======================= 组织联动共通导入开始  ============================= --%>
					<s:action name="BINOLCM13_query" namespace="/common"
						executeResult="true">
						<s:param name="businessType">1</s:param>
						<s:param name="operationType">1</s:param>
					</s:action>
					<%-- ======================= 组织联动共通导入结束  ============================= --%>
				</div>
				<p class="clearfix">
					<button class="right search" type="submit"
						onclick="BINOLSSPRM45.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<%-- 查询 --%>
						<span class="button-text"><s:text name="PRM45_search" /></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="section" class="section hide">
			<div class="section-header">
				<strong> <span
					class="ui-icon icon-ttl-section-search-result"></span> <%-- 查询结果一览 --%>
					<s:text name="PRM45_results_list" />
				</strong>
			</div>
			<div class="section-content" id="result_list">
				<div class="toolbar clearfix">
					<div id="print_param_hide" class="hide">
						<input type="hidden" name="pageId" value="BINOLSTBIL10" />
					</div>
					<span style="margin-right: 10px;"> <a id="export"
						class="export"
						onclick="BINOLSSPRM45.exportExcel('${xls_url}');return false;">
							<span class="ui-icon icon-export"></span> <span
							class="button-text"><s:text name="global.page.exportExcel" /></span>
					</a>
					</span> <span id="headInfo" style=""></span> <span class="right"><a
						class="setting"><span class="ui-icon icon-setting"></span> <span
							class="button-text"> <%-- 设置列 --%> <s:text
									name="PRM45_colSetting" />
						</span></a></span>
				</div>
				<table id="dataTable" cellpadding="0" cellspacing="0" border="0"
					class="jquery_table" width="100%">
					<thead>
						<tr>
							<th><s:text name="PRM45_region" /></th>
							<%-- 区域 --%>
							<th><s:text name="PRM45_city" /></th>
							<%-- 城市 --%>
							<th><s:text name="PRM45_department" /></th>
							<%-- 部门类型 --%>
							<th><s:text name="PRM45_departName" /></th>
							<%-- 部门名称  --%>
							<th><s:text name="PRM45_stockTakingNo" /></th>
							<%-- 盘点单号 --%>
							<th><s:text name="PRM45_employeeName" /></th>
							<%-- 盘点员 --%>
							<th><s:text name="PRM45_date" /></th>
							<%-- 盘点日期 --%>
							<th><s:text name="PRM45_type" /></th>
							<%-- 盘点类型 --%>
							<th><s:text name="PRM45_Comments" /></th>
							<%-- 盘点原因 --%>
							<th><s:text name="PRM45_Quantity" /></th>
							<%-- 账面库存 --%>
							<th><s:text name="PRM45_realQuantity" /></th>
							<%-- 实盘数量 --%>
							<th><s:text name="PRM45_gainQuantity" /></th>
							<%-- 盘差 --%>							
							<th><s:text name="PRM45_profitKbn0" /></th>
							<%-- 盘盈 --%>
							<th><s:text name="PRM45_profitKbn1" /></th>
							<%-- 盘亏 --%>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	<div class="hide">
		<div id="deleteButton"><s:text name="global.page.delete" /></div>商品列表删除按钮
	</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ============ 弹出dataTable 促销品共通导入 START ================= --%>
<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />
<%-- ============ 弹出dataTable 促销品共通导入 END =================== --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
<script type="text/javascript">
        // 节日
        var holidays = '${holidays }';
        $('#startDate').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#endDate').val();
                return [value,'maxDate'];
            }
        });
        $('#endDate').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#startDate').val();
                return [value,'minDate'];
            }
        });
    </script>
<input type="hidden" id="defStartDate" value='' />
<input type="hidden" id="defEndDate" value='' />