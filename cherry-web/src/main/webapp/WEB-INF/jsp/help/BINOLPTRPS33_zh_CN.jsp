<%--产品销售记录查询帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">
               	<div class="text">
               		“电商订单一览”页面，是显示通过接口等方式从第三方获取订单数据的画面。
               		此画面可以查询到单据状态条件中列出的各个状态时的订单数据。
               	</div>
            </div>
        </div>	
        
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>单据状态</strong></div>
            <div class="box-yew-content">
                <div class="step-content">
                    <label>1</label>      
                    <div class="step">
						已下单未付款：提交订单，未付款；
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>      
                    <div class="step">                    	
						已付款未发货：已付款，未进入物流；
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>3</label>      
                    <div class="step">                    	
						已发货：进入物流。
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>4</label>      
                    <div class="step">                    	
						已完成：订单结束。
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>5</label>      
                    <div class="step">                    	
						取消订单：取消订单（包括退款完成）。
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
						单据状态为[取消订单]的订单数据，数量及金额、运费等系统会将其置为0。
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

        
