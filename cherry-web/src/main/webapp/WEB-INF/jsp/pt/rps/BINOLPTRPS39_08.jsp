<%-- 生成代理商优惠券弹出框--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<div id="actionResultDisplay"></div>
<form id="createForm">
    <s:i18n name="i18n.pt.BINOLPTRPS39">
        <div>
	  		<table style="margin:auto; width:100%;" class="detail">
		  		<tr>
		  			<!-- 批次名称-->
			  		<th><s:text name="rps39_deliverNo"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  		<td><span><s:textfield name="deliverNo" cssClass="text" maxlength="30"/></span></td>
		  		</tr>
	  		</table>
  		</div>
    </s:i18n>
</form>