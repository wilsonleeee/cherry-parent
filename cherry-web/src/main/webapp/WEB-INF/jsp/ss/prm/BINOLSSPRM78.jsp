<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/json2.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/commonAjax.js"></script>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/jiahua/jiahua.css?v=20160720" type="text/css">
<s:i18n name="i18n.ss.BINOLSSPRM77">
<html>
<head>
<title><s:text name="PRM77.title1"></s:text> </title> 
</head>

<body>
<div id="param" style="display:none;">
	<s:url id="s_getComputeRule" value="/ss/BINOLSSPRM74_getComputeRule" />
		<a id="getComputeRuleUrl" href="${s_getComputeRule}" ></a>
	<s:url id="s_getCouponInfo" value="/ss/BINOLSSPRM74_getCouponInfo" />
		<a id="getCouponInfoUrl" href="${s_getCouponInfo}" ></a>
	<s:url id="s_collect" value="/ss/BINOLSSPRM74_collect" />
		<a id="collectUrl" href="${s_collect}" ></a>
	<input id="collectErr" value='<s:text name="PRM74.collectErr"></s:text>'>
	<input id="closeWindowErr" value='<s:text name="PRM74.closeWindowErr"></s:text>'>
	<input id="connectNetErr" value='<s:text name="PRM74.connectNetErr"></s:text>'>
	<input id="connectTimes" value="1">
	<input id="datasourceName" value='<s:property value="datasourceName"/>'>
	<%--会员手机号 --%>
	<input id="memberPhone_param" value='<s:property value="%{result_map.main_map.MP}"/>'>
	<%--主单信息 --%>
	<input id="main_json" value='<s:property value="%{result_map.main_json}"/>'/>
	<%--原始购物车信息 --%>
	<input id="shoppingcart_json" value='<s:property value="%{result_map.shoppingcart_json}"/>'/>
	<%--分组后购物车信息 --%>
	<input id="shoppingcartOrder_json" value='<s:property value="%{result_map.shoppingcartOrder_json}"/>'/>
	<%--第一次返回的Result信息,id标准为全部是0（全部未选中状态） --%>
	<input id='<s:property value="%{result_map.result_id}"/>'  value='<s:property value="%{result_map.result_json}"/>'/>
	<%--返回的信息值，当为0时成功 --%>
	<input id="resultCode" value='<s:property value="%{result_map.resultCode}"/>'>
	<input id="resultMessage" value='<s:property value="%{result_map.resultMessage}"/>'>
	<div id="bodyId"></div>	
</div>
<div class="jh_div">
	<cherry:form id="promotion_head">
	<div class="jh_top">
 	    <s:if test="result_map.main_map.ML.isEmpty() ||null == result_map.main_map.ML">
 	    	<div class="top_item"><label><s:text name="PRM74.customer"></s:text> </label><span>VBC000000001</span></div>
 	    </s:if>
 	    <s:else>
	   	    <div class="top_item"><label><s:text name="PRM74.memName"></s:text> </label><span><s:property value="%{result_map.main_map.MN}"/></span></div>
	        <div class="top_item"><label><s:text name="PRM74.memCard"></s:text> </label><span id="memberCode"><s:property value="%{result_map.main_map.MC}"/></span></div>
	        <div class="top_item"><label><s:text name="PRM74.memPhone"></s:text> </label><span><s:property value="%{result_map.main_map.MP}"/></span></div>
	        <div class="top_item"><label><s:text name="PRM74.memLevel"></s:text> </label><span><s:property value="%{result_map.main_map.MLN}"/></span></div>
 	    </s:else> 
    </div>
	</cherry:form>
    <s:property value="%{paramdataStr}"/>
    <div class="jh_bottom">
    	<div class="bottom_left">
        	<div class="bottom_item"><input class="jh_input" maxlength="15" onkeypress="if(event.keyCode==13) {BINOLSSPRM74.addCoupon();return false;}" id="inputCouponCode" placeholder='<s:text name="PRM74.title6"></s:text>'/><button class="jh_btn" onclick="BINOLSSPRM74.addCoupon();return false;"><s:text name="PRM74.add"></s:text></button></div>
            <div class="bottom_item promotion">
            	<label><s:text name="PRM74.receivableTotal"></s:text></label><span id="receivableTotal"><s:property value="%{result_map.sum_cart}"/></span><input style="display:none;" id="receivableTotal_old" value='<s:property value="%{result_map.sum_cart}"/>'>
            	<label><s:text name="PRM74.totalDiscount"></s:text></label><span id="discountTotal">0.0</span> 
            	<label><s:text name="PRM74.actualTotal"></s:text></label><span id="actualTotal"><s:property value="%{result_map.sum_cart}"/></span>
            </div>
        </div>
    	<div class="bottom_right">
        	<button class="jh_btn2" onfocus="this.blur()" onclick="BINOLSSPRM74.collect();return false;"><span class="iconbg icon_1"></span><span class="btn_text"><s:text name="PRM74.conform"></s:text> </span></button>
            <button class="jh_btn2" onfocus="this.blur()" id="show" onclick='BINOLSSPRM74.showhidediv("msg");return false;'><span class="iconbg icon_2"></span><span class="btn_text"><s:text name="PRM74.allActivity"></s:text> </span></button>
            <button class="jh_btn2" onfocus="this.blur()" onclick="BINOLSSPRM74.collectWithoutRule();return false;" style="width:120px;"><span class="iconbg icon_3"></span><span class="btn_text"><s:text name="PRM74.noActivity"></s:text> </span></button>
            <button class="jh_btn2" onfocus="this.blur()" onclick="BINOLSSPRM74.closeWindow();return false;"><span class="iconbg icon_4"></span><span class="btn_text"><s:text name="PRM74.cancel"></s:text> </span></button>
        </div>
    </div>
</div>

</body>
</html>

</s:i18n>
