<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM54.js"></script>
<script type="text/javascript">
function agree(){
	cherryAjaxRequest({
		url:$("#s_agree").html(),
		param:$("#mainForm").formSerialize(),
		callback:SSPRM54_AuditSuccess
	});
}

function disagree(){
	cherryAjaxRequest({
		url:$("#s_disagree").html(),
		param:$("#mainForm").formSerialize(),
		callback:SSPRM54_AuditSuccess
	});
}

function SSPRM54_AuditSuccess(msg){
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$("#buttonarea1").hide();
		$("#buttonarea2").show();
	}
}
function ClosePopupForm(){
	var ob = window.opener.document.getElementById("currentUnitID")
	if(ob){
		if(ob.value=='TOP'){
			window.opener.refreshTaskList();
		}
	}
	window.close();
}
</script>
<s:i18n name="i18n.ss.BINOLSSPRM54">
<s:url id="url_agree" value="/ss/BINOLSSPRM54_AGREE" />
<span id ="s_agree" style="display:none">${url_agree}</span>

<s:url id="url_disagree" value="/ss/BINOLSSPRM54_DISAGREE" />
<span id ="s_disagree" style="display:none">${url_disagree}</span>

<div class="main container clearfix" style="width:90%">
<div class="panel ui-corner-all" >
      <div class="panel-content">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          </div>
          <div class="section-content">
          <div id="actionResultDisplay"></div>
            <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
            <input type="hidden" name="proAllocationId" id="proAllocationId" value="<s:property value='proAllocationId'/>"/>
            <input type="hidden" name="taskInstanceID" id="taskInstanceID" value="<s:property value='taskInstanceID'/>"/>
            <input type="hidden" value="<s:property value="returnInfo.UpdateTime"/>" name="updateTime" id="updateTime"/>
		    <input type="hidden" value="<s:property value="returnInfo.ModifyCount"/>" name="modifyCount" id="modifyCount"/>
            <input type="hidden" id="verifiedFlag" value='<s:property value="#returnInfo.verifiedFlag">'/>
             <input type="hidden" id="csrftoken" name="csrftoken"/>
             <div class="table clearfix" style="margin-bottom: 0">
              <div class="tr">
              	<%-- 调拨单号 --%>
                <div class="th"><s:text name="PRM54_allocationNo"/></div>
                <div class="td"><s:property value="#returnInfo.allocationNo"/></div>
                <%-- 调拨日期 --%>
                <div class="th"><s:text name="PRM54_date"/></div>
                <div class="td"><s:property value="#returnInfo.allocationDate"/></div>
              </div>   
              <div class="tr">
              	<%-- 申请部门 --%>
                <div class="th"><s:text name="PRM54_sendOrg"/></div>
                <div class="td"><s:property value="#returnInfo.sendDepart"/></div>
                <%-- 接受部门 --%>
                <div class="th"><s:text name="PRM54_receiveOrg"/></div>
                <div class="td"><s:property value="#returnInfo.receiveDepart"/></div>
              </div>
              <div class="tr">
              	<%-- 审核状态 --%>
              	<div class="th"><s:text name="PRM54_verifiedFlag"/></div>
                <div class="td"><s:property value='#application.CodeTable.getVal("1007", returnInfo.verifiedFlag)'/></div>
                
                <%-- 操作员 --%>
                <div class="th"><s:text name="PRM54_employeeName"/></div>
                <div class="td"><s:property value="#returnInfo.employeeName"/></div>
              </div>
             </div>
             <div class="table" style="border-top:none;">
             	<div class="tr">
	             	<%-- 调拨理由 --%>
	               <div class="th"><s:text name="PRM54_reason"/></div>
	               <div class="td" style="width: 85%;"><s:property value="#returnInfo.reason"/></div>
	            </div>
             </div>
            </cherry:form>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 调拨明细一览 --%>
            <s:text name="PRM54_results_list"/>
            </strong>
          </div>
          <div class="section-content">          	
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th class="center"><s:text name="PRM54_num"/></th><%-- 编号 --%>
                  <th class="center"><s:text name="PRM54_unitCode"/></th><%-- 厂商编码 --%>
                  <th class="center"><s:text name="PRM54_barCode"/></th><%-- 产品条码 --%>
                   <th class="center"><s:text name="PRM54_nameTotal"/></th><%-- 产品名称 --%>
                  <th class="center"><s:text name="PRM54_price"/></th><%-- 价格 --%>
                  <th class="center"><s:text name="PRM54_quantity"/></th><%-- 数量 --%>
                  <th class="center"><s:text name="PRM54_amount"/></th><%-- 金额 --%>
                  <th class="center"><s:text name="PRM54_reason"/></th><%-- 备注 --%>
                </tr>
              </thead>
              <tbody>
                <s:iterator value="returnList" status="status">
                	<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  <td><s:property value="#status.index+1"/></td>
	                  <td><span><s:property value="unitCode"/></span></td>
	                   <td><span><s:property value="barCode"/></span></td>
	                  <td><span><s:property value="nameTotal"/></span></td>	
	                 
	                  <td><s:text name="format.price"><s:param value="price"></s:param></s:text></td>
	                  <td><s:text name="format.number"><s:param value="quantity"></s:param></s:text></td>
	                  <td><s:text name="format.price"><s:param value="price * quantity"></s:param></s:text></td>
	                  <td><p><s:property value="reason"/></p></td>
	                </tr>
                </s:iterator>
              </tbody>
            </table>
           	</div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="PRM54_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="PRM54_totalAmount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td class="center"><s:text name="format.number"><s:param value="returnInfo.totalQuantity"></s:param></s:text></td>
             	<td class="center"><s:text name="format.price"><s:param value="returnInfo.totalAmount"></s:param></s:text></td>
              </tr>
            </table>
            <hr class="space" />
            
            <div id="buttonarea1" class="center">
             <button class="confirm" onclick="agree();"><span class="ui-icon icon-confirm"></span>
              <%-- 通过 --%>
              <span class="button-text"><s:text name="btnAgree"/></span>
             </button>
              <button class="close" onclick="disagree();"><span class="ui-icon icon-close"></span>
              <%-- 不通过 --%>
              <span class="button-text"><s:text name="btnDisagree"/></span>
              </button>
              <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
              <%-- 关闭 --%>
              <span class="button-text"><s:text name="btnDonothing"/></span>
             </button>
            </div>
            <div id="buttonarea2" class="center" style="display:none">
              <button class="close" onclick="ClosePopupForm();"><span class="ui-icon icon-close"></span>
              <%-- 关闭 --%>
              <span class="button-text"><s:text name="btnClose"/></span>
             </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    </div>
    <script type="text/javascript">
    $("#csrftoken").val(window.opener.document.getElementById("csrftoken").value);
    var value =$('#verifiedFlag').val();
    if(value!='1'){
        $('#buttonarea1').hide();  
        $('#buttonarea2').show(); 
    }
    </script>
</s:i18n>
