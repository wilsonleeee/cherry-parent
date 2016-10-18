<%-- 考勤信息查询 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/buy/BINOLMOBUY01.js"></script>
<s:i18n name="i18n.mo.BINOLMOBUY01">
    <div class="hide">
    	<s:text name="global.page.select" id="select_default"/>
    	<input type="hidden" id="_select_default" value="${select_default }"/>
        <s:url id="getUdiskAttendanceStatisticsList_url" value="/mo/BINOLMOBUY01_getUdiskAttendanceStatisticsList"/>
        <a id="getUdAttStaListurl" href="${getUdiskAttendanceStatisticsList_url}"></a>
        <s:url id="statisticsExport" action="BINOLMOBUY01_statisticsExport" ></s:url>
        <a id="statisticsExportUrl" href="${statisticsExport}"></a>
        <s:url id="getRegionDepartsAndChannels" value="/mo/BINOLMOBUY01_getRegionDepartsAndChannels"/>
        <a id="getRegionDepartsAndChannelsUrl" href="${getRegionDepartsAndChannels}"></a>
    </div>
    <div class="panel-header">
        <%-- 考勤信息查询--%>
        <div class="clearfix">
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
            <span class="right">
            </span>
        </div>
    </div>
    <div id="errorMessage"></div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
    <cherry:form id="main_Form" class="inline">
	</cherry:form>
   	<form id="mainForm1" class="inline">
       <div class="box">
               <div class="box-header">
                   <strong>
                       <span class="ui-icon icon-ttl-search"></span>
                       <%-- 查询条件 --%>
                       <s:text name="BUY01_condition"/>
                   </strong>
                   <input type="checkbox" name="empValidFlag" id="empValidFlag" value='0'/><label for="empValidFlag"><s:text name="BUY01_dimission"/></label>
               </div>
               <div class="box-content clearfix">
                   <div class="column" style="width:50%;">
                       <p>
                       <%-- 所属品牌 --%>
                           <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
	                           <label><s:text name="BUY01_brandInfo"></s:text></label>
	                           <s:text name="BUY01_selectAll" id="BUY01_selectAll"/>
	                           <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{select_default}" onchange="BINOLMOBUY01.getRegionDepartsAndChannels(this)"></s:select>
                           </s:if>
                       </p>
                       <p>
                       <%-- U盘序列号 --%>
                         <label><s:text name="BUY01_udiskSN"/></label>
                         <s:textfield name="udiskSN" cssClass="text" maxlength="100"/>
                       </p>
                       <p>
                       <%-- 员工姓名--%>
                         <label><s:text name="BUY01_employeeName"/></label>
                         <s:textfield name="employeeName" cssClass="text" maxlength="30"/>
                       </p>
                       <%--大区 --%>
	        		<p>
	        			<label style="font-size:13px"><s:text name="BUY01_reginDepart"/></label>
	        			<s:text name="BUY01_selectAll" id="BUY01_selectAll"/>
	        			<s:if test="null != regionDepartList">
	        				<s:select name="reginDepartID" list="regionDepartList" listKey="departId" listValue="departName" headerKey="" headerValue="%{BUY01_selectAll}"></s:select>
	        			</s:if>
	        			<s:else>
	        				<select name="reginDepartID" id="reginDepartID">
	        					<option value=""><s:text name="BUY01_selectAll"></s:text></option>
	        				</select>
	        			</s:else>
		           </p>
		           
                   </div>
                   <div class="column last" style="width:49%;">
                       <p style="margin: 0.5em 0 0.8em;">
                       <%-- 考勤日期--%>
                           <label><s:text name="BUY01_attendanceDate"></s:text></label>
                           <span><s:textfield id="startAttendanceDate1" name="startAttendanceDate" cssClass="date" cssStyle="width:85px"/></span>
                           - 
                           <span><s:textfield id="endAttendanceDate1" name="endAttendanceDate" cssClass="date" cssStyle="width:85px"/></span>
                       </p>
                       
		            <p style="margin: 0.2em 0 0.8em;">
		                <%-- 所属省份 --%>
		                <s:hidden name="provinceId" id="provinceId"/>
		                <label><s:text name="BUY01_province"/></label>
		                <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
		                	<span id="provinceText" class="button-text choice" ><s:text name="global.page.select"/></span>
		     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
		     		 	</a>
		              </p>
		              <p>
		                <%-- 所属城市 --%>
       							<s:hidden name="cityId" id="cityId"/>
		                <label><s:text name="BUY01_city"/></label>
		                <a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
		                	<span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
		     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
		     		 	</a>
		              </p>
                   </div>
                   <div style="padding:10px">
	                <hr style="margin: 0 0 0.5em">
	                <div style="width:100%;float:left" class="box2 box2-active">
                           <div class="box2-header clearfix">
                               <strong class="left active">
                                   <s:text name="BUY01_importTime"></s:text>
                               </strong>
                               <a onclick="BINOLMOBUY01.addImportantTime(this);return false;" class="add right" style="margin-top:3px;">
								   <span class="button-text"><s:text name='BUY01_addImportantTime'></s:text></span>
								   <span class="ui-icon icon-add"></span>
							   </a>
                           </div>
                           <div style="padding:.5em;" class="box2-content clearfix"  id="import">
                               <div id="divRule">
                                   <div id="importTimeDiv" style="float:left;width:100%;">
                                   	<s:iterator value="importantTimeList" id="importantTimeList">
                                   		<p class="clearfix">
                                   			<span id="showImportantTime" >
                                   				<span class="left">
                                   				<label class="showtime">
                                   				<span class="ui-icon icon-clock"></span>开始时间：
                                   				<span id="startTime"  class="font_time"><s:property value='startTime'></s:property></span></label>
                                   				-
                                   				<label class="showtime">
                                   				<span class="ui-icon icon-clock"></span>结束时间：
                                   				<span id="endTime" class="font_time"><s:property value='endTime'></s:property></span></label>
                                   				</span>
                                   				<a title="<s:text name='BUY01_clickToEditImportantTime'></s:text>" style="cursor:pointer;" onclick="BINOLMOBUY01.editImportantTime(this);return false;">
                                   					<span style="font-size:12px"><s:text name="global.page.edit"></s:text></span>
                                   				</a>
                                   			</span>
                                            <%-- 关键时间段--%>
                                            <span id="editImportantTime" class="hide">
                                            	<span class="left">
                                            	<label class="edittime">
                                            	<span class="ui-icon icon-clock"></span>开始时间：
                                           		<input type="text" id="importTimeStartArr" name="importTimeStartArr" class="text" maxlength="9" style="width:80px" value="<s:property value='startTime'></s:property>"></input>
                                           		</label>
                                           		-
                                           		<label class="edittime">
                                           		<span class="ui-icon icon-clock"></span>结束时间：
                                           		<input type="text" id="importTimeEndArr" name="importTimeEndArr" class="text" maxlength="9" style="width:80px" value="<s:property value='endTime'></s:property>"></input>
                                           		</label>
                                           		</span>
                                           		<a title="<s:text name="BUY01_clickToDeleteImportantTime"></s:text>" style="cursor:pointer;margin-top:3px" onclick="BINOLMOBUY01.deleteImportantTime(this);return false;">
                                           			<span style="font-size:12px"><s:text name="global.page.delete"></s:text></span>
                                           		</a>
                                           		|
                                           		<a title="<s:text name="BUY01_clickToBack"></s:text>" style="cursor:pointer;margin-top:3px" onclick="BINOLMOBUY01.back(this);return false;">
                                           			<span style="font-size:12px"><s:text name="global.page.back"></s:text></span>
                                           		</a>
                                            </span>
                                        </p>
                                   	</s:iterator>
                                   </div>
                               </div>
                           </div>
                       </div>
                       <div style="width:100%" class="box2 box2-active right">
                           <div class="box2-header clearfix">
                               <strong class="left active">
                                   <s:text name="BUY01_CounterChannel"></s:text>
                               </strong>
                               <span style="padding-left: 8px;">
                               	<s:text name="BUY01_selectAll" id="BUY01_selectAll"/>
                                <input type="hidden" value="${BUY01_selectAll}" id="_BUY01_selectAll"/>
                                <s:if test="null != channelList">
                                	<s:select id="channelSelect" onchange="BINOLMOBUY01.addChannel(this);return false;" list="channelList" listKey="channelId" listValue="channelName" headerKey="" headerValue="%{BUY01_selectAll}"/>
                                </s:if>
                               	<s:else>
                               		<select id="channelSelect" onchange="BINOLMOBUY01.addChannel(this);return false;">
			        					<option value=""><s:text name="BUY01_selectAll"></s:text></option>
			        				</select>
                               	</s:else>
                               </span>
                               <input id="allowChannel" name="channelFlag" type="radio" value="1" checked="checked">
                               <label style="font-size:12px"><s:text name="BUY01_checkedChannel"/></label>
                               <input id="forbiddenChannel" name="channelFlag" type="radio" value="0" disabled="disabled">
                               <label style="font-size:12px"><s:text name="BUY01_nonCheckedChannel"/></label>
                           </div>
		                <div style="padding:.5em;" class="box2-content clearfix" id="channel">
                               <div id="divRule">
                                   <div id="checkedChannelDiv" style="float:left;width:100%;"></div>
                               </div>
                           </div>
                          </div>
               </div>
               </div>
               <p class="clearfix">
                   <button class="right search" type="submit" onclick="BINOLMOBUY01.getUdiskAttendanceStatisticsList();return false;">
                       <span class="ui-icon icon-search-big"></span>
                       <%-- 查询 --%>
                       <span class="button-text"><s:text name="BUY01_search"/></span>
                   </button>
               </p>
       </div>
       </form>
       <div id="section1" class="section hide">
         <div class="section-header">
             <strong>
                 <span class="ui-icon icon-ttl-section-search-result"></span>
                 <%-- 查询结果一览 --%>
                 <s:text name="BUY01_results_list"/>
             </strong>
         </div>
         <div class="section-content">
             <div class="toolbar clearfix">
              <cherry:show domId="BINOLMOBUY0101">
                 <span class="left">
                     <a id="export1" class="export">
                         <span class="ui-icon icon-export"></span>
                         <span class="button-text"><s:text name="global.page.export"/></span>
                     </a>
                 </span>
              </cherry:show> 
                 <span class="right">
                     <a class="setting">
                         <span class="ui-icon icon-setting"></span>
                         <span class="button-text">
                             <%-- 设置列 --%>
                             <s:text name="BUY01_colSetting"/>
                         </span>
                     </a>
                 </span>
             </div>
             <table id="dataTable1" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                 <thead>
                 <tr>
                     <th><s:text name="BUY01_num"/></th><%-- No. --%>
                     <th><s:text name="BUY01_udiskSN"/></th><%-- U盘序列号  --%>
                     <th><s:text name="BUY01_employeeName"/></th><%-- 员工名称  --%>
                     <th><s:text name="BUY01_categoryName"/></th><%-- 岗位 --%>
                     <th><s:text name="BUY01_reginDepart"/></th><%-- 大区--%>
                     <th><s:text name="BUY01_city"/></th><%-- 城市--%>
                     <th><s:text name="BUY01_arriveCountersNm"/></th><%-- 巡柜柜台数 --%>
                     <th><s:text name="BUY01_arriveDays"/></th><%-- 巡柜天数--%>
                     <th><s:text name="BUY01_arriNumPerDay"/></th><%-- 平均每天拜访柜台数 --%>
                     <th><s:text name="BUY01_perCoutArriTime"/></th><%-- 平均每家柜台停留时间(小时)--%>
                     <th><s:text name="BUY01_importTimeDays"/></th><%-- 关键时间段内在柜天数 --%>
                     <th><s:text name="BUY01_arriveDiffCountersNm"/></th><%-- 拜访不同柜台数 --%>
                     <th><s:text name="BUY01_departNum"/></th><%-- 应拜访不同柜台数 --%>
                 </tr>
                 </thead>
             </table>
         </div>
     </div>
	</div>  	 
 <%-- ================== 关键时间段DIV  Start  ======================= --%>
	<div id="importantDivDemo" class="hide">
		<p class="clearfix">
            <span>
            	<span class="left">
            		<s:textfield name="importTimeStartArr" cssClass="text" maxlength="9" cssStyle="width:120px"/>
            		-
            		<s:textfield name="importTimeEndArr" cssClass="text" maxlength="9" cssStyle="width:120px"/>
            	</span>
             	<a title="<s:text name="BUY01_clickToDeleteImportantTime"></s:text>" style="cursor:pointer;margin-top:3px" onclick="BINOLMOBUY01.deleteImportantTime(this);return false;">
           			<span style="font-size:12px"><s:text name="global.page.delete"></s:text></span>
           		</a>
             </span>
		</p>
	</div>
	<%-- ================== 关键时间段DIV  END  ======================= --%>
 </s:i18n>
 <%-- ================== 省市选择DIV START ======================= --%>
	<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<s:if test="%{reginList != null && !reginList.isEmpty()}">
	<div class="clearfix">
		<ul class="u2"><li onclick="BINOLMOBUY01.getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList">
 		<s:set name="provinceList" value="provinceList" />
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="#provinceList">
         		<li id="<s:property value="provinceId" />" onclick="BINOLMOBUY01.getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
   	</s:if>
	</div>
	<div id="cityTemp" class="ui-option hide"></div>
	<%-- 下级区域查询URL --%>
	<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
	<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
	<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
	<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
	<%-- ================== 省市选择DIV  END  ======================= --%>
	
	
	
	<%-- ================== 显示选中的区域DIV  Start  ======================= --%>
	<div id="channelDivDemo" class="hide">
		<span>
            <input type="hidden"></input>
			<span class="bg_title" style="margin: 0px 5px;"><s:property value="channelName"/></span>
			<span onclick="return false;" class="close"><span class="ui-icon ui-icon-close"></span></span>
		</span>
	</div>
	
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
    <script type="text/javascript">
        // 节日
        var holidays = '${holidays }';
        $('#startAttendanceDate1').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#endAttendanceDate1').val();
                return [value,'maxDate'];
            }
        });
        $('#endAttendanceDate1').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#startAttendanceDate1').val();
                return [value,'minDate'];
            }
        });
        
    </script>