<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/unq/BINOLPTUNQ03.js"></script>

<s:i18n name="i18n.pt.BINOLPTUNQ03">
<%-- 唯一码维护模板下载URL --%>
<s:url id="downLoad_url" value="/download/唯一码维护模板.xls"/>

<s:url id="import_url" value="/pt/BINOLPTUNQ03_importUnqCodeDetails"/> <%-- 唯一码明细维护（批量导入） --%>
<s:hidden name="importUrlID" value="%{import_url}"/>

    <div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
		</div>
    </div>
   <%-- ================== 错误信息提示 START ======================= --%>
	<div id="actionResultDisplay"></div>
	<div id="errorMessage"></div>
	<div style="display: none" id="successMessageTemp">
		<div class="actionSuccess">
		<ul><li><span><s:text name="UNQ00009"/></span></li></ul>      
		</div>
	</div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
		<ul><li><span><s:text name="UNQ00011"/></span></li></ul>      
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
 <div class="panel-content"> 
  <div class="box">
           <cherry:form id="mainForm" class="inline">
	    <div class="panel-content">
	    	<div class="box-yew">
				<div class="box-yew-header clearfix">
					<strong class="left">
						<span class="ui-icon icon-help-star-yellow"></span>
						<s:text name="PTUNQ.importExplanation" />
					</strong> 
					<a id="expandCondition" style="margin-left: 25px; font-size: 12px; width: 80px" class="ui-select right"> 
						<span class="button-text choice" style="min-width: 50px;">
							<s:text name="PTUNQ.moreExplanation" />
						</span> 
						<span class="ui-icon ui-icon-triangle-1-n"></span>
					</a>
				</div>
				<div class="box-yew-content">
					<div class="step-content">
						<label style="margin: 1px 0 0 0px;">1</label>
						<div class="step">
						<span><s:text name="PTUNQ.importExplanationDetail1" /></span>
						</div>
						<div class="line"></div>
					</div>
					<div id="moreCondition" style="display: none;">
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">2</label>
							<div class="step">
								<span><s:text name="PTUNQ.importExplanationDetail2" /></span>
							</div>
							<div class="line"></div>
						</div>
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">3</label>
							<div class="step">
								<span><s:text name="PTUNQ.importExplanationDetail3" /></span>
							</div>
							<div class="line"></div>
						</div>
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">4</label>
							<div class="step">
								<span><s:text name="PTUNQ.importExplanationDetail4" /></span>
							</div>
							<div class="line"></div>
						</div>
							<div class="step-content">
							<label style="margin: 1px 0 0 0px;">5</label>
							<div class="step">
								<span><s:text name="PTUNQ.importExplanationDetail5" /></span>
							</div>
							<div class="line"></div>
						</div>
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">6</label>
							<div class="step">
								<span><s:text name="PTUNQ.importExplanationDetail6" /></span>
							</div>
							<div class="line"></div>
						</div>					
						</div>
				</div>
			</div>	    	    
			<div class="box2 box2-active">
				<div class="box2-header clearfix">
					<strong class="active left">
						<img id="excel" src="/Cherry/css/common/blueprint/images/excel.png">
						<s:text name="批量导入" />
					</strong>
				</div> 
		        <div class="box2-content clearfix">
	        		<div class="box4-content">
						<span style="margin-left:10px; display: inline;" class="left hide"> 
							<span class="highlight"><s:text name="※ " /></span>
							<s:text name="PTUNQ.pleaseUseStandardModel"/>
                            <a id="downloadURL" href="${downLoad_url }"><s:text name="PTUNQ.modelDown"/></a>
						    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
						    <input type="button" value="<s:text name="global.page.browse"/>"/>
						    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="BINOLPTUNQ03.deleteActionMsg();pathExcel.value=this.value;return false;" /> 
						    <input type="button" value="<s:text name="PTUNQ.batchImport"/>" onclick="BINOLPTUNQ03.ajaxFileUpload('${import_url}');return false;" id="upload"/>
	        				<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
						</span>
					</div>
		        </div>
		    </div>
	    </div>
	    </cherry:form>
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

<div class="hide">
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
</div>
