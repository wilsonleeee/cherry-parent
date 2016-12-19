<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/json2.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/commonAjax.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/ss/prm/BINOLSSPRM74_3.js?V=20161218"></script>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/jiahua/style2.css?V=20160811" type="text/css">
<s:i18n name="i18n.ss.BINOLSSPRM74_3">
<html>
<head>
<title><s:text name="PRM74_3.title1"></s:text> </title> 
</head>

<body>
<div id="param" style="display:none;">
	<s:url id="s_promotionProSendCheck" value="/ss/BINOLSSPRM74_promotionProSendCheck" />
		<a id="promotionProSendCheckUrl" href="${s_promotionProSendCheck}" ></a>
	<s:url id="s_promotionSendProCollect" value="/ss/BINOLSSPRM74_promotionSendProCollect" />
		<a id="promotionSendProCollectUrl" href="${s_promotionSendProCollect}" ></a>
	<input id="connectTimes" value="1">
	<input id="datasourceName" value='<s:property value="datasourceName"/>'>
	<input id="collectErr" value='<s:text name="PRM74_3.collectErr"></s:text>'>
	<input id="closeWindowErr" value='<s:text name="PRM74_3.closeWindowErr"></s:text>'>
	<input id="connectNetErr" value='<s:text name="PRM74_3.connectNetErr"></s:text>'>
	<%--会员手机号 --%>
	<input id="memberPhone_param" value='<s:property value="%{result_map.main_map.MP}"/>'>
	<%--主单信息 --%>
	<input id="main_json" value='<s:property value="%{result_map.main_json}"/>'/>
	<%--第一次返回的Result信息,id标准为全部是0（全部未选中状态） --%>
	<input id='<s:property value="%{result_map.result_id}"/>'  value='<s:property value="%{result_map.result_json}"/>'/>
	<%--返回的信息值，当为0时成功 --%>
	<input id="resultCode" value='<s:property value="%{result_map.resultCode}"/>'>
	<input id="resultMessage" value='<s:property value="%{result_map.resultMessage}"/>'>
</div>
<div class="jh_div">
	<div class="jh_top">
    	<s:if test="result_map.main_map.ML.isEmpty() ||null == result_map.main_map.ML">
 	    	<div class="top_item"><label><s:text name="PRM74_3.customer"></s:text> </label><span id="memberCode">VBC000000001</span></div>
 	    </s:if>
 	    <s:else>
	   	    <div class="top_item"><label><s:text name="PRM74_3.memberName"></s:text> </label><span><s:property value="%{result_map.main_map.MN}"/></span></div>
	        <div class="top_item"><label><s:text name="PRM74_3.memberCode"></s:text> </label><span id="memberCode"><s:property value="%{result_map.main_map.MC}"/></span></div>
	        <div class="top_item"><label><s:text name="PRM74_3.memberPhone"></s:text> </label><span><s:property value="%{result_map.main_map.MP}"/></span></div>
	        <div class="top_item"><label><s:text name="PRM74_3.memberLevel"></s:text> </label><span><s:property value="%{result_map.main_map.MLN}"/></span></div>
 	    </s:else> 
    </div>
    <div class="jh_contentbox">
        <div class="leftbox">
        	<div class="formtitle"><h1><span class="leftline1"></span><s:text name="PRM74_3.title2"></s:text> </h1></div>
        	<div class="tablebox">
            	<table id="rule_table">
            		<thead>
                	<tr>
                    	<th></th>
                        <%-- <th><s:text name="PRM74_3.couponType"></s:text> </th> --%>
                        <th><s:text name="PRM74_3.couponCodeName"></s:text> </th>
                        <th><s:text name="PRM74_3.couponRule"></s:text> </th>
                        <th><s:text name="PRM74_3.startTime"></s:text> </th>
                        <th><s:text name="PRM74_3.endTime"></s:text> </th>
                    </tr>
                    </thead>
                    <tbody>
                    <s:iterator value="%{result_map.rule_list}"   var="rule">
                	<tr onmouseover="BINOLSSPRM74_3.onmouseoverEvent(this,1);" onmouseout="BINOLSSPRM74_3.onmouseoutEvent(this);" onclick="BINOLSSPRM74_3.showDescriptionDtl(this);">
                    	<td>
                        	<div class="checkbox">
                        		<input name="checkbox-1" type="checkbox" class="regular-checkbox" id="checkbox-1" onclick="BINOLSSPRM74_3.checkCoupon(this);">
                            	<label for="checkbox-1"><span></span></label>
                            </div>
                        </td>
                        <%-- <td><s:property value="typeName"/> </td> --%>
                        <td width="20%"><s:property value="couponCode"/> </td>
                        <td width="25%"><s:property value="couponName"/> </td>
                        <td><s:property value="startTime"/> </td>
                        <td><s:property value="endTime"/> </td>
                        <td  style="display:none;">
                        	<input name="couponName" value='<s:property value="couponName"/>'>
                        	<input name="maincode" value='<s:property value="maincode"/>'>
                        	<input name="couponType" value='<s:property value="couponType"/>'>
                        	<input name="descriptionDtl" value='<s:property value="descriptionDtl"/>'>
                        	<input name="couponCode" value='<s:property value="couponCode"/>'>
                        	<input name="couponType" value='<s:property value="couponType"/>'>
                        	<input name="maxCount" value='<s:property value="maxCount"/>'>
                        	<input name="fullFlag" value='<s:property value="fullFlag"/>'>
                        	<input name="password" value=""/>
                        </td>
                    </tr>
                    </s:iterator>
                    </tbody>
                </table>
            </div>
            <div class="explainbox">
            	<h2><span class="leftline3"></span><s:text name="PRM74_3.title3"></s:text> </h2>
                <p></p>
            </div>            
        </div>
        <div class="rightbox">
        	<div class="itembox none">
                <div class="formtitle"><h1><span class="leftline2"></span><s:text name="PRM74_3.title4"></s:text> </h1></div>
                <div class="scanningbox"><label><s:text name="PRM74_3.title5"></s:text> </label><input class="jh_input" onkeypress="if(event.keyCode==13) {BINOLSSPRM74_3.scannerEvent(this);}"/></div>
                <div class="tablebox gift">
                    <table id="activityProTable">
                    	<thead>
	                        <tr>
	                        	<th></th>
	                            <th><s:text name="PRM74_3.unicode"></s:text> </th>
	                            <th><s:text name="PRM74_3.proName"></s:text> </th>
	                            <th><s:text name="PRM74_3.giftQuantity"></s:text> </th>
	                            <th><s:text name="PRM74_3.unitPrice"></s:text> </th>
	                        </tr>
                        </thead>
                        <s:iterator value= "%{result_map.couponProList}"   var ="product">
                        	<tbody id='<s:property value="#product.couponCode"/>' style="display:none;">
	                        	<s:iterator value="#product.pro_list" var="productDetail" status="i">
			                        <tr onmouseover="BINOLSSPRM74_3.onmouseoverEvent(this,2)" onmouseout="BINOLSSPRM74_3.onmouseoutEvent(this);">
			                        	<td>
				                        	<div class="checkbox" >
				                        		<input onclick="BINOLSSPRM74_3.checkPro(this);" name="checkbox-b<s:property value='%{#i.index}'/>" type="checkbox" class="regular-checkbox" id="checkbox-b<s:property value='%{#i.index}'/>">
				                            	<label for="checkbox-b<s:property value='%{#i.index}'/>"><span></span></label>
				                            </div>
	                        			</td>
			                            <td><s:property value="unitCode"/> </td>
			                            <td><s:property value="proName"/> </td>
			                            <td><s:property value="ProQuantity"/> </td>
			                            <td><s:property value="price"/> </td>
			                            <td style="display:none;">
			                            	<input name="unitCode" value='<s:property value="unitCode"/>'/>
			                            	<input name="barCode" value='<s:property value="barCode"/>'/>
			                            	<input name="price" value='<s:property value="price"/>'/>
			                            	<input name="quantity" value='<s:property value="ProQuantity"/>'/>
			                            </td>
			                        </tr>
			                     </s:iterator>
	                        </tbody>
                        </s:iterator>
                    </table>
                </div> 
            </div>
        </div>
    </div>
    <div class="jh_bottom">
    	<div class="bottom_left">
        	<div class="bottom_item"><input maxlength="15" id="couponCode_input" onkeypress="if(event.keyCode==13) {BINOLSSPRM74_3.appendCoupon();}" class="jh_input" placeholder='<s:text name="PRM74_3.title6"></s:text>'/><button onclick="BINOLSSPRM74_3.appendCoupon();return false;" onfocus="this.blur()" class="jh_btn"><s:text name="PRM74_3.append"></s:text> </button></div>
        </div>
    	<div class="bottom_right">
        	<button class="jh_btn2" onfocus="this.blur()" onclick="BINOLSSPRM74_3.collect();"><span class="iconbg icon_1"></span><span class="btn_text"><s:text name="PRM74_3.confirmCoupon"></s:text> </span></button>
            <button class="jh_btn2" onfocus="this.blur()" onclick="BINOLSSPRM74_3.closeWindow();"><span class="iconbg icon_4"></span><span class="btn_text"><s:text name="PRM74_3.cancelCoupon"></s:text> </span></button>
        </div>
    </div>
</div>
<div class="windowbox2" id="err" style="display:none;">
	<span class="close2" id="show3" onclick='BINOLSSPRM74_3.showhidediv("err");return false;'></span>
	<div class="windowsubbox">
		<div class="error" id="showResultMessage"></div>
		<div class="windowsbtnbox">
			<button class="jh_btn2" onfocus="this.blur()" onclick='BINOLSSPRM74_3.showhidediv("err");return false;'><span class="iconbg icon_1"></span><span class="btn_text"><s:text name="PRM74_3.confirm"></s:text> </span></button>
			<%-- <button class="btn_qx"><s:text name="PRM74_3.cancel"></s:text></button> --%>
		</div>
	</div>
</div>
<div class="windowbox" id="dzq" style="display:none;">
	<span class="close2" id="show4" onclick='BINOLSSPRM74_3.showhidediv("dzq");return false;'></span>
	<div class="windowsubbox">
		<div class="title"><s:text name="PRM74_3.title7"></s:text> </div>
		<div class="dzqbox">
			<s:if test="result_map.main_map.ML.isEmpty() ||null == result_map.main_map.ML">
				<div class="dzqsubbox"><label><s:text name="PRM74_3.memberPhone"/></label><span id="mobileNo"><input class="dzq_input"  placeholder='<s:property value="PRM74_3.phoneCheckCodeTitle"/>'/></span></div>
			</s:if>
			<s:else>
				<div class="dzqsubbox"><label><s:text name="PRM74_3.memberCode"></s:text></label><span><s:property value="%{result_map.main_map.MC}"/></span></div>
				<div class="dzqsubbox"><label><s:text name="PRM74_3.memberPhone"/></label><span id="mobileNo"><s:property value="%{result_map.main_map.MP}"/></span></div>
			</s:else>
			<div class="dzqsubbox"><label><s:text name="PRM74_3.couponCode"></s:text> </label><span id="couponCode"></span></div>
			<div class="dzqsubbox"><label><s:text name="PRM74_3.couponPassword"></s:text> </label><span><input id="couponPassword" class="dzq_input" placeholder='<s:text name="PRM74_3.couponPasswordInput"></s:text>'/></span></div>
			<div class="windowsbtnbox">
				<button class="btn_qr" onclick="BINOLSSPRM74_3.commitCoupon();"><s:text name="PRM74_3.confirm"></s:text> </button>
				<button class="btn_qx" onclick='BINOLSSPRM74_3.showhidediv("dzq");return false;'><s:text name="PRM74_3.cancel"></s:text> </button>
			</div>
		</div>
	</div>
</div>
<div class="windowbox" id="dzq2" style="display:none;">
	<span class="close2" id="show4" onclick='BINOLSSPRM74_3.showhidediv("dzq2");return false;'></span>
	<div class="windowsubbox">
		<div class="title"><s:text name="PRM74_3.title7"></s:text> </div>
		<div class="dzqbox">
			<s:if test="result_map.main_map.ML.isEmpty() ||null == result_map.main_map.ML">
				<div class="dzqsubbox"><label><s:text name="PRM74_3.memberPhone"/></label><span id="mobileNo2"><input class="dzq_input"  placeholder='<s:property value="PRM74_3.phoneCheckCodeTitle"/>'/></span></div>
			</s:if>
			<s:else>
				<div class="dzqsubbox"><label><s:text name="PRM74_3.memberCode"></s:text></label><span><s:property value="%{result_map.main_map.MC}"/></span></div>
				<div class="dzqsubbox"><label><s:text name="PRM74_3.memberPhone"/></label><span id="mobileNo2"><s:property value="%{result_map.main_map.MP}"/></span></div>
			</s:else>
			<div class="dzqsubbox"><label><s:text name="PRM74_3.couponCode"></s:text> </label><span id="couponCode2"></span></div>
			<div class="dzqsubbox"><label><s:text name="PRM74_3.couponPassword"></s:text> </label><span><input id="couponPassword2" class="dzq_input" placeholder='<s:text name="PRM74_3.couponPasswordInput"></s:text>'/></span></div>
			<div class="windowsbtnbox">
				<button class="btn_qr" id="dzq2Commit"><s:text name="PRM74_3.confirm"></s:text> </button>
				<button class="btn_qx" onclick='BINOLSSPRM74_3.showhidediv("dzq2");return false;'><s:text name="PRM74_3.cancel"></s:text> </button>
			</div>
		</div>
	</div>
</div>

</body>
</html>

</s:i18n>
