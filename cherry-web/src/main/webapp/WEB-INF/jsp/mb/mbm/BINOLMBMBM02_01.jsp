<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM02">
	<div class="section">
      <div class="section-header">
        <strong>
          <span class="ui-icon icon-ttl-section-info"></span>
          <s:text name="binolmbmbm02_memberBaseInfo"/>
        </strong>
	  </div>
      <div class="section-content">
      <table class="detail" cellpadding="0" cellspacing="0">
      	<tr>
		  <th><s:text name="binolmbmbm02_name"></s:text></th>
		  <td><span><s:property value="memberInfoMap.name"/></span></td>
		  <th><s:text name="binolmbmbm02_levelName"></s:text></th>
		  <td><span><s:property value="memberInfoMap.levelName"/></span></td>
		</tr>
		<tr>
		  <th><s:text name="binolmbmbm02_mobilePhone"></s:text></th>
		  <td><span><s:property value="memberInfoMap.mobilePhone"/></span></td>
		  <th><s:text name="binolmbmbm02_joinDate"></s:text></th>
		  <td><span><s:property value="memberInfoMap.joinDate"/></span></td>
		</tr>
      </table>
      </div>
	</div>
</s:i18n>	  