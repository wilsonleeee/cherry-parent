<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTJCS09">
<div id="aaData">
	<s:iterator value="logicDepotList" id="jcs" >
		<ul >
			<%-- 
		  	<li>
		  		<input type="checkbox" id="checkbox1" name="logicDepotId" onclick="BINOLSTJCS09.checkSelect()" value="<s:property value="logicDepotId+'*'+businessType"/>"/>
        	</li>
        	 --%>
		 	<%-- NO. --%>
		 	<li><s:property value="RowNumber"/></li>
		 	<%--业务所属--%>
		    <li><span><s:property value='#application.CodeTable.getVal("1181",logicType)'/></span></li>
		 	<%--产品类型--%>
		 	<li><span><s:property value='#application.CodeTable.getVal("1134",productType)'/></span></li>
		 	<%--业务类型--%>
		 	<s:if test='logicType == "0"'> 
		 		<li><span><s:property value='#application.CodeTable.getVal("1133",businessType)'/></span></li>
		 	</s:if>
		 	<s:else>
		 		<li><span><s:property value='#application.CodeTable.getVal("1184",businessType)'/></span></li>
		 	</s:else>
		 	<%--子类型--%>
		 	<li><span><s:property value='#application.CodeTable.getVal("1168",subType)'/></span></li>
		 	<%--逻辑仓库--%>
		    <li><span><s:property value='"("+logicInventoryCode+")"+inventoryName'/></span></li>
		    <%--逻辑仓库类型--%>
		    <%-- <li><span><s:property value='#application.CodeTable.getVal("1143",type)'/></span></li> --%>
		 	<%--优先级--%>
			<li><span><s:property value="configOrder"/></span></li>
		 	<%--备注--%>
			<li><span><s:property value="comments"/></span></li>
			<%--操作--%>
			<li>
				<span>
					<cherry:show domId="BINOLSTJCS09EDI">
				       	<a href="#" class="delete"  id="<s:property value='logicDepotId'/>" onclick="BINOLSTJCS09.popEditDialog(this);return false;">
					 		<span class="ui-icon icon-edit"></span>
					 		<span class="button-text"><s:text name="JCS09_edit"/></span>
					 	</a>
				 	</cherry:show>
					<cherry:show domId="BINOLSTJCS09DEL">
				       	<a href="#" class="delete"  id="delete" onclick="BINOLSTJCS09.delMsgInit('<s:property value="logicDepotId"/>');return false;">
					 		<span class="ui-icon icon-delete"></span>
					 		<span class="button-text"><s:text name="JCS09_delete"/></span>
					 	</a>
						
				 	</cherry:show>
				</span>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>