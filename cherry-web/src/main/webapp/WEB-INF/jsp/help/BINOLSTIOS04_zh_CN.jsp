<%--商品盘点帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		商品盘点就是对实体仓库所有的物品进行盘点。首先选择好所在的部门，此为必选项，还可以继续根据分类，
               		选择要盘点物品，然后点击“开始盘点”按钮，出现排查后的所有要盘点物品列表。
               		（相对商品盘点，可以直接按分类盘点所有该分类的商品，而无需要进入产品信息里面一个个地选择。）
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>               
                    <div class="step">
                                                                  开始盘点/取消：首先，填写好概要区域内的相关信息，点击“开始盘点”按钮 ，盘点的商品会根据盘点条件在下表中显示出来。
                                                                  如果要重新选择要盘点的物品，点击“取消”按钮， 即可将现有列表清空，再次根据需要选择要盘点的 物品，点击“盘点”按钮即可进行盘点。
                      <div class="line"></div>
                    </div>
                </div>
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                                  反映到库存：点击“盘点”按钮后，在“详细”列表下填写盘点物品的详细信息：盘点数量与备注，
                                                                  系统将会自动计算出盘差值与盘差金额。点击“反映库存”按钮后，出现提交成功的提示，
                                                                  并显示当前所在的和已完成的流程步骤。此次商品盘点操作将生成单据，将在“单据处理>盘点单处理”查询到。
                      <div class="line"></div>
                    </div>
                </div>                 
            </div>
        </div>
       
        
