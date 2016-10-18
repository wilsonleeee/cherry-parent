<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/pos/BINOLMOPOS01.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/pos/BINOLMOPOS01.js"></script>
<s:i18n name="i18n.mo.BINOLMOPOS01">
<form id="mainForm"  class="inline">
<s:text name="global.page.all" id="select_default"/>
<div id="errorMessage"></div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="section" id="section">
  <div class="section-header">
  	<strong>
	  	<span class="ui-icon icon-ttl-section-search-result"></span>
	  	<s:text name="global.page.list"></s:text>
  	</strong>
  </div>

  <div class="section-content">
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="storePayConfigTable">
      <thead>
        <tr>
          <th><s:checkbox id="checkAll" name="validFlag" onclick="bscom03_checkRecord(this,'#storePayConfigBody');"></s:checkbox></th>
          <th><s:text name="MOPOS01_storePayCode"></s:text></th>
          <th><s:text name="MOPOS01_storePayValue"></s:text></th>
        </tr>
      </thead>
      <tbody id="storePayConfigBody">
      <s:iterator value="codeListByCodeGrade" status="status">
      	<tr>
      		<td>
	      		<input name="codeKey" value=<s:property value="CodeKey"/> id="validFlag" onclick="bscom03_checkRecord(this,'#storePayConfigBody');" type="checkbox">
      		</td>
      		<td><s:property value="CodeKey"/></td>
      		<td><s:property value="Value"/></td>
      	</tr>
      </s:iterator>
      </tbody>
    </table>
  </div>
  
</div>
</div>
    <div class="center clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLMOPOS01.cancel();return false;">
    		<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="MOPOS01_cancel"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLMOPOS01.confirm();return false;">
			<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="MOPOS01_confirm"/></span>
		</button>
    </div>
 </form>
</s:i18n>