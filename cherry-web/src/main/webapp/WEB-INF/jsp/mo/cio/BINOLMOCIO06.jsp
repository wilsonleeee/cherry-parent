<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO06.js"></script>
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript">

if (window.opener) {
	       window.opener.lockParentWindow();
	}
	$().ready(function(){
		var issuedType = '${issuedType}';
		if(issuedType == "0"){
			$("#issued0").prop("checked",true);
		}else{
			$("#issued1").prop("checked",true);
		}
		<%--
		$("input[name='issued']").each(function(){
			$(this).click(function(){
					changeRadio($(this).val());
				});
		});
		--%>
	});
	
</script>
<style>
	.treebox_left{
		background:none repeat scroll 0 0 #FFFFFF;
		border:1px solid #FAD163;
		height:550px;
		border-color:#D6D6D6 #D6D6D6 
	}
	.treebox_right{
		background:none repeat scroll 0 0 #FFFFFF;
		border:1px solid #FAD163;
		height:520px;
		border-color:#D6D6D6 #D6D6D6 
	}
	p{
		margin:0 0 0.2em;
	}
	.section-header1 {
    border-bottom: 1px solid #E5E5E5;
    height: 24px;
    line-height: 24px;
    padding: 0 10px 5px 5px;
}
</style>
<s:i18n name="i18n.mo.BINOLMOCIO06">
<s:url id="changeRadio_url" value="/mo/BINOLMOCIO06_changeRadio"></s:url>
<s:url id="issuedPaper_url" value="/mo/BINOLMOCIO06_issuedPaper"></s:url>
<s:url id="getTreeNodes_url" value="/mo/BINOLMOCIO06_getTreeNodes"></s:url>
<div class="hide">
<s:text name="CIO06_filename" id="filename"/>
<s:url id="downloadUrl" value="%{filename}"/>
<s:url id="import_url" value="/mo/BINOLMOCIO06_import"/>
</div>
<div class="panel ui-corner-all">
<div id="div_main">
	<div class="main clearfix">
		<div class="panel-header">
     	<%-- ###问卷查询 --%>
       		<div class="clearfix"> 
       			<span class="breadcrumb left"> 
       				<span class="ui-icon icon-breadcrumb"></span>
       				<s:text name="CIO06_manage"/>&gt; <s:text name="CIO06_title"/> 
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
            			<div class="left" style="width:45%">
      	  					<p class="clearfix">
	      						<span class="left">
	      							<label><s:text name="CIO06_brandName"/>：</label>
	      							<span id="paperName"><s:property value="%{paperMap.brandName}"/></span>
	      						</span>
      	 					</p>
      	  					<p class="clearfix">
      	  						<span class="left">
      	  							<label><s:text name="CIO06_paperType"/>：</label>
      	  							<span><s:property value='#application.CodeTable.getVal("1107", paperMap.paperType)'/></span>
           						</span>
          					</p>
       					</div>
       					<div class="left" style="width:55%">
       						<p class="clearfix">
       							<span class="left">
	      							<label><s:text name="CIO06_maxPoint"/>：</label>
	      							<span id="maxPoint"><s:property value="%{paperMap.maxPoint}"/></span>
	      						</span>
	      					</p>
	      					<p class="date">
    							<label><s:text name="CIO06_publishDate"></s:text>：</label>
    							<%
    								SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
    								String nowDate = sf.format(new Date());
    							%>
    							<span><%out.print(nowDate);%></span>
    						</p>
       					</div>
           					<p class="clearfix">
          						<span class="left">
          						<%-- 问卷名称 --%>
            						<label style="text-align:left"><s:text name="CIO06_paperName"/>：</label>
            						<span><s:property value="%{paperMap.paperName}"/></span>
           						</span>
        					</p>
            		</div>
            		<%--下发类型 --%>
            		<p class="clearfix">
            			<input id="issued1" name="issued" type="radio" value="1"/><s:text name="CIO06_allow"></s:text>
           				<input id="issued0" name="issued" type="radio" value="0"/><s:text name="CIO06_forbidden"></s:text>
          			</p>
		       </div>
		                   
            <%--========================== 柜台excel导入START =============================--%>
            <div class="toolbar clearfix">
		        <strong>
		            <span class="left">
		                <span class="ui-icon icon-ttl-section-info"></span>
		                <s:text name="CIO06_importExcel_title"/>
		            </span>
		        </strong>
		    </div>
		    <%-- ================== 信息提示区 START ======================= --%>
			<div id="errorMessage"></div>
			<%-- ================== 信息提示区   END  ======================= --%>
		    <div>
		        <span class="highlight"><s:text name="CIO06_snow"/></span>
		        <s:text name="CIO06_notice"/>
		        <a href="${downloadUrl}"><s:text name="CIO06_download"/></a>
		    </div>
		    <div>
		        <table class="detail" cellpadding="0" cellspacing="0">
		            <tr>
		                <td>
		                     <span style="margin-left:10px; display: inline;" class="left hide"> 
							    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
							    <input type="button" value="<s:text name="global.page.browse"/>"/>
							    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="BINOLMOCIO06.deleteActionMsg();pathExcel.value=this.value;return false;" /> 
							    <input type="button" value="<s:text name="CIO06_importExcel_btn"/>" onclick="BINOLMOCIO06.ajaxFileUpload('${import_url}');return false;" id="upload" /> 
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
       					<s:text name="CIO06_choiceCounter"/>
       				</strong>
       				<span class="right">
						<input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 200px;" name="locationPosition" autocomplete="off">
						<a class="search" onclick="locationCutPosition(this);return false;">
							<span class="ui-icon icon-position"></span>
							<span class="button-text"><s:text name="CIO06_locationPosition"></s:text></span>
						</a>
					</span>
       			</div>
       			<hr class="space" />
		       <div class="jquery_tree">
    				<div class="jquery_tree treebox_left tree" id = "treeDemo" style="overflow: auto;background:#FCFCFC;">
     			 
      				</div>
    			</div>
	       </div>
       <hr class="space" />
    </div>
	<div id="editButton" class="center clearfix">
       			<button class="save" id="savePaper" onclick="issuedPaper();return false;">
            		<span class="ui-icon icon-save"></span>
            		<span class="button-text"><s:text name="global.page.save"/></span>
             	</button>
	            <button id="close" class="close" type="button"  onclick="window.close();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
            <hr class="space" />
</div>
</div>
<span id="changeRadio" style="display:none">${changeRadio_url}</span>
<span id="issuedPaper" style="display:none">${issuedPaper_url}</span>
<span id="getTreeNodes" style="display:none">${getTreeNodes_url}</span>
<s:form id="form">
	<s:hidden name="isIssued" value="%{paperMap.isIssued}"></s:hidden>
	<s:hidden name="maxPoint" value="%{paperMap.maxPoint}"></s:hidden>
	<s:hidden name="paperName" value="%{paperMap.paperName}"></s:hidden>
	<s:hidden name="organizationInfoId" value="%{paperMap.organizationInfoId}"></s:hidden>
	<s:hidden name="brandInfoId" value="%{paperMap.brandInfoId}"></s:hidden>
	<s:hidden name="paperId" value="%{paperMap.paperId}"></s:hidden>
	<s:hidden name="paperType" value="%{paperMap.paperType}"></s:hidden>
</s:form>
</s:i18n>