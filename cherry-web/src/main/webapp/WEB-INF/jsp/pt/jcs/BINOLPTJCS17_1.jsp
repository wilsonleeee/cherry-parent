<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 语言环境 --%>
<s:i18n name="i18n.pt.BINOLPTJCS17">

<div class="section-content">
	 <s:hidden name="res" value="%{chkExistCntMap.result}"/>
	 <s:hidden name="placeConflict" value="%{configMap.placeConflict}"/><%--地点冲突配置 --%>
     <s:if test='(chkExistCntMap.result != null && chkExistCntMap.result !="")'>
	     <div>
		     <span>
				<div id="errorDiv" class="actionError" >
					<ul>
						<s:if test='configMap.placeConflict == "placeConflict_1"'>
					    	<%-- 当前分配的地点存在以下冲突，点击"继续"则使用当前产品方案覆盖已分配给其他方案的地点。--%>
					    	<span id="errorSpan"> <s:text name="jcs17_msg1"/></span>
						</s:if>
						<s:elseif test='configMap.placeConflict == "placeConflict_0"'>
					    	<%-- 当前分配的地点存在以下冲突，请取消冲突地点原方案的分配。--%>
					    	<span id="errorSpan"> <s:text name="jcs17_msg1_0"/></span>
						</s:elseif>
					</ul>			
			  	</div>
		     </span>
	     </div>
     </s:if>
	 
     <s:if test='(chkExistCntMap != null && chkExistCntMap.size() != 0)'>
     <div class="box-yew" >
     	 <s:if test='(chkExistCntMap.existList != null && chkExistCntMap.existList.size() != 0)'>
     	 
   	 		<span><strong><%-- 当前分配的 --%> <s:text name="jcs17_msg2"/><span class="green"><s:property value='#application.CodeTable.getVal("1311",chkExistCntMap.locationType)'/></span><%-- 已被其他方案分配 --%> <s:text name="jcs17_msg3"/></strong> </span>
		     <hr>
		     <s:iterator value="chkExistCntMap.existList" id="existInfo">
		     	 <span>
		     	 	<span class="green"><s:property value="placeName"/></span>
		     	 	→
		     	 	<span class="red"><s:property value="SolutionName"/></span>
		     	 </span>
			     <hr>
		     </s:iterator>
	     <br/>
	     </s:if>
	     <s:if test='(chkExistCntMap.existCntList != null && chkExistCntMap.existCntList.size() != 0)'>
     	 		<span><strong><%-- 当前分配的 --%> <s:text name="jcs17_msg2"/><span class="green"><s:property value='#application.CodeTable.getVal("1311",chkExistCntMap.locationType)'/></span><%-- 中的部分柜台已被其他方案分配 --%><s:text name="jcs17_msg3"/></strong> </span>
     	 	<hr>
		     <s:iterator value="chkExistCntMap.existCntList" id="existCntInfo">
				 <span>
			     	 <span class="green"><s:property value="curPlaceName"/></span>
			     	 →
			     	 <s:if test="PlaceType == 3">
			     	 	<span class="red"><s:property value="SolutionName"/></span>
			     	 	<span>(<%--渠道--%> <s:text name="channel"/>：<s:property value="ChannelName"/>[<s:property value="DepartName"/>])</span>
			     	 </s:if>
			     	 <s:elseif test="PlaceType == 1">
			     	 	<span class="red"><s:property value="SolutionName"/></span>
			     	 	<span >(<%-- 区域城市 --%><s:text name="regCity"/>：<s:property value="RegionNameChinese"/>[<s:property value="DepartName"/>])</span>
			     	 </s:elseif>
			     	 <s:elseif test="PlaceType == 2">
			     	 	<span class="red"><s:property value="SolutionName"/></span>
			     	 	<span >(<%-- 区域城市指定柜台 --%><s:text name="regCityForCnt"/>：<s:property value="RegionNameChinese"/>[<s:property value="DepartName"/>])</span>
			     	 </s:elseif>
			     	 <s:elseif test="PlaceType == 4">
			     	 	<span class="red"><s:property value="SolutionName"/></span>
			     	 	<span >(<%-- 渠道指定柜台 --%><s:text name="channelForCnt"/>：<s:property value="RegionNameChinese"/>[<s:property value="DepartName"/>])</span>
			     	 </s:elseif>
			     	 <s:elseif test="PlaceType == 6">
			     	 	<span class="red"><s:property value="SolutionName"/></span>
			     	 	<span >(<%--导入柜台--%> <s:text name="byImport"/>：<s:property value="RegionNameChinese"/>)</span>
			     	 </s:elseif>
			     	 <s:elseif test="PlaceType == 7">
			     	 	<span class="red"><s:property value="SolutionName"/></span>
			     	 	<span >(<%--柜台自设--%> <s:text name="门店(柜台)自设"/>：<s:property value="DepartName"/>)</span>
			     	 </s:elseif>	
				 </span>     
		     <hr>
		     
		     </s:iterator>
	     </s:if>
	     <%-- 
	     <span ><s:property value="chkExistCntMap"/></span>
	     --%>
     </div>
     </s:if>
</div>

</s:i18n>