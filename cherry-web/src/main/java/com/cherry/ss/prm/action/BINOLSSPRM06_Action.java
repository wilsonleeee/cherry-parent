/*
 * @(#)BINOLSSPRM06_Action.java     1.0 2010/12/01
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM11_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM06_BL;
import com.cherry.ss.prm.form.BINOLSSPRM06_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 促销品分类添加Action
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM06_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM06_Form> {

    private static final long serialVersionUID = 8805008732002846455L;

	/** 参数FORM */
	private BINOLSSPRM06_Form form = new BINOLSSPRM06_Form();

	@Resource
	private BINOLSSPRM06_BL binOLSSPRM06_BL;

	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLCM11_BL binOLCM11_BL;

	/** 所属品牌List */
	private List brandInfoList;

	/** 促销品大分类名称*/
	private Map primaryMap = new HashMap();
	
	/** 促销品中分类名称*/
	private Map secondryMap = new HashMap();
	
	/** 促销品小分类名称*/
	private Map smallMap = new HashMap();
	
	/** 大分类List */
    private List primaryCateList;
    
    /** 中分类List */
    private List secondCateList = new ArrayList();
    
    /** 小分类List */
    private List smallCateList = new ArrayList();
	
	public BINOLSSPRM06_Form getModel() {
		return form;
	}

	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getBrandInfoList() {
		return brandInfoList;
	}
	
	public void setPrimaryMap(Map primaryMap) {
		this.primaryMap = primaryMap;
	}

	public Map getPrimaryMap() {
		return primaryMap;
	}

	public void setSecondryMap(Map secondryMap) {
		this.secondryMap = secondryMap;
	}

	public Map getSecondryMap() {
		return secondryMap;
	}

	public void setSmallMap(Map smallMap) {
		this.smallMap = smallMap;
	}

	public Map getSmallMap() {
		return smallMap;
	}

    public void setPrimaryCateList(List primaryCateList) {
        this.primaryCateList = primaryCateList;
    }

    public List getPrimaryCateList() {
        return primaryCateList;
    }

    public void setSecondCateList(List secondCateList) {
        this.secondCateList = secondCateList;
    }

    public List getSecondCateList() {
        return secondCateList;
    }

    public void setSmallCateList(List smallCateList) {
        this.smallCateList = smallCateList;
    }

    public List getSmallCateList() {
        return smallCateList;
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
	public String init() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo
				.getBIN_BrandInfoID()) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map brandInfo = new HashMap();
			// 品牌ID
			brandInfo.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
		}
		if (null != brandInfoList && !brandInfoList.isEmpty()) {
		    // 品牌信息
            Map brandInfo = (Map) brandInfoList.get(0);
            // 取得第一个品牌的ID,作为默认显示
            map.put(CherryConstants.BRANDINFOID, brandInfo.get(CherryConstants.BRANDINFOID));
            // 取得大分类List
            primaryCateList = binOLCM05_BL.getPrimaryCateList(map);
		}
		return SUCCESS;
	}

	/**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * 
	 */
	public String save() throws Exception {

		// 验证提交的参数
		if (!validateSave()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLSSPRM06");
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLSSPRM06");
		
		if(PromotionConstants.CategoryType_secondry.equals(form.getCategoryType()) || PromotionConstants.CategoryType_small.equals(form.getCategoryType())){
	        //查询分类的中文名称、英文名称
	        Map primaryCategory = new HashMap();
	        primaryCategory.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	        primaryCategory.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
	        primaryCategory.put("primaryCategoryCode", form.getPrimaryCategoryCode());
	        Map primaryCategoryName = binOLCM11_BL.getPrimaryCategoryName(primaryCategory);
	        String primaryCategoryNameCN = ConvertUtil.getString(primaryCategoryName.get("primaryCategoryNameCN"));
	        String primaryCategoryNameEN = ConvertUtil.getString(primaryCategoryName.get("primaryCategoryNameEN"));
	        //大分类中文名称
	        map.put("primaryCategoryNameCN", primaryCategoryNameCN);
	        //大分类英文名称
	        map.put("primaryCategoryNameEN", primaryCategoryNameEN);
		}
		if(PromotionConstants.CategoryType_small.equals(form.getCategoryType())){
	        Map secondryCategory = new HashMap();
	        secondryCategory.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	        secondryCategory.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
	        secondryCategory.put("secondryCategoryCode", form.getSecondryCategoryCode());
	        Map secondryCategoryName = binOLCM11_BL.getSecondryCategoryName(secondryCategory);
	        String secondryCategoryNameCN = ConvertUtil.getString(secondryCategoryName.get("secondryCategoryNameCN"));
	        String secondryCategoryNameEN = ConvertUtil.getString(secondryCategoryName.get("secondryCategoryNameEN"));
	        //中分类中文名称
	        map.put("secondryCategoryNameCN", secondryCategoryNameCN);
	        //中分类英文名称
	        map.put("secondryCategoryNameEN", secondryCategoryNameEN);
		}
		
		// 促销品添加插表处理
		binOLSSPRM06_BL.tran_addPrmType(map);
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * @throws Exception
	 * 
	 */
	public boolean validateSave() throws Exception {
        // 验证结果
        boolean isCorrect = true;
        // 大分类代码不为空验证
        if (CherryChecker.isNullOrEmpty(form.getPrimaryCategoryCode())) {
            this.addFieldError("primaryCategoryCode", getText("ECM00009",
                    new String[] { getText("PSS00030") }));
            isCorrect = false;
        } else {
            // 大分类代码不能超过4位验证
            if (form.getPrimaryCategoryCode().length() > 4) {
                this.addFieldError("primaryCategoryCode", getText("ECM00020",
                        new String[] { getText("PSS00030"), "4" }));
                isCorrect = false;
            }
            if (!CherryChecker.isAlphanumeric(form.getPrimaryCategoryCode())) {
                // 大分类代码英数验证
                this.addFieldError("primaryCategoryCode", getText("ECM00031", new String[]{getText("PSS00030")}));
                isCorrect = false;
            }
        }
        // 大分类中文名不能超过20位验证
        if (form.getPrimaryCategoryNameCN() != null
                && form.getPrimaryCategoryNameCN().length() > 20) {
            this.addFieldError("primaryCategoryNameCN", getText("ECM00020",
                    new String[] { getText("PSS00031"), "20" }));
            isCorrect = false;
        }
        // 大分类英文名不能超过20位验证
        if (form.getPrimaryCategoryNameEN() != null
                && form.getPrimaryCategoryNameEN().length() > 20) {
            this.addFieldError("primaryCategoryNameEN", getText("ECM00020",
                    new String[] { getText("PSS00032"), "20" }));
            isCorrect = false;
        }
        
        if(PromotionConstants.CategoryType_secondry.equals(form.getCategoryType()) || PromotionConstants.CategoryType_small.equals(form.getCategoryType())){
            // 中分类代码不为空验证
            if (CherryChecker.isNullOrEmpty(form.getSecondryCategoryCode())) {
                this.addFieldError("secondryCategoryCode", getText("ECM00009",
                        new String[] { getText("PSS00033") }));
                isCorrect = false;
            } else {
                // 中分类代码不能超过4位验证
                if (form.getSecondryCategoryCode() != null
                        && form.getSecondryCategoryCode().length() > 4) {
                    this.addFieldError("secondryCategoryCode", getText("ECM00020",
                            new String[] { getText("PSS00033"), "4" }));
                    isCorrect = false;
                }
                if (!CherryChecker.isAlphanumeric(form.getSecondryCategoryCode())) {
                    // 大分类代码英数验证
                    this.addFieldError("secondryCategoryCode", getText("ECM00031", new String[]{getText("PSS00033")}));
                    isCorrect = false;
                }
            }

            // 中分类中文名不能超过20位验证
            if (form.getSecondryCategoryNameCN() != null
                    && form.getSecondryCategoryNameCN().length() > 20) {
                this.addFieldError("secondryCategoryNameCN", getText("ECM00020",
                        new String[] { getText("PSS00034"), "20" }));
                isCorrect = false;
            }
            // 中分类英文名不能超过20位验证
            if (form.getSecondryCategoryNameEN() != null
                    && form.getSecondryCategoryNameEN().length() > 20) {
                this.addFieldError("secondryCategoryNameEN", getText("ECM00020",
                        new String[] { getText("PSS00035"), "20" }));
                isCorrect = false;
            }
        }
        
        if(PromotionConstants.CategoryType_small.equals(form.getCategoryType())){
            // 小分类代码不为空验证
            if (CherryChecker.isNullOrEmpty(form.getSmallCategoryCode())) {
                this.addFieldError("smallCategoryCode", getText("ECM00009",
                        new String[] { getText("PSS00036") }));
                isCorrect = false;
            } else {
                // 小分类代码不能超过4位验证
                if (form.getSmallCategoryCode() != null
                        && form.getSmallCategoryCode().length() > 4) {
                    this.addFieldError("smallCategoryCode", getText("ECM00020",
                            new String[] { getText("PSS00036"), "4" }));
                    isCorrect = false;
                }
                if (!CherryChecker.isAlphanumeric(form.getSmallCategoryCode())) {
                    // 小分类代码英数验证
                    this.addFieldError("smallCategoryCode", getText("ECM00031", new String[]{getText("PSS00036")}));
                    isCorrect = false;
                }
            }
            // 小分类中文名不能超过20位验证
            if (form.getSmallCategoryNameCN() != null
                    && form.getSmallCategoryNameCN().length() > 20) {
                this.addFieldError("smallCategoryNameCN", getText("ECM00020",
                        new String[] { getText("PSS00037"), "20" }));
                isCorrect = false;
            }
            // 小分类英文名不能超过20位验证
            if (form.getSmallCategoryNameEN() != null
                    && form.getSmallCategoryNameEN().length() > 20) {
                this.addFieldError("smallCategoryNameEN", getText("ECM00020",
                        new String[] { getText("PSS00038"), "20" }));
                isCorrect = false;
            }
        }

		if (isCorrect) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			// 大分类代码
			map.put("primaryCategoryCode", form.getPrimaryCategoryCode());
			// 中分类代码
			map.put("secondryCategoryCode", form.getSecondryCategoryCode());
			// 小分类代码
			map.put("smallCategoryCode", form.getSmallCategoryCode());
			// 验证是否存在同样的促销品分类ID
			if (!binOLCM05_BL.getPrmTypeIdCheck(map)) {
				String cateType = form.getCategoryType();
				if(cateType.equals(PromotionConstants.CategoryType_primary)){
				    this.addFieldError("primaryCategoryCode", getText("ESS00021"));
				}else if(cateType.equals(PromotionConstants.CategoryType_secondry)){
				    this.addFieldError("secondryCategoryCode", getText("ESS00021"));
				}else if(cateType.equals(PromotionConstants.CategoryType_small)){
				    this.addFieldError("smallCategoryCode", getText("ESS00021"));
				}
				isCorrect = false;
			}
		}
		
		return isCorrect;
	}
}
