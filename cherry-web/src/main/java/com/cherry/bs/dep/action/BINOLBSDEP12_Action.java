/*
 * @(#)BINOLBSDEP12_Action.java     1.0 2011.2.10
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
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.bl.BINOLBSDEP04_BL;
import com.cherry.bs.dep.bl.BINOLBSDEP11_BL;
import com.cherry.bs.dep.bl.BINOLBSDEP12_BL;
import com.cherry.bs.dep.form.BINOLBSDEP12_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 品牌编辑画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP12_Action extends BaseAction implements ModelDriven<BINOLBSDEP12_Form> {

	private static final long serialVersionUID = 1651382353725331399L;
	
	/** 品牌编辑画面Form */
	private BINOLBSDEP12_Form form = new BINOLBSDEP12_Form();
	
	/** 品牌编辑画面BL */
	@Resource
	private BINOLBSDEP12_BL binOLBSDEP12_BL;
	
	/** 品牌添加画面BL */
	@Resource
	private BINOLBSDEP11_BL binOLBSDEP11_BL;
	
	/** 添加部门画面BL */
	@Resource
	private BINOLBSDEP04_BL binOLBSDEP04_BL;
	
	/**
	 * 
	 * 品牌编辑画面初期处理
	 * 
	 * @return 品牌编辑画面 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		brandInfo = binOLBSDEP12_BL.getBrandInfo(map);
		if(brandInfo == null || brandInfo.isEmpty()) {
			this.addActionError(getText("EBS00016"));
		}
		return SUCCESS;
		
	}
	
	/**
	 * 更新品牌处理
	 * 
	 * @return 更新完了画面
	 */
	public String updateBrandInfo() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 原品牌代码
		map.put("oldBrandCode", form.getOldBrandCode());
		// 更新日时
		map.put("modifyTime", form.getModifyTime());
		// 更新次数
		map.put("modifyCount", form.getModifyCount());
		// 品牌名称中文
		if(form.getBrandNameChinese() != null && !"".equals(form.getBrandNameChinese())) {
			map.put("brandNameChinese", form.getBrandNameChinese());
		}
		// 品牌名称中文简称
		if(form.getBrandNameShort() != null && !"".equals(form.getBrandNameShort())) {
			map.put("brandNameShort", form.getBrandNameShort());
		}
		// 品牌名称外文
		if(form.getBrandNameForeign() != null && !"".equals(form.getBrandNameForeign())) {
			map.put("brandNameForeign", form.getBrandNameForeign());
		}
		// 品牌名称外文简称
		if(form.getBrandNameForeignShort() != null && !"".equals(form.getBrandNameForeignShort())) {
			map.put("brandNameForeignShort", form.getBrandNameForeignShort());
		}
		// 成立日期
		if(form.getFoundationDate() != null && !"".equals(form.getFoundationDate())) {
			map.put("foundationDate", form.getFoundationDate());
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSDEP12");
		try {
			// 更新品牌处理
			binOLBSDEP12_BL.tran_updateBrandInfo(map);
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
	 * 更新品牌处理前参数验证处理
	 * 
	 */
	public void validateUpdateBrandInfo() throws Exception {
		
		// 品牌名称中文必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getBrandNameChinese())) {
			this.addFieldError("brandNameChinese", getText("ECM00009",new String[]{getText("PBS00034")}));
		} else {
			// 品牌名称中文不能超过50位验证
			if(form.getBrandNameChinese().length() > 50) {
				this.addFieldError("brandNameChinese", getText("ECM00020",new String[]{getText("PBS00034"),"50"}));
			}
		}
		if(form.getBrandNameShort() != null && !"".equals(form.getBrandNameShort())) {
			// 品牌名称中文简称不能超过20位验证
			if(form.getBrandNameShort().length() > 20) {
				this.addFieldError("brandNameShort", getText("ECM00020",new String[]{getText("PBS00035"),"20"}));
			}
		}
		if(form.getBrandNameForeign() != null && !"".equals(form.getBrandNameForeign())) {
			// 品牌名称外文不能超过50位验证
			if(form.getBrandNameForeign().length() > 50) {
				this.addFieldError("brandNameForeign", getText("ECM00020",new String[]{getText("PBS00036"),"50"}));
			}
		}
		if(form.getBrandNameForeignShort() != null && !"".equals(form.getBrandNameForeignShort())) {
			// 品牌名称外文简称不能超过20位验证
			if(form.getBrandNameForeignShort().length() > 20) {
				this.addFieldError("brandNameForeignShort", getText("ECM00020",new String[]{getText("PBS00037"),"20"}));
			}
		}
		if(form.getFoundationDate() != null && !"".equals(form.getFoundationDate())) {
			// 成立日期日期格式验证
			if(!CherryChecker.checkDate(form.getFoundationDate())) {
				this.addFieldError("foundationDate", getText("ECM00022",new String[]{getText("PBS00008")}));
			}
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 品牌名称中文
			map.put("brandNameChinese", form.getBrandNameChinese());
			// 判断品牌名称是否已经存在
			String brandId2 = binOLBSDEP11_BL.checkBrandName(map);
			if(brandId2 != null && !form.getBrandInfoId().equals(brandId2)) {
				this.addFieldError("brandNameChinese", getText("EBS00015"));
			}
		}
		
	}
	
	/** 品牌信息 */
	private Map brandInfo;

	public Map getBrandInfo() {
		return brandInfo;
	}

	public void setBrandInfo(Map brandInfo) {
		this.brandInfo = brandInfo;
	}

	@Override
	public BINOLBSDEP12_Form getModel() {
		return form;
	}

}
