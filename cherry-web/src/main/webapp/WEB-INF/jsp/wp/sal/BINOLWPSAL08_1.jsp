<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/sal/BINOLWPSAL08_1.js?V=20160816"></script>
<div class="hide">
		<s:url id="s_getMatchRule" value="/wp/BINOLWPSAL08_getMatchRule" />
		<a id="getMatchRuleUrl" href="${s_getMatchRule}"></a>
		<s:url id="s_getComputeRule" value="/wp/BINOLWPSAL08_getComputeRule" />
		<a id="getComputeRuleUrl" href="${s_getComputeRule}"></a>
	<input type="hidden" id="NEW_CZK_PAY" value="<s:property value='NEW_CZK_PAY'/>">
	<input type="hidden" id="saleDetailList" value="<s:property value='saleDetailList'/>">
	<input type="hidden" id="baCode" value="<s:property value='baCode'/>">
	<input type="hidden" id="searchStr" value="<s:property value='searchStr'/>">
	<input type="hidden" id="totalDiscountRate" value="<s:property value='totalDiscountRate'/>">
	<input type="hidden" id="brandInfoId" value="<s:property value='brandInfoId'/>">
	<input type="hidden" id="outresult" >
	<input type="hidden" id="inputSaledetail" >
</div>
<div class="wp_tablebox"><!-- class="wp_detail_s" wp_detail_smartPromotion -->
	<table  width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail" align="center">
		<thead>
		<tr>
			<td>
				是否选中
			</td>
			<td>
				促销条件类型
			</td>
			<td>
				活动名称
			</td>
		</tr>
		</thead>
		<tbody id="activityTbody">
		</tbody>
		</table>
	<div class="bottom_butbox clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL08_1.confirm();return false;">
    		<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><s:text name="确定"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL08_1.cancel();return false;">
			<span class="ui-icon icon-close"></span>
			<span class="button-text"><s:text name="取消"/></span>
		</button>
    </div>
</div>