<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTSFH21">
<div id="aaData">
	<s:iterator value="backstageSaleReportList" id="backstageSaleReportMap">
		<ul>
			<li><span>${RowNumber}</span></li>
			<li>
				<span>
					<s:property value="departName"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="startDate"/>
				</span>
			</li>	
			<li>
				<span>
					<s:property value="endDate"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="FPC"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="barCode"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="prtName"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="saleQuantity"/>
				</span>
			</li>
			
			<li>
				<span>
					<s:property value="saleAmount"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="stockQuantity"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="UnitsInTransit"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="stockAmount"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="propNameMid"/>
				</span>
			</li>
			
			<li>
				<span>
					<s:property value="propNameBig"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="Brand"/>
				</span>
			</li>
			<li><span><s:property value="salePrice"/></span></li>		
		</ul>
	</s:iterator>
</div>
</s:i18n>
