<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 会员等级级别调整上一步URL --%>
<s:url id="back_url" value="BINOLMBLEL01_back"/>
<%-- 会员等级保存URL --%>
<s:url id="save_url" value="BINOLMBLEL01_save"/>
<s:i18n name="i18n.mb.BINOLMBLEL01">
<div class="panel-header">
	<div class="clearfix">
       	<span class="breadcrumb left">
       	<span class="ui-icon icon-breadcrumb"></span><s:text name="lel01.title"/> &gt; <s:text name="lel01.subTitle"/></span>
       </div>
    </div>
    <div class="panel-content">
      	<ol class="steps clearfix">
         	<li class="prev" style="width:49.9%"><span><s:text name="lel01.navigation1"/></span></li><%-- 1.设置会员等级 --%>
         	<li class="on last" style="width:49.9%"><span><s:text name="lel01.navigation2"/></span></li><%-- 2.设置等级级别 --%> 
       </ol> 
       	<%-- ================== 错误信息提示 START ======================= --%>
    	<div id="actionResultDisplay"></div>
    	<%-- ================== 错误信息提示   END  ======================= --%> 
       <div class="box2">
       		<cherry:form>
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
         	<div class="box2-header clearfix"><strong class="left"><s:text name="lel01.set_lel"/></strong></div>
         	<div id="tables" class="box2-content clearfix" style="padding:.5em .5em 0 .5em;">
         		<s:set id="size" value="levelList.size()"/>
         		<s:if test="#size > 0">
		           	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		             	<thead>
				            <tr>
				                <th scope="col" style="width: 20%;"><s:text name="lel01.grade"/></th><%-- 会员等级 --%>
				                <th scope="col" style="width: 15%;"><s:text name="lel01.name"/></th><%-- 等级名称 --%>
				                <th scope="col" style="width: 20%;"><s:text name="lel01.defaultLevel"/></th><%-- 默认等级 --%>
				                <th scope="col"><s:text name="lel01.option"/></th><%-- 操作 --%>
				            </tr>
			            </thead>
			            <tbody id="setGradeTable">
			            <s:iterator value="levelList" id="it1" status="st1">
			            	<tr id="<s:property value="#st1.count"/>" class="<s:if test="#st1.even">even</s:if><s:else>odd</s:else>">
				                <td>
				                	<s:iterator begin="1" end="#st1.count">
						       			<span class="ui-icon icon-star-big"></span>
						       		</s:iterator>
				                	<input type="hidden" name="grade" value="<s:property value="#st1.count"/>"/>
				                </td>
				                <td id="lel_<s:property value="#st1.count"/>">
				                	<span><s:property value="levelName"/></span>
				                	<input type="hidden" name="levelId" value="<s:property value="levelId"/>"/>
				                	<input type="hidden" name="levelName" value="<s:property value="levelName"/>"/>
				                	<input type="hidden" name="description" value="<s:property value="description"/>"/>
				                </td>
				                <td id="def_<s:property value="#st1.count"/>">
				                	<input type="radio" class="radio" name="defaultLevel" value="1" <s:if test="1 == defaultFlag || #st1.first">checked="checked"</s:if> id="default_<s:property value="#st1.count"/>"/>
				                	<label for="default_<s:property value="#st1.count"/>"><s:text name="lel01.defaultLevel" /></label>
				                </td>
				            	<s:if test='"0".equals(setPriorityFlag) || "".equals(setPriorityFlag)'>
				                <td class="center">
				                	<s:if test='!#st1.first'>
				            			<a href="#" onclick="BINOLMBLEL01.move(this,0);return false;" 
				            			class="left" title="<s:text name='lel01.first'/>"><span class="arrow-first"></span></a>
				            			<a href="#" onclick="BINOLMBLEL01.move(this,1);return false;" 
				            			class="left" title="<s:text name='lel01.up'/>"><span class="arrow-up"></span></a>
			            			</s:if>
			            			<s:else>
				            			<span class="left" style="height:16px; width:16px; display:block;"></span>
				            			<span class="left" style="height:16px; width:16px; display:block;"></span>
			            			</s:else>
			            			<s:if test='!#st1.last'>
				            			<a href="#" onclick="BINOLMBLEL01.move(this,2);return false;" 
				            			class="left" title="<s:text name='lel01.down'/>"><span class="arrow-down"></span></a>
				            			<a href="#" onclick="BINOLMBLEL01.move(this,3);return false;" 
				            			class="left" title="<s:text name='lel01.last'/>"><span class="arrow-last"></span></a>
			            			</s:if>
				                </td> 
				                </s:if>
				                <s:else>
				                	<td></td>
				                </s:else>
				             </tr>  
           					</s:iterator>  
			            </tbody>
		           	</table>
		           	<hr class="space" />
	           	</s:if>
         </div>
         <%-- 隐藏信息 --%>
         <div class="hide">
         	<input type="hidden" id="delJson" name="delJson" value="<s:property value="delJson"/>"/>
         	<input type="hidden" id="json" name="json" value="<s:property value="json"/>"/>
         </div>
         </cherry:form>
         <hr class="space" />
         <s:if test='"0".equals(changeFlag)'><p class="highlight"><s:text name="lel01.changeTip" /></p></s:if>
         <div class="center clearfix">
           	<button class="back" onclick="BINOLMBLEL01.back('${back_url}');return false;">
           		<span class="ui-icon icon-movel"></span>
           		<span class="button-text"><s:text name="global.page.previous"/></span>
           	</button>
           	<button class="save" onclick="BINOLMBLEL01.save('${save_url}');return false;">
           		<span class="ui-icon icon-save"></span>
           		<span class="button-text"><s:text name="global.page.save"/></span>
           	</button>
         </div>
      </div>
     </div>
</s:i18n>