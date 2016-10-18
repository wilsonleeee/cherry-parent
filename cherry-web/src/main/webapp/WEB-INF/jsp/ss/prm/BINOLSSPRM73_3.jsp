<%-- 批量生成优惠券弹出框--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript">
	$(function(){
	    // 表单验证初期化
		cherryValidate({
			formId: 'batchForm',
			rules: {
				batchCount: {required: true, number:true, min:1}
			}	
		});
	});
	
</script>
<div class="hide">
	<s:url id="importCoupon_Url" action="BINOLSSPRM73_importCouponByMemberPhone"/>
	<a id="importCouponUrl" href="${importCoupon_Url}"></a>
	<s:url id="memberDialogInit_Url" action="BINOLSSPRM73_memberInit"/>
	<a id="memberDialogInitUrl" href="${memberDialogInit_Url}"></a>
</div>
<div id="saveActionResult"></div>
<form id="batchForm">
    <s:i18n name="i18n.ss.BINOLSSPRM73">
        <div>
        	<div id="actionResultDisplay"></div>
		      	<div id="errorDiv" class="actionError" style="display:none;">
			      <ul>
			         <li><span id="errorSpan"></span></li>
			      </ul>			
	         </div>
	  		<table style="margin:auto; width:100%;" class="detail" id="sendCondition">
	  		<tr style="height: 35px;">
		  		<th>优惠券规则</th>
		  		<td><span><strong id="ruleNameSpan"><s:property value='ruleName'/></strong><input id="ruleCode_3" class="hide"  value='<s:property value="ruleCode"/>'></span></td>
	  		</tr>
	  		<tr style="height: 35px;">
		  		<th>生成方式</th>
		  		<td><span>
		  		<select  id="batchMode_3" name="batchMode" class="left" style="width:100px;" onchange="binolssprm73.changeBatchMode(this);return false;">
					<option value='0'>不限会员</option>
					<option value='1'>导入会员手机号</option>
				</select>
				<!-- <input type="hidden" name="batchMode" value="0"/> -->
		  		</span></td>
	  		</tr>
	  		<tr style="height: 35px;" class="hide">
	  			<td></td>
		  		<td>
		  			<div id="counterKbn1">
					    <input class="input_text" type="text" id="couponPathExcel" name="couponPathExcel"/>
					    <input type="button" value="<s:text name="global.page.browse"/>"/>
					    <input class="input_file" type="file" name="upExcel" id="couponUpExcel" size="33" onchange="couponPathExcel.value=this.value;return false;" /> 
					    <input type="button" value="导入" onclick="binolssprm73.couponUpload('1')"/>
			  				<img id="couponloading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
			  				<s:radio name="upMode" list="#application.CodeTable.getCodes('1398')" 
				                    		listKey="CodeKey" listValue="Value" value="1" id="counterUpMode"></s:radio>
			  				<br/>
			  				<a href="/Cherry/download/批量生成券导入模板(会员手机号).xls">下载模板</a>
			  				<a href="javascript:void(0)" style="margin-left:50px;" onclick="binolssprm73.couponDialog('3')">导入结果一览</a>
					</div>
		  		</td>
	  		</tr>
	  		<tr style="height: 35px;">
      			<th>本批次券数</th>
      			<td><span><s:textfield name="batchCount" cssClass="text" maxlength="10" value=""/></span></td>
      		</tr>
	  		</table>
  		</div>
    </s:i18n>
</form>

     <div class="center clearfix" id="pageButtonDialog" style="margin-top:50px;">
       <button class="confirm" id="confirmBtn">
       	<span class="ui-icon icon-confirm"></span>
       	<%-- 确定按钮 --%>
       	<span class="button-text">确定</span>
       </button>
	   <button class="close" onclick="dialogClose();"><span class="ui-icon icon-close"></span>
         <%-- 关闭 --%>
         <span class="button-text"><s:text name="global.page.close"/></span>
        </button>
        <img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
     </div>