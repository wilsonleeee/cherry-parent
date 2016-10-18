<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/bs/emp/BINOLBSEMP06_1.js"></script>
<%-- 实时刷新数据权限URL --%>
<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init" />
<s:hidden value="%{#refreshPrivilegeUrl}" id="refreshPrivilegeUrl"></s:hidden>
<%-- 保存编辑 --%>
<s:url id="saveEdit_url" value="/basis/BINOLBSEMP06_saveEdit"></s:url>

<s:i18n name="i18n.bs.BINOLBSEMP06">
<%--请选择 --%>
<s:text id="global.select" name="global.page.select"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
	<div class="panel-header">
		<div class="clearfix">
		<span class="breadcrumb left"> 
			<span class="ui-icon icon-breadcrumb"></span>
			<s:text name="EMP06_manage"/> &gt; <s:text name="EMP06_title"/>
		</span>
	  	</div>
	</div>
	<%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorMessage"></div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
    <form id="editForm" method="post" class="inline">
    	<%--=================== 基本信息 ===================== --%>
        <div id="actionResultDisplay"></div>
    	<div class="section" id="b_info">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="global.page.title"/><%-- 基本信息 --%>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
                  <tr>
                  	<th><s:text name="EMP06_brandName"/><span class="highlight">*</span></th><%-- 品牌 --%>
                    <td><span>
               			<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" value="%{baInfo.brandInfoId}"></s:select>
              		</span></td>
                    <th><s:text name="EMP06_BACode"/><span class="highlight">*</span></th><%-- 员工代号 --%>
                    <td><span>
                    <s:if test="!baInfo.baCodeError && !baInfo.baCodeRuleError">
       					<s:textfield id="baCode" maxlength="15" cssClass="text" name="baCode" value="%{baInfo.baCode}"></s:textfield>
        			</s:if>
        			<s:else>
        				<s:textfield id="baCode" maxlength="15" cssClass="text" cssStyle="color:red;" name="baCode" value="%{baInfo.baCode}" onchange="BINOLBSEMP06_1.clearError(this);"></s:textfield>
        			</s:else>
                    </span></td>
                  </tr>
                  <tr>
              		<th><s:text name="EMP06_counter"/></th><%-- 柜台 --%>
                    <td>
				      	<s:hidden name="organizationID" id="organizationID" value="%{baInfo.organizationID}"></s:hidden>
				      	<s:hidden name="counterCode" id="counterCode" value="%{baInfo.counterCode}"></s:hidden>
				      	<s:hidden name="counterName" id="counterName" value="%{baInfo.counterName}"></s:hidden>
				      	<span id="showCounterName">
					      	<s:if test="!baInfo.counterCodeError && !baInfo.counterNameError">
					      		<span id="showCounterName">
					      		<s:if test='null != baInfo.counterCode && !"".equals(baInfo.counterCode)'>
	                    		(<s:property value="baInfo.counterCode"/>)
	                    		</s:if>
	                    		<s:property value="baInfo.counterName"/>
	                    		</span>
	                    	</s:if>
	                    	<s:else>
	                    		<span id="showCounterName" style="color:red;">
	                    		<s:if test='null != baInfo.counterCode && !"".equals(baInfo.counterCode)'>
	                    		(<s:property value="baInfo.counterCode"/>)
	                    		</s:if>
	                    		<s:property value="baInfo.counterName"/>
	                    		</span>
	                    	</s:else>
                    	</span>
				      	<a id="selectCounterButton" class="add right" style="margin-left:50px" onclick="BINOLBSEMP06_1.popCounter(this);return false;">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
				    </td>
				    <th><s:text name="EMP06_BAName"/><span class="highlight">*</span></th><%-- 姓名 --%>
                    <td><span>
                    	<s:if test="!baInfo.baNameError">
                    		<s:textfield id="baName" maxlength="30" cssClass="text" name="baName" value="%{baInfo.baName}"></s:textfield>
                    	</s:if>
                    	<s:else>
                    		<s:textfield id="baName" maxlength="30" cssClass="text" cssStyle="color:red;" name="baName" value="%{baInfo.baName}" onchange="BINOLBSEMP06_1.clearError(this);"></s:textfield>
                    	</s:else>
                    </span></td>
                  </tr>
                  <tr>
                    <th><s:text name="EMP06_commtDate"/></th><%-- 入职日期  --%>
                    <td class="date"><span>
                    	<s:if test="!baInfo.commtDateError">
                    		<s:textfield id="commtDate" name="commtDate" cssClass="date" value="%{baInfo.commtDate}"/>
                    	</s:if><s:else>
                    		<s:textfield id="commtDate" cssClass="date" cssStyle="color:red;" name="commtDate" value="%{baInfo.commtDate}" onchange="BINOLBSEMP06_1.clearError(this);"></s:textfield>
                    	</s:else>
                    </span></td>
                    <th><s:text name="EMP06_depDate"/></th><%-- 离职日期  --%>
                    <td class="date"><span>
	                    <s:if test="!baInfo.depDateError && !baInfo.depDateValidError">
	                    	<span><s:textfield id="depDate" name="depDate" cssClass="date" value="%{baInfo.depDate}"/></span>
	                    </s:if><s:else>
	                    	<span><s:textfield id="depDate" cssClass="date" cssStyle="color:red;" name="depDate" value="%{baInfo.depDate}" onchange="BINOLBSEMP06_1.clearError(this);"></s:textfield></span>
	                    </s:else>
                    </span></td>
                  </tr>
                  <tr>
                  	<th><s:text name="EMP06_mobilePhone"/></th>
                  	<td><span>
	                    <s:if test="!baInfo.mobilePhoneError">
	                    	<span><s:textfield id="mobilePhone" name="mobilePhone" cssClass="text" value="%{baInfo.mobilePhone}"/></span>
	                    </s:if><s:else>
	                    	<span><s:textfield id="mobilePhone" cssClass="text" cssStyle="color:red;" name="mobilePhone" value="%{baInfo.mobilePhone}" onchange="BINOLBSEMP06_1.clearError(this);"></s:textfield></span>
	                    </s:else>
                    </span></td>
                  	<th></th>
                  	<td></td>
                  </tr>
                </table>
              </div>
         </div>
         <%--===================  操作按钮Start ===================== --%>
		<div id="saveButton" class="center clearfix">
			<button class="save" onclick="BINOLBSEMP06_1.saveEdit('${saveEdit_url}');return false;">
				<span class="ui-icon icon-save"></span>
				<span class="button-text"><s:text name="global.page.save"/></span>
			</button>
			<button id="close" class="close" type="button"  onclick="window.close();return false;">
				<span class="ui-icon icon-close"></span>
				<span class="button-text"><s:text name="global.page.close"/></span>
			</button>
		</div>
		<%--===================  操作按钮END ===================== --%>
    </form>
    </div>
</div>
</div>
</s:i18n>
<div class="hidden">
	<s:hidden id="editBaCode" name="baCode"></s:hidden>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		//节日
		var holidays = '${holidays }';
		$('#commtDate').cherryDate({
			holidayObj: holidays,
			beforeShow: function(input){
				var value = $('#depDate').val();
				return [value,'maxDate'];
			}
		});
		$('#depDate').cherryDate({
			holidayObj: holidays,
			beforeShow: function(input){
				var value = $('#commtDate').val();
				return [value,'minDate'];
			}
		});
	});
</script>