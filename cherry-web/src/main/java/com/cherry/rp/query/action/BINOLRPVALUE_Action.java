/*	
 * @(#)BINOLRPVALUE_Action.java     1.0 2010/11/08		
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

package com.cherry.rp.query.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM13_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

/**
 * BI报表查询条件的表示值共通Action
 * @author WangCT
 *
 */
public class BINOLRPVALUE_Action extends BaseAction {

	private static final long serialVersionUID = 6499632379036333873L;
	
	/** 组织结构共通BL */
	@Resource
	private BINOLCM13_BL binOLCM13_BL;
	
	/**
	 * AJAX 取得部门List
	 * 
	 * @throws Exception
	 */
	public void queryDepart() throws Exception {
		
		if(departId != null && !"".equals(departId)) {
			Map<String, Object> map = getMap();
			// BI报表业务类型
			map.put("businessType", businessType);
			// 操作类型为1：查询
			map.put("operationType", 1);
			// 取得柜台主管的场合
			if("-1".equals(type)) {
				// 部门ID
				map.put("departId", departId);
				// 取得柜台主管List
//				List<Map<String, Object>> employeeList = binOLCM13_BL.getEmployeeList(map);
				// 响应JSON对象
//				ConvertUtil.setResponseByAjax(response, employeeList);
			} 
			// 取得柜台的场合
			else if("4".equals(type)) {
				// 员工ID
				map.put("employeeId", departId);
				// 取得部门ID
//				String depId = binOLCM13_BL.getDepartId(map);
				// 部门ID
//				map.put("departId", depId);
				// 取得子部门List
//				List<Map<String, Object>> departList = binOLCM13_BL.getCounterList(map);
				// 响应JSON对象
//				ConvertUtil.setResponseByAjax(response, departList);
			} else {
				// 部门ID
				map.put("departId", departId);
				// 取得子部门List
				List<Map<String, Object>> departList = binOLCM13_BL.getDepartList(map);
				// 响应JSON对象
				ConvertUtil.setResponseByAjax(response, departList);
			}
		}
	}
	
	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// userId
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 组织Id
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌Id
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());

		return map;
	}
	
	/** 部门ID */
	private String departId;
	
	/** 部门类型 */
	private String type;
	
	/** BI报表业务类型 */
	private String businessType;

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}
