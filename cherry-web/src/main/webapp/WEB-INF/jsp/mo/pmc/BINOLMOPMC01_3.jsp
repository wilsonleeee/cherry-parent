<%-- 编辑菜单组 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	//节日
	var holidays = '${holidays }';
	$('#startDate').cherryDate({
	    holidayObj: holidays,
	    beforeShow: function(input){
	        var value = $('#endDate').val();
	        return [value,'maxDate'];
	    }
	});
	$('#endDate').cherryDate({
	    holidayObj: holidays,
	    beforeShow: function(input){
	        var value = $('#startDate').val();
	        return [value,'minDate'];
	    }
	});
	$(function(){
	    // 表单验证初期化
		cherryValidate({
			formId: 'operateForm',	
			rules: {
				startDate: {required:true,dateValid:true},    // 开始日期
				endDate: {required:true,dateValid:true},   // 结束日期
				menuGrpName: {required : true,maxlength : 50}
			}	
		});
	});
</script>
<div id="actionResultDisplay"></div>
<s:if test="menuGrpInfo != null">
    <form id="operateForm">
        <s:i18n name="i18n.mo.BINOLMOPMC01">
        	<s:hidden name="menuGrpID"></s:hidden>
            <s:hidden name="modifyTime" value="%{menuGrpInfo.updateTime}"></s:hidden>
            <s:hidden name="modifyCount" value="%{menuGrpInfo.modifyCount}"></s:hidden>
            <s:hidden name="machineType" value="%{menuGrpInfo.machineType}"></s:hidden>
            <div>
		  		<table style="margin:auto; width:100%;" class="detail">
		  		<tr>
			  		<th><s:text name="PMC01_menuGrpName"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  		<td><span><s:textfield name="menuGrpName" cssClass="text" maxlength="50" value="%{menuGrpInfo.menuGrpName}"/></span></td>
		  		</tr>
		  		<tr>
	      			<!-- 生效日期-->
	      			<th><s:text name="PMC01_validDate"></s:text><span class="highlight">*</span></th>
	      			<td class="date">
		      			<span><s:textfield id="startDate" name="startDate" cssClass="date" cssStyle="width:80px;" value="%{menuGrpInfo.startDate}"/></span>
			            <span>&nbsp;-&nbsp;</span> 
			            <span><s:textfield id="endDate" name="endDate" cssClass="date" cssStyle="width:80px;" value="%{menuGrpInfo.endDate}"/></span>
	    			</td>
	      		</tr>
	      		<tr>
			  		<th><s:text name="PMC01_machineType"/></th>
			  		<td><span><s:select name="machineType" 
	                      list='#application.CodeTable.getCodes("1284")' 
	                      listKey="CodeKey" listValue="Value" headerKey="" headerValue="" value="%{menuGrpInfo.machineType}" cssStyle="width:150px;" disabled="true"/></span></td>
		  		</tr>
		  		</table>
  			</div>
        </s:i18n>
    </form>
</s:if>
<s:else>
    <s:i18n name="i18n.message.message">
        <p class="message">
            <span><s:text name="EPL00001" /></span>
        </p>
    </s:i18n>
</s:else>