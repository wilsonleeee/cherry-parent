<%--调拨单处理帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		调拨单处理模块是对调拨信息的一个查询画面。点击一个调拨单号可以进入调拨单详细画面。可以查看它的基本信息和明细信息。
               		还可以查看它的单据流程及流程详情。点击修改执行者按钮进入修改参与者，可以修改调出的参与者身份类型和参与者。
               	</div>
            </div>
        </div>	
        
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                    	“未提交审核”状态的调拨单：在“调拨单处理”页面选择审核状态为“未提交审核”，点击“查询”按钮，
                    	查询结果将显示在“查询结果一览”列表中。点击“调拨单号”，打开该调拨单的详细页面，可以查看调拨单据在详细信息，
                    	点击该调拨单详细页面上的“单据流程”按钮，可查看该调拨单的流程图。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                    	添加新行/删除选中行：在打开的未提交审核的调拨单详细画面，点击“添加新行”、“删除选中行”按钮，可以重新编辑调拨产品信息。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>3</label>
                    <div class="step">
                    	保存/申请/删除：在打开的“未提交审核”的调拨单详细画面，点击“保存”按钮，保存修改的内容，不提交申请。点击“申请”按钮，
                    	可提交该调拨单到下一流程。点击“删除”按钮，可删除该订单。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>4</label>
                    <div class="step">
                    	“审核中”状态的调拨单：在“调拨单处理”页面选择审核状态为“审核中”，点击“查询”按钮，
                    	在“调拨单处理”页面中，点击查询结果一览中“审核中”状态的调拨单号，打开该调拨单的详细页面。
                    	点击“单据流程”标签项，可查看该调拨单的流程图。点击“修改执行者”按钮，可以修改参与者。
                    	修改完成后点击“确定”按钮，提交修改的内容。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>5</label>
                    <div class="step">
                    	同意/拒绝：在打开的“审核中”状态的调拨单详细画面，点击“同意”按钮，可将调拨单提交到下一个流程，
                    	审核状态将更改为“审核通过”。点击“拒绝”按钮，可将调拨单退回，审核状态将更改为“审核退回”。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>6</label>
                    <div class="step">
                    	“审核通过”状态的调拨单：在“调拨单处理”页面中，点击查询结果一览中“审核通过”状态的调拨单号，打开该调拨单的详细页面。
                    	  点击“单据流程”标签按钮，可以查看该调拨单的流程图。点击“修改执行者”按钮，确定调拨单的调出，并将调拨单提交到下一流程。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
     