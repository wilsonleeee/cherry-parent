<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM74.js?V=20161027"></script>
<s:i18n name="i18n.ss.BINOLSSPRM74">
	<div class="hide">
		<s:url value="/ss/BINOLSSPRM74_init" id="init_BINOLSSPRM74">
            <s:param name="TN" value=""/>
            <s:param name="TD" value=""/>
            <s:param name="TT" value=""/>
            <s:param name="BC" value=""/>
            <s:param name="CC" value=""/>
            <s:param name="MC" value=""/>
            <s:param name="MP" value=""/>
            <s:param name="ML" value=""/>
            <s:param name="SC" value=""/>
        </s:url>
	</div>
	<div class="clearfix">
		<cherry:form>
			<span class="right"> <%-- 规则添加按钮 --%> 
					<a href="${init_BINOLSSPRM74}" class="add"
						onclick="javascript:openWin(this);return false;"> <span
						class="button-text">打开窗口</span> <span
						class="ui-icon icon-add"></span>
					</a>
			</span>
		</cherry:form>
		</div>
</s:i18n>
