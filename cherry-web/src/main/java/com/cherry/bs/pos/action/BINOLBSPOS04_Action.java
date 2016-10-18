/*
 * @(#)BINOLBSPOS04_Action.java     1.0 2010/10/27
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.bl.BINOLBSPOS04_BL;
import com.cherry.bs.pos.form.BINOLBSPOS04_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 添加岗位画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS04_Action extends BaseAction implements ModelDriven<BINOLBSPOS04_Form> {

	
	private static final long serialVersionUID = 258867581582111559L;
	
	/** 添加岗位画面BL */
	@Resource
	private BINOLBSPOS04_BL binOLBSPOS04_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM00_BL binolcm00BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 添加岗位画面Form */
	private BINOLBSPOS04_Form form = new BINOLBSPOS04_Form();

	@Override
	public BINOLBSPOS04_Form getModel() {
		return form;
	}
	
	/**
	 * 
	 * 添加岗位画面初期处理
	 * 
	 * @return 添加岗位画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		} else {
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
			// 品牌名称
			brandMap.put("brandName", getText("PPL00006"));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
			map.put(CherryConstants.BRANDINFOID, CherryConstants.BRAND_INFO_ID_VALUE);
		}
		// 取得所属部门List
		orgList = binolcm00BL.getOrgList(map);
		if(orgList != null && !orgList.isEmpty()) {
			map.put(CherryConstants.ORGANIZATIONID, orgList.get(0).get("departId"));
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 取得上级岗位信息List
				higherPositionList = binOLBSPOS04_BL.getPositionByOrg(map);
			} else {
				map.remove(CherryConstants.BRANDINFOID);
				// 取得上级岗位信息List
				higherPositionList = binOLBSPOS04_BL.getPositionByOrg(map);
				map.put(CherryConstants.BRANDINFOID, CherryConstants.BRAND_INFO_ID_VALUE);
			}
		}
		// 取得岗位类别信息List
		positionCategoryList = binOLBSPOS04_BL.getPositionCategoryList(map);
		// 取得经销商List
		resellerInfoList = binolcm00BL.getResellerInfoList(map);
		
		return SUCCESS;
	}
	
	/**
	 * 根据品牌ID过滤下拉列表数据
	 * 
	 */
	public String filterByBrandInfo() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 取得部门List
		List<Map<String, Object>> list = binolcm00BL.getOrgList(map);
		resultMap.put("orgList", list);
		if(list != null && !list.isEmpty()) {
			map.remove(CherryConstants.BRANDINFOID);
			map.put(CherryConstants.ORGANIZATIONID, list.get(0).get("departId"));
			// 取得上级岗位信息List
			resultMap.put("higherPositionList", binOLBSPOS04_BL.getPositionByOrg(map));
		}
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 取得岗位类别信息List
		resultMap.put("positionCategoryList", binOLBSPOS04_BL.getPositionCategoryList(map));
		// 取得经销商List
		resultMap.put("resellerInfoList", binolcm00BL.getResellerInfoList(map));
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(JSONUtil.serialize(resultMap));
		return null;
	}
	
	/**
	 * 根据部门ID过滤下拉列表数据
	 * 
	 */
	public String filterByOrg() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属部门
		map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 取得上级岗位信息List
			resultMap.put("higherPositionList", binOLBSPOS04_BL.getPositionByOrg(map));
		} else {
			// 取得上级岗位信息List
			resultMap.put("higherPositionList", binOLBSPOS04_BL.getPositionByOrg(map));
		}
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(JSONUtil.serialize(resultMap));
		return null;
	}
	
	/**
	 * 
	 * 添加岗位处理
	 * 
	 * @return 添加完了画面
	 */
	public String addPosition() throws Exception {

		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		} else {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSPOS04");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSPOS04");
		// 添加岗位处理
		binOLBSPOS04_BL.tran_addPosition(map);
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 添加岗位前字段验证处理
	 * 
	 */
	public void validateAddPosition() throws Exception {
		
		// 部门ID为空时，不能创建岗位
		if(CherryChecker.isNullOrEmpty(form.getOrganizationId())) {
			this.addActionError(getText("EBS00008"));
		} else {
			// 上级岗位为空时
			if(CherryChecker.isNullOrEmpty(form.getPath())) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				int count = binOLBSPOS04_BL.getPositionIdByOrgInfo(map);
				if(count != 0) {
					this.addActionError(getText("EBS00009"));
				} else {
					List<String> list = binOLBSPOS04_BL.getOrgIdByOrgInfo(map);
					if(!list.contains(form.getOrganizationId())) {
						this.addActionError(getText("EBS00010"));
					}
				}
			}
		}
		
		// 岗位名称必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getPositionName())) {
			this.addFieldError("positionName", getText("ECM00009",new String[]{getText("PBS00015")}));
		} else {
			// 岗位名称不能超过50位验证
			if(form.getPositionName().length() > 50) {
				this.addFieldError("positionName", getText("ECM00020",new String[]{getText("PBS00015"),"50"}));
			}
		}
		// 岗位类别ID必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getPositionCategoryId())) {
			this.addFieldError("positionCategoryId", getText("ECM00009",new String[]{getText("PBS00039")}));
		}
		// 岗位外文名称不能超过50位验证
		if(form.getPositionNameForeign() != null && form.getPositionNameForeign().length() > 50) {
			this.addFieldError("positionNameForeign", getText("ECM00020",new String[]{getText("PBS00016"),"50"}));
		}
		// 岗位描述不能超过200位验证
		if(form.getPositionDESC() != null && form.getPositionDESC().length() > 200) {
			this.addFieldError("positionDESC", getText("ECM00020",new String[]{getText("PBS00017"),"200"}));
		}
		if(form.getFoundationDate() != null && !"".equals(form.getFoundationDate())) {
			// 成立日期日期格式验证
			if(!CherryChecker.checkDate(form.getFoundationDate())) {
				this.addFieldError("foundationDate", getText("ECM00022",new String[]{getText("PBS00008")}));
			}
		}
	}
	
	/** 上级岗位信息List */
	List<Map<String, Object>> higherPositionList;
	
	/** 经销商List */
	private List<Map<String, Object>> resellerInfoList;
	
	/** 部门List */
	private List<Map<String, Object>> orgList;
	
	/** 岗位类别信息List */
	private List<Map<String, Object>> positionCategoryList;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getHigherPositionList() {
		return higherPositionList;
	}

	public void setHigherPositionList(List<Map<String, Object>> higherPositionList) {
		this.higherPositionList = higherPositionList;
	}

	public List<Map<String, Object>> getResellerInfoList() {
		return resellerInfoList;
	}

	public void setResellerInfoList(List<Map<String, Object>> resellerInfoList) {
		this.resellerInfoList = resellerInfoList;
	}

	public List<Map<String, Object>> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<Map<String, Object>> orgList) {
		this.orgList = orgList;
	}

	public List<Map<String, Object>> getPositionCategoryList() {
		return positionCategoryList;
	}

	public void setPositionCategoryList(
			List<Map<String, Object>> positionCategoryList) {
		this.positionCategoryList = positionCategoryList;
	}

}
