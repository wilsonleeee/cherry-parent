<%--订货单处理帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		订货单处理的所有订单来自于终端的订货信息。这里可以查询所有状态的订单，其中产品名称可以进行模糊查询。根据需要，               		
               		填写查询条件区域内的查询条件，可以选择详细的查询条件进行排查，然后点击“查询”按钮，显示查询结果列表，
               		勾选需要打印的单据，点击“批量打印”按钮，可以进行打印。
               	</div>
            </div>
        </div>
        
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>               
                    <div class="step">
                                                             订货单详细：在查询结果列表，点击任意一个"订货单号"可以查看该单据的详细信息。点击“查看订货方库存”按钮可以查看该订单的库存详细信息，
                                                             点击“单据流程”可以查看该业务的单据流程图和已完成的处理步骤。 除此外，还可以点击“修改执行者”按钮，修改该单据下
                                                             一个步骤的参与者以及身份类型。                            
                    </div>
                    <div class="line"></div>
                </div>
                </div>
               </div>
        
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>注意事项</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                     	如果单据还在工作流当中，且操作用户有权限，还可以对它进行处理。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
        
