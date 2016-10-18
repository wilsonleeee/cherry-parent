<%--调入申请扩展帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		“调入申请”就是部门向部门申请促销品的调入。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                                                                添加新行：在“调入申请”页面，选择“调入部门”、“调入仓库”、“调出部门”和“调入逻辑仓库”选项。
                                                                然后在明细区域点击“添加新行”按钮， 弹出“促销产品信息”窗口，选择一个或多个产品信息，点击“确定”
                                                                返回到调入申请画面，申请调入的促销品将显示在明细列表内， 可以编辑调入促销品的数量和备注，
                                                                勾选某些促销品点击“删除选中行”按钮进行删除。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">                        
                                                               保存/申请： 在添加新行后，回到“调入申请”画面，点击“保存”按钮可保存已填写的信息不提交申请， 该调入申请将成生调拨单，在“调拨单处理”页面可查询到。点击
                     “申请”按钮，将提交到下一流程， 该调拨单将在“调拨单处理”页面中查询到。
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
                                                             需要注意的：只有同一上级部门的下级部门之间才能进行调入申请。柜台之间不能进行调入申请。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
       
