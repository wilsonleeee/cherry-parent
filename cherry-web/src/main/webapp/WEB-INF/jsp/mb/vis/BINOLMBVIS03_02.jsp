<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBVIS03">
<div class="main container clearfix">
<div class="panel ui-corner-all">
	  <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="mbvis03_visitMng"/> &gt; <s:text name="mbvis03_addVisitType"/>
	       </span>
	    </div>
	  </div>
	  <div id="actionResultDisplay"></div>
	  <div class="panel-content clearfix">
        <cherry:form id="saveForm" class="inline" csrftoken="false">
			<div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="global.page.title"/>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
	                <tr>
				      <th>
				      	<s:text name="mbvis03_visitTypeCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span>
				      </th>
				      <td>
				      	<span><s:textfield name="visitTypeCode" cssClass="text" maxlength="20"></s:textfield></span>
				      </td>
				      <th><s:text name="mbvis03_visitTypeName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
					  <td><span><s:textfield name="visitTypeName" cssClass="text" maxlength="20"></s:textfield></span></td>
				    </tr>
                </table>
			  </div>
			</div>
		</cherry:form>	
			
			<div class="section" id="counterHeadDiv">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="mbvis03_setVisitObj"/>
              	</strong>
              </div>
              <div class="section-content">
				<div id="visitObjJson">
				<s:action name="BINOLCM33_init" namespace="/common" executeResult="true"></s:action>
				</div>
			  </div>
			</div>
            
            <div class="center clearfix">
       			<button class="save" type="button" id="saveButton">
            		<span class="ui-icon icon-save"></span>
            		<span class="button-text"><s:text name="global.page.save"/></span>
             	</button>
	            <button class="close" type="button" onclick="window.close();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
      </div>
</div>
</div>

<div class="hide">
<s:a action="BINOLMBVIS03_add" id="addUrl"></s:a>
</div>
</s:i18n>

<script type="text/javascript">
$(function(){
	cherryValidate({
		formId: 'saveForm',
		rules: {
			visitTypeCode: {required: true, maxlength: 20},
			visitTypeName: {required: true, maxlength: 20}
		}
	});
	
	$("#saveButton").click(function(){
		if(!$('#saveForm').valid()) {
			return false;
		}
		save();
		return false;
	});
	
	function save() {
		var url = $("#addUrl").attr("href");
		var params = $("#saveForm").serialize();
		var searchParam = $("#visitObjJson").find(":input").serializeForm2Json(false);
		if(searchParam) {
			params = params + "&visitObjJson=" + searchParam;
		}
		var callback = function(msg) {
			if($('#actionResultBody').length > 0) {
				if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
			}
		};
		cherryAjaxRequest({
			url: url,
			param : params,
			callback: callback
		});
		
		
	}
});

</script>
