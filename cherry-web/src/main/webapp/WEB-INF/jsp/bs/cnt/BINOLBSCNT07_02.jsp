<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
		$(function(){
			var currentDay = new Date();
			currentDay.setDate(currentDay.getDate());//

			$('#endSetDate').cherryDate({
				minDate : currentDay
			});
			// 表单验证初期化
			cherryValidate({
				formId: 'msgForm',
				rules: {
					endSetDate: {required:true,dateValid:true}   // 开始日期
				}
			});

	});
</script>
<div id="actionResultDisplay"></div>
<form id="msgForm">
	<s:i18n name="i18n.bs.BINOLBSCNT07">
		<div style='text-align: center;'>
			<p class="date" style="margin: 50px 0 0 0;">
				<label><s:text name="CNT07.endDate" /></label>
				<span><s:textfield id="endSetDate" name="endSetDate" cssClass="date" cssStyle="width:80px;" readonly="true"/></span>
			</p>
			<p class="message" style="margin: 50px auto 0;">
				<span style="font-size: 14px;color: #666;">
					<s:text name="CNT07.disableMessage" />
				</span>
			</p>
		</div>
	</s:i18n>
</form>