<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- 语言 -->
<s:set id="language" value="#session.language" />
<%-- ==================== BOM组件信息开始 ====================== --%>
<s:i18n name="i18n.pt.BINOLPTJCS03">
<s:if test="bomList != null && bomList.size > 0">
<div id="tabs-5" class="ui-tabs-panel">
   	<div class="section">
	    <div class="section-content">
			<table cellpadding="0" cellspacing="0" style="width: 100%">
				<thead>
					<tr>
						<th><s:text name="JCS03_unitCode"/></th>
			        	<th><s:text name="JCS03_barCode"/></th>
			        	<th><s:text name="JCS03_nameTotal"/></th>
			        	<th><s:text name="JCS03_BOMprice"/>(<s:text name="JCS03_moneryUnit"/>)</th>
			        	<th><s:text name="JCS03_BOMCount"/></th>
			        </tr>
				</thead>
			    <tbody>
			    	<s:iterator value="bomList" status="status">
			    	<tr class="<s:if test="#status.odd == true">odd</s:if><s:else>even</s:else>">
			    		<td><span><s:property value="unitCode"/></span></td>
			    		<td><span><s:property value="barCode"/></span></td>
			    		<td><span><s:if test="#language.equal('en_US')"><s:property value="nameEN"/></s:if><s:else></s:else><s:property value="nameCN"/></span></td>
			    		<td><span><s:property value="bomPrice"/></span></td>
			    		<td><span><s:property value="bomQuantity"/></span></td>
			    	</tr>
			    	</s:iterator>
			    </tbody>
			</table>
		</div>
    </div>
</div>
 </s:if>  
</s:i18n>
