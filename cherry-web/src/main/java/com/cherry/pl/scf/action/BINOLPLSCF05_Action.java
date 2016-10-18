/*
 * @(#)BINOLPLSCF05_Action.java     1.0 2010/10/27
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

package com.cherry.pl.scf.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.pl.scf.bl.BINOLPLSCF05_BL;

/**
 * 删除审核审批配置信息Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF05_Action extends BaseAction {
	
	private static final long serialVersionUID = 3245127712845256336L;
	
	/** 删除审核审批配置信息BL */
	@Resource
	private BINOLPLSCF05_BL binOLPLSCF05_BL;
	
	/**
	 * 删除审核审批配置信息处理
	 * 
	 * @return 删除完了画面 
	 */
	public String deleteAudit() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 审核审批配置信息ID
		map.put("auditPrivilegeId", auditPrivilegeId);
		// 更新日时
		map.put(CherryConstants.MODIFY_TIME, modifyTime);
		// 更新次数
		map.put(CherryConstants.MODIFY_COUNT, modifyCount);
		try {
			// 删除审核审批配置信息
			binOLPLSCF05_BL.tran_deleteAudti(map);
		} catch (Exception e) {
			// 删除失败的场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                throw e;
            }    
		}
		
		return SUCCESS;
	}
	
	/** 审核审批配置信息ID */
	private String auditPrivilegeId;
	
	/** 更新日时 */
	private String modifyTime;
	
	/** 更新次数 */
	private String modifyCount;

	public String getAuditPrivilegeId() {
		return auditPrivilegeId;
	}

	public void setAuditPrivilegeId(String auditPrivilegeId) {
		this.auditPrivilegeId = auditPrivilegeId;
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

}
