<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/mo/mup/BINOLMOMUP04.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.mo.BINOLMOMUP04">
	<div class="main container clearfix">
		<div class="panel ui-corner-all">
			<div id="div_main">
				<div class="panel-header">
					<div class="clearfix">
						<span class="breadcrumb left"> 
							<span class="ui-icon icon-breadcrumb"></span> 
							<s:text name="MUP04_title1" />&nbsp;&gt;&nbsp; 
							<s:text name="MUP04_add"></s:text>
						</span>
					</div>
				</div>
				<div id="actionResultDisplay"></div>
				<div class="panel-content clearfix">
					<div class="section">
						<div class="section-header">
							<strong>
								<span class="ui-icon icon-ttl-section-info"></span>
								<s:text name="MUP04_baseInfo"></s:text>
							</strong>
						</div>
						<div class="section-content">
							<cherry:form id="add" csrftoken="false" onsubmit="save();return false;">
								<table class="detail" cellpadding="0" cellspacing="0">
									<tr>
										<th>
											<s:text name="MUP04_version"></s:text>
												<span class="highlight">
												<s:text name="global.page.required"></s:text>
											</span>
										</th>
										<td>
											<span>
												<s:textfield name="version" cssClass="text" maxlength="50"></s:textfield>
											</span>
										</td>
										<th>
											<s:text name="MUP04_downloadUrl"></s:text>
											<span class="highlight">
												<s:text name="global.page.required"></s:text>
											</span>
										</th>
										<td>
											<span>
												<s:textfield name="downloadUrl" cssClass="text" maxlength="50"></s:textfield>
											</span>
										</td>
									</tr>
									<tr>
										<th>
											<s:text name="MUP04_openUpdateTime"></s:text>
										</th>
	  									<td>
	  										<p class="date">
			  									<span>
			  										<s:textfield name="openUpdateTime" cssClass="date" value="%{endDate}"></s:textfield>
			  									</span>
		  									</p>
	  									</td>
									</tr>
								</table>
							</cherry:form>
						</div>
					</div>
					<hr class="space" />
					<div class="center">
						<s:a action="BINOLMOMUP04_save" id="MUP04_save"
							cssStyle="display: none;"></s:a>
						<button class="save" onclick="save();return false;">
							<span class="ui-icon icon-save"></span><span class="button-text"><s:text
									name="global.page.save"></s:text></span>
						</button>
						<button class="close" type="button"
							onclick="window.close();return false;">
							<span class="ui-icon icon-close"></span><span class="button-text"><s:text
									name="global.page.close" /></span>
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</s:i18n>
