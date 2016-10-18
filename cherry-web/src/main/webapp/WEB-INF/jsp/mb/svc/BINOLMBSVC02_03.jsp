<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/svc/BINOLMBSVC02_03.js?v=20160727"></script>
<s:i18n name="i18n.mb.BINOLMBSVC02">
<div class="hide">
	<s:url id="s_checkCard" value="/mb/BINOLMBSVC03_checkCard" />
		<a id="checkCardUrl" href="${s_checkCard}"></a>
	<s:url id="s_openCard" value="/mb/BINOLMBSVC02_openCard" />
		<a id="openCardUrl" href="${s_openCard}"></a>
	<div id="result_dialog_success"><s:text name="SVC02_openCardSuccess"></s:text></div>
	<div id="result_dialog_error"><s:text name="SVC02_openCardError"></s:text> </div>
</div>
<div id="result_success" class="hide">
	<div class="dialog2 clearfix" 
			id="result_dialog">
			<p class="clearfix">
			<p class="success">
				<span></span>
			</p>
	</div>
</div>
<div id="result_error" class="hide">
	<div class="dialog2 clearfix" 
			id="result_dialog">
			<p class="clearfix">
			<p class="message">
				<span></span>
			</p>
	</div>
</div>
<div class="wp_tablebox">
<form id="openCard_form">
	<span class="right">
		<a  class="add"
			onclick="BINOLMBSVC02_03.addLines();return false;"> <span
			class="button-text"><s:text name="SVC02_addLines" /></span> <span
			class="ui-icon icon-add"></span>
		</a>
	</span>
	<span class="left">
		<label style="width: 50px;" class="text"><s:text name="SVC02_counterCode" /></label>
		<span><input id="counterName" class="text" name="counterName"  style="width:80px" /></span>
	</span>
	<input type="password" style="position: absolute; top: -999px"/>
	<table  width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table" align="center">
		<thead>
			<tr>
				<th>
					<s:text name="SVC02_cardNo"></s:text>
				</th>
				<th>
					<s:text name="SVC02_payAmount"></s:text>
				</th>
				<th>
					<s:text name="SVC02_cardState"></s:text>
				</th>
				<th>
					<s:text name="SVC02_password"></s:text>
				</th>
				<th>
					<s:text name="SVC02_errorMsg"></s:text>
				</th>
				<th>
					<s:text name="SVC02_operator"></s:text>
				</th>
			</tr>
		</thead>
		<tbody id="cardTbody">
		</tbody>
	</table>
</form>
	<div class="center clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLMBSVC02_03.confirm();return false;">
    		<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><s:text name="SVC02_confirm"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLMBSVC02_03.cancel();return false;">
			<span class="ui-icon icon-close"></span>
			<span class="button-text"><s:text name="SVC02_cancel"/></span>
		</button>
    </div>
    <div class="hide" id="cardModel">
    	<table  width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table" align="center">
    		<tr>
				<td>
					<span>
						<input name="cardCode" class="text" onfocus="BINOLMBSVC02_03.checkCounter();return false;" onblur="BINOLMBSVC02_03.checkCard(this);return false;" style="width: 90px" >
					</span>
				</td>
				<td>
					<span>
						<input name="rechargeValue" class="text" style="width:90px" onblur="BINOLMBSVC02_03.mouseleaveFc(this);return false;">
					</span>
				</td>
				<td>
					<span>
						<s:select name="cardState" Cssstyle="width:186px;" list='#application.CodeTable.getCodes("1339")'
								listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}" value="''" onblur="BINOLMBSVC02_03.mouseleaveFc(this);return false;"/>
					</span>
				</td>
				<td>
					<span>
						<input style="display:none"/>
						<input name="cardPassword" type="password" class="text" style="width:90px" value="" onblur="BINOLMBSVC02_03.mouseleaveFc(this);return false;">
					</span>
				</td>
				<td>
					<span>
					</span>
				</td>
				<td>
					<span>
						<a class="delete" onclick="BINOLMBSVC02_03.delLine(this);return false;">
						<span class="ui-icon icon-delete"></span>
						<span class="button-text"><s:text name='SVC03_delLine'></s:text></span>
						</a>
					</span>
				</td>
			</tr>
    	</table>
    </div>
</div>
</s:i18n>
