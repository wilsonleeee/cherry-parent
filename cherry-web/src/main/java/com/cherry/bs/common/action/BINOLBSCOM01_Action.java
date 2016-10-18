/*		
 * @(#)BINOLBSCOM01_Action.java     1.0 2010/10/27		
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

package com.cherry.bs.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.common.bl.BINOLBSCOM01_BL;
import com.cherry.bs.common.form.BINOLBSCOM01_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 基础模块POPUP画面共通Action
 * 
 * @author WangCT
 *
 */
public class BINOLBSCOM01_Action extends BaseAction implements ModelDriven<BINOLBSCOM01_Form> {

	private static final long serialVersionUID = -4233143506247881645L;
	
	/** 基础模块POPUP画面共通Form */
	private BINOLBSCOM01_Form form = new BINOLBSCOM01_Form();
	
	/** 基础模块POPUP画面共通BL */
	@Resource
	private BINOLBSCOM01_BL binOLBSCOM01_BL;
	
	@Resource
	private CodeTable code;
	
	/**
	 * 用户一览画面生成处理
	 * 
	 * @return 用户一览画面
	 */
	public String popEmployee() {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户登录时
		if (form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId()) 
				&& CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(form.getBrandInfoId())) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("employeeKw", form.getSSearch());
		}
		// 员工节点
		if(form.getEmpPath() != null && !"".equals(form.getEmpPath())) {
			map.put("empPath", form.getEmpPath());
		}
		// 员工ID
		if(form.getEmployeeId() != null && !"".equals(form.getEmployeeId())) {
			map.put("curEmployeeId", form.getEmployeeId());
		}
		// 岗位类别存在的场合
		if(form.getPositionCategoryId() != null && !"".equals(form.getPositionCategoryId())) {
			map.put("positionCategoryId", form.getPositionCategoryId());
			if(form.getFromPage() != null) {
				map.put("likeEmpGrade", binOLBSCOM01_BL.getPosCategoryGrade(map));
			} else {
				map.put("higherGrade", binOLBSCOM01_BL.getPosCategoryGrade(map));
			}
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put("userId", String.valueOf(userInfo.getBIN_UserID()));
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 取得用户总数
		int count = binOLBSCOM01_BL.getEmployeeCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得用户List
			form.setEmployeeList(binOLBSCOM01_BL.getEmployeeList(map));
		}
		if(form.getFromPage() != null) {
			return "likeEmployee";
		}
		return SUCCESS;
	}
	
	/**
	 * 部门一览画面生成处理
	 * 
	 * @return 部门一览画面
	 */
	@SuppressWarnings("unchecked")
	public String popDepart() {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户登录时
		if (form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId()) 
				&& CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(form.getBrandInfoId())) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("departKw", form.getSSearch());
		}
		boolean flag = "1".equals(form.getGradeFlag())? false:true;
		// 部门类别存在的场合
		if(form.getType() != null && !"".equals(form.getType())) {
			String grade = code.propGrade("1000", form.getType());
			// 部门类型List
			List typeObjList = code.getCodesByGrade("1000");
			if(typeObjList != null && !typeObjList.isEmpty()) {
				List<String> typeList = new ArrayList<String>();
				for(int i = 0; i < typeObjList.size(); i++) {
					String type = (String)((Map)typeObjList.get(i)).get("CodeKey");
					int gradeTemp = (Integer)((Map)typeObjList.get(i)).get("Grade");
					if(flag){
						if(Integer.parseInt(grade) >= gradeTemp) {
							typeList.add(type);
						}
					}else{
						if(Integer.parseInt(grade) <= gradeTemp) {
							typeList.add(type);
						}
					}
				}
				if(typeList != null && !typeList.isEmpty()) {
					map.put("type", typeList);
				}
			}
		}
		// 部门节点存在的场合
		if(form.getDepartPath() != null && !"".equals(form.getDepartPath())) {
			map.put("departPath", form.getDepartPath());
		}
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得部门总数
		int count = binOLBSCOM01_BL.getDepartCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得部门List
			form.setDepartList(binOLBSCOM01_BL.getDepartList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 用户一览画面生成处理（柜台管理用）
	 * 
	 * @return 用户一览画面
	 */
	public String popCounterEmp() {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("employeeKw", form.getSSearch());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put("userId", String.valueOf(userInfo.getBIN_UserID()));
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 取得用户总数
		int count = binOLBSCOM01_BL.getEmployeeCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得用户List
			form.setEmployeeList(binOLBSCOM01_BL.getEmployeeList(map));
		}
		if(form.getFromPage() != null) {
			return "likeEmployee";
		}
		return SUCCESS;
	}
	
	/**
	 * 上级部门一览画面生成处理（柜台管理用）
	 * 
	 * @return 部门一览画面
	 */
	public String popHigherOrg() {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("departKw", form.getSSearch());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 取得部门总数
		int count = binOLBSCOM01_BL.getHigherOrgCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得部门List
			form.setDepartList(binOLBSCOM01_BL.getHigherOrgList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 下级柜台一览画面生成处理（部门管理用）
	 * 
	 * @return 柜台一览画面
	 */
	public String popLowerCounter() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户登录时
		if (form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId()) 
				&& CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(form.getBrandInfoId())) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("departKw", form.getSSearch());
		}
		// 部门节点
		if(form.getDepartPath() != null && !"".equals(form.getDepartPath())) {
			map.put("departPath", form.getDepartPath());
		}
		map.put("type", new String[]{"4"});
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 取得部门总数
		int count = binOLBSCOM01_BL.getDepartCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得部门List
			form.setDepartList(binOLBSCOM01_BL.getDepartList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 部门联系人一览画面生成处理（部门管理用）
	 * 
	 * @return 部门联系人一览画面
	 * @throws Exception 
	 */
	public String popDepartEmp() throws Exception {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("employeeKw", form.getSSearch());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 取得部门联系人总数
		int count = binOLBSCOM01_BL.getDepartEmpCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得部门联系人List
			form.setEmployeeList(binOLBSCOM01_BL.getDepartEmpList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 上级部门取得（部门管理用）
	 * 
	 * @return 上级部门一览画面
	 */
	@SuppressWarnings("unchecked")
	public String popHigherDepart() {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户登录时
		if (form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId()) 
				&& CherryConstants.BRAND_INFO_ID_VALUE != Integer.parseInt(form.getBrandInfoId())) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("departKw", form.getSSearch());
		}
		// 部门类别存在的场合
		if(form.getType() != null && !"".equals(form.getType())) {
			String grade = code.propGrade("1000", form.getType());
			// 部门类型List
			List typeObjList = code.getCodesByGrade("1000");
			if(typeObjList != null && !typeObjList.isEmpty()) {
				List<String> typeList = new ArrayList<String>();
				for(int i = 0; i < typeObjList.size(); i++) {
					String type = (String)((Map)typeObjList.get(i)).get("CodeKey");
					int gradeTemp = (Integer)((Map)typeObjList.get(i)).get("Grade");
					if(Integer.parseInt(grade) >= gradeTemp) {
						typeList.add(type);
					}
				}
				if(typeList != null && !typeList.isEmpty()) {
					map.put("type", typeList);
				}
			}
		}
		// 部门节点存在的场合
		if(form.getDepartPath() != null && !"".equals(form.getDepartPath())) {
			map.put("departPath", form.getDepartPath());
		}
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "0");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得部门总数
		int count = binOLBSCOM01_BL.getDepartCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得部门List
			form.setDepartList(binOLBSCOM01_BL.getDepartList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 取得上级区域信息
	 * 
	 * @return 上级区域信息
	 */
	@SuppressWarnings("unchecked")
	public String popRegion() {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("regionKw", form.getSSearch());
		}
		// 区域类别存在的场合
		if(form.getRegionType() != null && !"".equals(form.getRegionType())) {
			String grade = code.propGrade("1151", form.getRegionType());
			// 区域类型List
			List typeObjList = code.getCodesByGrade("1151");
			if(typeObjList != null && !typeObjList.isEmpty()) {
				List<String> typeList = new ArrayList<String>();
				for(int i = 0; i < typeObjList.size(); i++) {
					String type = (String)((Map)typeObjList.get(i)).get("CodeKey");
					int gradeTemp = (Integer)((Map)typeObjList.get(i)).get("Grade");
					if(Integer.parseInt(grade) > gradeTemp) {
						typeList.add(type);
					}
				}
				if(typeList != null && !typeList.isEmpty()) {
					map.put("regionType", typeList);
				}
			}
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得区域总数
		int count = binOLBSCOM01_BL.getRegionCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得区域List
			form.setRegionList(binOLBSCOM01_BL.getRegionList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 取得省份或直辖市信息
	 * 
	 * @return 省份或直辖市
	 */
	@SuppressWarnings("unchecked")
	public String popProvince() {	
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("regionKw", form.getSSearch());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 取得省份或直辖市总数
		int count = binOLBSCOM01_BL.getProvinceCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得省份或直辖市List
			form.setRegionList(binOLBSCOM01_BL.getProvinceList(map));
		}
		return SUCCESS;
	}
	@Override
	public BINOLBSCOM01_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
