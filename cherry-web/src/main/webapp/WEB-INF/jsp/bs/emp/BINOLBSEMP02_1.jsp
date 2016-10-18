<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>


<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>
<script type="text/javascript">
$(document).ready(function() {

	oTableArr = new Array(null,null);
    fixedColArr = new Array(null,null);
	$('.tabs').tabs();
    // 表格列选中
    //$('#dataTableAttendance_wrapper thead th').live('click',function(){
        //$("th.sorting").removeClass('sorting');
        //$(this).addClass('sorting');
    //});
    //点击考勤记录
	$("a@[href='#tabs-2']").click(function() {
	    //oTableArr = new Array(null,null);
	    //fixedColArr = new Array(null,null);
	    //$(window).unbind('resize');
        searchBASAttendance();
    });
    
} );

//显示或隐藏更多信息
function bsemp02_more(object) {
	if($('#empMore').is(':hidden')) {
		$('#empMore').show();
		$(object).find(".button-text").html($("#hideMoreInfo").html());
		$(object).find(".ui-icon").removeClass("ui-icon-triangle-1-s").addClass("ui-icon-triangle-1-n");
	} else {
		$('#empMore').hide();
		$(object).find(".button-text").html($("#showMoreInfo").html());
		$(object).find(".ui-icon").removeClass("ui-icon-triangle-1-n").addClass("ui-icon-triangle-1-s");
	}
}

/*
 * BAS考勤查询
 */
function searchBASAttendance(){
     if($("#employeeId").val() == "" || $("#employeeId").val() == undefined){
         $("#dataTableAttendance").addClass("hide");
    	 return false;
     }else{
    	 $("#dataTableAttendance").removeClass("hide");
     }
     var url = $("#basAttendanceUrl").attr("href");
     // 查询参数序列化
     var parentToken = parentTokenVal();
     var params = "?employeeId="+$("#employeeId").val();
     if($("#csrftoken").val() != "" && $("#csrftoken").val() != undefined){
    	 params = params+"&csrftoken=" +$("#csrftoken").val();
     }else if(parentToken != "" && parentToken != undefined){
    	 params = params+"&csrftoken=" +parentToken;
     }else{
         return false;
     }
     
     url = url + params;
     // 显示结果一览
     $("#section").show();
     // 表格设置
     var tableSetting = {
             // 表格ID
             tableId : '#dataTableAttendance',
             // 数据URL
             url : url,
             // 表格默认排序
             aaSorting : [[ 2, "desc" ]],
             // 表格列属性设置
             aoColumns : [  { "sName": "no","bSortable": false,"sWidth": "1%"},
                            { "sName": "departName","sWidth": "39%"},
                            { "sName": "arriveTime","sWidth": "30%"},
                            { "sName": "leaveTime","sWidth": "30%"}
                        ],
                            
            // 不可设置显示或隐藏的列  
            //aiExclude :[0, 1],
            // 横向滚动条出现的临界宽度
            sScrollX : "100%",
            index: 2
            // 固定列数
            //fixedColumns : 2
     };
     // 调用获取表格函数
     getTable(tableSetting);
}

/*
 * 数据权限查询
 */
function searchPrivilege(){
	if($("#employeeId").val() == "" || $("#employeeId").val() == undefined){
        $("#dataTableDepPrivilege").addClass("hide");
    	return false;
    }else{
    	$("#dataTableDepPrivilege").removeClass("hide");
    }
    var url = $("#searchPrivilegeUrl").attr("href");
    var csrftoken = $('#csrftoken').serialize();
 	if(!csrftoken) {
 		csrftoken = $('#csrftoken',window.opener.document).serialize();
 	}
 	var params = $("#privilegeForm").serialize();
    url = url + '?' + csrftoken + '&' + params;

    $("#privilegeDiv").show();
    var type = $("#privilegeForm :input[name='privilegeType'][checked]").val();
    if(type == "0") {
    	// 表格设置
        var tableSetting = {
                 // 表格ID
                 tableId : '#dataTableDepPrivilege',
                 // 数据URL
                 url : url,
                 // 表格默认排序
                 aaSorting : [[ 0, "desc" ]],
                 // 表格列属性设置
                 aoColumns : [  { "sName": "departCode","sWidth": "40%"},
                                { "sName": "type","sWidth": "30%"},
                                { "sName": "privilegeFlag","sWidth": "30%"}
                            ],
                                
                // 不可设置显示或隐藏的列  
                //aiExclude :[0, 1],
                // 横向滚动条出现的临界宽度
                sScrollX : "100%",
                iDisplayLength: 10,
                index: 0
                // 固定列数
                //fixedColumns : 2
        };
        // 调用获取表格函数
        getTable(tableSetting);
    } else {
    	// 表格设置
        var tableSetting = {
                 // 表格ID
                 tableId : '#dataTableEmpPrivilege',
                 // 数据URL
                 url : url,
                 // 表格默认排序
                 aaSorting : [[ 0, "desc" ]],
                 // 表格列属性设置
                 aoColumns : [  { "sName": "employeeCode","sWidth": "40%"},
                                { "sName": "departName","sWidth": "20%"},
                                { "sName": "categoryName","sWidth": "20%"},
                                { "sName": "privilegeFlag","sWidth": "20%"}
                            ],
                                
                // 不可设置显示或隐藏的列  
                //aiExclude :[0, 1],
                // 横向滚动条出现的临界宽度
                sScrollX : "100%",
                iDisplayLength: 10,
                index: 1
                // 固定列数
                //fixedColumns : 2
        };
        // 调用获取表格函数
        getTable(tableSetting);
    }
}

function changePrivilegeType(object) {

	var type = $(object).val();
	$("#departCode").val("");
	$("#departName").val("");
	$("#businessType").val("A");
	$("#operationType").val("1");
	$("#privilegeDiv").hide();
	if(type == "0") {
		$("#depPrivilegeDiv").show();
		$("#empPrivilegeDiv").hide();
	} else {
		$("#empPrivilegeDiv").show();
		$("#depPrivilegeDiv").hide();
	}
}

</script>
<%-- 登陆用户员工ID --%>
<s:set id="empId1" value="#session.userinfo.BIN_EmployeeID"/>
<%-- 编辑员工ID --%>
<s:set id="empId2" value="employeeInfo.employeeId"/>
<s:i18n name="i18n.bs.BINOLBSEMP02">
        <div class="tabs">
          <ul>
            <li><a href="#tabs-1"><s:text name="global.page.title"/></a></li><%-- 基本信息 --%>
            <li><a href="#tabs-2"><s:text name="emp_attendance"/></a></li><%-- 考勤记录 --%>
            <%--<li><a href="#tabs-3"><s:text name="emp_turnover"/></a></li>--%><%-- 营业额 --%>
            <li><a href="#tabs-4"><s:text name="emp_privilege"/></a></li><%-- 数据权限 --%>
          </ul>
          <cherry:form id="mainForm" method="post" class="inline" csrftoken="false" action="BINOLBSEMP03_init">
          <s:hidden name="employeeId" value="%{employeeInfo.employeeId}"/>
          <s:hidden name="fromPage" value="1"></s:hidden>
          <input type="hidden" id="parentCsrftoken" name="csrftoken"/>
          <s:hidden name="higher" value="%{employeeInfo.higher}" />
          <%-- ====================== 基本信息DIV开始 ===================== --%>
          <div id="tabs-1">
            <div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info"></span>
              		<s:text name="global.page.title"/><%-- 基本信息 --%>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
                  <tr>
                    <th><s:text name="employee.code"/></th><%-- 员工代号 --%>
                    <td><span><s:property value="employeeInfo.employeeCode"/></span></td>
                    <th><s:text name="employee.name"/></th><%-- 姓名 --%>
                    <td><span><s:property value='employeeInfo.employeeName'/></span></td>
                  </tr>
                  <tr>
                  	<th><s:text name="employee.brand"/></th><%-- 品牌 --%>
                    <td><span>
                    <s:if test="%{employeeInfo.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
                    <s:else><s:property value='employeeInfo.brandName'/></s:else>
                    </span></td>
                    <th><s:text name="employee.foreignName"/></th><%-- 外文名 --%>
                    <td><span><s:property value='employeeInfo.employeeNameForeign'/></span></td>
                  </tr>
                  <tr>
                  	<th><s:text name="employee.post"/></th><%-- 岗位 --%>
                    <td><span><s:property value='employeeInfo.categoryName'/></span></td>
                    <th><s:text name="employee.phone"/></th><%-- 联系电话 --%>
                    <td><span><s:property value='employeeInfo.phone'/></span></td>
                  </tr>
                  <tr>
                    <th><s:text name="employee.depart"/></th><%-- 部门 --%>
                    <td><span>
                    <s:if test='%{employeeInfo.departCode != null && !"".equals(employeeInfo.departCode)}'>
                    (<s:property value="employeeInfo.departCode"/>)<s:property value='employeeInfo.departName'/>
                    </s:if>
                    </span></td>
                    <th><s:text name="employee.mobilePhone"/></th><%-- 手机 --%>
                    <td><span><s:property value='employeeInfo.mobilePhone'/></span></td>
                  </tr>
                  <tr>
                    <th><s:text name="employee.email"/></th><%-- 电子邮箱 --%>
                    <td><span><s:property value='employeeInfo.email'/></span></td>
                    <th><s:text name="employee.identityCard"/></th><%-- 身份证号 --%>
                    <td><span><s:property value='employeeInfo.identityCard'/></span></td>
                  </tr>
                </table>
             </div>
          </div>
          <s:if test="%{employeeInfo.userId == null}">
          <div class="section" id="b_longin">
            <div class="section-header">
            	<strong>
            		<span class="ui-icon icon-ttl-section-info"></span>
            		<s:text name="employee.longinName"/>
            	</strong>
            </div>
            <div class="section-content">
              <table class="detail" cellpadding="0" cellspacing="0">
                <tr>
                  <th style="width: 15%"><s:text name="employee.longinName"/></th><%-- 登录帐号 --%>
                  <td style="width: 85%"><span><s:text name="employee.noLonginName"/></span></td>
                </tr>
              </table>
          	</div>
          </div>
          </s:if>
          <s:else>
          <div class="section" id="b_longin">
            <div class="section-header">
            	<strong>
            		<span class="ui-icon icon-ttl-section-info"></span>
            		<s:text name="employee.longinName"/>
            	</strong>
            </div>
            <div class="section-content">
              <table class="detail" cellpadding="0" cellspacing="0">
                <s:if test='%{createBIUser != null && "true".equals(createBIUser)}'>
                <tr>
                  <th><s:text name="employee.longinName"/></th><%-- 登录帐号 --%>
                  <td><span><s:property value="employeeInfo.longinName"/></span></td>
                  <th><s:text name="employee.biFlag"/></th>
                  <td><span><s:if test="%{employeeInfo.biFlag == 1}"><s:text name="employee.biFlag1"/></s:if><s:else><s:text name="employee.biFlag2"/></s:else></span></td>
                </tr>
                <tr>
                  <th><s:text name="employee.isEnable"/></th>
                  <td><span><s:if test="%{employeeInfo.validFlag == 1}"><s:text name="employee.enable"/></s:if><s:else><s:text name="employee.disnable"/></s:else></span></td>
                </tr>
                </s:if>
                <s:else>
                <tr>
                  <th><s:text name="employee.longinName"/></th><%-- 登录帐号 --%>
                  <td><span><s:property value="employeeInfo.longinName"/></span></td>
                  <th><s:text name="employee.isEnable"/></th>
                  <td><span><s:if test="%{employeeInfo.validFlag == 1}"><s:text name="employee.enable"/></s:if><s:else><s:text name="employee.disnable"/></s:else></span></td>
                </tr>
                </s:else>
              </table>
          	</div>
          </div>
          </s:else>
          
          
          <%--=================== 上司 ===================== --%>
          <div class="section">
              <div class="section-header clearfix">
              	<strong class="left">
            		<span class="ui-icon icon-ttl-section-info"></span>
            		<s:text name="employee.higher"/>
            	</strong>
              </div>
              <div class="section-content">
				<table class="detail" cellpadding="0" cellspacing="0">
				  <tr>
                   	<th style="width: 15%"><s:text name="employee.higher"/></th><%-- 上司 --%>
                    <td style="width: 85%"><s:property value='employeeInfo.higherName'/></td>
                  </tr>
				</table>
			  </div>
            </div>
            
            <%--=================== 关注用户信息 ===================== --%>
	      <div class="section" id="b_likeEmployee">
            <div class="section-header clearfix">
            	<strong class="left">
            		<span class="ui-icon icon-ttl-section-info"></span>
            		<s:text name="employee.likeEmployee"/>
            	</strong>
            </div>
	        <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="40%"><s:text name="employee.name"/></th>
				      <th width="30%"><s:text name="employee.depart"/></th>
				      <th width="30%"><s:text name="employee.post"/></th>
				    </tr>
				  </thead>
				  <tbody>
				    <s:iterator value="likeEmployeeList" id="likeEmployeeMap">
				    <tr>
				    <td><s:property value="#likeEmployeeMap.employeeName"/></td>
				    <td><s:property value="#likeEmployeeMap.departName"/></td>
				    <td><s:property value="#likeEmployeeMap.categoryName"/></td>
				    </tr>
				    </s:iterator>
				  </tbody>
				</table>
		  	</div>
	      </div>
          
          <%--=================== 管辖部门信息 ===================== --%>
	      <div class="section" id="b_followDepart">
            <div class="section-header clearfix">
            	<strong class="left">
            		<span class="ui-icon icon-ttl-section-info"></span>
            		<s:text name="employee.followDepart"/>
            	</strong>
            </div>
	        <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="employee.departCode"/></th>
				      <th width="40%"><s:text name="employee.departName"/></th>
				      <th width="20%"><s:text name="employee.departType"/></th>
				      <th width="10%"><s:text name="global.page.validFlag"/></th>
				    </tr>
				  </thead>
				  <tbody>
				    <s:iterator value="employeeDepartList" id="employeeDepartMap">
				    <s:if test="%{#employeeDepartMap.manageType == 1}">
				    <tr>
				    <td>
				    <s:if test="%{modeFlg != null}">
				    <s:url action="BINOLBSDEP02_init" id="organizationInfoUrl">
						<s:param name="organizationId" value="%{#employeeDepartMap.organizationId}"></s:param>
					</s:url>
				    <s:url action="BINOLBSCNT02_init" id="counterDetailUrl">
						<s:param name="organizationId" value="%{#employeeDepartMap.organizationId}"></s:param>
					</s:url>
					<s:if test="%{#employeeDepartMap.departType == 4}">
					<a href="${counterDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;" style="color: #3366FF;">
						<s:property value="#employeeDepartMap.departCode"/>
					</a>
					</s:if>
					<s:else>
					<a href="${organizationInfoUrl }" class="popup" onclick="javascript:openWin(this);return false;" style="color: #3366FF;">
						<s:property value="#employeeDepartMap.departCode"/>
					</a>
					</s:else>
					</s:if>
					<s:else>
					<s:property value="#employeeDepartMap.departCode"/>
					</s:else>
				    </td>
				    <td><s:property value="#employeeDepartMap.departName"/></td>
				    <td><s:property value='#application.CodeTable.getVal("1000", #employeeDepartMap.departType)' /></td>
				    <td class="center">
						<s:if test='"1".equals(validFlag)'>
							<span class='ui-icon icon-valid' title="启用"></span>
						</s:if><%-- 有效区分 --%>
						<s:else>
							<span class='ui-icon icon-invalid' title="停用"></span>
						</s:else>
				    </td>
				    </tr>
				    </s:if>
				    </s:iterator>
				  </tbody>
				</table>
		  	</div>
	      </div>
	      
	      <%--=================== 关注部门信息 ===================== --%>
	      <div class="section" id="b_likeDepart">
            <div class="section-header clearfix">
            	<strong class="left">
            		<span class="ui-icon icon-ttl-section-info"></span>
            		<s:text name="employee.likeDepart"/>
            	</strong>
            </div>
	        <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="employee.departCode"/></th>
				      <th width="40%"><s:text name="employee.departName"/></th>
				      <th width="20%"><s:text name="employee.departType"/></th>
				      <th width="10%"><s:text name="global.page.validFlag"/></th>
				    </tr>
				  </thead>
				  <tbody>
				    <s:iterator value="employeeDepartList" id="employeeDepartMap">
				    <s:if test="%{#employeeDepartMap.manageType == 0}">
				    <tr>
				    <td>
				    <s:if test="%{modeFlg != null}">
				    <s:url action="BINOLBSDEP02_init" id="organizationInfoUrl">
						<s:param name="organizationId" value="%{#employeeDepartMap.organizationId}"></s:param>
					</s:url>
				    <s:url action="BINOLBSCNT02_init" id="counterDetailUrl">
						<s:param name="organizationId" value="%{#employeeDepartMap.organizationId}"></s:param>
					</s:url>
					<s:if test="%{#employeeDepartMap.departType == 4}">
					<a href="${counterDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;" style="color: #3366FF;">
						<s:property value="#employeeDepartMap.departCode"/>
					</a>
					</s:if>
					<s:else>
					<a href="${organizationInfoUrl }" class="popup" onclick="javascript:openWin(this);return false;" style="color: #3366FF;">
						<s:property value="#employeeDepartMap.departCode"/>
					</a>
					</s:else>
					</s:if>
					<s:else>
						<s:property value="#employeeDepartMap.departCode"/>
					</s:else>
				    </td>
				    <td><s:property value="#employeeDepartMap.departName"/></td>
				    <td><s:property value='#application.CodeTable.getVal("1000", #employeeDepartMap.departType)' /></td>
				    <td class="center">
						<s:if test='"1".equals(validFlag)'>
							<span class='ui-icon icon-valid' title="启用"></span>
						</s:if><%-- 有效区分 --%>
						<s:else>
							<span class='ui-icon icon-invalid' title="停用"></span>
						</s:else>
				    </td>
				    </tr>
				    </s:if>
				    </s:iterator>
				  </tbody>
				</table>
		  	</div>
	      </div>
	      
	      <%--=================== 被关注用户信息 ===================== --%>
	      <div class="section">
            <div class="section-header clearfix">
            	<strong class="left">
            		<span class="ui-icon icon-ttl-section-info"></span>
            		<s:text name="employee.beLikedEmployee"/>
            	</strong>
            </div>
	        <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="40%"><s:text name="employee.name"/></th>
				      <th width="30%"><s:text name="employee.depart"/></th>
				      <th width="30%"><s:text name="employee.post"/></th>
				    </tr>
				  </thead>
				  <tbody>
				    <s:iterator value="beLikedEmployeeList" id="beLikedEmployeeMap">
				    <tr>
				    <td><s:property value="#beLikedEmployeeMap.employeeName"/></td>
				    <td><s:property value="#beLikedEmployeeMap.departName"/></td>
				    <td><s:property value="#beLikedEmployeeMap.categoryName"/></td>
				    </tr>
				    </s:iterator>
				  </tbody>
				</table>
		  	</div>
	      </div>
          
          <%--=================== 更多信息 ===================== --%>  
          <div class="section">
            <div class="section-header clearfix">
            	<span id="hideMoreInfo" class="hide"><s:text name="employee.hideMoreInfo" /></span>
            	<span id="showMoreInfo" class="hide"><s:text name="employee.showMoreInfo" /></span>
            	<a class="add left" href="#" onclick="bsemp02_more(this); return false;">
      				<span class="ui-icon ui-icon-triangle-1-s"></span><span class="button-text"><s:text name="employee.showMoreInfo"/></span>
      			</a>
            </div>
            <div class="section-content hide" id="empMore">
              	<%--=================== 用户其他信息 ===================== --%>
	            <div class="section">
	              <div class="section-content">
					<table class="detail" cellpadding="0" cellspacing="0">
	                  <tr>
	                    <th><s:text name="employee.birthDay"/></th><%-- 生日 --%>
                    	<td><span><s:property value='employeeInfo.birthDay'/></span></td>
                    	<th><s:text name="employee.gender"/></th><%-- 性别 --%>
                    	<td><span><s:property value='#application.CodeTable.getVal("1006", employeeInfo.gender)'/></span></td>
	                  </tr>
	                  <tr>
	                    <th><s:text name="employee.academic"/></th><%-- 学历 --%>
                    	<td><span><s:property value='#application.CodeTable.getVal("1042", employeeInfo.academic)'/></span></td>
                    	<th><s:text name="employee.maritalStatus"/></th><%-- 婚姻状况 --%>
                    	<td><span><s:property value='#application.CodeTable.getVal("1043", employeeInfo.maritalStatus)'/></span></td>
	                  </tr>
	                </table>
				  </div>
	            </div>
	            <div class="section">
              <div class="section-header clearfix">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info"></span>
              		<s:text name="employee.addressInfo"/><%-- 地址信息 --%>
              	</strong>
              </div>
              <div class="section-content">  
                <%-- ===== 地址信息LIST为空时 ====== --%>
                <s:if test="null == addressList || addressList.size()==0">
                	<table class="detail" cellpadding="0" cellspacing="0">
	                  <caption>
		                  	<s:text name="employee.def_address"/><%-- 常用地址 --%> 
		                  	<span class="highlight"><s:text name="employee.def_display"/></span><%-- 默认显示 --%>
	                  </caption>
	                  <tr>
	                    <th><s:text name="employee.province"/></th><%-- 省份 --%>
	                    <td><span></span></td>
	                    <th><s:text name="employee.city"/></th><%-- 城市 --%>
	                    <td><span></span></td>
	                  </tr>
	                  <tr>
	                    <th><s:text name="employee.zipCode"/></th><%-- 邮编 --%>
	                    <td><span></span></td>
	                    <th><s:text name="employee.address"/></th><%-- 地址 --%>
	                    <td><span></span></td>
	                  </tr>
	                </table>
                </s:if>
                <s:iterator value="addressList">
	                <table class="detail" cellpadding="0" cellspacing="0">
	                  <caption>
	                  	<s:if test="defaultFlag == 1">
		                  	<s:text name="employee.def_address"/><%-- 常用地址 --%> 
		                  	<span class="highlight"><s:text name="employee.def_display"/></span><%-- 默认显示 --%>
	                  	</s:if>
	                  	<s:else>
	                  		<s:text name="employee.bkp_address"/><%-- 备用地址 --%>
	                  	</s:else>
	                  </caption>
	                  <tr>
	                    <th><s:text name="employee.province"/></th><%-- 省份 --%>
	                    <td><span><s:property value='provinceName'/></span></td>
	                    <th><s:text name="employee.city"/></th><%-- 城市 --%>
	                    <td><span><s:property value='cityName'/></span></td>
	                  </tr>
	                  <tr>
	                    <th><s:text name="employee.zipCode"/></th><%-- 邮编 --%>
	                    <td><span><s:property value='zipCode'/></span></td>
	                    <th><s:text name="employee.address"/></th><%-- 地址 --%>
	                    <td><p><s:property value='address'/></p></td>
	                  </tr>
	                </table>
                </s:iterator>
              </div>
            </div>
            <div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info"></span>
              		<s:text name="employee.quit_info"/><%-- 入离职信息  --%>
              	</strong>
              </div>
              <div class="section-content">
				<s:if test="null==quitList || quitList.size()==0">
					<table class="detail" cellpadding="0" cellspacing="0">
                   		<tr>
		                    <th><s:text name="employee.com_date"/></th><%-- 入职日期  --%>
		                    <td><span></span></td>
		                    <th><s:text name="employee.dep_date"/></th><%-- 离职日期  --%>
		                    <td><span></span></td>
		                </tr>
		                <tr>
		                    <th><s:text name="employee.dep_reason"/></th><%-- 离职理由  --%>
		                    <td colspan="3"><span></span></td>
		                </tr>
		            </table>
				</s:if>
                <s:iterator value="quitList">
	                <s:if test="#empId1 == #empId2">
	                	<table class="detail" cellpadding="0" cellspacing="0">
	                   		<tr>
			                    <th><s:text name="employee.com_date"/></th><%-- 入职日期  --%>
			                    <td><span><s:property value='comDate'/></span></td>
			                </tr>
			            </table>
	                </s:if>
	                <s:else>
	                	<table class="detail" cellpadding="0" cellspacing="0">
	                   		<tr>
			                    <th><s:text name="employee.com_date"/></th><%-- 入职日期  --%>
			                    <td><span><s:property value='comDate'/></span></td>
			                    <th><s:text name="employee.dep_date"/></th><%-- 离职日期  --%>
			                    <td><span><s:property value='depDate'/></span></td>
			                </tr>
			                <tr>
			                    <th><s:text name="employee.dep_reason"/></th><%-- 离职理由  --%>
			                    <td colspan="3"><span><s:property value='depReason'/></span></td>
			                </tr>
			            </table>
	                </s:else>
               	</s:iterator>
              </div>
            </div>
	            
	            
            </div>
          </div>    
              
          
          
            
            
            <%-- 操作按钮 --%>
            <div id="editButton" class="center clearfix">
          		<s:if test="%{modeFlg != null}">
          			<s:if test="maintainBa">
          				<cherry:show domId="BINOLBSEMP0104">
		          			<s:url action="BINOLBSEMP03_init" id="updateEmployeeUrl">
							<s:param name="employeeId" value="%{employeeInfo.employeeId}"></s:param>
							</s:url>
							<a href="${updateEmployeeUrl }" id="updateEmployeeUrl" style="display: none;"></a>
		          			<button class="edit" onclick="openWin('#updateEmployeeUrl');return false">
			              		<span class="ui-icon icon-edit-big"></span>
			              		<span class="button-text"><s:text name="global.page.edit"/></span>
			              	</button>
		          		</cherry:show>
		          		<s:url action="BINOLBSEMP05_delete" id="delEmployee">
		          			<s:param name="employeeId" value="%{employeeInfo.employeeId}"></s:param>
		          			<s:param name="userId" value="%{employeeInfo.userId}"></s:param>
		          			<s:param name="longinName" value="%{employeeInfo.longinName}"></s:param>
		          		</s:url>
		          		<s:if test="%{employeeInfo.empValidFlag == 1}">
		          		<cherry:show domId="BINOLBSEMP0103">
		          		<button class="edit" onclick="bsemp01_editValidFlag('disable','${delEmployee }');return false;">
		              		<span class="ui-icon icon-delete-big"></span>
		              		<span class="button-text"><s:text name="global.page.disable"/></span>
		              	</button>
		              	</cherry:show>
		          		</s:if>
		          		<s:else>
		          		<cherry:show domId="BINOLBSEMP0102">
		          		<button class="edit" onclick="bsemp01_editValidFlag('enable','${delEmployee }');return false;">
		              		<span class="ui-icon icon-success"></span>
		              		<span class="button-text"><s:text name="global.page.enable"/></span>
		              	</button>
		              	</cherry:show>
		          		</s:else>
          			</s:if>
          			<s:elseif test='%{employeeInfo.categoryCode == null || !"01".equals(employeeInfo.categoryCode)}'>
	          			<cherry:show domId="BINOLBSEMP0104">
		          			<s:url action="BINOLBSEMP03_init" id="updateEmployeeUrl">
							<s:param name="employeeId" value="%{employeeInfo.employeeId}"></s:param>
							</s:url>
							<a href="${updateEmployeeUrl }" id="updateEmployeeUrl" style="display: none;"></a>
		          			<button class="edit" onclick="openWin('#updateEmployeeUrl');return false">
			              		<span class="ui-icon icon-edit-big"></span>
			              		<span class="button-text"><s:text name="global.page.edit"/></span>
			              	</button>
		          		</cherry:show>
		          		<s:url action="BINOLBSEMP05_delete" id="delEmployee">
		          			<s:param name="employeeId" value="%{employeeInfo.employeeId}"></s:param>
		          			<s:param name="userId" value="%{employeeInfo.userId}"></s:param>
		          			<s:param name="longinName" value="%{employeeInfo.longinName}"></s:param>
		          		</s:url>
		          		<s:if test="%{employeeInfo.empValidFlag == 1}">
		          		<cherry:show domId="BINOLBSEMP0103">
		          		<button class="edit" onclick="bsemp01_editValidFlag('disable','${delEmployee }');return false;">
		              		<span class="ui-icon icon-delete-big"></span>
		              		<span class="button-text"><s:text name="global.page.disable"/></span>
		              	</button>
		              	</cherry:show>
		          		</s:if>
		          		<s:else>
		          		<cherry:show domId="BINOLBSEMP0102">
		          		<button class="edit" onclick="bsemp01_editValidFlag('enable','${delEmployee }');return false;">
		              		<span class="ui-icon icon-success"></span>
		              		<span class="button-text"><s:text name="global.page.enable"/></span>
		              	</button>
		              	</cherry:show>
		          		</s:else>
	          		</s:elseif>
          		</s:if>
          		<s:else>
          			<s:if test="maintainBa">
          				<cherry:show domId="BINOLBSEMP0104">
		          			<button class="edit" onclick="bsemp02_editEmp();return false;">
			              		<span class="ui-icon icon-edit-big"></span>
			              		<span class="button-text"><s:text name="global.page.edit"/></span>
			              	</button>
		          		</cherry:show>
          			</s:if>
	          		<s:elseif test='%{employeeInfo.categoryCode == null || !"01".equals(employeeInfo.categoryCode)}'>
		          		<cherry:show domId="BINOLBSEMP0104">
		          			<button class="edit" onclick="bsemp02_editEmp();return false;">
			              		<span class="ui-icon icon-edit-big"></span>
			              		<span class="button-text"><s:text name="global.page.edit"/></span>
			              	</button>
		          		</cherry:show>
	          		</s:elseif>
		            <button id="close" class="close" type="button"  onclick="bsemp02_doClose();return false;">
		           		<span class="ui-icon icon-close"></span>
		           		<span class="button-text"><s:text name="global.page.close"/></span>
		         	</button>
          		</s:else>
            </div>
            
          </div>
          </cherry:form>
          <%-- ====================== 基本信息DIV结束 ===================== --%>
          <%-- ====================== 考勤记录DIV开始 ===================== --%>
          <div id="tabs-2">
<!--          	<s:text name="employee.attendance"/>-->
          	<div class="center clearfix">
                <s:url action="BINOLBSEMP02_basAttendance" id="basAttendanceUrl"/>
                <a id="basAttendanceUrl" href="${basAttendanceUrl}"></a>
                <table id="dataTableAttendance" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="EMP02_num"/></th><%-- No. --%>
                        <th><s:text name="EMP02_departName"/></th><%-- 柜台  --%>
                        <th><s:text name="EMP02_arriveTime"/></th><%-- 到柜时间 --%>
                        <th><s:text name="EMP02_leaveTime"/></th><%-- 离柜时间--%>
                    </tr>
                    </thead>
                </table>
              <s:if test="%{modeFlg == null}">
              <button id="close" class="close" type="button"  onclick="bsemp02_doClose();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
          	  </button>
          	  </s:if>
            </div>
          </div>
          <%-- ====================== 考勤记录DIV结束 ===================== --%>
          <%-- ======================= 营业额DIV开始 ====================== --%>
          <%--
          <div id="tabs-3">
          	<s:text name="employee.businessSum"/>
          	<div class="center clearfix">
              <s:if test="%{modeFlg == null}">
              <button id="close" class="close" type="button"  onclick="bsemp02_doClose();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
          	  </button>
          	  </s:if>
            </div>
          </div>
          -- %>
          <%-- ======================= 营业额DIV结束 ====================== --%>
          <%-- ======================= 数据权限DIV开始 ====================== --%>
          <div id="tabs-4">
	      <div class="box">
	        <s:url action="BINOLBSEMP02_searchPrivilege" id="searchPrivilegeUrl"/>
	        <a id="searchPrivilegeUrl" href="${searchPrivilegeUrl}"></a>
	        <form id="privilegeForm" class="inline" onsubmit="searchPrivilege();return false;">
	          <s:hidden name="employeeId" value="%{employeeInfo.employeeId}"/>
	          <div class="box-header">
	          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
            	<input type="radio" name="privilegeType" id="privilegeType0" value="0" checked="checked" onclick="changePrivilegeType(this);"/><label for="privilegeType0"><s:text name="employee.departPrivilege"/></label>
            	<input type="radio" name="privilegeType" id="privilegeType1" value="1" onclick="changePrivilegeType(this);"/><label for="privilegeType1"><s:text name="employee.employeePrivilege"/></label>
	          </div>
	          <div class="box-content clearfix">
	            <div class="column" style="width:50%; height: auto;">
	              <p>
	              	<label><s:text name="employee.departCode"/></label>
	                <s:textfield name="departCode" cssClass="text"/>
	              </p>
	              <p>
	               	<label><s:text name="employee.departName"/></label>
	               	<s:textfield name="departName" cssClass="text"/>
	              </p>
	            </div>
	            <div class="column last" style="width:49%; height: auto;">
	              <p style="height: 23px;">
	                <label><s:text name="employee.businessType"/></label>
	                <s:select list='#application.CodeTable.getCodes("1048")' listKey="CodeKey" listValue="Value" name="businessType"></s:select>
	              </p>
	              <p>
	               	<label><s:text name="employee.operationType"/></label>
	               	<s:select list='#application.CodeTable.getCodes("1049")' listKey="CodeKey" listValue="Value" name="operationType" value="1"></s:select>
	              </p>
	            </div>
	          </div>
	          <p class="clearfix">
	         	<button class="right search" onclick="searchPrivilege();return false;">
	         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
	         	</button>	
	          </p>
	        </form>
	      </div>
          	
      	  <div id="privilegeDiv" class="section hide">
	        <div class="section-header">
	        	<strong>
	        		<span class="ui-icon icon-ttl-section-search-result"></span>
	        		<s:text name="global.page.list"/>
	        	</strong>
	        </div>
        	<div class="section-content">
              <div id="depPrivilegeDiv">
                <table id="dataTableDepPrivilege" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="employee.departName"/></th>
                        <th><s:text name="employee.departType"/></th>
                        <th><s:text name="employee.privilegeFlag"/></th>
                    </tr>
                    </thead>
                </table>
              </div>
              <div id="empPrivilegeDiv" style="display: none">
                <table id="dataTableEmpPrivilege" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="employee.name"/></th>
                        <th><s:text name="employee.depart"/></th>
                        <th><s:text name="employee.post"/></th>
                        <th><s:text name="employee.privilegeFlag"/></th>
                    </tr>
                    </thead>
                </table>
              </div>
        	</div>
      	  </div> 
      	  
      	  <div class="center clearfix">
      	    <s:if test="%{modeFlg == null}">
              <button id="close" class="close" type="button"  onclick="bsemp02_doClose();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
          	  </button>
          	</s:if>
      	  </div>     	
          	
          </div>
          <%-- ======================= 数据权限DIV结束 ====================== --%>
        
        </div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>