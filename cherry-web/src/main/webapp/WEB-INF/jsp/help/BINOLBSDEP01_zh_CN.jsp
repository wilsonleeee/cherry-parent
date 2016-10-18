<%--部门管理模块页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		部门管理模块是专对部门信息的管理的一个画面，可以进行增，删，改，停用与启用。
               		其中部门的显示模式分为树模式和列表模式，根据需要选择一种模式都可以对部门信息进行操作。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
                <div class="step-content">
                	<label>1</label>
                	<div class="step">
                	编辑:点击左边的任意一个部门，在右边会显示出部门的具体信息，点击“编辑”按钮，进入编辑画面，
                                                                       注意最高级别的部门不能进行编辑，只能查看该部门的信息。
                	</div>
                    <div class="line"></div>
            	</div>            	
            	<div class="step-content">
                	<label>2</label>
                	<div class="step">
                	上级部门：在部门编辑画面中，点击“上级部门”按钮，弹出部门信息的窗口， 在部门信息的窗口选择上级部门，
                		        然后点击“确定”按钮，只能添加一个上级部门。添加的部门可以在编辑页面上，点击“删除”按钮，进行删除。
                	</div>
                    <div class="line"></div>
            	</div>  
            	
            	<div class="step-content">
                	<label>3</label>
                	<div class="step">
                	添加下级柜台：在部门编辑画面中，点击“添加下级柜台”按钮，弹出柜台信息窗口，在柜台信息窗口选择下级柜台，可以选择多个柜台。
                			    然后点击“确定”按钮。  添加的柜台信息可以在编辑页面上，点击“删除”按钮，进行删除。
                	</div>
                    <div class="line"></div>
            	</div>  
            	
            	<div class="step-content">
                	<label>4</label>
                	<div class="step">
                	添加地址：在部门编辑画面中，点击“添加地址”按钮，显示新地址的编辑信息， 填写相关的地址信息，其中“地址一”为必填项。点击“删除”按钮，
                                                                                可删除该地址。
                	</div>
                    <div class="line"></div>
            	</div>  
            	
            	<div class="step-content">
                	<label>5</label>
                	<div class="step">
                	添加部门联系人：编辑在部门编辑页面点击“添加部门联系人”按钮，  显示部门联系人的相关信息，选择联系人，
                                                                                            最后点击“保存”按钮，将编辑的内容保存起来。
                	</div>
                    <div class="line"></div>
            	</div>  
            	          	
                <div class="step-content">
                    <label>6</label>
                    <div class="step">
                                                               创建部门：点击“创建部门”按钮，进入创建部门画面，其中部门代码，测试区分，部门类型，部门名称为必填项。
                                                               绑定仓库有两种选择，一种是选择已有仓库，还有一种是创建默认仓库，选择其中一种。点击“上级部门”
                                                               进入部门信息画面，单选上一个部门就可以了。点击“添加地址”按钮，其中“地址1”的内容为必填项。点击
                     “删除”按钮可以将不需要的信息添加项目删除。点击“保存”即可以创建部门。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>7</label>
                    <div class="step">
                                                                 定位:在输入框中输入部分关键字，点击“定位”按钮将查询出符合条件的部门信息，并以树形结构显示。也可以展开前面的加号查询。
                                                                 选中的一个部门，在右边窗口即将出现部门的具体画面。
                                                                
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>8</label>               
                    <div class="step">
                                                               查询（列表模式）：查询按钮用于查询出符合条件的部门信息，并显示在查询结果一览的下面。查询信息可以根据查询条件自由设置，默认为查询所有启用部门信息。
                                                               也可以自己勾选“包含停用部门”复选框，将启用与停用的符合条件的部门信息全部查询出来。点击部门代码编号，还可以查看部门明细信息。                        
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>9</label>               
                    <div class="step">
                                                               停用/启用（列表模式）:选择需要启用或者停用的部门，在弹出的确认框，点击“确定”按钮，即可完成部门的启用或者停用。                     
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
