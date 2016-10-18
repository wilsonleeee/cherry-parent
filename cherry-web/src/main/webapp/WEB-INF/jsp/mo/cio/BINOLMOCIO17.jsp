<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/mo/cio/BINOLMOCIO17.js"></script>
<s:i18n name="i18n.mo.BINOLMOCIO17">
	<form id="mainForm">
		<s:text name="global.page.all" id="select_default"/>
		<div class="panel ui-corner-all">
			<div id="div_main">
				<div class="panel-header">
					<div class="clearfix"> 
						<span class="breadcrumb left">	    
							<span class="ui-icon icon-breadcrumb"></span>
							<s:text name="CIO17_native1" />&nbsp;&gt;&nbsp;
							<s:text name="CIO17_native2" />
						</span>
					</div>
				</div>
				<div id="errorMessage"></div>
 				<div id="actionResultDisplay"></div>
 				<div class="panel-content clearfix">
 					<div class="box">
 						<input type="hidden" value="<s:property value="importBatchId"/>"  name="importBatchId" />
 						<div class="box-header"> 
 							<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
 						</div>
 						<div class="box-content clearfix">
 							<div class="column" style="width:50%; height: auto;">
 								<p>
						        	<label style="width:80px;"><s:text name="CIO17_messageTitle" /></label>
						            <span>
						            	<s:textfield name="messageTitle" cssClass="text" maxlength="10"></s:textfield>
									</span>
						        </p>
						        <p>
						        	<label style="width:80px;"><s:text name="CIO17_messageBody" /></label>
						            <span>
						            	<s:textfield name="messageBody" cssClass="text" maxlength="50"></s:textfield>
									</span>
						        </p>
						        <p>
						          	<label style="width:80px;"><s:text name="CIO17_counterCode" /></label>
						            <span>
						            	<s:textfield name="counterCode" cssClass="text" maxlength="15"></s:textfield>
									</span>
						        </p>
						        <p>
						          	<label style="width:80px;"><s:text name="CIO17_counterName" /></label>
						            <span>
						            	<s:textfield name="counterName" cssClass="text" maxlength="50"></s:textfield>
									</span>
						        </p>
 							</div>
 							<div class="column last" style="width:49%; height: auto;">
 								<p class="date">
						        	<label style="width:80px;"><s:text name="CIO17_startValidDate"/></label>
						            <span>
						            	<s:textfield id="startValidDateBegin" name="startValidDateBegin" cssClass="date"/>
						            </span>
						            - 
						            <span>
						            	<s:textfield id="startValidDateFinish" name="startValidDateFinish" cssClass="date"/>
						            </span>
						        </p>
						        <p class="date">
						        	<label style="width:80px;"><s:text name="CIO17_endValidDate"/></label>
						            <span>
						            	<s:textfield id="endValidDateBegin" name="endValidDateBegin" cssClass="date"/>
						            </span>
						            - 
						            <span>
						            	<s:textfield id="endValidDateFinish" name="endValidDateFinish" cssClass="date"/>
						            </span>
						        </p>
						        <p>
						        	<label style="width:80px;"><s:text name="CIO17_importResult"/></label>
						            <span>
						            	<s:select list='#application.CodeTable.getCodes("1250")' listKey="CodeKey" listValue="Value" id="importResult" name="importResult"  
										headerKey="" cssStyle="width:120px;" headerValue="%{#select_default}"
										onchange="BINOLMOCIO17.search();return false;"></s:select>
						            </span>
						        </p>
						        <p>
						        	<label style="width:80px;"><s:text name="CIO17_publishStatus"/></label>
						            <span>
						            	<s:select list='#application.CodeTable.getCodes("1177")' listKey="CodeKey" listValue="Value" id="publishStatus" name="publishStatus"  
										headerKey="" sccStyle="width:120px;" headerValue="%{#select_default}"
										onchange="BINOLMOCIO17.search();return false;"></s:select>
						            </span>
						        </p>
 							</div>
 						</div>
 						<p class="clearfix">
	 						<button class="right search" onclick="BINOLMOCIO17.search();return false;">
					      		<span class="ui-icon icon-search-big"></span>
						      	<span class="button-text">
						      		<s:text name="global.page.search"></s:text>
					      		</span>
				      		</button>
 						</p>
 					</div>
 					<div class="section hide" id="cntMsgImportInfo">
 						<div class="section-header">
 							<strong>
							  	<span class="ui-icon icon-ttl-section-search-result"></span>
							  	<s:text name="global.page.list"></s:text>
						  	</strong>
 						</div>
	 					<div class="section-content">
	 						<div class="toolbar clearfix">
	 							<cherry:show domId="BINOLMOCIO16EXP">
									<a  class="export left" onclick="javascript:BINOLMOCIO17.exportExcel();return false;">
										<span class="ui-icon icon-export"></span>
										<span class="button-text"><s:text name="CIO17_exportExcel_btn"></s:text></span>
									</a>
							  	</cherry:show>
							  	<span class="right">
						  			<a class="setting">
						  				<span class="ui-icon icon-setting"></span>
					              		<span class="button-text"><s:text name="global.page.colSetting"/></span>
				              		</a>
				              	</span>
	 						</div>
	 						<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="cntMsgImportInfoDataTable">
	 							<thead>
							        <tr>
										<th><s:text name="NO."></s:text></th>
										<th><s:text name="CIO17_messageTitle"></s:text></th>
										<th><s:text name="CIO17_messageBody"></s:text></th>
										<th><s:text name="CIO17_startValidDate"></s:text></th>
										<th><s:text name="CIO17_endValidDate"></s:text></th>
										<th><s:text name="CIO17_importDate"></s:text></th>
										<th><s:text name="CIO17_importResult"></s:text></th>
										<th><s:text name="CIO17_publishStatus"></s:text></th>
							        </tr>
								</thead>
								<tbody></tbody>
	 						</table>
	 					</div>
 					</div>
 				</div>
			</div>
		</div>
		<div class="hide">
			<s:url action="BINOLMOCIO17_search" id="searchUrl" />
			<a href="${searchUrl}" id="search_Url"></a>
			<s:url value="BINOLMOCIO17_getCntMsgImportDetail" id="detaiURL"/>
			<a href="${detaiURL }" id="detai_URL"></a>
			<%-- 柜台消息（Excel导入）明细导出URL --%>
			<s:url action="BINOLMOCIO17_export" id="export_url"/>
			<a href="${export_url }" id="exportURL"></a>
			<div id="popuTitle"><s:text name="CIO17_popuTitle" /></div>
			<div id="popuOK"><s:text name="global.page.ok" /></div>
			<div id="processing"><span class="dataTables_processing"></span></div>
		</div>
	</form>
</s:i18n>
<script type="text/javascript">
        // 节日
        var holidays = '${holidays }';
        // 生效开始日期的查询日期段
        $('#startValidDateBegin').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#startValidDateFinish').val();
                return [value,'maxDate'];
            }
        });
        $('#startValidDateFinish').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#startValidDateBegin').val();
                return [value,'minDate'];
            }
        });
     	// 生效结束日期的查询日期段
        $('#endValidDateBegin').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#endValidDateFinish').val();
                return [value,'maxDate'];
            }
        });
        $('#endValidDateFinish').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#endValidDateBegin').val();
                return [value,'minDate'];
            }
        });
</script>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<div id="detailData" class="hide"></div>
