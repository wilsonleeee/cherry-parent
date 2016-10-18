<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF16.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#joinDateStart').cherryDate();
	$('#joinDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#joinDateStart').val();
			return [value,'minDate'];
		}
	});
	$('#saleDateStart').cherryDate();
	$('#saleDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#saleDateStart').val();
			return [value,'minDate'];
		}
	});
});
</script>

<s:i18n name="i18n.pl.BINOLPLSCF16">
    <div class="panel-header">
        <div class="clearfix">
        <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span> 
        </div>
    </div>
    <div class="panel-content">
        <cherry:form id="updateIndexForm">
        <div id="section" class="section">
	      <div class="toolbar clearfix section-header">
	        <strong>
	          <span class="ui-icon icon-ttl-section-search-result"></span>
	          <s:text name="BINOLPLSCF16.memIndexMag"></s:text>
	        </strong>
	      </div>
          <div class="section-content">
           	<label><s:text name="BINOLPLSCF16.joinDate"></s:text></label>
           	<s:textfield name="joinDateStart" cssClass="date"></s:textfield>-<s:textfield name="joinDateEnd" cssClass="date"></s:textfield>
           	<s:url action="BINOLPLSCF16_createMemIndex" id="createMemIndexUrl"></s:url>
           	<a class="add" onclick="binolplscf16.updateIndex('${createMemIndexUrl}');return false;">
           	  <span class="ui-icon icon-add"></span>
           	  <span class="button-text"><s:text name="BINOLPLSCF16.createIndex" /></span>
           	</a>
           	<s:url action="BINOLPLSCF16_updMemIndex" id="updMemIndexUrl"></s:url>
           	<a class="add" onclick="binolplscf16.updateIndex('${updMemIndexUrl}');return false;">
           	  <span class="ui-icon icon-add"></span>
           	  <span class="button-text"><s:text name="BINOLPLSCF16.updateIndex" /></span>
           	</a>
           	<s:url value="/common/BINOLCM35_searchMemInit" id="searchMemInitUrl"></s:url>
			<a class="add" onClick="binolplscf16.searchMemInit('${searchMemInitUrl }');return false;">
			  <span class="ui-icon icon-search" style="margin-left:2px;"></span>
			  <span class="button-text"><s:text name="BINOLPLSCF16.searchIndex"></s:text></span>
			</a>
          </div>
          <div class="toolbar clearfix section-header">
            <strong>
              <span class="ui-icon icon-ttl-section-search-result"></span>
              <s:text name="BINOLPLSCF16.saleIndexMag"></s:text>
            </strong>
          </div>
          <div class="section-content">
           	<label><s:text name="BINOLPLSCF16.saleDate"></s:text></label>
           	<s:textfield name="saleDateStart" cssClass="date"></s:textfield>-<s:textfield name="saleDateEnd" cssClass="date"></s:textfield>
           	<s:url action="BINOLPLSCF16_createSaleIndex" id="createMemIndexUrl"></s:url>
           	<a class="add" onclick="binolplscf16.updateIndex('${createMemIndexUrl}');return false;">
           	  <span class="ui-icon icon-add"></span>
           	  <span class="button-text"><s:text name="BINOLPLSCF16.createIndex" /></span>
           	</a>
           	<s:url action="BINOLPLSCF16_updSaleIndex" id="updMemIndexUrl"></s:url>
           	<a class="add" onclick="binolplscf16.updateIndex('${updMemIndexUrl}');return false;">
           	  <span class="ui-icon icon-add"></span>
           	  <span class="button-text"><s:text name="BINOLPLSCF16.updateIndex" /></span>
           	</a>
           	<s:url value="/common/BINOLCM35_searchMemSaleInit" id="searchMemSaleInitUrl"></s:url>
			<a class="add" onClick="binolplscf16.searchMemSaleInit('${searchMemSaleInitUrl }', this);return false;">
			  <span class="ui-icon icon-search" style="margin-left:2px;"></span>
			  <span class="button-text"><s:text name="BINOLPLSCF16.searchIndex"></s:text></span>
			</a>
          </div>
        </div>
        </cherry:form>
    </div>
    <div class="hide">
      <div id="confirmMes"><s:text name="BINOLPLSCF16.confirmMes"></s:text></div>
    </div>
</s:i18n>
