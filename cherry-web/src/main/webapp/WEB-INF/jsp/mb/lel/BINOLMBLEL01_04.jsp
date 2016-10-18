<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<s:i18n name="i18n.mb.BINOLMBLEL01">
<div id="validDateDigMain" >
<div id="actionResultDiv"></div>
<div id="actionResultDisplay"></div>
<div id="validDateDig" class="hide">
<div class="box2-active">
       <%--  div class="box2 box2-content ui-widget">
            <p class="gray"><s:text name="lel01.memberTip" /></p>
             <%--<p class="gray" style="margin:0px;"><s:text name="lel01.useTip" /></p>
        div--%>
    </div>
    <div class="box2 box2-content ui-widget">
    	<%--等级类别--%>
    	<p><span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><s:text name="lel01.levelKbnTxt" /></span>
    	<span>
    	<input type="radio" class="radio" value="0" name="levelKbn" id="levelKbn0" checked="checked" style="margin-right:10px;"/><label for="levelKbn0"><s:text name="lel01.offiLevelTxt" /></label>
    	<input type="radio" class="radio" value="1" name="levelKbn" id="levelKbn1" style="margin-right:10px;"/><label for="levelKbn1"><s:text name="lel01.unoffiLevelTxt" /></label>
    	</span>
    	</p>
    	<%--开始时间--%>
        <p><span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><s:text name="lel01.startTimeTxt" /></span>
        <input type="radio" class="radio" value="0" name="startTimeKbn" id="startTimeKbn0" checked="checked" style="margin-right:10px;"/><label for="startTimeKbn0"><s:text name="lel01.startTimeInfo" /></label>
    	<input type="radio" class="radio" value="1" name="startTimeKbn" id="startTimeKbn1" style="margin-right:10px;"/><label for="startTimeKbn1"><s:text name="lel01.offiTimeTxt" /></label>
        
        </p>
        <%--结束时间--%>
        <p><span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><s:text name="lel01.endTimeTxt" /></span></p>
        <p style="text-indent:24px;"><input type="radio" class="radio" value="0" name="normalYear" id="normalYear0" checked="checked" style="margin-right:10px;">
       <span> <input class="text" name="memberDate0" id="memberDate0" type="text" style="width:30px; "></span><s:text name="lel01.Innormal" /><span class="gray" ><s:text name="lel01.endDate" /> </span>
       <br/>
         <span style="margin-left:30px;"> 	
         <input type="checkbox" name="kpLevel" id='kpLevel1' value='1' onclick="BINOLMBLEL01.showHideSpan(this,'#kpLevelEndSpan')"/><span>保级设置不同有效期</span>
    	<span id ="kpLevelEndSpan" class="hide">延期 <input class="text" name="kpLevelEnd" id="kpLevelEnd" type="text" style="width:30px; ">个自然年</span></span>
       </p>
        <p style="text-indent:24px;">
        <input type="radio" value="1" class="radio" name="normalYear" id="normalYear1" style="margin-right:10px;">
        <span><input class="text" name="memberDate1" id="memberDate1" type="text" style="width:30px;"></span>
        	<s:text name="lel01.yearAft" />&nbsp;&nbsp;
        	<s:select name="endKbn1" id="endKbn1" list='#application.CodeTable.getCodes("1285")' listKey="CodeKey" listValue="Value"/>
        </p>
        <p style="text-indent:24px;">
        <input type="radio" value="2" class="radio" name="normalYear" id="normalYear2" style="margin-right:10px;">
        <span><input class="text" name="memberDate2" id="memberDate2" type="text" style="width:30px;"></span><s:text name="lel01.monthAft" />
       	<s:select name="endKbn2" id="endKbn2" list='#application.CodeTable.getCodes("1285")' listKey="CodeKey" listValue="Value"/>
        </p>
        <p style="text-indent:24px;">
        <input name="normalYear" id="normalYear3" type="radio" class="radio" value="3" style="margin-right:10px;"><s:text name="lel01.forever" /></p>
        <p><span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><s:text name="lel01.recalcLevelTxt" /></span><span><s:text name="lel01.keepLevelDespTxt" /></span></p>
       <p style="text-indent:10px;">
       <input type="radio" class="radio" value="0" name="keepLevelKbn" id="keepLevelKbn0" checked="checked" /><label for="keepLevelKbn0"><s:text name="lel01.noKeepLevelTxt" /></label>
    	<input type="radio" class="radio" value="1" name="keepLevelKbn" id="keepLevelKbn1" /><label for="keepLevelKbn1"><s:text name="lel01.allKeepLevelTxt" /></label>
    	<input type="radio" class="radio" value="2" name="keepLevelKbn" id="keepLevelKbn2" /><label for="keepLevelKbn2"><s:text name="lel01.someKeepLevelTxt" /></label>
    	<s:text name="lel01.cardStartTxt" /><input type="text" value="" name="cardStart" class="number" id="cardStart"/><br/>
    	<label class="gray" style="margin-left:320px;"><s:text name="lel01.multTextTxt" /></label>
    	</p>
    	<p><span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><s:text name="lel01.chlTxt" /></span>
    	<span>
    	<s:iterator value='#application.CodeTable.getCodes("1301")' status="channel">
    		<input type="checkbox" name="chlCd" id='chlCd_<s:property value="#channel.index"/>' value='<s:property value="CodeKey"/>'/>
    		<label for="chlCd_<s:property value="#channel.index"/>" style="cursor:pointer;"><span><s:property value="Value"/></span></label>
    	</s:iterator>
    	</span>
    	</p>
    </div>
    <div class="box4 box4-content ui-widget">
        <p><input name="moreDate" id="moreDate" type="checkbox" value="1" checked="checked" style="margin-right:10px;"><s:text name="lel01.passDate" /> </p>
        <%-- <p class="bg_yew gray" style="padding-left:24px; line-height:23px; margin-bottom:0px;"><s:text name="lel01.example" /><br/>
       <s:text name="lel01.choice" /> <br/>
        <s:text name="lel01.notChoice" /><br/>
       <s:text name="lel01.diff" /> </p>--%>
    </div>
</div>
<div class="hide">
	<span id="setMsgTitle"><s:text name="lel01.memberDate" /></span>
	<span id="dialogConfirm"><s:text name="lel01.sure" /></span>
	<span id="dialogCancel"><s:text name="lel01.cancel" /></span>
	<span id="text0"><s:text name="text0"/></span>
	<span id="text1"><s:text name="text1"/></span>
	<span id="text2"><s:text name="text2"/></span>
	<span id="text3"><s:text name="text3"/></span>
</div>
<div class="hide" id="dialogInit"></div>
</div>
</s:i18n>