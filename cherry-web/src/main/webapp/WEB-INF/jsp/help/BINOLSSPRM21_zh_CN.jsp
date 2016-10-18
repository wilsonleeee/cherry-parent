<%--商品盘点帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">               		
               		商品盘点，这里是针对所有促销品的库存和帐面进行盘点，将盘点信息存入库存中。 所有的盘点操作后点击“反映到库存”按钮，
                                                             都将生成盘点单，可在“各类报表”目录下的页面查询相应的盘点单。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">                                                    
                                                                开始盘点：根据盘点需求，选择相关的盘点条件，点击“开始盘点”按钮。点击“开始盘点”按钮后，显示所有相关盘点商品，
                                                                可对每项商品进行盘点。盘点完成后，点击页面下方“反映到库存”按钮。 点击“取消”按钮，将取消查询列表的显示，
                                                                返回到查询界面。默认的会将所有启用的促销品信息盘点出来，如果想盘点停用的促销品选择“包括停用促销品”复选框即可。      
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
       
