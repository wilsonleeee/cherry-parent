/*
 * @(#)BINOLPLRLM05_Action.java     1.0 2010/10/27
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

package com.cherry.pl.rlm.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.pl.common.PrivilegeConstants;
import com.cherry.pl.rlm.bl.BINOLPLRLM05_BL;

/**
 * 删除角色Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLM05_Action extends BaseAction {
	
	private static final long serialVersionUID = 8094869974079476951L;

	/** 删除角色BL */
	@Resource
	private BINOLPLRLM05_BL binOLPLRLM05_BL;
	
	/**
	 * 删除角色处理
	 * 
	 * @return String 
	 */
	public String deleteRole() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 角色ID
		map.put(PrivilegeConstants.ROLE_ID, roleId);
		// 更新日时
		map.put(CherryConstants.MODIFY_TIME, modifyTime);
		// 更新次数
		map.put(CherryConstants.MODIFY_COUNT, modifyCount);
		try {
			// 删除角色
			binOLPLRLM05_BL.tran_deleteRole(map);
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
	
	/** 角色ID */
	private String roleId;
	
	/** 更新日时 */
	private String modifyTime;
	
	/** 更新次数 */
	private String modifyCount;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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
