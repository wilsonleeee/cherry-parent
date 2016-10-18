<%-- 柜台消息管理 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO23.js"></script>
<style>
.column label {
    width:100px;
}
.treebox_left{
		background:none repeat scroll 0 0 #FFFFFF;
		border:1px solid #FAD163;
		height:420px;
		border-color:#D6D6D6 #D6D6D6 
}
.tree_out{
	padding: 2em;
}
/* Firefox hack */
@-moz-document url-prefix(){td{height:27px;}}
</style>
<s:i18n name="i18n.mo.BINOLMOCIO23">
    <div class="hide">
        <s:url id="search_url" value="/mo/BINOLMOCIO23_search"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:text name="global.page.select" id="select_default"/>
    </div>
    <div class="panel-header">
        <div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
        </div>
    </div>
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
            <cherry:form id="mainForm" class="inline" onsubmit="BINOLMOCIO23.search(); return false;">
                <div class="box-header"></div>
                <div class="box-content clearfix">
                    <div class="column" style="width:49%">
                    	<p>
	                    <%-- 所属品牌 --%>
	                        <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
	                        <label><s:text name="CIO23_brandInfo"></s:text></label>
	                        <s:text name="CIO23_select" id="CIO23_select"/>
	                        <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" cssStyle="width:100px;"></s:select>
	                        </s:if>
	                    </p>
                        <p>
                        <%-- 消息标题 --%>
                            <label><s:text name="CIO23_messageTitle"></s:text></label>
                            <s:textfield name="messageTitleOrBody" cssClass="text" maxlength="50"/>
                        </p>
                        <p>
                        <%-- 消息类型 --%>
                            <label><s:text name="CIO23_messageType"></s:text></label>
                             <s:select id="messageType" name="messageType" list="#application.CodeTable.getCodes('1413')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#select_default}"/>
                        </p>
                    </div>
                    <div class="column last" style="width:50%">
                        <p class="date">
                        <%-- 生效日期--%>
                            <label><s:text name="CIO23_validDate"></s:text></label>
                            <span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
                            - 
                            <span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
                        </p> 
                        
                    </div>
                </div>
                <p class="clearfix">
                    <button class="right search" type="submit" onclick="BINOLMOCIO23.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="CIO23_search"/></span>
                    </button>
                </p>
            </cherry:form>
        </div>
        <div id="section" class="section hide">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <%-- 查询结果一览 --%>
                    <s:text name="CIO23_results_list"/>
                </strong>
            </div>
            <div class="section-content">
            <div id ="ui-tabs" class="ui-tabs">
			  	<ul class ="ui-tabs-nav clearfix">
		            <%--进行中--%>
		            <li id="tab2_li" class="ui-tabs-selected" onclick = "BINOLMOCIO23.cntMsgDateFilter('in_progress',this);"><a><s:text name="CIO23_in_progress"/></a></li>
		           	<%--已过期--%>
		            <li id="tab4_li" onclick = "BINOLMOCIO23.cntMsgDateFilter('past_due',this);"><a><s:text name="CIO23_past_due"/></a></li>
		            <%--未开始--%>
		            <li id="tab3_li" onclick = "BINOLMOCIO23.cntMsgDateFilter('not_start',this);"><a><s:text name="CIO23_not_start"/></a></li>
		            <%--其他--%>
		            <li id="tab1_li"  onclick = "BINOLMOCIO23.cntMsgDateFilter('not_release',this);"><a><s:text name="CIO23_not_release"/></a></li>
	          	</ul>
	        	<div id="tabs-1" class="ui-tabs-panel" style="overflow-x:auto;overflow-y:hidden;">
	                <div class="toolbar clearfix">
	                    <span class="right">
	                        <a class="setting">
	                            <span class="ui-icon icon-setting"></span>
	                            <span class="button-text">
	                                <%-- 设置列 --%>
	                                <s:text name="CIO23_colSetting"/>
	                            </span>
	                        </a>
	                    </span>
	                </div>
	                <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	                    <thead>
	                    <tr>
	                        <th><s:text name="CIO23_num"/></th><%-- No. --%>
	                        <th class="center"><s:text name="CIO23_messageTitle"/></th><%-- 消息标题 --%>
	                        <th class="center"><s:text name="CIO23_messageType"/></th><%--  消息类型--%>
	                        <th class="center"><s:text name="CIO23_startValidDate"/></th><%--  消息生效开始日期 --%>
	                        <th class="center"><s:text name="CIO23_endValidDate"/></th><%--  消息生效截止日期 --%>
	                        <th class="center"><s:text name="CIO23_publishDate"/></th><%-- 发布日期 --%>
	                       <%--  <th class="center"><s:text name="CIO23_status"/></th> 消息状态 --%>
	                    </tr>
	                    </thead>
	                </table>
                </div>
            </div>
            </div>
        </div>
    </div>
    <div class="hide" id="dialogInit"></div>
    <div class="hide">
        <div id="addMsgTitle"><s:text name="CIO23_addTitle" /></div>
        <div id="detailTitle"><s:text name="CIO23_detail" /></div>
        <div id="updateMsgTitle"><s:text name="CIO23_updateTitle" /></div>
        <div id="deleteMsgTitle"><s:text name="CIO23_deleteTitle" /></div>
        <div id="disableMsgTitle"><s:text name="CIO23_disable" /></div>
        <div id="enableMsgTitle"><s:text name="CIO23_enable" /></div>
        <div id="deleteMsgText"><p class="message"><span><s:text name="CIO23_deleteText" /></span></p></div>
        <div id="disableMsgText"><p class="message"><span><s:text name="CIO23_disableText" /></span></p></div>
        <div id="enableMsgText"><p class="message"><span><s:text name="CIO23_enableText" /></span></p></div>
        <div id="saveSuccessTitle"><s:text name="save_success" /></div>
        <div id="dialogConfirm"><s:text name="dialog_confirm" /></div>
        <div id="dialogCancel"><s:text name="dialog_cancel" /></div>
        <div id="dialogClose"><s:text name="dialog_close" /></div>
        <div id="dialogInitMessage"><s:text name="dialog_init_message" /></div>
        <div id="deleteMsgText"><p class="message"><span><s:text name="delete_message" /></span></p></div>
    </div>
    
    <div id="pop" style="display:none">
     	 <div id="region"></div>
     	 <div id="messageDatail" class="ui-dialog-content ui-widget-content" style="width: auto; min-height: 0px;" ></div>
     	 <div id="detail">
     	 	<p class="clearfix center">
	     	 	<strong>
		            <span id="messageTitle"></span>
		        </strong>
	      	</p>
	      	<div class="section">
	       		<div class="section-header">
	              	<strong>
	              		<span class="ui-icon icon-ttl-section-info-edit"></span>
	              		<s:text name="CIO23_messageBody"/><%-- 问题详细 --%>
	              	</strong>
	             </div>
       		</div>
       		<div class="box2-active">
	       		<div class="box2 box2-content ui-widget" >
	       			<div style="word-break:break-all;word-wrap:break-word;">
	       				<label style="display: block;margin-right: 0px; " id="messageBody"></label>
	       			</div>
	       		</div>
       		</div>
     	 </div>
     </div>
    <div class="hide">
    	<div id="doRegion"><s:text name="CIO23_doRegion" /></div>
	    <div id="publisCounter"><s:text name="CIO23_publisCounter" /></div>
	    <div id="selMode1"><s:text name="global.page.selMode1"/></div>
	    <div id="selMode2"><s:text name="global.page.selMode2"/></div>
	    <div id="selMode3"><s:text name="global.page.selMode3"/></div>
	    <div id="select_default"><s:text name="global.page.select" /></div>
	    <div id="channelRegion"><s:text name="global.page.channelRegion"/></div>
    </div>
 </s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<%-- ================== dataTable共通导入    END  ======================= --%>
    <script type="text/javascript">
        // 节日
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
    </script>

<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EMO00036" value='<s:text name="EMO00036"/>'/>
</div>