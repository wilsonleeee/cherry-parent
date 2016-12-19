<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/json2.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/commonAjax.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/ss/prm/BINOLSSPRM74.js?v=20161218"></script>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/jiahua/jiahua.css?v=20160811" type="text/css">
<s:i18n name="i18n.ss.BINOLSSPRM74">
<html>
<head>
<title><s:text name="PRM74.title1"></s:text> </title> 
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
	<input id="usePoint_main" value="" />
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
    <div class="jh_contentbox">
        <div class="leftbox">
        	<div class="formtitle"><h1><span class="leftline1"></span><s:text name="PRM74.title2"></s:text></h1></div>
        	<div class="tablebox">
            	<table id="promotion_table">
            		<thead>
	                	<tr>
	                    	<th></th>
	                        <th><s:text name="PRM74.activityType"></s:text> </th>
	                        <th><s:text name="PRM74.activityName"></s:text> </th>
	                        <th><s:text name="PRM74.planDiscountPrice"></s:text> </th>
	                        <th><s:text name="PRM74.endTime"></s:text> </th>
	                        <th><s:text name="PRM74.actualDiscountPrice"></s:text> </th>
	                    </tr>
            		</thead>
            		<tbody id="coupon_table">
            			<s:iterator value="%{result_map.coupon_list}" id="coupon" status="i">
            				<tr onmouseover="BINOLSSPRM74.onmouseoverEvent(this);" onmouseout="BINOLSSPRM74.onmouseoutEvent(this);" onclick="BINOLSSPRM74.showDescriptionDtl(this);">
		                    	<td>
		                        	<div class="checkbox" >
		                        		<input onclick="BINOLSSPRM74.openCouponWindow(this);" name="checkbox-c<s:property value='%{#i.index}'/>" type="checkbox" class="regular-checkbox" id="checkbox-c<s:property value='%{#i.index}'/>">
		                            	<label for="checkbox-c<s:property value='%{#i.index}'/>"><span></span></label>
		                            </div>
		                        </td>
		                        <td><s:property value="typeName"/></td>
		                        <td><s:property value="couponName"/>(<s:property value="couponCode"/>)</td>
		                        <s:if test="#coupon.couponType == 3">
		                        	<td></td>
		                        </s:if>
		                        <s:else>
		                        	<s:if test="#coupon.couponType == 5">
		                        		<td><s:property value="planDiscountPrice"/>%</td>
		                        	</s:if>
		                        	<s:else>
			                        	<td><s:property value="planDiscountPrice"/></td>
		                        	</s:else>
		                        </s:else>
		                        <td><s:property value="endTime"/></td>
		                        <td></td>
		                        <td style="display:none;">
		                        	<input name="couponCode" value='<s:property value="couponCode"/>'/>
		                        	<input name="couponName" value='<s:property value="couponName"/>'/>
		                        	<input name="maincode" value='<s:property value="maincode"/>'/>
		                        	<input name="couponType" value='<s:property value="couponType"/>'/>
									<input name="unicode" value='<s:property value="unicode"/>'/>
									<input name="barcode" value='<s:property value="barcode"/>'/>
									<input name="planDiscountPrice" value="<s:property value="planDiscountPrice"/>">
		                        	<input name="actualDiscountPrice" value="">
									<input name="checkFlag" value='<s:property value="checkflag"/>'/>
									<input name="passwordFlag" value='<s:property value="passwordFlag"/>'/>
									<input name="password" value="">
									<input name="descriptionDtl" value='<s:property value="descriptionDtl"/>'/>
									<input name="ZGQArr" value='<s:property value="ZGQArr"/>'/>
		                        </td>
		                    </tr>
            			</s:iterator>
            		</tbody>
                    <tbody id="rule_table">
	                    <s:iterator value="%{result_map.rule_list}" id="rule" status="i">
		                	<tr onmouseover="BINOLSSPRM74.onmouseoverEvent(this);" onmouseout="BINOLSSPRM74.onmouseoutEvent(this);" onclick="BINOLSSPRM74.showDescriptionDtl(this);">
		                    	<td>
		                        	<div class="checkbox" >
		                        		<s:if test="activityType == 'JFDK'">
		                        			<input onclick="BINOLSSPRM74.openPointWindow(this);" name="checkbox-a<s:property value='%{#i.index}'/>" type="checkbox" class="regular-checkbox" id="checkbox-a<s:property value='%{#i.index}'/>">
		                        		</s:if>
		                        		<s:else>
			                        		<input onclick="BINOLSSPRM74.changeCheck(this);" name="checkbox-a<s:property value='%{#i.index}'/>" type="checkbox" class="regular-checkbox" id="checkbox-a<s:property value='%{#i.index}'/>">
		                        		</s:else>
		                            	<label for="checkbox-a<s:property value='%{#i.index}'/>"><span></span></label>
		                            </div>
		                        </td>
		                        <td>
		                        	<s:if test="rulecondtype == 2 ">
		                        		<s:text name="PRM74.activity1"></s:text> <%--商品折扣 --%>
		                        	</s:if>
		                        	<s:else>
		                        		<s:text name="PRM74.activity2"></s:text> <%--整单折扣 --%>
		                        	</s:else>
		                        </td>
		                        <td><s:property value="mainname"/></td>
		                        <td><s:property value="planDiscountPrice"/></td>
		                        <td><s:property value="endTime"/></td>
		                        <td></td>
		                        <td style="display:none;">
		                        	<input name="flag" value="<s:property value="flag"/>"/>
		                        	<input name="times" value="<s:property value="times"/>"/>
		                        	<input name="matchtimes" value="<s:property value="matchtimes"/>"/>
		                        	<input name="maincode" value="<s:property value="maincode"/>"/>
		                        	<input name="mainname" value="<s:property value="mainname"/>"/>
		                        	<input name="rulecondtype" value="<s:property value="rulecondtype"/>"/>
		                        	<input name="ismust" value="<s:property value="ismust"/>"/>
		                        	<input name="subcampaignvalid" value="<s:property value="subcampaignvalid"/>"/>
		                        	<input name="level" value="<s:property value="level"/>"/>
		                        	<input name="checkFlag" value="<s:property value="checkFlag"/>"/>
		                        	<input name="activitycode" value="<s:property value="activitycode"/>"/>
		                        	<input name="planDiscountPrice" value="<s:property value="planDiscountPrice"/>"/>
		                        	<input name="actualDiscountPrice" value=""/>
		                        	<input name="activityType" value="<s:property value="activityType"/>"/>
		                        	<input name="descriptionDtl" value="<s:property value="descriptionDtl"/>"/>
		                        	<input name="unicode" value="<s:property value="unicode"/>"/>
		                        	<input name="barcode" value="<s:property value="barcode"/>"/>
		                        	<input name="productNumber" value="<s:property value="productNumber"/>"/>
		                        	<input name="ZGQFlag" value='<s:property value="ZGQFlag"/>'/>
		                        	<input name="fullFlag" value='<s:property value="fullFlag"/>'/>
		                        	<input name="couponFlag" value='<s:property value="couponFlag"/>'/>
		                        	<input name="unitPoint" value='<s:property value="unitPoint"/>'/>
		                        	<input name="maxPoint" value='<s:property value="maxPoint"/>'/>
		                        	<input name="computePoint" value=""/>
		                        </td>
		                    </tr>
	                    </s:iterator>
                    </tbody>
                </table>
            </div>
            <div class="explainbox">
            	<h2><span class="leftline3"></span><s:text name="PRM74.title5"></s:text> </h2>
                <p></p>
            </div>           
        </div>
        <div class="rightbox">
        	<div class="itembox">
                <div class="formtitle"><h1><span class="leftline4"></span><s:text name="PRM74.shoppingCartDetail"></s:text></h1></div>
                <div class="tablebox gift">
                    <table>
                        <tr>
                            <th><s:text name="PRM74.productBarcode"></s:text> </th>
                            <th><s:text name="PRM74.name"></s:text> </th>
                            <th><s:text name="PRM74.quantity"></s:text> </th>
                            <th><s:text name="PRM74.price"></s:text> </th>
                        </tr>
                        <s:iterator value="%{result_map.shoppingcart_list}" id="cart">
                        <tr onmouseover="this.className='tron2'" onmouseout="this.className=''">
                            <td><s:property value="unitCode"/></td>
                            <td><s:property value="nameTotal"/></td>
                            <td><s:property value="quantity"/></td>
                            <td><s:property value="salePrice"/></td>
                        </tr> 
                        </s:iterator>
                    </table>
                </div> 
            </div>
        	<div class="itembox none">
                <div class="formtitle"><h1><span class="leftline2"></span><s:text name="PRM74.title3"></s:text></h1></div>
                <div class="scanningbox"><label><s:text name="PRM74.title4"></s:text></label><input class="jh_input" id="productSearchStr" onkeypress="if(event.keyCode==13) {BINOLSSPRM74.pbAfterSelectFun();return false;}"/></div>
                <div class="tablebox gift">
                    <table id="activityProTable">
                    	<thead>
	                        <tr>
	                        	<th></th>
	                            <th><s:text name="PRM74.productBarcode"></s:text> </th>
	                            <th><s:text name="PRM74.name"></s:text> </th>
	                            <th><s:text name="PRM74.giftAmount"></s:text> </th>
	                            <th><s:text name="PRM74.giftPrice"></s:text> </th>
	                        </tr>
                    	</thead>
                       <s:iterator value="%{result_map.product_map}"   var="product">
                       	<tbody id='<s:property value="#product.maincode"/>' style="display:none;">
	                        <s:iterator value="#product.product_list" var="productDetail" status="i">
		                        <tr onmouseover="this.className='tron3'" onmouseout="this.className=''">
		                            <td>
			                        	<div class="checkbox" >
			                        		<input onclick="BINOLSSPRM74.changeProductCheck(this);" name="checkbox-b<s:property value='%{#i.index}'/>" type="checkbox" class="regular-checkbox" id="checkbox-b<s:property value='%{#i.index}'/>">
			                            	<label for="checkbox-b<s:property value='%{#i.index}'/>"><span></span></label>
			                            </div>
                        			</td>
		                            <td><s:property value="#productDetail.unicode"/></td>
		                            <td><s:property value="#productDetail.proname"/></td>
		                            <td><s:property value="#productDetail.quantity"/></td>
		                            <td><s:property value="#productDetail.price"/></td>
		                            <td style="display:none;">
		                        		<input name="quantity" style="display:none;" value='<s:property value="#productDetail.quantity"/>'/>
		                        		<input name="price" style="display:none;" value='<s:property value="#productDetail.price"/>'/>
		                        		<input name="salePrice" style="display:none;" value='<s:property value="#productDetail.salePrice"/>'/>
		                        		<input name="proname" style="display:none;" value='<s:property value="#productDetail.proname"/>'/>
		                        		<input name="barcode" style="display:none;" value='<s:property value="#productDetail.barcode"/>'/>
		                        		<input name="unicode" style="display:none;" value='<s:property value="#productDetail.unicode"/>'/>
		                            </td>
		                        </tr>
	                        </s:iterator>
	                        	<tr style="display:none;">
	                        		<td>
	                        			<input name="priceTotal" value="">
		                        		<input name="salePriceTotal" value="">
	                        		</td>
	                        	</tr>
                       	</tbody>
                       </s:iterator>
                    </table>
                </div> 
            </div>
        </div>
    </div>
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
<div class="mask" id="msg" style="display:none;">
    <span class="close" id="show2" onclick='BINOLSSPRM74.showhidediv("msg");return false;'></span>
	<div class="openbox">
        <div class="tablebox open">
            <table>
                <tr>
                    <th><s:text name="PRM74.activityType"></s:text> </th>
                    <th><s:text name="PRM74.activityName"></s:text> </th>
                    <th><s:text name="PRM74.promotionInstruction"></s:text> </th>
                    <th><s:text name="PRM74.discountContent"></s:text> </th>
                    <th><s:text name="PRM74.endTime"></s:text> </th>
                </tr>
                <s:iterator value="%{result_map.allActivity}" id="allAct" status="i">
                <tr onmouseover="this.className='tron2'" onmouseout="this.className=''">
                    <td>
                    	<s:if test="RuleType == '2'">
                        		<s:text name="PRM74.activity1"></s:text> 
                       	</s:if>
                       	<s:else>
                       		<s:text name="PRM74.activity2"></s:text> 
                       	</s:else>
                    </td>
                    <td><s:property value="RuleName"/></td>
                    <td><s:property value="DescriptionDtl"/></td>
                    <td></td>
                    <td><s:property value="ActivityToDate"/></td>
                </tr> 
                </s:iterator>
            </table>
        </div>
    </div>
</div>
<div class="windowbox2" id="err" style="display:none;">
	<span class="close2" id="show3" onclick='BINOLSSPRM74.showhidediv("err");return false;'></span>
	<div class="windowsubbox">
		<div class="error" id="showResultMessage"></div>
		<div class="windowsbtnbox">
			<%-- <button class="jh_btn2" onclick='BINOLSSPRM74.showhidediv("err");return false;'><s:text name="PRM74.conform"></s:text></button> --%>
			<button class="jh_btn2" onfocus="this.blur()" onclick='BINOLSSPRM74.showhidediv("err");return false;'><span class="iconbg icon_1"></span><span class="btn_text"><s:text name="PRM74.conform"></s:text> </span></button>
			<%--
			<button class="btn_qx"><s:text name="PRM74.cancel"></s:text></button>
			 --%>
		</div>
	</div>
</div>
<div class="windowbox" id="dzq" style="display:none;">
	<span class="close2" id="show4" onclick='BINOLSSPRM74.showhidediv("dzq");return false;'></span>
	<div class="windowsubbox">
		<div class="title"><s:text name="PRM74.couponTitle"/></div>
		<div class="dzqbox">
			<%-- <div class="dzqsubbox"><label><s:text name="PRM74.couponActivity"/></label><span id="couponName2"></span></div> --%>
			<s:if test="result_map.main_map.ML.isEmpty() ||null == result_map.main_map.ML">
				<div class="dzqsubbox"><label><s:text name="PRM74.memPhone"/></label><span id="mobileNo1"><input maxlength="11" class="dzq_input"  placeholder='<s:property value="PRM74.phoneCheckCodeTitle"/>'/></span></div>
			</s:if>
			<s:else>
				<div class="dzqsubbox"><label><s:text name="PRM74.couponMemberCode"/></label><span><s:property value="%{result_map.main_map.MC}"/></span></div>
				<div class="dzqsubbox"><label><s:text name="PRM74.memPhone"/></label><span id="mobileNo1"><s:property value="%{result_map.main_map.MP}"/></span></div>
			</s:else>
			<div class="dzqsubbox"><label><s:text name="PRM74.couponCode"/> </label><span id="couponCode1"><%-- <input class="dzq_input" placeholder='<s:property value="PRM74.couponCodeTitle"/>'/> --%></span></div>
			<div class="dzqsubbox"><label><s:text name="PRM74.couponCheckCode"/></label><span><input id="password1" class="dzq_input" placeholder='<s:property value="PRM74.couponCheckCodeTitle"/>'/></span></div>
			<div class="windowsbtnbox">
				<button class="btn_qr" onclick="BINOLSSPRM74.commitCoupon();return false;"><s:text name="PRM74.conform"/></button>
				<button class="btn_qx" onclick='BINOLSSPRM74.showhidediv("dzq");return false;'><s:text name="PRM74.cancel"/></button>
			</div>
		</div>
	</div>
</div>
<div class="windowbox" id="dzq2" style="display:none;">
	<span class="close2" id="show4" onclick='BINOLSSPRM74.showhidediv("dzq2");return false;'></span>
	<div class="windowsubbox">
		<div class="title"><s:text name="PRM74.couponTitle"/></div>
		<div class="dzqbox">
			<%-- <div class="dzqsubbox"><label><s:text name="PRM74.couponActivity"/></label><span id="couponName2"></span></div> --%>
			<s:if test="result_map.main_map.ML.isEmpty() ||null == result_map.main_map.ML">
				<div class="dzqsubbox"><label><s:text name="PRM74.memPhone"/></label><span><input id="mobileNo" maxlength="11" class="dzq_input" onblur="BINOLSSPRM74.checkMobile(this);return false;"  placeholder='<s:property value="PRM74.phoneCheckCodeTitle"/>'/></span></div>
			</s:if>
			<s:else>
				<div class="dzqsubbox"><label><s:text name="PRM74.couponMemberCode"/></label><span><s:property value="%{result_map.main_map.MC}"/></span></div>
				<div class="dzqsubbox"><label><s:text name="PRM74.memPhone"/></label><span id="mobileNo"><s:property value="%{result_map.main_map.MP}"/></span></div>
			</s:else>
			<div class="dzqsubbox"><label><s:text name="PRM74.couponCode"/> </label><span id="couponCode2"><%-- <input class="dzq_input" placeholder='<s:property value="PRM74.couponCodeTitle"/>'/> --%></span></div>
			<div class="dzqsubbox"><label><s:text name="PRM74.couponCheckCode"/></label><span><input id="password" class="dzq_input" placeholder='<s:property value="PRM74.couponCheckCodeTitle"/>'/></span></div>
			<div class="windowsbtnbox">
				<button class="btn_qr" onclick="BINOLSSPRM74.checkCouponTo();return false;"><s:text name="PRM74.conform"/></button>
				<button class="btn_qx" onclick='BINOLSSPRM74.showhidediv("dzq2");return false;'><s:text name="PRM74.cancel"/></button>
			</div>
		</div>
	</div>
</div>
<div class="windowbox3" id="jfdh" style="display:none;">
	<span class="close2" id="jfdhClose"></span>
	<div class="windowsubbox">
		<div class="title"><s:text name="PRM74.pointTitle"/> </div>
		<div class="dzqbox">
			<%-- <div class="dzqsubbox"><label><s:text name="PRM74.couponActivity"/></label><span id="couponName2"></span></div> --%>
			<div class="dzqsubbox"><label class="w1"><s:text name="PRM74.jfdxRule"/></label><span id="pointRule"></span></div>
			<div class="dzqsubbox"><label class="w1"><s:text name="PRM74.couponMemberCode"/></label><span><s:property value="%{result_map.main_map.MC}"/></span></div>
			<div class="dzqsubbox"><label class="w1"><s:text name="PRM74.nowPoint"/></label><span><s:property value="%{result_map.main_map.MPOINT}"/></span></div>
			<div class="dzqsubbox"><label class="w1"><s:text name="PRM74.maxPoint"/></label><span id="maxPoint"></span></div>
			<div class="dzqsubbox"><label class="w1"><s:text name="PRM74.unitPoint"/></label><span id="unitPoint"></span></div>
			<div class="dzqsubbox"><label class="w1"><s:text name="PRM74.usePoint"/></label><span><input id="usePoint" class="dzq_input"/></span></div>
			<div class="windowsbtnbox">
				<button class="btn_qr" id="jfdhConfirm"><s:text name="PRM74.conform"/></button>
				<button class="btn_qx" id="jfdhCancel"><s:text name="PRM74.cancel"/></button>
			</div>
		</div>
	</div>
</div>

</body>
</html>

</s:i18n>
