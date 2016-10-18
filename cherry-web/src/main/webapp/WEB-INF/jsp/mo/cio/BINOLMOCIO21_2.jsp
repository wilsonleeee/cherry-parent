<%-- 新增订货消息 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	//节日
	var holidays = '${holidays }';
	$('#startValidDate').cherryDate({
	    holidayObj: holidays,
	    beforeShow: function(input){
	        var value = $('#endValidDate').val();
	        return [value,'maxDate'];
	    }
	});
	$('#endValidDate').cherryDate({
	    holidayObj: holidays,
	    beforeShow: function(input){
	        var value = $('#startValidDate').val();
	        return [value,'minDate'];
	    }
	});
$(function(){
    // 表单验证初期化
    cherryValidate({
        formId: 'msgForm',
        rules: {
            brandInfoId: {required: true},
            messageTitle: {required: true,maxlength: 10},
            messageBody: {required: false,byteLengthValid: [200]},//字节数最大为238（一个中文两个字节）
            startValidDate: {required:true,dateValid:true},    // 开始日期
            endValidDate: {required:true,dateValid:true}   // 结束日期
        }
    });
});
</script>
<div id="actionResultDisplay"></div>
<form id="msgForm">
    <s:i18n name="i18n.mo.BINOLMOCIO21">
	    <s:text name="global.page.select" id="select_default"/>
        <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
	        <p class="clearfix">
	            <label><s:text name="CIO21_brandInfo"></s:text></label>
	            <s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId" ></s:select>
	            <span class="highlight"><s:text name="global.page.required"></s:text></span>
	        </p>
        </s:if>
        <p class="clearfix">
            <label><s:text name="CIO21_messageTitle" /></label>
            <s:textfield name="messageTitle" cssClass="text" maxlength="10"/>
            <span class="highlight"><s:text name="global.page.required"></s:text></span>
        </p>
        <p class="date">
            <label><s:text name="CIO21_validDate" /></label>
            <span><s:textfield id="startValidDate" name="startValidDate" cssClass="date" cssStyle="width:80px;"/></span>
            - 
            <span><s:textfield id="endValidDate" name="endValidDate" cssClass="date" cssStyle="width:80px;"/></span>
            <span class="highlight"><s:text name="global.page.required"></s:text></span>
        </p>
        <p class="clearfix">
            <label><s:text name="CIO21_messageType" /></label>
            <s:select id="messageType" name="messageType" list="#application.CodeTable.getCodes('1413')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#select_default}"/>

        	<span><a href="javascript:void(0);" onclick="popUploadFile(this);return false;"><s:text name="CIO21_pointUploadFile"/></a></span>
        </p>
        <p class="clearfix">
            <label><s:text name="CIO21_messageBody" /></label>
            <s:textarea id="messageBody" name="messageBody" onpropertychange="return isMaxLen(this);" maxlength="3000"></s:textarea>
        </p>
        	<span style="margin-left:56px;">
           		<span class="highlight" class="left">※</span>
           		<span class="gray"><s:text name="CIO21_PopMessageTitle"/></span>
	    	</span>
	 <div id="baseInfo">
	</div>
    </s:i18n>
</form>