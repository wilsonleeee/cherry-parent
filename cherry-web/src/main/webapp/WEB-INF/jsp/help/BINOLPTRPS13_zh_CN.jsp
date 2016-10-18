<%--产品销售记录查询帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">
               	<div class="text">
               		“产品销售记录查询”页面，是一个对销售单据查询的画面，根据查询条件可以在查询结果一览下面查询出单据信息。
               		   除了在查询条件处可以查询出符合要求的查询信息外，还可以设置商品，来查询出该商品的所有销售单据。
               	</div>
            </div>
        </div>	
        
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
                <div class="step-content">
                    <label>1</label>      
                    <div class="step">
                    	 选择产品/选择促销品（销售商品）：在销售商品区域，点击"选择产品"按钮，在弹出的产品信息窗口中，选择一种或者多种产品信息，
                    	 	点击“确定”后，产品信息显示在销售商品列表处。如果是选择多个商品，在销售商品之间要选择一种关系，如果多种产品的销售
                    	 	单据信息都需要查询出来，那么，就选择"AND"。如果多种产品只要能查询出其中一种销售单据就可以了，就选择“OR”。
                    	 	“选择促销品的”的操作同“选择产品”。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>      
                    <div class="step">                    	
                    		查询：在查询条件区域填写好相关的查询条件信息， 其中销售人员可以输入部分关键字实现模糊查询。
                    		点击“查询”按钮，即可以将符合查询条件的销售单据查询出来。
                    		在查询条件的基础上，如果还需要进一步筛选，那么在销售商品区域选择产品，即可查询出指定产品的
                    		且符合查询条件的销售单据信息。                    		
                    		在上面的基础上如果还需要精确的话，则可以选择具体的销售模式进行查询，点击“查询”按钮，就可以
                    		在查询结果一览下面显示查询结果信息了。                        	                         
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
                                                             需要注意的：销售商品模块区域选择的商品间的关系可以是“OR”或者是“AND”。连带商品模块区域选择的商品间的关系也可以是
                    OR”或者是“AND”。但是，销售商品与连带商品这两个模块间的关系，则必须是“AND”关系。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                                                            如果只想用于测试人员使用的话，勾选“公显示测试部门”复选框，即可以查询出测试数据供测试人员使用。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
        
