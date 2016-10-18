<%-- 菜单组菜单配置--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/pmc/BINOLMOPMC03.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<style>
.msgBody{
    width:520px;
}
.section-header1 {
    border-bottom: 1px solid #E5E5E5;
    height: 24px;
    line-height: 24px;
    padding: 0 10px 5px 5px;
}
</style>

<s:i18n name="i18n.mo.BINOLMOPMC03">
<div class="hide">
    <s:url id="getCounterTree_url" action="BINOLMOPMC03_getCounterTree"/>
    <a id="getCounterTreeUrl" href="${getCounterTree_url}"></a>
    <s:url id="getChannelCntTree_url" action="BINOLMOPMC03_getChannelCntTree"/>
    <a id="getChannelCntTreeUrl" href="${getChannelCntTree_url}"></a>
    <s:url id="saveCounterTree_url" action="BINOLMOPMC03_saveCounterTree"/>
    <a id="saveCounterTreeUrl" href="${saveCounterTree_url}"></a>
    <div id="select_default"><s:text name="global.page.select" /></div>
    <s:text name="PMC03_filename" id="filename"/>
    <s:url id="downloadUrl" value="%{filename}"/>
    <s:url id="import_url" value="/mo/BINOLMOPMC03_import"/>
</div>
<form id="treeForm" onsubmit="">
<s:hidden name="menuGrpID" value="%{#request.menuGrpID}"></s:hidden>
<s:hidden name="startDate" value="%{posMenuGrpInfo.startDate}"></s:hidden>
<s:hidden name="endDate" value="%{posMenuGrpInfo.endDate}"></s:hidden>
<s:hidden name="machineType" value="%{posMenuGrpInfo.machineType}"></s:hidden>
</form>
<div class="panel ui-corner-all">
<div id="div_main">
	<div class="panel-header">
	    <div class="clearfix">
	        <span class="breadcrumb left"> 
	            <span class="ui-icon icon-breadcrumb"></span>
	            <%-- 菜单配置管理 --%>
	            <s:text name="PMC03_breadcrumb"/>&gt;
	            <%-- 配置菜单 --%>
	            <s:text name="PMC03_title"/>
	        </span>
	    </div>
	</div>
    <div id="actionResultDisplay"></div>
	<div class="panel-content" >
            <div class="box">
                <div class="box-header">
		            <strong>
						<span class="ui-icon icon-ttl-section-info-edit"></span>
						<s:text name="global.page.title"/><%-- 基本信息 --%>
					</strong>
				</div>
                <div class="box-content clearfix">
                    <div class="left">
			        	<%-- 所属品牌 --%>
						<p class="clearfix">
			    			<span class="left">
								<label><s:text name="PMC03_brandName"/>：</label>
								<label><span><s:property value="posMenuGrpInfo.brandName"/></span></label>
							</span>
						</p>
						<p class="clearfix">
                            <span class="left">
                                <label><s:text name="PMC03_validDate"/>：</label>
                                <!-- 加入生效起止日期后只有两种情况（1：起止日期都空，2：起止日期都不空） -->
                                <s:if test='null != posMenuGrpInfo.startDate && !"".equals(posMenuGrpInfo.startDate) && null != posMenuGrpInfo.endDate && !"".equals(posMenuGrpInfo.endDate)'>
	                                <span><s:property value="posMenuGrpInfo.startDate"/></span>
	                                ~
	                                <span><s:property value="posMenuGrpInfo.endDate"/></span>
                                </s:if>
                            </span>
                        </p>
						<%-- 菜单组名称 --%>
						<p class="clearfix">
						    <span class="left">
								<label><s:text name="PMC03_posMenuGrpName"/>：</label>
								<label><span><s:property value="posMenuGrpInfo.posMenuGrpName"/></span></label>
							</span>
						</p>
						<%-- 机器类型--%>
						<p class="clearfix">
						    <span class="left">
								<label><s:text name="PMC03_machineType"/>：</label>
								<label><span><s:property value='#application.CodeTable.getVal("1284",posMenuGrpInfo.machineType)'/></span></label>
							</span>
						</p>
					</div>
                </div>
            </div>
            
            <%--========================== 柜台excel导入START =============================--%>
            <div class="toolbar clearfix">
		        <strong>
		            <span class="left">
		                <span class="ui-icon icon-ttl-section-info"></span>
		                <s:text name="PMC03_importExcel_title"/>
		            </span>
		        </strong>
		    </div>
		    <%-- ================== 信息提示区 START ======================= --%>
			<div id="errorMessage"></div>
			<%-- ================== 信息提示区   END  ======================= --%>
		    <div>
		        <span class="highlight"><s:text name="PMC03_snow"/></span>
		        <s:text name="PMC03_notice"/>
		        <a href="${downloadUrl}"><s:text name="PMC03_download"/></a>
		    </div>
		    <div>
		        <table class="detail" cellpadding="0" cellspacing="0">
		            <tr>
		                <td>
		                     <span style="margin-left:10px; display: inline;" class="left hide"> 
							    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
							    <input type="button" value="<s:text name="global.page.browse"/>"/>
							    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="BINOLMOPMC03.deleteActionMsg();pathExcel.value=this.value;return false;" /> 
							    <input type="button" value="<s:text name="PMC03_importExcel_btn"/>" onclick="BINOLMOPMC03.ajaxFileUpload('${import_url}');return false;" id="upload" /> 
							    <img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display: none;">
							</span>
		                    
		                </td>
		            </tr>
		        </table>
		    </div>
		    <%-- 错误信息 (请选择上传文件)--%>
		    <div id="errMsg">
		    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/><%-- 请选择上传文件。 --%>
		    </div>
		    <hr class="space" />
            <%--========================== 柜台excel导入END =============================--%>
            
            <div class="clearfix section-header1"> 
                <strong>
					<span class="ui-icon icon-flag"></span>
					<s:text name="PMC03_choicePosMenu"/>
				</strong>
				<span>
					<select name="selMode" id="selMode" onchange="BINOLMOPMC03.ajaxGetNodes();return false;">
						<option value="1"><s:text name="global.page.selMode1"/></option>
						<option value="2"><s:text name="global.page.selMode2"/></option>
						<option value="3"><s:text name="global.page.selMode3"/></option>
					</select>
				</span>
				<span class="right">
					<input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 200px;" name="locationPosition" autocomplete="off">
					<a class="search" onclick="BINOLMOPMC03.locationPosition(this);return false;">
						<span class="ui-icon icon-position"></span>
						<span class="button-text"><s:text name="PMC03_locationPosition"></s:text></span>
					</a>
				</span>
            </div>
            <hr class="space" />
            <div class="treebox" style="overflow:auto;">
            	<div id="channelRegionDiv" class="hide" style="padding: 5px 5px 5px 8px;border-bottom: 1px dashed #D6D6D6;">
				<strong><s:text name="global.page.channelRegion"/></strong>
				<span></span>
				</div>
			    <div class="jquery_tree treebox tree" id="treeDemo" style="overflow: auto;background:#FCFCFC;">
			    </div>
			</div>
            <hr class="space" />
            <div class="center">
			    <button class="save" type="button" onclick="BINOLMOPMC03.save();">
					<span class="ui-icon icon-confirm"></span>
					<span class="button-text"><s:text name="PMC03_publishMenuCounter"></s:text></span>
				</button>
				<button class="close" type="button" onclick="window.close();return false;">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><s:text name="global.page.close"/></span>
			    </button>
		    </div>
        </div>
<!-- 进度条 -->
<div id="dataTable_processing" class="dataTables_processing"  style="text-algin:left;"></div>
</div>
</div>
</s:i18n>