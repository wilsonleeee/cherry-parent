/*	
 * @(#)BINOLSSPRM01_Action.java     1.0 2010/11/23		
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM01_BL;
import com.cherry.ss.prm.form.BINOLSSPRM01_Form;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销品查询Action
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM01_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM01_Form> {

	private static final long serialVersionUID = 7052727878284081396L;

	/** 参数FORM */
	private BINOLSSPRM01_Form form = new BINOLSSPRM01_Form();

	@Resource
	private BINOLSSPRM01_BL binolssprm01_BL;

	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 促销产品List */
	private List promotionProList;

	/** 所管辖的品牌List */
	private List brandInfoList;
	
	/** 共通 */
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    /** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;

	@Override
	public BINOLSSPRM01_Form getModel() {
		return form;
	}

	public List getPromotionProList() {
		return promotionProList;
	}

	public void setPromotionProList(List promotionProList) {
		this.promotionProList = promotionProList;
	}

	public List getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
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
	public String init() throws Exception {
		// 参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo
				.getBIN_BrandInfoID());
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo
				.getBIN_BrandInfoID()) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		}else{
			brandInfoList = new ArrayList<Map<String, Object>>();
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put(CherryConstants.BRANDINFOID, userInfo
				.getBIN_BrandInfoID());
			String brandName = binOLCM05_BL.getBrandName(map);
			temp.put(CherryConstants.BRAND_NAME, brandName);
			brandInfoList.add(temp);
		}
		return SUCCESS;
	}

	/**
	 * <p>
	 * AJAX促销品查询
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 促销活动套装折扣产品条码
		searchMap.put("promTzzkUnitCode",
				PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
		// 取得促销产品总数
		int count = binolssprm01_BL.getPrmCount(searchMap);
		if (count > 0) {
			// 取得促销产品信息List
			promotionProList = binolssprm01_BL
					.searchPromotionProList(searchMap);
		}
		// form表单设置
		// 显示记录
		form.setITotalDisplayRecords(count);
		// 总记录
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLSSPRM01_1";
	}
	
	/**
	 * 产品实时下发
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void issuePrm() throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> searchMap = getSearchMap();
		
		// 实时下发
		try{
			resultMap = binolssprm01_BL.tran_issuedPrm(searchMap); 
			ConvertUtil.setResponseByAjax(response, resultMap);
		} catch(Exception e){
			resultMap.put("result", "1");
			ConvertUtil.setResponseByAjax(response, resultMap);
		}
	}

	/**
	 * 停用促销产品
	 * 
	 * @return
	 * @throws Exception
	 */
	public String operate() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 登陆用户ID
		int userId = userInfo.getBIN_UserID();

		// 促销产品数组（有效区分+促销产品ID+更新日期+更新次数）
		String[] prmInfos = form.getPromotionProInfo();
		// 促销品有效区分
		String validFlag = null;
		if ("1".equals(form.getOptFlag())) {
			// 促销品有效
			validFlag = "1";
		} else {
			// 促销品停用
			validFlag = "0";
		}
		try {
			// 更新数据库
			binolssprm01_BL.tran_operatePrm(prmInfos, validFlag, userId);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return ERROR;
		}
		return null;
	}

	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 */
	private Map<String, Object> getSearchMap() {

		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);

		// 设置日期
		if (!CherryChecker.isEmptyString(form.getSellStartDate(), true)
				&& CherryChecker.isEmptyString(form.getSellEndDate(), true)) {
			form.setSellEndDate(CherryConstants.longLongAfter);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		String brandInfoId = form.getBrandInfoId();
		// 非总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE != userInfo
				.getBIN_BrandInfoID()) {
			brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			// 所属部门ID
			map.put(CherryConstants.ORGANIZATIONID, userInfo
					.getBIN_OrganizationID());
		}
		// 操作者
		map.put("EmployeeId", userInfo.getBIN_EmployeeID()+ "");
		// 登陆用户所属品牌ID
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 登陆用户所属品牌Codes
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 促销品全称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 厂商编码
		map.put(CherryConstants.UNITCODE, form.getUnitCode());
		// 促销品条码
		map.put(CherryConstants.BARCODE, form.getBarCode());
		// 促销品开始销售日期
		map.put(CherryConstants.SELLSTARTDATE, form.getSellStartDate());
		// 促销品结束销售日期
		map.put(CherryConstants.SELLENDDATE, form.getSellEndDate());
		// 有效区分
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());
		// 促销品类型
		map.put("promCate", form.getPromCate());
		//是否下发到POS
		map.put("isPosIss", form.getIsPosIss());
		//促销品（促销品类型=促销礼品   启用该属性）
		map.put("mode", form.getMode());
		return map;
	}
	 /**
     * 导出Excel
     * @throws JSONException 
     */
    public String export() throws JSONException{
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 促销活动套装折扣产品条码
		searchMap.put("promTzzkUnitCode",
				PromotionConstants.PROMOTION_TZZK_UNIT_CODE);
        // 取得考勤信息List
        try {
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM01", language, "downloadFileName");
            setExcelStream(new ByteArrayInputStream(binolssprm01_BL.exportExcel(searchMap)));
        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }

        return "BINOLSSPRM01_excel";
    }
    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getDownloadFileName() throws UnsupportedEncodingException {
    	//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }
}
