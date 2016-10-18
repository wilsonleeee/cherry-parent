<%--发货单处理帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">
               	<div class="text">
               		“发货单处理”页面的订单来自“订货单处理”页面经过一审流程后的订单，和直接经过“发货”页面保存后而未提交的订单。
               		   根据需要填写和选择各项查询条件，点击“查询”按钮，页面将显示查询结果列表。通过审核状态和处理状态可以查看和
               		   处理不同时期的发货单据。
               	</div>      
            </div>
        </div>	

        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>注意事项</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                     	查询“未提交审核”状态的订单：选择查询条件区域的审核状态项“未提交审核”进行查询，在查询结果列表中点击“未提交审核”
                     	状态的订单号，进入该订单的详细页面。点击标签“单据流程”按钮，可查看该订单所完成的流程或当前的流程。此订单直接由
                     	“发货”生成且未提交审核，所以不显示流程图。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                     	导出/提交：在“未提交审核”状态的发货单详细页面中，点击“导出”按钮，可将该订单的详细信息生成报表文件保存下来。点击“提交”按钮，
                     	可将此订单提交到下一流程。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>3</label>
                    <div class="step">
                     	编辑（未提交审核）：在查询结果列表中点击“未提交审核”状态的订单号，进入发货单详细画面。点击“编辑”按钮，可对该订单的基本信息
                     	做再次的更改。点击“添加新行”可以再次选择产品，选择好产品后，勾选产品信息项点击“删除选中行”按钮，进行删除。可
                     	在产品的信息行里的文本框内修改和编辑产品信息。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>4</label>
                    <div class="step">
                     	保存/提交：编辑或修改完成后，点击“保存”按钮可将修改后的信息保存。点击“提交”按钮可将编辑好的内容保存并提交到
                     	下一个流程，提交后的订单审核状态将变为“审核中”。点击“返回”按钮将取消页面的保存并回退到修改前的页面。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>5</label>
                    <div class="step">
                     	查询“审核中”状态的订单：选择查询条件区域的审核状态项“审核中”进行查询，在查询结果列表中点击“审核中”状态的订单号，
                     	进入该订单的详细页面。点击“导出”按钮，可以将该页面的产品信息生成报表文件保存。点击“同意”按钮，可将该订单提交到下
                     	一个流程，提交后的审核状态变为“审核通过”。点击“拒绝”后，订单的审核状态将会变为“审核退回”。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>6</label>
                    <div class="step">
                     	编辑（审核中）：在查询结果列表中点击“审核中”状态的订单号，进入该订单的详细页面，点击“编辑”按钮，可再次更改订单的相关信息。
                     	在编辑页面内可修改订单相关信息，也可对产品信息进行修改。点击“添加新行”按钮，可添加需要的产品。勾选好需要删除的产
                     	品项，点击“删除选中行”按钮，可进行删除。编辑完成后，点击“同意”或者“拒绝”来改变订单的状态。
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>7</label>
                    <div class="step">
                     	查询“审核通过”的“未处理”状态的订单：
                     	选择查询条件区域的审核状态项“审核通过”，处理状态为“未处理”进行查询，在查询结果列表中点击“审核通过”状态的订单号，
                     	进入该订单的详细页面。点击“单据流程”按钮，可查看该订单当前所在的流程和已完成的流程，点击“修改执行者”按钮可以修改
                     	该流程的执行人，带*星为必填。点击“导出”按钮可将该订单信息生成报表文件保存。
                    </div>
                    <div class="line"></div>
                </div>
                
                  <div class="step-content">
                    <label>8</label>
                    <div class="step">
                     	发货：在“审核通过”的“未处理”状态的详细页面，点击“发货”按钮可将订单提交到下一流程，提交后，查询列表中的处理状态将会变为己方已发货。                   	
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>9</label>
                    <div class="step">
                     	“审核通过”的“对方已收货”状态的订单：
                     	选择查询条件区域的审核状态项“审核通过”进行查询，处理状态为“已收货”进行查询，点击“导出”按钮，
                     	可将该订单的详细信息生成报表文件保存。点击“单据流程”按钮，进入该单据的流程页面。单据流程页面
                     	可以查看该订单的当前流程和已完成的流程。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
