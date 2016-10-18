<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript" src="/Cherry/js/pl/rlm/BINOLPLRLM03.js"></script>

<s:i18n name="i18n.pl.BINOLPLRLM01">  
<form id="resourceForm"> 
  <s:hidden name="roleId" id="roleId"></s:hidden>
  <s:iterator value="resourceList" id="resource">
  	<s:if test='%{"LG".equals(#resource.menuId)}'>
  	  <s:hidden name="menuId" value="%{#resource.menuId}"></s:hidden>
  	</s:if>
  </s:iterator>
  <div class="sidemenu2 left" style="width:140px;">
	<div class="sidemenu2-header"><strong><s:text name="resource_filter_title"/></strong></div>
    <ul class="u1">
      <li onclick="selectSubSys(this,null);" class="on"><s:text name="resource_all"/></li>
      <s:iterator value="resourceList" id="resource">
      	<li onclick="selectSubSys(this,'<s:property value="#resource.menuId"/>');"> <s:property value="#resource.menuNm"/></li>
      </s:iterator>
	</ul>
  </div>
  <div class="left" style="width:78%">
  <s:iterator value="resourceList" id="resource" status="resourceStatus">
    <div class="group" id='resource_<s:property value="#resource.menuId"/>'>
      <div class="group-header clearfix">
        <s:checkbox name="menuId" fieldValue="%{#resource.menuId}" value="%{#resource.isChecked}" id="subSysId"></s:checkbox>
        <strong><s:property value="#resource.menuNm"/></strong>
        <s:if test="%{#resource.list != null && !#resource.list.isEmpty()}">
          <span class="expand"><span class="ui-icon ui-icon-triangle-1-s"></span></span>
        </s:if>
      </div>
      <s:if test="%{#resource.list != null && !#resource.list.isEmpty()}">
      <div class="group-content clearfix">
      <s:iterator value="#resource.list" id="moduleIdList" status="moduleStatus">
        <div class="group">
          <div class="group-header">
            <s:checkbox name="menuId" fieldValue="%{#moduleIdList.menuId}" value="%{#moduleIdList.isChecked}" id="moduleId"></s:checkbox>
            <strong><s:property value="#moduleIdList.menuNm"/></strong>
            <s:if test="%{#moduleIdList.list != null && !#moduleIdList.list.isEmpty()}">
              <span class="expand"><span class="ui-icon ui-icon-triangle-1-s"></span></span>
            </s:if>
          </div>
          <s:if test="%{#moduleIdList.list != null && !#moduleIdList.list.isEmpty()}">
          <div class="group-content clearfix">
            <table border="0" width="100%" class="editable"> 
              <tr>
                <th style="width: 4%"></th>
                <th style="width: 30%"><s:text name="resource_page"/></th>
                <th style="width: 22%"><s:text name="resource_module"/></th>
                <th style="width: 22%"><s:text name="resource_subSys"/></th>
              </tr>
              <s:iterator value="#moduleIdList.list" id="pageIdList" status="pageStatus">
              <tr>
                <td><s:checkbox name="menuId" fieldValue="%{#pageIdList.menuId}" value="%{#pageIdList.isChecked}" id="pageId"></s:checkbox></td>
                <td>
                  <p><span class="left"><s:property value="#pageIdList.menuNm"/></span></p>
                  <s:if test="%{#pageIdList.list != null && !#pageIdList.list.isEmpty()}">
                    <span class="expand right"><span class="ui-icon ui-icon-triangle-1-s"></span></span>
                  </s:if>
                </td>
                <td><p><s:property value="#moduleIdList.menuNm"/></p></td>
                <td><p><s:property value="#resource.menuNm"/></p></td>
              </tr>
              <s:if test="%{#pageIdList.list != null && !#pageIdList.list.isEmpty()}">
              <tr class="after hide">
                <td colspan="4">
					<div>
					<span class="after-title"><span class="ui-icon ui-icon-wrench"></span><s:text name="resource_control_title"/></span>
					<ul class="clearfix">
					<s:iterator value="#pageIdList.list" id="control" status="controlStatus">
					  <li><s:checkbox name="menuId" fieldValue="%{#control.menuId}" value="%{#control.isChecked}" id="controlId"></s:checkbox>${control.menuNm }</li>
					</s:iterator>
					</ul>
					</div>
				</td>
              </tr>
              </s:if>
              </s:iterator>
            </table>
          </div>
          </s:if>
      </div>
      </s:iterator>
      </div>
      </s:if>
    </div>
  </s:iterator>    
  </div>
</form>      
</s:i18n>
<div class="hide">
<s:url action="BINOLPLRLM03_authorize" id="roleAuthorizeUrl"></s:url>
<a id="roleAuthorizeUrl" href="${roleAuthorizeUrl }"></a>
</div>