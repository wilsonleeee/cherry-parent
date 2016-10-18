<%-- 生成代理商优惠券弹出框--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	//节日
	var holidays = '${holidays }';
	$('#startDate').cherryDate({
	    holidayObj: holidays,
	    beforeShow: function(input){
	        var value = $('#endDate').val();
	        return [value,'maxDate'];
	    }
	});
	$('#endDate').cherryDate({
	    holidayObj: holidays,
	    beforeShow: function(input){
	        var value = $('#startDate').val();
	        return [value,'minDate'];
	    }
	});

	$(function(){
	    // 表单验证初期化
		cherryValidate({
			formId: 'createForm',
			rules: {
				batchName:{required : true,maxlength : 30},
				createBatchCode: {required:true,maxlength : 25},    // 生成优惠券批次号
				batchCouponCount: {required:true,integerValid:true},   // 批次券数，非负整数
				startDate: {required:true,dateValid:true},    // 开始日期
				endDate: {required:true,dateValid:true},   // 结束日期
				parValue: {pointValid:[10,2]},   // 正数
				useTimes: {integerValid:true},   // 正整数
				amountCondition: {maxlength : 100}
			}	
		});
	});
	
</script>
<div id="actionResultDisplay"></div>
<form id="createForm">
    <s:i18n name="i18n.bs.BINOLBSEMP07">
        <div>
	  		<table style="margin:auto; width:100%;" class="detail">
	  		<tr>
	  			<!-- 批次名称-->
		  		<th><s:text name="EMP07_batchName"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		  		<td><span><s:textfield name="batchName" cssClass="text" maxlength="30"/></span></td>
	  		</tr>
	  		<tr>
	  			<!-- 批次号-->
		  		<th><s:text name="EMP07_batchCode"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		  		<td><span><s:textfield name="createBatchCode" cssClass="text" maxlength="25"/></span></td>
	  		</tr>
	  		<tr>
      			<!-- 批次券数-->
      			<th><s:text name="EMP07_batchCouponCount"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
      			<td><span><s:textfield name="batchCouponCount" cssClass="text" maxlength="10"/></span></td>
      		</tr>
      		<tr>
      			<!-- 有效日期-->
      			<th><s:text name="EMP07_validDate"></s:text><span class="highlight">*</span></th>
      			<td class="date">
	      			<span><s:textfield id="startDate" name="startDate" cssClass="date" cssStyle="width:80px;"/></span>
		            <span>&nbsp;-&nbsp;</span> 
		            <span><s:textfield id="endDate" name="endDate" cssClass="date" cssStyle="width:80px;"/></span>
    			</td>
      		</tr>
      		<tr>
	  			<!-- 优惠券类型-->
		  		<th><s:text name="EMP07_couponType"/></th>
		  		<td>
		  			<select id="couponType" name="couponType">
		  				<option value=""><s:text name="global.page.select"/></option> 
                    	<option value="A"><s:text name="EMP07_couponTypeA"/></option>
                    	<option value="B"><s:text name="EMP07_couponTypeB"/></option>
                    </select>
                </td>
	  		</tr>
      		<tr>
	  			<!-- 优惠券面值-->
		  		<th><s:text name="EMP07_parValue"/></th>
		  		<td><span><s:textfield name="parValue" cssClass="text" maxlength="10"/></span></td>
	  		</tr>
	  		<tr>
	  			<!-- 优惠券可使用次数-->
		  		<th><s:text name="EMP07_useTimes"/></th>
		  		<td><span><s:textfield name="useTimes" cssClass="text" maxlength="10"/></span></td>
	  		</tr>
	  		<tr>
      			<!-- 优惠券金额条件-->
      			<th><s:text name="EMP07_amountCondition"></s:text></th>
      			<td><span><s:textfield name="amountCondition" cssClass="text" maxlength="100"/></span></td>
      		</tr>
      		<tr>
      			<!-- 代理商 -->
		  		<th><s:text name="EMP07_baName"/></th>
		  		<td><span>
		  			<input id="selectMode0" name="selectMode" type="radio" value="0"/><s:text name="EMP07_all"></s:text>
           			<input id="selectMode1" name="selectMode" type="radio" value="1" checked/><s:text name="EMP07_allow"></s:text>
           			<%-- <input id="selectMode2" name="selectMode" type="radio" value="2"/><s:text name="EMP07_forbidden"></s:text> --%>
           		</span></td>
	  		</tr>
	  		</table>
  		</div>
    </s:i18n>
</form>