<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/res/BINOLBSRES04.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="i18n.bs.BINOLBSRES04">
<s:text name="global.page.select" id="select_default"/>
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="div_main">
			<div class="panel-header">
				<div class="clearfix"> 
					<span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="RES04_ResManage" />&nbsp;&gt;&nbsp;<s:text name="RES04_ResEdit"></s:text></span> 
				</div>
			</div>
		<div id="actionResultDisplay"></div>
			<div class="panel-content clearfix">
				<div class="section">
				<div class="section-header">
					<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="RES04_detail"></s:text></strong>
				</div>
					<div class="section-content">
						<form id="add" onsubmit="save();return false;">
							<s:hidden name="regionId" id="regionId"/>
							<s:hidden name="provinceId" id="provinceId"/>
          					<s:hidden name="cityId" id="cityId"/>
							<table class="detail" cellpadding="0" cellspacing="0">
						  	 	<tr>
						  			<th><s:text name="RES04_brand"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span><s:text name="请选择" id="CHA04_select"/></th>
						  		  	<td><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select></span></td>
						        	<th><s:text name="RES04_resellerCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
						        	<td><span><s:textfield name="resellerCode" cssClass="text" maxlength="20"></s:textfield></span></td>
								</tr>  	
						     	<tr>
						     		<th><s:text name="RES04_resellerName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
						        	<td><span><s:textfield name="resellerName" cssClass="text"  maxlength="30"/></span></td>		   
						      		<th><s:text name="RES04_resellerNameShort"></s:text></th>
						        	<td><span><s:textfield name="resellerNameShort" cssClass="text" maxlength="20"/></span></td>
								</tr>
								<tr>
									<th><s:text name="RES04_levelCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
									<td><span><s:select list='#application.CodeTable.getCodes("1315")' listKey="CodeKey" listValue="Value" id="levelCode" name="levelCode" headerKey="" headerValue="%{#select_default}" onchange="changeLevel();"></s:select></span></td>
									<th><s:text name="RES04_provinceName"></s:text></th>
									<td>
										<a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
						                	<span id="provinceText" class="button-text choice"><s:text name="global.page.select"/></span>
						     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
						     		 	</a>
									</td>
								</tr>	    		     
								<tr>
									<th><s:text name="RES04_parentResellerCode"></s:text></th>
						        	<td>
						        		<span>
						        		<span id="parentResellerName"></span>
										<input id="parentResellerCode" type="hidden" value="" name="parentResellerCode">
										<a id="parentResellerCodeBtn" class="add right" onclick="selectParentReseller();" style="display:none;">
											<span class="ui-icon icon-search"></span>
											<span class="button-text"><s:text name="global.page.select" /></span>
										</a>
										</span>
						        	</td>
									<th><s:text name="RES04_cityName"></s:text></th>
									<td>
										<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
						                	<span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
						     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
						     		 	</a>
									</td>	    		     
								</tr>	
								<tr>
									<th><s:text name="RES04_type"></s:text></th>
									<td><span><s:select list='#application.CodeTable.getCodes("1314")' listKey="CodeKey" listValue="Value" name="type" headerKey="" headerValue="%{#select_default}"></s:select></span></td>	    		     
									<th><s:text name="RES04_priceFlag"></s:text></th>
							        <td>
								        <span>
											<p class="clearfix">
						                    	<input id="RES04_specificReseller" name="priceFlag" type="radio" value="1" <s:if test="%{resellerDetail.priceFlag == 1}">checked</s:if>/><s:text name="RES04_specificReseller"></s:text>
						                    	<input id="RES04_resellerDegree" name="priceFlag" type="radio" value="0" <s:if test="%{resellerDetail.priceFlag == 0}">checked</s:if>/><s:text name="RES04_resellerDegree"></s:text>
					               			</p>						
										</span>
									</td>        
								</tr>	
						     	<tr>
							        <th><s:text name="RES04_mobile"></s:text></th>
							        <td><span><s:textfield name="mobile" cssClass="text" maxlength="20"></s:textfield></span></td> 
							        <th><s:text name="RES04_telephone"></s:text></th>
							        <td><span><s:textfield name="telephone" cssClass="text" maxlength="20"/></span></td> 
						    	</tr>
						    	<tr>
						        	<th><s:text name="RES04_legalPerson"></s:text></th>
						        	<td colspan="3"><span><s:textfield name="legalPerson" cssClass="text" maxlength="30"></s:textfield></span></td> 
						        </tr>
							</table>    
						 </form>  
					</div>
				</div> 
			<hr class="space" />
				<div class="center">
					<s:a action="BINOLBSRES04_save" id="RES04_save" cssStyle="display: none;"></s:a>
					<button class="save" onclick="save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
					<button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
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
<div id="cityTemp" class="ui-option hide"></div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>
<s:url value="/common/BINOLCM02_initResellerDialog" id="initResellerDialogUrl"></s:url>
<a href="${initResellerDialogUrl }" id="initResellerDialog"></a>
</s:i18n> 