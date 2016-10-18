<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS25.js"></script>
<%-- 产品模板下载URL --%>
<s:url id="downLoad_url" value="/download/产品导入模板(门店用).xls"/>
<%-- 产品批量导入URL --%>
<s:url id="import_url" value="/pt/BINOLPTJCS25_import"/>
<s:i18n name="i18n.pt.BINOLPTJCS05">
    <div class="panel-header">
        <div class="clearfix"> 
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
        </div>
    </div>
    <%-- ================== 信息提示区 START ======================= --%>
	<div id="errorMessage"></div>
	<%-- ================== 信息提示区   END  ======================= --%>
    <div class="panel-content">
	    	<div class="box-yew">
				<div class="box-yew-header clearfix">
					<strong class="left">
						<span class="ui-icon icon-help-star-yellow"></span>
						<s:text name="JCS05_importExplanation" />
					</strong> 
					<a id="expandCondition" style="margin-left: 25px; font-size: 12px; width: 80px" class="ui-select right"> 
						<span class="button-text choice" style="min-width: 50px;">
							<s:text name="JCS05_moreExplanation" />
						</span> 
						<span class="ui-icon ui-icon-triangle-1-n"></span>
					</a>
				</div>
				<div class="box-yew-content">
					<div class="step-content">
						<label style="margin: 1px 0 0 0px;">1</label>
						<div class="step">
						<%--<s:text name="binolstios09_explanation1" /> --%>
						<span>厂商编码在系统中是唯一的，其作为产品导入时的唯一性标记。</span>
						</div>
						<div class="line"></div>
					</div>
					<div id="moreCondition" style="display: none;">
						<div class="step-content hide">
							<label style="margin: 1px 0 0 0px;">2</label>
							<div class="step">
							<%-- 
								<span>如果产品方案中已经存在产品，则导入excel时会覆盖产品方案中原来的产品。
								若产品方案已分配过，则老产品方案分配的柜台将被无效掉，并将新产品方案分配到柜台。
								</span>
								--%>
								<span>如果系统中存在一个条码被多个产品使用，则都会被更新。
								</span>
								
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
						<s:text name="JCS05_title" />
					</strong>
				</div> 
		        <div class="box2-content clearfix">
	        		<div class="box4-content">
						<span style="margin-left:10px; display: inline;" class="left hide"> 
							<span class="highlight"><s:text name="JCS05_snow"/></span><s:text name="JCS05_notice"/>
                            <a id="downloadURL" href="${downLoad_url }"><s:text name="JCS05_download"/></a>
						    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
						    <input type="button" value="<s:text name="global.page.browse"/>"/>
						    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="pathExcel.value=this.value;BINOLPTJCS25.deleteActionMsg();return false;" /> 
						    <input type="button" value="<s:text name="JCS05_importExcel_btn"/>" onclick="BINOLPTJCS25.ajaxFileUpload('${import_url}');return false;" id="upload"/>
	        				<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
						</span>
					</div>
		        </div>
		    </div>
			
			
        <div class="section-content hide" >
        	<cherry:form id="mainForm" class="inline">
        		<div>
					<span class="highlight"><s:text name="JCS05_snow"/></span><s:text name="JCS05_notice"/>
					<a href="${downLoad_url }"><s:text name="JCS05_download"/></a>
					<s:if test="brandInfoList.size() > 1">
					<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"></s:select>
					</s:if>
					<s:else><input type="hidden" id="brandInfoId" name="brandInfoId" value='<s:property value="brandInfoId"/>'/></s:else>
				</div>
        		<span>
        			<input class="input_text" type="text" id="pathExcel" name="pathExcel" />
					<input type="button" value="<s:text name="global.page.browse"/>"/>
        			<input type="file" name="upExcel" id="upExcel" class="input_file" size="33" onchange="pathExcel.value=this.value;BINOLPTJCS25.deleteActionMsg();return false;"/>
        			<input type="button" value="<s:text name="JCS05_importExcel_btn"/>" onclick="BINOLPTJCS25.ajaxFileUpload('${import_url}');return false;" id="upload"/>
        			<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">  
        		</span> 
        	</cherry:form>
        </div>
    </div>
    <%-- 错误信息 --%>
    <div id="errMsg">
    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/><%-- 请选择上传文件。 --%>
    </div>
</s:i18n>