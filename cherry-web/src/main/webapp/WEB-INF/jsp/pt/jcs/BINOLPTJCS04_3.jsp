<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 产品查询URL --%>
<s:url id="search_url" value="/pt/BINOLPTJCS04_search" />
<input type="hidden" id="search_url" value="${search_url}" />
<script type="text/javascript">
$(function() {
	productBinding({elementId:"unitCode",showNum:20,targetShow:"unitCode"});//厂商编码
	productBinding({elementId:"barCode",showNum:20,targetShow:"barCode"});//产品条码
	productBinding({elementId:"nameTotal",showNum:20,targetShow:"nameTotal"});//产品名称
	// 	cntProductBinding({elementId:"nameTotal",showNum:20,targetShow:"nameTotal",counterCode:"NR111908"});//产品名称
	var url = $("#search_url").val();
	BINOLPTJCS04.search(url);
	// 输入框trim处理
	$(":text").bind('focusout',function(){ 
		var $this = $(this);$this.val($.trim($this.val()));});
} );
</script>
<!-- 停用或启用url -->
<s:url id="disOrEnable_Url" action="BINOLPTJCS04_disOrEnable" />
<!-- 查看相同产品是否存在有效数据 -->
<s:url id="getPrtDetail_Url" action="BINOLPTJCS04_getPrtDetail" />
<!-- 查看当前启用的条码是否存在且有效 -->
<s:url id="getPrtBarCodeVF_Url" action="BINOLPTJCS04_getPrtBarCodeVF" />
<span class="hide" id="disOrEnable">${disOrEnable_Url}</span>
<span class="hide" id="prtDetailUrl">${getPrtDetail_Url}</span>
<span class="hide" id="prtBarCodeVFUrl">${getPrtBarCodeVF_Url}</span>
<s:i18n name="i18n.pt.BINOLPTJCS04">
	<s:text id="selectAll" name="global.page.all" />
	<s:hidden id="isU2M" value="%{isU2M}" />
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="actionResultDisplay"></div>
	<div id="errorMessage"></div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
			<ul>
				<li><span><s:text name="JCS04.errorMessage" /></span></li>
			</ul>
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
		<div class="hide">
			<s:url id="export" action="BINOLPTJCS04_export"></s:url>
			<a id="downUrl" href="${export}"></a>
		</div>
		<div class="box-content clearfix">
			<cherry:form id="mainForm" class="inline" onsubmit="search(); return false;">
				<div class="box-header" style="padding-left: 10px;">
					<strong>
						<span class="ui-icon icon-ttl-search"></span> 
						<s:text name="global.page.condition" />
					</strong>
				</div>
				<table class="detail" cellpadding="0" cellspacing="0" style="margin: 0">
					<tbody>
						<tr>
							<td style="padding-left: 15px;">
								<label style="width: 65px;"> <s:text name="pro.brandInfo" /></label> <%-- 品牌 --%> 
								<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" id="brandInfoId" />
							</td>
							<td style="padding-left: 15px;">
								<label style="width: 65px;"><s:text name="pro.unitCode" /></label> <%-- 产品厂商编码 --%>
								<s:textfield name="unitCode" cssClass="text" />
							</td>
						</tr>
						<tr>
							<td style="padding-left: 15px;">
								<label style="width: 65px;"><s:text name="pro.mode" /></label> <%-- 产品类型--%> 
								<s:select name="mode" list='#application.CodeTable.getCodes("1136")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}" />
							</td>
							<td style="padding-left: 15px;">
								<label style="width: 65px;"><s:text name="pro.barCode" /></label> <%-- 产品条码--%>
								<s:textfield name="barCode" cssClass="text" />
							</td>
						</tr>
						<tr>
							<td style="padding-left: 15px;">
								<label style="width: 65px;"><s:text name="pro.status" /></label> <%-- 产品状态 --%> 
								<s:select name="status" list='#application.CodeTable.getCodes("1016")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}" />
							</td>
							<td style="padding-left: 15px;">
								<label style="width: 65px;"><s:text name="pro.nameTotal" /></label> <%-- 产品全名--%> 
								<s:textfield name="nameTotal" cssClass="text" maxlength="50" />
							</td>
						</tr>
						<tr>
							<td style="padding-left: 15px;">
								<label style="width: 65px;"><s:text name="pro.validFlag" /></label> <%-- 有效状态--%> 
								<s:select name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}" value="1" />
							</td>
							<td style="padding-left: 15px;">
								 <label style="width: 65px;"><s:text name="pro.product" /> <s:text name="pro.originalBrand" /></label><%-- 子品牌 --%>
								 <s:select name="originalBrand" list='#application.CodeTable.getCodes("1299")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}" />
							</td>
						</tr>
						<tr>
						    <td style="padding-left: 15px;">
						        <label style="width: 65px;"><s:text name="pro.isExchanged"/></label><%-- 可否用于积分兑换 --%>
       				            <s:select name="isExchanged" list='#application.CodeTable.getCodes("1220")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}" />
						    </td>
						    <td style="padding-left: 15px;"></td>
						</tr>
						<%-- 动态显示隐藏分类信息--%>
						<%-- 						<tr> 
							<td colspan="2">
								<div style="text-align: center; background: none repeat scroll 0% 0% rgb(238, 238, 238); padding: 3px 0px;">
									<a style="margin-left: 0px; font-size: 12px;height: 20px;width: 100%;" class="ui-select" onclick="dynamicShowHide();">
										<span id='cateText' style="min-width: 50px;" class="button-text choice">显示查询分类信息</span>
										<span  class="ui-icon ui-icon-triangle-1-n"></span>
									</a>
								</div>
							</td>
						</tr>  --%>
						<tr>
							<td colspan="2" id="dynamicShowHideCate">
								<%-- ======================= 产品分类开始  ========================================================== --%>
								<div class="detail_box2">
									<div class="section-header clearfix">
										<strong class="left"> 
											<span class="ui-icon icon-ttl-search"></span>
											<s:text name="JCS04_cateInfo" /> <%-- 产品分类 --%>
										</strong>
									</div> 
									<%-- ================== 错误信息提示 START ======================= --%>
									<div id="cateInfoMessDiv">
										<div id="errorMessageCate"></div>
										<div style="display: none" id="errorMessageTemp1">
											<div class="actionError">
												<ul>
													<li><span><s:text name="JCS04.cateInfoEmptyError" /></span></li>
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
				<p class="clearfix" style="margin-top: 10px;">
					<%--<cherry:show domId="">--%>
					<%-- 产品查询按钮 --%>
					<button class="right search" type="button" onclick="BINOLPTJCS04.search('${search_url}');return false;">
						<span class="ui-icon icon-search-big"></span>
						<span class="button-text"><s:text name="global.page.search" /></span>
					</button>
					<%--</cherry:show>--%>
				</p>
			</cherry:form>
		</div>
		<div id="section" class="section">
			<div class="section-header">
				<strong> 
					<span class="ui-icon icon-ttl-section-search-result"></span> <s:text name="global.page.list" />
				</strong>
			</div>
			<div class="section-content">
				<div class="toolbar clearfix">
					<span class="left"> 
						<cherry:show domId="BINOLPTJCS04EXP">
							<a id="export" class="export"> 
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><s:text name="global.page.export" /></span>
							</a>
						</cherry:show>
						 <s:url action="BINOLPTJCS04_delete" id="delProduct"></s:url> 
						 <cherry:show domId="BINOLPTJCS04BAT">
							<a href="" class="delete" onclick="BINOLPTJCS04.editBatchValidFlag('disable','${delProduct}');return false;">
								<span class="ui-icon icon-disable"></span>
								<span class="button-text"><s:text name="global.page.disable" /></span>
							</a>
						</cherry:show>
					</span> 
					<span class="right"> 
						<a class="setting">
							<span class="ui-icon icon-setting"></span> <%-- 列设置按钮  --%>
							<span class="button-text"><s:text name="global.page.colSetting" /></span>
						</a>
					</span>
				</div>

				<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
						<tr>
							<th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');" /></th>
							<th><s:text name="pro.number" /></th>
							<%-- 编号 --%>
							<th><s:text name="pro.nameTotalCN" /></th>
							<%-- 中文名称--%>
							<th><s:text name="pro.nameTotalEN" /></th>
							<%-- 英文名称--%>
							<th><s:text name="pro.unitCode" /></th>
							<%-- 产品厂商编码 --%>
							<th><s:text name="pro.barCode" /></th>
							<%-- 产品条码 --%>
							<th><s:text name="pro.originalBrand" /></th>
							<%-- 品牌 --%>
							<th><s:text name="pro.itemType" /></th>
							<%-- 品类  --%>
							<th><s:text name="JCS04.primaryCategoryBig" /></th>
							<%-- 大分类--%>
							<th><s:text name="JCS04.primaryCategoryMedium" /></th>
							<%-- 中分类 --%>
							<th><s:text name="JCS04.primaryCategorySmall" /></th>
							<%-- 小分类 --%>
							<th><s:text name="pro.mode" /></th>
							<%-- 产品类型 --%>
							<th><s:text name="pro.guige" /></th>
							<%-- 计量单位  --%>
							<th><s:text name="pro.standardCost" /></th>
							<%-- 成本价格 --%>
							<th><s:text name="pro.orderPrice" /></th>
							<%-- 采购价格 --%>
							<th><s:text name="pro.salePrice" /></th>
							<%-- 销售价格 --%>
							<th><s:text name="pro.memPrice" /></th>
							<%-- 会员价格 --%>
							<th><s:text name="pro.validFlag" /></th>
							<%-- 有效区分 --%>
							<th><s:text name="global.page.option" /></th>
							<%-- 操作 --%>
							<th><s:text name="pro.isExchanged"/></th><%-- 积分兑换 --%>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	<div class="hide" id="dialogInitDIV"></div>
	<div style="display: none;">
		<div id="dialogConfirm">
			<s:text name="global.page.goOn" />
		</div>
		<div id="dialogConfirmOK">
			<s:text name="global.page.ok" />
		</div>
		<div id="dialogCancel">
			<s:text name="global.page.cancle" />
		</div>
		<div id="enableValTitle">
			<s:text name="pro.enableValidFlag" />
		</div>
		<div id="disableValTitle">
			<s:text name="pro.disableValidFlag" />
		</div>
		<div id="confirIsDisable">
			<s:text name="JCS04.confirIsDisable" />
		</div>
		<div id="confirIsEnable">
			<s:text name="JCS04.confirIsEnable" />
		</div>

		<div id="disableTitle">
			<s:text name="JCS04.disableTitle" />
		</div>
		<div id="enableTitle">
			<s:text name="JCS04.enableTitle" />
		</div>
		<div id="disableMessage">
			<p class="message">
				<span><s:text name="JCS04.disableMessage" /></span>
			</p>
		</div>
		<div id="enableMessage">
			<p class="message">
				<span><s:text name="JCS04.enableMessage" /></span>
			</p>
		</div>
		<%-- <div id="dialogConfirm"><s:text name="global.page.ok" /></div> --%>
		<div id="dialogClose">
			<s:text name="global.page.close" />
		</div>
	</div>
	<div class="hide" id="dialogInit"></div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

