/*
 * @(#)BINOLBSREG04_Action.java     1.0 2011/11/23
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
package com.cherry.bs.reg.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.reg.form.BINOLBSREG04_Form;
import com.cherry.bs.reg.interfaces.BINOLBSREG04_IF;
import com.cherry.bs.reg.service.BINOLBSREG04_Service;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 区域添加画面Action
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
@SuppressWarnings("unchecked")
public class BINOLBSREG04_Action extends BaseAction implements ModelDriven<BINOLBSREG04_Form> {

	private static final long serialVersionUID = 2148547499194389572L;
	
	/** 区域添加画面BL */
	@Resource
	private BINOLBSREG04_IF binOLBSREG04_BL;
	
	/** 区域添加画面Service */
	@Resource
	private BINOLBSREG04_Service binOLBSREG04_Service;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private CodeTable code;
	
	/**
	 * 
	 * 区域添加画面初期处理
	 * 
	 * @return 区域添加画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 总部用户的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 添加区域处理
	 * 
	 */
	public String save() throws Exception {
		
		// form参数放入Map中
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSREG04");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSREG04");
		//区域类型
		map.put("regionType", "0");
		if(form.getIgnoreFlg() == null || "0".equals(form.getIgnoreFlg())) {
			// 根据区域名称取得类似该区域名称的其他区域信息
			Map<String, Object> likeRegionInfo = binOLBSREG04_Service.getLikeRegionInfo(map);
			if(likeRegionInfo != null) {
				String regionNameChinese = (String)likeRegionInfo.get("regionNameChinese");
				this.addActionMessage(getText("EBS00050", new String[]{regionNameChinese}));
				return "confirm";
			}
		}
		// 添加区域处理
		binOLBSREG04_BL.tran_addRegion(map);
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 添加区域前字段验证处理
	 * 
	 */
	public void validateSave() throws Exception {
		// 区域代码必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getRegionCode())) {
			this.addFieldError("regionCode", getText("ECM00009",new String[]{getText("PBS00064")}));
		} else {
			// 不能超过10位验证
			if(form.getRegionCode().length() > 10) {
				this.addFieldError("regionCode", getText("ECM00020",new String[]{getText("PBS00064"),"10"}));
			} 
			// 英文数字验证
			else if(!CherryChecker.isAlphanumeric(form.getRegionCode())) {
				this.addFieldError("regionCode", getText("ECM00031",new String[]{getText("PBS00064")}));
			}
		}
		// 区域中文名称必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getRegionNameChinese())) {
			this.addFieldError("regionNameChinese", getText("ECM00009",new String[]{getText("PBS00062")}));
		} else {
			// 区域中文名称不能超过50位验证
			if(form.getRegionNameChinese().length() > 50) {
				this.addFieldError("regionNameChinese", getText("ECM00020",new String[]{getText("PBS00062"),"50"}));
			}
		}
		// 品牌必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getBrandInfoId())) {
			this.addFieldError("brandInfoId", getText("ECM00009",new String[]{getText("PBS00048")}));
		}
//		// 区域类型必须入力验证
//		if(CherryChecker.isNullOrEmpty(form.getRegionType())) {
//			this.addFieldError("regionType", getText("ECM00009",new String[]{getText("PBS00063")}));
//		}
		// 区域英文名称不能超过50位验证
		if(form.getRegionNameForeign() != null && form.getRegionNameForeign().length() > 50) {
			this.addFieldError("regionNameForeign", getText("ECM00020",new String[]{getText("PBS00065"),"50"}));
		}
		// 助记码不为空的场合
		if(form.getHelpCode() != null && !"".equals(form.getHelpCode())) {
			// 不能超过10位验证
			if(form.getHelpCode().length() > 10) {
				this.addFieldError("helpCode", getText("ECM00020",new String[]{getText("PBS00066"),"10"}));
			} 
			// 英文数字验证
			else if(!CherryChecker.isAlphanumeric(form.getHelpCode())) {
				this.addFieldError("helpCode", getText("ECM00031",new String[]{getText("PBS00066")}));
			}
		}
		// 邮编不为空的场合
		if(form.getZipCode() != null && !"".equals(form.getZipCode())) {
			// 不能超过10位验证
			if(form.getZipCode().length() > 10) {
				this.addFieldError("zipCode", getText("ECM00020",new String[]{getText("PBS00011"),"10"}));
			} 
			// 英文数字验证
			else if(!CherryChecker.isAlphanumeric(form.getZipCode())) {
				this.addFieldError("zipCode", getText("ECM00031",new String[]{getText("PBS00011")}));
			}
			
		}
		// 电话区号不为空的场合
		if(form.getTeleCode() != null && !"".equals(form.getTeleCode())) {
			// 不能超过5位验证
			if(form.getTeleCode().length() > 5) {
				this.addFieldError("teleCode", getText("ECM00020",new String[]{getText("PBS00067"),"5"}));
			} 
			// 数字验证
			else if(!CherryChecker.isNumeric(form.getTeleCode())) {
				this.addFieldError("teleCode", getText("ECM00021",new String[]{getText("PBS00067")}));
			}
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			// 区域代码
			map.put("regionCode", form.getRegionCode());
			int count = binOLBSREG04_Service.getRegionCodeCount(map);
			if(count > 0) {
				this.addFieldError("regionCode", getText("EBS00051"));
			}
		}
	}
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 上级区域信息 */
	private Map higherRegionInfo;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public Map getHigherRegionInfo() {
		return higherRegionInfo;
	}

	public void setHigherRegionInfo(Map higherRegionInfo) {
		this.higherRegionInfo = higherRegionInfo;
	}

	/** 区域添加画面Form */
	private BINOLBSREG04_Form form = new BINOLBSREG04_Form();
	
	@Override
	public BINOLBSREG04_Form getModel() {
		return form;
	}

}
