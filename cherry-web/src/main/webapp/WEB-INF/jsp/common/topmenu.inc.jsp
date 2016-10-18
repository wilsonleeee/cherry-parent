<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script language="javascript">

function requestTopMenu(MENU_URL,MENU_ID){
	var action = $("#hiddenbasePath").val()+MENU_URL + "?MENU_ID=" +MENU_ID;
	
	//if(!submitflag){
	//	return false;
	//}
	if($("#csrftoken").val()==undefined){	
		return false;
	}
	//submitflag = false;	
	$("#menuform").attr("action",action);
	$("#menuform").append("<input type='hidden' name='csrftoken' value='"+ $("#csrftoken").val()+"'/>");
	$("#menuform").submit();
	
}
</script>
<form id="menuform" method="post"></form>
<s:i18n name="i18n.common.commonText">
<input type="hidden" value="<%= request.getContextPath() %>" id="hiddenbasePath"></input>
<input type="hidden" value="<s:property value='#parameters.MENU_ID'/>" id="currentTopMenu"></input>
<s:iterator value="#session.topmenu" id="topmenumap">
 	<li id="<s:property value='#topmenumap.MENU_ID'/>" class="" >
    <a style="cursor:pointer;"  onclick ='requestTopMenu("<s:property value='#topmenumap.MENU_URL'/>","<s:property value='#topmenumap.MENU_ID'/>")' ><s:text name="%{#topmenumap.MENU_ID}" /></a>
    </li>
</s:iterator>

<script language="javascript">
var currenttopmenu = $('#currentTopMenu').val();
	$('#'+currenttopmenu).addClass("on");
</script>
</s:i18n>
