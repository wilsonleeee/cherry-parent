<%--入库模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		入库功能可以用来增减一个部门的产品库存。和发货不同，入库只会影响到入库部门的库存，不会影响其它部门的库存。
					如果是给非柜台以外的部门做入库，审核通过后，库存立即就会发生改变。如果是给柜台做入库，则审核通过后，
					还需要在柜台终端上做入库，柜台的库存才会改变。入库时也可以输入负数，这样会扣减库存。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>               
                    <div class="step">
                                                                              添加新行：首先，填写好概要区域内的相关信息， 点击“添加新行”按钮，弹出产品信息对话框，选择一条或者多条产品明细信息，
                                                                              也可以在输入框中输入部分关键字， 点击“查找”按钮查找产品信息，点击“确定”即可完成产品信息的添加。
                                                                              返回“入库”画面，在新增的信息处填写入库商品的 详细信息：批次号、数量和备注。 点击“确定”按钮，
                                                                              页面显示当前完成的流程步骤。此页面将提交到审核部门进行审核，也可以在“单据处理>入库单处理”页面中查询。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>

        
