<%-- 新增菜单组 --%>
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
			formId: 'operateForm',	
			rules: {
				startDate: {required:true,dateValid:true},    // 开始日期
				endDate: {required:true,dateValid:true},   // 结束日期
				menuGrpName: {required : true,maxlength : 50},
				machineType: {required : true}
			}	
		});
	});
</script>
<div id="actionResultDisplay"></div>
<form id="operateForm">
    <s:i18n name="i18n.mo.BINOLMOPMC01">
        <div>
	  		<table style="margin:auto; width:100%;" class="detail">
	  		<tr>
		  		<th><s:text name="PMC01_menuGrpName"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		  		<td><span><s:textfield name="menuGrpName" cssClass="text" maxlength="50"/></span></td>
	  		</tr>
	  		<tr>
      			<!-- 生效日期-->
      			<th><s:text name="PMC01_validDate"></s:text><span class="highlight">*</span></th>
      			<td class="date">
	      			<span><s:textfield id="startDate" name="startDate" cssClass="date" cssStyle="width:80px;"/></span>
		            <span>&nbsp;-&nbsp;</span> 
		            <span><s:textfield id="endDate" name="endDate" cssClass="date" cssStyle="width:80px;"/></span>
    			</td>
      		</tr>
      		<tr>
	  			<s:text name="PMC01_select" id="PMC01_select"/>
		  		<th><s:text name="PMC01_machineType"/><span class="highlight">*</span></th>
		  		<td><span><s:select name="machineType" 
                      list='#application.CodeTable.getCodes("1284")' 
                      listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PMC01_select}" cssStyle="width:150px;"/></span></td>
	  		</tr>
	  		</table>
  		</div>
    </s:i18n>
</form>