<%--发货模板帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">               		             		
               		发货是两个部门间的库存转移，发货方会扣减库存，收货方会增加库存。如果在做发货业务时，不慎输多了数量，
				       还可以使用负数的发货单进行对冲。对于负数的发货单，发货方会增加库存，收货方会扣减库存。               		
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>               
                    <div class="step">
					   	        添加新行：首先，填写好概要区域内的相关信息，点击“添加新行”按钮，弹出产品信息对话框，选择一条或者多条产品明细信息，
					   	        也可以在输入框中输入部分关键字，点击“查找”按钮 查找产品信息，点击“确定”即可完成产品信息的添加。返回“发货”画面。
                    </div>
                    <div class="line"></div>
                </div> 
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
					   	        暂存/提交：添加新行，返回到“发货”页面后， 在新增的信息处填写发货物品的出库数量和备注。点击“保存”按钮，
					   	        即可完成发货单据的保存，并且在“单据处理>发货单处理”页面中查询出来。点击“提交”按钮，页面显示当前完成的流程步骤。
					   	        此页面将提交到审核部门进行审核，也可以在“单据处理>发货单处理”页面中查询。
					   	         列表右下方有总数量和总金额的统计表。                                                                                 
                    </div>
                    <div class="line"></div>
                </div>   
                
                <div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                              生成空白单：击此按钮可以将所有的产品信息全部显示出来，当需要添加全部产品信息或者大多数的产品信息时，
                                                              可以点击“生成空白单”，输入出库数量和备注，点击“保存”或“提交”即可（操作步骤同“添加新行”）。              
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>                     
      
