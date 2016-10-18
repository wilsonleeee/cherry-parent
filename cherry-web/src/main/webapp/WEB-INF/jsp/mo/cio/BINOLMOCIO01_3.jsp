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
<s:if test="counterMessage != null">
    <form id="msgForm">
        <s:i18n name="i18n.mo.BINOLMOCIO01">
            <s:hidden name="counterMessageId"></s:hidden>
            <s:hidden name="modifyTime" value="%{counterMessage.updateTime}"></s:hidden>
            <s:hidden name="modifyCount" value="%{counterMessage.modifyCount}"></s:hidden>
            <p class="clearfix">
                <label><s:text name="CIO01_messageTitle" /></label>
                <s:textfield name="messageTitle" cssClass="text" maxlength="10" value="%{counterMessage.messageTitle}"/>
                <span class="highlight"><s:text name="global.page.required"></s:text></span>
            </p>
            <p class="date">
	            <label><s:text name="CIO01_validDate" /></label>
	            <span><s:textfield id="startValidDate" name="startValidDate" cssClass="date" cssStyle="width:80px;" value="%{counterMessage.startValidDate}"/></span>
	            - 
	            <span><s:textfield id="endValidDate" name="endValidDate" cssClass="date" cssStyle="width:80px;" value="%{counterMessage.endValidDate}"/></span>
	            <span class="highlight"><s:text name="global.page.required"></s:text></span>
	        </p>
            <p class="clearfix">
                <label><s:text name="CIO01_messageBody" /></label>
                <s:textarea id="messageBody" name="messageBody" onpropertychange="return isMaxLen(this);" maxlength="144" value="%{counterMessage.messageBody}"></s:textarea>
            </p>
            <span style="margin-left:56px;">
               <span class="highlight" class="left">※</span>
               <span class="gray"><s:text name="global.page.PopMessageTitle"/></span>
	        </span>
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