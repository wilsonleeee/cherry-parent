<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script>
$(function(){
	//开始销售日期
	BINOLPTJCS24_00.initDate("#sellStartDate", "#sellEndDate", "maxDate");
	// 停止销售日期
	BINOLPTJCS24_00.initDate("#sellEndDate", "#sellStartDate", "minDate");
});
</script>
<s:i18n name="i18n.pt.BINOLPTJCS03">
<s:text name="JCS03_selectFirst" id="JCS03_selectFirst"/>     
<%-- ======================= 扩展属性开始  ============================= --%>
<table class="detail" cellpadding="0" cellspacing="0">
 	<tbody>
	</tbody>
</table>
<%-- 用户自定义扩展属性 --%>
<s:if test="productExtCount > 0">
	<div class="box2-active">
		<div class="detail_box2">
			<table cellspacing="0" cellpadding="0" class="detail" style="margin-bottom:0px;">
				<tbody>
				<tr>
		      		<%-- ======================= 产品扩展属性导入开始  ============================= --%>
				  	<s:action name="BINOLCM12_addPrtExt" namespace="/common" executeResult="true">
				  		<s:param name="brandInfoId"><s:property value="brandInfoId"/></s:param>
				  		<s:param name="id"><s:property value="productId"/></s:param>
				  		<s:param name="parity">0</s:param>
				  	</s:action>
				  	<%-- ======================= 产品扩展属性导入结束  ============================= --%>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
</s:if>
</s:i18n>