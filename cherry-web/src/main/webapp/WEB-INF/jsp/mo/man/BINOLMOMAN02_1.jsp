<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mo.BINOLMOMAN02">
    <div class="toolbar clearfix">
        <strong>
            <span class="left">
                <span class="ui-icon icon-ttl-section-info"></span>
                <s:text name="MAN02_base_title"/>
            </span>
        </strong>
    </div>
    <table id="machineDetail" class="detail" cellpadding="0" cellspacing="0">
        <tr>
            <th>
                <%-- 类型 --%>
                <s:text name="MAN02_machineType"/>
                <span class="highlight"><s:text name="global.page.required"/></span>
            </th>
            <td>
                <span>
                   <s:text name="MAN02_select" id="MAN02_select"/>
                   <s:select name="machineType" 
                      list='#application.CodeTable.getCodesWithFilter("1101","MP")' 
                      listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{MAN02_select}" cssStyle="width:150px;"/>
                </span>
            </td>
            <th></th>
            <td></td>
        </tr>
        <tr>
            <th>
                <%-- 机器编号 --%>
                <s:text name="MAN02_machineCode"/>
                <span class="highlight"><s:text name="global.page.required"/></span>
            </th>
            <td>
                <span>
                    <s:textfield name="machineCode" cssClass="text" maxlength="15" onblur="BINOLMOMAN02.checkDuplicate();"/>
                </span>
            </td>
            <th>
                <%-- 手机卡号 --%>
                <s:text name="MAN02_phoneCode"/>
            </th>
            <td>
                <span><s:textfield name="phoneCode" maxlength="11" cssClass="text"/></span>
            </td>
        </tr>

        <tr>
            <th>
                <%-- 备注 --%>
                <s:text name="MAN02_comment"/>
            </th>
            <td colspan="3">
                <span><s:textfield name="comment" cssClass="text" cssStyle="width:550px" maxlength="200"/></span>
            </td>
        </tr>
    </table>
    <div style="border-bottom:1px #ccc dashed;padding-bottom:10px;" class="center">
    	<a id="add" class="add" onclick="BINOLMOMAN02.addMachine(this);return false;">
		<span class="ui-icon icon-enable"></span>
		<span class="button-text"><s:text name="MAN02_confirmAdd"/></span>
		</a>
	</div>
    <hr class="space" />
    <div class="toolbar clearfix">
        <strong>
            <span class="left">
                <span class="ui-icon icon-ttl-section-info"></span>
                <s:text name="MAN02_importExcel_title"/>
            </span>
        </strong>
    </div>
    <div>
        <span class="highlight"><s:text name="MAN02_snow"/></span>
        <s:text name="MAN02_notice"/>
        <a href="${downloadUrl}"><s:text name="MAN02_download"/></a>
    </div>
    <div>
        <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
                <td>
                	<input type="hidden" value="" name="csrftoken" id="csrftoken">
                     <span style="margin-left:10px; display: inline;" class="left hide"> 
					    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
					    <input type="button" value="<s:text name="global.page.browse"/>"/>
					    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="BINOLMOMAN02.deleteActionMsg();pathExcel.value=this.value;return false;" /> 
					    <input type="button" value="<s:text name="MAN02_importExcel_btn"/>" onclick="BINOLMOMAN02.ajaxFileUpload()" id="upload" /> 
					    <img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display: none;">
					</span>
                    
                </td>
            </tr>
        </table>
    </div>
    <hr class="space" />
    <div id ="machineDiv" style= "width:666px;overflow-x:auto;overflow-y:hidden;">

    </div>
    <div id="mydetail" class="hide">
        <div id="tabledefault">
        <table id="machineInfoArr" class="editable"> 
            <tbody>
                <tr><th><s:text name="MAN02_no"/></th>
                    <th><s:text name="MAN02_machineCode"/></th>
                    <th style="width:100px"><s:text name="MAN02_phoneCode"/></th>
                    <th><s:text name="MAN02_machineType"/></th>
                    <th style="min-width:100px"><s:text name="MAN02_comment"/></th>
                    <th><s:text name="MAN02_operation"/></th>
                </tr>
            </tbody>
        </table>
        </div>
    </div>
    <hr class="space" />
    <div class="center">
        <button class="save" type="button" onclick="BINOLMOMAN02.save();return false;">
            <span class="ui-icon icon-save"></span>
            <span class="button-text"><s:text name="global.page.save"></s:text>
            </span>
        </button>
    </div>
 </s:i18n>
<div class="hide">
<div id="delMachine">
    <a class="delete" href="#" onclick="BINOLMOMAN02.delDiv(this); return false;">
        <span class="ui-icon icon-delete"></span>
        <span class="button-text"><s:text name="global.page.delete"/></span>
    </a>
 </div>
 </div>