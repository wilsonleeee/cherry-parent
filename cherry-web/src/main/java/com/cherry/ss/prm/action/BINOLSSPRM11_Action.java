/*		
 * @(#)BINOLSSPRM11_Action.java     1.0 2010/11/29		
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
package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.ss.prm.bl.BINOLSSPRM10_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM11_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM12_BL;
import com.cherry.ss.prm.form.BINOLSSPRM11_Form;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLSSPRM11_Action extends BaseAction implements
ModelDriven<BINOLSSPRM11_Form>{

	/**
	 * 促销品类别编辑
	 */
	private static final long serialVersionUID = 1L;
	
	/** 参数FORM */
	private BINOLSSPRM11_Form form = new BINOLSSPRM11_Form();
	/**促销品类别编辑 */
	@Resource
	private BINOLSSPRM11_BL binolssprm11_BL;
	/**促销品类别详细 */
	@Resource
	private BINOLSSPRM12_BL binolssprm12_BL;
	/**促销品类别添加 */
	@Resource
	private BINOLSSPRM10_BL binolssprm10_BL;

	/** 促销品类别*/
	private Map prmCateInfo;
	
	/** 上级促销品类别信息List */
	private List higherCategoryList;
	
	@Override
	public BINOLSSPRM11_Form getModel() {
		return form;
	}

	/** 促销品类别*/
	public void setPrmCateInfo(Map prmCateInfo) {
		this.prmCateInfo = prmCateInfo;
	}

	public Map getPrmCateInfo() {
		return prmCateInfo;
	}

	/** 上级促销品类别信息List */
	public void setHigherCategoryList(List higherCategoryList) {
		this.higherCategoryList = higherCategoryList;
	}

	public List getHigherCategoryList() {
		return higherCategoryList;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE,
						CherryConstants.OPERATION_TYPE1);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 促销产品类别ID
		map.put("prmCategoryId", form.getPrmCategoryId());
		// 促销品类别信息
		prmCateInfo = binolssprm12_BL.searchPrmCategoryInfo(map);
		map.put("categoryPath", prmCateInfo.get("path"));
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, prmCateInfo.get(CherryConstants.BRANDINFOID));
		// 上级促销品类别List
		higherCategoryList = binolssprm10_BL.getHigherCategoryList(map);
		
			
		return SUCCESS;
	}

	public String save() throws Exception{

		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());

		try {
		  binolssprm11_BL.tran_updatePrmCate(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		
	}
	
	/**
	 * 
	 * 保存前字段验证处理
	 * 
	 */
	public void validateSave() throws Exception {
		// 类别码不为空验证
		if(CherryChecker.isNullOrEmpty(form.getItemClassCode())) {
			this.addFieldError("itemClassCode", getText("ECM00009",new String[]{"类别码"}));
		} else {
			// 类别码不能超过20位验证
			if(form.getItemClassCode().length() > 20) {
				this.addFieldError("itemClassCode", getText("ECM00020",new String[]{"类别码","20"}));
			}
		}
		// 类别中文名不能超过50位验证
		if(form.getItemClassNameCN() != null && form.getItemClassNameCN().length() > 50) {
			this.addFieldError("itemClassNameCN", getText("ECM00020",new String[]{"类别中文名","50"}));
		}
		// 类别英文名不能超过50位验证
		if(form.getItemClassNameEN() != null && form.getItemClassNameEN().length() > 50) {
			this.addFieldError("itemClassNameEN", getText("ECM00020",new String[]{"类别英文名","50"}));
		}
		// 类别特征码不能超过4位验证
		if(form.getCurClassCode() != null && form.getCurClassCode().length() > 4) {
			this.addFieldError("curClassCode", getText("ECM00020",new String[]{"类别特征码","4"}));
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			//所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put("itemClassCode", form.getItemClassCode());
			String categoryId = binolssprm11_BL.getPrmCategoryIdCheck(map);
			if(categoryId != null && !"".equals(categoryId) && !form.getPrmCategoryId().equals(categoryId)) {
				this.addActionError(getText("ESS00022"));
			}
		}
	}
}
