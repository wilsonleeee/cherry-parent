<%--发货单处理帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		发货单处理是对发货页面处理结果的一种显示，也可以对货物的不同状态进行审核，
               		这里的发货单处理专对促销品。“发货单处理”页面可查询到所有状态的发货单。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                                                                 未提交审批的发货单：在“发货单处理”页面中的查询条件区域，选择审核状态为“未提交审核”，点击“查询”按钮，显示查询一览列表，
				                 点击“发货单处理”页面上的“未提交审核”状态的发货单，打开该发货单的详细页面。“未提交审核”状态的发货单可以继续修改发货单的内容。				                
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                                                                 保存修改/发货： 在“未提交审核”的详细发货单画面， 修改好相关的内容，点击“保存修改”按钮可保存修改后的内容，不提交流程。点击“发货”按钮，
                                                                 即可将发货单提交到发货流程，并且审核状态为“审核通过”。点击“删除”按钮，可将发货单删除。        
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>3</label>
                    <div class="step">
                                                                 审核通过的发货单：在“发货单处理”页面中的查询条件区域，选择审核状态为“审核通过”，点击“查询”按钮，显示查询一览列表，
				                 点击任意一个“发货单号”则打开相应的发货单的详细页面。“审核通过”状态的发货单，只能查看详细内容和单据流程图。           
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>