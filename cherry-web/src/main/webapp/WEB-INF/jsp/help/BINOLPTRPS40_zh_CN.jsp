<%--产品销售记录查询帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">
               	<div class="text">
               		“电商拦截订单一览”页面，是将订单明细中的商品没有在店务通系统中找到对应的产品的相关订单显示出来的画面。
               		一旦订单明细中的商品找到对应关系，即不会再在此画面显示。
               	</div>
            </div>
        </div>	
        
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>单据状态</strong></div>
            <div class="box-yew-content">
                <div class="step-content">
                    <label>1</label>      
                    <div class="step">
						已下单未付款：提交订单，未付款； --对应宏巍的[未付款订单]
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>      
                    <div class="step">                    	
						已付款未发货：已付款，未进入物流；--对应宏巍的[客审订单][财审订单][打印快递单][订单配货][缺货订单]
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>3</label>      
                    <div class="step">                    	
						已发货：进入物流。--对应宏巍的[订单出库][途中订单][订单结算]
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>4</label>      
                    <div class="step">                    	
						已完成：订单结束。--对应宏巍的[订单成功]
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>5</label>      
                    <div class="step">                    	
						取消订单：取消订单（包括退款完成）。--对应宏巍的[订单取消]
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>6</label>      
                    <div class="step">                    	
						注：已下单未付款状态，但是有单据付款时间。此订单为预售单，在店务通订单中会有注明。       
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
						单据状态为[取消订单]的订单数据，数量及金额系统会将其置为0。
                    </div>
                    <div class="line"></div>
           		</div>
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
					    其中若已付款(包括给预付款)或退款，且有产品对应关系的订单发送写入【产品销售记录查询】。
                    </div>
                    <div class="line"></div>
                </div>
      		</div>
     	</div>
        
