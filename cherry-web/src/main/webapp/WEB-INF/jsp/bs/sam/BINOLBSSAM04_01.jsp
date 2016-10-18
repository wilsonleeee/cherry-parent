<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<script type="text/javascript" src="/Cherry/js/bs/sam/BINOLBSSAM04.js"></script>
<s:i18n name="i18n.bs.BINOLBSSAM04">
<div id="aaData">
    <s:iterator value="resultSalesBonusRateList" status="status">
     <ul>
     	<li><s:property value="#status.index+iDisplayStart+1" /></li>
     	<li>
     		<s:if test='bonusEmployeePosition=="-9999"'><s:text name="BSSAM04_position"/></s:if>
     		<s:else><s:property value="bonusEmployeePosition"/></s:else>
     	</li>
     	<li>
     		<s:if test='saleEmployeePosition=="-9999"'><s:text name="BSSAM04_position"/></s:if>
     		<s:else><s:property value="saleEmployeePosition"/></s:else></li>
       	<li><s:property value="counterCode"/></li>
       	<li><s:property value="beginAmount"/></li>
       	<li><s:property value="endAmount"/></li>
       	<li><s:property value="bonusRate"/></li>
       	<li><s:property value="memo"/></li>
       	<li>
			<a href="/Cherry/basis/BINOLBSSAM04_editInit.action?recordId=${recordId}" class="delete" onclick="javascript:openWin(this);return false;">
                <span class="ui-icon icon-edit"></span>
                <span class="button-text"><s:text name="BSSAM04_edit"/></span>
            </a>
            <a href="javascript:void(0)" onclick="BINOLBSSAM04.delete('${recordId}');return false;" class="delete">
                <span class="ui-icon icon-delete"></span>
                <span class="button-text"><s:text name="BSSAM04_delete"/></span>
            </a>
		</li>
     </ul>
    </s:iterator>
</div>
</s:i18n>