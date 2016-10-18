<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp"
	flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM02.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.ss.BINOLSSPRM02">
	<s:text name="PRM02_selectFirst" id="PRM02_selectFirst" />
	<div class="main container clearfix">
		<div class="panel ui-corner-all">
			<div class="panel-header">
				<div class="clearfix">
					<span class="breadcrumb left"> <span
						class="ui-icon icon-breadcrumb"></span> <s:text
							name="prm02_manage" /> &gt; <s:text name="prm02_header" />
					</span>
				</div>
			</div>
			<div class="panel-content">
				<%-- 保存 --%>
				<s:url id="save_url" action="BINOLSSPRM02_save" />
				<%-- 类别查询 --%>
				<s:url id="queryCate" action="BINOLCM05_queryCate"
					namespace="/common" />
				<%-- 中分类查询 --%>
				<s:url id="querySecondCate" action="BINOLCM05_querySecondCate"
					namespace="/common" />
				<%-- 小分类查询 --%>
				<s:url id="querySmallCate" action="BINOLCM05_querySmallCate"
					namespace="/common" />
				<div id="actionResultDisplay"></div>
				<div class="section">
					<div class="section-header">
						<strong><span class="ui-icon icon-ttl-section-info-edit"></span>
							<%-- 基本信息 --%> <s:text name="PRM02_basicTab" /></strong>
					</div>
					<div class="section-content">
						<cherry:form id="mainForm" method="post" class="inline"
							csrftoken="false">
							<table id="baseInfo" class="detail" cellpadding="0"
								cellspacing="0">
								<tr>
									<th>
										<%-- 品牌 --%> <label> <s:text name="PRM02_brand" /> <span
											class="highlight"><s:text name="global.page.required" /></span>
									</label>
									</th>
									<td><span> <s:select id="brandSel"
												name="brandInfoId" list="brandInfoList"
												listKey="brandInfoId" listValue="brandName"
												onchange="getSelOpts('%{queryCate}');" tabindex="1" />
									</span></td>
									<th>
										<%-- 大分类 --%> <label><s:text name="PRM02_primaryCate" /></label>
									</th>
									<td><span> <s:if
												test="null != primaryCateList && !primaryCateList.isEmpty()">
												<s:select id="priCateSel" name="primaryCateCode"
													list="primaryCateList" listKey="primaryCateCode"
													listValue="primaryCateName"
													onchange="doSecondCate(this, '%{querySecondCate}');"
													headerKey="" headerValue="%{PRM02_selectFirst}"
													tabindex="13" />
											</s:if> <s:else>
												<select id="priCateSel" name="primaryCateCode" tabindex="11">
													<option value="">
														<s:text name="PRM02_selectFirst" />
													</option>
												</select>
											</s:else>
									</span></td>
								</tr>
								<tr>
									<th><label><s:text name="PRM02_promCate" /></label>
									<%-- 促销品类型 --%></th>
									<td><span> 
												<s:select name="promCate1" id="promCate1"
												list='#application.CodeTable.getCodes("1139")' tabindex="2"
												listKey="CodeKey" listValue="Value"
												onchange="changeCate(this);return false;"/>
											<input type="hidden" name="virtualPrm" id="virtualPrm" value="${virtualPrm}"/>												
											<input type="hidden" name="promCate" id="promCate"/>											
									</span></td>
									<th>
										<%-- 中分类 --%> <label><s:text name="PRM02_secondCate" /></label>
									</th>
									<td><span> <select id="secCateSel"
											name="secondCateCode"
											onchange="removeErrorSpan(this);doSmallCate(this, '${querySmallCate}');"
											tabindex="14">
												<option value="">
													<s:text name="PRM02_selectFirst" />
												</option>
										</select>
									</span></td>
								</tr>
								<tr>
									<th>
										<%-- 厂商编码 --%> <label> <s:text name="PRM02_unitCode" />
											<span class="highlight"><s:text
													name="global.page.required"/></span>
									</label>
									</th>
									<td>
										<span> 
										<s:if test="promUBRule == 1">
											<s:textfield name="unitCode"
													cssClass="text" maxlength="20" tabindex="3" onkeyup="this.value=this.value.toUpperCase()"/>
							      		</s:if>
				      					<s:elseif test="promUBRule == 2">	
							      			<span><s:property value="unitCode"/></span>
							      			<s:hidden id="unitCode" name="unitCode" value="%{unitCode}"></s:hidden>
				      					</s:elseif>	
										</span>
									</td>

									<th>
										<%-- 小分类 --%> <label><s:text name="PRM02_smallCate" /></label>
									</th>
									<td><span> <select id="smallCateSel"
											name="smallCateCode" onchange="removeErrorSpan(this);"
											tabindex="15">
												<option value="">
													<s:text name="PRM02_selectFirst" />
												</option>
										</select>
									</span></td>
								</tr>
								<tr>
									<th>
										<%--  促销产品条码--%> <s:text name="PRM02_barCode" /> <span
										class="highlight"><s:text name="global.page.required" /></span>
									</th>
									<td>
										<span> 
											<s:if test="promUBRule == 1">
												<s:textfield name="barCode"
													cssClass="text" tabindex="4" maxlength="13" onkeyup="this.value=this.value.toUpperCase()"/>
								      		</s:if>
					      					<s:elseif test="promUBRule == 2">	
								      			<span><s:property value="barCode"/></span>
								      			<s:hidden id="barCode" name="barCode" value="%{barCode}"></s:hidden>
					      					</s:elseif>	
										</span> 
											<%--
				                   			<br/>
				                    			<span style="font-size: 12px;color:#808080;clear:both;">
					                 				<s:text name="PRM02_barCodeText"/>
					                 			</span>
					                 		--%>
					                 </td>
									<th>
										<%-- 标准成本 --%> <label id="standardCost"><s:text
												name="PRM02_standardCost" /></label> <label id="sellCost"
										style="color: red;" class="hide"><s:text
												name="PRM02_sellCost" /></label>
									</th>
									<td><span> <s:textfield name="standardCost"
												cssClass="price" maxlength="18" tabindex="16" /> <%-- 元 --%>
											<s:text name="PRM02_moneryUnit" />
									</span> <span id="cost_tip" class="hide"
										style="font-size: 12px; color: #808080; clear: both;"><s:text
												name="PRM02_tip" /></span></td>
								</tr>
								<tr>
									<th>
										<%-- 中文名 --%> <label> <s:text name="PRM02_nameTotal" />
											<span class="highlight"><s:text
													name="global.page.required" /></span>
									</label>
									</th>
									<td><span> <s:textfield name="nameTotal"
												cssClass="text" maxlength="40"
												onkeyup="return isMaxByteLen(this)" tabindex="5" />
									</span></td>

									<th>
										<%-- 容量 --%> <label><s:text name="PRM02_volume" /></label>
									</th>
									<td><span> <s:textfield name="volume"
												cssClass="number" maxlength="9" tabindex="17" /> <s:select
												name="volumeUnit"
												list='#application.CodeTable.getCodes("1028")'
												cssStyle="width:auto;" listKey="CodeKey" listValue="Value"
												tabindex="18" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%-- 中文简称 --%> <label><s:text name="PRM02_nameShort" /></label>
									</th>
									<td><span> <s:textfield name="nameShort"
												cssClass="text" maxlength="20" tabindex="6" />
									</span></td>
									<th>
										<%-- 重量 --%> <label><s:text name="PRM02_weight" /></label>
									</th>
									<td><span> <s:textfield name="weight"
												cssClass="number" maxlength="9" tabindex="19" /> <s:select
												name="weightUnit"
												list='#application.CodeTable.getCodes("1029")'
												cssStyle="width:auto;" listKey="CodeKey" listValue="Value"
												tabindex="20" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%-- 别名 --%> <label><s:text name="PRM02_nameAlias" /></label>
									</th>
									<td><span> <s:textfield name="nameAlias"
												cssClass="text" maxlength="50" tabindex="7" />
									</span></td>

									<th>
										<%-- 开始销售日期 --%> <label> <s:text
												name="PRM02_sellStartDate" />
									</label>
									</th>
									<td><span> <s:textfield name="sellStartDate"
												cssClass="date" tabindex="21" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%-- 英文名 --%> <label><s:text name="PRM02_nameForeign" /></label>
									</th>
									<td><span> <s:textfield name="nameForeign"
												cssClass="text" maxlength="40"
												onkeyup="return isMaxByteLen(this)" tabindex="8" />
									</span></td>

									<th>
										<%-- 停止销售日期 --%> <label> <s:text
												name="PRM02_sellEndDate" />
									</label>
									</th>
									<td><span> <s:textfield name="sellEndDate"
												cssClass="date" tabindex="22" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%-- 英文简称 --%> <label><s:text
												name="PRM02_nameShortForeign" /></label>
									</th>
									<td><span> <s:textfield name="nameShortForeign"
												cssClass="text" maxlength="20" tabindex="9" />
									</span></td>
									<th>
										<%-- 是否库存管理--%> <label><s:text name="PRM02_isStock" /></label>
									</th>
									<td><span> <s:select name="isStock"
												list='#application.CodeTable.getCodes("1140")'
												listKey="CodeKey" listValue="Value" tabindex="23" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%-- 样式 --%> <label><s:text name="PRM02_styleCode" /></label>
									</th>
									<td><span> <s:select name="styleCode"
												list='#application.CodeTable.getCodes("1012")'
												listKey="CodeKey" listValue="Value" 
												headerKey="" headerValue="%{PRM02_selectFirst}"
												tabindex="10" />
									</span></td>
									<th>
										<%-- 保质期 --%> <label><s:text name="PRM02_shelfLife" /></label>
									</th>
									<td><span> <s:textfield name="shelfLife"
												cssClass="number" maxlength="9" tabindex="24" /> <%-- 月 --%>
											<s:text name="PRM02_month" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%-- 使用方式 --%> <label><s:text
												name="PRM02_operationStyle" /></label>
									</th>
									<td><span> <s:select name="operationStyle"
												list='#application.CodeTable.getCodes("1013")'
												listKey="CodeKey" listValue="Value" tabindex="11"
												headerKey="" headerValue="%{PRM02_selectFirst}" />
									</span></td>
									<th>
										<%-- 促销品图片--%> <label><s:text name="PRM02_image" /></label>
									</th>
									<td><span> <%-- 上传图片--%> <a
											href="javascript:void(0);"
											onclick="javascript:popUploadFile(this);return false;"><s:text
													name="PRM02_UpImage" /></a>
									</span></td>
								</tr>
								<tr>
									<th><label><s:text name="PRM02_exPoint" /></label></th>
									<%-- 可兑换积分值 --%>
									<td><span><input id="exPoint" class="text"
											name="exPoint" disabled="disabled" tabindex="12" /></span></td>
									<%-- 可否用于积分兑换 --%>
									<th><label><s:text name="PRM02_isExchanged" /></label></th>
									<td><span> <s:select name="isExchanged" list='#application.CodeTable.getCodes("1220")' listKey="CodeKey" listValue="Value" />
									</span></td>
								</tr>
								<tr>
									<th>
										<%--是否下发到POS--%>
										<label>
											<s:text name="PRM02_isPosIss" />
											<span class="highlight">
												<s:text name="global.page.required" />
											</span>
										</label>
									</th>
									<td>
										<span>
											<s:select name="isPosIss" list='#application.CodeTable.getCodes("1341")' 
											listKey="CodeKey" listValue="Value" tabindex="23" />
										</span>
									</td>
									<th><p id="validate_mode"><s:text name="PRM02_mode"/></p></th>
									<td><p id="mode1">
											<s:select name="mode" list='#application.CodeTable.getCodes("1345")' 
											listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM02_selectFirst}"/>
										</p></td>
								</tr>
							</table>
						</cherry:form>
					</div>
				</div>
				<div class="center clearfix" id="pageButton">
					<button class="save" type="submit" onclick="doSave('${save_url}')">
						<span class="ui-icon icon-save"></span>
						<%-- 保存 --%>
						<span class="button-text"><s:text name="global.page.save" /></span>
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
	<jsp:include page="/WEB-INF/jsp/common/popUploadFile.jsp" flush="true" />
	<input type="hidden" id="dateHolidays" name="dateHolidays"
		value="${holidays}" />
	<input type="hidden" id="nameTotalMsg"
		value="<s:text name='PRM02_nameTotalMsg'/>" />
</s:i18n>
