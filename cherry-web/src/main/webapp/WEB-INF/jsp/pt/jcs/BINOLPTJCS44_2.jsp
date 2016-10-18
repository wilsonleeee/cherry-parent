<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/pt/jcs/BINOLPTJCS44_1.js?V=20160823"></script>
<s:i18n name="i18n.pt.BINOLPTJCS44">
<div id="searchPrintPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: block; width: auto; min-height: 300px;">
	<input type="hidden" id="rowNumber" value="0"/>
	<input type="hidden" id="rowCode" value="0"/>
	<input type="hidden" id="counterCode" name="counterCode" value="<s:property value='counterCode'/>"/>
    <div class="wpleft_header">
	    <div class="header_box">
	    	<s:text name="请扫描产品" />
			<input id="productSearchStr" name="productSearchStr" type="text" class="date" maxlength="30" style="width:180px;" value="" />
			<span class="right">
		    	<button id="printRecord" class="close" type="button" onclick="BINOLPTJCS44_1.printRecord();return false;">
		    		<span class="ui-icon icon-prints"></span>
					<span class="button-text"><s:text name="打印"/></span>
				</button>
				<button id="btnClose" class="close" type="button" onclick="BINOLPTJCS44_1.close();return false;">
		    		<span class="ui-icon icon-close"></span>
		            <span class="button-text"><s:text name="关闭"/></span>
				</button>
			</span>
	    </div>
    </div><br/>
    <div class="wp_tablebox">
      <table id="searchPrintTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
        <thead>
          <tr>
            <th width="5%"><s:text name="序号"/></th>
            <th width="15%"><s:text name="产品条码"/></th>
            <th width="15%"><s:text name="厂商编码"/></th>
            <th width="30%"><s:text name="产品名称"/></th>
            <th width="15%"><s:text name="会员价"/></th>
            <th width="15%"><s:text name="零售价"/></th>
            <th width="5%"><s:text name="操作"/></th>
          </tr>
        </thead>
        <tbody id="searchPrintbody">
        </tbody>
      </table>
    </div>
    <div class="hide" id="messageDialogTitle"><s:text name="消息"/></div>
	<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
		<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
		<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
		<p class="center">
			<button id="btnMessageConfirm" class="close" type="button">
	    		<span class="ui-icon icon-confirm"></span>
	            <span class="button-text"><s:text name="确定"/></span>
			</button>
		</p>
	</div>
</div>
</s:i18n>
