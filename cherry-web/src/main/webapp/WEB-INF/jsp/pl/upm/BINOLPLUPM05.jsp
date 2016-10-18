<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 密码安全配置信息添加保存URL --%>
<s:url id="addSave_url" action="BINOLPLUPM05_save"/>
<s:i18n name="i18n.pl.BINOLPLUPM05">
<div id="saveDialog" class="hide">
	<div id="saveActionResult"></div>
	<form id="saveForm" class="inline">
    <table class="detail" cellpadding="0" cellspacing="0" id="saveTable">
    <tr>
    <%-- 所属品牌 --%>
       <th><s:text name="UPM05_brandName"/></th>
       <td width="85%">
       		<%-- 品牌名称 --%>
          	<s:property value="brandName"/>
          	<%-- 所属品牌ID --%>
			<input type="hidden" name="brandInfoId" value="<s:property value='brandInfoId'/>"/>
       	</td>
      </tr>
     <tr>
     <%-- 密码有效期 --%>
       <th>
       	<s:text name="UPM05_duration"/>
       	<span class="highlight"><s:text name="global.page.required"/></span>
       </th>
       <td width="85%">
       		<span><s:textfield name="duration" cssClass="number" maxlength="9"/>&nbsp;
       			<%-- 天 --%>
       			<s:text name="UPM05_day"/>
       		</span>
       	</td>
      </tr>
      <tr>
      <%-- 密码修改提醒 --%>
       <th><s:text name="UPM05_remindAhead"/></th>
       <td width="85%">
       		<span><s:textfield name="remindAhead" cssClass="number" maxlength="2"/>&nbsp;
       			<%-- 天 --%>
       			<s:text name="UPM05_day"/>
       		</span>
       		<span style="margin-left:20px;"><span class="highlight"><s:text name="UPM05_snow"/></span>
       			<span class="gray"><s:text name="UPM05_remindAheadPrompt"/></span>
       		</span>
       	</td>
      </tr>
      <tr>
      <%-- 密码重复间隔 --%>
       <th><s:text name="UPM05_repetitionInterval"/></th>
       <td width="85%">
       		<span><s:textfield name="repetitionInterval" cssClass="number" maxlength="2"/>&nbsp;
       			<%-- 次 --%>
       			<s:text name="UPM05_times"/>
       		</span>
       		<span style="margin-left:20px;"><span class="highlight"><s:text name="UPM05_snow"/></span>
       			<span class="gray"><s:text name="UPM05_repetInterPrompt"/></span>
       		</span>
       	</td>
      </tr>
      <tr>
      <%-- 密码复杂度 --%>
       <th><s:text name="UPM05_complexity"/></th>
       <td width="85%">
       <span>
       		<%-- 允许使用 --%>
       		<s:text name="UPM05_include"/>
       		<input type="checkbox" id="hasAlpha" name="hasAlpha" value="1" checked = "checked"/> 
       		<%-- 英文 --%>
       		<s:text name="UPM05_hasAlpha"/>
       		<input type="checkbox" id="hasNumeric" name="hasNumeric" value="1" checked = "checked"/> 
       		<%-- 数字 --%>
       		<s:text name="UPM05_hasNumeric"/>
       		<input type="checkbox" id="hasOtherChar" name="hasOtherChar" onclick="doOtherChar(this);" value="1" /> 
       		<%-- 其他字符 --%>
       		<s:text name="UPM05_hasOtherChar"/>
       		<input type="text" id="otherChar" name="otherChar" class="number" disabled="disabled" value='<s:property value="pwConfInfo.otherChar"/>'/>
       		 <%--<span><s:textfield name="complexity" cssClass="text" maxlength="15" value="%{pwConfInfo.complexity}"/></span>
       		<span style="margin-left:20px;"><span class="highlight"><s:text name="UPM05_snow"/></span>
       			<s:text name="UPM05_complexityPrompt"/>
       		</span>--%>
       		<span id="errMsg" style="float:none;"><input type="hidden" name="showMsg" disabled="disabled"/></span>
       	</span>
       	<br/>
       		<span style="margin-left:20px;" class="hide" id="otherCharText"><span class="highlight"><s:text name="UPM05_snow"/></span>
       			<span class="gray"><s:text name="UPM05_otherCharText"/></span>
       		</span>
       	</td>
      </tr>
      <tr>
      <%-- 密码长度 --%>
       <th>
       <s:text name="UPM05_pwLength"/>
       <span class="highlight"><s:text name="global.page.required"/></span>
       </th>
       <td width="85%">
       		<%-- 密码最小长度 --%>
       		<span> <s:text name="UPM05_minLength"/> <s:textfield name="pwLength" cssClass="number" maxlength="2" /> <s:text name="UPM05_num"/></span>
       		<span style="margin-left:20px;">
       			<%-- 密码最大长度 --%>
       			<s:text name="UPM05_maxLength"/> <s:textfield name="maxLength" cssClass="number" maxlength="2" /> <s:text name="UPM05_num"/>
       		</span>
       	</td>
      </tr>
      <tr>
      <%-- 重试次数 --%>
       <th><s:text name="UPM05_retryTimes"/></th>
       <td width="85%">
       		<span><s:textfield name="retryTimes" cssClass="number" maxlength="2" value="3"/>&nbsp;
       			<%-- 次 --%>
       			<s:text name="UPM05_times"/>
       		</span>
       		<span style="margin-left:20px;"><span class="highlight"><s:text name="UPM05_snow"/></span>
       			<span class="gray"><s:text name="UPM05_retryTimesPrompt"/></span>
       		</span>
       	</td>
      </tr>
      <%--<tr>
      <%-- 账号锁定时间 
       <th><s:text name="UPM05_lockPeriod"/></th>
       <td width="85%">
       		<span><s:textfield name="lockPeriod" cssClass="number" maxlength="9"/>&nbsp;
       			<%-- 小时 
       			<s:text name="UPM05_hour"/>
       		</span>
       		<span style="margin-left:20px;"><span class="highlight"><s:text name="UPM05_snow"/></span>
       			<s:text name="UPM05_lockPeriodPrompt"/>
       		</span>
       	</td>
      </tr>--%>
      <tr>
      <%-- 访问跟踪 --%>
       <th><s:text name="UPM05_isTracable"/></th>
       <td width="85%">
       		<s:select name="isTracable" list='#application.CodeTable.getCodes("1045")' listKey="CodeKey" listValue="Value" value="1"/>
       	</td>
      </tr>
      <tr>
      <%-- 密码找回 --%>
       <th><s:text name="UPM05_isTrievable"/></th>
       <td width="85%">
       		<s:select name="isTrievable" list='#application.CodeTable.getCodes("1046")' listKey="CodeKey" listValue="Value" value="1"/>
       	</td>
      </tr>
     </table>
     </form>
     <hr class="space"/>
     <div class="center clearfix" id="pageButton">
       <button class="confirm" onclick="doSave('${addSave_url}')" id="confirmBtn">
       	<span class="ui-icon icon-confirm"></span>
       	<%-- 确定按钮 --%>
       	<span class="button-text"><s:text name="UPM05_confirmButton"/></span>
       </button>
	   <button class="close" onclick="doClose('#saveDialog')"><span class="ui-icon icon-close"></span>
         <%-- 关闭 --%>
         <span class="button-text"><s:text name="global.page.close"/></span>
        </button>
     </div>
</div>
</s:i18n>