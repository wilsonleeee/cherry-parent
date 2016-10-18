<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/json2.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/commonAjax.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/ss/prm/BINOLSSPRM74_2.js?v=20160905"></script>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/jiahua/style.css?v=20160811" type="text/css">
<s:i18n name="i18n.ss.BINOLSSPRM74_2">
<html>
<head>
<meta charset="UTF-8">
<title><s:text name="PRM74_2.title1"></s:text> </title>
</head>

<body>
<div id="param" style="display:none;">
	<s:url id="s_promotionSend" value="/ss/BINOLSSPRM74_promotionSend" />
		<a id="promotionSendUrl" href="${s_promotionSend}" ></a>
	<input id="confirmErr" value='<s:text name="PRM74_2.confirmErr"></s:text>'/>
	<input id="connectErr" value='<s:text name="PRM74_2.connectErr"></s:text>'/>
	<input id="connectTimes" value="1">
	<input id="sendChooseFlag" value='<s:property value="sendChooseFlag"/>'>
	<input id="datasourceName" value='<s:property value="datasourceName"/>'>
	<input id="main_map_json" value='<s:property value="%{result_map.main_map_json}"/>'>
	<input id="cart_list_json" value='<s:property value="%{result_map.cart_list_json}"/>'>
	<input id="rule_list_json" value='<s:property value="%{result_map.rule_list_json}"/>'>
	<%--返回的信息值，当为0时成功 --%>
	<input id="resultCode" value='<s:property value="%{result_map.resultCode}"/>'>
	<input id="resultMessage" value='<s:property value="%{result_map.resultMessage}"/>'>
</div>


<div class="jh_div">
    <div class="jh_contentbox">
		<div class="dzqbox">
			<div class="dzqsubbox"><label><s:text name="PRM74_2.phone"></s:text></label>
				<s:if test="result_map.memberCode.isEmpty() ||null == result_map.memberCode">
					<span id="mobileNo">
						<input maxlength="11" onblur="BINOLSSPRM74_2.checkMobile(this);return false;" class="dzq_input" placeholder='<s:text name="PRM74_2.inputPhone"></s:text>'/><span id="mobileError" class="red" style="display:none;"><s:text name="PRM74_2.mobileError"></s:text> </span>
					</span>
				</s:if>
				<s:else>
					<span id="mobileNo"><s:property value="result_map.memberPhone"/></span>
					<span class="prompt"><span class="red">*</span><s:text name="PRM74_2.title4"></s:text></span>
				</s:else>
			</div>
			<div class="dzqsubbox"><label><s:text name="PRM74_2.member"></s:text></label>
				<s:if test="result_map.memberCode.isEmpty() ||null == result_map.memberCode">
					<span id="memberCode"><s:text name="PRM74_2.memberCommon"></s:text></span>
				</s:if>
 	    		<s:else>
 	    			<span id="memberCode"><s:property value="result_map.memberCode"/></span>
 	    		</s:else>
			</div>
			<div class="dzqsubbox"><label><s:text name="PRM74_2.tradeNo"></s:text></label><span>&nbsp;<s:property value="result_map.tradeNoIF"/></span></div>
		</div>
		<div class="formtitle"><h1><span class="leftline1"></span><s:text name="PRM74_2.title2"></s:text></h1></div>
		<div class="tablebox">
			<table id="rule_table">
				<thead>
					<tr>
						<th></th>
						<th><s:text name="PRM74_2.activityType"></s:text> </th>
						<th><s:text name="PRM74_2.activityName"></s:text> </th>
						<th><s:text name="PRM74_2.startDate"></s:text> </th>
						<th><s:text name="PRM74_2.endDate"></s:text> </th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="%{result_map.rule_list}" id="rule">
						<tr onmouseover="this.className='tron'" onmouseout="this.className=''">
							<td>
								<div class="checkbox">
										<input type="checkbox" onclick='BINOLSSPRM74_2.checkGroup(this);'/>
									<%-- <s:if test="#rule.groupFlag == 0">
										<input type="checkbox"/>
									</s:if>
									<s:elseif test="#rule.groupFlag == 1">
										<input name='<s:property value="groupId"/>' type="radio"/>
									</s:elseif> --%>
								</div>
							</td>
							<td><s:property value="ruleTypeName"/></td>
							<td><s:property value="ruleName"/></td>
							<td><s:property value="startDate"/></td>
							<td><s:property value="endDate"/></td>
							<td style="display:none;">
								<input name="maincode" value='<s:property value="maincode"/>'/>
								<input name="ruleType" value='<s:property value="ruleType"/>'/>
								<input name="groupId" value='<s:property value="groupId"/>'/>
								<input name="groupFlag" value='<s:property value="groupFlag"/>'/>
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
    </div>
</div>
<div class="windowbox2" id="dzq" style="display:none;height:250px;margin:-190px 0 0 -180px;">
	<span class="close2" id="show4" onclick='BINOLSSPRM74_2.cancel();return false;'></span>
	<div class="windowsubbox">
		<div class="title"><s:text name="PRM74_2.title3"></s:text> </div>
		<div class="error"><s:text name="PRM74_2.sendSuccess"></s:text> <br/></div>
		<div class="windowsbtnbox">
			<button class="btn_qr" onclick='BINOLSSPRM74_2.cancel();return false;'><s:text name="PRM74_2.confirm"></s:text>  </button>
		</div>
	</div>
</div>

<div class="windowbox2" id="err" style="display:none;">
	<span class="close2" id="show3" onclick='BINOLSSPRM74_2.showhidediv("err");return false;'></span>
	<div class="windowsubbox">
		<div class="error" id="showResultMessage"></div>
		<div class="windowsbtnbox">
			<button class="jh_btn2" onfocus="this.blur()" onclick='BINOLSSPRM74_2.showhidediv("err");return false;'><span class="iconbg icon_1"></span><span class="btn_text"><s:text name="PRM74_2.confirm"></s:text> </span></button>
			<%--
			<button class="btn_qx"><s:text name="PRM74.cancel"></s:text></button>
			 --%>
		</div>
	</div>
</div>

<div class="jh_bottom">
    	<div>
        	<button class="jh_btn2" onfocus="this.blur()" onclick="BINOLSSPRM74_2.confirm();return false;"><span class="iconbg icon_1"></span><span class="btn_text"><s:text name="PRM74_2.confirm"></s:text> </span></button>
            <button class="jh_btn2" onfocus="this.blur()" onclick="BINOLSSPRM74_2.cancel();return false;"><span class="iconbg icon_4"></span><span class="btn_text"><s:text name="PRM74_2.cancel"></s:text> </span></button>
        </div>
</div>

</body>
</html>


</s:i18n>
