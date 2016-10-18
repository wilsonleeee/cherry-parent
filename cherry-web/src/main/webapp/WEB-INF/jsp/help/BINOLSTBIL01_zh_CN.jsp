<%--入库单处理帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">
               	<div class="text">
               		入库单处理页面的入库单信息来自“仓库业务”里“发货”提交后的入库单。
               		其审核状态有未提交审核，审核中，审核通过，审核退回。同发货单处理一样，
               		如果单据还在业务工作流当中，且操作用户有权限，可以对它进行下个步骤的
               		处理。如果单据流程已经完成或者操作用户没有权限则只可以对入库单进行查看。
               		（具体操作细节可以参考“发货单处理”）
               	</div>
            </div>
        </div>	
        
        
