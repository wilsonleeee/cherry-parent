<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 密码安全配置信息编辑保存URL --%>
<s:url id="editSave_url" action="BINOLPLUPM06_save"/>
<s:i18n name="i18n.pl.BINOLPLUPM06">
<div id="saveDialog" class="hide">
	<div id="saveActionResult"></div>
	<form id="saveForm" class="inline">
    <table class="detail" cellpadding="0" cellspacing="0" id="saveTable">
    <tr>
    <%-- 所属品牌 --%>
       <th><s:text name="UPM06_brandName"/></th>
       <td width="85%">
       		<%-- 品牌名称 --%>
          	<s:property value="pwConfInfo.brandName"/>
          	<%-- 密码配置ID --%>
			<input type="hidden" name="pwConfId" value="${pwConfInfo.pwConfId}"/>
			<%-- 更新日时 --%>
			<input type="hidden" name="pwUpdateTime" value="${pwConfInfo.pwUpdateTime}"/>
			<%-- 更新次数 --%>
			<input type="hidden" name="modifyCount" value="${pwConfInfo.modifyCount}"/>
       	</td>
      </tr>
     <tr>
     <%-- 密码有效期 --%>
       <th>
       	<s:text name="UPM06_duration"/>
       	<span class="highlight"><s:text name="global.page.required"/></span>
       </th>
       <td width="85%">
       		<span><s:textfield name="duration" cssClass="number" maxlength="9" value="%{pwConfInfo.duration}"/>&nbsp;
       			<%-- 天 --%>
       			<s:text name="UPM06_day"/>
       		</span>
       	</td>
      </tr>
      <tr>
      <%-- 密码修改提醒 --%>
       <th><s:text name="UPM06_remindAhead"/></th>
       <td width="85%">
       		<span><s:textfield name="remindAhead" cssClass="number" maxlength="2" value="%{pwConfInfo.remindAhead}"/>&nbsp;
       			<%-- 天 --%>
       			<s:text name="UPM06_day"/>
       		</span>
       		<span style="margin-left:20px;"><span class="highlight"><s:text name="UPM06_snow"/></span>
       			<span class="gray"><s:text name="UPM06_remindAheadPrompt"/></span>
       		</span>
       	</td>
      </tr>
      <tr>
       <%-- 密码重复间隔 --%>
       <th><s:text name="UPM06_repetitionInterval"/></th>
       <td width="85%">
       		<span><s:textfield name="repetitionInterval" cssClass="number" maxlength="2" value="%{pwConfInfo.repetitionInterval}"/>&nbsp;
       			<%-- 次 --%>
       			<s:text name="UPM06_times"/>
       		</span>
       		<span style="margin-left:20px;"><span class="highlight"><s:text name="UPM06_snow"/></span>
       			<span class="gray"><s:text name="UPM06_repetInterPrompt"/></span>
       		</span>
       	</td>
      </tr>
      <tr>
      <%-- 密码复杂度 --%>
       <th><s:text name="UPM06_complexity"/></th>
       <td width="85%">
       <span>
       		<%-- 允许使用 --%>
       		<s:text name="UPM06_include"/>
       		<input type="checkbox" id="hasAlpha" name="hasAlpha" value="1" <s:if test='pwConfInfo.hasAlpha == "1"'>checked = "checked"</s:if>/> 
       		<%-- 英文 --%>
       		<s:text name="UPM06_hasAlpha"/>
       		<input type="checkbox" id="hasNumeric" name="hasNumeric" value="1" <s:if test='pwConfInfo.hasNumeric == "1"'>checked = "checked"</s:if>/> 
       		<%-- 数字 --%>
       		<s:text name="UPM06_hasNumeric"/>
       		<input type="checkbox" id="hasOtherChar" name="hasOtherChar" onclick="doOtherChar(this);" value="1" <s:if test='pwConfInfo.hasOtherChar == "1"'>checked = "checked"</s:if>/> 
       		<%-- 其他字符 --%>
       		<s:text name="UPM06_hasOtherChar"/>
       		<input type="text" id="otherChar" name="otherChar" class="number" style="width:120px;" disabled="disabled" value='<s:property value="pwConfInfo.otherChar"/>'/>
       		 <%--<span><s:textfield name="complexity" cssClass="text" maxlength="15" value="%{pwConfInfo.complexity}"/></span>
       		<span style="margin-left:20px;"><span class="highlight"><s:text name="UPM06_snow"/></span>
       			<s:text name="UPM06_complexityPrompt"/>
       		</span>--%>
       		<script type="text/javascript">
       		if ($("#hasOtherChar").is(":checked")) {
       			$("#otherCharText").show();
       			$("#otherChar").removeAttr("disabled");
       		} 
       		</script>
       		<span id="errMsg" style="float:none;"><input type="hidden" name="showMsg" disabled="disabled"/></span>
       	</span>
       	<br/>
       		<span style="margin-left:20px;" class="hide" id="otherCharText"><span class="highlight"><s:text name="UPM06_snow"/></span>
       			<span class="gray"><s:text name="UPM06_otherCharText"/></span>
       		</span>
       	</td>
      </tr>
      <tr>
      <%-- 密码长度 --%>
       <th>
       <s:text name="UPM06_pwLength"/>
       <span class="highlight"><s:text name="global.page.required"/></span>
       </th>
       <td width="85%">
       		<%-- 密码最小长度 --%>
       		<span> <s:text name="UPM06_minLength"/> <s:textfield name="pwLength" cssClass="number" maxlength="2" value="%{pwConfInfo.pwLength}"/> <s:text name="UPM06_num"/></span>
       		<span style="margin-left:20px;">
       			<%-- 密码最大长度 --%>
       			<s:text name="UPM06_maxLength"/> <s:textfield name="maxLength" cssClass="number" maxlength="2" value="%{pwConfInfo.maxLength}"/> <s:text name="UPM06_num"/>
       		</span>
       	</td>
      </tr>
      <tr>
      <%-- 重试次数 --%>
       <th><s:text name="UPM06_retryTimes"/></th>
       <td width="85%">
       		<span><s:textfield name="retryTimes" cssClass="number" maxlength="2" value="%{pwConfInfo.retryTimes}"/>&nbsp;
       			<%-- 次 --%>
       			<s:text name="UPM06_times"/>
       		</span>
       		<span style="margin-left:20px;"><span class="highlight"><s:text name="UPM06_snow"/></span>
       			<span class="gray"><s:text name="UPM06_retryTimesPrompt"/></span>
       		</span>
       	</td>
      </tr>
      <%--<tr>
      <%-- 账号锁定时间 
       <th><s:text name="UPM06_lockPeriod"/></th>
       <td width="85%">
       		<span><s:textfield name="lockPeriod" cssClass="number" maxlength="9" value="%{pwConfInfo.lockPeriod}"/>&nbsp;
       			<%-- 小时 
       			<s:text name="UPM06_hour"/>
       		</span>
       		<span style="margin-left:20px;"><span class="highlight"><s:text name="UPM06_snow"/></span>
       			<s:text name="UPM06_lockPeriodPrompt"/>
       		</span>
       	</td>
      </tr>--%>
      <tr>
       <%-- 访问跟踪 --%>
       <th><s:text name="UPM06_isTracable"/></th>
       <td width="85%">
       		<s:select name="isTracable" list='#application.CodeTable.getCodes("1045")' listKey="CodeKey" listValue="Value" value="%{pwConfInfo.isTracable}"/>
       	</td>
      </tr>
      <tr>
      <%-- 密码找回 --%>
       <th><s:text name="UPM06_isTrievable"/></th>
       <td width="85%">
       		<s:select name="isTrievable" list='#application.CodeTable.getCodes("1046")' listKey="CodeKey" listValue="Value" value="%{pwConfInfo.isTrievable}"/>
       	</td>
      </tr>
        <tr>
            <%-- 密码过期策略 --%>
            <th><s:text name="UPM06_overdueTactic"/></th>
            <td width="85%">
            <s:select name="overdueTactic" list='#application.CodeTable.getCodes("1274")' listKey="CodeKey" listValue="Value" value="%{pwConfInfo.overdueTactic}"/>
        </td>
      </tr>
     </table>
     </form>
     <hr class="space"/>
     <div class="center clearfix" id="pageButton">
       <button class="confirm" onclick="doSave('${editSave_url}')" id="confirmBtn">
       	<span class="ui-icon icon-confirm"></span>
       	<%-- 确定按钮 --%>
       	<span class="button-text"><s:text name="UPM06_confirmButton"/></span>
       </button>
	   <button class="close" onclick="doClose('#saveDialog')"><span class="ui-icon icon-close"></span>
         <%-- 关闭 --%>
         <span class="button-text"><s:text name="global.page.close"/></span>
        </button>
     </div>
</div>
</s:i18n>