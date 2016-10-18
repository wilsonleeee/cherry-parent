<%-- 编辑柜台消息 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript">
$(function(){
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
<s:if test="departmentMessage != null">
    <form id="msgForm">
        <s:i18n name="i18n.mo.BINOLMOCIO21">
            <s:hidden name="departmentMessageId" value="%{departmentMessage.departmentMessageId}"></s:hidden>
            <s:hidden name="modifyTime" value="%{departmentMessage.updateTime}"></s:hidden>
            <s:hidden name="modifyCount" value="%{departmentMessage.modifyCount}"></s:hidden>
            <p class="clearfix">
                <label><s:text name="CIO21_messageTitle" /></label>
                <s:textfield name="messageTitle" cssClass="text" maxlength="10" value="%{departmentMessage.messageTitle}"/>
                <span class="highlight"><s:text name="global.page.required"></s:text></span>
            </p>
            <p class="date">
	            <label><s:text name="CIO21_validDate" /></label>
	            <span><s:textfield id="startValidDate" name="startValidDate" cssClass="date" cssStyle="width:80px;" value="%{departmentMessage.startValidDate}"/></span>
	            - 
	            <span><s:textfield id="endValidDate" name="endValidDate" cssClass="date" cssStyle="width:80px;" value="%{departmentMessage.endValidDate}"/></span>
	            <span class="highlight"><s:text name="global.page.required"></s:text></span>
	        </p>
	        <p class="clearfix">
            <label><s:text name="CIO21_messageType" /></label>
           <%--  <s:select id="messageType" name="messageType" list="#application.CodeTable.getCodes('1413')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#select_default}"/> --%>
			<s:select list="#application.CodeTable.getCodes('1413')" id="messageType" name="messageType" listKey="CodeKey" listValue="Value"></s:select>        	
        	</p>
        	<p class="clearfix">
        	<span>
        	<s:text name='CIO21_uploadedFile'/>
        	<a title="<s:text name='CIO21_showDetail'/>" id="<s:property value="%{departmentMessage.filePathShow}"/>" name='<s:property value="%{departmentMessage.filePathShow}"/>' style="cursor:pointer;" href="<s:property value="%{departmentMessage.filePathShow}"/>">
        	<s:property value="%{departmentMessage.fileName}"/>
        	</a>
        	</span>
        	<span>&nbsp;&nbsp;<a href="javascript:void(0);" onclick="popUploadFile(this);return false;"><s:text name="CIO21_pointUploadFile"/></a></span>
        	</p>
            <p class="clearfix">
                <label><s:text name="CIO21_messageBody" /></label>
                <s:textarea id="messageBody" name="messageBody"  maxlength="3000" value="%{departmentMessage.messageBody}"></s:textarea>
            </p>
            <span style="margin-left:56px;">
               <span class="highlight" class="left">※</span>
               <span class="gray"><s:text name="CIO21_PopMessageTitle"/></span>
	        </span>
	       <div id="baseInfo">
	       <input type="hidden" value="<s:property value="%{departmentMessage.filePath}"/>" name="filePathEdit">
			<input type="hidden" value="<s:property value="%{departmentMessage.fileName}"/>" name="fileNameEdit">
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