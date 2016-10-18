/*	
 * @(#)BINOLPLGAD01_Form.java     1.0 @2012-12-5		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.pl.gad.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 *
 * 小工具管理Form
 *
 * @author jijw
 *
 * @version  2012-12-5
 */
public class BINOLPLGAD01_Form extends DataTable_BaseForm {

	/**小工具信息ID*/
    private String gadgetInfoId;
    
    /**所属画面ID*/
    private String pageId;

    /**小工具代码*/
    private String gadgetCode;
    
    /**小工具名称*/
    private String gadgetName;

    /**小工具配置地址*/
    private String gadgetConfPath;
    
    /**小工具参数*/
    private String gadgetParam;
    
    /**#######################  产品分类小工具 start ################################## **/
    
	/**小工具参数:产品分类-需要显示的分类*/
    private String[] categoryList;
    
	/**小工具参数:产品分类-分类属性显示数量*/
    private String cateMax;
    
    /**#######################  产品分类小工具  end  ################################## **/

    public String getGadgetInfoId() {
		return gadgetInfoId;
	}

	public void setGadgetInfoId(String gadgetInfoId) {
		this.gadgetInfoId = gadgetInfoId;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getGadgetCode() {
		return gadgetCode;
	}

	public void setGadgetCode(String gadgetCode) {
		this.gadgetCode = gadgetCode;
	}

	public String getGadgetName() {
		return gadgetName;
	}

	public void setGadgetName(String gadgetName) {
		this.gadgetName = gadgetName;
	}

	public String getGadgetConfPath() {
		return gadgetConfPath;
	}

	public void setGadgetConfPath(String gadgetConfPath) {
		this.gadgetConfPath = gadgetConfPath;
	}

	public String getGadgetParam() {
		return gadgetParam;
	}

	public void setGadgetParam(String gadgetParam) {
		this.gadgetParam = gadgetParam;
	}
	
    public String[] getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(String[] categoryList) {
		this.categoryList = categoryList;
	}

	public String getCateMax() {
		return cateMax;
	}

	public void setCateMax(String cateMax) {
		this.cateMax = cateMax;
	}
	
}
