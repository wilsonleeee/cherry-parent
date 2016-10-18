<%--发货帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		这里的发货只是针对促销品的发货，发货就是从一个部门向另外一个部门发货。               		
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                                                                 添加新行：选择“发货部门”、“发货仓库”、“逻辑仓库”、“收货部门”、“发货理由”相关项。
                                                                 点击“添加新行”按钮，弹出促销产品信息对话框，在这里可以选择多个产品信息，选择后，点击“确定”
                                                                 即可完成产品的增加。当当前库存小于出库数量的时候会弹出“发货数量大于库存数量的提示框”，
                                                                 可以单击“继续”可以继续发货，但是一般情况下出库数量要<=当前库存。                                                                
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                                                              发货/暂存：添加新行后，可以在“出库数量”和“备注”里面填写数据，然后点击“暂存”，即可将发货信息保存起来，
                                                              点击“发货”将会在审核部门通过审核，并且产生发货单据。
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                               复制发货单：点击“复制发货单”按钮，弹出“发货单信息”窗口，根据需要选择发货单，然后点击“确定”按钮，
                                                               即可返回到“发货”页面，并且复制单据将显示在列表下。
                                                               即可返回到“发货”页面，此发货单为已存在的订单，所以这里面无需再填写发货数量了，填写好“备注”信息。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>