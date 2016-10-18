<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 

<script type="text/javascript">
$(function() {
	
	// 展开合并按钮事件绑定处理
	$('#popRoleTable span.expand').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-s')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			//$(this).children('.ui-icon').switchClass('ui-icon-triangle-1-s','ui-icon-triangle-1-n');
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			//$(this).children('.ui-icon').switchClass('ui-icon-triangle-1-n','ui-icon-triangle-1-s');
		}
		if($(this).parent('.group-header').parent('.group').children('.group-content').is(':hidden')) {
			$(this).parent('.group-header').parent('.group').children('.group-content').show();
		} else {
			$(this).parent('.group-header').parent('.group').children('.group-content').hide();
		}
		if($(this).parent('td').parent('tr').next('.after').is(':hidden')) {
			//$(this).parent('td').parent('tr').addClass('active').next('.after').addClass('active').show();
			$(this).parent('td').parent('tr').next('.after').addClass('active').removeClass('hide');
		} else {
			//$(this).parent('td').parent('tr').removeClass('active').next('.after').removeClass('active').hide();
			$(this).parent('td').parent('tr').next('.after').removeClass('active').addClass('hide');
		}
		return false;
	}); 
});	
//权限画面左边菜单事件，控制右边子系统的显示和隐藏
function selectSubSys(object,id) {
	$(object).siblings().removeClass("on");
	$(object).addClass("on");
	if(id == null) {
		$("div.group[id^=resource_]").show();
	} else {
		$("#resource_"+id).siblings().hide();
		$("#resource_"+id).show();
	}
}
</script>

<s:i18n name="i18n.pl.BINOLPLRLM01">  
  <s:if test="%{resourceList != null && !resourceList.isEmpty()}">
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
            <strong><s:property value="#moduleIdList.menuNm"/></strong>
            <s:if test="%{#moduleIdList.list != null && !#moduleIdList.list.isEmpty()}">
              <span class="expand"><span class="ui-icon ui-icon-triangle-1-s"></span></span>
            </s:if>
          </div>
          <s:if test="%{#moduleIdList.list != null && !#moduleIdList.list.isEmpty()}">
          <div class="group-content clearfix">
            <table border="0" width="100%" class="editable"> 
              <tr>
                <th style="width: 30%"><s:text name="resource_page"/></th>
                <th style="width: 22%"><s:text name="resource_module"/></th>
                <th style="width: 22%"><s:text name="resource_subSys"/></th>
              </tr>
              <s:iterator value="#moduleIdList.list" id="pageIdList" status="pageStatus">
              <tr>
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
                <td colspan="5">
					<div>
					<span class="after-title"><span class="ui-icon ui-icon-wrench"></span><s:text name="resource_control_title"/></span>
					<ul class="clearfix">
					<s:iterator value="#pageIdList.list" id="control" status="controlStatus">
					  <li>${control.menuNm }|</li>
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
  </s:if>  
</s:i18n>