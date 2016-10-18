/*
 * @(#)BINOLBSPOS08_Action.java     1.0 2010/10/27
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.bl.BINOLBSPOS07_BL;
import com.cherry.bs.pos.bl.BINOLBSPOS08_BL;
import com.cherry.bs.pos.form.BINOLBSPOS08_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 更新岗位类别画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS08_Action extends BaseAction implements ModelDriven<BINOLBSPOS08_Form> {
	
	private static final long serialVersionUID = -2545908039583059502L;
	
	/** 更新岗位类别画面BL */
	@Resource
	private BINOLBSPOS08_BL binOLBSPOS08_BL;
	
	/** 岗位类别详细画面BL */
	@Resource
	private BINOLBSPOS07_BL binOLBSPOS07_BL;
	
	/** 更新岗位类别画面Form */
	private BINOLBSPOS08_Form form = new BINOLBSPOS08_Form();
	
	/**
	 * 
	 * 更新岗位类别画面初期处理
	 * 
	 * @return 更新岗位类别画面 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 岗位类别ID
		map.put("positionCategoryId", form.getPositionCategoryId());
		// 查询岗位类别信息
		posCategoryInfo = binOLBSPOS07_BL.getPosCategoryInfo(map);
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 更新岗位类别
	 * 
	 * @return 更新完了画面
	 */
	public String updatePosCategory() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSPOS08");
		try {
			// 更新岗位类别
			binOLBSPOS08_BL.tran_updatePosCategory(map);
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
	 * 更新岗位类型前字段验证处理
	 * 
	 */
	public void validateUpdatePosCategory() throws Exception {
		
//		// 类别代码必须入力验证
//		if(CherryChecker.isNullOrEmpty(form.getCategoryCode())) {
//			this.addFieldError("categoryCode", getText("ECM00009",new String[]{"类别代码"}));
//		} else {
//			// 类别代码不能超过2位验证
//			if(form.getCategoryCode().length() > 2) {
//				this.addFieldError("categoryCode", getText("ECM00020",new String[]{"类别代码","2"}));
//			}
//		}
		// 类别名称必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getCategoryName())) {
			this.addFieldError("categoryName", getText("ECM00009",new String[]{getText("PBS00018")}));
		} else {
			// 类别名称不能超过20位验证
			if(form.getCategoryName().length() > 20) {
				this.addFieldError("categoryName", getText("ECM00020",new String[]{getText("PBS00018"),"20"}));
			}
		}
		// 岗位级别必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getPosGrade())) {
			this.addFieldError("posGrade", getText("ECM00009",new String[]{"岗位级别"}));
		} else {
			// 岗位级别必须是数字验证
			if(!CherryChecker.isNumeric(form.getPosGrade())) {
				this.addFieldError("posGrade", getText("ECM00021",new String[]{"岗位级别"}));
			}
		}
		// 类别外文名称不能超过20位验证
		if(form.getCategoryNameForeign() != null && form.getCategoryNameForeign().length() > 20) {
			this.addFieldError("categoryNameForeign", getText("ECM00020",new String[]{getText("PBS00019"),"20"}));
		}
		// 验证同一组织中是否存在同样的岗位名称
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌存在的场合
			if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			} else {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
			// 类别代码
//			map.put("categoryCode", form.getCategoryCode());
			// 类别名称
			map.put("categoryName", form.getCategoryName());
			
			String posCategoryId = binOLBSPOS08_BL.getPosCategoryNameCheck(map);
			if(posCategoryId != null && !"".equals(posCategoryId) && !posCategoryId.equals(form.getPositionCategoryId())) {
				this.addActionError(getText("EBS00075"));
			}
		}
	}
	
	/** 岗位类别信息 */
	private Map posCategoryInfo;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	public Map getPosCategoryInfo() {
		return posCategoryInfo;
	}

	public void setPosCategoryInfo(Map posCategoryInfo) {
		this.posCategoryInfo = posCategoryInfo;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@Override
	public BINOLBSPOS08_Form getModel() {
		return form;
	}

}
