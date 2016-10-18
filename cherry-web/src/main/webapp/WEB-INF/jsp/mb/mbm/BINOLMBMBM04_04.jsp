<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM04"> 
	<table class="detail point-info" style="margin-bottom: 3px;">
           <tr>
		  <th><s:text name="binolmbmbm04_freezePoint" /></th>
		  <td><span><s:property value="memPointInfo.totalPoint"/></span></td>
		  <th><s:text name="binolmbmbm04_changablePoint" /></th>
		  <td><span><s:property value="memPointInfo.changablePoint"/></span></td>
	    </tr>
	  </table>
         <table class="detail point-info" style="margin-bottom: 3px;">
           <tr>
		  <th><s:text name="binolmbmbm04_totalPoint" /></th>
		  <td><span><s:property value="memPointInfo.totalPoint"/></span></td>
		  <th><s:text name="binolmbmbm04_totalChanged" /></th>
		  <td><span><s:property value="memPointInfo.totalChanged"/></span></td>
	    </tr>
	  </table>
         <table class="detail point-info" style="margin-bottom: 3px;">
           <tr>
		  <th><s:text name="binolmbmbm04_curDisablePoint" /></th>
		  <td><span><s:property value="memPointInfo.curDisablePoint"/></span></td>
		  <th><s:text name="binolmbmbm04_curDealDate" /></th>
		  <td><span><s:property value="memPointInfo.curDealDate"/></span></td>
	    </tr>
	    <tr>
		  <th><s:text name="binolmbmbm04_totalDisablePoint" /></th>
		  <td><span><s:property value="memPointInfo.totalDisablePoint"/></span></td>
		  <th><s:text name="binolmbmbm04_preDisableDate" /></th>
		  <td><span><s:property value="memPointInfo.preDisableDate"/></span></td>
	    </tr>
	  </table>
</s:i18n>