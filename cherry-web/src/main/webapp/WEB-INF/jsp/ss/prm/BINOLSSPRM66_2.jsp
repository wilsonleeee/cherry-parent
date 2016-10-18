<%--入库单导入 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true" />
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM66_2.js"></script>
<div class="hide">
	<s:url namespace="/ss" action="BINOLSSPRM66_importPrmInDepot" id="import_url" />
	<s:url id="downLoad_url" value="/download/促销品入库单导入模版.xls"/>
</div>
<s:i18n name="i18n.ss.BINOLSSPRM66">
<form id="importForm">
	<div class="panel ui-corner-all">
		<div id="div_main">
			<div class="panel-header">
				<div class="clearfix">
					<span class="breadcrumb left"> 
						<span class="ui-icon icon-breadcrumb"></span>
						<s:text name="SSPRM66_import_title" />&nbsp;&gt;&nbsp;
						<s:text name="SSPRM66_import"></s:text>
					</span>
				</div>
			</div>
			<%-- ================== 信息提示区 START ======================= --%>
			
			<div id="actionResultDisplay"></div>
			<div id="errorMessage"></div>
			<%-- ================== 信息提示区   END  ======================= --%>
			<div class="panel-content clearfix">
				<div class="box-yew">
					<div class="box-yew-header clearfix">
						<strong class="left">
							<span class="ui-icon icon-help-star-yellow"></span>
							<s:text name="SSPRM66_importExplanation" />
						</strong> 
						<a id="expandCondition" style="margin-left: 25px; font-size: 12px; width: 80px" class="ui-select right"> 
							<span class="button-text choice" style="min-width: 50px;">
								<s:text name="SSPRM66_moreExplanation" />
							</span> 
							<span class="ui-icon ui-icon-triangle-1-n"></span>
						</a>
					</div>
					<div class="box-yew-content">
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">1</label>
							<div class="step">
								<s:text name="SSPRM66_explanation1" />
							</div>
							<div class="line"></div>
						</div>
						<div id="moreCondition" style="display: none;">
							<div class="step-content">
								<label style="margin: 1px 0 0 0px;">2</label>
								<div class="step">
									<s:text name="SSPRM66_explanation2" />
								</div>
								<div class="line"></div>
							</div>
							<div class="step-content">
								<label style="margin: 1px 0 0 0px;">3</label>
								<div class="step">
									<s:text name="SSPRM66_explanation3" />
								</div>
								<div class="line"></div>
							</div>
							<div class="step-content">
								<label style="margin: 1px 0 0 0px;">4</label>
								<div class="step">
									<s:text name="SSPRM66_explanation4" />
								</div>
								<div class="line"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="box2 box2-active">
					<div class="box2-header clearfix">
						<strong class="active left">
							<span class="ui-icon icon-ttl-section-info"></span>
							<s:text name="SSPRM66_importCondition" />
						</strong>
					</div>
					<div class="box2-content clearfix">
						<table class="detail" cellpadding="0" cellspacing="0">
							<tr>
								<th><s:text name="SSPRM66_brandName" /></th>
								<td>
									<s:if test="brandInfoList.size() > 1">
										<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" Cssstyle="width:120px;"></s:select>
									</s:if> 
									<s:else>
										<s:iterator value="brandInfoList">
											<s:property value="brandName" />
											<input class="hide" id="brandInfoId" name="brandInfoId" value='<s:property value="brandInfoId"/>'/>
										</s:iterator>
									</s:else>
								</td>
								<th><s:text name="SSPRM66_importBatch" /><span class="highlight">*</span></th>
								<td>
									<span id="importBatchCodeSpan">
										<s:textfield name="importBatchCode" id="importBatchCode" cssClass="text" maxlength="25" />
									</span>
									<span>
										<input type="checkbox" id="isChecked" name="isChecked" onclick="BINOLSSPRM66_2.isChecked(this);">
										 <s:text name="SSPRM66_fromExcel" />
									</span>
								</td>
							</tr>
							<tr>
								<th><s:text name="SSPRM66_importRepeatData" /><span class="highlight">*</span></th>
								<td>
									<span>
										<select name="importRepeat" id="importRepeat" >
											<option value=""><s:text name="SSPRM66_select" /></option>
											<option value="0"><s:text name="SSPRM66_no" /></option>
											<option value="1"><s:text name="SSPRM66_yes" /></option>
										</select>
									</span>
								</td>
								<th><s:text name="SSPRM66_importComments" /><span class="highlight">*</span></th>
								<td>
									<span  style="width:75%">
										<textarea  id="comments" style="height:45px;width:100%;" class="text" maxlength="200"  name="comments"></textarea>
									</span>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div class="box2 box2-active">
					<div class="box2-header clearfix">
						<strong class="active left">
							<img id="excel" src="/Cherry/css/common/blueprint/images/excel.png">
							<s:text name="SSPRM66_importAll" />
						</strong>
					</div>
					<div class="box2-content clearfix">
						<div style="overflow-x:auto;overflow-y:hidden" id="table_scroll">
							<div class="box4-content">
								<div style="margin-top: 0px;" class="relation clearfix">
									<span class="left" style="margin:5px 0 0 5px;"> <span class="highlight">※</span>
										<s:text name="SSPRM66_notice" /> <a href="${downLoad_url }"><s:text name="SSPRM66_download" /></a>
									</span> 
									<span style="margin-left:10px; display: inline;" class="left hide"> 
										<input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
										<input type="button" value="<s:text name="global.page.browse"/>"/>
										<input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="pathExcel.value=this.value;BINOLSSPRM66_2.deleteActionMsg();return false;" /> 
										<input type="button" value="<s:text name="SSPRM66_importAll"/>" onclick="BINOLSSPRM66_2.ajaxFileUpload('${import_url}');" id="upload" /> 
										<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display: none;">
									</span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="center clearfix" id="closeButton">
		            <button onclick="BINOLSSPRM66_2.close();return false;" type="button" class="close" id="close">
		           		<span class="ui-icon icon-close"></span>
		           		<span class="button-text"><s:text name="global.page.close"/></span>
		         	</button>
			    </div>
			</div>
		</div>
	</div>
    <div id="errMsg">
   		<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/><%-- 请选择上传文件。 --%>
   		<input type="hidden" id="errmsg2" value='<s:text name="SSPRM66_error1"/>'/>
	    <input type="hidden" id="errmsg3" value='<s:text name="SSPRM66_error2"/>'/>
	    <input type="hidden" id="errmsg4" value='<s:text name="SSPRM66_error3"/>'/>
   	</div>
</form>
</s:i18n>