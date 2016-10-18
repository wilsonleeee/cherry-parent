<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOMAN06">

<div id="aaData">
	<s:iterator value="udiskList" status="status">
		<ul>
			<%--选择 --%>
			<li> 
				<s:if test='ownerRight == 0'>
					<s:checkbox id="%{udiskInfoId}" name="validFlag" value="false" fieldValue="0" onclick="binOLMOMAN06.checkSelect(this);"/>
				</s:if>
           		<s:else>
           			<s:checkbox id="%{udiskInfoId}" name="validFlag" value="false" fieldValue="1" onclick="binOLMOMAN06.checkSelect(this);"/>
           		</s:else>
        	</li>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- U盘SN --%>
			<li>
				<span><s:property value='udiskSn'/></span>
			</li>
			<%-- 员工姓名 --%>
			<li>
				<span id="employee">
					<s:if test='"".equals(employeeName) || employeeName == null'>
						<label class="highlight"><s:text name="MAN06_noEmployee"></s:text></label>
					</s:if>
					<s:else>
						<s:property value='"("+employeeCode+")"+employeeName'/>
					</s:else>
				</span>
			</li>
			<%-- 权限级别  --%>
			<li><span id="ownerRight">
				<s:if test='ownerRight == 0'>
					<label class="highlight"><s:text name="MAN06_beDisabled"></s:text></label>
				</s:if>
				<s:else>
					<s:property value='categoryName'/>
				</s:else>
				</span>
			</li>
			<%-- 品牌 --%>
			<li><span><s:property value='"("+brandCode+")"+brandName'/></span></li>
			<%-- 操作 --%>
			<li>
				<span id="option">
					<s:if test='ownerRight == 0'>
						<a class="add" onclick="binOLMOMAN06.popDataTableOfEmployee(this,$('#brandInfoId').serialize());return false;" id="<s:property value='udiskInfoId'/>">
                    		<span class="ui-icon icon-add"></span>
                    		<span class="button-text"><s:text name="MAN06_bind"></s:text></span>
                		</a>
					</s:if>
				</span>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>