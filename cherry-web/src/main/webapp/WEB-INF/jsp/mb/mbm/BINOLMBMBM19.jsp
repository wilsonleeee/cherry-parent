<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM19.js"></script>
<s:i18n name="i18n.mb.BINOLMBMBM19">
<%-- 模板下载URL --%>
<s:url id="downLoad_url_A" value="/download/新增会员档案模板.xls"/>
<s:url id="downLoad_url_B" value="/download/更新会员档案模板.xls"/>
<%-- 批量导入URL --%>
<s:url id="import_url" value="/mb/BINOLMBMBM19_importMember"/>
<s:text name="global.page.select" id="select_default"/>
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
<div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="binolmbmbm19_importExcel_title" />&nbsp;&gt;&nbsp;<s:text name="binolmbmbm19_importMem"></s:text></span> 
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
     <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span><s:text name="binolmbmbm19_importExplanation" /></strong> <a id="expandCondition" style="margin-left: 0px; font-size: 12px;width:80px" class="ui-select right">
	        <span class="button-text choice" style="min-width: 50px;"><s:text name="binolmbmbm19_moreExplanation"/></span>
	 		<span class="ui-icon ui-icon-triangle-1-n"></span>
		</a></div>
        <div class="box-yew-content">
        	<s:if test="addMemflag">
	        	<div class="step-content">
	                <label  style="margin:1px 0 0 0px;">*</label>
	                <div class="step">
	                    <s:text name="binolmbmbm19_explanation1"/>                                                                                         
	                </div>
	               <div class="line"></div>
	            </div>
           </s:if>
           <s:if test="updateMemflag">
	             <div class="step-content">
	                <label  style="margin:1px 0 0 0px;">*</label>
	                <div class="step">
	                   <s:text name="binolmbmbm19_explanation2"/>                                 
	                </div>
	                <div class="line"></div>
	             </div>
           </s:if>
            <div id="moreCondition" style="display: none;">
            <div class="step-content">
            <label  style="margin:1px 0 0 0px;">*</label>
            <div class="step">
                <s:text name="binolmbmbm19_explanation3"/>                                                 
            </div>
              <div class="line"></div>
       		 </div>
   		 </div>
 </div>
  </div>
<form id="importForm" >
<div class="box2 box2-active">
			<div class="box2-header clearfix"><strong class="active left"><span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm19_importCondit" /></strong></div>
			<div class="box2-content clearfix">
  <div class="section-content" id="detail">
 <table class="detail" cellpadding="0" cellspacing="0">
		<tr>
			<th><s:text name="binolmbmbm19_brandName" /></th>
			<td>
				<s:if test="brandInfoList.size() > 1">
					<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" Cssstyle="width:120px;"></s:select>
				</s:if> 
				<s:else>
					<s:iterator value="brandInfoList"><s:property value="brandName" /></s:iterator>
					<input class="hide" name="brandInfoId" value='<s:property value="brandInfoId"/>'/>
				</s:else>
			</td>
			<th><s:text name="binolmbmbm19_importName" /><span class="highlight">*</span></th>
			<td>
				<span>
                  <s:textfield name="importName" cssClass="text" maxlength="30"></s:textfield>
				</span>
			</td>
		</tr>
		<tr>
			<th><s:text name="binolmbmbm19_importType" /><span class="highlight">*</span></th>
			<td colspan="3"> 
				<s:if test="addMemflag">
					<span>
			        	<input type="radio" id="importType_1" name="importType" value="1" onclick="BINOLMBMBM19.changeMode(this);" checked="checked">
			        	<label for="importType_1">
				        	<span class="bg_yew" id="bg_yew_1">
					        	<span style="line-height:20px;" class="highlight" id="highlight_1">
					        	<s:text name="binolmbmbm19_import_Type_1" />
					        	</span>
				        	</span>
			        	</label>
			        </span>
				</s:if>		
				<s:if test="updateMemflag">		
					<span>
			        	<input type="radio" id="importType_2" name="importType" value="2" onclick="BINOLMBMBM19.changeMode(this);" <s:if test="!addMemflag">checked="checked"</s:if>>
			        	<label for="importType_2">
				        	<span id="bg_yew_2">
					        	<span style="line-height:20px;" id="highlight_2">
					        	<s:text name="binolmbmbm19_import_Type_2" />
					        	</span>
					        </span>
						</label>
			        </span>
		        </s:if>
			</td>
		</tr>
		 <tr>
			<th><s:text name="binolmbmbm19_reason" /><span class="highlight">*</span></th>
			<td style="word-wrap: break-word; word-break: break-all"  colspan="3">
			 <span style="width:79%">
				<input  id="reason" style="height:45px;width:100%;" class="text" maxlength="100"  name="reason">
			</span>
			</td>
		</tr>
	</table>    
  </div>
</div> 
</div> 
</form>
<div class="box2 box2-active">
		<div class="box2-header clearfix"><strong class="active left"><img id="excel" src="/Cherry/css/common/blueprint/images/excel.png">  <s:text name="binolmbmbm19_importBatch" /></strong></div>
		<div class="box2-content clearfix">
	        	  <div style="overflow-x:auto;overflow-y:hidden" id="table_scroll">
	        		<div class="box4-content">
						<div style="margin-top: 0px;" class="relation clearfix">
							<span class="left" style="margin:5px 0 0 5px;"> <span class="highlight">※</span>
							<s:if test="addMemflag">
								<span id="downLoad_A" >
									<s:text name="binolmbmbm19_notice" /> <a href="${downLoad_url_A }"><s:text name="binolmbmbm19_downloadAdd" /></a>
								</span>
							</s:if>
							<s:if test="updateMemflag">
								<span id="downLoad_B" class="hide">
									<s:text name="binolmbmbm19_notice" /> <a href="${downLoad_url_B }"><s:text name="binolmbmbm19_downloadUpd" /></a>
								</span>
							</s:if>
							</span> 
							<span style="margin-left:10px; display: inline;" class="left hide"> 
								<input class="input_text" type="text" id="pathExcel" name="pathExcel" />
								<input type="button" value="<s:text name="global.page.browse"/>"/>
								<input class="input_file" size="33" type="file" name="upExcel" id="upExcel" onchange="pathExcel.value=this.value;BINOLMBMBM19.deleteActionMsg();return false;" />
							    <s:if test="addMemflag || updateMemflag">
								<input type="button" value="<s:text name="binolmbmbm19_importBatch"/>" onclick="BINOLMBMBM19.ajaxFileUpload('${import_url}');" id="upload" /> 
								</s:if>
								<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display: none;">
							</span>
						</div>
				</div>
	          <hr class="space">
	        </div>
	      </div>
	</div>
	<div class="center clearfix" id="closeButton">
            <button onclick="BINOLMBMBM19.close();return false;" type="button" class="close" id="close">
           		<span class="ui-icon icon-close"></span>
           		<span class="button-text"><s:text name="binolmbmbm19_close"/></span>
         	</button>
    </div>
</div>
</div>
</div>
  <%-- 错误信息 --%>
    <div id="errMsg">
    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/><%-- 请选择上传文件。 --%>
      	<input type="hidden" id="exportTip1" value='<s:text name="binolmbmbm19_exportTip1"/>'/><%-- exportTip1 --%>
  		<input type="hidden" id="exportTip2" value='<s:text name="binolmbmbm19_exportTip2"/>'/><%-- exportTip2 --%>
    </div>
</s:i18n> 