/*
 * @(#)BINOLBSREG03_Action.java     1.0 2011/11/23
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.reg.form.BINOLBSREG03_Form;
import com.cherry.bs.reg.interfaces.BINOLBSREG02_IF;
import com.cherry.bs.reg.interfaces.BINOLBSREG03_IF;
import com.cherry.bs.reg.service.BINOLBSREG04_Service;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 区域更新画面Action
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
@SuppressWarnings("unchecked")
public class BINOLBSREG03_Action extends BaseAction implements ModelDriven<BINOLBSREG03_Form> {
	
	private static final long serialVersionUID = -1366306369473677603L;

	/** 区域更新画面BL */
	@Resource
	private BINOLBSREG03_IF binOLBSREG03_BL;
	
	/** 区域详细画面BL */
	@Resource
	private BINOLBSREG02_IF binOLBSREG02_BL;
	
	/** 区域添加画面Service */
	@Resource
	private BINOLBSREG04_Service binOLBSREG04_Service;
	
	private List provinceList;
	public List getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List provinceList) {
		this.provinceList = provinceList;
	}


	/**
	 * 
	 * 区域更新画面初期处理
	 * 
	 * @return 区域更新画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		map.put("regionId", form.getRegionId());
		// 取得区域详细信息
		regionInfo = binOLBSREG02_BL.getRegionInfo(map);
		provinceList=binOLBSREG02_BL.getProvinceList(map);
		return SUCCESS;
	}
	
	/**
	 * 
	 * 更新区域处理
	 * 
	 */
	public String update() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 剔除map中的空值
		map = CherryUtil.removeEmptyVal(map);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSREG03");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSREG03");
		if(form.getIgnoreFlg() == null || "0".equals(form.getIgnoreFlg())) {
			// 根据区域名称取得类似该区域名称的其他区域信息
			Map<String, Object> likeRegionInfo = binOLBSREG04_Service.getLikeRegionInfo(map);
			if(likeRegionInfo != null) {
				String regionNameChinese = (String)likeRegionInfo.get("regionNameChinese");
				this.addActionMessage(getText("EBS00050", new String[]{regionNameChinese}));
				return "confirm";
			}
		}
		try {
			// 更新区域处理
			binOLBSREG03_BL.tran_updateRegion(map);
		} catch (Exception e) {
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());       
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }else{
                throw e;
            }    
		}
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 更新区域前字段验证处理
	 * 
	 */
	public void validateUpdate() throws Exception {
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
		// 区域类型必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getRegionType())) {
			this.addFieldError("regionType", getText("ECM00009",new String[]{getText("PBS00063")}));
		}
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
		if(!this.hasFieldErrors() && !form.getOldRegionCode().equals(form.getRegionCode())) {
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
	
	/** 区域详细信息 */
	private Map regionInfo;
	
	public Map getRegionInfo() {
		return regionInfo;
	}

	public void setRegionInfo(Map regionInfo) {
		this.regionInfo = regionInfo;
	}

	/** 区域更新画面Form */
	private BINOLBSREG03_Form form = new BINOLBSREG03_Form();

	@Override
	public BINOLBSREG03_Form getModel() {
		return form;
	}

}
