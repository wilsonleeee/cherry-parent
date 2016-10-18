/*  
 * @(#)BINOLMOCIO01_Form.java     1.0 2011/06/09      
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
 * 柜台消息管理Form
 * 
 * @author niushunjie
 * @version 1.0 2011.06.09
 */
public class BINOLMOCIO21_Form extends DataTable_BaseForm {
    
    /** 品牌ID */
    private String brandInfoId;
    
    /** 订货消息ID */
    private String departmentMessageId;
    
    /** 消息标题 */
    private String messageTitle;
    
    /** 消息内容 */
    private String messageBody;
    
    /** 发布日期开始 */
    private String startDate;
    
    /** 发布日期截止 */
    private String endDate;
    
    /** 消息标题或内容 */
    private String messageTitleOrBody;
    
    /** 更新时间 */
    private String modifyTime;
    
    /** 更新次数 */
    private String modifyCount;
    
    /** 柜台消息ID组 */
    /*private String[] counterMessageArr;*/
    
    /** 柜台消息生效开始日期 */
    private String startValidDate;
    
    /** 柜台消息生效截止日期 */
    private String endValidDate;
    
    /** 柜台消息状态（0：停用，1：启用）*/
    private String status;
    
    /** 柜台选择模式(1：按区域，2：按渠道) */
	private String selMode;
	
	/** 是否加权限标志 0：不加权限查询 1：加权限查询 */
	private String privilegeFlag;
	
	/** 大区ID */
	private String channelRegionId;
	
	/**当前柜台消息ID*/
	private String currentMessageId;
	
	/**消息类型*/
	private String messageType;

	/**文件路径*/
	private String fileOrImagePath;
	
	/**文件名称*/
	private String fileOrImageName;

	/**编辑下的文件路径*/
	private String filePathEdit;
	
	/**编辑下的文件名称*/
	private String fileNameEdit;
	
	public String getFilePathEdit() {
		return filePathEdit;
	}

	public void setFilePathEdit(String filePathEdit) {
		this.filePathEdit = filePathEdit;
	}

	public String getFileNameEdit() {
		return fileNameEdit;
	}

	public void setFileNameEdit(String fileNameEdit) {
		this.fileNameEdit = fileNameEdit;
	}

	public String getFileOrImagePath() {
		return fileOrImagePath;
	}

	public void setFileOrImagePath(String fileOrImagePath) {
		this.fileOrImagePath = fileOrImagePath;
	}

	public String getFileOrImageName() {
		return fileOrImageName;
	}

	public void setFileOrImageName(String fileOrImageName) {
		this.fileOrImageName = fileOrImageName;
	}

	public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getDepartmentMessageId() {
		return departmentMessageId;
	}

	public void setDepartmentMessageId(String departmentMessageId) {
		this.departmentMessageId = departmentMessageId;
	}

	public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMessageTitleOrBody() {
        return messageTitleOrBody;
    }

    public void setMessageTitleOrBody(String messageTitleOrBody) {
        this.messageTitleOrBody = messageTitleOrBody;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyCount() {
        return modifyCount;
    }

    public void setModifyCount(String modifyCount) {
        this.modifyCount = modifyCount;
    }

	public String getStartValidDate() {
		return startValidDate;
	}

	public void setStartValidDate(String startValidDate) {
		this.startValidDate = startValidDate;
	}

	public String getEndValidDate() {
		return endValidDate;
	}

	public void setEndValidDate(String endValidDate) {
		this.endValidDate = endValidDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getCurrentMessageId() {
		return currentMessageId;
	}

	public void setCurrentMessageId(String currentMessageId) {
		this.currentMessageId = currentMessageId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

}
