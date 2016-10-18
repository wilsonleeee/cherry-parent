<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popMemSearch.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var index = '${index}';
	$('#joinDateStart'+index).cherryDate({
		beforeShow: function(input){
			var value = $('#joinDateEnd'+index).val();
			return [value,'maxDate'];
		}
	});
	$('#joinDateEnd'+index).cherryDate({
		beforeShow: function(input){
			var value = $('#joinDateStart'+index).val();
			return [value,'minDate'];
		}
	});
	$('#saleTimeStart'+index).cherryDate({
		beforeShow: function(input){
			var value = $('#saleTimeEnd'+index).val();
			return [value,'maxDate'];
		}
	});
	$('#saleTimeEnd'+index).cherryDate({
		beforeShow: function(input){
			var value = $('#saleTimeStart'+index).val();
			return [value,'minDate'];
		}
	});
	$('#notSaleTimeStart'+index).cherryDate({
		beforeShow: function(input){
			var value = $('#notSaleTimeEnd'+index).val();
			return [value,'maxDate'];
		}
	});
	$('#notSaleTimeEnd'+index).cherryDate({
		beforeShow: function(input){
			var value = $('#notSaleTimeStart'+index).val();
			return [value,'minDate'];
		}
	});
	$('#participateTimeStart'+index).cherryDate({
		beforeShow: function(input){
			var value = $('#participateTimeEnd'+index).val();
			return [value,'maxDate'];
		}
	});
	$('#participateTimeEnd'+index).cherryDate({
		beforeShow: function(input){
			var value = $('#participateTimeStart'+index).val();
			return [value,'minDate'];
		}
	});
	$('#levelAdjustDayStart'+index).cherryDate({
		beforeShow: function(input){
			var value = $('#levelAdjustDayEnd'+index).val();
			return [value,'maxDate'];
		}
	});
	$('#levelAdjustDayEnd'+index).cherryDate({
		beforeShow: function(input){
			var value = $('#levelAdjustDayStart'+index).val();
			return [value,'minDate'];
		}
	});
	
	if ($('#lastSaleDateStart'+index).length > 0) {
		$('#lastSaleDateStart'+index).cherryDate({
			beforeShow: function(input){
				var value = $('#lastSaleDateEnd'+index).val();
				return [value,'maxDate'];
			}
		});
		$('#lastSaleDateEnd'+index).cherryDate({
			beforeShow: function(input){
				var value = $('#lastSaleDateStart'+index).val();
				return [value,'minDate'];
			}
		});
	}
	
	if ($('#firstStartDay'+index).length > 0) {
		$('#firstStartDay'+index).cherryDate({
			beforeShow: function(input){
				var value = $('#firstEndDay'+index).val();
				return [value,'maxDate'];
			}
		});
		$('#firstEndDay'+index).cherryDate({
			beforeShow: function(input){
				var value = $('#firstStartDay'+index).val();
				return [value,'minDate'];
			}
		});
	}
	
	if ($('#clubJoinTimeStart').length > 0) {
		$('#clubJoinTimeStart').cherryDate({
			beforeShow: function(input){
				var value = $('#clubJoinTimeEnd').val();
				return [value,'maxDate'];
			}
		});
		$('#clubJoinTimeEnd').cherryDate({
			beforeShow: function(input){
				var value = $('#clubJoinTimeStart').val();
				return [value,'minDate'];
			}
		});
	}
});
</script>
<s:set value="index" var="index"></s:set>
<div>
<div id="memSearchRequestDiv">

	<s:hidden name="organizationInfoId"></s:hidden>
	<s:hidden name="brandInfoId"></s:hidden>
	<s:hidden name="orgCode"></s:hidden>
	<s:hidden name="brandCode"></s:hidden>
	<s:hidden name="disableCondition"></s:hidden>
	<s:hidden name="privilegeFlag"></s:hidden>
	<s:hidden name="userId"></s:hidden>
	<s:hidden name="clubMod"></s:hidden>
    <s:text name="global.page.select" id="select_default"/>          
	<div class="box2 box2-active">       
      <div class="box2-header clearfix">
      	<strong class="active left"><s:text name="global.page.memBaseInfo" /></strong>
		<s:url action="BINOLCM33_searchMemInit" id="searchMemInitUrl"></s:url>
		<a onClick="popmemsearch.searchMemInit('${searchMemInitUrl }', this);return false;" class="add right">
		  <span class="ui-icon icon-search" style="margin-left:2px;"></span>
		  <span class="button-text"><s:text name="global.page.searchActObject" /></span>
		</a>
        <s:url action="BINOLCM33_addInit" id="addInitUrl"></s:url>
        <a onClick="popmemsearch.addSearchRequestInit('${addInitUrl }', this);return false;" class="add right">
          <span class="ui-icon icon-save-s" style="margin-left:2px;"></span>
          <span class="button-text"><s:text name="global.page.saveCondition" /></span>
        </a>
		<s:url action="BINOLCM33_searchInit" id="searchInitUrl"></s:url>
		<a onClick="popmemsearch.searchInit('${searchInitUrl }', this);return false;" class="add right">
		  <span class="ui-icon icon-search" style="margin-left:2px;"></span>
		  <span class="button-text"><s:text name="global.page.selHisCondition" /></span>
		</a>
		<a class="delete right" onclick="popmemsearch.resetCondition(this, '${index}');return false;">
	      <span class="ui-icon icon-disable" style="margin-left:2px;"></span>
	      <span class="button-text"><s:text name="global.page.resetCondition"></s:text></span>
	 	</a>
      </div>
      <div class="box2-content terminal">
		<table class="detail" cellpadding="0" cellspacing="0" style="margin-bottom:0px;">
		  <tbody>
			<tr>
			  <td colspan="4" style="line-height:25px;">
			    <span class="ui-widget breadcrumb" style="margin-right:50px;">
			    <s:if test="%{disableConditionMap.addrNotEmpty != null}">
			    <input type="checkbox" name="addrNotEmpty" value="1" <s:if test="%{addrNotEmpty==1}">checked</s:if> disabled="disabled"/>
			    <s:hidden name="addrNotEmpty"></s:hidden>
			    </s:if>
			    <s:else>
			    <input type="checkbox" name="addrNotEmpty" value="1" <s:if test="%{addrNotEmpty==1}">checked</s:if>/>
			    </s:else>
			    <s:text name="global.page.addrNotEmpty" />
			    </span>
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
                <s:if test="%{disableConditionMap.telNotEmpty != null}">
                <input type="checkbox" name="telNotEmpty" value="1" <s:if test="%{telNotEmpty==1}">checked</s:if> disabled="disabled"/>
                <s:hidden name="telNotEmpty"></s:hidden>
                </s:if>
                <s:else>
                <input type="checkbox" name="telNotEmpty" value="1" <s:if test="%{telNotEmpty==1}">checked</s:if>/>
                </s:else>
                <s:text name="global.page.telNotEmpty" />
                </span>
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
                <s:if test="%{disableConditionMap.emailNotEmpty != null}">
                <input type="checkbox" name="emailNotEmpty" value="1" <s:if test="%{emailNotEmpty==1}">checked</s:if> disabled="disabled"/>
                <s:hidden name="emailNotEmpty"></s:hidden>
                </s:if>
                <s:else>
                <input type="checkbox" name="emailNotEmpty" value="1" <s:if test="%{emailNotEmpty==1}">checked</s:if>/>
                </s:else>
                <s:text name="global.page.emailNotEmpty" />
                </span>
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
			    <s:if test="%{disableConditionMap.couNotEmpty != null}">
			    <input type="checkbox" name="couNotEmpty" value="1" <s:if test="%{couNotEmpty==1}">checked</s:if> disabled="disabled"/>
			    <s:hidden name="couNotEmpty"></s:hidden>
			    </s:if>
			    <s:else>
			    <input type="checkbox" name="couNotEmpty" value="1" <s:if test="%{couNotEmpty==1}">checked</s:if>/>
			    </s:else>
			    <s:text name="global.page.couNotEmpty" />
			    </span>
                <%-- 
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
                <s:if test="%{disableConditionMap.telCheck != null}">
                <input type="checkbox" name="telCheck" value="1" <s:if test="%{telCheck==1}">checked</s:if> disabled="disabled"/>
                <s:hidden name="telCheck"></s:hidden>
                </s:if>
                <s:else>
                <input type="checkbox" name="telCheck" value="1" <s:if test="%{telCheck==1}">checked</s:if>/>
                </s:else>
                <s:text name="global.page.telCheck" />
                </span>
                --%>
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
                <s:if test="%{disableConditionMap.testType != null}">
                <input type="checkbox" name="testType" value="1" <s:if test="%{testType==1}">checked</s:if> disabled="disabled"/>
                <s:hidden name="testType"></s:hidden>
                </s:if>
                <s:else>
                <input type="checkbox" name="testType" value="1" <s:if test="%{testType==1}">checked</s:if>/>
                </s:else>
                <s:text name="global.page.memTestType" />
                </span>
              </td>
            </tr>
            <s:if test='%{#application.CodeTable.getCodes("1204") != null && !#application.CodeTable.getCodes("1204").isEmpty()}'>
            <tr>
              <th><s:text name="global.page.memType" /></th>
              <td colspan="3">
				<span>
				<s:if test="%{disableConditionMap.memType != null}">
				<s:checkboxlist list='#application.CodeTable.getCodes("1204")' listKey="CodeKey" listValue="Value" name="memType" value="%{memType}" disabled="true"></s:checkboxlist>
				<s:iterator value="memType" id="_memType">
				<s:hidden name="memType" value="%{#_memType}"></s:hidden>
				</s:iterator>
				</s:if>
				<s:else>
				<s:checkboxlist list='#application.CodeTable.getCodes("1204")' listKey="CodeKey" listValue="Value" name="memType" value="%{memType}"></s:checkboxlist>
				</s:else>
				</span>
	   		  </td>
            </tr>
            </s:if>
            <s:if test='%{"3".equals(clubMod)}'>
            <tr>
              <th><s:text name="global.page.memLevel" /></th>
              <td colspan="3">
				<span>
				<s:if test="%{disableConditionMap.memLevel != null}">
				<s:checkboxlist list="memLevelList" listKey="memberLevelId" listValue="levelName" name="memLevel" value="%{memLevel}" disabled="true"></s:checkboxlist>
				<s:iterator value="memLevel" id="_memLevel">
				<s:hidden name="memLevel" value="%{#_memLevel}"></s:hidden>
				</s:iterator>
				</s:if>
				<s:else>
				<s:checkboxlist list="memLevelList" listKey="memberLevelId" listValue="levelName" name="memLevel" value="%{memLevel}" ></s:checkboxlist>
				</s:else>
				</span>
	   		  </td>
            </tr>
            </s:if>
            <tr>
              <th><s:text name="global.page.joinDate" /></th>
              <td colspan="3">
				<s:if test="%{disableConditionMap.joinDateMode != null && disableConditionMap.joinDateMode != -1}">
				<span>
				<select name="joinDateMode" onchange="popmemsearch.selectDateMode(this);" disabled="disabled">
					<option value="-1" <s:if test="%{joinDateMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="1" <s:if test="%{joinDateMode == 1}">selected</s:if>><s:text name="global.page.curJoinDate"/></option>
					<option value="0" <s:if test="%{joinDateMode == 0}">selected</s:if>><s:text name="global.page.dynamic"/></option>
					<option value="9" <s:if test="%{joinDateMode == 9}">selected</s:if>><s:text name="global.page.custom"/></option>
				</select>
				<s:hidden name="joinDateMode"></s:hidden>
				</span>
				<span class='<s:if test="%{joinDateMode != 0}">hide</s:if>'>
				<s:text name="global.page.joinDateDes" />
				<s:textfield name="joinDateRange" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield>
				<s:select list='#application.CodeTable.getCodes("1239")' listKey="CodeKey" listValue="Value" name="joinDateUnit" cssStyle="width:auto;" disabled="true"></s:select>
				<select name="joinDateUnitFlag" style="width:auto;" disabled="disabled">
					<option value="1" <s:if test="%{joinDateUnitFlag == 1}">selected</s:if>><s:text name="global.page.joinDateUnitFlag1" /></option>
					<option value="2" <s:if test="%{joinDateUnitFlag == 2}">selected</s:if>><s:text name="global.page.joinDateUnitFlag2" /></option>
				</select>
				<s:hidden name="joinDateRange"></s:hidden>
				<s:hidden name="joinDateUnit"></s:hidden>
				<s:hidden name="joinDateUnitFlag"></s:hidden>
				</span>
				<span class='<s:if test="%{joinDateMode != 9}">hide</s:if>'>
				<s:textfield name="joinDateStart" cssClass="date" id="joinDateStartTemp%{index}" cssStyle="width:80px" disabled="true"/>-<s:textfield name="joinDateEnd" cssClass="date" id="joinDateEndTemp%{index}" cssStyle="width:80px" disabled="true"/>
				<s:hidden name="joinDateStart" id="joinDateStartTemp%{index}"></s:hidden>
				<s:hidden name="joinDateEnd" id="joinDateEndTemp%{index}"></s:hidden>
				</span>
				<span style="margin-left:20px;" class='<s:if test="%{joinDateMode == null || joinDateMode == -1}">hide</s:if>'>
				<s:text name="global.page.joinDateSaleDateRel" />
				<input id="joinDateSaleDateRel1" type="radio" name="joinDateSaleDateRel" value="1" <s:if test="%{joinDateSaleDateRel == 1}">checked</s:if> disabled="disabled"><label for="joinDateSaleDateRel1"><s:text name="global.page.and" /></label>
				<input id="joinDateSaleDateRel2" type="radio" name="joinDateSaleDateRel" value="2" <s:if test="%{joinDateSaleDateRel == 2}">checked</s:if> disabled="disabled"><label for="joinDateSaleDateRel2"><s:text name="global.page.or" /></label>
				<s:hidden name="joinDateSaleDateRel"></s:hidden>
				</span>
				</s:if>
				<s:else>
				<span>
				<select name="joinDateMode" onchange="popmemsearch.selectDateMode(this);">
					<option value="-1" <s:if test="%{joinDateMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="1" <s:if test="%{joinDateMode == 1}">selected</s:if>><s:text name="global.page.curJoinDate"/></option>
					<option value="0" <s:if test="%{joinDateMode == 0}">selected</s:if>><s:text name="global.page.dynamic"/></option>
					<option value="9" <s:if test="%{joinDateMode == 9}">selected</s:if>><s:text name="global.page.custom"/></option>
				</select>
				</span>
				<span class='<s:if test="%{joinDateMode != 0}">hide</s:if>'>
				<s:text name="global.page.joinDateDes" />
				<s:textfield name="joinDateRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
				<s:select list='#application.CodeTable.getCodes("1239")' listKey="CodeKey" listValue="Value" name="joinDateUnit" cssStyle="width:auto;"></s:select>
				<select name="joinDateUnitFlag" style="width:auto;">
					<option value="1" <s:if test="%{joinDateUnitFlag == 1}">selected</s:if>><s:text name="global.page.joinDateUnitFlag1" /></option>
					<option value="2" <s:if test="%{joinDateUnitFlag == 2}">selected</s:if>><s:text name="global.page.joinDateUnitFlag2" /></option>
				</select>
				</span>
				<span class='<s:if test="%{joinDateMode != 9}">hide</s:if>'>
				<s:textfield name="joinDateStart" cssClass="date" id="joinDateStart%{index}" cssStyle="width:80px"/>-<s:textfield name="joinDateEnd" cssClass="date" id="joinDateEnd%{index}" cssStyle="width:80px"/>
				</span>
				<span style="margin-left:20px;" class='<s:if test="%{joinDateMode == null || joinDateMode == -1}">hide</s:if>'>
				<s:text name="global.page.joinDateSaleDateRel" />
				<input id="joinDateSaleDateRel1" type="radio" name="joinDateSaleDateRel" value="1" <s:if test="%{joinDateSaleDateRel == 1}">checked</s:if>><label for="joinDateSaleDateRel1"><s:text name="global.page.and" /></label>
				<input id="joinDateSaleDateRel2" type="radio" name="joinDateSaleDateRel" value="2" <s:if test="%{joinDateSaleDateRel == 2}">checked</s:if>><label for="joinDateSaleDateRel2"><s:text name="global.page.or" /></label>
				</span>
				</s:else>
	   		  </td>
            </tr>
            <tr>
              <th><s:text name="global.page.birthDay"/></th>
              <td colspan="3">
                <s:if test="%{disableConditionMap.birthDayMode != null && disableConditionMap.birthDayMode != -1}">
                <span>
                <select name="birthDayMode" onchange="popmemsearch.selectBirthDayMode(this);" disabled="disabled">
					<option value="-1" <s:if test="%{birthDayMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="0" <s:if test="%{birthDayMode == 0}">selected</s:if>><s:text name="global.page.curBirthDayMonth"/></option>
					<option value="1" <s:if test="%{birthDayMode == 1}">selected</s:if>><s:text name="global.page.curBirthDayDate"/></option>
					<option value="2" <s:if test="%{birthDayMode == 2}">selected</s:if>><s:text name="global.page.dynamicBirthDay"/></option>
					<option value="9" <s:if test="%{birthDayMode == 9}">selected</s:if>><s:text name="global.page.customBirthDay"/></option>
					<option value="3" <s:if test="%{birthDayMode == 3}">selected</s:if>><s:text name="global.page.birthDayRange"/></option>
				</select>
				<s:hidden name="birthDayMode"></s:hidden>
                </span>
                <span class='<s:if test="%{birthDayMode != 0}">hide</s:if>'>
                <s:textfield name="birthDayDateStart" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield><s:text name="global.page.date"/>-<s:textfield name="birthDayDateEnd" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield><s:text name="global.page.date"/>
                <input type="checkbox" name="joinDateFlag" value="1" <s:if test="%{joinDateFlag==1}">checked</s:if> id="joinDateFlag1" disabled="disabled"/><label for="joinDateFlag1"><s:text name="global.page.joinDateFlag" /></label>
                <s:hidden name="birthDayDateStart"></s:hidden>
                <s:hidden name="birthDayDateEnd"></s:hidden>
                <s:hidden name="joinDateFlag"></s:hidden>
                </span>
                <span class='<s:if test="%{birthDayMode != 1}">hide</s:if>'>
                <input type="checkbox" name="curDayJoinDateFlag" value="1" <s:if test="%{curDayJoinDateFlag==1}">checked</s:if> id="curDayJoinDateFlag1" disabled="disabled"/><label for="curDayJoinDateFlag1"><s:text name="global.page.curDayJoinDateFlag" /></label>
                <s:hidden name="curDayJoinDateFlag"></s:hidden>
                </span>
                <span class='<s:if test="%{birthDayMode != 2}">hide</s:if>'>
                <select name="birthDayPath" style="width:auto;" disabled="disabled">
					<option value="1" <s:if test="%{birthDayPath == 1}">selected</s:if>><s:text name="global.page.birthDayPath1" /></option>
					<option value="2" <s:if test="%{birthDayPath == 2}">selected</s:if>><s:text name="global.page.birthDayPath2" /></option>
				</select>
                <s:textfield name="birthDayRange" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield>
                <select name="birthDayUnit" style="width:auto;" disabled="disabled">
					<option value="1" <s:if test="%{birthDayUnit == 1}">selected</s:if>><s:text name="global.page.birthDayUnit1" /></option>
					<option value="2" <s:if test="%{birthDayUnit == 2}">selected</s:if>><s:text name="global.page.birthDayUnit2" /></option>
				</select>
				<s:text name="global.page.birthDayText" />
				<s:hidden name="birthDayPath"></s:hidden>
				<s:hidden name="birthDayRange"></s:hidden>
				<s:hidden name="birthDayUnit"></s:hidden>
                </span>
                <span class='<s:if test="%{birthDayMode != 9}">hide</s:if>'>
                <s:select list="monthList" name="birthDayMonth" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" onchange="popmemsearch.selectMonth(this,'0');" value="%{birthDayMonth}" disabled="true"></s:select><s:text name="global.page.month"/>
          		<s:if test="%{dateList != null && !dateList.isEmpty()}">
          		<s:select list="dateList" name="birthDayDate" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" value="%{birthDayDate}" disabled="true"></s:select><s:text name="global.page.date"/>
          		</s:if>
          		<s:else>
          		<select name="birthDayDate" style="width:auto;" disabled="disabled"><option value=""><s:text name="global.page.select" /></option></select><s:text name="global.page.date"/>
          		</s:else>
          		<s:hidden name="birthDayMonth"></s:hidden>
          		<s:hidden name="birthDayDate"></s:hidden>
                </span>
                <span class='<s:if test="%{birthDayMode != 3}">hide</s:if>'>
                <s:select list="monthList" name="birthDayMonthRangeStart" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" onchange="popmemsearch.selectMonth(this,'1');" value="%{birthDayMonthRangeStart}" disabled="true"></s:select><s:text name="global.page.month"/>
          		<s:if test="%{dateRangeStartList != null && !dateRangeStartList.isEmpty()}">
          		<s:select list="dateRangeStartList" name="birthDayDateRangeStart" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" value="%{birthDayDateRangeStart}" disabled="true"></s:select><s:text name="global.page.date"/>
          		</s:if>
          		<s:else>
          		<select name="birthDayDateRangeStart" style="width:auto;" disabled="disabled"><option value=""><s:text name="global.page.select" /></option></select><s:text name="global.page.date"/>
          		</s:else>-
          		<s:select list="monthList" name="birthDayMonthRangeEnd" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" onchange="popmemsearch.selectMonth(this,'1');" value="%{birthDayMonthRangeEnd}" disabled="true"></s:select><s:text name="global.page.month"/>
          		<s:if test="%{dateRangeEndList != null && !dateRangeEndList.isEmpty()}">
          		<s:select list="dateRangeEndList" name="birthDayDateRangeEnd" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" value="%{birthDayDateRangeEnd}" disabled="true"></s:select><s:text name="global.page.date"/>
          		</s:if>
          		<s:else>
          		<select name="birthDayDateRangeEnd" style="width:auto;" disabled="disabled"><option value=""><s:text name="global.page.select" /></option></select><s:text name="global.page.date"/>
          		</s:else>
          		<s:hidden name="birthDayMonthRangeStart"></s:hidden>
          		<s:hidden name="birthDayDateRangeStart"></s:hidden>
          		<s:hidden name="birthDayMonthRangeEnd"></s:hidden>
          		<s:hidden name="birthDayDateRangeEnd"></s:hidden>
                </span>
                <span style="margin-left:20px;">
                <span>
                <select name="birthDayDateMode" onchange="popmemsearch.selectBirthDayDateMode(this);">
					<option value="-1" <s:if test="%{birthDayDateMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="0" <s:if test="%{birthDayDateMode == 0}">selected</s:if>><s:text name="global.page.curBirthDayMonth"/></option>
					<option value="1" <s:if test="%{birthDayDateMode == 1}">selected</s:if>><s:text name="global.page.curBirthDayDate"/></option>
				</select>
				</span>
				<span class='<s:if test="%{birthDayDateMode != 0}">hide</s:if>'>
				<s:textfield name="birthDayDateMoreStart" cssClass="text" cssStyle="width:30px;"></s:textfield><s:text name="global.page.date"/>-<s:textfield name="birthDayDateMoreEnd" cssClass="text" cssStyle="width:30px;"></s:textfield><s:text name="global.page.date"/>
				</span>
				</span>
                </s:if>
                
                <s:else>
                <span>
                <select name="birthDayMode" onchange="popmemsearch.selectBirthDayMode(this);">
					<option value="-1" <s:if test="%{birthDayMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="0" <s:if test="%{birthDayMode == 0}">selected</s:if>><s:text name="global.page.curBirthDayMonth"/></option>
					<option value="1" <s:if test="%{birthDayMode == 1}">selected</s:if>><s:text name="global.page.curBirthDayDate"/></option>
					<option value="2" <s:if test="%{birthDayMode == 2}">selected</s:if>><s:text name="global.page.dynamicBirthDay"/></option>
					<option value="9" <s:if test="%{birthDayMode == 9}">selected</s:if>><s:text name="global.page.customBirthDay"/></option>
					<option value="3" <s:if test="%{birthDayMode == 3}">selected</s:if>><s:text name="global.page.birthDayRange"/></option>
				</select>
                </span>
                <span class='<s:if test="%{birthDayMode != 0}">hide</s:if>'>
                <s:textfield name="birthDayDateStart" cssClass="text" cssStyle="width:30px;"></s:textfield><s:text name="global.page.date"/>-<s:textfield name="birthDayDateEnd" cssClass="text" cssStyle="width:30px;"></s:textfield><s:text name="global.page.date"/>
                <input type="checkbox" name="joinDateFlag" value="1" <s:if test="%{joinDateFlag==1}">checked</s:if> id="joinDateFlag1"/><label for="joinDateFlag1"><s:text name="global.page.joinDateFlag" /></label>
                </span>
                <span class='<s:if test="%{birthDayMode != 1}">hide</s:if>'>
                <input type="checkbox" name="curDayJoinDateFlag" value="1" <s:if test="%{curDayJoinDateFlag==1}">checked</s:if> id="curDayJoinDateFlag1"/><label for="curDayJoinDateFlag1"><s:text name="global.page.curDayJoinDateFlag" /></label>
                </span>
                <span class='<s:if test="%{birthDayMode != 2}">hide</s:if>'>
                <select name="birthDayPath" style="width:auto;">
					<option value="1" <s:if test="%{birthDayPath == 1}">selected</s:if>><s:text name="global.page.birthDayPath1" /></option>
					<option value="2" <s:if test="%{birthDayPath == 2}">selected</s:if>><s:text name="global.page.birthDayPath2" /></option>
				</select>
                <s:textfield name="birthDayRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
                <select name="birthDayUnit" style="width:auto;">
					<option value="1" <s:if test="%{birthDayUnit == 1}">selected</s:if>><s:text name="global.page.birthDayUnit1" /></option>
					<option value="2" <s:if test="%{birthDayUnit == 2}">selected</s:if>><s:text name="global.page.birthDayUnit2" /></option>
				</select>
				<s:text name="global.page.birthDayText" />
                </span>
                <span class='<s:if test="%{birthDayMode != 9}">hide</s:if>'>
                <s:select list="monthList" name="birthDayMonth" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" onchange="popmemsearch.selectMonth(this,'0');" value="%{birthDayMonth}"></s:select><s:text name="global.page.month"/>
          		<s:if test="%{dateList != null && !dateList.isEmpty()}">
          		<s:select list="dateList" name="birthDayDate" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" value="%{birthDayDate}"></s:select><s:text name="global.page.date"/>
          		</s:if>
          		<s:else>
          		<select name="birthDayDate" style="width:auto;"><option value=""><s:text name="global.page.select" /></option></select><s:text name="global.page.date"/>
          		</s:else>
                </span>
                <span class='<s:if test="%{birthDayMode != 3}">hide</s:if>'>
                <s:select list="monthList" name="birthDayMonthRangeStart" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" onchange="popmemsearch.selectMonth(this,'1');" value="%{birthDayMonthRangeStart}"></s:select><s:text name="global.page.month"/>
          		<s:if test="%{dateRangeStartList != null && !dateRangeStartList.isEmpty()}">
          		<s:select list="dateRangeStartList" name="birthDayDateRangeStart" cssStyle="width:auto;" value="%{birthDayDateRangeStart}"></s:select><s:text name="global.page.date"/>
          		</s:if>
          		<s:else>
          		<select name="birthDayDateRangeStart" style="width:auto;"><option value=""><s:text name="global.page.select" /></option></select><s:text name="global.page.date"/>
          		</s:else>-
          		<s:select list="monthList" name="birthDayMonthRangeEnd" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" onchange="popmemsearch.selectMonth(this,'1');" value="%{birthDayMonthRangeEnd}"></s:select><s:text name="global.page.month"/>
          		<s:if test="%{dateRangeEndList != null && !dateRangeEndList.isEmpty()}">
          		<s:select list="dateRangeEndList" name="birthDayDateRangeEnd" cssStyle="width:auto;" value="%{birthDayDateRangeEnd}"></s:select><s:text name="global.page.date"/>
          		</s:if>
          		<s:else>
          		<select name="birthDayDateRangeEnd" style="width:auto;"><option value=""><s:text name="global.page.select" /></option></select><s:text name="global.page.date"/>
          		</s:else>
                </span>
                </s:else>
              </td>
            </tr>
            <tr>
			  <th><s:text name="global.page.memCode" /></th>
              <td <s:if test='%{"1".equals(clubMod)}'> colspan="3" </s:if>>
                <span>
                <s:if test="%{disableConditionMap.memCode != null}">
                <s:textfield name="memCode" cssClass="text" disabled="true"/>
                <s:hidden name="memCode"></s:hidden>
                </s:if>
                <s:else>
                <s:textfield name="memCode" cssClass="text"/>
                </s:else>
                </span>
			  </td>
			  <s:if test='%{"3".equals(clubMod)}'>
              <th><s:text name="global.page.memberPoint" /></th>
              <td>
                <span>
                <s:if test="%{disableConditionMap.memberPointStart != null || disableConditionMap.memberPointEnd != null}">
                <s:textfield name="memberPointStart" cssClass="text" cssStyle="width:75px;" disabled="true"/>-<s:textfield name="memberPointEnd" cssClass="text" cssStyle="width:75px;" disabled="true"/>
                <s:hidden name="memberPointStart"></s:hidden>
                <s:hidden name="memberPointEnd"></s:hidden>
                </s:if>
                <s:else>
                <s:textfield name="memberPointStart" cssClass="text" cssStyle="width:75px;"/>-<s:textfield name="memberPointEnd" cssClass="text" cssStyle="width:75px;" />
                </s:else>
                </span>
			  </td>
			  </s:if>
            </tr>
            <tr>
              <th><s:text name="global.page.memCounter" /></th>
			  <td <s:if test='%{"1".equals(clubMod)}'> colspan="3" </s:if>>
                <span>
                <s:hidden name="regionId"></s:hidden>
			    <s:hidden name="provinceId"></s:hidden>
                <s:hidden name="cityId"></s:hidden>
                <s:hidden name="memCounterId"></s:hidden>
                <s:hidden name="channelRegionId"></s:hidden>
                <s:hidden name="channelId"></s:hidden>
                <s:hidden name="exclusiveFlag"></s:hidden>
                <s:hidden name="modeFlag"></s:hidden>
                <s:hidden name="couValidFlag"></s:hidden>
                <s:hidden name="regionName"></s:hidden>
                <s:hidden name="belongId"></s:hidden>
                <span id="regionNameDiv" style="line-height: 18px;">
                <s:if test='%{regionName != null && !"".equals(regionName)}'>
		        <s:property value="%{regionName}"/>
		        <s:if test="%{disableConditionMap.modeFlag != null}"></s:if>
		        <s:else>
		        <span class="close" style="margin: 0 10px 2px 5px;" onclick="popmemsearch.delProvinceHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		        </s:else>
		        </s:if>
                </span>
                <s:if test="%{disableConditionMap.modeFlag != null}"></s:if>
                <s:else>
                <s:url action="BINOLCM02_initRegionDialog" namespace="/common" id="initRegionDialogUrl"></s:url>
		        <a class="add" onclick="popmemsearch.popRegionDialog('${initRegionDialogUrl}',this);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
                </s:else>
              	</span>
              </td>
              <s:if test='%{"3".equals(clubMod)}'>
              <th><s:text name="global.page.changablePoint" /></th>
              <td>
                <span>
                <s:if test="%{disableConditionMap.changablePointStart != null || disableConditionMap.changablePointEnd != null}">
                <s:textfield name="changablePointStart" cssClass="text" cssStyle="width:75px;" disabled="true"/>-<s:textfield name="changablePointEnd" cssClass="text" cssStyle="width:75px;" disabled="true"/>
                <s:hidden name="changablePointStart"></s:hidden>
                <s:hidden name="changablePointEnd"></s:hidden>
                </s:if>
                <s:else>
                <s:textfield name="changablePointStart" cssClass="text" cssStyle="width:75px;"/>-<s:textfield name="changablePointEnd" cssClass="text" cssStyle="width:75px;" />
                </s:else>
                </span>
			  </td>
			  </s:if>
            </tr>
            <tr>
			  <th><s:text name="global.page.mebSex" /></th>
              <td>
              	<span>
              	<s:if test="%{disableConditionMap.mebSex != null}">
              	<s:checkboxlist list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" name="mebSex" value="%{mebSex}" disabled="true"></s:checkboxlist>
              	<s:iterator value="mebSex" id="_mebSex">
              	<s:hidden name="mebSex" value="%{#_mebSex}"></s:hidden>
              	</s:iterator>
              	</s:if>
              	<s:else>
              	<s:checkboxlist list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" name="mebSex" value="%{mebSex}" ></s:checkboxlist>
              	</s:else>
              	</span>
              </td>
              <th><s:text name="global.page.memAge" /></th>
              <td>
                <span>
                <s:if test="%{disableConditionMap.ageStart != null || disableConditionMap.ageEnd != null}">
                <s:textfield name="ageStart" cssClass="text" cssStyle="width:75px;" disabled="true"/>-<s:textfield name="ageEnd" cssClass="text" cssStyle="width:75px;" disabled="true"/>
                <s:hidden name="ageStart"></s:hidden>
                <s:hidden name="ageEnd"></s:hidden>
                </s:if>
                <s:else>
                <s:textfield name="ageStart" cssClass="text" cssStyle="width:75px;" />-<s:textfield name="ageEnd" cssClass="text" cssStyle="width:75px;" />
                </s:else>
                </span>
              </td>
			</tr>  
			<s:if test='%{"3".equals(clubMod)}'>
			<tr>
              <th><s:text name="global.page.referFlag" /></th>
              <td colspan="3">
                  <s:if test="%{disableConditionMap.referFlag != null && disableConditionMap.referFlag != -1}">
                  <span>
                  <select name="referFlag" onchange="popmemsearch.selectReferFlag(this);" disabled="disabled">
					<option value="-1" <s:if test="%{referFlag == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="1" <s:if test="%{referFlag == 1}">selected</s:if>><s:text name="global.page.referFlag1"/></option>
					<option value="2" <s:if test="%{referFlag == 2}">selected</s:if>><s:text name="global.page.referFlag2"/></option>
				  </select>
				  <s:hidden name="referFlag"></s:hidden>
				  </span>
				  <span class='<s:if test="%{referFlag != 1}">hide</s:if>'>
					<s:text name="global.page.referredMemCode" />：<s:textfield name="referredMemCode"  cssClass="text" cssStyle="width:100px" disabled="true"/>
				  	<s:hidden name="referredMemCode"></s:hidden>
				  </span>
                  <span class='<s:if test="%{referFlag != 2}">hide</s:if>'>
					<s:text name="global.page.referrerMemCode" />：<s:textfield name="referrerMemCode"  cssClass="text" cssStyle="width:100px" disabled="true"/>
					<s:hidden name="referrerMemCode"></s:hidden>
				  </span>
                  </s:if>
                  <s:else>
                  <span>
                  <select name="referFlag" onchange="popmemsearch.selectReferFlag(this);" style="width:135px;">
					<option value="-1" <s:if test="%{referFlag == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="1" <s:if test="%{referFlag == 1}">selected</s:if>><s:text name="global.page.referFlag1"/></option>
					<option value="2" <s:if test="%{referFlag == 2}">selected</s:if>><s:text name="global.page.referFlag2"/></option>
				  </select>
				  </span>
                  <span class='<s:if test="%{referFlag != 1}">hide</s:if>'>
					<s:text name="global.page.referredMemCode" />：<s:textfield name="referredMemCode"  cssClass="text" cssStyle="width:100px"/>
				  </span>
                  <span class='<s:if test="%{referFlag != 2}">hide</s:if>'>
					<s:text name="global.page.referrerMemCode" />：<s:textfield name="referrerMemCode"  cssClass="text" cssStyle="width:100px"/>
				  </span>
                  </s:else>
              </td>
            </tr>
            <tr>
              <th><s:text name="global.page.levelAdjustDay" /></th>
              <td colspan="3">
                  <s:if test="%{disableConditionMap.levelAdjustDayFlag != null && disableConditionMap.levelAdjustDayFlag != -1}">
                    <span>
					<select name="levelAdjustDayFlag" onchange="popmemsearch.selectLevelAdjustDayMode(this);" disabled="disabled">
						<option value="-1" <s:if test="%{levelAdjustDayFlag == -1}">selected</s:if>><s:text name="global.page.select"/></option>
						<option value="1" <s:if test="%{levelAdjustDayFlag == 1}">selected</s:if>><s:text name="global.page.curLevelAdjustDay"/></option>
						<option value="2" <s:if test="%{levelAdjustDayFlag == 2}">selected</s:if>><s:text name="global.page.dynamic"/></option>
						<option value="3" <s:if test="%{levelAdjustDayFlag == 3}">selected</s:if>><s:text name="global.page.custom"/></option>
					</select>
					<s:hidden name="levelAdjustDayFlag"></s:hidden>
					</span>
					<span class='<s:if test="%{levelAdjustDayFlag != 2}">hide</s:if>'>
					<s:text name="global.page.levelAdjustDayDes" />
					<s:textfield name="levelAdjustDayRange" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield>
	                <select name="levelAdjustDayUnit" style="width:auto;" disabled="disabled">
						<option value="1" <s:if test="%{levelAdjustDayUnit == 1}">selected</s:if>><s:text name="global.page.levelAdjustDayUnit1" /></option>
						<option value="2" <s:if test="%{levelAdjustDayUnit == 2}">selected</s:if>><s:text name="global.page.levelAdjustDayUnit2" /></option>
					</select>
					<s:hidden name="levelAdjustDayRange"></s:hidden>
					<s:hidden name="levelAdjustDayUnit"></s:hidden>
					</span>
					<span class='<s:if test="%{levelAdjustDayFlag != 3}">hide</s:if>'>
					<s:textfield name="levelAdjustDayStart" cssClass="date" id="levelAdjustDayStartTemp%{index}" cssStyle="width:80px" disabled="true"/>-<s:textfield name="levelAdjustDayEnd" cssClass="date" id="levelAdjustDayEndTemp%{index}" cssStyle="width:80px" disabled="true"/>
					<s:hidden name="levelAdjustDayStart" id="levelAdjustDayStartTemp%{index}"></s:hidden>
					<s:hidden name="levelAdjustDayEnd" id="levelAdjustDayEndTemp%{index}"></s:hidden>
					</span>
					<span style="margin-left:20px;" class='<s:if test="%{levelAdjustDayFlag == null || levelAdjustDayFlag == -1}">hide</s:if>'>
					<s:text name="global.page.levelChangeType" />
					<s:select list='#application.CodeTable.getCodes("1249")' listKey="CodeKey" listValue="Value" name="levelChangeType" headerKey="" headerValue="%{#select_default}" cssStyle="width:auto;" disabled="true"></s:select>
					<s:hidden name="levelChangeType"></s:hidden>
					</span>
                  </s:if>
                  <s:else>
                  	<span>
					<select name="levelAdjustDayFlag" onchange="popmemsearch.selectLevelAdjustDayMode(this);">
						<option value="-1" <s:if test="%{levelAdjustDayFlag == -1}">selected</s:if>><s:text name="global.page.select"/></option>
						<option value="1" <s:if test="%{levelAdjustDayFlag == 1}">selected</s:if>><s:text name="global.page.curLevelAdjustDay"/></option>
						<option value="2" <s:if test="%{levelAdjustDayFlag == 2}">selected</s:if>><s:text name="global.page.dynamic"/></option>
						<option value="3" <s:if test="%{levelAdjustDayFlag == 3}">selected</s:if>><s:text name="global.page.custom"/></option>
					</select>
					</span>
					<span class='<s:if test="%{levelAdjustDayFlag != 2}">hide</s:if>'>
					<s:text name="global.page.levelAdjustDayDes" />
					<s:textfield name="levelAdjustDayRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
	                <select name="levelAdjustDayUnit" style="width:auto;">
						<option value="1" <s:if test="%{levelAdjustDayUnit == 1}">selected</s:if>><s:text name="global.page.levelAdjustDayUnit1" /></option>
						<option value="2" <s:if test="%{levelAdjustDayUnit == 2}">selected</s:if>><s:text name="global.page.levelAdjustDayUnit2" /></option>
					</select>
					</span>
					<span class='<s:if test="%{levelAdjustDayFlag != 3}">hide</s:if>'>
					<s:textfield name="levelAdjustDayStart" cssClass="date" id="levelAdjustDayStart%{index}" cssStyle="width:80px"/>-<s:textfield name="levelAdjustDayEnd" cssClass="date" id="levelAdjustDayEnd%{index}" cssStyle="width:80px"/>
					</span>
					<span style="margin-left:20px;" class='<s:if test="%{levelAdjustDayFlag == null || levelAdjustDayFlag == -1}">hide</s:if>'>
					<s:text name="global.page.levelChangeType" />
					<s:select list='#application.CodeTable.getCodes("1249")' listKey="CodeKey" listValue="Value" name="levelChangeType" headerKey="" headerValue="%{#select_default}" cssStyle="width:auto;"></s:select>
					</span>
                  </s:else>
              </td>
            </tr>  
            <tr>
              <th><s:text name="global.page.lastSaleDate" /></th>
              <td colspan="3">
              	<span>
              	<s:if test="%{disableConditionMap.lastSaleDateStart != null || disableConditionMap.lastSaleDateEnd != null}">
                <s:textfield name="lastSaleDateStart" cssClass="date" id="lastSaleDateStart%{index}" cssStyle="width:80px" disabled="true"/>
              	-<s:textfield name="lastSaleDateEnd" cssClass="date" id="lastSaleDateEnd%{index}" cssStyle="width:80px" disabled="true"/>
                <s:hidden name="lastSaleDateStart" id="lastSaleDateStart%{index}"></s:hidden>
                <s:hidden name="lastSaleDateEnd" id="lastSaleDateEnd%{index}"></s:hidden>
                </s:if>
                <s:else>
                <s:textfield name="lastSaleDateStart" cssClass="date" id="lastSaleDateStart%{index}" cssStyle="width:80px"/>
              	-<s:textfield name="lastSaleDateEnd" cssClass="date" id="lastSaleDateEnd%{index}" cssStyle="width:80px"/>
                </s:else>
                 </span>
              </td>
            </tr>             
            </s:if>
            <tr>
              <th><s:text name="global.page.notSaleDays" /></th>
              <td colspan="3">
               <s:if test="%{disableConditionMap.noSaleDaysMode == null && disableConditionMap.notSaleDays != null}">
               	<span>
                <select name="noSaleDaysMode"  disabled="disabled">
					<option value="1" <s:if test="%{noSaleDaysMode == 1}">selected</s:if>><s:text name="global.page.noSaleDaysMode1" /></option>
					<option value="2" <s:if test="%{noSaleDaysMode == 2}">selected</s:if>><s:text name="global.page.noSaleDaysMode2" /></option>
				</select>
				<s:hidden name="noSaleDaysMode" value="1"></s:hidden>
				</span>
				<span>
				<s:text name="global.page.afterFirstBuy" />
				 <s:textfield name="notSaleDays" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield>
                  <s:hidden name="notSaleDays"></s:hidden>
                  <s:text name="global.page.notSaleDays1" />
                   &nbsp;&nbsp;
			       <label class="gray"><s:text name="global.page.nsdMode1Txt" /></label>
				</span>
               </s:if>
               <s:else>
	               <s:if test="%{disableConditionMap.noSaleDaysMode != null && (disableConditionMap.notSaleDays != null || disableConditionMap.firstStartDay != null || disableConditionMap.firstEndDay != null)}">
	               <span>
	                <select name="noSaleDaysMode" disabled="disabled">
						<option value="1" <s:if test="%{noSaleDaysMode == 1}">selected</s:if>><s:text name="global.page.noSaleDaysMode1" /></option>
						<option value="2" <s:if test="%{noSaleDaysMode == 2}">selected</s:if>><s:text name="global.page.noSaleDaysMode2" /></option>
					</select>
					<s:hidden name="noSaleDaysMode"></s:hidden>
					</span>
					<span>
						<s:if test="%{disableConditionMap.noSaleDaysMode == 1}">
							 <s:text name="global.page.afterFirstBuy" />
							<s:if test="%{disableConditionMap.notSaleDays != null}">
			                  <s:textfield name="notSaleDays" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield>
			                  <s:hidden name="notSaleDays"></s:hidden>
			                  <s:text name="global.page.notSaleDays1" />
			                  </s:if>
			                  <s:else>
			                  <s:textfield name="notSaleDays" cssClass="text" cssStyle="width:30px;"></s:textfield>
			                  <s:text name="global.page.notSaleDays1" />
			                  </s:else>
			                  &nbsp;&nbsp;
			                   <label class="gray"><s:text name="global.page.nsdMode1Txt" /></label>
						</s:if>
						<s:else>
							<s:text name="global.page.firstBuyDayRange" />
							<s:if test="%{disableConditionMap.firstStartDay != null || disableConditionMap.firstEndDay != null}">
			                <s:textfield name="firstStartDay" cssClass="date" id="firstStartDay%{index}" cssStyle="width:80px" disabled="true"/>
			              	-<s:textfield name="firstEndDay" cssClass="date" id="firstEndDay%{index}" cssStyle="width:80px" disabled="true"/>
			                <s:hidden name="firstStartDay" id="firstStartDay%{index}"></s:hidden>
			                <s:hidden name="firstEndDay" id="firstEndDay%{index}"></s:hidden>
			                </s:if>
			                <s:else>
			                <s:textfield name="firstStartDay" cssClass="date" id="firstStartDay%{index}" cssStyle="width:80px"/>
			              	-<s:textfield name="firstEndDay" cssClass="date" id="firstEndDay%{index}" cssStyle="width:80px"/>
			                </s:else>
			                &nbsp;&nbsp;
			                 	<s:text name="global.page.afterFirstBuy" />
			                <s:if test="%{disableConditionMap.notSaleDays != null}">
			                  <s:textfield name="notSaleDaysRange" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield>
			                  <s:hidden name="notSaleDaysRange"></s:hidden>
			                  <s:text name="global.page.notSaleDays1" />
			                  </s:if>
			                  <s:else>
			                  <s:textfield name="notSaleDaysRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
			                  <s:text name="global.page.notSaleDays1" />
			                  </s:else>
						</s:else>
					</span>
	               </s:if>
	               <s:else>
	               		<span>
		                <select name="noSaleDaysMode" onchange="popmemsearch.selectFirstDayMode(this);" >
							<option value="1" <s:if test="%{noSaleDaysMode == 1}">selected</s:if>><s:text name="global.page.noSaleDaysMode1" /></option>
							<option value="2" <s:if test="%{noSaleDaysMode == 2}">selected</s:if>><s:text name="global.page.noSaleDaysMode2" /></option>
						</select>
						</span>
						<span class='<s:if test="%{noSaleDaysMode == 2}">hide</s:if>' id="spanNoSaleDaysMode1">
							<s:text name="global.page.afterFirstBuy" />
			              	 <s:textfield name="notSaleDays" cssClass="text" cssStyle="width:30px;"></s:textfield>
			                  <s:text name="global.page.notSaleDays1" />
			                   &nbsp;&nbsp;
			                   <label class="gray"><s:text name="global.page.nsdMode1Txt" /></label>
						</span>
						<span class='<s:if test="%{noSaleDaysMode != 2}">hide</s:if>' id="spanNoSaleDaysMode2">
							<s:text name="global.page.firstBuyDayRange" />
							 <s:textfield name="firstStartDay" cssClass="date" id="firstStartDay%{index}" cssStyle="width:80px"/>
			              	-<s:textfield name="firstEndDay" cssClass="date" id="firstEndDay%{index}" cssStyle="width:80px"/>
			              	 &nbsp;&nbsp;
			              	 <s:text name="global.page.afterFirstBuy" />
			              	 <s:textfield name="notSaleDaysRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
			                  <s:text name="global.page.notSaleDays1" />
						</span>
	               </s:else>
               </s:else>
              </td>
            </tr>              
          </tbody>
        </table>
      </div>
	</div>
	<s:if test='%{"1".equals(clubMod)}'>
	 <div class="box2 box2-active moreCondition">         
      <div class="box2-header clearfix"><strong class="active left"><s:text name="global.page.clubAttrs" /></strong></div>
      <div class="box2-content terminal">
        <table class="detail" cellpadding="0" cellspacing="0" style="margin-bottom:0px;" id="clubAttrs">
          <tbody>
           <tr>
              <th><s:text name="global.page.memClub" /></th>
              <td colspan="3">
				<span>
				<s:if test="%{disableConditionMap.memberClubId != null}">
				<s:select list="clubList" listKey="memberClubId" listValue="clubName" Cssstyle="width:150px;" disabled="disabled"/>
				<s:hidden name="memberClubId"></s:hidden>
				</s:if>
				 <s:else>
				 <s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="popmemsearch.changeLevel(this);return false;" Cssstyle="width:150px;"/>
				 </s:else>
				</span>
	   		  </td>
            </tr>
            <tr>
              <th><s:text name="global.page.memLevel" /></th>
              <td colspan="3">
				<span id="levelSpan">
				<s:if test="%{disableConditionMap.memLevel != null}">
				<s:if test="null != memLevelList && memLevelList.size() > 0">
					<s:checkboxlist list="memLevelList" listKey="memberLevelId" listValue="levelName" name="memLevel" value="%{memLevel}" disabled="true"></s:checkboxlist>
					<s:iterator value="memLevel" id="_memLevel">
					<s:hidden name="memLevel" value="%{#_memLevel}"></s:hidden>
					</s:iterator>
				</s:if>
				</s:if>
				<s:else>
				<s:if test="null != memLevelList && memLevelList.size() > 0">
					<s:checkboxlist list="memLevelList" listKey="memberLevelId" listValue="levelName" name="memLevel" value="%{memLevel}" ></s:checkboxlist>
				</s:if>
				</s:else>
				</span>
	   		  </td>
            </tr>
             <tr>
             <th><s:text name="global.page.clubMemCounter" /></th>
              <td>
              <span>
              <s:hidden name="clubRegionId"></s:hidden>
			    <s:hidden name="clubProvinceId"></s:hidden>
                <s:hidden name="clubCityId"></s:hidden>
                <s:hidden name="clubMemCounterId"></s:hidden>
                <s:hidden name="clubChannelRegionId"></s:hidden>
                <s:hidden name="clubChannelId"></s:hidden>
                <s:hidden name="clubExclusiveFlag"></s:hidden>
                <s:hidden name="clubModeFlag"></s:hidden>
                <s:hidden name="clubCouValidFlag"></s:hidden>
                <s:hidden name="clubRegionName"></s:hidden>
                <s:hidden name="clubBelongId"></s:hidden>
                <span id="clubRegionNameDiv" style="line-height: 18px;">
                 <s:if test='%{clubRegionName != null && !"".equals(clubRegionName)}'>
		        <s:property value="%{clubRegionName}"/>
		        <s:if test="%{disableConditionMap.clubModeFlag != null}"></s:if>
		        <s:else>
		        <span class="close" style="margin: 0 10px 2px 5px;" onclick="popmemsearch.delProvinceHtml(this,'1');"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		        </s:else>
		        </s:if>
                </span>
                <s:if test="%{disableConditionMap.clubModeFlag != null}"></s:if>
                <s:else>
                <s:url action="BINOLCM02_initRegionDialog" namespace="/common" id="initRegionDialogUrl"></s:url>
		        <a class="add" onclick="popmemsearch.popRegionDialog('${initRegionDialogUrl}',this,'1');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
                </s:else>
                </span>
              </td>
              <th><s:text name="global.page.memberPoint" /></th>
              <td>
                <span>
                <s:if test="%{disableConditionMap.memberPointStart != null || disableConditionMap.memberPointEnd != null}">
                <s:textfield name="memberPointStart" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"/>-<s:textfield name="memberPointEnd" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"/>
                <s:hidden name="memberPointStart"></s:hidden>
                <s:hidden name="memberPointEnd"></s:hidden>
                </s:if>
                <s:else>
                	<s:if test='%{memberClubId == null || "".equals(memberClubId)}'>
	                <s:textfield name="memberPointStart" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"/>-<s:textfield name="memberPointEnd" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"/>
	                </s:if>
	                <s:else>
                		<s:textfield name="memberPointStart" cssClass="text club-attr" cssStyle="width:75px;"/>-<s:textfield name="memberPointEnd" cssClass="text club-attr" cssStyle="width:75px;" />
                	</s:else>
                </s:else>
                </span>
              </td>
             </tr>
             <tr>
             <th><s:text name="global.page.clubJoinTimeStart" /></th>
              <td>
              <s:if test="%{disableConditionMap.clubJoinTimeStart != null || disableConditionMap.clubJoinTimeEnd != null}">
                <span class="no-club-date"><input type="text" class="date" disabled="disabled" style="width:80px;" value='<s:property value="clubJoinTimeStart"/>'/><button class="ui-datepicker-trigger" disabled="disabled"></button></span>
				<span class="no-club-date">-<input type="text" class="date" disabled="disabled" style="width:80px;" value='<s:property value="clubJoinTimeEnd"/>'/><button class="ui-datepicker-trigger" disabled="disabled"></button></span>
                <s:hidden name="clubJoinTimeStart"></s:hidden>
                <s:hidden name="clubJoinTimeEnd"></s:hidden>
                </s:if>
                <s:else>
                	<s:if test='%{memberClubId == null || "".equals(memberClubId)}'>
                	<span class="no-club-date"><input type="text" class="date" disabled="disabled"/><button class="ui-datepicker-trigger" disabled="disabled"></button></span>
					<span class="no-club-date">-<input type="text" class="date" disabled="disabled"/><button class="ui-datepicker-trigger" disabled="disabled"></button></span>
					<span class="hide club-date"><s:textfield name="clubJoinTimeStart" cssClass="date" cssStyle="width:80px;"/></span>
					<span class="hide club-date">-<s:textfield name="clubJoinTimeEnd" cssClass="date" cssStyle="width:80px;"/></span>
                	 </s:if>
	                <s:else>
                	<span class="hide no-club-date"><input type="text" class="date" disabled="disabled" style="width:80px;"/><button class="ui-datepicker-trigger" disabled="disabled"></button></span>
					<span class="hide no-club-date">-<input type="text" class="date" disabled="disabled" style="width:80px;"/><button class="ui-datepicker-trigger" disabled="disabled"></button></span>
					<span class="club-date"><s:textfield name="clubJoinTimeStart" cssClass="date" cssStyle="width:80px;"/></span>
					<span class="club-date">-<s:textfield name="clubJoinTimeEnd" cssClass="date" cssStyle="width:80px;"/></span>
                	</s:else>
                </s:else>
              	
              </td>
			  <th><s:text name="global.page.changablePoint" /></th>
              <td>
              <span>
                <s:if test="%{disableConditionMap.changablePointStart != null || disableConditionMap.changablePointEnd != null}">
                <s:textfield name="changablePointStart" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"/>-<s:textfield name="changablePointEnd" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"/>
                <s:hidden name="changablePointStart"></s:hidden>
                <s:hidden name="changablePointEnd"></s:hidden>
                </s:if>
                <s:else>
                	<s:if test='%{memberClubId == null || "".equals(memberClubId)}'>
	                <s:textfield name="changablePointStart" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"/>-<s:textfield name="changablePointEnd" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"/>
	                </s:if>
	                <s:else>
                		<s:textfield name="changablePointStart" cssClass="text club-attr" cssStyle="width:75px;"/>-<s:textfield name="changablePointEnd" cssClass="text club-attr" cssStyle="width:75px;" />
                	</s:else>
                </s:else>
                </span>
              </td>
             </tr>
             <tr>
              <th><s:text name="global.page.referFlag" /></th>
              <td colspan="3">
              	<s:if test="%{disableConditionMap.referFlag != null && disableConditionMap.referFlag != -1}">
                  <span>
                  <select name="referFlag" onchange="popmemsearch.selectReferFlag(this);" disabled="disabled">
					<option value="-1" <s:if test="%{referFlag == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="1" <s:if test="%{referFlag == 1}">selected</s:if>><s:text name="global.page.referFlag1"/></option>
					<option value="2" <s:if test="%{referFlag == 2}">selected</s:if>><s:text name="global.page.referFlag2"/></option>
				  </select>
				  <s:hidden name="referFlag"></s:hidden>
				  </span>
				  <span class='<s:if test="%{referFlag != 1}">hide</s:if>'>
					<s:text name="global.page.referredMemCode" />：<s:textfield name="referredMemCode"  cssClass="text" cssStyle="width:100px" disabled="true"/>
				  	<s:hidden name="referredMemCode"></s:hidden>
				  </span>
                  <span class='<s:if test="%{referFlag != 2}">hide</s:if>'>
					<s:text name="global.page.referrerMemCode" />：<s:textfield name="referrerMemCode"  cssClass="text" cssStyle="width:100px" disabled="true"/>
					<s:hidden name="referrerMemCode"></s:hidden>
				  </span>
                  </s:if>
                  <s:else>
                  <span>
                  <select name="referFlag" onchange="popmemsearch.selectReferFlag(this);" style="width:135px;" class="club-attr" <s:if test='%{memberClubId == null || "".equals(memberClubId)}'>disabled="disabled"</s:if> >
					<option value="-1" <s:if test="%{referFlag == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="1" <s:if test="%{referFlag == 1}">selected</s:if>><s:text name="global.page.referFlag1"/></option>
					<option value="2" <s:if test="%{referFlag == 2}">selected</s:if>><s:text name="global.page.referFlag2"/></option>
				  </select>
				  </span>
                  <span class='<s:if test="%{referFlag != 1}">hide</s:if>'>
					<s:text name="global.page.referredMemCode" />：<s:textfield name="referredMemCode"  cssClass="text" cssStyle="width:100px"/>
				  </span>
                  <span class='<s:if test="%{referFlag != 2}">hide</s:if>'>
					<s:text name="global.page.referrerMemCode" />：<s:textfield name="referrerMemCode"  cssClass="text" cssStyle="width:100px"/>
				  </span>
                  </s:else>
              </td>
             </tr>
          </tbody>
        </table>
      </div>
	</div>
	</s:if>
	    <s:if test='%{"3".equals(clubMod)}'>
    <s:if test='%{"1".equals(tagFlag)}'>
	<div class="box2 box2-active moreCondition">         
      <div class="box2-header clearfix"><strong class="active left"><s:text name="global.page.MemberTag" /></strong></div>
      <div class="box2-content terminal">
        <table class="detail" cellpadding="0" cellspacing="0" style="margin-bottom:0px;">
          <tbody>
            <tr>
              <th><s:text name="global.page.isNewMember" /></th>
              <td><span>
              	<select name="isNewMember" style="width:135px;" <s:if test='%{disableConditionMap.isNewMember != null && disableConditionMap.isNewMember != ""}'> disabled="disabled" </s:if> >
					<option value=""><s:text name="global.page.select"/></option>
					<option value="1" <s:if test='%{isNewMember == "1"}'>selected</s:if>><s:text name="global.page.yes" /></option>
					<option value="0" <s:if test='%{isNewMember == "0"}'>selected</s:if>><s:text name="global.page.no" /></option>
				  </select>
				  <s:if test='%{disableConditionMap.isNewMember != null && disableConditionMap.isNewMember != ""}'> <s:hidden name="isNewMember"></s:hidden> </s:if>
              </span>
              </td>
              <th><s:text name="global.page.flagBuyCount" /></th>
              <td>
               <span>
              	<select name="flagBuyCount" style="width:135px;" <s:if test='%{disableConditionMap.flagBuyCount != null && disableConditionMap.flagBuyCount != ""}'> disabled="disabled" </s:if>>
					<option value=""><s:text name="global.page.select"/></option>
					<option value="N1" <s:if test='%{flagBuyCount== "N1"}'>selected</s:if>><s:text name="global.page.flagBuyCountVal1" /></option>
					<option value="N2" <s:if test='%{flagBuyCount == "N2"}'>selected</s:if>><s:text name="global.page.flagBuyCountVal2" /></option>
					<option value="N3" <s:if test='%{flagBuyCount == "N3"}'>selected</s:if>><s:text name="global.page.flagBuyCountVal3" /></option>
				</select>
				<s:if test='%{disableConditionMap.flagBuyCount != null && disableConditionMap.flagBuyCount != ""}'> <s:hidden name="flagBuyCount"></s:hidden> </s:if>
              </span>
              </td>
            </tr>
            <tr>
              <th><s:text name="global.page.isActivityMember" /></th>
              <td>
              <span>
                <select name="isActivityMember" style="width:135px;" <s:if test='%{disableConditionMap.isActivityMember != null && disableConditionMap.isActivityMember != ""}'> disabled="disabled" </s:if>>
					<option value=""><s:text name="global.page.select"/></option>
					<option value="1" <s:if test='%{isActivityMember == "1"}'>selected</s:if>><s:text name="global.page.yes" /></option>
					<option value="0" <s:if test='%{isActivityMember == "0"}'>selected</s:if>><s:text name="global.page.no" /></option>
				  </select>
				  <s:if test='%{disableConditionMap.isActivityMember != null && disableConditionMap.isActivityMember != ""}'> <s:hidden name="isActivityMember"></s:hidden> </s:if>
				</span>
              </td>
               <th><s:text name="global.page.actiCount" /></th>
              <td>
              <span>
              <s:if test="%{disableConditionMap.actiCountStart != null || disableConditionMap.actiCountEnd != null}">
              <s:textfield name="actiCountStart" cssClass="text" cssStyle="width:75px;" disabled="true"></s:textfield>-<s:textfield name="actiCountEnd" cssClass="text" cssStyle="width:75px;" disabled="true"></s:textfield>
              	<s:hidden name="actiCountStart"></s:hidden>
                <s:hidden name="actiCountEnd"></s:hidden>
              </s:if>
              <s:else>
             	<s:textfield name="actiCountStart" cssClass="text" cssStyle="width:75px;"></s:textfield>-<s:textfield name="actiCountEnd" cssClass="text" cssStyle="width:75px;"></s:textfield>
              </s:else>
              </span>
              </td>
            </tr>
            <tr>
              <th><s:text name="global.page.favActiType" /></th>
              <td>
              <span>
                <select name="favActiType" style="width:135px;" <s:if test='%{disableConditionMap.favActiType != null && disableConditionMap.favActiType != ""}'> disabled="disabled" </s:if>>
					<option value=""><s:text name="global.page.select"/></option>
					<option value="1" <s:if test='%{favActiType == "1"}'>selected</s:if>><s:text name="global.page.favActiType1" /></option>
					<option value="2" <s:if test='%{favActiType == "2"}'>selected</s:if>><s:text name="global.page.favActiType2" /></option>
				  </select>
				  <s:if test='%{disableConditionMap.favActiType != null && disableConditionMap.favActiType != ""}'> <s:hidden name="favActiType"></s:hidden> </s:if>
				</span>
              </td>
               <th><s:text name="global.page.unBuyInterval" /></th>
              <td>
             <span>
             <select name="unBuyInterval" style="width:135px;" <s:if test='%{disableConditionMap.unBuyInterval != null && disableConditionMap.unBuyInterval != ""}'> disabled="disabled" </s:if>>
					<option value=""><s:text name="global.page.select"/></option>
					<option value="3" <s:if test='%{unBuyInterval == "3"}'>selected</s:if>><s:text name="global.page.unBuyInterval1" /></option>
					<option value="6" <s:if test='%{unBuyInterval == "6"}'>selected</s:if>><s:text name="global.page.unBuyInterval2" /></option>
					<option value="9" <s:if test='%{unBuyInterval == "9"}'>selected</s:if>><s:text name="global.page.unBuyInterval3" /></option>
					<option value="10" <s:if test='%{unBuyInterval == "10"}'>selected</s:if>><s:text name="global.page.unBuyInterval4" /></option>
				  </select>
				   <s:if test='%{disableConditionMap.unBuyInterval != null && disableConditionMap.unBuyInterval != ""}'> <s:hidden name="unBuyInterval"></s:hidden> </s:if>
             </span>
              </td>
            </tr>
            <tr>
              <th><s:text name="global.page.mostCateBClass" /></th>
              <td colspan="3">
                <span class="right" >
                	<s:if test="%{disableConditionMap.mostCateBClassId != null}">
                	</s:if>
                	<s:else>
						<a onclick="popmemsearch.openCateDialog('mostCateBClassId','memPrtCate_1',<s:property value='bigPropId'/>,this,'checkbox');return false;" class="add right"> 
							<span class="ui-icon icon-search"></span>
							<span class="button-text"><s:text name="global.page.select" /></span>
						</a>
					</s:else>
				</span>
				<table class="all_clean" style="margin-left: 15px;">
					<tbody id="memPrtCate_1">
					<s:iterator value="mostCateBClassList" id="mostCateBClassMap">
					<tr style="float : left"><td><span class="list_normal">
					<span class="text" style="line-height:19px;"><s:property value="cateValName"/></span>
					<s:if test="%{disableConditionMap.mostCateBClassId != null}">
	                </s:if>
	                <s:else>
					<span class="close" style="margin: 4px 0 0 6px;" onclick="popmemsearch.removeCate(this);return false;"><span class="ui-icon ui-icon-close"></span></span>
					</s:else>
					<input type="hidden" name="cateValId" value='<s:property value="cateValId"/>'/>
					<input type="hidden" name="mostCateBClassId" value='<s:property value="cateValId"/>'/>
					</span></td></tr>
					</s:iterator>
					</tbody>
				</table>
              </td>
            </tr>
          </tbody>
          <tr>
              <th><s:text name="global.page.mostCateMClass" /></th>
              <td colspan="3">
                <span class="right" >
                 	<s:if test="%{disableConditionMap.mostCateMClassId != null}">
                	</s:if>
                	<s:else>
						<a onclick="popmemsearch.openCateDialog('mostCateMClassId','memPrtCateMClass_1',<s:property value='midPropId'/>,this,'checkbox');return false;" class="add right"> 
							<span class="ui-icon icon-search"></span>
							<span class="button-text"><s:text name="global.page.select" /></span>
						</a>
					</s:else>
				</span>
				<table class="all_clean" style="margin-left: 15px;">
					<tbody id="memPrtCateMClass_1">
					<s:iterator value="mostCateMClassList" id="mostCateMClassMap">
					<tr style="float : left"><td><span class="list_normal">
					<span class="text" style="line-height:19px;"><s:property value="cateValName"/></span>
					<s:if test="%{disableConditionMap.mostCateMClassId != null}">
	                </s:if>
	                <s:else>
					<span class="close" style="margin: 4px 0 0 6px;" onclick="popmemsearch.removeCate(this);return false;"><span class="ui-icon ui-icon-close"></span></span>
					</s:else>
					<input type="hidden" name="cateValId" value='<s:property value="cateValId"/>'/>
					<input type="hidden" name="mostCateMClassId" value='<s:property value="cateValId"/>'/>
					</span></td></tr>
					</s:iterator>
					</tbody>
				</table>
              </td>
            </tr>
          <tr>
              <th><s:text name="global.page.mostPrt" /></th>
              <td colspan="3">
                <span class="right" >
                				<s:if test="%{disableConditionMap.mostPrtId != null}">
			                	</s:if>
			                	<s:else>
								<a onclick="popmemsearch.openMemProDialog(1);return false;" class="add right"> 
									<span class="ui-icon icon-search"></span>
									<span class="button-text"><s:text name="global.page.select" /></span>
								</a>
								</s:else>
				</span>
				<table class="all_clean" style="margin-left: 15px;">
					<tbody id="memPrt_1">
					<s:iterator value="mostPrtList" id="mostPrtMap">
					<tr style="float : left"><td><span class="list_normal">
					<span class="text" style="line-height:19px;">
					<s:property value="nameTotal"/>
					(U: <s:property value="unitCode"/> B: <s:property value="barCode"/>)</span>
					<s:if test="%{disableConditionMap.mostPrtId != null}">
                	</s:if>
                	<s:else>
					<span class="close" style="margin: 4px 0 0 6px;" onclick="popmemsearch.removeCate(this);return false;"><span class="ui-icon ui-icon-close"></span></span>
					</s:else>
					<input type="hidden" name="prtVendorId" value='<s:property value="prtVendorId"/>'/>
					<input type="hidden" name="mostPrtId" value='<s:property value="prtVendorId"/>'/>
					</span></td></tr>
					</s:iterator>
					</tbody>
				</table>
              </td>
            </tr>
            <tr>
               <th><s:text name="global.page.pct" /></th>
              <td colspan="3">
             <span>
              <s:if test="%{disableConditionMap.pctStart != null || disableConditionMap.pctEnd != null}">
              <s:textfield name="pctStart" cssClass="text" cssStyle="width:75px;" disabled="true"></s:textfield>-<s:textfield name="pctEnd" cssClass="text" cssStyle="width:75px;" disabled="true"></s:textfield>
              	<s:hidden name="pctStart"></s:hidden>
                <s:hidden name="pctEnd"></s:hidden>
              </s:if>
              <s:else>
             	<s:textfield name="pctStart" cssClass="text" cssStyle="width:75px;"></s:textfield>-<s:textfield name="pctEnd" cssClass="text" cssStyle="width:75px;"></s:textfield>
              </s:else>
              </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
	</div>
	</s:if>
	</s:if>
    <div class="box2 box2-active moreCondition">         
      <div class="box2-header clearfix"><strong class="active left"><s:text name="global.page.memSaleInfo" /></strong></div>
      <div class="box2-content terminal">
        <table class="detail" cellpadding="0" cellspacing="0" style="margin-bottom:0px;">
          <tbody>
            <tr class="changeSaleFlag">
			  <s:if test="%{disableConditionMap.isSaleFlag != null && disableConditionMap.isSaleFlag != -1}">
			  <td colspan="4" style="line-height:25px;" class="td_point">
			    <span class="ui-widget breadcrumb" style="margin-right:5px;">
			    <input id="isSaleFlag1" type="radio" name="isSaleFlag" value="1" onclick="popmemsearch.changeSaleFlag(this);" <s:if test="%{isSaleFlag == null || isSaleFlag == 1}">checked</s:if> disabled="disabled"><label for="isSaleFlag1"><s:text name="global.page.isSaleFlag1" /></label>
			    </span>
                <span class="ui-widget breadcrumb" style="margin-right:5px;">
                <input id="isSaleFlag0" type="radio" name="isSaleFlag" value="0" onclick="popmemsearch.changeSaleFlag(this);" <s:if test="%{isSaleFlag == 0}">checked</s:if> disabled="disabled"><label for="isSaleFlag0"><s:text name="global.page.isSaleFlag0" /></label>
                </span>
                <s:hidden name="isSaleFlag"></s:hidden>
              </td>
			  </s:if>
			  <s:else>
			  <td colspan="4" style="line-height:25px;" class="td_point">
			    <span class="ui-widget breadcrumb" style="margin-right:5px;">
			    <input id="isSaleFlag1" type="radio" name="isSaleFlag" value="1" onclick="popmemsearch.changeSaleFlag(this);" <s:if test="%{isSaleFlag == null || isSaleFlag == 1}">checked</s:if>><label for="isSaleFlag1"><s:text name="global.page.isSaleFlag1" /></label>
			    </span>
                <span class="ui-widget breadcrumb" style="margin-right:5px;">
                <input id="isSaleFlag0" type="radio" name="isSaleFlag" value="0" onclick="popmemsearch.changeSaleFlag(this);" <s:if test="%{isSaleFlag == 0}">checked</s:if>><label for="isSaleFlag0"><s:text name="global.page.isSaleFlag0" /></label>
                </span>
              </td>
			  </s:else>
            </tr>
            
            <tr class='hasSale <s:if test="%{isSaleFlag == 0}">hide</s:if>'>
              <th><s:text name="global.page.saleTime" /></th>
              <td>
                <s:if test="%{disableConditionMap.saleTimeMode != null && disableConditionMap.saleTimeMode != -1}">
                <span>
                <select name="saleTimeMode" onchange="popmemsearch.selectDateMode(this);" disabled="disabled">
					<option value="-1" <s:if test="%{saleTimeMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="0" <s:if test="%{saleTimeMode == 0}">selected</s:if>><s:text name="global.page.dynamic"/></option>
					<option value="9" <s:if test="%{saleTimeMode == 9}">selected</s:if>><s:text name="global.page.custom"/></option>
				</select>
				<s:hidden name="saleTimeMode"></s:hidden>
				</span>
                <span class='<s:if test="%{saleTimeMode != 0}">hide</s:if>'>
                <s:text name="global.page.saleDateDes" />
                <s:textfield name="saleTimeRange" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield>
                <s:select list='#application.CodeTable.getCodes("1239")' listKey="CodeKey" listValue="Value" name="saleTimeUnit" cssStyle="width:auto;" disabled="true"></s:select>
                <s:text name="global.page.saleTimeUnitFlag" />
                <s:hidden name="saleTimeRange"></s:hidden>
                <s:hidden name="saleTimeUnit"></s:hidden>
                </span>
                <span class='<s:if test="%{saleTimeMode != 9}">hide</s:if>'>
                <s:textfield name="saleTimeStart" cssClass="date" id="saleTimeStartTemp%{index}" cssStyle="width:80px" disabled="true"/>-<s:textfield name="saleTimeEnd" cssClass="date" id="saleTimeEndTemp%{index}" cssStyle="width:80px" disabled="true"/>
                <s:hidden name="saleTimeStart" id="saleTimeStartTemp%{index}"></s:hidden>
                <s:hidden name="saleTimeEnd" id="saleTimeEndTemp%{index}"></s:hidden>
                </span>
                </s:if>
                <s:else>
                <span>
                <select name="saleTimeMode" onchange="popmemsearch.selectDateMode(this);">
					<option value="-1" <s:if test="%{saleTimeMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="0" <s:if test="%{saleTimeMode == 0}">selected</s:if>><s:text name="global.page.dynamic"/></option>
					<option value="9" <s:if test="%{saleTimeMode == 9}">selected</s:if>><s:text name="global.page.custom"/></option>
				</select>
				</span>
                <span class='<s:if test="%{saleTimeMode != 0}">hide</s:if>'>
                <s:text name="global.page.saleDateDes" />
                <s:textfield name="saleTimeRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
                <s:select list='#application.CodeTable.getCodes("1239")' listKey="CodeKey" listValue="Value" name="saleTimeUnit" cssStyle="width:auto;"></s:select>
                <s:text name="global.page.saleTimeUnitFlag" />
                </span>
                <span class='<s:if test="%{saleTimeMode != 9}">hide</s:if>'>
                <s:textfield name="saleTimeStart" cssClass="date" id="saleTimeStart%{index}" cssStyle="width:80px"/>-<s:textfield name="saleTimeEnd" cssClass="date" id="saleTimeEnd%{index}" cssStyle="width:80px"/>
                </span>
                </s:else>
              </td>
              <th><s:text name="global.page.saleCount" /></th>
              <td>
              	<span>
              	<s:if test="%{disableConditionMap.saleCountStart != null || disableConditionMap.saleCountEnd != null}">
              	<s:textfield name="saleCountStart" cssClass="text" cssStyle="width:75px;" disabled="true"/>-<s:textfield name="saleCountEnd" cssClass="text" cssStyle="width:75px;" disabled="true"/>
              	<s:hidden name="saleCountStart"></s:hidden>
              	<s:hidden name="saleCountEnd"></s:hidden>
              	</s:if>
              	<s:else>
              	<s:textfield name="saleCountStart" cssClass="text" cssStyle="width:75px;" />-<s:textfield name="saleCountEnd" cssClass="text" cssStyle="width:75px;" />
              	</s:else>
              	</span>
              </td>
            </tr>
            <tr class='hasSale <s:if test="%{isSaleFlag == 0}">hide</s:if>'>
              <th><s:text name="global.page.saleCounter" /></th>
              <td colspan=3> 
              <span>
                <s:hidden name="saleRegionId"></s:hidden>
			    <s:hidden name="saleProvinceId"></s:hidden>
                <s:hidden name="saleCityId"></s:hidden>
                <s:hidden name="saleMemCounterId"></s:hidden>
                <s:hidden name="saleChannelRegionId"></s:hidden>
                <s:hidden name="saleChannelId"></s:hidden>
                <s:hidden name="saleModeFlag"></s:hidden>
                <s:hidden name="saleCouValidFlag"></s:hidden>
                <s:hidden name="saleRegionName"></s:hidden>
                <s:hidden name="saleBelongId"></s:hidden>
                <span id="saleRegionNameDiv" style="line-height: 18px;">
                <s:if test='%{saleRegionName != null && !"".equals(saleRegionName)}'>
		        <s:property value="%{saleRegionName}"/>
		        <s:if test="%{disableConditionMap.saleModeFlag != null}"></s:if>
		        <s:else>
		        <span class="close" style="margin: 0 10px 2px 5px;" onclick="popmemsearch.delSaleProvinceHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		        </s:else>
		        </s:if>
                </span>
                <s:if test="%{disableConditionMap.saleModeFlag != null}"></s:if>
                <s:else>
                <s:url action="BINOLCM02_initRegionDialog" namespace="/common" id="initRegionDialogUrl"></s:url>
		        <a class="add" onclick="popmemsearch.popSaleRegionDialog('${initRegionDialogUrl}',this);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
                </s:else>
              </span>
              </td>
            </tr>
            <tr class="hasSale">
              <th><s:text name="global.page.payQuantity" /></th>
              <td>
              	<span>
                <s:if test="%{disableConditionMap.payQuantityStart != null || disableConditionMap.payQuantityEnd != null}">
                <s:textfield name="payQuantityStart" cssClass="text" cssStyle="width:75px;" disabled="true"/>-<s:textfield name="payQuantityEnd" cssClass="text" cssStyle="width:75px;" disabled="true"/>
                <s:hidden name="payQuantityStart"></s:hidden>
                <s:hidden name="payQuantityEnd"></s:hidden>
                </s:if>
                <s:else>
                <s:textfield name="payQuantityStart" cssClass="text" cssStyle="width:75px;" />-<s:textfield name="payQuantityEnd" cssClass="text" cssStyle="width:75px;" />
                </s:else>
                </span>
              </td>
              <th><s:text name="global.page.payAmount" /></th>
              <td>
                <span>
                <s:if test="%{disableConditionMap.payAmountStart != null || disableConditionMap.payAmountEnd != null}">
                <s:textfield name="payAmountStart" cssClass="text" cssStyle="width:75px;" disabled="true"/>-<s:textfield name="payAmountEnd" cssClass="text" cssStyle="width:75px;" disabled="true"/>
                <s:hidden name="payAmountStart"></s:hidden>
                <s:hidden name="payAmountEnd"></s:hidden>
                </s:if>
                <s:else>
                <s:textfield name="payAmountStart" cssClass="text" cssStyle="width:75px;" />-<s:textfield name="payAmountEnd" cssClass="text" cssStyle="width:75px;" />
                </s:else>
                </span>
			  </td>
            </tr>
            <tr class='hasSale <s:if test="%{isSaleFlag == 0}">hide</s:if>'>
              <th><s:text name="global.page.saleProduct" /></th>
              <td colspan="3">
			   	<s:if test="%{disableConditionMap.buyPrtVendorId != null || disableConditionMap.buyCateValId != null}">
			   	<div class="right">
			   		<s:text name="global.page.relation" />
			   		<input id="relation2" type="radio" value="2" name="relation" <s:if test="%{relation == null || relation == 2}">checked</s:if> disabled="disabled">
					<label for="relation2"><s:text name="global.page.relation2" /></label>
			   		<input id="relation1" type="radio" value="1" name="relation" <s:if test="%{relation == 1}">checked</s:if> disabled="disabled">
					<label for="relation1"><s:text name="global.page.relation1" /></label>
					<s:hidden name="relation"></s:hidden>
			   	</div>
			   	</s:if>
			   	<s:else>
			   	<div class="right" style="margin:2px 0px;">
					<a class="add" onClick="popmemsearch.openProDialog(this, '${index}','0');return false;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selPro" /></span>
					</a>
					<a class="add" onClick="popmemsearch.openProTypeDialog(this, '${index}','0');return false;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selProCat" /></span>
					</a>
				</div>
				<div class="right">
			   		<s:text name="global.page.relation" />
			   		<input id="relation2" type="radio" value="2" name="relation" <s:if test="%{relation == null || relation == 2}">checked</s:if>>
					<label for="relation2"><s:text name="global.page.relation2" /></label>
			   		<input id="relation1" type="radio" value="1" name="relation" <s:if test="%{relation == 1}">checked</s:if>>
					<label for="relation1"><s:text name="global.page.relation1" /></label>
			   	</div>
			   	</s:else>
				<table cellspacing="0" cellpadding="0" style="width:100%;" id="proTable${index}" class='<s:if test="%{proInfoList == null || proInfoList.isEmpty()}">hide</s:if>'>
				  <thead>
					<tr>
					  <th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
					  <th style="width:35%;"><s:text name="global.page.productname" /></th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="proShowDiv${index}">
				  	<s:iterator value="proInfoList" id="proInfoMap">
				  	<tr>
				  	  <td class="hide"><input type="hidden" name="prtVendorId" value='<s:property value="prtVendorId"/>'/><input type="hidden" name="buyPrtVendorId" value='<s:property value="prtVendorId"/>'/></td>
				  	  <td style="width:25%;"><s:property value="unitCode"/></td>
				  	  <td style="width:25%;"><s:property value="barCode"/></td>
				  	  <td style="width:35%;"><s:property value="nameTotal"/></td>
				  	  <td class="center" style="width:15%;">
				  	    <s:if test="%{disableConditionMap.buyPrtVendorId != null || disableConditionMap.buyCateValId != null}"></s:if>
				  	    <s:else>
				  	    <a href="javascript:void(0);" onClick="popmemsearch.deleteHtml(this);return false;"><s:text name="global.page.delete" /></a>
				  	    </s:else>
				  	  </td>
				  	</tr>
				  	</s:iterator>
				  </tbody>
				</table>
				<table cellspacing="0" cellpadding="0" style="width:100%;" id="proTypeTable${index}" class='<s:if test="%{proCatInfoList == null || proCatInfoList.isEmpty()}">hide</s:if>'>
				  <thead>
					<tr>
					  <th style="width:35%;"><s:text name="global.page.cateVal" /></th>
					  <th style="width:50%;"><s:text name="global.page.cateValName" /></th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="proTypeShowDiv${index}">
				    <s:iterator value="proCatInfoList" id="proCatInfoMap">
				  	<tr>
				  	  <td class="hide"><input type="hidden" name="cateValId" value='<s:property value="cateValId"/>'/><input type="hidden" name="buyCateValId" value='<s:property value="cateValId"/>'/></td>
				  	  <td style="width:25%;"><s:property value="cateVal"/></td>
				  	  <td style="width:25%;"><s:property value="cateValName"/></td>
				  	  <td class="center" style="width:15%;">
				  	    <s:if test="%{disableConditionMap.buyPrtVendorId != null || disableConditionMap.buyCateValId != null}"></s:if>
				  	    <s:else>
				  	    <a href="javascript:void(0);" onClick="popmemsearch.deleteHtml(this);return false;"><s:text name="global.page.delete" /></a>
				  	    </s:else>
				  	  </td>
				  	</tr>
				  	</s:iterator>
				  </tbody>
				</table>
              </td>
            </tr>
            <tr class='hasSale <s:if test="%{isSaleFlag == 0}">hide</s:if>'>
              <th><s:text name="global.page.notSaleProduct" /></th>
              <td colspan="3">
			   	<s:if test="%{disableConditionMap.notPrtVendorId != null || disableConditionMap.notCateValId != null}">
			   	<div class="right">
			   		<s:text name="global.page.relation" />
			   		<input id="notRelation2" type="radio" value="2" name="notRelation" <s:if test="%{notRelation == null || notRelation == 2}">checked</s:if> disabled="disabled">
					<label for="notRelation2"><s:text name="global.page.relation2" /></label>
			   		<input id="notRelation1" type="radio" value="1" name="notRelation" <s:if test="%{notRelation == 1}">checked</s:if> disabled="disabled">
					<label for="notRelation1"><s:text name="global.page.relation1" /></label>
					<s:hidden name="notRelation"></s:hidden>
			   	</div>
			   	</s:if>
			   	<s:else>
			   	<div class="right" style="margin:2px 0px;">
					<a class="add" onClick="popmemsearch.openProDialog(this, '${index}','1');return false;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selPro" /></span>
					</a>
					<a class="add" onClick="popmemsearch.openProTypeDialog(this, '${index}','1');return false;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selProCat" /></span>
					</a>
				</div>
				<div class="right">
			   		<s:text name="global.page.relation" />
			   		<input id="notRelation2" type="radio" value="2" name="notRelation" <s:if test="%{notRelation == null || notRelation == 2}">checked</s:if>>
					<label for="notRelation2"><s:text name="global.page.relation2" /></label>
			   		<input id="notRelation1" type="radio" value="1" name="notRelation" <s:if test="%{notRelation == 1}">checked</s:if>>
					<label for="notRelation1"><s:text name="global.page.relation1" /></label>
			   	</div>
			   	</s:else>
				<table cellspacing="0" cellpadding="0" style="width:100%;" id="notProTable${index}" class='<s:if test="%{notProInfoList == null || notProInfoList.isEmpty()}">hide</s:if>'>
				  <thead>
					<tr>
					  <th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
					  <th style="width:35%;"><s:text name="global.page.productname" /></th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="notProShowDiv${index}">
				  	<s:iterator value="notProInfoList" id="proInfoMap">
				  	<tr>
				  	  <td class="hide"><input type="hidden" name="prtVendorId" value='<s:property value="prtVendorId"/>'/><input type="hidden" name="notPrtVendorId" value='<s:property value="prtVendorId"/>'/></td>
				  	  <td style="width:25%;"><s:property value="unitCode"/></td>
				  	  <td style="width:25%;"><s:property value="barCode"/></td>
				  	  <td style="width:35%;"><s:property value="nameTotal"/></td>
				  	  <td class="center" style="width:15%;">
				  	    <s:if test="%{disableConditionMap.notPrtVendorId != null || disableConditionMap.notCateValId != null}"></s:if>
				  	    <s:else>
				  	    <a href="javascript:void(0);" onClick="popmemsearch.deleteHtml(this);return false;"><s:text name="global.page.delete" /></a>
				  	    </s:else>
				  	  </td>
				  	</tr>
				  	</s:iterator>
				  </tbody>
				</table>
				<table cellspacing="0" cellpadding="0" style="width:100%;" id="notProTypeTable${index}" class='<s:if test="%{notProCatInfoList == null || notProCatInfoList.isEmpty()}">hide</s:if>'>
				  <thead>
					<tr>
					  <th style="width:35%;"><s:text name="global.page.cateVal" /></th>
					  <th style="width:50%;"><s:text name="global.page.cateValName" /></th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="notProTypeShowDiv${index}">
				    <s:iterator value="notProCatInfoList" id="proCatInfoMap">
				  	<tr>
				  	  <td class="hide"><input type="hidden" name="cateValId" value='<s:property value="cateValId"/>'/><input type="hidden" name="notCateValId" value='<s:property value="cateValId"/>'/></td>
				  	  <td style="width:25%;"><s:property value="cateVal"/></td>
				  	  <td style="width:25%;"><s:property value="cateValName"/></td>
				  	  <td class="center" style="width:15%;">
				  	    <s:if test="%{disableConditionMap.notPrtVendorId != null || disableConditionMap.notCateValId != null}"></s:if>
				  	    <s:else>
				  	    <a href="javascript:void(0);" onClick="popmemsearch.deleteHtml(this);return false;"><s:text name="global.page.delete" /></a>
				  	    </s:else>
				  	  </td>
				  	</tr>
				  	</s:iterator>
				  </tbody>
				</table>
              </td>
            </tr>
            
            <tr class='notSale <s:if test="%{isSaleFlag == null || isSaleFlag == 1}">hide</s:if>'>
              <th><s:text name="global.page.notSaleTime" /></th>
              <td colspan="3" style="width:85%">
                <s:if test="%{disableConditionMap.notSaleTimeMode != null && disableConditionMap.notSaleTimeMode != -1}">
                <span>
                <select name="notSaleTimeMode" onchange="popmemsearch.selectDateMode(this, 1);" disabled="disabled">
					<option value="-1" <s:if test="%{notSaleTimeMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="0" <s:if test="%{notSaleTimeMode == 0}">selected</s:if>><s:text name="global.page.dynamic"/></option>
					<option value="9" <s:if test="%{notSaleTimeMode == 9}">selected</s:if>><s:text name="global.page.custom"/></option>
					<option value="8" <s:if test="%{notSaleTimeMode == 8}">selected</s:if>><s:text name="global.page.lastSaleDay"/></option>
				</select>
				<s:hidden name="notSaleTimeMode"></s:hidden>
				</span>
                <span class='<s:if test="%{notSaleTimeMode != 0}">hide</s:if>'>
                <s:text name="global.page.notSaleDateDes" />
                <s:textfield name="notSaleTimeRange" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield>
                <s:select list='#application.CodeTable.getCodes("1239")' listKey="CodeKey" listValue="Value" name="notSaleTimeUnit" cssStyle="width:auto;" disabled="true"></s:select>
                <s:text name="global.page.notSaleTimeUnitFlag" />
                <s:hidden name="notSaleTimeRange"></s:hidden>
                <s:hidden name="notSaleTimeUnit"></s:hidden>
                </span>
                <span class='<s:if test="%{notSaleTimeMode != 9}">hide</s:if>'>
                <s:textfield name="notSaleTimeStart" cssClass="date" id="notSaleTimeStartTemp%{index}" cssStyle="width:80px" disabled="true"/>-<s:textfield name="notSaleTimeEnd" cssClass="date" id="notSaleTimeEndTemp%{index}" cssStyle="width:80px" disabled="true"/>
                <s:hidden name="notSaleTimeStart" id="notSaleTimeStartTemp%{index}"></s:hidden>
                <s:hidden name="notSaleTimeEnd" id="notSaleTimeEndTemp%{index}"></s:hidden>
                </span>
                <span class='<s:if test="%{notSaleTimeMode != 8}">hide</s:if>'>
                <s:text name="global.page.notSaleDateDes" />
                <s:textfield name="notSaleTimeRangeLast" cssClass="text" cssStyle="width:30px;" disabled="true"></s:textfield>
                <s:text name="global.page.Day"/>
                <s:text name="global.page.notSaleTimeUnitFlag" />
                <s:hidden name="notSaleTimeRangeLast"></s:hidden>
                </span>
                </s:if>
                <s:else>
                <span>
                <select name="notSaleTimeMode" onchange="popmemsearch.selectDateMode(this, 1);">
					<option value="-1" <s:if test="%{notSaleTimeMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="0" <s:if test="%{notSaleTimeMode == 0}">selected</s:if>><s:text name="global.page.dynamic"/></option>
					<option value="9" <s:if test="%{notSaleTimeMode == 9}">selected</s:if>><s:text name="global.page.custom"/></option>
					<option value="8" <s:if test="%{notSaleTimeMode == 8}">selected</s:if>><s:text name="global.page.lastSaleDay"/></option>
				</select>
				</span>
                <span class='<s:if test="%{notSaleTimeMode != 0}">hide</s:if>'>
                <s:text name="global.page.notSaleDateDes" />
                <s:textfield name="notSaleTimeRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
                <s:select list='#application.CodeTable.getCodes("1239")' listKey="CodeKey" listValue="Value" name="notSaleTimeUnit" cssStyle="width:auto;"></s:select>
                <s:text name="global.page.notSaleTimeUnitFlag" />
                </span>
                <span class='<s:if test="%{notSaleTimeMode != 9}">hide</s:if>'>
                <s:textfield name="notSaleTimeStart" cssClass="date" id="notSaleTimeStart%{index}" cssStyle="width:80px"/>-<s:textfield name="notSaleTimeEnd" cssClass="date" id="notSaleTimeEnd%{index}" cssStyle="width:80px"/>
                </span>
                <span class='<s:if test="%{notSaleTimeMode != 8}">hide</s:if>'>
                <s:text name="global.page.notSaleDateDes" />
                <s:textfield name="notSaleTimeRangeLast" cssClass="text" cssStyle="width:30px;"></s:textfield>
                <s:text name="global.page.Day"/>
                <s:text name="global.page.notSaleTimeUnitFlag" />
                </span>
                </s:else>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
	</div>
	
	<div class="box2 box2-active moreCondition">
	  <div class="box2-header clearfix"><strong class="active left"><s:text name="global.page.campaignRecord" /></strong></div>
      <div class="box2-content terminal">
        <table class="detail" cellpadding="0" cellspacing="0" style="margin-bottom:0px;">
          <tbody>
            <tr>
              <th><s:text name="global.page.campaignTime" /></th>
              <td>
              <s:if test="%{disableConditionMap.participateTimeStart != null || disableConditionMap.participateTimeEnd != null}">
              <s:textfield name="participateTimeStart" id="participateTimeStartTemp%{index}" cssClass="date" cssStyle="width:80px" disabled="true"/>-<s:textfield name="participateTimeEnd" id="participateTimeEndTemp%{index}" cssClass="date" cssStyle="width:80px" disabled="true"/>
              <s:hidden name="participateTimeStart" id="participateTimeStartTemp%{index}"></s:hidden>
              <s:hidden name="participateTimeEnd" id="participateTimeEndTemp%{index}"></s:hidden>
              </s:if>
              <s:else>
              <s:textfield name="participateTimeStart" id="participateTimeStart%{index}" cssClass="date" cssStyle="width:80px"/>-<s:textfield name="participateTimeEnd" id="participateTimeEnd%{index}" cssClass="date" cssStyle="width:80px"/>
              </s:else>
              </td>
              <th><s:text name="global.page.campaignCounter" /></th>
              <td>
                <s:hidden name="campaignCounterId"></s:hidden>
                <s:hidden name="campaignCounterName"></s:hidden>
                <span id="campaignCounterDiv" style="line-height: 18px;">
                  <s:if test='%{campaignCounterName != null && !"".equals(campaignCounterName)}'>
			      <s:property value="%{campaignCounterName}"/>
			      <s:if test="%{disableConditionMap == null || disableConditionMap.campaignCounterId == null}">
			      <span class="close" style="margin: 0 10px 2px 5px;" onclick="popmemsearch.delCampaignCounterHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>			      
			      </s:if>
			      </s:if>
                </span>
                <s:if test="%{disableConditionMap == null || disableConditionMap.campaignCounterId == null}">
                <s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
                <a class="add" onclick="popmemsearch.popCampaignCounterList('${searchCounterInitUrl}',this);return false;">
				  <span class="ui-icon icon-search"></span>
				  <span class="button-text"><s:text name="global.page.Popselect" /></span>
			    </a>
			    </s:if>
              </td>
            </tr>
            <tr>
              <th><s:text name="global.page.campaignName" /></th>
              <td colspan="3">
                <s:hidden name="campaignMode"></s:hidden>
                <s:hidden name="campaignCode"></s:hidden>
                <s:hidden name="campaignName"></s:hidden>
                <span id="campaignDiv" style="line-height: 18px;">
                  <s:if test='%{campaignName != null && !"".equals(campaignName)}'>
			      <s:property value="%{campaignName}"/>
			      <s:if test="%{disableConditionMap == null || disableConditionMap.campaignCode == null}">
			      <span class="close" style="margin: 0 10px 2px 5px;" onclick="popmemsearch.delCampaignHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>			      
			      </s:if>
			      </s:if>
                </span>
                <s:if test="%{disableConditionMap == null || disableConditionMap.campaignCode == null}">
                <s:url action="BINOLCM02_initCampaignDialog" namespace="/common" id="searchCampaignInitUrl"></s:url>
                <a class="add" onclick="popmemsearch.popCampaignList('${searchCampaignInitUrl}',this);return false;">
				  <span class="ui-icon icon-search"></span>
				  <span class="button-text"><s:text name="global.page.Popselect" /></span>
			    </a>
			    </s:if>
              </td>
            </tr>
            <tr>
              <th><s:text name="global.page.campaignState" /></th>
              <td colspan="3">
                <span>
				<s:if test="%{disableConditionMap.campaignState != null}">
				<s:checkboxlist list='#application.CodeTable.getCodes("1116")' listKey="CodeKey" listValue="Value" name="campaignState" value="%{campaignState}" disabled="true"></s:checkboxlist>
				<s:iterator value="campaignState" id="_campaignState">
				<s:hidden name="campaignState" value="%{#_campaignState}"></s:hidden>
				</s:iterator>
				</s:if>
				<s:else>
				<s:checkboxlist list='#application.CodeTable.getCodes("1116")' listKey="CodeKey" listValue="Value" name="campaignState" value="%{campaignState}"></s:checkboxlist>
				</s:else>
				</span>
              </td>
            </tr>
          </tbody>
        </table>      
      </div>
	</div>
	              
	<%-- 
	<div class="advanced" style="border-top:none;">
	  <div>
		<a href="javascript:void(0);" onFocus="this.blur();" onClick="popmemsearch.moreCondition(this);return false;">
		  <s:text name="global.page.moreCondition" />
		</a>
		<span class="arrow_down"></span>
	  </div>
	</div>
	<s:url action="BINOLCM33_conditionDisplay" id="conditionDisplayUrl">
	</s:url>
	<a href="${conditionDisplayUrl }" id="conditionDisplayUrl"></a>
	<div id="conditionDisplayDiv"></div>
	--%> 
	

	<div class="hide">
	  <div id="moreCondition"><s:text name="global.page.moreCondition" /></div>
	  <div id="baseCondition"><s:text name="global.page.baseCondition" /></div>
	  <div id="deleteButton"><s:text name="global.page.delete" /></div>
	  <div id="selExclusiveFlag1"><s:text name="global.page.selExclusiveFlag1" /></div>
	  <div id="selExclusiveFlag2"><s:text name="global.page.selExclusiveFlag2" /></div>
	  <s:url action="BINOLCM33_init" id="conditionInitUrl">
	    <s:param name="index" value="%{index}"></s:param>
	  </s:url>
	  <a href="${conditionInitUrl }" id="conditionInitUrl"></a>
	  <s:url action="BINOLCM33_searchLevel" id="searchLevel_Url"></s:url>
	  <a href="${searchLevel_Url }" id="searchLevelUrl"></a>
	</div>

</div>
</div>