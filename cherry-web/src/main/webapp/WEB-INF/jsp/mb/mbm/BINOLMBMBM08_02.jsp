<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM03">
<div id="pointTypeA">
				<div class="section-header">
                   <strong class="left active"> <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm03_pointsRepair"/></strong>
				</div>
                <div class="section-content box2-active">
                 <div class="box2-content clearfix">
                    <%-- 指定日期 --%>
	                <p><span class="ui-icon icon-arrow-crm" style="margin-top:6px;"></span><span class="bg_title"><s:text name="binolmbmbm03_pointDate"/><span class="highlight">*</span></span>
	                <span><s:textfield  cssStyle="width:103px;" name="dateTime" cssClass="date" value="" /></span>	
					</p>
					<div id="timeTemplate1">
					<%-- 指定时间--%>
					<p><span class="ui-icon icon-arrow-crm" style="margin-top:6px;"></span><span class="bg_title"><s:text name="binolmbmbm03_pointHour"/><span class="highlight">*</span></span>	
					<span><input type="text"  style="width:20px; " class="input number" name="startHH"  id="startHH"  maxlength="2" value="00"/>: <input type="text"  style="width:20px;" class="input number" name="startMM" id="startMM" value="00" maxlength="2"/>: <input type="text" style="width:20px;" class="input number" name="startSS"  id="startSS" value="00" maxlength="2"/></span>
					</p>
					</div>
					<%-- 总积分值 --%>
					<p><span class="ui-icon icon-arrow-crm" style="margin-top:6px;"></span><span class="bg_title"><s:text name="binolmbmbm03_pointTotal"/><span class="highlight">*</span></span>
					<span><input  style="width: 103px;" type="text" maxlength="15" value='<s:property value="memberInfoMap.totalPoint"/>' id="totalPoint" name="totalPoint" class="text ac_input" autocomplete="off"></span>
					<s:hidden name="oldtotalPoint"/>
					</p>
	                <div class="gray"><span class="highlight">※</span><s:text name="binolmbmbm03_reasonDetail1"/></div>
                 </div>
                </div>
     			</div>
                <div id="pointTypeB">
                <div class="section-header">
                 <strong class="left active"> <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm03_pointsMaintain"/></strong>
                 </div>
				<div class="section-content box2-active">
                    <div class="box2-content clearfix">
                    <%-- 指定日期 --%>
                    <p><span class="ui-icon icon-arrow-crm" style="margin-top:6px;"></span><span class="bg_title"><s:text name="binolmbmbm03_pointDate"/><span class="highlight">*</span></span>
	                <span><s:textfield  cssStyle="width:104px;" name="difdateTime" cssClass="date" value="" /></span>	
					</p>
					<div id="timeTemplate2">
					<%-- 指定时间--%>
					<p><span class="ui-icon icon-arrow-crm" style="margin-top:6px;"></span><span class="bg_title"><s:text name="binolmbmbm03_pointHour"/><span class="highlight">*</span></span>	
					<span><input type="text"  style="width:20px; " class="input number" name="startHour"  id="startHour"  maxlength="2" value="00"/>: <input type="text"  style="width:20px;" class="input number" name="startMinute" id="startMinute" value="00" maxlength="2"/>: <input type="text" style="width:20px;" class="input number" name="startSecond"  id="startSecond" value="00" maxlength="2"/></span>
					</p>
					</div>
					<%-- 积分差值 --%>
					<p><span class="ui-icon icon-arrow-crm" style="margin-top:6px;"></span><span class="bg_title"><s:text name="binolmbmbm03_pointDif"/><span class="highlight">*</span></span>
					<span>
					<span><input type="text" style="width: 103px;" value="" id="difPoint" name="difPoint" class="text ac_input" maxlength="9" autocomplete="off"></span>
                    </span>
				</p>
                <div class="gray"><span class="highlight">※</span><s:text name="binolmbmbm03_reasonDetail2"/><span class="red"><s:text name="binolmbmbm03_reasonDetail3"/></span></div>
                </div></div>
                </div>
</s:i18n>
<script type="text/javascript">
var holidays = '${holidays }';
$('#dateTime').cherryDate({
	
});
$('#difdateTime').cherryDate({
	
});
</script>