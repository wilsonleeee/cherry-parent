<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/WEB-INF/cherrytld/cherrytags.tld" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS27">
	<div id="aaData">
		<s:iterator value="inventoryOperationStatisticList" id="map">
			<ul>
				<!-- 基础数据 -->
				<li>${RowNumber}</li>
				<li><s:property value="StatisticDate" /></li>
				<li><s:property value="weekday" /></li>
				<li><s:property value="departName" /></li>
				<li><s:property value="#application.CodeTable.getVal('1000',departType)" /></li>
  				<s:iterator id="fMap" value="bussinessParameterList">
					<!-- 柜台与柜台主管 -->
					<s:if test='%{#fMap.validFlag == "1" && #fMap.parameterParent != "DPT"}'>
						<s:iterator id="sMap" value="#fMap.childrenList" status="sStatus" >
							<s:iterator id="tMap" value="#sMap.childrenList" status="tStatus" >
								<s:set value="#tMap.parameterData" var="bussinessType" />
								<s:if test='%{#bussinessType == "BASM"}'>
									<li>${map[bussinessType] }</li>
								</s:if>
								<s:else>
									<li><span>${map[bussinessType] }</span></li>
								</s:else>
							</s:iterator>
						</s:iterator>
					</s:if> 
					<!-- 其他部门 -->   
					<s:if test='%{#fMap.validFlag == "1" && #fMap.parameterParent == "DPT"}'>  	
						<s:iterator value="departList" id="depart">
							<s:iterator id="sMap" value="#fMap.childrenList" status="sStatus" >
								<s:iterator id="tMap" value="#sMap.childrenList" status="tStatus" >
									<s:set value="%{#tMap.parameterData+#depart}" var="bussinessType" />
									<s:if test='%{#tMap.parameterData == "DSEM"}'>
										<li>${map[bussinessType] }</li>
									</s:if>
									<s:else>
										<li><span>${map[bussinessType] }</span></li>
									</s:else>
									
								</s:iterator>
							</s:iterator>
						</s:iterator>
					</s:if>	
				</s:iterator>
			</ul>
		</s:iterator>
	</div>
</s:i18n>