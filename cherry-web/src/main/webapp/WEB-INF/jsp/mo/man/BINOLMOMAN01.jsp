<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/man/BINOLMOMAN01.js"></script>
<style>
/* Firefox hack */
@-moz-document url-prefix(){td{height:26px;}}
</style>
<s:i18n name="i18n.mo.BINOLMOMAN01">
    <div class="hide">
        <s:url id="search_url" value="/mo/BINOLMOMAN01_search"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:url action="BINOLMOMAN01_disable" id="disableMachine"></s:url>
        <s:url action="BINOLMOMAN01_enable" id="enableMachine"></s:url>
        <s:url action="BINOLMOMAN01_unbind" id="unbindCounter"></s:url>
        <s:url action="BINOLMOMAN01_delete" id="delete_url"></s:url>
        <s:url action="BINOLMOMAN01_scrap" id="scrap_url"></s:url>
        <s:url action="BINOLMOMAN01_issueMachine" id="issueMachine_url"></s:url>
        <s:url action="BINOLMOMAN02_init" id="add_url"></s:url>
    </div>
      <div class="panel-header">
        <%-- 机器一览 --%>
        <div class="clearfix">
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
            <span class="right">
            </span>
        </div>
      </div>
      <div id="actionResultDisplay"></div>
      <div id="errorMessage"></div>
      <%-- ================== 错误信息提示 START ======================= --%>
      <div id="errorDiv2" style="display:none">
        <div class="actionError">
          <ul>
             <li><span id="errorSpan2"></span></li>
          </ul>
        </div>
      </div>
      <%-- ================== 错误信息提示   END  ======================= --%>
      <div class="panel-content">
        <div class="box">
          <cherry:form id="mainForm" class="inline" onsubmit="BINOLMOMAN01.search(); return false;">
          <%-- IE只有一个text框 ，按 Enter键跳转其他页面hack --%>
          <input class="hide"/>
            <div class="box-header">
            <strong>
                <span class="ui-icon icon-ttl-search"></span>
                <%-- 查询条件 --%>
                <s:text name="MAN01_condition"/>
            </strong>
            </div>
            <div class="box-content clearfix">
              <div class="column" style="width:50%;">
                <p>
                <%-- 所属品牌 --%>
                    <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
                    <label><s:text name="MAN01_brandInfo"></s:text></label>
                    <s:text name="MAN01_select" id="MAN01_select"/>
                    <s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" cssStyle="width:100px;"></s:select>
                    </s:if>
                </p>
                 <p>
                <%-- 机器编号 --%>
                  <label><s:text name="MAN01_machineCodeNew"/></label>
                  <s:textfield name="machineCode" cssClass="text" maxlength="30"/>
                </p>
                <p>
                <%-- MAC地址--%>
                  <label><s:text name="MAN01_mobileMacAddress"/></label>
                  <s:textfield name="mobileMacAddress" cssClass="text" maxlength="50"/>
                </p>
                <p>
                <%-- 柜台 --%>
                    <label><s:text name="MAN01_counter"/></label>
                    <s:textfield name="counterCodeName" cssClass="text" maxlength="30"/>
                </p>
              </div>
              <div class="column last" style="width:49%;">
              	<p>
                <%-- 类型 --%>
                   <label><s:text name="MAN01_machineType"/></label>
                   <s:text name="MAN01_selectAll" id="MAN01_selectAll"/>
                   <s:select name="machineType" 
                      list='#application.CodeTable.getCodes("1101")' 
                      listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{MAN01_selectAll}" cssStyle="width:100px;"/>
                 </p>
                 <p>
                 <%-- 机器状态 --%>
                   <label><s:text name="MAN01_machineStatus"/></label>
                   <s:text name="MAN01_selectAll" id="MAN01_selectAll"/>
                   <s:select name="machineStatus" 
                      list='#application.CodeTable.getCodes("1102")' 
                      listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{MAN01_selectAll}" cssStyle="width:100px;"/>
                 </p>
                 <p>
                 <%-- 绑定状态 --%>
                   <label><s:text name="MAN01_bindStatus"/></label>
                   <s:text name="MAN01_selectAll" id="MAN01_selectAll"/>
                   <s:select name="bindStatus" 
                      list='#application.CodeTable.getCodes("1103")' 
                      listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{MAN01_selectAll}" cssStyle="width:100px;"/>
                 </p>
              </div>
            </div>
            <p class="clearfix">
              <%--<cherry:show domId="MOMAN0101QUERY">--%>
                    <button class="right search" type="submit" onclick="BINOLMOMAN01.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="MAN01_search"/></span>
                    </button>
               <%--</cherry:show>--%>
            </p>
          </cherry:form>
        </div>
        <div id="section" class="section hide">
          <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-search-result"></span>
            <%-- 查询结果一览 --%>
            <s:text name="MAN01_results_list"/>
         </strong>
        </div>
          <div class="section-content">
            <div class="toolbar clearfix">
              <span class="left">
              <cherry:show domId="MOMAN01ENABLE">
                <a href="${enableMachine}" id="enableBtn" class="add" onclick="BINOLMOMAN01.confirmInit(this,'enable');return false;">
                <span class="ui-icon icon-enable"></span>
                <span class="button-text"><s:text name="MAN01_enable"/></span>
                </a>
              </cherry:show>
              <cherry:show domId="MOMAN01DISABLE">
                <a href="${disableMachine}" id="disableBtn" class="delete" onclick="BINOLMOMAN01.confirmInit(this,'disable');return false;">
                <span class="ui-icon icon-disable"></span>
                <span class="button-text"><s:text name="MAN01_disable"/></span>
                </a>
              </cherry:show>
              <cherry:show domId="MOMAN01ISSUE">
                <a href="${issueMachine_url}" id="enableBtn" class="add" onclick="BINOLMOMAN01.confirmInit(this,'issue');return false;">
                <span class="ui-icon icon-publish"></span>
                <span class="button-text"><s:text name="MAN01_issue"/></span>
                </a>
              </cherry:show>
              <cherry:show domId="MOMAN01DELETE">
                <a href="${delete_url}" id="deleteBtn" class="delete" onclick="BINOLMOMAN01.confirmInit(this,'delete');return false;">
                <span class="ui-icon icon-delete"></span>
                <span class="button-text"><s:text name="MAN01_delete"/></span>
                </a>
              </cherry:show>
              <cherry:show domId="MOMAN01SCRAP">
	              <a href="${scrap_url}" id="scrapBtn" class="delete" onclick="BINOLMOMAN01.confirmInit(this,'scrap');return false;">
	                <span class="ui-icon icon-delete"></span>
	                <span class="button-text"><s:text name="MAN01_scrap"/></span>
	              </a>
              </cherry:show>
              <cherry:show domId="MOMAN0101UNBIND">
                <a href="${unbindCounter}" id="unbindBtn" class="delete" onclick="BINOLMOMAN01.confirmInit(this,'unbind');return false;">
                <span class="ui-icon icon-disable"></span>
                <span class="button-text"><s:text name="MAN01_unbind"/></span>
                </a>
              </cherry:show>
              </span>
                <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
                <span class="button-text">
                <%-- 设置列 --%>
                <s:text name="MAN01_colSetting"/>
                </span></a></span></div>
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th><s:checkbox name="allSelect" id="allSelect" onclick="BINOLMOMAN01.checkSelectAll(this)"/></th><%-- 选择 --%>
                  <th><s:text name="MAN01_num"/></th><%-- No. --%>
                  <th><s:text name="MAN01_machineCodeNew"/></th><%-- 新机器编号（15位）  --%>
                  <th><s:text name="MAN01_machineCode"/></th><%-- 老机器编号（10位） --%>
                  <th><s:text name="MAN01_machineType"/></th><%-- 类型  --%>
                  <th><s:text name="MAN01_counterNameIF"/></th><%-- 当前使用柜台 --%>
                  <th><s:text name="MAN01_softWareVersion"/></th><%-- 软件版本 --%>
                  <th><s:text name="MAN01_mobileMacAddress"/></th><%-- MAC地址 --%>
                  <th><s:text name="MAN01_phoneCode"/></th><%-- 通讯卡号 --%>
                  <th><s:text name="MAN01_employeeName"/></th><%-- 责任人 --%>
                  <th><s:text name="MAN01_machineStatus"/></th><%-- 机器状态 --%>
                  <th><s:text name="MAN01_startTime"/></th><%-- 时间 --%>
                  <th><s:text name="MAN01_createTime"/></th><%-- 添加时间 --%>
                  <th><s:text name="MAN01_bindCounter"/></th><%-- 绑定设置柜台 --%>
<!--                  <th><s:text name="MAN01_operate"/></th><%-- 操作 --%>-->
                </tr>
              </thead>
            </table>
          </div>
        </div>
      </div>
      <div class="hide" id="dialogInit"></div>
      <div style="display: none;">
      <div id="disableText"><p class="message"><span><s:text name="MAN01_disableText"/></span></p></div>
      <div id="disableTitle"><s:text name="MAN01_disableTitle"/></div>
      <div id="enableText"><p class="message"><span><s:text name="MAN01_enableText"/></span></p></div>
      <div id="enableTitle"><s:text name="MAN01_enableTitle"/></div>
      <div id="unbindText"><p class="message"><span><s:text name="MAN01_unbindText"/></span></p></div>
      <div id="unbindTitle"><s:text name="MAN01_unbindTitle"/></div>
      <div id="deleteMachineText"><p class="message"><span><s:text name="MAN01_deleteMachineText"/></span></p></div>
      <div id="deleteMachineTitle"><s:text name="MAN01_deleteMachineTitle"/></div>
      <div id="issueText"><p class="message"><span><s:text name="MAN01_issueText"/></span></p></div>
      <div id="issueTitle"><s:text name="MAN01_issueTitle"/></div>
      <div id="scrapText"><p class="message"><span><s:text name="MAN01_scrapText"/></span></p></div>
      <div id="scrapTitle"><s:text name="MAN01_scrapTitle"/></div>
      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      <div id="dialogClose"><s:text name="global.page.close" /></div>
      <div id="save_success_message"><s:text name="MAN01_save_success_message" /></div>
      </div>
 </s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="EMO00001"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="EMO00003"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="EMO00004"/>'/>
    <input type="hidden" id="errmsg4" value='<s:text name="EMO00014"/>'/>
    <input type="hidden" id="errmsg5" value='<s:text name="EMO00064"/>'/>
    <input type="hidden" id="errmsg6" value='<s:text name="EMO00065"/>'/>
    <!-- 您选择的机器中含有已报废的机器，不能执行该操作，请重新选择！-->
    <input type="hidden" id="errmsg7" value='<s:text name="EMO00096"/>'/>
</div>