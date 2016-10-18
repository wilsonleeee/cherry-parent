<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/res/BINOLBSRES02.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.bs.BINOLBSRES02">
<div class="main container clearfix">
<div class="panel ui-corner-all">
	<div id="div_main">
		<div class="panel-header">
			<form id="toEditForm" action="BINOLBSRES03_init" method="post" csrftoken="false">
				<input type="hidden" name="resellerInfoId" value='<s:property value="resellerInfoId"/>'/>
				<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
			</form>
			<div class="clearfix"> 
				<span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="RES02_title" />&nbsp;&gt;&nbsp;<s:text name="RES02_detail"></s:text></span> 
			</div>
		</div>
		<div class="panel-content clearfix">
			<div class="section">
				<div class="section-header">
					<strong>
						<span class="ui-icon icon-ttl-section-info"></span>
						<s:text name="RES02_info"></s:text>
					</strong>
				</div>
				<div class="section-content" id="detail">
					<table class="detail" cellpadding="0" cellspacing="0">					
						<tr>
							<th><s:text name="RES02_resellerCode"></s:text></th>
							<td><span><s:property value="resellerDetail.resellerCode"/></span></td>
							<th><s:text name="RES02_resellerName"></s:text></th>
							<td><span><s:property value="resellerDetail.resellerName"/></span></td>							
						</tr>     
						<tr>	
							<th><s:text name="RES02_resellerNameShort"></s:text></th>
							<td><span><s:property value="resellerDetail.resellerNameShort"/></span></td>
							<th><s:text name="RES02_provinceName"></s:text></th>
							<td><span><s:property value="resellerDetail.provinceName"/></span></td>	
							
						</tr>
						<tr>
							<th><s:text name="RES02_levelCode"></s:text></th>
							<td><span><s:property value='#application.CodeTable.getVal("1315",resellerDetail.levelCode)' /></span></td>
							<th><s:text name="RES02_cityName"></s:text></th>
							<td><span><s:property value="resellerDetail.cityName"/></span></td>							
						</tr>
						<tr>
							<th><s:text name="RES02_ParentResellerCode"></s:text></th>
							<td>
								<span>
									<s:if test="%{resellerDetail.parentResellerCode != null && resellerDetail.parentResellerCode != ''}">
										（<s:property value="resellerDetail.parentResellerCode"/>）
									</s:if>
									<s:property value="resellerDetail.parentResellerName"/>
								</span>
							</td>
							<th><s:text name="RES02_priceFlag"></s:text></th>
							<td>
								<span>
									<s:if test="resellerDetail.priceFlag == 0">
										<s:text name="RES02_resellerDegree" />
						        	</s:if>
						        	<s:elseif test="resellerDetail.priceFlag == 1">
						        		<s:text name="RES02_specificReseller" />
						        	</s:elseif>
								</span>
							</td>							
						</tr>
						<tr>
							<th><s:text name="RES02_Type"></s:text></th>							
							<td><span><s:property value='#application.CodeTable.getVal("1314",resellerDetail.type)' /></span></td>
							<th><s:text name="RES02_legalPerson"></s:text></th>
							<td><span><s:property value="resellerDetail.legalPerson"/></span></td>			
						</tr>
						<tr>
							<th><s:text name="RES02_telephone"></s:text></th>
							<td><span><s:property value="resellerDetail.telePhone"/></span></td>
							<th><s:text name="RES02_mobile"></s:text></th>
							<td><span><s:property value="resellerDetail.mobile"/></span></td>
						</tr> 
						<tr>
							<th><s:text name="RES02_validFlag"></s:text></th>
							<td colspan="3">
								<span>
									<s:if test="resellerDetail.validFlag == 1">
										<s:text name="RES02_valid" />
						        	</s:if>
						        	<s:else>
						        		<s:text name="RES02_invalid" />
						        	</s:else>
								</span>
							</td>
						</tr> 
						<%--
						<tr>
							<th><s:text name="RES02_levelCode"></s:text></th>
							<td><span><s:property value="resellerDetail.levelCode"/></span></td>
							<th><s:text name="RES02_status"></s:text></th>
							<td><span><s:property value="resellerDetail.status"/></span></td>
						</tr> 
						--%>
					</table>    
				</div>
			</div> 
			<div class="center">
			<cherry:show domId="BSRES02EDIT">
				<button class="edit" onclick="edit();return false;">
					<span class="ui-icon icon-edit-big"></span> <%-- 编辑按钮 --%>
					<span class="button-text"><s:text name="global.page.edit"/></span>
				</button>
			</cherry:show>
				<button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
			</div>
		</div>
	</div>
</div>
</div>
</s:i18n> 