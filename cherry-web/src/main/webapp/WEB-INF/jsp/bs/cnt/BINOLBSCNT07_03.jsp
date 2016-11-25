<%-- 新增柜台消息 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	$(function(){
		// 表单验证初期化
		cherryValidate({
			formId: 'msgForm',
			rules: {
				pointChange: {required: true,maxlength: 10,validIntNumNotZero:true},
				comment: {byteLengthValid: [200]},//字节数最大为238（一个中文两个字节）
			}
		});
	});
</script>
<div id="actionResultDisplay"></div>
<form id="msgForm">
	<s:i18n name="i18n.bs.BINOLBSCNT07">
		<p class="clearfix">
			<label style="width: 100px;"><s:text name="CNT07.pointChange" /></label>
			<s:textfield name="pointChange" cssClass="text" maxlength="10" cssStyle="width: 285px"/>
			<span class="highlight"><s:text name="global.page.required"></s:text></span>
		</p>
		<p class="clearfix">
			<label style="width: 100px;"><s:text name="CNT07.comment" /></label>
			<s:textarea id="comment" name="comment" cssStyle="width: 280px;vertical-align: top;height: 60px;"  onpropertychange="return isMaxLen(this);" maxlength="144"></s:textarea>
		</p>
        <%--	<span style="margin-left:56px;">
           		<span class="highlight" class="left">※</span>
           		<span class="gray"><s:text name="global.page.PopMessageTitle"/></span>
	    	</span>--%>
		<p class="message" style="margin: 30px auto 0;">
			<span style="font-size: 14px;color: #666;">
				<s:text name="CNT07.pointChangeMessage" />
			</span>
		</p>
	</s:i18n>
</form>