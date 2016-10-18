/*
 * @(#)BINOLBSPOS02_Action.java     1.0 2010/10/27
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

package com.cherry.bs.pos.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.bl.BINOLBSPOS02_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;

/**
 * 岗位详细画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS02_Action extends BaseAction {
	
	private static final long serialVersionUID = -2914442522220984976L;
	
	/** 岗位详细画面BL */
	@Resource
	private BINOLBSPOS02_BL binOLBSPOS02_BL;
	
	/**
	 * 岗位详细画面初期处理
	 * 
	 * @return 岗位详细画面 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(modeFlg != null && !"".equals(modeFlg)) {
			String[] positionIds = positionId.split("_");
			if(positionIds.length > 1) {
				if("1".equals(positionIds[positionIds.length-1])) {
					map.put("employeeId", positionIds[0]);
					employeeInfo = binOLBSPOS02_BL.getEmployeeInfo(map);
					return "success_employee";
				} else {
					map.put("counterInfoId", positionIds[0]);
					counterInfo = binOLBSPOS02_BL.getCounterInfo(map);
					return "success_counter";
				}
			} else {
				// 岗位ID
				map.put(CherryConstants.POSITIONID, positionId);
				// 查询岗位信息
				positionInfo = binOLBSPOS02_BL.getPositionInfo(map);
				
				return "success_tree";
			}
		} else {
			// 岗位ID
			map.put(CherryConstants.POSITIONID, positionId);
			// 查询岗位信息
			positionInfo = binOLBSPOS02_BL.getPositionInfo(map);
			
			return SUCCESS;
		}
	}
	
	/** 岗位ID */
	private String positionId;
	
	/** 岗位信息 */
	private Map positionInfo;
	
	/** 员工信息 */
	private Map employeeInfo;
	
	/** 柜台信息 */
	private Map counterInfo;
	
	/** 列表和树模式迁移判断flg */
	private String modeFlg;

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public Map getPositionInfo() {
		return positionInfo;
	}

	public void setPositionInfo(Map positionInfo) {
		this.positionInfo = positionInfo;
	}

	public String getModeFlg() {
		return modeFlg;
	}

	public void setModeFlg(String modeFlg) {
		this.modeFlg = modeFlg;
	}

	public Map getEmployeeInfo() {
		return employeeInfo;
	}

	public void setEmployeeInfo(Map employeeInfo) {
		this.employeeInfo = employeeInfo;
	}

	public Map getCounterInfo() {
		return counterInfo;
	}

	public void setCounterInfo(Map counterInfo) {
		this.counterInfo = counterInfo;
	}

}
