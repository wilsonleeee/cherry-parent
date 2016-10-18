/*
 * @(#)BINOLPLPLT04_Action.java     1.0 2010/10/27
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

package com.cherry.pl.plt.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.pl.plt.bl.BINOLPLPLT04_BL;

/**
 * 删除权限类型Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLPLT04_Action extends BaseAction {
	
	private static final long serialVersionUID = -1406948425225151579L;
	
	/** 删除权限类型BL */
	@Resource
	private BINOLPLPLT04_BL binOLPLPLT04_BL;
	
	/**
	 * 删除权限类型处理
	 * 
	 * @return 删除完了画面 
	 */
	public String deletePlt() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 权限类型记录ID
		map.put("privilegeTypeId", privilegeTypeId);
		// 更新日时
		map.put(CherryConstants.MODIFY_TIME, modifyTime);
		// 更新次数
		map.put(CherryConstants.MODIFY_COUNT, modifyCount);
		try {
			// 删除权限类型
			binOLPLPLT04_BL.tran_deletePlt(map);
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
	
	/** 权限类型记录ID */
	private String privilegeTypeId;
	
	/** 更新日时 */
	private String modifyTime;
	
	/** 更新次数 */
	private String modifyCount;

	public String getPrivilegeTypeId() {
		return privilegeTypeId;
	}

	public void setPrivilegeTypeId(String privilegeTypeId) {
		this.privilegeTypeId = privilegeTypeId;
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
