<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/WEB-INF/cherrytld/cherrytags.tld" %>
<s:i18n name="i18n.pt.BINOLPTRPS27">
<div class="hide">
	<s:url action="BINOLPTRPS27_setSchedules" id="setTaskUrl" />
	<a id="setParameter_url" href="${setTaskUrl }"></a>
	<input name="timeOnlyTitle" class="hide" id="timeOnlyTitle" value="<s:text name='PTRPS27_timeOnlyTitle' />" />
	<input name="currentText" class="hide" id="currentText" value="<s:text name='PTRPS27_currentText'/>"/>
	<input name="closeText" class="hide" id="closeText" value="<s:text name='PTRPS27_closeText'/>"/>
	<input name="timeText" class="hide" id="timeText" value=""/>
	<input name="hourText" class="hide" id="hourText" value=""/>
	<input name="minuteText" class="hide" id="minuteText" value=""/>
	<input id="parameter_type" type="hidden" value="ST">
</div>
<div id="actionResult"></div>
<div class="panel">
	<div class="panel-content">
		<p>
			<label><s:text name="PTRPS27_statisticTime2"/></label>
			<input type="text" value='<s:text name="PTRPS27_statisticTime3" />' class="text" style="width: 30px;border: none;" readonly="readonly" > 
			<input type="text" value='<s:property value='taskMap.taskCode' />' id="runTime" name="runTime" class="text" > 
			<span class="highlight"><s:text name="PTRPS27_warnInfo"></s:text> </span>
		</p>
	</div>
</div>
</s:i18n>
<script>
$(document).ready(function(){
	$("#runTime").timepicker({
		timeOnlyTitle: $("#timeOnlyTitle").val(),
		currentText: $("#currentText").val(),
		closeText: $("#closeText").val(),
		timeText: $("#timeText").val(),
		hourText: $("#hourText").val(),
		minuteText: $("#minuteText").val()
	});
	
});
</script>



