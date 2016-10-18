<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/res/BINOLBSRES03.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="i18n.bs.BINOLBSRES03">
<s:text name="global.page.select" id="select_default"/>
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="div_main">
		<div class="panel-header">
		<cherry:form id="toDetailForm" action="BINOLBSRES02_init" method="post" csrftoken="false">
		<input type="hidden" name="resellerInfoId" value='<s:property value="resellerDetail.resellerInfoId"/>'/>
		<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
		</cherry:form>
		<div class="clearfix"> 
		<span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="RES03_ResManage" />&nbsp;&gt;&nbsp;<s:text name="RES03_ResEdit"></s:text></span> 
		</div>
		</div>
		<div id="actionResultDisplay"></div>
			<div class="panel-content clearfix">
				<div class="section">
					<div class="section-header">
						<strong>
								<span class="ui-icon icon-ttl-section-info"></span>
								<s:text name="RES03_detail"></s:text>
						</strong>
					</div>
				<div class="section-content">
					<form id="update" csrftoken="false" onsubmit="save();return false;">
						<s:hidden name="regionId" id="regionId" value="%{resellerDetail.regionId}" />
						<s:hidden name="provinceId" id="provinceId" value="%{resellerDetail.provinceId}" />
         				<s:hidden name="cityId" id="cityId" value="%{resellerDetail.cityId}" />
						<input type="hidden" name="resellerInfoId" value='<s:property value="%{resellerDetail.resellerInfoId}"/>'/>
							<s:hidden name="modifyTime" value="%{resellerDetail.modifyTime}"></s:hidden>
							<s:hidden name="modifyCount" value="%{resellerDetail.modifyCount}"></s:hidden>
						<table class="detail" cellpadding="0" cellspacing="0">
							<tr>
								<th><s:text name="RES03_resellerCode"></s:text></th>
								<td><span><s:property value="%{resellerDetail.resellerCode}"/></span><s:textfield name="resellerCode" cssClass="hide" value="%{resellerDetail.resellerCode}"></s:textfield></td> 
								<th><s:text name="RES03_resellerName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
								<td><span><s:textfield name="resellerName" cssClass="text" value="%{resellerDetail.resellerName}" maxlength="30"/></span></td>
							</tr>
							<tr>
								<th><s:text name="RES03_resellerNameShort"></s:text></th>
								<td><span><s:textfield name="resellerNameShort" cssClass="text" value="%{resellerDetail.resellerNameShort}" maxlength="20"/></span></td>								  
								<th><s:text name="RES03_provinceName"></s:text></th>
								<td>
									<a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
										<span id="provinceText" class="button-text choice">
											<s:if test="%{resellerDetail.provinceName != null && resellerDetail.provinceName != ''}">
												<s:property value="resellerDetail.provinceName "/>
											</s:if>
											<s:else>
												<s:text name="global.page.select"/>
											</s:else>
										</span>
										<span class="ui-icon ui-icon-triangle-1-s"></span>
									</a>
								</td>
							</tr>	  
							<tr>
								<th><s:text name="RES03_levelCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
								<td>
									<span>
										<s:if test="%{resellerDetail.levelCode != null && resellerDetail.levelCode != ''}">
											<s:select disabled="true" list='#application.CodeTable.getCodes("1315")' listKey="CodeKey" listValue="Value"  headerKey="" headerValue="%{#select_default}" value="%{resellerDetail.levelCode}" onchange="changeLevel();"></s:select>
											<s:hidden name="levelCode" id="levelCode" value="%{resellerDetail.levelCode}" />
										</s:if>
										<s:else>
											<s:select name="levelCode" id="levelCode" list='#application.CodeTable.getCodes("1315")' listKey="CodeKey" listValue="Value"  headerKey="" headerValue="%{#select_default}" value="%{resellerDetail.levelCode}" onchange="changeLevel();"></s:select>
										</s:else>
									</span>
								</td>
								<th><s:text name="RES03_cityName"></s:text></th>
								<td>
									<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
										<span id="cityText" class="button-text choice">
											<s:if test="%{resellerDetail.cityName != null && resellerDetail.cityName != ''}">
												<s:property value="resellerDetail.cityName "/>
											</s:if>
											<s:else>
												<s:text name="global.page.select"/>
											</s:else>
										</span>
										<span class="ui-icon ui-icon-triangle-1-s"></span>
									</a>
								</td>	    		     
							</tr>	
							<tr>
								<th><s:text name="RES03_parentResellerCode"></s:text></th>
								<td>
									<span>
										<span id="parentResellerName">
											<s:if test="%{resellerDetail.parentResellerCode != null && resellerDetail.parentResellerCode != ''}">
												（<s:property value="resellerDetail.parentResellerCode"/>）
											</s:if>
											<s:property value="resellerDetail.parentResellerName"/>
										</span>
										<input id="parentResellerCode" type="hidden" value="<s:property value="resellerDetail.parentResellerCode"/>" name="parentResellerCode">
										<a id="parentResellerCodeBtn" class="add" onclick="selectParentReseller();" <s:if test="%{resellerDetail.levelCode == 1}">style="display:none;"</s:if> >
											<span class="ui-icon icon-search"></span>
											<span class="button-text"><s:text name="global.page.select" /></span>
										</a>
									</span>
								</td>
								<th><s:text name="RES03_priceFlag"></s:text></th>
								<td>
									<span>
									<p class="clearfix">
			                    		<input id="RES03_specificReseller" name="priceFlag" type="radio" value="1" <s:if test="%{resellerDetail.priceFlag == 1}">checked</s:if>/><s:text name="RES03_specificReseller"></s:text>
			                    		<input id="RES03_resellerDegree" name="priceFlag" type="radio" value="0" <s:if test="%{resellerDetail.priceFlag == 0}">checked</s:if>/><s:text name="RES03_resellerDegree"></s:text>
			               			 </p>						
									</span>
								</td>        
							</tr>								
							<tr>
								<th><s:text name="RES03_type"></s:text></th>
								<td>
									<span><s:select list='#application.CodeTable.getCodes("1314")' listKey="CodeKey" listValue="Value" name="type" headerKey="" headerValue="%{#select_default}" value="%{resellerDetail.type}"></s:select></span>
								</td>
								<th><s:text name="RES03_legalPerson"></s:text></th>
								<td><span><s:textfield name="legalPerson" cssClass="text" value="%{resellerDetail.legalPerson}" maxlength="30"></s:textfield></span></td>  
							</tr>
							<tr>
								<th><s:text name="RES03_telephone"></s:text></th>
								<td><span><s:textfield name="telephone" cssClass="text" value="%{resellerDetail.telePhone}" maxlength="20"/></span></td>
								<th><s:text name="RES03_mobile"></s:text></th>
								<td><span><s:textfield name="mobile" cssClass="text" value="%{resellerDetail.mobile}" maxlength="20"></s:textfield></span></td>  
							</tr>
							<tr>
								<th><s:text name="RES03_validFlag"></s:text></th>
								<td colspan="3">
									<s:if test="resellerDetail.validFlag == 1">
										<s:text name="RES03_valid" />
									</s:if>
									<s:else>
										<s:text name="RES03_invalid" />
									</s:else>
									<s:textfield name="validFlag" cssClass="hide" value="%{resellerDetail.validFlag}" maxlength="1"></s:textfield>
								</td>  
							</tr>
							<%--
							<tr>
							<th><s:text name="RES03_levelCode"></s:text></th>
							<td><span><s:textfield name="levelCode" cssClass="text" value="%{resellerDetail.levelCode}" maxlength="50"/></span></td>
							<th><s:text name="RES03_status"></s:text></th>
							<td><span><s:textfield name="status" cssClass="text" value="%{resellerDetail.status}" maxlength="50"/></span></td>
							</tr>
							--%>
						</table>    
				</form>  
				</div>
				</div> 
				<div class="center clearfix" id="pageButton">
					<button class="save" onclick="save();return false;">
						<s:a action="BINOLBSRES03_save" id="saveUrl" cssStyle="display: none;"></s:a>
						<span class="ui-icon icon-save"></span>
						<span class="button-text"><s:text name="global.page.save"/></span>
					</button>
					<button id="back" class="back" type="button" onclick="doBack()">
						<span class="ui-icon icon-back"></span>
						<span class="button-text"><s:text name="global.page.back"/></span>
					</button>
					<button class="close" onclick="window.close();">
						<span class="ui-icon icon-close"></span>
						<span class="button-text"><s:text name="global.page.close"/></span><%--close --%>
					</button>
				</div>
			</div>
		</div>
	</div>
</div>
<%-- ================== 省市选择DIV START ======================= --%>
<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<div class="clearfix">
		<span class="label"><s:text name="global.page.range"></s:text></span>
		<ul class="u2"><li onclick="bscom03_getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="%{#reginMap.reginId}" />_<s:property value="provinceId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
</div>
<div id="cityTemp" class="ui-option hide">
	<s:if test="%{cityList != null && !cityList.isEmpty()}">
	<ul class="u2">
		<li onclick="bscom03_getNextRegin(this, '${select_default }', 2);"><s:text name="global.page.all"></s:text></li>
		<s:iterator value="cityList">
			<li id="<s:property value="regionId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 2);"><s:property value="regionName"/></li>
		</s:iterator>
	</ul>
	</s:if>
</div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>
<s:url value="/common/BINOLCM02_initResellerDialog" id="initResellerDialogUrl"></s:url>
<a href="${initResellerDialogUrl }" id="initResellerDialog"></a>

</s:i18n> 