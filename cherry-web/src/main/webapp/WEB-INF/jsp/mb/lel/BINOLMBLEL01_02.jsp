<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script>
$(function(){
	//日期初始化
	BINOLMBLEL01.initDate();
});
</script>
<%-- 会员等级级别调整URL --%>
<s:url id="next_url" value="BINOLMBLEL01_next"/>
<%-- 会员等级查询初始化画面URL --%>
<s:url id="init_url" value="BINOLMBLEL01_init"/>
<%-- 会员有效期保存URL --%>
<s:url id="saveMemberDate_url" value="BINOLMBLEL01_saveMemberDate"/>
<s:i18n name="i18n.mb.BINOLMBLEL01">
<div class="panel-header">
     <div class="clearfix">
       	<span class="breadcrumb left">
       	<span class="ui-icon icon-breadcrumb"></span><s:text name="lel01.title"/> &gt; <s:text name="lel01.subTitle"/></span>
     </div>
</div>
   <div class="panel-content">
       <ol class="steps clearfix">
         <li class="on" style="width:49.9%"><span><s:text name="lel01.navigation1"/></span></li><%-- 1.设置会员等级 --%>
         <li class="last" style="width:49.9%"><span><s:text name="lel01.navigation2"/></span></li><%-- 2.设置等级级别 --%> 
       </ol>
       	<%-- ================== 错误信息提示 START ======================= --%>
    	<div id="actionResultDisplay"></div>
    	<%-- ================== 错误信息提示   END  ======================= --%>
       <div class="box2">
         <div class="box3">
         	<div <s:if test='memberClubId != null && !"".equals(memberClubId)'>class="left" </s:if>><strong><s:text name="lel01.brand"/>：</strong><s:property value="brandName"/>
         	<input type="hidden" id="brandInfoId" name="brandInfoId" value="<s:property value="brandInfoId"/>"/>
         	<input type="hidden" id="brandName" name="brandName" value="<s:property value="brandName"/>"/>
         	</div>
         	<s:if test='memberClubId != null && !"".equals(memberClubId)'>
         		<div class="center"><strong><s:text name="lel01.clubName" />：</strong><s:property value="memberClubName"/> </div>
         		<input type="hidden" id="memberClubId" name="memberClubId" value="<s:property value="memberClubId"/>"/>
         	</s:if>
         </div>
         <div class="box2-header clearfix">
         	<strong class="left"><s:text name="lel01.setting"/></strong><%-- 会员等级设定 --%> 
         	<a class="add right" onclick="BINOLMBLEL01.addLine('#levelTable','#levelInfo');return false;">
         		<span class="ui-icon icon-add"></span>
         		<span class="button-text"><s:text name="lel01.add"/></span><%-- 添加会员等级 --%> 
         	</a>
         </div>
         <div class="box2-content clearfix" style="padding:.5em .5em 0 .5em; overflow:auto">
         	<cherry:form id="mainForm">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
            	<thead>
              	<tr>
                	<th scope="col" style="width: 4%;"><s:text name="lel01.No"/></th><%-- No. --%>
                	<th scope="col" style="width: 15%;"><s:text name="lel01.name"/></th><%-- 等级名称 --%>
                	<th scope="col" style="width: 14%;"><s:text name="lel01.code"/></th><%-- 等级编码 --%>
                	<th scope="col" style="width: 23%;"><s:text name="lel01.despt"/></th><%-- 会员等级描述--%>
                	<th scope="col" style="width: 20%;"><s:text name="lel01.levelDate"/></th><%-- 会员有效期 --%>
                	<th scope="col" style="width: 8%;" class="center"><s:text name="lel01.option"/></th><%-- 操作 --%>
             	</tr>
             	</thead>
             	<tbody id="levelTable">
              		<s:iterator value="levelList" status="status">
	               	<tr class="<s:if test="#status.even">even</s:if><s:else>odd</s:else>">
	               		<td><s:property value="#status.count"/></td>
	               		<s:if test="enable == 0">
	               			<%-- 等级名称 --%>
		               		<td>
		               			<span><input class="text" name="levelName" value="<s:property value="levelName"/>"/><span 
		               				class="highlight"><s:text name="global.page.required"/></span></span>
		               		</td>
		               		<%-- 会员等级编码--%>
		               		<td>
		               			<span><input class="text" name="levelCode" value="<s:property value="levelCode"/>" style="width: 100px;"/></span>
		               		</td>
		               		<%-- 会员等级描述--%>
		               		<td>
		               			<span><input class="text" name="description" value="<s:property value="description"/>" maxlength="100"/></span>
		               			<input type="hidden" name="levelId" value="<s:property value="levelId"/>"/>
		               		</td>
		                	<td id="memberDate">
		                		<span><s:property value="memberDate"/><s:text name='%{textName}'/></span>
		                		<input type="hidden" name="memberDate" value="<s:property value='memberDate'/>"/>
		                		<input type="hidden" name="textName" value="<s:property value='textName'/>"/>
		                		<input type="hidden" name="periodvalidity" id="periodvalidity" value="<s:property value='periodvalidity'/>"/>
		                		<span><a class="edit right" onclick="BINOLMBLEL01.addMsgInit(this, 'BINOLMBLEL01_saveMemberDate', '<s:property value="levelId"/>');return false;">
	          					<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="lel01.setDate" /></span>
	          					</a></span>
	          				</td>
		                	<%-- 操作 --%>
		                	<td class="center">
		                		<s:if test='"0".equals(deleteFlag) '>
		                		<a class="delete" onclick="BINOLMBLEL01.delLine(this);return false;">
		                			<span class="ui-icon icon-delete"></span>
		                			<span class="button-text"><s:text name="global.page.delete"/></span>
		                		</a>
		                		</s:if>
		                		<input type="hidden" name="enable" value="0"/>	
		                	</td>
	               		</s:if>
	               		<s:else>
	               			<%-- 等级名称 --%>
		               		<td>
		               			<span><input class="text" name="levelName" value="<s:property value="levelName"/>"/></span>
		               			<input type="hidden" name="levelId" value="<s:property value="levelId"/>"/>
		               		</td>
		               		<%-- 会员等级编码--%>
		               		<td>
		               			<span><input class="text" name="levelCode" value="<s:property value="levelCode"/>" disabled="disabled" style="width: 100px;"/></span>
		               		</td>
		               		<%-- 会员等级描述--%>
		               		<td>
		               			<span><input class="text" name="description" value="<s:property value="description"/>"/></span>
		               		</td>
		                	<%-- 会员有效期 --%>
		                	<td id="memberDate">
		                		<span><s:property value="memberDate"/><s:text name='%{textName}'/></span>
		                		<input type="hidden" name="memberDate" value="<s:property value='memberDate'/>"/>
		                		<input type="hidden" name="textName" value="<s:property value='textName'/>"/>
		                		<input type="hidden" name="periodvalidity" id="periodvalidity" value="<s:property value='periodvalidity'/>"/>
		                		<span><a class="edit right" onclick="BINOLMBLEL01.addMsgInit(this, 'BINOLMBLEL01_saveMemberDate', '<s:property value="levelId"/>');return false;">
	          					<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="lel01.setDate" /></span>
	          					</a></span>
	          				</td>
		                	<%-- 操作 --%>
		                	<td>
		                		<span>
		                			<input type="hidden" name="enable" value="1"/>
		                		</span>
		                	</td>
	               		</s:else>
	              	</tr>
              	</s:iterator>
              	</tbody>
            </table>
           </cherry:form>
           <hr class="space" />
           <p class="highlight"><s:text name="lel01.tip" /></p>
         </div>
         <hr class="space" />
         <div class="center clearfix">
           <button class="back" onclick="BINOLMBLEL01.list('${init_url}');return false;">
           	<span class="ui-icon icon-cancel"></span>
           	<span class="button-text"><s:text name="global.page.cancle" /></span>
           </button>
           <button class="save" onclick="BINOLMBLEL01.next('${next_url}');return false;">
           	<span class="ui-icon icon-mover"></span>
           	<span class="button-text"><s:text name="global.page.next" /></span>
           </button>
         </div>
       </div>
   </div>
<div class="hide">
	<%-- 保存删除的会员等级Id --%>
	<div id="delList">
		<s:iterator value="delList">
			<span><input type="hidden" name="levelId" value="<s:property value="levelId"/>"/></span>
		</s:iterator>
	</div>
	<table>
	<tbody id="levelInfo">
		<tr>
			<td></td>
			<td>
				<span><input class="text" name="levelName" value=""/><span class="highlight"><s:text name="global.page.required"/></span></span>
			</td>
			<td><span><input class="text" name="levelCode" value="" style="width: 100px;"/></span></td>
			<td><span><input class="text" name="description" value="" maxlength="100"/></span></td>
			<td id="memberDate">
				<span></span>
             	<input type="hidden" name="memberDate" value="<s:property value='memberDate'/>"/>
             	<input type="hidden" name="textName" value="<s:property value='textName'/>"/>
		        <input type="hidden" name="periodvalidity" id="periodvalidity" value="<s:property value='periodvalidity'/>"/>
          		<span><a class="edit right" onclick="BINOLMBLEL01.addMsgInit(this, 'BINOLMBLEL01_saveMemberDate', '<s:property value="levelId"/>');return false;">
				<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="lel01.setDate" /></span>
				</a></span>
			</td>
			<td class="center">
          		<a class="delete" onclick="BINOLMBLEL01.delLine(this);return false;">
          			<span class="ui-icon icon-delete"></span>
          			<span class="button-text"><s:text name="global.page.delete"/></span>
          		</a>
          		<input type="hidden" name="enable" value="0"/>	
          	</td>
		</tr>
	</tbody>
	</table>
</div>
</s:i18n>
<%-- ======================= 会员有效期设定POP开始  ============================= --%>
<jsp:include page="/WEB-INF/jsp/mb/lel/BINOLMBLEL01_04.jsp" flush="true"/>
<%-- ======================= 会员有效期设定POP结束  ============================= --%>
<input type="hidden" id="dateHolidays" name="dateHolidays" value="${holidays}"/>