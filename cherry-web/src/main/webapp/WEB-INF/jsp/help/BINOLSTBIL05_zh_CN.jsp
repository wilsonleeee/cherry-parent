<!-- 报损单处理模块帮助页面 -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">
               	<div class="text">
               		在“报损单处理”页面的查询条件区域，填写好所需的查询条件，
               		点击“查询”按钮，显示查询结果一览列表。点击报损单号，进入报损单的详细页面。
               		在报损单详细页面中，点击“导出”按钮，可将报损单的详细信息生成报表文件保存。
               		点击“单据流程”标签按钮，可查看该报损单的流程图。
               		（具体操作细节可以参考“发货单处理”）
               	</div>
            </div>
		</div>
		
		<div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>注意事项</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                                                             需要注意的：如果单据还在业务工作流当中，且操作用户有权限，可以对它进行下个步骤的。
               		处理。如果单据流程已经完成或者操作用户没有权限则只可以对入库单进行查看。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
		
		