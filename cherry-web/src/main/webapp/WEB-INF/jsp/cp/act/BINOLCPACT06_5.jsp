<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/cp/act/BINOLCPACT06.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
//节日
var holidays = '${holidays }';
$('#batchFromTime').cherryDate({
	holidayObj: holidays,
	beforeShow: function(input){
		var value = $('#batchToTime').val();
		return [value,'maxDate'];
	}
});
$('#batchToTime').cherryDate({
	holidayObj: holidays,
	beforeShow: function(input){
		var value = $('#batchFromTime').val();
		return [value,'minDate'];
	}
});
</script>
<s:i18n name="i18n.cp.BINOLCPACT06">
<%-- ================== 信息提示区 START ======================= --%>
<div id="actionDisplay">
	<div id="errorDiv" class="actionError box4" style="display:none;">
		<ul>
		    <li><span id="errorSpan"></span></li>
		</ul>			
	</div>
	<div id="successDiv" class="actionSuccess box4" style="display:none;">
		<ul class="actionMessage">
	  		<li><span id="successSpan"></span></li>
	 	</ul>
	</div>
</div>
<div id="errorMessage"></div>
<%-- ================== 信息提示区   END  ======================= --%>
<form id="editForm" method="post">
<div class="" id="confirmInitBody">
<s:hidden name="billNum" value="%{editInfoMap.billNum}"></s:hidden>
 <div class="actionError box4" id="errorEditDiv" style="color:#000;">
	<ul>
	  <li>
	  <span id="errorEditSpan">
	  <s:text name="ACT06_editMessageBill"/>
	  <span style="font-size:15px;color:#ff6d06;" > <s:text name="ACT06_batchEditBill"/></span>
	  <s:text name="ACT06_optionTip"/><span style="font-size:15px;color:#ff6d06;" ><s:text name="ACT06_editTip"/></span>
	  </span>
	  </li>
	</ul>			
 </div>
 <div style="margin-top:0px;" class="box4">
   <div class="box4-header"> <strong><span style="color:#CC6600;font-size:13px;" id="messageCondition"><s:text name="ACT06_searcCondition"/>
	<span class=""  style="position: relative; margin-left:20px;color:#404040;"  class="ui-widget"> 
	 <s:text name="ACT06_conditionBill"/><s:text name="ACT06_totalCount"/>&nbsp;<strong><span id="count" style="font-size:16px;" class="green"><s:property value="%{editInfoMap.billNum}"/></span></strong>&nbsp;<s:text name="ACT06_totalRecord"/>
     <input type="hidden" name="countId" id="countId" value="1"/>
	</span>
   </span></strong></div>
     <div class="box4-content clearfix " id="batchConditDiv"></div>
 </div>
 <div style="margin-top:0px;" class="box4">
   <div class="box4-header"> <strong><span style="color:#CC6600;font-size:13px;" id="editCondition"><s:text name="ACT06_editContent"/></span></strong>
   	 <a style="margin:2px 5px 0 5px;" class="add right" onclick="BINOLCPACT06.openCampDialog(this)">
		 <span style="left: 0; position: absolute;" class="ui-icon icon-add"></span>
		 <span style="position: static;" class="button-text"><s:text name="ACT06_addContent" /></span> 
	 </a>
   </div>
    <div class="box4-content clearfix">
   <ul class="sortable">
   <li style="margin:5px 0px;" id="1">
   <table cellspacing="0" cellpadding="0" style="width:100%;">
     <tbody>
       <tr>
          <th style="width:30%;">
          	<span class="ui-icon icon-arrow-crm"></span>
          	<span><s:text name="ACT06_counterCode" /><span class="highlight">*</span></span>
          </th>
          <td style="width:70%;">
			<span>
				<select onchange="BINOLCPACT06.changeReferType(this,'3');" style="width:140px;" class="left valid" name="counterType">
				<option value="1" selected=""><s:text name="ACT06_resCounter" /></option>
				<option value="2"><s:text name="ACT06_shipCounter" /></option>
				<option value="0"><s:text name="ACT06_ConsumCounter" /></option>
				</select>
				<div class="left hide" id="referType_3_0" >
					<span><input id="batchCounter"  class="text" name="batchCounter"  style="width: 120px;"></span>
				</div>
			</span>
		  	<a class="delete right" onclick="BINOLCPACT06.removeButton(this);" style="display: block; margin-top:3px;">
				<span class="ui-icon icon-delete"></span>
				<span class="button-text"><s:text name="ACT06_delete" /></span>                    
			</a>
          </td>
        </tr>
    </tbody>
    </table>
    </li>
    <li  style="margin: 5px 0px;" id="2">
    <table cellspacing="0" cellpadding="0" style="width:100%;">
     <tbody>
       <tr>
          <th style="width:30%;">
          	<span class="ui-icon icon-arrow-crm"></span>
          	<span><s:text name="ACT06_getFromTime" /><span class="highlight">*</span></span>
          </th>
          <td style="width:70%;">
			<span>
				<select onchange="BINOLCPACT06.changeReferType(this,'1');" style="width:140px;" class="left valid" name="referFromType">
				<option value="0" selected=""><s:text name="ACT06_specDate" /></option>
				<option value="1"><s:text name="ACT06_adjustDate" /></option>
				</select>
				<div class="left " id="referType_1_0">
			      	<span><s:textfield name="batchFromTime"  cssStyle="width: 120px;" cssClass="date"/></span>
				</div>
				<div class="left hide" id="referType_1_1" >
					<select style="width:55px;" name="referFromParam">
						<option value="2"><s:text name="ACT06_delayed" /></option>
						<option value="1"><s:text name="ACT06_advance" /></option>
					</select>
					<span style="float:none;"><input name="referFromValue" maxlength="6" class="number"></span>
					<select style="width:55px;" name="referFromDate">
						<option value="1"><s:text name="ACT06_day" /></option>
						<option value="2"><s:text name="ACT06_month" /></option>
					</select>
				</div>
			</span>
		  	<a class="delete right" onclick="BINOLCPACT06.removeButton(this);" style="display: block; margin-top:3px;">
				<span class="ui-icon icon-delete"></span>
				<span class="button-text"><s:text name="ACT06_delete" /></span>                    
			</a>
          </td>
        </tr>
    </tbody>
    </table>
    </li>
    <li  style="margin: 5px 0px;" id="3">
    <table cellspacing="0" cellpadding="0" style="width:100%;">
     <tbody>
       <tr>
          <th style="width:30%;">
          	<span class="ui-icon icon-arrow-crm"></span>
          	<span><s:text name="ACT06_getToTime" /><span class="highlight">*</span></span>
          	
          </th>
          <td style="width:70%;">
			<span>
				<select onchange="BINOLCPACT06.changeReferType(this,'2');" style="width:140px;" class="left valid" name="referToType">
				<option value="0" selected=""><s:text name="ACT06_specDate" /></option>
				<option value="1"><s:text name="ACT06_adjustDate" /></option>
				</select>
				<div class="left " id="referType_2_0">
			      	<span><s:textfield name="batchToTime"  cssStyle="width: 120px;" cssClass="date"/></span>
				</div>
				<div class="left hide" id="referType_2_1">
					<select style="width:55px;" name="referToParam">
						<option value="2"><s:text name="ACT06_delayed" /></option>
						<option value="1"><s:text name="ACT06_advance" /></option>
					</select>
					<span style="float:none;"><input name="referToValue" maxlength="6" class="number"></span>
					<select style="width:55px;" name="referToDate">
						<option value="1"><s:text name="ACT06_day" /></option>
						<option value="2"><s:text name="ACT06_month" /></option>
					</select>
				</div>
			</span>
		  	<a class="delete right" onclick="BINOLCPACT06.removeButton(this);" style="display: block; margin-top:3px;">
				<span class="ui-icon icon-delete"></span>
				<span class="button-text"><s:text name="ACT06_delete" /></span>                    
			</a>
          </td>
        </tr>
    </tbody>
    </table>
    </li>
    </ul>
</div>
 </div>
</div>
</form>
<div id="dataTable_processing_2" class="dataTables_processing"  style="text-algin:left;"></div>
<div id="PopDialogMain">
	<div id="PopTimeDialog" class="hide">
	   	<table id="PopTime_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
	   		<thead>
		        <tr>
		           <th><s:text name="ACT06_select" /></th>
		           <th><s:text name="ACT06_modifyContent" /></th>
		        </tr>
		    </thead>
	    	<tbody>
		    	<tr class="center">
					<td><input type="checkbox" class="checkbox" value='1'/></td>
					<td><span><s:text name="ACT06_counterCode" /></span></td>
				</tr>
				<tr class="center">
					<td><input type="checkbox" class="checkbox" value='2'/></td>
					<td><span><s:text name="ACT06_getFromTime" /></span></td>
				</tr>
				<tr class="center">
					<td><input type="checkbox" class="checkbox" value='3'/></td>
					<td><span><s:text name="ACT06_getToTime" /></span></td>
				</tr>
			</tbody>
	   	</table>
	   	<hr>
	  	<span id="global_page_ok" style="display:none"><s:text name="global.page.ok" /></span>
		<span id="PopTimeTitle" style="display:none"><s:text name="ACT06_modifyContent" /></span>
	</div>
</div>
<span id="message_Error1" class="hide"><s:text name="ACT06_error1"/></span>
<span id="message_Error2" class="hide"><s:text name="ACT06_error2"/></span>
<span id="message_Error3" class="hide"><s:text name="ACT06_error3"/></span>
<span id="message_Error4" class="hide"><s:text name="ACT06_error4"/></span>
<span id="message_Error5" class="hide"><s:text name="ACT06_error5"/></span>
<span id="message_Error9" class="hide"><s:text name="ACT06_error6"/></span>
<span id="message_Error10" class="hide"><s:text name="ACT06_error7"/></span>
<span id="message_Error11" class="hide"><s:text name="ACT06_error8"/></span>
<span id="message_Error12" class="hide"><s:text name="ACT06_error9"/></span>
<span id="message_Error13" class="hide"><s:text name="ACT06_error10"/></span>
<span id="message_Error14" class="hide"><s:text name="ACT06_error11"/></span>
<span id="message_Error15" class="hide"><s:text name="ACT06_error12"/></span>
<span id="message_Error16" class="hide"><s:text name="ACT06_error13"/></span>
</s:i18n>