/*
 * @(#)BINOLMOMAN02_Form.java     1.0 2011/4/2
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
package com.cherry.mo.man.form;

import com.cherry.cm.form.DataTable_BaseForm;
/**
 * 
 * 添加机器Form
 * 
 * @author niushunjie
 * @version 1.0 2011.4.2
 */
public class BINOLMOMAN02_Form extends DataTable_BaseForm{
    /** 品牌ID */
    private String brandInfoId;

    /** 机器编号 */
    private String[] machineCodeArr;
    
    /** 手机卡号 */
    private String[] phoneCodeArr;
    
    /** 机器类型*/
    private String[] machineTypeArr;
    
    /**导入区分*/
    private String[] importFlagArr;
    
    /** 备注 */
    private String[] commentArr;

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String[] getMachineCodeArr() {
        return machineCodeArr;
    }

    public void setMachineCodeArr(String[] machineCodeArr) {
        this.machineCodeArr = machineCodeArr;
    }

    public String[] getPhoneCodeArr() {
        return phoneCodeArr;
    }

    public void setPhoneCodeArr(String[] phoneCodeArr) {
        this.phoneCodeArr = phoneCodeArr;
    }

    public String[] getMachineTypeArr() {
        return machineTypeArr;
    }

    public void setMachineTypeArr(String[] machineTypeArr) {
        this.machineTypeArr = machineTypeArr;
    }

    public String[] getCommentArr() {
        return commentArr;
    }

    public void setCommentArr(String[] commentArr) {
        this.commentArr = commentArr;
    }

    public void clear(){
        this.brandInfoId = null;
        this.machineCodeArr = null;
        this.phoneCodeArr = null;
        this.machineTypeArr = null;
        this.commentArr = null;
    }

	public String[] getImportFlagArr() {
		return importFlagArr;
	}

	public void setImportFlagArr(String[] importFlagArr) {
		this.importFlagArr = importFlagArr;
	}
    
}
