<%--销售目标设定模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		销售目标设定模块显示了不同类型的销售目标信息，类型有区域、柜台、美容顾问、修改类型,
               		下面会展示出对应的类型结果。其中目标年月需要注意一下输写格式。
               	</div>
            </div>
        </div>	
        
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                  添加销售目标：点击“添加销售目标”在弹出的添加销售目标对话框中，输入目标年月,数量与金额指标,设定的类型有区域，
                                                                  柜台或者美容顾问，根据需要修改，点击“确定”即可完成修改。
                    </div>
                    <div class="line"></div>
                </div>  
                
                <div class="step-content">
                    <label>2</label>             
                    <div class="step">
                                                                  下发到终端：点击“下发到终端”按钮，弹出销售目标下发对话框，如果想下发所有未下发的销售目标到终端就点击“确定”，即可
                                                                  完成将要下发的数据下发到POS机。  没有下发到终端的销售目标在下发状态里面会有一个未下发的灰色图标作为区分。
                    </div>
                    <div class="line"></div>
                    
                    <label>3</label>             
                    <div class="step">
                                                                  编辑：查询结果一览下的每条销售目标都有一个编辑按钮，点击“编辑”按钮在弹出的编辑对话框中，可以对金额指标和数量指标进入修改，
                                                                  修改后点击“确定”即可完成信息的编辑。
                    </div>
                    <div class="line"></div>
                </div>  
               </div>
              </div>           
        
       