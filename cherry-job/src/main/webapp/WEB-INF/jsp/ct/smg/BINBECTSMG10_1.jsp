<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:iterator value="templateList" id="templateMap" status="status">
	<tr class='<s:if test="%{#status.index%2 == 0}">odd</s:if><s:else>even</s:else>'>
		<td><s:property value="%{#status.index + 1}"/>
		<input type="hidden" id="templateId" name="templateId" value='<s:property value="templateId"/>'/>
		<input type="hidden" id="oldCode" name="oldCode" value='<s:property value="tempCode"/>'/>
		</td>
		<td style="max-width: 600px">
		  <span class="template-content"><s:property value="content"/></span>
		</td>
		<td class="template-code">
		<s:if test="%{#templateMap.tempCode != null}">
		<s:property value="tempCode"/>
		</s:if>
		<s:else>
			<input type="text" name="tempCode" id="tempCode"/>
		</s:else>
		</td>
		<td>
		<input type="hidden" id="content" name="content" value='<s:property value="content"/>'/>
		<span class='btn-save <s:if test="%{#templateMap.templateId != 0}"> hide </s:if>'>
		<a class="add" onclick="updateCode(this);return false;">
			<span class="ui-icon icon-enable"></span><span class="button-text">保存</span>
		</a>
		</span>
		<span class='btn-edit <s:if test="%{#templateMap.templateId == 0}"> hide </s:if>'>
		<a class="add" onclick="edit(this);return false;">
			<span class="ui-icon icon-enable"></span><span class="button-text">编辑</span>
		</a>
		</span>
		<span class="btn-cancel hide">
		<a class="delete" onclick="cancel(this);return false;">
			<span class="ui-icon icon-delete"></span><span class="button-text">取消</span>
		</a>
		</span>
		</td>
	</tr>
</s:iterator>