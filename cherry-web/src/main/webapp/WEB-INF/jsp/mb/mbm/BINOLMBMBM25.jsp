<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM25.js"></script>
<s:i18n name="i18n.mb.BINOLMBMBM25">
<%-- 模板下载URL --%>
<s:url id="downLoad_url" value="/download/会员关键属性模板.xls"/>
<%-- 批量导入URL --%>
<s:url id="import_url" value="/mb/BINOLMBMBM25_importMember"/>
<s:text name="global.page.select" id="select_default"/>
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
<div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="binolmbmbm25_importPoint" />&nbsp;&gt;&nbsp;<s:text name="binolmbmbm25_importExcel_title"></s:text></span> 
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
     <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span><s:text name="binolmbmbm25_importExplanation" /></strong> <a id="expandCondition" style="margin-left: 25px; font-size: 12px;width:80px" class="ui-select right">
	        <span class="button-text choice" style="min-width: 50px;"><s:text name="binolmbmbm25_moreExplanation"/></span>
	 		<span class="ui-icon ui-icon-triangle-1-n"></span>
		</a></div>
        <div class="box-yew-content">
        	<div class="step-content">
                <label  style="margin:1px 0 0 0px;">1</label>
                <div class="step">
				<s:text name="binolmbmbm25_explanation1"/>                                                                        
                </div>
               <div class="line"></div>
            </div>
                <div id="moreCondition" style="display: none;">
                <div class="step-content">
                <label  style="margin:1px 0 0 0px;">2</label>
                <div class="step">
                    <s:text name="binolmbmbm25_explanation2"/>                            
                </div>
                  <div class="line"></div>
                </div>
       		 </div>
 </div>
  </div>
<form id="importForm" >
<div class="box2 box2-active">
			<div class="box2-header clearfix"><strong class="active left"><span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm25_importExcel_con" /></strong></div>
			<div class="box2-content clearfix">
 <table class="detail" cellpadding="0" cellspacing="0">
		<tr>
			<th><s:text name="binolmbmbm25_brand" /></th>
			<td>
				<s:if test="brandInfoList.size() > 1">
					<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" Cssstyle="width:120px;"></s:select>
				</s:if> 
				<s:else>
					<s:iterator value="brandInfoList"><s:property value="brandName" /></s:iterator>
					<input class="hide" name="brandInfoId" value='<s:property value="brandInfoId"/>'/>
				</s:else>
			</td>
			<th><s:text name="binolmbmbm25_importName" /><span class="highlight">*</span></th>
			<td>
				<span>
                  <s:textfield name="importName" cssClass="text" maxlength="30"></s:textfield>
				</span>
			</td>
		</tr>
		 <tr>
		  <s:if test='%{!"3".equals(clubMod)}'>
		  <th><s:text name="binolmbptm25_memclub" /></th>
			<td>
				<s:if test='%{clubList != null && clubList.size() != 0}'>
				   <s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" Cssstyle="width:150px;"/>
                </s:if>
			</td>
		   </s:if>
			<th><s:text name="binolmbmbm25_reason" /><span class="highlight">*</span></th>
			<td style="word-wrap: break-word; word-break: break-all" <s:if test='%{"3".equals(clubMod)}'> colspan="3"  </s:if>>
				<span style="width:80%">
					<input  id="reason" style="height:45px;width:100%;" class="text" maxlength="100"  name="reason">
				</span>
			</td>
		</tr>
	</table>    
	</div>
</div> 
</form>

<div class="box2 box2-active">
		<div class="box2-header clearfix"><strong class="active left"><img id="excel" src="/Cherry/css/common/blueprint/images/excel.png">  <s:text name="binolmbmbm25_importBatch" /></strong></div>
		<div class="box2-content clearfix">
	        	  <div style="overflow-x:auto;overflow-y:hidden" id="table_scroll">
	        		<div class="box4-content">
						<div style="margin-top: 0px;" class="relation clearfix">
							<span class="left" style="margin:5px 0 0 5px;"> <span class="highlight">※</span>
								<s:text name="binolmbmbm25_notice" /> <a href="${downLoad_url }"><s:text name="binolmbmbm25_download" /></a>
							</span> 
							<span style="margin-left:10px; display: inline;" class="left hide"> 
							    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
								<input type="button" value="<s:text name="global.page.browse"/>"/>
								<input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="pathExcel.value=this.value;BINOLMBMBM25.deleteActionMsg();return false;" /> 
								<input type="button" value="<s:text name="binolmbmbm25_importBatch"/>" onclick="BINOLMBMBM25.ajaxFileUpload('${import_url}');" id="upload" /> 
								<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display: none;">
							</span>
						</div>
				</div>
	          <hr class="space">
	        </div>
       </div>
       </div>
	<div class="center clearfix" id="closeButton">
            <button onclick="BINOLMBMBM25.close();return false;" type="button" class="close" id="close">
           		<span class="ui-icon icon-close"></span>
           		<span class="button-text"><s:text name="binolmbmbm25_close"/></span>
         	</button>
    </div>
</div>
</div>
</div>
  <%-- 错误信息 --%>
    <div id="errMsg">
    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/><%-- 请选择上传文件。 --%>
    	<input type="hidden" id="errmsg2" value='<s:text name="binolmbmbm25_selectPointType"/>'/><%-- 请选择积分类型。 --%>
  		<input type="hidden" id="errmsg3" value='<s:text name="binolmbmbm25_selectImportType"/>'/><%-- 请选择导入方式。 --%>
  		<input type="hidden" id="errmsg4" value='<s:text name="binolmbmbm25_reasonNotNull"/>'/><%-- 请选择导入方式。 --%>
  		<input type="hidden" id="exportTip1" value='<s:text name="binolmbmbm25_exportTip1"/>'/><%-- exportTip1 --%>
  		<input type="hidden" id="exportTip2" value='<s:text name="binolmbmbm25_exportTip2"/>'/><%-- exportTip2 --%>
    </div>
</s:i18n> 