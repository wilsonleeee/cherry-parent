<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/cp/act/BINOLCPACT13.js"></script>
<s:i18n name="i18n.cp.BINOLCPACT13">
<%-- 模板下载URL --%>
<s:url id="downLoad_url" value="/download/活动产品库存模板.xls"/>
<%-- 批量导入URL --%>
<s:url id="import_url" value="/cp/BINOLCPACT13_importStock"/>
<s:text name="global.page.select" id="select_default"/>
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
<div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="ACT13_importExcel_title" />&nbsp;&gt;&nbsp;<s:text name="ACT13_importMem"></s:text></span> 
  </div>
</div>
  <%-- ================== 信息提示区 START ======================= --%>
   <div id="actionDisplay">
	<div id="errorDiv" class="actionError" style="display:none;">
		<ul>
		    <li><span id="errorSpan"></span></li>
		</ul>			
  	</div>
  	<div id="successDiv" class="actionSuccess" style="display:none;">
		<ul class="actionMessage">
	  		<li><span id="successSpan"></span></li>
	 	</ul>
	</div>
	</div>
	<div id="errorMessage"></div>
	<%-- ================== 信息提示区   END  ======================= --%>
<div class="panel-content clearfix">
<div class="box-yew">
	<div class="box-yew-header clearfix">
		<strong class="left"><span class="ui-icon icon-help-star-yellow"></span><s:text name="ACT13_importExplanation" /></strong> 
		<a id="expandCondition" style="margin-left: 0px; font-size: 12px;width:80px" class="ui-select right">
	        <span class="button-text choice" style="min-width: 50px;"><s:text name="ACT13_moreExplanation"/></span>
	 		<span class="ui-icon ui-icon-triangle-1-n"></span>
		</a>
	</div>
	<div class="box-yew-content">
	  	<div class="step-content">
			<label  style="margin:1px 0 0 0px;">1</label>
			<div class="step">
				<s:text name="ACT13_explanation1"/>                                                                                         
		    </div>
			<div class="line"></div>
		</div>
		<div class="step-content">
			<label  style="margin:1px 0 0 0px;">2</label>
			<div class="step">
				<s:text name="ACT13_explanation2"/>                                 
		    </div>
		    <div class="line"></div>
		</div>
		<div id="moreCondition" style="display: none;">
			<div class="step-content">
				<label  style="margin:1px 0 0 0px;">3</label>
				<div class="step">
			    	<s:text name="ACT13_explanation3"/>                                                 
			    </div>
			    <div class="line"></div>
			</div>
			<div class="step-content">
				<label  style="margin:1px 0 0 0px;">4</label>
				<div class="step">
			    	<s:text name="ACT13_explanation4"/>                                                 
			    </div>
			    <div class="line"></div>
			</div>
		</div>
	</div>
</div>
<form id="importForm" >
<div class="box2 box2-active">
	<div class="box2-header clearfix">
		<strong class="active left"><span class="ui-icon icon-ttl-section-info"></span><s:text name="ACT13_importCondit" /></strong>
	</div>
	<div class="box2-content clearfix">
		<div class="section-content" id="detail">
		<table class="detail" cellpadding="0" cellspacing="0">
			<tr>
				<th><s:text name="ACT13_brandName" /></th>
				<td>
					<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" Cssstyle="width:120px;"></s:select>
				</td>
				<th><s:text name="ACT13_importType" /><span class="highlight">*</span></th>
				<td> 
					<p class="clearfix">
	                    <input id="importAdd" name="importType" type="radio" value="0" checked/><s:text name="ACT13_import_Type_1"></s:text>
	                    <input id="importUpdate" name="importType" type="radio" value="1" /><s:text name="ACT13_import_Type_2"></s:text>
	                </p>
				</td>
			</tr>
		</table>    
		</div>
	</div> 
</div>
</form>
<div class="box2 box2-active">
		<div class="box2-header clearfix"><strong class="active left"><img id="excel" src="/Cherry/css/common/blueprint/images/excel.png">  <s:text name="ACT13_importBatch" /></strong></div>
		<div class="box2-content clearfix">
	        	  <div style="overflow-x:auto;overflow-y:hidden" id="table_scroll">
	        		<div class="box4-content">
						<div style="margin-top: 0px;" class="relation clearfix">
							<span class="left" style="margin:5px 0 0 5px;"> <span class="highlight">※</span>
								<span id="downLoad" >
									<s:text name="ACT13_notice" /> <a href="${downLoad_url }"><s:text name="ACT13_download" /></a>
								</span>
							</span> 
							<span style="margin-left:10px; display: inline;" class="left hide"> 
								<input class="input_text" type="text" id="pathExcel" name="pathExcel" />
								<input type="button" value="<s:text name="global.page.browse"/>"/>
								<input class="input_file" size="33" type="file" name="upExcel" id="upExcel" onchange="pathExcel.value=this.value;BINOLCPACT13.deleteActionMsg();return false;" />
								<input type="button" value="<s:text name="ACT13_importBatch"/>" onclick="BINOLCPACT13.ajaxFileUpload('${import_url}');" id="upload" /> 
								<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display: none;">
							</span>
						</div>
				</div>
	          <hr class="space">
	        </div>
	      </div>
	</div>
	<div class="center clearfix" id="closeButton">
            <button onclick="BINOLCPACT13.close();return false;" type="button" class="close" id="close">
           		<span class="ui-icon icon-close"></span>
           		<span class="button-text"><s:text name="ACT13_close"/></span>
         	</button>
    </div>
</div>
</div>
</div>
  <%-- 错误信息 --%>
    <div id="errMsg">
    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/><%-- 请选择上传文件。 --%>
    	<input type="hidden" id="exportTip1" value='<s:text name="ACT13_exportTip1"/>'/><%-- exportTip1 --%>
  		<input type="hidden" id="exportTip2" value='<s:text name="binolmbmbm19_exportTip2"/>'/><%-- exportTip2 --%>
    </div>
</s:i18n> 