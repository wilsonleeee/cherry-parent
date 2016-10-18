<%--自由盘点帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		自由盘点就是对实体仓库的任何单个或多个物品进行清点统计的画面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>               
                    <div class="step">                       
                    	添加新行：首先选择好要盘点的那个实体仓库、逻辑仓库、 是否按批次盘点和填写好盘点理由。然后点击“添加新行”按钮，
               			在弹出的产品信息画面中选择一个或者多个产品好信息，然后填写盘点物品的详细信息：盘点数量与备注。系统将会自动计算出盘差值。
               			点击“反映到库存”按钮，页面出现当前已完成和当前所在的流程步骤。此盘点信息可在“单据处理>盘点单处理”查询到。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
      