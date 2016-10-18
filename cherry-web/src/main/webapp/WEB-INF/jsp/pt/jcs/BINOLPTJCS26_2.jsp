<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ==================== 分类信息 ====================== --%>
<s:i18n name="i18n.pt.BINOLPTJCS06">
<div id="tabs-2">
   <div class="section">
      <div class="section-content">
        <table class="detail" cellpadding="0" cellspacing="0">
        	<s:iterator value="cateList" status="status">
        		<s:if test='#status.odd == true'><tr></s:if>
        			<th><%-- 分类名 --%>
			    		<s:if test="teminalFlag == 1">
            				<span class="teminalTrans1" title='<s:text name="JCS06.teminalTrans1" />'style="margin:5px 0;">
            				<span><s:property value="propName"/></span>
            				</span>
            			</s:if>
            			<s:elseif test="teminalFlag == 2">
            				<span class="teminalTrans2" title='<s:text name="JCS06.teminalTrans2" />'style="margin:5px 0;">
            				<span><s:property value="propName"/></span>
            				</span>
            			</s:elseif>
            			<s:elseif test="teminalFlag == 3">
            				<span class="teminalTrans3" title='<s:text name="JCS06.teminalTrans3" />'style="margin:5px 0;">
            				<span><s:property value="propName"/></span>
            				</span>
            			</s:elseif>
            			<s:else><span><s:property value="propName"/></span></s:else>
            		</th><%-- 分类名称 --%>
  					<td><span><s:property value="propValName"/></span></td><%-- 分类选项值 --%>
        		<s:if test='#status.even == true || #status.last ==true'></tr></s:if>
        	</s:iterator>
        </table>
      </div>
    </div>
</div>
</s:i18n>
