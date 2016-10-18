/*
 * @(#)BINOLBSDEP02_Action.java     1.0 2010/10/27
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

package com.cherry.bs.dep.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cnt.bl.BINOLBSCNT02_BL;
import com.cherry.bs.dep.bl.BINOLBSDEP02_BL;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;

/**
 * 部门详细画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP02_Action extends BaseAction {

	private static final long serialVersionUID = -1997738681683301646L;
	
	/** 部门详细画面BL */
	@Resource
	private BINOLBSDEP02_BL binOLBSDEP02_BL;
	
	/** 柜台详细画面BL */
	@Resource
	private BINOLBSCNT02_BL binOLBSCNT02_BL;

	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** 是否支持部门协同维护*/
	private boolean maintainOrgSynergy;

	/** 是否支持专柜协同维护*/
	private boolean maintainCoutSynergy;

	public boolean isMaintainOrgSynergy() {
		return maintainOrgSynergy;
	}

	public void setMaintainOrgSynergy(boolean maintainOrgSynergy) {
		this.maintainOrgSynergy = maintainOrgSynergy;
	}
	
	
	public boolean isMaintainCoutSynergy() {
		return maintainCoutSynergy;
	}

	public void setMaintainCoutSynergy(boolean maintainCoutSynergy) {
		this.maintainCoutSynergy = maintainCoutSynergy;
	}

	/**
	 * 部门详细画面初期处理
	 * 
	 * @return 部门详细画面 
	 * @throws Exception 
	 */
	public String init() throws Exception {
		
		if(organizationId != null && !"".equals(organizationId)) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 语言
			String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
			if(language != null) {
				map.put(CherryConstants.SESSION_LANGUAGE, language);
			}
			// 部门ID
			map.put(CherryConstants.ORGANIZATIONID, organizationId);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			//品牌Code
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			String departType = binOLBSDEP02_BL.getDepartType(map);
			if(departType != null && "4".equals(departType)) {
				// 查询柜台信息
				counterInfo = binOLBSCNT02_BL.getCounterInfo(map);

				// 是否支持部门协同
				this.maintainCoutSynergy = binOLCM14_BL.isConfigOpen("1050", userInfo.getBIN_OrganizationInfoID(), (Integer)counterInfo.get(CherryConstants.BRANDINFOID));
			} else {
				// 查询部门信息
				organizationInfo = binOLBSDEP02_BL.getOrganizationInfo(map);
				// 查询部门地址List
				departAddressList = binOLBSDEP02_BL.getDepartAddressList(map);
				// 取得部门联系人List
				departContactList = binOLBSDEP02_BL.getDepartContactList(map);
				// 取得所属部门的员工
				employeeInDepartList = binOLBSDEP02_BL.getEmployeeInDepartList(map);
				// 取得管辖或者关注指定部门的人的信息
				employeeList = binOLBSDEP02_BL.getEmployeeList(map);

				// 是否支持部门协同
				this.maintainOrgSynergy = binOLCM14_BL.isConfigOpen("1371", userInfo.getBIN_OrganizationInfoID(), (Integer)organizationInfo.get(CherryConstants.BRANDINFOID));
			}
		}
		
		if(modeFlg != null && !"".equals(modeFlg)) {
			return "success_tree";
		}
		
		return SUCCESS;
	}
	
	/** 部门ID */
	private String organizationId;
	
	/** 部门信息 */
	private Map organizationInfo;
	
	/** 柜台信息 */
	private Map counterInfo;
	
	/** 部门地址List */
	private List<Map<String, Object>> departAddressList;
	
	/** 部门联系人List */
	private List<Map<String, Object>> departContactList;
	
	/** 所属部门的员工List */
	private List<Map<String, Object>> employeeInDepartList;
	
	/** 管辖或者关注指定部门的人的信息 */
	private List<Map<String, Object>> employeeList;
	
	/** 列表和树模式迁移判断flg */
	private String modeFlg;

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public Map getOrganizationInfo() {
		return organizationInfo;
	}

	public void setOrganizationInfo(Map organizationInfo) {
		this.organizationInfo = organizationInfo;
	}

	public List<Map<String, Object>> getDepartAddressList() {
		return departAddressList;
	}

	public void setDepartAddressList(List<Map<String, Object>> departAddressList) {
		this.departAddressList = departAddressList;
	}

	public List<Map<String, Object>> getDepartContactList() {
		return departContactList;
	}

	public void setDepartContactList(List<Map<String, Object>> departContactList) {
		this.departContactList = departContactList;
	}

	public String getModeFlg() {
		return modeFlg;
	}

	public void setModeFlg(String modeFlg) {
		this.modeFlg = modeFlg;
	}

	public Map getCounterInfo() {
		return counterInfo;
	}

	public void setCounterInfo(Map counterInfo) {
		this.counterInfo = counterInfo;
	}

	public List<Map<String, Object>> getEmployeeInDepartList() {
		return employeeInDepartList;
	}

	public void setEmployeeInDepartList(
			List<Map<String, Object>> employeeInDepartList) {
		this.employeeInDepartList = employeeInDepartList;
	}

	public List<Map<String, Object>> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Map<String, Object>> employeeList) {
		this.employeeList = employeeList;
	}

}
