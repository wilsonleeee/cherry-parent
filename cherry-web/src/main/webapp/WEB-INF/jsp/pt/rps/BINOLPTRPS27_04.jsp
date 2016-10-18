<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/WEB-INF/cherrytld/cherrytags.tld"%>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS27_04.js"></script>
<s:url action="BINOLPTRPS27_getChildrenBussiness" id="getChildrenBussiness"/>
<a id="getChildrenBussiness_url" href="${getChildrenBussiness }" /> 
<s:url action="BINOLPTRPS27_setBussiness" id="setParameter"/>
<a id="setParameter_url" href="${setParameter }" /> 
<input id="parameter_type" type="hidden" value="BT">
<s:i18n name="i18n.pt.BINOLPTRPS27">
	<ul>
		<s:iterator value="bussinessParameterList" id="bussinessParameter">
			<div class="group">
				<div class="group-header clearfix">
					<s:checkbox name="ParameterData" fieldValue="%{#bussinessParameter.inventoryOperationParamId}" value="%{#bussinessParameter.isChecked}" id="dpCheck"></s:checkbox>
					<s:property value="parameterName" />
					<s:if
						test="%{#bussinessParameter.childrenList != null && !#bussinessParameter.childrenList.isEmpty()}">
						<span class="expand"><span class="ui-icon ui-icon-triangle-1-s"></span></span>
					</s:if>
				</div>
				<div class="group-content clearfix">
					<table border="0" width="100%" class="editable">
						<tr>
							<th style="width: 4%"></th>
							<th style="width: 30%"><s:text name="PTRPS27_bussinessType" /></th>
							<th style="width: 22%"><s:text name="PTRPS27_operator" /></th>
							<th style="width: 22%"><s:text name="PTRPS27_sortNo" /></th>
						</tr>
						<s:iterator value="#bussinessParameter.childrenList" id="childMap" status="status">
						<tr>
							<td>
								<s:checkbox name="ParameterData" fieldValue="%{#childMap.inventoryOperationParamId}" value="%{#childMap.isChecked}" id="gpCheck">
								</s:checkbox>
							</td>
							<td>
								<span class="left"><s:property value="#childMap.parameterName" /></span>
			                    <span class="expand right" id="<s:property value="#childMap.parameterData" />">
			                    	<span class="ui-icon ui-icon-triangle-1-s"></span>
			                    </span>
							</td>
							<td><s:text name="#bussinessParameter.parameterName" /></td>
							<td><s:property value="#childMap.parameterSortNo" /></td>
						</tr>
						<tr class="after hide">
			                <td colspan="4">
								<div>
									<span class="after-title"><span class="ui-icon ui-icon-wrench"></span><s:property value="#childMap.parameterName" /></span>
									<ul class="clearfix" id="bt<s:property value="#childMap.parameterData" />">
										<s:iterator value="#childMap.childrenList" id="childMap1" status="status">
											<li>
												<s:checkbox  name="ParameterData" fieldValue="%{#childMap1.inventoryOperationParamId}" value="%{#childMap1.isChecked}" id="btCheck">
												</s:checkbox>
				                    			<s:property value="#childMap1.parameterName" />
			                    			</li>
			                    		</s:iterator>
									</ul>
								</div>
							</td>
			             </tr>
						</s:iterator>
					</table>
				</div>
			</div>
		</s:iterator>
	</ul>
</s:i18n>
