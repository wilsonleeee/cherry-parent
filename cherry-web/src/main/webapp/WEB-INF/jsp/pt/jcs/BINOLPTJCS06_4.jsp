<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- 语言 -->
<s:set id="language" value="#session.language" />
<%-- ==================== 图片信息 ====================== --%>
<s:i18n name="i18n.pt.BINOLPTJCS06">
<div id="tabs-4" class="ui-tabs-panel">
   <div class="section">
      <div class="section-content">
        <ul class="u3 clearfix">
        	<s:iterator value="imgList">
        		<li>
        			<a href="#"><img alt="<s:if test="#language.equals('en_US')"><s:property value='nameEN'/></s:if><s:else><s:property value='nameCN'/></s:else>" 
        			src="<s:property value='path'/>" width="120" height="120"></a>
        		</li>
        	</s:iterator>
        </ul>
      </div>
    </div>
</div>
</s:i18n>
