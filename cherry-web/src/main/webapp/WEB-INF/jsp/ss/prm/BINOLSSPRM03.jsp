<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp"
	flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM03.js"></script>
<%-- 日历 --%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<%-- 保存按钮 --%>
<s:url id="save_url" action="BINOLSSPRM03_save" />
<%-- 查询中分类--%>
<s:url id="querySecondCate" action="BINOLCM05_querySecondCate"
	namespace="/common" />
<%-- 查询小分类--%>
<s:url id="querySmallCate" action="BINOLCM05_querySmallCate"
	namespace="/common" />
<s:i18n name="i18n.ss.BINOLSSPRM03">
	<s:if test="prmInfo.promCate.equals('TZZK')">
		<s:set id="disabled" value="true" />
	</s:if>
	<s:else>
		<s:set id="disabled" value="false" />
	</s:else>
	<div class="hide">
		<s:text name="prm03_select" id="prm03_select" />
		<s:text name="prm03_filterDepart" id="defVal" />
		<div id="prm03_filterDepart">${defVal}</div>
	</div>
	<div class="main container clearfix">
		<div class="panel ui-corner-all">
			<div class="panel-header">
				<div class="clearfix">
					<span class="breadcrumb left"> <span
						class="ui-icon icon-breadcrumb"></span> <s:text
							name="prm03_manage" /> &gt; <s:text name="prm03_edit" />
					</span>
				</div>
			</div>
			<div class="panel-content" id="div_main">
				<div id="actionResultDisplay"></div>
				<form id="toDetailForm" action="BINOLSSPRM04_init" method="post">
					<%-- 促销品ID --%>
					<input type="hidden" name="promotionProId"
						value="${prmInfo.promotionProId}" />
					<%-- 品牌ID --%>
					<input type="hidden" name="brandInfoId"
						value="${prmInfo.brandInfoId}" />
					<input type="hidden" name="csrftoken" id="parentCsrftoken" />
				</form>
				<div class="section">
					<div class="section-header">
						<strong><span class="ui-icon icon-ttl-section-info"></span>
							<%-- 基本信息 --%> <s:text name="global.page.title" /></strong>
					</div>
					<s:if test='"1".equals(prmInfo.editFlag)'>
						<div id="errorMessage">
							<div class="actionError">
								<ul>
									<li><span> 
											<s:if
												test='"1".equals(prmInfo.editFlag)'>
												<s:text name="PRM03.warning" />
											</s:if>
									</span></li>
									<s:if
										test='"1".equals(prmInfo.editFlag) && "TZZK".equals(prmInfo.promCate)'>
										<li><span><s:text name="PRM03.TZZKMsg" /></span></li>
									</s:if>
									<s:if
										test='"1".equals(prmInfo.editFlag) && "DHCP".equals(prmInfo.promCate)'>
										<li><span><s:text name="PRM03.DHCPMsg" /></span></li>
									</s:if>
								</ul>
							</div>
						</div>
					</s:if>

					<div class="section-content">
						<cherry:form id="mainForm" method="post" class="inline"
							csrftoken="false">
							<table id="basicInfo" class="detail" cellpadding="0"
								cellspacing="0">
								<tr>
									<th>
										<%-- 品牌--%> <s:text name="prm03.brandName" />
									</th>
									<td><span> <s:select id="brandSel"
												name="brandInfoId" list="brandInfoList"
												listKey="brandInfoId" listValue="brandName"
												value="%{prmInfo.brandInfoId}" disabled="true" tabindex="1" />
											<input type="hidden" name="brandInfoId"
											value="<s:property value='prmInfo.brandInfoId' />" />
									</span></td>
									<th>
										<%-- 大分类名称--%> <s:text name="prm03.primaryCategoryName" />
									</th>
									<td><span> <s:if
												test="null != primaryCateList && !primaryCateList.isEmpty()">
												<s:select id="priCateSel" name="primaryCateCode"
													list="PrimaryCateList" listKey="primaryCateCode"
													listValue="primaryCateName"
													onchange="doSecondCate(this, '%{querySecondCate}');"
													headerKey="" headerValue="%{prm03_select}"
													value="%{prmInfo.primaryCateCode}" tabindex="13" />
											</s:if> <s:else>
												<select id="priCateSel" name="primaryCateCode" tabindex="13">
													<option value="">
														<s:text name="prm03_select" />
													</option>
												</select>
											</s:else>
									</span></td>
								</tr>
								<tr>
									<th><label><s:text name="PRM03.promCate" /></label>
									<%-- 促销品类型 --%></th>
									<td><span> <s:select name="promCate1" id="promCate1"
												list='#application.CodeTable.getCodes("1139")' tabindex="2"
												listKey="CodeKey" listValue="Value"
												value="%{prmInfo.promCate}"
												onchange="changeCate(this);return false;" disabled="true"/> 	
											<input type="hidden" name="promCate" id="promCate" value="${prmInfo.promCate}"/>
									</span></td>
									<th>
										<%-- 中分类名称--%> <s:text name="prm03.secondryCategoryName" />
									</th>
									<td><span> <s:if
												test="null != secondCateList && !secondCateList.isEmpty()">
												<s:select id="secCateSel" name="secondCateCode"
													list="SecondCateList" listKey="secondCateCode"
													listValue="secondCateName"
													onchange="removeErrorSpan(this);doSmallCate(this, '%{querySmallCate}');"
													headerKey="" headerValue="%{prm03_select}"
													value="%{prmInfo.secondCateCode}" tabindex="14" />
											</s:if> <s:else>
												<select id="secCateSel" name="secondCateCode"
													onchange="removeErrorSpan(this);doSmallCate(this, '${querySmallCate}');"
													tabindex="14">
													<option value="">
														<s:text name="prm03_select" />
													</option>
												</select>
											</s:else>
									</span></td>
								</tr>
								<tr>
									<th>
										<%--  厂商编码 --%> <s:text name="prm03.unitCode" /> <span
										class="highlight"><s:text name="global.page.required" /></span>
									</th>
									<td>
										<%-- 促销产品ID --%> 
										<input type="hidden" name="promotionProId" value="${prmInfo.promotionProId}" /> 
										<%-- 更新日时 --%> 
										<input type="hidden" name="prmUpdateTime" value="${prmInfo.prmUpdateTime}" /> 
										<%-- 更新次数 --%> 
										<input type="hidden" name="prmModifyCount" value="${prmInfo.prmModifyCount}" /> 
										<%-- 更新前的厂商编码  --%> 
										<input type="hidden" name="oldUnitCode" value="<s:property value='prmInfo.unitCode'/>" /> 
										<%-- 更新前的有效区分  --%>
										<input type="hidden" name="prevValidFlag" value="<s:property value='prmInfo.validFlag'/>" /> 
										<%-- 可编辑标志  --%>
										<input type="hidden" name="editFlag" value="<s:property value='prmInfo.editFlag'/>" /> 
										<%-- 下发后可编辑标志  --%>
										<%-- <input type="hidden" name="sendEditFlag" value="<s:property value='prmInfo.sendEditFlag'/>"/>  --%>
										<span>
										<s:if test="promUBRule == 1">
											<s:if test='"1".equals(prmInfo.editFlag) || "ZDTL".equals(prmInfo.promCate)'>
												<s:property value='%{prmInfo.unitCode}' />
											</s:if> 
											<s:else>
												 
													<s:textfield name="unitCode" cssClass="text"
														maxlength="20" value="%{prmInfo.unitCode}" tabindex="3" onkeyup="this.value=this.value.toUpperCase()" />
												
											</s:else>
										</s:if>
										<s:elseif test="promUBRule == 2">	
											<s:property value='%{prmInfo.unitCode}' />
											<s:hidden name="unitCode" value="%{prmInfo.unitCode}"></s:hidden>
										</s:elseif>
										</span>
									</td>
									<th>
										<%-- 小分类名称--%> <s:text name="prm03.smallCategoryName" />
									</th>
									<td><span> <s:if
												test="null != smallCateList && !smallCateList.isEmpty()">
												<s:select id="smallCateSel" name="smallCateCode"
													list="SmallCateList" listKey="smallCateCode"
													listValue="smallCateName" onchange="removeErrorSpan(this);"
													headerKey="" headerValue="%{prm03_select}"
													value="%{prmInfo.smallCateCode}" tabindex="15" />
											</s:if> <s:else>
												<select id="smallCateSel" name="smallCateCode"
													onchange="removeErrorSpan(this);" tabindex="15">
													<option value="">
														<s:text name="prm03_select" />
													</option>
												</select>
											</s:else>
									</span></td>
								</tr>
								<tr>
									<th>
										<%--  促销产品条码--%> <s:text name="prm03.barCode" /> <span
										class="highlight"><s:text name="global.page.required" /></span>
									</th>
									<td>
										<div id="prtVendor">
											<%--
						  						<div id="prmFacValid" > <label class="hide left" id="usedBarcode" ><s:text name="PRM03.usedBarcode" /></label>
	                   		                    <div class="clear"></div> 
                   		                    --%>
											<s:iterator value="prmFacList" id="prmFacValid" status="R">
												<span style="padding-bottom: 2px;"
													
													<s:if test='"0".equals(#prmFacValid.facValidFlag)'>class="hide vendorSpan"</s:if>
													<s:else>class="vendorSpan"</s:else>
												> 
													<%-- 促销产品厂商ID--%>
													<input type="hidden" name="prmVendorId" value="${prmFacValid.prmPrtVendorId}" /> 
													<%-- 促销产品条码--%> 
													<input type="hidden" name="barCode" value="<s:property value='#prmFacValid.barCode'/>" /> 
													<%-- 生产厂商更新日时--%>
													<input type="hidden" name="facUpdateTime" value="${prmFacValid.facUpdateTime}" /> 
													<%-- 生产厂商更新次数--%> 
													<input type="hidden" name="facModifyCount" value="${prmFacValid.facModifyCount}" /> 
													<%-- 有效区分--%> 
													<input type="hidden" name="facValidFlag" value="<s:property value='#prmFacValid.facValidFlag'/>" />
													<%-- 删除标志 --%> 
													<input type="hidden" name="deleteFlag" value="1" /> 
													<span style="padding-bottom: 2px;"> 
													<%-- 促销品新条码 --%>
													<s:if test="promUBRule == 1">
														<s:if test='"1".equals(prmInfo.editFlag) || "ZDTL".equals(prmInfo.promCate)'>
															<label style="margin-right: 10px;"><s:property value='#prmFacValid.barCode' /></label>
														</s:if> 
														<s:else>
															<input name="newBarCode" class="text" maxlength="13" value="<s:property value='#prmFacValid.barCode'/>"
																tabindex="4" onkeyup="this.value=this.value.toUpperCase()" />
															<%--
			                  									<input style="width:109px; height:14px;" name="newBarCode" type="text" value="<s:property value='#prmFacValid.barCode'/>" tabindex="4"/>
                      											<span class="close" onClick="deleteBarCode(this)"><span title="停用" class="ui-icon ui-icon-close"></span></span>
                      										 --%>
														</s:else>
													</s:if>
													<s:elseif test="promUBRule == 2">
															<label style="margin-right: 10px;"><s:property value='#prmFacValid.barCode' /></label>
															<input type="hidden" name="newBarCode" value="<s:property value='#prmFacValid.barCode'/>"></input>
													</s:elseif>
													</span>
												</span>
											</s:iterator>
										</div>
										<div class="clear"></div> 
										<label class="hide left" id="validBarcode"><s:text name="PRM03.info" /></label>
										<div class="clear"></div>
										<div id="prmFacInvalid">
											<s:iterator value="prmFacList" id="prmFacInvalid" status="R">
												<span style="padding-bottom: 2px;"
													<s:if test='("1".equals(#prmFacInvalid.facValidFlag) || ("1".equals(prmInfo.editFlag) || "ZDTL".equals(prmInfo.promCate)))'>class="hide vendorSpan"</s:if>
													<s:else>class="vendorSpan"</s:else>> 
													<%-- 促销产品厂商ID--%>
													<input type="hidden" name="prmVendorId" value="${prmFacInvalid.prmPrtVendorId}" /> 
													<%-- 促销产品条码--%>
													<input type="hidden" name="barCode" value="<s:property value='#prmFacInvalid.barCode'/>" /> 
													<%-- 生产厂商更新日时--%>
													<input type="hidden" name="facUpdateTime" value="${prmFacInvalid.facUpdateTime}" /> 
													<%-- 生产厂商更新次数--%>
													<input type="hidden" name="facModifyCount" value="${prmFacInvalid.facModifyCount}" /> 
													<%-- 有效区分--%> 
													<input type="hidden" name="facValidFlag" value="<s:property value='#prmFacInvalid.facValidFlag'/>" />
													<span> 
													<%-- 启用标志 --%> 
													<input type="hidden" name="validPrmFlag" value="0" /> 
													<%-- 促销品新条码 --%> 
													<input style="width: 109px; height: 14px;" readonly="readonly" name="newBarCode" type="text"
														value="<s:property value='#prmFacInvalid.barCode'/>"
														tabindex="4" /> 
													<span onclick="startBarCode(this);return false;" class="close">
														<span title="启用" class="ui-icon ui-icon-plus"></span>
													</span>
													</span>
												</span>
											</s:iterator>
										</div>
										</div>
										<div class="clear"></div> <s:if
											test='!"1".equals(prmInfo.editFlag) && !"ZDTL".equals(prmInfo.promCate)'>
											<%-- <div style="width:285px;"><div class="right"><span class="ui-icon icon-add"></span><a href="javascript:void(0)" onClick="javascript:$('#addBarCode').show();return false;"><s:text name="prm03_addBarCode"/></a></div></div> --%>
											<div class="clear"></div>
											<span class="hide" id="addBarCode"> 
												<span> 
													<input name="barCodeAdd" value="" tabindex="4" id="barCode" class="text" type="text">
												</span> 
											<br> 
												<span style="font-size: 12px; color: #808080; clear: both;">
														<s:text name="prm03_barCodeText" />
												</span>
											</span>
										</s:if>
									</td>
									<th>
										<%--  促销品容量 --%> <s:text name="prm03.volume" />
									</th>
									<td><span> <s:textfield name="volume"
												cssClass="number" maxlength="9" value="%{prmInfo.volume}"
												tabindex="16" disabled="#disabled" /> <s:select
												name="volumeUnit"
												list='#application.CodeTable.getCodes("1028")'
												listKey="CodeKey" listValue="Value" cssStyle="width:auto;"
												value="%{prmInfo.volumeUnitMeasureCode}" tabindex="17"
												disabled="#disabled" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%--  促销品全称 --%> <s:text name="prm03.nameTotal" /> <span
										class="highlight"><s:text name="global.page.required" /></span>
									</th>
									<td><span> <s:if
												test='"ZDTL".equals(prmInfo.promCate)'>
												<s:textfield name="nameTotal" cssClass="text" maxlength="40"
													value="%{prmInfo.nameTotal}" tabindex="5" readonly="true" />
											</s:if> <s:else>
												<s:textfield name="nameTotal" cssClass="text" maxlength="40"
													onkeyup="return isMaxByteLen(this)"
													value="%{prmInfo.nameTotal}" tabindex="5" />
											</s:else>
									</span></td>
									<th>
										<%--  促销品重量 --%> <s:text name="prm03.weight" />
									</th>
									<td><span> <s:textfield name="weight"
												cssClass="number" maxlength="9" value="%{prmInfo.weight}"
												tabindex="18" disabled="#disabled" /> <s:select
												name="weightUnit"
												list='#application.CodeTable.getCodes("1029")'
												listKey="CodeKey" listValue="Value" cssStyle="width:auto;"
												value="%{prmInfo.weightUnitMeasureCode}" tabindex="19"
												disabled="#disabled" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%--  促销品中文名简称 --%> <s:text name="prm03.nameShort" />
									</th>
									<td><span> <s:textfield name="nameShort"
												cssClass="text" maxlength="20" value="%{prmInfo.nameShort}"
												tabindex="6" />
									</span></td>
									<th>
										<%-- 促销品开始销售日期 --%> <s:text name="prm03.sellStartDate" />
									</th>
									<td><span> <s:textfield name="sellStartDate"
												cssClass="date" value="%{prmInfo.sellStartDate}"
												tabindex="20" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%--  促销品别名--%> <s:text name="prm03.nameAlias" />
									</th>
									<td><span> <s:textfield name="nameAlias"
												cssClass="text" maxlength="50" value="%{prmInfo.nameAlias}"
												tabindex="7" />
									</span></td>

									<th>
										<%-- 促销品结束销售日期 --%> <s:text name="prm03.sellEndDate" />
									</th>
									<td><span> <s:textfield name="sellEndDate"
												cssClass="date" value="%{prmInfo.sellEndDate}" tabindex="21" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%--  促销品英文名 --%> <s:text name="prm03.nameForeign" />
									</th>
									<td><span> <s:textfield name="nameForeign"
												cssClass="text" maxlength="40"
												onkeyup="return isMaxByteLen(this)"
												value="%{prmInfo.nameForeign}" tabindex="8" />
									</span></td>

									<th>
										<%--  保质期--%> <s:text name="prm03.shelfLife" />
									</th>
									<td><span> <s:textfield name="shelfLife"
												cssClass="number" maxlength="9" value="%{prmInfo.shelfLife}"
												tabindex="22" disabled="#disabled" /> <%-- 月 --%> <s:text
												name="prm03_month" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%--  促销品英文简称 --%> <s:text name="prm03.nameShortForeign" />
									</th>
									<td><span> <s:textfield name="nameShortForeign"
												cssClass="text" maxlength="30"
												value="%{prmInfo.nameShortForeign}" tabindex="9" />
									</span></td>

									<th>
										<%--  标准成本--%> <s:if
											test='"TZZK".equals(prmInfo.promCate) || "DHCP".equals(prmInfo.promCate)'>
											<label id="sellCost" style="color: red;"><s:text
													name="prm03.sellCost" /></label>
										</s:if> <s:else>
											<label id="standardCost"><s:text
													name="prm03.standardCost" /></label>
										</s:else>
									</th>
									<td><span> <s:if
												test='prmInfo.promCate.equals("ZDTL") || ("1".equals(prmInfo.editFlag) && "TZZK".equals(prmInfo.promCate))'>
												<s:textfield name="standardCost" cssClass="price"
													maxlength="18" value="%{prmInfo.standardCost}"
													tabindex="23" disabled="true" />
												<input type="hidden" name="standardCost" id="standardCost"
													value="<s:property value="prmInfo.standardCost"/>" />
											</s:if> <s:elseif test='!prmInfo.promCate.equals("ZDTL")'>
												<s:textfield name="standardCost" cssClass="price"
													maxlength="18" value="%{prmInfo.standardCost}"
													tabindex="23" />
											</s:elseif> <%-- 元 --%> <s:text name="prm03_moneryUnit" />
									</span> <span id="cost_tip"
										<s:if test="!prmInfo.promCate.equals('TZZK')">class="hide"</s:if>
										style="font-size: 12px; color: #808080; clear: both;"><s:text
												name="PRM03.tip" /></span></td>
								</tr>
								<tr>
									<th><label><s:text name="PRM03.exPoint" /></label></th>
									<%-- 可兑换积分值 --%>
									<td><span> <s:if
												test='"1".equals(prmInfo.editFlag) && "DHCP".equals(prmInfo.promCate)'>
												<input class="text" id="exPoint" name="exPoint"
													value="<s:property value="prmInfo.exPoint"/>" tabindex="10"
													disabled="disabled" />
												<input type="hidden" name="exPoint"
													value="<s:property value="prmInfo.exPoint"/>" />
											</s:if> <s:else>
												<input class="text" id="exPoint" name="exPoint"
													value="<s:property value="prmInfo.exPoint"/>" tabindex="10"
													<s:if test="'1'.equals(prmInfo.editFlag) || !prmInfo.promCate.equals('DHCP')">disabled="disabled"</s:if> />
											</s:else></span></td>
									<th><label><s:text name="PRM03.isStock" /></label></th>
									<%-- 是否库存管理--%>
									<td><span> <s:if
												test="!prmInfo.promCate.equals('ZDTL')">
												<s:select name="isStock"
													list='#application.CodeTable.getCodes("1140")'
													tabindex="24" listKey="CodeKey" listValue="Value"
													value="%{prmInfo.isStock}" />
											</s:if> <s:else>
												<s:select name="isStock"
													list='#application.CodeTable.getCodes("1140")'
													tabindex="24" listKey="CodeKey" listValue="Value"
													value="%{prmInfo.isStock}" disabled="true" />
												<input type="hidden" name="isStock" id="isStock"
													value="<s:property value='prmInfo.isStock'/>" />
											</s:else>
									</span></td>
								</tr>
								<tr>
									<th>
										<%-- 样式--%> <s:text name="prm03.styleCode" />
									</th>
									<td><span> <s:select name="styleCode"
												list='#application.CodeTable.getCodes("1012")'
												listKey="CodeKey" listValue="Value"
												value="%{prmInfo.styleCode}" tabindex="11"
												disabled="#disabled" headerKey=""
												headerValue="%{prm03_select}" />
									</span></td>
									<th>
										<%-- 有效区分--%> <s:text name="prm03.validFlag" />
									</th>
									<td><s:if test='"ZDTL".equals(prmInfo.promCate)'>
											<s:property
												value="#application.CodeTable.getVal('1137',prmInfo.validFlag)" />
											<input type="hidden" name="validFlag"
												value="<s:property value='prmInfo.validFlag'/>" />
										</s:if> <s:else>
											<span> <s:select id="validFlagSel" name="validFlag"
													list='#application.CodeTable.getCodes("1137")'
													listKey="CodeKey" listValue="Value"
													value="%{prmInfo.validFlag}" tabindex="25" />
											</span>
										</s:else></td>
								</tr>
								<tr>
									<th>
										<%--  促销品使用方式 --%> <s:text name="prm03.operationStyle" />
									</th>
									<td><span> <s:select name="operationStyle"
												list='#application.CodeTable.getCodes("1013")'
												listKey="CodeKey" listValue="Value"
												value="%{prmInfo.operationStyle}" tabindex="12"
												disabled="#disabled" headerKey=""
												headerValue="%{prm03_select}" />
									</span></td>
									<th>
										<%-- 促销品图片--%> <s:text name="prm03.imagePath" />
									</th>
									<td><span> <%-- 上传图片--%> <a
											href="javascript:void(0);"
											onclick="javascript:popUploadFile(this);return false;"><s:text
													name="prm03_image" /></a>
									</span></td>
								</tr>
								<tr>
									<th><label><s:text name="prm03.isExchanged" /></label></th>
									<%-- 可否用于积分兑换 --%>
									<td><span><s:select id="isExchangedSel" name="isExchanged"
													list='#application.CodeTable.getCodes("1220")'
													listKey="CodeKey" listValue="Value"
													value="%{prmInfo.isExchanged}"  /></span></td>
									<th>
										<label><s:text name="PRM03_isPosIss" /></label>
										<span class="highlight"><s:text name="global.page.required" /></span>
									</th>
									<td><span><s:select id="isPosIssSel" name="isPosIss"
													list='#application.CodeTable.getCodes("1341")'
													listKey="CodeKey" listValue="Value"
													value="%{prmInfo.isPosIss}"  /></span></td>
								</tr><tr id="validate_mode">
									<%-- 促销品类型 --%>
									<th><label><s:text name="prm03.mode" /></label></th>									
									<td><span><s:select name="mode" id="mode" 
													list='#application.CodeTable.getCodes("1345")'
													listKey="CodeKey" listValue="Value"  
													headerKey=""  headerValue="%{prm03_select}"
													value="%{prmInfo.mode}"/></span></td>
									<th></th>
									<td></td>
								</tr>
							</table>
						</cherry:form>
					</div>
				</div>
				<hr id="page_hr" class="hide" />
				<div class="center clearfix" id="pageButton">
					<button id="save" class="save" type="button"
						onclick="doSave('${save_url}')">
						<span class="ui-icon icon-save"></span> <span class="button-text"><s:text
								name="global.page.save" /></span>
					</button>
					<button id="back" class="back" type="button" onclick="doBack()">
						<span class="ui-icon icon-back"></span> <span class="button-text"><s:text
								name="global.page.back" /></span>
					</button>
					<button class="close" onclick="window.close();">
						<span class="ui-icon icon-close"></span>
						<%-- 关闭 --%>
						<span class="button-text"><s:text name="global.page.close" /></span>
					</button>
				</div>
			</div>
		</div>
	</div>
	<div id="prtVendor">
		<s:iterator value="prmFacList" id="prmFac">
			<span> <%-- 促销产品厂商ID--%> <input type="hidden"
				name="prmVendorId" value="${prmFac.prmPrtVendorId}" /> <%-- 促销产品条码--%>
				<input type="hidden" name="barCode"
				value="<s:property value='#prmFac.barCode'/>" /> <%-- 生产厂商更新日时--%> <input
				type="hidden" name="facUpdateTime" value="${prmFac.facUpdateTime}" />
				<%-- 生产厂商更新次数--%> <input type="hidden" name="facModifyCount"
				value="${prmFac.facModifyCount}" /> <%-- 有效区分--%> <input
				type="hidden" name="facValidFlag"
				value="<s:property value='#prmFac.facValidFlag'/>" />
			</span>
		</s:iterator>
	</div>
	<jsp:include page="/WEB-INF/jsp/common/popUploadFile.jsp" flush="true" />
	<%-- 假日 --%>
	<input type="hidden" id="dateHolidays" name="dateHolidays"
		value="${holidays}" />
	<%-- 系统时间 --%>
	<input type="hidden" id="today" name="today" value="${today}" />
	<div class="hide">
		<div id="disableTitle">
			<s:text name="prm03_disableTitle" />
		</div>
		<div id="disableMessage">
			<p class="message">
				<span><s:text name="prm03_disableMessage" /></span>
			</p>
		</div>
		<div id="enableTitle">
			<s:text name="prm03_enableTitle" />
		</div>
		<div id="enableMessage">
			<p class="message">
				<span><s:text name="prm03_enableMessage" /></span>
			</p>
		</div>
		<div id="dialogConfirm">
			<s:text name="global.page.ok" />
		</div>
		<div id="dialogCancel">
			<s:text name="global.page.cancle" />
		</div>
	</div>
	<div class="hide" id="dialogInit"></div>
</s:i18n>
