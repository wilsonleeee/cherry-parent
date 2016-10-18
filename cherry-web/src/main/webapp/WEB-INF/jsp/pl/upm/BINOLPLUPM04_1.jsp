<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.pl.BINOLPLUPM04">
<s:if test="null != pwConfInfo && !pwConfInfo.isEmpty()">
<div class="toolbar clearfix section-header">
    <strong> 
    <span class="left">
    	<span class="ui-icon icon-ttl-section-info"></span>
      	<%-- 密码安全配置信息  --%>
      	<s:text name="UPM04_pwInfo"/>
    </span>
    </strong>
    <cherry:show domId="PLUPM0204EDITA">
   	<a class="add right" onclick="javascript:popEditDialog();return false;" href="javascript:void(0);">
       	<span class="ui-icon icon-edit"></span>
       	<%-- 修改  --%>
       	<span class="button-text"><s:text name="UPM04_editButton"/></span>
	</a>
 	</cherry:show>
   </div>
<table class="detail" cellpadding="0" cellspacing="0">
     <tr>
     	<%-- 密码有效期  --%>
       <th><s:text name="UPM04_duration"/></th>
       <td>
       		<span>
	       		<%-- 密码有效期 --%>
	       		<s:if test="null != pwConfInfo.duration">
	       			<s:property value="pwConfInfo.duration"/>
	       			<%-- 天 --%>
	       			<s:text name="UPM04_day"/>
	       		</s:if>
       		</span>
       	</td>
       	<%-- 密码修改提醒  --%>
       <th><s:text name="UPM04_remindAhead"/></th>
       <td>
       		<span>
       			<%-- 密码修改提醒 --%>
	       		<s:if test="null != pwConfInfo.remindAhead">
	       			<%-- 提前 --%>															<%-- 天 --%>
	       			<s:text name="UPM04_before"/><s:property value="pwConfInfo.remindAhead"/><s:text name="UPM04_day"/>
	       		</s:if>
       		</span>
       		
       </td>
     </tr>
     <tr>
     <%-- 密码重复间隔 --%>
       <th><s:text name="UPM04_repetitionInterval"/></th>
       <td>
       		<span>
       			<%-- 密码重复间隔 --%>
       			<s:property value="pwConfInfo.repetitionInterval"/>
       		</span>
       	</td>
       	<%-- 密码复杂度 --%>
       <th><s:text name="UPM04_complexity"/></th>
       <td>
       		<span>
       		<%-- 允许使用 --%>
       		<s:text name="UPM04_include"/>
       		<%-- 英文 --%>
       		<s:if test='pwConfInfo.hasAlpha == "1"'><s:text name="UPM04_hasAlpha"/></s:if>
       		<%-- 数字 --%>
       		<s:if test='pwConfInfo.hasNumeric == "1"'><s:text name="UPM04_hasNumeric"/></s:if>
       		<%-- 其他字符 --%>
       		<s:if test='pwConfInfo.hasOtherChar == "1"'><s:text name="UPM04_hasOtherChar"/>
       		<s:property value="pwConfInfo.otherCharSpace"/><s:text name="UPM04_hasOtherCharClose"/>
       		</s:if>
       		</span>
       </td>
     </tr>
     <tr>
     	<%-- 密码长度 --%>
       <th><s:text name="UPM04_pwLength"/></th>
       <td>
				<s:if test='null != pwConfInfo.pwLength && !"".equals(pwConfInfo.pwLength)'>
					<%-- 密码最小长度 --%>
       				<span><s:text name="UPM04_minLength"/> <s:property value="pwConfInfo.pwLength"/> <s:text name="UPM04_num"/></span>
       			</s:if>
       		
       			<s:if test='null != pwConfInfo.maxLength && !"".equals(pwConfInfo.maxLength)'>
       				<%-- 密码最大长度 --%>
       				<span style="margin-left:20px;"><s:text name="UPM04_maxLength"/> <s:property value="pwConfInfo.maxLength"/> <s:text name="UPM04_num"/></span>
       			</s:if>
       		
       </td>
       <%-- 允许重试次数 --%>
       <th><s:text name="UPM04_retryTimes"/></th>
       <td>
       		<span>
       			<%-- 允许重试次数 --%>
       			<s:property value="pwConfInfo.retryTimes"/>
       		</span>
       </td>
     </tr>
     <tr>
       <%-- 访问跟踪 --%>
       <th><s:text name="UPM04_isTracable"/></th>
       <td>
       		<span>
       			<%-- 访问跟踪 --%>
       			<s:property value='#application.CodeTable.getVal("1045", pwConfInfo.isTracable)'/>
       		</span>
       </td>
       <%-- 密码找回 --%>
       <th><s:text name="UPM04_isTrievable"/></th>
       <td>
       		<span>
       			<%-- 密码找回 --%>
       			<s:property value='#application.CodeTable.getVal("1046", pwConfInfo.isTrievable)'/>
       		</span>
       </td>
     </tr>
    <tr>
        <%--密码过期策略 --%>
        <th><s:text name="UPM04_overdueTactic"/></th>
        <td>
            <span>
                <%-- 密码过期策略--%>
                <s:property value='#application.CodeTable.getVal("1274", pwConfInfo.overdueTactic)'/>
            </span>
        </td>
        <th></th>
        <td></td>
    </tr>
 </table>
 </s:if>
 <s:else>
	 <div class="toolbar clearfix">
	    <span class="left">
	    	<cherry:show domId="PLUPM0204ADDAA">
	    	<%-- 您还没有设置该品牌的安全密码配置。请点击"添加配置"按钮，进行设置。 --%>
	    	<s:text name="UPM04_addMessage"/>
	    	<a class="add" onclick="javascript:popAddDialog();return false;" href="javascript:void(0);">
	         	<span class="ui-icon icon-add"></span>
	         	<%-- 添加配置 --%>
	         	<span class="button-text"><s:text name="UPM04_addButton"/></span>
	  		</a>
	  		</cherry:show>
	    </span>
	   </div>
 </s:else>
 </s:i18n>