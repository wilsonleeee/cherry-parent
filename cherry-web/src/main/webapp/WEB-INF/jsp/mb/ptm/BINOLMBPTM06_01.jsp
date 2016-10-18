<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBPTM06">
 <div class="section-header">
        <strong>
     		<span class="ui-icon icon-ttl-section-info"></span>
     		<s:text name="global.page.reportSummy"/>
     	</strong>
     </div>
     <div class="section-content">
        <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="60%">
        	<thead>
		        <tr>
		           <th><s:text name="BINOLMBPTM06_memberTotal"/></th>
		           <th><s:text name="BINOLMBPTM06_newMemberTotal"/></th>
		           <th><s:text name="BINOLMBPTM06_newMemberQRPointsTotal"/></th>
		           <th><s:text name="BINOLMBPTM06_oldMemberTotal"/></th>
		           <th><s:text name="BINOLMBPTM06_oldMemberQRPointsTotal"/></th>
		       	</tr>
	       	</thead>
	       	<tbody>
				<tr>
				   <td><span class="right"><s:property value="pointInfoSummy.memberTotal"/></span></td>
		           <td><span class="right"><s:property value="pointInfoSummy.newMemberTotal"/></span></td>
		           <td><span class="right"><s:property value="pointInfoSummy.newMemberQRPointsTotal"/></span></td>
		           <td><span class="right"><s:property value="pointInfoSummy.oldMemberTotal"/></span></td>
		           <td><span class="right"><s:property value="pointInfoSummy.oldMemberQRPointsTotal"/></span></td>
				</tr>
			</tbody>
  		</table>
 	</div>

</s:i18n>