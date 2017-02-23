<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.ss.BINOLSSPRM13">
<div class="box4" id="baseInfo">
	<div class="box4-header clearfix">
		<strong class="left"><s:text name="grpTime" /></strong>
		<s:if test='!"edit".equals(showType) && !"copy".equals(showType)'>
		<%-- 添加活动组 --%>
		<a class="add right" id="addActGrp"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="addPrmActiveGrp"/></span></a>
		<%-- 编辑活动组 --%>
		<a href="#" class="add right" onclick="eidtPrmActGrop();"><span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="editPrmActiveGrp"/></span></a>
		</s:if>
	</div>
	<div class="box4-content clearfix">
		<table class="detail">
           	<tbody>
               	<tr>
                   <th><s:text name="prmActiveName"/><span class="red">*</span></th>
                   <td><span><input class="text" name ="prmActiveName" maxlength="50" value="<s:property value='map.prmActiveName'/>"/></span></td>
                   <th><s:text name="selectBrandName"/><span class="highlight">*</span></th>
                   <td><s:select list="brandInfoList" name="brandInfoId" listKey="brandInfoId" listValue="brandName" id="brandInfoId" value="%{map.brandInfoId}" /></td>
               	</tr>
			  	<tr>
				  	<th><s:text name="prmActiveGrp"/><span class="highlight">*</span></th>
				  	<td colspan="3">
				  		<s:if test='"edit".equals(showType) || "copy".equals(showType)'>
				  			<s:select cssClass="left" list="prmActiveGrpList" id="prmActGrp" name="prmActGrp" onchange="groupChange(this);" 
					  		listKey="promotionActGrpID" listValue="groupName" cssStyle="width:185px;" value="%{map.actGrpID}" disabled="true"></s:select>
					  		<input type="hidden" name="prmActGrp" value="<s:property value='map.actGrpID'/>"/>
				  		</s:if>
				  		<s:else>
					  		<s:select cssClass="left" list="prmActiveGrpList" id="prmActGrp" name="prmActGrp" onchange="groupChange(this);" 
					  		listKey="promotionActGrpID" listValue="groupName" cssStyle="width:185px;" value="%{map.actGrpID}"></s:select>
				  		</s:else>
				  		<div id="groupInfo"><jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM13_3.jsp" flush="true"/></div>
				  	</td>
			  	</tr>
			  	<tr>
				  <th><div><s:text name="prmActiveType"/></div></th>
				  <td>
				  	<s:if test='"edit".equals(showType) || "copy".equals(showType)'>
				  		<select disabled="disabled" style="width:185px;">
				  			<option>
				  			<s:property value="#application.CodeTable.getVal('1185',map.activityType)"/>
				  			<s:property value="#application.CodeTable.getVal('1210',map.activityType)"/>
				  			</option>
				  		</select>
				  		<input type="hidden" name="activityType" value="<s:property value='map.activityType'/>"/>
				  	</s:if>
				  	<s:else>
				  		<s:select id="activityType_DHHD" name="activityType" list='#application.CodeTable.getCodes("1185")' 
				  		cssStyle="width:185px;display:none;" listKey="CodeKey" listValue="Value" value="%{map.activityType}"/>
				  		<s:select id="activityType_CXHD" name="activityType" list='#application.CodeTable.getCodes("1210")' 
				  		cssStyle="width:185px;display:none;" listKey="CodeKey" listValue="Value" value="%{map.activityType}" headerKey="" headerValue=""/>
				  	</s:else>
				  </td>
				  <th rowspan="2"><s:text name="prmDescription"/></th>
				  <td rowspan="2">
				  	<textarea name ="descriptionDtl" maxlength="100" style="height: 40px;padding:0px;"><s:property value='map.descriptionDtl'/></textarea>
				  </td>
			  	</tr>
			  	<tr>
			  	  <th><s:text name="terminalChange"/><span class="highlight">*</span></th>
				  <td><s:select name="mainModify" list='#application.CodeTable.getCodes("1040")' cssStyle="width:185px;" listKey="CodeKey" listValue="Value" value="%{map.mainModify}"/></td>	
			  	</tr>
				<%--<s:if test='"1".equals(map.couponFlag)'>--%>
					<%--<tr>--%>
						<%--<th><s:text name="systemCode"/></th>--%>
						<%--<td>--%>
						<%--<span>--%>
							<%--<s:select name="systemCode" list='#application.CodeTable.getCodes("1426")' cssStyle="width:185px;"--%>
									  <%--listKey="CodeKey" listValue="Value" value="%{map.systemCode}" headerKey="" headerValue=""/>--%>
						<%--</span>--%>
						<%--</td>--%>
						<%--<th><s:text name="linkMainCode"/></th>--%>
						<%--<td>--%>
						<%--<span><input id="linkMainCode" class="text" name="linkMainCode" style="width: 140px;" value="<s:property value='map.linkMainCode'/>"/>--%>
						<%--</span>--%>
						<%--</td>--%>
					<%--</tr>--%>
				<%--</s:if>--%>
   			</tbody>
   		</table>
	</div>
</div>
<s:if test='!"edit".equals(showType) && !"copy".equals(showType)'>
<div id="prm_act_grp_dialog_Main" >
<div class="dialog2 clearfix" style="display:none" id="prm_act_grp_dialog">
	<p class="clearfix">
		<%-- 促销活动组名 --%>
		<label><s:text name="prmActiveGrpName"/></label>
		<span class="highlight">*</span>
		<input type ="text" class ="text" name ="groupName" id="groupName" maxlength="50"/>
	</p>
	<p class="clearfix">
		<%-- 促销活动组类型 --%>
		<label><s:text name="prmActiveGrpType"/></label>
		<span style="margin-left:23px;">
			<select id="prmGrpType" name="prmGrpType" onchange="showSetReserve(this);">
				<s:iterator value='#application.CodeTable.getCodes("1174")'>
					<s:if test='CodeKey == "CXHD" || CodeKey == "DHHD"'>
					<option value="${CodeKey}">${Value}</option>
					</s:if>
				</s:iterator>
			</select>
		</span>
	</p>
	<p class="hide" id="choiceReserve">
		<%-- 是否需要预约 --%>
		<label><s:text name="needReserve"/></label>
		<span style="margin-left:12px;">
			<s:select name="needReserve" id="needReserve" onchange="showSetTime(this);" list='#application.CodeTable.getCodes("1148")' listKey="CodeKey" listValue="Value"/>
		</span>
	</p>
	<%-- 活动组时间设置DIV --%>
	<div id="grpSetTimeDiv" class="box2-active hide" >
		<div class="box2-content start clearfix" style="padding:5px;margin-bottom:5px;">
		<div class="clearfix">
			<%-- 促销活动组预约开始时间 --%>
			<label><s:text name="prmActiveGrpReserveBeginDate"/></label>
			<span class="highlight">*</span>
			<span><input type="text" style="width:80px;" class="date startTime" name="reserveBeginDate"/></span>
		</div>
		<div class="clearfix">
			<%-- 促销活动组预约结束时间 --%>
			<label><s:text name="prmActiveGrpReserveEndDate"/></label>
			<span class="highlight">*</span>
			<span><input type="text" style="width:80px;" class="date endTime" name="reserveEndDate"/></span>
		</div>
		</div>
	</div>
	<div class="box2-active" >
		<div class="box2-content start" style="padding:5px;margin-bottom:5px;">
			<div class="clearfix">
				<%-- 促销活动组领用开始时间 --%>
				<label><s:text name="prmActiveGrpActivityBeginDate"/></label>
				<span class="highlight">*</span>
				<span><input type="text" style="width:80px;" class="date startTime" name="activityBeginDate"/></span>
			</div>
			<div class="clearfix">
				<%-- 促销活动组领用结束时间 --%>
				<label><s:text name="prmActiveGrpActivityEndDate"/></label>
				<span class="highlight">*</span>
				<span><input type="text" style="width:80px;" class="date endTime" name="activityEndDate"/></span>
				</div>
			</div>
		</div>
	</div>
</div>
</s:if>
</s:i18n>