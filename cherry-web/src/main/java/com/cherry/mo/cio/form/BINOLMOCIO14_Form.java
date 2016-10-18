/*  
 * @(#)BINOLMOCIO14_Form.java     1.0 2011/06/14      
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
package com.cherry.mo.cio.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 柜台消息发布Form
 * 
 * @author niushunjie
 * @version 1.0 2011.06.14
 */
public class BINOLMOCIO14_Form extends DataTable_BaseForm {
    /** 品牌ID */
    private String brandInfoId;
    
    /** 柜台消息ID */
    private String counterMessageId;

    /** 柜台消息ID */
    private String regionType;
    
    /** 禁止接受消息的区域柜台节点 */
    private String forbiddenNodesArray;
    
    /** 允许接受消息的区域柜台节点 */
    private String allowNodesArray;
    
    /** 选择控制标记 */
    private String radioControlFlag;
    
    /** 柜台选择模式(1：按区域，2：按渠道) */
	private String selMode;
	
	/** 是否加权限标志 0：不加权限查询 1：加权限查询 */
	private String privilegeFlag;
	
	/** 大区ID */
	private String channelRegionId;

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getCounterMessageId() {
        return counterMessageId;
    }

    public void setCounterMessageId(String counterMessageId) {
        this.counterMessageId = counterMessageId;
    }

    public String getRegionType() {
        return regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }

    public String getForbiddenNodesArray() {
        return forbiddenNodesArray;
    }

    public void setForbiddenNodesArray(String forbiddenNodesArray) {
        this.forbiddenNodesArray = forbiddenNodesArray;
    }

    public String getAllowNodesArray() {
        return allowNodesArray;
    }

    public void setAllowNodesArray(String allowNodesArray) {
        this.allowNodesArray = allowNodesArray;
    }

    public void setRadioControlFlag(String radioControlFlag) {
        this.radioControlFlag = radioControlFlag;
    }

    public String getRadioControlFlag() {
        return radioControlFlag;
    }

	public String getSelMode() {
		return selMode;
	}

	public void setSelMode(String selMode) {
		this.selMode = selMode;
	}

	public String getPrivilegeFlag() {
		return privilegeFlag;
	}

	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

	public String getChannelRegionId() {
		return channelRegionId;
	}

	public void setChannelRegionId(String channelRegionId) {
		this.channelRegionId = channelRegionId;
	}

}
