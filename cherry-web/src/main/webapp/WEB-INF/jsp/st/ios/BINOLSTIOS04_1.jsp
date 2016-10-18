<%-- 商品盘点DataTable --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherry.js"></script>
<script type="text/javascript">
       if(refreshSessionTimerID == null){
	       	refreshSessionTimerID = setInterval("refreshCherrySession($('#s_refreshAjax').html())", 30*60*1000);
       }
</script>
<s:i18n name="i18n.st.BINOLSTIOS04">
<div id="hidden1Result1Div1" style="display: none"></div>
    <div class="section-header">
        <strong>
            <span class="ui-icon icon-ttl-section-list"></span>
            <s:text name="IOS04_detail"/>
        </strong>
    </div>
    <div class="section-content">
        <div class="toolbar clearfix"></div>      
        <div style="width:100%;overflow-x:scroll;">
            <input type="hidden" id="rowNumber" value="1"/>
            <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
                <thead>
                    <tr>
                        <th class="tableheader" width="5%"><s:text name="IOS04_num"/></th>
                        <th class="tableheader" width="5%"><s:text name="IOS04_code"/></th>
                        <th class="tableheader" width="5%"><s:text name="IOS04_barcode"/></th>
                        <th class="tableheader" width="5%"><s:text name="IOS04_name"/></th>
                        <th class="tableheader" width="5%"><s:text name="IOS04_price"/></th>
                        <th class="tableheader <s:if test='!"true".equals(isBatchStockTaking)'>hide</s:if>" width="5%"><s:text name="IOS04_batchNo"/></th>
                        <th class="tableheader <s:if test='!"0".equals(blindFlag)'>hide</s:if>" width="5%"><s:text name="IOS04_bookCount"/></th>
                        <th class="tableheader" width="5%"><s:text name="IOS04_stockTakingQuantity"/></th>
                        <th class="tableheader <s:if test='!"0".equals(blindFlag)'>hide</s:if>" width="5%"><s:text name="IOS04_GainQuantity"/></th>
                        <th class="tableheader <s:if test='!"0".equals(blindFlag)'>hide</s:if>" width="5%"><s:text name="IOS04_GainAmount"/></th>
                        <th class="tableheader" width="5%"><s:text name="IOS04_remark"/></th>
                </thead>
                <tbody id="databody">
                    <s:iterator value="productList" id="productMap" status="rn">
                        <tr id='dataRow<s:property value="#rn.index+1"/>' >
                            <td class="center" id="dataTd0"><s:property value="#rn.index+1"/></td>
                            <td id="dataTd1" style="white-space:nowrap"><s:property value='UnitCode' /></td>
                            <td id="dataTd2"><s:property value='BarCode' /></td>
                            <td id="dataTd3"><s:property value='prodcutName' /></td>
                            <td id="dataTd4"><s:text name="format.price"><s:param value="SalePrice"></s:param></s:text></td>
                            <td id="dataTd5" class="<s:if test='!"true".equals(isBatchStockTaking)'>hide</s:if>"><span><input type="text" id="batchNoArr<s:property value="#rn.index+1"/>" name="batchNoArr" class="text-number" onchange="BINOLSTIOS04.checkBatchNo(this);" ondragstart="return false;" ondragover="return false;" ondragend="return false;" ondrop="return false;"></input></span></td>
                            <td id="dataTd6" class="<s:if test='!"0".equals(blindFlag)'>hide</s:if>" style="text-align: right;"><s:property value='Quantity' /></td>
                            <td id="dataTd7"><input type="text" id="quantityArr<s:property value="#rn.index+1"/>" name="quantityArr" class="text-number" maxlength="9" size="10" onchange="BINOLSTIOS04.changeCount(this);" ondragstart="return false;" ondragover="return false;" ondragend="return false;" ondrop="return false;"></input></td>
                            <td id="gainCount<s:property value="#rn.index+1"/>" style="text-align: right;" class="<s:if test='!"0".equals(blindFlag)'>hide</s:if>"></td>
                            <td id="dataTd9" style="text-align: right;" class="<s:if test='!"0".equals(blindFlag)'>hide</s:if>"></td>
                            <td id="dataTd10"><input type="text" name="reasonArr" maxlength="200"></input></td>
                            <td style="display:none" id="dataTd11">
                                <input type="hidden" id="bookCountArr<s:property value="#rn.index+1"/>" name="bookCountArr" value="<s:property value='Quantity'/>"/>
                                <input type="hidden" id="gainCountArr<s:property value="#rn.index+1"/>" name="gainCountArr" />
                                <input type="hidden" id="productVendorIDArr<s:property value="#rn.index+1"/>" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
                                <input type="hidden" id="priceArr<s:property value="#rn.index+1"/>" name="priceArr" value="<s:property value='SalePrice'/>"/>
                            </td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>
        </div>
        <hr class="space" />
        <div class="center clearfix">
        	<cherry:show domId="BINOLSTIOS0402">
        	<button class="save" type="button" onclick="BINOLSTIOS04.save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.saveTemp"/></span></button>
            </cherry:show>
            <cherry:show domId="BINOLSTIOS0401">
            <button class="confirm" type="button" onclick="BINOLSTIOS04.confirm();"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="IOS04_save"/></span></button>
            </cherry:show>
        </div>  
    </div>
</s:i18n>