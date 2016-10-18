<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/st/ios/BINOLSTIOS09_01.js"></script>
<%-- 入库单导入模板下载URL --%>
<s:if test='null != downloadPath && !downloadPath.equals("")'>
    <s:url id="downLoad_url" value='%{downloadPath}'/>
</s:if>
<s:else>
    <s:url id="downLoad_url" value="/download/产品入库单导入模版.xls"/>
</s:else>
<%-- 入库单导入导入URL --%>
<s:url id="import_url" value="/st/BINOLSTIOS09_import"/>
<s:url id="getDownloadPathByAjaxURL" value="/st/BINOLSTIOS09_getDownloadPathByAjax"/>
<div class="hide">
    <a id="getDownloadPathByAjaxURL" href="${getDownloadPathByAjaxURL}"></a>
</div>
<s:i18n name="i18n.st.BINOLSTIOS09">
<s:text name="global.page.select" id="select_default"/>
<div class="panel ui-corner-all">
	<div id="div_main">
	    <div class="panel-header">
	        <div class="clearfix"> 
				<span class="breadcrumb left">	    
					<span class="ui-icon icon-breadcrumb"></span><s:text name="binolstios09_native1" />&nbsp;&gt;&nbsp;<s:text name="binolstios09_native2"></s:text>
				</span>
	        </div>
	    </div>
	    <%-- ================== 信息提示区 START ======================= --%>
		<div id="actionDisplay">
			<div id="errorDiv" class="actionError" style="display:none;">
				<ul>
				    <li>
				    	<span id="errorSpan"></span>
				    </li>
				</ul>			
		  	</div>
		  	<div id="successDiv" class="actionSuccess" style="display:none;">
				<ul class="actionMessage">
			  		<li>
			  			<span id="successSpan">	</span>
			  		</li>
			 	</ul>
			</div>
		</div>
		<div id="errorMessage"></div>
		<%-- ================== 信息提示区   END  ======================= --%>
	    <form id="mainForm">
	    <div class="panel-content">
	    	<div class="box-yew">
				<div class="box-yew-header clearfix">
					<strong class="left">
						<span class="ui-icon icon-help-star-yellow"></span>
						<s:text name="binolstios09_importExplanation" />
					</strong> 
					<a id="expandCondition" style="margin-left: 25px; font-size: 12px; width: 80px" class="ui-select right"> 
						<span class="button-text choice" style="min-width: 50px;">
							<s:text name="binolstios09_moreExplanation" />
						</span> 
						<span class="ui-icon ui-icon-triangle-1-n"></span>
					</a>
				</div>
				<div class="box-yew-content">
					<div class="step-content">
						<label style="margin: 1px 0 0 0px;">1</label>
						<div class="step">
							<s:text name="binolstios09_explanation1" />
						</div>
						<div class="line"></div>
					</div>
					<div id="moreCondition" style="display: none;">
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">2</label>
							<div class="step">
								<s:text name="binolstios09_explanation2" />
							</div>
							<div class="line"></div>
						</div>
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">3</label>
							<div class="step">
								<s:text name="binolstios09_explanation3" />
							</div>
							<div class="line"></div>
						</div>
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">4</label>
							<div class="step">
								<s:text name="binolstios09_explanation5" />
							</div>
							<div class="line"></div>
						</div>
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">5</label>
							<div class="step">
								<s:text name="binolstios09_explanation4" />
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
						<s:text name="binolstios09_inDepotConditions" />
					</strong>
				</div>
				<div class="box2-content clearfix">
					 <table class="detail" cellpadding="0" cellspacing="0">
						<tr>
							<th><s:text name="binolstios09_brandName" /></th>
							<td>
								<s:if test="brandInfoList.size() > 1">
									<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" Cssstyle="width:120px;" onchange="BINOLSTIOS09_01.changeBrand();"></s:select>
								</s:if> 
								<s:else>
									<s:iterator value="brandInfoList"><s:property value="brandName" />
										<input  type="hidden" id="brandInfoId" name="brandInfoId" value='<s:property value="brandInfoId"/>'/>
									</s:iterator>
								</s:else>
							</td>
							<th><s:text name="binolstios09_importBatch" /><span class="highlight">*</span></th>
							<td>
								<span id="importBatchCodeSpan"><s:textfield name="importBatchCode" id="importBatchCode" cssClass="text" maxlength="25" /></span>
								<span>
									<input type="checkbox" id="isChecked" name="isChecked" onclick="BINOLSTIOS09_01.isChecked(this);">
									 <s:text name="binolstios09_fromExcel" />
								</span>
							</td>
						</tr>
						 <tr>
						 	<th><s:text name="binolstios09_importRepeatData" /><span class="highlight">*</span></th>
							<td>
								<span>
									<select name="importRepeat" id="importRepeat" >
										<option value=""><s:text name="binolstios09_select" /></option>
										<option value="0"><s:text name="binolstios09_no" /></option>
										<option value="1"><s:text name="binolstios09_yes" /></option>
									</select>
								</span>
							</td>
							<th><s:text name="binolstios09_inDepotReson" /><span class="highlight">*</span></th>
							<td style="word-wrap: break-word; word-break: break-all;" >
								<span>
									<s:textarea cssStyle="height:25px;"  maxlength="200" id="comments" name="comments"></s:textarea>
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
						<s:text name="binolstios09_title" />
					</strong>
				</div> 
		        <div class="box2-content clearfix">
	        		<div class="box4-content">
						<span style="margin-left:10px; display: inline;" class="left hide"> 
							<span class="highlight"><s:text name="binolstios09_snow" /></span>
							<s:text name="binolstios09_notice"/>
                            <a id="downloadURL" href="${downLoad_url }"><s:text name="binolstios09_down"/></a>
						    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
						    <input type="button" value="<s:text name="global.page.browse"/>"/>
						    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="BINOLSTIOS09_01.deleteActionMsg();pathExcel.value=this.value;return false;" /> 
						    <input type="button" value="<s:text name="binolstios09_importExcel_btn"/>" onclick="BINOLSTIOS09_01.ajaxFileUpload('${import_url}');return false;" id="upload"/>
	        				<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
						</span>
					</div>
		        </div>
		    </div>
		     <div class="center clearfix" id="closeButton">
	            <button onclick="BINOLSTIOS09_01.close();return false;" type="button" class="close" id="close">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
		    </div>
	    </div>
	    </form>
	   	<%-- 错误信息 --%>
	    <div id="errMsg">
	    	<%-- 请选择上传文件。 --%>
	    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/>
	    	<input type="hidden" id="errmsg2" value='<s:text name="binolstios09_error1"/>'/>
	    	<input type="hidden" id="errmsg3" value='<s:text name="binolstios09_error2"/>'/>
	    	<input type="hidden" id="errmsg4" value='<s:text name="binolstios09_error3"/>'/>
	    </div>
	</div>
</div>
</s:i18n>
