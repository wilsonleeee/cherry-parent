<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/wem/BINOLBSWEM04_02.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/wem/BINOLBSWEM04_03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<body id="div_main">
<s:i18n name="i18n.bs.BINOLBSWEM04">
	<s:text name="global.page.select" id="select_default"/>
	<div style="display: none;">
		<div id="dialogConfirm">
			<s:text name="global.page.ok" />
		</div>
		<div id="dialogCancel">
			<s:text name="global.page.cancle" />
		</div>
		<div id="dialogClose">
			<s:text name="global.page.close" />
		</div>
		<s:url id="assignSearch_url" action="BINOLBSWEM01_assignSearch" />
		<s:hidden name="assignSearch_url" value="%{assignSearch_url}" />
		<s:url id="assign_url" action="BINOLBSWEM01_assign" />
		<s:hidden name="assign_url" value="%{assign_url}" />
		<input id="parentCsrftoken" name="csrftoken" value="${csrftoken }" class="hide"/>
		<s:url action="BINOLBSWEM04_save" id="saveUrl"></s:url>
	</div>
	<div class="main container clearfix">
		<div class="panel ui-corner-all">
			<div class="panel-header">
				<div class="clearfix">
					<span class="breadcrumb left">
						<s:text name="WEM04_wechatMerchant" /> &gt;
						<s:text name="WEM04_modify" />
					</span>
					<!-- 审核不通过 -->
					<span class="breadcrumb right">
						<a class="add" onclick="BINOLBSWEM04_02.save('${saveUrl }', 2);return false;">
							<span class="ui-icon icon-delete"></span>
							<span class="button-text"><span class="highlight"><s:text name="WEM04_auditUnpassed"/></span></span>
						</a>
					</span>
				</div>
			</div>
			<div id="actionResultDisplay"></div>
			<div class="hide" id="dialogInit"></div>
			<div class="panel-content clearfix">
				<input id="mainEmployeeId" class="hide" value="${agentInfoMap.employeeId }"/>
				<cherry:form id="mainForm" class="inline" csrftoken="false">
					<div class="section">
						<div class="section-header">
							<strong>
								<span class="ui-icon icon-ttl-section-info-edit"></span>
								<s:text name="global.page.title" />
								：<span class="highlight"><s:property value="agentInfoMap.billCode"/></span>
							</strong>
						</div>
						<div class="section-content">
							<input name="agentOpenID" class="hide" value="${agentInfoMap.agentOpenID }"/>
							<input name="agentApplyId" class="hide" value="${agentInfoMap.agentApplyId }"/>
							<table class="detail" cellpadding="0" cellspacing="0">
								<tr>
									<!-- 手机-->
									<th>
										<s:text name="WEM04_mobilePhone" />
										<span class="highlight"><s:text name="global.page.required"/></span>
									</th>
									<td>
										<span><s:property value="agentInfoMap.agentMobile"/>
										<input name="agentMobile" class="hide" value="${agentInfoMap.agentMobile }"/></span>
									</td>
								</tr>
								<tr>
									<!-- 姓名-->
									<th>
										<s:text name="WEM04_name"></s:text>
										<span class="highlight"><s:text name="global.page.required"/></span>
									</th>
									<td>
										<span><input name="agentName" value="${agentInfoMap.agentName }" class="text"/> </span>
									</td>
								</tr>
								<tr>
									<!-- 省份-->
									<th><s:text name="WEM04_provinceName" /></th>
									<td>
										<span>
											<input class="hide" id="provinceId" name="provinceId" value="${agentInfoMap.provinceId }"/>
											<a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
												<span id="provinceText" class="button-text choice">
													<s:if test='%{agentInfoMap.agentProvince != null && !"".equals(agentInfoMap.agentProvince)}'>
														<s:property value="agentInfoMap.agentProvince"/>
													</s:if>
													<s:else>
														<s:text name="global.page.select" />
													</s:else>
												</span>
												<span class="ui-icon ui-icon-triangle-1-s"></span>
											</a>
										</span>
									</td>
								</tr>
								<tr>
									<!-- 城市 -->
									<th><s:text name="WEM04_cityName" /></th>
									<td>
										<span>
											<input class="hide" id="cityId" name="cityId" value="${agentInfoMap.cityId }"/>
											<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
												<span id="cityText" class="button-text choice">
													<s:if test='%{agentInfoMap.agentCity != null && !"".equals(agentInfoMap.agentCity)}'>
														<s:property value="agentInfoMap.agentCity"/>
													</s:if>
													<s:else>
														<s:text name="global.page.select" />
													</s:else>
												</span>
												<span class="ui-icon ui-icon-triangle-1-s"></span>
											</a>
										</span>
									</td>
								</tr>
								<tr>
									<!-- 部门类型-->
									<th><s:text name="WEM04_level"></s:text>
										<span class="highlight"><s:text name="global.page.required"/></span>
									</th>
									<td>
										<span>
											<s:select id="agentLevel" name="agentLevel" list="agentLevelList" listKey="codeKey" 
												listValue="value1" headerKey="" headerValue='%{#select_default}'
												value="%{agentInfoMap.agentLevel }">
											</s:select>
										</span>
										<!-- 升级申请时显示原上级 -->
										<s:if test='agentInfoMap.applyType.compareTo(3) > 0'>
										（<s:text name="WEM04_oldLevel"/>：<s:property value='#application.CodeTable.getVal("1000", agentInfoMap.oldAgentLevel)' />）
										</s:if>
									</td>
								</tr>
								<tr>
									<!-- 上级手机号-->
									<th><s:text name="WEM04_super" /><span class="highlight"><s:text name="global.page.required"/></span></th>
									<td>
										<s:url id="selectInit_url" value="BINOLBSWEM01_assignInit"/>
										<p>
											<span>
												<input id="superMobile" name="superMobile" class="hide" value="${agentInfoMap.superMobile}"/>
												<span id="superMobileTemp"><s:property value="agentInfoMap.superName"/>
													(<s:property value="agentInfoMap.superMobile"/>-<s:property value="agentInfoMap.superAgentLevelName"/>)
												</span>
												<a style="margin-top:4px;" class="add" href="${selectInit_url }" onclick="BINOLBSWEM04_03.select(this);return false;">
													<span class="ui-icon icon-add"></span>
													<span class="button-text"><s:text name="WEM04_selectSuper"/></span>
												</a>
											</span>
										</p>
									</td>
								</tr>
								<!-- 申请类型为修改上级时，显示原上级信息 -->
								<s:if test='"3".equals(agentInfoMap.applyType)'>
									<tr>
										<!-- 原上级手机号-->
										<th><s:text name="WEM04_oldSuper" /></th>
										<td>
											<p>
												<span>
													<s:property value="oldSuperAgentInfoMap.agentName"/>
													(<s:property value="oldSuperAgentInfoMap.agentMobile"/>-<s:property value="oldSuperAgentInfoMap.agentLevelName"/>)
												</span>
											</p>
										</td>
									</tr>
								</s:if>
								<tr>
									<!-- 审核理由-->
									<th>
										<s:text name="WEM04_reason" />
									</th>
									<td>
										<textarea id="reason" style="width:825px;height:30px;resize:both;" name="reason" >${agentInfoMap.reason }</textarea>
									</td>
								</tr>
							</table>
						</div>
					</div>
					<%-- 操作按钮 --%>
					<div id="editButton" class="center clearfix">
						<button class="save" onclick="BINOLBSWEM04_02.save('${saveUrl }', 1);return false;">
							<span class="ui-icon icon-save"></span> <span class="button-text">
							<s:text name="WEM04_auditPassed" /></span>
						</button>
						<button id="close" class="close" type="button" onclick="javascript:window.close();return false;">
							<span class="ui-icon icon-close"></span>
							<span class="button-text"><s:text name="global.page.close" /></span>
						</button>
					</div>
					<%-- ====================== 基本信息DIV结束 ===================== --%>
				</cherry:form>
			</div>
		</div>
	</div>
	<%-- ================== 省市选择DIV START ======================= --%>
	<div id="provinceTemp" class="ui-option hide" style="width:325px;">
		<div class="clearfix">
			<span class="label"><s:text name="global.page.range"></s:text></span>
			<ul class="u2"><li onclick="bscom03_getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
		</div>
		<s:iterator value="reginList">
	 		<s:set name="provinceList" value="provinceList" />
	    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
	    	<ul class="u2">
	     		<s:iterator value="#provinceList">
	         		<li id="<s:property value="provinceId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 1);">
	         			<s:property value="provinceName"/>
	         		</li>
	      		</s:iterator>
	      	</ul>
	    	</div>
	   	</s:iterator>
	</div>
	<div id="cityTemp" class="ui-option hide"></div>
	<%-- 下级区域查询URL --%>
	<s:url id="querySubRegionUrl" value="/common/BINOLCM08_querySubRegion"></s:url>
	<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
	<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
	<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
	<%-- ================== 省市选择DIV  END  ======================= --%>
	
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
</s:i18n>
</body>
