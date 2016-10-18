<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS16.js"></script>
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
<s:i18n name="i18n.pt.BINOLPTRPS16">
	<s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart" />
	<s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory" />
	<div class="hide">
		<s:url id="search_url" value="/pt/BINOLPTRPS16_search" />
		<a id="searchUrl" href="${search_url}"></a>
		<s:text name="RPS16_select" id="defVal" />
		<div id="RPS16_select">${defVal}</div>
		<%-- EXCEL导出URL --%>
		<s:url id="xls_url" value="/pt/BINOLPTRPS16_export" />
	</div>
	<div class="hide">
		<div id="deleteButton"><s:text name="global.page.delete" /></div><!-- 商品列表删除按钮 -->
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
			<cherry:form id="mainForm" class="inline" onsubmit="BINOLPTRPS16.search(); return false;">
				<input name="brandInfoId" id="brandInfoId" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
				<div class="box-header">
					<strong> <span class="ui-icon icon-ttl-search"></span> <%-- 查询条件 --%>
						<s:text name="RPS16_condition" />
					</strong>
				</div>
				<div class="box-content clearfix">
					<div style="padding: 15px 0px 5px;">
						<table class="detail" cellpadding="0" cellspacing="0" id="rps16searchId">
							<tbody>
								<tr>
									<!-- 盘点日期 -->
									<th><s:text name="RPS16_date" /></th>
									<td>
										<p id="dateCover" class="date">
											<span>
												<s:textfield id="startDate" name="startDate" cssClass="date" /> -
												<s:textfield id="endDate" name="endDate" cssClass="date" />
											</span>
										</p>
									</td>
								   <!-- 合并方式 -->
								   <th>
								  		<s:text name="RPS16_codeMergeType" />
								   </th>
								   <td>
									    <select id="codeMergeType" name="codeMergeType" style="width:120px;">
			                                <option value=""  <s:if test='"".equals(codeMergeType)'>selected</s:if>><s:text name="RPS16_codeMergeType_no" /></option>
			                                <option value="BarCode" <s:if test='"BarCode".equals(codeMergeType)'>selected</s:if>><s:text name="RPS16_codeMergeType_barcode" /></option>
			                                <option value="UnitCode" <s:if test='"UnitCode".equals(codeMergeType)'>selected</s:if>><s:text name="RPS16_codeMergeType_unitcode" /></option>
			                                <option value="Custom"><s:text name="RPS16_custom"/></option>
			                            </select>
								   </td>
								</tr>
								 <!-- 产品名称 -->
								<tr>
									<th>
										<s:text name="RPS16_productName"/>
									</th>
									<td colspan="3">
										<a class="add" onClick="BINOLPTRPS16.openProPopup();return false;">
											<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selPro" /></span>
										</a>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="checkbox" id="includeFlag" name="includeFlag" value="1"><s:text name="RPS16_exclude" />
									</td>
								</tr>
								<tr id="productionTr" class="hide" >
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
													<tbody id="production_ID"></tbody>
												</table>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td colspan="4" id="dynamicShowHideCate">
										<%-- ======================= 产品分类开始  ========================================================== --%>
										<div class="detail_box2">
											<div class="section-header clearfix">
												<strong class="left"> 
													<span class="ui-icon icon-ttl-search"></span>
													<s:text name="RPS16_cateInfo" /> <%-- 产品分类 --%>
												</strong>
											</div> 
											<%-- ================== 错误信息提示 START ======================= --%>
											<div id="cateInfoMessDiv">
												<div id="errorMessageCate"></div>
												<div style="display: none" id="errorMessageTemp1">
													<div class="actionError">
														<ul>
															<li><span><s:text name="RPS16_cateInfoEmptyError" /></span></li>
														</ul>
													</div>
												</div>
											</div>
											<%-- ================== 错误信息提示   END  ======================= --%>
											<!-- 产品信息分类 -->
											<div id="cateInfo" class="section-content">
												<jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS00_1.jsp" flush="true" />
											</div>
										</div>
									 <%-- ======================= 产品分类结束  ========================================================== --%>
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
						onclick="BINOLPTRPS16.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<%-- 查询 --%>
						<span class="button-text"><s:text name="RPS16_search" /></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="section" class="section hide">
			<div class="section-header">
				<strong> <span
					class="ui-icon icon-ttl-section-search-result"></span> <%-- 查询结果一览 --%>
					<s:text name="RPS16_results_list" />
				</strong>
			</div>
			<div class="section-content" id="result_list">
				<div class="toolbar clearfix">
					<div id="print_param_hide" class="hide">
						<input type="hidden" name="pageId" value="BINOLSTBIL10" />
					</div>
					<span style="margin-right: 10px;"> <a id="export"
						class="export"
						onclick="BINOLPTRPS16.exportExcel('${xls_url}');return false;">
							<span class="ui-icon icon-export"></span> <span
							class="button-text"><s:text name="global.page.exportExcel" /></span>
					</a>
					</span> <span id="headInfo" style=""></span> <span class="right"><a
						class="setting"><span class="ui-icon icon-setting"></span> <span
							class="button-text"> <%-- 设置列 --%> <s:text
									name="RPS16_colSetting" />
						</span></a></span>
				</div>
				<table id="dataTable" cellpadding="0" cellspacing="0" border="0"
					class="jquery_table" width="100%">
					<thead>
						<tr>
							<th><s:text name="RPS16_region" /></th>
							<%-- 区域 --%>
							<th><s:text name="RPS16_city" /></th>
							<%-- 城市 --%>
							<th><s:text name="RPS16_department" /></th>
							<%-- 部门类型 --%>
							<th><s:text name="RPS16_departName" /></th>
							<%-- 部门名称  --%>
							<th><s:text name="RPS16_stockTakingNo" /></th>
							<%-- 盘点单号 --%>
							<th><s:text name="RPS16_employeeName" /></th>
							<%-- 盘点员 --%>
							<th><s:text name="RPS16_date" /></th>
							<%-- 盘点日期 --%>
							<th><s:text name="RPS16_type" /></th>
							<%-- 盘点类型 --%>
							<th><s:text name="RPS16_Comments" /></th>
							<%-- 盘点原因 --%>
							<th><s:text name="RPS16_Quantity" /></th>
							<%-- 账面库存 --%>
							<th><s:text name="RPS16_realQuantity" /></th>
							<%-- 实盘数量 --%>
							<th><s:text name="RPS16_gainQuantity" /></th>
							<%-- 盘差 --%>							
							<th><s:text name="RPS16_profitKbn0" /></th>
							<%-- 盘盈 --%>
							<th><s:text name="RPS16_profitKbn1" /></th>
							<%-- 盘亏 --%>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ============ 弹出dataTable 产品共通导入 END =================== --%>
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
